package data.dataKeeper;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataPPL.pplSQLSchema.PPLSchema;
import data.dataPPL.pplSQLSchema.PPLTable;
import data.dataPPL.pplTransition.AtomicChange;
import data.dataPPL.pplTransition.PPLTransition;
import data.dataPPL.pplTransition.TableChange;
import data.dataProccessing.Worker;
import gui.mainEngine.TableUpdater;
import gui.mainEngine.TreeManager;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.tableConstructors.TableConstructionIDU;
import gui.tableElements.tableConstructors.TableConstructionWithClusters;
import phaseAnalyzer.engine.PhaseAnalyzerMainEngine;
import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;

public class TableManager {
	private TreeMap<String,PPLSchema> allPPLSchemas = null;
	private TreeMap<String,PPLTable> allPPLTables = null;
	private ArrayList<AtomicChange> atomicChanges = null;
	private TreeMap<String,TableChange> tableChanges = null;
	private TreeMap<String,TableChange> tableChangesForTwo = null;
	private TreeMap<Integer,PPLTransition> allPPLTransitions = null;
	private String 	projectDataFolder=null;
	
	
	public TableManager()
	{
		allPPLSchemas = new TreeMap<String,PPLSchema>();
		allPPLTables = new  TreeMap<String,PPLTable>();
		atomicChanges = new ArrayList<AtomicChange>();
		tableChanges = new TreeMap<String,TableChange>();
		tableChangesForTwo = new TreeMap<String,TableChange>();
		allPPLTransitions = new TreeMap<Integer,PPLTransition>();
	}
	

	public void setData(String filename, String transitionsFile){
		System.out.println("TableManager: " + filename);
		
		Worker w = new Worker(filename,transitionsFile);
		try {
			w.work();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setAllPPLSchemas(w.getAllPPLSchemas());
		setAllPPLTables(w.getAllPPLTables());
		setAllPPLTransitions(w.getAllPPLTransitions());
		setTableChanges(w.getAllTableChanges());
		setAtomicChanges(w.getAtomicChanges());
		setProjectDataFolder(w.getDataFolder());
		

		
	}


	public TreeMap<String, PPLSchema> getAllPPLSchemas() {
		return allPPLSchemas;
	}


	public void setAllPPLSchemas(TreeMap<String, PPLSchema> allPPLSchemas) {
		this.allPPLSchemas = allPPLSchemas;
	}


	public TreeMap<String, PPLTable> getAllPPLTables() {
		return allPPLTables;
	}


	public void setAllPPLTables(TreeMap<String, PPLTable> allTables) {
		this.allPPLTables = allTables;
	}


	public ArrayList<AtomicChange> getAtomicChanges() {
		return atomicChanges;
	}


	public void setAtomicChanges(ArrayList<AtomicChange> atomicChanges) {
		this.atomicChanges = atomicChanges;
	}


	public TreeMap<String, TableChange> getTableChanges() {
		return tableChanges;
	}


	public void setTableChanges(TreeMap<String, TableChange> tableChanges) {
		this.tableChanges = tableChanges;
	}


	public TreeMap<String, TableChange> getTableChangesForTwo() {
		return tableChangesForTwo;
	}


	public void setTableChangesForTwo(TreeMap<String, TableChange> tableChangesForTwo) {
		this.tableChangesForTwo = tableChangesForTwo;
	}


	public TreeMap<Integer, PPLTransition> getAllPPLTransitions() {
		return allPPLTransitions;
	}


	public void setAllPPLTransitions(TreeMap<Integer, PPLTransition> allPPLTransitions) {
		this.allPPLTransitions = allPPLTransitions;
	}


	public String getProjectDataFolder() {
		return projectDataFolder;
	}


	public void setProjectDataFolder(String projectDataFolder) {
		this.projectDataFolder = projectDataFolder;
	}
	
	public void fillTable(ClusterManager clusterManager, TreeManager treeManager, TableData tableData, JTabbedPane tabbedPane, TableUpdater aux, String inputCsv, String outputAssessment1, String outputAssessment2, String projectName) {
		TableConstructionIDU table=new TableConstructionIDU(allPPLSchemas, allPPLTransitions);
		final String[] columns=table.constructColumns();
		final String[][] rows=table.constructRows();
		
		saveData(columns,rows,"simple", projectName);
		
		tableData.setSegmentSizeZoomArea(table.getSegmentSize());

		tableData.setFinalColumnsZoomArea(columns);
		tableData.setFinalRowsZoomArea(rows);

		if (aux != null && tabbedPane != null)
		{
			tabbedPane.setSelectedIndex(0);
			aux.makeGeneralTableIDU(tableData);
		}
		
		
		clusterManager.setTimeWeight((float)0.5);
        clusterManager.setChangeWeight((float)0.5);
        clusterManager.setPreProcessingTime(false);
        clusterManager.setPreProcessingChange(false);
        if(getAllPPLTransitions().size()<56){
        	clusterManager.setNumberOfPhases(40);
        }
        else{
        	clusterManager.setNumberOfPhases(56);
        }
        clusterManager.setNumberOfClusters(14);
        
        System.out.println(clusterManager.getTimeWeight()+" "+clusterManager.getChangeWeight());
        
		PhaseAnalyzerMainEngine mainEngine = new PhaseAnalyzerMainEngine(inputCsv,outputAssessment1,outputAssessment2, clusterManager);

		Double b=new Double(0.3);
		Double d=new Double(0.3);
		Double c=new Double(0.3);
			
		mainEngine.parseInput();		
		System.out.println("\n\n\n");
		mainEngine.extractPhases(clusterManager.getNumberOfPhases());
		
		mainEngine.connectTransitionsWithPhases(allPPLTransitions);
		clusterManager.setPhaseCollectors(mainEngine.getPhaseCollectors());
		TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(allPPLSchemas, allPPLTables,b,d,c);
		mainEngine2.extractClusters(clusterManager.getNumberOfClusters());
		clusterManager.setClusterCollectors(mainEngine2.getClusterCollectors());
		mainEngine2.print();
		
		if(clusterManager.getPhaseCollectors().size()!=0){
			TableConstructionWithClusters tableP=new TableConstructionWithClusters(clusterManager, this);
			final String[] columnsP=tableP.constructColumns();
			final String[][] rowsP=tableP.constructRows();
			
			saveData(columnsP,rowsP,"phases", projectName);
			
			tableData.setSegmentSize(tableP.getSegmentSize());
			tableData.setFinalColumns(columnsP);
			tableData.setFinalRows(rowsP);
			
			if (aux != null && treeManager != null && tabbedPane != null) {
				tabbedPane.setSelectedIndex(0);
				aux.makeGeneralTablePhases(tableData);
				treeManager.fillClustersTree(clusterManager);
			}
			
		}
		System.out.println("Schemas:"+getAllPPLSchemas().size());
		System.out.println("Transitions:"+getAllPPLTransitions().size());
		System.out.println("Tables:"+getAllPPLTables().size());

	}
	
	
	public void saveData(String[] columns, String[][] rows, String type, String projectName)
	{
		String testFile = "test_" + type + "_" + projectName;
		
		String testString = "";
		
		for (int i=0;i<columns.length;i++)
		{
			testString+=columns[i] + "/n";
		}
		testString+="COLUMNS_ROWS_SEPARATOR/n";
		
		for (int i=0; i<rows.length;i++)
		{
			for (int j=0; j<columns.length;j++)
			{
				testString+=rows[i][j] + "/n";
			}
			testString+="ROWS_SPEARATOR/n";
		}
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(testFile));
			writer.write(testString);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
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
            
            int selectedRow=tableData.getLifeTimeTable().getSelectedRow();
            
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
