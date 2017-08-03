package com.voxelgameslib.voxelgameslib.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.voxelgameslib.voxelgameslib.exception.DependencyGraphException;

/**
 * Represents a graph of nodes. Every node is of GraphNode type and it has set a value of the
 * generic type T. It basically derives an evaluation order out of its nodes. A node gets the chance
 * to be evaluated when all the incoming nodes were previously evaluated. The evaluating method of
 * the NodeValueListener is used to notify the outside of the fact that a node just got the chance
 * to be evaluated. A value of the node that is of the generic type T is passed as argument to the
 * evaluating method.
 *
 * @author nicolae caralicea
 */
public final class Graph<T> {

    /**
     * These are basically the nodes of the graph
     */
    private Map<T, GraphNode<T>> nodes = new HashMap<>();
    /**
     * The callback interface used to notify of the fact that a node just got the evaluation
     */
    private NodeValueListener<T> listener;
    /**
     * It holds a list of the already evaluated nodes
     */
    private List<GraphNode<T>> evaluatedNodes = new ArrayList<>();

    /**
     * The main constructor that has one parameter representing the callback mechanism used by this
     * class to notify when a node gets the evaluation.
     *
     * @param listener The callback interface implemented by the user classes
     */
    public Graph(NodeValueListener<T> listener) {
        this.listener = listener;
    }

    /**
     * Allows adding of new dependencies to the graph. "evalFirstValue" needs to be evaluated before
     * "evalAfterValue"
     *
     * @param evalFirstValue The parameter that needs to be evaluated first
     * @param evalAfterValue The parameter that needs to be evaluated after
     */
    public void addDependency(T evalFirstValue, T evalAfterValue) {
        GraphNode<T> firstNode;
        GraphNode<T> afterNode;
        if (nodes.containsKey(evalFirstValue)) {
            firstNode = nodes.get(evalFirstValue);
        } else {
            firstNode = createNode(evalFirstValue);
            nodes.put(evalFirstValue, firstNode);
        }
        if (nodes.containsKey(evalAfterValue)) {
            afterNode = nodes.get(evalAfterValue);
        } else {
            afterNode = createNode(evalAfterValue);
            nodes.put(evalAfterValue, afterNode);
        }
        firstNode.addGoingOutNode(afterNode);
        afterNode.addComingInNode(firstNode);
    }

    /**
     * Creates a graph node of the T generic type
     *
     * @param value The value that is hosted by the node
     * @return a generic GraphNode object
     */
    private GraphNode<T> createNode(T value) {
        GraphNode<T> node = new GraphNode<>();
        node.value = value;
        return node;
    }

    /**
     * Takes all the nodes and calculates the dependency order for them.
     */
    public void generateDependencies() {
        List<GraphNode<T>> orphanNodes = getOrphanNodes();
        if (orphanNodes == null) {
            throw new DependencyGraphException("No orphan node found: we got circular dependencies!");
        }

        List<GraphNode<T>> nextNodesToDisplay = new ArrayList<>();
        for (GraphNode<T> node : orphanNodes) {
            listener.evaluating(node.value);
            evaluatedNodes.add(node);
            nextNodesToDisplay.addAll(node.getGoingOutNodes());
        }
        generateDependencies(nextNodesToDisplay);
    }

    /**
     * Generates the dependency order of the nodes passed in as parameter
     *
     * @param nodes The nodes for which the dependency order order is executed
     */
    private void generateDependencies(List<GraphNode<T>> nodes) {
        List<GraphNode<T>> nextNodesToDisplay = null;
        for (GraphNode<T> node : nodes) {
            if (!isAlreadyEvaluated(node)) {
                List<GraphNode<T>> comingInNodes = node.getComingInNodes();
                if (areAlreadyEvaluated(comingInNodes)) {
                    listener.evaluating(node.value);
                    evaluatedNodes.add(node);
                    List<GraphNode<T>> goingOutNodes = node.getGoingOutNodes();
                    if (goingOutNodes != null) {
                        if (nextNodesToDisplay == null) {
                            nextNodesToDisplay = new ArrayList<>();
                        }
                        // add these too, so they get a chance to be displayed
                        // as well
                        nextNodesToDisplay.addAll(goingOutNodes);
                    }
                } else {
                    if (nextNodesToDisplay == null) {
                        nextNodesToDisplay = new ArrayList<>();
                    }
                    // the checked node should be carried
                    nextNodesToDisplay.add(node);
                }
            }
        }
        if (nextNodesToDisplay != null) {
            generateDependencies(nextNodesToDisplay);
        }
        // here the recursive call ends
    }

    /**
     * Checks to see if the passed in node was aready evaluated A node defined as already evaluated
     * means that its incoming nodes were already evaluated as well
     *
     * @param node The Node to be checked
     * @return The return value represents the node evaluation status
     */
    private boolean isAlreadyEvaluated(GraphNode<T> node) {
        return evaluatedNodes.contains(node);
    }

    /**
     * Check to see if all the passed nodes were already evaluated. This could be thought as an and
     * logic between every node evaluation status
     *
     * @param nodes The nodes to be checked
     * @return The return value represents the evaluation status for all the nodes
     */
    private boolean areAlreadyEvaluated(List<GraphNode<T>> nodes) {
        return evaluatedNodes.containsAll(nodes);
    }

    /**
     * These nodes represent the starting nodes. They are firstly evaluated. They have no incoming
     * nodes. The order they are evaluated does not matter.
     *
     * @return It returns a list of graph nodes
     */
    private List<GraphNode<T>> getOrphanNodes() {
        List<GraphNode<T>> orphanNodes = null;
        Set<T> keys = nodes.keySet();
        for (T key : keys) {
            GraphNode<T> node = nodes.get(key);
            if (node.getComingInNodes() == null) {
                if (orphanNodes == null) {
                    orphanNodes = new ArrayList<>();
                }
                orphanNodes.add(node);
            }
        }
        return orphanNodes;
    }
}