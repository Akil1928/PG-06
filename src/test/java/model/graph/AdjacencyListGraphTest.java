package model.graph;

import model.LinkedList.ListException;
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

        } catch (GraphException e) {
            throw new RuntimeException(e);
        } catch (ListException e) {
            throw new RuntimeException(e);
        }
    }

}