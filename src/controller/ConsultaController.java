package controller;

import exceptions.ArquivoInvalidoException;
import exceptions.PersistenciaException;
import exceptions.BuscaInvalidaException;
import model.Consulta;
import model.Exportavel;
import persistence.ArquivoService;
import utils.CsvWriter;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConsultaController {
    private List<Consulta> consultas = new ArrayList<>();
    private final ArquivoService service;

    public ConsultaController() {
        try {
            service = new ArquivoService();
        } catch (PersistenciaException e) {
            throw new RuntimeException("Falha ao inicializar ArquivoService", e);
        }
    }

    public void loadData(String binPath) throws PersistenciaException {
        try {
            consultas = service.readBinary(binPath);
        } catch (PersistenciaException e) {
            // Sem binário, carrega CSV e grava binário
            try {
                Properties props = new Properties();
                try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties")) {
                    if (in == null) throw new IOException("application.properties não encontrado");
                    props.load(in);
                }
                String csvPath = props.getProperty("consultas.csv");
                consultas = service.readCsv(csvPath);
                service.writeBinary(consultas, binPath);
            } catch (IOException | ArquivoInvalidoException ex) {
                throw new PersistenciaException("Erro ao carregar dados: " + ex.getMessage(), ex);
            }
        }
    }

    public List<Consulta> buscar(String crm, String cpf,
                                 LocalDate dataInicial, LocalDate dataFinal,
                                 Integer mesesSemConsulta) throws BuscaInvalidaException {
        if ((crm == null || crm.isBlank()) && (cpf == null || cpf.isBlank())
                && dataInicial == null && dataFinal == null && mesesSemConsulta == null) {
            throw new BuscaInvalidaException("Pelo menos um parâmetro deve ser informado");
        }
        LocalDate hoje = LocalDate.now();
        return consultas.stream()
                .filter(c -> crm == null || crm.isBlank() || c.getMedico().getCrm().equals(crm))
                .filter(c -> cpf == null || cpf.isBlank() || c.getPaciente().getCpf().equals(cpf))
                .filter(c -> dataInicial == null || !c.getData().isBefore(dataInicial))
                .filter(c -> dataFinal == null || !c.getData().isAfter(dataFinal))
                .filter(c -> mesesSemConsulta == null || c.getData().isBefore(hoje.minusMonths(mesesSemConsulta)))
                .sorted(Comparator.comparing(Consulta::getData)
                                  .thenComparing(Consulta::getHorario))
                .collect(Collectors.toList());
    }

    public void exportarCsv(List<Consulta> resultado, String outPath) throws IOException {
        List<Exportavel> exportList = new ArrayList<>(resultado);
        new CsvWriter().export(exportList, outPath);
    }

    public List<Consulta> getAllConsultas() {
        return new ArrayList<>(consultas);
    }

    public void consolidarRelatorio(List<List<Consulta>> resultadosParciais, String relatorioPath) throws IOException {
        List<Exportavel> exportList = resultadosParciais.stream()
                .flatMap(List::stream)
                .map(c -> (Exportavel) c)
                .collect(Collectors.toList());
        new CsvWriter().export(exportList, relatorioPath);
    }
}
