package ru.mpei;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class TripletDeque<T> implements Deque<T>, Containerable {

    private Container<T> firstContainer;    // Первый контейнер
    private Container<T> lastContainer;     // Последний контейнер
    private int size = 0;   // Кол-во элементов в общем
    private int containerCapacity = 5;    // Размер контейнера

    public TripletDeque() {
        Container<T> container = new Container<>(containerCapacity);
        this.firstContainer = container;
        this.lastContainer = container;
    }

    public TripletDeque(int containerCapacity) {    // Можно задать размер контейнера
        this.containerCapacity = containerCapacity;
        Container<T> container = new Container<>(containerCapacity);
        this.firstContainer = container;
        this.lastContainer = container;
    }

    @Override
    //объект данных добавляется в последний контейнер, в первую свободную ячейку слева.
    public void addLast(T element) {

        // Если последний контейнер заполнен, добавляем новый контейнер и пере привязываем ссылки
        if (lastContainer.getLastElement() == containerCapacity) {
            Container<T> newContainer = new Container<>(containerCapacity);
            newContainer.setPrev(lastContainer);
            lastContainer.setNext(newContainer);
            lastContainer = newContainer;
            lastContainer.setLastElement(0);
        }
        lastContainer.setArray(lastContainer.getLastElement(), element);
        lastContainer.setLastElement(lastContainer.getLastElement() + 1);

        size++;

    }

    @Override
    public void addFirst(T element) {

        // Если первый контейнер заполнен, создаём новый контейнер и связываем с первым
        if (firstContainer.getFirstElement() == 0) {
            Container<T> newContainer = new Container<>(containerCapacity);
            newContainer.setNext(firstContainer);
            firstContainer.setPrev(newContainer);
            firstContainer = newContainer;
            firstContainer.setFirstElement(containerCapacity); // Новый контейнер начинается с конца
            firstContainer.setLastElement(containerCapacity);
        }
        // Уменьшаем индекс firstElement и добавляем элемент
        firstContainer.setFirstElement(firstContainer.getFirstElement() - 1);
        firstContainer.setArray(firstContainer.getFirstElement(), element);
        size++;

    }

    @Override
    public boolean offerFirst(T t) {
        return false;
    }

    @Override
    public boolean offerLast(T t) {
        return false;
    }

    @Override
    public T removeFirst() {
        return null;
    }

    @Override
    public T removeLast() {
        return null;
    }

    @Override
    public T pollFirst() {
        return null;
    }

    @Override
    public T pollLast() {
        return null;
    }

    @Override
    public T getFirst() {
        T[] array = (T[]) firstContainer.getArray();
        return array[firstContainer.getFirstElement()];
    }

    @Override
    public T getLast() {
        T[] array = (T[]) lastContainer.getArray();
        return array[lastContainer.getLastElement()-1];
    }

    @Override
    public T peekFirst() {
        return null;
    }

    @Override
    public T peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean add(T t) {
        return false;
    }

    @Override
    public boolean offer(T t) {
        return false;
    }

    @Override
    public T remove() {
        return null;
    }

    @Override
    public T poll() {
        return null;
    }

    @Override
    public T element() {
        return null;
    }

    @Override
    public T peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void push(T t) {

    }

    @Override
    public T pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public Iterator<T> descendingIterator() {
        return null;
    }

    public Object[] getContainerByIndex(int cIndex) {
        Container<T> current = firstContainer;
        int currentIndex = 0;

        // Идем по контейнерам до нужного индекса
        while (current != null && currentIndex < cIndex) {
            current = current.next;
            currentIndex++;
        }

        if (current != null) {
            // Возвращаем содержимое контейнера, если нашли нужный контейнер
            return current.getArray();
        }

        return null;
    }
}