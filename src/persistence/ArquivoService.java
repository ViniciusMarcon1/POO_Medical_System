package persistence;

import exceptions.ArquivoInvalidoException;
import exceptions.PersistenciaException;
import model.Consulta;
import model.Medico;
import model.Medico.Especialidade;
import model.Paciente;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço de leitura de arquivos CSV e persistência binária.
 */
public class ArquivoService {

    public List<Consulta> readCsv(String path) throws ArquivoInvalidoException {
        List<Consulta> consultas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String header = br.readLine();
            if (header == null || !header.contains("CRM")) {
                throw new ArquivoInvalidoException("Cabeçalho CSV inválido ou ausente.");
            }

            String line;
            int linha = 1;

            while ((line = br.readLine()) != null) {
                linha++;
                String[] campos = line.split(",");

                if (campos.length < 7) {
                    throw new ArquivoInvalidoException("Linha " + linha + ": número insuficiente de colunas.");
                }

                try {
                    String crm = campos[0].trim();
                    String nomeMedico = campos[1].trim();
                    String especialidadeStr = campos[2].trim();
                    String cpf = campos[3].trim();
                    String nomePaciente = campos[4].trim();
                    String dataNascStr = campos[5].trim();
                    String dataConsultaStr = campos[6].trim();

                    Especialidade especialidade = Especialidade.valueOf(especialidadeStr.toUpperCase());
                    LocalDate dataNascimento = LocalDate.parse(dataNascStr);
                    LocalDate dataConsulta = LocalDate.parse(dataConsultaStr);

                    Medico medico = new Medico(crm, nomeMedico, especialidade);
                    Paciente paciente = new Paciente(cpf, nomePaciente, dataNascimento);
                    Consulta consulta = new Consulta(medico, paciente, dataConsulta);

                    consultas.add(consulta);

                } catch (IllegalArgumentException | DateTimeParseException e) {
                    throw new ArquivoInvalidoException("Linha " + linha + ": dados inválidos.", e);
                }
            }

        } catch (IOException e) {
            throw new ArquivoInvalidoException("Erro ao ler o arquivo CSV.", e);
        }

        return consultas;
    }

    public void writeBinary(List<Consulta> consultas, String path) throws PersistenciaException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(consultas);
        } catch (IOException e) {
            throw new PersistenciaException("Erro ao gravar arquivo binário.", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Consulta> readBinary(String path) throws PersistenciaException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (List<Consulta>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao ler arquivo binário.", e);
        }
    }
}