package a2posted;

import java.util.HashMap;
import java.util.HashSet;

public class Dijkstra {

	private IndexedHeap pq;
	private static int edgeCount = 0; // Use this to give names to the edges.
	private HashMap<String, Edge> edges = new HashMap<String, Edge>();

	private HashMap<String, String> parent;
	private HashMap<String, Double> dist; // This is variable "d" in lecture
											// notes
	private String startingVertex;

	HashSet<String> setS;
	HashSet<String> setVminusS;

	public Dijkstra() {
		pq = new IndexedHeap();
		setS = new HashSet<String>();
		setVminusS = new HashSet<String>();
		parent = new HashMap<String, String>();
		dist = new HashMap<String, Double>();
	}

	/*
	 * Run Dijkstra's algorithm from a vertex whose name is given by the string
	 * s.
	 */

	public void dijkstraVertices(Graph graph, String s) {

		// temporary variables

		String u;
		double distToU, costUV;

		HashMap<String, Double> uAdjList;
		initialize(graph, s);

		// TODO check if this is still necessary.
		parent.put(s, null);
		pq.add(s, 0.0); // shortest path from s to s is 0.
		this.startingVertex = s;

		// --------- BEGIN: ADD YOUR CODE HERE -----------------------
		while (!pq.isEmpty()) {
			// get the minimum element from the pq.
			u = pq.nameOfMin();

			// Add the minimum element to the set of known indicies.
			// Remove it from undecided portion. Its parents are already
			// decided!
			setS.add(u);
			setVminusS.remove(u);

			// store the properties of the minimum element and remove it from
			// the pq.
			distToU = pq.getMinPriority();
			pq.removeMin();

			// get the minimum elements' adjacency list and search through it
			// for the closest edges.
			uAdjList = graph.getAdjList().get(u);
			for (String destination : uAdjList.keySet()) {
				// if we haven't already visited this location, then we will see
				// if its the closest edge.
				if (setVminusS.contains(destination)) {
					// avoid complier errors by adding to pq when necessary.
					if (!pq.contains(destination))
						pq.add(destination);

					// store the distance from u to v.
					costUV = uAdjList.get(destination);
					double tmpDistToV = distToU + costUV;
					System.out.println(tmpDistToV);
					// update the distances if necessary to reflect the best
					// path.
					if (tmpDistToV < pq.getPriority(destination)) {
						dist.put(destination, tmpDistToV);
						pq.changePriority(destination, tmpDistToV);
						parent.put(destination, u);
					}
				}
			}
		}

		// --------- END: ADD YOUR CODE HERE -----------------------
	}

	public void dijkstraEdges(Graph graph, String startingVertex) {

		// Makes sets of the names of vertices, rather than vertices themselves.
		// (Could have done it either way.)

		// temporary variables

		Edge e;
		String u, v;
		double tmpDistToV;

		initialize(graph, startingVertex);

		// --------- BEGIN: ADD YOUR CODE HERE -----------------------

		// add edges of starting vertex to the graph.
		this.pqAddEdgesFrom(graph, startingVertex);
		parent.put(startingVertex, null);
		dist.put(startingVertex, 0.0);

		while (setS.size() < graph.getVertices().size()) {
			// get the minimum element from the pq.
			String minEdge = pq.nameOfMin();
			e = edges.get(minEdge);
			u = e.getStartVertex();
			v = e.getEndVertex();
			tmpDistToV = graph.getAdjList().get(u).get(v) + dist.get(u);
			pq.removeMin();

			if (setS.contains(u) && setS.contains(v)) {
			} // do nothing
			else {
				parent.put(v, u);
				dist.put(v, tmpDistToV);
				setS.add(v);
				setVminusS.remove(v);
				pqAddEdgesFrom(graph, v);
			}
		}
		// --------- END: ADD YOUR CODE HERE -----------------------
	}

	/*
	 * This initialization code is common to both of the methods that you need
	 * to implement so I just factored it out.
	 */

	private void initialize(Graph graph, String startingVertex) {
		// initialization of sets V and VminusS, dist, parent variables
		//

		// add all the elements of the graph to the VminusS.
		// then set the distances to these v from s to be infinity (we don't
		// know them yet).
		// then the parent is unknown, so we set that to null.
		for (String v : graph.getVertices()) {
			System.out.println(v);
			setVminusS.add(v);
			dist.put(v, Double.POSITIVE_INFINITY);
			parent.put(v, null);
		}
		this.startingVertex = startingVertex;

		// Transfer the starting vertex from VminusS to S and

		setVminusS.remove(startingVertex);
		setS.add(startingVertex);
		dist.put(startingVertex, 0.0);
		parent.put(startingVertex, null);
	}

	/*
	 * helper method for dijkstraEdges: Whenever we move a vertex u from V\S to
	 * S, add all edges (u,v) in E to the priority queue of edges.
	 * 
	 * For each edge (u,v), if this edge gave a shorter total distance to v than
	 * any previous paths that terminate at v, then this edge will be removed
	 * from the priority queue before these other vertices.
	 */

	public void pqAddEdgesFrom(Graph g, String u) {
		double distToU = dist.get(u);
		for (String v : g.getAdjList().get(u).keySet()) { // all edges of form
															// (u, v)

			edgeCount++;
			Edge e = new Edge(edgeCount, u, v);
			edges.put(e.getName(), e);
			pq.add(e.getName(), distToU + g.getAdjList().get(u).get(v));
		}
	}

	// -------------------------------------------------------------------------------------------

	public String toString() {
		String s = "";
		s += "\nRan Dijkstra from vertex " + startingVertex + "\n";
		for (String v : parent.keySet()) {
			s += v + "'s parent is " + parent.get(v);
			s += "   and pathlength is " + dist.get(v) + "\n";
		}
		return s;
	}

	// This class is used only to keep track of edges in the priority queue.

	private class Edge {

		private String edgeName;
		private String u, v;

		Edge(int i, String u, String v) {
			this.edgeName = "e_" + Integer.toString(i);
			this.u = u;
			this.v = v;
		}

		public String getName() {
			return edgeName;
		}

		String getStartVertex() {
			return u;
		}

		String getEndVertex() {
			return v;
		}

		public String toString() {
			return edgeName + " : " + u + " " + v;
		}
	}

}
