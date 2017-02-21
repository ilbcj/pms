package com.pms.dao;

import com.pms.model.WhiteListColumn;

public interface WhiteListColumnDAO {
	public WhiteListColumn WhiteListColumnAdd(WhiteListColumn column) throws Exception;
	public WhiteListColumn GetWhiteListColumnByKey(String columnKey) throws Exception;
}
