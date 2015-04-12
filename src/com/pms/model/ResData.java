package com.pms.model;

public class ResData {
	private int id;
	private String name="";
	private String resource_id;
	private int resource_status;
	private String resource_describe;
	private String resource_remark;
	private int delete_status;
	private int resource_type;
	private String clue_dst_sys;
	private String dataset_sensitive_level;
	private String data_set;
	private String section_class;
	private String element;
	private String section_relation_class;
	private String row_constraint;
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
	public int getResource_type() {
		return resource_type;
	}
	public void setResource_type(int resource_type) {
		this.resource_type = resource_type;
	}
	public String getClue_dst_sys() {
		return clue_dst_sys;
	}
	public void setClue_dst_sys(String clue_dst_sys) {
		this.clue_dst_sys = clue_dst_sys;
	}
	public String getDataset_sensitive_level() {
		return dataset_sensitive_level;
	}
	public void setDataset_sensitive_level(String dataset_sensitive_level) {
		this.dataset_sensitive_level = dataset_sensitive_level;
	}
	public String getData_set() {
		return data_set;
	}
	public void setData_set(String data_set) {
		this.data_set = data_set;
	}
	public String getSection_class() {
		return section_class;
	}
	public void setSection_class(String section_class) {
		this.section_class = section_class;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getSection_relation_class() {
		return section_relation_class;
	}
	public void setSection_relation_class(String section_relation_class) {
		this.section_relation_class = section_relation_class;
	}
	public String getRow_constraint() {
		return row_constraint;
	}
	public void setRow_constraint(String row_constraint) {
		this.row_constraint = row_constraint;
	}
	public String getLatest_mod_time() {
		return latest_mod_time;
	}
	public void setLatest_mod_time(String latest_mod_time) {
		this.latest_mod_time = latest_mod_time;
	}
}
