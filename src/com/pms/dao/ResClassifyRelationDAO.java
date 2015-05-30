package com.pms.dao;

import java.util.List;

import com.pms.model.ResRelationClassify;

public interface ResClassifyRelationDAO {
	
	public ResRelationClassify ResRelationClassifySave(ResRelationClassify rc) throws Exception;

	public List<ResRelationClassify> QueryAllResRelationClassify() throws Exception;
	
}
