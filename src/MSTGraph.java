import java.util.*;

/**
 * Ethan Anderson
 * cse373 hwk5
 * A representation of a graph.
 * Assumes that we do not have negative cost edges in the graph.
 */
public class MSTGraph implements Graph {
    // store the adjacency list as a map of maps, where Map of vertex --> Map of vertex --> weight
    // this allows O(1) lookup for getting all vertices and adjacencies
    private HashMap<Vertex, HashMap<Vertex, Integer>> adjacencyList;
    // store the set of all the edges in the graph, makes getting collection of vertices O(1)
    private HashSet<Edge> edgeSet;

    /**
     * Creates a MyGraph object with the given collection of vertices
     * and the given collection of edges.
     * @param v a collection of the vertices in this graph
     * @param e a collection of the edges in this graph
     */
    public MSTGraph(Collection<Vertex> v, Collection<Edge> e){
        // handle if client gives null values
        if(v == null || e == null)
            throw new IllegalArgumentException("Parameters can't be null ");

        // initialize a new hashmap to store the adjacency list
        adjacencyList = new HashMap<>();
        // initialize a new hashset to store all the edges in the graph which makes O(1) to get all edges space trade off
        edgeSet = new HashSet<>();

        //iterate through the vertices and add them to the adjacency list

        for(Vertex vertex: v){
            adjacencyList.put(vertex, new HashMap<>());
        }

        // iterate through the edges and add the adjacencies to the list
        for(Edge edge: e){
            // throw exception if weight is negative
            if(edge.getWeight() < 0)
                throw new IllegalArgumentException("Edge weight cannot be negative");
            // throw exception if the from or to vertex is not in the graph
            if(!adjacencyList.containsKey(edge.getSource()))
                throw new NoSuchElementException("Source vertex " + edge.getSource() + " is not in the graph");
            if(!adjacencyList.containsKey(edge.getDestination()))
                throw new NoSuchElementException("Destination vertex " + edge.getDestination() + " is not in the graph");

            // add the adjacency
            // KRUSKAL MODIFICATION TO MAKE UNDIRECTED, don't add twice in this way we only take the first arbitrary edge given
            if(edgeCost(edge.getSource(), edge.getDestination()) == -1 && edgeCost(edge.getDestination(), edge.getSource()) == -1){
                addAdjacency(edge.getSource(), edge.getDestination(), edge.getWeight());
                // add the edge to the set of edges which makes getEdges O(1)
                edgeSet.add(edge);
            }



        }

    }

    // helper to add an adjacency for a source and destination with a weight
    // throws illegal argument exception if the
    private void addAdjacency(Vertex source, Vertex destination, int weight) throws IllegalArgumentException{

        // add a new HashMap if the source vertex doesn't have one
        adjacencyList.putIfAbsent(source, new HashMap<>());

        if(adjacencyList.get(source).containsKey(destination)){ // check if the destination is already in the adjacency
            // throw exception if the weight of the adjacency is different
            if(adjacencyList.get(source).get(destination) != weight)
                throw new IllegalArgumentException("Non equal duplicate edges");
        }else {
            //System.out.println("adding destination and weight");
            adjacencyList.get(source).put(destination, weight);
        }
    }

    /**
     * Return the collection of vertices of this graph
     * @return the vertices as a collection (which is anything iterable)
     */
    public Collection<Vertex> vertices() {
        // return the an unmodifiable key set of all the vertices in the adjacency list O(1)!
        // This assures that a client cannot modify the set that they are given
        return Collections.unmodifiableSet(adjacencyList.keySet());
    }

    /**
     * Return the collection of edges of this graph
     * @return the edges as a collection (which is anything iterable)
     */
    public Collection<Edge> edges() {
        // return an unmodifiable set of all the edges
        // because we make the space trade off of keeping all the edges in the graph, this is O(1)
        return Collections.unmodifiableSet(edgeSet);
    }

    /**
     * Return a collection of vertices adjacent to a given vertex v.
     *   i.e., the set of all vertices w where edges v -> w exist in the graph.
     * Return an empty collection if there are no adjacent vertices.
     * @param v one of the vertices in the graph
     * @return an iterable collection of vertices adjacent to v in the graph
     * @throws IllegalArgumentException if v does not exist.
     */
    public Collection<Vertex> adjacentVertices(Vertex v) {
        // throw the condition if v does not exist
        //System.out.println(this.vertices());
        if(!adjacencyList.containsKey(v))
            throw new IllegalArgumentException("Vertex " + v + " does not exist in the graph");
        // return a new empty hashSet if the vertex doesn't have one
        if(adjacencyList.get(v) == null)
            return new HashSet<>();
        return Collections.unmodifiableSet(adjacencyList.get(v).keySet());
    }

    /**
     * Test whether vertex b is adjacent to vertex a (i.e. a -> b) in a directed graph.
     * Assumes that we do not have negative cost edges in the graph.
     * @param a one vertex
     * @param b another vertex
     * @return cost of edge if there is a directed edge from a to b in the graph,
     * return -1 otherwise.
     * @throws IllegalArgumentException if a or b do not exist.
     */
    public int edgeCost(Vertex a, Vertex b) {
        // throw exception if a or b do not exist
        if(!adjacencyList.containsKey(a) || !adjacencyList.containsKey(b))
            throw new IllegalArgumentException("Vertex does not exist in graph");
        // look up if the adjacency is there
        Integer weight = adjacencyList.get(a).get(b);
        if(weight == null)
            return -1;
        else
            return weight;

    }

    /**
     * Returns the shortest path from a to b in the graph, or null if there is
     * no such path.  Assumes all edge weights are nonnegative.
     * Uses Dijkstra's algorithm.
     * @param a the starting vertex
     * @param b the destination vertex
     * @return a Path where the vertices indicate the path from a to b in order
     *   and contains a (first) and b (last) and the cost is the cost of
     *   the path. Returns null if b is not reachable from a.
     * @throws IllegalArgumentException if a or b does not exist.
     */
    public Path shortestPath(Vertex a, Vertex b) {
        if(!adjacencyList.containsKey(a) || !adjacencyList.containsKey(b)){
            throw new IllegalArgumentException("Vertex does not exist in the graph");
        }

        // initialize a priority queue of vertex wrappers
        PriorityQueue<VertexWrapper> queue = new PriorityQueue<>();
        // initialize a map to store the distance of each vertex
        HashMap<Vertex, Integer> dist = new HashMap<>();
        // initialize a map to store the back paths
        HashMap<Vertex, Vertex> prev = new HashMap<>();
        HashSet<Vertex> visited = new HashSet<>();

        // set all the distances of the vertices to be infinity
        for(Vertex v : adjacencyList.keySet()){
            dist.put(v, Integer.MAX_VALUE);
            prev.put(v, null);
        }
        // re-update the cost of start to be 0
        dist.put(a, 0);

        // insert the source into the queue
        queue.add(new VertexWrapper(a, 0));

        // main dijkstra loop
        while(!queue.isEmpty()) {
            // remove the top item
            VertexWrapper tmp = queue.poll();
            // unwrap it
            Vertex u = tmp.getVertex();

            // break out of the loop if we found the destination
            if(u.equals(b)){
                break; // <============== BREAK HERE
            }

            // only visit it if we haven't already
            if (!visited.contains(u)) {
                // check all the adjacencies
                for (Vertex v : adjacentVertices(u)) {
                    // calculate the alternative distance and see if it's less than what we have stored
                    int alt = dist.get(u) + edgeCost(u, v);
                    if (alt < dist.get(v)) {
                        // update the cost and back path
                        dist.put(v, alt);
                        prev.put(v, u);
                        // reinsert a wrapper
                        queue.add(new VertexWrapper(v, alt));
                    }
                }
                // mark it as visited
                visited.add(u);
            }
        }


        // find the path from the destination vertex back to the start
        Deque<Vertex> pathStack = new ArrayDeque<>();
        // get a reference for path
        Vertex curr = b;
        while(prev.get(curr) != null){
            pathStack.push(curr); // push to stack because we need to invert it
            curr = prev.get(curr); // get the next back path
        }
        // add the last one
        pathStack.push(curr);
        // return null if the path is unconnected
        if(!pathStack.peek().equals(a)){
            return null;
        }
        int cost = dist.get(b);
        List<Vertex> path = new LinkedList<>();
        while(!pathStack.isEmpty()){
            path.add(pathStack.pop());
        }

        return new Path(path, cost);

    }

    public Set<Edge> kruskalMST(){
        // initialize a priority queue with all the edges, utilizes Floyd's method
        PriorityQueue<Edge> queue = new PriorityQueue<>(edgeSet);

        // create a new DisjointSet
        DisjointSet ds = new DisjointSet(vertices().size());

        // set up the mapping of Vertex to it's representation in the disjoint set
        HashMap<Vertex, Integer> indexMap = new HashMap<>();
        int i = 0;
        for(Vertex v: vertices()){
            indexMap.put(v, i);
            i++;
        }

        // keep a set of the spanning tree
        Set<Edge> span = new HashSet<>();

        while(!queue.isEmpty()){
            // get the next lowest cost edge from the queue
            Edge e = queue.poll();

            // union disjoint sets and add to our tracked span
            int si = indexMap.get(e.getSource());
            int di = indexMap.get(e.getDestination());
            if(ds.find(si) != ds.find(di)){
                ds.union(si,di);
                // add edge to our span
                span.add(e);
            }
        }
        return span;
//        // this is kinda janky
//        int cost = 0;
//        LinkedList<Vertex> path = new LinkedList<>();
//        for(Edge e: span){
//            path.add(e.getSource());
//            path.add(e.getDestination());
//            cost += e.getWeight();
//        }
//
//        return new Path(path, cost);
    }

    class DisjointSet {
        int[] partition;

        DisjointSet(int numElements){
            partition = new int[numElements];
            for(int i = 0; i< partition.length; i++){
                partition[i] = -1;
            }
        }

        public void union(int v1, int v2){
            // find the roots of v1 and v2 to keep the abstraction to client
            int root1 = find(v1);
            int root2 = find(v2);

            // if root2 is taller make it the new overall root (we don't want to make our taller set even taller)
            // update the size of the new overall root to include the older tree size
            if(partition[root2] < partition[root1]){
                partition[root2] += partition[root1];
                partition[root1] = root2;
            }else{ // root 1 is taller
                partition[root1] += partition[root2];
                partition[root2] = root1;
            }
        }

        public int find(int v) {
            if(partition[v] < 0){ // if the set is already a root, return it
                return v;
            }else{
                // Find out who the root is; compress path by making the root x's parent.
                partition[v] = find(partition[v]);
                return partition[v];// Return the root
            }
        }
    }

    /*
    Wrapper class for vertex, keeps the current cost of the distance to it
    allows for ordering in a priority queue by implementing the comparable interface
     */
    class VertexWrapper implements Comparable<VertexWrapper>{
        private Vertex vertex;
        private Integer cost;

        // construct a vertex wrapper with cost and vertex
        VertexWrapper(Vertex vertex, Integer cost){
            this.vertex = vertex;
            this.cost = cost;
        }

        // defines the compareTo in order to implement comparable for ordering in priority queue
        @Override
        public int compareTo(VertexWrapper o) {
            return this.cost - o.getCost();
        }

        // return the cost
        public Integer getCost(){
            return this.cost;
        }
        public Vertex getVertex(){
            return this.vertex;
        }

    }

}
