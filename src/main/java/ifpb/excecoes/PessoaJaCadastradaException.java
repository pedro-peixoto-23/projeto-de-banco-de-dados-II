package ifpb.excecoes;

public class PessoaJaCadastradaException extends Exception {
    public PessoaJaCadastradaException() {
        super("A pessoa já está cadastrada no banco de dados");
    }
}
