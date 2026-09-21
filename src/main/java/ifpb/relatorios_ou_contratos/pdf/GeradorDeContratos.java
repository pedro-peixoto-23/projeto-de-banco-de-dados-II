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
import ifpb.modelo.pessoa.Locatario;
import ifpb.modelo.proposta_de_aluguel.PeriodoExibicaoPeca;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;

import java.io.IOException;


public class GeradorDeContratos {
    private static final String nomeArquivoContrato = "contrato.pdf";

    public static String getNomeArquivoContrato() {
        return nomeArquivoContrato;
    }

    public static void gerarContrato(PropostaDeAluguel propostaDeAluguel) {
        Locatario locatario = propostaDeAluguel.getLocatario();

        try {
            PdfWriter writer = new PdfWriter(GeradorDeContratos.nomeArquivoContrato);

            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(35, 35, 35, 35);

            PdfFont fonteHelveticaBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fonteHelvetica = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            int tamanhoParagrafo = 15;
            int tamanhoMargemParagrafos = 20;
            int tamanhoAssinaturas = 12;

            // Título

            Paragraph titulo = new Paragraph("CONTRATO")
                    .setFont(fonteHelveticaBold)
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(titulo);


            Paragraph linhaDivisoria = new Paragraph("_".repeat(150))
                    .setFont(fonteHelveticaBold)
                    .setFontSize(5)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(30);

            document.add(linhaDivisoria);

            // Dados do locador

            Paragraph tituloDadosLocador = new Paragraph("Dados do locador")
                    .setFont(fonteHelveticaBold)
                    .setFontSize(tamanhoParagrafo)
                    .setTextAlignment(TextAlignment.LEFT);

            document.add(tituloDadosLocador);

            List listaDadosLocador = new List()
                    .setSymbolIndent(5)
                    .setListSymbol("•")
                    .setFont(fonteHelvetica)
                    .setFontSize(tamanhoParagrafo)
                    .setMarginBottom(tamanhoMargemParagrafos);

            listaDadosLocador.add(new ListItem("Nome: Pedro Peixoto Viana de Oliveira"))
                    .add(new ListItem("CPF: 99999999999"));

            document.add(listaDadosLocador);

            // Dados do Locatário

            Paragraph tituloDadosLocatario = new Paragraph("Dados do locatário")
                    .setFont(fonteHelveticaBold)
                    .setFontSize(tamanhoParagrafo)
                    .setTextAlignment(TextAlignment.LEFT);
            document.add(tituloDadosLocatario);

            List listaDadosLocatario = new List()
                    .setSymbolIndent(5)
                    .setListSymbol("•")
                    .setFont(fonteHelvetica)
                    .setFontSize(tamanhoParagrafo)
                    .setMarginBottom(tamanhoMargemParagrafos);

            listaDadosLocatario.add(new ListItem("Nome: " + locatario.getNome()))
                    .add(new ListItem("CPF: " + locatario.getCpf()));

            document.add(listaDadosLocatario);

            // Dados da proposta

            Paragraph tituloDadosProposta = new Paragraph("Dados da proposta")
                    .setFont(fonteHelveticaBold)
                    .setFontSize(tamanhoParagrafo)
                    .setTextAlignment(TextAlignment.LEFT);
            document.add(tituloDadosProposta);

            List listaDadosProposta = new List()
                    .setSymbolIndent(5)
                    .setListSymbol("•")
                    .setFont(fonteHelvetica)
                    .setFontSize(tamanhoParagrafo)
                    .setMarginBottom(50);

            listaDadosProposta
                    .add(new ListItem("ID: " + propostaDeAluguel.getId()))
                    .add(new ListItem("Nome da peça: " + propostaDeAluguel.getPeca().getNome()))
                    .add(new ListItem(
                            String.format(
                                    "Valor total do aluguel: R$ %.2f",
                                    propostaDeAluguel.calcularValorTotalAluguel()
                            )
                    ))
                    .add(new ListItem("Locatário: " + propostaDeAluguel.getLocatario().getNome()))
                    .add(new ListItem(
                            String.format(
                                    "Preço do ticket: R$ %.2f",
                                    propostaDeAluguel.getPrecoTicket()
                            )
                    ));

            int numeroPeriodo = 1;

            for (PeriodoExibicaoPeca periodo : propostaDeAluguel.getPeriodosDeTempoExibicao()) {
                listaDadosProposta.add(new ListItem("Período " + numeroPeriodo + ": " + periodo.getPeriodoDeTempo().getDataInicio() + " até " + periodo.getPeriodoDeTempo().getDataFim()));

                numeroPeriodo += 1;
            }

            document.add(listaDadosProposta);

            // Assinaturas

            Paragraph assinaturaLocatario = new Paragraph("____________________________________________________________\nAssinatura do Locatário")
                    .setFont(fonteHelvetica)
                    .setFontSize(tamanhoAssinaturas)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(assinaturaLocatario);

            Paragraph dataAssinatura = new Paragraph("_______/_______/___________\nData")
                    .setFont(fonteHelvetica)
                    .setFontSize(tamanhoAssinaturas)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(dataAssinatura);


            Paragraph assinaturaLocador = new Paragraph("____________________________________________________________\nAssinatura do Locador")
                    .setFont(fonteHelvetica)
                    .setFontSize(tamanhoAssinaturas)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(assinaturaLocador);

            Paragraph dataAssinaturaLocador = new Paragraph("_______/_______/___________\nData")
                    .setFont(fonteHelvetica)
                    .setFontSize(tamanhoAssinaturas)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(dataAssinaturaLocador);


            document.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}


