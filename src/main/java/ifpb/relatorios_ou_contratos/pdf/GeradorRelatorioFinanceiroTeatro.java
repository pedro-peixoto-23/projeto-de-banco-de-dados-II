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

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GeradorRelatorioFinanceiroTeatro {

    private static final String nomeArquivoRelatorio = "relatorio_financeiro_teatro.pdf";

    public static String getNomeArquivoRelatorio() {
        return nomeArquivoRelatorio;
    }

    public static void gerarRelatorio(LocalDate dataInicio, LocalDate dataFim, BigDecimal totalVendaIngressos, BigDecimal totalAlugueis, BigDecimal totalGeral) {
        try {
            PdfWriter writer = new PdfWriter(nomeArquivoRelatorio);

            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(35, 35, 35, 35);

            PdfFont fonteHelveticaBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fonteHelvetica = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            int tamanhoParagrafo = 15;

            // Título

            Paragraph titulo = new Paragraph("RELATÓRIO FINANCEIRO DO TEATRO")
                    .setFont(fonteHelveticaBold).setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(titulo);

            Paragraph linhaDivisoria = new Paragraph("_".repeat(150))
                    .setFont(fonteHelveticaBold).setFontSize(5)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(30);
            document.add(linhaDivisoria);

            // Período do relatório

            Paragraph tituloPeriodo = new Paragraph("Período do relatório")
                    .setFont(fonteHelveticaBold)
                    .setFontSize(tamanhoParagrafo);
            document.add(tituloPeriodo);

            List listaPeriodo = new List()
                    .setSymbolIndent(5)
                    .setListSymbol("•")
                    .setFont(fonteHelvetica)
                    .setFontSize(tamanhoParagrafo)
                    .setMarginBottom(30);

            listaPeriodo.add(
                    new ListItem("Data inicial: " + dataInicio.format(formatadorData))).add(
                    new ListItem("Data final: " + dataFim.format(formatadorData))
            );
            document.add(listaPeriodo);

            // Resumo financeiro

            Paragraph tituloFinanceiro = new Paragraph("Resumo financeiro").setFont(fonteHelveticaBold).setFontSize(tamanhoParagrafo);
            document.add(tituloFinanceiro);

            List listaFinanceira = new List()
                    .setSymbolIndent(5)
                    .setListSymbol("•")
                    .setFont(fonteHelvetica)
                    .setFontSize(tamanhoParagrafo)
                    .setMarginBottom(30);
            listaFinanceira.add(
                    new ListItem(String.format("Total arrecadado com venda de ingressos: R$ %.2f", totalVendaIngressos))).add(
                    new ListItem(String.format("Total obtido com contratos de aluguel: R$ %.2f", totalAlugueis))
            );
            document.add(listaFinanceira);

            // Total geral

            Paragraph total = new Paragraph(String.format("Total geral de receitas: R$ %.2f", totalGeral)).setFont(fonteHelveticaBold).setFontSize(17).setTextAlignment(TextAlignment.CENTER);
            document.add(total);

            document.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
