package ifpb.telas.administrador;

import ifpb.telas.componente_personalizado.*;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;
import lombok.Getter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


@Getter
public class TelaCadastroADM extends TelaPadrao {
    private CaixaDeTextoPadrao caixaTextoNome;
    private CaixaDeTextoPadrao caixaTextoEmail;
    private CaixaDeTextoSenhas caixaTextoNovaSenha;
    private CaixaDeTextoSenhas caixaTextoConfirmacaoSenha;

    public TelaCadastroADM() {
        super("Cadastro (ADM)", PaletaDeCores.branco, 380, 531);
    }

    public void desenhar() {
        Painel painelCadastro = new Painel(PaletaDeCores.branco, getXTamanho(), getYTamanho());

        LabelTitulo labelTitulo = new LabelTitulo(PaletaDeCores.preto, "Cadastro (ADM)", 35, 36, 310, ValoresPadroes.alturaLabelTitulo);
        painelCadastro.add(labelTitulo);

        LabelPadrao labelNome = new LabelPadrao("Nome", 35, 121, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelNome);

        caixaTextoNome = new CaixaDeTextoPadrao(35, 147, 310);
        painelCadastro.add(caixaTextoNome);

        LabelPadrao labelEmail = new LabelPadrao("E-mail", 35, 202, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelEmail);

        caixaTextoEmail = new CaixaDeTextoPadrao(35, 228, 310);
        painelCadastro.add(caixaTextoEmail);

        LabelPadrao labelSenha = new LabelPadrao("Senha", 35, 283, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelSenha);

        caixaTextoNovaSenha = new CaixaDeTextoSenhas(35, 309, 310);
        painelCadastro.add(caixaTextoNovaSenha);

        LabelPadrao labelConfirmacaoSenha = new LabelPadrao("Confirmação de senha", 35, 364, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelConfirmacaoSenha);

        caixaTextoConfirmacaoSenha = new CaixaDeTextoSenhas(35, 390, 310);
        painelCadastro.add(caixaTextoConfirmacaoSenha);

        BotaoPadrao botaoCadastrar = new BotaoPadrao("Cadastrar", PaletaDeCores.azulForte, PaletaDeCores.branco, 35, 465, 186);
        botaoCadastrar.addActionListener(new OuvinteBotaoCadastrarADM(this));
        painelCadastro.add(botaoCadastrar);

        BotaoPadrao botaoCancelar = new BotaoPadrao("Cancelar", PaletaDeCores.vermelho, PaletaDeCores.branco, 232, 465, 111);
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoCancelarPressionado) {
                dispose();
                new TelaLogin();
            }
        });
        painelCadastro.add(botaoCancelar);

        add(painelCadastro, new GridBagConstraints());
    }
}

