package edu.mit.ll.vizlinc.components;

import edu.mit.ll.vizlinc.concurrency.VizLincLongTask;
import edu.mit.ll.vizlinc.model.AllFacetListModels;
import edu.mit.ll.vizlinc.model.DBManager;
import edu.mit.ll.vizlinc.model.FacetValue;
import edu.mit.ll.vizlinc.model.LocationValue;
import edu.mit.ll.vizlinc.model.PersonValue;
import edu.mit.ll.vizlinc.model.ResultDocSetTableModel;
import edu.mit.ll.vizlinc.model.VLQueryListener;
import edu.mit.ll.vizlinc.utils.DBUtils;
import edu.mit.ll.vizlinc.utils.UIUtils;
import edu.mit.ll.vizlincdb.document.Document;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static java.nio.file.StandardCopyOption.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.lucene.store.Directory;
import org.gephi.utils.progress.Progress;
import org.gephi.utils.progress.ProgressTicket;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

/**
 * Displays all the documents that match the query.
 */
@ConvertAsProperties(
        dtd = "-//edu.mit.ll.vizlinc.components//DocList//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "DocListTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "layoutmode", openAtStartup = true)
@ActionID(category = "Window", id = "edu.mit.ll.vizlinc.components.DocListTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_DocListAction",
        preferredID = "DocListTopComponent")
@Messages(
        {
    "CTL_DocListAction=DocList",
    "CTL_DocListTopComponent=Working Set",
    "HINT_DocListTopComponent=Documents that match your query"
})
public final class DocListTopComponent extends TopComponent implements VLQueryListener
{
    //TODO: Make sure that the current list is cleared before fetching into memory a 
    //new document list.
    private final String SORT_BY_AZ = "A-Z";
    private final String SORT_BY_HITS = "Mentions";
    private final String[] SORT_BY_OPTIONS = new String[]
    {
        SORT_BY_AZ, SORT_BY_HITS
    };

    public DocListTopComponent()
    {
        if(!DBManager.getInstance().isReady())
        {
            return;
        }
        
        initComponents();
        setName(Bundle.CTL_DocListTopComponent());
        setToolTipText(Bundle.HINT_DocListTopComponent());
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);

        //Init doc number label
        updateDocNumLabel(documentTable.getModel().getRowCount());
        //Document table:
        //Make table sortable
        TableRowSorter<ResultDocSetTableModel> sorter = new TableRowSorter(getTableModel());
        documentTable.setRowSorter(sorter);
        //Set row selection listener
        documentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                boolean enableOpenBtn = true;
                if (documentTable.getSelectedRow() == -1)
                {
                    enableOpenBtn = false;
                }
                openBtn.setEnabled(enableOpenBtn);
              
            }
        });
        //If list is not empty save button is enable
        if(documentTable.getRowCount() > 0){
            saveDocLstBtn.setEnabled(true);
        }
        final VLQueryListener thisAsAListener = this;
        WindowManager.getDefault().invokeWhenUIReady(new Runnable()
        {
            @Override
            public void run()
            {
                FacetedSearchTopComponent searchWin = UIUtils.getFacetedSearchWindow();
                if (searchWin != null)
                {
                    searchWin.addQueryListener(thisAsAListener);
                }
            }
        });
    }

    private TableModel createResultDocSetTableModel()
    {
        List<Document> docs;
        try
        {
            docs = DBUtils.getAllDocuments();
            return new ResultDocSetTableModel(docs);
        } catch (SQLException e)
        {
            UIUtils.reportException(this, e);
            return null;
        }
        //DocListModel model = new DocListModel(docs);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        docNumberLabel = new javax.swing.JLabel();
        openBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        documentTable = new javax.swing.JTable();
        saveDocLstBtn = new javax.swing.JButton();
        exportDocBtn = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(DocListTopComponent.class, "DocListTopComponent.jButton1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(docNumberLabel, org.openide.util.NbBundle.getMessage(DocListTopComponent.class, "DocListTopComponent.docNumberLabel.text")); // NOI18N

        openBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mit/ll/vizlinc/ui/icons/open_document.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(openBtn, org.openide.util.NbBundle.getMessage(DocListTopComponent.class, "DocListTopComponent.openBtn.text")); // NOI18N
        openBtn.setToolTipText(org.openide.util.NbBundle.getMessage(DocListTopComponent.class, "DocListTopComponent.openBtn.toolTipText")); // NOI18N
        openBtn.setEnabled(false);
        openBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openBtnActionPerformed(evt);
            }
        });

        documentTable.setModel(createResultDocSetTableModel());
        documentTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        documentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openSelectedDocOnDoubleClick(evt);
            }
        });
        jScrollPane2.setViewportView(documentTable);

        org.openide.awt.Mnemonics.setLocalizedText(saveDocLstBtn, org.openide.util.NbBundle.getMessage(DocListTopComponent.class, "DocListTopComponent.saveDocLstBtn.text")); // NOI18N
        saveDocLstBtn.setEnabled(false);
        saveDocLstBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDocLstBtnActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(exportDocBtn, org.openide.util.NbBundle.getMessage(DocListTopComponent.class, "DocListTopComponent.exportDocBtn.text")); // NOI18N
        exportDocBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportDocBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(docNumberLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(exportDocBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveDocLstBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(openBtn))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(docNumberLabel)
                    .addComponent(openBtn)
                    .addComponent(saveDocLstBtn)
                    .addComponent(exportDocBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void openBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_openBtnActionPerformed
    {//GEN-HEADEREND:event_openBtnActionPerformed
        openBtn.setEnabled(false);
        openSelectedDocument();
    }//GEN-LAST:event_openBtnActionPerformed

    private void openSelectedDocOnDoubleClick(java.awt.event.MouseEvent evt)//GEN-FIRST:event_openSelectedDocOnDoubleClick
    {//GEN-HEADEREND:event_openSelectedDocOnDoubleClick

        if (evt.getSource() == documentTable && evt.getClickCount() == 2)
        {
            openSelectedDocument();
        }
    }//GEN-LAST:event_openSelectedDocOnDoubleClick

    /**
     * BtnActionPerformed for the saveDocList button. This method is for save in a text file the names of the documents showed by Document Table.
     * @param evt 
     */
    private void saveDocLstBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDocLstBtnActionPerformed
        FileNameExtensionFilter     fileExten   = new FileNameExtensionFilter("Text Document", "txt");
        JFileChooser                fileChooser = new JFileChooser();
        fileChooser.setFileFilter(fileExten);
        
        //If the person select a file to save the results then all the docuements names in the documentTable are save to the file
        if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            try {
                File            file    = new File(fileChooser.getSelectedFile() + ".txt");
                BufferedWriter  bw      = new BufferedWriter(new FileWriter(file, true));
                file.createNewFile();
                
                for(int i = 0; i < documentTable.getRowCount(); i++){
                    String temp = documentTable.getValueAt(i, 0).toString();
                    String result = temp.substring(0, temp.lastIndexOf(".txt"));
                    bw.write(result);
                    bw.newLine();
                }
                bw.close();
                
                
            } catch (IOException ex) {
                //TODO-Glorimar enternder que hasce esto                
                UIUtils.reportException(this, ex);
            }
            
        } 
        
    }//GEN-LAST:event_saveDocLstBtnActionPerformed

    private void exportDocBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportDocBtnActionPerformed

         
         //Perform query in a different thread
        final VizLincLongTask task = new VizLincLongTask("Executing Or query...")
        {
            @Override
            public void execute()
            {
                ProgressTicket pt = this.getProgressTicket();
                Progress.setDisplayName(pt, "Exporting...");
                Progress.start(pt);
                
                JFileChooser    fileChooser         = new JFileChooser();
                File            outputDirectory;                                    //Directory to copy all the documents in the documentTable
                File            documentsDirectory;                                 //Directory with all the source documents
                int             userSelectionOption;                                //variable to save the result of the user selection option: Ok or Cancel
                DirectoryStream<Path> filesInDocDir;
                List            docList;                                            //list with all documents in documentTable

                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                userSelectionOption = fileChooser.showOpenDialog(null); //TODO-Glorimar : use the vizlinc main component

                if(userSelectionOption != JFileChooser.APPROVE_OPTION){
                    return;
                }
                documentsDirectory = fileChooser.getSelectedFile();
                userSelectionOption = fileChooser.showSaveDialog(null);

                if(userSelectionOption != JFileChooser.APPROVE_OPTION){
                    return;
                }
                outputDirectory = fileChooser.getSelectedFile();

                try {
                    filesInDocDir = Files.newDirectoryStream(Paths.get(documentsDirectory.getAbsolutePath()) );

                    docList = new ArrayList<String>();

                    for(int i = 0; i < documentTable.getRowCount(); i++){
                        String fileName = documentTable.getValueAt(i, 0).toString().substring(0, documentTable.getValueAt(i, 0).toString().lastIndexOf(".txt"));
                        docList.add(fileName);

                    }


                    //TODO-Glorimar: verify the comparision in docList.Cont 
                    for(Path p : filesInDocDir){
                        String fileName = p.getFileName().toString();
                        Path pathToCopyFile = Paths.get(outputDirectory.getPath() + "\\" + fileName);



                        if(docList.contains(fileName)){
                            //Path tempPath = Paths.get(outputDirectory.getPath() + "\\" + p.getFileName());
                            Files.createFile(pathToCopyFile);
                            Files.copy(p, pathToCopyFile, REPLACE_EXISTING);
                        }
                    }
                    filesInDocDir.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        };
        task.run();
        
        
    }//GEN-LAST:event_exportDocBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel docNumberLabel;
    private javax.swing.JTable documentTable;
    private javax.swing.JButton exportDocBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton openBtn;
    private javax.swing.JButton saveDocLstBtn;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened()
    {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed()
    {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p)
    {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p)
    {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    /**
     * Sets the displayed document list to the one passed as argument
     *
     * @param resultDocList new result list
     */
    private void setDocumentList(final List<Document> resultDocList)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                updateListAndDocLabel(resultDocList);
            }
        });
    }

    private void updateListAndDocLabel(List<Document> docs)
    {    
        ResultDocSetTableModel model = getTableModel();
        try
        {
            model.setList(docs);
        } 
        catch(Exception e)
        {
            UIUtils.reportException(this, e);
            return;
        }
        //Update label to show number of documents in current list
        int listSize = docs.size();
        updateDocNumLabel(listSize);
        //Enable list
        documentTable.setEnabled(true);
        //Enable Save Button
        saveDocLstBtn.setEnabled(true);
        
        //if there is something selected in the list enable open button
        if (documentTable.getSelectedRow() != -1)
        {
            openBtn.setEnabled(true);
        }
    }

    private void updateDocNumLabel(int listSize)
    {
        DecimalFormat dF = new DecimalFormat("###,###");
        String msg = dF.format((long) listSize);
        if (listSize == 1)
        {
            msg = msg + " document";
        } else
        {
            msg = msg + " documents";
        }

        docNumberLabel.setText(msg);
    }
    
    private ResultDocSetTableModel getTableModel()
    {
        return (ResultDocSetTableModel) documentTable.getModel();

    }

    /**
     * Reflects that a query is in progress and that display will be updated
     * soon Note: this method is always called from the UI dispatch thread
     */
    @Override
    public void aboutToExecuteQuery()
    {
        try
        {
            SwingUtilities.invokeAndWait(new Runnable()
            {
                @Override
                public void run()
                {
                    docNumberLabel.setText("Please Wait...");
                    documentTable.setEnabled(false);
                    saveDocLstBtn.setEnabled(false);
                }
            });
        }
        catch (Exception e)
        {
            UIUtils.reportException(this, e);
        }
    }

    @Override
    public void queryFinished(List<Document> documents, List<LocationValue> locationsInFacetTree, List<PersonValue> peopleInFacetTree)
    {
        setDocumentList(documents);
    }

    private boolean areFiltersSelected()
    {
        int numFilters = UIUtils.getQueryWindow().getFilters().size();
        return (numFilters > 0) ? true : false;
    }

    private void openSelectedDocument()
    {
        int selectedRow = documentTable.getSelectedRow();
        if (selectedRow != -1)
        {
            
            Document selectedDocument = getTableModel().getDocumentByRow(documentTable.convertRowIndexToModel(selectedRow));
            //Lookup doc content window
            DocViewTopComponent docWin = (DocViewTopComponent) WindowManager.getDefault().findTopComponent("DocViewTopComponent");
            docWin.setDoc(selectedDocument);
            
        }
    }
}