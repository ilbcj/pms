package com.pms.dao;

import java.util.List;

import com.pms.model.ResRowResource;
import com.pms.model.ResRowResourcePrivate;

public interface ResRowResourceDAO {
	
	public ResRowResource ResRelationRowSave(ResRowResource rr) throws Exception;
	
	public ResRowResourcePrivate ResRelationRowPrivateSave(ResRowResourcePrivate rrrp) throws Exception;

	public List<ResRowResource> QueryAllResRelationRow() throws Exception;
	
	public List<ResRowResourcePrivate> QueryAllResRelationRowPrivate() throws Exception;

	public int ResRowRelationImportClear() throws Exception;
	
}
