package com.pms.model;

public class ResFeature {
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	
	private int id;
	private String name="";
	private String resource_id;
	private int resource_status;
	private String resource_describe;
	private String resource_remark;
	private int delete_status;
	private String app_id;
	private String parent_resource;
	private String resource_order;
	private String system_type;
	private String latest_mod_time;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResource_id() {
		return resource_id;
	}
	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}
	public int getResource_status() {
		return resource_status;
	}
	public void setResource_status(int resource_status) {
		this.resource_status = resource_status;
	}
	public String getResource_describe() {
		return resource_describe;
	}
	public void setResource_describe(String resource_describe) {
		this.resource_describe = resource_describe;
	}
	public String getResource_remark() {
		return resource_remark;
	}
	public void setResource_remark(String resource_remark) {
		this.resource_remark = resource_remark;
	}
	public int getDelete_status() {
		return delete_status;
	}
	public void setDelete_status(int delete_status) {
		this.delete_status = delete_status;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getParent_resource() {
		return parent_resource;
	}
	public void setParent_resource(String parent_resource) {
		this.parent_resource = parent_resource;
	}
	public String getResource_order() {
		return resource_order;
	}
	public void setResource_order(String resource_order) {
		this.resource_order = resource_order;
	}
	public String getSystem_type() {
		return system_type;
	}
	public void setSystem_type(String system_type) {
		this.system_type = system_type;
	}
	public String getLatest_mod_time() {
		return latest_mod_time;
	}
	public void setLatest_mod_time(String latest_mod_time) {
		this.latest_mod_time = latest_mod_time;
	}
}
