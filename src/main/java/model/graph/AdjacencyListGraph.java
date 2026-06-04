package model.graph;

import model.LinkedList.ListException;
import model.Node;
import model.Queue.QueueException;
import model.Stack.StackException;

public class AdjacencyListGraph <T extends Comparable<T>> extends AdjacencyMatrixGraph<T>{
    public AdjacencyListGraph(int n, boolean directed) {
        super(n, directed);
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
       boolean getVertexA = false;
       boolean getVertexB = false;
        Vertex<T> vertexA = getVertex(a);
        getVertexA = getNodeNeighbor(vertexA.headnode, b)!=null;
        if(!directed) {
            getVertexB = getNodeNeighbor(vertexB.headnode, a) != null;
            Vertex<T> vertexB = getVertex(b);
        }
        return !directed ? getVertexA && getVertexB : getVertexA;
    }

    private boolean getNodeNeighbor(Node<T> headnode, T b) {
    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException(" Adjacency list Graph Not Contains Vertex");
        if (!containsEdge(a, b)) {
            Vertex<T> vertexA = getVertex(a);
            vertexA.headnode= addNeighbor(vertexA.headnode, b, null);
            if(!directed) {
                Vertex<T> vertexB = getVertex(b);
                vertexB.headnode = addNeighbor(vertexB.headnode, a, null);
            }
            
        }
    }

    private Node<T> addNeighbor(Node<T> headnode, T b, Object o) {
        Node<T> node = new Node<>(element, weight);
        if (headnode == null) 
        headnode = node;
        else{
            Node<T> aux = headnode;
            while(aux.neighbor != null) {
                aux = aux.neighbor;
                aux.neighbor = node;
            }
        }
        return headnode;
        
        
        
    }

    private Vertex<T> getVertex(T element){
        for (int i = 0; i < counter; i++) 
            if(equals(vertexList[i].data, element))
                return vertexList[i];
            return null;
        
    }

    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {
        super.addWeight(a, b, weight);
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T weight) throws GraphException, ListException {
        super.addEdgeAndWeight(a, b, weight);
    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
        super.removeVertex(element);
    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        super.removeEdge(a, b);
    }

    @Override
    public String dfs() throws GraphException, StackException, ListException {
        return super.dfs();
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        return super.bfs();
    }
}
