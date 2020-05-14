package gui.mainEngine;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import data.dataKeeper.ClusterManager;
import data.dataKeeper.TableData;
import data.dataKeeper.TableManager;
import data.dataSorters.PldRowSorter;
import gui.listeners.tables.*;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.commons.ShowDetailsComponents;
import gui.tableElements.tableRenderers.GeneralTableIDUDefaultTableCellRenderer;
import gui.tableElements.tableRenderers.GeneralTablePhasesDefaultTableCellRenderer;
import gui.tableElements.tableRenderers.IDUHeaderTableRenderer;
import gui.tableElements.tableRenderers.IDUTableRenderer;

public class TableUpdater {
	
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton uniformlyDistributedButton;
	private JButton notUniformlyDistributedButton;
	private JButton showThisToPopup;
	
	private MyTableModel zoomModel;
	private MyTableModel generalModel;

	private JScrollPane tmpScrollPaneZoomArea;
	private JScrollPane tmpScrollPane;
	
	private JPanel lifeTimePanel;

	private JTextArea descriptionText;
	
	private TableManager tableManager;
	private ClusterManager clusterManager;
	
	private ShowDetailsComponents showDetails;

	public void setShowDetails(ShowDetailsComponents showDetails)
	{
		this.showDetails = showDetails;
	}
	
	public TableUpdater(JScrollPane tmpScrollPaneZoomArea,JScrollPane tmpScrollPane, JPanel lifeTimePanel, MyTableModel zoomModel,MyTableModel generalModel, JTextArea descriptionText) 
	{
		this.tmpScrollPaneZoomArea = tmpScrollPaneZoomArea;
		this.tmpScrollPane = tmpScrollPane;
		this.lifeTimePanel = lifeTimePanel;
		this.zoomModel = zoomModel;
		this.generalModel = generalModel;
		this.descriptionText = descriptionText;
	}
	
	public void setButtons(JButton zoomInButton, JButton zoomOutButton, JButton uniformlyDistributedButton,
			JButton notUniformlyDistributedButton, JButton showThisToPopup, JButton undoButton)
	{
		this.zoomInButton = zoomInButton;
		this.zoomOutButton = zoomOutButton;
		this.uniformlyDistributedButton = uniformlyDistributedButton;
		this.notUniformlyDistributedButton = notUniformlyDistributedButton;
		this.showThisToPopup = showThisToPopup;
	}
	
	public void setManagers (TableManager tableManager, ClusterManager clusterManager)
	{
		this.tableManager = tableManager;
		this.clusterManager = clusterManager;
	}
	
	public void setButtonsVisible()
	{
		zoomInButton.setVisible(true);
		zoomOutButton.setVisible(true);
	}

public void makeGeneralTableIDU(final TableData tableData) {
	
		PldRowSorter sorter=new PldRowSorter(tableData.getFinalRowsZoomArea(),tableManager);
		
		tableData.setFinalRowsZoomArea(sorter.sortRows2());
	    
		tableData.setShowingPld(true);
		zoomInButton.setVisible(true);
		zoomOutButton.setVisible(true);
		
		showThisToPopup.setVisible(true);
		
		int numberOfColumns=tableData.getFinalRowsZoomArea()[0].length;
		int numberOfRows=tableData.getFinalRowsZoomArea().length;
		
		tableData.setSelectedRows(new ArrayList<Integer>());
		
		String[][] rows=new String[numberOfRows][numberOfColumns];
		
		for(int i=0; i<numberOfRows; i++){
			
			rows[i][0]=tableData.getFinalRowsZoomArea()[i][0];
			
		}
		
		zoomModel=new MyTableModel(tableData.getFinalColumnsZoomArea(), rows);
		
		//final JvTable generalTable=new JvTable(zoomModel);
		tableData.setZoomAreaTable(new JvTable(zoomModel));
		
		
		tableData.getZoomAreaTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		if(tableData.getRowHeight()<1){
			tableData.setRowHeight(1);
		}
		if (tableData.getColumnWidth()<1) {
			tableData.setColumnWidth(1);
		}
		
		for(int i=0; i<tableData.getZoomAreaTable().getRowCount(); i++){
			tableData.getZoomAreaTable().setRowHeight(i, tableData.getRowHeight());
				
		}

		
		tableData.getZoomAreaTable().setShowGrid(false);
		tableData.getZoomAreaTable().setIntercellSpacing(new Dimension(0, 0));
		
		
		
		for(int i=0; i<tableData.getZoomAreaTable().getColumnCount(); i++){
			if(i==0){
				tableData.getZoomAreaTable().getColumnModel().getColumn(0).setPreferredWidth(tableData.getColumnWidth());
				
			}
			else{
				tableData.getZoomAreaTable().getColumnModel().getColumn(i).setPreferredWidth(tableData.getColumnWidth());
				
			}
		}
		
		int start=-1;
		int end=-1;
		if (clusterManager.getPhaseCollectors()!=null && tableData.getWholeCol()!=-1 && tableData.getWholeCol()!=0) {
			start=clusterManager.getPhaseCollectors().get(0).getPhases().get(tableData.getWholeCol()-1).getStartPos();
			end=clusterManager.getPhaseCollectors().get(0).getPhases().get(tableData.getWholeCol()-1).getEndPos();
		}


		
		if(tableData.getWholeCol()!=-1){
			for(int i=0; i<tableData.getZoomAreaTable().getColumnCount(); i++){
				if(!(tableData.getZoomAreaTable().getColumnName(i).equals("Table name"))){
					if(Integer.parseInt(tableData.getZoomAreaTable().getColumnName(i))>=start && Integer.parseInt(tableData.getZoomAreaTable().getColumnName(i))<=end){
			
						tableData.getZoomAreaTable().getColumnModel().getColumn(i).setHeaderRenderer(new IDUHeaderTableRenderer());
			
					}
				}
			}
		}
		
		final IDUTableRenderer renderer = new IDUTableRenderer(descriptionText,tableData.getFinalRowsZoomArea(), tableManager, tableData.getSegmentSize());
		//generalTable.setDefaultRenderer(Object.class, renderer);
		
		

		tableData.getZoomAreaTable().setDefaultRenderer(Object.class, new GeneralTableIDUDefaultTableCellRenderer(tableData, tableManager, clusterManager, descriptionText));
		
		tableData.getZoomAreaTable().addMouseListener(new GeneralTableIDUMouseAdapter(tableData, tableData.getZoomAreaTable(), tableData.getZoomAreaTable(), renderer));
		tableData.getZoomAreaTable().getTableHeader().addMouseListener(new GeneralTableIDUHeaderMouseAdapter(tableData, tableData.getZoomAreaTable(),renderer));
		
		//tableData.setZoomAreaTable(generalTable);
		tmpScrollPaneZoomArea.setViewportView(tableData.getZoomAreaTable());
		tmpScrollPaneZoomArea.setAlignmentX(0);
		tmpScrollPaneZoomArea.setAlignmentY(0);
		tmpScrollPaneZoomArea.setBounds(300,300,950,250);
		tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
		//lifeTimePanel.setCursor(gui.getCursor());
		lifeTimePanel.add(tmpScrollPaneZoomArea);

		
		
	}


public void makeGeneralTablePhases(final TableData tableData) {
	uniformlyDistributedButton.setVisible(true);
	
	notUniformlyDistributedButton.setVisible(true);
	
	int numberOfColumns=tableData.getFinalRows()[0].length;
	int numberOfRows=tableData.getFinalRows().length;
	
	tableData.setSelectedRows(new ArrayList<Integer>());
	
	String[][] rows=new String[numberOfRows][numberOfColumns];
	
	for(int i=0; i<numberOfRows; i++){
		
		rows[i][0]=tableData.getFinalRows()[i][0];
		
	}
	
	generalModel=new MyTableModel(tableData.getFinalColumns(), rows);
	
	//final JvTable generalTable=new JvTable(generalModel);
	tableData.setLifeTimeTable(new JvTable(generalModel));
	
	
	tableData.getLifeTimeTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	
	tableData.getLifeTimeTable().setShowGrid(false);
	tableData.getLifeTimeTable().setIntercellSpacing(new Dimension(0, 0));

	
	
	for(int i=0; i<tableData.getLifeTimeTable().getColumnCount(); i++){
		if(i==0){
			tableData.getLifeTimeTable().getColumnModel().getColumn(0).setPreferredWidth(86);
		}
		else{
			
			tableData.getLifeTimeTable().getColumnModel().getColumn(i).setPreferredWidth(1);
		}
	}
	
	tableData.getLifeTimeTable().setDefaultRenderer(Object.class, new GeneralTablePhasesDefaultTableCellRenderer(tableData, tableManager, clusterManager, descriptionText));

	tableData.getLifeTimeTable().addMouseListener(new MouseAdapter() {


		@Override
		   public void mouseClicked(MouseEvent e) {
			
			if (e.getClickCount() == 1) {
				JTable target = (JTable)e.getSource();
		         
		         tableData.setSelectedRowsFromMouse(target.getSelectedRows());
		         tableData.setSelectedColumn(target.getSelectedColumn());
		         tableData.getLifeTimeTable().repaint();
			}

		   }
	});
	
	tableData.getLifeTimeTable().addMouseListener(new GeneralTablePhasesMouseAdapter(tableData, tableData.getLifeTimeTable(), showDetails, tableManager, clusterManager));

	
	tableData.getLifeTimeTable().getTableHeader().addMouseListener(new MouseAdapter() {

	    @Override
	    public void mouseClicked(MouseEvent e) {
	        tableData.setWholeCol(tableData.getLifeTimeTable().columnAtPoint(e.getPoint()));
	        String name = tableData.getLifeTimeTable().getColumnName(tableData.getWholeCol());
	        System.out.println("Column index selected " + tableData.getWholeCol() + " " + name);
	        tableData.getLifeTimeTable().repaint();

	        if (tableData.isShowingPld()) {
		        makeGeneralTableIDU(tableData);
			}
	    }
	});
	
	tableData.getLifeTimeTable().getTableHeader().addMouseListener(new MouseAdapter() {

	    @Override
	    public void mouseReleased(MouseEvent e) {
	    	if(SwingUtilities.isRightMouseButton(e)){
				System.out.println("Right Click");
				
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem clearColumnSelectionItem = new JMenuItem("Clear Column Selection");
				        clearColumnSelectionItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
				            	tableData.setWholeCol(-1);
				            	tableData.getLifeTimeTable().repaint();

				            	if(tableData.isShowingPld()){
				            		makeGeneralTableIDU(tableData);
				            	}
				            }
				        });
				        popupMenu.add(clearColumnSelectionItem);
				        JMenuItem showDetailsItem = new JMenuItem("Show Details for this Phase");
				        
				        
				        
				        showDetailsItem.addActionListener(new TableHeaderShowDetailsActionListener(tableData, tableManager, clusterManager, showDetails, tableData.getLifeTimeTable()));
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(tableData.getLifeTimeTable(), e.getX(),e.getY());

				    
			}
		
	   }
	    
	});
	
	
	//tableData.setLifeTimeTable(generalTable);
	
	tmpScrollPane.setViewportView(tableData.getLifeTimeTable());
	tmpScrollPane.setAlignmentX(0);
	tmpScrollPane.setAlignmentY(0);
    tmpScrollPane.setBounds(300,30,950,265);
    tmpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    tmpScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
	//lifeTimePanel.setCursor(getCursor());
	lifeTimePanel.add(tmpScrollPane);
    
	
	
}






}
