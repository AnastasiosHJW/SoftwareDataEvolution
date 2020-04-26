package data.dataKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JTabbedPane;

import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;
import data.dataProccessing.Worker;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

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
			// TODO Auto-generated catch block
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
	
	public void fillTable(ClusterManager clusterManager, TableData tableData, JTabbedPane tabbedPane, GuiAuxilliary aux, String inputCsv, String outputAssessment1, String outputAssessment2) {
		TableConstructionIDU table=new TableConstructionIDU(allPPLSchemas, allPPLTransitions);
		final String[] columns=table.constructColumns();
		final String[][] rows=table.constructRows();
		tableData.setSegmentSizeZoomArea(table.getSegmentSize());

		tableData.setFinalColumnsZoomArea(columns);
		tableData.setFinalRowsZoomArea(rows);
		tabbedPane.setSelectedIndex(0);
		aux.makeGeneralTableIDU(tableData);
		
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
        
		PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(inputCsv,outputAssessment1,outputAssessment2, clusterManager);

		Double b=new Double(0.3);
		Double d=new Double(0.3);
		Double c=new Double(0.3);
			
		mainEngine.parseInput();		
		System.out.println("\n\n\n");
		mainEngine.extractPhases(clusterManager.getNumberOfPhases());
		
		mainEngine.connectTransitionsWithPhases(allPPLTransitions);
		clusterManager.setPhaseCollectors(mainEngine.getPhaseCollectors());
		TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(allPPLSchemas, allPPLTables,b,d,c);
		mainEngine2.extractClusters2(clusterManager.getNumberOfClusters());
		clusterManager.setClusterCollectors(mainEngine2.getClusterCollectors());
		mainEngine2.print();
		
		if(clusterManager.getPhaseCollectors().size()!=0){
			TableConstructionWithClusters tableP=new TableConstructionWithClusters(clusterManager, this);
			final String[] columnsP=tableP.constructColumns();
			final String[][] rowsP=tableP.constructRows();
			tableData.setSegmentSize(tableP.getSegmentSize());
			tableData.setFinalColumns(columnsP);
			tableData.setFinalRows(rowsP);
			tabbedPane.setSelectedIndex(0);
			aux.makeGeneralTablePhases(tableData, tabbedPane);
			//fillClustersTree();
		}
		System.out.println("Schemas:"+getAllPPLSchemas().size());
		System.out.println("Transitions:"+getAllPPLTransitions().size());
		System.out.println("Tables:"+getAllPPLTables().size());

	}
	
	
	

}
