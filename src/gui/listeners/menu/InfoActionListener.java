package gui.listeners.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import data.dataKeeper.GlobalManager;
import gui.dialogs.ProjectInfoDialog;

public class InfoActionListener implements ActionListener {

	private GlobalManager globalManager;
	
	public InfoActionListener(GlobalManager globalManager)
	{
		this.globalManager = globalManager;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if(!(globalManager.getProjectManager().getCurrentProject()==null)){
			
			
			System.out.println("Project Name:"+globalManager.getProjectManager().getProjectName());
			System.out.println("Dataset txt:"+globalManager.getProjectManager().getDatasetTxt());
			System.out.println("Input Csv:"+globalManager.getProjectManager().getInputCsv());
			System.out.println("Output Assessment1:"+globalManager.getProjectManager().getOutputAssessment1());
			System.out.println("Output Assessment2:"+globalManager.getProjectManager().getOutputAssessment2());
			System.out.println("Transitions File:"+globalManager.getProjectManager().getTransitionsFile());
			
			System.out.println("Schemas:"+globalManager.getTableManager().getAllPPLSchemas().size());
			System.out.println("Transitions:"+globalManager.getTableManager().getAllPPLTransitions().size());
			System.out.println("Tables:"+globalManager.getTableManager().getAllPPLTables().size());
			
			
			ProjectInfoDialog infoDialog = new ProjectInfoDialog(globalManager.getProjectManager().getProjectName(),globalManager.getProjectManager().getDatasetTxt(),globalManager.getProjectManager().getInputCsv(),
					globalManager.getProjectManager().getTransitionsFile(),globalManager.getTableManager().getAllPPLSchemas().size(),
					globalManager.getTableManager().getAllPPLTransitions().size(), globalManager.getTableManager().getAllPPLTables().size());
			
			infoDialog.setVisible(true);
		}
		else{
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
		
		
		
		
	}

}
