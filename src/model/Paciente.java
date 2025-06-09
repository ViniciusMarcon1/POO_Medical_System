package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa um paciente com CPF, nome e data de nascimento.
 */
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cpf;
    private String nome;
    private LocalDate dataNascimento;

    public Paciente(String cpf, String nome, LocalDate dataNascimento) {
        this.cpf = cpf;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Paciente)) return false;
        Paciente other = (Paciente) obj;
        return Objects.equals(cpf, other.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }
}