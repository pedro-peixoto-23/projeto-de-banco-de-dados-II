package ifpb.enumeradores;

public enum DiaDaSemana {
    NENHUM(""),
    SEGUNDA("Segunda-feira"),
    TERCA("Terça-feira"),
    QUARTA("Quarta-feira"),
    QUINTA("Quinta-feira"),
    SEXTA("Sexta-feira"),
    SABADO("Sábado"),
    DOMINGO("Domingo");

    private final String descricao;

    DiaDaSemana(String descricao) {
        this.descricao = descricao;
    }

    public String toString() {
        return descricao;
    }
}
