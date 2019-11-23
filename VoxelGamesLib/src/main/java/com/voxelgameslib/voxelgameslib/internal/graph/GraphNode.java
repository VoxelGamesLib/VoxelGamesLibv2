package com.voxelgameslib.voxelgameslib.internal.graph;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * It represents the node of the graph. It holds a user value that is passed back to the user when a node gets the
 * chance to be evaluated.
 *
 * @author nicolae caralicea
 */
final class GraphNode<T> {

    T value;
    private List<GraphNode<T>> comingInNodes;
    private List<GraphNode<T>> goingOutNodes;

    /**
     * Adds an incoming node to the current node
     *
     * @param node The incoming node
     */
    void addComingInNode(@Nonnull GraphNode<T> node) {
        if (comingInNodes == null) {
            comingInNodes = new ArrayList<>();
        }
        comingInNodes.add(node);
    }

    /**
     * Adds an outgoing node from the current node
     *
     * @param node The outgoing node
     */
    void addGoingOutNode(@Nonnull GraphNode<T> node) {
        if (goingOutNodes == null) {
            goingOutNodes = new ArrayList<>();
        }
        goingOutNodes.add(node);
    }

    /**
     * Provides all the coming in nodes
     *
     * @return The coming in nodes
     */
    @Nonnull
    List<GraphNode<T>> getComingInNodes() {
        return comingInNodes;
    }

    /**
     * Provides all the going out nodes
     *
     * @return The going out nodes
     */
    @Nonnull
    List<GraphNode<T>> getGoingOutNodes() {
        return goingOutNodes;
    }
}
