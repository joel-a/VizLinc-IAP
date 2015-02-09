/*
 * 
 */
package edu.mit.ll.vizlinc.components;

import edu.mit.ll.vizlinc.graph.GraphManager;
import edu.mit.ll.vizlinc.graph.layout.community.Cluster;
import edu.mit.ll.vizlinc.model.DBManager;
import edu.mit.ll.vizlinc.model.GraphOperationListener;
import edu.mit.ll.vizlinc.model.LocationValue;
import edu.mit.ll.vizlinc.model.PersonValue;
import edu.mit.ll.vizlinc.model.VLQueryListener;
import edu.mit.ll.vizlinc.utils.UIUtils;
import edu.mit.ll.vizlincdb.document.Document;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;
import org.gephi.graph.api.Node;

/**
 * Displays one-click graph analytics
 */
@ConvertAsProperties(
        dtd = "-//edu.mit.ll.vizlinc.components//GraphTools//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "GraphToolsTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "layoutmode", openAtStartup = true)
@ActionID(category = "Window", id = "edu.mit.ll.vizlinc.components.GraphToolsTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_GraphToolsAction",
        preferredID = "GraphToolsTopComponent")
@Messages({
    "CTL_GraphToolsAction=Graph Tools",
    "CTL_GraphToolsTopComponent=Graph Tools",
    "HINT_GraphToolsTopComponent=This is a Graph Tools window"
})
public final class GraphToolsTopComponent extends TopComponent implements GraphOperationListener, VLQueryListener {

    public GraphToolsTopComponent() throws FileNotFoundException {
        if(!DBManager.getInstance().isReady())
        {
            return;
        }
        initComponents();
        setName(Bundle.CTL_GraphToolsTopComponent());
        setToolTipText(Bundle.HINT_GraphToolsTopComponent());
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);

        //Find map window and save a pointer to it. This should happen when the UI is ready
        final GraphToolsTopComponent self = this;
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                TopComponent graphTopComponent = WindowManager.getDefault().findTopComponent("GraphTopComponent");
                graphTopComponent.setDisplayName("Social Network Graph");
                
                GraphManager.getInstance().addGraphOperationListener(self);
                FacetedSearchTopComponent searchWin = UIUtils.getFacetedSearchWindow();
                if(searchWin != null)
                {
                    searchWin.addQueryListener(self);
                }
                
//                for (Component component : graphTopComponent.getComponents()) {
//                    if (component instanceof Container) {
//                        for (Component c : ((Container) component).getComponents()) {
//                            System.out.println(":::" + c.getClass().getName());
//                            if (c.getClass().getName().equals("org.gephi.visualization.component.GraphTopComponent$AddonsBar")) {
//                                for (Component c1 : ((Container) c).getComponents()) {
//                                    System.out.println("  :::" + c1.getClass().getName());
//                                }
//                            }
//                        }
//                    }
//                }
            }
        });
    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBoxShowEdges = new javax.swing.JCheckBox();
        jCheckBoxShowAllLabels = new javax.swing.JCheckBox();
        jButtonPageRank = new javax.swing.JButton();
        jButtonCentrality = new javax.swing.JButton();
        jButtonCluster = new javax.swing.JButton();
        jCheckBoxCentralityShowBySize = new javax.swing.JCheckBox();
        jCheckBoxCentralityShowByColor = new javax.swing.JCheckBox();
        jCheckBoxPageRankShowByColor = new javax.swing.JCheckBox();
        jCheckBoxPageRankShowBySize = new javax.swing.JCheckBox();
        jButtonResetSizes = new javax.swing.JButton();
        jButtonResetColors = new javax.swing.JButton();
        jSpinnerResetSizesValue = new javax.swing.JSpinner();
        jSpinnerClusterLambda = new javax.swing.JSpinner();
        jLabelLogLambda = new javax.swing.JLabel();
        jButtonOneHop = new javax.swing.JButton();
        jButtonTwoHop = new javax.swing.JButton();
        jButtonShowAllInQuery = new javax.swing.JButton();
        jLabelGraphStatus = new javax.swing.JLabel();
        clusterLayoutBtn = new javax.swing.JButton();
        jCheckBoxClosenessShowBySize = new javax.swing.JCheckBox();
        jButtonCloseness = new javax.swing.JButton();
        jCheckBoxClosenessShowByColor = new javax.swing.JCheckBox();

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxShowEdges, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jCheckBoxShowEdges.text")); // NOI18N
        jCheckBoxShowEdges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxShowEdgesActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxShowAllLabels, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jCheckBoxShowAllLabels.text")); // NOI18N
        jCheckBoxShowAllLabels.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxShowAllLabelsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonPageRank, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jButtonPageRank.text_3")); // NOI18N
        jButtonPageRank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPageRankActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonCentrality, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jButtonCentrality.text_3")); // NOI18N
        jButtonCentrality.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCentralityActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonCluster, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jButtonCluster.text")); // NOI18N
        jButtonCluster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClusterActionPerformed(evt);
            }
        });

        jCheckBoxCentralityShowBySize.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxCentralityShowBySize, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jCheckBoxCentralityShowBySize.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxCentralityShowByColor, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jCheckBoxCentralityShowByColor.text")); // NOI18N
        jCheckBoxCentralityShowByColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCentralityShowByColorActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxPageRankShowByColor, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jCheckBoxPageRankShowByColor.text")); // NOI18N
        jCheckBoxPageRankShowByColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPageRankShowByColorActionPerformed(evt);
            }
        });

        jCheckBoxPageRankShowBySize.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxPageRankShowBySize, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jCheckBoxPageRankShowBySize.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonResetSizes, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jButtonResetSizes.text")); // NOI18N
        jButtonResetSizes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetSizesActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonResetColors, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jButtonResetColors.text")); // NOI18N
        jButtonResetColors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetColorsActionPerformed(evt);
            }
        });

        jSpinnerResetSizesValue.setModel(new javax.swing.SpinnerNumberModel(4, 1, 20, 1));

        jSpinnerClusterLambda.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(-2.0f), Float.valueOf(2.0f), Float.valueOf(0.5f)));

        org.openide.awt.Mnemonics.setLocalizedText(jLabelLogLambda, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jLabelLogLambda.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonOneHop, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jButtonOneHop.text")); // NOI18N
        jButtonOneHop.setToolTipText(org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jButtonOneHop.toolTipText")); // NOI18N
        jButtonOneHop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOneHopActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonTwoHop, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jButtonTwoHop.text")); // NOI18N
        jButtonTwoHop.setToolTipText(org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jButtonTwoHop.toolTipText")); // NOI18N
        jButtonTwoHop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTwoHopActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonShowAllInQuery, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jButtonShowAllInQuery.text")); // NOI18N
        jButtonShowAllInQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonShowAllInQueryActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabelGraphStatus, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jLabelGraphStatus.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(clusterLayoutBtn, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.clusterLayoutBtn.text")); // NOI18N
        clusterLayoutBtn.setEnabled(false);
        clusterLayoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clusterLayoutBtnActionPerformed(evt);
            }
        });

        jCheckBoxClosenessShowBySize.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxClosenessShowBySize, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jCheckBoxClosenessShowBySize.text")); // NOI18N
        jCheckBoxClosenessShowBySize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxClosenessShowBySizeActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButtonCloseness, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jButtonCloseness.text")); // NOI18N
        jButtonCloseness.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClosenessActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxClosenessShowByColor, org.openide.util.NbBundle.getMessage(GraphToolsTopComponent.class, "GraphToolsTopComponent.jCheckBoxClosenessShowByColor.text")); // NOI18N
        jCheckBoxClosenessShowByColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxClosenessShowByColor1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelGraphStatus)
                .addContainerGap(972, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxShowEdges)
                    .addComponent(jCheckBoxShowAllLabels))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonResetSizes, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonResetColors))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinnerResetSizesValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBoxPageRankShowBySize)
                        .addGap(1, 1, 1)
                        .addComponent(jCheckBoxPageRankShowByColor, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonPageRank, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBoxCentralityShowBySize)
                        .addGap(1, 1, 1)
                        .addComponent(jCheckBoxCentralityShowByColor))
                    .addComponent(jButtonCentrality, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonCloseness, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBoxClosenessShowBySize)
                        .addGap(1, 1, 1)
                        .addComponent(jCheckBoxClosenessShowByColor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelLogLambda)
                    .addComponent(jButtonCluster))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clusterLayoutBtn)
                    .addComponent(jSpinnerClusterLambda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonOneHop)
                        .addGap(23, 23, 23)
                        .addComponent(jButtonTwoHop))
                    .addComponent(jButtonShowAllInQuery))
                .addGap(59, 59, 59))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jCheckBoxShowEdges)
                                    .addComponent(jButtonResetSizes)
                                    .addComponent(jSpinnerResetSizesValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButtonPageRank)
                                .addComponent(jButtonCentrality))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jCheckBoxPageRankShowBySize)
                                    .addComponent(jCheckBoxShowAllLabels)
                                    .addComponent(jButtonResetColors))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jCheckBoxCentralityShowBySize)
                                    .addComponent(jCheckBoxPageRankShowByColor)
                                    .addComponent(jCheckBoxCentralityShowByColor))))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButtonCluster)
                                .addComponent(jButtonOneHop)
                                .addComponent(jButtonTwoHop)
                                .addComponent(clusterLayoutBtn))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jSpinnerClusterLambda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelLogLambda)
                                .addComponent(jButtonShowAllInQuery)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jCheckBoxClosenessShowBySize)
                                    .addComponent(jCheckBoxClosenessShowByColor)))))
                    .addComponent(jButtonCloseness))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelGraphStatus)
                .addContainerGap(21, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxShowAllLabelsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxShowAllLabelsActionPerformed
        // Use ActionPerformed instead of ItemStateChanged so we won't trigger on programmatic changes.
        GraphManager.getInstance().setShowAllLabels(jCheckBoxShowAllLabels.isSelected());
    }//GEN-LAST:event_jCheckBoxShowAllLabelsActionPerformed

    private void jCheckBoxShowEdgesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxShowEdgesActionPerformed
        GraphManager.getInstance().setShowEdges(jCheckBoxShowEdges.isSelected());
    }//GEN-LAST:event_jCheckBoxShowEdgesActionPerformed

    private void jButtonClusterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClusterActionPerformed
        // 5 trials is fine.
        GraphManager.getInstance().cluster(5, Math.pow(10.0, (Float) jSpinnerClusterLambda.getValue()));
    }//GEN-LAST:event_jButtonClusterActionPerformed

    private void jButtonPageRankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPageRankActionPerformed
        final boolean bySize = jCheckBoxPageRankShowBySize.isSelected();
        final boolean byColor = jCheckBoxPageRankShowByColor.isSelected();
        GraphManager.getInstance().pageRank(bySize, byColor);
    }//GEN-LAST:event_jButtonPageRankActionPerformed

    private void jButtonCentralityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCentralityActionPerformed
        final boolean bySize = jCheckBoxCentralityShowBySize.isSelected();
        final boolean byColor = jCheckBoxCentralityShowByColor.isSelected();
        GraphManager.getInstance().centrality(bySize, byColor);
    }//GEN-LAST:event_jButtonCentralityActionPerformed

    private void jCheckBoxPageRankShowByColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPageRankShowByColorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxPageRankShowByColorActionPerformed

    private void jCheckBoxCentralityShowByColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCentralityShowByColorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxCentralityShowByColorActionPerformed

    private void jButtonResetSizesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetSizesActionPerformed
        GraphManager.getInstance().resetSizes((float) (Integer) jSpinnerResetSizesValue.getValue());
    }//GEN-LAST:event_jButtonResetSizesActionPerformed

    private void jButtonResetColorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetColorsActionPerformed
        GraphManager.getInstance().resetColors(new Color(0.6f, 0.6f, 0.6f));   // default color used by Gephi.
    }//GEN-LAST:event_jButtonResetColorsActionPerformed

    private void jButtonTwoHopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTwoHopActionPerformed
        GraphManager.getInstance().displayNHops(2);
    }//GEN-LAST:event_jButtonTwoHopActionPerformed

    private void jButtonShowAllInQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonShowAllInQueryActionPerformed
            GraphManager.getInstance().displayQueryGraph();
    }//GEN-LAST:event_jButtonShowAllInQueryActionPerformed

    private void jButtonOneHopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOneHopActionPerformed
        GraphManager.getInstance().displayNHops(1);
    }//GEN-LAST:event_jButtonOneHopActionPerformed

    private void clusterLayoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clusterLayoutBtnActionPerformed
        GraphManager manager = GraphManager.getInstance();
        manager.showSuperNodeGraph();
    }//GEN-LAST:event_clusterLayoutBtnActionPerformed

    private void jButtonClosenessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClosenessActionPerformed
        final boolean bySize = true;//jCheckBoxClosenessShowBySize.isSelected();
        //final boolean byColor = jCheckBoxClosenessShowByColor.isSelected();
        GraphManager.getInstance().closeness(bySize);
    }//GEN-LAST:event_jButtonClosenessActionPerformed

    private void jCheckBoxClosenessShowByColor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxClosenessShowByColor1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxClosenessShowByColor1ActionPerformed

    private void jCheckBoxClosenessShowBySizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxClosenessShowBySizeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxClosenessShowBySizeActionPerformed

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clusterLayoutBtn;
    private javax.swing.JButton jButtonCentrality;
    private javax.swing.JButton jButtonCloseness;
    private javax.swing.JButton jButtonCluster;
    private javax.swing.JButton jButtonOneHop;
    private javax.swing.JButton jButtonPageRank;
    private javax.swing.JButton jButtonResetColors;
    private javax.swing.JButton jButtonResetSizes;
    private javax.swing.JButton jButtonShowAllInQuery;
    private javax.swing.JButton jButtonTwoHop;
    private javax.swing.JCheckBox jCheckBoxCentralityShowByColor;
    private javax.swing.JCheckBox jCheckBoxCentralityShowBySize;
    private javax.swing.JCheckBox jCheckBoxClosenessShowByColor;
    private javax.swing.JCheckBox jCheckBoxClosenessShowBySize;
    private javax.swing.JCheckBox jCheckBoxPageRankShowByColor;
    private javax.swing.JCheckBox jCheckBoxPageRankShowBySize;
    private javax.swing.JCheckBox jCheckBoxShowAllLabels;
    private javax.swing.JCheckBox jCheckBoxShowEdges;
    private javax.swing.JLabel jLabelGraphStatus;
    private javax.swing.JLabel jLabelLogLambda;
    private javax.swing.JSpinner jSpinnerClusterLambda;
    private javax.swing.JSpinner jSpinnerResetSizesValue;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
        GraphManager.getInstance().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if (propertyName.equals("showEdges")) {
                    jCheckBoxShowEdges.setSelected((Boolean) evt.getNewValue());
                } else if (propertyName.equals("showAllLabels")) {
                    jCheckBoxShowAllLabels.setSelected((Boolean) evt.getNewValue());
                }
            }
        });
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
    
    private String graphScope = "";
    private String nodeSizeInfo = "";
    private String nodeColorInfo = "";
    private int numberOfNodes;
    
    /**
     * Describe which nodes the graph is showing.
     * @param status 
     */
    public void setGraphScope(String scope, int numNodes) {
        graphScope = scope;
        numberOfNodes = numNodes;
        displayGraphStatus();
    }
    
    /**
     * Describe what the node sizes mean.
     * @param info 
     */
    public void setGraphNodeSizeInfo(String info) {
        nodeSizeInfo = info;
        displayGraphStatus();
    }
    
    /**
     * Describe what the node colors mean.
     * @param info 
     */
    public void setGraphNodeColorInfo(String info) {
        nodeColorInfo = info;
        displayGraphStatus();
    }
    
    public void setEnabledClusterBtn(boolean b)
    {
        this.clusterLayoutBtn.setEnabled(b);
    }
    
    void displayGraphStatus() {
        StringBuilder status = new StringBuilder("<html><b>Graph: </b>");
        status.append(String.valueOf(numberOfNodes));
        status.append(" people: ");
        status.append(graphScope);
        if (GraphManager.getInstance().singletonNodesRemoved()) {
            status.append(" (singleton nodes removed)");
        }
        
        status.append("&nbsp;&nbsp;&nbsp;&nbsp;");
        
        if (nodeSizeInfo.equals(nodeColorInfo) && !nodeSizeInfo.isEmpty()) {
            status.append("<b>Node Size and Color:</b>&nbsp;&nbsp;");
            status.append(nodeSizeInfo);
        } else {
            if (!nodeSizeInfo.isEmpty()) {
                status.append("<b>Node Size:</b>  ");
                status.append(nodeSizeInfo);
            }
            status.append("&nbsp;&nbsp;&nbsp;&nbsp;");
            if (!nodeColorInfo.isEmpty()) {
                status.append("<b>Node Color:</b>  ");
                status.append(nodeColorInfo);
            }
        }

        status.append("</html>");
        
        jLabelGraphStatus.setText(status.toString());
    }

    @Override
    public void aboutToPerformGraphOperation() {
        UIUtils.setEnabledForAllLeafComponents(this, false);
    }

    @Override
    public void graphOperationFinished() {
        UIUtils.setEnabledForAllLeafComponents(this, true);
    }

    @Override
    public void aboutToExecuteQuery() {
        UIUtils.setEnabledForAllLeafComponents(this, false);
    }

    @Override
    public void queryFinished(List<Document> documents, List<LocationValue> locationsInFacetTree, List<PersonValue> peopleInFacetTree) {
        UIUtils.setEnabledForAllLeafComponents(this, true);
        //A search changes the graph therefore the cluster graph option shouldn't be available until clustering is 
        //run again.
        this.clusterLayoutBtn.setEnabled(false);
    }
}
