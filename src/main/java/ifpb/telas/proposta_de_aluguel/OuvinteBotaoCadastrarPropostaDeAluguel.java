package ifpb.telas.proposta_de_aluguel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import javax.swing.JOptionPane;

import ifpb.dao.DaoPessoa;
import ifpb.dao.DaoPropostaDeAluguel;
import ifpb.dao.DaoRegraDePreco;
import ifpb.email.Mensageiro;
import ifpb.enumeradores.TipoDePessoa;
import ifpb.enumeradores.Turno;
import ifpb.excecoes.NovoIntervaloGerandoConflitoException;
import ifpb.excecoes.PessoaNaoExistenteException;
import ifpb.modelo.intervalos.IntervaloDatas;
import ifpb.modelo.intervalos.IntervaloHorarios;
import ifpb.modelo.pessoa.Locatario;
import ifpb.modelo.pessoa.Pessoa;
import ifpb.modelo.proposta_de_aluguel.Peca;
import ifpb.modelo.proposta_de_aluguel.PeriodoExibicaoPeca;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import ifpb.relatorios_ou_contratos.pdf.GeradorDeContratos;
import ifpb.telas.Sessao;
import ifpb.telas.configuracao.ValidadorDeDados;
import ifpb.telas.configuracao.ValoresPadroes;
import ifpb.telas.pessoa.TelaCadastroPessoas;
import org.apache.commons.mail2.core.EmailException;

public class OuvinteBotaoCadastrarPropostaDeAluguel implements ActionListener {
    private final TelaCadastroPropostaDeAluguel tela;

    public OuvinteBotaoCadastrarPropostaDeAluguel(TelaCadastroPropostaDeAluguel tela) {
        this.tela = tela;
    }

    public void actionPerformed(ActionEvent botaoCadastrarPropostaDeAluguelPressionado) {
        if (Sessao.getDaoRegraDePreco().contarRegrasPorValor() == 0) {
            JOptionPane.showMessageDialog(null, "É preciso ter pelo menos uma regra por valor!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cpf = tela.getCaixaTextoCPF().getText();
        String nomePeca = tela.getCaixaTextoNome().getText();
        String dataInicio = tela.getCaixaTextoDataInicio().getText();
        String dataFim = tela.getCaixaTextoDataFim().getText();
        int indiceTurno = tela.getEscolhaTurno().getSelectedIndex();
        String horarioInicio = tela.getCaixaTextoHorarioInicio().getText();
        String horarioFim = tela.getCaixaTextoHorarioFim().getText();
        String valorTicket = tela.getCaixaTextoValorTicket().getText();

        if (cpf.contains(" ") || nomePeca.isBlank() || dataInicio.contains(" ") || dataFim.contains(" ") || indiceTurno == 0 || horarioInicio.contains(" ") || horarioFim.contains(" ") || valorTicket.isBlank()) {
            JOptionPane.showMessageDialog(null, "Todos os campos precisam ser preenchidos!", "Atenção", JOptionPane.WARNING_MESSAGE);

            return;
        }

        DaoPessoa daoPessoa = Sessao.getDaoPessoa();
        DaoRegraDePreco daoRegraDePreco = Sessao.getDaoRegraDePreco();
        DaoPropostaDeAluguel daoPropostaDeAluguel = Sessao.getDaoPropostaDeAluguel();
        Locatario locatario;
        IntervaloDatas intervaloDatas;
        Turno turno = Turno.values()[indiceTurno];
        IntervaloHorarios intervaloPeca;
        BigDecimal valorTicketFinal;
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
        LocalDate dataInicioConvertida;
        LocalDate dataFimConvertida;

        try {
            dataInicioConvertida = LocalDate.parse(dataInicio, formatadorData);
            dataFimConvertida = LocalDate.parse(dataFim, formatadorData);
        } catch (DateTimeParseException exception) {
            JOptionPane.showMessageDialog(null, "Data inválida! Insira uma data válida no formato dd/MM/aaaa.", "Atenção", JOptionPane.WARNING_MESSAGE);

            return;
        }

        try {
            Pessoa pessoa = daoPessoa.buscarPorCPF(cpf);

            if (!(pessoa instanceof Locatario)) {
                JOptionPane.showMessageDialog(null, "A pessoa cadastrada com esse CPF não é um locatário!", "Atenção", JOptionPane.WARNING_MESSAGE);

                return;
            }

            locatario = (Locatario) pessoa;
            intervaloDatas = new IntervaloDatas(dataInicioConvertida, dataFimConvertida);
            intervaloPeca = new IntervaloHorarios(LocalTime.parse(horarioInicio, DateTimeFormatter.ofPattern("HH:mm")), LocalTime.parse(horarioFim, DateTimeFormatter.ofPattern("HH:mm")));
            IntervaloHorarios intervaloOcupacaoTeatro = new IntervaloHorarios(LocalTime.parse(horarioInicio, DateTimeFormatter.ofPattern("HH:mm")).minusHours(ValoresPadroes.duracaoAntesEDepoisDaPeca), LocalTime.parse(horarioFim, DateTimeFormatter.ofPattern("HH:mm")).plusHours(ValoresPadroes.duracaoAntesEDepoisDaPeca));
            IntervaloHorarios intervaloDeHorarioTurno = Turno.values()[indiceTurno].getIntervaloDeHorario();

            if (!intervaloOcupacaoTeatro.isDentro(intervaloDeHorarioTurno)) {
                JOptionPane.showMessageDialog(null, "O intervalo de horário inserido é inválido para o turno.\n" + "O intervalo para esse turno precisa estar dentro de: " + intervaloDeHorarioTurno + ".\nLembre-se que deve ser contada 1 hora antes e depois " + "que será utilizado para acomodação.", "Atenção", JOptionPane.WARNING_MESSAGE);

                return;
            }

            ValidadorDeDados.isValorTicketValido(valorTicket);
            valorTicketFinal = new BigDecimal(valorTicket);

            Peca peca = new Peca(nomePeca);

            PeriodoExibicaoPeca periodoExibicaoPeca = new PeriodoExibicaoPeca(intervaloDatas, turno, intervaloPeca, intervaloOcupacaoTeatro, daoRegraDePreco.buscarTodos());

            daoPropostaDeAluguel.verificarConflitoComPeriodosExistentes(periodoExibicaoPeca);

            PropostaDeAluguel propostaDeAluguel = new PropostaDeAluguel(peca, periodoExibicaoPeca, locatario, valorTicketFinal);
            daoPropostaDeAluguel.salvar(propostaDeAluguel);

            GeradorDeContratos.gerarContrato(propostaDeAluguel);
            Mensageiro.enviarMensagem(locatario.getEmail(), "Contrato de aluguel", "Olá,\nSegue o seu contrato de aluguel.\n\nAtenciosamente,\nGerenciador de Teatro.", GeradorDeContratos.getNomeArquivoContrato());

            JOptionPane.showMessageDialog(null, "Proposta cadastrada com sucesso e contrato enviado para o email do locatário.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            tela.dispose();

            new TelaPropostasDeAluguel();
        } catch (PessoaNaoExistenteException exception) {
            JOptionPane.showMessageDialog(null, "Locatário não existente!\n" + "Você será redirecionado para a página " + "de cadastro de Locatário.", "Atenção", JOptionPane.WARNING_MESSAGE);

            new TelaCadastroPessoas(cpf, TipoDePessoa.LOCATARIO);
        } catch (NovoIntervaloGerandoConflitoException exception) {
            JOptionPane.showMessageDialog(null, "Já existe uma peça cadastrada nesse período/horário!", "Atenção", JOptionPane.WARNING_MESSAGE);
        } catch (DateTimeParseException exception) {
            JOptionPane.showMessageDialog(null, "Horário inválido!", "Atenção", JOptionPane.WARNING_MESSAGE);
        } catch (DateTimeException exception) {
            JOptionPane.showMessageDialog(null, "Data inválida!", "Atenção", JOptionPane.WARNING_MESSAGE);
        } catch (EmailException exception) {
            JOptionPane.showMessageDialog(null, "Problema ao enviar o contrato por email.", "Atenção", JOptionPane.WARNING_MESSAGE);
            exception.printStackTrace();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }
}