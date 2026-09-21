package ifpb.telas.pessoa;

import ifpb.enumeradores.Sexo;
import ifpb.enumeradores.TipoDePessoa;
import ifpb.telas.Sessao;
import ifpb.telas.componente_personalizado.*;
import ifpb.telas.configuracao.PaletaDeCores;
import ifpb.telas.configuracao.ValoresPadroes;
import ifpb.telas.principal.TelaInicial;
import lombok.Getter;

import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;


@Getter
public class TelaCadastroPessoas extends TelaPadrao {
    private TipoDePessoa tipoDePessoa;
    private CaixaDeTextoComMascara caixaTextoCPF;
    private CaixaDeTextoPadrao caixaTextoEmail;
    private ComboBoxPadrao<Sexo> escolhaSexo;
    private ComboBoxPadrao<TipoDePessoa> escolhaTipoPessoa;
    private CaixaDeTextoPadrao caixaTextoNome;
    private CaixaDeTextoComMascara caixaTextoTelefone;
    private CaixaDeTextoComMascara caixaTextoNascimento;

    public TelaCadastroPessoas() {
        super("Cadastro (Pessoa)", PaletaDeCores.branco, 756, 531);
    }

    public TelaCadastroPessoas(String cpf, TipoDePessoa tipoDePessoa) {
        super("Cadastro (Pessoa)", PaletaDeCores.branco, 756, 531);

        this.tipoDePessoa = tipoDePessoa;

        caixaTextoCPF.setText(cpf);
        caixaTextoCPF.setEnabled(false);

        escolhaTipoPessoa.setSelectedItem(tipoDePessoa);
        escolhaTipoPessoa.setEnabled(false);
    }

    public void desenhar() {
        Painel painelCadastro = new Painel(PaletaDeCores.branco, this.getXTamanho(), this.getYTamanho());
        add(painelCadastro);

        LabelTitulo labelTitulo = new LabelTitulo(PaletaDeCores.preto, "Cadastro (Pessoa)", 35, 36, 686, ValoresPadroes.alturaLabelTitulo);
        painelCadastro.add(labelTitulo);

        try {
            MaskFormatter mascaraCPF = new MaskFormatter("###.###.###-##");
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            MaskFormatter mascaraTelefone = new MaskFormatter("(##)#####-####");
            mascaraCPF.setPlaceholderCharacter(' ');
            mascaraData.setPlaceholderCharacter(' ');
            mascaraTelefone.setPlaceholderCharacter(' ');

            LabelPadrao labelCPF = new LabelPadrao("CPF", 35, 121, 310, ValoresPadroes.alturaLabel);
            painelCadastro.add(labelCPF);

            caixaTextoCPF = new CaixaDeTextoComMascara(mascaraCPF, 35, 147, 310, ValoresPadroes.alturaCaixasDeTexto);
            painelCadastro.add(caixaTextoCPF);

            LabelPadrao labeltelefone = new LabelPadrao("Telefone", 411, 202, 310, ValoresPadroes.alturaLabel);
            painelCadastro.add(labeltelefone);

            caixaTextoTelefone = new CaixaDeTextoComMascara(mascaraTelefone, 411, 228, 310, ValoresPadroes.alturaCaixasDeTexto);
            painelCadastro.add(caixaTextoTelefone);

            LabelPadrao labelDataNascimento = new LabelPadrao("Data de nascimento", 411, 280, 310, ValoresPadroes.alturaLabel);
            painelCadastro.add(labelDataNascimento);

            caixaTextoNascimento = new CaixaDeTextoComMascara(mascaraData, 411, 306, 310, ValoresPadroes.alturaCaixasDeTexto);
            painelCadastro.add(caixaTextoNascimento);
        } catch (ParseException exception) {
            exception.getStackTrace();
        }

        LabelPadrao labelEmail = new LabelPadrao("E-mail", 35, 202, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelEmail);

        caixaTextoEmail = new CaixaDeTextoPadrao(35, 228, 310);
        painelCadastro.add(caixaTextoEmail);

        LabelPadrao labelSexo = new LabelPadrao("Sexo", 35, 283, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelSexo);

        escolhaSexo = new ComboBoxPadrao<Sexo>(Sexo.values(), 35, 309, 310, ValoresPadroes.alturaCaixasDeTexto);
        painelCadastro.add(escolhaSexo);

        LabelPadrao labelTipoPessoa = new LabelPadrao("Tipo", 35, 364, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelTipoPessoa);

        escolhaTipoPessoa = new ComboBoxPadrao<TipoDePessoa>(TipoDePessoa.values(), 35, 390, 310, ValoresPadroes.alturaCaixasDeTexto);
        painelCadastro.add(escolhaTipoPessoa);

        LabelPadrao labelNome = new LabelPadrao("Nome", 411, 121, 310, ValoresPadroes.alturaLabel);
        painelCadastro.add(labelNome);

        caixaTextoNome = new CaixaDeTextoPadrao(411, 147, 310);
        painelCadastro.add(caixaTextoNome);

        BotaoPadrao botaoCadastrar = new BotaoPadrao("Cadastrar", PaletaDeCores.azulForte, PaletaDeCores.branco, 35, 465, 472);
        botaoCadastrar.addActionListener(new OuvinteBotaoCadastrarPessoa(this));
        painelCadastro.add(botaoCadastrar);

        BotaoPadrao botaoCancelar = new BotaoPadrao("Cancelar", PaletaDeCores.vermelho, PaletaDeCores.branco, 520, 465  , 201);
        botaoCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent botaoCancelarPressionado) {
                dispose();

                if (tipoDePessoa == null) {
                    new TelaInicial();
                }
            }
        });
        painelCadastro.add(botaoCancelar);
    }
}

