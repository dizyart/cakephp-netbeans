/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2012 Sun Microsystems, Inc.
 */
package org.cakephp.netbeans.options;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;

final class CakePhpPanel extends javax.swing.JPanel {
        private static final long serialVersionUID = 1542234585504356049L;

        private final CakePhpOptionsPanelController controller;
        private CakePhpOptionsPanelRegisterDialog dialog;
        private CakePhpPluginTableModel model = new CakePhpPluginTableModel();

        CakePhpPanel(CakePhpOptionsPanelController controller) {
                this.controller = controller;
                initComponents();
                initialize();
                // TODO listen to changes in form fields and call controller.changed()
        }

        public void initialize(){
                pluginTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                pluginTable.setSize(new Dimension(600, 600));
                TableColumnModel columnModel = pluginTable.getColumnModel();
                TableColumn column = columnModel.getColumn(CakePhpPluginTableModel.NAME);
                column.setMinWidth(150);
                column.setPreferredWidth(150);
                column = columnModel.getColumn(CakePhpPluginTableModel.URL);
                column.setMinWidth(450);
                column.setPreferredWidth(450);

        }
        /**
         * This method is called from within the constructor to initialize the
         * form. WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pluginListLabel = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        messageLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pluginTable = new javax.swing.JTable();

        org.openide.awt.Mnemonics.setLocalizedText(pluginListLabel, org.openide.util.NbBundle.getMessage(CakePhpPanel.class, "CakePhpPanel.pluginListLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(addButton, org.openide.util.NbBundle.getMessage(CakePhpPanel.class, "CakePhpPanel.addButton.text")); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(editButton, org.openide.util.NbBundle.getMessage(CakePhpPanel.class, "CakePhpPanel.editButton.text")); // NOI18N
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(deleteButton, org.openide.util.NbBundle.getMessage(CakePhpPanel.class, "CakePhpPanel.deleteButton.text")); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        messageLabel.setForeground(new java.awt.Color(255, 0, 0));
        org.openide.awt.Mnemonics.setLocalizedText(messageLabel, org.openide.util.NbBundle.getMessage(CakePhpPanel.class, "CakePhpPanel.messageLabel.text")); // NOI18N
        messageLabel.setToolTipText(org.openide.util.NbBundle.getMessage(CakePhpPanel.class, "CakePhpPanel.messageLabel.toolTipText")); // NOI18N

        pluginTable.setModel(model);
        pluginTable.setColumnSelectionAllowed(true);
        pluginTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(pluginTable);
        pluginTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pluginListLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(messageLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pluginListLabel)
                    .addComponent(messageLabel))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

        private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
                // Open Dialog
                initDialog();
                dialog.setTitle("Add");
                dialog.setVisible(true);

                // validate
                if(!isRegisterOK()){
                        return;
                }

                // Resiter plugins
                model.addPlugin(new CakePhpPlugin(dialog.getPluginName().trim(), dialog.getUrl().trim()));

                controller.changed();
        }//GEN-LAST:event_addButtonActionPerformed

        private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
                int index = pluginTable.getSelectedRow();
                // is not selected
                if(index == -1){
                        return;
                }
                // Open Dialog
                initDialog();
                CakePhpPlugin selected = model.getPlugins().get(index);
                dialog.setPluginName(selected.getName());
                dialog.setUrl(selected.getUrl());
                dialog.setTitle("Edit");
                dialog.setVisible(true);

                // validate
                if(!isRegisterOK()){
                        return;
                }

                // Resiter plugins
                CakePhpPlugin plugin = new CakePhpPlugin(dialog.getPluginName().trim(), dialog.getUrl().trim());
                model.editPlugin(index, plugin);
                controller.changed();
        }//GEN-LAST:event_editButtonActionPerformed

        private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
                int index = pluginTable.getSelectedRow();
                if(index == -1){
                        return;
                }
                model.removePlugin(index);
                controller.changed();
        }//GEN-LAST:event_deleteButtonActionPerformed

        private void initDialog(){
                Frame f = WindowManager.getDefault().getMainWindow();
                dialog = new CakePhpOptionsPanelRegisterDialog(f, true);
                int x = f.getX() + (f.getWidth() - dialog.getWidth()) / 2;
                int y = f.getY() + (f.getHeight() - dialog.getHeight()) / 2;
                dialog.setLocation(x, y);
        }

        private boolean isRegisterOK(){
                if(dialog != null){
                        if(dialog.getPluginName().isEmpty() || dialog.getUrl().isEmpty()){
                                return false;
                        }
                }
                return true;
        }

        void load() {
                model.setPlugins(CakePhpOptions.getInstance().getPlugins());
        }

        void store() {
                if(controller.isChanged()){
                        CakePhpOptions.getInstance().setPlugins(model.getPlugins());
                }
        }

        boolean valid() {
                return true;
        }

        private class CakePhpPluginTableModel extends AbstractTableModel{
                private static final int NAME = 0;
                private static final int URL = 1;
                private static final long serialVersionUID = 6148058724466511289L;
                private List<CakePhpPlugin> plugins;
                private String[] column;

                public CakePhpPluginTableModel(){
                        column = new String[]{
                                NbBundle.getMessage(CakePhpPanel.class, "CakePhpPanel.pluginTable.columnModel.title0"),
                                NbBundle.getMessage(CakePhpPanel.class, "CakePhpPanel.pluginTable.columnModel.title1")
                        };
                        plugins = new ArrayList<CakePhpPlugin>();
                }

                @Override
                public int getRowCount() {
                        return plugins.size();
                }

                @Override
                public int getColumnCount() {
                        return column.length;
                }

                @Override
                public Object getValueAt(int rowIndex, int columnIndex) {
                        CakePhpPlugin plugin = plugins.get(rowIndex);
                        if(columnIndex == NAME){
                                return plugin.getName();
                        }else if(columnIndex == URL){
                                return plugin.getUrl();
                        }
                        return null;
                }

                @Override
                public  String getColumnName(int columnIndex){
                        return column[columnIndex];
                }

                public void setPlugins(List<CakePhpPlugin> plugins){
                        this.plugins = plugins;
                }

                public List<CakePhpPlugin> getPlugins(){
                        return plugins;
                }

                public void addPlugin(CakePhpPlugin plugin){
                        plugins.add(plugin);
                        int index = plugins.indexOf(plugin);
                        fireTableRowsInserted(index, index);
                }

                public void editPlugin(int index, CakePhpPlugin plugin){
                        plugins.set(index, plugin);
                        fireTableRowsUpdated(index, index);
                }

                public void removePlugin(int index) {
                        plugins.remove(index);
                        fireTableRowsDeleted(index, index);
                }
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JLabel pluginListLabel;
    private javax.swing.JTable pluginTable;
    // End of variables declaration//GEN-END:variables
}
