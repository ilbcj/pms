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
import com.pms.model.UserRole;
import com.pms.model.UserRoleView;

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
	public List<Privilege> QueryPrivilegesByOwnerId(String ownerid, int ownertype)
			throws Exception {
 		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Privilege> rs = null;
		String sqlString = "select * from privilege where owner_id = :owner_id and owner_type = :owner_type ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Privilege.class);
			q.setString("owner_id", ownerid);
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
	
	public void UpdatePrivilegeByOwnerId(String ownerid, int ownertype,
			List<Privilege> privileges) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String sqlString = "delete from privilege where owner_id = :owner_id and owner_type = :owner_type ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setString("owner_id", ownerid);
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
	public List<ResRole> QueryPrivInfosByUserid(String userid) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRole> rs = null;
		String sqlString = "SELECT a.* FROM res_role a, privilege b where b.owner_id=:owner_id and owner_type=:owner_type and a.id = b.role_id";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
			q.setString("owner_id", userid);
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
	public List<ResRole> QueryPrivInfosByUsersOrg(String userid) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResRole> rs = null;
		String sqlString = "SELECT a.* FROM res_role a, privilege b where b.owner_id=(select parent_id from user where certificate_code_md5 = :userid) and owner_type=:owner_type and a.id = b.role_id ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
			q.setString("userid", userid);
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
	public List<ResRole> QueryPrivInfosByUsersGroup(String groupid)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<ResRole> rs = null;
		String sqlString = "SELECT a.* FROM res_role a, privilege b where b.owner_id=:owner_id and owner_type=:owner_type and a.id = b.role_id";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResRole.class);
			q.setString("owner_id", groupid);
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
	
	@Override
	public UserRole UserRoleUpdate(UserRole  userRole) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();		
		try 
		{	
			userRole = (UserRole) session.merge(userRole);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名用户。");
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
		return userRole;
	}
	
	
	@Override
	public UserRole GetUserRoleByUserIdRoleID(String uid, String rid) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		UserRole rs = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE_ROLE where CERTIFICATE_CODE_MD5 = :CERTIFICATE_CODE_MD5 and BUSINESS_ROLE = :BUSINESS_ROLE";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(UserRole.class);
			q.setString("CERTIFICATE_CODE_MD5", uid);
			q.setString("BUSINESS_ROLE", rid);
			rs = (UserRole) q.uniqueResult();
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
	
	@Override
	public UserRoleView GetUserRoleViewByUserIdRoleID(String uid, String rid) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		UserRoleView rs = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE_ROLE_VIEW where CERTIFICATE_CODE_MD5 = :CERTIFICATE_CODE_MD5 and role_id = :role_id";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(UserRoleView.class);
			q.setString("CERTIFICATE_CODE_MD5", uid);
			q.setString("role_id", rid);
			rs = (UserRoleView) q.uniqueResult();
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
	public List<UserRoleView> QueryUserRoleView() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<UserRoleView> rs = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE_ROLE_VIEW";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(UserRoleView.class);
			
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
	public List<UserRole> QueryUserRole() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<UserRole> rs = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE_ROLE";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(UserRole.class);
			
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
