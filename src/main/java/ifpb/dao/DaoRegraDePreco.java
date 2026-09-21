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

    public boolean existeRegraComMesmaConfiguracao(RegraDePreco regraDePreco) {
        List<RegraDePreco> regrasExistentes = buscarTodos();

        for (RegraDePreco regra : regrasExistentes) {
            if (regraDePreco.temMesmaConfiguracao(regra)) {
                return true;
            }
        }

        return false;
    }

    public int contarRegrasPorValor() {
        TypedQuery<Long> typedQuery = entityManager.createQuery(
            "select count(regra) from RegraDePreco regra where regra.ano is null and regra.mes is null and regra.diaDaSemana is null and regra.turno is null and regra.intervaloDeHorario.horarioInicio is null and regra.intervaloDeHorario.horarioFim is null ",
            Long.class
        );

        return typedQuery.getSingleResult().intValue();
    }
}
