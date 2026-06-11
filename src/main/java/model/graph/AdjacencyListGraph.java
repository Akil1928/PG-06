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
    public int getVertexDegree(T element) throws GraphException, ListException {
        if (isEmpty()) throw new GraphException("Adjacency List Graph is Empty");
        if (!containsVertex(element)) throw new GraphException("Vertex not found");

        Vertex<T> vertex = getVertex(element);
        int degree = 0;

        // Contar vecinos en la lista enlazada
        Node<T> aux = vertex.headnode;
        while (aux != null) {
            degree++;
            aux = aux.neighbor;
        }

        return degree;
    }


    @Override
    public int getGraphDegree() throws GraphException, ListException {
        if (isEmpty()) throw new GraphException("Adjacency List Graph is Empty");

        int maxDegree = 0;

        // Encontrar el grado máximo
        for (int i = 0; i < counter; i++) {
            int degree = getVertexDegree(vertexList[i].data);
            if (degree > maxDegree) {
                maxDegree = degree;
            }
        }

        return maxDegree;
    }


    @Override
    public int totalEdges() throws GraphException, ListException {
        if (isEmpty()) throw new GraphException("Adjacency List Graph is Empty");

        int edges = 0;

        // Contar todas las aristas en las listas de adyacencia
        for (int i = 0; i < counter; i++) {
            Node<T> aux = vertexList[i].headnode;
            while (aux != null) {
                edges++;
                aux = aux.neighbor;
            }
        }

        // Si es no dirigido, dividir por 2 (cada arista se cuenta dos veces)
        return directed ? edges : edges / 2;
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
        if (!containsVertex(element))
            throw new GraphException("Adjacency list Graph Not Contains Vertex");

        int index = indexOf(element);
        if (index != -1) {
            for (int i = index; i < counter - 1; i++)
                vertexList[i] = vertexList[i + 1];

            vertexList[counter - 1] = null;
            counter--;

            for (int i = 0; i < counter; i++) {
                Vertex<T> vertex = vertexList[i];
                if (vertex.headnode != null) {
                    vertex.headnode = removeNeighbor(vertex.headnode, element);
                }
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
        if (equals(headNode.data, element)) headNode = headNode.neighbor;
        else {
            Node<T> prev = headNode;
            while (prev.neighbor != null) {
                if (equals(prev.neighbor.data, element)) {
                    Node<T> removed = prev.neighbor;
                    prev.neighbor = removed.neighbor;
                }
                prev = prev.neighbor;
            }
        }
        return headNode;
    }


    @Override
    public String dfs() throws GraphException, StackException, ListException {
        if (isEmpty()) throw new GraphException("Adjacency List Graph is Empty");

        setVisited(false); // marca todos los vértices como no visitados
        // inicia en el vértice 0
        String info = vertexList[0].data + ", ";
        vertexList[0].setVisited(true); // lo marca
        stack.clear();
        stack.push(0); // lo apila

        while (!stack.isEmpty()) {
            // obtiene un vértice adyacente no visitado desde la lista enlazada
            int index = adjacentVertexNotVisitedByList((int) stack.top());
            if (index == -1) // no lo encontró
                stack.pop();
            else {
                vertexList[index].setVisited(true); // lo marca
                info += vertexList[index].data + ", "; // lo muestra
                stack.push(index); // inserta la posición
            }
        }
        return info;
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        if (isEmpty()) throw new GraphException("Adjacency List Graph is Empty");

        setVisited(false); // marca todos los vértices como no visitados
        // inicia en el vértice 0
        String info = vertexList[0].data + ", ";
        vertexList[0].setVisited(true); // lo marca
        queue.clear();
        queue.enQueue(0); // encola el elemento
        int v2;

        while (!queue.isEmpty()) {
            int v1 = (int) queue.deQueue(); // remueve el vértice de la cola
            // hasta que no tenga vecinos sin visitar
            while ((v2 = adjacentVertexNotVisitedByList(v1)) != -1) {
                vertexList[v2].setVisited(true); // lo marca
                info += vertexList[v2].data + ", "; // lo muestra
                queue.enQueue(v2); // lo encola
            }
        }
        return info;
    }


    private int adjacentVertexNotVisitedByList(int index) {
        Node<T> aux = vertexList[index].headnode; // primer vecino en la lista enlazada
        while (aux != null) {
            int neighborIndex = indexOf(aux.data); // busca el índice del vecino en vertexList
            if (neighborIndex != -1 && !vertexList[neighborIndex].isVisited())
                return neighborIndex; // retorna la posición del vecino no visitado
            aux = aux.neighbor;
        }
        return -1; // no encontró vecino no visitado
    }

    // settea el atributo visitado de cada vértice
    private void setVisited(boolean value) {
        for (int i = 0; i < counter; i++) {
            vertexList[i].setVisited(value);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency List Graph:\n");
        String graphType = directed ? "Directed" : "Undirected";
        sb.append("Graph Type: ").append(graphType).append("\n");

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
