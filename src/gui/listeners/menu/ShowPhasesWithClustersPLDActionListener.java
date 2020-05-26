package gui.listeners.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import data.dataKeeper.ClusterManager;
import data.dataKeeper.GlobalManager;
import data.dataKeeper.ProjectManager;
import data.dataKeeper.TableData;
import gui.dialogs.ParametersJDialog;
import gui.mainEngine.TableUpdater;
import gui.mainEngine.TreeManager;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class ShowPhasesWithClustersPLDActionListener implements ActionListener {
	private GlobalManager globalManager;
	private TableData tableData;
	private TableUpdater tableUpdater;
	private TreeManager treeManager;
	private JTabbedPane tabbedPane;
	
	
	
	public ShowPhasesWithClustersPLDActionListener(GlobalManager globalManager, TableData tableData,
			TableUpdater tableUpdater, TreeManager treeManager, JTabbedPane tabbedPane) {
		super();
		this.globalManager = globalManager;
		this.tableData = tableData;
		this.tableUpdater = tableUpdater;
		this.treeManager = treeManager;
		this.tabbedPane = tabbedPane;
	}



	@Override
	public void actionPerformed(ActionEvent arg0) {
		tableData.setWholeCol(-1);
		ProjectManager projectManager = globalManager.getProjectManager();
		ClusterManager clusterManager = globalManager.getClusterManager();
		if(!(projectManager.getProject()==null)){
			
			ParametersJDialog jD=new ParametersJDialog(true);
			
			jD.setModal(true);
			
			jD.setVisible(true);
			
			if(jD.getConfirmation()){
			
	            clusterManager.setTimeWeight(jD.getTimeWeight());
	            clusterManager.setChangeWeight(jD.getChangeWeight());
	            clusterManager.setPreProcessingTime(jD.getPreProcessingTime());
	            clusterManager.setPreProcessingChange(jD.getPreProcessingChange());
	            clusterManager.setNumberOfPhases(jD.getNumberOfPhases());
	            clusterManager.setNumberOfClusters(jD.getNumberOfClusters());
	            clusterManager.setBirthWeight(jD.geBirthWeight());
	            clusterManager.setDeathWeight(jD.getDeathWeight());
	            clusterManager.setChangeWeightCl(jD.getChangeWeightCluster());
	            
	            System.out.println(clusterManager.getTimeWeight()+" "+clusterManager.getChangeWeight());
	            
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
				}*/
				
				//mainEngine.connectTransitionsWithPhases(globalManager.getTableManager().getAllPPLTransitions());
				//clusterManager.setPhaseCollectors(mainEngine.getPhaseCollectors());
				clusterManager.makePhases(projectManager.getInputCsv(),projectManager.getOutputAssessment1(),projectManager.getOutputAssessment2(),globalManager.getTableManager().getAllPPLTransitions());
	            
	            //TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(globalManager.getTableManager().getAllPPLSchemas(), globalManager.getTableManager().getAllPPLTables(),clusterManager.getBirthWeight(),clusterManager.getDeathWeight(),clusterManager.getChangeWeightCl());
				//mainEngine2.extractClusters(clusterManager.getNumberOfClusters());
				//clusterManager.setClusterCollectors(mainEngine2.getClusterCollectors());
				//mainEngine2.print();
				clusterManager.makeClusters(globalManager.getTableManager().getAllPPLSchemas(), globalManager.getTableManager().getAllPPLTables(), clusterManager.getChangeWeightCl());
				
				if(clusterManager.getPhaseCollectors().size()!=0){
					TableConstructionWithClusters table=new TableConstructionWithClusters(globalManager.getClusterManager(), globalManager.getTableManager());
					final String[] columns=table.constructColumns();
					final String[][] rows=table.constructRows();
					tableData.setSegmentSize(table.getSegmentSize());
					System.out.println("Schemas: "+globalManager.getTableManager().getAllPPLSchemas().size());
					System.out.println("C: "+columns.length+" R: "+rows.length);

					tableData.setFinalColumns(columns);
					tableData.setFinalRows(rows);
					tabbedPane.setSelectedIndex(0);
					tableUpdater.makeGeneralTablePhases(tableData);
					treeManager.fillClustersTree(clusterManager);
					
					/*
					ClusterValidatorMainEngine lala;
					try {
						lala = new ClusterValidatorMainEngine(globalDataKeeper);
						lala.run();

					} catch (IOException e) {
						e.printStackTrace();
					}
					*/
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

}
