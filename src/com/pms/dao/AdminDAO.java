package com.pms.dao;

import java.util.List;

import com.pms.model.Admin;

public interface AdminDAO {
	public void AdminAdd(Admin admin) throws Exception;
	public void AdminMod(Admin admin) throws Exception;
	public void AdminDel(Admin admin) throws Exception;
	public List<Admin> GetAllAdmins() throws Exception;
	public Admin GetAdminById(int id) throws Exception;
	public Admin GetAdminByLoginid(String loginid) throws Exception;
}
