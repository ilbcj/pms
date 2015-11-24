package com.pms.webservice.model.acquiredata;

import com.pms.webservice.model.Item;

public class Feedback {
	private Item file;
	private Item status;
	
	public Item getFile() {
		return file;
	}
	public void setFile(Item file) {
		this.file = file;
	}
	public Item getStatus() {
		return status;
	}
	public void setStatus(Item status) {
		this.status = status;
	}
}
