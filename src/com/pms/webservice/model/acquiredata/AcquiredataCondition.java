package com.pms.webservice.model.acquiredata;

import java.util.List;

import com.pms.webservice.model.Item;


/**
 * WA_COMMON_010220
 * @author ilbcj
 *
 */

public class AcquiredataCondition {
	private List<Item> allDataItems;
	private List<Item> addDataItems;
	
	public List<Item> getAllDataItems() {
		return allDataItems;
	}
	public void setAllDataItems(List<Item> allDataItems) {
		this.allDataItems = allDataItems;
	}
	public List<Item> getAddDataItems() {
		return addDataItems;
	}
	public void setAddDataItems(List<Item> addDataItems) {
		this.addDataItems = addDataItems;
	}
}
