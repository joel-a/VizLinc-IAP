/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iap;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.List;
import javax.swing.JFileChooser;

/**
 *
 * @author Glory
 */
public class Utils {
    
    public static File selectAnInputFile(){
        JFileChooser    fileChooser         = new JFileChooser();
        File            resultFile;
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
    
}
