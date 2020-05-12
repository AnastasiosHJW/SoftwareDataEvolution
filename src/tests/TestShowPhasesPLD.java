package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.After;
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
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;

public class TestShowPhasesPLD {

	private GlobalManager globalManager;
	private static String[] projectName;
	private static String[] fileName;
	private static String testFilename;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		projectName = new String[8];

		//String sourcePath = "C:\\Users\\Anastasios\\eclipse-workspace\\PlutarchParallelLives3\\filesHandler\\inis\\";
		String sourcePath = ".\\filesHandler\\inis\\";
		//String testPath = "C:\\Users\\Anastasios\\eclipse-workspace\\PlutarchParallelLives3\\TestData\\";
		String testPath = ".\\TestData\\";

		testFilename = testPath+ "ShowPhasesPLD_";
		
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

		for (int i=1;i<8;i++)
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
	
			ProjectManager projectManager = globalManager.getProjectManager();
			if(!(projectManager.getProject()==null)){
				tableData.setWholeCol(-1);
				
				ClusterManager clusterManager = globalManager.getClusterManager();
				
					//default settings
		            clusterManager.setTimeWeight((float) 0.5);
		            clusterManager.setChangeWeight((float) 0.5);
		            clusterManager.setPreProcessingTime(false);
		            clusterManager.setPreProcessingChange(false);
		            clusterManager.setNumberOfPhases(56);
		            
		            System.out.println(clusterManager.getTimeWeight()+" "+clusterManager.getChangeWeight());
		            
					PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(projectManager.getInputCsv(),projectManager.getOutputAssessment1(),projectManager.getOutputAssessment2(),
							clusterManager.getTimeWeight(),clusterManager.getChangeWeight(),clusterManager.getPreProcessingTime(),clusterManager.getPreProcessingChange());

					mainEngine.parseInput();		
					System.out.println("\n\n\n");
					mainEngine.extractPhases(clusterManager.getNumberOfPhases());
					mainEngine.connectTransitionsWithPhases(globalManager.getTableManager().getAllPPLTransitions());
					clusterManager.setPhaseCollectors(mainEngine.getPhaseCollectors());
					
					
					if(clusterManager.getPhaseCollectors().size()!=0){
						TableConstructionPhases table=new TableConstructionPhases(globalManager);
						final String[] columns=table.constructColumns();
						final String[][] rows=table.constructRows();
						tableData.setSegmentSize(table.getSegmentSize());
						//System.out.println("Schemas: "+globalManager.getTableManager().getAllPPLSchemas().size());
						//System.out.println("C: "+columns.length+" R: "+rows.length);

						tableData.setFinalColumns(columns);
						tableData.setFinalRows(rows);
						//tabbedPane.setSelectedIndex(0);
						//tableUpdater.makeGeneralTablePhases(tableData);
						//treeManager.fillPhasesTree(globalManager);
					}
					else{
						//JOptionPane.showMessageDialog(null, "Extract Phases first");
					}
				}
			
				String testFile = testFilename+projectName[i];
				System.out.println(testFile);
				String baselineDataString = readFileString(testFile);
		
				assertEquals(tableData.getTableDataString(), baselineDataString);
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
