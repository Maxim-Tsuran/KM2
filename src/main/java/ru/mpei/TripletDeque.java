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

    public TripletDeque(int containerCapacity, int maxSize) {    // Можно задать размер контейнера и очереди
        this.containerCapacity = containerCapacity;
        this.maxSize = maxSize;
        Container<T> container = new Container<>(containerCapacity);
        this.firstContainer = container;
        this.lastContainer = container;
    }

    @Override
    //объект данных добавляется в последний контейнер, в первую свободную ячейку слева.
    public void addLast(T element) {

        if (element == null)
            throw new NullPointerException("Попытка добавления null");

        if (size >= maxSize) {
            throw new IllegalStateException("Превышен размер очереди");
        }
        if (current_addLast == 0 && lastContainer.getLastElement() == 0){

            lastContainer.setArray(0, element);
            current_addLast++;
        } else{
            // Если последний контейнер заполнен, добавляем новый контейнер и пере привязываем ссылки
            if (lastContainer.getLastElement() >= containerCapacity - 1) {
                Container<T> newContainer = new Container<>(containerCapacity);
                newContainer.setPrev(lastContainer);
                lastContainer.setNext(newContainer);
                lastContainer = newContainer;
                lastContainer.setLastElement(0);
            }
            if (size == 0 || lastContainer.getArray()[lastContainer.getLastElement()] == null){
                lastContainer.setLastElement(0);
                lastContainer.setArray(lastContainer.getLastElement(),element);

            } else{
                lastContainer.setArray(lastContainer.getLastElement() + 1, element);
                lastContainer.setLastElement(lastContainer.getLastElement() + 1);
                current_addLast++;
            }
        }

        size++;
    }

    @Override
    public void addFirst(T element) {

        if (element == null)
            throw new NullPointerException("Попытка добавления null");
        if (size >= maxSize) {
            throw new IllegalStateException("Превышен размер очереди");
        }
        if (current_addFirst == 0 ){
            firstContainer.setFirstElement(containerCapacity-1); // Новый контейнер начинается с конца
            firstContainer.setLastElement(containerCapacity-1); // Новый контейнер начинается с конца
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
                firstContainer.setLastElement(containerCapacity-1);
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

        if (firstContainer.getArray()[firstContainer.getFirstElement()] == null){
            firstContainer.setFirstElement(0);
        }
        T element = (T) firstContainer.getArray()[firstContainer.getFirstElement()];
        firstContainer.setArray(firstContainer.getFirstElement(), null);

        if (firstContainer.getFirstElement() != containerCapacity - 1 &&
                firstContainer.getArray()[firstContainer.getFirstElement() + 1] != null) {

            firstContainer.setFirstElement(firstContainer.getFirstElement()+1);
            firstContainer.setLastElement(firstContainer.getLastElement()-1);
        }
        else{
            if(firstContainer.getNext() != null){
                T next = (T) firstContainer.getNext();
                this.firstContainer.setNext(null);
                this.firstContainer = (Container<T>) next;
                this.firstContainer.setPrev(null);
            }
            firstContainer.setLastElement(0);
        }
        size--;
        return element;
    }

    @Override
    public T removeLast() {

        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }

        T element = (T) lastContainer.getArray()[lastContainer.getLastElement()];
        lastContainer.setArray(lastContainer.getLastElement(), null);

        if (lastContainer.getLastElement() != 0 &&
                lastContainer.getArray()[lastContainer.getLastElement() - 1] != null){
            lastContainer.setLastElement(lastContainer.getLastElement()-1);
        }
        else{
            if(lastContainer.getPrev() != null){
                T prev = (T) lastContainer.getPrev();
                this.lastContainer.setPrev(null);
                this.lastContainer = (Container<T>) prev;
                this.lastContainer.setNext(null);
            }
            lastContainer.setFirstElement(containerCapacity);
        }
        size--;
        return element;
    }

    @Override
    public T pollLast() {

        if (size == 0) {
            return null;
        }
        T element = (T) lastContainer.getArray()[lastContainer.getLastElement()];
        lastContainer.setArray(lastContainer.getLastElement(), null);

        if (lastContainer.getLastElement() != 0 &&
                lastContainer.getArray()[lastContainer.getLastElement() - 1] != null){
            lastContainer.setLastElement(lastContainer.getLastElement()-1);
        }
        else{
            if(lastContainer.getPrev() != null){
                T prev = (T) lastContainer.getPrev();
                this.lastContainer.setPrev(null);
                this.lastContainer = (Container<T>) prev;
                this.lastContainer.setNext(null);
            }
            lastContainer.setFirstElement(containerCapacity);
        }
        size--;
        return element;
    }

    @Override
    public T pollFirst() {
        if (size == 0) {
            return null;
        }
        if (firstContainer.getArray()[firstContainer.getFirstElement()] == null){
            firstContainer.setFirstElement(0);
        }
        T element = (T) firstContainer.getArray()[firstContainer.getFirstElement()];
        firstContainer.setArray(firstContainer.getFirstElement(), null);

        if (firstContainer.getFirstElement() != containerCapacity - 1 &&
                firstContainer.getArray()[firstContainer.getFirstElement() + 1] != null) {

            firstContainer.setFirstElement(firstContainer.getFirstElement()+1);
            firstContainer.setLastElement(firstContainer.getLastElement()-1);
        }
        else{
            if(firstContainer.getNext() != null){
                T next = (T) firstContainer.getNext();
                this.firstContainer.setNext(null);
                this.firstContainer = (Container<T>) next;
                this.firstContainer.setPrev(null);
            }
            firstContainer.setLastElement(0);
        }
        size--;
        return element;
    }

    @Override
    public T getFirst() {
        if (size == 0){
            throw new NoSuchElementException("Элементов нет");
        } else{
            T[] array = (T[]) firstContainer.getArray();
            return array[firstContainer.getFirstElement()];
        }
    }

    @Override
    public T getLast() {
        if (size == 0){
            throw new NoSuchElementException("Элементов нет");
        } else{
            T[] array = (T[]) lastContainer.getArray();
            return array[lastContainer.getLastElement()];
        }
    }

    @Override
    public T peekFirst() {
        return firstContainer.getArray()[firstContainer.getFirstElement()];
    }

    @Override
    public T peekLast() {
        return lastContainer.getArray()[lastContainer.getLastElement()];
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

                    current.setArray(current.getLastElement(), null);
                    current.setLastElement(current.getLastElement() - 1);
                    size--;

                    if (current.getLastElement() == -1){
                        if (current == firstContainer) {
                            firstContainer = current.getNext();
                        }
                        if (current == lastContainer) {
                            lastContainer = current.getPrev();
                        }

                        if (current.getPrev() != null) {
                            current.getPrev().setNext(current.getNext());
                        }
                        if (current.next != null) {
                            current.getNext().setPrev(current.getPrev());
                        };
                    }
                    return true;

                }
            }
            current = current.getNext();
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
            current = current.getNext();
        }
        return false;
    }

    @Override
    public boolean add(T t) {
        try {
            addLast(t);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    @Override
    public boolean offer(T t) {
        try {
            addLast(t);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    @Override
    public T remove() {
        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }
        if (firstContainer.getArray()[firstContainer.getFirstElement()] == null){
            firstContainer.setFirstElement(0);
        }
        T element = (T) firstContainer.getArray()[firstContainer.getFirstElement()];
        firstContainer.setArray(firstContainer.getFirstElement(), null);

        if (firstContainer.getFirstElement() != containerCapacity - 1 &&
                firstContainer.getArray()[firstContainer.getFirstElement() + 1] != null) {

            firstContainer.setFirstElement(firstContainer.getFirstElement()+1);
            firstContainer.setLastElement(firstContainer.getLastElement()-1);
        }
        else{
            if(firstContainer.getNext() != null){
                T next = (T) firstContainer.getNext();
                this.firstContainer.setNext(null);
                this.firstContainer = (Container<T>) next;
                this.firstContainer.setPrev(null);
            }
            firstContainer.setLastElement(0);
        }
        size--;
        return element;
    }

    @Override
    public T poll() {
        if (size == 0) {
            return null;
        }
        if (firstContainer.getArray()[firstContainer.getFirstElement()] == null){
            firstContainer.setFirstElement(0);
        }
        T element = (T) firstContainer.getArray()[firstContainer.getFirstElement()];
        firstContainer.setArray(firstContainer.getFirstElement(), null);

        if (firstContainer.getFirstElement() != containerCapacity - 1 &&
                firstContainer.getArray()[firstContainer.getFirstElement() + 1] != null) {

            firstContainer.setFirstElement(firstContainer.getFirstElement()+1);
            firstContainer.setLastElement(firstContainer.getLastElement()-1);
        }
        else{
            if(firstContainer.getNext() != null){
                T next = (T) firstContainer.getNext();
                this.firstContainer.setNext(null);
                this.firstContainer = (Container<T>) next;
                this.firstContainer.setPrev(null);
            }
            firstContainer.setLastElement(0);
        }
        size--;
        return element;
    }

    @Override
    public T element() {
        if (size == 0){
            throw new NoSuchElementException("Элементов нет");
        } else{
            T[] array = (T[]) firstContainer.getArray();
            return array[firstContainer.getFirstElement()];
        }
    }

    @Override
    public T peek() {
        return firstContainer.getArray()[firstContainer.getFirstElement()];
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        Iterator<? extends T> iterator = c.iterator();

        while (iterator.hasNext()) {
            T element = iterator.next();
            if (!offerLast(element)) {
                return false;
            }
        }
        return true;
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
        if (t == null)
            throw new NullPointerException("Попытка добавления null");
        if (size >= maxSize) {
            throw new IllegalStateException("Превышен размер очереди");
        }
        if (current_addFirst == 0 ){
            firstContainer.setFirstElement(containerCapacity-1); // Новый контейнер начинается с конца
            firstContainer.setLastElement(containerCapacity-1); // Новый контейнер начинается с конца
            // добавляем элемент
            firstContainer.setArray(firstContainer.getFirstElement(), t);
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
                firstContainer.setLastElement(containerCapacity-1);
            }
            // Уменьшаем индекс firstElement и добавляем элемент
            firstContainer.setFirstElement(firstContainer.getFirstElement() - 1);
            firstContainer.setArray(firstContainer.getFirstElement(), t);
            size++;
            current_addFirst++;

        }

    }

    @Override
    public T pop() {
        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }
        if (firstContainer.getArray()[firstContainer.getFirstElement()] == null){
            firstContainer.setFirstElement(0);
        }
        T element = (T) firstContainer.getArray()[firstContainer.getFirstElement()];
        firstContainer.setArray(firstContainer.getFirstElement(), null);

        if (firstContainer.getFirstElement() != containerCapacity - 1 &&
                firstContainer.getArray()[firstContainer.getFirstElement() + 1] != null) {

            firstContainer.setFirstElement(firstContainer.getFirstElement()+1);
            firstContainer.setLastElement(firstContainer.getLastElement()-1);
        }
        else{
            if(firstContainer.getNext() != null){
                T next = (T) firstContainer.getNext();
                this.firstContainer.setNext(null);
                this.firstContainer = (Container<T>) next;
                this.firstContainer.setPrev(null);
            }
            firstContainer.setLastElement(0);
        }
        size--;
        return element;
    }

    @Override
    public boolean remove(Object o) {
        Container<T> current = firstContainer;
        while (current != null) {
            for (int i = 0; i < containerCapacity; i++) {
                if (o.equals(current.getArray()[i])) {

                    for (int j = i; j < containerCapacity - 1; j++) {
                        current.getArray()[j] = current.getArray()[j+1];
                    }

                    current.setArray(current.getLastElement(), null);
                    current.setLastElement(current.getLastElement() - 1);
                    size--;

                    if (current.getLastElement() == -1){
                        if (current == firstContainer) {
                            firstContainer = current.getNext();
                        }
                        if (current == lastContainer) {
                            lastContainer = current.getPrev();
                        }

                        if (current.getPrev() != null) {
                            current.getPrev().setNext(current.getNext());
                        }
                        if (current.next != null) {
                            current.getNext().setPrev(current.getPrev());
                        };
                    }
                    return true;

                }
            }
            current = current.getNext();
        }
        return false;
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
        return false;
    }

    @Override
    public int size() {
        return size;
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
        NewIterator iterator =new NewIterator (firstContainer, containerCapacity);
        return iterator;
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
        throw new UnsupportedOperationException("Метод не реализован");
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