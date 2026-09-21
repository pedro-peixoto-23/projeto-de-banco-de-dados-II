package ifpb.telas.administrador;

import ifpb.dao.DaoUsuario;
import ifpb.excecoes.EmailNaoExistenteException;
import ifpb.excecoes.SenhaIncorretaException;
import ifpb.telas.Sessao;
import ifpb.telas.configuracao.ValidadorDeDados;
import ifpb.telas.principal.TelaInicial;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OuvinteBotaoEntrarADM implements ActionListener {
    TelaLogin tela;

    public OuvinteBotaoEntrarADM(TelaLogin tela) {
        this.tela = tela;
    }

    public void actionPerformed(ActionEvent botaoEntrarPressionado) {
        String email = tela.getCaixaTextoUsuario().getText();
        String senha = new String(tela.getCaixaTextoSenha().getPassword());

        if (!ValidadorDeDados.isEmailValido(email)) {
            JOptionPane.showMessageDialog(null, "O endereço de e-mail fornecido é inválido!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (senha.isBlank()) {
            JOptionPane.showMessageDialog(null, "Todos os campos precisam ser preenchidos!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DaoUsuario daoUsuario = Sessao.getDaoUsuario();
            daoUsuario.autenticarCredenciais(email, senha);

            tela.dispose();

            new TelaInicial();
        } catch (EmailNaoExistenteException exception) {
            JOptionPane.showMessageDialog(null, "Não existe usuário cadastrado com esse endereço de e-mail!", "Atenção", JOptionPane.WARNING_MESSAGE);
        } catch (SenhaIncorretaException exception) {
            JOptionPane.showMessageDialog(null, "Senha incorreta!", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }
}
