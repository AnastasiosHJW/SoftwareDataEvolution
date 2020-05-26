package gui.listeners.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import data.dataKeeper.ClusterManager;
import data.dataKeeper.GlobalManager;
import data.dataKeeper.ProjectManager;
import data.dataKeeper.TableData;
import gui.dialogs.ParametersJDialog;
import gui.mainEngine.TableUpdater;
import gui.mainEngine.TreeManager;
import gui.tableElements.tableConstructors.TableConstructionPhases;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;

public class ShowPhasesPLDActionListener implements ActionListener {
	
	private GlobalManager globalManager;
	private TableData tableData;
	private TableUpdater tableUpdater;
	private JTabbedPane tabbedPane;
	private TreeManager treeManager;
	
	

	public ShowPhasesPLDActionListener(GlobalManager globalManager, TableData tableData, TableUpdater tableUpdater,
			JTabbedPane tabbedPane, TreeManager treeManager) {
		super();
		this.globalManager = globalManager;
		this.tableData = tableData;
		this.tableUpdater = tableUpdater;
		this.tabbedPane = tabbedPane;
		this.treeManager = treeManager;
	}



	@Override
	public void actionPerformed(ActionEvent arg0) {
		ProjectManager projectManager = globalManager.getProjectManager();
		if(!(projectManager.getProject()==null)){
			tableData.setWholeCol(-1);
			ParametersJDialog jD=new ParametersJDialog(false);
			
			jD.setModal(true);
			
			
			jD.setVisible(true);
			
			ClusterManager clusterManager = globalManager.getClusterManager();
			
			if(jD.getConfirmation()){
			
	            clusterManager.setTimeWeight(jD.getTimeWeight());
	            clusterManager.setChangeWeight(jD.getChangeWeight());
	            clusterManager.setPreProcessingTime(jD.getPreProcessingTime());
	            clusterManager.setPreProcessingChange(jD.getPreProcessingChange());
	            clusterManager.setNumberOfPhases(jD.getNumberOfPhases());
	            
	            System.out.println(clusterManager.getTimeWeight()+" "+clusterManager.getChangeWeight());
	            
	            clusterManager.makePhases(projectManager.getInputCsv(),projectManager.getOutputAssessment1(),projectManager.getOutputAssessment2(), globalManager.getTableManager().getAllPPLTransitions());
				//PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(projectManager.getInputCsv(),projectManager.getOutputAssessment1(),projectManager.getOutputAssessment2(),clusterManager);

				//mainEngine.parseInput();		
				//System.out.println("\n\n\n");
				//mainEngine.extractPhases(clusterManager.getNumberOfPhases());
				/*try {
					mainEngine.extractReportAssessment1();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					mainEngine.extractReportAssessment2();
				} catch (IOException e) {
					e.printStackTrace();
				}
				*/
				//mainEngine.connectTransitionsWithPhases(globalManager.getTableManager().getAllPPLTransitions());
				//clusterManager.setPhaseCollectors(mainEngine.getPhaseCollectors());
				
				
				if(clusterManager.getPhaseCollectors().size()!=0){
					TableConstructionPhases table=new TableConstructionPhases(globalManager);
					final String[] columns=table.constructColumns();
					final String[][] rows=table.constructRows();
					tableData.setSegmentSize(table.getSegmentSize());
					System.out.println("Schemas: "+globalManager.getTableManager().getAllPPLSchemas().size());
					System.out.println("C: "+columns.length+" R: "+rows.length);

					tableData.setFinalColumns(columns);
					tableData.setFinalRows(rows);
					tabbedPane.setSelectedIndex(0);
					tableUpdater.makeGeneralTablePhases(tableData);
					treeManager.fillPhasesTree(globalManager);
					saveTableDataString();
				}
				else{
					JOptionPane.showMessageDialog(null, "Extract Phases first");
				}
			}
		}
		else{
			
			JOptionPane.showMessageDialog(null, "Please select a project first!");
			
		}
		
		
	}
	
	
	public String getTableDataString()
	{
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Final columns: \n");
		for (int i=0;i<tableData.getFinalColumns().length;i++)
		{
			sb.append(tableData.getFinalColumns()[i]);
			sb.append("\n");
		}
		
		sb.append("Final rows: \n");
		for (int i=0;i<tableData.getFinalRows().length;i++)
		{
			for (int j=0;j<tableData.getFinalRows()[i].length;j++)
			{
				sb.append(tableData.getFinalRows()[i][j]);
				sb.append("\n");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}

	public void saveTableDataString()
	{
		String basepath = "C:\\Users\\Anastasios\\eclipse-workspace\\PlutarchParallelLives3";
		String testFile = basepath + "AtlasPhases";
		
		String testData = getTableDataString();
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(testFile));
			writer.write(testData);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
