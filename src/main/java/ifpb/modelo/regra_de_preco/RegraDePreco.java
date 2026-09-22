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

/**
 * Representa uma regra utilizada para definir o valor de aluguel
 * do teatro por hora.
 *
 * Uma regra pode possuir critérios específicos de ano, mês,
 * dia da semana, turno e intervalo de horário. Os critérios
 * não informados funcionam como condições genéricas para aplicação
 * da regra.
 */

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

    /**
     * Verifica se esta regra possui a mesma configuração de outra regra,
     * considerando o valor por hora e todos os critérios de aplicação.
     *
     * @param outraRegra regra que será comparada.
     * @return true se as regras possuírem a mesma configuração,
     * false caso contrário.
     */
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

    /**
     * Atualiza os dados e critérios de aplicação desta regra de preço.
     *
     * @param valorPorHora novo valor cobrado por hora.
     * @param ano novo ano de aplicação da regra.
     * @param mes novo mês de aplicação da regra.
     * @param diaDaSemana novo dia da semana de aplicação da regra.
     * @param turno novo turno de aplicação da regra.
     * @param intervaloDeHorario novo intervalo de horário de aplicação da regra.
     */
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

    /**
     * Verifica se os critérios de data e turno desta regra
     * são compatíveis com a data e o turno informados.
     *
     * Critérios não definidos na regra não restringem sua aplicação.
     *
     * @param dataDaPeca data que será verificada.
     * @param turnoPeca turno que será verificado.
     * @return true se a regra for aplicável para a data e ao turno,
     * false caso contrário.
     */
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

    /**
     * Verifica se esta regra de preço pode ser aplicada em uma
     * determinada data, turno e horário.
     *
     * Caso a regra não possua intervalo de horário definido,
     * a verificação considera apenas os critérios de data e turno.
     *
     * @param dataDaPeca data de exibição da peça.
     * @param turnoPeca turno da exibição.
     * @param horario horário que será avaliado.
     * @return true se a regra puder ser aplicada, false caso contrário.
     */
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
