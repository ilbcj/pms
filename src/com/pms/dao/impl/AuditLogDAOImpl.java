package com.pms.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.AuditLogDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.AuditUserLog;

public class AuditLogDAOImpl implements AuditLogDAO {
	
	@Override
	public AuditUserLog AuditUserLogAdd(AuditUserLog auditUserLog) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			auditUserLog = (AuditUserLog) session.merge(auditUserLog);
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
		return auditUserLog;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditUserLog> GetAllAuditUserLogs(AuditUserLog criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AuditUserLog> rs = null;
		String sqlString = "select * from wa_authority_auditlog_user where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag =:flag ";
			}
//			if(criteria.getCode() != null && criteria.getCode().length() > 0) {
//				sqlString += " and code = :code ";
//			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AuditUserLog.class);
			if( criteria != null ) {
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag",  criteria.getFlag() );
				}
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
	public int GetAuditUserLogsCount(AuditUserLog criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from wa_authority_auditlog_user where 1=1 ";
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