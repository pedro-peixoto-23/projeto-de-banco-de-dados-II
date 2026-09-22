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

    /**
     * Busca as vendas de ingresso associadas a uma proposta de aluguel
     * em uma determinada data de exibição.
     *
     * @param proposta proposta de aluguel relacionada às vendas.
     * @param dataDaPeca data de exibição da peça.
     * @return lista de vendas encontradas para a proposta e a data informadas.
     */
    public List<VendaDeIngresso> buscarPorPropostaEData(PropostaDeAluguel proposta, LocalDate dataDaPeca) {
        TypedQuery<VendaDeIngresso> typedQuery = entityManager.createQuery("select venda from VendaDeIngresso venda where venda.propostaDeAluguel = :proposta and venda.dataDaPeca = :dataDaPeca", VendaDeIngresso.class);
        typedQuery.setParameter("proposta", proposta);
        typedQuery.setParameter("dataDaPeca", dataDaPeca);

        return typedQuery.getResultList();
    }

    /**
     * Verifica se existem vendas de ingresso para uma proposta
     * em datas posteriores à data informada.
     *
     * @param proposta proposta de aluguel que será verificada.
     * @param dataEncerramento data utilizada como limite para a consulta.
     * @return true se existir alguma venda após a data informada, false caso contrário.
     */
    public boolean existeVendaAposData(PropostaDeAluguel proposta, LocalDate dataEncerramento) {
        TypedQuery<Long> typedQuery = entityManager.createQuery("select count(venda) from VendaDeIngresso venda where venda.propostaDeAluguel = :proposta and venda.dataDaPeca > :dataEncerramento", Long.class);
        typedQuery.setParameter("proposta", proposta);
        typedQuery.setParameter("dataEncerramento", dataEncerramento);

        return typedQuery.getSingleResult() > 0;
    }

    /**
     * Gera a lista de presença de uma proposta em uma determinada data,
     * agrupando as vendas por espectador e somando a quantidade de ingressos
     * adquiridos por cada um.
     *
     * @param proposta proposta de aluguel utilizada para gerar a lista.
     * @param dataDaPeca data de exibição da peça.
     * @return lista de controle de presença dos espectadores.
     */
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

    /**
     * Gera o controle financeiro de uma proposta de aluguel,
     * calculando o total arrecadado com ingressos, o valor do aluguel
     * e o valor líquido resultante.
     *
     * Caso a proposta esteja encerrada, o aluguel é calculado somente
     * até a data de encerramento.
     *
     * @param proposta proposta de aluguel utilizada no cálculo.
     * @return controle financeiro contendo total arrecadado, valor do aluguel e valor líquido.
     */
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

    /**
     * Calcula o valor total das vendas de ingressos do teatro
     * cujas datas de exibição estejam dentro do intervalo informado.
     *
     * As datas inicial e final do intervalo são consideradas no cálculo.
     *
     * @param intervaloDatas intervalo de datas utilizado no cálculo.
     * @return valor total das vendas de ingressos no intervalo.
     */
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
