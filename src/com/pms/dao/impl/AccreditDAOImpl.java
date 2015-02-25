package com.pms.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.AccreditDAO;
import com.pms.model.AdminAccredit;
import com.pms.model.HibernateUtil;

public class AccreditDAOImpl implements AccreditDAO{

	public void AccreditMod(int adminid, int[] privilegeids) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			String sqlString = "delete from accredit where aid = :aid ";
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("aid", adminid);
			q.executeUpdate();
			
			AdminAccredit aa = new AdminAccredit();
			aa.setAid(adminid);
			aa.setPid(0);
			session.save(aa);
			if(privilegeids != null)
			{
				for(int i=0;i<privilegeids.length;i++)
				{
					aa = new AdminAccredit();
					aa.setAid(adminid);
					aa.setPid(privilegeids[i]);
					session.save(aa);
				}
				
			}
			
			tx.commit();
			
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			
			throw cne.getSQLException();
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
	
	public void AccreditDel(int adminid) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			String sqlString = "delete from accredit where aid = :aid ";
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("aid", adminid);
			q.executeUpdate();
	
			tx.commit();
			
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			
			throw cne.getSQLException();
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

	@SuppressWarnings("unchecked")
	public List<AdminAccredit> GetAdminAccreditById(int id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AdminAccredit> rs = null;
		String sqlString = "select * from accredit where aid = :aid";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AdminAccredit.class);
			q.setInteger("aid", id);

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
