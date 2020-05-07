package data.dataKeeper;

import java.io.IOException;

import javax.swing.JTabbedPane;

import org.antlr.v4.runtime.RecognitionException;

import gui.mainEngine.TableUpdater;
import gui.mainEngine.TreeManager;

public class GlobalManager {
	
	private TableManager tableManager;
	private ClusterManager clusterManager;
	private ProjectManager projectManager;
	
	public GlobalManager()
	{
		tableManager = new TableManager();
		clusterManager = new ClusterManager();
		projectManager = new ProjectManager();
	}
	
	public void importData(String fileName, TreeManager treeManager, TableData tableData, TableUpdater aux, JTabbedPane tabbedPane) throws RecognitionException, IOException
	{
		projectManager.importData(fileName, tableManager, clusterManager, treeManager, tableData, aux, tabbedPane);
	}

	public TableManager getTableManager() {
		return tableManager;
	}

	public ClusterManager getClusterManager() {
		return clusterManager;
	}

	public ProjectManager getProjectManager() {
		return projectManager;
	}

	
}
