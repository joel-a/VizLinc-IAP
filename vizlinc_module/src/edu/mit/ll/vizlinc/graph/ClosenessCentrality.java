/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.ll.vizlinc.graph;

import static edu.mit.ll.vizlinc.graph.Closeness.CLOSENESS;
import java.util.HashMap;
import java.util.HashSet;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.statistics.spi.Statistics;
import org.gephi.graph.api.*;
import java.util.LinkedList;
import java.util.Set;
import javax.swing.JOptionPane;
import org.gephi.algorithms.shortestpath.DijkstraShortestPathAlgorithm;
import org.gephi.data.attributes.api.AttributeOrigin;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.Progress;
import org.gephi.utils.progress.ProgressTicket;


/**
 * 
 *
 * @author Glorimar
 */
public class ClosenessCentrality implements Statistics, LongTask{
    
    public static final String  CLOSENESS = "closeness";
    private int                 N;                          //ammount of node in the visible graph
    private ProgressTicket      progress;
    private boolean             isCanceled;
    private boolean             isNormalized;
    private boolean             isDirected;
    private int                 numRuns;
    private boolean             isWeighted;
    
    
 
    
    //--------------------------------------------------------------------------
    //CONSTRUCTORS
    //--------------------------------------------------------------------------
    /**
     * By default directed is set to false, normalized is set to true and weighted is set to false.
     */
    public ClosenessCentrality(){
        isDirected      = false;  //for now Vizlin just work with undirected graph
        isNormalized    = true;
        isWeighted      = false;
        
    }
    
   

    
    @Override
    public void execute(GraphModel graphModel, AttributeModel attributeModel) {
        
        Graph graph = graphModel.getGraphVisible();
        
        this.N      = graph.getNodeCount();
        
        //block the graph while in use by this class
        graph.readLock();
        
        //add Closeness Centrality attribute to the nodes in the graph
        initializeAttributeColunms(attributeModel);
        
        //Start the progress
        Progress.start(progress, N);
        
        
        //calculate closeness
        int progressCount = 0;
        DijkstraShortestPathAlgorithm dijAlg;
        for(Node nSource : graph.getNodes()){
            //increase progress
            Progress.progress(progress, ++progressCount);
            
            progress.setDisplayName("Working with " + nSource + " of " + N);
            //calculate geodesic path
            dijAlg = new DijkstraShortestPathAlgorithm(graph, nSource);
            dijAlg.compute();
            
            //add all the geodesic distance: sumdij
            HashMap<Node, Double> distances = dijAlg.getDistances();
            double sumDij = 0;
            for(Node node : distances.keySet()){
                sumDij += distances.get(node);
            }
            
            //calculate closeness 
            double c = (N - 1)/sumDij;
            
            //add result to node attribute
            nSource.getAttributes().setValue(CLOSENESS, new Double(c));
            JOptionPane.showMessageDialog(null, nSource.getAttributes().getValue(CLOSENESS) + "   " + sumDij);
            
            if(isCanceled){
                return;
            }
        }
        
        //
        graph.readUnlock();
        Progress.finish(progress);
        
        
    }

    @Override
    public String getReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean cancel() {
        this.isCanceled = true;
        return this.isCanceled;
    }

    @Override
    public void setProgressTicket(ProgressTicket progressTicket) {
        this.progress = progressTicket;
    }

   

    /**
     * @return the progress
     */
    public ProgressTicket getProgress() {
        return progress;
    }

    

    /**
     * @return the isCanceled
     */
    public boolean isCanceled() {
        return isCanceled;
    }

    /**
     * @param isCanceled the isCanceled to set
     */
    public void setCanceled(boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    /**
     * @return the isNormalized
     */
    public boolean isNormalized() {
        return isNormalized;
    }

    /**
     * @param isNormalized the isNormalized to set
     */
    public void setNormalized(boolean isNormalized) {
        this.isNormalized = isNormalized;
    }

    /**
     * @return the isDirected
     */
    public boolean isDirected() {
        return isDirected;
    }

    /**
     * @param isDirected the isDirected to set
     */
    public void setDirected(boolean isDirected) {
        this.isDirected = isDirected;
    }

    /**
     * @return the numRuns
     */
    public int getNumRuns() {
        return numRuns;
    }

    /**
     * @return the isWeighted
     */
    public boolean isWeighted() {
        return isWeighted;
    }

    /**
     * @param isWeighted the isWeighted to set
     */
    public void setWeighted(boolean isWeighted) {
        this.isWeighted = isWeighted;
    }
    
    //---------------------------------------------------------------------
    //PRIVATE METHODS
    //---------------------------------------------------------------------
    
    /**
     * Add the Closeness Centrality attribute to the nodes in the graph, and set the closeness value to cero by default
     * @param attributeModel 
     */
    private void initializeAttributeColunms(AttributeModel attributeModel) {
        AttributeTable nodeTable = attributeModel.getNodeTable();
        if (!nodeTable.hasColumn(CLOSENESS)) {
            nodeTable.addColumn(CLOSENESS, "Closeness Centrality", AttributeType.DOUBLE, AttributeOrigin.COMPUTED, new Double(0));
        }
        
    }
    
    /* to calculate shortPath with edge weight = 1;m
    private void dijWeightless(){
        graph.readLock();
        Set<Node> unsettledNodes = new HashSet<Node>();
        Set<Node> settledNodes = new HashSet<Node>();

        //Initialize
        for (Node node : graph.getNodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        distances.put(sourceNode, 0d);
        unsettledNodes.add(sourceNode);

        while (!unsettledNodes.isEmpty()) {

            // find node with smallest distance value
            Double minDistance = Double.POSITIVE_INFINITY;
            Node minDistanceNode = null;
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

            for (Edge edge : graph.getEdges(minDistanceNode)) {
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
    }*/

    
}


