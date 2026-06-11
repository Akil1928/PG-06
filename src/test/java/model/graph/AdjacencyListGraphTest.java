package model.graph;

import model.LinkedList.ListException;
import model.Queue.QueueException;
import model.Stack.StackException;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyListGraphTest {
    @Test
    void testAdjecencyListGraph() {
        AdjacencyListGraph<Integer> graph = new AdjacencyListGraph<>(10, false);
        try {
            //agregamos vertices
            for (int i = 0; i < 10; i++)
                graph.addVertex(i);

            //agregamos aristas - AHORA CONECTAMOS DESDE EL VÉRTICE 0
            graph.addEdgeAndWeight(0, 1, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(1, 2, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(1, 3, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(2, 3, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(2, 5, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(3, 4, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(4, 5, new Random().nextInt(5, 30));
            System.out.println(graph);
            System.out.println("DFS Transversal: " + graph.dfs());
            System.out.println("BFS Transversal: " + graph.bfs());

            //eliminemos algunos vertices
            System.out.println("Eliminando el vertice 1...");
            graph.removeVertex(1);
            System.out.println("Eliminando el vertice 2...");
            graph.removeVertex(2);
            System.out.println("Eliminando el vertice 3...");
            graph.removeVertex(3);
            System.out.println(graph);



            graph.addVertex(6);
            graph.addVertex(7);
            graph.addEdgeAndWeight(4, 7, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(5, 6, new Random().nextInt(5, 30));
            System.out.println(graph);

            // Prueba de los métodos de grado y aristas
            System.out.println("\n=== MÉTRICAS DEL GRAFO ===");
            System.out.println("Grado del vértice 0: " + graph.getVertexDegree(0));
            System.out.println("Grado del vértice 1: " + graph.getVertexDegree(1));
            System.out.println("Grado del vértice 2: " + graph.getVertexDegree(2));
            System.out.println("Grado máximo del grafo: " + graph.getGraphDegree());
            System.out.println("Total de aristas: " + graph.totalEdges());

            //eliminemos algunas aristas
            System.out.println("Remove Edge: 4-5");
            graph.removeEdge(4, 5);

        } catch (GraphException | ListException e) {
            throw new RuntimeException(e);
        } catch (QueueException e) {
            throw new RuntimeException(e);
        } catch (StackException e) {
            throw new RuntimeException(e);
        }
        System.out.println(graph);
    }
}