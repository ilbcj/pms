package com.pms.model;

public class Application {
	public final static String PWDPOLICYSYS = "sys";
	public final static String PWDPOLICYSELF = "self";
	public final static String CHECKBOXCHECKED = "on";
	public final static String PWDSTRENGTHLOWER = "1";
	public final static String PWDSTRENGTHUPPER = "2";
	public final static String PWDSTRENGTHNUMBER = "3";
	public final static String PWDSTRENGTHSPECIAL = "4";
	
	private int id;
	private String name="";
	private String flag="";
	private String address;
	private String url;
	private String unit;
	private String charge;
	private String charge_contact;
	private String vender;
	private String vender_contact;
	private String hasAccountType;
	private String hasPwdAccount;
	private String pwd_policy;
	private String default_username;
	private String default_pwd_type;
	private String default_pwd;
	private int pwdlen_min;
	private int pwdlen_max;
	private String pwdstrength;
	private String hasCertAccount;
	private String cert_policy_pki;
	private String tstamp;
	
	public String getTstamp() {
		return tstamp;
	}
	public void setTstamp(String tstamp) {
		this.tstamp = tstamp;
	}
	public String getCert_policy_pki() {
		return cert_policy_pki;
	}
	public void setCert_policy_pki(String cert_policy_pki) {
		this.cert_policy_pki = cert_policy_pki;
	}
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
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCharge() {
		return charge;
	}
	public void setCharge(String charge) {
		this.charge = charge;
	}
	public String getCharge_contact() {
		return charge_contact;
	}
	public void setCharge_contact(String charge_contact) {
		this.charge_contact = charge_contact;
	}
	public String getVender() {
		return vender;
	}
	public void setVender(String vender) {
		this.vender = vender;
	}
	public String getVender_contact() {
		return vender_contact;
	}
	public void setVender_contact(String vender_contact) {
		this.vender_contact = vender_contact;
	}

	public String getHasAccountType() {
		return hasAccountType;
	}
	public void setHasAccountType(String hasAccountType) {
		this.hasAccountType = hasAccountType;
	}
	public String getHasPwdAccount() {
		return hasPwdAccount;
	}
	public void setHasPwdAccount(String hasPwdAccount) {
		this.hasPwdAccount = hasPwdAccount;
	}
	public String getHasCertAccount() {
		return hasCertAccount;
	}
	public void setHasCertAccount(String hasCertAccount) {
		this.hasCertAccount = hasCertAccount;
	}
	public String getPwd_policy() {
		return pwd_policy;
	}
	public void setPwd_policy(String pwd_policy) {
		this.pwd_policy = pwd_policy;
	}
	public String getDefault_username() {
		return default_username;
	}
	public void setDefault_username(String default_username) {
		this.default_username = default_username;
	}
	public String getDefault_pwd_type() {
		return default_pwd_type;
	}
	public void setDefault_pwd_type(String default_pwd_type) {
		this.default_pwd_type = default_pwd_type;
	}
	public String getDefault_pwd() {
		return default_pwd;
	}
	public void setDefault_pwd(String default_pwd) {
		this.default_pwd = default_pwd;
	}
	public int getPwdlen_min() {
		return pwdlen_min;
	}
	public void setPwdlen_min(int pwdlen_min) {
		this.pwdlen_min = pwdlen_min;
	}
	public int getPwdlen_max() {
		return pwdlen_max;
	}
	public void setPwdlen_max(int pwdlen_max) {
		this.pwdlen_max = pwdlen_max;
	}
	public String getPwdstrength() {
		return pwdstrength;
	}
	public void setPwdstrength(String pwdstrength) {
		this.pwdstrength = pwdstrength;
	}
	
	
}
