package com.pms.dao;

import java.util.List;

import com.pms.model.ResValue;

public interface ResValueDAO {
	
	public ResValue ResValueSave(ResValue val) throws Exception;

	List<ResValue> QueryRowResValue(String dataSet, String element)
			throws Exception;
	
}
