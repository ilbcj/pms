package com.pms.model;

import java.util.HashMap;
import java.util.Map;

public class User {
	public final static int USERSTATUSINUSE = 0;
	public final static int USERSTATUSREVOKE = 1;
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	
	public final static String USER_SEXCODE_UNKNOWN = "0";
	public final static String USER_SEXCODE_MALE = "1";
	public final static String USER_SEXCODE_FEMALE = "2";
	public final static String USER_SEXCODE_UNSPOKEN = "9";
	
	public final static Map<String, String> SexCode = new HashMap<String, String>();
	static{
		SexCode.put("未知的性别", USER_SEXCODE_UNKNOWN);
		SexCode.put("男", USER_SEXCODE_MALE);
		SexCode.put("女", USER_SEXCODE_FEMALE);
		SexCode.put("", USER_SEXCODE_UNSPOKEN);
	}
	
	public final static String USER_POLICE_SORT_GUOBAO = "1";//国保
	public final static String USER_POLICE_SORT_JINGZHEN = "2";//经侦
	public final static String USER_POLICE_SORT_ZHIAN = "3";//治安
	public final static String USER_POLICE_SORT_XINGZHEN = "5";//刑侦
	public final static String USER_POLICE_SORT_CHURUJING = "6";//出入境
	public final static String USER_POLICE_SORT_TIEDAOGONGANJU = "10";//铁道公安局
	public final static String USER_POLICE_SORT_WANGAN = "11";//网安
	public final static String USER_POLICE_SORT_XINGDONGJISHU = "12";//行动技术
	public final static String USER_POLICE_SORT_JINDU = "21";//禁毒
	public final static String USER_POLICE_SORT_KEXIN = "22";//科信
	public final static String USER_POLICE_SORT_FANXIEJIAO = "26";//反邪教
	public final static String USER_POLICE_SORT_FANKONG = "27";//反恐
	public final static String USER_POLICE_SORT_QINGBAOZHONGXIN = "28";//情报中心
	public final static String USER_POLICE_SORT_JINGBAO = "81";//经保
	public final static String USER_POLICE_SORT_WENBAO = "82";//文保
	public final static String USER_POLICE_SORT_QITA = "99";//其他
	
	public final static Map<String, String> PoliceSort = new HashMap<String, String>();
	static{
		PoliceSort.put("国保", USER_POLICE_SORT_GUOBAO);
		PoliceSort.put("经侦", USER_POLICE_SORT_JINGZHEN);
		PoliceSort.put("治安", USER_POLICE_SORT_ZHIAN);
		PoliceSort.put("刑侦", USER_POLICE_SORT_XINGZHEN);
		PoliceSort.put("出入境", USER_POLICE_SORT_CHURUJING);
		PoliceSort.put("铁道公安局", USER_POLICE_SORT_TIEDAOGONGANJU);
		PoliceSort.put("网安", USER_POLICE_SORT_WANGAN);
		PoliceSort.put("行动技术", USER_POLICE_SORT_XINGDONGJISHU);
		PoliceSort.put("禁毒", USER_POLICE_SORT_JINDU);
		PoliceSort.put("科信", USER_POLICE_SORT_KEXIN);
		PoliceSort.put("反邪教", USER_POLICE_SORT_FANXIEJIAO);
		PoliceSort.put("反恐", USER_POLICE_SORT_FANKONG);
		PoliceSort.put("情报中心", USER_POLICE_SORT_QINGBAOZHONGXIN);
		PoliceSort.put("经保", USER_POLICE_SORT_JINGBAO);
		PoliceSort.put("文保", USER_POLICE_SORT_WENBAO);
		PoliceSort.put("其他", USER_POLICE_SORT_QITA);
	}

	public final static String USER_BUSINESS_TYPE_GUANLI = "1";//管理类
	public final static String USER_BUSINESS_TYPE_JIANKONG = "2";//监控类
	public final static String USER_BUSINESS_TYPE_GUANKONG = "3";//管控类
	public final static String USER_BUSINESS_TYPE_ZHENKONG = "4";//侦控类
	public final static String USER_BUSINESS_TYPE_ZHENCHA = "5";//侦查类
	public final static String USER_BUSINESS_TYPE_TEZHEN = "6";//特侦类
	public final static String USER_BUSINESS_TYPE_ZHONGHE = "7";//综合类
	public final static String USER_BUSINESS_TYPE_TONGYONG = "8";//通用类
	public final static String USER_BUSINESS_TYPE_QITA = "99";//其他
	
	public final static Map<String, String> BusinessType = new HashMap<String, String>();
	static{
		BusinessType.put("管理类", USER_BUSINESS_TYPE_GUANLI);
		BusinessType.put("监控类", USER_BUSINESS_TYPE_JIANKONG);
		BusinessType.put("管控类", USER_BUSINESS_TYPE_GUANKONG);
		BusinessType.put("侦控类", USER_BUSINESS_TYPE_ZHENKONG);
		BusinessType.put("侦查类", USER_BUSINESS_TYPE_ZHENCHA);
		BusinessType.put("特侦类", USER_BUSINESS_TYPE_TEZHEN);
		BusinessType.put("综合类", USER_BUSINESS_TYPE_ZHONGHE);
		BusinessType.put("通用类", USER_BUSINESS_TYPE_TONGYONG);
		BusinessType.put("其他", USER_BUSINESS_TYPE_QITA);
	}

	private int id;
	private String NAME="";//name
	private String CERTIFICATE_CODE_MD5;//idnum
	private String CERTIFICATE_CODE_SUFFIX;
	private String SEXCODE;//sex
	private String GA_DEPARTMENT;//parent_id
	private String UNIT;
	private int ORG_LEVEL;
	private String POLICE_SORT;//police_type
	private String POLICE_NO;//police_num
	private String SENSITIVE_LEVEL;//max_sensitive_level
	private String BUSINESS_TYPE;//unit
	private String TAKE_OFFICE;//title
	private int USER_STATUS;//status
	private String position;
	private String dept;
	private int DELETE_STATUS;
	private int DATA_VERSION;
	private String LATEST_MOD_TIME;//tstamp
	
	public int getUSER_STATUS() {
		return USER_STATUS;
	}
	public void setUSER_STATUS(int uSER_STATUS) {
		USER_STATUS = uSER_STATUS;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getCERTIFICATE_CODE_MD5() {
		return CERTIFICATE_CODE_MD5;
	}
	public void setCERTIFICATE_CODE_MD5(String cERTIFICATE_CODE_MD5) {
		CERTIFICATE_CODE_MD5 = cERTIFICATE_CODE_MD5;
	}
	public String getCERTIFICATE_CODE_SUFFIX() {
		return CERTIFICATE_CODE_SUFFIX;
	}
	public void setCERTIFICATE_CODE_SUFFIX(String cERTIFICATE_CODE_SUFFIX) {
		CERTIFICATE_CODE_SUFFIX = cERTIFICATE_CODE_SUFFIX;
	}
	public String getSEXCODE() {
		return SEXCODE;
	}
	public void setSEXCODE(String sEXCODE) {
		SEXCODE = sEXCODE;
	}
	public String getGA_DEPARTMENT() {
		return GA_DEPARTMENT;
	}
	public void setGA_DEPARTMENT(String gA_DEPARTMENT) {
		GA_DEPARTMENT = gA_DEPARTMENT;
	}
	public String getUNIT() {
		return UNIT;
	}
	public void setUNIT(String uNIT) {
		UNIT = uNIT;
	}
	public int getORG_LEVEL() {
		return ORG_LEVEL;
	}
	public void setORG_LEVEL(String oRG_LEVEL) {
		if("部".equals(oRG_LEVEL)) {
			ORG_LEVEL = Organization.ORG_LEVEL_MINISTRY;
		}
		else if("省".equals(oRG_LEVEL)) {
			ORG_LEVEL = Organization.ORG_LEVEL_PROVINCE;
		}
		else if("市".equals(oRG_LEVEL)) {
			ORG_LEVEL = Organization.ORG_LEVEL_CITY;
		}
		else if("县".equals(oRG_LEVEL)) {
			ORG_LEVEL = Organization.ORG_LEVEL_COUNTY;
		}
		else if("基层所队".equals(oRG_LEVEL)) {
			ORG_LEVEL = Organization.ORG_LEVEL_GRASSROOTS;
		}
		else {
			ORG_LEVEL = 0;
		}
	}
	public void setORG_LEVEL(int oRG_LEVEL) {
		ORG_LEVEL = oRG_LEVEL;
	}
	public String getPOLICE_SORT() {
		return POLICE_SORT;
	}
	public void setPOLICE_SORT(String pOLICE_SORT) {
		POLICE_SORT = pOLICE_SORT;
	}
	public String getPOLICE_NO() {
		return POLICE_NO;
	}
	public void setPOLICE_NO(String pOLICE_NO) {
		POLICE_NO = pOLICE_NO;
	}
	public String getSENSITIVE_LEVEL() {
		return SENSITIVE_LEVEL;
	}
	public void setSENSITIVE_LEVEL(String sENSITIVE_LEVEL) {
		SENSITIVE_LEVEL = sENSITIVE_LEVEL;
	}
	public String getBUSINESS_TYPE() {
		return BUSINESS_TYPE;
	}
	public void setBUSINESS_TYPE(String bUSINESS_TYPE) {
		BUSINESS_TYPE = bUSINESS_TYPE;
	}
	public String getTAKE_OFFICE() {
		return TAKE_OFFICE;
	}
	public void setTAKE_OFFICE(String tAKE_OFFICE) {
		TAKE_OFFICE = tAKE_OFFICE;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public int getDELETE_STATUS() {
		return DELETE_STATUS;
	}
	public void setDELETE_STATUS(int dELETE_STATUS) {
		DELETE_STATUS = dELETE_STATUS;
	}
	public int getDATA_VERSION() {
		return DATA_VERSION;
	}
	public void setDATA_VERSION(int dATA_VERSION) {
		DATA_VERSION = dATA_VERSION;
	}
	public String getLATEST_MOD_TIME() {
		return LATEST_MOD_TIME;
	}
	public void setLATEST_MOD_TIME(String lATEST_MOD_TIME) {
		LATEST_MOD_TIME = lATEST_MOD_TIME;
	}
	
	public static String GetSexCode(String name) {
		String result = SexCode.get(name);
		return result;
	}
	
	public static String GetPoliceSortCode(String name) {
		String result = PoliceSort.get(name);
		return result;
	}
	
	public static String GetBusinessTypeCode(String name) {
		String result = BusinessType.get(name);
		return result;
	}
}
