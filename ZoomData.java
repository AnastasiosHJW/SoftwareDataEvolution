package gui.mainEngine;

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
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataKeeper.TableData;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableConstructors.PldConstruction;
import gui.tableElements.tableConstructors.TableConstructionClusterTablesPhasesZoomA;
import gui.tableElements.tableConstructors.TableConstructionZoomArea;

public class ZoomData {
	
	TableData tableData;
	

public void showClusterSelectionToZoomArea(int selectedColumn,String selectedCluster){

	
	ArrayList<String> tablesOfCluster=new ArrayList<String>();
	for(int i=0; i <tablesSelected.size(); i++){
		String[] selectedClusterSplit= tablesSelected.get(i).split(" ");
		int cluster=Integer.parseInt(selectedClusterSplit[1]);
		ArrayList<String> namesOfTables=globalDataKeeper.getClusterCollectors().get(0).getClusters().get(cluster).getNamesOfTables();
		for(int j=0; j<namesOfTables.size(); j++){
			tablesOfCluster.add(namesOfTables.get(j));
		}
		System.out.println(tablesSelected.get(i));
	}
	
	PldConstruction table;
	if (selectedColumn==0) {
		table= new TableConstructionClusterTablesPhasesZoomA(globalDataKeeper,tablesOfCluster);
	}
	else{
		table= new TableConstructionZoomArea(globalDataKeeper,tablesOfCluster,selectedColumn);
	}
	final String[] columns=table.constructColumns();
	final String[][] rows=table.constructRows();
	segmentSizeZoomArea = table.getSegmentSize();
	System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
	System.out.println("C: "+columns.length+" R: "+rows.length);

	finalColumnsZoomArea=columns;
	finalRowsZoomArea=rows;
	tabbedPane.setSelectedIndex(0);
	makeZoomAreaTableForCluster();
	
	
}

	public void makeZoomAreaTable() {
	showingPld=false;
	int numberOfColumns=finalRowsZoomArea[0].length;
	int numberOfRows=finalRowsZoomArea.length;
	
	
	final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];
	
	for(int i=0; i<numberOfRows; i++){
		
		rowsZoom[i][0]=finalRowsZoomArea[i][0];
		
	}
	
	zoomModel=new MyTableModel(finalColumnsZoomArea, rowsZoom);
	
	final JvTable zoomTable=new JvTable(zoomModel);
	
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
	        
	        
	        
	        String tmpValue=finalRowsZoomArea[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);
	        
	        if(column==wholeColZoomArea){
	        	
	        	String description="Transition ID:"+table.getColumnName(column)+"\n";
	        	description=description+"Old Version Name:"+globalDataKeeper.getAllPPLTransitions().
        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
        		description=description+"New Version Name:"+globalDataKeeper.getAllPPLTransitions().
        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
        		
    			description=description+"Transition Changes:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterChangesForOneTr(rowsZoom)+"\n";
    			description=description+"Additions:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterAdditionsForOneTr(rowsZoom)+"\n";
    			description=description+"Deletions:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterDeletionsForOneTr(rowsZoom)+"\n";
    			description=description+"Updates:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfClusterUpdatesForOneTr(rowsZoom)+"\n";

    		
	        	
	        	descriptionText.setText(description);
	        	Color cl = new Color(255,69,0,100);
        		c.setBackground(cl);
        		
        		return c;
	        }
	        else if(selectedColumnZoomArea==0){
	        	if (isSelected){
	        		String description="Table:"+finalRowsZoomArea[row][0]+"\n";
	        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getBirth()+"\n";
	        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getBirthVersionID()+"\n";
	        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getDeath()+"\n";
	        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getDeathVersionID()+"\n";
	        		description=description+"Total Changes:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
	        				getTotalChangesForOnePhase(Integer.parseInt(table.getColumnName(1)), Integer.parseInt(table.getColumnName(table.getColumnCount()-1)))+"\n";
	        		descriptionText.setText(description);
	        		
	        		Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
	        	}
	        }
	        else{
	        	if (isSelected && hasFocus){
		        	
	        		String description="";
	        		if(!table.getColumnName(column).contains("Table name")){
		        		description="Table:"+finalRowsZoomArea[row][0]+"\n";
		        		
		        		description=description+"Old Version Name:"+globalDataKeeper.getAllPPLTransitions().
		        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
		        		description=description+"New Version Name:"+globalDataKeeper.getAllPPLTransitions().
		        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
		        		if(globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
		        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
		        			description=description+"Transition Changes:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
		        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
		        			description=description+"Additions:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
		        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
		        			description=description+"Deletions:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
		        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
		        			description=description+"Updates:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).
		        					getNumberOfUpdatesForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
		        			
		        		}
		        		else{
		        			description=description+"Transition Changes:0"+"\n";
		        			description=description+"Additions:0"+"\n";
		        			description=description+"Deletions:0"+"\n";
		        			description=description+"Updates:0"+"\n";
		        			
		        		}
		        		
		        		descriptionText.setText(description);
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
        		else if(numericValue> 0&& numericValue<=segmentSizeZoomArea[3]){
        			
        			insersionColor=new Color(176,226,255);
	        	}
        		else if(numericValue>segmentSizeZoomArea[3] && numericValue<=2*segmentSizeZoomArea[3]){
        			insersionColor=new Color(92,172,238);
        		}
        		else if(numericValue>2*segmentSizeZoomArea[3] && numericValue<=3*segmentSizeZoomArea[3]){
        			
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
		         
		         selectedRowsFromMouse = target.getSelectedRows();
		         selectedColumnZoomArea = target.getSelectedColumn();
		         zoomAreaTable.repaint();
			}
		    
		   }
	});
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {
			
				if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

					JTable target1 = (JTable)e.getSource();
					selectedColumnZoomArea=target1.getSelectedColumn();
					selectedRowsFromMouse=target1.getSelectedRows();
					System.out.println(target1.getSelectedColumn());
					System.out.println(target1.getSelectedRow());
					final ArrayList<String> tablesSelected = new ArrayList<String>();
					for(int rowsSelected=0; rowsSelected<selectedRowsFromMouse.length; rowsSelected++){
						tablesSelected.add((String) zoomTable.getValueAt(selectedRowsFromMouse[rowsSelected], 0));
						System.out.println(tablesSelected.get(rowsSelected));
					}
						
					
				}
			
		   }
	});
	
	// listener
	zoomTable.getTableHeader().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	    	wholeColZoomArea = zoomTable.columnAtPoint(e.getPoint());
	        String name = zoomTable.getColumnName(wholeColZoomArea);
	        System.out.println("Column index selected " + wholeCol + " " + name);
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
				            	wholeColZoomArea=-1;
				            	zoomTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(zoomTable, e.getX(),e.getY());
				    
			}
		
	   }
	    
	});
	
	
	zoomAreaTable=zoomTable;
	
	tmpScrollPaneZoomArea.setViewportView(zoomAreaTable);
	tmpScrollPaneZoomArea.setAlignmentX(0);
	tmpScrollPaneZoomArea.setAlignmentY(0);
	tmpScrollPaneZoomArea.setBounds(300,300,950,250);
	tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	

	lifeTimePanel.setCursor(getCursor());
	lifeTimePanel.add(tmpScrollPaneZoomArea);
	
	
	
}

public void makeZoomAreaTableForCluster() {
	showingPld=false;
	int numberOfColumns=finalRowsZoomArea[0].length;
	int numberOfRows=finalRowsZoomArea.length;
	undoButton.setVisible(true);
	
	final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];
	
	for(int i=0; i<numberOfRows; i++){
		
		rowsZoom[i][0]=finalRowsZoomArea[i][0];
		
	}
	
	zoomModel=new MyTableModel(finalColumnsZoomArea, rowsZoom);
	
	final JvTable zoomTable=new JvTable(zoomModel);
	
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
	        
	        
	        
	        String tmpValue=finalRowsZoomArea[row][column];
	        String columnName=table.getColumnName(column);
	        Color fr=new Color(0,0,0);
	        c.setForeground(fr);
	        
	        if(column==wholeColZoomArea && wholeColZoomArea!=0){
	        	
	        	String description=table.getColumnName(column)+"\n";
	          	description=description+"First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getStartPos()+"\n";
        		description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getEndPos()+"\n";
        		description=description+"Total Changes For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalUpdates()+"\n";
        		description=description+"Additions For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalAdditionsOfPhase()+"\n";
        		description=description+"Deletions For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalDeletionsOfPhase()+"\n";
        		description=description+"Updates For This Phase:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
        				get(column-1).getTotalUpdatesOfPhase()+"\n";
	        	
        		descriptionText.setText(description);
	        	
	        	Color cl = new Color(255,69,0,100);

        		c.setBackground(cl);
        		return c;
	        }
	        else if(selectedColumnZoomArea==0){
	        	if (isSelected){
	        		
	        		
		        		String description="Table:"+finalRowsZoomArea[row][0]+"\n";
		        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getBirth()+"\n";
		        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getBirthVersionID()+"\n";
		        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getDeath()+"\n";
		        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getDeathVersionID()+"\n";
		        		description=description+"Total Changes:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getTotalChanges()+"\n";
		        		descriptionText.setText(description);

	        	
	        		
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
		        		
	        			description=description+"Old Version:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
		        		description=description+"New Version:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n\n";
		
	        			//description=description+"First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
		        				//get(column-1).getStartPos()+"\n";
		        		//description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
		        			//	get(column-1).getEndPos()+"\n\n";
	        			description=description+"Table:"+finalRowsZoomArea[row][0]+"\n";
		        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getBirth()+"\n";
		        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getBirthVersionID()+"\n";
		        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getDeath()+"\n";
		        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(finalRowsZoomArea[row][0]).getDeathVersionID()+"\n";
		        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
		        		
		        		descriptionText.setText(description);

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
        		else if(numericValue> 0&& numericValue<=segmentSizeZoomArea[3]){
        			
        			insersionColor=new Color(176,226,255);
	        	}
        		else if(numericValue>segmentSizeZoomArea[3] && numericValue<=2*segmentSizeZoomArea[3]){
        			insersionColor=new Color(92,172,238);
        		}
        		else if(numericValue>2*segmentSizeZoomArea[3] && numericValue<=3*segmentSizeZoomArea[3]){
        			
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
		         
		         selectedRowsFromMouse = target.getSelectedRows();
		         selectedColumnZoomArea = target.getSelectedColumn();
		         zoomAreaTable.repaint();
			}
		   
		   }
	});
	
	zoomTable.addMouseListener(new MouseAdapter() {
		@Override
		   public void mouseReleased(MouseEvent e) {
			
				if(SwingUtilities.isRightMouseButton(e)){
					System.out.println("Right Click");

						JTable target1 = (JTable)e.getSource();
						selectedColumnZoomArea=target1.getSelectedColumn();
						selectedRowsFromMouse=target1.getSelectedRows();
						System.out.println(target1.getSelectedColumn());
						System.out.println(target1.getSelectedRow());
						
						tablesSelected = new ArrayList<String>();

						for(int rowsSelected=0; rowsSelected<selectedRowsFromMouse.length; rowsSelected++){
							tablesSelected.add((String) zoomTable.getValueAt(selectedRowsFromMouse[rowsSelected], 0));
							System.out.println(tablesSelected.get(rowsSelected));
						}
		            	if (zoomTable.getColumnName(selectedColumnZoomArea).contains("Phase")) {

							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show Details");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
				            		firstLevelUndoColumnsZoomArea=finalColumnsZoomArea;
					            	firstLevelUndoRowsZoomArea=finalRowsZoomArea;
				            		showSelectionToZoomArea(selectedColumnZoomArea);
									
					            	
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
	    	wholeColZoomArea = zoomTable.columnAtPoint(e.getPoint());
	        String name = zoomTable.getColumnName(wholeColZoomArea);
	        System.out.println("Column index selected " + wholeCol + " " + name);
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
				            	wholeColZoomArea=-1;
				            	zoomTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(zoomTable, e.getX(),e.getY());
				    
			}
		
	   }
	    
	});
	
	
	zoomAreaTable=zoomTable;
	
	tmpScrollPaneZoomArea.setViewportView(zoomAreaTable);
	tmpScrollPaneZoomArea.setAlignmentX(0);
	tmpScrollPaneZoomArea.setAlignmentY(0);
	tmpScrollPaneZoomArea.setBounds(300,300,950,250);
	tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	

	lifeTimePanel.setCursor(getCursor());
	lifeTimePanel.add(tmpScrollPaneZoomArea);
	
	
	
}


public void showSelectionToZoomArea(int selectedColumn){
	
	TableConstructionZoomArea table=new TableConstructionZoomArea(globalDataKeeper,tablesSelected,selectedColumn);
	final String[] columns=table.constructColumns();
	final String[][] rows=table.constructRows();
	segmentSizeZoomArea = table.getSegmentSize();

	System.out.println("Schemas: "+globalDataKeeper.getAllPPLSchemas().size());
	System.out.println("C: "+columns.length+" R: "+rows.length);

	finalColumnsZoomArea=columns;
	finalRowsZoomArea=rows;
	tabbedPane.setSelectedIndex(0);
	makeZoomAreaTable();
	
	
	
}


}
