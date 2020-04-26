package tableClustering.clusterExtractor.engine;

import java.util.ArrayList;
import java.util.TreeMap;

import tableClustering.clusterExtractor.analysis.ClusterExtractor;
import tableClustering.clusterExtractor.analysis.ClusterExtractorFactory;
import tableClustering.clusterExtractor.commons.ClusterCollector;
import data.dataKeeper.GlobalDataKeeper;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;

public class TableClusteringMainEngine {
	
	private GlobalDataKeeper dataKeeper;
	private Double birthWeight;
	private Double deathWeight;
	private Double changeWeight;
	private ArrayList<ClusterCollector> clusterCollectors;
	private ClusterExtractorFactory clusterExtractorFactory;
	private ClusterExtractor clusterExtractor;
	private ArrayList<ClusterCollector> allClusterCollectors;
	
	private TreeMap<String,PPLSchema> allPPLSchemas = null;
	private TreeMap<String,PPLTable> allPPLTables = null;

	//to be deprecated
	public TableClusteringMainEngine(GlobalDataKeeper dataKeeper,Double birthWeight, Double deathWeight,
			Double changeWeight){
		
		this.dataKeeper=dataKeeper;
		this.birthWeight=birthWeight;
		this.deathWeight=deathWeight;
		this.changeWeight=changeWeight;
		
		clusterExtractorFactory = new ClusterExtractorFactory();
		clusterExtractor = clusterExtractorFactory.createClusterExtractor("AgglomerativeClusterExtractor");
		
		allClusterCollectors = new ArrayList<ClusterCollector>();

	}
	

	public TableClusteringMainEngine(TreeMap<String,PPLSchema> allPPLSchemas, TreeMap<String,PPLTable> allPPLTables,Double birthWeight, Double deathWeight,
			Double changeWeight){
		

		this.birthWeight=birthWeight;
		this.deathWeight=deathWeight;
		this.changeWeight=changeWeight;
		
		clusterExtractorFactory = new ClusterExtractorFactory();
		clusterExtractor = clusterExtractorFactory.createClusterExtractor("AgglomerativeClusterExtractor");
		
		allClusterCollectors = new ArrayList<ClusterCollector>();
		
		this.allPPLSchemas = allPPLSchemas;
		this.allPPLTables = allPPLTables;

	}
	
	//to be deprecated
	public void extractClusters(int numClusters){
		
		clusterCollectors = new ArrayList<ClusterCollector>();
		ClusterCollector clusterCollector = new ClusterCollector();
		clusterCollector = clusterExtractor.extractAtMostKClusters(dataKeeper, numClusters, birthWeight, deathWeight, changeWeight);
		clusterCollector.sortClustersByBirthDeath();
		clusterCollectors.add(clusterCollector);
		
		allClusterCollectors.add(clusterCollector);

	}
	
	public void extractClusters2(int numClusters){
		
		clusterCollectors = new ArrayList<ClusterCollector>();
		ClusterCollector clusterCollector = new ClusterCollector();
		clusterCollector = clusterExtractor.extractAtMostKClusters(allPPLSchemas, allPPLTables, numClusters, birthWeight, deathWeight, changeWeight);
		clusterCollector.sortClustersByBirthDeath();
		clusterCollectors.add(clusterCollector);
		
		allClusterCollectors.add(clusterCollector);

	}
	
	public void print(){
		
		String toPrint="";
		
		for(int i=0; i<allClusterCollectors.size(); i++){
			ClusterCollector clusterCollector=allClusterCollectors.get(i);
			toPrint=toPrint+clusterCollector.toString();
			
		}
		
		System.out.println(toPrint);
	}
	
	public ArrayList<ClusterCollector> getClusterCollectors(){
		return allClusterCollectors;
	}
	
}
