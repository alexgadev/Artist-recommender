package cat.urv.deim;

import cat.urv.deim.exceptions.ArestaNoTrobada;
import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.LlistaBuida;
import cat.urv.deim.exceptions.LlistaPlena;
import cat.urv.deim.exceptions.VertexNoTrobat;

public class GrafBipartit<K1 extends Comparable<K1>, V1, K2 extends Comparable<K2>, V2, E>
        implements TADGrafBipartit<K1, V1, K2, V2, E> {

    private static class Vertex<V, K extends Comparable<K>, E>{
        private V value;
        TADHashMap<K, E> adjacents;

        Vertex(V value){
            this.value = value;

            this.adjacents = new HashingHashmap<>(16);
        }

        public V getValue(){
            return this.value;
        }

        public void setValue(V val){
            this.value = val;
        }

        public void addEdge(K key, E weight){
            this.adjacents.inserir(key, weight);
        }

        public TADLlista<K> adjacentVertices(){
            return adjacents.obtenirClaus();
        }

        public void removeEdge(K key){
            try{
                this.adjacents.esborrar(key);
            }
            catch (ElementNoTrobat e){}
        }

        public boolean hasEdge(K key){
            return adjacents.buscar(key);
        }

        public E getEdge(K key){
            try
            {
                return adjacents.consultar(key);
            }
            catch(ElementNoTrobat e){ return null; }
        }

        public int getNumEdges(){
            return this.adjacents.numElements();
        }
    }

    private TADHashMap<K1, Vertex<V1, K2, E>> leftSet;
    private TADHashMap<K2, Vertex<V2, K1, E>> rightSet;

    public GrafBipartit(int size){
        if(size <= 0) throw new IllegalArgumentException();
        this.leftSet = new HashingHashmap<>(size);
        this.rightSet = new HashingHashmap<>(size);
    }

    @Override
    public void inserirVertexEsquerra(K1 key, V1 value) {
        try{
            Vertex<V1, K2, E> current = this.leftSet.consultar(key);
            current.setValue(value);
        }
        catch(ElementNoTrobat e){
            this.leftSet.inserir(key, new Vertex<>(value));
        }
    }

    @Override
    public V1 consultarVertexEsquerra(K1 key) throws VertexNoTrobat {
        try{
            Vertex<V1, K2, E> current = this.leftSet.consultar(key);
            return current.getValue();
        }
        catch(ElementNoTrobat e){
            throw new VertexNoTrobat();
        }
    }

    @Override
    public void esborrarVertexEsquerra(K1 key) throws VertexNoTrobat {
        try{
            // buscar si te adjacencies per eliminar les relacions corresponents
            Vertex<V1, K2, E> adjacency = this.leftSet.consultar(key);
            TADLlista<K2> keys = adjacency.adjacentVertices();

            if(!keys.esBuida()){
                for(int i = 0; i < keys.numElem(); i++){
                    try{
                        Vertex<V2, K1, E> adjacency2 = this.rightSet.consultar(keys.consultar(i));
                        adjacency2.removeEdge(key);
                    }
                    catch (LlistaBuida e){}
                }
            }

            // eliminar del hashmap
            this.leftSet.esborrar(key);
        }
        catch(ElementNoTrobat e){
            throw new VertexNoTrobat();
        }
    }

        @Override
        public int numVertexEsquerra() {
            return this.leftSet.numElements();
        }

        @Override
        public TADLlista<K1> obtenirVertexIDsEsquerra() {
            return ordenar(this.leftSet.obtenirClaus());
        }

        @Override
        public void inserirVertexDret(K2 key, V2 value) {
            try{
                Vertex<V2, K1, E> current = this.rightSet.consultar(key);
                current.setValue(value);
            }
            catch(ElementNoTrobat e){
                this.rightSet.inserir(key, new Vertex<>(value));
            }
        }

        @Override
        public V2 consultarVertexDret(K2 key) throws VertexNoTrobat {
            try{
                Vertex<V2, K1, E> current = this.rightSet.consultar(key);
                return current.getValue();
            }
            catch(ElementNoTrobat e){
                throw new VertexNoTrobat();
            }
        }

        @Override
        public void esborrarVertexDret(K2 key) throws VertexNoTrobat {
            try{
            // buscar si te adjacencies per eliminar les relacions corresponents
            Vertex<V2, K1, E> adjacency = this.rightSet.consultar(key);
            TADLlista<K1> keys = adjacency.adjacentVertices();

            if(!keys.esBuida()){
                for(int i = 0; i < keys.numElem(); i++){
                    try{
                        Vertex<V1, K2, E> adjacency2 = this.leftSet.consultar(keys.consultar(i));
                        adjacency2.removeEdge(key);
                    }
                    catch (LlistaBuida e){}
                }
            }

            // eliminar del hashmap
            this.rightSet.esborrar(key);
            }
            catch(ElementNoTrobat e){
                throw new VertexNoTrobat();
            }
        }

        @Override
        public int numVertexDret() {
            return this.rightSet.numElements();
        }

        @Override
        public TADLlista<K2> obtenirVertexIDsDret() {
            return ordenar(this.rightSet.obtenirClaus());
        }

        private <K extends Comparable<K>> TADLlista<K> ordenar(TADLlista<K> llista) {
            TADLlista<K> sorted = new LlistaArrayList<>(llista.numElem());
            try {
                for (int i = 0; i < llista.numElem(); i++) {
                    K elem = llista.consultar(i);
                    int pos = 0;
                    while (pos < sorted.numElem() && elem.compareTo(sorted.consultar(pos)) > 0)
                        pos++;
                    sorted.inserir(elem, pos);
                }
            } catch (LlistaBuida | LlistaPlena e) {}
            return sorted;
        }

        @Override
        public void inserirAresta(K1 v1, K2 v2, E pes) throws VertexNoTrobat {
            // millor comprovar abans per no inserir erroneament nomes a un dels conjunts
            if(!this.leftSet.buscar(v1) || !this.rightSet.buscar(v2)) throw new VertexNoTrobat();

            try{
                // afegir al conjunt esquerre
                Vertex<V1, K2, E> adjacency = this.leftSet.consultar(v1);
                adjacency.addEdge(v2, pes);

                // afegir al conjunt dret
                Vertex<V2, K1, E> adjacency2 = this.rightSet.consultar(v2);
                adjacency2.addEdge(v1, pes);
            }
            catch(ElementNoTrobat e){} // mai arribara a aquest punt
        }

        @Override
        public void inserirAresta(K1 v1, K2 v2) throws VertexNoTrobat {
            inserirAresta(v1, v2, null);
        }

        @Override
        public boolean existeixAresta(K1 v1, K2 v2) throws VertexNoTrobat {
            try{
                Vertex<V1, K2, E> adjacency = this.leftSet.consultar(v1);
                Vertex<V2, K1, E> adjacency2 = this.rightSet.consultar(v2);
                return adjacency.hasEdge(v2) && adjacency2.hasEdge(v1);
            }
            catch(ElementNoTrobat e){
                throw new VertexNoTrobat();
            }
        }

        @Override
        public E consultarAresta(K1 v1, K2 v2) throws VertexNoTrobat, ArestaNoTrobada {
            // millor comprovar abans per evitar comportaments indefinits
            if(!this.leftSet.buscar(v1) || !this.rightSet.buscar(v2)) throw new VertexNoTrobat();

            try{
                Vertex<V1, K2, E> adjacency = this.leftSet.consultar(v1);

                if(adjacency.hasEdge(v2)){
                    return adjacency.getEdge(v2);
                }
                throw new ArestaNoTrobada();
            }
            catch(ElementNoTrobat e){ return null; } // mai arribara
        }

        @Override
        public void esborrarAresta(K1 v1, K2 v2) throws VertexNoTrobat, ArestaNoTrobada {
            // millor comprovar abans per evitar comportaments indefinits
            if(!this.leftSet.buscar(v1) || !this.rightSet.buscar(v2)) throw new VertexNoTrobat();

            try{
                // esborrar al conjunt esquerre
                Vertex<V1, K2, E> adjacency = this.leftSet.consultar(v1);
                if(adjacency.hasEdge(v2)){
                    adjacency.removeEdge(v2);
                }
                else{
                    throw new ArestaNoTrobada();
                }

                // esborrar al conjunt dret
                Vertex<V2, K1, E> adjacency2 = this.rightSet.consultar(v2);
                if(adjacency2.hasEdge(v1)){
                    adjacency2.removeEdge(v1);
                }
                else{
                    throw new ArestaNoTrobada();
                }
            }
            catch(ElementNoTrobat e){} // mai arribara a aquest punt
        }

        @Override
        public int numArestes() {
            int n = 0;
            for (Vertex<V1,K2,E> vertex : leftSet) {
                n += vertex.getNumEdges();
            }
            return n;
        }

        @Override
        public boolean vertexEsquerraAillat(K1 key) throws VertexNoTrobat {
            try{
                Vertex<V1, K2, E> adjacency = this.leftSet.consultar(key);
                return adjacency.getNumEdges() == 0;
            }
            catch(ElementNoTrobat e){
                throw new VertexNoTrobat();
            }
        }

        @Override
        public boolean vertexDretAillat(K2 key) throws VertexNoTrobat {
            try{
                Vertex<V2, K1, E> adjacency = this.rightSet.consultar(key);
                return adjacency.getNumEdges() == 0;
            }
            catch(ElementNoTrobat e){
                throw new VertexNoTrobat();
            }
        }

        @Override
        public int numAdjacentsEsquerra(K1 key) throws VertexNoTrobat {
            try{
                Vertex<V1, K2, E> adjacency = this.leftSet.consultar(key);
                return adjacency.getNumEdges();
            }
            catch(ElementNoTrobat e){
                throw new VertexNoTrobat();
            }
        }

        @Override
        public int numAdjacentsDret(K2 key) throws VertexNoTrobat {
            try{
                Vertex<V2, K1, E> adjacency = this.rightSet.consultar(key);
                return adjacency.getNumEdges();
            }
            catch(ElementNoTrobat e){
                throw new VertexNoTrobat();
            }
        }

        @Override
        public TADLlista<K2> obtenirAdjacentsEsquerra(K1 key) throws VertexNoTrobat {
            try{
                Vertex<V1, K2, E> adjacency = this.leftSet.consultar(key);
                return adjacency.adjacentVertices();
            }
            catch(ElementNoTrobat e){
                throw new VertexNoTrobat();
            }
        }

        @Override
        public TADLlista<K1> obtenirAdjacentsDret(K2 key) throws VertexNoTrobat {
            try{
                Vertex<V2, K1, E> adjacency = this.rightSet.consultar(key);
                return adjacency.adjacentVertices();
            }
            catch(ElementNoTrobat e){
                throw new VertexNoTrobat();
            }
        }

        @Override
        public TADLlista<K1> obtenirVertexsEsquerraAmb2AdjacentsCompartits(K1 key) throws VertexNoTrobat {
            try{
                TADLlista<K1> list = new LlistaArrayList<>(this.leftSet.numElements());

                Vertex<V1, K2, E> vertex = this.leftSet.consultar(key);
                TADLlista<K2> keyAdjs = vertex.adjacentVertices();  // adjacents al parametre
                TADLlista<K1> allLeftKeys = this.leftSet.obtenirClaus(); // totes les claus del costat esquerre

                // bucle per totes les claus del costat esquerre
                for(int i = 0; i < allLeftKeys.numElem(); i++){
                    try{
                        K1 currentKey = allLeftKeys.consultar(i);
                        if(currentKey.equals(key)) continue;

                        // obtenim els adjacents de la clau actual
                        Vertex<V1, K2, E> currentVertex = this.leftSet.consultar(currentKey);
                        TADLlista<K2> currentAdjs = currentVertex.adjacentVertices();

                        // per tot adjacent a clau actual, comprova si la clau de parametre tambe te mateix adjacent
                        int n = 0;
                        for(int j = 0; j < currentAdjs.numElem(); j++){
                            try{
                                if(keyAdjs.existeix(currentAdjs.consultar(j))) n++;
                            }
                            catch(LlistaBuida e){}
                            if(n >= 2){
                                list.inserir(currentKey);
                                break;
                            }
                        }
                    }
                    catch(LlistaBuida | ElementNoTrobat | LlistaPlena e){}
                }
                return list;
            }
            catch(ElementNoTrobat e){
                throw new VertexNoTrobat();
            }
        }

        @Override
        public int[] distribucioGrauK1() {
            if (leftSet.numElements() == 0) return new int[0];

            int maxGrau = 0;
            for (Vertex<V1,K2,E> vertex : leftSet)
                if (vertex.getNumEdges() > maxGrau) maxGrau = vertex.getNumEdges();

            int[] graus = new int[maxGrau + 1];
            for (Vertex<V1,K2,E> vertex : leftSet)
                graus[vertex.getNumEdges()]++;

            return graus;
        }

        @Override
        public int[] distribucioGrauK2() {
            if (rightSet.numElements() == 0) return new int[0];

            int maxGrau = 0;
            for (Vertex<V2,K1,E> vertex : rightSet)
                if (vertex.getNumEdges() > maxGrau) maxGrau = vertex.getNumEdges();

            int[] graus = new int[maxGrau + 1];
            for (Vertex<V2,K1,E> vertex : rightSet)
                graus[vertex.getNumEdges()]++;

            return graus;
        }

        public int max(int a, int b){
            return a < b ? b : a;
        }

}
