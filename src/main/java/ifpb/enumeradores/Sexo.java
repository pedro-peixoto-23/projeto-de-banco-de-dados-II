package ifpb.enumeradores;

public enum Sexo {
    NENHUM(""),
    MASCULINO("Masculino"),
    FEMININO("Feminino");

    public final String descricao;

    Sexo(String descricao) {
        this.descricao = descricao;
    }

    public String toString() {
        return descricao;
    }
}
