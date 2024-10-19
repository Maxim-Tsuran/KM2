package ru.mpei;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TripletDeque<T> implements Deque<T>, Containerable {

    private Container<T> firstContainer;    // Первый контейнер
    private Container<T> lastContainer;     // Последний контейнер
    private int size = 0;   // Кол-во элементов в общем
    private int maxSize = 1000;
    private int containerCapacity = 5;    // Размер контейнера
    private int current_addFirst = 0;
    private int current_addLast = 0;

    public TripletDeque() {
        Container<T> container = new Container<>(containerCapacity);
        this.firstContainer = container;
        this.lastContainer = container;
    }

    public TripletDeque(int containerCapacity, int maxSize) {    // Можно задать размер контейнера
        this.containerCapacity = containerCapacity;
        this.maxSize = maxSize;
        Container<T> container = new Container<>(containerCapacity);
        this.firstContainer = container;
        this.lastContainer = container;
    }

    @Override
    //объект данных добавляется в последний контейнер, в первую свободную ячейку слева.
    public void addLast(T element) {

        if (size >= maxSize) {
            throw new IllegalStateException("Превышен размер очереди");
        }

        if (current_addLast == 0){
            lastContainer.setLastElement(lastContainer.getLastElement() + 1);
            // добавляем элемент
            lastContainer.setArray(lastContainer.getLastElement(), element);
            size++;
            current_addFirst++;
        }
        else {
            // Если последний контейнер заполнен, добавляем новый контейнер и пере привязываем ссылки
            if (lastContainer.getLastElement() - 1 == containerCapacity) {
                Container<T> newContainer = new Container<>(containerCapacity);
                newContainer.setPrev(lastContainer);
                lastContainer.setNext(newContainer);
                lastContainer = newContainer;
                lastContainer.setLastElement(0);
            }
            lastContainer.setArray(lastContainer.getLastElement(), element);
            current_addFirst++;
            size++;
        }


    }

    @Override
    public void addFirst(T element) {

        if (size >= maxSize) {
            throw new IllegalStateException("Превышен размер очереди");
        }
        if (current_addFirst == 0){
            firstContainer.setFirstElement(containerCapacity-1); // Новый контейнер начинается с конца
            firstContainer.setLastElement(containerCapacity); // Новый контейнер начинается с конца
            // добавляем элемент
            firstContainer.setArray(firstContainer.getFirstElement(), element);
            size++;
            current_addFirst++;
        }
        else {
            // Если первый контейнер заполнен, создаём новый контейнер и связываем с первым
            if (firstContainer.getFirstElement() == 0) {
                Container<T> newContainer = new Container<>(containerCapacity);
                newContainer.setNext(firstContainer);
                firstContainer.setPrev(newContainer);
                firstContainer = newContainer;
                firstContainer.setFirstElement(containerCapacity); // Новый контейнер начинается с конца
            }
            // Уменьшаем индекс firstElement и добавляем элемент
            firstContainer.setFirstElement(firstContainer.getFirstElement() - 1);
            firstContainer.setArray(firstContainer.getFirstElement(), element);
            size++;
            current_addFirst++;

        }

    }

    @Override
    public boolean offerFirst(T element) {
        try {
            addFirst(element);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    @Override
    public boolean offerLast(T element) {
        try {
            addLast(element);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    @Override
    public T removeFirst() {

        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }
        return pollFirst();
    }

    @Override
    public T removeLast() {

        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }
        return pollLast();    }

    @Override
    public T pollLast() {

        if (size == 0) {
            return null;
        }
        T element = (T) lastContainer.getArray()[lastContainer.getLastElement()-1];
        lastContainer.setArray(lastContainer.getLastElement()-1, null);

        if (lastContainer.getLastElement()-1 != 0){
            lastContainer.setLastElement(lastContainer.getLastElement()-1);
        }
        else{
            if(lastContainer.getPrev() != null){
                T prev = (T) lastContainer.getPrev();
                this.lastContainer.setPrev(null);
                this.lastContainer = (Container<T>) prev;
                this.lastContainer.setNext(null);
            }
        }
        size--;
        return element;
    }

    @Override
    public T pollFirst() {
        if (size == 0) {
            return null;
        }
        T element = (T) firstContainer.getArray()[firstContainer.getFirstElement()];
        firstContainer.setArray(firstContainer.getFirstElement(), null);

        if (firstContainer.getFirstElement() != containerCapacity - 1){
            firstContainer.setFirstElement(firstContainer.getFirstElement()+1);
        }
        else{
            if(firstContainer.getNext() != null){
                T next = (T) firstContainer.getNext();
                this.firstContainer.setNext(null);
                this.firstContainer = (Container<T>) next;
                this.firstContainer.setPrev(null);
            }
        }
        size--;
        return element;
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

    // Удаляем первое появление элемента слева. При удалении остальные элементы контейнера сдвигаются влево, чтобы не
    // было дырок с null, а в конце добавляем null
    @Override
    public boolean removeFirstOccurrence(Object o) {

        Container<T> current = firstContainer;
        while (current != null) {
            for (int i = 0; i < containerCapacity; i++) {
                if (o.equals(current.getArray()[i])) {
                    for (int j = i; j < containerCapacity - 1; j++) {
                        current.getArray()[j] = current.getArray()[j+1];
                    }
                    current.getArray()[current.getLastElement() - 1] = null;
                    current.setLastElement(current.getLastElement() - 1);
                    size--;
                    return true;
                }
            }
            current = current.next;
        }
        return false;
    }

    // Удаляем первое появление элемента справа. При удалении остальные элементы контейнера сдвигаются влево, чтобы не
    // было дырок с null, а в конце добавляем null
    @Override
    public boolean removeLastOccurrence(Object o) {

        Container<T> current = firstContainer;
        while (current != null) {
            for (int i = containerCapacity - 1; i >= 0; i--) {
                if (o.equals(current.getArray()[i])) {
                    for (int j = i; j < containerCapacity - 1; j++) {
                        current.getArray()[j] = current.getArray()[j + 1];
                    }
                    current.getArray()[current.getLastElement() - 1] = null;
                    current.setLastElement(current.getLastElement() - 1);
                    size--;
                    return true;
                }
            }
            current = current.next;
        }
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
        Container<T> container = new Container<>(containerCapacity);
        this.firstContainer = container;
        this.lastContainer = container;
        size = 0;
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
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    // Проверяем, есть ли элемент в очереди
    @Override
    public boolean contains(Object elements) {
        Container<T> current = firstContainer;
        while (current != null) {
            for (int i = 0; i < containerCapacity; i++) {
                if (elements.equals(current.getArray()[i])) {
                    return true;
                }
            }
            current = current.next;
        }
        return false;    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        }
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