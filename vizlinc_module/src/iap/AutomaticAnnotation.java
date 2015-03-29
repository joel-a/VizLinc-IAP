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
import java.util.List;
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
import org.json.JSONObject;
//import org.gephi.utils.progress.Progress;
//import org.openide.util.Exceptions;
/**
 *
 * @author Glory
 */
public class AutomaticAnnotation {
    
    //fields for wikimedia 
    final String wmEndpoint          = "http://en.wikipedia.org/w/api.php?";     //connect to the english database
    final String wmAction            = "action=query";   //wikimedia action
    final String wmSpecifyPageBy     = "pageids=";
    final String WM_SPACE             = "%20";            //wikimedia space normalized
    final String wmProp              = "prop=revisions"; //wikimedia property, for more properties http://www.mediawiki.org/wiki/API:Properties     
    final String wmRvprop            = "rvprop=content"; //wmProp parameter
    final String wmFormat            = "format=json";    
    final String wmOccupation        = "Infobox";
    final int    namesByBlockOf      = 50;              //make page query by ammount of namesByBlockOf at the same time
    final String WIKI_BASIC_INFO_URL = "http://en.wikipedia.org/w/api.php?action=query&redirects&prop=pageprops&continue=&format=json&titles=";
   
    //fields for wikidata
    final String wikiDataEntity     = "http://www.wikidata.org/wiki/Special:EntityData/";
    final String WIKIDATA_URL        = "https://www.wikidata.org/w/api.php?continue=&format=json&action=query&redirects&prop=revisions&rvprop=content&titles=";
    final String wikiDataTitleKeyWord = "wgTitle\":\"";
    
    
    //fieds for annotation
    ArrayList<String>       namesOnFile;
    HashMap<String, String> annotationMap;
    HashMap<String, String> wikiNameMap;            //HashMap<wikiname, file name>. 
    HashMap<String, String> pageIdsMap;             //Hash Map<Person name, Page Id for MediaWiki>
    HashMap<String, String> titlesMap;              //HasMap<Person name, wikidata title>
    File                    inputFile;
    File                    outputFile;
    int                     totalPersonsInFile = 0;
    int                     progressCounter = 0; 
    VizLincLongTask         progress;
    DataBaseWiki            wiki2Use;
    ArrayList<String>       missingPersons;
    
    
    /**
     * 
     * @param progress 
     */
    public AutomaticAnnotation(VizLincLongTask progress){
        this.progress = progress;
        
        
    }
    
    /**
     * 
     * @param inputFile - a csv file with just one column for the names of the person
     * @throws Exception 
     */
    /*
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
        //progress.getProgressTicket().finish();
    }*/
    
    
    /**
     * Makes the annotations using the wikimedia database. If the person is not found in the database, 
     * it sets the role to "null". If the name of the person refers to more than one person, the role for this person is set to "more than one option." 
     * If the person is in the database and do not have any ambiguity the role is set to the Infobox value.
     */
    
    private ArrayList<String> setAnnotationWithWikiMedia(List<String> names){
        totalPersonsInFile = 0;
        progressCounter = 0;
        
        ArrayList<String> missingPersons = new ArrayList();
        //progress.getProgressTicket().setDisplayName("Annotatimg...");
        //progress.getProgressTicket().switchToDeterminate(totalPersonsInFile);
        
        for(String name : names){
            try{
                URL url = setWMURL(pageIdsMap.get(name));
                JsonReader jReader = Json.createReader(url.openStream());
                JsonObject pages = jReader.readObject().getJsonObject("query").getJsonObject("pages");
                for(String pageId : pages.keySet()){
                    
                    //progress.getProgressTicket().progress(++progressCounter);
                    JsonObject personInfo = pages.getJsonObject(pageId);
                    
                    if(personInfo.keySet().contains("missing")){        //the person is not in the wikimedia database
                        annotationMap.put(wikiNameMap.get(name), "null");
                        missingPersons.add(name);
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
                                        annotationMap.put(wikiNameMap.get(name), line.substring(line.indexOf(wmOccupation.toLowerCase()) + wmOccupation.length()));
                                         
                                        break;
                                    }
                                }
                                
                            }else{                                                      //ambiguity 
                                annotationMap.put(wikiNameMap.get(name), "more than one option");
                            }
                        }
                    }
                    
                }
                
                
                
            } catch (MalformedURLException ex) {
                Logger.getLogger(AutomaticAnnotation.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                 //Exceptions.printStackTrace(ex);
             }
        }
        return missingPersons;
        
    }
    
    private ArrayList<String> setAnnotationWithWikiMedia(){
        totalPersonsInFile = 0;
        progressCounter = 0;
        
        ArrayList<String> missingPersons = new ArrayList();
        //progress.getProgressTicket().setDisplayName("Annotatimg...");
        //progress.getProgressTicket().switchToDeterminate(totalPersonsInFile);
        
        for(String name : wikiNameMap.keySet()){
            try{
                URL url = setWMURL(pageIdsMap.get(name));
                JsonReader jReader = Json.createReader(url.openStream());
                JsonObject pages = jReader.readObject().getJsonObject("query").getJsonObject("pages");
                for(String pageId : pages.keySet()){
                    
                    //progress.getProgressTicket().progress(++progressCounter);
                    JsonObject personInfo = pages.getJsonObject(pageId);
                    
                    if(personInfo.keySet().contains("missing")){        //the person is not in the wikimedia database
                        annotationMap.put(wikiNameMap.get(name), "null");
                        missingPersons.add(name);
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
                                        annotationMap.put(wikiNameMap.get(name), line.substring(line.indexOf(wmOccupation.toLowerCase()) + wmOccupation.length()));
                                         
                                        break;
                                    }
                                }
                                
                            }else{                                                      //ambiguity 
                                annotationMap.put(wikiNameMap.get(name), "more than one option");
                            }
                        }
                    }
                    
                }
                
                
                
            } catch (MalformedURLException ex) {
                Logger.getLogger(AutomaticAnnotation.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                 //Exceptions.printStackTrace(ex);
             }
        }
        return missingPersons;
        
    }
    private ArrayList<String> setAnnotationWithWikiData() throws MalformedURLException, IOException{
        progressCounter = 0;
        ArrayList<String> missingPersons = new ArrayList();
        ArrayList<String>       queryNames     = new ArrayList();
        
        for(String name : titlesMap.keySet()){
            if(titlesMap.get(name).startsWith("Q")){
                URL url = new URL(wikiDataEntity + titlesMap.get(name));
                JsonObject jsonData = Json.createReader(url.openStream()).readObject().getJsonObject("entities").getJsonObject(titlesMap.get(name));
                if(jsonData.keySet().contains("descriptions") && jsonData.getJsonObject("descriptions") != null){  //some description in wikidata are empty
                    JsonObject description = jsonData.getJsonObject("descriptions");
                    if(description.getJsonObject("en") != null){                        //description in english
                    String role = description.getJsonObject("en").getJsonString("value").toString();
                    annotationMap.put(wikiNameMap.get(name), role);
                    
                    }else{
                        missingPersons.add(name);
                    }
                }else{
                    missingPersons.add(name);
                }
            }else{
                missingPersons.add(name);
            }
       } 
        return missingPersons;
    }
    
    /**
     * Reads the input file, normalizes the names to make wikimedia queries, and save them in blocks of namesByBlockOf in an ArrayList
     * @param inputFile
     * @return 
     */
    private  ArrayList<String> getNamesOnFile(File inputFile) throws FileNotFoundException, IOException{
        totalPersonsInFile = 0;                                 //reset ammount of persons
        
        ArrayList       names   = new ArrayList<String>();
        BufferedReader  reader  = new BufferedReader(new FileReader(inputFile));
            
        String name;
        while((name = reader.readLine()) != null){
            totalPersonsInFile++;
            names.add(normalizeNamesForMediaWiki(name));       
        }
        
        return names;
    }
    
    
    
    /**
     * Prepare a string s to be a page title for a wikimedia query. In wikimedia the spaces and the underscores 
     * are represented by a %20 and the first character for each word have to be capitalized. 
     * @param s
     * @return 
     */
    private static String normalizeNamesForMediaWiki(String s){
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
    
     
    
    private URL setWMURL(String names) throws MalformedURLException{
        if(names == null){
            throw new IllegalArgumentException();
        }
        return new URL(wmEndpoint + wmAction + "&" + wmFormat + "&"+ wmProp + "&" + wmRvprop + "&" + wmSpecifyPageBy + names + "&redirects");
          
    }
    
    private void writeResultToFile() throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        //progress.getProgressTicket().setDisplayName("Writing...");
        //progress.getProgressTicket().switchToDeterminate(annotationMap.size());
        progressCounter = 0;
        for(String name : annotationMap.keySet()){
            //progress.getProgressTicket().progress(++progressCounter);
            writer.write(name + "," + annotationMap.get(name));
            writer.newLine();
            writer.flush();
        }
        
        writer.close();
        
        JOptionPane.showMessageDialog(null, "Annotation result saved in " + outputFile.getName() , "Results Saved", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * This method connect to wikipedia and look for the pageID for wikipedia and the title 
     * for wikidata for each person in the given list.
     * @param names - ArrayList with the names of people (already normalized for wikidata) to which the method is going to look for the wikidata title id.
     * @throws MalformedURLException
     * @throws IOException 
     */
    public void setWikiIds(List<String> names) throws MalformedURLException, IOException{
        wikiNameMap = new HashMap();
        pageIdsMap  = new HashMap();
        titlesMap   = new HashMap();
        annotationMap = new HashMap();
        URL tempUrl;
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for (String name : names){
            tempUrl = new URL(WIKI_BASIC_INFO_URL + name);
            JsonObject jsonData = Json.createReader(tempUrl.openStream()).readObject().getJsonObject("query").getJsonObject("pages");   //jsonobject key batchcompleted / query
            writer.write(name + "  " + jsonData.keySet() );
            writer.newLine();
            writer.flush();
            for(String pageKey : jsonData.keySet()){
                
                if(pageKey.startsWith("-")){
                    annotationMap.put(name.replace("%20", " "), "not found");
                    break;
                }
                
                JsonObject  personDataJson  = jsonData.getJsonObject(pageKey);
                writer.write(personDataJson.keySet().toString() );
                writer.newLine();
                writer.flush();
                
                if(personDataJson.containsKey("missing")){
                    annotationMap.put(name.replace("%20", " "), "not found");
                    break;
                }
                
                if(!personDataJson.containsKey("pageprops") || personDataJson.getJsonObject("pageprops").containsKey("disambiguation")){
                    annotationMap.put(name.replace("%20", " "), "more than one option");
                    break;
                }
                
                String      wikiName        = personDataJson.getJsonString("title").toString().replace("\"", "");
                String      pageId          = personDataJson.getJsonNumber("pageid").toString();
                String      title           = "null";
                if(personDataJson.getJsonObject("pageprops").containsKey("wikibase_item")){
                    title = personDataJson.getJsonObject("pageprops").getJsonString("wikibase_item").toString().replace("\"", "");
                }
                
                writer.write(wikiName + "\t\t" + title + "\t\t" + pageId );
                writer.newLine();writer.newLine();writer.newLine();
                writer.flush();
                
                pageIdsMap.put(wikiName, pageId);
                titlesMap.put(wikiName, title);
                wikiNameMap.put(wikiName, name.replace("%20", " "));
                
                
                
            }
            
        }
        writer.close();
        
    }
    
    
    public HashMap<String, String> getAnnotations(){
        return annotationMap;
    }
    
    public void setWikiToUse(DataBaseWiki wiki2Use){
        this.wiki2Use = wiki2Use;
    }
    
    public void setInputFile(File input){
        if(input == null){
            throw new IllegalArgumentException("Input file is null");
        }
        this.inputFile = input;
        
    }
    
    public void setOutputFile(File outputFile){
        if(outputFile == null){
            throw new IllegalArgumentException("Input file is null");
        }
        this.outputFile = outputFile;
    }
    
    
    public void execute() throws Exception{
        progress.getProgressTicket().start();
        progress.getProgressTicket().setDisplayName("Obtaining names in file...");
        if(inputFile == null || outputFile == null){
            throw new Exception("Input or output file have not be set.");
        }
        namesOnFile = getNamesOnFile(inputFile);
        progress.getProgressTicket().setDisplayName("Getting wiki ids...");
        setWikiIds(namesOnFile);
        if(wiki2Use == DataBaseWiki.WIKIDATA){
            progress.getProgressTicket().setDisplayName("Annotating data with WikiData...");
            missingPersons = setAnnotationWithWikiData();
            for(String name : missingPersons){
                annotationMap.put(wikiNameMap.get(name), "null");
            }
        }else if(wiki2Use == DataBaseWiki.MEDIAWIKI){
            progress.getProgressTicket().setDisplayName("Annotating data with MediaWiki...");
            missingPersons = setAnnotationWithWikiMedia();
        }else{
            progress.getProgressTicket().setDisplayName("Annotating data with WikiData...");
            missingPersons = setAnnotationWithWikiData();
            if(missingPersons.size() > 0){
                progress.getProgressTicket().setDisplayName("Annotating data with MediaWiki...");
                missingPersons = setAnnotationWithWikiMedia(missingPersons);
            }
        }
        progress.getProgressTicket().setDisplayName("Writting results...");
        writeResultToFile();
        progress.getProgressTicket().finish();
       
    }
    public enum DataBaseWiki{
        WIKIDATA,
        MEDIAWIKI,
        WIKIDATA_MEDIAWIKI;
    }
}
