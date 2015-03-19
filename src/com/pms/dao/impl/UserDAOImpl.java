package com.pms.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.UserDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.User;

public class UserDAOImpl implements UserDAO {

	@Override
	public User UserAdd(User user) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			user = (User) session.merge(user);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名机构。");
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
		return user;
	}

	@Override
	public void UserMod(User user) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.update(user);
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
	public void UserDel(User user) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.delete(user);
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
	public List<User> GetUsersByParentId(int pid, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<User> rs = null;
		String sqlString = "select * from user where parent_id = :parent_id ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
			q.setInteger("parent_id", pid);
			q.setFirstResult((page-1) * rows);   
			q.setMaxResults(rows);   
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
	public List<User> GetUsersByParentIdWithNoPage(int pid, User criteria)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<User> rs = null;
		String sqlString = "select * from user where parent_id = :parent_id ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getStatus() != User.STATUSNONE) {
				sqlString += " and status = :status ";
			}
			if(criteria.getUnit() != null && criteria.getUnit().length() > 0) {
				sqlString += " and unit like :unit ";
			}
			if(criteria.getPolice_type() != null && criteria.getPolice_type().length() > 0) {
				sqlString += " and police_type like :police_type ";
			}
			if(criteria.getSex() != null && criteria.getSex().length() > 0) {
				sqlString += " and sex like :sex ";
			}
			if(criteria.getIdnum() != null && criteria.getIdnum().length() > 0) {
				sqlString += " and idnum like :idnum ";
			}
			if(criteria.getMax_sensitive_level() != null && criteria.getMax_sensitive_level().length() > 0) {
				sqlString += " and max_sensitive_level like :max_sensitive_level ";
			}
			if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
				sqlString += " and position like :position ";
			}
			if(criteria.getDept() != null && criteria.getDept().length() > 0) {
				sqlString += " and dept like :dept ";
			}
			if(criteria.getTitle() != null && criteria.getTitle().length() > 0) {
				sqlString += " and title like :title ";
			}
			if(criteria.getPolice_num() != null && criteria.getPolice_num().length() > 0) {
				sqlString += " and police_num like :police_num ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
			q.setInteger("parent_id", pid);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getStatus() != User.STATUSNONE) {
					q.setInteger( "status", criteria.getStatus() );
				}
	
				if(criteria.getUnit() != null && criteria.getUnit().length() > 0) {
					q.setString( "unit", "%" + criteria.getUnit() + "%" );
				}
				if(criteria.getPolice_type() != null && criteria.getPolice_type().length() > 0) {
					q.setString( "police_type", "%" + criteria.getPolice_type() + "%" );
				}
				if(criteria.getSex() != null && criteria.getSex().length() > 0) {
					q.setString( "sex", "%" + criteria.getSex() + "%" );
				}
				if(criteria.getIdnum() != null && criteria.getIdnum().length() > 0) {
					q.setString( "idnum", "%" + criteria.getIdnum() + "%" );
				}
				if(criteria.getMax_sensitive_level() != null && criteria.getMax_sensitive_level().length() > 0) {
					q.setString( "max_sensitive_level", "%" + criteria.getMax_sensitive_level() + "%" );
				}
				if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
					q.setString( "position", "%" + criteria.getPosition() + "%" );
				}
				if(criteria.getDept() != null && criteria.getDept().length() > 0) {
					q.setString( "dept", "%" + criteria.getDept() + "%" );
				}
				if(criteria.getTitle() != null && criteria.getTitle().length() > 0) {
					q.setString( "title", "%" + criteria.getTitle() + "%" );
				}
				if(criteria.getPolice_num() != null && criteria.getPolice_num().length() > 0) {
					q.setString( "police_num", "%" + criteria.getPolice_num() + "%" );
				}
			}
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
	public int GetUsersCountByParentId(int pid, User criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from user where parent_id = :parent_id ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getStatus() != User.STATUSNONE) {
				sqlString += " and status = :status ";
			}
			if(criteria.getUnit() != null && criteria.getUnit().length() > 0) {
				sqlString += " and unit like :unit ";
			}
			if(criteria.getPolice_type() != null && criteria.getPolice_type().length() > 0) {
				sqlString += " and police_type like :police_type ";
			}
			if(criteria.getSex() != null && criteria.getSex().length() > 0) {
				sqlString += " and sex like :sex ";
			}
			if(criteria.getIdnum() != null && criteria.getIdnum().length() > 0) {
				sqlString += " and idnum like :idnum ";
			}
			if(criteria.getMax_sensitive_level() != null && criteria.getMax_sensitive_level().length() > 0) {
				sqlString += " and max_sensitive_level like :max_sensitive_level ";
			}
			if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
				sqlString += " and position like :position ";
			}
			if(criteria.getDept() != null && criteria.getDept().length() > 0) {
				sqlString += " and dept like :dept ";
			}
			if(criteria.getTitle() != null && criteria.getTitle().length() > 0) {
				sqlString += " and title like :title ";
			}
			if(criteria.getPolice_num() != null && criteria.getPolice_num().length() > 0) {
				sqlString += " and police_num like :police_num ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("parent_id", pid);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getStatus() != User.STATUSNONE) {
					q.setInteger( "status", criteria.getStatus() );
				}
	
				if(criteria.getUnit() != null && criteria.getUnit().length() > 0) {
					q.setString( "unit", "%" + criteria.getUnit() + "%" );
				}
				if(criteria.getPolice_type() != null && criteria.getPolice_type().length() > 0) {
					q.setString( "police_type", "%" + criteria.getPolice_type() + "%" );
				}
				if(criteria.getSex() != null && criteria.getSex().length() > 0) {
					q.setString( "sex", "%" + criteria.getSex() + "%" );
				}
				if(criteria.getIdnum() != null && criteria.getIdnum().length() > 0) {
					q.setString( "idnum", "%" + criteria.getIdnum() + "%" );
				}
				if(criteria.getMax_sensitive_level() != null && criteria.getMax_sensitive_level().length() > 0) {
					q.setString( "max_sensitive_level", "%" + criteria.getMax_sensitive_level() + "%" );
				}
				if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
					q.setString( "position", "%" + criteria.getPosition() + "%" );
				}
				if(criteria.getDept() != null && criteria.getDept().length() > 0) {
					q.setString( "dept", "%" + criteria.getDept() + "%" );
				}
				if(criteria.getTitle() != null && criteria.getTitle().length() > 0) {
					q.setString( "title", "%" + criteria.getTitle() + "%" );
				}
				if(criteria.getPolice_num() != null && criteria.getPolice_num().length() > 0) {
					q.setString( "police_num", "%" + criteria.getPolice_num() + "%" );
				}
			}
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

//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Organization> GetOrgNodeByParentId(int pid) throws Exception {
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		List<Organization> rs = null;
//		String sqlString = "select * from organization where parent_id = :parent_id ";
//		try {
//			Query q = session.createSQLQuery(sqlString).addEntity(Organization.class);
//			q.setInteger("parent_id", pid);
//			rs = q.list();
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//			System.out.println(e.getMessage());
//			throw e;
//		} finally {
//			HibernateUtil.closeSession();
//		}
//		return rs;
//	}
//
//	@Override
//	public boolean OrgHasChild(int pid) throws Exception {
//		int rs = GetOrgNodeCountByParentId(pid);
//		return rs > 0;
//	}
//

//	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Organization> GetOrgNodeByParentId(int pid, Organization condition)
//			throws Exception {
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		List<Organization> rs = null;
//		String sqlString = "select * from organization where parent_id = :parent_id ";
//		if(condition.getName() != null && condition.getName().length() > 0) {
//			sqlString += " and name like :name ";
//		}
//		if(condition.getUid() != null && condition.getUid().length() > 0) {
//			sqlString += " and uid = :uid ";
//		}
//		if(condition.getOrg_level() != null && condition.getOrg_level().length() > 0) {
//			sqlString += " and org_level = :org_level ";
//		}
//			
//		try {
//			Query q = session.createSQLQuery(sqlString).addEntity(Organization.class);
//			q.setInteger("parent_id", pid);
//			if(condition.getName() != null && condition.getName().length() > 0) {
//				q.setString( "name", "%" + condition.getName() + "%" );
//			}
//			if(condition.getUid() != null && condition.getUid().length() > 0) {
//				q.setString( "uid", condition.getUid() );
//			}
//			if(condition.getOrg_level() != null && condition.getOrg_level().length() > 0) {
//				q.setString( "org_level", condition.getOrg_level() );
//			}
//			rs = q.list();
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//			System.out.println(e.getMessage());
//			throw e;
//		} finally {
//			HibernateUtil.closeSession();
//		}
//		return rs;
//	}
//

//
//	@SuppressWarnings("unchecked")
//	@Override
//	public Organization GetOrgNodeById(int id) throws Exception {
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		Organization rs = null;
//		String sqlString = "select * from organization where id = :id ";
//		try {
//			Query q = session.createSQLQuery(sqlString).addEntity(Organization.class);
//			q.setInteger("id", id);
//			rs = (Organization) q.uniqueResult();
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//			System.out.println(e.getMessage());
//			throw e;
//		} finally {
//			HibernateUtil.closeSession();
//		}
//		return rs;
//	}

}
