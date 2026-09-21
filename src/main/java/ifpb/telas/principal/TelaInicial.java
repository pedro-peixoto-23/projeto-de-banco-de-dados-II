package ifpb.telas.principal;

import ifpb.telas.administrador.TelaLogin;
import ifpb.telas.componente_personalizado.*;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;
import ifpb.telas.proposta_de_aluguel.TelaPropostasDeAluguel;
import ifpb.telas.regra_de_preco.TelaRegrasDePreco;
import ifpb.telas.relatorio_financeiro.TelaRelatorioFinanceiroTeatro;
import ifpb.telas.venda_de_ingresso.TelaVendaDeIngresso;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaInicial extends TelaPadrao {
    public TelaInicial() {
        super("Tela inicial", PaletaDeCores.azulForte, 1200, 670);
    }

    public void desenhar() {
        Painel painelTelaPrincipal = new Painel(PaletaDeCores.azulForte, this.getXTamanho(), this.getYTamanho());

        BotaoPadrao botaoSair = new BotaoPadrao(new ImageIcon("src/administrador/login-de-usuario.png"), 1130, 40, 50, 50);
        botaoSair.setToolTipText("Sair do sistema");
        botaoSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoSairPressionado) {
                dispose();
                new TelaLogin();
            }
        });
        painelTelaPrincipal.add(botaoSair);


        LabelTitulo labelTitulo = new LabelTitulo(PaletaDeCores.branco, "Gerenciador de teatro", 319, 114, 562, ValoresPadroes.alturaLabelTitulo);
        labelTitulo.setForeground(PaletaDeCores.branco);
        painelTelaPrincipal.add(labelTitulo);

        BotaoTelaInicial botaoRegrasDePreco = new BotaoTelaInicial("Regras de preço", PaletaDeCores.branco, PaletaDeCores.azulForte, 319, 198, 253);
        botaoRegrasDePreco.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoSairPressionado) {
                dispose();
                new TelaRegrasDePreco();
            }
        });
        painelTelaPrincipal.add(botaoRegrasDePreco);

        BotaoTelaInicial botaoPropostasDeAluguel = new BotaoTelaInicial("Propostas de aluguel", PaletaDeCores.branco, PaletaDeCores.azulForte, 628, 198, 253);
        botaoPropostasDeAluguel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoSairPressionado) {
                dispose();
                new TelaPropostasDeAluguel();
            }
        });
        painelTelaPrincipal.add(botaoPropostasDeAluguel);

        BotaoTelaInicial botaoVendaDeIngresso = new BotaoTelaInicial("Vender ingresso", PaletaDeCores.amarelo, PaletaDeCores.azulForte, 319, 394, 253);
        botaoVendaDeIngresso.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoSairPressionado) {
                dispose();
                new TelaVendaDeIngresso();
            }
        });
        painelTelaPrincipal.add(botaoVendaDeIngresso);

        BotaoTelaInicial botaoRelatorioFinanceiro = new BotaoTelaInicial("Gerar relatório financeiro", PaletaDeCores.amarelo, PaletaDeCores.azulForte, 628, 394, 253);
        botaoRelatorioFinanceiro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoSairPressionado) {
                dispose();
                new TelaRelatorioFinanceiroTeatro();
            }
        });
        painelTelaPrincipal.add(botaoRelatorioFinanceiro);

        add(painelTelaPrincipal, new GridBagConstraints());
    }
}

