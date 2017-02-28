package com.pms.webservice.dao;

import java.util.List;

public interface SearchDAO {
	public static final int TYPEUSER = 1;
	public static final int TYPEORG = 2;
	public static final int TYPERESDATA = 3;
	public static final int TYPERESFUN = 4;
	public static final int TYPEROLE = 5;
	public static final int TYPEUSER_ROLE = 6;
	public static final int TYPEROLE_RESOURCE = 7;
	@SuppressWarnings("rawtypes")
	public List SqlQueryAllCols(String sql, int type, int first, int count) throws Exception;
}
