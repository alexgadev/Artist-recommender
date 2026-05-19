package cat.urv.deim;

import java.util.ArrayList;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.LlistaBuida;
import cat.urv.deim.exceptions.LlistaPlena;


public class LlistaArrayList<E> implements TADLlista<E> {
    private ArrayList<E> list;
    private final int maxElems;

    public LlistaArrayList(int size){
        this.list = new ArrayList<>(size);
        this.maxElems = size;
    }

    @Override
    public void inserir(E elem) throws LlistaPlena{
        if(this.list.size() == this.maxElems) throw new LlistaPlena();
        this.list.add(elem);
    }

    @Override
    public void inserir(E elem, int pos) throws LlistaPlena{
        if(this.list.size() == this.maxElems) throw new LlistaPlena();
        this.list.add(pos, elem);
    }

    @Override
    public void esborrar(int pos) throws LlistaBuida{
        if(this.list.size() == 0) throw new LlistaBuida();
        this.list.remove(pos);
    }

    @Override
    public E consultar(int pos) throws LlistaBuida{
        if(this.list.size() == 0) throw new LlistaBuida();
        return this.list.get(pos);
    }

    @Override
    public int numElem(){
        return this.list.size();
    }

    @Override
    public boolean esBuida(){
        return this.list.isEmpty();
    }

    @Override
    public boolean esPlena(){
        return this.list.size() == this.maxElems;
    }

    @Override
    public boolean existeix(E elem){
        return this.list.contains(elem);
    }

    @Override
    public int posicio(E elem) throws ElementNoTrobat{
        int index = this.list.indexOf(elem);
        if (index == -1) throw new ElementNoTrobat();
        else return index;
    }

    @Override
    public E anterior(E elem) throws ElementNoTrobat{
        int index = this.list.indexOf(elem);
        if (index == 0) return null;
        if (index == -1) throw new ElementNoTrobat();
        else return this.list.get(index - 1);
    }

    @Override
    public E seguent(E elem) throws ElementNoTrobat{
        int index = this.list.indexOf(elem);
        if (index + 1 == this.list.size()) return null;
        if (index == -1) throw new ElementNoTrobat();
        else return this.list.get(index + 1);
    }
}
