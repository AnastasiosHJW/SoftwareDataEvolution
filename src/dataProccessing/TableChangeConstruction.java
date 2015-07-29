package dataProccessing;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import data.pplSqlSchema.PPLTable;
import data.pplTransition.AtomicChange;
import data.pplTransition.TableChange;

public class TableChangeConstruction {
	
	private static ArrayList<AtomicChange> atomicChanges = new ArrayList<AtomicChange>();
	private static TreeMap<String,TableChange> allTableChanges;
	private static TreeMap<String,PPLTable> allTables = new TreeMap<String,PPLTable>();


	public TableChangeConstruction(ArrayList<AtomicChange> tmpAtomicChanges,TreeMap<String,PPLTable> tmpAllTables){
		
		atomicChanges=tmpAtomicChanges;
		allTables=tmpAllTables;
		allTableChanges = new  TreeMap<String,TableChange>();
		
	}
	
	public void makeTableChanges(){
		
		//allTableChanges  = new TreeMap<String,TreeMap<String,ArrayList<AtomicChange>>>();

		
		for(int i=0; i<atomicChanges.size(); i++){
			
			
			
			if(allTableChanges.containsKey(atomicChanges.get(i).getAffectedTableName())){
				
				Integer transitionID=atomicChanges.get(i).getTransitionID();
				
				
				if(allTableChanges.get(atomicChanges.get(i).getAffectedTableName()).getTableAtomicChanges().containsKey(transitionID)){
				
					allTableChanges.get(atomicChanges.get(i).getAffectedTableName()).getTableAtomicChanges().get(transitionID).add(atomicChanges.get(i));
				
				}
				else{
					
					ArrayList<AtomicChange> tmpAtomicChanges = new ArrayList<AtomicChange>();
					
					allTableChanges.get(atomicChanges.get(i).getAffectedTableName()).getTableAtomicChanges().put(transitionID, tmpAtomicChanges);
					
					allTableChanges.get(atomicChanges.get(i).getAffectedTableName()).getTableAtomicChanges().get(transitionID).add(atomicChanges.get(i));
					
				}
				
			}
			else{
				
				TreeMap<Integer,ArrayList<AtomicChange>> tmpAtomicChanges = new TreeMap<Integer,ArrayList<AtomicChange>>();
						
				Integer transitionID=atomicChanges.get(i).getTransitionID();

				tmpAtomicChanges.put(transitionID,new ArrayList<AtomicChange>());
				
				tmpAtomicChanges.get(transitionID).add(atomicChanges.get(i));

				TableChange tmpTableChange= new TableChange(atomicChanges.get(i).getAffectedTableName(), tmpAtomicChanges);
				
				allTableChanges.put(atomicChanges.get(i).getAffectedTableName(), tmpTableChange);
				
			}
			
		}
		
		for (Map.Entry<String, TableChange> t : allTableChanges.entrySet()) {
			
			TableChange tmpTableChange = t.getValue();
			allTables.get(t.getKey()).setTableChanges(tmpTableChange);
			allTables.get(t.getKey()).setTotalChanges();

				
		}
		
		/*
		for (Map.Entry<String, PPLTable> t : allTables.entrySet()) {
			
			PPLTable tmpTableChange = t.getValue();
			
			TableChange lala=tmpTableChange.getTableChanges();
			
			TreeMap<String, ArrayList<AtomicChange>>lolo =lala.getTableAtomicChanges();
			
			
			for (Map.Entry<String, ArrayList<AtomicChange>> l : lolo.entrySet()) {
				
				
				ArrayList<AtomicChange> pf = l.getValue();
				
				for (int j = 0; j < pf.size(); j++) {
					
					System.out.println(lala.getAffectedTableName()+" "+pf.get(j).toString());
					
				}

			}
				
		}
		*/
		
	}
	
	public TreeMap<String,TableChange> getTableChanges(){
		
		return allTableChanges;
		
	}

}
