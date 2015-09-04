package com.pms.webservice.model.exchange;

import com.pms.webservice.model.Condition;

/**
 * WA_COMMON_010130
 * @author ilbcj
 *
 */

public class ExchangeCondition {
	private Condition condition;
	private String table;
	private boolean async;
	private String syncKey;
	private int allReturnCount;
	private int currentReturnCount;
	private Common010131 common010131;
	
	public Condition getCondition() {
		return condition;
	}
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public boolean isAsync() {
		return async;
	}
	public void setAsync(boolean async) {
		this.async = async;
	}
	public String getSyncKey() {
		return syncKey;
	}
	public void setSyncKey(String syncKey) {
		this.syncKey = syncKey;
	}
	public int getAllReturnCount() {
		return allReturnCount;
	}
	public void setAllReturnCount(int allReturnCount) {
		this.allReturnCount = allReturnCount;
	}
	public int getCurrentReturnCount() {
		return currentReturnCount;
	}
	public void setCurrentReturnCount(int currentReturnCount) {
		this.currentReturnCount = currentReturnCount;
	}
	public Common010131 getCommon010131() {
		return common010131;
	}
	public void setCommon010131(Common010131 common010131) {
		this.common010131 = common010131;
	}
}
