/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.ll.vizlinc.graph;


import java.util.HashMap;
import java.util.Map;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.GraphModel;
import org.gephi.statistics.plugin.GraphDistance;
import org.gephi.statistics.spi.Statistics;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.ProgressTicket;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.h2.table.Column;
import java.io.IOException;
import org.gephi.statistics.spi.Statistics;
import org.gephi.graph.api.*;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.gephi.data.attributes.api.AttributeOrigin;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.data.attributes.api.AttributeType;
//import org.gephi.attribute.api.AttributeModel;
//import org.gephi.attribute.api.Column;
//import org.gephi.attribute.api.Table;
//import org.gephi.utils.TempDirUtils;
//import org.gephi.utils.TempDirUtils.TempDir;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.Progress;
import org.gephi.utils.progress.ProgressTicket;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;


/**
 * 
 *
 * @author Glorimar
 */
public class Closeness implements Statistics, LongTask{
    
    public static final String  CLOSENESS = "closeness";
    private double[]            closeness;
    private double              avgDist;
    private int                 N;    //ammount of node in the visible graph
    private ProgressTicket      progress;
    private boolean             isCanceled;
    private int                 shortestPaths;
    private boolean             isNormalized;
    private boolean             isDirected;
    private int                 numRuns = 100;
 
    
    
    public Closeness(){
        isDirected = false;  //for now Vizlin just work with undirected graph
        /*
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        if (graphController != null && graphController.getGraphModel() != null) {
            isDirected = graphController.getGraphModel().isDirected();
        }
        */
    }
    //----------------------------------------------
    //Methods
    //----------------------------------------------
    @Override
    public void execute(GraphModel gm, AttributeModel am) {
        Graph graph = gm.getGraphVisible();
        execute(graph, am);
    }

    
    public void execute(Graph hgraph, AttributeModel attributeModel){
        //Initials set ups
        isCanceled = false;
        initializeAttributeColunms(attributeModel);
        hgraph.readLock();                              //lock the visible graph so any future change can be made by othe process
        this.N = hgraph.getNodeCount();
        initializeStartValues(); 
        
        //---
        Progress.start(progress, numRuns);
        
        HashMap<Node, Integer> indicies = createIndiciesMap(hgraph); //hashmap to relate the node with an index position

        //Closeness centrality calculation:
        JOptionPane.showMessageDialog(null, "Antes de hacer calculo", CLOSENESS, JOptionPane.ERROR_MESSAGE);
        closeness = calculateClosenessMetrics(hgraph, indicies, isDirected, isNormalized);
        
        JOptionPane.showMessageDialog(null, "Guardando valores", CLOSENESS, JOptionPane.ERROR_MESSAGE);
        //Save values to nodes:
        saveCalculatedValues(hgraph, indicies, closeness);
        
        //Release the graph and finish the process
        hgraph.readUnlock();
        Progress.finish(progress);
    }
    
    
    
    //------------------
    //PRIVATE METHODS
    //------------------
    /**
     * Add the attribute of Closeness Centrality to the node attribute table
     * @param attributeModel
     * @return 
     */
    private void initializeAttributeColunms(AttributeModel attributeModel) {
      
        AttributeTable nodeTable = attributeModel.getNodeTable();
        AttributeColumn column = nodeTable.getColumn(CLOSENESS);
        if (column == null) {
             nodeTable.addColumn(CLOSENESS, "Closeness Centrality", AttributeType.DOUBLE, AttributeOrigin.COMPUTED, new Double(0));
        }
    }
    
    private void initializeStartValues() {
        closeness = new double[N];
        avgDist = 0;
        shortestPaths = 0;
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
            //Glorimar-TODO: check why setAttribute method is not recognized 
            s.getAttributes().setValue(CLOSENESS, nodeCloseness[s_index]);
            
        }
          
    }
    
     private void setInitParametetrsForNode(LinkedList<Node>[] P, int[] d, int index, int n) {           
            for (int j = 0; j < n; j++) {
                P[j] = new LinkedList<Node>();
                d[j] = -1;
            }
            d[index] = 0;
    }
     
     /**
      * Return a EdgeIterable object that depend of the type of graph. 
      * If the Graph is directed this method return all the out edge for the vector given as parameter. 
      * If it is not a directed graph then it return all the edge associate to the given vector as a parameter.
      * @param hgraph
      * @param v
      * @param directed
      * @return 
      */
     private EdgeIterable getEdgeIter(Graph hgraph, Node v, boolean directed) {
            EdgeIterable edgeIter = null;
            if (directed) {
                edgeIter = ((DirectedGraph) hgraph).getOutEdges(v);
            } else {
                edgeIter = hgraph.getEdges(v);
             }
            return edgeIter;
    }
    
    /**
     * If the isNormalized field is set to true this method normalize the nodes closeness value 
     * @param hgraph
     * @param indicies
     * @param nodeCloseness
     * @param directed
     * @param normalized 
     */
    private void calculateCorrection(Graph hgraph, HashMap<Node, Integer> indicies, double[] nodeCloseness, boolean directed, boolean normalized) {
         
         if (normalized){
            for (Node s : hgraph.getNodes()) {
                int s_index = indicies.get(s);
                nodeCloseness[s_index] = (nodeCloseness[s_index] == 0) ? 0 : 1.0 / nodeCloseness[s_index]; 
            }
         }
         
     }
    
    
    private double[] calculateClosenessMetrics(Graph hgraph, HashMap<Node, Integer> indicies, boolean directed, boolean normalized) {
        
        int n = hgraph.getNodeCount();
        
        double[] nodeCloseness = new double[n];
        
        Progress.start(progress, hgraph.getNodeCount());
        int count = 0;
        
        for (Node s : hgraph.getNodes()) {
            JOptionPane.showMessageDialog(null, "Loop para el nodo " + s.toString(), CLOSENESS, JOptionPane.ERROR_MESSAGE);
            LinkedList<Node>[]  P       = new LinkedList[n];
                      
            //double[]            theta   = new double[n]; //???????????????????????????????????????????????
            int[]               d       = new int[n];  //distance
            
            int                 s_index = indicies.get(s);
            
            //setInitParametetrsForNode(P, d, s_index, n); //????????????????????????????????
            
            //Inicializa arreglo de lista logada de nodos asi como arreglo de enteros para distancia
            for (int j = 0; j < n; j++) {
                P[j] = new LinkedList<Node>();
                d[j] = -1;
            }
             
            d[s_index] = 0;

            LinkedList<Node> Q = new LinkedList<Node>();            
            Q.addLast(s);
             
            
            while (!Q.isEmpty()) {
                Node v = Q.removeFirst();
                int v_index = indicies.get(v);

                EdgeIterable edgeIter = getEdgeIter(hgraph, v, directed);
                
            JOptionPane.showMessageDialog(null, "edge iteration" + s.toString(), CLOSENESS, JOptionPane.ERROR_MESSAGE);
                for (Edge edge : edgeIter) {
                    Node reachable = hgraph.getOpposite(v, edge);  

                    int r_index = indicies.get(reachable);
                    if (d[r_index] < 0) {
                        Q.addLast(reachable);
                        d[r_index] = d[v_index] + 1;
                    }
                    if (d[r_index] == (d[v_index] + 1)) {
                        P[r_index].addLast(v);
                    }
                }
                 JOptionPane.showMessageDialog(null, "edge iteration finished" + s.toString(), CLOSENESS, JOptionPane.ERROR_MESSAGE);
            }
            double reachable = 0;  //cantidad de nodos alcanzables por el nodo en estudio
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

            count++;
            if (isCanceled) {
                hgraph.readUnlockAll();
                return nodeCloseness;
            }
            Progress.progress(progress, count);
        }

        avgDist /= shortestPaths;//mN * (mN - 1.0f);

        calculateCorrection(hgraph, indicies, nodeCloseness, directed, normalized);
        
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

    @Override
    public void setProgressTicket(ProgressTicket pt) {
        this.progress = pt;    
    }
    
    /*
     for (Node n : graph.getNodes()) {
            int inDegree = 0;
            int outDegree = 0;
            int degree = 0;
            if (isDirected) {
                inDegree = calculateInDegree(directedGraph, n);
                outDegree = calculateOutDegree(directedGraph, n);
            }
            degree = calculateDegree(graph, n);

            if (updateAttributes) {
                n.setAttribute(DEGREE, degree);
                if (isDirected) {
                    n.setAttribute(INDEGREE, inDegree);
                    n.setAttribute(OUTDEGREE, outDegree);
                    updateDegreeDists(inDegree, outDegree, degree);
                } else {
                    updateDegreeDists(degree);

                }
            }
    */
    
    
}
