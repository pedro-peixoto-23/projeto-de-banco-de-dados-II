package ifpb.enumeradores;

public enum Mes {
    NENHUM(""),
    JANEIRO("Janeiro"),
    FEVEREIRO("Fevereiro"),
    MARCO("Março"),
    ABRIL("Abril"),
    MAIO("Maio"),
    JUNHO("Junho"),
    JULHO("Julho"),
    AGOSTO("Agosto"),
    SETEMBRO("Setembro"),
    OUTUBRO("Outubro"),
    NOVEMBRO("Novembro"),
    DEZEMBRO("Dezembro");

    private final String descricao;

    Mes(String descricao) {
        this.descricao = descricao;
    }

    public String toString() {
        return descricao;
    }
}
