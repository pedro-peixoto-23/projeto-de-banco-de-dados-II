package ifpb.modelo.intervalos;

import ifpb.excecoes.IntervaloDeDataInvalidoException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Embeddable
public class IntervaloDatas {
    @Column(nullable = false)
    private LocalDate dataInicio;
    @Column(nullable = false)
    private LocalDate dataFim;

    public IntervaloDatas(LocalDate dataInicio, LocalDate dataFim) throws IntervaloDeDataInvalidoException {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;

        if (!isValido()) {
            throw new IntervaloDeDataInvalidoException();
        }
    }

    public boolean isValido() {
        return !dataInicio.isAfter(dataFim);
    }

    @Override
    public String toString() {
        return dataInicio + " até " + dataFim;
    }
}