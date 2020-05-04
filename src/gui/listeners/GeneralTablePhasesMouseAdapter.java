package gui.listeners;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataKeeper.ClusterManager;
import data.dataKeeper.TableData;
import data.dataKeeper.TableManager;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.commons.ShowDetailsComponents;
import gui.tableElements.tableConstructors.PldConstruction;
import gui.tableElements.tableConstructors.TableConstructionClusterTablesPhasesZoomA;
import gui.tableElements.tableConstructors.TableConstructionZoomArea;

public class GeneralTablePhasesMouseAdapter extends MouseAdapter {
	
	private TableData tableData;
	private JvTable generalTable;
	
	private ShowDetailsComponents showDetails;
	
	private TableManager tableManager;
	private ClusterManager clusterManager;
	
	

	
	public GeneralTablePhasesMouseAdapter(TableData tableData, JvTable generalTable,
			ShowDetailsComponents showDetails, TableManager tableManager, ClusterManager clusterManager) {
		super();
		this.tableData = tableData;
		this.generalTable = generalTable;
		this.showDetails = showDetails;
		this.tableManager = tableManager;
		this.clusterManager = clusterManager;
	}

	@Override
	   public void mouseClicked(MouseEvent e) {
		
		if (e.getClickCount() == 1) {
			JTable target = (JTable)e.getSource();
	         
	         tableData.setSelectedRowsFromMouse(target.getSelectedRows());
	         tableData.setSelectedColumn(target.getSelectedColumn());
	         tableData.getLifeTimeTable().repaint();
		}

	   }
	
	@Override
	   public void mouseReleased(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON3){
				System.out.println("Right Click");

				JTable target1 = (JTable)e.getSource();
				tableData.setSelectedColumn(target1.getSelectedColumn());
				tableData.setSelectedRowsFromMouse(new int[target1.getSelectedRows().length]);
				tableData.setSelectedRowsFromMouse(target1.getSelectedRows());
				
				final String sSelectedRow = (String) generalTable.getValueAt(target1.getSelectedRow(),0);
				tableData.setTablesSelected(new ArrayList<String>());

				for(int rowsSelected=0; rowsSelected<tableData.getSelectedRowsFromMouse().length; rowsSelected++){
					tableData.getTablesSelected().add((String) generalTable.getValueAt(tableData.getSelectedRowsFromMouse()[rowsSelected], 0));
				}
				
				JPopupMenu popupMenu = new JPopupMenu();
		        JMenuItem showDetailsItem = new JMenuItem("Show Details for the selection");
		        showDetailsItem.addActionListener(new TableShowDetailsActionListener(tableData, tableManager, clusterManager, showDetails, sSelectedRow));
		        popupMenu.add(showDetailsItem);
		        JMenuItem clearSelectionItem = new JMenuItem("Clear Selection");
		        clearSelectionItem.addActionListener(new ActionListener() {

		            @Override
		            public void actionPerformed(ActionEvent le) {
		            	
		            	tableData.setSelectedFromTree(new ArrayList<String>());
		            	tableData.getLifeTimeTable().repaint();
		            }
		        });
		        popupMenu.add(clearSelectionItem);
		        popupMenu.show(generalTable, e.getX(),e.getY());
				      
			}
		
	   }
	

}
