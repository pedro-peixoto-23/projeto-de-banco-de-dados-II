package ifpb.dao;

import ifpb.modelo.regra_de_preco.RegraDePreco;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DaoRegraDePreco {
    private final EntityManager entityManager;

    public void salvar(RegraDePreco regraDePreco) {
        entityManager.getTransaction().begin();
        entityManager.persist(regraDePreco);
        entityManager.getTransaction().commit();
    }

    public RegraDePreco buscarPorID(Long id) {
        return entityManager.find(RegraDePreco.class, id);
    }

    public List<RegraDePreco> buscarTodos() {
        TypedQuery<RegraDePreco> typedQuery = entityManager.createQuery("select regraDePreco from RegraDePreco regraDePreco", RegraDePreco.class);
        return typedQuery.getResultList();
    }

    public void atualizar(RegraDePreco regraDePreco) {
        entityManager.getTransaction().begin();
        entityManager.merge(regraDePreco);
        entityManager.getTransaction().commit();
    }

    public void remover(RegraDePreco regraDePreco) {
        entityManager.getTransaction().begin();
        entityManager.remove(regraDePreco);
        entityManager.getTransaction().commit();
    }

    /**
     * Verifica se já existe uma regra de preço com a mesma configuração
     * da regra informada.
     *
     * @param regraDePreco regra de preço que será comparada com as regras cadastradas.
     * @return true se existir uma regra com a mesma configuração, false caso contrário.
     */
    public boolean existeRegraComMesmaConfiguracao(RegraDePreco regraDePreco) {
        List<RegraDePreco> regrasExistentes = buscarTodos();

        for (RegraDePreco regra : regrasExistentes) {
            if (regraDePreco.temMesmaConfiguracao(regra)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Conta a quantidade de regras de preço definidas apenas pelo valor por hora,
     * sem restrições de ano, mês, dia da semana, turno ou intervalo de horário.
     *
     * @return quantidade de regras de preço baseadas somente no valor por hora.
     */
    public int contarRegrasPorValor() {
        TypedQuery<Long> typedQuery = entityManager.createQuery(
            "select count(regra) from RegraDePreco regra where regra.ano is null and regra.mes is null and regra.diaDaSemana is null and regra.turno is null and regra.intervaloDeHorario.horarioInicio is null and regra.intervaloDeHorario.horarioFim is null ",
            Long.class
        );

        return typedQuery.getSingleResult().intValue();
    }
}
