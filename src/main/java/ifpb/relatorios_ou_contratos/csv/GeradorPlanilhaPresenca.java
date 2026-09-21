package ifpb.relatorios_ou_contratos.csv;

import ifpb.modelo.ingresso.ControlePresenca;
import ifpb.modelo.proposta_de_aluguel.PeriodoExibicaoPeca;
import ifpb.modelo.proposta_de_aluguel.PropostaDeAluguel;
import ifpb.telas.Sessao;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class GeradorPlanilhaPresenca {
    public static String gerarPlanilhaPorData(PropostaDeAluguel propostaDeAluguel, LocalDate dataDaPeca, List<ControlePresenca> listaPresenca) throws IOException {
        String nomeArquivo = "lista_presenca_" + propostaDeAluguel.getId() + "_" + dataDaPeca + ".csv";

        FileWriter arquivo = new FileWriter(nomeArquivo);

        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        arquivo.write("Peça;" + propostaDeAluguel.getPeca().getNome() + "\n\n");
        arquivo.write("Nome;CPF;Data;Quantidade de ingressos\n");

        for (ControlePresenca controle : listaPresenca) {
            arquivo.write(controle.getEspectador().getNome() + ";" + controle.getEspectador().getCpf() + ";" + dataDaPeca.format(formatadorData) + ";" + controle.getQuantidadeIngressos() + "\n");
        }

        arquivo.close();

        return nomeArquivo;
    }

    public static String gerarPlanilhaGeral(PropostaDeAluguel propostaDeAluguel) throws IOException {
        String nomeArquivo = "lista_presenca_geral_" + propostaDeAluguel.getId() + ".csv";

        FileWriter arquivo = new FileWriter(nomeArquivo);

        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        arquivo.write("Peça;" + propostaDeAluguel.getPeca().getNome() + "\n\n");
        arquivo.write("Nome;CPF;Data;Quantidade de ingressos\n");

        for (PeriodoExibicaoPeca periodo : propostaDeAluguel.getPeriodosDeTempoExibicao()) {
            LocalDate diaAtual = periodo.getPeriodoDeTempo().getDataInicio();
            LocalDate dataFim = periodo.getPeriodoDeTempo().getDataFim();

            while (!diaAtual.isAfter(dataFim)) {
                List<ControlePresenca> listaPresenca = Sessao.getDaoVendaDeIngresso().gerarListaPresencaPorDataParaProposta(propostaDeAluguel, diaAtual);

                for (ControlePresenca controle : listaPresenca) {
                    arquivo.write(controle.getEspectador().getNome() + ";" + controle.getEspectador().getCpf() + ";" + diaAtual.format(formatadorData) + ";" + controle.getQuantidadeIngressos() + "\n");
                }

                diaAtual = diaAtual.plusDays(1);
            }
        }

        arquivo.close();

        return nomeArquivo;
    }
}
