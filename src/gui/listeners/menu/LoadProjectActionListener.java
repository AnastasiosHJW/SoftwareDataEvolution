package gui.listeners.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.antlr.v4.runtime.RecognitionException;

import data.dataKeeper.GlobalManager;
import data.dataKeeper.TableData;
import gui.mainEngine.Gui;
import gui.mainEngine.TableUpdater;
import gui.mainEngine.TreeManager;

public class LoadProjectActionListener implements ActionListener {

	private TableUpdater tableUpdater;
	private TableData tableData;
	private GlobalManager globalManager;
	private TreeManager treeManager;
	private JTabbedPane tabbedPane;
	private Gui gui;

	
	public LoadProjectActionListener(TableUpdater tableUpdater, TableData tableData, GlobalManager globalManager,
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
          

		}
		else{
			return;
		}
		try {
			
			globalManager.importData(fileName, treeManager, tableData ,tableUpdater, tabbedPane);
			System.out.println("Data imported");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
		} catch (RecognitionException e) {
			
			JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
			return;
		}
		
	}

}
