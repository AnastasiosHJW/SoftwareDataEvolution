package data.dataKeeper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JTabbedPane;

import org.antlr.v4.runtime.RecognitionException;

import gui.mainEngine.TableUpdater;
import gui.mainEngine.TreeManager;
import gui.tableElements.commons.MyTableModel;

public class ProjectManager {
	
	private String projectName;
	private String datasetTxt;
	private String inputCsv;
	private String outputAssessment1;
	private String outputAssessment2;
	
	private String transitionsFile;
	private String currentProject;



	public void importData(String fileName, TableManager tableManager, ClusterManager clusterManager, TreeManager treeManager, TableData tableData, TableUpdater aux, JTabbedPane tabbedPane) throws IOException, RecognitionException {
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		
		String line;
		
		while(true) {
			line = br.readLine();
			if (line == null) 
				break;
			if(line.contains("Project-name")){
				String[] projectNameTable=line.split(":");
				projectName=projectNameTable[1];
			}
			else if(line.contains("Dataset-txt")){
				String[] datasetTxtTable=line.split(":");
				datasetTxt=datasetTxtTable[1];
			}
			else if(line.contains("Input-csv")){
				String[] inputCsvTable=line.split(":");
				inputCsv=inputCsvTable[1];
			}
			else if(line.contains("Assessement1-output")){
				String[] outputAss1=line.split(":");
				outputAssessment1=outputAss1[1];
			}
			else if(line.contains("Assessement2-output")){
				String[] outputAss2=line.split(":");
				outputAssessment2=outputAss2[1];
			}
			else if(line.contains("Transition-xml")){
				String[] transitionXmlTable=line.split(":");
				transitionsFile=transitionXmlTable[1];
			}
			
			
		};	
		
		br.close();
		
		
		System.out.println("Project Name:"+projectName);
		System.out.println("Dataset txt:"+datasetTxt);
		System.out.println("Input Csv:"+inputCsv);
		System.out.println("Output Assessment1:"+outputAssessment1);
		System.out.println("Output Assessment2:"+outputAssessment2);
		System.out.println("Transitions File:"+transitionsFile);

		tableManager.setData(datasetTxt, transitionsFile);
		
		System.out.println(tableManager.getAllPPLTables().size());
		
        System.out.println(fileName);
        
        
        tableManager.fillTable(clusterManager, treeManager, tableData, tabbedPane, aux, inputCsv, outputAssessment1, outputAssessment2, projectName);
        
        if (treeManager != null)
        {
            treeManager.fillTree(tableManager);
        }


		currentProject=fileName;
	}



	public String getProjectName() {
		return projectName;
	}



	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}



	public String getDatasetTxt() {
		return datasetTxt;
	}



	public void setDatasetTxt(String datasetTxt) {
		this.datasetTxt = datasetTxt;
	}



	public String getInputCsv() {
		return inputCsv;
	}



	public void setInputCsv(String inputCsv) {
		this.inputCsv = inputCsv;
	}



	public String getOutputAssessment1() {
		return outputAssessment1;
	}



	public void setOutputAssessment1(String outputAssessment1) {
		this.outputAssessment1 = outputAssessment1;
	}



	public String getOutputAssessment2() {
		return outputAssessment2;
	}



	public void setOutputAssessment2(String outputAssessment2) {
		this.outputAssessment2 = outputAssessment2;
	}



	public String getTransitionsFile() {
		return transitionsFile;
	}



	public void setTransitionsFile(String transitionsFile) {
		this.transitionsFile = transitionsFile;
	}



	public String getCurrentProject() {
		return currentProject;
	}



	public void setCurrentProject(String currentProject) {
		this.currentProject = currentProject;
	}
	
	
	
}
