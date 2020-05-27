package data.dataKeeper;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;
import data.dataProccessing.Worker;
import gui.mainEngine.TableUpdater;
import gui.mainEngine.TreeManager;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;

public class TableManager {
	private TreeMap<String,PPLSchema> allPPLSchemas = null;
	private TreeMap<String,PPLTable> allPPLTables = null;
	private ArrayList<AtomicChange> atomicChanges = null;
	private TreeMap<String,TableChange> tableChanges = null;
	private TreeMap<String,TableChange> tableChangesForTwo = null;
	private TreeMap<Integer,PPLTransition> allPPLTransitions = null;
	private String 	projectDataFolder=null;
	
	
	public TableManager()
	{
		allPPLSchemas = new TreeMap<String,PPLSchema>();
		allPPLTables = new  TreeMap<String,PPLTable>();
		atomicChanges = new ArrayList<AtomicChange>();
		tableChanges = new TreeMap<String,TableChange>();
		tableChangesForTwo = new TreeMap<String,TableChange>();
		allPPLTransitions = new TreeMap<Integer,PPLTransition>();
	}
	

	public void setData(String filename, String transitionsFile){
		System.out.println("TableManager: " + filename);
		
		Worker w = new Worker(filename,transitionsFile);
		try {
			w.work();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setAllPPLSchemas(w.getAllPPLSchemas());
		setAllPPLTables(w.getAllPPLTables());
		setAllPPLTransitions(w.getAllPPLTransitions());
		setTableChanges(w.getAllTableChanges());
		setAtomicChanges(w.getAtomicChanges());
		setProjectDataFolder(w.getDataFolder());
		

		
	}


	public TreeMap<String, PPLSchema> getAllPPLSchemas() {
		return allPPLSchemas;
	}


	public void setAllPPLSchemas(TreeMap<String, PPLSchema> allPPLSchemas) {
		this.allPPLSchemas = allPPLSchemas;
	}


	public TreeMap<String, PPLTable> getAllPPLTables() {
		return allPPLTables;
	}


	public void setAllPPLTables(TreeMap<String, PPLTable> allTables) {
		this.allPPLTables = allTables;
	}


	public ArrayList<AtomicChange> getAtomicChanges() {
		return atomicChanges;
	}


	public void setAtomicChanges(ArrayList<AtomicChange> atomicChanges) {
		this.atomicChanges = atomicChanges;
	}


	public TreeMap<String, TableChange> getTableChanges() {
		return tableChanges;
	}


	public void setTableChanges(TreeMap<String, TableChange> tableChanges) {
		this.tableChanges = tableChanges;
	}


	public TreeMap<String, TableChange> getTableChangesForTwo() {
		return tableChangesForTwo;
	}


	public void setTableChangesForTwo(TreeMap<String, TableChange> tableChangesForTwo) {
		this.tableChangesForTwo = tableChangesForTwo;
	}


	public TreeMap<Integer, PPLTransition> getAllPPLTransitions() {
		return allPPLTransitions;
	}


	public void setAllPPLTransitions(TreeMap<Integer, PPLTransition> allPPLTransitions) {
		this.allPPLTransitions = allPPLTransitions;
	}


	public String getProjectDataFolder() {
		return projectDataFolder;
	}


	public void setProjectDataFolder(String projectDataFolder) {
		this.projectDataFolder = projectDataFolder;
	}
	
	public void fillTable(ClusterManager clusterManager, TreeManager treeManager, TableData tableData, JTabbedPane tabbedPane, TableUpdater aux, String inputCsv, String outputAssessment1, String outputAssessment2, String projectName) {
		TableConstructionIDU table=new TableConstructionIDU(allPPLSchemas, allPPLTransitions);
		final String[] columns=table.constructColumns();
		final String[][] rows=table.constructRows();
		
		tableData.setSegmentSizeZoomArea(table.getSegmentSize());

		tableData.setFinalColumnsZoomArea(columns);
		tableData.setFinalRowsZoomArea(rows);

		if (aux != null && tabbedPane != null)
		{
			tabbedPane.setSelectedIndex(0);
			aux.makeGeneralTableIDU(tableData);
		}
		
		
		clusterManager.setTimeWeight((float)0.5);
        clusterManager.setChangeWeight((float)0.5);
        clusterManager.setPreProcessingTime(false);
        clusterManager.setPreProcessingChange(false);
        if(getAllPPLTransitions().size()<56){
        	clusterManager.setNumberOfPhases(40);
        }
        else{
        	clusterManager.setNumberOfPhases(56);
        }
        clusterManager.setNumberOfClusters(14);
        
        System.out.println(clusterManager.getTimeWeight()+" "+clusterManager.getChangeWeight());
        
        clusterManager.setBirthWeight(0.3);
		clusterManager.setDeathWeight(0.3);
		clusterManager.setChangeWeight((float) 0.3);
			
		clusterManager.makePhases(inputCsv,outputAssessment1,outputAssessment2, allPPLTransitions);

		clusterManager.makeClusters(allPPLSchemas, allPPLTables, 0.3);
		
		if(clusterManager.getPhaseCollectors().size()!=0){
			TableConstructionWithClusters tableP=new TableConstructionWithClusters(clusterManager, this);
			final String[] columnsP=tableP.constructColumns();
			final String[][] rowsP=tableP.constructRows();
			
			
			tableData.setSegmentSize(tableP.getSegmentSize());
			tableData.setFinalColumns(columnsP);
			tableData.setFinalRows(rowsP);
			
			if (aux != null && treeManager != null && tabbedPane != null) {
				tabbedPane.setSelectedIndex(0);
				aux.makeGeneralTablePhases(tableData);
				treeManager.fillClustersTree(clusterManager);
			}
			
		}
		System.out.println("Schemas:"+getAllPPLSchemas().size());
		System.out.println("Transitions:"+getAllPPLTransitions().size());
		System.out.println("Tables:"+getAllPPLTables().size());

	}
	

}
