package com.pms.dao;

import java.util.List;

import com.pms.model.ResColumn;

public interface ResColumnDAO {
	
	public ResColumn ResColumnSave(ResColumn col) throws Exception;

	public List<ResColumn> QueryAllColumn() throws Exception;

	List<ResColumn> QueryRowColumn(String dataSet) throws Exception;

	List<ResColumn> QueryColumnColumn(String dataSet, String sectionClass)
			throws Exception;
	
}
