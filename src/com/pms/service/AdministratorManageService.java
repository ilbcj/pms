/**   
 * @ClassName:     ${AdministratorManageService}   
 * @Description:   ${管理员管理功能}   
 * 
 * @ProductName:   ${中盈集中授权平台}
 * @author         ${北京中盈网信科技有限公司}  
 * @version        V1.0     
 * @Date           ${2014.8.19} 
*/
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
			throw new Exception("原口令不正确，修改口令失败!");
		}
		return ;
	}
	
	public int QueryAdministrators(Admin criteria, int page, int rows, List<Admin> items) throws Exception {
		AdminDAO admindao = new AdminDAOImpl();
		List<Admin> admins = admindao.GetAdmins(criteria, page, rows);
		items.addAll(admins);
		int total = admins.size();
		
		return total;
	}
	
	public Admin SaveAdmin(Admin admin, List<Integer> pid) throws Exception {
		AdminDAO admindao = new AdminDAOImpl();
		admin = admindao.AdminsAdd(admin);
		
		AccreditDAO accreditdao = new AccreditDAOImpl();
		accreditdao.UpdateAdminAccredits(admin.getId(), pid);
		
		return admin;
	}
	
	public List<AdminAccredit> QueryAccreditByAdminId(int aid) throws Exception {
		AccreditDAO accreditdao = new AccreditDAOImpl();
		List<AdminAccredit> accredit = accreditdao.GetAdminAccreditById( aid );
		return accredit;
	}
	
	public Admin QueryAdminByLoginid(String loginid) throws Exception {
		AdminDAO admindao = new AdminDAOImpl();
		Admin admin = admindao.GetAdminByLoginid(loginid);
		return admin;
	}
	
	public void DeleteAdmin(List<Integer> id) throws Exception {
		if(id == null)
			return;
		
		Admin target;
		AdminDAO admindao = new AdminDAOImpl();
		for(int i = 0; i< id.size(); i++) {
			target = new Admin();
			target.setId(id.get(i));
			
			admindao.AdminOfAccreditDel(target);
		}
		
		return ;
	}
}
