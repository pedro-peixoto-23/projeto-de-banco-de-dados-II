package ifpb.telas.venda_de_ingresso;

import ifpb.dao.DaoPessoa;
import ifpb.dao.DaoVendaDeIngresso;
import ifpb.email.Mensageiro;
import ifpb.enumeradores.TipoDePessoa;
import ifpb.excecoes.PessoaNaoExistenteException;
import ifpb.excecoes.ValorQtdTicketsInvalidoException;
import ifpb.modelo.ingresso.VendaDeIngresso;
import ifpb.modelo.pessoa.Espectador;
import ifpb.modelo.pessoa.Pessoa;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import ifpb.relatorios_ou_contratos.pdf.GeradorDeIngressos;
import ifpb.telas.Sessao;
import ifpb.telas.configuracao.ValidadorDeDados;
import ifpb.telas.pessoa.TelaCadastroPessoas;
import ifpb.telas.principal.TelaInicial;
import org.apache.commons.mail2.core.EmailException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class OuvinteBotaoCadastrarVendaIngresso implements ActionListener {
    private final TelaVendaDeIngresso tela;

    public OuvinteBotaoCadastrarVendaIngresso(TelaVendaDeIngresso tela) {
        this.tela = tela;
    }

    public void actionPerformed(ActionEvent botaoCadastrarVendaDeIngressoPressionado) {
        String cpf = tela.getCaixaTextoCPF().getText();
        PropostaDeAluguel propostaDeAluguel = (PropostaDeAluguel) tela.getEscolhaPeca().getSelectedItem();
        LocalDate dataDaPeca = (LocalDate) tela.getEscolhaDataPeca().getSelectedItem();
        String txtQtdIngressos =  tela.getCaixaTextoQtdTicket().getText();
        int qtdIngressos;
        DaoPessoa daoPessoa = Sessao.getDaoPessoa();
        DaoVendaDeIngresso daoVendaDeIngresso = Sessao.getDaoVendaDeIngresso();
        Espectador espectador;

        if (cpf.contains(" ") || propostaDeAluguel == null || dataDaPeca == null || txtQtdIngressos.isBlank()) {
            JOptionPane.showMessageDialog(null, "Todos os campos precisam ser preenchidos!", "Atenção", JOptionPane.WARNING_MESSAGE);

            return;
        }

        try {
            ValidadorDeDados.isValorQtdTicketsValido(txtQtdIngressos);
            qtdIngressos = Integer.parseInt(txtQtdIngressos);
        } catch (ValorQtdTicketsInvalidoException exception) {
            JOptionPane.showMessageDialog(null, "Valor da quantidade de tickets inválido!", "Atenção", JOptionPane.WARNING_MESSAGE);

            return;
        }

        try {
            Pessoa pessoa = daoPessoa.buscarPorCPF(cpf);

            if (!(pessoa instanceof Espectador)) {
                JOptionPane.showMessageDialog(null, "A pessoa cadastrada com esse CPF não é um espectador!", "Atenção", JOptionPane.WARNING_MESSAGE);

                return;
            }

            espectador = (Espectador) pessoa;

            VendaDeIngresso vendaDeIngresso = new VendaDeIngresso(espectador, propostaDeAluguel, dataDaPeca, qtdIngressos, propostaDeAluguel.getPrecoTicket());

            daoVendaDeIngresso.salvar(vendaDeIngresso);

            GeradorDeIngressos.gerarIngressos(vendaDeIngresso);
            Mensageiro.enviarMensagem(espectador.getEmail(), "Ingressos", "Olá,\nSeguem os seus ingressos.\n\nAtenciosamente,\nGerenciador de teatro.", GeradorDeIngressos.getNomeArquivoIngressos());
        } catch (PessoaNaoExistenteException exception) {
            JOptionPane.showMessageDialog(null, "Espectador não existente!\n" + "Você será redirecionado para a página de cadastro de Espectador.", "Atenção", JOptionPane.WARNING_MESSAGE);

            new TelaCadastroPessoas(cpf, TipoDePessoa.ESPECTADOR);

            return;
        } catch (EmailException exception) {
            JOptionPane.showMessageDialog(null, "Problema ao enviar os ingressos por email!", "Atenção", JOptionPane.WARNING_MESSAGE);
            throw new RuntimeException(exception);
        }

        JOptionPane.showMessageDialog(null, "Venda de ingresso cadastrada com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        tela.dispose();
        new TelaInicial();
    }
}
