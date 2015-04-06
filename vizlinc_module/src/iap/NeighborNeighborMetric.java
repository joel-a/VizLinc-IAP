/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iap;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeOrigin;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.statistics.spi.Statistics;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.Progress;
import org.gephi.utils.progress.ProgressTicket;
import org.openide.util.Lookup;

/**
 *
 * @author Glorimar Castro
 */
public class NeighborNeighborMetric implements LongTask, Statistics{
    public static final String  NEIGHBOR_NEIGHBOR = "Avrg of Neighbors-Neighbors centrality";
    public static final String  NEIGHBOR_NEIGHBOR_LOG = "Log of Neighbors-Neighbors";
    private ProgressTicket      progress;
    private boolean             isCanceled;
    
    public NeighborNeighborMetric(){
        
    }
    
    
    @Override
    public boolean cancel() {
        return this.isCanceled = true;
    }

    @Override
    public void setProgressTicket(ProgressTicket pt) {
       this.progress = pt;
    }

    @Override
    public void execute(GraphModel gm, AttributeModel am) {
        Graph visibleGraph = gm.getGraphVisible();
        Ranking degreeRank = Lookup.getDefault().lookup(RankingController.class).getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
        int progressCount = 0;
        int N = visibleGraph.getNodeCount();
        
        initializeAttributeColunms(am);
        
        if(this.progress != null){
            Progress.start(progress, N);
        }
        
        for(Node sourceN : visibleGraph.getNodes()){                        //Iterate through all nodes in visible graph
            int totalAdjacentNeighbors = (int)degreeRank.getValue(sourceN);
            int totalNonAdjacentNeighbors = 0;
            double totalLogOfNonAdjacentNeigh = 0;
            for(Node neighborNode : visibleGraph.getNeighbors(sourceN)){    //Iterate trough all neighbors of the source Node
                int amntNeighbors = (int)degreeRank.getValue(neighborNode);
                totalLogOfNonAdjacentNeigh += Math.log10(amntNeighbors);
                totalNonAdjacentNeighbors += amntNeighbors;
            }
            
            double neighbosNeighAvr = 0;
            if(totalAdjacentNeighbors != 0){
                neighbosNeighAvr = totalNonAdjacentNeighbors/totalAdjacentNeighbors;
            }
        
            sourceN.getAttributes().setValue(NEIGHBOR_NEIGHBOR, neighbosNeighAvr);
            sourceN.getAttributes().setValue(NEIGHBOR_NEIGHBOR_LOG, totalLogOfNonAdjacentNeigh);
            
            if(isCanceled)
                return;
            
            if(progress != null)
                Progress.progress(progress, ++progressCount);
        }
         
        if(progress != null)
            Progress.finish(progress);
        
    }

    @Override
    public String getReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    /**
     * Add attribute to the nodes in the graph, and set the closeness value to cero by default
     * @param attributeModel 
     */
    private void initializeAttributeColunms(AttributeModel attributeModel) {
        AttributeTable nodeTable = attributeModel.getNodeTable();
        if (!nodeTable.hasColumn(NEIGHBOR_NEIGHBOR)) {
            nodeTable.addColumn(NEIGHBOR_NEIGHBOR, "Neighbors of Neighbors Centrality", AttributeType.DOUBLE, AttributeOrigin.COMPUTED, new Double(0));
        }
        if (!nodeTable.hasColumn(NEIGHBOR_NEIGHBOR_LOG)) {
            nodeTable.addColumn(NEIGHBOR_NEIGHBOR_LOG, "Neighbors of Neighbors Log Centrality", AttributeType.DOUBLE, AttributeOrigin.COMPUTED, new Double(0));
        }
        
    }
}
