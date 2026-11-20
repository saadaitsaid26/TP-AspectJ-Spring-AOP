package ma.enset.tpaop.aspectj.metier;

import java.util.Objects;

/**
 * Compte bancaire en mémoire, identifié par son code et caractérisé par son solde.
 */
public class Compte {

    private Long code;
    private double solde;

    public Compte(Long code, double solde) {
        this.code = code;
        this.solde = solde;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compte compte)) {
            return false;
        }
        return Objects.equals(code, compte.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "Compte{code=" + code + ", solde=" + solde + '}';
    }
}
