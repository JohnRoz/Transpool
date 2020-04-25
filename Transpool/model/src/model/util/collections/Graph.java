package model.util.collections;


import java.util.*;

/**
 * *** WARNING! This class is not Thread-Safe!! ***
 * @param <T> Type Parameter for the vertices of the graph.
 * @param <K> Type Parameter for the edges of the graph.
 * @author Jonathan Rozenblat
 */
public class Graph<T, K> {
    private final Map<Vertex<T, K>, List<Vertex<T, K>>> adjacencyList = new HashMap<>();
    private final Set<Edge<T, K>> edgesPool = new HashSet<>();
    //region Add Vertex
    public void addVertex(T toAdd) {
        addVertex(new Vertex<>(toAdd));
    }

    private void addVertex(Vertex<T, K> toAdd) {
        adjacencyList.putIfAbsent(toAdd, new ArrayList<>());
    }
    //endregion

    public void removeVertex(T toRemove) {
        Vertex<T, K> v = getVertexByItem(toRemove);
        if (v != null) {
            // Remove v's reference from from the lists that point to it
            adjacencyList.values().forEach(verticesList -> verticesList.remove(v));

            // Remove the literal edges that point to v
            adjacencyList.keySet().forEach(vertex -> vertex.outwardEdges.removeIf(edge -> edge.dest.equals(v)));

            // Remove v from the list
            adjacencyList.remove(v);
            v.destroy();
        }
    }

    public void addEdge(T src, T dst, K weight, boolean isBiDirectional) {
        Vertex<T, K> srcVertex = new Vertex<>(src);
        Vertex<T, K> dstVertex = new Vertex<>(dst);

        if (!adjacencyList.containsKey(srcVertex))
            addVertex(srcVertex);

        if (!adjacencyList.containsKey(dstVertex))
            addVertex(dstVertex);

        srcVertex = getVertexByItem(src);
        dstVertex = getVertexByItem(dst);

        adjacencyList.get(srcVertex).add(dstVertex);
        srcVertex.addEdge(dstVertex, weight, isBiDirectional);

        if (isBiDirectional)
            adjacencyList.get(dstVertex).add(srcVertex);
    }

    public void removeEdge(K toRemove) {
        //TODO: REMOVE THE VERTEX FROM THE VERTICES LIST!!
        adjacencyList.values().forEach(verticesList -> verticesList.remove(v));
        adjacencyList.keySet().forEach(vertex -> vertex.removeEdge(toRemove));
    }

    private void removeEdgesFromEndpoint(Vertex<T, K> endpoint) {
        endpoint.getOutwardEdges().clear();
    }

    private Vertex<T, K> getVertexByItem(T item) {
        Vertex<T, K> toFind = new Vertex<>(item);

        if (adjacencyList.containsKey(toFind))
            for (Vertex<T, K> vertex : adjacencyList.keySet()) {
                if (vertex.equals(toFind))
                    return vertex;
            }

        return null;
    }

    private static class Vertex<T, K>{
        private T item;
        private Set<Edge<T, K>> outwardEdges;

        public Vertex(T item) {
            this.item = item;
            outwardEdges = new HashSet<>();
        }

        public void destroy() {
            item = null;

            for (Edge<T, K> edge : outwardEdges) {
                edge.disconnect();
            }

            outwardEdges = null;
        }

        //region Add Edge
        public void addEdge(Vertex<T, K> to, K edgeValue, boolean isBiDirectional) {
          addEdge(to, edgeValue);

            if (isBiDirectional)
                to.addEdge(this, edgeValue);
        }

        private void addEdge(Vertex<T, K> to, K edgeValue) {
            outwardEdges.add(new Edge<>(to, edgeValue));
        }
        //endregion

        public void removeEdge(K edgeToRemove) {
            Edge<T, K> toRemove = new Edge<>(null, edgeToRemove);
            outwardEdges.remove(toRemove);
        }

        public void clearEdges() {
            this.getOutwardEdges().clear();
        }

        private Edge<T, K> getEdgeByWeight(K weight) {
            Edge<T, K> toGet = new Edge<>(null, weight);
            for (Edge<T, K> outwardEdge : outwardEdges) {
                if (outwardEdge.equals(toGet))
                    return outwardEdge;
            }

            return null;
        }

        private boolean hasEdge(K weight) {
            return getEdgeByWeight(weight) != null;
        }

        //region Getters
        private T getItem() {
            return item;
        }

        private Set<Edge<T, K>> getOutwardEdges() {
            return outwardEdges;
        }
        //endregion

        //region Equals & HashCode

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Vertex)) return false;

            Vertex<?, ?> vertex = (Vertex<?, ?>) o;

            return getItem() != null ? getItem().equals(vertex.getItem()) : vertex.getItem() == null;
        }

        @Override
        public int hashCode() {
            return getItem() != null ? getItem().hashCode() : 0;
        }

        //endregion
    }

    private static class Edge<T, K>{
        private Vertex<T, K> dest;
        private K weight;

        private Edge(Vertex<T, K> dest, K value) {
            this.dest = dest;
            this.weight = value;
        }

        //region Destroy Edge
        private static void disconnectEdge(Edge<?, ?> toDisconnect) {
            toDisconnect.getDest().outwardEdges.remove(toDisconnect);

            toDisconnect.dest = null;
            toDisconnect.weight = null;
        }

        private void disconnect() {
            disconnectEdge(this);
        }
        //endregion

        //region Getters
        private Vertex<T, K> getDest() {
            return dest;
        }

        private K getWeight() {
            return weight;
        }

        //endregion

        //region Equals & HashCode

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Edge)) return false;

            Edge<?, ?> edge = (Edge<?, ?>) o;

            return getWeight() != null ? getWeight().equals(edge.getWeight()) : edge.getWeight() == null;
        }

        @Override
        public int hashCode() {
            return getWeight() != null ? getWeight().hashCode() : 0;
        }

        //endregion
    }
}
