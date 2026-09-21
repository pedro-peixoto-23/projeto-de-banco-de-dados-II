package ifpb.telas.administrador;

import ifpb.modelo.usuario.Usuario;
import ifpb.telas.componente_personalizado.*;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
public class TelaRedefinicaoDeSenha extends TelaPadrao {
    private long codigoConfirmacaoGerado;
    private Usuario usuario;
    private CaixaDeTextoPadrao caixaTextoCodigoConfirmacao;
    private CaixaDeTextoSenhas caixaTextoNovaSenha;
    private CaixaDeTextoSenhas caixaTextoConfirmacaoSenha;

    public TelaRedefinicaoDeSenha(long codigoConfirmacaoGerado, Usuario usuario) {
        super("Redefinição de senha", PaletaDeCores.branco, 380, 365);

        this.codigoConfirmacaoGerado = codigoConfirmacaoGerado;
        this.usuario = usuario;
    }

    public void desenhar() {
        Painel painelRedefinicaoDeSenha = new Painel(PaletaDeCores.branco, getXTamanho(), getYTamanho());

        LabelPadrao labelCodigoConfirmacao = new LabelPadrao("Código de confirmação", 35, 36, 310, ValoresPadroes.alturaLabel);
        painelRedefinicaoDeSenha.add(labelCodigoConfirmacao);

        caixaTextoCodigoConfirmacao = new CaixaDeTextoPadrao(35, 62, 310);
        painelRedefinicaoDeSenha.add(caixaTextoCodigoConfirmacao);

        LabelPadrao labelNovaSenha = new LabelPadrao("Nova senha", 35, 117, 310, ValoresPadroes.alturaLabel);
        painelRedefinicaoDeSenha.add(labelNovaSenha);

        caixaTextoNovaSenha = new CaixaDeTextoSenhas(35, 143, 310);
        painelRedefinicaoDeSenha.add(caixaTextoNovaSenha);

        LabelPadrao labelConfirmacaoSenha = new LabelPadrao("Confirmação de senha", 35, 198, 310, ValoresPadroes.alturaLabel);
        painelRedefinicaoDeSenha.add(labelConfirmacaoSenha);

        caixaTextoConfirmacaoSenha = new CaixaDeTextoSenhas(35, 224, 310);
        painelRedefinicaoDeSenha.add(caixaTextoConfirmacaoSenha);

        BotaoPadrao botaoRedefinicao = new BotaoPadrao("Redefinir", PaletaDeCores.azulForte, PaletaDeCores.branco, 35, 299, 186);
        botaoRedefinicao.addActionListener(new OuvinteBotaoRedefinirSenha(this));
        painelRedefinicaoDeSenha.add(botaoRedefinicao);

        BotaoPadrao botaoCancelar = new BotaoPadrao("Cancelar", PaletaDeCores.vermelho, PaletaDeCores.branco, 233, 299, 111);
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoCancelarPressionado) {
                dispose();
                new TelaLogin();
            }
        });
        painelRedefinicaoDeSenha.add(botaoCancelar);

        add(painelRedefinicaoDeSenha, new GridBagConstraints());
    }
}
