package ifpb.excecoes;

public class ValorQtdTicketsInvalidoException extends RuntimeException {
    public ValorQtdTicketsInvalidoException() {
        super("O valor da quantidade de tickets é inválida!");
    }
}
