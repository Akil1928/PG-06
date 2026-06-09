package model.graph;

import model.LinkedList.LinkedList;
import model.LinkedList.ListException;
import model.Node;
import model.Queue.LinkedQueue;
import model.Queue.QueueException;
import model.Stack.LinkedStack;
import model.Stack.StackException;

public class LinkedGraph<T extends Comparable<T>> extends LinkedList implements Graph<T> {
    public boolean directed;
    public LinkedStack<Integer> stack;
    public LinkedQueue<Integer> queue;

    public LinkedGraph(boolean directed) {
        super();
        this.directed = directed;
        stack = new LinkedStack<>();
        queue = new LinkedQueue<>();
    }

    @Override
    public boolean containsVertex(T element) throws GraphException, ListException {
        return contains(element);
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        boolean containsB = false;
        Node<T> nodeA = getNode(a);
        Node<T> nodeB = getNode(b);

        if (!directed)
            return !directed ? getNodeNeighbor(nodeA, b) != null && getNodeNeighbor(nodeB, a) != null
                    : getNodeNeighbor(nodeA, b) != null;

        return getNodeNeighbor(nodeA, b) != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("| ----- Linked Graph ----- |\n");
        String graphType = directed ? "Directed" : "Undirected";
        sb.append("Graph Type: ").append(graphType).append("\n");

        sb.append(super.toString());
        //mostramos los vertices y sus aristas
        try {
            int len = size();
            for(int i = 0; i < len; i++) {
                sb.append("\n(").append(get(i)).append(")---- Vertex [").append(getNodeByIndex(i).data).append("]");
                Node<T> aux = getNodeByIndex(i).neighbor;
                while(aux != null) {
                    sb.append("\n Edge: \"").append(aux.data).append(", weight: ").append(aux.weight);
                    aux = aux.neighbor;
                }
            }
        } catch (ListException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    @Override
    public void addVertex(T element) throws GraphException, ListException {
    if(isEmpty()) add(element);
    else if(!containsVertex(element)) add(element); //si no existe el vertice, agrego el elemento
    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
    if(containsVertex(a)|| !containsVertex(b))
        throw new GraphException("Linked Graph is Empty");
if(!containsEdge(a, b)) {
    Node<T> nodeA = getNode(a);
    //a partir del nodo A construya la lista de vecinos
    addNeighbor(nodeA,b,null);
    if(!directed) {
        Node<T> nodeB = getNode(b);
        //a partir del nodo B construya la lista de vecinos
        addNeighbor(nodeB,a,null); //null para el peso
    }
}
    }

    private void addNeighbor(Node<T> headnode, T element, Object weight) {
        Node<T> node = new Node<>(element, weight);
        if (headnode.neighbor == null) {
            headnode.neighbor = node;
        } else {
            Node<T> aux = headnode.neighbor;
            //me muevo por la lista hasta el ultimo nodo
            while (aux.neighbor != null)
                aux = aux.neighbor; //se mueve al siguiente nodo vecino
            //se sale cuando aux.neighbor es null
            aux.neighbor = node;//entonces conectamos el nodo final
        }
    }

    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {
        if(containsEdge(a, b)) {
            Node<T> nodeA = getNode(a);
            //recuperamos la lista de vecinos del nodo A para settear el peso
            getNodeNeighbor(nodeA, b).weight = weight; //null para el peso
            if (!directed) {
                Node<T> nodeB = getNode(b);
                //recuperamos la lista de vecinos del nodo B para settear el peso
                getNodeNeighbor(nodeB, a).weight = weight; //null para el peso
            }
        }
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T c) throws GraphException, ListException {

    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
        if (containsVertex(element))
            throw new GraphException("Adjacency list Graph is Empty");

        remove(element); //eliminamos el vertice del grafo
        //buscamos el rastro del vertice en las listas enlazadas de vecinos de los otros vertices
        int len = size();
        for (int i = 0; i < len; i++) {
            Node<T> node = getNodeByIndex(i);
            removeNeighbor(node, element);
        }
    }

    private void removeNeighbor(Node<T> headNode, T element) throws ListException {
        if(headNode.neighbor==null) throw new ListException("Linked List in Graph is Empty");

        //Caso 1. el elemento a suprimir es el primero
        if(equals(headNode.neighbor.data, element)) {
            headNode.neighbor = headNode.neighbor.neighbor; //queda apuntando al sgte nodo vecino
        }
        //Caso 2. El elemento a suprimir puede estar en medio o al final de la lista
        else {
            Node<T> prev = headNode.neighbor; //anterior
            while(prev.neighbor!=null) {
                if(equals(prev.neighbor.data, element)){
                    Node<T> removed = prev.neighbor; //es el nodo a eliminar
                    //desenlaza el nodo
                    prev.neighbor = removed.neighbor;
                }
                prev = prev.neighbor; //lo movemos al sgte vecino
            }
        }
    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Linked Graph Graph Not Contains Vertex");

        if (!containsEdge(a, b))
            throw new GraphException("Linked Graph Graph Not Contains Edge");

        Node<T> nodeA = getNode(a);
        removeNeighbor(nodeA, b);
        if (!directed) {
            Node<T> nodeB = getNode(b);
            removeNeighbor(nodeB, a);
        }
    }

    @Override
    public String dfs() throws GraphException, StackException, ListException {
        return "";
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        return "";
    }

    private Node<T> getNodeNeighbor(Node<T> headnode, T element) {
        if (headnode.neighbor == null) return null;
        Node<T> aux = headnode.neighbor;
        while (aux != null) {
            if (aux.data.compareTo(element) == 0) return aux;
            aux = aux.neighbor;
        }
        return null;//si llega no encontro el nodo
    }

    /// // AYUDAS ////////////
    public boolean equals(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }

    // Método genérico de comparación
    public int compareElements(T a, T b) {
        return a.compareTo(b);
    }
}
