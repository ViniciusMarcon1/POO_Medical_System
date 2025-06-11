package model;

import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Consulta extends Pessoa implements Exportavel, Serializable {
    private static final long serialVersionUID = 1L;

    private Medico medico;
    private Paciente paciente;
    private LocalDate data;
    private LocalTime horario;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final char DELIMITER;

    static {
        char d = ',';
        try (InputStream in = Consulta.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                d = props.getProperty("csv.delimitador").charAt(0);
            }
        } catch (IOException e) {
            // mantém vírgula se falhar
        }
        DELIMITER = d;
    }

    public Consulta() {}

    public Consulta(Medico medico, Paciente paciente, LocalDate data, LocalTime horario) {
        super();
        this.medico = medico;
        this.paciente = paciente;
        this.data = data;
        this.horario = horario;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public String getResumo() {
        return String.format("CRM:%s | Paciente:%s | Data:%s Hora:%s",
            medico.getCrm(),
            paciente.getCpf(),
            data.format(DATE_FORMATTER),
            horario.format(TIME_FORMATTER)
        );
    }

    @Override
    public String toCsv() {
        return String.join(Character.toString(DELIMITER),
            medico.getCrm(),
            paciente.getCpf(),
            data.format(DATE_FORMATTER),
            horario.format(TIME_FORMATTER)
        );
    }
}
