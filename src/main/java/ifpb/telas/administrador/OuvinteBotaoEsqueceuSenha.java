package ifpb.telas.administrador;

import ifpb.dao.DaoUsuario;
import ifpb.email.Mensageiro;
import ifpb.modelo.usuario.Usuario;
import ifpb.telas.Sessao;
import org.apache.commons.mail2.core.EmailException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class OuvinteBotaoEsqueceuSenha implements ActionListener {
    private final TelaLogin tela;

    public OuvinteBotaoEsqueceuSenha(TelaLogin tela) {
        this.tela = tela;
    }

    public void actionPerformed(ActionEvent botaoEsqueceuSenhaPressionado) {
        DaoUsuario daoUsuario = Sessao.getDaoUsuario();

        Usuario usuario = daoUsuario.buscarTodos().get(0);

        long codigoConfirmacaoGerado = System.currentTimeMillis();

        String email = usuario.getEmail();

        String titulo = "Código de confirmação para mudança de senha";
        String mensagem = "Olá,\n\nSeu código de confirmação para troca de senha: " + codigoConfirmacaoGerado + "\n\nAtenciosamente,\nMensagem automática de Gerenciador de peças de teatro.";

        try {
            Mensageiro.enviarMensagem(email, titulo,mensagem);
        } catch (EmailException exception) {
            JOptionPane.showMessageDialog(null, "Não conseguimos enviar o código de verificação para o seu e-mail\n Tente mais tarde!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(null, "Código de verificação enviado para " + usuario.getEmail() + ".", "Aviso", JOptionPane.INFORMATION_MESSAGE);

        tela.dispose();
        new TelaRedefinicaoDeSenha(codigoConfirmacaoGerado, usuario);
    }
}