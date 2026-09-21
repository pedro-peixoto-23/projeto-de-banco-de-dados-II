package ifpb.telas.pessoa;

import ifpb.dao.DaoPessoa;
import ifpb.enumeradores.Sexo;
import ifpb.enumeradores.TipoDePessoa;
import ifpb.excecoes.PessoaJaCadastradaException;
import ifpb.modelo.pessoa.Espectador;
import ifpb.modelo.pessoa.Locatario;
import ifpb.modelo.pessoa.Pessoa;
import ifpb.telas.Sessao;
import ifpb.telas.configuracao.ValidadorDeDados;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JOptionPane;


public class OuvinteBotaoCadastrarPessoa implements  ActionListener {
    private final TelaCadastroPessoas tela;

    public OuvinteBotaoCadastrarPessoa(TelaCadastroPessoas tela) {
        this.tela = tela;
    }

    public void actionPerformed(ActionEvent botaoCadastrarPessoaPressionado) {
        String cpf = tela.getCaixaTextoCPF().getText();
        String email = tela.getCaixaTextoEmail().getText();
        int indiceSexo = tela.getEscolhaSexo().getSelectedIndex();
        String nome = tela.getCaixaTextoNome().getText();
        String telefone = tela.getCaixaTextoTelefone().getText();
        String dataNascimento = tela.getCaixaTextoNascimento().getText();

        if (cpf.contains(" ") || email.isBlank() || indiceSexo == 0 || nome.isBlank() || telefone.contains(" ") || dataNascimento.contains(" ")) {
            JOptionPane.showMessageDialog(null, "Todos os campos precisam ser preenchidos!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ValidadorDeDados.isEmailValido(email)) {
            JOptionPane.showMessageDialog(null, "Endereço de e-mail inválido.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DaoPessoa daoPessoa = Sessao.getDaoPessoa();
        Pessoa pessoa;

        try {
            TipoDePessoa tipoDePessoa = (TipoDePessoa) tela.getEscolhaTipoPessoa().getSelectedItem();
            if (tipoDePessoa.equals(TipoDePessoa.LOCATARIO)) {
                pessoa = new Locatario(nome, Sexo.values()[indiceSexo], cpf, email, LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy")), telefone);
            } else {
                pessoa = new Espectador(nome, Sexo.values()[indiceSexo], cpf, email, LocalDate.parse(dataNascimento, DateTimeFormatter.ofPattern("dd/MM/yyyy")), telefone);
            }

            daoPessoa.salvar(pessoa);
        } catch (DateTimeParseException exception) {
            JOptionPane.showMessageDialog(null, "Data inválida!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (PessoaJaCadastradaException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(null, "Pessoa adicionada com sucesso!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        tela.dispose();
    }
}

