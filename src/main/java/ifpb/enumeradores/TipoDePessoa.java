package ifpb.enumeradores;

public enum TipoDePessoa {
    NENHUM(""),
    LOCATARIO("Locatário"),
    ESPECTADOR("Espectador");

    private final String descricao;

    TipoDePessoa(String descricao) {
        this.descricao = descricao;
    }

    public String toString() {
        return descricao;
    }
}

