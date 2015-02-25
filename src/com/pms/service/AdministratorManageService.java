package com.pms.service;

import java.util.List;

import com.pms.dao.AccreditDAO;
import com.pms.dao.AdminDAO;
import com.pms.dao.impl.AccreditDAOImpl;
import com.pms.dao.impl.AdminDAOImpl;
import com.pms.model.Admin;
import com.pms.model.AdminAccredit;

public class AdministratorManageService {
	public boolean AddAdministrator(Admin admin, int[] types) throws Exception
	{
		AdminDAO admindao = new AdminDAOImpl();
		admin.setStatus(Admin.INUSE);
		admindao.AdminAdd(admin);
		
		AccreditDAO accreditdao = new AccreditDAOImpl();
		accreditdao.AccreditMod(admin.getId(), types);
		return true;
	}
	
	public boolean ModAdministrator(Admin admin, int[] types) throws Exception
	{
		AdminDAO admindao = new AdminDAOImpl();
		if(admin.getPwd() == null || admin.getPwd().length() == 0)
		{
			Admin dataInDb = admindao.GetAdminById(admin.getId());
			admin.setPwd(dataInDb.getPwd());
		}
		admindao.AdminMod(admin);
		
		AccreditDAO accreditdao = new AccreditDAOImpl();
		accreditdao.AccreditMod(admin.getId(), types);
		return true;
	}

	public boolean DelAdministrator(Admin admin) throws Exception {
		AdminDAO admindao = new AdminDAOImpl();
		Admin delAdmin = admindao.GetAdminById(admin.getId());
		delAdmin.setStatus(Admin.DELETE);
		admindao.AdminDel(delAdmin);
		
		AccreditDAO accreditdao = new AccreditDAOImpl();
		accreditdao.AccreditDel(admin.getId());
		return true;
	}

	public List<Admin> QueryAdministrators() throws Exception {
		AdminDAO admindao = new AdminDAOImpl();
		List<Admin> admins = admindao.GetAllAdmins();
		return admins;
	}

	public Admin QueryAdministratorById(int id) throws Exception {
		AdminDAO admindao = new AdminDAOImpl();
		Admin admin = admindao.GetAdminById(id);
		return admin;
	}

	public int[] QueryAdminAccreditById(int id) throws Exception {
		AccreditDAO accreditdao = new AccreditDAOImpl();
		List<AdminAccredit> accredits = accreditdao.GetAdminAccreditById(id);
		int[] rs = new int[accredits.size()];
		for(int i=0; i<rs.length; i++)
		{
			rs[i] = accredits.get(i).getPid();
		}
		return rs;
	}
	
	public int[] QueryAdminAccreditByLoginid(String loginid) throws Exception {
		AdminDAO admindao = new AdminDAOImpl();
		Admin admin = admindao.GetAdminByLoginid(loginid);
		AccreditDAO accreditdao = new AccreditDAOImpl();
		List<AdminAccredit> accredits = accreditdao.GetAdminAccreditById(admin.getId());
		int[] rs = new int[accredits.size()];
		for(int i=0; i<rs.length; i++)
		{
			rs[i] = accredits.get(i).getPid();
		}
		return rs;
	}

	public void UpdateAdministratorPwd(int id, String oldpwd, String newpwd) throws Exception {
		AdminDAO admindao = new AdminDAOImpl();
		Admin admin = admindao.GetAdminById(id);
		if(admin.getPwd().equals(oldpwd))
		{
			admin.setPwd(newpwd);
			admindao.AdminMod(admin);
		}
		else
		{
			throw new Exception("原口令不正确，修改口令失败�?");
		}
		return ;
	}
}
