package ifpb.dao;

import ifpb.enumeradores.Status;
import ifpb.excecoes.NovoIntervaloGerandoConflitoException;
import ifpb.modelo.intervalos.IntervaloDatas;
import ifpb.modelo.intervalos.IntervaloHorarios;
import ifpb.modelo.proposta_de_aluguel.ControleValorDiarioAluguel;
import ifpb.modelo.proposta_de_aluguel.PeriodoExibicaoPeca;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
public class DaoPropostaDeAluguel {
    private final EntityManager entityManager;

    public void salvar(PropostaDeAluguel proposta) {
        entityManager.getTransaction().begin();
        entityManager.persist(proposta);
        entityManager.getTransaction().commit();
    }

    public PropostaDeAluguel buscarPorID(Long id) {
        return entityManager.find(PropostaDeAluguel.class, id);
    }

    public List<PropostaDeAluguel> buscarTodos() {
        TypedQuery<PropostaDeAluguel> typedQuery = entityManager.createQuery("select proposta from PropostaDeAluguel proposta", PropostaDeAluguel.class);

        return typedQuery.getResultList();
    }

    public void atualizar(PropostaDeAluguel proposta) {
        entityManager.getTransaction().begin();
        entityManager.merge(proposta);
        entityManager.getTransaction().commit();
    }

    public void remover(PropostaDeAluguel proposta) {
        entityManager.getTransaction().begin();
        entityManager.remove(proposta);
        entityManager.getTransaction().commit();
    }

    public List<PropostaDeAluguel> buscarPorCpfLocatario(String cpf) {
        TypedQuery<PropostaDeAluguel> typedQuery = entityManager.createQuery("select proposta from PropostaDeAluguel proposta where proposta.locatario.cpf = :cpf", PropostaDeAluguel.class);
        typedQuery.setParameter("cpf", cpf);

        return typedQuery.getResultList();
    }

    public List<PropostaDeAluguel> buscarAtivas() {
        TypedQuery<PropostaDeAluguel> typedQuery = entityManager.createQuery("select proposta from PropostaDeAluguel proposta where proposta.status = :statusAtivo or proposta.status = :statusAlterado", PropostaDeAluguel.class);
        typedQuery.setParameter("statusAtivo", Status.ATIVO);
        typedQuery.setParameter("statusAlterado", Status.CONTRATADO_COM_ALTERACAO);

        return typedQuery.getResultList();
    }

    public void verificarConflitoComPeriodosExistentes(PeriodoExibicaoPeca periodoNovo) throws NovoIntervaloGerandoConflitoException {
        LocalDate dataInicioNovo = periodoNovo.getPeriodoDeTempo().getDataInicio();
        LocalDate dataFimNovo = periodoNovo.getPeriodoDeTempo().getDataFim();

        TypedQuery<PeriodoExibicaoPeca> typedQuery = entityManager.createQuery("select periodo from PropostaDeAluguel proposta join proposta.periodosDeTempoExibicao periodo where proposta.status <> :statusEncerrado and periodo.periodoDeTempo.dataInicio <= :dataFimNovo and periodo.periodoDeTempo.dataFim >= :dataInicioNovo", PeriodoExibicaoPeca.class);
        typedQuery.setParameter("statusEncerrado", Status.ENCERRADO);
        typedQuery.setParameter("dataInicioNovo", dataInicioNovo);
        typedQuery.setParameter("dataFimNovo", dataFimNovo);

        List<PeriodoExibicaoPeca> periodosConflitantesPorData = typedQuery.getResultList();

        for (PeriodoExibicaoPeca periodoExistente : periodosConflitantesPorData) {
            IntervaloHorarios intervaloHorarioPeriodoNovo = periodoNovo.getIntervaloDeOcupacaoDoTeatro();
            IntervaloHorarios intervaloHorarioPeriodoExistente = periodoExistente.getIntervaloDeOcupacaoDoTeatro();

            if (intervaloHorarioPeriodoNovo.temConflitoCom(intervaloHorarioPeriodoExistente)) {
                throw new NovoIntervaloGerandoConflitoException();
            }
        }
    }

    public BigDecimal calcularValorTotalDeAluguelDoTeatroPorIntervalo(IntervaloDatas intervaloDatas) {
        BigDecimal total = BigDecimal.ZERO;

        List<PropostaDeAluguel> propostas = buscarTodos();

        for (PropostaDeAluguel proposta : propostas) {
            if (proposta.getStatus() == Status.EM_AVALIACAO) {
                continue;
            }

            for (PeriodoExibicaoPeca periodo : proposta.getPeriodosDeTempoExibicao()) {
                for (ControleValorDiarioAluguel controle : periodo.getListaControleValorDiarioAluguel()) {
                    LocalDate data = controle.getDia();

                    boolean estaNoPeriodo = !data.isBefore(intervaloDatas.getDataInicio()) && !data.isAfter(intervaloDatas.getDataFim());

                    if (!estaNoPeriodo) {
                        continue;
                    }

                    if (proposta.getStatus() == Status.ENCERRADO && proposta.getDataEncerramento() != null && data.isAfter(proposta.getDataEncerramento())) {
                        continue;
                    }

                    total = total.add(controle.getValor());
                }
            }
        }

        return total;
    }
}