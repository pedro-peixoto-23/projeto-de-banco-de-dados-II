package ifpb.telas.administrador;

import ifpb.dao.DaoUsuario;
import ifpb.excecoes.EmailJaCadastradoException;
import ifpb.modelo.usuario.Usuario;
import ifpb.telas.Sessao;
import ifpb.telas.configuracao.ValidadorDeDados;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class OuvinteBotaoCadastrarADM implements ActionListener {
    private final TelaCadastroADM tela;

    public OuvinteBotaoCadastrarADM(TelaCadastroADM tela) {
        this.tela = tela;
    }

    public void actionPerformed(ActionEvent botaoCadastrarPressionado) {
        String nome = tela.getCaixaTextoNome().getText();
        String email = tela.getCaixaTextoEmail().getText();
        String novaSenha = new String(tela.getCaixaTextoNovaSenha().getPassword());
        String confirmacaoNovaSenha = new String(tela.getCaixaTextoConfirmacaoSenha().getPassword());

        if (!ValidadorDeDados.isEmailValido(email)) {
            JOptionPane.showMessageDialog(null, "O endereço de e-mail fornecido é inválido!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!novaSenha.equals(confirmacaoNovaSenha)) {
            JOptionPane.showMessageDialog(null, "Campos de nova senha e confirmação diferentes!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (nome.isBlank() || novaSenha.isBlank() || confirmacaoNovaSenha.isBlank()) {
            JOptionPane.showMessageDialog(null, "Todos os campos precisam ser preenchidos!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DaoUsuario daoUsuario = Sessao.getDaoUsuario();
            daoUsuario.salvar(new Usuario(nome, email, novaSenha));
        } catch (EmailJaCadastradoException exception) {
            JOptionPane.showMessageDialog(null, "E-mail inserido já está cadastrado no sistema!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(null, "Administrador cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        tela.dispose();
        new TelaLogin();
    }
}

