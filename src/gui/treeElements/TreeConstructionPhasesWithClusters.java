package gui.treeElements;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import tableClustering.clusterExtractor.commons.Cluster;
import data.dataKeeper.ClusterManager;
import data.dataKeeper.GlobalDataKeeper;

public class TreeConstructionPhasesWithClusters implements TreeConstruction {
	
	private GlobalDataKeeper dataKeeper=null;
	private ClusterManager clusterManager;

	//to be deprecated
	public TreeConstructionPhasesWithClusters(GlobalDataKeeper dataKeeper) {
		this.dataKeeper=dataKeeper;
	}
	
	
	public TreeConstructionPhasesWithClusters(ClusterManager clusterManager) {
		this.clusterManager=clusterManager;
	}
	
	// to be deprecated
	@Override
	public JTree constructTree() {
		
		DefaultMutableTreeNode top=new DefaultMutableTreeNode("Clusters");		
				
		ArrayList<Cluster> clusters=dataKeeper.getClusterCollectors().get(0).getClusters();
		
		for(int i=0; i<clusters.size(); i++){
			
			DefaultMutableTreeNode a=new DefaultMutableTreeNode("Cluster "+i);
			top.add(a);
			ArrayList<String> tables=clusters.get(i).getNamesOfTables();
			
			for(String tableName:tables){
				DefaultMutableTreeNode a1=new DefaultMutableTreeNode(tableName);
				a.add(a1);
				
			}
			
		}
		
		JTree treeToConstruct = new JTree(top);
		
		return treeToConstruct;
	}
	
	public JTree constructTree2() {
		
		DefaultMutableTreeNode top=new DefaultMutableTreeNode("Clusters");		
				
		ArrayList<Cluster> clusters=clusterManager.getClusterCollectors().get(0).getClusters();
		
		for(int i=0; i<clusters.size(); i++){
			
			DefaultMutableTreeNode a=new DefaultMutableTreeNode("Cluster "+i);
			top.add(a);
			ArrayList<String> tables=clusters.get(i).getNamesOfTables();
			
			for(String tableName:tables){
				DefaultMutableTreeNode a1=new DefaultMutableTreeNode(tableName);
				a.add(a1);
				
			}
			
		}
		
		JTree treeToConstruct = new JTree(top);
		
		return treeToConstruct;
	}


}
