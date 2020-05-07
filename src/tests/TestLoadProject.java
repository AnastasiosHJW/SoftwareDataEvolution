package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.dataKeeper.*;
import gui.mainEngine.TableUpdater;
import gui.mainEngine.TreeManager;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;

public class TestLoadProject {

	
	private ProjectManager projectManager;
	private static String[] projectName;
	private static String[] fileName;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		projectName = new String[8];
		
		String path = "C:\\Users\\Anastasios\\eclipse-workspace\\PlutarchParallelLives3\\filesHandler\\inis\\";
		
		projectName[0] = "Atlas";
		projectName[1] = "bioSQL";
		projectName[2] = "Coppermine";
		projectName[3] = "Ensembl";
		projectName[4] = "mediaWiki";
		projectName[5] = "Opencart";
		projectName[6] = "phpBB";
		projectName[7] = "typo3";
		
		fileName = new String[8];
		
		fileName[0] = path + "Atlas.ini";
		fileName[1] = path + "biosql.ini";
		fileName[2] = path + "coppermine.ini";
		fileName[3] = path + "ensembl.ini";
		fileName[4] = path + "mwiki.ini";
		fileName[5] = path + "opencart.ini";
		fileName[6] = path + "phpBB.ini";
		fileName[7] = path + "typo3.ini";
		
	}
	
	@Before
	public void setUp() throws Exception
	{
		projectManager = new ProjectManager();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test(){
		
		
		for (int i=0;i<8;i++)
		{
			String baseSimpleFile = "baseline_simple_" + projectName[i];
			String testSimpleFile = "test_simple_" + projectName[i];
			String basePhasesFile = "baseline_phases_" + projectName[i];
			String testPhasesFile = "test_phases_" + projectName[i];
			
			
			
			try {
				TreeManager treeManager = null;
				TableUpdater aux = null;
				JTabbedPane tab = null;
				projectManager.importData(fileName[i], new TableManager(), new ClusterManager(), treeManager, new TableData() ,aux, tab);
				//projectManager.importDataTest(fileName[i], new TableManager(), new ClusterManager(), new TableData());
			} catch (RecognitionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			String baseSimpleString = readFileString(baseSimpleFile);
			String testSimpleString = readFileString(testSimpleFile);
			String basePhasesString = readFileString(basePhasesFile);
			String testPhasesString = readFileString(testPhasesFile);
			
			assertEquals(baseSimpleString, testSimpleString);
			assertEquals(basePhasesString, testPhasesString);
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
