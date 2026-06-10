package model.LinkedList;

import model.Node;

public class LinkedList <T> implements List<T> {

    private Node<T> head;//inicio de la lista
    private Node<T> tail;//final de la lista


    @Override
    public int size() throws ListException {
        if(isEmpty()){
            throw new ListException("Linked List is empty");
        }
        Node<T> aux = head;
        int count = 0;
        while(aux!= null){
            count++;
            aux = aux.next;
        }
        return count;
    }

    @Override
    public void clear() {
        head = tail = null;

    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public void add(T element) {
        Node<T> node = new Node<>(element);
        if(head == null){
            head = node;
            tail = node;
        }else{
            //significa que head apunta a un nodo existente
            Node<T> aux = head;
            //me muevo por la lista hasta alcanzar el ultimo elemento
            while(aux.next != null){
                //aux.next es la flecha
                aux = aux.next;//lo mueve al siguiente nodo
            }
            //cuando se sale del while, aux.next es igual a null
            aux.next = node;
            tail = node;//lo ponemos a apuntar al ultimo nodo de la lista
        }
    }

    @Override
    public void addFirst(T element) {
        Node<T> node = new Node<>(element);
        node.next = head;
        head = node;//para que el nuevo nodo quede de primero
        if(tail==null) tail = node; //si la lista estaba vacia
    }

    @Override
    public void addLast(T element) {
        add(element);
    }

    @Override
    public void addInSortedList(T element) {
        //por implementar
    }

    @Override
    public void remove(T element) throws ListException {
        if (isEmpty()) {
            throw new ListException("Linked List is empty");
        }

        // Case 1: Element to remove is the head
        if (equals(head.data, element)) {
            head = head.next;
            if (head == null) { // List became empty
                tail = null;
            }
            return;
        }

        // Case 2: Element to remove is in the middle or at the tail
        Node<T> prev = head;
        Node<T> current = head.next;
        while (current != null) {
            if (equals(current.data, element)) {
                prev.next = current.next;
                if (current == tail) { // Removed the tail node
                    tail = prev;
                }
                return;
            }
            prev = current;
            current = current.next;
        }
        // If element was not found, no action needed or throw an exception
        // For now, assuming it's fine if element is not found.
    }


    @Override
    public T removeFirst() throws ListException {
        if(isEmpty()){
            throw new ListException("Linked List is empty");
        }
        T first = head.data;
        head = head.next;
        if(head==null) tail = null;
        return first;
    }

    @Override
    public T removeLast() throws ListException {
        if(isEmpty()){
            throw new ListException("Linked List is empty");
        }
        if(head.next == null){ //solo un elemento
            T last = head.data;
            clear();
            return last;
        }
        Node<T> aux = head;
        Node<T> prev = head;
        while (aux.next != null){
            prev = aux;
            aux = aux.next;
        }
        T last = aux.data;
        prev.next = null;
        tail = prev;
        return last;
    }

    @Override
    public boolean contains(T element) throws ListException {
        if(isEmpty()){
            throw new ListException("Linked List is empty");
        }
        Node<T> aux = head;
        while(aux!= null){
            if(equals(aux.data, element)) return true;
            aux = aux.next;
        }
        return false;
    }

    @Override
    public void sort() throws ListException {
    }

    @Override
    public int indexOf(T element) throws ListException {
        if(isEmpty()){
            throw new ListException("Linked List is empty");
        }
        Node<T> aux = head;
        int index = 0; // Changed from 1 to 0 for 0-based indexing
        while(aux!= null){
            if(equals(aux.data, element)) return index;
            index++;
            aux = aux.next;
        }
        return -1;
    }

    @Override
    public T getFirst() throws ListException {
        if (isEmpty()){
            throw new ListException("Linked list is empty");
        }
        return head.data;
    }

    @Override
    public T getLast() throws ListException {
        if (isEmpty()){
            throw new ListException("Linked list is empty");
        }
        return tail.data;
    }

    @Override
    public T getPrev(T element) throws ListException {
        if(isEmpty() || equals(head.data, element)) return null;
        Node<T> aux = head;
        while (aux.next != null){
            if(equals(aux.next.data, element)){
                return aux.data;
            }
            aux = aux.next;
        }
        return null;
    }

    @Override
    public T getNext(T element) throws ListException {
        if(isEmpty()) return null;
        Node<T> aux = head;
        while (aux != null){
            if(equals(aux.data, element)){
                if(aux.next != null) return aux.next.data;
                else return null;
            }
            aux = aux.next;
        }
        return null;
    }

    @Override
    public T get(int index) throws ListException {
        if (isEmpty()){
            throw new ListException("Linked list is empty");
        }
        Node<T> aux = head;
        int count = 0; // Changed from 1 to 0 for 0-based indexing
        while(aux != null){
            if (count == index) return aux.data;
            count++;
            aux = aux.next;
        }
        return null;
    }

    public Node<T> getTail() {
        return tail;
    }

    public Node<T> getHead() {
        return head;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("HEAD → ");
        Node<T> aux = head;
        while(aux != null){
            sb.append("[").append(aux.data).append("]");
            if(aux.next != null) sb.append(" → ");
            aux = aux.next;
        }
        sb.append(" → NULL");
        return sb.toString();
    }

    public Node<T> getNodeByIndex(int index) throws ListException{
        if(isEmpty()){
            throw new ListException("Linked list is empty");
        }
        Node<T> aux = head;
        int pos = 0; // Changed from 1 to 0 for 0-based indexing
        while(aux != null){
            if (pos == index) return aux;
            aux = aux.next;
            pos++;
        }
        return null;
    }

    public Node<T> getNode(T element) throws ListException {
        if(isEmpty()) {
            throw new ListException("Linked List is empty");
        }
        Node<T> aux = head;
        while(aux!=null){
            if(equals(aux.data, element)) return aux;
            aux = aux.next;
        }
        return null;
    }

    private boolean equals (T a, T b){
        return  a == null ? b == null : a.equals(b);
    }
}