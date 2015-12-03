/**   
 * @ClassName:     ${LoginAction}   
 * @Description:   ${管理员登录control}   
 * 
 * @ProductName:   ${中盈集中授权平台}
 * @author         ${北京中盈网信科技有限公司}  
 * @version        V1.0     
 * @Date           ${2014.8.12} 
*/
package com.pms.controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.pms.service.AuthService;
import com.pms.util.ConfigHelper;

@SuppressWarnings("serial")
public class LoginAction extends ActionSupport {

	private String loginid;
	private String pwd;
	private String message;
	private String adminname;
	private String random;
	private boolean result;
	
	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	public String getAdminname() {
		return adminname;
	}

	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String Loginpwd()
	{
		ActionContext ctx = ActionContext.getContext();
		String randFromSession = (String)ctx.getSession().get("auth.random");
		if(randFromSession == null || !randFromSession.equals(this.random))
		{
			message = "登录信息不正确，请重新打开浏览器！";
			return ERROR;
		}
		AuthService as = new AuthService();
		if(!as.CheckStatus(loginid))
		{
			message = "管理员不存在或已被锁定，登录失败！";
			return ERROR;
		}
		if( as.LoginPwdService(loginid, pwd) )
		{
			ctx.getSession().put("admin", loginid);
		}
		else
		{
			message = "信息不正确，登录失败！";
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String Jump()
	{
		ConfigHelper.getInstance();
		return SUCCESS;
	}
	
	public String QueryAdminInfo()
	{
		ActionContext ctx = ActionContext.getContext();
		adminname = (String) ctx.getSession().get("admin");
		this.setResult(true);
		return SUCCESS;
	}
	
	public String Logout()
	{
		ActionContext ctx = ActionContext.getContext();
		ctx.getSession().put("admin", null);
		this.setResult(true);
		return SUCCESS;
	}
}
