package com.pms.dao;

import java.util.List;

import com.pms.model.ResClassifyRelationResource;

public interface ResClassifyRelationResourceDAO {
	
	public ResClassifyRelationResource ResRelationClassifySave(ResClassifyRelationResource rc) throws Exception;

	public List<ResClassifyRelationResource> QueryAllResRelationClassify() throws Exception;

	public List<ResClassifyRelationResource> QueryResRelationClassify(String dataSet) throws Exception;
	
	public int ResClassifyRelationImportClear() throws Exception;
	
}
