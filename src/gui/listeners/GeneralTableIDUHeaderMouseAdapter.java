package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import data.dataKeeper.TableData;
import gui.tableElements.commons.JvTable;
import gui.tableElements.tableRenderers.IDUTableRenderer;

public class GeneralTableIDUHeaderMouseAdapter extends MouseAdapter {
	
	private TableData tableData;
	private JvTable generalTable;
	private IDUTableRenderer renderer;
	
	
	
	
	public GeneralTableIDUHeaderMouseAdapter(TableData tableData, JvTable generalTable, IDUTableRenderer renderer) {
		super();
		this.tableData = tableData;
		this.generalTable = generalTable;
		this.renderer = renderer;
	}

	@Override
    public void mouseClicked(MouseEvent e) {
    	tableData.setWholeColZoomArea(generalTable.columnAtPoint(e.getPoint()));
        renderer.setWholeCol(generalTable.columnAtPoint(e.getPoint()));
        //String name = generalTable.getColumnName(wholeColZoomArea);
        //System.out.println("Column index selected " + wholeColZoomArea + " " + name);
        generalTable.repaint();
    }
	
	 @Override
	    public void mouseReleased(MouseEvent e) {
	    	if(SwingUtilities.isRightMouseButton(e)){
				System.out.println("Right Click");
				
						final JPopupMenu popupMenu = new JPopupMenu();
				        JMenuItem showDetailsItem = new JMenuItem("Clear Column Selection");
				        showDetailsItem.addActionListener(new ActionListener() {

				            @Override
				            public void actionPerformed(ActionEvent e) {
				            	tableData.setWholeColZoomArea(-1);
						        renderer.setWholeCol(tableData.getWholeColZoomArea());

				            	generalTable.repaint();
				            }
				        });
				        popupMenu.add(showDetailsItem);
				        popupMenu.show(generalTable, e.getX(),e.getY());
				    
			}
		
	   }
}
