package com.pms.dao.impl;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.AuditLogDescribeDao;
import com.pms.model.AuditGroupLogDescribe;
import com.pms.model.AuditOrgLogDescribe;
import com.pms.model.HibernateUtil;
import com.pms.model.AuditUserLogDescribe;

public class AuditLogDescribeDAOImpl implements AuditLogDescribeDao {
	
	@Override
	public AuditUserLogDescribe AuditUserLogDescribeAdd(AuditUserLogDescribe auditUserLogDescribe) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			auditUserLogDescribe = (AuditUserLogDescribe) session.merge(auditUserLogDescribe);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名日志。");
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
		return auditUserLogDescribe;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditUserLogDescribe> GetUserLogDescByLogId(int logid) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AuditUserLogDescribe> rs = null;
		String sqlString = "select * from wa_authority_auditlog_user_describe where logid = :logid";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AuditUserLogDescribe.class);
			q.setInteger("logid", logid);
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
	public AuditOrgLogDescribe AuditOrgLogDescribeAdd(AuditOrgLogDescribe auditOrgLogDescribe) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			auditOrgLogDescribe = (AuditOrgLogDescribe) session.merge(auditOrgLogDescribe);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名日志。");
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
		return auditOrgLogDescribe;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditOrgLogDescribe> GetOrgLogDescByLogId(int logid) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AuditOrgLogDescribe> rs = null;
		String sqlString = "select * from wa_authority_auditlog_org_describe where logid = :logid";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AuditOrgLogDescribe.class);
			q.setInteger("logid", logid);
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
	public AuditGroupLogDescribe AuditGroupLogDescribeAdd(AuditGroupLogDescribe auditGroupLogDescribe) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			auditGroupLogDescribe = (AuditGroupLogDescribe) session.merge(auditGroupLogDescribe);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名日志。");
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
		return auditGroupLogDescribe;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditGroupLogDescribe> GetGroupLogDescByLogId(int logid) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AuditGroupLogDescribe> rs = null;
		String sqlString = "select * from wa_authority_auditlog_group_describe where logid = :logid";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AuditGroupLogDescribe.class);
			q.setInteger("logid", logid);
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