package data.dataKeeper;

import java.util.ArrayList;

import gui.tableElements.commons.JvTable;

public class TableData {
	
	
	private Integer[] segmentSize=new Integer[4];
	private Integer[] segmentSizeZoomArea=new Integer[4];
	private Integer[] segmentSizeDetailedTable=new Integer[3];
	

	private String[] finalColumns=null;
	private String[][] finalRows=null;
	
	private int[] selectedRowsFromMouse;
	
	private String[] firstLevelUndoColumnsZoomArea=null;
	private String[][] firstLevelUndoRowsZoomArea=null;
	
	private String[] finalColumnsZoomArea=null;
	private String[][] finalRowsZoomArea=null;
	
	private int selectedColumn=-1;
	private int selectedColumnZoomArea=-1;
	
	private int wholeCol=-1;
	private int wholeColZoomArea=-1;
	
	private ArrayList<String> selectedFromTree=new ArrayList<String>();
	private ArrayList<String> tablesSelected = new ArrayList<String>();
	private ArrayList<Integer> selectedRows=new ArrayList<Integer>();
	
	private int rowHeight=1;
	private int columnWidth=1;
	
	private boolean showingPld=false;
	
	private JvTable zoomAreaTable = null;
	private JvTable LifeTimeTable = null;
	
	public JvTable getZoomAreaTable() {
		return zoomAreaTable;
	}
	public void setZoomAreaTable(JvTable zoomAreaTable) {
		this.zoomAreaTable = zoomAreaTable;
	}
	public JvTable getLifeTimeTable() {
		return LifeTimeTable;
	}
	public void setLifeTimeTable(JvTable lifeTimeTable) {
		LifeTimeTable = lifeTimeTable;
	}
	
	public String[] getFirstLevelUndoColumnsZoomArea() {
		return firstLevelUndoColumnsZoomArea;
	}
	public void setFirstLevelUndoColumnsZoomArea(String[] firstLevelUndoColumnsZoomArea) {
		this.firstLevelUndoColumnsZoomArea = firstLevelUndoColumnsZoomArea;
	}
	public String[][] getFirstLevelUndoRowsZoomArea() {
		return firstLevelUndoRowsZoomArea;
	}
	public void setFirstLevelUndoRowsZoomArea(String[][] firstLevelUndoRowsZoomArea) {
		this.firstLevelUndoRowsZoomArea = firstLevelUndoRowsZoomArea;
	}
	public ArrayList<Integer> getSelectedRows() {
		return selectedRows;
	}
	public void setSelectedRows(ArrayList<Integer> selectedRows) {
		this.selectedRows = selectedRows;
	}

	public int getRowHeight() {
		return rowHeight;
	}
	public void setRowHeight(int rowHeight) {
		this.rowHeight = rowHeight;
	}
	public int getColumnWidth() {
		return columnWidth;
	}
	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}
	public boolean isShowingPld() {
		return showingPld;
	}
	public void setShowingPld(boolean showingPld) {
		this.showingPld = showingPld;
	}
	public Integer[] getSegmentSize() {
		return segmentSize;
	}
	public void setSegmentSize(Integer[] segmentSize) {
		this.segmentSize = segmentSize;
	}
	public Integer[] getSegmentSizeZoomArea() {
		return segmentSizeZoomArea;
	}
	public void setSegmentSizeZoomArea(Integer[] segmentSizeZoomArea) {
		this.segmentSizeZoomArea = segmentSizeZoomArea;
	}
	public Integer[] getSegmentSizeDetailedTable() {
		return segmentSizeDetailedTable;
	}
	public void setSegmentSizeDetailedTable(Integer[] segmentSizeDetailedTable) {
		this.segmentSizeDetailedTable = segmentSizeDetailedTable;
	}
	public String[] getFinalColumns() {
		return finalColumns;
	}
	public void setFinalColumns(String[] finalColumns) {
		this.finalColumns = finalColumns;
	}
	public String[][] getFinalRows() {
		return finalRows;
	}
	public void setFinalRows(String[][] finalRows) {
		this.finalRows = finalRows;
	}
	public int[] getSelectedRowsFromMouse() {
		return selectedRowsFromMouse;
	}
	public void setSelectedRowsFromMouse(int[] selectedRowsFromMouse) {
		this.selectedRowsFromMouse = selectedRowsFromMouse;
	}
	public String[] getFinalColumnsZoomArea() {
		return finalColumnsZoomArea;
	}
	public void setFinalColumnsZoomArea(String[] finalColumnsZoomArea) {
		this.finalColumnsZoomArea = finalColumnsZoomArea;
	}
	public String[][] getFinalRowsZoomArea() {
		return finalRowsZoomArea;
	}
	public void setFinalRowsZoomArea(String[][] finalRowsZoomArea) {
		this.finalRowsZoomArea = finalRowsZoomArea;
	}
	public int getSelectedColumn() {
		return selectedColumn;
	}
	public void setSelectedColumn(int selectedColumn) {
		this.selectedColumn = selectedColumn;
	}
	public int getSelectedColumnZoomArea() {
		return selectedColumnZoomArea;
	}
	public void setSelectedColumnZoomArea(int selectedColumnZoomArea) {
		this.selectedColumnZoomArea = selectedColumnZoomArea;
	}
	public int getWholeCol() {
		return wholeCol;
	}
	public void setWholeCol(int wholeCol) {
		this.wholeCol = wholeCol;
	}
	public int getWholeColZoomArea() {
		return wholeColZoomArea;
	}
	public void setWholeColZoomArea(int wholeColZoomArea) {
		this.wholeColZoomArea = wholeColZoomArea;
	}
	public ArrayList<String> getSelectedFromTree() {
		return selectedFromTree;
	}
	public void setSelectedFromTree(ArrayList<String> selectedFromTree) {
		this.selectedFromTree = selectedFromTree;
	}
	public ArrayList<String> getTablesSelected() {
		return tablesSelected;
	}
	public void setTablesSelected(ArrayList<String> tablesSelected) {
		this.tablesSelected = tablesSelected;
	}
	
	
	public String getTableDataString()
	{
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Final columns: \n");
		for (int i=0;i<finalColumns.length;i++)
		{
			sb.append(finalColumns[i]);
			sb.append("\n");
		}
		
		sb.append("Final rows: \n");
		for (int i=0;i<finalRows.length;i++)
		{
			for (int j=0;j<finalRows[i].length;j++)
			{
				sb.append(finalRows[i][j]);
				sb.append("\n");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}


}
