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
        Node<T> nodeA = getNode(a);
        Node<T> nodeB = getNode(b);

        if (!directed)
            return getNodeNeighbor(nodeA, b) != null && getNodeNeighbor(nodeB, a) != null;

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
            for (int i = 1; i <= len; i++) {  // Índices base-1
                Node<T> node = getNodeByIndex(i);
                if (node != null) {
                    sb.append("\n(").append(get(i)).append(")---- Vertex [").append(node.data).append("]");
                    Node<T> aux = node.neighbor;
                    while (aux != null) {
                        sb.append("\n Edge: \"").append(aux.data).append(", weight: ").append(aux.weight).append("\"");
                        aux = aux.neighbor;
                    }
                }
            }
        } catch (ListException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    @Override
    public void addVertex(T element) throws GraphException, ListException {
        if (isEmpty()) add(element);
        else if (!containsVertex(element)) add(element);
    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Linked Graph Not Contains Vertex");
        if (!containsEdge(a, b)) {
            Node<T> nodeA = getNode(a);
            addNeighbor(nodeA, b, null);
            if (!directed) {
                Node<T> nodeB = getNode(b);
                addNeighbor(nodeB, a, null);
            }
        }
    }

    private void addNeighbor(Node<T> headnode, T element, Object weight) {
        Node<T> node = new Node<>(element, weight);
        if (headnode.neighbor == null) {
            headnode.neighbor = node;
        } else {
            Node<T> aux = headnode.neighbor;
            while (aux.neighbor != null)
                aux = aux.neighbor;
            aux.neighbor = node;
        }
    }

    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {
        if (containsEdge(a, b)) {
            Node<T> nodeA = getNode(a);
            getNodeNeighbor(nodeA, b).weight = weight;
            if (!directed) {
                Node<T> nodeB = getNode(b);
                getNodeNeighbor(nodeB, a).weight = weight;
            }
        }
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T c) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b)) {
            throw new GraphException("Cannot add edge. One or both vertices do not exist in the graph.");
        }

        if (!containsEdge(a, b)) {
            Node<T> nodeA = getNode(a);
            addNeighbor(nodeA, b, c);
        } else {
            Node<T> nodeA = getNode(a);
            getNodeNeighbor(nodeA, b).weight = c;
        }

        if (!directed) {
            if (!containsEdge(b, a)) {
                Node<T> nodeB = getNode(b);
                addNeighbor(nodeB, a, c);
            } else {
                Node<T> nodeB = getNode(b);
                getNodeNeighbor(nodeB, a).weight = c;
            }
        }
    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
        if (!containsVertex(element))
            throw new GraphException("Linked Graph Not Contains Vertex");

        remove(element);
        int len = size();
        for (int i = 1; i <= len; i++) {  // Índices base-1
            Node<T> node = getNodeByIndex(i);
            if (node != null && node.neighbor != null) {
                removeNeighbor(node, element);
            }
        }
    }

    private void removeNeighbor(Node<T> headNode, T element) {
        if (headNode.neighbor == null) return;

        // Caso 1: el elemento a suprimir es el primero
        if (equals(headNode.neighbor.data, element)) {
            headNode.neighbor = headNode.neighbor.neighbor;
            return;
        }

        // Caso 2: el elemento está en medio o al final
        Node<T> prev = headNode.neighbor;
        while (prev != null && prev.neighbor != null) {
            if (equals(prev.neighbor.data, element)) {
                Node<T> removed = prev.neighbor;
                prev.neighbor = removed.neighbor;
                break;
            }
            prev = prev.neighbor;
        }
    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Linked Graph Not Contains Vertex");

        if (!containsEdge(a, b))
            throw new GraphException("Linked Graph Not Contains Edge");

        Node<T> nodeA = getNode(a);
        removeNeighbor(nodeA, b);
        if (!directed) {
            Node<T> nodeB = getNode(b);
            removeNeighbor(nodeB, a);
        }
    }

    @Override
    public String dfs() throws GraphException, StackException, ListException {
        if (isEmpty()) throw new GraphException("Linked Graph is Empty");

        int numVertices = size();
        boolean[] visited = new boolean[numVertices + 1]; // +1 para índices base-1
        StringBuilder info = new StringBuilder();

        stack.clear();

        // Inicia en el vértice 1 (primer elemento)
        info.append(get(1)).append(", ");
        visited[1] = true;
        stack.push(1);

        while (!stack.isEmpty()) {
            int index = adjacentVertexNotVisitedByList((int) stack.top(), visited);
            if (index == -1)
                stack.pop();
            else {
                visited[index] = true;
                info.append(get(index)).append(", ");
                stack.push(index);
            }
        }

        return info.toString();
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        if (isEmpty()) throw new GraphException("Linked Graph is Empty");

        int numVertices = size();
        boolean[] visited = new boolean[numVertices + 1]; // +1 para índices base-1
        StringBuilder info = new StringBuilder();

        queue.clear();

        // Inicia en el vértice 1 (primer elemento)
        info.append(get(1)).append(", ");
        visited[1] = true;
        queue.enQueue(1);
        int v2;

        while (!queue.isEmpty()) {
            int v1 = (int) queue.deQueue();
            while ((v2 = adjacentVertexNotVisitedByList(v1, visited)) != -1) {
                visited[v2] = true;
                info.append(get(v2)).append(", ");
                queue.enQueue(v2);
            }
        }

        return info.toString();
    }

    private int adjacentVertexNotVisitedByList(int index, boolean[] visited) throws ListException {
        Node<T> currentVertex = getNodeByIndex(index);
        if (currentVertex == null) return -1;

        Node<T> neighbor = currentVertex.neighbor;

        while (neighbor != null) {
            int neighborIndex = getIndexOfVertex(neighbor.data);
            if (neighborIndex != -1 && !visited[neighborIndex])
                return neighborIndex;
            neighbor = neighbor.neighbor;
        }
        return -1;
    }

    private Node<T> getNodeNeighbor(Node<T> headnode, T element) {
        if (headnode == null || headnode.neighbor == null) return null;
        Node<T> aux = headnode.neighbor;
        while (aux != null) {
            if (aux.data.compareTo(element) == 0) return aux;
            aux = aux.neighbor;
        }
        return null;
    }

    private int getIndexOfVertex(T element) throws ListException {
        for (int i = 1; i <= size(); i++) {  // Índices base-1
            if (get(i).equals(element)) {
                return i;
            }
        }
        return -1;
    }

    public boolean equals(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }

    public int compareElements(T a, T b) {
        return a.compareTo(b);
    }
}