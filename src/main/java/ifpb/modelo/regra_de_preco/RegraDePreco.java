package ifpb.modelo.regra_de_preco;


import ifpb.enumeradores.DiaDaSemana;
import ifpb.enumeradores.Mes;
import ifpb.enumeradores.Turno;
import ifpb.modelo.intervalos.IntervaloHorarios;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;


@NoArgsConstructor
@Getter

@Table(name = "tb_regra_de_preco")
@Entity
public class RegraDePreco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valorPorHora;

    private Integer ano;

    @Enumerated(EnumType.STRING)
    private Mes mes;

    @Enumerated(EnumType.STRING)
    private DiaDaSemana diaDaSemana;

    @Enumerated(EnumType.STRING)
    private Turno turno;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "horarioInicio", column = @Column(name = "horarioInicio", nullable = true)),
            @AttributeOverride(name = "horarioFim", column = @Column(name = "horarioFim", nullable = true))
    })
    private IntervaloHorarios intervaloDeHorario;

    public RegraDePreco(BigDecimal valorPorHora, Integer ano, Mes mes, DiaDaSemana diaDaSemana, Turno turno, IntervaloHorarios intervaloDeHorario) {
        this.valorPorHora = valorPorHora;
        this.ano = ano;
        this.mes = mes;
        this.diaDaSemana = diaDaSemana;
        this.turno = turno;
        this.intervaloDeHorario = intervaloDeHorario;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof RegraDePreco)) {
            return false;
        }

        RegraDePreco regraDePreco = (RegraDePreco) object;

        return (id != null) && id.equals(regraDePreco.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return String.format("Ano: %d - Mês: %s - Dia: %s - Horário: %s", ano, mes, diaDaSemana, intervaloDeHorario);
    }

    public boolean temMesmaConfiguracao(RegraDePreco outraRegra) {
        return valorPorHora.compareTo(outraRegra.getValorPorHora()) == 0
                && Objects.equals(ano, outraRegra.getAno())
                && Objects.equals(mes, outraRegra.getMes())
                && Objects.equals(diaDaSemana, outraRegra.getDiaDaSemana())
                && Objects.equals(turno, outraRegra.getTurno())
                && Objects.equals(
                intervaloDeHorario,
                outraRegra.getIntervaloDeHorario()
        );
    }

    public void atualizarRegra(
            BigDecimal valorPorHora,
            Integer ano,
            Mes mes,
            DiaDaSemana diaDaSemana,
            Turno turno,
            IntervaloHorarios intervaloDeHorario) {
        this.valorPorHora = valorPorHora;
        this.ano = ano;
        this.mes = mes;
        this.diaDaSemana = diaDaSemana;
        this.turno = turno;
        this.intervaloDeHorario = intervaloDeHorario;
    }

    private boolean seAplicaNaDataETurno(LocalDate dataDaPeca, Turno turnoPeca) {
        if (ano != null && dataDaPeca.getYear() != ano) {
            return false;
        }

        if (mes != null && dataDaPeca.getMonthValue() != mes.ordinal()) {
            return false;
        }

        if (diaDaSemana != null && dataDaPeca.getDayOfWeek().getValue() != diaDaSemana.ordinal()) {
            return false;
        }

        if (turno != null && !turno.equals(turnoPeca)) {
            return false;
        }

        return true;
    }

    public boolean seAplica(LocalDate dataDaPeca, Turno turnoPeca, LocalTime horario) {
        if (!seAplicaNaDataETurno(dataDaPeca, turnoPeca)) {
            return false;
        }

        if (intervaloDeHorario == null) {
            return true;
        }

        return !horario.isBefore(intervaloDeHorario.getHorarioInicio()) && horario.isBefore(intervaloDeHorario.getHorarioFim());
    }
}
