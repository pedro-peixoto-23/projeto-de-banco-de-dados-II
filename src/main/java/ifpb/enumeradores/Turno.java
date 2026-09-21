package ifpb.enumeradores;

import ifpb.modelo.intervalos.IntervaloHorarios;

import java.time.LocalTime;

public enum Turno {
    NENHUM("", null),
    MANHA("Manhã", new IntervaloHorarios(LocalTime.of(8, 00), LocalTime.of(12, 00))),
    TARDE("Tarde", new IntervaloHorarios(LocalTime.of(13, 00), LocalTime.of(18, 00))),
    NOITE("Noite", new IntervaloHorarios(LocalTime.of(19, 00), LocalTime.of(23, 00)));

    private final String descricao;
    private final IntervaloHorarios intervaloDeHorario;

    Turno(String descricao, IntervaloHorarios periodoDeTempo) {
        this.descricao = descricao;
        this.intervaloDeHorario = periodoDeTempo;
    }

    public IntervaloHorarios getIntervaloDeHorario() {
        return intervaloDeHorario;
    }

    public String toString() {
        return descricao;
    }
}