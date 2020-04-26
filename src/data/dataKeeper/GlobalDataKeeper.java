package data.dataKeeper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JTextArea;

import org.antlr.v4.runtime.RecognitionException;

import phaseAnalyzer.commons.PhaseCollector;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.commons.ClusterCollector;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;
import data.dataProccessing.Worker;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;

public class GlobalDataKeeper {

	private TreeMap<String,PPLSchema> allPPLSchemas = null;
	private TreeMap<String,PPLTable> allTables = null;
	private ArrayList<AtomicChange> atomicChanges = null;
	private TreeMap<String,TableChange> tableChanges = null;
	private TreeMap<String,TableChange> tableChangesForTwo = null;
	private TreeMap<Integer,PPLTransition> allPPLTransitions = null;
	private ArrayList<PhaseCollector> phaseCollectors = null;
	private ArrayList<ClusterCollector> clusterCollectors = null;

	private String 	projectDataFolder=null;
	private String filename=null;
	private String transitionsFile="";
	

	public GlobalDataKeeper(String fl,String transitionsFile){
		allPPLSchemas = new TreeMap<String,PPLSchema>();
		allTables = new  TreeMap<String,PPLTable>();
		atomicChanges = new ArrayList<AtomicChange>();
		tableChanges = new TreeMap<String,TableChange>();
		tableChangesForTwo = new TreeMap<String,TableChange>();
		allPPLTransitions = new TreeMap<Integer,PPLTransition>();
		phaseCollectors = new ArrayList<PhaseCollector>();
		clusterCollectors = new ArrayList<ClusterCollector>();
		filename=fl;
		this.transitionsFile=transitionsFile;
	}
	
	private void init(String fl,String transitionsFile){
		allPPLSchemas = new TreeMap<String,PPLSchema>();
		allTables = new  TreeMap<String,PPLTable>();
		atomicChanges = new ArrayList<AtomicChange>();
		tableChanges = new TreeMap<String,TableChange>();
		tableChangesForTwo = new TreeMap<String,TableChange>();
		allPPLTransitions = new TreeMap<Integer,PPLTransition>();
		phaseCollectors = new ArrayList<PhaseCollector>();
		clusterCollectors = new ArrayList<ClusterCollector>();
		filename=fl;
		this.transitionsFile=transitionsFile;
	}
	
	public GlobalDataKeeper(){
		
		
	}
	
	public void setData(){
		System.out.println("GlobalDataKeeper: " + filename);
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
		setAllTableChanges(w.getAllTableChanges());
		setAtomicChanges(w.getAtomicChanges());
		setDataFolder(w.getDataFolder());
		

		
	}
	
	public void setPhaseCollectors(ArrayList<PhaseCollector> phaseCollectors){
		this.phaseCollectors=phaseCollectors;
	}
	
	public void setClusterCollectors(ArrayList<ClusterCollector> clusterCollectors){
		this.clusterCollectors=clusterCollectors;
	}
	
	private void setAllPPLSchemas(TreeMap<String,PPLSchema> tmpAllPPLSchemas){
		
		allPPLSchemas=tmpAllPPLSchemas;
		
	}
	
	private void setAllPPLTables(TreeMap<String,PPLTable> tmpAllTables){
		 allTables=tmpAllTables;

		
	}
	
	private void setAtomicChanges(ArrayList<AtomicChange> tmpAtomicChanges){
		
		 atomicChanges=tmpAtomicChanges;
		
	}
	
	private void setAllTableChanges(TreeMap<String,TableChange> tmpTableChanges){
		
		 tableChanges=tmpTableChanges;
		
	}
	
	
	private void setAllPPLTransitions(TreeMap<Integer,PPLTransition> tmpAllPPLTransitions){
		
		 allPPLTransitions=tmpAllPPLTransitions;
		
	}
	
	private void setDataFolder(String tmpProjectDataFolder){
		 projectDataFolder=tmpProjectDataFolder;
	}
	
	public TreeMap<String,PPLSchema> getAllPPLSchemas(){
		
		return allPPLSchemas;
		
	}
	
	public TreeMap<String,PPLTable> getAllPPLTables(){
		
		return allTables;
		
	}
	
	public ArrayList<AtomicChange> getAtomicChanges(){
		
		return atomicChanges;
		
	}
	
	public TreeMap<String,TableChange> getAllTableChanges(){
		
		return tableChanges;
		
	}
	
	public TreeMap<String,TableChange> getTmpTableChanges(){
		
		return tableChangesForTwo;
		
	}
	
	public TreeMap<Integer,PPLTransition> getAllPPLTransitions(){
		
		return allPPLTransitions;
		
	}
	
	public String getDataFolder(){
		return projectDataFolder;
	}
	
	public ArrayList<PhaseCollector> getPhaseCollectors(){
		return this.phaseCollectors;
	}
	
	public ArrayList<ClusterCollector> getClusterCollectors(){
		return this.clusterCollectors;
	}
	

	
	/*
	
	public void fillTable(GeneralTableIDUData IDUdata, TableData tableData, JTextArea descriptionText) {
		TableConstructionIDU table=new TableConstructionIDU(this);
		final String[] columns=table.constructColumns();
		final String[][] rows=table.constructRows();
		segmentSizeZoomArea = table.getSegmentSize();

		finalColumnsZoomArea=columns;
		finalRowsZoomArea=rows;
		tabbedPane.setSelectedIndex(0);
		IDUdata.makeGeneralTableIDU(tableData, this, descriptionText);
		
		timeWeight = (float)0.5;
        changeWeight = (float)0.5;
        preProcessingTime = false;
        preProcessingChange = false;
        if(getAllPPLTransitions().size()<56){
        	numberOfPhases=40;
        }
        else{
        	numberOfPhases = 56;
        }
	    numberOfClusters =14;
        
        System.out.println(timeWeight+" "+changeWeight);
        
		PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(inputCsv,outputAssessment1,outputAssessment2,timeWeight,changeWeight,preProcessingTime,preProcessingChange);

		Double b=new Double(0.3);
		Double d=new Double(0.3);
		Double c=new Double(0.3);
			
		mainEngine.parseInput();		
		System.out.println("\n\n\n");
		mainEngine.extractPhases(numberOfPhases);
		
		mainEngine.connectTransitionsWithPhases(this);
		setPhaseCollectors(mainEngine.getPhaseCollectors());
		TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(this,b,d,c);
		mainEngine2.extractClusters(numberOfClusters);
		setClusterCollectors(mainEngine2.getClusterCollectors());
		mainEngine2.print();
		
		if(getPhaseCollectors().size()!=0){
			TableConstructionWithClusters tableP=new TableConstructionWithClusters(this);
			final String[] columnsP=tableP.constructColumns();
			final String[][] rowsP=tableP.constructRows();
			segmentSize=tableP.getSegmentSize();
			finalColumns=columnsP;
			finalRows=rowsP;
			tabbedPane.setSelectedIndex(0);
			GeneralTablePhaseData GTPD = new GeneralTablePhaseData(generalModel);
			GTPD.addListenersAndRenderers(this, tableData, descriptionText, lifeTimeTable, IDUdata);
			fillClustersTree();
		}
		System.out.println("Schemas:"+getAllPPLSchemas().size());
		System.out.println("Transitions:"+getAllPPLTransitions().size());
		System.out.println("Tables:"+getAllPPLTables().size());

	}
	
	*/
	
}
