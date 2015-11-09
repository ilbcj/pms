package com.pms.dao;

import java.util.List;

import com.pms.model.ResColumn;

public interface ResColumnDAO {
	
	public ResColumn ResColumnSave(ResColumn col) throws Exception;

	public List<ResColumn> QueryAllColumn() throws Exception;
	
	public ResColumn QueryColumnByElement(String dataset, String element) throws Exception;
}
