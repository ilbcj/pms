package com.pms.webservice.dao;

import java.util.List;

import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;
import com.pms.model.User;

public interface ExchangeDAO {
	public int SqlExchangeData(String sql) throws Exception;

	public List<User> SearchUserData(String search) throws Exception;
	
	public List<ResRole> SearchRoleData(String search) throws Exception;
	
	public List<ResFeature> SearchResFeatureData(String search) throws Exception;
	
	public List<ResRoleResource> SearchResRoleResourceData(String search) throws Exception;
}
