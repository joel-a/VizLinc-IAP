/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iap;

import edu.mit.ll.vizlinc.concurrency.VizLincLongTask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Glory
 */
public class LeadershipAnnotation {
    final char      separator       = ',';  
    final String    LEADERSHIP_KEY  = "1";
    final String    NO_LEADER_KEY   = "0";
    File        inputFile;
    File        outputFile;
    ArrayList<String>   leadershipAnnotation;
    String[]            leadershipKeyWords;            //list of words that identify a person as a leader
    VizLincLongTask progress;
    
    /**
     * 
     * @param inFile
     * @param outFile
     * @param keyWords
     * @throws IOException 
     */
    public LeadershipAnnotation(File inFile, File outFile, String[] keyWords, VizLincLongTask progress) throws IOException{
        
        if(inFile == null || outFile == null){
            throw new IllegalArgumentException("File null");
        }
        if( keyWords == null){
            throw new IllegalArgumentException("List null");
        }
        this.progress = progress;
        inputFile            = inFile;
        outputFile           = outFile;
        leadershipKeyWords   = keyWords;  
        leadershipAnnotation = new ArrayList();
        
        progress.getProgressTicket().start();
        readDataOnFile();
        setLeadershipAnnotations();
        writeResultsOnFile();
        progress.getProgressTicket().finish();
        
    }
    
    private void readDataOnFile() throws IOException{
        //progress.getProgressTicket().setDisplayName("Reading File...");
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String line;
        while((line = reader.readLine()) != null){
            int firstCommaIndex = line.indexOf(",");
            leadershipAnnotation.add(line.substring(0, line.indexOf(",", firstCommaIndex + 1)));
        }
        reader.close();
    }
    
    private void setLeadershipAnnotations(){
       // progress.getProgressTicket().setDisplayName("Making Annotations...");
        //progress.getProgressTicket().switchToDeterminate(leadershipAnnotation.size());
        int progressCount = 0;
        JOptionPane.showMessageDialog(null, leadershipAnnotation.size());
        for(int i = 0; i < leadershipAnnotation.size(); i++){
            boolean dataSetted  = false;
            String  dataLine    = leadershipAnnotation.get(i);
            
            for(String keyWord : leadershipKeyWords){
                if(dataLine.contains(keyWord)){
                    leadershipAnnotation.set(i, dataLine + separator + LEADERSHIP_KEY);
                    dataSetted = true;
                    break;
                }
            }
            
            if(!dataSetted){
                leadershipAnnotation.set(i, dataLine + separator + NO_LEADER_KEY);
            }
            //progress.getProgressTicket().progress(++progressCount);
        }
    }
    
    private void writeResultsOnFile() throws IOException{
        //progress.getProgressTicket().switchToIndeterminate();
        //progress.getProgressTicket().setDisplayName("Writing results...");
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for(String dataLine : leadershipAnnotation){
            writer.write(dataLine);
            writer.newLine();
            writer.flush();
        }
        writer.close();
    }
    
    
}
