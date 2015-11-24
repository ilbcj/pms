package com.pms.dao;

import java.util.List;

import com.pms.model.ResRelationColumn;

public interface ResColumnRelationDAO {
	
	public ResRelationColumn ResRelationColumnSave(ResRelationColumn rc) throws Exception;

	public List<ResRelationColumn> QueryAllResRelationColumn() throws Exception;

	public int ResColumnRelationImportClear() throws Exception;
	
}
