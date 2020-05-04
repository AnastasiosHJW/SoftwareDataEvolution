package gui.tableElements.tableRenderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataKeeper.ClusterManager;
import data.dataKeeper.TableData;
import data.dataKeeper.TableManager;

public class GeneralTableIDUDefaultTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	private TableData tableData;
	private TableManager tableManager;
	private ClusterManager clusterManager;
	private JTextArea descriptionText;

	public GeneralTableIDUDefaultTableCellRenderer(TableData tableData, TableManager tableManager, ClusterManager clusterManager, JTextArea descriptionText)
	{
		super();
		this.tableData = tableData;
		this.tableManager = tableManager;
		this.clusterManager = clusterManager;
		this.descriptionText = descriptionText;
	}
	
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        //System.out.println(row + " " + column);
        //System.out.println(tableData.getFinalRows().length + " " + tableData.getFinalRows()[row].length);
        
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
}

