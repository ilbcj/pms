package com.pms.dao;

import java.util.List;

import com.pms.model.ResDataSetSensitive;

public interface ResDatasetSensitiveDAO {
	
	public ResDataSetSensitive ResDataSetSensitiveSave(ResDataSetSensitive dss) throws Exception;

	public List<ResDataSetSensitive> QueryAllDataSetSensitive() throws Exception;
	
}
