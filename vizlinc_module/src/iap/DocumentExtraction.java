package iap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.nio.file.Path;
import javax.swing.JFileChooser;

/**
 *
 * @author Glorimar Castro
 */
public class DocumentExtraction {
    public static void main(String[] arg){
        String          directoryName       = "default";
        JFileChooser    fileChooser         = new JFileChooser();
        fileChooser.showOpenDialog(null);
        File            inputFileList       = fileChooser.getSelectedFile();
        File            outputDirectory     = new File("D:\\MITLL_IAP\\documentsResult\\" + directoryName);
        
        System.out.println(inputFileList);
        
        
    }
}
