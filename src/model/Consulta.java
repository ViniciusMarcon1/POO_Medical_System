package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa uma consulta médica entre um paciente e um médico.
 */
public class Consulta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Medico medico;
    private Paciente paciente;
    private LocalDate data;

    public Consulta(Medico medico, Paciente paciente, LocalDate data) {
        this.medico = medico;
        this.paciente = paciente;
        this.data = data;
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

    /**
     * Retorna um resumo da consulta no formato:
     * "CRM: {crm} | Paciente: {nome} | Data: {data}"
     */
    public String getResumo() {
        return String.format(
                "CRM: %s | Paciente: %s | Data: %s",
                medico != null ? medico.getCrm() : "N/A",
                paciente != null ? paciente.getNome() : "N/A",
                data != null ? data.toString() : "N/A"
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Consulta)) return false;
        Consulta other = (Consulta) obj;
        return Objects.equals(medico, other.medico)
                && Objects.equals(paciente, other.paciente)
                && Objects.equals(data, other.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medico, paciente, data);
    }
}