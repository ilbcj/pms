package com.pms.dao;

import java.util.List;

import com.pms.model.ResRelationColumnClassify;

public interface ResColumnClassifyRelationDAO {
	
	public ResRelationColumnClassify ResRelationColumnClassifySave(ResRelationColumnClassify rcc) throws Exception;

	public List<ResRelationColumnClassify> QueryAllResRelationColumnClassify() throws Exception;

	public List<ResRelationColumnClassify> QueryResRelationColumnClassify(String dataSet) throws Exception;
	
	public ResRelationColumnClassify QueryResRelationColumnClassify(String src, String dest) throws Exception;
	
}
