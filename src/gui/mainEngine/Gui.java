package gui.mainEngine;

import gui.dialogs.CreateProjectJDialog;
import gui.dialogs.EnlargeTable;
import gui.listeners.menu.*;
import gui.listeners.tables.UndoButtonMouseAdapter;
import gui.tableElements.commons.JvTable;
import gui.tableElements.commons.MyTableModel;
import gui.tableElements.commons.ShowDetailsComponents;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import org.antlr.v4.runtime.RecognitionException;

import tableClustering.clusterExtractor.engine.TableClusteringMainEngine;
import tableClustering.clusterValidator.engine.ClusterValidatorMainEngine;
import data.dataKeeper.*;


public class Gui extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JPanel lifeTimePanel = new JPanel();
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	
	private MyTableModel detailedModel = null;
	private MyTableModel generalModel = null;
	private MyTableModel zoomModel = null;

	//private JvTable LifeTimeTable=null;
	private JvTable zoomAreaTable=null;
	
	private JScrollPane tmpScrollPane =new JScrollPane();
	private JScrollPane treeScrollPane= new JScrollPane();
	private JScrollPane tmpScrollPaneZoomArea =new JScrollPane();

	private String project=null;


	private JTree tablesTree=new JTree();
	private JPanel sideMenu=new JPanel();
	private JPanel tablesTreePanel=new JPanel();
	private JPanel descriptionPanel=new JPanel();
	private JLabel treeLabel;
	private JLabel generalTableLabel;
	private JLabel zoomAreaLabel;
	private JLabel descriptionLabel;
	private JTextArea descriptionText;
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton uniformlyDistributedButton;
	private JButton notUniformlyDistributedButton;
	private JButton showThisToPopup;

	
	private JButton undoButton;
	private JMenu mnProject;
	private JMenuItem mntmInfo;
	
	private TreeManager treeManager;
	
	private TableData tableData;
	
	private GlobalManager globalManager;
	private ShowDetailsComponents showDetails;
	
	private TableUpdater tableUpdater;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					//return;
					e.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Gui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setResizable(false);
		
	
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		

		sideMenu.setBounds(0, 0, 280, 600);
		sideMenu.setBackground(Color.DARK_GRAY);
		
		
		
		GroupLayout gl_sideMenu = new GroupLayout(sideMenu);
		gl_sideMenu.setHorizontalGroup(
				gl_sideMenu.createParallelGroup(Alignment.LEADING)
		);
		gl_sideMenu.setVerticalGroup(
				gl_sideMenu.createParallelGroup(Alignment.LEADING)
		);
		
		sideMenu.setLayout(gl_sideMenu);
		
		tablesTreePanel.setBounds(10, 400, 260, 180);
		tablesTreePanel.setBackground(Color.LIGHT_GRAY);
		
		GroupLayout gl_tablesTreePanel = new GroupLayout(tablesTreePanel);
		gl_tablesTreePanel.setHorizontalGroup(
				gl_tablesTreePanel.createParallelGroup(Alignment.LEADING)
		);
		gl_tablesTreePanel.setVerticalGroup(
				gl_tablesTreePanel.createParallelGroup(Alignment.LEADING)
		);
		
		tablesTreePanel.setLayout(gl_tablesTreePanel);
		
		treeLabel=new JLabel();
		treeLabel.setBounds(10, 370, 260, 40);
		treeLabel.setForeground(Color.WHITE);
		treeLabel.setText("Tree");
		
		descriptionPanel.setBounds(10, 190, 260, 180);
		descriptionPanel.setBackground(Color.LIGHT_GRAY);
		
		GroupLayout gl_descriptionPanel = new GroupLayout(descriptionPanel);
		gl_descriptionPanel.setHorizontalGroup(
				gl_descriptionPanel.createParallelGroup(Alignment.LEADING)
		);
		gl_descriptionPanel.setVerticalGroup(
				gl_descriptionPanel.createParallelGroup(Alignment.LEADING)
		);
		
		descriptionPanel.setLayout(gl_descriptionPanel);
		
		descriptionText=new JTextArea();
		descriptionText.setBounds(5, 5, 250, 170);
		descriptionText.setForeground(Color.BLACK);
		descriptionText.setText("");
		descriptionText.setBackground(Color.LIGHT_GRAY);
		
		descriptionPanel.add(descriptionText);
		
		
		descriptionLabel=new JLabel();
		descriptionLabel.setBounds(10, 160, 260, 40);
		descriptionLabel.setForeground(Color.WHITE);
		descriptionLabel.setText("Description");
		
		sideMenu.add(treeLabel);
		sideMenu.add(tablesTreePanel);
		
		sideMenu.add(descriptionLabel);
		sideMenu.add(descriptionPanel);

		lifeTimePanel.add(sideMenu);
		
		JButton buttonHelp=new JButton("Help");
		buttonHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String message ="To open a project, you must select a .txt file that contains the names ONLY of " +
									"the SQL files of the dataset that you want to use."+"\n" +
									"The .txt file must have EXACTLY the same name with the folder " +
									"that contains the DDL Scripts of the dataset."+ "\n" +
									"Both .txt file and dataset folder must be in the same folder.";
				JOptionPane.showMessageDialog(null,message); 				
			}
		});
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1474, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
		);
		
		
		tabbedPane.addTab("LifeTime Table", null, lifeTimePanel, null);
		
		GroupLayout gl_lifeTimePanel = new GroupLayout(lifeTimePanel);
		gl_lifeTimePanel.setHorizontalGroup(
			gl_lifeTimePanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 1469, Short.MAX_VALUE)
		);
		gl_lifeTimePanel.setVerticalGroup(
			gl_lifeTimePanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 743, Short.MAX_VALUE)
		);
		lifeTimePanel.setLayout(gl_lifeTimePanel);
		
		
		generalTableLabel=new JLabel("Parallel Lives Diagram");
		generalTableLabel.setBounds(300, 0, 150, 30);
		generalTableLabel.setForeground(Color.BLACK);
		
		zoomAreaLabel=new JLabel();
		zoomAreaLabel.setText("<HTML>Z<br>o<br>o<br>m<br><br>A<br>r<br>e<br>a</HTML>");
		zoomAreaLabel.setBounds(1255, 325, 15, 300);
		zoomAreaLabel.setForeground(Color.BLACK);
		
		zoomInButton = new JButton("Zoom In");
		zoomInButton.setBounds(1000, 560, 100, 30);
		
		
		
		zoomInButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				tableData.setRowHeight(tableData.getRowHeight()+2);
				tableData.setColumnWidth(tableData.getColumnWidth()+1);
				tableData.getZoomAreaTable().setZoom(tableData.getRowHeight(),tableData.getColumnWidth());
				
			}
		});
		
		zoomOutButton = new JButton("Zoom Out");
		zoomOutButton.setBounds(1110, 560, 100, 30);
		
		zoomOutButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				tableData.setRowHeight(tableData.getRowHeight()-2);
				tableData.setColumnWidth(tableData.getColumnWidth()-1);
				if(tableData.getRowHeight()<1){
					tableData.setRowHeight(1);
				}
				if (tableData.getColumnWidth()<1) {
					tableData.setColumnWidth(1);
				}
				tableData.getZoomAreaTable().setZoom(tableData.getRowHeight(),tableData.getColumnWidth());
				
			}
		});
		
		zoomInButton.setVisible(false);
		zoomOutButton.setVisible(false);
		
		
		showThisToPopup = new JButton("Enlarge");
		showThisToPopup.setBounds(800, 560, 100, 30);
		
		showThisToPopup.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				
				EnlargeTable showEnlargmentPopup= new EnlargeTable(tableData.getFinalRowsZoomArea(),tableData.getFinalColumnsZoomArea(),tableData.getSegmentSizeZoomArea());
				showEnlargmentPopup.setBounds(100, 100, 1300, 700);
				
				showEnlargmentPopup.setVisible(true);
				
				
			}
		});
		
		showThisToPopup.setVisible(false);
		
		
		undoButton = new JButton("Undo");
		undoButton.setBounds(680, 560, 100, 30);
		
		/*
		undoButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				if (tableData.getFirstLevelUndoColumnsZoomArea()!=null) {
					tableData.setFinalColumnsZoomArea(tableData.getFirstLevelUndoColumnsZoomArea());
					tableData.setFinalRowsZoomArea(tableData.getFirstLevelUndoRowsZoomArea());
					//TODO see how to change this
					//makeZoomAreaTableForCluster();
				}
				
			}
		});
		*/
		
		undoButton.addMouseListener(new UndoButtonMouseAdapter(tableData, showDetails, globalManager));
		undoButton.setVisible(false);
		
		
		uniformlyDistributedButton = new JButton("Same Width"); 
		uniformlyDistributedButton.setBounds(980, 0, 120, 30);
		
		uniformlyDistributedButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
			    tableData.getLifeTimeTable().uniformlyDistributed(1);
			    
			  } 
		});
		
		uniformlyDistributedButton.setVisible(false);
		
		notUniformlyDistributedButton = new JButton("Over Time"); 
		notUniformlyDistributedButton.setBounds(1100, 0, 120, 30);
		
		notUniformlyDistributedButton.addMouseListener(new MouseAdapter() {
			@Override
			   public void mouseClicked(MouseEvent e) {
				tableData.getLifeTimeTable().notUniformlyDistributed(globalManager);
			    
			  } 
		});
		
		notUniformlyDistributedButton.setVisible(false);
		
		lifeTimePanel.add(zoomInButton);
		lifeTimePanel.add(undoButton);
		lifeTimePanel.add(zoomOutButton);
		lifeTimePanel.add(uniformlyDistributedButton);
		lifeTimePanel.add(notUniformlyDistributedButton);
		lifeTimePanel.add(showThisToPopup);

		lifeTimePanel.add(zoomAreaLabel);
		
		lifeTimePanel.add(generalTableLabel);
		
		contentPane.setLayout(gl_contentPane);
		
		showDetails = new ShowDetailsComponents(descriptionText, tabbedPane, tmpScrollPaneZoomArea, zoomModel, zoomAreaTable, lifeTimePanel, undoButton);
		
		globalManager = new GlobalManager();
		tableData = new TableData();
		
		tableUpdater = new TableUpdater(tmpScrollPaneZoomArea,tmpScrollPane,lifeTimePanel,
				zoomModel, generalModel, descriptionText);
		tableUpdater.setButtons(zoomInButton, zoomOutButton, uniformlyDistributedButton, notUniformlyDistributedButton, showThisToPopup, undoButton);
		treeManager = new TreeManager(treeLabel, tablesTree, sideMenu, tablesTreePanel,
 treeScrollPane, tableData.getSelectedFromTree(),  tableData);
		tableUpdater.setShowDetails(showDetails);
		
		
		tableUpdater.setManagers(globalManager.getTableManager(),globalManager.getClusterManager());
		
		
		JMenuItem mntmCreateProject = new JMenuItem("Create Project");
		mntmCreateProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				CreateProjectJDialog createProjectDialog=new CreateProjectJDialog("","","","","","");

				createProjectDialog.setModal(true);
				
				
				createProjectDialog.setVisible(true);
				
				if(createProjectDialog.getConfirmation()){
					
					createProjectDialog.setVisible(false);
					
					File file = createProjectDialog.getFile();
		            System.out.println(file.toString());
		            globalManager.getProjectManager().setProject(file.getName());
		            String fileName=file.toString();
		            System.out.println("!!"+project);
		          
					try {
						globalManager.importData(fileName, treeManager, tableData, tableUpdater, tabbedPane);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
						return;
					} catch (RecognitionException e) {
						
						JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
						return;
					}
					
					
				}
				
		            
				
			}
		});
		mnFile.add(mntmCreateProject);
		
		JMenuItem mntmLoadProject = new JMenuItem("Load Project");		
		mntmLoadProject.addActionListener(new LoadProjectActionListener(tableUpdater, tableData, globalManager, treeManager, tabbedPane, Gui.this));
		mnFile.add(mntmLoadProject);

		
		JMenuItem mntmEditProject = new JMenuItem("Edit Project");
		mntmEditProject.addActionListener(new EditProjectActionListener(tableUpdater, tableData, globalManager, treeManager, tabbedPane, Gui.this));
		mnFile.add(mntmEditProject);
		
		
		JMenu mnTable = new JMenu("Table");
		menuBar.add(mnTable);
		
		JMenuItem mntmShowLifetimeTable = new JMenuItem("Show Full Detailed LifeTime Table");
		mntmShowLifetimeTable.addActionListener(new ShowLifetimeTableActionListener(tabbedPane, globalManager, tableData));
		mnFile.add(mntmShowLifetimeTable);
		
		JMenuItem mntmShowGeneralLifetimeIDU = new JMenuItem("Show PLD");
		mntmShowGeneralLifetimeIDU.addActionListener(new ShowPLDActionListener(tableUpdater, tableData, globalManager, treeManager, tabbedPane));
		mnTable.add(mntmShowGeneralLifetimeIDU);
		
		JMenuItem mntmShowGeneralLifetimePhasesPLD = new JMenuItem("Show Phases PLD");
		
		mntmShowGeneralLifetimePhasesPLD.addActionListener(new ShowPhasesPLDActionListener(globalManager,tableData,tableUpdater,tabbedPane, treeManager));
		mnTable.add(mntmShowGeneralLifetimePhasesPLD);
		
		JMenuItem mntmShowGeneralLifetimePhasesWithClustersPLD = new JMenuItem("Show Phases With Clusters PLD");
		
		mntmShowGeneralLifetimePhasesWithClustersPLD.addActionListener(new ShowPhasesWithClustersPLDActionListener(globalManager, tableData, tableUpdater, treeManager, tabbedPane));
		mnTable.add(mntmShowGeneralLifetimePhasesWithClustersPLD);
		
		
		mnTable.add(mntmShowLifetimeTable);
		

		
		mnProject = new JMenu("Project");
		menuBar.add(mnProject);
		
		mntmInfo = new JMenuItem("Info");
		mntmInfo.addActionListener(new InfoActionListener(globalManager));
		mnProject.add(mntmInfo);
		buttonHelp.setBounds(900,900 , 80, 40);
		menuBar.add(buttonHelp);
		
		
		
		
		pack();
		setBounds(30, 30, 1300, 700);

		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {

		
	}

	public void optimize() throws IOException{
		
		String lalaString="Birth Weight:"+"\tDeath Weight:"+"\tChange Weight:"+"\tTotal Cohesion:"+"\tTotal Separation:"+"\n";
		int counter=0;
		for(double wb=0.0; wb<=1.0; wb=wb+0.01){
			
			for(double wd=(1.0-wb); wd>=0.0; wd=wd-0.01){
				
					double wc=1.0-(wb+wd);
					TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(globalManager.getTableManager().getAllPPLSchemas(),globalManager.getTableManager().getAllPPLTables(),wb,wd,wc);
					mainEngine2.extractClusters(globalManager.getClusterManager().getNumberOfClusters());
					globalManager.getClusterManager().setClusterCollectors(mainEngine2.getClusterCollectors());
					
					ClusterValidatorMainEngine lala = new ClusterValidatorMainEngine(globalManager);
					lala.run();
					
					lalaString=lalaString+wb+"\t"+wd+"\t"+wc
							+"\t"+lala.getTotalCohesion()+"\t"+lala.getTotalSeparation()+"\t"+(wb+wd+wc)+"\n";
			
					counter++;
					System.err.println(counter);
				
				
			}
			
			
			
		}
		
		FileWriter fw;
		try {
			fw = new FileWriter("lala.csv");
			
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(lalaString);
			bw.close();
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		
		System.out.println(lalaString);
		
		
	}
	
	public void getExternalValidityReport() throws IOException{
		
		String lalaString="Birth Weight:"+"\tDeath Weight:"+"\tChange Weight:"+"\n";
		int counter=0;
		
		TableClusteringMainEngine mainEngine2 = new TableClusteringMainEngine(globalManager.getTableManager().getAllPPLSchemas(),globalManager.getTableManager().getAllPPLTables(),0.333,0.333,0.333);
		mainEngine2.extractClusters(4);
		globalManager.getClusterManager().setClusterCollectors(mainEngine2.getClusterCollectors());
		
		ClusterValidatorMainEngine lala = new ClusterValidatorMainEngine(globalManager);
		lala.run();
		
		lalaString=lalaString+"\n"+"0.333"+"\t"+"0.333"+"\t"+"0.333"
				+"\n"+lala.getExternalEvaluationReport();
		
		for(double wb=0.0; wb<=1.0; wb=wb+0.5){
			
			for(double wd=(1.0-wb); wd>=0.0; wd=wd-0.5){
				
					double wc=1.0-(wb+wd);
					mainEngine2 = new TableClusteringMainEngine(globalManager.getTableManager().getAllPPLSchemas(),globalManager.getTableManager().getAllPPLTables(),wb,wd,wc);
					mainEngine2.extractClusters(4);
					globalManager.getClusterManager().setClusterCollectors(mainEngine2.getClusterCollectors());
					
					lala = new ClusterValidatorMainEngine(globalManager);
					lala.run();
					
					lalaString=lalaString+"\n"+wb+"\t"+wd+"\t"+wc
							+"\n"+lala.getExternalEvaluationReport();
			
					counter++;
					System.err.println(counter);
				
				
			}
			
			
			
		}
		
		FileWriter fw;
		try {
			fw = new FileWriter("lala.csv");
			
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(lalaString);
			bw.close();
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		
		System.out.println(lalaString);
		
		
	}

	public void setDescription(String descr){
		descriptionText.setText(descr);
	}
	
	public TableUpdater getTableUpdater()
	{
		return tableUpdater;
	}
	
	public GlobalManager getGlobalManager()
	{
		return globalManager;
	}
	
	public TableData getTableData()
	{
		return tableData;
	}
	
	public TreeManager getTreeManager()
	{
		return treeManager;
	}
	
	public JTabbedPane getTabbedPane()
	{
		return tabbedPane;
	}

	
}
