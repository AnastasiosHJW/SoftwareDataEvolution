package data.dataKeeper;

import java.util.ArrayList;

import phaseAnalyzer.commons.PhaseCollector;
import tableClustering.clusterExtractor.commons.ClusterCollector;

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
