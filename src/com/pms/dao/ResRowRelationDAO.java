package com.pms.dao;

import java.util.List;

import com.pms.model.ResRelationRow;
import com.pms.model.ResRelationRowPrivate;

public interface ResRowRelationDAO {
	
	public ResRelationRow ResRelationRowSave(ResRelationRow rr) throws Exception;
	
	public ResRelationRowPrivate ResRelationRowPrivateSave(ResRelationRowPrivate rrrp) throws Exception;

	public List<ResRelationRow> QueryAllResRelationRow() throws Exception;
	
	public List<ResRelationRowPrivate> QueryAllResRelationRowPrivate() throws Exception;

	public int ResRowRelationImportClear() throws Exception;
	
}
