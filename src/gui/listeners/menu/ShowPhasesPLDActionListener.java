package gui.listeners.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import data.dataKeeper.ClusterManager;
import data.dataKeeper.GlobalManager;
import data.dataKeeper.TableData;
import gui.dialogs.ParametersJDialog;
import gui.mainEngine.TableUpdater;
import gui.tableElements.tableConstructors.TableConstructionPhases;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;

public class ShowPhasesPLDActionListener implements ActionListener {
	
	private GlobalManager globalManager;
	private TableData tableData;
	private TableUpdater tableUpdater;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(!(project==null)){
			tableData.setWholeCol(-1);
			ParametersJDialog jD=new ParametersJDialog(false);
			
			jD.setModal(true);
			
			
			jD.setVisible(true);
			
			ClusterManager clusterManager = globalManager.getClusterManager()
			
			if(jD.getConfirmation()){
			
	            clusterManager.setTimeWeight(jD.getTimeWeight());
	            clusterManager.setChangeWeight(jD.getChangeWeight());
	            clusterManager.setPreProcessingTime(jD.getPreProcessingTime());
	            clusterManager.setPreProcessingChange(jD.getPreProcessingChange());
	            clusterManager.setNumberOfPhases(jD.getNumberOfPhases());
	            
	            System.out.println(clusterManager.getTimeWeight()+" "+clusterManager.getChangeWeight());
	            
				PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(inputCsv,outputAssessment1,outputAssessment2,timeWeight,changeWeight,preProcessingTime,preProcessingChange);

				mainEngine.parseInput();		
				System.out.println("\n\n\n");
				mainEngine.extractPhases(clusterManager.getNumberOfPhases());
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
				mainEngine.connectTransitionsWithPhases(globalDataKeeper);
				clusterManager.setPhaseCollectors(mainEngine.getPhaseCollectors());
				
				
				if(clusterManager.getPhaseCollectors().size()!=0){
					TableConstructionPhases table=new TableConstructionPhases(globalDataKeeper);
					final String[] columns=table.constructColumns();
					final String[][] rows=table.constructRows();
					tableData.setSegmentSize(table.getSegmentSize());
					System.out.println("Schemas: "+globalManager.getTableManager().getAllPPLSchemas().size());
					System.out.println("C: "+columns.length+" R: "+rows.length);

					tableData.setFinalColumns(columns);
					tableData.setFinalRows(rows);
					tabbedPane.setSelectedIndex(0);
					tableUpdater.makeGeneralTablePhases(tableData);
					fillPhasesTree();
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
