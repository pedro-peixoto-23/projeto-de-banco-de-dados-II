package ifpb.email;


import org.apache.commons.mail2.core.EmailException;
import org.apache.commons.mail2.javax.EmailAttachment;
import org.apache.commons.mail2.javax.MultiPartEmail;
import org.apache.commons.mail2.javax.SimpleEmail;


public class Mensageiro {
    private static String emailSistema = "";
    private static String senhaSistema = "";

    public static void enviarMensagem(String email, String titulo, String mensagem) throws EmailException {
        SimpleEmail simpleEmail = new SimpleEmail();

        simpleEmail.setHostName("smtp.gmail.com");
        simpleEmail.setSmtpPort(587);
        simpleEmail.setStartTLSEnabled(true);
        simpleEmail.setStartTLSRequired(true);
        simpleEmail.setAuthentication(emailSistema, senhaSistema);

        simpleEmail.setFrom(emailSistema, "Pedro Peixoto");
        simpleEmail.addTo(email);
        simpleEmail.setSubject(titulo);
        simpleEmail.setMsg(mensagem);

        simpleEmail.send();
    }

    public static void enviarMensagem(String email, String titulo, String mensagem, String nomeArquivo) throws EmailException {
        MultiPartEmail multiPartEmail = new MultiPartEmail();

        multiPartEmail.setHostName("smtp.gmail.com");
        multiPartEmail.setSmtpPort(587);
        multiPartEmail.setStartTLSEnabled(true);
        multiPartEmail.setStartTLSRequired(true);
        multiPartEmail.setAuthentication(emailSistema, senhaSistema);

        multiPartEmail.setFrom(emailSistema, "Pedro Peixoto");
        multiPartEmail.addTo(email);
        multiPartEmail.setSubject(titulo);
        multiPartEmail.setMsg(mensagem);

        EmailAttachment anexo = new EmailAttachment();
        anexo.setPath(nomeArquivo);
        anexo.setDisposition(EmailAttachment.ATTACHMENT);
        anexo.setName(nomeArquivo);

        multiPartEmail.attach(anexo);
        multiPartEmail.send();
    }

    public static void main(String[] args) throws EmailException {
        Mensageiro.enviarMensagem("pedropeixoto54398@gmail.com", "teste", "teste");
    }
}
