package com.pms.webservice.model.auth;

import java.util.List;

import com.pms.webservice.model.Condition;

/**
 * WA_COMMON_010031
 * @author ilbcj
 *
 */
public class AuthCondition {
	private String sensitiveLevel;
	private Condition stc;
	private List<Common010032> common010032;//common010032 has removed ,but still use it to hold input param
	
	public String getSensitiveLevel() {
		return sensitiveLevel;
	}
	public void setSensitiveLevel(String sensitiveLevel) {
		this.sensitiveLevel = sensitiveLevel;
	}
	public Condition getStc() {
		return stc;
	}
	public void setStc(Condition stc) {
		this.stc = stc;
	}
	public List<Common010032> getCommon010032() {
		return common010032;
	}
	public void setCommon010032(List<Common010032> common010032) {
		this.common010032 = common010032;
	}
}
