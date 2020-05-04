package gui.mainEngine;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.TreePath;

import data.dataKeeper.ClusterManager;
import data.dataKeeper.TableData;
import data.dataKeeper.TableManager;
import data.dataSorters.PldRowSorter;
import gui.mainEngine.Gui;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableConstructors.PldConstruction;
import gui.tableElements.tableConstructors.TableConstructionClusterTablesPhasesZoomA;
import gui.tableElements.tableConstructors.TableConstructionZoomArea;
import gui.tableElements.tableRenderers.IDUHeaderTableRenderer;
import gui.tableElements.tableRenderers.IDUTableRenderer;
import gui.treeElements.TreeConstructionPhasesWithClusters;
public class GuiAux {
		
		private JButton zoomInButton;
		private JButton zoomOutButton;
		private JButton uniformlyDistributedButton;
		private JButton notUniformlyDistributedButton;
		private JButton showThisToPopup;
		private JButton undoButton;
		
		private MyTableModel zoomModel;
		private MyTableModel generalModel;

		private JScrollPane tmpScrollPaneZoomArea;
		private JScrollPane tmpScrollPane;
		
		private JPanel lifeTimePanel;

		private JvTable zoomAreaTable;
		private JvTable LifeTimeTable;

		private JTextArea descriptionText;
		
		private TableManager tableManager;
		private ClusterManager clusterManager;

		public GuiAux(JScrollPane tmpScrollPaneZoomArea,JScrollPane tmpScrollPane, JPanel lifeTimePanel, JvTable zoomAreaTable, MyTableModel zoomModel,MyTableModel generalModel, JTextArea descriptionText) 
		{
			this.tmpScrollPaneZoomArea = tmpScrollPaneZoomArea;
			this.tmpScrollPane = tmpScrollPane;
			this.lifeTimePanel = lifeTimePanel;
			this.zoomAreaTable=zoomAreaTable;
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
			this.undoButton = undoButton;
		}
		
		public void setManagers (TableManager tableManager, ClusterManager clusterManager)
		{
			this.tableManager = tableManager;
			this.clusterManager = clusterManager;
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
			if (clusterManager.getPhaseCollectors()!=null && tableData.getWholeCol()!=-1 && tableData.getWholeCol()!=0) {
				start=clusterManager.getPhaseCollectors().get(0).getPhases().get(tableData.getWholeCol()-1).getStartPos();
				end=clusterManager.getPhaseCollectors().get(0).getPhases().get(tableData.getWholeCol()-1).getEndPos();
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
			
			final IDUTableRenderer renderer = new IDUTableRenderer(descriptionText,tableData.getFinalRowsZoomArea(), tableManager, tableData.getSegmentSize());
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
			        	description=description+"Old Version Name:"+tableManager.getAllPPLTransitions().
		        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
		        		description=description+"New Version Name:"+tableManager.getAllPPLTransitions().
		        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
		        		
	        			description=description+"Transition Changes:"+tableManager.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfChangesForOneTr()+"\n";
	        			description=description+"Additions:"+tableManager.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfAdditionsForOneTr()+"\n";
	        			description=description+"Deletions:"+tableManager.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfDeletionsForOneTr()+"\n";
	        			description=description+"Updates:"+tableManager.getAllPPLTransitions().get(Integer.parseInt(table.getColumnName(column))).getNumberOfUpdatesForOneTr()+"\n";

	        			
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
			        		description=description+"Birth Version Name:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getBirth()+"\n";
			        		description=description+"Birth Version ID:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getBirthVersionID()+"\n";
			        		description=description+"Death Version Name:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getDeath()+"\n";
			        		description=description+"Death Version ID:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getDeathVersionID()+"\n";
			        		description=description+"Total Changes:"+tableManager.getAllPPLTables().get(tableData.getFinalRowsZoomArea()[row][0]).getTotalChanges()+"\n";

			        		
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
	        
			//lifeTimePanel.setCursor(gui.getCursor());
			lifeTimePanel.add(tmpScrollPaneZoomArea);
			
			
			
		}


	public void makeGeneralTablePhases(final TableData tableData, final JTabbedPane tabbedPane) {
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
		
		final JvTable generalTable=new JvTable(generalModel);
		
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
		        	
	        		descriptionText.setText(description);
		        	
		        	Color cl = new Color(255,69,0,100);

	        		c.setBackground(cl);
	        		return c;
		        }
		        else if(tableData.getSelectedColumn()==0){
		        	if (isSelected){
		        		
		        		if(tableData.getFinalRows()[row][0].contains("Cluster")){
			        		String description="Cluster:"+tableData.getFinalRows()[row][0]+"\n";
			        		description=description+"Birth Version Name:"+clusterManager.getClusterCollectors().get(0).getClusters().get(row).getBirthSqlFile()+"\n";
			        		description=description+"Birth Version ID:"+clusterManager.getClusterCollectors().get(0).getClusters().get(row).getBirth()+"\n";
			        		description=description+"Death Version Name:"+clusterManager.getClusterCollectors().get(0).getClusters().get(row).getDeathSqlFile()+"\n";
			        		description=description+"Death Version ID:"+clusterManager.getClusterCollectors().get(0).getClusters().get(row).getDeath()+"\n";
			        		description=description+"Tables:"+clusterManager.getClusterCollectors().get(0).getClusters().get(row).getNamesOfTables().size()+"\n";
			        		description=description+"Total Changes:"+clusterManager.getClusterCollectors().get(0).getClusters().get(row).getTotalChanges()+"\n";
			        		
			        		descriptionText.setText(description);
		        		}
		        		else{
			        		String description="Table:"+tableData.getFinalRows()[row][0]+"\n";
			        		description=description+"Birth Version Name:"+tableManager.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getBirth()+"\n";
			        		description=description+"Birth Version ID:"+tableManager.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getBirthVersionID()+"\n";
			        		description=description+"Death Version Name:"+tableManager.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getDeath()+"\n";
			        		description=description+"Death Version ID:"+tableManager.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getDeathVersionID()+"\n";
			        		description=description+"Total Changes:"+tableManager.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getTotalChanges()+"\n";
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
				        		description=description+"Tables:"+clusterManager.getClusterCollectors().get(0).getClusters().get(row).getNamesOfTables().size()+"\n\n";

				        		description=description+table.getColumnName(column)+"\n";
				        		description=description+"First Transition ID:"+clusterManager.getPhaseCollectors().get(0).getPhases().
				        				get(column-1).getStartPos()+"\n";
				        		description=description+"Last Transition ID:"+clusterManager.getPhaseCollectors().get(0).getPhases().
				        				get(column-1).getEndPos()+"\n\n";
				        		description=description+"Total Changes For This Phase:"+tmpValue+"\n";
				        		
			        		}
			        		else{
			        			description=table.getColumnName(column)+"\n";
				        		description=description+"First Transition ID:"+clusterManager.getPhaseCollectors().get(0).getPhases().
				        				get(column-1).getStartPos()+"\n";
				        		description=description+"Last Transition ID:"+clusterManager.getPhaseCollectors().get(0).getPhases().
				        				get(column-1).getEndPos()+"\n\n";
			        			description=description+"Table:"+tableData.getFinalRows()[row][0]+"\n";
				        		description=description+"Birth Version Name:"+tableManager.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getBirth()+"\n";
				        		description=description+"Birth Version ID:"+tableManager.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getBirthVersionID()+"\n";
				        		description=description+"Death Version Name:"+tableManager.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getDeath()+"\n";
				        		description=description+"Death Version ID:"+tableManager.getAllPPLTables().get(tableData.getFinalRows()[row][0]).getDeathVersionID()+"\n";
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
				            		showClusterSelectionToZoomArea(tableData.getSelectedColumn(),sSelectedRow, tableData, tabbedPane);

				            	}
				            	else{
				            		showSelectionToZoomArea(tableData.getSelectedColumn(), tableData, tabbedPane);
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
			        makeGeneralTableIDU(tableData);
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
					            		makeGeneralTableIDU(tableData);
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
					            		
					            		showSelectionToZoomArea(tableData.getWholeCol(), tableData, tabbedPane);	
					            	}
					            	else{
					            		showClusterSelectionToZoomArea(tableData.getWholeCol(), "", tableData, tabbedPane);
					            	}
					            	
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(generalTable, e.getX(),e.getY());
					    
				}
			
		   }
		    
		});
		
		
		LifeTimeTable=generalTable;
		
		tmpScrollPane.setViewportView(LifeTimeTable);
		tmpScrollPane.setAlignmentX(0);
		tmpScrollPane.setAlignmentY(0);
	    tmpScrollPane.setBounds(300,30,950,265);
	    tmpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    tmpScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    
		//lifeTimePanel.setCursor(getCursor());
		lifeTimePanel.add(tmpScrollPane);
	    
		
		
	}



	public void showClusterSelectionToZoomArea(int selectedColumn,String selectedCluster, TableData tableData, JTabbedPane tabbedPane)
	{
		
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
		tabbedPane.setSelectedIndex(0);
		makeZoomAreaTableForCluster(tableData, tabbedPane);
		
		
	}



	public void makeZoomAreaTable(final TableData tableData) {
		tableData.setShowingPld(false);
		int numberOfColumns=tableData.getFinalRowsZoomArea()[0].length;
		int numberOfRows=tableData.getFinalRowsZoomArea().length;
		
		
		final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];
		
		for(int i=0; i<numberOfRows; i++){
			
			rowsZoom[i][0]=tableData.getFinalRowsZoomArea()[i][0];
			
		}
		
		zoomModel=new MyTableModel(tableData.getFinalColumnsZoomArea(), rowsZoom);
		
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

	    		
		        	
		        	descriptionText.setText(description);
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
		
		
		zoomAreaTable=zoomTable;
		
		tmpScrollPaneZoomArea.setViewportView(zoomAreaTable);
		tmpScrollPaneZoomArea.setAlignmentX(0);
		tmpScrollPaneZoomArea.setAlignmentY(0);
		tmpScrollPaneZoomArea.setBounds(300,300,950,250);
		tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		

		//lifeTimePanel.setCursor(getCursor());
		lifeTimePanel.add(tmpScrollPaneZoomArea);
		
		
		
	}


	public void showSelectionToZoomArea(int selectedColumn, TableData tableData, JTabbedPane tabbedPane){
		
		TableConstructionZoomArea table=new TableConstructionZoomArea(tableManager, clusterManager,tableData.getTablesSelected(),selectedColumn);
		final String[] columns=table.constructColumns();
		final String[][] rows=table.constructRows();
		tableData.setSegmentSizeZoomArea(table.getSegmentSize());

		System.out.println("Schemas: "+tableManager.getAllPPLSchemas().size());
		System.out.println("C: "+columns.length+" R: "+rows.length);

		tableData.setFinalColumnsZoomArea(columns);
		tableData.setFinalRowsZoomArea(rows);
		tabbedPane.setSelectedIndex(0);
		makeZoomAreaTable(tableData);
		
		
		
	}


	public void makeZoomAreaTableForCluster(final TableData tableData, final JTabbedPane tabbedPane) {
		tableData.setShowingPld(false);
		int numberOfColumns=tableData.getFinalRowsZoomArea()[0].length;
		int numberOfRows=tableData.getFinalRowsZoomArea().length;
		undoButton.setVisible(true);
		
		final String[][] rowsZoom=new String[numberOfRows][numberOfColumns];
		
		for(int i=0; i<numberOfRows; i++){
			
			rowsZoom[i][0]=tableData.getFinalRowsZoomArea()[i][0];
			
		}
		
		zoomModel=new MyTableModel(tableData.getFinalColumnsZoomArea(), rowsZoom);
		
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
		        	
	        		descriptionText.setText(description);
		        	
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
					            		showSelectionToZoomArea(tableData.getSelectedColumnZoomArea(), tableData, tabbedPane);
										
						            	
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
		
		
		zoomAreaTable=zoomTable;
		
		tmpScrollPaneZoomArea.setViewportView(zoomAreaTable);
		tmpScrollPaneZoomArea.setAlignmentX(0);
		tmpScrollPaneZoomArea.setAlignmentY(0);
		tmpScrollPaneZoomArea.setBounds(300,300,950,250);
		tmpScrollPaneZoomArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tmpScrollPaneZoomArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		

		//lifeTimePanel.setCursor(getCursor());
		lifeTimePanel.add(tmpScrollPaneZoomArea);
		
		
		
	}



}

