package com.pms.controller.conveter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;
import com.pms.dto.PrivilegeTemp;

public class SystemAttrUserConverter extends DefaultTypeConverter {
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object convertValue(Map context, Object value, Class toType) {
		if(value == null) {
			return null;
		}
		List<String> res = new ArrayList<String>();
		String json = ((String [])value)[0];
		JSONArray jsonArray = JSONArray.fromObject(json);
		for (int i = 0; i < jsonArray.size(); i++) {
			String dictValue = jsonArray.get(i).toString();
			res.add(dictValue);
		}
		return res;
	}

}
