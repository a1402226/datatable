package com.edupsousa.datatable;

import java.util.LinkedHashMap;

public class ExportToHTML implements ExportInterface {

	@Override
	public String export(DataTable dt,
			LinkedHashMap<String, Integer> columnsTypes) {
		DataTableRow row;
		String output = "";
		output += "<table>\n<tr>";
		for (String collumnName : columnsTypes.keySet()) {
			output += "<td>" + collumnName + "</td>";
		}
		output += "</tr>\n";
		for (int i = 0; i < dt.rowsCount(); i++) {
			output += "<tr>";
			row = dt.getRow(i);
			for (String collumnName : columnsTypes.keySet()) {
				output += "<td>" + row.getValue(collumnName) + "</td>";
			}
			output += "</tr>\n";
		}
		output += "</table>\n";
		return output;
	}

}
