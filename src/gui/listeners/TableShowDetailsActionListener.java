package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import data.dataKeeper.ClusterManager;
import data.dataKeeper.TableData;
import data.dataKeeper.TableManager;
import gui.tableElements.commons.ShowDetailsComponents;

public class TableShowDetailsActionListener extends ShowDetailsActionListener implements ActionListener {

	private String sSelectedRow;
	public TableShowDetailsActionListener(TableData tableData, TableManager tableManager, ClusterManager clusterManager,
			ShowDetailsComponents showDetails, String sSelectedRow) {
		super(tableData, tableManager, clusterManager, showDetails);
		this.sSelectedRow = sSelectedRow;
	}

	@Override
    public void actionPerformed(ActionEvent le) {
    	if(sSelectedRow.contains("Cluster ")){
    		showClusterSelectionToZoomArea(tableData.getSelectedColumn(), sSelectedRow);

    	}
    	else{
    		showSelectionToZoomArea(tableData.getSelectedColumn());
    	}
    }
}
