package ifpb.dao;

import ifpb.excecoes.EmailJaCadastradoException;
import ifpb.excecoes.EmailNaoExistenteException;
import ifpb.excecoes.SenhaIncorretaException;
import ifpb.modelo.usuario.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DaoUsuario {
    private final EntityManager entityManager;

    public void salvar(Usuario usuario) throws EmailJaCadastradoException {
        if (existeUsuarioComEmail(usuario.getEmail())) {
            throw new EmailJaCadastradoException(usuario.getEmail());
        }

        entityManager.getTransaction().begin();
        entityManager.persist(usuario);
        entityManager.getTransaction().commit();
    }

    public Usuario buscarPorID(Long id) {
        return entityManager.find(Usuario.class, id);
    }

    public List<Usuario> buscarTodos() {
        TypedQuery<Usuario> typedQuery = entityManager.createQuery("select usuario from Usuario usuario", Usuario.class);

        return typedQuery.getResultList();
    }

    public Usuario buscarPorEmail(String email) throws EmailNaoExistenteException {
        TypedQuery<Usuario> typedQuery = entityManager.createQuery("select usuario from Usuario usuario where usuario.email = :email", Usuario.class);

        typedQuery.setParameter("email", email);

        List<Usuario> usuarios = typedQuery.getResultList();

        if (usuarios.isEmpty()) {
            throw new EmailNaoExistenteException(email);
        }

        return usuarios.get(0);
    }

    public boolean existeUsuarioComEmail(String email) {
        TypedQuery<Long> typedQuery = entityManager.createQuery("select count(usuario) from Usuario usuario where usuario.email = :email", Long.class);
        typedQuery.setParameter("email", email);

        return typedQuery.getSingleResult() > 0;
    }

    public boolean existeUsuarioCadastrado() {
        TypedQuery<Long> typedQuery = entityManager.createQuery("select count(usuario) from Usuario usuario", Long.class);

        return typedQuery.getSingleResult() > 0;
    }

    public void autenticarCredenciais(String email, String senha) throws EmailNaoExistenteException, SenhaIncorretaException {
        Usuario usuario = buscarPorEmail(email);

        if (!usuario.getSenha().equals(senha)) {
            throw new SenhaIncorretaException();
        }
    }

    public void alterarSenha(Usuario usuario, String novaSenha) {
        usuario.setSenha(novaSenha);

        entityManager.getTransaction().begin();
        entityManager.merge(usuario);
        entityManager.getTransaction().commit();
    }
}