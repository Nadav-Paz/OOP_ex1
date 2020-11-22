package ex1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {

	@Test
	void addRemoveNode() {
		weighted_graph g = new WGraph_DS();
		int[]arr = {0,1,2,3,4,5,1};
		for (int i = 0; i < arr.length; i++) {
			g.addNode(arr[i]);
		}
				
		assertEquals(g.nodeSize(),6);
		for (int i = 0; i < g.nodeSize(); i++) {
			node_info node = g.removeNode(i);
			assertNotNull(node, "Node is Null!");
		}
		
		try {
			node_info node = g.removeNode(6);
			assertNull(node);
		}
		catch (Exception e) {
			assertTrue(false,"An exception has occurred!");
		}
	}
	
	@Test
	void Edges() {
		weighted_graph g = new WGraph_DS();
		int[]arr = {0,1,2,1,3,4,5,1,3};
		for (int i = 0; i < arr.length; i++) {
			g.addNode(arr[i]);
		}
		for (int i = 0; i < arr.length - 1; i++) {
			g.connect(arr[i], arr[i + 1], i);
		}

		int[]brr = {0,2,2,7,4,5,6,7,8};
		assertEquals(6, g.edgeSize());
		for (int i = 0; i < arr.length - 1; i++) {
			assertEquals(brr[i], g.getEdge(arr[i], arr[i + 1]));
		}
		
		assertFalse(g.hasEdge(2, 5));

		assertEquals(-1, g.getEdge(1, 4));
		
		g.removeEdge(0, 1);
		assertEquals( -1, g.getEdge(1, 0));
		assertEquals(5, g.edgeSize());
		
		g.removeNode(1);
		assertEquals(2, g.edgeSize());
		assertEquals(5, g.nodeSize());
	}
	
	@Test
	void collections() {
		weighted_graph g = new WGraph_DS();
		int[]arr = {0,1,2,1,3,4,5,1,3};
		for (int i = 0; i < arr.length; i++) {
			g.addNode(arr[i]);
			g.getNode(arr[i]).setTag(i);
		}
		for (int i = 0; i < arr.length - 1; i++) {
			g.connect(arr[i], arr[i + 1], i);
		}
		
		int[] brr = {0,7,2,8,5,6};
		
		for(node_info node : g.getV()) {
			assertEquals(brr[node.getKey()],node.getTag());
		}
		
		boolean[] visited = {false, false, false, false, false, false};
		for(node_info node : g.getV(1)) {
			visited[node.getKey()] = true;
		}
		assertTrue(visited[0]);
		assertFalse(visited[1]);
		assertTrue(visited[2]);
		assertTrue(visited[3]);
		assertFalse(visited[4]);
		assertTrue(visited[5]);
	}
}