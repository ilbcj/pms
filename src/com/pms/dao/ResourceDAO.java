package com.pms.dao;

import java.util.List;

import com.pms.model.ResData;
import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;

public interface ResourceDAO {

	ResFeature FeatureAdd(ResFeature feature) throws Exception;
	void FeatureDel(ResFeature feature) throws Exception;
	List<ResFeature> GetFeatures(ResFeature criteria, int page, int rows) throws Exception;
	int GetFeaturesCount(ResFeature criteria) throws Exception;
	
	ResData DataAdd(ResData data) throws Exception;
	void DataDel(ResData data) throws Exception;
	List<ResData> GetDatas(ResData criteria, int page, int rows) throws Exception;
	int GetDatasCount(ResData criteria) throws Exception;
	
	ResRole RoleAdd(ResRole role) throws Exception;
	void RoleDel(ResRole role) throws Exception;
	List<ResRole> GetRoles(ResRole criteria, int page, int rows) throws Exception;
	int GetRolesCount(ResRole criteria) throws Exception;
	
	void UpdateFeatureRoleResource(int id, List<Integer> featureIds) throws Exception;
	void UpdateDataRoleResource(int id, List<Integer> dataIds) throws Exception;
	List<ResRoleResource> GetRoleResourcesByRoleid(int id) throws Exception;
	ResFeature GetFeatureById(int id) throws Exception;
	ResData GetDataById(int id) throws Exception;
	
}
