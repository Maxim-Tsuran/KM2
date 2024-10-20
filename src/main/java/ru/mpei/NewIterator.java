package ru.mpei;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class NewIterator<T> implements Iterator<T> {

    private Container<T> iterContainer;
    private int idEl;
    private T element;
    private int contCapacity;

    public NewIterator(Container<T> iterContainer, int contCapacity)  {
        this.iterContainer = iterContainer;
        this.idEl = iterContainer.getFirstElement();
        this.contCapacity = contCapacity;
    }

    @Override
    public boolean hasNext() {
        if (idEl < contCapacity && iterContainer.getArray()[idEl] != null) {
            return true;
        } else if (iterContainer.getNext() != null) {
            iterContainer = iterContainer.getNext();
            idEl = 0;
            return hasNext(); // Проверить следующий контейнер
        }
        return false;
    }

    @Override
    public T next() {
        if (hasNext()){
            this.element = (T) this.iterContainer.getArray()[this.idEl];
            this.idEl++;
            if (this.idEl == contCapacity && this.iterContainer.getNext() != null){
                this.idEl = 0;
                this.iterContainer = (Container<T>) this.iterContainer.getNext();
            }
            return this.element;
        } else {
             throw new NoSuchElementException("Кончились элементы");
        }
    }
}
