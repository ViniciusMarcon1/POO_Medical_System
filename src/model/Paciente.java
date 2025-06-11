package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Paciente extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    private String cpf;
    private LocalDate dataNascimento;

    public Paciente() { super(); }

    public Paciente(String nome, String cpf, LocalDate dataNascimento) {
        super(nome);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paciente)) return false;
        Paciente paciente = (Paciente) o;
        return Objects.equals(cpf, paciente.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }
}
