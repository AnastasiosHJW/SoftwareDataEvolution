package gui.treeElements;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import data.dataKeeper.GlobalDataKeeper;
import data.dataKeeper.TableManager;
import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;

public class TreeConstructionGeneral {

	private GlobalDataKeeper dataKeeper=null;
	private TableManager tableManager;
	
	public TreeConstructionGeneral(GlobalDataKeeper dataKeeper){
		this.dataKeeper=dataKeeper;
	}
	
	public TreeConstructionGeneral(TableManager tableManager){
		this.tableManager=tableManager;
	}
	
	public JTree constructTree(){
		
		
		DefaultMutableTreeNode top=new DefaultMutableTreeNode("Versions");
		TreeMap<String, PPLSchema> schemas=dataKeeper.getAllPPLSchemas();
		
		for (Map.Entry<String,PPLSchema> pplSc : schemas.entrySet()) {
			
			DefaultMutableTreeNode a=new DefaultMutableTreeNode(pplSc.getKey());
		    top.add(a);
		    TreeMap<String, PPLTable> tables=pplSc.getValue().getTables();
		    
			for (Map.Entry<String,PPLTable> pplT : tables.entrySet()) {
				DefaultMutableTreeNode a1=new DefaultMutableTreeNode(pplT.getKey());
				a.add(a1);
			}
		    
		}
		
		JTree treeToConstruct = new JTree(top);
		
		return treeToConstruct;
		
	}
	
public JTree constructTree2(){
		
		
		DefaultMutableTreeNode top=new DefaultMutableTreeNode("Versions");
		TreeMap<String, PPLSchema> schemas=tableManager.getAllPPLSchemas();
		
		for (Map.Entry<String,PPLSchema> pplSc : schemas.entrySet()) {
			
			DefaultMutableTreeNode a=new DefaultMutableTreeNode(pplSc.getKey());
		    top.add(a);
		    TreeMap<String, PPLTable> tables=pplSc.getValue().getTables();
		    
			for (Map.Entry<String,PPLTable> pplT : tables.entrySet()) {
				DefaultMutableTreeNode a1=new DefaultMutableTreeNode(pplT.getKey());
				a.add(a1);
			}
		    
		}
		
		JTree treeToConstruct = new JTree(top);
		
		return treeToConstruct;
		
	}
	
}
