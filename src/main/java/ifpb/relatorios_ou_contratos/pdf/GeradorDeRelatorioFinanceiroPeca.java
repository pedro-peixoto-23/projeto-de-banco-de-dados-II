package ifpb.relatorios_ou_contratos.pdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import ifpb.dao.DaoVendaDeIngresso;
import ifpb.modelo.proposta_de_aluguel.ControleFinanceiroProposta;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import ifpb.telas.Sessao;

import java.io.IOException;
import java.math.BigDecimal;

public class GeradorDeRelatorioFinanceiroPeca {

    private static final String nomeArquivoRelatorio = "relatorio_financeiro_peca.pdf";

    public static String getNomeArquivoRelatorio() {
        return nomeArquivoRelatorio;
    }

    public static void gerarRelatorio(PropostaDeAluguel propostaDeAluguel) {
        DaoVendaDeIngresso daoVendaDeIngresso = Sessao.getDaoVendaDeIngresso();

        ControleFinanceiroProposta controleFinanceiro = daoVendaDeIngresso.gerarControleFinanceiroProposta(propostaDeAluguel);

        try {
            PdfWriter writer = new PdfWriter(nomeArquivoRelatorio);

            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(35, 35, 35, 35);

            PdfFont fonteHelveticaBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fonteHelvetica = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            int tamanhoParagrafo = 15;

            Paragraph titulo = new Paragraph("RELATÓRIO FINANCEIRO DA PEÇA").setFont(fonteHelveticaBold).setFontSize(20).setTextAlignment(TextAlignment.CENTER);
            document.add(titulo);

            Paragraph linhaDivisoria = new Paragraph("_".repeat(150)).setFont(fonteHelveticaBold).setFontSize(5).setTextAlignment(TextAlignment.CENTER).setMarginBottom(30);
            document.add(linhaDivisoria);

            Paragraph tituloDados = new Paragraph("Dados da proposta").setFont(fonteHelveticaBold).setFontSize(tamanhoParagrafo);
            document.add(tituloDados);

            List listaDados = new List().setSymbolIndent(5).setListSymbol("•").setFont(fonteHelvetica).setFontSize(tamanhoParagrafo).setMarginBottom(30);

            listaDados.add(new ListItem("ID: " + propostaDeAluguel.getId())).add(new ListItem("Peça: " + propostaDeAluguel.getPeca().getNome())).add(new ListItem("Locatário: " + propostaDeAluguel.getLocatario().getNome()));

            document.add(listaDados);

            Paragraph tituloFinanceiro = new Paragraph("Resumo financeiro").setFont(fonteHelveticaBold).setFontSize(tamanhoParagrafo);
            document.add(tituloFinanceiro);

            List listaFinanceira = new List().setSymbolIndent(5).setListSymbol("•").setFont(fonteHelvetica).setFontSize(tamanhoParagrafo).setMarginBottom(30);

            listaFinanceira.add(new ListItem(String.format("Total arrecadado com ingressos: R$ %.2f", controleFinanceiro.getTotalArrecadado()))).add(new ListItem(String.format("Valor total do aluguel: R$ %.2f", controleFinanceiro.getValorAluguel()))).add(new ListItem(String.format("Valor líquido: R$ %.2f", controleFinanceiro.getValorLiquido())));

            document.add(listaFinanceira);

            BigDecimal valorLiquido = controleFinanceiro.getValorLiquido();

            Paragraph resultado;

            if (valorLiquido.compareTo(BigDecimal.ZERO) > 0) {
                resultado = new Paragraph(String.format("Valor a ser repassado ao artista: R$ %.2f", valorLiquido));
            } else if (valorLiquido.compareTo(BigDecimal.ZERO) < 0) {
                resultado = new Paragraph(String.format("Valor devido pelo artista ao teatro: R$ %.2f", valorLiquido.abs()));
            } else {
                resultado = new Paragraph("Não há saldo pendente entre o teatro e o artista.");
            }

            resultado.setFont(fonteHelveticaBold).setFontSize(15).setTextAlignment(TextAlignment.CENTER);
            document.add(resultado);

            document.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}

