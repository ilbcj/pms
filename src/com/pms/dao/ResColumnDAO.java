package com.pms.dao;

import java.util.List;

import com.pms.model.ResColumn;
import com.pms.model.ResColumnPrivate;

public interface ResColumnDAO {
	
	public ResColumn ResColumnSave(ResColumn col) throws Exception;
	
	public ResColumnPrivate ResColumnPrivateSave(ResColumnPrivate rcp) throws Exception;
	
	public List<ResColumn> QueryAllColumn() throws Exception;

	List<ResColumn> QueryRowColumn(String dataSet) throws Exception;

	List<ResColumn> QueryColumnColumn(String dataSet, String sectionClass)
			throws Exception;
	
	public ResColumn QueryColumnByElement(String dataset, String element) throws Exception;
	
	public ResColumnPrivate QueryColumnPrivateByElement(String dataset, String element) throws Exception;
}
