package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JTabbedPane;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.dataKeeper.GlobalManager;
import data.dataKeeper.TableData;
import gui.mainEngine.TableUpdater;
import gui.mainEngine.TreeManager;
import gui.tableElements.tableConstructors.TableConstructionIDU;

public class TestShowPLD {

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
		testFilename = testPath+ "ShowPLD_";
		
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

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void test() {

		for (int i=0;i<8;i++)
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
		
			TableConstructionIDU table=new TableConstructionIDU(globalManager.getTableManager().getAllPPLSchemas(), globalManager.getTableManager().getAllPPLTransitions());
			final String[] columns=table.constructColumns();
			final String[][] rows=table.constructRows();
			tableData.setSegmentSizeZoomArea(table.getSegmentSize());

			tableData.setFinalColumnsZoomArea(columns);
			tableData.setFinalRowsZoomArea(rows);
			//tableUpdater.makeGeneralTableIDU(tableData);
			//treeManager.fillTree(globalManager.getTableManager());
		
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
