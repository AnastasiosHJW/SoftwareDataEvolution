package data.dataKeeper;

import java.util.ArrayList;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.PPLTransition;
import phaseAnalyzer.commons.PhaseCollector;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.commons.ClusterCollector;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class ClusterManager {
	
	private ArrayList<PhaseCollector> phaseCollectors = null;
	private ArrayList<ClusterCollector> clusterCollectors = null;
	
	private Float timeWeight=null;
	private Float changeWeight=null;
	private Double birthWeight=null;
	private Double deathWeight=null;
	private Double changeWeightCl=null;

	private Integer numberOfPhases=null;
	private Integer numberOfClusters=null;
	private Boolean preProcessingTime=null;
	private Boolean preProcessingChange=null;
	
	public ClusterManager()
	{
		phaseCollectors = new ArrayList<PhaseCollector>();
		clusterCollectors = new ArrayList<ClusterCollector>();
	}
	

	
	public void makePhases(String inputCsv, String outputAssessment1, String outputAssessment2, TreeMap<Integer, PPLTransition> allPPLTransitions)
	{
		PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(inputCsv,outputAssessment1,outputAssessment2, this);
		mainEngine.parseInput();
		mainEngine.extractPhases(numberOfPhases);
		mainEngine.connectTransitionsWithPhases(allPPLTransitions);
		phaseCollectors = mainEngine.getPhaseCollectors();
	}
	
	public void makeClusters(TreeMap<String, PPLSchema> allPPLSchemas, TreeMap<String, PPLTable> allPPLTables, Double changeWeightP)
	{
		TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(allPPLSchemas, allPPLTables,birthWeight,deathWeight, changeWeightP);
		mainEngine2.extractClusters(numberOfClusters);
		clusterCollectors = mainEngine2.getClusterCollectors();
		mainEngine2.print();
	}

	public Float getTimeWeight() {
		return timeWeight;
	}

	public void setTimeWeight(Float timeWeight) {
		this.timeWeight = timeWeight;
	}

	public Float getChangeWeight() {
		return changeWeight;
	}

	public void setChangeWeight(Float changeWeight) {
		this.changeWeight = changeWeight;
	}

	public Double getBirthWeight() {
		return birthWeight;
	}

	public void setBirthWeight(Double birthWeight) {
		this.birthWeight = birthWeight;
	}

	public Double getDeathWeight() {
		return deathWeight;
	}

	public void setDeathWeight(Double deathWeight) {
		this.deathWeight = deathWeight;
	}

	public Double getChangeWeightCl() {
		return changeWeightCl;
	}

	public void setChangeWeightCl(Double changeWeightCl) {
		this.changeWeightCl = changeWeightCl;
	}

	public Integer getNumberOfPhases() {
		return numberOfPhases;
	}

	public void setNumberOfPhases(Integer numberOfPhases) {
		this.numberOfPhases = numberOfPhases;
	}

	public Integer getNumberOfClusters() {
		return numberOfClusters;
	}

	public void setNumberOfClusters(Integer numberOfClusters) {
		this.numberOfClusters = numberOfClusters;
	}

	public Boolean getPreProcessingTime() {
		return preProcessingTime;
	}

	public void setPreProcessingTime(Boolean preProcessingTime) {
		this.preProcessingTime = preProcessingTime;
	}

	public Boolean getPreProcessingChange() {
		return preProcessingChange;
	}

	public void setPreProcessingChange(Boolean preProcessingChange) {
		this.preProcessingChange = preProcessingChange;
	}

	public ArrayList<PhaseCollector> getPhaseCollectors() {
		return phaseCollectors;
	}

	public void setPhaseCollectors(ArrayList<PhaseCollector> phaseCollectors) {
		this.phaseCollectors = phaseCollectors;
	}

	public ArrayList<ClusterCollector> getClusterCollectors() {
		return clusterCollectors;
	}

	public void setClusterCollectors(ArrayList<ClusterCollector> clusterCollectors) {
		this.clusterCollectors = clusterCollectors;
	}
	
	

}
