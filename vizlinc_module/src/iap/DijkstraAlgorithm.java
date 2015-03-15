/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;

/**
 *
 * @author Glorimar Castro
 */
public class DijkstraAlgorithm extends org.gephi.algorithms.shortestpath.DijkstraShortestPathAlgorithm{

    public DijkstraAlgorithm(Graph graph, Node sourceNode) {
        super(graph, sourceNode);
    }
    
    public Node getSourceNode(){
        return sourceNode;
    }
    
    /**
     * If distance between node A and sourceNode is already calculated it look for it in the HashMap
     * given as parameter and added to the distance for sourceNode and skip that calculation
     * @param shorthDistances 
     */
    public void compute(HashMap<Node, HashMap<Node, Double>> shorthDistances){
        graph.readLock();
        Set<Node> unsettledNodes = new HashSet<Node>();
        Set<Node> settledNodes = new HashSet<Node>();
        
        //add to settleNodes the nodes already calculated
        for(Node n : shorthDistances.keySet()){
            if( shorthDistances.get(n).containsKey(sourceNode)){        //make sgure that the distance netween sourceNode and n is given
                settledNodes.add(n);
            }
        }
        //Initialize
        for (Node node : graph.getNodes()) {
            if(settledNodes.contains(node)){
                distances.put(node, shorthDistances.get(node).get(sourceNode));
            }else{
                distances.put(node, Double.POSITIVE_INFINITY);
            }
            
        }
        distances.put(sourceNode, 0d);
        unsettledNodes.add(sourceNode);

        while (!unsettledNodes.isEmpty()) {

            // find node with smallest distance value
            Double  minDistance     = Double.POSITIVE_INFINITY;
            Node    minDistanceNode = null;
            
            for (Node k : unsettledNodes) {
                Double dist = distances.get(k);     
                if (minDistanceNode == null) {
                    minDistanceNode = k;
                }

                if (dist.compareTo(minDistance) < 0) {
                    minDistance = dist;
                    minDistanceNode = k;
                }
            }
            unsettledNodes.remove(minDistanceNode);
            settledNodes.add(minDistanceNode);

            for (Edge edge : graph.getEdges(minDistanceNode)) {         //here it looks for the neightbors to see were to continue
                Node neighbor = graph.getOpposite(minDistanceNode, edge);
                if (!settledNodes.contains(neighbor)) {
                    double dist = getShortestDistance(minDistanceNode) + edgeWeight(edge);
                    if (getShortestDistance(neighbor) > dist) {

                        distances.put(neighbor, dist);
                        predecessors.put(neighbor, edge);
                        unsettledNodes.add(neighbor);
                        maxDistance = Math.max(maxDistance, dist);
                    }
                }
            }
        }

        graph.readUnlock();
        
    }
    
    private double getShortestDistance(Node destination) {
        Double d = distances.get(destination);
        if (d == null) {
            return Double.POSITIVE_INFINITY;
        } else {
            return d;
        }
    }
    
}
