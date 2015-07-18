package com.pms.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.PrivilegeDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.Privilege;
import com.pms.model.ResRole;

public class PrivilegeDAOImpl implements PrivilegeDAO {

	@Override
	public Privilege PrivilegeAdd(Privilege priv) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			priv = (Privilege) session.merge(priv);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重复授权。");
		}
		catch(org.hibernate.exception.SQLGrammarException e)
		{
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return priv;
	}

	@Override
	public void PrivilegeMod(Privilege priv) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.update(priv);
			tx.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return;	

	}

	@Override
	public void PrivilegeDel(Privilege priv) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.delete(priv);
			tx.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Privilege> QueryPrivilegesByOwnerId(int ownerid, int ownertype)
			throws Exception {
 		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Privilege> rs = null;
		String sqlString = "select * from privilege where owner_id = :owner_id and owner_type = :owner_type ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Privilege.class);
			q.setInteger("owner_id", ownerid);
			q.setInteger("owner_type", ownertype);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	public void UpdatePrivilegeByOwnerId(int ownerid, int ownertype,
			List<Privilege> privileges) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String sqlString = "delete from privilege where owner_id = :owner_id and owner_type = :owner_type ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("owner_id", ownerid);
			q.setInteger("owner_type", ownertype);
			q.executeUpdate();
			
			for(int i = 0; i<privileges.size(); i++) {
				session.merge(privileges.get(i));
			}			
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return;
	}

	public int QueryPrivilegesCountByOwnerId(int id, int ownertype) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs = 0;
		String sqlString = "select count(*) from privilege where owner_id = :owner_id and owner_type = :owner_type ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("owner_id", id);
			q.setInteger("owner_type", ownertype);
			rs = ((BigInteger)q.uniqueResult()).intValue();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResRole> QueryPrivInfosByUserid(int userid) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRole> rs = null;
		String sqlString = "SELECT a.* FROM res_role a, privilege b where b.owner_id=:owner_id and owner_type=:owner_type and a.id = b.role_id";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
			q.setInteger("owner_id", userid);
			q.setInteger("owner_type", Privilege.OWNERTYPEUSER);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResRole> QueryPrivInfosByUsersOrg(int userid) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResRole> rs = null;
		String sqlString = "SELECT a.* FROM res_role a, privilege b where b.owner_id=(select parent_id from user where id = :userid) and owner_type=:owner_type and a.id = b.role_id ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
			q.setInteger("userid", userid);
			q.setInteger("owner_type", Privilege.OWNERTYPEORG);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResRole> QueryPrivInfosByUsersGroup(int groupid)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRole> rs = null;
		String sqlString = "SELECT a.* FROM res_role a, privilege b where b.owner_id=:owner_id and owner_type=:owner_type and a.id = b.role_id";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
			q.setInteger("owner_id", groupid);
			q.setInteger("owner_type", Privilege.OWNERTYPEUSERGROUP);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
}
