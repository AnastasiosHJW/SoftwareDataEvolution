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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import gui.mainEngine.ZoomData;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;

public class GeneralTablePhaseData {
	
	
	private JvTable generalTable;
	private ZoomData zoomData;
	
	public GeneralTablePhaseData(MyTableModel generalModel)
	{
		generalTable=new JvTable(generalModel);
		
		generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		generalTable.setShowGrid(false);
		generalTable.setIntercellSpacing(new Dimension(0, 0));
		
		
		
		for(int i=0; i<generalTable.getColumnCount(); i++){
			if(i==0){
				generalTable.getColumnModel().getColumn(0).setPreferredWidth(86);
				
			}
			else{
				
				generalTable.getColumnModel().getColumn(i).setPreferredWidth(1);
				
			}
		}
	}
	
	public void addListenersAndRenderers(final GlobalDataKeeper globalDataKeeper, 
			final TableData tableData, final JTextArea descriptionText, 
			final JvTable LifeTimeTable, final GeneralTableIDUData IDUdata)
	{

		generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        	        
		        String tmpValue=tableData.getFinalRows()[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        c.setForeground(fr);
		        
		        if(column==tableData.getWholeCol() && tableData.getWholeCol()!=0){
		        	
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
		        else if(tableData.getSelectedColumn()==0){
		        	if (isSelected){
		        		
		        		if(tableData.getFinalRows()[row][0].contains("Cluster")){
			        		String description="Cluster:"+tableData.getFinalRows()[row][0]+"\n";
			        		description=description+"Birth Version Name:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getBirthSqlFile()+"\n";
			        		description=description+"Birth Version ID:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getBirth()+"\n";
			        		description=description+"Death Version Name:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getDeathSqlFile()+"\n";
			        		description=description+"Death Version ID:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getDeath()+"\n";
			        		description=description+"Tables:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getNamesOfTables().size()+"\n";
			        		description=description+"Total Changes:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getTotalChanges()+"\n";
			        		
			        		descriptionText.setText(description);
		        		}
		        		else{
			        		String description="Table:"+tableData.getFinalRows()[row][0]+"\n";
			        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getBirth()+"\n";
			        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getBirthVersionID()+"\n";
			        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getDeath()+"\n";
			        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getDeathVersionID()+"\n";
			        		description=description+"Total Changes:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getTotalChanges()+"\n";
			        		descriptionText.setText(description);

		        		}

		        		
		        		
		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		return c;
		        	}
		        }
		        else{
		        	
		        	if(tableData.getSelectedFromTree().contains(tableData.getFinalRows()[row][0])){

		        		Color cl = new Color(255,69,0,100);
		        		
		        		c.setBackground(cl);
		        		
		        		return c;
		        	}
		        	
		        	if (isSelected && hasFocus){
			        	
		        		String description="";
		        		if(!table.getColumnName(column).contains("Table name")){
		        			
			        		if(tableData.getFinalRows()[row][0].contains("Cluster")){

				        		description=tableData.getFinalRows()[row][0]+"\n";
				        		description=description+"Tables:"+globalDataKeeper.getClusterCollectors().get(0).getClusters().get(row).getNamesOfTables().size()+"\n\n";

				        		description=description+table.getColumnName(column)+"\n";
				        		description=description+"First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
				        				get(column-1).getStartPos()+"\n";
				        		description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
				        				get(column-1).getEndPos()+"\n\n";
				        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
				        		
			        		}
			        		else{
			        			description=table.getColumnName(column)+"\n";
				        		description=description+"First Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
				        				get(column-1).getStartPos()+"\n";
				        		description=description+"Last Transition ID:"+globalDataKeeper.getPhaseCollectors().get(0).getPhases().
				        				get(column-1).getEndPos()+"\n\n";
			        			description=description+"Table:"+tableData.getFinalRows()[row][0]+"\n";
				        		description=description+"Birth Version Name:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getBirth()+"\n";
				        		description=description+"Birth Version ID:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getBirthVersionID()+"\n";
				        		description=description+"Death Version Name:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getDeath()+"\n";
				        		description=description+"Death Version ID:"+globalDataKeeper.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getDeathVersionID()+"\n";
				        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
				        		
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
	        		else if(numericValue> 0&& numericValue<=tableData.getSegmentSize()[3]){
	        			
	        			insersionColor=new Color(176,226,255);
		        	}
	        		else if(numericValue>tableData.getSegmentSize()[3] && numericValue<=2*tableData.getSegmentSize()[3]){
	        			insersionColor=new Color(92,172,238);
	        		}
	        		else if(numericValue>2*tableData.getSegmentSize()[3] && numericValue<=3*tableData.getSegmentSize()[3]){
	        			
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
	        			c.setBackground(Color.gray);
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
			         tableData.setSelectedColumn(target.getSelectedColumn());
			         LifeTimeTable.repaint();
				}

			   }
		});
		
		generalTable.addMouseListener(new MouseAdapter() {
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
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent le) {
				            	if(sSelectedRow.contains("Cluster ")){
				            		zoomData.showClusterSelectionToZoomArea(tableData.getSelectedColumn(),sSelectedRow);

				            	}
				            	else{
				            		zoomData.showSelectionToZoomArea(tableData.getSelectedColumn());
				            	}
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        JMenuItem clearSelectionItem = new JMenuItem("Clear Selection");
				        clearSelectionItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent le) {
				            	
				            	tableData.setSelectedFromTree(new ArrayList<String>());
				            	LifeTimeTable.repaint();
				            }
				        });
				        popupMenu.add(clearSelectionItem);
				        popupMenu.show(generalTable, e.getX(),e.getY());
						      
					}
				
			   }
		});
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	tableData.setWholeCol(generalTable.columnAtPoint(e.getPoint()));
		        String name = generalTable.getColumnName(tableData.getWholeCol());
		        System.out.println("Column index selected " + tableData.getWholeCol() + " " + name);
		        generalTable.repaint();
		        if (tableData.isShowingPld()) {
			        IDUdata.makeGeneralTableIDU(tableData,globalDataKeeper);
				}
		    }
		});
		
		generalTable.getTableHeader().addMouseListener(new MouseAdapter() {
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
					            	generalTable.repaint();
					            	if(tableData.isShowingPld()){
					            		IDUdata.makeGeneralTableIDU(tableData,globalDataKeeper);
					            	}
					            }
					        });
					        popupMenu.add(clearColumnSelectionItem);
					        JMenuItem showDetailsItem = new JMenuItem("Show Details for this Phase");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
									String sSelectedRow=tableData.getFinalRows()[0][0];
									System.out.println("?"+sSelectedRow);
									tableData.setTablesSelected(new ArrayList<String>());
					            	for(int i=0; i<tableData.getFinalRows().length; i++)
					            		tableData.getTablesSelected().add((String) generalTable.getValueAt(i, 0));

					            	if(!sSelectedRow.contains("Cluster ")){
					            		
					            		zoomData.showSelectionToZoomArea(tableData.getWholeCol());	
					            	}
					            	else{
					            		zoomData.showClusterSelectionToZoomArea(tableData.getWholeCol(), "");
					            	}
					            	
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(generalTable, e.getX(),e.getY());
					    
				}
			
		   }
		    
		});
		
	}

}
