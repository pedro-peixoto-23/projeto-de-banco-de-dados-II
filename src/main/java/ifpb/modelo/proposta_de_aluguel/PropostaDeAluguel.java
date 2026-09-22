package ifpb.modelo.proposta_de_aluguel;

import ifpb.enumeradores.Status;
import ifpb.excecoes.NovoIntervaloGerandoConflitoException;
import ifpb.modelo.pessoa.Locatario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma proposta de aluguel do teatro.
 *
 * A proposta associa uma peça a um locatário, mantém seus períodos
 * de exibição, o preço dos ingressos, o estado atual do contrato
 * e as informações financeiras relacionadas ao seu encerramento.
 */

@Getter
@NoArgsConstructor()

@Table(name = "tb_proposta_de_aluguel")
@Entity
public class PropostaDeAluguel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Peca peca;

    @ManyToOne
    @JoinColumn(name = "locatario_id", nullable = false)
    private Locatario locatario;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "proposta_id", nullable = false)
    private List<PeriodoExibicaoPeca> periodosDeTempoExibicao = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoTicket;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private LocalDate dataEncerramento;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorIngressosRepassado;

    @Column(precision = 10, scale = 2)
    private BigDecimal saldoAluguel;

    public PropostaDeAluguel(Peca peca, PeriodoExibicaoPeca periodoDeTempoProposta, Locatario locatario, BigDecimal precoTicket) {
        this.peca = peca;
        this.locatario = locatario;
        this.precoTicket = precoTicket;
        this.status = Status.EM_AVALIACAO;

        this.periodosDeTempoExibicao.add(periodoDeTempoProposta);
    }

    @Override
    public String toString() {
        return id + " - " + peca.getNome();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof PropostaDeAluguel)) {
            return false;
        }

        PropostaDeAluguel propostaDeAluguel = (PropostaDeAluguel) object;

        return (id != null) && (id.equals(propostaDeAluguel.getId()));
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * Calcula o valor total do aluguel da proposta somando
     * os valores de todos os seus períodos de exibição.
     *
     * @return valor total do aluguel com duas casas decimais.
     */
    public BigDecimal calcularValorTotalAluguel() {
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (PeriodoExibicaoPeca periodo : periodosDeTempoExibicao) {
            valorTotal = valorTotal.add(periodo.gerarValorTotalPeriodo());
        }

        return valorTotal.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Obtém a primeira data de exibição considerando
     * todos os períodos cadastrados na proposta.
     *
     * @return data de início mais antiga entre os períodos.
     */
    public LocalDate gerarDataInicioAluguel() {
        LocalDate menorDataInicio = null;

        for (PeriodoExibicaoPeca periodo : periodosDeTempoExibicao) {
            LocalDate dataInicio = periodo.getPeriodoDeTempo().getDataInicio();
            if (menorDataInicio == null || dataInicio.isBefore(menorDataInicio)) {
                menorDataInicio = dataInicio;
            }
        }

        return menorDataInicio;
    }

    /**
     * Obtém a última data de exibição entre todos os períodos
     * pertencentes à proposta.
     *
     * @return maior data de término encontrada nos períodos.
     */
    public LocalDate gerarDataFimAluguel() {
        LocalDate maiorDataFim = null;

        for (PeriodoExibicaoPeca periodo : periodosDeTempoExibicao) {
            LocalDate dataFim = periodo.getPeriodoDeTempo().getDataFim();

            if (maiorDataFim == null || dataFim.isAfter(maiorDataFim)) {
                maiorDataFim = dataFim;
            }
        }

        return maiorDataFim;
    }

    /**
     * Adiciona um novo período de exibição na proposta
     * e atualiza o status para contratado com alteração.
     *
     * @param novoPeriodo novo período de exibição da proposta.
     */
    public void adicionarNovoPeriodo(PeriodoExibicaoPeca novoPeriodo) throws NovoIntervaloGerandoConflitoException {
        periodosDeTempoExibicao.add(novoPeriodo);
        status = Status.CONTRATADO_COM_ALTERACAO;
    }

    /**
     * Busca o período de exibição que contém a data informada.
     *
     * As datas inicial e final de cada período são consideradas
     * pertencentes ao intervalo.
     *
     * @param data data de exibição que será procurada.
     * @return período correspondente à data ou null caso não seja encontrado.
     */
    public PeriodoExibicaoPeca buscarPeriodoPorData(LocalDate data) {
        for (PeriodoExibicaoPeca periodo : periodosDeTempoExibicao) {
            if (!data.isBefore(periodo.getPeriodoDeTempo().getDataInicio()) && !data.isAfter(periodo.getPeriodoDeTempo().getDataFim())) {
                return periodo;
            }
        }

        return null;
    }

    /**
     * Gera todas as datas de exibição da proposta a partir
     * dos períodos cadastrados.
     *
     * @return lista contendo todas as datas de exibição da proposta.
     */
    public ArrayList<LocalDate> gerarDatasExibicao() {
        ArrayList<LocalDate> datas = new ArrayList<>();

        for (PeriodoExibicaoPeca periodo : periodosDeTempoExibicao) {
            LocalDate dataAtual = periodo.getPeriodoDeTempo().getDataInicio();
            LocalDate dataFim = periodo.getPeriodoDeTempo().getDataFim();

            while (!dataAtual.isAfter(dataFim)) {
                datas.add(dataAtual);
                dataAtual = dataAtual.plusDays(1);
            }
        }

        return datas;
    }

    /**
     * Calcula o valor do aluguel acumulado até a data informada,
     * considerando os valores diários existentes nos períodos da proposta.
     *
     * A própria data limite também é considerada no cálculo.
     *
     * @param dataEncerramento data limite para o cálculo.
     * @return valor do aluguel acumulado até a data informada.
     */
    public BigDecimal calcularValorAluguelAteData(LocalDate dataEncerramento) {
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (PeriodoExibicaoPeca periodo : periodosDeTempoExibicao) {
            for (ControleValorDiarioAluguel controle : periodo.getListaControleValorDiarioAluguel()) {
                if (!controle.getDia().isAfter(dataEncerramento)) {
                    valorTotal = valorTotal.add(controle.getValor());
                }
            }
        }

        return valorTotal.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Verifica se uma data pode ser utilizada para o encerramento
     * da proposta.
     *
     * A data é considerada válida quando pertence a pelo menos
     * um dos períodos de exibição da proposta.
     *
     * @param dataEncerramento data que será validada.
     * @return true se a data pertencer a algum período,
     * false caso contrário.
     */
    public boolean isDataEncerramentoValida(LocalDate dataEncerramento) {
        if (dataEncerramento == null) {
            return false;
        }

        for (PeriodoExibicaoPeca periodo : periodosDeTempoExibicao) {
            LocalDate dataInicio = periodo.getPeriodoDeTempo().getDataInicio();
            LocalDate dataFim = periodo.getPeriodoDeTempo().getDataFim();

            if (!dataEncerramento.isBefore(dataInicio) && !dataEncerramento.isAfter(dataFim)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Registra as informações financeiras do encerramento da proposta
     * e altera seu status para encerrado.
     *
     * @param dataEncerramento data em que o contrato foi encerrado.
     * @param valorIngressosRepassado valor proveniente dos ingressos
     * que deve ser repassado após o acerto do aluguel.
     * @param saldoAluguel valor de aluguel ainda devido no encerramento.
     */
    public void registrarEncerramento(LocalDate dataEncerramento, BigDecimal valorIngressosRepassado, BigDecimal saldoAluguel) {
        this.dataEncerramento = dataEncerramento;
        this.valorIngressosRepassado = valorIngressosRepassado;
        this.saldoAluguel = saldoAluguel;
        this.status = Status.ENCERRADO;
    }
}