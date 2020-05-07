package gui.listeners.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import data.dataKeeper.ClusterManager;
import data.dataKeeper.TableData;
import data.dataKeeper.TableManager;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.ShowDetailsComponents;

public class TableHeaderShowDetailsActionListener extends ShowDetailsActionListener implements ActionListener {

	private JvTable generalTable;
	 public TableHeaderShowDetailsActionListener(TableData tableData, TableManager tableManager,
			ClusterManager clusterManager, ShowDetailsComponents showDetails, JvTable generalTable) {
		super(tableData, tableManager, clusterManager, showDetails);
		this.generalTable = generalTable;
	}

	@Override
     public void actionPerformed(ActionEvent e) {
			String sSelectedRow=tableData.getFinalRows()[0][0];
			System.out.println("?"+sSelectedRow);
			tableData.setTablesSelected(new ArrayList<String>());
     	for(int i=0; i<tableData.getFinalRows().length; i++)
     		tableData.getTablesSelected().add((String) generalTable.getValueAt(i, 0));

     	if(!sSelectedRow.contains("Cluster ")){
     		
     		showSelectionToZoomArea(tableData.getWholeCol());	
     	}
     	else{
     		showClusterSelectionToZoomArea(tableData.getWholeCol(), "");
     	}
     	
     }

}
