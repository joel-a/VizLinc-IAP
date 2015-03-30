/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iap;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Glorimar Castro
 */
public class RemoveDuplicatesLinesInfile {
    int amountOfremovedLines;
    
    
    public void execute(File inputFile1, File inputFile2, File outputFile) throws IOException{
        if(inputFile1 == null || inputFile2 == null || outputFile == null)
            throw new IllegalArgumentException("Input files can not be null");
        amountOfremovedLines = 0;
        
        ArrayList<String> resultLines   = new ArrayList();
        HashMap<String, String> labelsInFile1 = iap.Utils.getLabelsToLineMap(inputFile1);
        HashMap<String, String> labelsInFile2 = iap.Utils.getLabelsToLineMap(inputFile2);
        
        for(String label : labelsInFile1.keySet()){
            if(!labelsInFile2.containsKey(label)){
                resultLines.add(labelsInFile1.get(label));
            }else{
                amountOfremovedLines++;
            }
        }
        
        iap.Utils.writeListToFile(resultLines, outputFile);
    }
    
    public int getAmountOfRemovedLines(){
        return amountOfremovedLines;
    }
    
    /*
    private ArrayList<String> getLinesInFile(File input) throws FileNotFoundException, IOException{
        ArrayList<String> result = new ArrayList();
        
        BufferedReader reader = new BufferedReader(new FileReader(input));
        String line;
        while((line = reader.readLine()) != null){
            result.add(line);
        }
        reader.close();
        
        return result;
    }*/
}
