package gui.tableElements.tableRenderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;

import data.dataKeeper.TableManager;

public class IDUTableRenderer extends DefaultTableCellRenderer{
	
	private static final long serialVersionUID = 1L;
	private String[][] finalRows;
	private int wholeCol=-1;
	private String description="";
	private int selectedColumn;
	//private ArrayList<String> selectedFromTree = new ArrayList<String>();
	private Integer[] segmentSize=new Integer[3];
	private TableManager tableManager;
	private JTextArea descriptionText;
	
	
	
	public IDUTableRenderer(JTextArea descriptionText,String[][] finalRows , TableManager tableManager,Integer[] segmentSize){
		this.finalRows=finalRows;
		//this.wholeCol = wholeCol;
		this.tableManager = tableManager;
		//this.selectedColumn=selectedColumn;
		//this.selectedFromTree = selectedFromTree;
		this.segmentSize=segmentSize;
		this.descriptionText = descriptionText;
	}
	
	
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        String tmpValue=finalRows[row][column];
        String columnName=table.getColumnName(column);
        Color fr=new Color(0,0,0);
        
        this.setForeground(fr);
        this.setOpaque(true);
      
        if(column==wholeCol){
        	
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
        else if(selectedColumn==0){
    		
        	if (isSelected){
        		Color cl = new Color(255,69,0,100);
        		c.setBackground(cl);
        		
        		String description="Table:"+finalRows[row][0]+"\n";
        		description=description+"Birth Version Name:"+tableManager.getAllPPLTables().get(finalRows[row][0]).getBirth()+"\n";
        		description=description+"Birth Version ID:"+tableManager.getAllPPLTables().get(finalRows[row][0]).getBirthVersionID()+"\n";
        		description=description+"Death Version Name:"+tableManager.getAllPPLTables().get(finalRows[row][0]).getDeath()+"\n";
        		description=description+"Death Version ID:"+tableManager.getAllPPLTables().get(finalRows[row][0]).getDeathVersionID()+"\n";
        		description=description+"Total Changes:"+tableManager.getAllPPLTables().get(finalRows[row][0]).getTotalChanges()+"\n";

        		
        		descriptionText.setText(description);
        		
        		return c;
        		
        		
        	}
        }
        else{

        	/*
        	if(selectedFromTree.contains(finalRows[row][0])){


        		Color cl = new Color(255,69,0,100);
        		
        		c.setBackground(cl);
        		
        		return c;
        	}
        	
        	 */
        	
        	if (isSelected && hasFocus){

        		String description="";
        		if(!table.getColumnName(column).contains("Table name")){
	        		description="Table:"+finalRows[row][0]+"\n";
	        		
	        		description=description+"Old Version Name:"+tableManager.getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(column))).getOldVersionName()+"\n";
	        		description=description+"New Version Name:"+tableManager.getAllPPLTransitions().
	        				get(Integer.parseInt(table.getColumnName(column))).getNewVersionName()+"\n";		        		
	        		if(tableManager.getAllPPLTables().get(finalRows[row][0]).
	        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column)))!=null){
	        			description=description+"Transition Changes:"+tableManager.getAllPPLTables().get(finalRows[row][0]).
	        				getTableChanges().getTableAtChForOneTransition(Integer.parseInt(table.getColumnName(column))).size()+"\n";
	        			description=description+"Additions:"+tableManager.getAllPPLTables().get(finalRows[row][0]).
	        					getNumberOfAdditionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
	        			description=description+"Deletions:"+tableManager.getAllPPLTables().get(finalRows[row][0]).
	        					getNumberOfDeletionsForOneTr(Integer.parseInt(table.getColumnName(column)))+"\n";
	        			description=description+"Updates:"+tableManager.getAllPPLTables().get(finalRows[row][0]).
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
    		else if(numericValue> 0&& numericValue<=segmentSize[1]){
    			
    			insersionColor=new Color(176,226,255);
        	}
    		else if(numericValue>segmentSize[1] && numericValue<=2*segmentSize[1]){
    			insersionColor=new Color(92,172,238);
    		}
    		else if(numericValue>2*segmentSize[1] && numericValue<=3*segmentSize[1]){
    			
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
	
	
	public String getText(){
		return description;
	}
	
	public void setWholeCol(int wholeCol){
		this.wholeCol = wholeCol;
	}
	
	public void setSelCol(int selectedColumn){
		this.selectedColumn = selectedColumn;
	}

}
