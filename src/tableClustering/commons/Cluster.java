package tableClustering.commons;

import java.util.Map;
import java.util.TreeMap;

import data.pplSqlSchema.PPLTable;

public class Cluster {

	private int birth;
	private String birthVersion;
	private int death;
	private String deathVersion;
	private int totalChanges=0;
	private TreeMap<String,PPLTable> tables=null;
	
	public Cluster(){
		
		tables=new TreeMap<String, PPLTable>();
	}
	
	public Cluster(int birth,String birthVersion, int death,String deathVersion, int totalChanges){
		
		this.birth=birth;
		this.birthVersion=birthVersion;
		this.death=death;
		this.deathVersion=deathVersion;
		this.totalChanges=totalChanges;
		tables=new TreeMap<String, PPLTable>();

		
	}
	
	public TreeMap<String,PPLTable> getTables(){
		return tables;
	}
	
	public void addTable(PPLTable table){
		this.tables.put(table.getName(), table);
	}
	
	public int getBirth(){
		return this.birth;
	}
	
	public int getDeath(){
		return this.death;
	}
	
	public double distance(Cluster anotherCluster,float birthWeight, float deathWeight ,float changeWeight){
		
		double changeDistance = Math.abs(this.totalChanges - anotherCluster.totalChanges);
	
		double birthDistance = Math.abs(this.birth-anotherCluster.birth);
		double deathDistance = Math.abs(this.death-anotherCluster.death);


		double totalDistance = changeWeight * changeDistance + birthWeight * birthDistance + deathWeight * deathDistance;
		return totalDistance;
		
	}
	
	public Cluster mergeWithNextCluster(Cluster nextCluster){
		
		Cluster newCluster = new Cluster();
	
		int minBirth;
		String minBirthVersion="";
		if(this.birth<=nextCluster.birth){
			minBirth=this.birth;
			minBirthVersion=this.birthVersion;
		}
		else {
			minBirth=nextCluster.birth;
			minBirthVersion=nextCluster.birthVersion;
		}
		
		int maxDeath;
		String maxDeathVersion="";
		if(this.death>=nextCluster.death){
			maxDeath=this.death;
			maxDeathVersion=this.deathVersion;
		}
		else {
			maxDeath=nextCluster.death;
			maxDeathVersion=nextCluster.deathVersion;

		}
		
		newCluster.birth = minBirth;
		newCluster.birthVersion = minBirthVersion;
		newCluster.death =maxDeath;
		newCluster.deathVersion = maxDeathVersion;
		
		newCluster.totalChanges = this.totalChanges + nextCluster.totalChanges;
		
		for (Map.Entry<String,PPLTable> tab : tables.entrySet()) {
			
			newCluster.addTable(tab.getValue());
			
		}
		
		for (Map.Entry<String,PPLTable> tabNext : nextCluster.getTables().entrySet()) {
			
			newCluster.addTable(tabNext.getValue());
			
		}
		
		//System.out.println(subPhases.size());
		//TODO FIX FIX FIX FIX
		//Add any other attributes necessary!!
		return newCluster;
	}
	
	public String toString(){
		
		System.out.println(this.tables.size());

		String toReturn="Cluster: ";
		
		
		toReturn=toReturn+this.birth+"\t"+this.death+"\t"+this.totalChanges+"\n";
		
		
		for(Map.Entry<String, PPLTable> t: this.tables.entrySet()){
			toReturn=toReturn+t.getKey()+"\t"+t.getValue().getBirth()+"\t"+t.getValue().getDeath()+"\t"+t.getValue().getTotalChanges()+"\n";
		}
		
		
		return toReturn;
		
	}
	
	
}
