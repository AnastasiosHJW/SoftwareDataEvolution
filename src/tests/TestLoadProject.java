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
import gui.mainEngine.*;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;

public class TestLoadProject {

	private Gui gui;
	private static String[] projectName;
	private static String[] fileName;
	private static String testFilename;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		projectName = new String[8];
		
		String sourcePath = ".\\filesHandler\\inis\\";
		String testPath = ".\\TestData\\";
		testFilename = testPath+ "LoadProject_";
		
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
		gui = new Gui();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test(){
		
		
		for (int i=0;i<8;i++)
		{
			GlobalManager globalManager = gui.getGlobalManager();
			TreeManager treeManager = gui.getTreeManager();
			TableUpdater tableUpdater = gui.getTableUpdater();
			TableData tableData = gui.getTableData();
			JTabbedPane tab = gui.getTabbedPane();
			
			
			try {
				globalManager.importData(fileName[i], treeManager, tableData ,tableUpdater, tab);
				
			} catch (RecognitionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
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
