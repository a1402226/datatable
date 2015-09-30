package com.edupsousa.datatable;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DataTable {

	public static final int TYPE_INT = 0;
	public static final int TYPE_STRING = 1;

	public static final int FORMAT_CSV = 0;
	public static final int FORMAT_HTML = 1;

	private LinkedHashMap<String, Integer> columnsTypes = new LinkedHashMap<String, Integer>();
	private ArrayList<DataTableRow> rows = new ArrayList<DataTableRow>();
	private ExportInterface export;

	public int columnsCount() {
		return columnsTypes.size();
	}

	public int rowsCount() {
		return rows.size();
	}

	public void addCollumn(String name, int type) {
		columnsTypes.put(name, type);
	}

	public boolean hasCollumn(String name) {
		return columnsTypes.containsKey(name);
	}

	public DataTableRow createRow() {
		return new DataTableRow(this);
	}

	public void insertRow(DataTableRow row) {
		checkRowCompatibilityAndThrows(row);
		rows.add(row);
	}

	public DataTableRow lastRow() {
		return rows.get(rows.size()-1);
	}

	public int getCollumnType(String collumn) {
		return columnsTypes.get(collumn);
	}

	private void checkRowCompatibilityAndThrows(DataTableRow row) {
		for (String collumnName : columnsTypes.keySet()) {
			if (row.hasValueFor(collumnName) && 
					!(isValueCompatible(columnsTypes.get(collumnName), row.getValue(collumnName)))) {
				throw new ClassCastException("Wrong type for collumn " + collumnName + ".");
			}
		}
	}

	private boolean isValueCompatible(int type, Object value) {
		if (type == this.TYPE_INT && !(value instanceof Integer)) {
			return false;
		} else if (type == this.TYPE_STRING && !(value instanceof String)) {
			return false;
		}
		return true;
	}

	public DataTableRow getRow(int i) {
		return rows.get(i);
	}

	public String export(int format) {
		if (format == DataTable.FORMAT_CSV)
			export = new ExportToCSV();
		if (format == DataTable.FORMAT_HTML)
			export = new ExportToHTML();
		return export.export(this, columnsTypes);
	}

	public void insertRowAt(DataTableRow row, int index) {
		rows.add(index, row);
	}

	public DataTable filterEqual(String collumn, Object value) {
		DataTable output = emptyDataTableSameColumns();
		DataTableRow row;
		for (int i = 0; i < this.rowsCount(); i++) {
			row = this.getRow(i);
			if (row.getValue(collumn)==value)
				output.insertRow(row);
		}
		return output;
	}

	public DataTable sortAscending(String collumn) {
		if(columnsTypes.get(collumn)==TYPE_STRING)
			throw new ClassCastException("Only Integer columns can be sorted.");
		DataTable output = emptyDataTableSameColumns();
		DataTableRow[] rows = orderRows(fillRowArray(),collumn);
		for (int i = 0; i < rows.length; i++){
			output.insertRow(rows[i]);
		}
		return output;
	}

	public DataTable filterNotEqual(String collumn, Object value) {
		DataTable output = emptyDataTableSameColumns();
		DataTableRow row;
		for (int i = 0; i < this.rowsCount(); i++) {
			row = this.getRow(i);
			if (row.getValue(collumn)!=value)
				output.insertRow(row);
		}
		return output;
	}

	public DataTable sortDescending(String collumn) {
		if(columnsTypes.get(collumn)==TYPE_STRING)
			throw new ClassCastException("Only Integer columns can be sorted.");
		DataTable output = emptyDataTableSameColumns();
		DataTableRow[] rows = orderRows(fillRowArray(),collumn);
		for (int i = rows.length-1; i >= 0; i--){
			output.insertRow(rows[i]);
		}
		return output;
	}

	private DataTable emptyDataTableSameColumns(){
		DataTable output = new DataTable();
		for (String collumnName : columnsTypes.keySet()) {
			int type = columnsTypes.get(collumnName);
			output.addCollumn(collumnName, type);
		}
		return output;
	}

	private DataTableRow[] fillRowArray() {
		DataTableRow[] rows = new DataTableRow[this.rowsCount()];
		for (int i = 0; i < this.rowsCount(); i++)
			rows[i] = this.getRow(i);
		return rows;
	}

	private DataTableRow[] orderRows(DataTableRow[] rows, String collumn){
		for (int i = 0; i < rows.length-1; i++){
			for (int j = 0; j < rows.length-1; j++){
				if((int)rows[j].getValue(collumn)>(int)rows[j+1].getValue(collumn)){
					DataTableRow temp = rows[j+1];
					rows[j+1] = rows[j];
					rows[j] = temp;
				}
			}
		}
		return rows;
	}
}
