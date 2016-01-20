package com.pms.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import com.pms.dao.AdminDAO;
import com.pms.dao.impl.AdminDAOImpl;
import com.pms.model.Admin;
import com.pms.model.AdminAccredit;

public class AuthService {

	private Pattern regAction = Pattern.compile("/.*action$");
	private Pattern regLoginReqHtml = Pattern.compile("/page/.*jsp$");
	private Pattern regOrganizationReqMenuHtml = Pattern.compile("/page/menu/organization.html");
	private Pattern regOrganizationReqHtml = Pattern.compile("/page/organization/.*html$");
	private Pattern regUserReqMenuHtml = Pattern.compile("/page/menu/user.html");
	private Pattern regUserReqHtml = Pattern.compile("/page/user/.*html$");
	private Pattern regGroupReqMenuHtml = Pattern.compile("/page/menu/group.html");
	private Pattern regGroupReqHtml = Pattern.compile("/page/group/.*html$");
	private Pattern regRoleReqMenuHtml = Pattern.compile("/page/menu/role.html");
	private Pattern regRoleReqHtml = Pattern.compile("/page/role/.*html$");
	private Pattern regResourceReqMenuHtml = Pattern.compile("/page/menu/resource.html");
	private Pattern regResourceReqHtml = Pattern.compile("/page/resource/.*html$");
	private Pattern regPrivilegeReqMenuHtml = Pattern.compile("/page/menu/privilege.html");
	private Pattern regPrivilegeReqHtml = Pattern.compile("/page/privilege/.*html$");
	private Pattern regSystemReqMenuHtml = Pattern.compile("/page/menu/system.html");
	private Pattern regSystemReqHtml = Pattern.compile("/page/system/.*html$");
	private Pattern regLogReqMenuHtml = Pattern.compile("/page/menu/log.html");
	private Pattern regLogReqHtml = Pattern.compile("/page/log/.*html$");
	private Pattern regAdminReqMenuHtml = Pattern.compile("/page/menu/admin.html");
	private Pattern regAdminReqHtml = Pattern.compile("/page/admin/.*html$");
	
	public boolean CheckStatus(String loginid)
	{
		AdminDAO dao = new AdminDAOImpl();
		Admin admin = null;
		try {
			admin = dao.GetAdminByLoginid(loginid);
		
			if(admin  == null)
			{
				return false;
			}
			if(Admin.FROZEN == admin.getStatus())
			{
				String frozentime = admin.getFrozentime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
						Locale.SIMPLIFIED_CHINESE);
				sdf.parse(frozentime);
				
				Calendar frozen = sdf.getCalendar();
				Calendar now=Calendar.getInstance();
				//锁定时间�?分钟后解�?
				now.add(Calendar.MINUTE, -5);
				boolean flag = now.after(frozen);
				if(!flag)
				{
					System.out.println("administrator '" + loginid + "' has frozen when " + frozentime);
					return false;
				}
				else
				{
					admin.setStatus(Admin.INUSE);
					dao.AdminMod(admin);
					return true;
				}
			}
			else{
				return true;
			}
		}
		catch(Exception e)
		{
			e.getMessage();
			return false;
		}
	}
		
	public boolean LoginPwdService( String loginid, String pwd )
	{
		AdminDAO dao = new AdminDAOImpl();
		Admin admin = null;
		try {
			admin = dao.GetAdminByLoginid(loginid);
		
			if(admin  == null)
			{
				return false;
			}
			
			if( admin.getPwd() != null && admin.getPwd().equals(pwd)) {
				admin.setErrorcount(0);
				admin.setStatus(Admin.INUSE);
				dao.AdminMod(admin);
				return true;
			}
			else
			{
				int count = admin.getErrorcount();
				//尝试次数�?次后锁定
				if(count >= 4)
				{
					admin.setErrorcount(0);
					admin.setStatus(Admin.FROZEN);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
							Locale.SIMPLIFIED_CHINESE);
					String timenow = sdf.format(new Date());
					admin.setFrozentime(timenow);
					dao.AdminMod(admin);
				}
				else
				{
					admin.setErrorcount(admin.getErrorcount() + 1);
					dao.AdminMod(admin);
				}
			}
		} catch (ParseException e) {
			e.getMessage();
		} catch (Exception e) {
			e.getMessage();
		}
		return false;
	}
	
	public boolean AccessPrivilege(String loginid, String url )
	{
//		if(url == null || url.length() == 0) {
//			return false;
//		}
		AdministratorManageService ams = new AdministratorManageService();
		try {
			int types[] = ams.QueryAdminAccreditByLoginid(loginid);
			if(types == null || types.length == 0) {
				return false;
			}
			if(regAction.matcher(url).find() || regLoginReqHtml.matcher(url).find()) {
				return true;
			}
			if(regAction.matcher(url).find() || regOrganizationReqMenuHtml.matcher(url).find() || regOrganizationReqHtml.matcher(url).find()) {
				return isContained(types, AdminAccredit.ACCREDITORGANIZATION);
			}
			if(regAction.matcher(url).find() || regUserReqMenuHtml.matcher(url).find() || regUserReqHtml.matcher(url).find()) {
				return isContained(types, AdminAccredit.ACCREDITUSER);
			}
			if(regAction.matcher(url).find() || regGroupReqMenuHtml.matcher(url).find() || regGroupReqHtml.matcher(url).find()) {
				return isContained(types, AdminAccredit.ACCREDITGROUP);
			}
			if(regAction.matcher(url).find() || regRoleReqMenuHtml.matcher(url).find() || regRoleReqHtml.matcher(url).find()) {
				return isContained(types, AdminAccredit.ACCREDITROLE);
			}
			if(regAction.matcher(url).find() || regResourceReqMenuHtml.matcher(url).find() || regResourceReqHtml.matcher(url).find()) {
				return isContained(types, AdminAccredit.ACCREDITRESOURCE);
			}
			if(regAction.matcher(url).find() || regPrivilegeReqMenuHtml.matcher(url).find() || regPrivilegeReqHtml.matcher(url).find()) {
				return isContained(types, AdminAccredit.ACCREDITPRIVILEGE);
			}
			if(regAction.matcher(url).find() || regSystemReqMenuHtml.matcher(url).find() || regSystemReqHtml.matcher(url).find()) {
				return isContained(types, AdminAccredit.ACCREDITSYSTEM);
			}
			if(regAction.matcher(url).find() || regLogReqMenuHtml.matcher(url).find() || regLogReqHtml.matcher(url).find()) {
				return isContained(types, AdminAccredit.ACCREDITAUDITLOG);
			}
			if(regAction.matcher(url).find() || regAdminReqMenuHtml.matcher(url).find() || regAdminReqHtml.matcher(url).find()) {
				return isContained(types, AdminAccredit.ACCREDITADMIN);
			}
		} catch (Exception e) {
			e.getMessage();
		}
		System.out.println("[Accessd url]\"" + url + "\"");
		return false;
	}
	
	/**
	 * 判断pids中是否包含指定的pid
	 * @param pids	权限列表
	 * @param pid	指定权限
	 * @return	包含返回true，不包含返回false
	 */
	private boolean isContained(int[] pids, int pid)
	{
		for(int item : pids)
		{
			if(pid == item) {
				return true;
			}
		}
		return false;
	}
}
