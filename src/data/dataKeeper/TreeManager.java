package data.dataKeeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import gui.tableElements.commons.JvTable;
import gui.treeElements.TreeConstructionGeneral;
import gui.treeElements.TreeConstructionPhasesWithClusters;

public class TreeManager {
	private JLabel treeLabel;
	
	private JTree tablesTree=new JTree();
	
	private JPanel sideMenu=new JPanel();
	private JPanel tablesTreePanel=new JPanel();
	
	private JScrollPane treeScrollPane= new JScrollPane();
	
	private ArrayList<String> selectedFromTree=new ArrayList<String>();
	
	private JvTable LifeTimeTable;
	

	public TreeManager(JLabel treeLabel, JTree tablesTree, JPanel sideMenu, JPanel tablesTreePanel,
			JScrollPane treeScrollPane, ArrayList<String> selectedFromTree, JvTable LifeTimeTable) {
		this.treeLabel = treeLabel;
		this.tablesTree = tablesTree;
		this.sideMenu = sideMenu;
		this.tablesTreePanel = tablesTreePanel;
		this.treeScrollPane = treeScrollPane;
		this.selectedFromTree = selectedFromTree;
		this.LifeTimeTable = LifeTimeTable;
	}

	public void fillClustersTree(ClusterManager clusterManager){
		
		 TreeConstructionPhasesWithClusters tc=new TreeConstructionPhasesWithClusters(clusterManager);
		 tablesTree=tc.constructTree2();
		 
		 tablesTree.addTreeSelectionListener(new TreeSelectionListener () {
			    public void valueChanged(TreeSelectionEvent ae) { 
			    	TreePath selection = ae.getPath();
			    	selectedFromTree.add(selection.getLastPathComponent().toString());
			    	System.out.println(selection.getLastPathComponent().toString()+" is selected");
			    	
			    }
		 });
		 
		 tablesTree.addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {
					
						if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");
							
							final JPopupMenu popupMenu = new JPopupMenu();
					        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
					        showDetailsItem.addActionListener(new ActionListener() {

					            @Override
					            public void actionPerformed(ActionEvent e) {
					          
					                LifeTimeTable.repaint();
					            	
					            }
					        });
					        popupMenu.add(showDetailsItem);
					        popupMenu.show(tablesTree, e.getX(),e.getY());
							        	
						}
					
				   }
			});
		 
		 treeScrollPane.setViewportView(tablesTree);
		 
		 
		 treeScrollPane.setBounds(5, 5, 250, 170);
		 treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 treeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 tablesTreePanel.add(treeScrollPane);

		 treeLabel.setText("Clusters Tree");

		 sideMenu.revalidate();
		 sideMenu.repaint();
		 
		
	}
	
	public void fillTree(TableManager tableManager){
		
		 TreeConstructionGeneral tc=new TreeConstructionGeneral(tableManager);
		 tablesTree=new JTree();
		 tablesTree=tc.constructTree2();
		 tablesTree.addTreeSelectionListener(new TreeSelectionListener () {
			    public void valueChanged(TreeSelectionEvent ae) { 
			    	TreePath selection = ae.getPath();
			    	selectedFromTree.add(selection.getLastPathComponent().toString());
			    	System.out.println(selection.getLastPathComponent().toString()+" is selected");
			    	
			    }
		 });
		 
		 tablesTree.addMouseListener(new MouseAdapter() {
				@Override
				   public void mouseReleased(MouseEvent e) {
					
						if(SwingUtilities.isRightMouseButton(e)){
							System.out.println("Right Click Tree");
								
									final JPopupMenu popupMenu = new JPopupMenu();
							        JMenuItem showDetailsItem = new JMenuItem("Show This into the Table");
							        showDetailsItem.addActionListener(new ActionListener() {
		
							            @Override
							            public void actionPerformed(ActionEvent e) {
							          
							                LifeTimeTable.repaint();
							            	
							            }
							        });
							        popupMenu.add(showDetailsItem);
							        popupMenu.show(tablesTree, e.getX(),e.getY());
							        							        
								//}
							//}
						}
					
				   }
			});
		 
		 treeScrollPane.setViewportView(tablesTree);
		 
		 treeScrollPane.setBounds(5, 5, 250, 170);
		 treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 treeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 tablesTreePanel.add(treeScrollPane);
		 
		 treeLabel.setText("General Tree");

		 sideMenu.revalidate();
		 sideMenu.repaint();		
		
	}
	
	
}
