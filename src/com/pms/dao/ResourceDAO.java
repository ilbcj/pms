package com.pms.dao;

import java.util.List;

import com.pms.model.AttrDictionary;
import com.pms.model.ResData;
import com.pms.model.ResDataOrg;
import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.model.ResRoleOrg;
import com.pms.model.ResRoleResource;

public interface ResourceDAO {
	ResRoleResource ResRoleResourceAdd(ResRoleResource resRoleResource) throws Exception;
	ResFeature FeatureAdd(ResFeature feature) throws Exception;
	ResRoleOrg ResRoleOrgAdd(ResRoleOrg resRoleOrg) throws Exception;
	ResDataOrg ResDataOrgAdd(ResDataOrg resDataOrg) throws Exception;
	void FeatureDel(ResFeature feature) throws Exception;
	List<ResFeature> GetFeatures(ResFeature criteria, int page, int rows) throws Exception;
	int GetFeaturesCount(ResFeature criteria) throws Exception;
	
	ResData DataAdd(ResData data) throws Exception;
	void DataDel(ResData data) throws Exception;
	List<ResData> GetDatas(List<String> resource_status, List<String> delete_status, List<String> resource_type,
			List<String> dataset_sensitive_level, List<String> data_set, List<String> section_class, 
			List<String> element, List<String> section_relatioin_class, 
			ResData criteria, int page, int rows) throws Exception;
	int GetDatasCount(ResData criteria) throws Exception;
	List<AttrDictionary> GetDatasDictionarys(int id) throws Exception;
	List<AttrDictionary> GetRolesDictionarys(int id) throws Exception;
	
	ResRole RoleAdd(ResRole role) throws Exception;
	void RoleDel(ResRole role) throws Exception;
	public List<ResRole> GetAllRoles() throws Exception;
	List<ResRole> GetRoles(ResRole criteria, int page, int rows) throws Exception;
	int GetRolesCount(ResRole criteria) throws Exception;
	
	void UpdateFeatureRoleResource(String roleId, List<String> featureIds) throws Exception;
	void UpdateDataRoleResource(String roleId, List<String> dataIds) throws Exception;
	List<ResRoleResource> GetAllResRoles() throws Exception;
	List<ResRoleResource> GetRoleResourcesByRoleid(String id) throws Exception;
	List<ResRole> GetRoleById(int id) throws Exception;
	ResFeature GetFeatureByResId(String id) throws Exception;
	List<ResFeature> GetFeatureById(int id) throws Exception;
	ResData GetDataByResId(String ResId) throws Exception;
	List<ResData> GetDataById(int id) throws Exception;
	
	List<ResData> GetDatasByRole(String roleId) throws Exception;
	List<ResRoleOrg> GetResRoleOrgByRoleid(String id) throws Exception;
	List<ResDataOrg> GetResDataOrgByResId(String id) throws Exception;	
}
