package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa um médico com CRM, nome e especialidade.
 */
public class Medico implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Especialidade {
        CLINICO_GERAL,
        PEDIATRA,
        CARDIOLOGISTA,
        DERMATOLOGISTA,
        ORTOPEDISTA,
        GINECOLOGISTA,
        PSIQUIATRA
    }

    private String crm;
    private String nome;
    private Especialidade especialidade;

    public Medico(String crm, String nome, Especialidade especialidade) {
        this.crm = crm;
        this.nome = nome;
        this.especialidade = especialidade;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Medico)) return false;
        Medico other = (Medico) obj;
        return Objects.equals(crm, other.crm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crm);
    }
}