package com.pms.dao;

import com.pms.model.ResData;

public interface ResDataDAO {
	
	public ResData ResDataOfColumnSave(ResData rd, String orgid) throws Exception;

	public void ResDataOfRelationColumnSave(ResData rd, String clue_SRC_SYS) throws Exception;

	public void ResDataOfRelationRowSave(ResData rd, String clue_SRC_SYS) throws Exception;

	public void ResDataOfRelationClassifySave(ResData rd, String clue_SRC_SYS) throws Exception;

//	public List<ResColumn> QueryAllColumn() throws Exception;
	
}
