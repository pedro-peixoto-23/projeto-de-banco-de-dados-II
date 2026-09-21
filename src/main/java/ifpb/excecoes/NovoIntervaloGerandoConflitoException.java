package ifpb.excecoes;

public class NovoIntervaloGerandoConflitoException extends Exception {
    public NovoIntervaloGerandoConflitoException() {
        super("Intevalo inválido. Ele está gerando conflito com os outros.");
    }
}
