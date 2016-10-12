package queue;

import java.util.AbstractQueue;
import java.util.Iterator;

public class MostRecentlyInsertedQueue<E> extends AbstractQueue<E> {
    
    private Integer[] values;

    public MostRecentlyInsertedQueue(int maxsize) {
        values = new Integer[maxsize];
    }

    public boolean offer(E arg0) {
        
        return false;
    }

    public E peek() {
        // TODO Auto-generated method stub
        return null;
    }

    public E poll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
