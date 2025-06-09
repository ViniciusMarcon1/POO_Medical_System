package utils;

import model.Consulta;
import model.Medico;
import model.Paciente;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Utilitário para exportar dados de consultas em formato CSV.
 */
public class CsvWriter {

    /**
     * Exporta uma lista de consultas para um arquivo CSV.
     *
     * @param consultas Lista de consultas a exportar
     * @param path Caminho do arquivo de saída
     * @throws IOException Em caso de erro de escrita
     */
    public void export(List<Consulta> consultas, String path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {

            writer.write("CRM,Nome Médico,Especialidade,CPF,Nome Paciente,Data Nascimento,Data Consulta");
            writer.newLine();

            for (Consulta consulta : consultas) {
                Medico m = consulta.getMedico();
                Paciente p = consulta.getPaciente();

                String linha = String.join(",",
                        m.getCrm(),
                        m.getNome(),
                        m.getEspecialidade().name(),
                        p.getCpf(),
                        p.getNome(),
                        p.getDataNascimento().toString(),
                        consulta.getData().toString()
                );

                writer.write(linha);
                writer.newLine();
            }
        }
    }
}