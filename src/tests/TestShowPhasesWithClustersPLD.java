package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.dataKeeper.ClusterManager;
import data.dataKeeper.GlobalManager;
import data.dataKeeper.ProjectManager;
import data.dataKeeper.TableData;
import gui.dialogs.ParametersJDialog;
import gui.mainEngine.TableUpdater;
import gui.mainEngine.TreeManager;
import gui.tableElements.tableConstructors.TableConstructionPhases;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class TestShowPhasesWithClustersPLD {
	
	private GlobalManager globalManager;
	private static String[] projectName;
	private static String[] fileName;
	private static String testFilename;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		projectName = new String[8];
		
		String sourcePath = "C:\\Users\\Anastasios\\eclipse-workspace\\PlutarchParallelLives3\\filesHandler\\inis\\";
		String testPath = "C:\\Users\\Anastasios\\eclipse-workspace\\PlutarchParallelLives3\\TestData\\";
		testFilename = testPath+ "ShowPhasesWithClustersPLD_";
		
		projectName[0] = "Atlas";
		projectName[1] = "bioSQL";
		projectName[2] = "Coppermine";
		projectName[3] = "Ensembl";
		projectName[4] = "mediaWiki";
		projectName[5] = "Opencart";
		projectName[6] = "phpBB";
		projectName[7] = "typo3";
		
		fileName = new String[8];
		
		fileName[0] = sourcePath + "Atlas.ini";
		fileName[1] = sourcePath + "biosql.ini";
		fileName[2] = sourcePath + "coppermine.ini";
		fileName[3] = sourcePath + "ensembl.ini";
		fileName[4] = sourcePath + "mwiki.ini";
		fileName[5] = sourcePath + "opencart.ini";
		fileName[6] = sourcePath + "phpBB.ini";
		fileName[7] = sourcePath + "typo3.ini";
		
	}
	
	@Before
	public void setUp() throws Exception
	{
		globalManager = new GlobalManager();
	}

	@Test
	public void test() {

		for (int i=2;i<8;i++)
		{
		
			TableData tableData = new TableData();
		
			try {
				TreeManager treeManager = null;
				TableUpdater aux = null;
				JTabbedPane tab = null;
			
				globalManager.importData(fileName[i], treeManager, tableData ,aux, tab);
			
			} catch (RecognitionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			tableData.setWholeCol(-1);
			ProjectManager projectManager = globalManager.getProjectManager();
			ClusterManager clusterManager = globalManager.getClusterManager();
			if(!(projectManager.getProject()==null)){
				
				//ParametersJDialog jD=new ParametersJDialog(true);
				
				//jD.setModal(true);
				
				//jD.setVisible(true);
				
				if(true)//jD.getConfirmation()){
				
		            clusterManager.setTimeWeight((float) 0.5);
		            clusterManager.setChangeWeight((float) 0.5);
		            clusterManager.setPreProcessingTime(false);
		            clusterManager.setPreProcessingChange(false);
		            clusterManager.setNumberOfPhases(56);
		            clusterManager.setNumberOfClusters(14);
		            clusterManager.setBirthWeight(0.333);
		            clusterManager.setDeathWeight(0.333);
		            clusterManager.setChangeWeightCl(0.333);
		            
		            System.out.println(clusterManager.getTimeWeight()+" "+clusterManager.getChangeWeight());
		            
		            PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(projectManager.getInputCsv(),projectManager.getOutputAssessment1(),projectManager.getOutputAssessment2(),
							clusterManager.getTimeWeight(),clusterManager.getChangeWeight(),clusterManager.getPreProcessingTime(),clusterManager.getPreProcessingChange());
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
					}*/
					
					mainEngine.connectTransitionsWithPhases(globalManager.getTableManager().getAllPPLTransitions());
					clusterManager.setPhaseCollectors(mainEngine.getPhaseCollectors());
					TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(globalManager.getTableManager().getAllPPLSchemas(), globalManager.getTableManager().getAllPPLTables(),clusterManager.getBirthWeight(),clusterManager.getDeathWeight(),clusterManager.getChangeWeightCl());
					mainEngine2.extractClusters2(clusterManager.getNumberOfClusters());
					clusterManager.setClusterCollectors(mainEngine2.getClusterCollectors());
					mainEngine2.print();
					
					if(clusterManager.getPhaseCollectors().size()!=0){
						TableConstructionWithClusters table=new TableConstructionWithClusters(globalManager.getClusterManager(), globalManager.getTableManager());
						final String[] columns=table.constructColumns();
						final String[][] rows=table.constructRows();
						tableData.setSegmentSize(table.getSegmentSize());
						System.out.println("Schemas: "+globalManager.getTableManager().getAllPPLSchemas().size());
						System.out.println("C: "+columns.length+" R: "+rows.length);

						tableData.setFinalColumns(columns);
						tableData.setFinalRows(rows);
						//tabbedPane.setSelectedIndex(0);
						//tableUpdater.makeGeneralTablePhases(tableData);
						//treeManager.fillClustersTree(clusterManager);

					}
					else{
						//JOptionPane.showMessageDialog(null, "Extract Phases first");
					}
				}
			
			//else{
				
				//JOptionPane.showMessageDialog(null, "Please select a project first!");
				
			//}
			
				String testFile = testFilename+projectName[i];
				System.out.println(testFile);
				String baselineDataString = readFileString(testFile);
		
				assertEquals(baselineDataString, tableData.getTableDataString());
			}
	
			
	}
	

	private String readFileString(String file)

	{
		StringBuilder sBuilder = new StringBuilder();
	
		try (BufferedReader br = new BufferedReader(new FileReader(file))) 
		{
 
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) 
			{
				sBuilder.append(sCurrentLine).append("\n");
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return sBuilder.toString();
    
	}


}
