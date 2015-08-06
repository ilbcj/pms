package com.pms.dao;

import java.util.List;

import com.pms.model.AttrDictionary;
import com.pms.model.ResData;
import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;
import com.pms.model.ResRoleResourceImport;

public interface ResourceDAO {

	ResFeature FeatureAdd(ResFeature feature) throws Exception;
	void FeatureDel(ResFeature feature) throws Exception;
	List<ResFeature> GetFeatures(ResFeature criteria, int page, int rows) throws Exception;
	int GetFeaturesCount(ResFeature criteria) throws Exception;
	
	ResData DataAdd(ResData data) throws Exception;
	void DataDel(ResData data) throws Exception;
	List<ResData> GetDatas(ResData criteria, int page, int rows) throws Exception;
	int GetDatasCount(ResData criteria) throws Exception;
	List<AttrDictionary> GetDatasDictionarys(int id) throws Exception;
	
	ResRole RoleAdd(ResRole role) throws Exception;
	void RoleDel(ResRole role) throws Exception;
	List<ResRole> GetRoles(ResRole criteria, int page, int rows) throws Exception;
	int GetRolesCount(ResRole criteria) throws Exception;
	
	void UpdateFeatureRoleResource(String roleId, List<String> featureIds) throws Exception;
	void UpdateDataRoleResource(String roleId, List<String> dataIds) throws Exception;
	List<ResRoleResource> GetRoleResourcesByRoleid(String id) throws Exception;
	ResFeature GetFeatureById(String id) throws Exception;
	ResData GetDataById(String id) throws Exception;
	
	List<ResData> GetDatasByRole(String roleId) throws Exception;
	List<ResData> GetColumnDatasByDataSet(String dataSet) throws Exception;
	
	ResRoleResourceImport ResRoleResourceImportAdd(ResRoleResourceImport rrri) throws Exception; 
	List<ResRoleResourceImport> GetResRoleResourceImport() throws Exception;
}
