package gui.listeners.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import data.dataKeeper.GlobalDataKeeper;
import gui.mainEngine.Gui;
import gui.tableElements.tableConstructors.TableConstructionAllSquaresIncluded;

public class ShowLifetimeTableActionListener implements ActionListener {

	private JTabbedPane tabbedPane;
	private GlobalDataKeeper globalDataKeeper;
	private String currentProject;
	private Gui gui;

	public ShowLifetimeTableActionListener(JTabbedPane tabbedPane, GlobalDataKeeper globalDataKeeper, String currentProject, Gui gui) {
		super();
		this.tabbedPane = tabbedPane;
		this.globalDataKeeper = globalDataKeeper;
		this.currentProject = currentProject;
		this.gui = gui;		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!(currentProject==null)){
			TableConstructionAllSquaresIncluded table=new TableConstructionAllSquaresIncluded(globalDataKeeper);
			final String[] columns=table.constructColumns();
			final String[][] rows=table.constructRows();
			gui.setSegmentSizeDetailedTable(table.getSegmentSize());
			tabbedPane.setSelectedIndex(0);
			gui.makeDetailedTable(columns,rows,true);
		}
		else{
			JOptionPane.showMessageDialog(null, "Select a Project first");
			return;
		}
	}

}
