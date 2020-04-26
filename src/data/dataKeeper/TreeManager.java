package data.dataKeeper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import gui.treeElements.TreeConstructionPhasesWithClusters;

public class TreeManager {
	
	/*
	public void fillClustersTree(){
		
		 TreeConstructionPhasesWithClusters tc=new TreeConstructionPhasesWithClusters(globalDataKeeper);
		 tablesTree=tc.constructTree();
		 
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
	
	*/
}
