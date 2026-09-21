package ifpb.relatorios_ou_contratos.pdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import ifpb.modelo.ingresso.Ingresso;
import ifpb.modelo.ingresso.VendaDeIngresso;
import ifpb.modelo.proposta_de_aluguel.PeriodoExibicaoPeca;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class GeradorDeIngressos {
    private static final String nomeArquivoIngressos = "ingressos.pdf";

    public static String getNomeArquivoIngressos() {
        return nomeArquivoIngressos;
    }

    public static void gerarIngressos(VendaDeIngresso vendaDeIngresso) {
        try {
            PdfWriter writer = new PdfWriter(nomeArquivoIngressos);

            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(50, 50, 50, 50);

            PdfFont fonteHelveticaBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fonteHelvetica = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            PeriodoExibicaoPeca periodoDaPeca = vendaDeIngresso.getPropostaDeAluguel().buscarPeriodoPorData(vendaDeIngresso.getDataDaPeca());

            int numeroIngresso = 1;


            for (Ingresso ingresso : vendaDeIngresso.getIngressos()) {
                Paragraph titulo = new Paragraph("INGRESSO").setFont(fonteHelveticaBold).setFontSize(24).setTextAlignment(TextAlignment.CENTER);
                document.add(titulo);

                Paragraph linhaDivisoria = new Paragraph("_".repeat(100)).setFont(fonteHelvetica).setFontSize(6).setTextAlignment(TextAlignment.CENTER).setMarginBottom(30);
                document.add(linhaDivisoria);

                Paragraph dadosIngresso = new Paragraph("Ingresso: " + numeroIngresso + "\n\n" + "Código do ingresso: " + ingresso.getId() + "\n\n" + "Espectador: " + vendaDeIngresso.getEspectador().getNome() + "\n\n" + "CPF: " + vendaDeIngresso.getEspectador().getCpf() + "\n\n" + "Peça: " + vendaDeIngresso.getPropostaDeAluguel().getPeca().getNome() + "\n\n" + "Data: " + vendaDeIngresso.getDataDaPeca().format(formatadorData) + "\n\n" + "Horário: " + periodoDaPeca.getIntervaloDeHorario() + "\n\n" + "Valor: R$ " + String.format("%.2f", vendaDeIngresso.getValorUnitario())).setFont(fonteHelvetica).setFontSize(15);
                document.add(dadosIngresso);

                Paragraph aviso = new Paragraph("\nApresente este ingresso na entrada do teatro.").setFont(fonteHelveticaBold).setFontSize(12).setTextAlignment(TextAlignment.CENTER);

                document.add(aviso);

                if (numeroIngresso < vendaDeIngresso.getQuantidade()) {
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                }

                numeroIngresso++;
            }

            document.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
