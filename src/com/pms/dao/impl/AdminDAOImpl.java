package com.pms.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.AdminDAO;
import com.pms.model.Admin;
import com.pms.model.HibernateUtil;

public class AdminDAOImpl implements AdminDAO{

	public void AdminAdd(Admin admin) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			session.save(admin);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			
			throw new Exception("存在重名管理员�?");
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
		return;
	}
	
	@Override
	public Admin AdminsAdd(Admin admin) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			admin = (Admin) session.merge(admin);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名群体。");
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
		return admin;
	}

	public void AdminMod(Admin admin) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.update(admin);
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

	public void AdminDel(Admin admin) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.delete(admin);
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
	public List<Admin> GetAllAdmins() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Admin> rs = null;
		String sqlString = "select * from admin ";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Admin.class);
			
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
	public List<Admin> GetAdmins(Admin criteria, int page, int rows) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Admin> rs = null;
		String sqlString = "select * from admin where 1=1 ";
		if( criteria != null ) {
			if(criteria.getLoginid() != null && criteria.getLoginid().length() > 0) {
				sqlString += " and loginid = :loginid ";
			}
			if(criteria.getStatus() != 0) {
				sqlString += " and status = :status ";
			}
		}
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Admin.class);
			if( criteria != null ) {
				if(criteria.getLoginid() != null && criteria.getLoginid().length() > 0) {
					q.setString( "loginid", criteria.getLoginid() );
				}
				if(criteria.getStatus() != 0) {
					q.setInteger( "status", criteria.getStatus() );
				}
			}
			if( page > 0 && rows >0 ) {
				q.setFirstResult((page-1) * rows);   
				q.setMaxResults(rows);   
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

	public Admin GetAdminById(int id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		Admin rs = null;
		String sqlString = "select * from admin where id = :id";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Admin.class);
			q.setInteger("id", id);

			rs = (Admin)q.uniqueResult();
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

	public Admin GetAdminByLoginid(String loginid) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		Admin rs = null;
		String sqlString = "select * from admin where loginid = :loginid";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Admin.class);
			q.setString("loginid", loginid);

			rs = (Admin)q.uniqueResult();
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
	public void AdminOfAccreditDel(Admin admin) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Admin> rs = null;
		String sqlString = "select * from admin where id = :id";
		
		try
		{
			Query q = session.createSQLQuery(sqlString).addEntity(Admin.class);
			q.setInteger("id", admin.getId());
			rs = q.list();
			
			Admin ad= null;
			for(int i = 0; i<rs.size(); i++) {
				ad = new Admin();
				ad.setId(rs.get(i).getId());
				ad.setLoginid(rs.get(i).getLoginid());
				ad.setName(rs.get(i).getName());
				ad.setUnit(rs.get(i).getUnit());
				ad.setPwd(rs.get(i).getPwd());
				ad.setStatus(Admin.DELETE);
				ad.setFrozentime(rs.get(i).getFrozentime());
				ad.setErrorcount(rs.get(i).getErrorcount());
				session.merge(ad);
			}
			
			sqlString = "delete from accredit where aid = :aid ";
			q = session.createSQLQuery(sqlString);
			q.setInteger("aid", admin.getId());
			q.executeUpdate();
			
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

}
