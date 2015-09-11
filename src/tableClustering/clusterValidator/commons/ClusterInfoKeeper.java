package tableClustering.clusterValidator.commons;

import java.util.Map;
import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLTable;
import tableClustering.clusterExtractor.commons.Cluster;
import tableClustering.clusterValidator.clusterValidityMetrics.clusterMetrics.ClusterCohesionMetric;
import tableClustering.clusterValidator.clusterValidityMetrics.clusterMetrics.ClusterSeparationMetric;

public class ClusterInfoKeeper {
	
	private Cluster cluster = new Cluster();
	private Centroid clusterCentroid=null;
	private Centroid overallCentroid=null;
	private Double clusterCohesion = null;
	private Double clusterSeparation = null;

	
	public ClusterInfoKeeper(Cluster cluster,Centroid overallCentroid){
		this.cluster=cluster;
		this.overallCentroid=overallCentroid;
		initialize();
	}
	
	public ClusterInfoKeeper(){
		this.cluster=cluster;
		initialize();
	}
	
	private void initialize(){
		
		initializeCentroid();
		computeClusterCohesion();
		computeClusterSeparation();
		
		
	}
	
	private void initializeCentroid(){
		
		TreeMap<String, PPLTable> tables=this.cluster.getTables();
		double x=0;
		double y=0;
		double z=0;
		for(Map.Entry<String,PPLTable> pplTab:tables.entrySet()){
			x = x +pplTab.getValue().getBirthVersionID();
			y = y+pplTab.getValue().getDeathVersionID();
			z= z+pplTab.getValue().getTotalChanges();
		}
		
		x= x/tables.size();
		y= y/tables.size();
		z= z/tables.size();
		
		this.clusterCentroid=new Centroid(x, y, z);
		
		//System.out.println(this.clusterCentroid.getX()+" "+this.clusterCentroid.getY()+" "+this.clusterCentroid.getZ());
		
	}
	
	private void computeClusterCohesion(){
		
		ClusterCohesionMetric cohesionMetricCalculator = new ClusterCohesionMetric(this);
		cohesionMetricCalculator.computeMetric();
		clusterCohesion=cohesionMetricCalculator.getResult();
		//System.out.println(clusterCohesion);
		
	}
	
	private void computeClusterSeparation(){
		
		ClusterSeparationMetric separationMetricCalculator = new ClusterSeparationMetric(clusterCentroid,overallCentroid);
		separationMetricCalculator.computeMetric();
		clusterSeparation=(double)this.cluster.getTables().size()*separationMetricCalculator.getResult();
		//System.out.println(clusterSeparation+"\n");
		
	}
	
	public Cluster getCluster(){
		return this.cluster;
	}
	
	public Centroid getCentroid(){
		return this.clusterCentroid;
	}
	
	public Double getClusterCohesion() {
		return clusterCohesion;
	}
	
	public Double getClusterSeparation() {
		return clusterSeparation;
	}

}
