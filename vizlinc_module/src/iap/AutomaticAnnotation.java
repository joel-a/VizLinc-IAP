package iap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import edu.mit.ll.vizlinc.concurrency.VizLincLongTask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import org.gephi.utils.progress.Progress;
import org.openide.util.Exceptions;
/**
 *
 * @author Glory
 */
public class AutomaticAnnotation {
    
    //fields for wikimedia 
    final String wmEndpoint          = "http://en.wikipedia.org/w/api.php?";     //connect to the english database
    final String wmAction            = "action=query";   //wikimedia action
    final String wmSpecifyPageBy     = "titles=";
    final String wmSpace             = "%20";            //wikimedia space normalized
    final String wmProp              = "prop=revisions"; //wikimedia property, for more properties http://www.mediawiki.org/wiki/API:Properties     
    final String wmRvprop            = "rvprop=content"; //wmProp parameter
    final String wmFormat            = "format=json";    
    final String wmOccupation        = "Infobox";
    final int    namesByBlockOf      = 50;              //make page query by ammount of namesByBlockOf at the same time
    
    //fields for wikidata
    final String wikiDataEntity     = "http://www.wikidata.org/wiki/Special:EntityData/";
    final String wikiDataByNameUrl  = "http://www.wikidata.org/wiki/Special:ItemByTitle/enwiki/";
    final String wikiDataTitleKeyWord = "wgTitle\":\"";
    HashMap<String, String> wmTitlesID;
    
    
    //fieds for annotation
    HashMap<String, String> annotationMap;
    URL                     url;
    File                    inputFile;
    File                    outputFile;
    int                     totalPersonsInFile = 0;
    int                     progressCounter = 0; 
    VizLincLongTask         progress;
    
    
    /**
     * 
     * @param inputFile - a csv file with just one column for the names of the person
     * @throws Exception 
     */
    public AutomaticAnnotation(File inputFile, File outputFile, VizLincLongTask progress, DataBaseWiki wikiToConnect) throws Exception{
        annotationMap   = new HashMap();
        this.progress = progress;
        
        if(!iap.Utils.checkFileExtension("csv", (this.inputFile = inputFile)) && !iap.Utils.checkFileExtension("txt", (this.inputFile = inputFile))){
            throw new Exception("Illegal input file extension. The file have to be a csv file");
        }
        if((this.outputFile = outputFile) == null){
            throw new Exception("An output file have to be selected");
        }
        
        if(wikiToConnect == DataBaseWiki.WIKI_DATA){
            setAnnotationWithWikiData();
        }else{
            setAnnotationWithWikiMedia();
        }
        JOptionPane.showMessageDialog(null, "Escribiendo");
        writeResultToFile();
        progressCounter = 0;
        progress.getProgressTicket().finish();
    }
    
    
    /**
     * Makes the annotations using the wikimedia database. If the person is not found in the database, 
     * it sets the role to "null". If the name of the person refers to more than one person, the role for this person is set to "more than one option." 
     * If the person is in the database and do not have any ambiguity the role is set to the Infobox value.
     */
    public void setAnnotationWithWikiMedia(){
        totalPersonsInFile = 0;
        progressCounter = 0;
        annotationMap.clear();
        
        ArrayList<String>       queryTitles     = getNamesOnFileForWikiMedia("|", inputFile, namesByBlockOf);
        progress.getProgressTicket().setDisplayName("Annotatimg...");
        progress.getProgressTicket().switchToDeterminate(totalPersonsInFile);
        
        for(String names : queryTitles){
            try{
                url = setWMURL(names);
                JsonReader jReader = Json.createReader(url.openStream());
                JsonObject pages = jReader.readObject().getJsonObject("query").getJsonObject("pages");
                for(String pageId : pages.keySet()){
                    
                    progress.getProgressTicket().progress(++progressCounter);
                    JsonObject personInfo = pages.getJsonObject(pageId);
                    
                    String name = personInfo.getString("title");
                    
                    if(personInfo.keySet().contains("missing")){        //the person is not in the wikimedia database
                        annotationMap.put(name, "null");
                    }else{
                         JsonArray revisionsValue = personInfo.getJsonArray("revisions");
                        for(int i = 0; i < revisionsValue.size(); i++){
                            String content = revisionsValue.getJsonObject(i).getJsonString("*").getString().toLowerCase();
                            if(content.contains(wmOccupation.toLowerCase())){
                                Scanner sc = new Scanner(content);
                                String line; 
                                
                                while(sc.hasNextLine()){
                                    line = sc.nextLine();
                                    if (line.contains(wmOccupation.toLowerCase())){
                                        annotationMap.put(name, line.substring(line.indexOf(wmOccupation.toLowerCase()) + wmOccupation.length()));
                                         
                                        break;
                                    }
                                }
                                
                            }else{                                                      //ambiguity 
                                annotationMap.put(name, "more than one option");
                            }
                        }
                    }
                    
                }
                
                
                
            } catch (MalformedURLException ex) {
                Logger.getLogger(AutomaticAnnotation.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                 Exceptions.printStackTrace(ex);
             }
        }
        
        
    }
    
    
    public void setAnnotationWithWikiData(){
        totalPersonsInFile = 0;
        progressCounter = 0;
        annotationMap.clear();
        
        ArrayList<String>       queryNames     = getNamesOnFileForWikiData("|", inputFile);
        JOptionPane.showMessageDialog(null, "Nombres obtenidos");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            wmTitlesID = getWikiDataTitles(queryNames);
            progressCounter = 0;
            progress.getProgressTicket().setDisplayName("Making Annotations");
            progress.getProgressTicket().switchToDeterminate(totalPersonsInFile);
            for(String name : wmTitlesID.keySet()){
                if(wmTitlesID.get(name).startsWith("Q")){
                    url = new URL(wikiDataEntity + wmTitlesID.get(name));
                    JsonObject temp = Json.createReader(url.openStream()).readObject().getJsonObject("entities").getJsonObject(wmTitlesID.get(name));
                    
                    writer.write( name + "  " + temp);
                    writer.newLine();
                    writer.flush();
                    
                    
                    if(temp.keySet().contains("descriptions") && temp.getJsonObject("descriptions") != null){  //some description in wikidata are empty
                        JsonObject description = temp.getJsonObject("descriptions");
                        if(description.getJsonObject("en") != null){
                            String role = description.getJsonObject("en").getJsonString("value").toString();
                            annotationMap.put(name, role);
                        }else{
                            annotationMap.put(name, "no description");
                        }
                        
                        
                        
                    
                        writer.write(description.toString());
                        writer.newLine();
                        writer.flush();
                    }else{
                        annotationMap.put(name, "no description");
                        
                    }
                }else{
                    annotationMap.put(name, "null");
                }
                progress.getProgressTicket().progress(++progressCounter);
                
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(AutomaticAnnotation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    /**
     * Reads the input file, normalizes the names to make wikimedia queries, and save them in blocks of namesByBlockOf in an ArrayList
     * @param delimiter
     * @param inputFile
     * @param namesByBlockOf
     * @return 
     */
    public  ArrayList<String> getNamesOnFileForWikiMedia(String delimiter, File inputFile, int namesByBlockOf){
        ArrayList<String>   result  = new ArrayList();
        int count    = 0;          //to keep record of how many names were saved on one string.
        
        progress.getProgressTicket().setDisplayName("Obtaining names in file...");
        progress.getProgressTicket().start();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String names = "";
            String tempName;
            
            while((tempName = reader.readLine()) != null){
                totalPersonsInFile++;
                if(++count%namesByBlockOf != 0){
                    names += delimiter + wikimediaTitleNormalize(tempName);
                }else{
                    result.add(names.substring(1));         //remove the first "|" 
                    names = delimiter + wikimediaTitleNormalize(tempName);
                }  
            }
            
            
            //add las namnes not added. The names are not added if the ammount of names in the string not equal 20
            if(count%namesByBlockOf != 0){
                result.add(names.substring(1));
            }
            
            
            reader.close();
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AutomaticAnnotation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AutomaticAnnotation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    /**
     * Read the names in the input file and add the to an array list already normalized for wikidata queries. 
     * @param delimiter
     * @param inputFile
     * @return 
     */
    public  ArrayList<String> getNamesOnFileForWikiData(String delimiter, File inputFile){
        ArrayList<String>   result  = new ArrayList();
        int count    = 0;          //to keep record of how many names were saved on one string.
        
        this.progress.getProgressTicket().setDisplayName("Getting Names");
        progress.getProgressTicket().start();
        totalPersonsInFile = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String names = "";
            String tempName;
            
            while((tempName = reader.readLine()) != null){
                totalPersonsInFile++;
                result.add(wikiDataTitleNormalize(tempName, " "));
                totalPersonsInFile++;
              
            }
            
            reader.close();
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AutomaticAnnotation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AutomaticAnnotation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    /**
     * Prepare a string s to be a page title for a wikimedia query. In wikimedia the spaces and the underscores 
     * are represented by a %20 and the first character for each word have to be capitalized. 
     * @param s
     * @return 
     */
    public static String wikimediaTitleNormalize(String s){
        if(s == null){
            throw new IllegalArgumentException();
        }
        String result = "";
        String[] temp = s.split(" ");
        
        for(String str : temp){
            result += "%20" + str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
        
        if(result.length() > 3){
            result = result.substring(3);
        }
        return result;
    }
    
     /**
     * Prepare a string s to be a page title for a wikidata query. In wikidata the first character for each word have to be capitalized. 
     * @param s - string to be normalized 
     * @param separator - the character that separates the words in the string s (e.g " ", "," or "_".
     * @return 
     */
    public static String wikiDataTitleNormalize(String s, String separator){
        if(s == null){
            throw new IllegalArgumentException();
        }
        String result = "";
        String[] temp = s.split(separator);
        
        for(String str : temp){
            result += "_" + str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
        
        return result.substring(1);
    }
    
    private URL setWMURL(String names) throws MalformedURLException{
        if(names == null){
            throw new IllegalArgumentException();
        }
        return new URL(wmEndpoint + wmAction + "&" + wmFormat + "&"+ wmProp + "&" + wmRvprop + "&" + wmSpecifyPageBy + names + "&redirects");
          
    }
    
    private void writeResultToFile() throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        progress.getProgressTicket().setDisplayName("Writing...");
        progress.getProgressTicket().switchToDeterminate(annotationMap.size());
        progressCounter = 0;
        for(String name : annotationMap.keySet()){
            progress.getProgressTicket().progress(++progressCounter);
            writer.write(name + "," + annotationMap.get(name));
            writer.newLine();
        }
        
        writer.close();
        
        JOptionPane.showMessageDialog(null, "Annotation result saved in " + outputFile.getName() + "  " + progressCounter + "  "  + annotationMap.size(), "Results Saved", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * This method connect to wikidata and looks for the title id for each person mentioned in the ArrayList given as parameter.
     * @param names - ArrayList with the names of people (already normalized for wikidata) to which the method is going to look for the wikidata title id.
     * @return
     * @throws MalformedURLException
     * @throws IOException 
     */
    private HashMap<String, String> getWikiDataTitles(ArrayList<String> names) throws MalformedURLException, IOException{
        HashMap<String, String> result = new HashMap();
        URL tempUrl;
        int titleKeyWordLength = wikiDataTitleKeyWord.length();
        progress.getProgressTicket().setDisplayName("Getting WikiData Titles ID...");
        for(String name : names){
            tempUrl = new URL(wikiDataByNameUrl + name);
            URLConnection conecction = tempUrl.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conecction.getInputStream()));
            String str;
            while((str = reader.readLine()) != null){
                if(str.contains(wikiDataTitleKeyWord)){
                    int indexOfWgTitle = str.indexOf(wikiDataTitleKeyWord);
                    String title = str.substring(indexOfWgTitle + titleKeyWordLength, str.indexOf("\"", indexOfWgTitle + titleKeyWordLength));
                    result.put(name, title);
                
                }
            }
            reader.close();
        }
        
        return result;
    }
    public HashMap<String, String> getAnnotations(){
        return annotationMap;
    }
    
    public enum DataBaseWiki{
        WIKI_DATA,
        WIKI_MEDIA;
    }
}
