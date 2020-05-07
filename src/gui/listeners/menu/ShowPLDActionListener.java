package gui.listeners.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import data.dataKeeper.GlobalManager;
import data.dataKeeper.TableData;
import gui.mainEngine.TableUpdater;
import gui.mainEngine.TreeManager;
import gui.tableElements.tableConstructors.TableConstructionIDU;

public class ShowPLDActionListener implements ActionListener {

	private TableUpdater tableUpdater;
	private TableData tableData;
	private GlobalManager globalManager;
	private TreeManager treeManager;
	private JTabbedPane tabbedPane;
	
	
	
	
	
	public ShowPLDActionListener(TableUpdater tableUpdater, TableData tableData, GlobalManager globalManager,
			TreeManager treeManager, JTabbedPane tabbedPane) {
		super();
		this.tableUpdater = tableUpdater;
		this.tableData = tableData;
		this.globalManager = globalManager;
		this.treeManager = treeManager;
		this.tabbedPane = tabbedPane;
	}





	@Override
	public void actionPerformed(ActionEvent arg0) {
		 if(!(globalManager.getProjectManager().getCurrentProject()==null)){
			//zoomInButton.setVisible(true);
			//zoomOutButton.setVisible(true);
			tableUpdater.setButtonsVisible();
			TableConstructionIDU table=new TableConstructionIDU(globalManager.getTableManager().getAllPPLSchemas(), globalManager.getTableManager().getAllPPLTransitions());
			final String[] columns=table.constructColumns();
			final String[][] rows=table.constructRows();
			tableData.setSegmentSizeZoomArea(table.getSegmentSize());
			System.out.println("Schemas: "+globalManager.getTableManager().getAllPPLSchemas().size());
			System.out.println("C: "+columns.length+" R: "+rows.length);

			tableData.setFinalColumnsZoomArea(columns);
			tableData.setFinalRowsZoomArea(rows);
			tabbedPane.setSelectedIndex(0);
			tableUpdater.makeGeneralTableIDU(tableData);
			treeManager.fillTree(globalManager.getTableManager());
			
		}
		else{
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
	}

}
