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

        super.removeVertex(element);
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

    private Node<T> removeNeighbor(Node<T> headnode, T element) {
        if (headnode == null) return null;

        // Si el nodo a eliminar es el primero
        if (headnode.data.compareTo(element) == 0) {
            return headnode.neighbor;
        }

        // Buscar el nodo a eliminar
        Node<T> aux = headnode;
        while (aux.neighbor != null) {
            if (aux.neighbor.data.compareTo(element) == 0) {
                aux.neighbor = aux.neighbor.neighbor;
                return headnode;
            }
            aux = aux.neighbor;
        }
        return headnode;
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