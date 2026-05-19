package cat.urv.deim;

import java.util.Objects;

public class Artista implements Comparable<Artista> {
    private final int id;
    private final String artista;

    public Artista(int id, String artista) {
        this.id = id;
        this.artista = artista;
    }

    public int getId() {
        return id;
    }

    public String getArtista() {
        return artista;
    }

    @Override
    public int compareTo(Artista altra) {
        int comparacio = artista.compareTo(altra.artista);
        if (comparacio != 0) {
            return comparacio;
        }
        return Integer.compare(id, altra.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Artista canco)) {
            return false;
        }
        return id == canco.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Canco{id=" + id +
                ", artista='" + artista +
                '}';
    }
}
