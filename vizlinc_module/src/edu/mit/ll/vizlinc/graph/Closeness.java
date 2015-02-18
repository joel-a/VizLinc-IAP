/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.ll.vizlinc.graph;


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
public class Closeness implements Statistics, LongTask{
    
    public static final String  CLOSENESS = "closeness";
    private double[]            closeness;
    private double              avgDist;
    private int                 N;                          //ammount of node in the visible graph
    private ProgressTicket      progress;
    private boolean             isCanceled;
    private int                 shortestPaths;
    private boolean             isNormalized;
    private boolean             isDirected;
    private int                 numRuns;
    private GeodesicAlgorithm   geodAlgorithm;
 
    
    //--------------------------------------------------------------------------
    //CONSTRUCTORS
    //--------------------------------------------------------------------------
    public Closeness(){
        isDirected      = false;  //for now Vizlin just work with undirected graph
        isNormalized    = true;
        geodAlgorithm   = GeodesicAlgorithm.DIJKSTRA;
        numRuns         = 100;
        
    }
    
    public Closeness(boolean isDirected, boolean isNormalized, GeodesicAlgorithm geodAlgorithm){
        this.isDirected     = isDirected;
        this.isNormalized   = isNormalized;
        this.geodAlgorithm  = geodAlgorithm;
        this.numRuns        = 100;
    }
    
    
    //--------------------------------------------------------------------------
    //Methods
    //--------------------------------------------------------------------------
    @Override
    public void execute(GraphModel gm, AttributeModel am) {
        Graph graph = gm.getGraphVisible();
        execute(graph, am);
    }

    
    public void execute(Graph hgraph, AttributeModel attributeModel){
        //Initials set ups
        hgraph.readLock();                              //lock the visible graph so any future change cannot be made by othe process
        isCanceled      = false;
        this.N          = hgraph.getNodeCount();
        closeness       = new double[N];
        avgDist         = 0;
        shortestPaths   = 0;
        initializeAttributeColunms(attributeModel);
  
        HashMap<Node, Integer> indicies = createIndiciesMap(hgraph); //a hashmap to relate a node with a position index

        //Closeness centrality calculation:
        this.closeness = calculateClosenessMetrics(hgraph, indicies);
        
        //Save values to nodes:
        saveCalculatedValues(hgraph, indicies, closeness);
        
        //Release the graph and finish the process
        hgraph.readUnlock();
        Progress.finish(progress);
    }
    
    
    
    /**
     * Add the attribute of Closeness Centrality to the node attribute table
     * @param attributeModel
     * @return 
     */
    private void initializeAttributeColunms(AttributeModel attributeModel) {
        
       // Glorimar-TODO: try to add  org.gephi.attribute.api.Table or change Table to attributeTable in org.gephi.data.attribute.api.Table
       AttributeTable nodeTable = attributeModel.getNodeTable();  //it need to import org.gephi.attribute.api.Table;
       if (!nodeTable.hasColumn(CLOSENESS)) {
            nodeTable.addColumn(CLOSENESS, "Closeness Centrality", AttributeType.DOUBLE, AttributeOrigin.COMPUTED, new Double(0));
        }
    }
    
     
    private  HashMap<Node, Integer> createIndiciesMap(Graph hgraph) {
       HashMap<Node, Integer> indicies = new HashMap<Node, Integer>();
        int index = 0;
        for (Node s : hgraph.getNodes()) {
            indicies.put(s, index);
            index++;
        } 
        return indicies;
    }
     
    private void saveCalculatedValues(Graph hgraph, HashMap<Node, Integer> indicies, double[] nodeCloseness) {
         
        for (Node s : hgraph.getNodes()) {
            int s_index = indicies.get(s);
            s.getAttributes().setValue(CLOSENESS, new Double(nodeCloseness[s_index]));
        }
          
    }
    private void setInitParametetrsForNode(double[] d, int index, int n) {           
            for (int j = 0; j < n; j++) {
                d[j] = -1;
            }
            d[index] = 0;
    }
    /* to use just for keep a record of the shorterst path
     private void setInitParametetrsForNode(LinkedList<Node>[] P, int[] d, int index, int n) {           
            for (int j = 0; j < n; j++) {
                P[j] = new LinkedList<Node>();
                d[j] = -1;
            }
            d[index] = 0;
    }*/
     
     /**
      * Return a EdgeIterable object that depend of the type of graph. 
      * If the Graph is directed this method return all the out edge for the vector given as parameter. 
      * If it is not a directed graph then it return all the edge associate to the given vector as a parameter.
      * @param hgraph
      * @param v
      * @param directed
      * @return 
      */

    
    /**
     * If the isNormalized field is set to true this method normalize the nodes closeness value 
     * @param hgraph
     * @param indicies
     * @param nodeCloseness
     * @param directed
     * @param normalized 
     */
    private void calculateCorrection(Graph hgraph, HashMap<Node, Integer> indicies, double[] nodeCloseness) {
         
         if (this.isNormalized){
            for (Node s : hgraph.getNodes()) {
                int s_index = indicies.get(s);
                nodeCloseness[s_index] = (nodeCloseness[s_index] == 0) ? 0 : 1.0 / nodeCloseness[s_index]; 
            }
         }
         
     }
    
    //Glorimar-TODO: terminar
    private void calculateDijkstraAlgorithm(Graph hgraph, HashMap<Node, Integer> indicies, Node sourceNode, double[] d){
     
        Set<Node>               unsettledNodes  = new HashSet<Node>(); //S
        Set<Node>               settledNodes    = new HashSet<Node>();
        HashMap<Node, Double>   distances       = new HashMap<Node, Double>();
        HashMap<Node,Edge>      predecessors    = new HashMap<Node, Edge>();   
        double                  maxDistance     = 0;

        //Initialize
        for (Node node : hgraph.getNodes()) {
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

            for (Edge edge : hgraph.getEdges(minDistanceNode)) {
                Node neighbor = hgraph.getOpposite(minDistanceNode, edge);
                if (!settledNodes.contains(neighbor)) {
                    double shortestDist = distances.get(minDistanceNode);
                    double dist = shortestDist + edge.getWeight();
                    if (distances.get(neighbor) > dist) {

                        distances.put(neighbor, dist);
                        predecessors.put(neighbor, edge);
                        unsettledNodes.add(neighbor);
                        maxDistance = Math.max(maxDistance, dist);
                    }
                }
            }
        }
        
        for (Node node : hgraph.getNodes()) {
            d[indicies.get(node)] = (distances.get(node) == Double.POSITIVE_INFINITY ? -1 : distances.get(node));
        }

    }
    
   
    private void calculateBreadthFirstSearch(Graph hgraph, HashMap<Node, Integer> indicies, Node source, double[] d){
        
        LinkedList<Node> Q = new LinkedList<Node>();
            Q.addLast(source);
            while (!Q.isEmpty()) {
                Node v = Q.removeFirst();
                int v_index = indicies.get(v);

                EdgeIterable edgeIter = hgraph.getEdges(v);

                for (Edge edge : edgeIter) {
                    Node reachable = hgraph.getOpposite(v, edge);

                    int r_index = indicies.get(reachable);
                    if (d[r_index] < 0) {
                        Q.addLast(reachable);
                        d[r_index] = d[v_index] + 1;
                    }
                    /* To keep record of path
                    if (d[r_index] == (d[v_index] + 1)) {
                        P[r_index].addLast(v);
                    }*/
                }
            }
    }
    
    /**
     * Method for calculate the closeness centrality for each node in the visible graph.
     * @param hgraph
     * @param indicies
     * @param directed
     * @param normalized
     * @return 
     */
    private double[] calculateClosenessMetrics(Graph hgraph, HashMap<Node, Integer> indicies) {
        int         count           = 0;
        int         n               = hgraph.getNodeCount();    //ammount of node in the visible graph
        double[]    nodeCloseness   = new double[n];            //array to save the closeness value for each node. the index in the array represent the node n with indice = to index in the inicies hashmap
        //Glorimar-NOTE: this is the command controling the progress of the algorithm
        Progress.start(progress, numRuns);
        
        for (Node s : hgraph.getNodes()) {
            //LinkedList<Node>[]  P       = new LinkedList[n];
            double[]            d       = new double[n];  //arreglo de distancias
            int                 s_index = indicies.get(s);
            
            setInitParametetrsForNode(d, s_index, n); 
            
            if(geodAlgorithm == GeodesicAlgorithm.BREADTH_FIRST_SEARCH){
                calculateBreadthFirstSearch(hgraph, indicies, s, d);
            }else{
                calculateDijkstraAlgorithm(hgraph, indicies, s, d);
            }

            double reachable = 0;
            for (int i = 0; i < n; i++) {
                if (d[i] > 0) {
                    avgDist += d[i];
                    nodeCloseness[s_index] += d[i];
                    reachable++;
                }
            }


            if (reachable != 0) {
                nodeCloseness[s_index] /= reachable;
            }

            shortestPaths += reachable;

            ++count;
            if (isCanceled) {
                hgraph.readUnlockAll();
                return nodeCloseness;
            }
            
            Progress.progress(progress, count); //solo calcula los primeros 100 nodo
            if(count == numRuns){
                break;
            }
        }

        avgDist /= shortestPaths;//mN * (mN - 1.0f);

        calculateCorrection(hgraph, indicies, nodeCloseness);
        
        return nodeCloseness;
    }
    
    
    
    @Override
    public String getReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean cancel() {
        return isCanceled;
    }

    //***************************************************************************
    //Setters
    //***************************************************************************
    @Override
    public void setProgressTicket(ProgressTicket pt) {
        this.progress = pt;    
    }
    
    /**
     * To indicate if the graph is directed(true) or undirected(false).
     * @param isDirected 
     */
    public void setIsDirectedGraph(boolean isDirected){
        this.isDirected = isDirected;
    }
    
    
    
    
    public enum GeodesicAlgorithm{
        DIJKSTRA,
        BREADTH_FIRST_SEARCH;
    }
    
    
}
