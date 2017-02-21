package com.pms.dao;

import java.util.List;

import com.pms.model.ResClassifyResource;

public interface ResClassifyResourceDAO {
	
	public ResClassifyResource ResClassifyResourceSave(ResClassifyResource rc) throws Exception;

	public List<ResClassifyResource> QueryAllResClassifyResource() throws Exception;
	
	public ResClassifyResource QueryResClassifyResource(String dataset, String element) throws Exception;

	public int ResClassifyResourceImportClear() throws Exception;
	
}
