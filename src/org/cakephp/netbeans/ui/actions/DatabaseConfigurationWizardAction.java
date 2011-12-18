package org.cakephp.netbeans.ui.actions;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import javax.swing.JComponent;
import org.cakephp.netbeans.CakeScript;
import org.cakephp.netbeans.ui.wizards.DatabaseConfigurationVisualPanel;
import org.cakephp.netbeans.ui.wizards.DatabaseConfigurationWizardPanel;
import org.cakephp.netbeans.util.CakePhpUtils;
import org.netbeans.modules.php.api.phpmodule.PhpModule;
import org.netbeans.modules.php.api.phpmodule.PhpProgram.InvalidPhpProgramException;
import org.netbeans.modules.php.spi.actions.BaseAction;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

// An example action demonstrating how the wizard could be called from within
// your code. You can move the code below wherever you need, or register an action:
 @ActionID(category="PHP", id="org.cakephp.netbeans.ui.wizards.CakeDBConfigurationWizardAction")
 @ActionRegistration(displayName="DB Configuration")
public final class DatabaseConfigurationWizardAction extends BaseAction implements ActionListener {
	private static final long serialVersionUID = 745967868766354783L;
	private static final String[] DB_KIND_10 = {"mysql", "db2", "firebird", "mssql", "mysqli", "odbc", "oracle", "postgres", "sqlite", "sybase"}; // NOI18N
	private static final String[] DB_KIND_6 = {"mysql", "mssql", "mysqli", "oracle", "postgres", "sqlite"}; // NOI18N
	private static final String[] DB_KIND2_4 = {"Mysql", "Postgres", "Sqlite", "Sqlserver"}; // NOI18N
	private static final DatabaseConfigurationWizardAction INSTANCE = new DatabaseConfigurationWizardAction();
	private WizardDescriptor.Panel[] panels;

	private DatabaseConfigurationWizardAction(){
		
	}
	
	public static DatabaseConfigurationWizardAction getInstance(){
		return INSTANCE;
	}

	@Override
	protected String getFullName() {
	        return NbBundle.getMessage(DatabaseConfigurationWizardAction.class, "LBL_CakePhpAction", getPureName());
	}

	@Override
	protected String getPureName() {
	        return NbBundle.getMessage(DatabaseConfigurationWizardAction.class, "LBL_DatabaseConfiguration");
	}
	
	@Override
	protected void actionPerformed(PhpModule phpModule) {
		WizardDescriptor.Panel<WizardDescriptor>[] wizardPanels = getPanels();
		DatabaseConfigurationVisualPanel visual = null;
		for(WizardDescriptor.Panel panel : wizardPanels){
			if(panel instanceof DatabaseConfigurationWizardPanel){
				 visual = (DatabaseConfigurationVisualPanel)panel.getComponent();
			}
		}
		if (visual instanceof DatabaseConfigurationVisualPanel) {
			String[] version = CakePhpUtils.getCakePhpVersionSplit(phpModule);
			if (Integer.parseInt(version[CakePhpUtils.CAKE_VERSION_MAJOR]) == 2) {
				visual.setDriverList(DB_KIND2_4);
			} else if (Integer.parseInt(version[CakePhpUtils.CAKE_VERSION_MAJOR]) == 1
				&& Integer.parseInt(version[CakePhpUtils.CAKE_VERSION_MINOR]) >= 3
				&& Integer.parseInt(version[CakePhpUtils.CAKE_VERSION_REVISION]) >= 12) {
				visual.setDriverList(DB_KIND_6);
			} else {
				visual.setDriverList(DB_KIND_10);
			}
			visual.getDriverList().setSelectedIndex(0);
		}
		
		WizardDescriptor wizardDescriptor = new WizardDescriptor(wizardPanels);
		// {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
		wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
		wizardDescriptor.putProperty(WizardDescriptor.PROP_IMAGE, ImageUtilities.loadImage("org/cakephp/netbeans/resources/visual_bake.png", true)); // NOI18N
		wizardDescriptor.setTitle("CakePHP Database Configration");
		Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
		dialog.setVisible(true);
		dialog.toFront();
		boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
		if (!cancelled) {
			// Create Input String
			StringBuilder sb = new StringBuilder();
			sb.append(visual.getNameField().getText());
			sb.append("\n");
			sb.append(visual.getDriverList().getSelectedValue());
			sb.append("\n");
			if (visual.getPersistentCheckBox().isSelected()) {
				sb.append("y");
			}
			sb.append("\n");
			sb.append(visual.getHostField().getText());
			sb.append("\n");
			sb.append(visual.getPortField().getText());
			sb.append("\n");
			sb.append(visual.getUserField().getText());
			sb.append("\n");
			if (visual.getPasswordField().getText().equals("")) {
				sb.append("\n");
				sb.append("y");
				sb.append("\n");
			} else {
				sb.append(visual.getPasswordField().getText());
				sb.append("\n");
			}
			sb.append(visual.getDatabaseNameField().getText());
			sb.append("\n");
			sb.append(visual.getPrefixField().getText());
			sb.append("\n");
			sb.append(visual.getEncodingField().getText());
			sb.append("\n");
			sb.append("\n"); // confirm "y"
			sb.append("\n"); // continue "n"
			CakeScript cakeScript;
			try {
				cakeScript = CakeScript.forPhpModule(phpModule);
				cakeScript.initDatabaseConfig(sb.toString());
			} catch (InvalidPhpProgramException ex) {
				Exceptions.printStackTrace(ex);
			}
		}
	}

	/**
	 * Initialize panels representing individual wizard's steps and sets
	 * various properties for them influencing wizard appearance.
	 */
	private WizardDescriptor.Panel[] getPanels() {
		if (panels == null) {
			panels = new WizardDescriptor.Panel[]{
				new DatabaseConfigurationWizardPanel()
			};
			String[] steps = new String[panels.length];
			for (int i = 0; i < panels.length; i++) {
				Component c = panels[i].getComponent();
				// Default step name to component name of panel. Mainly useful
				// for getting the name of the target chooser to appear in the
				// list of steps.
				steps[i] = c.getName();
				if (c instanceof JComponent) { // assume Swing components
					JComponent jc = (JComponent) c;
					// Sets step number of a component
					// TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_*:
					jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
					// Sets steps names for a panel
					jc.putClientProperty("WizardPanel_contentData", steps);
					// Turn on subtitle creation on each step
					jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
					// Show steps on the left side with the image on the background
					jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
					// Turn on numbering of all steps
					jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
				}
			}
		}
		return panels;
	}
}
