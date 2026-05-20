package cat.urv.deim;

import java.util.Objects;

public class Usuari implements Comparable<Usuari> {
    private final String sha;
    private final String genere;
    private final int edat;
    private final String pais;
    private final String naixement;

    public Usuari(String sha, String genere, int edat, String pais, String naixement) {
        this.sha = sha;
        this.genere = genere;
        this.edat = edat;
        this.pais = pais;
        this.naixement = naixement;
    }

    public String getSha() {
        return sha;
    }

    public String getGenere() {
        return genere;
    }

    public int getEdat() {
        return edat;
    }

    public String getPais() {
        return pais;
    }

    public String getNaixement() {
        return naixement;
    }

    @Override
    public int compareTo(Usuari altre) {
        return sha.compareTo(altre.sha);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuari usuari)) {
            return false;
        }
        return sha == usuari.sha && Objects.equals(sha, usuari.sha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sha);
    }

    @Override
    public String toString() {
        return "Usuari{sha=" + sha +
                ", genere='" + genere + '\'' +
                ", edat=" + edat + '\'' +
                ", pais='" + pais + '\'' +
                ", naixement='" + naixement +
                '}';
    }
}
