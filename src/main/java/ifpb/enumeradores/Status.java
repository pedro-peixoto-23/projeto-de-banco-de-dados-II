package ifpb.enumeradores;

public enum Status {
    NENHUM(""),
    EM_AVALIACAO("Em avaliação"),
    ATIVO("Ativo"),
    ENCERRADO("Encerrado"),
    CONTRATADO_COM_ALTERACAO("Contratado com alteração");

    public final String descricao;

    Status(String descricao) {
        this.descricao = descricao;
    }

    public String toString() {
        return descricao;
    }
}
