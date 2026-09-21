package ifpb.telas.administrador;


import ifpb.telas.Sessao;
import ifpb.telas.componente_personalizado.*;
import ifpb.telas.configuracao.FontesPadroes;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


@Getter
public class TelaLogin extends TelaPadrao {
    private CaixaDeTextoPadrao caixaTextoUsuario;
    private CaixaDeTextoSenhas caixaTextoSenha;

    public TelaLogin() {
        super("Login", PaletaDeCores.branco, 380, 385);
    }

    public void desenhar() {
        Painel painelLogin = new Painel(PaletaDeCores.branco, getXTamanho(), getYTamanho());

        LabelTitulo labelTitulo = new LabelTitulo(PaletaDeCores.preto, "Login", 35, 36, 310, ValoresPadroes.alturaLabelTitulo);
        painelLogin.add(labelTitulo);

        LabelPadrao labelUsuario = new LabelPadrao("Usuário", 35, 121, 310, ValoresPadroes.alturaLabel);
        painelLogin.add(labelUsuario);

        caixaTextoUsuario = new CaixaDeTextoPadrao(35, 147, 310);
        painelLogin.add(caixaTextoUsuario);

        JLabel labelSenha = new LabelPadrao("Senha", 35, 202, 310, ValoresPadroes.alturaLabel);
        painelLogin.add(labelSenha);

        caixaTextoSenha = new CaixaDeTextoSenhas(35, 228, 310);
        painelLogin.add(caixaTextoSenha);

        JButton botaoEsqueceuSenha = new JButton("Esqueci a minha senha");
        botaoEsqueceuSenha.setFont(FontesPadroes.botaoEsqueciMinhaSenha);
        botaoEsqueceuSenha.setBounds(35,269,310,18);
        botaoEsqueceuSenha.setForeground(PaletaDeCores.azulClaro);
        botaoEsqueceuSenha.setFocusPainted(false);
        botaoEsqueceuSenha.setBorderPainted(false);
        botaoEsqueceuSenha.setOpaque(false);
        botaoEsqueceuSenha.setContentAreaFilled(false);
        botaoEsqueceuSenha.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoEsqueceuSenha.setHorizontalAlignment(JButton.RIGHT);
        botaoEsqueceuSenha.setMargin(new Insets(0, 0, 0, 0));
        botaoEsqueceuSenha.addActionListener(new OuvinteBotaoEsqueceuSenha(this));
        painelLogin.add(botaoEsqueceuSenha);

        BotaoPadrao botaoEntrar = new BotaoPadrao("Entrar", PaletaDeCores.azulForte, PaletaDeCores.branco, 35, 319, 186);
        botaoEntrar.addActionListener(new OuvinteBotaoEntrarADM(this));
        painelLogin.add(botaoEntrar);

        BotaoPadrao botaoCancelar = new BotaoPadrao("Cancelar", PaletaDeCores.vermelho, PaletaDeCores.branco, 232, 319, 111);
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Sessao.encerrarSessao();
                System.exit(0);
            }
        });
        painelLogin.add(botaoCancelar);

        add(painelLogin, new GridBagConstraints());
    }
}
