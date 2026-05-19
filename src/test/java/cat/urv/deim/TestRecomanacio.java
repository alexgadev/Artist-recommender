package cat.urv.deim;

import org.junit.jupiter.api.Test;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.LlistaBuida;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRecomanacio {

    // -------------------------------------------------------------------------
    // Helpers de creació de dades
    // -------------------------------------------------------------------------

    private Usuari crearUsuariEspanya30Home() {
        return new Usuari(1, "m", 30, "Usuari Espanya", "Spain");
    }

    private Usuari crearUsuariEspanya28Home() {
        return new Usuari(2, "m", 28, "Usuari Espanya 2", "Spain");
    }

    private Usuari crearUsuariFranca35Dona() {
        return new Usuari(3, "f", 35, "Usuari Franca", "France");
    }

    private Usuari crearUsuariEspanya55Home() {
        return new Usuari(4, "m", 55, "Usuari Espanya 3", "Spain");
    }

    // Representa un artista (el camp titol = nom de l'artista)
    private Artista crearArtista1() {
        return new Artista(1, "Radiohead");
    }

    private Artista crearArtista2() {
        return new Artista(2, "Portishead");
    }

    private Artista crearArtista3() {
        return new Artista(3, "Muse");
    }

    private Artista crearArtista4() {
        return new Artista(4, "Blur");
    }

    // -------------------------------------------------------------------------
    // recomanació per país
    // -------------------------------------------------------------------------

    @Test
    public void testRecomanacioPerPaisRetornaArtistesDelMateixPais() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();
        Usuari u2 = crearUsuariEspanya28Home();
        Usuari u3 = crearUsuariFranca35Dona();
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();
        Artista a3 = crearArtista3();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirUsuari(u3);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);
        graf.inserirArtista(a3);

        graf.inserirEscoltes(u1, a1, 150);
        graf.inserirEscoltes(u2, a2, 200);
        graf.inserirEscoltes(u3, a3, 300);

        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 50, false, -1, true, false);

        assertTrue(recomanats.existeix("Portishead"));
        assertFalse(recomanats.existeix("Radiohead")); // ja escoltat per u1
        assertFalse(recomanats.existeix("Muse"));      // país diferent
    }

    @Test
    public void testRecomanacioPerPaisNoRetornaArtistesAmbPoquesProduccions() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();
        Usuari u2 = crearUsuariEspanya28Home();
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);

        graf.inserirEscoltes(u1, a1, 200);
        graf.inserirEscoltes(u2, a2, 30); // per sota del llindar de 100

        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, -1, true, false);

        assertEquals(0, recomanats.numElem());
    }

    @Test
    public void testRecomanacioPerPaisNoInclouArtistesJaEscoltats() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();
        Usuari u2 = crearUsuariEspanya28Home();
        Artista a1 = crearArtista1();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirArtista(a1);

        graf.inserirEscoltes(u1, a1, 150);
        graf.inserirEscoltes(u2, a1, 400);

        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, -1, true, false);

        assertFalse(recomanats.existeix("Radiohead"));
        assertEquals(0, recomanats.numElem());
    }

    // -------------------------------------------------------------------------
    // recomanació per edat
    // -------------------------------------------------------------------------

    @Test
    public void testRecomanacioPerEdatRetornaArtistesDelRangEdat() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();      // edat 30
        Usuari u2 = crearUsuariEspanya28Home();      // edat 28 (dins rang 5)
        Usuari u3 = crearUsuariEspanya55Home();      // edat 55 (fora rang)
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();
        Artista a3 = crearArtista3();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirUsuari(u3);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);
        graf.inserirArtista(a3);

        graf.inserirEscoltes(u1, a1, 100);
        graf.inserirEscoltes(u2, a2, 200);
        graf.inserirEscoltes(u3, a3, 300);

        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, 5, false, false);

        assertTrue(recomanats.existeix("Portishead"));
        assertFalse(recomanats.existeix("Muse")); // fora del rang d'edat
    }

    @Test
    public void testRecomanacioPerEdatFrontera() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();  // edat 30
        Usuari u2 = new Usuari(5, "user-es-25-f", "Usuari 25", "Spain", 25, "f"); // edat 25 = just at boundary
        Usuari u3 = new Usuari(6, "user-es-24-m", "Usuari 24", "Spain", 24, "m"); // edat 24 = out of boundary
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();
        Artista a3 = crearArtista3();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirUsuari(u3);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);
        graf.inserirArtista(a3);

        graf.inserirEscoltes(u1, a1, 100);
        graf.inserirEscoltes(u2, a2, 200);
        graf.inserirEscoltes(u3, a3, 300);

        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, 5, false, false);

        assertTrue(recomanats.existeix("Portishead"));  // |30-25| = 5, dins rang
        assertFalse(recomanats.existeix("Muse"));       // |30-24| = 6, fora rang
    }

    @Test
    public void testRecomanacioIgnoraEdatSiEdatDesconeguda() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = new Usuari(1, "user-no-age", "Sense edat", "Spain", -1, "m");
        Usuari u2 = crearUsuariEspanya28Home();
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);

        graf.inserirEscoltes(u1, a1, 100);
        graf.inserirEscoltes(u2, a2, 200);

        // l'usuari objectiu no té edat: la dimensió edat no es pot aplicar
        TADLlista<String> recomanats = graf.recomanacio("user-no-age", 100, false, 5, false, false);

        assertEquals(0, recomanats.numElem());
    }

    @Test
    public void testRecomanacioRangEdatNegatiuIgnoraFiltreEdat() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();
        Usuari u2 = crearUsuariEspanya55Home(); // edat molt diferent
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);

        graf.inserirEscoltes(u1, a1, 100);
        graf.inserirEscoltes(u2, a2, 150);

        // rangEdat = -1: no s'aplica filtre d'edat, tots els usuaris són similars
        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, -1, false, false);

        assertTrue(recomanats.existeix("Portishead"));
    }

    // -------------------------------------------------------------------------
    // recomanació per sexe
    // -------------------------------------------------------------------------

    @Test
    public void testRecomanacioPerSexeRetornaArtistesDelMateixSexe() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();  // m
        Usuari u2 = crearUsuariEspanya28Home();  // m (mateix sexe)
        Usuari u3 = crearUsuariFranca35Dona();   // f (sexe diferent)
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();
        Artista a3 = crearArtista3();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirUsuari(u3);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);
        graf.inserirArtista(a3);

        graf.inserirEscoltes(u1, a1, 100);
        graf.inserirEscoltes(u2, a2, 200);
        graf.inserirEscoltes(u3, a3, 300);

        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, true, -1, false, false);

        assertTrue(recomanats.existeix("Portishead"));
        assertFalse(recomanats.existeix("Muse")); // d'una usuaria de sexe diferent
    }

    @Test
    public void testRecomanacioSexeDesconegutNoEsConsidera() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home(); // m
        Usuari u2 = new Usuari(5, "user-no-sex", "Sense sexe", "Spain", 28, ""); // sense sexe
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);

        graf.inserirEscoltes(u1, a1, 100);
        graf.inserirEscoltes(u2, a2, 200);

        // usuari sense sexe no és considerat similar quan s'aplica filtre de sexe
        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, true, -1, false, false);

        assertFalse(recomanats.existeix("Portishead"));
    }

    // -------------------------------------------------------------------------
    // recomanació per artistes preferits
    // -------------------------------------------------------------------------

    @Test
    public void testRecomanacioPerPreferitsComparteixArtista() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();
        Usuari u2 = crearUsuariFranca35Dona(); // país/sexe/edat diferent
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();
        Artista a3 = crearArtista3();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);
        graf.inserirArtista(a3);

        // top-2 de u1: a1 (500), a2 (200)
        graf.inserirEscoltes(u1, a1, 500);
        graf.inserirEscoltes(u1, a2, 200);
        // u2 comparteix a1 amb u1 (top preferit)
        graf.inserirEscoltes(u2, a1, 400);
        graf.inserirEscoltes(u2, a3, 600);

        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, -1, false, true);

        assertTrue(recomanats.existeix("Muse"));       // a3: escoltat per u2 que comparteix preferit
        assertFalse(recomanats.existeix("Portishead")); // a2: ja escoltat per u1
    }

    @Test
    public void testRecomanacioPerPreferitsNoComparteixCapArtistaPreferit() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();
        Usuari u2 = crearUsuariFranca35Dona();
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();
        Artista a3 = crearArtista3();
        Artista a4 = crearArtista4();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);
        graf.inserirArtista(a3);
        graf.inserirArtista(a4);

        // top-2 de u1: a1, a2
        graf.inserirEscoltes(u1, a1, 500);
        graf.inserirEscoltes(u1, a2, 200);
        // u2 no escolta ni a1 ni a2
        graf.inserirEscoltes(u2, a3, 400);
        graf.inserirEscoltes(u2, a4, 300);

        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, -1, false, true);

        assertEquals(0, recomanats.numElem());
    }

    @Test
    public void testRecomanacioPerPreferitsSegonArtistaTambdCompta() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();
        Usuari u2 = crearUsuariFranca35Dona();
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();
        Artista a3 = crearArtista3();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);
        graf.inserirArtista(a3);

        // top-2 de u1: a1 (500), a2 (200)
        graf.inserirEscoltes(u1, a1, 500);
        graf.inserirEscoltes(u1, a2, 200);
        // u2 comparteix el segon preferit (a2)
        graf.inserirEscoltes(u2, a2, 350);
        graf.inserirEscoltes(u2, a3, 600);

        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, -1, false, true);

        assertTrue(recomanats.existeix("Muse"));
    }

    // -------------------------------------------------------------------------
    // recomanació combinada (país + edat)
    // -------------------------------------------------------------------------

    @Test
    public void testRecomanacioPerPaisIEdatRequereixTotsDos() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();
        Usuari u2 = crearUsuariEspanya28Home();  // mateix país + dins rang edat
        Usuari u3 = crearUsuariFranca35Dona();   // dins rang edat, país diferent
        Usuari u4 = crearUsuariEspanya55Home();  // mateix país, fora rang edat
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();
        Artista a3 = crearArtista3();
        Artista a4 = crearArtista4();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirUsuari(u3);
        graf.inserirUsuari(u4);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);
        graf.inserirArtista(a3);
        graf.inserirArtista(a4);

        graf.inserirEscoltes(u1, a1, 100);
        graf.inserirEscoltes(u2, a2, 200); // compleix pais + edat
        graf.inserirEscoltes(u3, a3, 300); // compleix edat però no país
        graf.inserirEscoltes(u4, a4, 400); // compleix país però no edat

        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, 5, true, false);

        assertTrue(recomanats.existeix("Portishead"));
        assertFalse(recomanats.existeix("Muse"));
        assertFalse(recomanats.existeix("Blur"));
    }

    @Test
    public void testRecomanacioPerPaisISexe() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();                                     // m, Spain
        Usuari u2 = crearUsuariEspanya28Home();                                     // m, Spain (match)
        Usuari u3 = new Usuari(5, "user-es-25-f", "U5", "Spain", 25, "f");         // f, Spain (no match sexe)
        Usuari u4 = new Usuari(6, "user-fr-28-m", "U6", "France", 28, "m");        // m, France (no match pais)
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();
        Artista a3 = crearArtista3();
        Artista a4 = crearArtista4();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirUsuari(u3);
        graf.inserirUsuari(u4);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);
        graf.inserirArtista(a3);
        graf.inserirArtista(a4);

        graf.inserirEscoltes(u1, a1, 100);
        graf.inserirEscoltes(u2, a2, 200);
        graf.inserirEscoltes(u3, a3, 300);
        graf.inserirEscoltes(u4, a4, 200);

        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, true, -1, true, false);

        assertTrue(recomanats.existeix("Portishead"));
        assertFalse(recomanats.existeix("Muse"));
        assertFalse(recomanats.existeix("Blur"));
    }

    // -------------------------------------------------------------------------
    // llista resultat sense duplicats
    // -------------------------------------------------------------------------

    @Test
    public void testRecomanacioSenseArtistesRepetits() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();
        Usuari u2 = crearUsuariEspanya28Home();
        Usuari u3 = new Usuari(5, "user-es-31-m", "Usuari Espanya 4", "Spain", 31, "m");
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirUsuari(u3);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);

        graf.inserirEscoltes(u1, a1, 100);
        // u2 i u3 escolten el mateix artista: ha d'aparèixer una sola vegada
        graf.inserirEscoltes(u2, a2, 200);
        graf.inserirEscoltes(u3, a2, 150);

        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, -1, true, false);

        assertEquals(1, recomanats.numElem());
        assertTrue(recomanats.existeix("Portishead"));
    }

    // -------------------------------------------------------------------------
    // casos límit i excepcions
    // -------------------------------------------------------------------------

    @Test
    public void testRecomanacioUsuariInexistentLlancaExcepcio() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        assertThrows(ElementNoTrobat.class,
                () -> graf.recomanacio("no-existeix", 100, false, -1, false, false));
    }

    @Test
    public void testRecomanacioSenseFiltresTotsElsUsuarisSonSimilars() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();
        Usuari u2 = crearUsuariFranca35Dona(); // país, sexe i edat diferents
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);

        graf.inserirEscoltes(u1, a1, 100);
        graf.inserirEscoltes(u2, a2, 200);

        // cap filtre actiu: tots els usuaris són similars
        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, -1, false, false);

        assertTrue(recomanats.existeix("Portishead"));
    }

    @Test
    public void testRecomanacioSenseUsuarisSimilarsRetornaLlistaBuida() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();
        Usuari u2 = crearUsuariFranca35Dona();
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);

        graf.inserirEscoltes(u1, a1, 150);
        graf.inserirEscoltes(u2, a2, 200);

        // filtre per país: u2 és de França, no és similar
        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, -1, true, false);

        assertEquals(0, recomanats.numElem());
    }

    @Test
    public void testRecomanacioGrafSenseAltresUsuarisRetornaLlistaBuida() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();
        Artista a1 = crearArtista1();

        graf.inserirUsuari(u1);
        graf.inserirArtista(a1);
        graf.inserirEscoltes(u1, a1, 200);

        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, -1, false, false);

        assertEquals(0, recomanats.numElem());
    }

    @Test
    public void testRecomanacioLlindarExacteNoComptaIgual() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home();
        Usuari u2 = crearUsuariEspanya28Home();
        Artista a1 = crearArtista1();
        Artista a2 = crearArtista2();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirArtista(a1);
        graf.inserirArtista(a2);

        graf.inserirEscoltes(u1, a1, 100);
        graf.inserirEscoltes(u2, a2, 100); // exactament igual al llindar

        // el llindar és estricte (>) o permissiu (>=)?
        // d'acord amb el README: "escoltat més d'un cert nombre de vegades" → >=
        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, -1, true, false);

        assertTrue(recomanats.existeix("Portishead"));
    }

    @Test
    public void testRecomanacioUsuariSenseEscoltesPropies() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(100);
        Usuari u1 = crearUsuariEspanya30Home(); // sense escolta pròpia
        Usuari u2 = crearUsuariEspanya28Home();
        Artista a1 = crearArtista1();

        graf.inserirUsuari(u1);
        graf.inserirUsuari(u2);
        graf.inserirArtista(a1);

        graf.inserirEscoltes(u2, a1, 200);

        TADLlista<String> recomanats = graf.recomanacio("user-es-30-m", 100, false, -1, true, false);

        // a1 no ha estat escoltat per u1, ha de ser recomanat
        assertTrue(recomanats.existeix("Radiohead"));
    }

    // -------------------------------------------------------------------------
    // helper per evitar l'excepció LlistaBuida
    // -------------------------------------------------------------------------

    private <E> E consultar(TADLlista<E> llista, int pos) {
        try {
            return llista.consultar(pos);
        } catch (LlistaBuida e) {
            throw new IllegalStateException(e);
        }
    }
}
