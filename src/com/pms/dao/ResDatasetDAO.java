package com.pms.dao;

import java.util.List;

import com.pms.model.ResDataSet;

public interface ResDatasetDAO {
	
	public ResDataSet ResDataSetSave(ResDataSet ds) throws Exception;

	public List<ResDataSet> QueryAllDataSet() throws Exception;

	List<ResDataSet> QueryRowDataSet() throws Exception;

	List<ResDataSet> QueryColumnDataSet() throws Exception;

	List<ResDataSet> QueryClassifyDataSet() throws Exception;
	
}
