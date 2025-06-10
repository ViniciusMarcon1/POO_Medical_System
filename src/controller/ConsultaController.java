package controller;

import exceptions.BuscaInvalidaException;
import exceptions.PersistenciaException;
import model.Consulta;
import persistence.ArquivoService;
import utils.CsvWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador responsável por carregar dados, buscar e exportar resultados.
 */
public class ConsultaController {

    private final ArquivoService arquivoService;
    private final CsvWriter csvWriter;
    private List<Consulta> todasConsultas;

    public ConsultaController() {
        this.arquivoService = new ArquivoService();
        this.csvWriter = new CsvWriter();
        this.todasConsultas = new ArrayList<>();
    }

    /**
     * Carrega a lista de consultas salvas em binário.
     *
     * @param binPath caminho do arquivo binário
     * @throws PersistenciaException em caso de erro de leitura
     */
    public void loadData(String binPath) throws PersistenciaException {
        this.todasConsultas = arquivoService.readBinary(binPath);
    }

    /**
     * Filtra a lista de consultas com base nos critérios informados.
     *
     * @param nomeMedico nome do médico (pode ser nulo ou vazio)
     * @param nomePaciente nome do paciente (pode ser nulo ou vazio)
     * @param data consulta na data exata (pode ser nula)
     * @return lista filtrada de consultas
     * @throws BuscaInvalidaException se nenhum filtro for informado
     */
    public List<Consulta> buscar(String nomeMedico, String nomePaciente, LocalDate data)
            throws BuscaInvalidaException {

        if ((nomeMedico == null || nomeMedico.isBlank()) &&
                (nomePaciente == null || nomePaciente.isBlank()) &&
                data == null) {
            throw new BuscaInvalidaException("Pelo menos um filtro deve ser informado.");
        }

        List<Consulta> resultado = new ArrayList<>();

        for (Consulta c : todasConsultas) {
            boolean match = true;

            if (nomeMedico != null && !nomeMedico.isBlank()) {
                match &= c.getMedico().getNome().toLowerCase().contains(nomeMedico.toLowerCase());
            }

            if (nomePaciente != null && !nomePaciente.isBlank()) {
                match &= c.getPaciente().getNome().toLowerCase().contains(nomePaciente.toLowerCase());
            }

            if (data != null) {
                match &= c.getData().equals(data);
            }

            if (match) {
                resultado.add(c);
            }
        }

        return resultado;
    }

    /**
     * Exporta uma lista de consultas para arquivo CSV.
     *
     * @param resultado lista a exportar
     * @param outPath caminho de saída
     * @throws IOException em caso de falha de escrita
     */
    public void exportarCsv(List<Consulta> resultado, String outPath) throws IOException {
        csvWriter.export(resultado, outPath);
    }
}