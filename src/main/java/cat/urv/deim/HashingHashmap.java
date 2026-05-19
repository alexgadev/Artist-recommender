package cat.urv.deim;

import java.util.HashMap;
import java.util.Iterator;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.LlistaPlena;

public class HashingHashmap<K, V> implements TADHashMap<K, V>{
    private HashMap<K, V> table;
    private int capacity;

    public HashingHashmap(int size){
        this.table = new HashMap<>(size);
        this.capacity = size;
    }

    @Override
    public void inserir(K key, V value) {
        if(this.table.size() / this.capacity > 0.75) this.capacity *= 2;
        this.table.put(key, value);
    }

    @Override
    public V consultar(K key) throws ElementNoTrobat {
        if(!this.table.containsKey(key)) throw new ElementNoTrobat();

        return this.table.get(key);
    }

    @Override
    public void esborrar(K key) throws ElementNoTrobat {
        if(!this.table.containsKey(key)) throw new ElementNoTrobat();

        this.table.remove(key);
    }

    @Override
    public boolean buscar(K key) {
        return this.table.containsKey(key);
    }

    @Override
    public boolean esBuida() {
        return this.table.isEmpty();
    }

    @Override
    public int numElements() {
        return this.table.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public TADLlista<K> obtenirClaus() {
        Object[] array = this.table.keySet().toArray();
        TADLlista<K> keySet = new LlistaArrayList<>(array.length);

        for (Object element : array) {
            try{
                keySet.inserir((K)element);
            }
            catch (LlistaPlena e){}
        }
        return keySet;
    }

    @Override
    public float factorCarrega() {
        return (float) this.table.size() / this.capacity;
    }

    @Override
    public int midaTaula() { // no hi ha altra manera real a part d'utiltizar Reflection que suposo que no es l'intencio
        return this.capacity;
    }

    @Override
    public Iterator<V> iterator() {
        return this.table.values().iterator();
    }
}
