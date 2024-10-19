package ru.mpei;

public class Container <T> {

    private T[] array;
    protected Container <T> next; // Ссылки на пред. и послед. контейнеры
    private Container <T> prev;
    private int firstElement;   // Индекс последнего и первого элементов
    private int lastElement;


    public Container(int containerCapacity) {
        this.array = (T[]) new Object[containerCapacity];
        this.next = null;
        this.prev = null;
        this.firstElement = 0;
        this.lastElement = 0;
    }

    public T[] getArray() {
        return array;
    }

    public void setArray(int index, T element) {
        array[index] = element;
    }

    public Container<T> getNext() {
        return next;
    }

    public void setNext(Container<T> next) {
        this.next = next;
    }

    public Container<T> getPrev() {
        return prev;
    }

    public void setPrev(Container<T> prev) {
        this.prev = prev;
    }

    public int getFirstElement() {
        return firstElement;
    }

    public void setFirstElement(int firstElement) {
        this.firstElement = firstElement;
    }

    public int getLastElement() {
        return lastElement;
    }

    public void setLastElement(int lastElement) {
        this.lastElement = lastElement;
    }
}
