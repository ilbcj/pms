package com.pms.controller.conveter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;
import com.pms.model.Rule;

public class GroupRulesConverter extends DefaultTypeConverter {
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object convertValue(Map context, Object value, Class toType) {
		if(value == null) {
			return null;
		}
		List<Rule> res = new ArrayList<Rule>();
		Map<String,Class<?>> m  = new HashMap<String,Class<?>>();
		m.put("attrid", Integer.class);
		m.put("rulename", String.class);
		m.put("ruletype", Integer.class);
		m.put("rulevalue", String.class);
		String json = ((String [])value)[0];
		JSONArray jsonArray = JSONArray.fromObject(json);
		for (int i = 0; i < jsonArray.size(); i++) {
			Rule rule = (Rule)JSONObject.toBean( jsonArray.getJSONObject(i), Rule.class, m );
			res.add(rule);
		}
		return res;
	}

}

