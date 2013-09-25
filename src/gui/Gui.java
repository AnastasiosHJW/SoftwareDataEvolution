package gui;


import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.antlr.v4.runtime.RecognitionException;
import org.jfree.chart.ChartPanel;

import results.IntensiveTablesFromTwoSchemasResults;
import results.LongLivedTablesResults;
import results.MostUpdatedAttributesResults;
import results.MostUpdatedTablesResults;
import results.PercentageOfChangesResults;
import results.Results;
import sqlSchema.Attribute;
import sqlSchema.Schema;
import sqlSchema.Table;
import transitions.ImportData;
import transitions.TransitionList;
import visualization.IntensiveTablesFromTwoSchemasVisualization;
import visualization.LongLivedTablesVisualization;
import visualization.MostUpdatedAttributesVisualization;
import visualization.MostUpdatedTablesVisualization;
import visualization.PercentageOfChangesVisualization;
import visualization.PercentageOfChangesVisualization.Rotator;
import visualization.Visualization;
import algorithms.Algorithm;
import algorithms.AssistantPercentageClassAlgo;
import algorithms.IntensiveTablesFromTwoSchemasAlgo;
import algorithms.LongLivedTablesAlgo;
import algorithms.MostUpdatedAttributesAlgo;
import algorithms.MostUpdatedTablesAlgo;
import algorithms.PercentageOfChangesAlgo;

public class Gui extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JPanel lifeTimePanel = new JPanel();
	private JPanel statistics = new JPanel();
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	
	private MyTableModel detailedModel = null;
	private MyTableModel generalModel = null;
	private JTable LifeTimeTable=null;
	
	private JButton buttonStop=null;
	
	private JLabel labelLongLivedTables=new JLabel();
	private JLabel labelMostUpdatedTables=new JLabel();
	private JLabel labelMostUpdatedAttributes=new JLabel();
	private JLabel labelMostIntensiveTables=new JLabel();
	private JLabel labelMostIntensiveInsersionsByPercentage=new JLabel();
	private JLabel labelMostIntensiveUpdatesByPercentage=new JLabel();
	private JLabel labelMostIntensiveDeletionsByPercentage=new JLabel();
	
	private JTextArea jTextAreaMostUpdatedAttributes=new JTextArea();
	private JTextArea jTextAreaMostIntensiveTables=new JTextArea();
	private JTextArea jTextAreaMostIntensiveInsersionsByPercentage=new JTextArea();
	private JTextArea jTextAreaMostIntensiveUpdatesByPercentage=new JTextArea();
	private JTextArea jTextAreaMostIntensiveDeletionsByPercentage=new JTextArea();
	
	@SuppressWarnings("rawtypes")
	private JList jListKLongLivedTables=new JList();
	@SuppressWarnings("rawtypes")
	private JList jListKMostUpdatedTables=new JList();
	@SuppressWarnings("rawtypes")
	private DefaultListModel listModelLongLivedTables=new DefaultListModel();
	@SuppressWarnings("rawtypes")
	private DefaultListModel listModelMostUpdatedTables=new DefaultListModel();
	private JScrollPane tmpScrollPane =new JScrollPane();
	private JScrollPane jScrollPaneKLongLivedTables=null;
	private JScrollPane jScrollPaneKMostUpdatedTables=null;
	private JScrollPane jScrollPaneKMostUpdatedAttributes=null;
	private JScrollPane jScrollPaneKMostIntensiveTables=null;
	private JScrollPane jScrollPaneKMostIntensiveInsersionsByPercentage=null;
	private JScrollPane jScrollPaneKMostIntensiveUpdatesByPercentage=null;
	private JScrollPane jScrollPaneKMostIntensiveDeletionsByPercentage=null;
	
	private ArrayList<Schema> AllSchemas=new ArrayList<Schema>();
	private ArrayList<TransitionList> AllTransitions=new ArrayList<TransitionList>();
	private ArrayList<Schema> LevelizedSchemas=new ArrayList<Schema>();
	private ArrayList<TransitionList> LevelizedTransitions=new ArrayList<TransitionList>();
	private ArrayList<Table> longLivedTables=new ArrayList<Table>();
	private ArrayList<Table> mostUpdatedTables=new ArrayList<Table>();
	private ArrayList<Table> mostIntensiveTables=new ArrayList<Table>();
	private ArrayList<Table> assistantList=new ArrayList<Table>();
	private ArrayList<Table> assistantListLineChart=new ArrayList<Table>();
	private ArrayList<Table> percentageOfChangesAboutTables=new ArrayList<Table>();
	private ArrayList<Attribute> mostUpdatedAttributes=new ArrayList<Attribute>();
	private ArrayList<Integer> selectedRows=new ArrayList<Integer>();
	
	private Rotator rotator=null;
	private Rotator rotator1=null;
	private Rotator rotator2=null;
	
	private Visualization chart=null;
	private Visualization chart1=null;
	private Visualization l=null;
	private ChartPanel chartPanel=null;
	private ChartPanel chartPanelPie=null;
	private ChartPanel chartPanelPie2=null;
	
	private String[] finalColumns=null;
	private String[][] finalRows=null;
	private String currentProject=null;
	private String currentProjectDataFolder=null;
	
	private Float[][] mostIntensiveInsersions=null;
	private Float[][] mostIntensiveUpdates=null;
	private Float[][] mostIntensiveDeletions=null;
	
	private Integer[] segmentSize=new Integer[3];
	private int rotation=0;
	
	private Results resultsLLTR=new LongLivedTablesResults();
	private Results resultsPOCR=new PercentageOfChangesResults();
	private Results resultsMUTR=new MostUpdatedTablesResults();
	private Results resultsMUAR=new MostUpdatedAttributesResults();
	private Results resultsITFTR=new IntensiveTablesFromTwoSchemasResults();
	
	private Algorithm finalLongLivedTables=null;
	private Algorithm finalMostUpdatedTables=null;
	private Algorithm finalMostUpdatedAttributes=null;
	private Algorithm intensiveTables=null;
	private Algorithm changes=null;
	
	private boolean levelizedTable;
	
	
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
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpenProject = new JMenuItem("Open Project");
		mntmOpenProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String fileName=null;
				
				JFileChooser fcOpen1 = new JFileChooser();
				
				int returnVal = fcOpen1.showDialog(Gui.this, "Open");
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					
		            File file = fcOpen1.getSelectedFile();
		            System.out.println(file.toString());
		            fileName=file.toString();
				
				}
				else{
					return;
				}
				
				
				try {
					importData(fileName);
				} catch (IOException e) {
					//e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
					return;
				} catch (RecognitionException e) {
					
					//e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Something seems wrong with this file");
					return;
				}
				
			}
		});
		mnFile.add(mntmOpenProject);
		
		JMenuItem mntmExportDataImage = new JMenuItem("Export Image");
		mntmExportDataImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			
				Robot robot = null;
				try {
					robot = new Robot();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Rectangle r=new Rectangle(30, 30, 1300, 700);
				
				BufferedImage img=robot.createScreenCapture(r);
				
				try {
					ImageIO.write(img, "jpg", new File("1.jpg"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		mnFile.add(mntmExportDataImage);
		
		JMenu mnTable = new JMenu("Table");
		menuBar.add(mnTable);
		
		JMenuItem mntmShowLifetimeTable = new JMenuItem("Show Full Detailed LifeTime Table");
		mntmShowLifetimeTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!(currentProject==null)){
					TableConstruction table=new TableConstruction(AllSchemas, AllTransitions);
					final String[] columns=table.constructColumns();
					final String[][] rows=table.constructRows();
					segmentSize=table.getSegmentSize();
					finalColumns=columns;
					finalRows=rows;
					tabbedPane.setSelectedIndex(0);
					makeDetailedTable(columns,rows,true);
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
			}
		});
		
		JMenuItem mntmShowGeneralLifetime = new JMenuItem("Show General LifeTime Table");
		mntmShowGeneralLifetime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 if(!(currentProject==null)){
					TableConstruction table=new TableConstruction(AllSchemas, AllTransitions);
					final String[] columns=table.constructColumns();
					final String[][] rows=table.constructRows();
					segmentSize=table.getSegmentSize();
					finalColumns=columns;
					finalRows=rows;
					tabbedPane.setSelectedIndex(0);
					makeGeneralTable();
					
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
			}
		});
		mnTable.add(mntmShowGeneralLifetime);
		
		JMenuItem mntmShowLifetimeTable_1 = new JMenuItem("Show LifeTime Table With Selected Level");
		mntmShowLifetimeTable_1.addActionListener(new ActionListener() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void actionPerformed(ActionEvent arg0) {
				if(!(currentProject==null)){
					final JDialog  insertLevelDialog=new JDialog();
					insertLevelDialog.setBounds(400,350,200, 200);
					insertLevelDialog.getContentPane().setLayout(null);
					
					JLabel  label=new JLabel("Choose Level");
					label.setBounds(55, 10, 80, 30);
					insertLevelDialog.getContentPane().add(label);
					
					String[] levels={"3","4","5","6","7","8","9","10","11","12"};
					
					final JComboBox jComboBoxLevels=new JComboBox(levels);
					jComboBoxLevels.setBounds(67,45,60,22);
					jComboBoxLevels.setSelectedIndex(0);
					
					insertLevelDialog.getContentPane().add(jComboBoxLevels);
					
					JButton insertKOK=new JButton("OK");
					insertKOK.setBounds(65, 85 , 60, 20);
					
					insertKOK.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							
							String selectedLevel=(String)jComboBoxLevels.getSelectedItem();
							
							int level=Integer.parseInt(selectedLevel);
							
							ArrayList<Schema> versionsWithLevel=calculateNumberofVersions(level);
							
							LevelizedLifeTime levelizedLifeTime=new LevelizedLifeTime(versionsWithLevel);
							
							for(int i=0;i<versionsWithLevel.size();i++){
								System.out.println(versionsWithLevel.get(i).getName());
							}
							
							try {
								levelizedLifeTime.makeTransitions();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							};
							
							insertLevelDialog.setVisible(false);
							
							LevelizedSchemas=levelizedLifeTime.getLevelizedSchemas();
							LevelizedTransitions=levelizedLifeTime.getLevelizedTransitions();
							
							TableConstruction table=new TableConstruction(LevelizedSchemas, LevelizedTransitions);
							segmentSize=table.getSegmentSize();
							finalColumns=table.constructColumns();
							finalRows=table.constructRows();
							tabbedPane.setSelectedIndex(0);
							makeDetailedTable(finalColumns, finalRows,false);
						}
						
	
						
					});
					insertLevelDialog.getContentPane().add(insertKOK);
					
					insertLevelDialog.setVisible(true);
				
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
			}
		});
		mnTable.add(mntmShowLifetimeTable_1);
		mnTable.add(mntmShowLifetimeTable);
		
		JMenuItem mntmSwapRows = new JMenuItem("Swap Two Rows of Detailed Table (S)");
		mntmSwapRows.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(selectedRows.isEmpty()||selectedRows.size()==1){
					
					JOptionPane.showMessageDialog(null, "Choose two rows first!!!");
					
					return;
					
				}
				
				String[] tmpValues=new String[LifeTimeTable.getColumnCount()];
				for(int i=0; i<LifeTimeTable.getColumnCount(); i++){
					
					tmpValues[i]=(String) LifeTimeTable.getValueAt(selectedRows.get(0), i);
					
					
				}
				
				for(int i=0; i<LifeTimeTable.getColumnCount(); i++){
					finalRows[selectedRows.get(0)][i]=(String) LifeTimeTable.getValueAt(selectedRows.get(1), i);
				}
				
				for(int i=0; i<LifeTimeTable.getColumnCount(); i++){
					finalRows[selectedRows.get(1)][i]=tmpValues[i];
				}
				
				makeDetailedTable(finalColumns,finalRows,true);
				
				selectedRows=new ArrayList<Integer>();
				
				
			}
		});
		mnTable.add(mntmSwapRows);
		
		JMenu mnStatistics = new JMenu("Statistics");
		menuBar.add(mnStatistics);
		
		JMenuItem mntmShowLongLivedTables = new JMenuItem("Show K Long Lived Tables");
		mntmShowLongLivedTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!(currentProject==null)){
					assistantList=new ArrayList<Table>();
					longLivedTables=new ArrayList<Table>();
					
					final JDialog  insertKDialog=new JDialog();
					insertKDialog.setBounds(400,350,200, 200);
					insertKDialog.getContentPane().setLayout(null);
					
					JLabel  label=new JLabel("Insert K");
					label.setBounds(67, 10, 80, 30);
					insertKDialog.getContentPane().add(label);
					
					final JTextField insertKTextField=new JTextField("");
					insertKTextField.setBounds(67, 55, 40, 20);
					insertKDialog.getContentPane().add(insertKTextField);
					
					JButton insertKOK=new JButton("OK");
					insertKOK.setBounds(62,120 , 60, 20);
					
					insertKOK.addActionListener(new ActionListener() {
						@SuppressWarnings("unchecked")
						public void actionPerformed(ActionEvent arg0) {
							int kNumber;
							
							String kString=insertKTextField.getText();
							
							if(kString.equals("")){
								JOptionPane.showMessageDialog(null, "K Cannot Be Empty");
								return;
							}
							try{
				                kNumber=Integer.parseInt(kString);
				            } catch(NumberFormatException nfe) {
				                JOptionPane.showMessageDialog(null, "K must be numeric");
				                return;
				            }
							
							insertKDialog.setVisible(false);
							
							tabbedPane.setSelectedIndex(1);
							
							finalLongLivedTables=new LongLivedTablesAlgo(AllSchemas,kNumber);
							
							resultsLLTR=new LongLivedTablesResults();
							resultsLLTR=(LongLivedTablesResults) finalLongLivedTables.compute();
							
							longLivedTables=resultsLLTR.getResults();
							
						
							showKLongLivedTables();
							
							if(longLivedTables.size()==0){
								JOptionPane.showMessageDialog(null, "Calculate K Long Lived Tables first");
								return;
							}
							
							
							
							chart=new LongLivedTablesVisualization();
							chart.draw(resultsLLTR);
							chartPanel=chart.getChart();
							
							
							
							statistics.add(chartPanel);
							statistics.revalidate();
							statistics.repaint();
							
						
						}
	
						
					});
					insertKDialog.getContentPane().add(insertKOK);
					
					
					insertKDialog.setVisible(true);
				
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
				
				
			}
		});
		mnStatistics.add(mntmShowLongLivedTables);
		
		JMenuItem mntmShowKMostUpdatedTable = new JMenuItem("Show K Most Updated Tables");
		mntmShowKMostUpdatedTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!(currentProject==null)){
					final JDialog  insertKDialog=new JDialog();
					insertKDialog.setBounds(400,350,200, 200);
					insertKDialog.getContentPane().setLayout(null);
					
					JLabel  label=new JLabel("Insert K");
					label.setBounds(67, 10, 80, 30);
					insertKDialog.getContentPane().add(label);
					
					final JTextField insertKTextField=new JTextField("");
					insertKTextField.setBounds(67, 55, 40, 20);
					insertKDialog.getContentPane().add(insertKTextField);
					
					JButton insertKOK=new JButton("OK");
					insertKOK.setBounds(62,120 , 60, 20);
					
					insertKOK.addActionListener(new ActionListener() {
						@SuppressWarnings("unchecked")
						public void actionPerformed(ActionEvent arg0) {
							int kNumber;
							String kString=insertKTextField.getText();
							
							if(kString.equals("")){
								JOptionPane.showMessageDialog(null, "K Cannot Be Empty");
								return;
							}
							try{
				                kNumber=Integer.parseInt(kString);
				            } catch(NumberFormatException nfe) {
				                JOptionPane.showMessageDialog(null, "K must be numeric");
				                return;
				            }
							
							insertKDialog.setVisible(false);
							
							TableConstruction table=new TableConstruction(AllSchemas, AllTransitions);
							table.constructColumns();
							table.constructRows();
							
							tabbedPane.setSelectedIndex(1);
							
							finalMostUpdatedTables=new MostUpdatedTablesAlgo(AllSchemas, kNumber);
							
							resultsMUTR=new MostUpdatedTablesResults();
							resultsMUTR=(MostUpdatedTablesResults) finalMostUpdatedTables.compute();
							
							mostUpdatedTables=resultsMUTR.getResults();
							
							
						
							showKMostUpdatedTables();
							
							if(mostUpdatedTables.size()==0){
								JOptionPane.showMessageDialog(null, "Calculate K Most Updated Tables first");
								return;
							}
							
							
							try {
								l = new MostUpdatedTablesVisualization(AllSchemas,currentProjectDataFolder);
								l.draw(resultsMUTR);
								//l.calculateChangesforTheseVersions();
								
								chartPanel=l.getChart();
								
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							JLabel searchVersion=new JLabel("Search Version With Version ID");
							searchVersion.setBounds(10, 320, 180, 30);
							final JTextField id=new JTextField();
							id.setBounds(48, 360, 50, 20);
							JButton search=new JButton("Search");
							search.setBounds(30, 405, 80, 30);
							search.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									int idNumber;
									String k=id.getText();
									try{
										idNumber=Integer.parseInt(k);
										
									}
									catch(Exception ex){
										JOptionPane.showMessageDialog(null,"id must be Numeric !!!");
										return;
									}
									
									JOptionPane.showMessageDialog(null,AllSchemas.get(idNumber).getName() );
									
								}
						
							});
							
							statistics.add(search);
							statistics.add(searchVersion);
							statistics.add(id);
							statistics.add(chartPanel);
							statistics.revalidate();
							statistics.repaint();
						
						}
	
						
					});
					insertKDialog.getContentPane().add(insertKOK);
	
					insertKDialog.setVisible(true);
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
				
			}
		});
		mnStatistics.add(mntmShowKMostUpdatedTable);
		
		JMenuItem mntmShowKMostUpdatedAttributes = new JMenuItem("Show K Most Updated Attributes");
		mntmShowKMostUpdatedAttributes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!(currentProject==null)){
					final JDialog  insertKDialog=new JDialog();
					insertKDialog.setBounds(400,350,200, 200);
					insertKDialog.getContentPane().setLayout(null);
					
					JLabel  label=new JLabel("Insert K");
					label.setBounds(67, 10, 80, 30);
					insertKDialog.getContentPane().add(label);
					
					final JTextField insertKTextField=new JTextField("");
					insertKTextField.setBounds(67, 55, 40, 20);
					insertKDialog.getContentPane().add(insertKTextField);
					
					JButton insertKOK=new JButton("OK");
					insertKOK.setBounds(62,120 , 60, 20);
					
					insertKOK.addActionListener(new ActionListener() {
						@SuppressWarnings("unchecked")
						public void actionPerformed(ActionEvent arg0) {
							int kNumber;
							String kString=insertKTextField.getText();
							
							if(kString.equals("")){
								JOptionPane.showMessageDialog(null, "K Cannot Be Empty");
								return;
							}
							try{
				                kNumber=Integer.parseInt(kString);
				            } catch(NumberFormatException nfe) {
				                JOptionPane.showMessageDialog(null, "K must be numeric");
				                return;
				            }
							
							insertKDialog.setVisible(false);
							
							tabbedPane.setSelectedIndex(1);
							
							finalMostUpdatedAttributes=new MostUpdatedAttributesAlgo(AllSchemas, AllTransitions,kNumber);
							
							resultsMUAR=new MostUpdatedAttributesResults();
							resultsMUAR=(MostUpdatedAttributesResults) finalMostUpdatedAttributes.compute();
							
							mostUpdatedAttributes=resultsMUAR.getResults();
							
						
							showKMostUpdatedAttributes();
							
							if(mostUpdatedAttributes.size()==0){
								JOptionPane.showMessageDialog(null, "Calculate K Most Updated Attributes first");
								return;
							}
							
							/*for(int i=0;i<7;i++){
								assistantList.add(elderTables.get(i));
							}*/
							
							chart1=new MostUpdatedAttributesVisualization();
							chart1.draw(resultsMUAR);
							chartPanel=chart1.getChart();
							
							
							
							statistics.add(chartPanel);
							statistics.revalidate();
							statistics.repaint();
						
						}
	
						
					});
					insertKDialog.getContentPane().add(insertKOK);
	
					insertKDialog.setVisible(true);
				}
				else{
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
				}
			}
		});
		mnStatistics.add(mntmShowKMostUpdatedAttributes);
		
		JMenuItem mntmShowKMost = new JMenuItem("Show K Most Intensive Tables Between Two Schemas");
		mntmShowKMost.addActionListener(new ActionListener() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void actionPerformed(ActionEvent arg0) {
				
				if(currentProject==null){
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
					
				}
				
				final JDialog  insertKDialog=new JDialog();
				insertKDialog.setBounds(400,350,400, 200);
				insertKDialog.getContentPane().setLayout(null);
				
				JLabel  label=new JLabel("From Version");
				label.setBounds(10, 0, 80, 30);
				insertKDialog.getContentPane().add(label);
				
				String[] allSchemasNames=new String[AllSchemas.size()];
				
				for(int i=0;i<AllSchemas.size();i++){
					allSchemasNames[i]=AllSchemas.get(i).getName();
				}
				
				final JComboBox jComboBoxAllSchemas=new JComboBox(allSchemasNames);
				jComboBoxAllSchemas.setBounds(10,30,180,30);
				jComboBoxAllSchemas.setSelectedIndex(0);
				
				insertKDialog.getContentPane().add(jComboBoxAllSchemas);
				
				JLabel  label2=new JLabel("To Version");
				label2.setBounds(10, 60, 80, 30);
				insertKDialog.getContentPane().add(label2);
				
				
				final JComboBox jComboBoxAllSchemas2=new JComboBox(allSchemasNames);
				jComboBoxAllSchemas2.setBounds(10,90,180,30);
				jComboBoxAllSchemas2.setSelectedIndex(0);
				
				insertKDialog.getContentPane().add(jComboBoxAllSchemas2);
				
				
				JLabel  label3=new JLabel("Insert K");
				label3.setBounds(250, 10, 80, 30);
				insertKDialog.getContentPane().add(label3);
				
				final JTextField insertKTextField=new JTextField("");
				insertKTextField.setBounds(250, 40, 40, 20);
				insertKDialog.getContentPane().add(insertKTextField);
				
				JButton insertKOK=new JButton("OK");
				insertKOK.setBounds(250,90 , 60, 20);
				
				insertKOK.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						int kNumber;
						String kString=insertKTextField.getText();
						
						if(kString.equals("")){
							JOptionPane.showMessageDialog(null, "K Cannot Be Empty");
							return;
						}
						try{
			                kNumber=Integer.parseInt(kString);
			            } catch(NumberFormatException nfe) {
			                JOptionPane.showMessageDialog(null, "K must be numeric");
			                return;
			            }
						
						if(jComboBoxAllSchemas.getSelectedItem()==null || jComboBoxAllSchemas2.getSelectedItem()==null){
							JOptionPane.showMessageDialog(null, " You must select both FROM and TO versions ");
							return;
						}
						
						
						String fromVersion=(String)jComboBoxAllSchemas.getSelectedItem();
						String toVersion=(String)jComboBoxAllSchemas2.getSelectedItem();
						
						Schema firstSchema=null;
						Schema secondSchema=null;
						
						for(int i=0; i<AllSchemas.size(); i++){
							if(fromVersion.equals(AllSchemas.get(i).getName())){
								firstSchema=AllSchemas.get(i);
								break;
							}
						}
						
						for(int i=0; i<AllSchemas.size(); i++){
							if(toVersion.equals(AllSchemas.get(i).getName())){
								secondSchema=AllSchemas.get(i);
								break;
							}
						}
						
						insertKDialog.setVisible(false);
						
						tabbedPane.setSelectedIndex(1);
						
						IntensiveTablesFromTwoSchemasAlgo  finalmostIntensiveTables=new IntensiveTablesFromTwoSchemasAlgo(firstSchema,
								secondSchema,currentProjectDataFolder , kNumber);
						try {
							finalmostIntensiveTables.findDifferenciesFromTwoSchemas();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						intensiveTables=finalmostIntensiveTables;
						
						resultsITFTR=new IntensiveTablesFromTwoSchemasResults();
						resultsITFTR=(IntensiveTablesFromTwoSchemasResults) intensiveTables.compute();
						mostIntensiveTables=resultsITFTR.getResults();
						
						
					
						showKMostIntensiveTablesBetweenTwoSchemas(fromVersion,toVersion);
						
						if(mostIntensiveTables.size()==0){
							JOptionPane.showMessageDialog(null, "Calculate K Most Intensive Tables first");
							return;
						}
						
						Visualization k=new IntensiveTablesFromTwoSchemasVisualization();
						k.draw(resultsITFTR);
						chartPanel=k.getChart();
						
						statistics.add(chartPanel);
						//statistics.add(chartPanelPie);
						//statistics.add(chartPanelPie2);
						statistics.revalidate();
						statistics.repaint();
					
					}

					
				});
				insertKDialog.getContentPane().add(insertKOK);
				
				
				
				insertKDialog.setVisible(true);
				
			}
		});
		mnStatistics.add(mntmShowKMost);
		
		JMenuItem mntmShowKMost_1 = new JMenuItem("Show K Most Intensive Changes by Percentage");
		mntmShowKMost_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(currentProject==null){
					JOptionPane.showMessageDialog(null, "Select a Project first");
					return;
					
				}
				final JDialog  insertKDialog=new JDialog();
				insertKDialog.setBounds(400,350,200, 200);
				insertKDialog.getContentPane().setLayout(null);
				
				JLabel  label=new JLabel("Insert K");
				label.setBounds(67, 10, 80, 30);
				insertKDialog.getContentPane().add(label);
				
				final JTextField insertKTextField=new JTextField("");
				insertKTextField.setBounds(67, 55, 40, 20);
				insertKDialog.getContentPane().add(insertKTextField);
				
				JButton insertKOK=new JButton("OK");
				insertKOK.setBounds(62,120 , 60, 20);
				
				insertKOK.addActionListener(new ActionListener() {
					@SuppressWarnings("unchecked")
					public void actionPerformed(ActionEvent arg0) {
						int kNumber;
						String kString=insertKTextField.getText();
						
						if(kString.equals("")){
							JOptionPane.showMessageDialog(null, "K Cannot Be Empty");
							return;
						}
						try{
			                kNumber=Integer.parseInt(kString);
			            } catch(NumberFormatException nfe) {
			                JOptionPane.showMessageDialog(null, "K must be numeric");
			                return;
			            }
						
						insertKDialog.setVisible(false);
						
						tabbedPane.setSelectedIndex(1);
						
						changes=new PercentageOfChangesAlgo(AllSchemas, AllTransitions);
						
						resultsPOCR=new PercentageOfChangesResults();
						resultsPOCR=(PercentageOfChangesResults) changes.compute();
						
						percentageOfChangesAboutTables=resultsPOCR.getResults();
					
						showKMostIntensiveChanges(kNumber);
						
						if(mostIntensiveInsersions==null){
							JOptionPane.showMessageDialog(null, "Calculate K Most Intensive Changes by percentage first");
							return;
						}
						
						
						
						Visualization pie=new PercentageOfChangesVisualization(mostIntensiveInsersions,"Insersions");
						
						PercentageOfChangesVisualization tmp=(PercentageOfChangesVisualization) pie;
						
						pie.draw(resultsPOCR);
						chartPanel=pie.getChart();
						
						rotator=tmp.getRotator();
						
						pie=new PercentageOfChangesVisualization(mostIntensiveUpdates,"Updates");
						
						tmp=(PercentageOfChangesVisualization) pie;
						pie.draw(resultsPOCR);

						chartPanelPie=pie.getChart();
						
						rotator1=tmp.getRotator();
						
						pie=new PercentageOfChangesVisualization(mostIntensiveDeletions,"Deletions");
						
						tmp=(PercentageOfChangesVisualization) pie;
						
						pie.draw(resultsPOCR);

						chartPanelPie2=pie.getChart();
						
						rotator2=tmp.getRotator();
						
						
						rotation=1;
						
						statistics.add(chartPanel);
						statistics.add(chartPanelPie);
						statistics.add(chartPanelPie2);
						statistics.revalidate();
						statistics.repaint();
					
					}

					
				});
				insertKDialog.getContentPane().add(insertKOK);

				insertKDialog.setVisible(true);
				
				
			}
		});
		mnStatistics.add(mntmShowKMost_1);
		
		JButton buttonHelp=new JButton("Help");
		buttonHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String message ="To open a project, you must select a .txt file that contains the names ONLY of " +
									"the SQL files of the dataset that you want to use."+"\n" +
									"The .txt file must have EXACTLY the same name with the folder " +
									"that contains the DDL Scripts of the dataset."+ "\n" +
									"Both .txt file and dataset folder must be to the project's folder.";
				JOptionPane.showMessageDialog(null,message); 				
			}
		});
		buttonHelp.setBounds(900,900 , 80, 40);
		menuBar.add(buttonHelp);
		
		
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
				.addGap(0, 703, Short.MAX_VALUE)
		);
		lifeTimePanel.setLayout(gl_lifeTimePanel);
		
		
		tabbedPane.addTab("Statistics", null, statistics, null);
		
		GroupLayout gl_statistics = new GroupLayout(statistics);
		gl_statistics.setHorizontalGroup(
			gl_statistics.createParallelGroup(Alignment.LEADING)
				.addGap(0, 1469, Short.MAX_VALUE)
		);
		gl_statistics.setVerticalGroup(
			gl_statistics.createParallelGroup(Alignment.LEADING)
				.addGap(0, 703, Short.MAX_VALUE)
		);
		statistics.setLayout(gl_statistics);
		
		contentPane.setLayout(gl_contentPane);
		
		pack();
		setBounds(30, 30, 1300, 700);

		
	}
	
	private void makeGeneralTable() {
		
		int numberOfColumns=finalRows[0].length;
		int numberOfRows=finalRows.length;
		
		selectedRows=new ArrayList<Integer>();
		
		String[][] rows=new String[numberOfRows][numberOfColumns];
		
		for(int i=0; i<numberOfRows; i++){
			
			rows[i][0]=finalRows[i][0];
			
		}
		
		generalModel=new MyTableModel(finalColumns, rows);
		
		JTable generalTable=new JTable(generalModel);
		
		generalTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		for(int i=0; i<generalTable.getColumnCount(); i++){
			if(i==0){
				generalTable.getColumnModel().getColumn(0).setPreferredWidth(150);
				generalTable.getColumnModel().getColumn(0).setMaxWidth(150);
				generalTable.getColumnModel().getColumn(0).setMinWidth(150);
			}
			else{
				generalTable.getColumnModel().getColumn(i).setPreferredWidth(1);
				generalTable.getColumnModel().getColumn(i).setMaxWidth(1);
				generalTable.getColumnModel().getColumn(i).setMinWidth(1);
			}
		}
		
		generalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    
			private static final long serialVersionUID = 1L;

			@Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        String tmpValue=finalRows[row][column];
		        String columnName=table.getColumnName(column);
		        Color fr=new Color(0,0,0);
		        c.setForeground(fr);
		      
		        try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;
		        	
		        	if(columnName.equals("I")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0&& numericValue<=segmentSize[0]){
		        			
		        			insersionColor=new Color(193,255,193);
			        	}
		        		else if(numericValue>segmentSize[0] && numericValue<=2*segmentSize[0]){
		        			insersionColor=new Color(84,255,159);
		        		}
		        		else if(numericValue>2*segmentSize[0] && numericValue<=3*segmentSize[0]){
		        			
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
		        	}
		        	
		        	if(columnName.equals("D")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0 && numericValue<=segmentSize[2]){
		        			
		        			insersionColor=new Color(255,106,106);
			        	}
		        		else if(numericValue>segmentSize[2] && numericValue<=2*segmentSize[2]){
		        			insersionColor=new Color(255,0,0);
		        		}
		        		else if(numericValue>2*segmentSize[2] && numericValue<=3*segmentSize[2]){
		        			
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
		});
		
		generalTable.addMouseListener(new MouseAdapter() {
			
			   public void mouseClicked(MouseEvent e) {
			      if (e.getClickCount() == 2) {
			         JTable target = (JTable)e.getSource();
			         
			         int row = target.getSelectedRow();
			         int column = target.getSelectedColumn();
			         
			         makeDetailedTable(finalColumns, finalRows,levelizedTable);
			         
			         LifeTimeTable.setCellSelectionEnabled(true);
			         
			         LifeTimeTable.changeSelection(row, column, false, false);
			         LifeTimeTable.requestFocus();
			         
			      }
			   }
		});
		
		
		LifeTimeTable=generalTable;
		
		tmpScrollPane.setViewportView(LifeTimeTable);
		tmpScrollPane.setAlignmentX(0);
		tmpScrollPane.setAlignmentY(0);
        tmpScrollPane.setBounds(0,0,1250,600);
        tmpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tmpScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
		lifeTimePanel.setCursor(getCursor());
		lifeTimePanel.add(tmpScrollPane);
		
		
		
	}

	private void makeDetailedTable(String[] columns , String[][] rows, final boolean levelized){
		
		levelizedTable=levelized;
		detailedModel=new MyTableModel(columns,rows);
		
		final JTable tmpLifeTimeTable= new JTable(detailedModel);
		
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
		        
		        try{
		        	int numericValue=Integer.parseInt(tmpValue);
		        	Color insersionColor=null;
		        	
		        	if(columnName.equals("I")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0 && numericValue<=segmentSize[0]){
		        			
		        			insersionColor=new Color(193,255,193);
			        	}
		        		else if(numericValue>segmentSize[0] && numericValue<=2*segmentSize[0]){
		        			insersionColor=new Color(84,255,159);
		        		}
		        		else if(numericValue>2*segmentSize[0] && numericValue<=3*segmentSize[0]){
		        			
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
		        		else if(numericValue>0 && numericValue<=segmentSize[1]){
		        			
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
		        	}
		        	
		        	if(columnName.equals("D")){
		        		if(numericValue==0){
		        			insersionColor=new Color(255,231,186);
		        		}
		        		else if(numericValue>0 && numericValue<=segmentSize[2]){
		        			
		        			insersionColor=new Color(255,106,106);
			        	}
		        		else if(numericValue>segmentSize[2] && numericValue<=2*segmentSize[2]){
		        			insersionColor=new Color(255,0,0);
		        		}
		        		else if(numericValue>2*segmentSize[2] && numericValue<=3*segmentSize[2]){
		        			
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
	    tmpLifeTimeTable.getSelectionModel().addListSelectionListener(new RowListener());
	    tmpLifeTimeTable.getColumnModel().getSelectionModel().addListSelectionListener(new ColumnListener());
	    tmpLifeTimeTable.addKeyListener(new MyKeyListener());
	    
	    
	    tmpLifeTimeTable.addMouseListener(new MouseAdapter() {
			
			   public void mouseClicked(MouseEvent e) {
			      if (e.getClickCount() == 2) {
			    	  JTable target = (JTable)e.getSource();
				         
				      int row = target.getSelectedRow();
				      int column = target.getSelectedColumn();
				         
				      makeGeneralTable();
				         
				      LifeTimeTable.setCellSelectionEnabled(true);
				         
				      LifeTimeTable.changeSelection(row, column, false, false);
				      LifeTimeTable.requestFocus();
			    	  
			         
			         
			      }
			   }
		});
	    
	    
	    LifeTimeTable=tmpLifeTimeTable;
	
		tmpScrollPane.setViewportView(LifeTimeTable);
		tmpScrollPane.setAlignmentX(0);
		tmpScrollPane.setAlignmentY(0);
		tmpScrollPane.setBounds(0,0,1250,600);
        tmpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tmpScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
       
		lifeTimePanel.setCursor(getCursor());
		lifeTimePanel.add(tmpScrollPane);
		
		
	}
	
	private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            
            int selectedRow=LifeTimeTable.getSelectedRow();
            
            selectedRows.add(selectedRow);
     
        }
    }
	
	private class ColumnListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
           
        }
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public class MyKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			
			if(selectedRows.isEmpty()||selectedRows.size()==1){
				
				JOptionPane.showMessageDialog(null, "Choose two rows first!!!");
				
				return;
				
			}
			
			int keyCode = e.getKeyCode();
			if(keyCode == KeyEvent.VK_S){
				String[] tmpValues=new String[LifeTimeTable.getColumnCount()];
				for(int i=0; i<LifeTimeTable.getColumnCount(); i++){
					tmpValues[i]=(String) LifeTimeTable.getValueAt(selectedRows.get(0), i);
					
					
				}
				
				for(int i=0; i<LifeTimeTable.getColumnCount(); i++){
					finalRows[selectedRows.get(0)][i]=(String) LifeTimeTable.getValueAt(selectedRows.get(1), i);
				}
				
				for(int i=0; i<LifeTimeTable.getColumnCount(); i++){
					finalRows[selectedRows.get(1)][i]=tmpValues[i];
				}
				
				makeDetailedTable(finalColumns,finalRows,true);
				
				selectedRows=new ArrayList<Integer>();
			}	
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private void importData(String fileName) throws IOException, RecognitionException {
		
		ImportData filesToImportData=new ImportData(fileName);
		
		filesToImportData.loadDataset();
		
		AllSchemas=filesToImportData.getAllSchemas();
		AllTransitions=filesToImportData.getAllTransitions();
        System.out.println(fileName);

		currentProject=fileName;
		currentProjectDataFolder=filesToImportData.getDataFolder();
		
	}
	
	@SuppressWarnings("unchecked")
	public void showKLongLivedTables(){
		
		labelLongLivedTables.setText("");
		labelLongLivedTables.setText(" Long Lived Tables");
		labelLongLivedTables.setBounds(15,0,170,50);
		
		jListKLongLivedTables.setBounds(0, 0, 180, 200);
		Color color=new Color(000,191,255);
		jListKLongLivedTables.setBackground(color);
		jListKLongLivedTables.setModel(listModelLongLivedTables);
		jListKLongLivedTables.setFont(new Font("PF Isotext Pro",Font.BOLD,16));
		
		MouseListener mouseListener = new MouseAdapter() {  
			public void mouseClicked(MouseEvent e) {  
				if (e.getClickCount() == 2) {  
					int index = jListKLongLivedTables.locationToIndex(e.getPoint());  
					System.out.println("Double clicked on Item " + index);
					if(assistantList.size()!=0){
						assistantList.remove(assistantList.size()-1);
						assistantList.add(longLivedTables.get(index));
						
						statistics.remove(chartPanel);
						
						chart=new LongLivedTablesVisualization();
						chart.draw(resultsLLTR);
						chartPanel=chart.getChart();
						
						statistics.revalidate();
						statistics.repaint();
						statistics.add(chartPanel);
					}
				}  
			}  
		};  
		jListKLongLivedTables.addMouseListener(mouseListener); 
		
		jScrollPaneKLongLivedTables=new JScrollPane(jListKLongLivedTables,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneKLongLivedTables.setBounds(20, 40, 180, 200);
		
		statistics.removeAll();
		statistics.revalidate();
		statistics.repaint();
		
		statistics.add(labelLongLivedTables);
		statistics.add(jScrollPaneKLongLivedTables);
		
		for(int i=0; i<longLivedTables.size(); i++){
			listModelLongLivedTables.removeAllElements();
		}
		
		for(int i=0; i<longLivedTables.size(); i++){
			listModelLongLivedTables.add(i, longLivedTables.get(i).getName());
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void showKMostUpdatedTables(){
		
		labelMostUpdatedTables.setText("");
		labelMostUpdatedTables.setText(" Most Updated Tables");
		labelMostUpdatedTables.setBounds(15,0,200,50);
		
		jListKMostUpdatedTables.setBounds(0, 0, 230, 200);
		Color color=new Color(000,191,255);
		jListKMostUpdatedTables.setBackground(color);
		jListKMostUpdatedTables.setModel(listModelMostUpdatedTables);
		jListKMostUpdatedTables.setFont(new Font("PF Isotext Pro",Font.BOLD,16));
		 
		
		
		MouseListener mouseListener = new MouseAdapter() {  
			public void mouseClicked(MouseEvent e) {  
				if (e.getClickCount() == 2) {  
					int index = jListKMostUpdatedTables.locationToIndex(e.getPoint());  
					System.out.println("Double clicked on Item " + index);
					if(assistantListLineChart.size()!=0){
						assistantListLineChart.remove(assistantListLineChart.size()-1);
						ArrayList<Table> tmp=assistantListLineChart;
						
						assistantListLineChart=new ArrayList<Table>();
						
						for(int k=0;k<tmp.size();k++){
							assistantListLineChart.add(tmp.get(k));
						}
						
						int found=0;
						
						for(int i=0;i<assistantListLineChart.size();i++){
							if(mostUpdatedTables.get(index).equals(assistantListLineChart.get(i))){
								found=1;
							}
						}
						
						if(found==0){
							assistantListLineChart.add(mostUpdatedTables.get(index));
						}
						else{
							found=0;
						}
						
						statistics.remove(chartPanel);
						
						try {
							l=new MostUpdatedTablesVisualization(AllSchemas,currentProjectDataFolder);
							l.draw(resultsMUTR);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						chartPanel=l.getChart();
						
						statistics.revalidate();
						statistics.repaint();
						statistics.add(chartPanel);
					}
				}  
			}  
		};
		jListKMostUpdatedTables.addMouseListener(mouseListener); 
		
		
		jScrollPaneKMostUpdatedTables=new JScrollPane(jListKMostUpdatedTables,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneKMostUpdatedTables.setBounds(20, 40, 230, 200);
		
		statistics.removeAll();
		statistics.revalidate();
		statistics.repaint();
		
		statistics.add(labelMostUpdatedTables);
		statistics.add(jScrollPaneKMostUpdatedTables);
		
		for(int i=0; i<mostUpdatedTables.size(); i++){
			listModelMostUpdatedTables.removeAllElements();
		}
		
		for(int i=0; i<mostUpdatedTables.size(); i++){
			listModelMostUpdatedTables.add(i, mostUpdatedTables.get(i).getName());
		}
		
	}
	
	public void showKMostUpdatedAttributes(){
		
		labelMostUpdatedAttributes.setText("");
		labelMostUpdatedAttributes.setText(" Most Updated Attributes");
		labelMostUpdatedAttributes.setBounds(15,0,200,50);
		
		
		jTextAreaMostUpdatedAttributes.setEditable(false);
		jTextAreaMostUpdatedAttributes.setBounds(0, 1, 600, 300);
		Color color=new Color(000,191,255);
		jTextAreaMostUpdatedAttributes.setBackground(color);
		jTextAreaMostUpdatedAttributes.setTabSize(20);
		jTextAreaMostUpdatedAttributes.setFont(new Font("PF Isotext Pro",Font.BOLD,16));
		
		String assistant="Attribute Name"+"\tNumber Of Changes"+"\tExists In Table";
		for(int i=0; i<mostUpdatedAttributes.size(); i++){
			
			assistant=assistant+"\n"+mostUpdatedAttributes.get(i).getName()+"\t"
					+ mostUpdatedAttributes.get(i).getTotalAttributeChanges()
					+"\t"+mostUpdatedAttributes.get(i).getTable().getName();
			
		}
		
		jTextAreaMostUpdatedAttributes.setText(assistant);
		
		jScrollPaneKMostUpdatedAttributes=new JScrollPane(jTextAreaMostUpdatedAttributes,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneKMostUpdatedAttributes.setBounds(20, 40, 600, 300);
		
		statistics.removeAll();
		statistics.revalidate();
		statistics.repaint();
		
		statistics.add(labelMostUpdatedAttributes);
		statistics.add(jScrollPaneKMostUpdatedAttributes);
		
	}
	
	public void showKMostIntensiveTablesBetweenTwoSchemas(String firstSchema,String secondSchema){
		
		
		
		labelMostIntensiveTables.setText("");
		labelMostIntensiveTables.setText(" Most Intensive Tables From Version "+firstSchema+" To Version "+secondSchema);
		labelMostIntensiveTables.setBounds(15,0,450,50);
		
		
		jTextAreaMostIntensiveTables.setEditable(false);
		jTextAreaMostIntensiveTables.setBounds(0, 1,450, 300);
		Color color=new Color(000,191,255);
		jTextAreaMostIntensiveTables.setBackground(color);
		jTextAreaMostIntensiveTables.setTabSize(20);
		jTextAreaMostIntensiveTables.setFont(new Font("PF Isotext Pro",Font.BOLD,16));
		
		String assistant="Table Name"+"\tNumber Of Changes";
		for(int i=0; i<mostIntensiveTables.size(); i++){
			
			assistant=assistant+"\n"+mostIntensiveTables.get(i).getName()+"\t"
					+ mostIntensiveTables.get(i).getCurrentChanges();
			
		}
		
		jTextAreaMostIntensiveTables.setText(assistant);
		
		jScrollPaneKMostIntensiveTables=new JScrollPane(jTextAreaMostIntensiveTables,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneKMostIntensiveTables.setBounds(20, 40, 450, 300);
		
		statistics.removeAll();
		statistics.revalidate();
		statistics.repaint();
		
		statistics.add(labelMostIntensiveTables);
		statistics.add(jScrollPaneKMostIntensiveTables);
		
		
	}
	
	public void showKMostIntensiveChanges(int k){
		
		
		buttonStop=new JButton("");
		buttonStop.setBounds(650, 10, 40, 40);
		buttonStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(rotation==1){
					rotator.stop();
					rotator1.stop();
					rotator2.stop();
					rotation=0;
				}
				else{
					rotator.start();
					rotator1.start();
					rotator2.start();
					rotation=1;
				}
				
			}
			
		});
		
		mostIntensiveInsersions=new Float[k][2];
		mostIntensiveUpdates=new Float[k][2];
		mostIntensiveDeletions=new Float[k][2];
		
		//Sort Percentage of Insersions ********************
		for(int i=0; i<k; i++){
			mostIntensiveInsersions[i][0]=(float) i;
			mostIntensiveInsersions[i][1]=(float) 0;
		}
		
		for(int i=0;i<percentageOfChangesAboutTables.size();i++){
			
			ArrayList<AssistantPercentageClassAlgo> assistant=percentageOfChangesAboutTables.get(i).getTransitions();
			
				for(int j=0; j<assistant.size(); j++){
					
					float min = mostIntensiveInsersions[0][1];
					int minPosition=0;
					for(int l=0; l<k; l++){
						if(mostIntensiveInsersions[l][1]<min){
							min=mostIntensiveInsersions[l][1];
							minPosition=l;
							
						}
					}
					if(assistant.get(j).getInsersionsPercentage()>mostIntensiveInsersions[minPosition][1]){
						mostIntensiveInsersions[minPosition][1]=assistant.get(j).getInsersionsPercentage();
						mostIntensiveInsersions[minPosition][0]=(float) i;
					}
					
			
				}
				
		}
		
		Arrays.sort(mostIntensiveInsersions, new Comparator<Float[]>() {
		    @Override
		    public int compare(Float[] s1, Float[] s2) {
		        Float t1 = s1[1];
		        Float t2 = s2[1];
		        
		        return t2.compareTo(t1);
		        
		    }
		});
		
		for(int i=0;i<k;i++){
			
			int l=(int)(Math.round(mostIntensiveInsersions[i][0]));
			System.out.println("Insersions "+percentageOfChangesAboutTables.get(l).getName()+" "+mostIntensiveInsersions[i][1]);
		}
		
		//Sort Percentage of Deletions **********************
		for(int i=0; i<k; i++){
			mostIntensiveUpdates[i][0]=(float) i;
			mostIntensiveUpdates[i][1]=(float) 0;
		}
		
		for(int i=0;i<percentageOfChangesAboutTables.size();i++){
			
			ArrayList<AssistantPercentageClassAlgo> assistant=percentageOfChangesAboutTables.get(i).getTransitions();
			
				for(int j=0; j<assistant.size(); j++){
					float min = mostIntensiveUpdates[0][1];
					int minPosition=0;
					for(int l=0; l<k; l++){
						if(mostIntensiveUpdates[l][1]<min){
							min=mostIntensiveUpdates[l][1];
							minPosition=l;
						}
					}
					if(assistant.get(j).getUpdatesPercentage()>mostIntensiveUpdates[minPosition][1]){
						mostIntensiveUpdates[minPosition][1]=assistant.get(j).getUpdatesPercentage();
						mostIntensiveUpdates[minPosition][0]=(float) i;
					}
					
			
				}
				
		}
		
		Arrays.sort(mostIntensiveUpdates, new Comparator<Float[]>() {
		    @Override
		    public int compare(Float[] s1, Float[] s2) {
		        Float t1 = s1[1];
		        Float t2 = s2[1];
		        
		        return t2.compareTo(t1);
		        
		    }
		});
		
		for(int i=0;i<k;i++){
			
			int l=(int)(Math.round(mostIntensiveUpdates[i][0]));
			System.out.println("Updates "+percentageOfChangesAboutTables.get(l).getName()+" "+mostIntensiveUpdates[i][1]);
		}
		
		//Sort Percentage of Deletions   ********************
		for(int i=0; i<k; i++){
			mostIntensiveDeletions[i][0]=(float) i;
			mostIntensiveDeletions[i][1]=(float) 0;
		}
		
		for(int i=0;i<percentageOfChangesAboutTables.size();i++){
			
			ArrayList<AssistantPercentageClassAlgo> assistant=percentageOfChangesAboutTables.get(i).getTransitions();
			
				for(int j=0; j<assistant.size(); j++){
					float min = mostIntensiveDeletions[0][1];
					int minPosition=0;
					for(int l=0; l<k; l++){
						if(mostIntensiveDeletions[l][1]<min){
							min=mostIntensiveDeletions[l][1];
							minPosition=l;
						}
					}
					if(assistant.get(j).getDeletionsPercentage()>mostIntensiveDeletions[minPosition][1]){
						mostIntensiveDeletions[minPosition][1]=assistant.get(j).getDeletionsPercentage();
						mostIntensiveDeletions[minPosition][0]=(float) i;
					}
					
			
				}
				
		}
		
		Arrays.sort(mostIntensiveDeletions, new Comparator<Float[]>() {
		    @Override
		    public int compare(Float[] s1, Float[] s2) {
		        Float t1 = s1[1];
		        Float t2 = s2[1];
		        
		        return t2.compareTo(t1);
		        
		    }
		});
		
		for(int i=0;i<k;i++){
			
			int l=(int)(Math.round(mostIntensiveDeletions[i][0]));
			System.out.println("Deletions "+percentageOfChangesAboutTables.get(l).getName()+" "+mostIntensiveDeletions[i][1]);
		}
		
		
		//Insersions GUI **********************
		labelMostIntensiveInsersionsByPercentage.setText("");
		labelMostIntensiveInsersionsByPercentage.setText(" Most Intensive Tables By Insersions(Percentage)");
		labelMostIntensiveInsersionsByPercentage.setBounds(15,0,300,50);
		
		
		jTextAreaMostIntensiveInsersionsByPercentage.setEditable(false);
		jTextAreaMostIntensiveInsersionsByPercentage.setBounds(0, 1, 600, 300);
		Color color=new Color(000,191,255);
		jTextAreaMostIntensiveInsersionsByPercentage.setBackground(color);
		jTextAreaMostIntensiveInsersionsByPercentage.setTabSize(20);
		jTextAreaMostIntensiveInsersionsByPercentage.setFont(new Font("PF Isotext Pro",Font.BOLD,16));
		
		String assistant="Table Name"+"\tOld Version"+"\tNew Version"+"\tPercentage";
		for(int i=0; i<k; i++){
			String oldVersion="";
			String newVersion="";
			int l=(int)(Math.round(mostIntensiveInsersions[i][0]));
			
			
			for(int t=0;t<percentageOfChangesAboutTables.get(l).getTransitions().size();t++){
				if(mostIntensiveInsersions[i][1]==percentageOfChangesAboutTables.get(l).getTransitions().get(t).getInsersionsPercentage()){
					oldVersion=percentageOfChangesAboutTables.get(l).getTransitions().get(t).getOldSchema().getName();
					newVersion=percentageOfChangesAboutTables.get(l).getTransitions().get(t).getNewSchema().getName();
				}
			}
			if(mostIntensiveInsersions[i][1]!=0){
				assistant=assistant+"\n"+percentageOfChangesAboutTables.get(l).getName()+"\t "+oldVersion+"\t"+newVersion+"\t"
					+ mostIntensiveInsersions[i][1];
			}
		}
		
		jTextAreaMostIntensiveInsersionsByPercentage.setText(assistant);
		
		jScrollPaneKMostIntensiveInsersionsByPercentage=new JScrollPane(jTextAreaMostIntensiveInsersionsByPercentage,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneKMostIntensiveInsersionsByPercentage.setBounds(20, 40, 600, 170);
		
		//Updates GUI ********************
		labelMostIntensiveUpdatesByPercentage.setText("");
		labelMostIntensiveUpdatesByPercentage.setText(" Most Intensive Tables By Updates(Percentage)");
		labelMostIntensiveUpdatesByPercentage.setBounds(15,208,300,50);
		
		
		jTextAreaMostIntensiveUpdatesByPercentage.setEditable(false);
		jTextAreaMostIntensiveUpdatesByPercentage.setBounds(0, 300, 600, 300);
		jTextAreaMostIntensiveUpdatesByPercentage.setBackground(color);
		jTextAreaMostIntensiveUpdatesByPercentage.setTabSize(20);
		jTextAreaMostIntensiveUpdatesByPercentage.setFont(new Font("PF Isotext Pro",Font.BOLD,16));
		
		assistant="Table Name"+"\tOld Version"+"\tNew Version"+"\tPercentage";
		for(int i=0; i<k; i++){
			String oldVersion="";
			String newVersion="";
			int l=(int)(Math.round(mostIntensiveUpdates[i][0]));
			
			for(int t=0;t<percentageOfChangesAboutTables.get(l).getTransitions().size();t++){
				if(mostIntensiveUpdates[i][1]==percentageOfChangesAboutTables.get(l).getTransitions().get(t).getUpdatesPercentage()){
					oldVersion=percentageOfChangesAboutTables.get(l).getTransitions().get(t).getOldSchema().getName();
					newVersion=percentageOfChangesAboutTables.get(l).getTransitions().get(t).getNewSchema().getName();
				}
			}
			if(mostIntensiveUpdates[i][1]!=0){
				assistant=assistant+"\n"+percentageOfChangesAboutTables.get(l).getName()+"\t"+oldVersion+"\t"+newVersion+"\t"
					+ mostIntensiveUpdates[i][1];
			}		
		}
		
		jTextAreaMostIntensiveUpdatesByPercentage.setText(assistant);
		
		jScrollPaneKMostIntensiveUpdatesByPercentage=new JScrollPane(jTextAreaMostIntensiveUpdatesByPercentage,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneKMostIntensiveUpdatesByPercentage.setBounds(20, 240, 600, 170);
		
		
		//Deletions GUI ******************
		labelMostIntensiveDeletionsByPercentage.setText("");
		labelMostIntensiveDeletionsByPercentage.setText(" Most Intensive Tables By Deletions(Percentage)");
		labelMostIntensiveDeletionsByPercentage.setBounds(15,400,300,50);
		
		
		jTextAreaMostIntensiveDeletionsByPercentage.setEditable(false);
		jTextAreaMostIntensiveDeletionsByPercentage.setBounds(0, 0, 600, 300);
		jTextAreaMostIntensiveDeletionsByPercentage.setBackground(color);
		jTextAreaMostIntensiveDeletionsByPercentage.setTabSize(20);
		jTextAreaMostIntensiveDeletionsByPercentage.setFont(new Font("PF Isotext Pro",Font.BOLD,16));
		
		assistant="Table Name"+"\tOld Version"+"\tNew Version"+"\tPercentage";
		for(int i=0; i<k; i++){
			String oldVersion="";
			String newVersion="";
			int l=(int)(Math.round(mostIntensiveDeletions[i][0]));
			
			for(int t=0;t<percentageOfChangesAboutTables.get(l).getTransitions().size();t++){
				if(mostIntensiveDeletions[i][1]==percentageOfChangesAboutTables.get(l).getTransitions().get(t).getDeletionsPercentage()){
					oldVersion=percentageOfChangesAboutTables.get(l).getTransitions().get(t).getOldSchema().getName();
					newVersion=percentageOfChangesAboutTables.get(l).getTransitions().get(t).getNewSchema().getName();
				}
			}
			if(mostIntensiveDeletions[i][1]!=0){
				assistant=assistant+"\n"+percentageOfChangesAboutTables.get(l).getName()+"\t"+oldVersion+"\t"+newVersion+"\t"
					+ mostIntensiveDeletions[i][1];
			}		
		}
		
		jTextAreaMostIntensiveDeletionsByPercentage.setText(assistant);
		
		jScrollPaneKMostIntensiveDeletionsByPercentage=new JScrollPane(jTextAreaMostIntensiveDeletionsByPercentage,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneKMostIntensiveDeletionsByPercentage.setBounds(20, 430, 600, 170);
		
		
		//Add Components to GUI ************************
		statistics.removeAll();
		statistics.revalidate();
		statistics.repaint();
		
		statistics.add(labelMostIntensiveInsersionsByPercentage);
		statistics.add(jScrollPaneKMostIntensiveInsersionsByPercentage);
		statistics.add(labelMostIntensiveUpdatesByPercentage);
		statistics.add(jScrollPaneKMostIntensiveUpdatesByPercentage);
		statistics.add(labelMostIntensiveDeletionsByPercentage);
		statistics.add(jScrollPaneKMostIntensiveDeletionsByPercentage);
		statistics.add(buttonStop);
		
	}
	
	
	private ArrayList<Schema> calculateNumberofVersions(int level){
		
		int selectedVersion=0;
		int assistant;
		
		ArrayList<Schema> versionsWithLevel=new ArrayList<Schema>();
		versionsWithLevel.add(AllSchemas.get(selectedVersion));
		
		if(level==3){
			
			selectedVersion=AllSchemas.size()/2;
			versionsWithLevel.add(AllSchemas.get(selectedVersion));
			
			
		}
		else if(level==4){
			
			assistant=AllSchemas.size()/4;
			selectedVersion=selectedVersion+assistant;
			
			while(selectedVersion<AllSchemas.size()){
				
				versionsWithLevel.add(AllSchemas.get(selectedVersion));
				selectedVersion=selectedVersion+assistant;
				
			}
			
		}
		else if(level==5){
			
			assistant=AllSchemas.size()/5;
			selectedVersion=selectedVersion+assistant;
			
			while(selectedVersion<AllSchemas.size()){
				
				versionsWithLevel.add(AllSchemas.get(selectedVersion));
				selectedVersion=selectedVersion+assistant;
				
			}
			
		}
		else if(level==6){
			
			assistant=AllSchemas.size()/6;
			selectedVersion=selectedVersion+assistant;
			
			while(selectedVersion<AllSchemas.size()){
				
				versionsWithLevel.add(AllSchemas.get(selectedVersion));
				selectedVersion=selectedVersion+assistant;
				
			}
			
		}
		else if(level==7){
			
			assistant=AllSchemas.size()/7;
			selectedVersion=selectedVersion+assistant;
			
			while(selectedVersion<AllSchemas.size()){
				
				versionsWithLevel.add(AllSchemas.get(selectedVersion));
				selectedVersion=selectedVersion+assistant;
				
			}
			
		}
		else if(level==8){
			
			assistant=AllSchemas.size()/8;
			selectedVersion=selectedVersion+assistant;
			
			while(selectedVersion<AllSchemas.size()){
				
				versionsWithLevel.add(AllSchemas.get(selectedVersion));
				selectedVersion=selectedVersion+assistant;
				
			}
			
		}
		else if(level==9){
			
			assistant=AllSchemas.size()/9;
			selectedVersion=selectedVersion+assistant;
			
			while(selectedVersion<AllSchemas.size()){
				
				versionsWithLevel.add(AllSchemas.get(selectedVersion));
				selectedVersion=selectedVersion+assistant;
				
			}
			
		}
		else if(level==10){
			
			assistant=AllSchemas.size()/10;
			selectedVersion=selectedVersion+assistant;
			
			while(selectedVersion<AllSchemas.size()){
				
				versionsWithLevel.add(AllSchemas.get(selectedVersion));
				selectedVersion=selectedVersion+assistant;
				
			}
			
		}
		else if(level==11){
			
			assistant=AllSchemas.size()/11;
			selectedVersion=selectedVersion+assistant;
			
			while(selectedVersion<AllSchemas.size()){
				
				versionsWithLevel.add(AllSchemas.get(selectedVersion));
				selectedVersion=selectedVersion+assistant;
				
			}
			
		}
		else if(level==12){
			
			assistant=AllSchemas.size()/12;
			selectedVersion=selectedVersion+assistant;
			
			while(selectedVersion<AllSchemas.size()){
				
				versionsWithLevel.add(AllSchemas.get(selectedVersion));
				selectedVersion=selectedVersion+assistant;
				
			}
			
		}
		
		selectedVersion=AllSchemas.size()-1;
		versionsWithLevel.add(AllSchemas.get(selectedVersion));
		return versionsWithLevel;
		
	}
	
	
	
}
