package tableClustering.clusterExtractor.analysis;

import tableClustering.clusterExtractor.commons.ClusterCollector;

import java.util.TreeMap;

import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;

public interface ClusterExtractor {

	public ClusterCollector extractAtMostKClusters(TreeMap<String,PPLSchema> allPPLSchemas, TreeMap<String,PPLTable> allPPLTables,
			int numClusters, Double birthWeight, Double deathWeight, Double changeWeight);
	
}
