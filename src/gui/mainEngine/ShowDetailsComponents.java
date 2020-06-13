package gui.mainEngine;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;

public class ShowDetailsComponents {
	
	

	private JTextArea descriptionText;
	private JTabbedPane tabbedPane;
	private JScrollPane tmpScrollPaneZoomArea;
	private MyTableModel zoomModel;
	private JvTable zoomAreaTable;
	private JPanel lifeTimePanel;
	private JButton undoButton;
	
	public ShowDetailsComponents(JTextArea descriptionText, JTabbedPane tabbedPane, JScrollPane tmpScrollPaneZoomArea,
			MyTableModel zoomModel, JvTable zoomAreaTable, JPanel lifeTimePanel, JButton undoButton) {
		this.descriptionText = descriptionText;
		this.tabbedPane = tabbedPane;
		this.tmpScrollPaneZoomArea = tmpScrollPaneZoomArea;
		this.zoomModel = zoomModel;
		this.zoomAreaTable = zoomAreaTable;
		this.lifeTimePanel = lifeTimePanel;
		this.undoButton = undoButton;
	}
	
	public void setZoomModel(MyTableModel zoomModel) {
		this.zoomModel = zoomModel;
	}

	public void setZoomAreaTable(JvTable zoomAreaTable) {
		this.zoomAreaTable = zoomAreaTable;
	}


	public JTextArea getDescriptionText() {
		return descriptionText;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public JScrollPane getTmpScrollPaneZoomArea() {
		return tmpScrollPaneZoomArea;
	}

	public MyTableModel getZoomModel() {
		return zoomModel;
	}

	public JvTable getZoomAreaTable() {
		return zoomAreaTable;
	}

	public JPanel getLifeTimePanel() {
		return lifeTimePanel;
	}

	public JButton getUndoButton() {
		return undoButton;
	}
	
	
	
	

}
