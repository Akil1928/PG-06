package model.graph;

import model.LinkedList.LinkedList;
import model.LinkedList.ListException;
import model.Node;
import model.Queue.LinkedQueue;
import model.Queue.QueueException;
import model.Stack.LinkedStack;
import model.Stack.StackException;

import java.util.Objects; // Import Objects for equals method

public class LinkedGraph<T extends Comparable<T>> extends LinkedList<T> implements Graph<T> {
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
    public boolean containsVertex(T element) throws ListException { // Removed GraphException
        return contains(element);
    }

    @Override
    public boolean containsEdge(T a, T b) throws ListException { // Removed GraphException
        Node<T> nodeA = getNode(a);
        Node<T> nodeB = getNode(b);

        if (!directed) { // If undirected
            return getNodeNeighbor(nodeA, b) != null && getNodeNeighbor(nodeB, a) != null;
        }
        // If directed
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
    if(!containsVertex(a) || !containsVertex(b)) // Corrected condition
        throw new GraphException("Cannot add edge. One or both vertices do not exist in the graph."); // Corrected message

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
            Node<T> neighborA = getNodeNeighbor(nodeA, b);
            if (neighborA != null) { // Add null check
                neighborA.weight = weight;
            }
            if (!directed) {
                Node<T> nodeB = getNode(b);
                //recuperamos la lista de vecinos del nodo B para settear el peso
                Node<T> neighborB = getNodeNeighbor(nodeB, a);
                if (neighborB != null) { // Add null check
                    neighborB.weight = weight;
                }
            }
        }
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T c) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b)) {
            throw new GraphException("Cannot add edge. One or both vertices do not exist in the graph.");
        }

        // Add edge (a, b) if it doesn't exist
        if (!containsEdge(a, b)) {
            Node<T> nodeA = getNode(a);
            addNeighbor(nodeA, b, c); // Add neighbor with weight
        } else {
            // If edge exists, just update the weight
            Node<T> nodeA = getNode(a);
            Node<T> neighborA = getNodeNeighbor(nodeA, b);
            if (neighborA != null) { // Add null check
                neighborA.weight = c;
            }
        }

        // If undirected, do the same for (b, a)
        if (!directed) {
            if (!containsEdge(b, a)) {
                Node<T> nodeB = getNode(b);
                addNeighbor(nodeB, a, c); // Add neighbor with weight
            } else {
                Node<T> nodeB = getNode(b);
                Node<T> neighborB = getNodeNeighbor(nodeB, a);
                if (neighborB != null) { // Add null check
                    neighborB.weight = c;
                }
            }
        }
    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
        if (!containsVertex(element))
            throw new GraphException("No se puede remover vertex. No existe en el grafo."); // Corrected message

        remove(element); //eliminamos el vertice del grafo
        //buscamos el rastro del vertice en las listas enlazadas de vecinos de los otros vertices
        int len = size();
        for (int i = 0; i < len; i++) {
            Node<T> node = getNodeByIndex(i);
            removeNeighbor(node, element);
        }
    }

    private void removeNeighbor(Node<T> headNode, T element) { // Removed ListException
        if(headNode.neighbor==null) return; // Modified: return silently if no neighbors

        //Caso 1. el elemento a suprimir es el primero
        if(Objects.equals(headNode.neighbor.data, element)) { // Used Objects.equals
            headNode.neighbor = headNode.neighbor.neighbor; //queda apuntando al sgte nodo vecino
        }
        //Caso 2. El elemento a suprimir puede estar en medio o al final de la lista
        else {
            Node<T> prev = headNode.neighbor; //anterior
            while(prev.neighbor!=null) {
                if(Objects.equals(prev.neighbor.data, element)){ // Used Objects.equals
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

    /***
     * RECORRIDO POR PROFUNDIDAD
     * @return A string representation of the DFS traversal
     * @throws model.graph.GraphException If the graph is empty
     * @throws model.Stack.StackException If there's an issue with the stack
     * @throws model.LinkedList.ListException If there's an issue with the linked list operations
     */
    @Override
    public String dfs() throws GraphException, StackException, ListException {
        if (isEmpty()) {
            throw new GraphException("Grafo Enlazado Vacio");
        }

        int numVertices = size();
        boolean[] visited = new boolean[numVertices]; // Array para controlar los vértices visitados
        StringBuilder sb = new StringBuilder(); // Para construir la cadena de recorrido
        LinkedStack<T> stack = new LinkedStack<>(); // Pila para el recorrido DFS

        // Recorrer todos los vértices para manejar grafos desconectados
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                // Iniciar un nuevo recorrido DFS desde este vértice no visitado
                stack.push(get(i)); // Removed redundant cast
                visited[i] = true;
                sb.append(get(i)).append(", "); // Añadirlo al resultado

                while (!stack.isEmpty()) {
                    T currentVertexData = stack.peek(); // Obtener el elemento superior sin removerlo
                    int currentVertexIndex = getIndexOfVertex(currentVertexData); // Obtener su índice

                    // Buscar un vecino no visitado del vértice actual
                    Node<T> neighborIterator = getNodeByIndex(currentVertexIndex).neighbor; // Obtener la lista de vecinos
                    T unvisitedNeighborData = null;

                    while (neighborIterator != null) {
                        T neighborData = neighborIterator.data;
                        int neighborIndex = getIndexOfVertex(neighborData);

                        if (!visited[neighborIndex]) {
                            unvisitedNeighborData = neighborData; // Encontramos un vecino no visitado
                            break;
                        }
                        neighborIterator = neighborIterator.neighbor;
                    }

                    if (unvisitedNeighborData != null) {
                        // Si se encontró un vecino no visitado, visitarlo y empujarlo a la pila
                        int unvisitedNeighborIndex = getIndexOfVertex(unvisitedNeighborData);
                        visited[unvisitedNeighborIndex] = true;
                        sb.append(unvisitedNeighborData).append(", ");
                        stack.push(unvisitedNeighborData);
                    } else {
                        // Si no hay vecinos no visitados, hacer "backtrack" (sacar de la pila)
                        stack.pop();
                    }
                }
            }
        }

        // Eliminar la coma y el espacio finales si la cadena no está vacía
        if (!sb.isEmpty()) { // Replaced sb.length() > 0
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    /***
     * RECORRIDO POR AMPLITUD
     * @return A string representation of the BFS traversal
     * @throws model.graph.GraphException If the graph is empty
     * @throws model.Queue.QueueException If there's an issue with the queue
     * @throws model.LinkedList.ListException If there's an issue with the linked list operations
     */
    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        if (isEmpty()) {
            throw new GraphException("Grafo Enlazado Vacio");
        }

        int numVertices = size();
        boolean[] visited = new boolean[numVertices];
        StringBuilder sb = new StringBuilder(); // Para construir la cadena de recorrido
        LinkedQueue<T> queue = new LinkedQueue<>(); // Cola para el recorrido BFS

        // Recorrer todos los vértices para manejar grafos desconectados
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                // Iniciar un nuevo recorrido BFS desde este vértice no visitado
                T startVertexData = get(i); // Removed redundant cast
                visited[i] = true;
                queue.enQueue(startVertexData);
                sb.append(startVertexData).append(", "); // Añadirlo al resultado

                while (!queue.isEmpty()) {
                    T currentVertexData = queue.deQueue();
                    int currentVertexIndex = getIndexOfVertex(currentVertexData); // Obtener su índice

                    // Recorrer los vecinos del vértice actual
                    Node<T> neighborIterator = getNodeByIndex(currentVertexIndex).neighbor; // Obtener la lista de vecinos
                    while (neighborIterator != null) {
                        T neighborData = neighborIterator.data;
                        int neighborIndex = getIndexOfVertex(neighborData);

                        if (!visited[neighborIndex]) {
                            visited[neighborIndex] = true;
                            queue.enQueue(neighborData);
                            sb.append(neighborData).append(", "); // Añadirlo al resultado
                        }
                        neighborIterator = neighborIterator.neighbor;
                    }
                }
            }
        }

        // Eliminar la coma y el espacio finales si la cadena no está vacía
        if (!sb.isEmpty()) { // Replaced sb.length() > 0
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private Node<T> getNodeNeighbor(Node<T> headnode, T element) {
        if (headnode == null || headnode.neighbor == null) return null; // Added null check for headnode
        Node<T> aux = headnode.neighbor;
        while (aux != null) {
            if (aux.data.compareTo(element) == 0) return aux;
            aux = aux.neighbor;
        }
        return null;//si llega no encontro el nodo
    }

    // Helper method to get the index of a vertex
    private int getIndexOfVertex(T element) throws ListException {
        for (int i = 0; i < size(); i++) {
            if (get(i).equals(element)) {
                return i;
            }
        }
        return -1; // Not found
    }

    /// // AYUDAS ////////////
    // Replaced custom equals with Objects.equals
    public boolean equals(T a, T b) {
        return Objects.equals(a, b);
    }

    // Método genérico de comparación - Removed as it's unused
    // public int compareElements(T a, T b) {
    //     return a.compareTo(b);
    // }
}
