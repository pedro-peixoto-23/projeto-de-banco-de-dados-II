package ifpb.telas;

import ifpb.dao.*;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import ifpb.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import lombok.Setter;

public class Sessao {
    private static EntityManager entityManagerRegraPreco;
    private static EntityManager entityManagerPessoa;
    private static EntityManager entityManagerPropostaDeAluguel;
    private static EntityManager entityManagerVendaIngresso;
    private static EntityManager entityManagerUsuario;

    @Getter
    private static DaoRegraDePreco daoRegraDePreco;
    @Getter
    private static DaoPessoa daoPessoa;
    @Getter
    private static DaoPropostaDeAluguel daoPropostaDeAluguel;
    @Getter
    private static DaoVendaDeIngresso daoVendaDeIngresso;
    @Getter
    private static DaoUsuario daoUsuario;
    @Getter
    @Setter
    private static PropostaDeAluguel propostaDeAluguelAtualParaDetalhamento;

    public static void iniciarSessao() {
        entityManagerRegraPreco = JPAUtil.gerarEntityManager();
        entityManagerPessoa = JPAUtil.gerarEntityManager();
        entityManagerPropostaDeAluguel = JPAUtil.gerarEntityManager();
        entityManagerVendaIngresso = JPAUtil.gerarEntityManager();
        entityManagerUsuario = JPAUtil.gerarEntityManager();
        daoRegraDePreco = new DaoRegraDePreco(entityManagerRegraPreco);
        daoPessoa = new DaoPessoa(entityManagerPessoa);
        daoPropostaDeAluguel = new DaoPropostaDeAluguel(entityManagerPropostaDeAluguel);
        daoVendaDeIngresso = new DaoVendaDeIngresso(entityManagerVendaIngresso);
        daoUsuario = new DaoUsuario(entityManagerUsuario);
    }

    public static void encerrarSessao() {
        if (entityManagerRegraPreco != null && entityManagerRegraPreco.isOpen()) {
            entityManagerRegraPreco.close();
        }

        if (entityManagerPessoa != null && entityManagerPessoa.isOpen()) {
            entityManagerPessoa.close();
        }

        if (entityManagerPropostaDeAluguel != null && entityManagerPropostaDeAluguel.isOpen()) {
            entityManagerPropostaDeAluguel.close();
        }

        if (entityManagerVendaIngresso != null && entityManagerVendaIngresso.isOpen()) {
            entityManagerVendaIngresso.close();
        }

        if (entityManagerUsuario != null && entityManagerUsuario.isOpen()) {
            entityManagerUsuario.close();
        }
    }
}
