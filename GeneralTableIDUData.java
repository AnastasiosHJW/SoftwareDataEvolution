package data.dataKeeper;

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
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataSorters.PldRowSorter;
import gui.mainEngine.Gui;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableRenderers.IDUHeaderTableRenderer;
import gui.tableElements.tableRenderers.IDUTableRenderer;

public class GeneralTableIDUData {
	
	
	public void makeGeneralTableIDU(final TableData tableData, final GlobalDataKeeper globalDataKeeper, final JTextArea descriptionText) {
	
		PldRowSorter sorter=new PldRowSorter(tableData.getFinalRowsZoomArea(),globalDataKeeper);
		
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
		
		final JvTable generalTable=new JvTable(zoomModel);
		
		generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		if(tableData.getRowHeight()<1){
			tableData.setRowHeight(1);
		}
		if (tableData.getColumnWidth()<1) {
			tableData.setColumnWidth(1);
		}
		
		for(int i=0; i<generalTable.getRowCount(); i++){
				generalTable.setRowHeight(i, tableData.getRowHeight());
				
		}

		
		generalTable.setShowGrid(false);
		generalTable.setIntercellSpacing(new Dimension(0, 0));
		
		
		
		for(int i=0; i<generalTable.getColumnCount(); i++){
			if(i==0){
				generalTable.getColumnModel().getColumn(0).setPreferredWidth(tableData.getColumnWidth());
				
			}
			else{
				generalTable.getColumnModel().getColumn(i).setPreferredWidth(tableData.getColumnWidth());
				
			}
		}
		
		int start=-1;
		int end=-1;
		if (globalDataKeeper.getPhaseCollectors()!=null && tableData.getWholeCol()!=-1 && tableData.getWholeCol()!=0) {
			start=globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(tableData.getWholeCol()-1).getStartPos();
			end=globalDataKeeper.getPhaseCollectors().get(0).getPhases().get(tableData.getWholeCol()-1).getEndPos();
		}


		
		if(tableData.getWholeCol()!=-1){
			for(int i=0; i<generalTable.getColumnCount(); i++){
				if(!(generalTable.getColumnName(i).equals("Table name"))){
					if(Integer.parseInt(generalTable.getColumnName(i))>=start && Integer.parseInt(generalTable.getColumnName(i))<=end){
			
						generalTable.getColumnModel().getColumn(i).setHeaderRenderer(new IDUHeaderTableRenderer());
			
					}
				}
			}
		}
		
		final IDUTableRenderer renderer = new IDUTableRenderer(Gui.this,tableData.getFinalRowsZoomArea(), globalDataKeeper, tableData.getSegmentSize());
		//generalTable.setDefaultRenderer(Object.class, renderer);
		
		
		
		generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
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
		        setOpaque(true);
		      
		        if(column==tableData.getWholeColZoomArea() && tableData.getWholeColZoomArea()!=0){
		        	
		        	String description="Transition ID:"+table.getColumnName(column)+"\n";
		        	description=description+"Old Version Name:"+globalDataKeeper.getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
	        		description=description+"New Version Name:"+globalDataKeeper.getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
	        		
        			description=description+"Transition Changes:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfChangesForOneTr()+"\n";
        			description=description+"Additions:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfAdditionsForOneTr()+"\n";
        			description=description+"Deletions:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfDeletionsForOneTr()+"\n";
        			description=description+"Updates:"+globalDataKeeper.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfUpdatesForOneTr()+"\n";

        			
	        		descriptionText.setText(description);
		        	
		        	Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
		        }
		        else if(tableData.getSelectedColumnZoomArea()==0){
		    		
		        	if (isSelected){
		        		Color cl = new Color(255,69,0,100);
		        		c.setBackground(cl);
		        		
		        		String description="Table:"+tableData.getFinalRowsZoomArea()[row][0]+"\n";
		        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getBirth()+"\n";
		        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getBirthVersionID()+"\n";
		        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getDeath()+"\n";
		        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getDeathVersionID()+"\n";
		        		description=description+"Total Changes:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getTotalChanges()+"\n";

		        		
		        		descriptionText.setText(description);
		        		
		        		return c;
		        		
		        		
		        	}
		        }
		        else{


		        	if(tableData.getSelectedFromTree().contains(tableData.getFinalRowsZoomArea()[row][0])){


		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		
		        		return c;
		        	}
		        	
			      
		        	
		        	if (isSelected && hasFocus){

		        		String description="";
		        		if(!table.getColumnName(column).contains("Table name")){
			        		description="Table:"+tableData.getFinalRowsZoomArea()[row][0]+"\n";
			        		
			        		description=description+"Old Version Name:"+globalDataKeeper.getAllPPLTransitions().
			        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
			        		description=description+"New Version Name:"+globalDataKeeper.getAllPPLTransitions().
			        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
			        		if(globalDataKeeper.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
			        			description=description+"Transition Changes:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).
			        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
			        			description=description+"Additions:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).
			        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			description=description+"Deletions:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).
			        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
			        			description=description+"Updates:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).
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
	        			insersionColor=new Color(154,205,50,200);
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
	        			c.setBackground(Color.GRAY);
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
				
		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 1) {
					JTable target = (JTable)e.getSource();
			         
					 tableData.setSelectedRowsFromMouse(target.getSelectedRows());
					 tableData.setSelectedColumnZoomArea(target.getSelectedColumn());
			         renderer.setSelCol(tableData.getSelectedColumnZoomArea());
			         target.getSelectedColumns();
			         
			         zoomAreaTable.repaint();
				}
				
			  }
		});
		
		generalTable.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseReleased(MouseEvent e) {
				
					if(SwingUtilities.isRightMouseButton(e)){
						System.out.println("Right Click");

						JTable target1 = (JTable)e.getSource();
						target1.getSelectedColumns();
						tableData.setSelectedRowsFromMouse(target1.getSelectedRows());
						System.out.println(target1.getSelectedColumns().length);
						System.out.println(target1.getSelectedRow());
						for(int rowsSelected=0; rowsSelected<tableData.getSelectedRowsFromMouse().length; rowsSelected++){
							System.out.println(generalTable.getValueAt(tableData.getSelectedRowsFromMouse()[rowsSelected], 0));
						}
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
				            	tableData.setSelectedFromTree(new ArrayList<String>());
				            	zoomAreaTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(generalTable, e.getX(),e.getY());
						        						        
						    
					}
				
			   }
		});
		
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	tableData.setWholeColZoomArea(generalTable.columnAtPoint(e.getPoint()));
		        renderer.setWholeCol(generalTable.columnAtPoint(e.getPoint()));
		        //String name = generalTable.getColumnName(wholeColZoomArea);
		        //System.out.println("Column index selected " + wholeColZoomArea + " " + name);
		        generalTable.repaint();
		    }
		});
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
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
							        renderer.setWholeCol(tableData.getWholeColZoomArea());

					            	generalTable.repaint();
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(generalTable, e.getX(),e.getY());
					    
				}
			
		   }
		    
		});
		
		zoomAreaTable=generalTable;
		tmpScrollPaneZoomArea.setViewportView(zoomAreaTable);
		tmpScrollPaneZoomArea.setAlignmentX(0);
		tmpScrollPaneZoomArea.setAlignmentY(0);
		tmpScrollPaneZoomArea.setBounds(300,300,950,250);
		tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
		lifeTimePanel.setCursor(getCursor());
		lifeTimePanel.add(tmpScrollPaneZoomArea);
		
		
		
	}


}
