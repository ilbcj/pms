package com.pms.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.AuditLogDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.AuditLog;

public class AuditLogDAOImpl implements AuditLogDAO {
	
	@Override
	public AuditLog AuditLogAdd(AuditLog auditLog) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			auditLog = (AuditLog) session.merge(auditLog);
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
		return auditLog;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditLog> GetAllAuditLogs(AuditLog criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AuditLog> rs = null;
		String sqlString = "select * from wa_authority_auditlog where 1 = 1 ";
		if( criteria != null ) {
//			if(criteria.getName() != null && criteria.getName().length() > 0) {
//				sqlString += " and name like :name ";
//			}
//			if(criteria.getCode() != null && criteria.getCode().length() > 0) {
//				sqlString += " and code = :code ";
//			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AuditLog.class);
			if( criteria != null ) {
//				if(criteria.getName() != null && criteria.getName().length() > 0) {
//					q.setString( "name", "%" + criteria.getName() + "%" );
//				}
//				if(criteria.getCode() != null && criteria.getCode().length() > 0) {
//					q.setString( "code", criteria.getCode());
//				}
			}
			if( page > 0 && rows > 0) {
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
	
	@Override
	public int GetAuditLogsCount(AuditLog criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from wa_authority_auditlog where 1=1 ";
		if( criteria != null ) {
//			if(criteria.getName() != null && criteria.getName().length() > 0) {
//			sqlString += " and name like :name ";
//		}
//		if(criteria.getCode() != null && criteria.getCode().length() > 0) {
//			sqlString += " and code = :code ";
//		}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			if( criteria != null ) {
//				if(criteria.getName() != null && criteria.getName().length() > 0) {
//				q.setString( "name", "%" + criteria.getName() + "%" );
//			}
//			if(criteria.getCode() != null && criteria.getCode().length() > 0) {
//				q.setString( "code", criteria.getCode());
//			}
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
}