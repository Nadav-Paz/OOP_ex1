package ex1;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;

public class WGraph_DS implements weighted_graph, Serializable{

	// generated serial version
	private static final long serialVersionUID = 429799367797663646L;

	private int mode_counter;
	private HashMap<Integer, node_info> verticies;
	private HashMap<Edge, Double> edges;


	public WGraph_DS() {
		this.mode_counter = 0;
		this.verticies = new HashMap<Integer, node_info>();
		this.edges = new HashMap<Edge, Double>();
	}

	public WGraph_DS(WGraph_DS g) {
		this();
		for(node_info node : g.getV())
			addNode(node);
		for(Edge edge : g.getE())
			connect(edge, g.getEdge(edge));
	}

	@Override
	public node_info getNode(int key) {
		return this.verticies.get(key);
	}

	@Override
	public boolean hasEdge(int node1, int node2) {
		if(getNode(node1) == null || getNode(node2) == null)
			return false;
		return ((NodeInfo)getNode(node1)).neigbors.containsKey(node2);
	}

	@Override
	public double getEdge(int node1, int node2) {
		Edge edge = ((NodeInfo)getNode(node1)).neigbors.get(node2);
		if(this.edges.containsKey(edge))
			return this.edges.get(edge);
		return -1;
	}

	public double getEdge(node_info node1, node_info node2) {
		return getEdge(node1.getKey(), node2.getKey());
	}

	public double getEdge(Edge edge) {
		int[] arr = edge.getNodes();
		return getEdge(arr[0], arr[1]);
	}

	@Override
	public void addNode(int key) {
		if(this.verticies.containsKey(key))
			return;	// Do nothing
		this.verticies.put(key, new NodeInfo(key));
		this.mode_counter++;
	}

	public void addNode(node_info node) {
		if(node == null)
			return; // Do nothing
		addNode(node.getKey());
	}

	@Override
	public void connect(int node1, int node2, double w) {
		if(getNode(node1) == null || getNode(node2) == null)
			return;

		Edge edge = ((NodeInfo)this.verticies.get(node1)).neigbors.get(node2);
		
		if(edge == null) 
			new Edge(node1, node2, w);
		else
			this.edges.put(edge, w < 0 ? 0 : w);
	}

	public void connect(node_info node1, node_info node2, double w) { // shorthand
		connect(node1.getKey(), node2.getKey(), w);
	}

	public void connect(Edge edge, double w) { // shorthand
		connect(edge.node1.getKey(), edge.node2.getKey(), w);
	}

	@Override
	public Collection<node_info> getV() {
		return this.verticies.values();
	}

	@Override
	public Collection<node_info> getV(int node_id) {
		if(!this.verticies.containsKey(node_id))
			return null;
		Collection<Integer> keys = ((NodeInfo)getNode(node_id)).neigbors.keySet();
		Collection<node_info> collection = new ArrayList<node_info>(keys.size());
		for(int key : keys)
			collection.add(getNode(key));
		return collection;
	}

	public Collection<node_info> getV(node_info node) {
		return getV(node.getKey());
	}

	public Collection<Edge> getE() {
		return this.edges.keySet();
	}

	@Override
	public node_info removeNode(int key) {
		NodeInfo node = (NodeInfo)this.verticies.remove(key);

		if(node == null)
			return null;

		for(Edge edge : node.neigbors.values()) {
			this.edges.remove(edge);
			edge.getOtherNode(key).neigbors.remove(key);
			this.mode_counter++;
		}
		node.neigbors = null;
		this.mode_counter++;
		return node;
	}

	@Override
	public void removeEdge(int node1, int node2) {
		if(getNode(node1) == null || getNode(node2) == null)
			return;	// edge does not exist

		Edge edge = ((NodeInfo)getNode(node1)).getNeigbor(node2);

		if(edge == null)
			return;
		edge.remove();
		this.mode_counter++;
	}

	@Override
	public int nodeSize() {
		return this.verticies.size();
	}

	@Override
	public int edgeSize() {
		return this.edges.size();
	}

	@Override
	public int getMC() {
		return this.mode_counter;
	}

	/*
	 *  equals checks if all keys and edges are equal between 2 weighted_graphs
	 */
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof weighted_graph))
			return false;

		weighted_graph g = (weighted_graph)o;

		// verifing equal edge size
		if(g.edgeSize() != this.edges.size())
			return false;

		// verifing same number of verticies
		if(g.nodeSize() != this.verticies.size())
			return false;

		// iterating over all vericies
		for(int node : this.verticies.keySet())
			if(g.getNode(node) == null)
				return false;
		// iterating over all edges
		for(Edge edge : this.edges.keySet()) {
			int[] arr = edge.getNodes();
			if(this.edges.get(edge) != g.getEdge(arr[0], arr[1]))
				return false;
		}
		return true;
	}

	protected class NodeInfo implements node_info, Comparable<node_info>, Serializable{

		private static final long serialVersionUID = 1L;
		private int key;
		private double tag;
		private String info;
		private HashMap<Integer, Edge> neigbors;

		public NodeInfo(int key, double tag, String info) {
			this.key = key;
			this.tag = tag;
			this.info = info;
			neigbors = new HashMap<Integer, Edge>();
		}

		public NodeInfo(int key) {
			this(key, 0, "");
		}

		public NodeInfo(node_info node) {
			this(node.getKey(), node.getTag(), new String (node.getInfo()));
		}

		@Override
		public int getKey() {
			return this.key;
		}

		@Override
		public String getInfo() {
			return this.info;
		}

		@Override
		public void setInfo(String s) {
			this.info = s;
		}

		@Override
		public double getTag() {
			return this.tag;
		}

		@Override
		public void setTag(double t) {
			this.tag = t;
		}

		public Edge getNeigbor(int key) {
			return this.neigbors.get(key);
		}

		@Override
		public int compareTo(node_info node){
			if(node.getTag() < this.tag)
				return 1;
			if(node.getTag() > this.tag)
				return -1;
			return 0;
		}

		@Override
		public String toString() {
			return "[" + this.key + ", " + this.tag + ", " + this.info + "]\n" + this.neigbors + "\n";
		}
		
	}

	protected class Edge implements Serializable{
		private static final long serialVersionUID = 1L;
		NodeInfo node1, node2;

		private Edge(int node1, int node2, double w){
			if(verticies.containsKey(node1) && verticies.containsKey(node2) && node1 != node2) {
				this.node1 = (NodeInfo) verticies.get(node1);
				this.node2 = (NodeInfo) verticies.get(node2);
				this.node1.neigbors.put(node2, this);
				this.node2.neigbors.put(node1, this);

				w = w < 0 ? 0 : w;
				edges.put(this, w);
			}
		}

		public int[] getNodes() {
			return new int[] {node1.getKey(), node2.getKey()};
		}

		public int getNode() {
			return node1.getKey();
		}

		public NodeInfo getNode(int key) {
			if(key == this.node1.key) return this.node1;
			if(key == this.node2.key) return this.node2;
			return null;
		}

		public NodeInfo getOtherNode(int key) {
			if(key == this.node1.key) return this.node2;
			if(key == this.node2.key) return this.node1;
			return null;
		}

		public void remove() {
			this.node1.neigbors.remove(node2.getKey());
			this.node2.neigbors.remove(node1.getKey());
			edges.remove(this);
		}

		@Override
		public boolean equals(Object o) {
			if(!(o instanceof Edge))
				return false;
			if(this.node1 == null || this.node2 == null)
				return false;
			Edge edge = (Edge)o;
			return (edge.node1.key == this.node1.key && edge.node2.key == this.node2.key) ||
					(edge.node1.key == this.node2.key && edge.node2.key == this.node1.key);
		}

		@Override
		public String toString() {
			return "{"+ this.node1.key + ", " + this.node2.key + ": " + edges.get(this) + "}";
		}
	}
}
