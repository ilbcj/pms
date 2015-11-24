package com.pms.dao;

import com.pms.model.ResDataTemplate;

public interface ResDataDAO {
	
	//public ResData ResDataOfColumnSave(ResData rd, String orgid) throws Exception;

	public void ImportResDataOfRelationColumn(ResDataTemplate rdt) throws Exception;

	public void ImportResDataOfRelationRow(ResDataTemplate rdt) throws Exception;

	public void ImportResDataOfRelationClassify(ResDataTemplate rdt) throws Exception;

//	public List<ResColumn> QueryAllColumn() throws Exception;
	public void UpdateResDataTemplateStatus(int status) throws Exception;
	
	public void DeleteResDataByDeletedRecordsInResDataTemplate() throws Exception;
	
}
