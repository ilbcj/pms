package com.pms.dao;

import java.util.List;

import com.pms.model.ResValue;
import com.pms.model.ResValuePrivate;

public interface ResValueDAO {
	
	public ResValue ResValueSave(ResValue val) throws Exception;
	
	public ResValuePrivate ResValuePrivateSave(ResValuePrivate rvp) throws Exception;

	List<ResValue> QueryRowResValue(String dataSet, String element)
			throws Exception;
	
}
