package gui.listeners.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import data.dataKeeper.TableData;
import gui.tableElements.commons.JvTable;
import gui.tableElements.tableRenderers.IDUTableRenderer;

public class GeneralTableIDUMouseAdapter extends MouseAdapter {
	
	private TableData tableData;
	private JvTable zoomAreaTable;
	private JvTable generalTable;
	private IDUTableRenderer renderer;
	

	
	
	
	public GeneralTableIDUMouseAdapter(TableData tableData, JvTable zoomAreaTable, JvTable generalTable, IDUTableRenderer renderer) {
		super();
		this.tableData = tableData;
		this.zoomAreaTable = zoomAreaTable;
		this.generalTable = generalTable;
		this.renderer = renderer;
	}

	@Override
	   public void mouseClicked(MouseEvent e) {
		
		if (e.getClickCount() == 1) {
			JTable target = (JTable)e.getSource();
	         
			 tableData.setSelectedRowsFromMouse(target.getSelectedRows());
			 tableData.setSelectedColumnZoomArea(target.getSelectedColumn());
	         renderer.setSelCol(tableData.getSelectedColumnZoomArea());
	         target.getSelectedColumns();
	         
	         zoomAreaTable.repaint();
		}
		
	  }
	
	@Override
	   public void mouseReleased(MouseEvent e) {
		
			if(SwingUtilities.isRightMouseButton(e)){
				System.out.println("Right Click");

				JTable target1 = (JTable)e.getSource();
				target1.getSelectedColumns();
				tableData.setSelectedRowsFromMouse(target1.getSelectedRows());
				System.out.println(target1.getSelectedColumns().length);
				System.out.println(target1.getSelectedRow());
				for(int rowsSelected=0; rowsSelected<tableData.getSelectedRowsFromMouse().length; rowsSelected++){
					System.out.println(generalTable.getValueAt(tableData.getSelectedRowsFromMouse()[rowsSelected], 0));
				}
				final JPopupMenu popupMenu = new JPopupMenu();
		        JMenuItem showDetailsItem = new JMenuItem("Clear Selection");
		        showDetailsItem.addActionListener(new ActionListener() {

		            @Override
		            public void actionPerformed(ActionEvent e) {
		            	tableData.setSelectedFromTree(new ArrayList<String>());
		            	zoomAreaTable.repaint();
		            }
		        });
		        popupMenu.add(showDetailsItem);
		        popupMenu.show(generalTable, e.getX(),e.getY());
				        						        
				    
			}
		
	   }

}
