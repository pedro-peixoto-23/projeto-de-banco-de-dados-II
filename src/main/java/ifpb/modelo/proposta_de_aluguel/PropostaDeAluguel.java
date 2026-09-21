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

    public BigDecimal calcularValorTotalAluguel() {
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (PeriodoExibicaoPeca periodo : periodosDeTempoExibicao) {
            valorTotal = valorTotal.add(periodo.gerarValorTotalPeriodo());
        }

        return valorTotal.setScale(2, RoundingMode.HALF_UP);
    }

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

    public void adicionarNovoPeriodo(PeriodoExibicaoPeca novoPeriodo) throws NovoIntervaloGerandoConflitoException {
        periodosDeTempoExibicao.add(novoPeriodo);
        status = Status.CONTRATADO_COM_ALTERACAO;
    }

    public PeriodoExibicaoPeca buscarPeriodoPorData(LocalDate data) {
        for (PeriodoExibicaoPeca periodo : periodosDeTempoExibicao) {
            if (!data.isBefore(periodo.getPeriodoDeTempo().getDataInicio()) && !data.isAfter(periodo.getPeriodoDeTempo().getDataFim())) {
                return periodo;
            }
        }

        return null;
    }

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

    public void registrarEncerramento(LocalDate dataEncerramento, BigDecimal valorIngressosRepassado, BigDecimal saldoAluguel) {
        this.dataEncerramento = dataEncerramento;
        this.valorIngressosRepassado = valorIngressosRepassado;
        this.saldoAluguel = saldoAluguel;
        this.status = Status.ENCERRADO;
    }
}