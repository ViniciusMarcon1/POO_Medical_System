package model;

import java.io.Serializable;
import java.util.Objects;

public class Medico extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    private String crm;
    private Especialidade especialidade;

    public enum Especialidade {
        CLINICO_GERAL,
        PEDIATRA,
        CARDIOLOGISTA,
        DERMATOLOGISTA,
        ORTOPEDISTA,
        GINECOLOGISTA,
        PSIQUIATRA
    }

    public Medico() { super(); }

    public Medico(String nome, String crm, Especialidade especialidade) {
        super(nome);
        this.crm = crm;
        this.especialidade = especialidade;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medico)) return false;
        Medico medico = (Medico) o;
        return Objects.equals(crm, medico.crm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crm);
    }
}
