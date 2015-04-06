/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iap;

import static iap.NeighborNeighborMetric.NEIGHBOR_NEIGHBOR;
import static iap.NeighborNeighborMetric.NEIGHBOR_NEIGHBOR_LOG;
import javax.swing.JOptionPane;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeOrigin;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
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
public class MetricAvrgOfNeighbors implements LongTask, Statistics{
    
    public static final String  NEIGHBORS_METRIC_AVRG = "Avrg of Metrics of Neighbors ";
    private boolean             isCanceled;
    private String              metric;
    private ProgressTicket      progress;
    
    
    
    @Override
    public boolean cancel() {
        return isCanceled = true;
    }

    @Override
    public void setProgressTicket(ProgressTicket pt) {
        this.progress = pt;
    }

    @Override
    public void execute(GraphModel gm, AttributeModel am) {
        if(metric == null){
            JOptionPane.showMessageDialog(null, "The metric has not been initialized.");
        }
        if(!hasMetric(metric, am)){
            JOptionPane.showMessageDialog(null, "The metric has not been calculated. Run the metric first and try again later.");
        }
        
        Graph visibleGraph = gm.getGraphVisible();
        Ranking rank        = Lookup.getDefault().lookup(RankingController.class).getModel().getRanking(Ranking.NODE_ELEMENT, metric);
        Ranking degreeRank  = Lookup.getDefault().lookup(RankingController.class).getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
        
        int progressCount = 0;
        int N = visibleGraph.getNodeCount();
        
        initializeAttributeColunms(am);
        
        if(this.progress != null){
            Progress.start(progress, N);
        }
        
        for(Node sourceN : visibleGraph.getNodes()){                        //Iterate through all nodes in visible graph
            int     totalAdjacentNeighbors  = (int)degreeRank.getValue(sourceN); //amnt of neighbors
            double  metricTotal             = 0;
            
            for(Node neighborNode : visibleGraph.getNeighbors(sourceN)){    //Iterate trough all neighbors of the source Node
                metricTotal += (double)rank.getValue(neighborNode);
            }
            
            double avrg = totalAdjacentNeighbors == 0 ? 0 : metricTotal / totalAdjacentNeighbors;        
            sourceN.getAttributes().setValue(getMetricName(), avrg);
            
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
    
    public void setMetric(String metric){
        this.metric = metric;
    }
    
    public String getMetricName(){
        return metric == null ? null : NEIGHBORS_METRIC_AVRG + metric;
    }
    
    /**
     * Add the Closeness Centrality attribute to the nodes in the graph, and set the closeness value to cero by default
     * @param attributeModel 
     */
    private void initializeAttributeColunms(AttributeModel attributeModel) {
        AttributeTable nodeTable = attributeModel.getNodeTable();
        if (!nodeTable.hasColumn(getMetricName())) {
            nodeTable.addColumn(getMetricName(), getMetricName(), AttributeType.DOUBLE, AttributeOrigin.COMPUTED, new Double(0));
        }
    }
    
    private boolean hasMetric(String metric, AttributeModel attributeModel){
        AttributeTable nodeTable = attributeModel.getNodeTable();
        if (nodeTable.hasColumn(metric) ){
            return true;
        }
        return false;
    }
}
