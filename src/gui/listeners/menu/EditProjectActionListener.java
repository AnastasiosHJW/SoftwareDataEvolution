package gui.listeners.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.antlr.v4.runtime.RecognitionException;

import data.dataKeeper.GlobalManager;
import data.dataKeeper.TableData;
import gui.dialogs.CreateProjectJDialog;
import gui.mainEngine.Gui;
import gui.mainEngine.TableUpdater;
import gui.mainEngine.TreeManager;

public class EditProjectActionListener implements ActionListener {

	private TableUpdater tableUpdater;
	private TableData tableData;
	private GlobalManager globalManager;
	private TreeManager treeManager;
	private JTabbedPane tabbedPane;
	private Gui gui;

	
	public EditProjectActionListener(TableUpdater tableUpdater, TableData tableData, GlobalManager globalManager,
			TreeManager treeManager, JTabbedPane tabbedPane, Gui gui) {
		super();
		this.tableUpdater = tableUpdater;
		this.tableData = tableData;
		this.globalManager = globalManager;
		this.treeManager = treeManager;
		this.tabbedPane = tabbedPane;
		this.gui = gui;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		String fileName=null;
		String project=null;
		String projectName=null;
		String datasetTxt = null;
		String inputCsv = null;
		String outputAssessment1 = null;
		String outputAssessment2 = null;
		String transitionsFile = null;

		
		
		File dir=new File("filesHandler/inis");
		JFileChooser fcOpen1 = new JFileChooser();
		fcOpen1.setCurrentDirectory(dir);
		int returnVal = fcOpen1.showDialog(gui, "Open");
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
            File file = fcOpen1.getSelectedFile();
            System.out.println(file.toString());
            project=file.getName();
            fileName=file.toString();
            System.out.println("!!"+project);
          
            BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(fileName));
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
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			
			}
			
			System.out.println(projectName);
			
			CreateProjectJDialog createProjectDialog=new CreateProjectJDialog(projectName,datasetTxt,inputCsv,outputAssessment1,outputAssessment2,transitionsFile);
		
			createProjectDialog.setModal(true);
			
			createProjectDialog.setVisible(true);
			
			if(createProjectDialog.getConfirmation()){
				
				createProjectDialog.setVisible(false);
				
				file = createProjectDialog.getFile();
	            System.out.println(file.toString());
	            project=file.getName();
	            fileName=file.toString();
	            System.out.println("!!"+project);
			
				try {
					globalManager.importData(fileName, treeManager, tableData ,tableUpdater, tabbedPane);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
					return;
				} catch (RecognitionException e) {
					
					JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
					return;
				}				
			}			
		}
		else{
			return;
		}		
	}
}
