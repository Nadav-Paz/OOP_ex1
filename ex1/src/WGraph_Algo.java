package ex1;

import java.util.List;
import java.util.Queue;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

public class WGraph_Algo implements weighted_graph_algorithms {

	private node_info src;
	private WGraph_DS g;
	private HashMap<node_info, node_info> node_parents;
	private int mode_counter;
	
	public WGraph_Algo() {
		this.g = new WGraph_DS(); // will be overwritten, using to avoid null exceptions
		this.src = null;
		this.node_parents = new HashMap<node_info, node_info>();
		this.mode_counter = -1;
	}

	@Override
	public void init(weighted_graph g) {
		this.g = (WGraph_DS)g;
	}

	@Override
	public weighted_graph getGraph() {
		return this.g;
	}

	@Override
	public weighted_graph copy() {
		return new WGraph_DS(this.g);
	}
	
	@Override
	public boolean isConnected() {
		if(this.g.nodeSize() == 0 || this.g.nodeSize() == 1)
			return true;
		// running isConnecterd
		Iterator <node_info> iterator = this.g.getV().iterator();
		if(iterator.hasNext())
			return isConnected(iterator.next());
		return false;
	}
	
	/**
	 * Using BFS to check connectivity
	 * Starting from source node
	 */
	public boolean isConnected(node_info node) {
		if(node == null)
			return true;

		int V = 0;
		this.src = node;
		
		// nanoTime() will retrieve a unique string for every run
		// therfore does not need initialization of all the nodes
		String visited_key = String.valueOf(System.nanoTime());

		node.setInfo(visited_key);
		Queue<node_info> queue = new LinkedList<node_info>();
		queue.add(node);

		while(!queue.isEmpty()) {
			node = queue.remove();
			V++;
			for(node_info neigbor : this.g.getV(node)) {
				if(neigbor.getInfo() != visited_key) {
					neigbor.setInfo(visited_key);
					queue.add(neigbor);
				}
			}
		}
		return V == this.g.nodeSize();
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		if(this.g.getNode(src) == null || this.g.getNode(dest) == null)
			return -1;
		
		if(this.g.getMC() != this.mode_counter || this.src != this.g.getNode(src)) {
			this.src = this.g.getNode(src);
			Dijkstra();
		}
		double dist = this.g.getNode(dest).getTag();
		return dist == Double.MAX_VALUE ? -1 : dist;
	}

	@Override
	public List<node_info> shortestPath(int src, int dest) {		
		if(shortestPathDist(src, dest) == -1)
			return null;
		node_info node = this.g.getNode(dest);
		List <node_info> reversed_list = new LinkedList<node_info>();
		
		while(node != null) {
			reversed_list.add(node);
			node = this.node_parents.get(node);
		}
		int size = reversed_list.size();
		List <node_info> list = new ArrayList<node_info>(size); // using initial size to save run time
		for(int i = size - 1; 0 <= i; i--)
			list.add(reversed_list.get(i));
		
		return list;
	}

	@Override
	public boolean save(String file) {
		try {
			FileOutputStream f = new FileOutputStream(file);
			ObjectOutputStream output = new ObjectOutputStream(f);
			output.writeObject(this.g);
			output.close();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}

	@Override
	public boolean load(String file) {
		try {
			FileInputStream f = new FileInputStream(file);
			ObjectInputStream input = new ObjectInputStream(f);
			this.g = (WGraph_DS) input.readObject();
			input.close();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	private void Dijkstra() {
		this.mode_counter = g.getMC();
		PriorityQueue<node_info> queue = new PriorityQueue<node_info>(this.g.nodeSize());
		HashMap<node_info, node_info> parents = (this.node_parents = new HashMap<node_info, node_info>());

		// nanoTime() will retrieve a unique string for every run
		// therfore does not need initialization of all the nodes
		String visited_key = String.valueOf(System.nanoTime());
		
		this.src.setTag(0);
		parents.put(this.src, null);
		
		for(node_info node : this.g.getV()) {
			if(node != this.src)
				node.setTag(Double.MAX_VALUE);
			queue.add(node);
		}
		
		while(!queue.isEmpty()) {
			node_info node = queue.remove();
			node.setInfo(visited_key);
			
			for(node_info neigbor : this.g.getV(node.getKey())) {
				if(neigbor.getInfo() != visited_key) {
					double dist = node.getTag() + this.g.getEdge(node, neigbor);
					if(dist < neigbor.getTag()) {
						queue.remove(neigbor);
						neigbor.setTag(dist);
						queue.add(neigbor);
						parents.put(neigbor, node);
					} // end if(dist < neigbor.getTag())
				} // end if(neigbor.getInfo() != this.visited_key)
			} // end for
		} // end while
	} // end Dijkstra()

}
