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

public class GrafUsuarisCancons {
    private TADGrafBipartit<String, Usuari, String, Artista, Integer> graf;

    public GrafUsuarisCancons(int mida) {
        this.graf = new GrafBipartit<>(mida);
    }

    public GrafUsuarisCancons(int mida, String fitxerArtEscoltats, String fitxerUsuaris) {
        this.graf = new GrafBipartit<>(mida);

        try(BufferedReader br = new BufferedReader(new FileReader(fitxerCancons))){
            String line = br.readLine(); // skip first

            while((line = br.readLine()) != null){
                String[] tokens = line.split("\t");
                Artista canco = new Artista(Integer.parseInt(tokens[1]),
                                    tokens[2]);

                this.graf.inserirVertexDret(canco.getTitol(), canco);
            }
        }
        catch(FileNotFoundException | ArrayIndexOutOfBoundsException ex){
            throw new IllegalArgumentException();
        }
        catch(IOException e){
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        try(BufferedReader br = new BufferedReader(new FileReader(fitxerUsuaris))){
            String line = br.readLine(); // skip first

            while((line = br.readLine()) != null){
                String[] tokens = line.split(",");
                Usuari usuari = new Usuari(Integer.parseInt(tokens[0]),
                                    tokens[1], tokens[2], tokens[3],
                                    Integer.parseInt(tokens[4]));

                this.graf.inserirVertexEsquerra(usuari.getNomUsuari(), usuari);
            }
        }
        catch(FileNotFoundException | ArrayIndexOutOfBoundsException ex){
            throw new IllegalArgumentException();
        }
        catch(IOException e){
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        if(fitxerValoracions != null){
            try(BufferedReader br = new BufferedReader(new FileReader(fitxerValoracions))){
                String line = br.readLine(); // skip first

                while((line = br.readLine()) != null){
                    String[] tokens = line.split(",");

                    try{
                        this.graf.inserirAresta(tokens[0], tokens[1], Integer.parseInt(tokens[2]));
                    }
                    catch(VertexNoTrobat e){}
                }
            }
            catch(FileNotFoundException | ArrayIndexOutOfBoundsException ex){
                throw new IllegalArgumentException();
            }
            catch(IOException e){
                System.err.println("Error reading CSV file: " + e.getMessage());
            }
        }
    }

    public void inserirUsuari(Usuari usuari) {
        this.graf.inserirVertexEsquerra(usuari.getNomUsuari(), usuari);
    }

    public Usuari consultarUsuari(String nomUsuari) throws ElementNoTrobat {
        try{
            return this.graf.consultarVertexEsquerra(nomUsuari);
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public void esborrarUsuari(String nomUsuari) throws ElementNoTrobat {
        try{
            this.graf.esborrarVertexEsquerra(nomUsuari);
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public int numUsuaris() {
        return this.graf.numVertexEsquerra();
    }

    public TADLlista<String> obtenirUsernames() {
        return this.graf.obtenirVertexIDsEsquerra();
    }

    public void inserirArtista(Artista canco) {
        this.graf.inserirVertexDret(canco.getTitol(), canco);
    }

    public Artista consultarArtista(String titol) throws ElementNoTrobat {
        try{
            return this.graf.consultarVertexDret(titol);
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public void esborrarArtista(String titol) throws ElementNoTrobat {
        try{
            this.graf.esborrarVertexDret(titol);
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public int numArtistes() {
        return this.graf.numVertexDret();
    }

    public TADLlista<String> obtenirTitols() {
        return this.graf.obtenirVertexIDsDret();
    }

    public void inserirValoracio(Usuari usuari, Artista canco, int valoracio) throws ElementNoTrobat {
        if(valoracio < 1 || valoracio > 5) throw new IllegalArgumentException();
        try{
            this.graf.inserirAresta(usuari.getNomUsuari(), canco.getTitol(), valoracio);
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public boolean existeixValoracio(Usuari usuari, Artista canco) {
        try{
            return this.graf.existeixAresta(usuari.getNomUsuari(), canco.getTitol());
        } catch(VertexNoTrobat e){ return false; }
    }

    public int consultarValoracio(Usuari usuari, Artista canco) throws ElementNoTrobat {
        try{
            return this.graf.consultarAresta(usuari.getNomUsuari(), canco.getTitol());
        } catch(VertexNoTrobat | ArestaNoTrobada e){ throw new ElementNoTrobat(); }
    }

    public void esborrarValoracio(Usuari usuari, Artista canco) throws ElementNoTrobat {
        try{
            this.graf.esborrarAresta(usuari.getNomUsuari(), canco.getTitol());
        } catch(VertexNoTrobat | ArestaNoTrobada e){ throw new ElementNoTrobat(); }
    }

    public int numValoracions() {
        return this.graf.numArestes();
    }

    public int numArtistesValorats(Usuari usuari) throws ElementNoTrobat {
        try{
            return this.graf.numAdjacentsEsquerra(usuari.getNomUsuari());
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public int numUsuarisQueHanValorat(Artista canco) throws ElementNoTrobat {
        try{
            return this.graf.numAdjacentsDret(canco.getTitol());
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public boolean teValoracionsUsuari(Usuari usuari) {
        try{
            return !this.graf.vertexEsquerraAillat(usuari.getNomUsuari());
        } catch(VertexNoTrobat e){ return false; }
    }

    public boolean teValoracionsCanco(Artista canco) {
        try{
            return !this.graf.vertexDretAillat(canco.getTitol());
        } catch(VertexNoTrobat e){ return false; }
    }

    public TADLlista<String> obtenirCanconsValorades(Usuari usuari) throws ElementNoTrobat {
        try{
            return this.graf.obtenirAdjacentsEsquerra(usuari.getNomUsuari());
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public TADLlista<String> obtenirUsuarisQueHanValorat(Artista canco) throws ElementNoTrobat {
        try{
            return this.graf.obtenirAdjacentsDret(canco.getTitol());
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public TADLlista<String> obtenirUsuarisAmb2CanconsCompartides(Usuari usuari) throws ElementNoTrobat {
        try{
            return this.graf.obtenirVertexsEsquerraAmb2AdjacentsCompartits(usuari.getNomUsuari());
        } catch(VertexNoTrobat e){ throw new ElementNoTrobat(); }
    }

    public int[] distribucioGrauUsuaris() {
        return this.graf.distribucioGrauK1();
    }

    public int[] distribucioGrauCancons() {
        return this.graf.distribucioGrauK2();
    }

    public int comptarCanconsAmbValoracioMitjaSuperiorA(double llindar) {
        int n = 0;
        TADLlista<String> ids = this.graf.obtenirVertexIDsDret();
        for (int i = 0; i < ids.numElem(); i++) {
            try {
                String titol = ids.consultar(i);
                TADLlista<String> usuaris = this.graf.obtenirAdjacentsDret(titol);
                if (usuaris.numElem() == 0) continue;
                double suma = 0;
                for (int j = 0; j < usuaris.numElem(); j++) {
                    try {
                        suma += this.graf.consultarAresta(usuaris.consultar(j), titol);
                    } catch (ArestaNoTrobada | VertexNoTrobat | LlistaBuida e) {}
                }
                if ((suma / usuaris.numElem()) > llindar) n++;
            } catch (VertexNoTrobat | LlistaBuida e) {}
        }
        return n;
    }

    public TADLlista<String> obtenirUsuarisQueHanEscoltatGenere(String genere) {
        TADLlista<String> usuarisGenere = new LlistaArrayList<>(this.graf.numVertexEsquerra());

        TADLlista<String> ids = this.graf.obtenirVertexIDsEsquerra();
        for (int i = 0; i < ids.numElem(); i++) {
            try {
                String usuari = ids.consultar(i);
                TADLlista<String> cancons = this.graf.obtenirAdjacentsEsquerra(usuari);
                if (cancons.numElem() == 0) continue;

                for (int j = 0; j < cancons.numElem(); j++) {
                    try {
                        Artista canco = this.graf.consultarVertexDret(cancons.consultar(j));
                        if(canco.getGenere().equals(genere)){
                            usuarisGenere.inserir(usuari);
                            break;
                        }
                    } catch (VertexNoTrobat | LlistaBuida | LlistaPlena e) {}
                }
            } catch (VertexNoTrobat | LlistaBuida e) {}
        }
        return usuarisGenere;
    }

    public boolean esBuida() {
        return this.graf.numVertexEsquerra() == 0 && this.graf.numVertexDret() == 0;
    }
}
