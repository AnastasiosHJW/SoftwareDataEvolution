package gui.listeners.tables;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
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

public abstract class ShowDetailsActionListener{
	
	protected TableData tableData;
	private TableManager tableManager;
	private ClusterManager clusterManager;

	private ShowDetailsComponents showDetails;


	public ShowDetailsActionListener(TableData tableData, TableManager tableManager, ClusterManager clusterManager,
			ShowDetailsComponents showDetails) {
		this.tableData = tableData;
		this.tableManager = tableManager;
		this.clusterManager = clusterManager;
		this.showDetails = showDetails;
	}


	

public void showClusterSelectionToZoomArea(int selectedColumn, String selectedRow)
{
	//int selectedColumn = tableData.getSelectedColumn();
	String selectedCluster = selectedRow;
	
	ArrayList<String> tablesOfCluster=new ArrayList<String>();
	for(int i=0; i <tableData.getTablesSelected().size(); i++){
		String[] selectedClusterSplit= tableData.getTablesSelected().get(i).split(" ");
		int cluster=Integer.parseInt(selectedClusterSplit[1]);
		ArrayList<String> namesOfTables=clusterManager.getClusterCollectors().get(0).getClusters().get(cluster).getNamesOfTables();
		for(int j=0; j<namesOfTables.size(); j++){
			tablesOfCluster.add(namesOfTables.get(j));
		}
		System.out.println(tableData.getTablesSelected().get(i));
	}
	
	PldConstruction table;
	if (selectedColumn==0) {
		table= new TableConstructionClusterTablesPhasesZoomA(tableManager, clusterManager,tablesOfCluster);
	}
	else{
		table= new TableConstructionZoomArea(tableManager, clusterManager,tablesOfCluster,selectedColumn);
	}
	final String[] columns=table.constructColumns();
	final String[][] rows=table.constructRows();
	tableData.setSegmentSizeZoomArea(table.getSegmentSize());
	System.out.println("Schemas: "+tableManager.getAllPPLSchemas().size());
	System.out.println("C: "+columns.length+" R: "+rows.length);

	tableData.setFinalColumnsZoomArea(columns);
	tableData.setFinalRowsZoomArea(rows);
	showDetails.getTabbedPane().setSelectedIndex(0);
	makeZoomAreaTableForCluster(tableData, showDetails.getTabbedPane());
	
	
}


public void showSelectionToZoomArea(int selectedColumn){
	
	TableConstructionZoomArea table=new TableConstructionZoomArea(tableManager, clusterManager,tableData.getTablesSelected(),selectedColumn);
	final String[] columns=table.constructColumns();
	final String[][] rows=table.constructRows();
	tableData.setSegmentSizeZoomArea(table.getSegmentSize());

	System.out.println("Schemas: "+tableManager.getAllPPLSchemas().size());
	System.out.println("C: "+columns.length+" R: "+rows.length);

	tableData.setFinalColumnsZoomArea(columns);
	tableData.setFinalRowsZoomArea(rows);
	showDetails.getTabbedPane().setSelectedIndex(0);
	makeZoomAreaTable(tableData);
	
	
	
}


public void makeZoomAreaTable(final TableData tableData) {
	tableData.setShowingPld(false);
	int numberOfColumns=tableData.getFinalRowsZoomArea()[0].length;
	int numberOfRows=tableData.getFinalRowsZoomArea().length;
	
	
	final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];
	
	for(int i=0; i<numberOfRows; i++){
		
		rowsZoom[i][0]=tableData.getFinalRowsZoomArea()[i][0];
		
	}
	
	showDetails.setZoomModel(new MyTableModel(tableData.getFinalColumnsZoomArea(), rowsZoom));
	
	final JvTable zoomTable=new JvTable(showDetails.getZoomModel());
	
	zoomTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	

	zoomTable.setShowGrid(false);
	zoomTable.setIntercellSpacing(new Dimension(0, 0));
	
	
	for(int i=0; i<zoomTable.getColumnCount(); i++){
		if(i==0){
			zoomTable.getColumnModel().getColumn(0).setPreferredWidth(150);
			
		}
		else{
			
			
			zoomTable.getColumnModel().getColumn(i).setPreferredWidth(20);
			zoomTable.getColumnModel().getColumn(i).setMaxWidth(20);
			zoomTable.getColumnModel().getColumn(i).setMinWidth(20);
		}
	}
	
	zoomTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
	{
	    
		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
	        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        
	        
	        
	        String tmpValue=tableData.getFinalRowsZoomArea()[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);
	        
	        if(column==tableData.getWholeColZoomArea()){
	        	
	        	String description="Transition ID:"+table.getColumnName(column)+"\n";
	        	description=description+"Old Version Name:"+tableManager.getAllPPLTransitions().
        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
        		description=description+"New Version Name:"+tableManager.getAllPPLTransitions().
        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
        		
    			description=description+"Transition Changes:"+tableManager.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterChangesForOneTr(rowsZoom)+"\n";
    			description=description+"Additions:"+tableManager.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterAdditionsForOneTr(rowsZoom)+"\n";
    			description=description+"Deletions:"+tableManager.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterDeletionsForOneTr(rowsZoom)+"\n";
    			description=description+"Updates:"+tableManager.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterUpdatesForOneTr(rowsZoom)+"\n";

    		
	        	
    			showDetails.getDescriptionText().setText(description);
	        	Color cl = new Color(255,69,0,100);
        		c.setBackground(cl);
        		
        		return c;
	        }
	        else if(tableData.getSelectedColumnZoomArea()==0){
	        	if (isSelected){
	        		String description="Table:"+tableData.getFinalRowsZoomArea()[row][0]+"\n";
	        		description=description+"Birth Version Name:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getBirth()+"\n";
	        		description=description+"Birth Version ID:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getBirthVersionID()+"\n";
	        		description=description+"Death Version Name:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getDeath()+"\n";
	        		description=description+"Death Version ID:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getDeathVersionID()+"\n";
	        		description=description+"Total Changes:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).
	        				getTotalChangesForOnePhase(Integer.parseInt(table.getColumnName(1)), Integer.parseInt(table.getColumnName(table.getColumnCount()-1)))+"\n";
	        		showDetails.getDescriptionText().setText(description);
	        		
	        		Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
	        	}
	        }
	        else{
	        	if (isSelected && hasFocus){
		        	
	        		String description="";
	        		if(!table.getColumnName(column).contains("Table name")){
		        		description="Table:"+tableData.getFinalRowsZoomArea()[row][0]+"\n";
		        		
		        		description=description+"Old Version Name:"+tableManager.getAllPPLTransitions().
		        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
		        		description=description+"New Version Name:"+tableManager.getAllPPLTransitions().
		        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
		        		if(tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).
		        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
		        			description=description+"Transition Changes:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).
		        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
		        			description=description+"Additions:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).
		        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
		        			description=description+"Deletions:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).
		        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
		        			description=description+"Updates:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).
		        					getNumberOfUpdatesForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
		        			
		        		}
		        		else{
		        			description=description+"Transition Changes:0"+"\n";
		        			description=description+"Additions:0"+"\n";
		        			description=description+"Deletions:0"+"\n";
		        			description=description+"Updates:0"+"\n";
		        			
		        		}
		        		
		        		showDetails.getDescriptionText().setText(description);
	        		}
	        		
	        		Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
		        }
	        	
	        }


	        try{
	        	int numericValue=Integer.parseInt(tmpValue);
	        	Color insersionColor=null;
				setToolTipText(Integer.toString(numericValue));

	        	
        		if(numericValue==0){
        			insersionColor=new Color(0,100,0);
        		}
        		else if(numericValue> 0&& numericValue<=tableData.getSegmentSizeZoomArea()[3]){
        			
        			insersionColor=new Color(176,226,255);
	        	}
        		else if(numericValue>tableData.getSegmentSizeZoomArea()[3] && numericValue<=2*tableData.getSegmentSizeZoomArea()[3]){
        			insersionColor=new Color(92,172,238);
        		}
        		else if(numericValue>2*tableData.getSegmentSizeZoomArea()[3] && numericValue<=3*tableData.getSegmentSizeZoomArea()[3]){
        			
        			insersionColor=new Color(28,134,238);
        		}
        		else{
        			insersionColor=new Color(16,78,139);
        		}
        		c.setBackground(insersionColor);
	        	
	        	return c;
	        }
	        catch(Exception e){
	        		

	        	
        		if(tmpValue.equals("")){
        			c.setBackground(Color.DARK_GRAY);
        			return c; 
        		}
        		else{
        			if(columnName.contains("v")){
        				c.setBackground(Color.lightGray);
        				setToolTipText(columnName);
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
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseClicked(MouseEvent e) {
			
			if (e.getClickCount() == 1) {
				JTable target = (JTable)e.getSource();
		         
		         tableData.setSelectedRowsFromMouse(target.getSelectedRows());
		         tableData.setSelectedColumnZoomArea(target.getSelectedColumn());
		         showDetails.getZoomAreaTable().repaint();
			}
		    
		   }
	});
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {
			
				if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

					JTable target1 = (JTable)e.getSource();
					tableData.setSelectedColumnZoomArea(target1.getSelectedColumn());
					tableData.setSelectedRowsFromMouse(target1.getSelectedRows());
					System.out.println(target1.getSelectedColumn());
					System.out.println(target1.getSelectedRow());
					final ArrayList<String> tablesSelected = new ArrayList<String>();
					for(int rowsSelected=0; rowsSelected<tableData.getSelectedRowsFromMouse().length; rowsSelected++){
						tablesSelected.add((String) zoomTable.getValueAt(tableData.getSelectedRowsFromMouse()[rowsSelected], 0));
						System.out.println(tablesSelected.get(rowsSelected));
					}
						
					
				}
			
		   }
	});
	
	// listener
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	tableData.setWholeColZoomArea(zoomTable.columnAtPoint(e.getPoint()));
	        String name = zoomTable.getColumnName(tableData.getWholeColZoomArea());
	        System.out.println("Column index selected " + tableData.getWholeCol() + " " + name);
	        zoomTable.repaint();
	    }
	});
	
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseReleased(MouseEvent e) {
	    	if(SwingUtilities.isRightMouseButton(e)){
				System.out.println("Right Click");
				
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
				            	tableData.setWholeColZoomArea(-1);
				            	zoomTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(zoomTable, e.getX(),e.getY());
				    
			}
		
	   }
	    
	});
	
	
	showDetails.setZoomAreaTable(zoomTable);
	
	showDetails.getTmpScrollPaneZoomArea().setViewportView(showDetails.getZoomAreaTable());
	showDetails.getTmpScrollPaneZoomArea().setAlignmentX(0);
	showDetails.getTmpScrollPaneZoomArea().setAlignmentY(0);
	showDetails.getTmpScrollPaneZoomArea().setBounds(300,300,950,250);
	showDetails.getTmpScrollPaneZoomArea().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	showDetails.getTmpScrollPaneZoomArea().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	

	//lifeTimePanel.setCursor(getCursor());
	showDetails.getLifeTimePanel().add(showDetails.getTmpScrollPaneZoomArea());
	
	
	
}


public void makeZoomAreaTableForCluster(final TableData tableData, final JTabbedPane tabbedPane) {
	tableData.setShowingPld(false);
	int numberOfColumns=tableData.getFinalRowsZoomArea()[0].length;
	int numberOfRows=tableData.getFinalRowsZoomArea().length;
	showDetails.getUndoButton().setVisible(true);
	
	final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];
	
	for(int i=0; i<numberOfRows; i++){
		
		rowsZoom[i][0]=tableData.getFinalRowsZoomArea()[i][0];
		
	}
	
	showDetails.setZoomModel(new MyTableModel(tableData.getFinalColumnsZoomArea(), rowsZoom));
	
	final JvTable zoomTable=new JvTable(showDetails.getZoomModel());
	
	zoomTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	
	
	zoomTable.setShowGrid(false);
	zoomTable.setIntercellSpacing(new Dimension(0, 0));
	

	
	for(int i=0; i<zoomTable.getColumnCount(); i++){
		if(i==0){
			zoomTable.getColumnModel().getColumn(0).setPreferredWidth(150);
			
		}
		else{
			
			zoomTable.getColumnModel().getColumn(i).setPreferredWidth(10);
			zoomTable.getColumnModel().getColumn(i).setMaxWidth(10);
			zoomTable.getColumnModel().getColumn(i).setMinWidth(70);
		}
	}
	
	zoomTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
	{
	    
		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	    {
	        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        
	        
	        
	        String tmpValue=tableData.getFinalRowsZoomArea()[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);
	        
	        if(column==tableData.getWholeColZoomArea() && tableData.getWholeColZoomArea()!=0){
	        	
	        	String description=table.getColumnName(column)+"\n";
	          	description=description+"First Transition ID:"+clusterManager.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getStartPos()+"\n";
        		description=description+"Last Transition ID:"+clusterManager.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getEndPos()+"\n";
        		description=description+"Total Changes For This Phase:"+clusterManager.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalUpdates()+"\n";
        		description=description+"Additions For This Phase:"+clusterManager.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalAdditionsOfPhase()+"\n";
        		description=description+"Deletions For This Phase:"+clusterManager.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalDeletionsOfPhase()+"\n";
        		description=description+"Updates For This Phase:"+clusterManager.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalUpdatesOfPhase()+"\n";
	        	
        		showDetails.getDescriptionText().setText(description);
	        	
	        	Color cl = new Color(255,69,0,100);

        		c.setBackground(cl);
        		return c;
	        }
	        else if(tableData.getSelectedColumnZoomArea()==0){
	        	if (isSelected){
	        		
	        		
		        		String description="Table:"+tableData.getFinalRowsZoomArea()[row][0]+"\n";
		        		description=description+"Birth Version Name:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getBirth()+"\n";
		        		description=description+"Birth Version ID:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getBirthVersionID()+"\n";
		        		description=description+"Death Version Name:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getDeath()+"\n";
		        		description=description+"Death Version ID:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getDeathVersionID()+"\n";
		        		description=description+"Total Changes:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getTotalChanges()+"\n";
		        		showDetails.getDescriptionText().setText(description);

	        	
	        		
	        		Color cl = new Color(255,69,0,100);
	        		
	        		c.setBackground(cl);
	        		return c;
	        	}
	        }
	        else{
	        	
	        	
	        	if (isSelected && hasFocus){
		        	
	        		String description="";
	        		if(!table.getColumnName(column).contains("Table name")){
	        			
		        		
	        			description="Transition "+table.getColumnName(column)+"\n";
		        		
	        			description=description+"Old Version:"+tableManager.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
		        		description=description+"New Version:"+tableManager.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n\n";
		
	        			//description=description+"First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
		        				//get(column-1).getStartPos()+"\n";
		        		//description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
		        			//	get(column-1).getEndPos()+"\n\n";
	        			description=description+"Table:"+tableData.getFinalRowsZoomArea()[row][0]+"\n";
		        		description=description+"Birth Version Name:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getBirth()+"\n";
		        		description=description+"Birth Version ID:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getBirthVersionID()+"\n";
		        		description=description+"Death Version Name:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getDeath()+"\n";
		        		description=description+"Death Version ID:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getDeathVersionID()+"\n";
		        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
		        		
		        		showDetails.getDescriptionText().setText(description);

	        		}
	        		
	        		Color cl = new Color(255,69,0,100);
	        		
	        		c.setBackground(cl);
	        		return c;
		        }
	        	
	        }


	        try{
	        	int numericValue=Integer.parseInt(tmpValue);
	        	Color insersionColor=null;
				setToolTipText(Integer.toString(numericValue));

	        	
        		if(numericValue==0){
        			insersionColor=new Color(0,100,0);
        		}
        		else if(numericValue> 0&& numericValue<=tableData.getSegmentSizeZoomArea()[3]){
        			
        			insersionColor=new Color(176,226,255);
	        	}
        		else if(numericValue>tableData.getSegmentSizeZoomArea()[3] && numericValue<=2*tableData.getSegmentSizeZoomArea()[3]){
        			insersionColor=new Color(92,172,238);
        		}
        		else if(numericValue>2*tableData.getSegmentSizeZoomArea()[3] && numericValue<=3*tableData.getSegmentSizeZoomArea()[3]){
        			
        			insersionColor=new Color(28,134,238);
        		}
        		else{
        			insersionColor=new Color(16,78,139);
        		}
        		c.setBackground(insersionColor);
	        	
	        	return c;
	        }
	        catch(Exception e){
	        		

	        	
        		if(tmpValue.equals("")){
        			c.setBackground(Color.DARK_GRAY);
        			return c; 
        		}
        		else{
        			if(columnName.contains("v")){
        				c.setBackground(Color.lightGray);
        				setToolTipText(columnName);
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
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseClicked(MouseEvent e) {
			
			if (e.getClickCount() == 1) {
				JTable target = (JTable)e.getSource();
		         
		         tableData.setSelectedRowsFromMouse(target.getSelectedRows());
		         tableData.setSelectedColumnZoomArea(target.getSelectedColumn());
		         showDetails.getZoomAreaTable().repaint();
			}
		   
		   }
	});
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {
			
				if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

						JTable target1 = (JTable)e.getSource();
						tableData.setSelectedColumnZoomArea(target1.getSelectedColumn());
						tableData.setSelectedRowsFromMouse(target1.getSelectedRows());
						System.out.println(target1.getSelectedColumn());
						System.out.println(target1.getSelectedRow());
						
						tableData.setTablesSelected(new ArrayList<String>());

						for(int rowsSelected=0; rowsSelected<tableData.getSelectedRowsFromMouse().length; rowsSelected++){
							tableData.getTablesSelected().add((String) zoomTable.getValueAt(tableData.getSelectedRowsFromMouse()[rowsSelected], 0));
							System.out.println(tableData.getTablesSelected().get(rowsSelected));
						}
		            	if (zoomTable.getColumnName(tableData.getSelectedColumnZoomArea()).contains("Phase")) {

							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show Details");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
				            		tableData.setFirstLevelUndoColumnsZoomArea(tableData.getFinalColumnsZoomArea());
					            	tableData.setFirstLevelUndoRowsZoomArea(tableData.getFinalRowsZoomArea());
				            		showSelectionToZoomArea(tableData.getSelectedColumnZoomArea());
									
					            	
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(zoomTable, e.getX(),e.getY());
		            	}
					        
					
				}
			
		   }
	});
	
	// listener
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	tableData.setWholeColZoomArea(zoomTable.columnAtPoint(e.getPoint()));
	        String name = zoomTable.getColumnName(tableData.getWholeColZoomArea());
	        System.out.println("Column index selected " + tableData.getWholeCol() + " " + name);
	        zoomTable.repaint();
	    }
	});
	
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseReleased(MouseEvent e) {
	    	if(SwingUtilities.isRightMouseButton(e)){
				System.out.println("Right Click");
				
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
				            	tableData.setWholeColZoomArea(-1);
				            	zoomTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(zoomTable, e.getX(),e.getY());
				    
			}
		
	   }
	    
	});
	
	
	showDetails.setZoomAreaTable(zoomTable);
	
	showDetails.getTmpScrollPaneZoomArea().setViewportView(showDetails.getZoomAreaTable());
	showDetails.getTmpScrollPaneZoomArea().setAlignmentX(0);
	showDetails.getTmpScrollPaneZoomArea().setAlignmentY(0);
	showDetails.getTmpScrollPaneZoomArea().setBounds(300,300,950,250);
	showDetails.getTmpScrollPaneZoomArea().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	showDetails.getTmpScrollPaneZoomArea().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	

	//lifeTimePanel.setCursor(getCursor());
	showDetails.getLifeTimePanel().add(showDetails.getTmpScrollPaneZoomArea());
	
	
	
}





}
