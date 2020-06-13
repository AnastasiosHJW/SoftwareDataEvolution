package gui.mainEngine;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataKeeper.ClusterManager;
import data.dataKeeper.TableData;
import data.dataKeeper.TableManager;
import data.dataSorters.PldRowSorter;
import gui.listeners.tables.*;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
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
	
	private JvTable zoomAreaTable = null;
	private JvTable LifeTimeTable = null;

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
		
		tableData.setFinalRowsZoomArea(sorter.sortRows());
	    
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
		zoomAreaTable = new JvTable(zoomModel);
		
		
		zoomAreaTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		if(tableData.getRowHeight()<1){
			tableData.setRowHeight(1);
		}
		if (tableData.getColumnWidth()<1) {
			tableData.setColumnWidth(1);
		}
		
		for(int i=0; i<zoomAreaTable.getRowCount(); i++){
			zoomAreaTable.setRowHeight(i, tableData.getRowHeight());
				
		}

		
		zoomAreaTable.setShowGrid(false);
		zoomAreaTable.setIntercellSpacing(new Dimension(0, 0));
		
		
		
		for(int i=0; i<zoomAreaTable.getColumnCount(); i++){
			if(i==0){
				zoomAreaTable.getColumnModel().getColumn(0).setPreferredWidth(tableData.getColumnWidth());
				
			}
			else{
				zoomAreaTable.getColumnModel().getColumn(i).setPreferredWidth(tableData.getColumnWidth());
				
			}
		}
		
		int start=-1;
		int end=-1;
		if (clusterManager.getPhaseCollectors()!=null && tableData.getWholeCol()!=-1 && tableData.getWholeCol()!=0) {
			start=clusterManager.getPhaseCollectors().get(0).getPhases().get(tableData.getWholeCol()-1).getStartPos();
			end=clusterManager.getPhaseCollectors().get(0).getPhases().get(tableData.getWholeCol()-1).getEndPos();
		}


		
		if(tableData.getWholeCol()!=-1){
			for(int i=0; i<zoomAreaTable.getColumnCount(); i++){
				if(!(zoomAreaTable.getColumnName(i).equals("Table name"))){
					if(Integer.parseInt(zoomAreaTable.getColumnName(i))>=start && Integer.parseInt(zoomAreaTable.getColumnName(i))<=end){
			
						zoomAreaTable.getColumnModel().getColumn(i).setHeaderRenderer(new IDUHeaderTableRenderer());
			
					}
				}
			}
		}
		
		final IDUTableRenderer renderer = new IDUTableRenderer(descriptionText,tableData.getFinalRowsZoomArea(), tableManager, tableData.getSegmentSize());
		//generalTable.setDefaultRenderer(Object.class, renderer);
		
		

		zoomAreaTable.setDefaultRenderer(Object.class, new GeneralTableIDUDefaultTableCellRenderer(tableData, tableManager, clusterManager, descriptionText));
		
		zoomAreaTable.addMouseListener(new GeneralTableIDUMouseAdapter(tableData, zoomAreaTable, zoomAreaTable, renderer));
		zoomAreaTable.getTableHeader().addMouseListener(new GeneralTableIDUHeaderMouseAdapter(tableData, zoomAreaTable,renderer));
		
		//tableData.setZoomAreaTable(generalTable);
		tmpScrollPaneZoomArea.setViewportView(zoomAreaTable);
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
	LifeTimeTable = new JvTable(generalModel);
	
	
	LifeTimeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	
	LifeTimeTable.setShowGrid(false);
	LifeTimeTable.setIntercellSpacing(new Dimension(0, 0));

	
	
	for(int i=0; i<LifeTimeTable.getColumnCount(); i++){
		if(i==0){
			LifeTimeTable.getColumnModel().getColumn(0).setPreferredWidth(86);
		}
		else{
			
			LifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(1);
		}
	}
	
	LifeTimeTable.setDefaultRenderer(Object.class, new GeneralTablePhasesDefaultTableCellRenderer(tableData, tableManager, clusterManager, descriptionText));

	LifeTimeTable.addMouseListener(new MouseAdapter() {


		@Override
		   public void mouseClicked(MouseEvent e) {
			
			if (e.getClickCount() == 1) {
				JTable target = (JTable)e.getSource();
		         
		         tableData.setSelectedRowsFromMouse(target.getSelectedRows());
		         tableData.setSelectedColumn(target.getSelectedColumn());
		         LifeTimeTable.repaint();
			}

		   }
	});
	
	LifeTimeTable.addMouseListener(new GeneralTablePhasesMouseAdapter(tableData, LifeTimeTable, showDetails, tableManager, clusterManager));

	
	LifeTimeTable.getTableHeader().addMouseListener(new MouseAdapter() {

	    @Override
	    public void mouseClicked(MouseEvent e) {
	        tableData.setWholeCol(LifeTimeTable.columnAtPoint(e.getPoint()));
	        String name = LifeTimeTable.getColumnName(tableData.getWholeCol());
	        System.out.println("Column index selected " + tableData.getWholeCol() + " " + name);
	        LifeTimeTable.repaint();

	        if (tableData.isShowingPld()) {
		        makeGeneralTableIDU(tableData);
			}
	    }
	});
	
	LifeTimeTable.getTableHeader().addMouseListener(new MouseAdapter() {

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
				            	LifeTimeTable.repaint();

				            	if(tableData.isShowingPld()){
				            		makeGeneralTableIDU(tableData);
				            	}
				            }
				        });
				        popupMenu.add(clearColumnSelectionItem);
				        JMenuItem showDetailsItem = new JMenuItem("Show Details for this Phase");
				        
				        
				        
				        showDetailsItem.addActionListener(new TableHeaderShowDetailsActionListener(tableData, tableManager, clusterManager, showDetails, LifeTimeTable));
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(LifeTimeTable, e.getX(),e.getY());

				    
			}
		
	   }
	    
	});
	
	
	//tableData.setLifeTimeTable(generalTable);
	
	tmpScrollPane.setViewportView(LifeTimeTable);
	tmpScrollPane.setAlignmentX(0);
	tmpScrollPane.setAlignmentY(0);
    tmpScrollPane.setBounds(300,30,950,265);
    tmpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    tmpScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
	//lifeTimePanel.setCursor(getCursor());
	lifeTimePanel.add(tmpScrollPane);
    
	
	
}

	public JvTable getZoomAreaTable() {
		return zoomAreaTable;
	}
	public void setZoomAreaTable(JvTable zoomAreaTable) {
		this.zoomAreaTable = zoomAreaTable;
	}
	
	public JvTable getLifeTimeTable() {
			return LifeTimeTable;
	}
	public void setLifeTimeTable(JvTable lifeTimeTable) {
		LifeTimeTable = lifeTimeTable;
	}


	public void makeDetailedTable(String[] columns , String[][] rows, final boolean levelized, final TableData tableData){
		
		MyTableModel detailedModel=new MyTableModel(columns,rows);
		
		final JvTable tmpLifeTimeTable= new JvTable(detailedModel);
		
		tmpLifeTimeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		if(levelized==true){
			for(int i=0; i<tmpLifeTimeTable.getColumnCount(); i++){
				if(i==0){
					tmpLifeTimeTable.getColumnModel().getColumn(0).setPreferredWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMaxWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMinWidth(150);
				}
				else{
					if(tmpLifeTimeTable.getColumnName(i).contains("v")){
						tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(100);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(100);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(100);
					}
					else{
						tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(25);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(25);
						tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(25);
					}
				}
			}
		}
		else{
			for(int i=0; i<tmpLifeTimeTable.getColumnCount(); i++){
				if(i==0){
					tmpLifeTimeTable.getColumnModel().getColumn(0).setPreferredWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMaxWidth(150);
					tmpLifeTimeTable.getColumnModel().getColumn(0).setMinWidth(150);
				}
				else{
					
					tmpLifeTimeTable.getColumnModel().getColumn(i).setPreferredWidth(20);
					tmpLifeTimeTable.getColumnModel().getColumn(i).setMaxWidth(20);
					tmpLifeTimeTable.getColumnModel().getColumn(i).setMinWidth(20);
					
				}
			}
		}
		
		tmpLifeTimeTable.setName("LifeTimeTable");
		
		
		tmpLifeTimeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        String tmpValue=(String) table.getValueAt(row, column);
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        c.setForeground(fr);
		        
		        
		        if(tableData.getSelectedColumn()==0){
		        	if (isSelected){
		        		Color cl = new Color(255,69,0, 100);

		        		c.setBackground(cl);
		        		
		        		return c;
		        	}
		        }
		        else{
		        	if (isSelected && hasFocus){
			        	
		        		c.setBackground(Color.YELLOW);
		        		return c;
			        }
		        	
		        }
		        
		        try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;
		        	
		        	if(columnName.equals("I")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0 && numericValue<=tableData.getSegmentSizeDetailedTable()[0]){
		        			
		        			insersionColor=new Color(193,255,193);
			        	}
		        		else if(numericValue>tableData.getSegmentSizeDetailedTable()[0] && numericValue<=2*tableData.getSegmentSizeDetailedTable()[0]){
		        			insersionColor=new Color(84,255,159);
		        		}
		        		else if(numericValue>2*tableData.getSegmentSizeDetailedTable()[0] && numericValue<=3*tableData.getSegmentSizeDetailedTable()[0]){
		        			
		        			insersionColor=new Color(0,201,87);
		        		}
		        		else{
		        			insersionColor=new Color(0,100,0);
		        		}
		        		c.setBackground(insersionColor);
		        	}
		        	
		        	if(columnName.equals("U")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0 && numericValue<=tableData.getSegmentSizeDetailedTable()[1]){
		        			
		        			insersionColor=new Color(176,226,255);
			        	}
		        		else if(numericValue>tableData.getSegmentSizeDetailedTable()[1] && numericValue<=2*tableData.getSegmentSizeDetailedTable()[1]){
		        			insersionColor=new Color(92,172,238);
		        		}
		        		else if(numericValue>2*tableData.getSegmentSizeDetailedTable()[1] && numericValue<=3*tableData.getSegmentSizeDetailedTable()[1]){
		        			
		        			insersionColor=new Color(28,134,238);
		        		}
		        		else{
		        			insersionColor=new Color(16,78,139);
		        		}
		        		c.setBackground(insersionColor);
		        	}
		        	
		        	if(columnName.equals("D")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0 && numericValue<=tableData.getSegmentSizeDetailedTable()[2]){
		        			
		        			insersionColor=new Color(255,106,106);
			        	}
		        		else if(numericValue>tableData.getSegmentSizeDetailedTable()[2] && numericValue<=2*tableData.getSegmentSizeDetailedTable()[2]){
		        			insersionColor=new Color(255,0,0);
		        		}
		        		else if(numericValue>2*tableData.getSegmentSizeDetailedTable()[2] && numericValue<=3*tableData.getSegmentSizeDetailedTable()[2]){
		        			
		        			insersionColor=new Color(205,0,0);
		        		}
		        		else{
		        			insersionColor=new Color(139,0,0);
		        		}
		        		c.setBackground(insersionColor);
		        	}
		        	
		        	return c;
		        }
		        catch(Exception e){
		        		
		        		if(tmpValue.equals("")){
		        			c.setBackground(Color.black);
			        		return c; 
		        		}
		        		else{
		        			if(columnName.contains("v")){
		        				c.setBackground(Color.lightGray);
		        				if(levelized==false){
		        					setToolTipText(columnName);
		        				}
		        			}
		        			else{
		        				Color tableNameColor=new Color(205,175,149);
		        				c.setBackground(tableNameColor);
		        			}
			        		return c; 
		        		}
		        		
		        		
		        }
		    }
		});
		
		tmpLifeTimeTable.setOpaque(true);
	    
		tmpLifeTimeTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    tmpLifeTimeTable.getSelectionModel().addListSelectionListener(new RowListener(tableData));
	    tmpLifeTimeTable.getColumnModel().getSelectionModel().addListSelectionListener(new ColumnListener());
	    
	    
	    
	    JScrollPane detailedScrollPane=new JScrollPane();
	    detailedScrollPane.setViewportView(tmpLifeTimeTable);
	    detailedScrollPane.setAlignmentX(0);
	    detailedScrollPane.setAlignmentY(0);
	    detailedScrollPane.setBounds(0,0,1280,650);
	    detailedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    detailedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
       
	    //detailedScrollPane.setCursor(getCursor());
	    
	    JDialog detailedDialog=new JDialog();
	    detailedDialog.setBounds(100, 100, 1300, 700);
	    
	    JPanel panelToAdd=new JPanel();
	    
	    GroupLayout gl_panel = new GroupLayout(panelToAdd);
	    gl_panel.setHorizontalGroup(
	    		gl_panel.createParallelGroup(Alignment.LEADING)
		);
	    gl_panel.setVerticalGroup(
	    		gl_panel.createParallelGroup(Alignment.LEADING)
		);
		panelToAdd.setLayout(gl_panel);
	    
	    panelToAdd.add(detailedScrollPane);
	    detailedDialog.getContentPane().add(panelToAdd);
	    detailedDialog.setVisible(true);
		
		
	}
	
	private class RowListener implements ListSelectionListener {
		private TableData tableData;
		
		public RowListener(TableData tableData)
		{
			super();
			this.tableData = tableData;
		}
		
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            
            int selectedRow=LifeTimeTable.getSelectedRow();
            
            tableData.getSelectedRows().add(selectedRow);
     
        }
    }
	
	private class ColumnListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
           
        }
    }

	




}
