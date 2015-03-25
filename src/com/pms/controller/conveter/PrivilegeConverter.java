package com.pms.controller.conveter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;
import com.pms.dto.PrivilegeTemp;

public class PrivilegeConverter extends DefaultTypeConverter {
	
	@Override      
	  
	public Object convertValue(Map context, Object value, Class toType) {
		if(value == null) {
			return null;
		}
		List<PrivilegeTemp> res = new ArrayList<PrivilegeTemp>();
		Map<String,Class<?>> m  = new HashMap<String,Class<?>>();
		m.put("appid", Integer.class);
		m.put("roleid", Integer.class);
		String json = ((String [])value)[0];
		JSONArray jsonArray = JSONArray.fromObject(json);
		for (int i = 0; i < jsonArray.size(); i++) {
			PrivilegeTemp priv = (PrivilegeTemp)JSONObject.toBean( jsonArray.getJSONObject(i), PrivilegeTemp.class, m );
			res.add(priv);
		}
		return res;
	}

}
