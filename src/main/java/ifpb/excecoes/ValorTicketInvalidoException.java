package ifpb.excecoes;

public class ValorTicketInvalidoException extends Exception {
    public ValorTicketInvalidoException() {
        super("Valor do ticket inválido.");
    }
}
