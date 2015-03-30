/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.gephi.graph.api.Node;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.openide.util.Lookup;

/**
 *
 * @author Glorimar Castro
 */
public class RemoveIsolatedNodes {
    
    final String SEPARATOR = ",";
    
    public RemoveIsolatedNodes(){
    }
    
    
    public void execute(File inputFile) throws IOException{
        if(inputFile == null){
            throw new IllegalArgumentException("Input file can not be null");
        }
        
        ArrayList<String>       result          = new ArrayList();
        HashMap<String, String> fileInfo        = getFileInfo(inputFile);
        HashMap<String, Node>   nodesInGraph    = iap.Utils.getLabelToNodeMap();        //Label, Node
        Ranking                 degreeRanking   = Lookup.getDefault().lookup(RankingController.class).getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
        
        for(String nodeLabel : fileInfo.keySet()){
            if(nodesInGraph.containsKey(nodeLabel) && (int)degreeRanking.getValue(nodesInGraph.get(nodeLabel)) > 0){
                result.add(fileInfo.get(nodeLabel));
            }
        }
        
        iap.Utils.writeListToFile(result, inputFile);
    }
    
    public void execute(File inputFile, File outputFile) throws IOException{
        if(inputFile == null){
            throw new IllegalArgumentException("Input file can not be null");
        }
        
        ArrayList<String>       result          = new ArrayList();
        HashMap<String, String> fileInfo        = getFileInfo(inputFile);
        HashMap<String, Node>   nodesInGraph    = iap.Utils.getLabelToNodeMap();        //Label, Node
        Ranking                 degreeRanking   = Lookup.getDefault().lookup(RankingController.class).getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
        
        for(String nodeLabel : fileInfo.keySet()){
            if(nodesInGraph.containsKey(nodeLabel) && (int)degreeRanking.getValue(nodesInGraph.get(nodeLabel)) > 0){
                result.add(fileInfo.get(nodeLabel));
            }
        }
        
        iap.Utils.writeListToFile(result, outputFile);
    }
    /**
     * Keys are the node labels and value are the lines on input file
     * @param input
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private HashMap<String, String> getFileInfo(File input) throws FileNotFoundException, IOException{
        HashMap<String, String> result = new HashMap();
        BufferedReader reader = new BufferedReader(new FileReader(input));
        String line;
        
        while((line = reader.readLine()) != null){
            int     commaIndx = line.contains(SEPARATOR) ? line.indexOf(SEPARATOR) : line.length();
            String  nodeLabel = line.substring(0, commaIndx).trim().toUpperCase();
            result.put(nodeLabel, line);
        }
        
        reader.close();
        return result;       
    }
    
   
    
}
