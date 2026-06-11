package model.graph;

import model.LinkedList.ListException;
import model.Queue.QueueException;
import model.Stack.StackException;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LinkedGraphTest {

    @Test
    void testLinkedGraph() {
        LinkedGraph<Integer> graph = new LinkedGraph<>(false);
        try {
            //agregamos vertices
            for (int i = 0; i < 10; i++)
                graph.addVertex(i);

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