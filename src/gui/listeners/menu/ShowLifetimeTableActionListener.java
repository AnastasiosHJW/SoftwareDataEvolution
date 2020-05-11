package gui.listeners.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import data.dataKeeper.GlobalManager;
import data.dataKeeper.TableData;
import gui.tableElements.tableConstructors.TableConstructionAllSquaresIncluded;

public class ShowLifetimeTableActionListener implements ActionListener {

	private JTabbedPane tabbedPane;
	private GlobalManager globalManager;
	private TableData tableData;

	public ShowLifetimeTableActionListener(JTabbedPane tabbedPane, GlobalManager globalManager, TableData tableData) {
		super();
		this.tabbedPane = tabbedPane;
		this.globalManager = globalManager;
		this.tableData = tableData;		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!(globalManager.getProjectManager().getCurrentProject()==null)){
			TableConstructionAllSquaresIncluded table=new TableConstructionAllSquaresIncluded(globalManager.getTableManager());
			final String[] columns=table.constructColumns();
			final String[][] rows=table.constructRows();
			tableData.setSegmentSizeDetailedTable(table.getSegmentSize());
			tabbedPane.setSelectedIndex(0);
			globalManager.getTableManager().makeDetailedTable(columns,rows,true, tableData);
		}
		else{
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
	}

}
