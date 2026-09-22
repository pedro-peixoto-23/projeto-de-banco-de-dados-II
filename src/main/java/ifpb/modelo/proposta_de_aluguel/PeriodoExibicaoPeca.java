package ifpb.modelo.proposta_de_aluguel;

import ifpb.enumeradores.Turno;
import ifpb.modelo.intervalos.IntervaloDatas;
import ifpb.modelo.intervalos.IntervaloHorarios;
import ifpb.modelo.regra_de_preco.RegraDePreco;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um período de exibição de uma peça no teatro.
 *
 * Mantém o intervalo de datas, o turno, o horário da apresentação,
 * o período de ocupação do teatro e os valores de aluguel
 * calculados para cada dia de exibição.
 */

@Getter
@NoArgsConstructor

@Table(name = "tb_periodo_exibicao_peca")
@Entity
public class PeriodoExibicaoPeca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private IntervaloDatas periodoDeTempo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Turno turno;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "horarioInicio", column = @Column(name = "horario_inicio_peca", nullable = false)),
                    @AttributeOverride(name = "horarioFim", column = @Column(name = "horario_fim_peca", nullable = false))
            }
    )
    private IntervaloHorarios intervaloDeHorario;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "horarioInicio", column = @Column(name = "horario_inicio_ocupacao", nullable = false)),
                    @AttributeOverride(name = "horarioFim", column = @Column(name = "horario_fim_ocupacao", nullable = false))
            }
    )
    private IntervaloHorarios intervaloDeOcupacaoDoTeatro;

    @ElementCollection
    @CollectionTable(
            name = "tb_valor_diario_aluguel",
            joinColumns = @JoinColumn(name = "periodo_exibicao_id")
    )
    @OrderBy("dia ASC")
    private List<ControleValorDiarioAluguel> listaControleValorDiarioAluguel = new ArrayList<>();

    public PeriodoExibicaoPeca(IntervaloDatas periodoDeTempo, Turno turno, IntervaloHorarios intervaloDeHorario, IntervaloHorarios intervaloDeOcupacaoDoTeatro, List<RegraDePreco> regrasDePreco) {
        this.periodoDeTempo = periodoDeTempo;
        this.turno = turno;
        this.intervaloDeHorario = intervaloDeHorario;
        this.intervaloDeOcupacaoDoTeatro = intervaloDeOcupacaoDoTeatro;

        preencherValorDiarioAluguel(regrasDePreco);
    }

    @Override
    public String toString() {
        return periodoDeTempo.toString();
    }

    /**
     * Calcula o valor total do aluguel deste período
     * somando os valores calculados para cada dia.
     *
     * @return valor total do período.
     */
    public BigDecimal gerarValorTotalPeriodo() {
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ControleValorDiarioAluguel controle : listaControleValorDiarioAluguel) {
            valorTotal = valorTotal.add(controle.getValor());
        }

        return valorTotal;
    }

    /**
     * Calcula o valor do aluguel do teatro para uma determinada data.
     *
     * O intervalo de ocupação é percorrido minuto a minuto. Para cada
     * minuto, são verificadas as regras de preço aplicáveis e utilizado
     * o maior valor por hora encontrado.
     *
     * @param data data para a qual o valor será calculado.
     * @param regrasDePreco regras disponíveis para o cálculo.
     * @return valor total do aluguel referente à data.
     */
    private BigDecimal calcularValorDiaria(LocalDate data, List<RegraDePreco> regrasDePreco) {
        LocalTime horarioAtual = intervaloDeOcupacaoDoTeatro.getHorarioInicio();
        LocalTime horarioFim = intervaloDeOcupacaoDoTeatro.getHorarioFim();

        BigDecimal valorTotal = BigDecimal.ZERO;

        while (horarioAtual.isBefore(horarioFim)) {
            BigDecimal maiorValorPorHora = BigDecimal.ZERO;

            for (RegraDePreco regraDePreco : regrasDePreco) {
                if (regraDePreco.seAplica(data, turno, horarioAtual)) {
                    if (regraDePreco.getValorPorHora().compareTo(maiorValorPorHora) > 0) {
                        maiorValorPorHora = regraDePreco.getValorPorHora();
                    }
                }
            }

            BigDecimal valorPorMinuto = maiorValorPorHora.divide(BigDecimal.valueOf(60), 10, RoundingMode.HALF_UP);
            valorTotal = valorTotal.add(valorPorMinuto);

            horarioAtual = horarioAtual.plusMinutes(1);
        }

        return valorTotal;
    }

    /**
     * Calcula e armazena o valor de aluguel de cada dia
     * pertencente ao período de exibição.
     *
     * @param regrasDePreco regras utilizadas no cálculo
     * dos valores diários.
     */
    private void preencherValorDiarioAluguel(List<RegraDePreco> regrasDePreco) {
        listaControleValorDiarioAluguel.clear();

        LocalDate diaAtual = periodoDeTempo.getDataInicio();
        LocalDate dataFim = periodoDeTempo.getDataFim();

        while (!diaAtual.isAfter(dataFim)) {
            BigDecimal valorDiaria = calcularValorDiaria(diaAtual, regrasDePreco);

            ControleValorDiarioAluguel controle = new ControleValorDiarioAluguel(diaAtual, valorDiaria);
            listaControleValorDiarioAluguel.add(controle);

            diaAtual = diaAtual.plusDays(1);
        }
    }
}