package com.edupsousa.datatable;

import java.util.LinkedHashMap;

public interface ExportInterface {
	public String export(DataTable dt, LinkedHashMap<String, Integer> columnsTypes);
}
