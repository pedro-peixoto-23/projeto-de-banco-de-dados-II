package ifpb.dao;

import ifpb.excecoes.PessoaJaCadastradaException;
import ifpb.excecoes.PessoaNaoExistenteException;
import ifpb.modelo.pessoa.Pessoa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DaoPessoa {
    private final EntityManager entityManager;

    public void salvar(Pessoa pessoa) throws PessoaJaCadastradaException {
        if (existePessoaComCPF(pessoa.getCpf())) {
            throw new PessoaJaCadastradaException();
        }

        entityManager.getTransaction().begin();
        entityManager.persist(pessoa);
        entityManager.getTransaction().commit();
    }

    public Pessoa buscarPorID(Long id) {
        return entityManager.find(Pessoa.class, id);
    }

    public List<Pessoa> buscarTodos() {
        TypedQuery<Pessoa> typedQuery = entityManager.createQuery(
                "select pessoa from Pessoa pessoa",
                Pessoa.class
        );

        return typedQuery.getResultList();
    }

    public Pessoa buscarPorCPF(String cpf) throws PessoaNaoExistenteException {
        TypedQuery<Pessoa> typedQuery = entityManager.createQuery(
                "select pessoa from Pessoa pessoa where pessoa.cpf = :cpf",
                Pessoa.class
        );
        typedQuery.setParameter("cpf", cpf);

        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException exception) {
            throw new PessoaNaoExistenteException();
        }
    }

    public boolean existePessoaComCPF(String cpf) {
        TypedQuery<Long> typedQuery = entityManager.createQuery(
                "select count(pessoa) from Pessoa pessoa where pessoa.cpf = :cpf",
                Long.class
        );
        typedQuery.setParameter("cpf", cpf);

        return typedQuery.getSingleResult() > 0;
    }

    public void atualizar(Pessoa pessoa) {
        entityManager.getTransaction().begin();
        entityManager.merge(pessoa);
        entityManager.getTransaction().commit();
    }

    public void remover(Pessoa pessoa) {
        entityManager.getTransaction().begin();
        entityManager.remove(pessoa);
        entityManager.getTransaction().commit();
    }
}
