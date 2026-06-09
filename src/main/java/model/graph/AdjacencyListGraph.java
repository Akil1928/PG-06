package model.graph;

import model.LinkedList.ListException;
import model.Node;
import model.Queue.QueueException;
import model.Stack.StackException;

public class AdjacencyListGraph<T extends Comparable<T>> extends AdjacencyMatrixGraph<T> {
    public AdjacencyListGraph(int n, boolean directed) {
        super(n, directed);
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        boolean getVertexA = false;
        boolean getVertexB = false;
        Vertex<T> vertexA = getVertex(a);
        if (vertexA == null) return false;

        getVertexA = getNodeNeighbor(vertexA.headnode, b) != null;
        if (!directed) {
            Vertex<T> vertexB = getVertex(b);
            if (vertexB == null) return false;
            getVertexB = getNodeNeighbor(vertexB.headnode, a) != null;
        }
        return !directed ? getVertexA && getVertexB : getVertexA;
    }

    private Node<T> getNodeNeighbor(Node<T> headnode, T element) {
        if (headnode == null) return null;
        Node<T> aux = headnode;
        while (aux != null) {
            if (aux.data.compareTo(element) == 0) return aux;
            aux = aux.neighbor;
        }
        return null;
    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency list Graph Not Contains Vertex");
        if (!containsEdge(a, b)) {
            Vertex<T> vertexA = getVertex(a);
            vertexA.headnode = addNeighbor(vertexA.headnode, b, null);
            if (!directed) {
                Vertex<T> vertexB = getVertex(b);
                vertexB.headnode = addNeighbor(vertexB.headnode, a, null);
            }
        }
    }

    private Node<T> addNeighbor(Node<T> headnode, T element, Object weight) {
        Node<T> node = new Node<>(element, weight);
        if (headnode == null) {
            headnode = node;
        } else {
            Node<T> aux = headnode;
            while (aux.neighbor != null) {
                aux = aux.neighbor;
            }
            aux.neighbor = node;
        }
        return headnode;
    }

    private Vertex<T> getVertex(T element) {
        for (int i = 0; i < counter; i++)
            if (equals(vertexList[i].data, element))
                return vertexList[i];
        return null;
    }

    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency list Graph Not Contains Vertex");

        if (containsEdge(a, b)) {
            Vertex<T> vertexA = getVertex(a);
            Node<T> nodeB = getNodeNeighbor(vertexA.headnode, b);
            if (nodeB != null) {
                nodeB.weight = weight;
            }

            if (!directed) {
                Vertex<T> vertexB = getVertex(b);
                Node<T> nodeA = getNodeNeighbor(vertexB.headnode, a);
                if (nodeA != null) {
                    nodeA.weight = weight;
                }
            }
        }
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T weight) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency list Graph Not Contains Vertex");
        if (!containsEdge(a, b)) {
            Vertex<T> vertexA = getVertex(a);
            vertexA.headnode = addNeighbor(vertexA.headnode, b, weight);
            if (!directed) {
                Vertex<T> vertexB = getVertex(b);
                vertexB.headnode = addNeighbor(vertexB.headnode, a, weight);
            }
        }
    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
        if (containsVertex(element))
            throw new GraphException("Adjacency list Graph is Empty");

        int index = indexOf(element);
        if (index != -1) { //si el vertice existe en la lista de vertices
            for (int i = index; i < counter - 1; i++)
                vertexList[i] = vertexList[i + 1];
            counter--;//lo debemos decrementar por el vertice suprimido

            //ahora debemos buscar el rastro del vertice suprimido en las listas enlazadas de los otros vertices
            //de vecinos a los otros vertices
            for (int i = 0; i < counter; i++) {
                Vertex<T> vertex = vertexList[i];
                vertex.headnode = removeNeighbor(vertex.headnode, element);
            }

        }

    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency list Graph Not Contains Vertex");

        if (!containsEdge(a, b))
            throw new GraphException("Adjacency list Graph Not Contains Edge");

        Vertex<T> vertexA = getVertex(a);
        vertexA.headnode = removeNeighbor(vertexA.headnode, b);

        if (!directed) {
            Vertex<T> vertexB = getVertex(b);
            vertexB.headnode = removeNeighbor(vertexB.headnode, a);
        }
    }

    private Node<T> removeNeighbor(Node<T> headNode, T element) throws ListException {
        if (headNode == null) throw new ListException("Linked List is Empty");
        //El elemento a suprimir es el primero
        if(equals(headNode.data, element)) headNode = headNode.neighbor; //Queda apuntado al siguiente nodo vecino
            //Caso 2. El elemento a suprimir puede estar en medio o al final de la lista
        else{
            Node<T> prev = headNode;//anterior
            while(prev.neighbor!=null){
                if(equals(prev.neighbor.data, element)) {
                    Node<T> removed = prev.neighbor; //Es el nodo a eliminar
                    //Desenlaza el nodo
                    prev.neighbor = removed.neighbor;
                }
                prev = prev.neighbor; //Lo movemos al siguiente vecino
            }

        }
        return headNode;//Modificado sin el nodo eliminado
    }

    @Override
    public String dfs() throws GraphException, StackException, ListException {
        return super.dfs();
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        return super.bfs();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency List Graph:\n");
        String graphType = directed ? "Directed" : "Undirected";
        sb.append("Graph Type: ").append(graphType).append("\n");

        // Mostrar todos los vértices y sus vecinos
        for (int i = 0; i < counter; i++) {
            sb.append("\nVertex [").append(i).append("]: ").append(vertexList[i].data);
            sb.append(" -> Neighbors: ");

            Node<T> aux = vertexList[i].headnode;
            if (aux == null) {
                sb.append("none");
            } else {
                while (aux != null) {
                    sb.append(aux.data);
                    if (aux.weight != null) {
                        sb.append(" (weight: ").append(aux.weight).append(")");
                    }
                    if (aux.neighbor != null) {
                        sb.append(" -> ");
                    }
                    aux = aux.neighbor;
                }
            }
        }
        return sb.toString();
    }
}