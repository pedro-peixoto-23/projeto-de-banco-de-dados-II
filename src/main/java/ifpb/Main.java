package ifpb;

import ifpb.telas.Sessao;
import ifpb.telas.administrador.TelaCadastroADM;
import ifpb.telas.administrador.TelaLogin;
import org.h2.tools.Server;

import java.sql.SQLException;

public class Main {

    private static Server h2Console;

    public static void main(String[] args) {
        Sessao.iniciarSessao();

        iniciarH2Console();

        if (Sessao.getDaoUsuario().existeUsuarioCadastrado()) {
            new TelaLogin();
        } else {
            new TelaCadastroADM();
        }
    }

    private static void iniciarH2Console() {
        try {
            h2Console = Server.createWebServer("-webPort", "8082").start();

            System.out.println("H2 Console iniciado:");
            System.out.println("http://localhost:8082");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> h2Console.stop()));
        } catch (SQLException exception) {
            System.out.println("Não foi possível iniciar o H2 Console.");
            exception.printStackTrace();
        }
    }
}