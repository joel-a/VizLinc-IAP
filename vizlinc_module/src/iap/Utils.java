/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.openide.util.Lookup;

/**
 *
 * @author Glory
 */
public class Utils {
    
    /**
     * 
     * @return 
     */
    public static File selectAnInputFile(){
        JFileChooser    fileChooser         = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int userSelectionOption = fileChooser.showOpenDialog(null); //TODO-Glorimar : use the vizlinc main component
        
        if(userSelectionOption == JFileChooser.APPROVE_OPTION){
            return fileChooser.getSelectedFile();
        }
        
        return null;
    }
    
    /**
     * 
     * @param extension
     * @return 
     */
    public static File selectAnInputFile(FileNameExtensionFilter extension){
        JFileChooser    fileChooser         = new JFileChooser();
        fileChooser.setFileFilter(extension);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int userSelectionOption = fileChooser.showOpenDialog(null); //TODO-Glorimar : use the vizlinc main component
        
        if(userSelectionOption == JFileChooser.APPROVE_OPTION){
            return fileChooser.getSelectedFile();
        }
        
        return null;
    }
    
    public static File selectAnOutputFile() throws IOException{
        JFileChooser    fileChooser         = new JFileChooser();
        File            resultFile;
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int userSelectionOption = fileChooser.showSaveDialog(null); //TODO-Glorimar : use the vizlinc main component
        
        if(userSelectionOption == JFileChooser.APPROVE_OPTION){
            if(!fileChooser.getSelectedFile().exists()){
                fileChooser.getSelectedFile().createNewFile();
            }
            return fileChooser.getSelectedFile();
        }
        
        return null;
    }
    /**
     * 
     * @param extension
     * @return
     * @throws IOException 
     */
    public static File selectAnOutputFile(FileNameExtensionFilter extension) throws IOException{
        JFileChooser    fileChooser         = new JFileChooser();
        File            resultFile;
        
        fileChooser.setFileFilter(extension);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int userSelectionOption = fileChooser.showSaveDialog(null); //TODO-Glorimar : use the vizlinc main component
        
        if(userSelectionOption == JFileChooser.APPROVE_OPTION){
            if(!fileChooser.getSelectedFile().exists()){
                fileChooser.getSelectedFile().createNewFile();
            }
            return fileChooser.getSelectedFile();
        }
        
        return null;
    }
    
    public static File selectAnInputDirectory(){
        JFileChooser    fileChooser         = new JFileChooser();
        File            resultDirectory;
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int userSelectionOption = fileChooser.showOpenDialog(null); //TODO-Glorimar : use the vizlinc main component
        
        if(userSelectionOption == JFileChooser.APPROVE_OPTION){
            return fileChooser.getSelectedFile();
        }
        
        return null;
    }
    
 
    public static boolean checkFileExtension(String extension, File inputFile){
        String fileName = inputFile.getName();
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
        if(fileExt.equalsIgnoreCase(extension)){
            return true;
        }
        return false;
    }
    
    /**
     * Return a HasMap with the node Labels as key and the value is the node itself 
     * @return 
     */
    public static HashMap<String, Node> getLabelToNodeMap(){
        Graph graph = Lookup.getDefault().lookup(GraphController.class).getModel().getGraphVisible();
        
        HashMap<String, Node> labelMap = new HashMap();
        
        for(Node node : graph.getNodes()){
            labelMap.put(node.getNodeData().getLabel(), node);
        }
        
        return labelMap;
    }
    
    public static void writeListToFile(List list, File inputFile) throws IOException{
        if(list == null || inputFile == null){
            throw new IllegalArgumentException("Input file or list can not be null");
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile));
        
        for(Object o : list){
            writer.write(o.toString());
            writer.newLine();
            writer.flush();
        }
        
        writer.close();
    }
    
    public static HashMap<String, String> getLabelsToLineMap(File input) throws FileNotFoundException, IOException{
        final String SEPARATOR = ",";
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
