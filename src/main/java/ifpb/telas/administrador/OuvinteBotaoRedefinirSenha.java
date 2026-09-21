package ifpb.telas.administrador;

import ifpb.dao.DaoUsuario;
import ifpb.modelo.usuario.Usuario;
import ifpb.telas.Sessao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class OuvinteBotaoRedefinirSenha implements ActionListener {
    private final TelaRedefinicaoDeSenha tela;

    public OuvinteBotaoRedefinirSenha(TelaRedefinicaoDeSenha tela) {
        this.tela = tela;
    }

    public void actionPerformed(ActionEvent botaoRedefinirSenhaPressionado) {
        long codigoConfirmacaoGerado = tela.getCodigoConfirmacaoGerado();
        long codigoConfirmacaoUsuario;

        try {
            codigoConfirmacaoUsuario = Long.parseLong(tela.getCaixaTextoCodigoConfirmacao().getText());
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(null, "Código de confirmação inválido!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuario = tela.getUsuario();

        String novaSenha = new String(tela.getCaixaTextoNovaSenha().getPassword());
        String confirmacaoSenha = new String(tela.getCaixaTextoConfirmacaoSenha().getPassword());

        if (codigoConfirmacaoGerado !=  codigoConfirmacaoUsuario) {
            JOptionPane.showMessageDialog(null, "Código de confirmação incorreto!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!novaSenha.equals(confirmacaoSenha)) {
            JOptionPane.showMessageDialog(null, "Campos de nova senha e confirmação diferentes!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (novaSenha.isBlank() || confirmacaoSenha.isBlank()) {
            JOptionPane.showMessageDialog(null, "Todos os campos precisam ser preenchidos!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DaoUsuario daoUsuario = Sessao.getDaoUsuario();
        daoUsuario.alterarSenha(usuario, novaSenha);

        JOptionPane.showMessageDialog(null, "Senha alterada com sucesso!", "Aviso", JOptionPane.INFORMATION_MESSAGE);

        tela.dispose();
        new TelaLogin();
    }
}
