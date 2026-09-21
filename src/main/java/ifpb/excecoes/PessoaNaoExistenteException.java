package ifpb.excecoes;

public class PessoaNaoExistenteException extends Exception {
    public PessoaNaoExistenteException() {
        super("A pessoa não existe no banco de dados.");
    }
}
