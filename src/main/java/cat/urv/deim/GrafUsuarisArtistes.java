package cat.urv.deim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import cat.urv.deim.exceptions.ArestaNoTrobada;
import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.LlistaBuida;
import cat.urv.deim.exceptions.LlistaPlena;
import cat.urv.deim.exceptions.VertexNoTrobat;

public class GrafUsuarisArtistes {
    private TADGrafBipartit<String, Usuari, String, Artista, Integer> graf;

    public GrafUsuarisArtistes(int mida) {
        this.graf = new GrafBipartit<>(mida);
    }

    public GrafUsuarisArtistes(int mida, String fitxerUsuaris, String fitxerArtEscoltats) {
        this.graf = new GrafBipartit<>(mida);

        try(BufferedReader br = new BufferedReader(new FileReader(fitxerUsuaris))){
            String line;

            while((line = br.readLine()) != null){
                String[] tokens = line.split("\t");

                Usuari usuari;
                if(tokens[2].isEmpty()){
                    usuari = new Usuari(tokens[0], tokens[1], -1,
                                    tokens[3], tokens[3]);
                }
                else{
                    usuari = new Usuari(tokens[0], tokens[1], Integer.parseInt(tokens[2]),
                                    tokens[3], tokens[3]);
                }
                this.graf.inserirVertexEsquerra(usuari.getSha(), usuari);
            }
        }
        catch(FileNotFoundException | ArrayIndexOutOfBoundsException ex){
            throw new IllegalArgumentException();
        }
        catch(IOException e){
            System.err.println("Error reading TSV file: " + e.getMessage());
        }

        try(BufferedReader br = new BufferedReader(new FileReader(fitxerArtEscoltats))){
            String line;

            while((line = br.readLine()) != null){
                String[] tokens = line.split("\t");

                Artista artista = new Artista(tokens[1], tokens[2]);
                this.graf.inserirVertexDret(artista.getId(), artista);

                this.graf.inserirAresta(tokens[0], artista.getId(), Integer.parseInt(tokens[3]));
            }
        }
        catch(FileNotFoundException | ArrayIndexOutOfBoundsException ex){
            throw new IllegalArgumentException();
        }
        catch(IOException | VertexNoTrobat e){
            System.err.println("Error reading TSV file: " + e.getMessage());
        }
    }

    public void inserirUsuari(Usuari usuari) {
        this.graf.inserirVertexEsquerra(usuari.getSha(), usuari);
    }

    public Usuari consultarUsuari(String sha) throws ElementNoTrobat {
        try{
            return this.graf.consultarVertexEsquerra(sha);
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public void esborrarUsuari(String sha) throws ElementNoTrobat {
        try{
            this.graf.esborrarVertexEsquerra(sha);
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public int numUsuaris() {
        return this.graf.numVertexEsquerra();
    }

    public TADLlista<String> obtenirShaUsuaris() {
        return this.graf.obtenirVertexIDsEsquerra();
    }

    public void inserirArtista(Artista artista) {
        this.graf.inserirVertexDret(artista.getId(), artista);
    }

    public Artista consultarArtista(String sha) throws ElementNoTrobat {
        try{
            return this.graf.consultarVertexDret(sha);
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public void esborrarArtista(String sha) throws ElementNoTrobat {
        try{
            this.graf.esborrarVertexDret(sha);
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public int numArtistes() {
        return this.graf.numVertexDret();
    }

    public TADLlista<String> obtenirShaArtistes() {
        return this.graf.obtenirVertexIDsDret();
    }

    public void inserirEscoltes(Usuari usuari, Artista artista, int escoltes) throws ElementNoTrobat {
        if(escoltes < 0) throw new IllegalArgumentException();
        try{
            this.graf.inserirAresta(usuari.getSha(), artista.getId(), escoltes);
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public boolean existeixEscoltes(Usuari usuari, Artista artista) {
        try{
            return this.graf.existeixAresta(usuari.getSha(), artista.getId());
        } catch(VertexNoTrobat e){ return false; }
    }

    public int consultarEscoltes(Usuari usuari, Artista artista) throws ElementNoTrobat {
        try{
            return this.graf.consultarAresta(usuari.getSha(), artista.getId());
        } catch(VertexNoTrobat | ArestaNoTrobada e){ throw new ElementNoTrobat(); }
    }

    public void esborrarEscoltes(Usuari usuari, Artista artista) throws ElementNoTrobat {
        try{
            this.graf.esborrarAresta(usuari.getSha(), artista.getId());
        } catch(VertexNoTrobat | ArestaNoTrobada e){ throw new ElementNoTrobat(); }
    }

    public int numEscoltes() {
        return this.graf.numArestes();
    }

    public int numArtistesEscoltats(Usuari usuari) throws ElementNoTrobat {
        try{
            return this.graf.numAdjacentsEsquerra(usuari.getSha());
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public int numUsuarisQueEscolten(Artista artista) throws ElementNoTrobat {
        try{
            return this.graf.numAdjacentsDret(artista.getId());
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public boolean teEscoltesUsuari(Usuari usuari) {
        try{
            return !this.graf.vertexEsquerraAillat(usuari.getSha());
        } catch(VertexNoTrobat e){ return false; }
    }

    public boolean teEscoltesArtista(Artista artista) {
        try{
            return !this.graf.vertexDretAillat(artista.getId());
        } catch(VertexNoTrobat e){ return false; }
    }

    public TADLlista<String> obtenirArtistesEscoltats(Usuari usuari) throws ElementNoTrobat {
        try{
            return this.graf.obtenirAdjacentsEsquerra(usuari.getSha());
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public TADLlista<String> obtenirUsuarisQueHanEscoltat(Artista artista) throws ElementNoTrobat {
        try{
            return this.graf.obtenirAdjacentsDret(artista.getId());
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public TADLlista<String> obtenirUsuarisAmb2ArtistesCompartits(Usuari usuari) throws ElementNoTrobat {
        try{
            return this.graf.obtenirVertexsEsquerraAmb2AdjacentsCompartits(usuari.getSha());
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public int[] distribucioGrauUsuaris() {
        return this.graf.distribucioGrauK1();
    }

    public int[] distribucioGrauArtistes() {
        return this.graf.distribucioGrauK2();
    }

    public boolean esBuida() {
        return this.graf.numVertexEsquerra() == 0 && this.graf.numVertexDret() == 0;
    }

    public TADLlista<String> recomanacio(String userId, int numMinEscoltes, boolean sexe, int rangEdat, boolean pais, boolean preferits){

        return null;
    }
}
