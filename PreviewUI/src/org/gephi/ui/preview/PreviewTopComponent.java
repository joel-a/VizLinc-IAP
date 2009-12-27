package org.gephi.ui.preview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import org.gephi.preview.api.GraphSheet;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.Utilities;

/**
 * Top component which displays the preview applet.
 */
final class PreviewTopComponent extends TopComponent {

    private static PreviewTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "PreviewTopComponent";
    private final ProcessingPreview sketch = new ProcessingPreview();

    private PreviewTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(PreviewTopComponent.class, "CTL_PreviewTopComponent"));
        setToolTipText(NbBundle.getMessage(PreviewTopComponent.class, "HINT_PreviewTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));

        bannerPanel.setVisible(false);

        // inits the preview applet
        previewPanel.add(sketch, BorderLayout.CENTER);
        sketch.init();

        // forces the controller instanciation
        PreviewUIController.findInstance();
    }

    /**
     * Shows the banner panel.
     *
     * @see PreviewUIController#showRefreshNotification()
     */
    public void showBannerPanel() {
        bannerPanel.setVisible(true);
    }

    /**
     * Hides the banner panel.
     *
     * @see PreviewUIController#hideRefreshNotification()
     */
    public void hideBannerPanel() {
        bannerPanel.setVisible(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        bannerPanel = new javax.swing.JPanel();
        bannerLabel = new javax.swing.JLabel();
        refreshButton = new javax.swing.JButton();
        previewPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        bannerPanel.setBackground(new java.awt.Color(178, 223, 240));
        bannerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        bannerPanel.setLayout(new java.awt.GridBagLayout());

        bannerLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gephi/ui/preview/resources/info.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(bannerLabel, org.openide.util.NbBundle.getMessage(PreviewTopComponent.class, "PreviewTopComponent.bannerLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        bannerPanel.add(bannerLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(refreshButton, org.openide.util.NbBundle.getMessage(PreviewTopComponent.class, "PreviewTopComponent.refreshButton.text")); // NOI18N
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        bannerPanel.add(refreshButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(bannerPanel, gridBagConstraints);

        previewPanel.setBackground(new java.awt.Color(255, 255, 255));
        previewPanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 535;
        gridBagConstraints.ipady = 304;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(previewPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        PreviewUIController.findInstance().refreshPreview();
    }//GEN-LAST:event_refreshButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bannerLabel;
    private javax.swing.JPanel bannerPanel;
    private javax.swing.JPanel previewPanel;
    private javax.swing.JButton refreshButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized PreviewTopComponent getDefault() {
        if (instance == null) {
            instance = new PreviewTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the PreviewTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized PreviewTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(PreviewTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof PreviewTopComponent) {
            return (PreviewTopComponent) win;
        }
        Logger.getLogger(PreviewTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return PreviewTopComponent.getDefault();
        }
    }

    /**
     * Refresh the preview applet.
     */
    public void refreshPreview() {
        sketch.refresh();
    }

    /**
     * Defines the preview graph to draw in the applet.
     *
     * @param graph  the preview graph to draw in the applet
     */
    public void setGraph(GraphSheet graphSheet) {
        sketch.setGraphSheet(graphSheet);
    }
}
