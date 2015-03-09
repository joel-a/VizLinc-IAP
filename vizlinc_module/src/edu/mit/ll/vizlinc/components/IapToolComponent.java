/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.ll.vizlinc.components;

import edu.mit.ll.vizlinc.concurrency.VizLincLongTask;
import iap.AutomaticAnnotation;
import iap.LeadershipAnnotation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.gephi.utils.progress.Progress;
import org.gephi.utils.progress.ProgressTicket;
import org.openide.util.Exceptions;

/**
 *
 * @author Glory
 */
public class IapToolComponent extends javax.swing.JPanel implements java.beans.Customizer {
    
    private Object bean;
    
    //AutomaticAnnotationField
    static private File inputFile;
    static private File outputFile;
    static private AutomaticAnnotation.DataBaseWiki dataBaseType;
    
    //Leadership Annotation Field
    iap.LeadershipAnnotation leadershipAnnotation;
    File leadAnnotInputFile;
    File leadAnnotOutputFile;
    String[] leadKeyWordsList;

    /**
     * Creates new customizer IapToolComponent
     */
    public IapToolComponent() {
        initComponents();
        
        
    }
    
    public void setObject(Object bean) {
        this.bean = bean;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar2 = new javax.swing.JProgressBar();
        jPanel1 = new javax.swing.JPanel();
        autoAnnotationBtn = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        outputFileTxtFieldLeadAnnot = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        inputFileTxtFieldLeadshipAnnot = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        leadershipKeyWords = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        autoAnnotationBtn.setText("Automatic Annotation");
        autoAnnotationBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoAnnotationBtnActionPerformed(evt);
            }
        });
        add(autoAnnotationBtn, java.awt.BorderLayout.PAGE_START);
        add(jProgressBar1, java.awt.BorderLayout.PAGE_END);

        jTabbedPane1.addTab("AutomatAnnot", jPanel3);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Leadership Annotation");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText(org.openide.util.NbBundle.getMessage(IapToolComponent.class, "AutomaticAnnotationOptionsComponent.jLabel1.text")); // NOI18N

        outputFileTxtFieldLeadAnnot.setEditable(false);
        outputFileTxtFieldLeadAnnot.setText(org.openide.util.NbBundle.getMessage(IapToolComponent.class, "AutomaticAnnotationOptionsComponent.outputFileTxtField.text")); // NOI18N

        jButton1.setText(org.openide.util.NbBundle.getMessage(IapToolComponent.class, "AutomaticAnnotationOptionsComponent.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText(org.openide.util.NbBundle.getMessage(IapToolComponent.class, "AutomaticAnnotationOptionsComponent.jLabel2.text")); // NOI18N

        jButton2.setText(org.openide.util.NbBundle.getMessage(IapToolComponent.class, "AutomaticAnnotationOptionsComponent.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        inputFileTxtFieldLeadshipAnnot.setEditable(false);
        inputFileTxtFieldLeadshipAnnot.setText(org.openide.util.NbBundle.getMessage(IapToolComponent.class, "AutomaticAnnotationOptionsComponent.inputFileTxtField.text")); // NOI18N

        jButton3.setText(org.openide.util.NbBundle.getMessage(IapToolComponent.class, "AutomaticAnnotationOptionsComponent.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel4.setText(org.openide.util.NbBundle.getMessage(IapToolComponent.class, "AutomaticAnnotationOptionsComponent.jLabel2.text")); // NOI18N

        leadershipKeyWords.setText(org.openide.util.NbBundle.getMessage(IapToolComponent.class, "AutomaticAnnotationOptionsComponent.outputFileTxtField.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(229, 229, 229))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(leadershipKeyWords, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(144, 144, 144)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inputFileTxtFieldLeadshipAnnot, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                            .addComponent(outputFileTxtFieldLeadAnnot))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(jButton2))))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputFileTxtFieldLeadshipAnnot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(outputFileTxtFieldLeadAnnot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jButton2))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(leadershipKeyWords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LeadershipAnnot", jPanel2);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    public static void setInputFile(File input){
        inputFile = input;
    }
    public static void setOutputFile(File input){
        outputFile = input;
    }
    public static void setDataBaseToUse(AutomaticAnnotation.DataBaseWiki input){
        dataBaseType = input;
    }
    
    
    private void autoAnnotationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoAnnotationBtnActionPerformed
         
         
        AutomaticAnnotationOptionsComponent options = new AutomaticAnnotationOptionsComponent(null, true); 
        options.setVisible(true);
        
        if(inputFile == null || outputFile == null || dataBaseType == null){
            return;
        }
        
        this.setEnabled(false);
        final VizLincLongTask task = new VizLincLongTask("Executing automatic annotation..."){
            @Override
            public void execute()
            {
                ProgressTicket pt = this.getProgressTicket();
                Progress.setDisplayName(pt, "Executing automatic annotation...");
                
               
                
                try {
                    iap.AutomaticAnnotation automaticAnnotation = new AutomaticAnnotation(inputFile, outputFile, this, dataBaseType);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex, "File Error", JOptionPane.ERROR_MESSAGE);
                }
            }
         };
         task.run();
         this.setEnabled(true);
    }//GEN-LAST:event_autoAnnotationBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        leadAnnotInputFile = iap.Utils.selectAnInputFile(new FileNameExtensionFilter("Comma Separated Value", "csv"));
        inputFileTxtFieldLeadshipAnnot.setText(leadAnnotInputFile.getPath());
       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            leadAnnotOutputFile = iap.Utils.selectAnOutputFile(new FileNameExtensionFilter("Comma Separared Vslue", "csv"));
            outputFileTxtFieldLeadAnnot.setText(leadAnnotOutputFile.getPath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Invalid OutputFile");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       if(leadAnnotOutputFile == null || leadAnnotInputFile == null){
           JOptionPane.showMessageDialog(jButton1, "Both files have to be given.");
           return;
       }
       if( leadershipKeyWords.getText().isEmpty()){
           JOptionPane.showConfirmDialog(jButton1, "At least one leadership key word have to be given.");
           return;
       }
       
       leadKeyWordsList = leadershipKeyWords.getText().split(",");
        final VizLincLongTask task = new VizLincLongTask("Executing automatic annotation..."){
            @Override
            public void execute()
            {
                try {
                    leadershipAnnotation = new LeadershipAnnotation(leadAnnotInputFile , leadAnnotOutputFile, leadKeyWordsList, this);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(jButton1, ex);
                }
            }
        };
        
       
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton autoAnnotationBtn;
    private javax.swing.JTextField inputFileTxtFieldLeadshipAnnot;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField leadershipKeyWords;
    private javax.swing.JTextField outputFileTxtFieldLeadAnnot;
    // End of variables declaration//GEN-END:variables
}
