package ifpb.dao;

import ifpb.enumeradores.Status;
import ifpb.modelo.ingresso.ControlePresenca;
import ifpb.modelo.ingresso.VendaDeIngresso;
import ifpb.modelo.intervalos.IntervaloDatas;
import ifpb.modelo.proposta_de_aluguel.ControleFinanceiroProposta;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class DaoVendaDeIngresso {
    private final EntityManager entityManager;

    public void salvar(VendaDeIngresso venda) {
        entityManager.getTransaction().begin();
        entityManager.persist(venda);
        entityManager.getTransaction().commit();
    }

    public VendaDeIngresso buscarPorID(Long id) {
        return entityManager.find(VendaDeIngresso.class, id);
    }

    public List<VendaDeIngresso> buscarTodos() {
        TypedQuery<VendaDeIngresso> typedQuery = entityManager.createQuery("select venda from VendaDeIngresso venda", VendaDeIngresso.class);

        return typedQuery.getResultList();
    }

    public void atualizar(VendaDeIngresso venda) {
        entityManager.getTransaction().begin();
        entityManager.merge(venda);
        entityManager.getTransaction().commit();
    }

    public void remover(VendaDeIngresso venda) {
        entityManager.getTransaction().begin();
        entityManager.remove(venda);
        entityManager.getTransaction().commit();
    }

    public List<VendaDeIngresso> buscarPorProposta(PropostaDeAluguel proposta) {
        TypedQuery<VendaDeIngresso> typedQuery = entityManager.createQuery("select venda from VendaDeIngresso venda where venda.propostaDeAluguel = :proposta", VendaDeIngresso.class);
        typedQuery.setParameter("proposta", proposta);

        return typedQuery.getResultList();
    }

    public List<VendaDeIngresso> buscarPorPropostaEData(PropostaDeAluguel proposta, LocalDate dataDaPeca) {
        TypedQuery<VendaDeIngresso> typedQuery = entityManager.createQuery("select venda from VendaDeIngresso venda where venda.propostaDeAluguel = :proposta and venda.dataDaPeca = :dataDaPeca", VendaDeIngresso.class);
        typedQuery.setParameter("proposta", proposta);
        typedQuery.setParameter("dataDaPeca", dataDaPeca);

        return typedQuery.getResultList();
    }

    public boolean existeVendaAposData(PropostaDeAluguel proposta, LocalDate dataEncerramento) {
        TypedQuery<Long> typedQuery = entityManager.createQuery("select count(venda) from VendaDeIngresso venda where venda.propostaDeAluguel = :proposta and venda.dataDaPeca > :dataEncerramento", Long.class);
        typedQuery.setParameter("proposta", proposta);
        typedQuery.setParameter("dataEncerramento", dataEncerramento);

        return typedQuery.getSingleResult() > 0;
    }

    public List<ControlePresenca> gerarListaPresencaPorDataParaProposta(PropostaDeAluguel proposta, LocalDate dataDaPeca) {
        List<VendaDeIngresso> vendas = buscarPorPropostaEData(proposta, dataDaPeca);
        List<ControlePresenca> listaPresenca = new ArrayList<>();

        for (VendaDeIngresso venda : vendas) {
            boolean espectadorJaEstaNaLista = false;

            for (ControlePresenca controle : listaPresenca) {
                if (controle.getEspectador().equals(venda.getEspectador())) {
                    controle.adicionarIngressos(venda.getQuantidade());
                    espectadorJaEstaNaLista = true;
                    break;
                }
            }

            if (!espectadorJaEstaNaLista) {
                ControlePresenca controlePresenca = new ControlePresenca(venda.getEspectador(), venda.getQuantidade());
                listaPresenca.add(controlePresenca);
            }
        }

        return listaPresenca;
    }

    public ControleFinanceiroProposta gerarControleFinanceiroProposta(PropostaDeAluguel proposta) {
        BigDecimal totalArrecadado = BigDecimal.ZERO;

        List<VendaDeIngresso> vendas = buscarPorProposta(proposta);

        for (VendaDeIngresso venda : vendas) {
            totalArrecadado = totalArrecadado.add(venda.calcularValorTotal());
        }

        BigDecimal valorAluguel;

        if (proposta.getStatus() == Status.ENCERRADO && proposta.getDataEncerramento() != null) {
            valorAluguel = proposta.calcularValorAluguelAteData(proposta.getDataEncerramento());
        } else {
            valorAluguel = proposta.calcularValorTotalAluguel();
        }

        BigDecimal valorLiquido = totalArrecadado.subtract(valorAluguel);

        return new ControleFinanceiroProposta(totalArrecadado, valorAluguel, valorLiquido);
    }

    public BigDecimal calcularTotalVendaIngressosDoTeatroPorIntervalo(IntervaloDatas intervaloDatas) {
        BigDecimal total = BigDecimal.ZERO;

        List<VendaDeIngresso> vendas = buscarTodos();

        for (VendaDeIngresso venda : vendas) {
            LocalDate dataDaPeca = venda.getDataDaPeca();

            boolean estaNoPeriodo = !dataDaPeca.isBefore(intervaloDatas.getDataInicio()) && !dataDaPeca.isAfter(intervaloDatas.getDataFim());

            if (estaNoPeriodo) {
                total = total.add(venda.calcularValorTotal());
            }
        }

        return total;
    }
}
