package ifpb.modelo.intervalos;

import java.time.LocalTime;
import java.util.Objects;

import ifpb.excecoes.IntervaloHorarioInvalidoException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class IntervaloHorarios {
    @Column(nullable = false)
    private LocalTime horarioInicio;
    @Column(nullable = false)
    private LocalTime horarioFim;

    public IntervaloHorarios() {

    }

    public IntervaloHorarios(LocalTime horarioInicio, LocalTime horarioFim) throws IntervaloHorarioInvalidoException {
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;

        if (!isValido()) {
            throw new IntervaloHorarioInvalidoException();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || !object.getClass().equals(this.getClass())) return false;

        IntervaloHorarios intervaloDeHorario = (IntervaloHorarios) object;
        return horarioInicio.equals(intervaloDeHorario.getHorarioInicio()) && horarioFim.equals(intervaloDeHorario.getHorarioFim());
    }

    @Override
    public int hashCode() {
        return Objects.hash(horarioInicio, horarioFim);
    }

    public boolean isValido() {
        return horarioInicio.isBefore(horarioFim);
    }

    public boolean isDentro(IntervaloHorarios intervaloDeHorario) {
        return !horarioInicio.isBefore(intervaloDeHorario.getHorarioInicio()) && !horarioFim.isAfter(intervaloDeHorario.horarioFim);
    }

    public boolean temConflitoCom(IntervaloHorarios intervaloHorario) {
        return horarioInicio.isBefore(intervaloHorario.horarioFim) && horarioFim.isAfter(intervaloHorario.horarioInicio);
    }

    public String toString() {
        return horarioInicio + " - " + horarioFim;
    }

}
