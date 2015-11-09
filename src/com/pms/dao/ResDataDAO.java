package com.pms.dao;

import com.pms.model.ResData;

public interface ResDataDAO {
	
	//public ResData ResDataOfColumnSave(ResData rd, String orgid) throws Exception;

	public void ImportResDataOfRelationColumn(ResData rd) throws Exception;

	public void ImportResDataOfRelationRow(ResData rd) throws Exception;

	public void ImportResDataOfRelationClassify(ResData rd) throws Exception;

//	public List<ResColumn> QueryAllColumn() throws Exception;
	public void UpdateResDataTemplateStatus(int status) throws Exception;
	
	public void DeleteResDataByDeletedRecordsInResDataTemplate() throws Exception;
	
}
