package com.pms.webservice.dao;

import java.util.List;

import com.pms.model.User;

public interface ExchangeDAO {
	public int SqlExchangeData(String sql) throws Exception;

	public List<User> SearchUserData(String search) throws Exception;
}
