package com.pms.dao;

import java.util.List;

import com.pms.model.ResRelationRow;

public interface ResRowRelationDAO {
	
	public ResRelationRow ResRelationRowSave(ResRelationRow rr) throws Exception;

	public List<ResRelationRow> QueryAllResRelationRow() throws Exception;

	public int ResRowRelationImportClear() throws Exception;
	
}
