package ex1;

import java.util.Random;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {

	@Test
	void isConnected() {
		weighted_graph_algorithms ga = new WGraph_Algo();
		assertTrue(ga.isConnected());
		
		weighted_graph g = new WGraph_DS();
		ga.init(g);
		assertTrue(ga.isConnected());
		g.addNode(0);
		assertTrue(ga.isConnected());
		g.addNode(1);
		assertFalse(ga.isConnected());
		g.connect(0, 1, 1);
		assertTrue(ga.isConnected());
		g.removeEdge(1, 0);
		assertFalse(ga.isConnected());

		int V = 10000, E = 1000000;
		for(int i = 0; i < V; i++) {
			g.addNode(i);
		}
		
		Random r = new Random();
		r.setSeed(1);
		while(g.edgeSize() < E) {
			g.connect(r.nextInt(V), r.nextInt(V), r.nextFloat());
		}
		
		assertTrue(ga.isConnected());
		
		for(node_info node : g.getV(V/2))
			g.removeEdge(V/2, node.getKey());
		
		assertFalse(ga.isConnected());
	}
	
	
	@Test
	void saveLoad() {
		weighted_graph g = new WGraph_DS();
		weighted_graph_algorithms ga = new WGraph_Algo();
		ga.init(g);
		String file = "test.obj";
		int V = 100, E = 1000;

		for(int i = 0; i < V; i++)
			g.addNode(i);
		
		Random r = new Random();
		r.setSeed(1);
		while(g.edgeSize() < E) {
			g.connect(r.nextInt(V), r.nextInt(V), r.nextFloat());
		}
		
		assertTrue(ga.save(file));
		
		weighted_graph_algorithms gb = new WGraph_Algo();
		assertTrue(gb.load(file));
		
		assertEquals(ga.getGraph(), gb.getGraph());
	}
	
	@Test
	void copy() {
		weighted_graph g = new WGraph_DS();
		weighted_graph_algorithms ga = new WGraph_Algo();
		ga.init(g);
		int V = 10000, E = 100000;
		
		for(int i = 0; i < V; i++)
			g.addNode(i);
		
		Random r = new Random();
		r.setSeed(1);
		while(g.edgeSize() < E) {
			g.connect(r.nextInt(V), r.nextInt(V), r.nextFloat());
		}
		
		weighted_graph _g = ga.copy();
		assertEquals(g, _g);
		g.removeNode(0);
		assertNotEquals(g, _g);
	}
	
	@Test
	void Dijkstra() {
		weighted_graph g = new WGraph_DS();
		weighted_graph_algorithms ga = new WGraph_Algo();
		ga.init(g);
		for(int i = 0; i < 6; i++)
			g.addNode(i);
		
		g.connect(0, 1, 0.2);
		g.connect(0, 3, 5);
		g.connect(0, 4, 4);
		g.connect(0, 5, 10);
		g.connect(1, 2, 7);
		g.connect(2, 3, 3);
		g.connect(2, 5, 2.9);
		g.connect(4, 2, 3);
		
		assertEquals(9.9, ga.shortestPathDist(0, 5));
		List<node_info> list = ga.shortestPath(0, 5);
		String path = "";
		for(node_info node : list)
			path = path + ", " + node.getKey() +", " + node.getTag();
		String exp = ", 0, 0, 4, 4.0, 2, 7.0, 5, 9.9";
		
		assertTrue(exp.equals(exp));
		g.removeEdge(2, 4);

		assertEquals(10, ga.shortestPathDist(0, 5));
	}
}