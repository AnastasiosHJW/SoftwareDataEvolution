package data.dataSorters;

import java.util.Map;

import data.dataKeeper.GlobalDataKeeper;
import data.dataKeeper.TableManager;
import data.dataPPL.pplSQLSchema.PPLTable;

public class PldRowSorter {
	
	private String[][] finalRows;
	private GlobalDataKeeper globalDataKeeper = new GlobalDataKeeper();
	private TableManager tableManager;

	//to be deprecated
	public PldRowSorter(String[][] finalRows,GlobalDataKeeper globalDataKeeper){
		this.finalRows=finalRows;
		this.globalDataKeeper=globalDataKeeper;
	}
	
	public PldRowSorter(String[][] finalRows,TableManager tableManager){
		this.tableManager = tableManager;
		this.finalRows=finalRows;
	}

	public String[][] sortRows2(){
		
		String[][] sortedRows=new String[finalRows.length][finalRows[0].length];
		
		PPLTableSortingClass tablesSorter=new PPLTableSortingClass();
		
	    Map<String, PPLTable> wtf=tableManager.getAllPPLTables();
	    int counter=0;
	    for(Map.Entry<String, PPLTable> ppl:tablesSorter.entriesSortedByBirthDeath(wtf)){
			for(int i=0; i<finalRows.length; i++ ){
				if (finalRows[i][0].equals(ppl.getKey())) {
					for(int j=0;j<finalRows[0].length; j++){
						sortedRows[counter][j]=finalRows[i][j];
					}                               
				}
			}
			
			counter++;
		}
		
		return sortedRows;
		
		
	}
	
	
	public String[][] sortRows(){
		
		String[][] sortedRows=new String[finalRows.length][finalRows[0].length];
		
		PPLTableSortingClass tablesSorter=new PPLTableSortingClass();
		
	    Map<String, PPLTable> wtf=globalDataKeeper.getAllPPLTables();
	    int counter=0;
	    for(Map.Entry<String, PPLTable> ppl:tablesSorter.entriesSortedByBirthDeath(wtf)){
			for(int i=0; i<finalRows.length; i++ ){
				if (finalRows[i][0].equals(ppl.getKey())) {
					for(int j=0;j<finalRows[0].length; j++){
						sortedRows[counter][j]=finalRows[i][j];
					}                               
				}
			}
			
			counter++;
		}
		
		return sortedRows;
		
		
	}
	
}
