package com.pms.dao;

import java.util.List;

import com.pms.model.AdminAccredit;

public interface AccreditDAO {
	public void AccreditMod(int adminid, int[] privilegeids) throws Exception;
	public void AccreditDel(int adminid) throws Exception;
	public List<AdminAccredit> GetAdminAccreditById(int id) throws Exception;
	void UpdateAdminAccredits(int id, List<Integer> pid) throws Exception;
}
