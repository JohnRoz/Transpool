package model.util.collections;


import java.util.*;
import java.util.stream.Collectors;

/**
 * *** WARNING! This class is NOT Thread-Safe!! ***
 * @param <T> Type Parameter for the vertices of the graph. Must override equals() & hashCode()
 * @param <K> Type Parameter for the edges of the graph. Must override equals() & hashCode()
 * @author Jonathan Rozenblat
 */
public class Graph<T, K> {
    private final Map<T, Vertex<T, K>> vertexMap = new HashMap<>();

    //region Mutate Graph
    public void addVertex(T toAdd) {
        vertexMap.putIfAbsent(toAdd, new Vertex<>(toAdd));
    }

    public void removeVertex(T toRemove) {
        if (vertexMap.containsKey(toRemove)) {
            Vertex<T, K> v = vertexMap.remove(toRemove);

            // Remove the literal edges that point to v
            //vertexMap.values().forEach(vertex -> vertex.outwardEdges.removeIf(edge -> edge.dest.equals(v)));
            vertexMap.values().forEach(vertex -> vertex.removeEdgeTo(v));
            v.destroy();
        }
    }

    public void addEdge(T src, T dst, K weight, boolean isBiDirectional) {
        if (!vertexMap.containsKey(src))
            addVertex(src);

        if (!vertexMap.containsKey(dst))
            addVertex(dst);

        Vertex<T, K> srcVertex = vertexMap.get(src);
        Vertex<T, K> dstVertex = vertexMap.get(dst);

        srcVertex.addEdge(dstVertex, weight, isBiDirectional);
    }

    public void removeEdge(K toRemove) {
        vertexMap.values().forEach(vertex -> vertex.removeEdge(toRemove));
    }

    private void clearEdgesFromEndpoint(Vertex<T, K> endpoint) {
        endpoint.clearEdges();
    }
    //endregion

    //region Getters
    public Set<T> getVertices() {
        return vertexMap.keySet();
    }

    public Set<K> getEdges() {
        Set<K> weights = new HashSet<>();

        vertexMap.values().forEach(vertex -> {
            weights.addAll(
                    // Get edges of vertex
                    vertex.getOutwardEdges()
                            // Get weights of edges
                            .stream().map(Edge::getWeight)
                            .collect(Collectors.toSet())
            );
        });

        return weights;
    }
    //endregion

    // TODO: Complete Mocks
    //region Query Graph
    public boolean doesPathExist(T from, T to) {
        // If at least one of the vertices do not exist
        if (!vertexMap.containsKey(from) || !vertexMap.containsKey(to))
            return false;

        return getPath(from, to) != null;
    }

    public Object getPath(T from, T to) {
        // If at least one of the vertices do not exist
        if (!vertexMap.containsKey(from) || !vertexMap.containsKey(to))
            return null;

        return null;
    }
    //endregion

    private static class Vertex<T, K>{
        private T item;
        private Set<Edge<T, K>> outwardEdges;

        public Vertex(T item) {
            this.item = item;
            outwardEdges = new HashSet<>();
        }

        public void destroy() {
            item = null;

            for (Edge<T, K> edge : getOutwardEdges()) {
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
            getOutwardEdges().add(new Edge<>(to, edgeValue));
        }
        //endregion

        public void removeEdgeTo(Vertex<T, K> vertex) {
            getOutwardEdges().removeIf(edge -> edge.getDest().equals(vertex));
        }

        public void removeEdge(K edgeToRemove) {
            Edge<T, K> toRemove = new Edge<>(null, edgeToRemove);
            getOutwardEdges().remove(toRemove);
        }

        public void clearEdges() {
            this.getOutwardEdges().clear();
        }

        private Edge<T, K> getEdgeByWeight(K weight) {
            Edge<T, K> toGet = new Edge<>(null, weight);
            for (Edge<T, K> outwardEdge : getOutwardEdges()) {
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
