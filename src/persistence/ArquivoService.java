// src/persistence/ArquivoService.java
package persistence;

import exceptions.ArquivoInvalidoException;
import exceptions.PersistenciaException;
import model.Consulta;
import model.Medico;
import model.Paciente;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class ArquivoService {
    private char delimiter;
    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter timeFormatter;
    private Map<String, Medico> medicosMap;
    private Map<String, Paciente> pacientesMap;

    public ArquivoService() throws PersistenciaException {
        Properties props = new Properties();
        // Carregar propriedades
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (in == null) {
                throw new PersistenciaException("application.properties não encontrado no classpath");
            }
            props.load(in);
            this.delimiter = props.getProperty("csv.delimitador").charAt(0);
            this.dateFormatter = DateTimeFormatter.ofPattern(props.getProperty("formato.data"));
            this.timeFormatter = DateTimeFormatter.ofPattern(props.getProperty("formato.hora"));
        } catch (IOException e) {
            throw new PersistenciaException("Erro ao carregar application.properties", e);
        }
        // Carregar médicos
        try {
            medicosMap = readMedicos(props.getProperty("medicos.csv"))
                .stream().collect(Collectors.toMap(Medico::getCrm, m -> m));
        } catch (ArquivoInvalidoException e) {
            throw new PersistenciaException("Erro ao carregar medicos.csv: " + e.getMessage(), e);
        }
        // Carregar pacientes
        try {
            pacientesMap = readPacientes(props.getProperty("pacientes.csv"))
                .stream().collect(Collectors.toMap(Paciente::getCpf, p -> p));
        } catch (ArquivoInvalidoException e) {
            throw new PersistenciaException("Erro ao carregar pacientes.csv: " + e.getMessage(), e);
        }
    }

    private List<Medico> readMedicos(String path) throws ArquivoInvalidoException {
        List<Medico> medicos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            reader.readLine(); // pular cabeçalho
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(Character.toString(delimiter));
                if (fields.length != 3) {
                    throw new ArquivoInvalidoException("Linha inválida em medicos.csv: " + line);
                }
                String crm = fields[0];
                String nome = fields[1];
                Medico.Especialidade esp;
                try {
                    esp = Medico.Especialidade.valueOf(fields[2]);
                } catch (IllegalArgumentException ex) {
                    throw new ArquivoInvalidoException("Especialidade inválida em medicos.csv: " + line);
                }
                medicos.add(new Medico(nome, crm, esp));
            }
        } catch (IOException e) {
            throw new ArquivoInvalidoException("Erro ao ler medicos.csv: " + e.getMessage());
        }
        return medicos;
    }

    private List<Paciente> readPacientes(String path) throws ArquivoInvalidoException {
        List<Paciente> pacientes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            reader.readLine(); // pular cabeçalho
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(Character.toString(delimiter));
                if (fields.length != 3) {
                    throw new ArquivoInvalidoException("Linha inválida em pacientes.csv: " + line);
                }
                String cpf = fields[0];
                String nome = fields[1];
                LocalDate dataNasc;
                try {
                    dataNasc = LocalDate.parse(fields[2], dateFormatter);
                } catch (DateTimeParseException ex) {
                    throw new ArquivoInvalidoException("Data de nascimento inválida em pacientes.csv: " + line);
                }
                pacientes.add(new Paciente(nome, cpf, dataNasc));
            }
        } catch (IOException e) {
            throw new ArquivoInvalidoException("Erro ao ler pacientes.csv: " + e.getMessage());
        }
        return pacientes;
    }

    public List<Consulta> readCsv(String path) throws ArquivoInvalidoException {
        List<Consulta> consultas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            reader.readLine(); // pular cabeçalho
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(Character.toString(delimiter));
                if (fields.length != 4) {
                    throw new ArquivoInvalidoException("Linha CSV inválida: " + line);
                }
                String crm = fields[0];
                String cpf = fields[1];
                LocalDate data;
                LocalTime horario;
                try {
                    data = LocalDate.parse(fields[2], dateFormatter);
                    horario = LocalTime.parse(fields[3], timeFormatter);
                } catch (DateTimeParseException e) {
                    throw new ArquivoInvalidoException("Formato de data/hora inválido na linha: " + line);
                }
                Medico medico = medicosMap.get(crm);
                if (medico == null) {
                    throw new ArquivoInvalidoException("CRM desconhecido em consultas.csv: " + crm);
                }
                Paciente paciente = pacientesMap.get(cpf);
                if (paciente == null) {
                    throw new ArquivoInvalidoException("CPF desconhecido em consultas.csv: " + cpf);
                }
                consultas.add(new Consulta(medico, paciente, data, horario));
            }
        } catch (IOException e) {
            throw new ArquivoInvalidoException("Erro ao ler CSV: " + e.getMessage());
        }
        return consultas;
    }

    public void writeBinary(List<Consulta> lista, String path) throws PersistenciaException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(lista);
        } catch (IOException e) {
            throw new PersistenciaException("Erro ao escrever arquivo binário", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Consulta> readBinary(String path) throws PersistenciaException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            return (List<Consulta>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao ler arquivo binário", e);
        }
    }
}
