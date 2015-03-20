/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iap;

import static iap.ClosenessCentrality.CLOSENESS;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeOrigin;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.statistics.spi.Statistics;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.ProgressTicket;

/**
 *
 * @author Glorimar Castro
 */
public class NodeDegree implements Statistics, LongTask{
    
    public static final String  DEGREE =    "degree";
    private int                 N;                          //ammount of node in the visible graph
    private ProgressTicket      progress;
    private boolean             isCanceled;
    
    public NodeDegree(){
        isCanceled = false;
    }
    

    @Override
    public void execute(GraphModel gm, AttributeModel am) {
        Graph graph;
        if(gm.isDirected()){
            graph = gm.getDirectedGraphVisible();
        }else{
            graph = gm.getUndirectedGraphVisible();
        }
        
        
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean cancel() {
        return isCanceled = true;
    }

    @Override
    public void setProgressTicket(ProgressTicket pt) {
        this.progress = pt;
    }
    
    private void initializeAttributeColunms(AttributeModel attributeModel) {
        AttributeTable nodeTable = attributeModel.getNodeTable();
        if (!nodeTable.hasColumn(DEGREE)) {
            nodeTable.addColumn(DEGREE, "Degree Centrality", AttributeType.DOUBLE, AttributeOrigin.COMPUTED, new Double(0));
        }
        
    }
    
}
