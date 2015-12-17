package com.pms.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.AuditLogDAO;
import com.pms.model.AuditGroupLog;
import com.pms.model.AuditOrgLog;
import com.pms.model.AuditPrivLog;
import com.pms.model.AuditResLog;
import com.pms.model.AuditRoleLog;
import com.pms.model.AuditSystemLog;
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
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag =:flag ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			if( criteria != null ) {
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag",  criteria.getFlag() );
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
	
	@Override
	public AuditOrgLog AuditOrgLogAdd(AuditOrgLog auditOrgLog) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			auditOrgLog = (AuditOrgLog) session.merge(auditOrgLog);
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
		return auditOrgLog;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditOrgLog> GetAllAuditOrgLogs(AuditOrgLog criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AuditOrgLog> rs = null;
		String sqlString = "select * from wa_authority_auditlog_org where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag =:flag ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AuditOrgLog.class);
			if( criteria != null ) {
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag",  criteria.getFlag() );
				}
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
	public int GetAuditOrgLogsCount(AuditOrgLog criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from wa_authority_auditlog_org where 1=1 ";
		if( criteria != null ) {
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag =:flag ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			if( criteria != null ) {
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag",  criteria.getFlag() );
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
	
	@Override
	public AuditGroupLog AuditGroupLogAdd(AuditGroupLog auditGroupLog) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			auditGroupLog = (AuditGroupLog) session.merge(auditGroupLog);
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
		return auditGroupLog;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditGroupLog> GetAllAuditGroupLogs(AuditGroupLog criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AuditGroupLog> rs = null;
		String sqlString = "select * from wa_authority_auditlog_group where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag =:flag ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AuditGroupLog.class);
			if( criteria != null ) {
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag",  criteria.getFlag() );
				}
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
	public int GetAuditGroupLogsCount(AuditGroupLog criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from wa_authority_auditlog_group where 1=1 ";
		if( criteria != null ) {
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag =:flag ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			if( criteria != null ) {
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag",  criteria.getFlag() );
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
	
	@Override
	public AuditRoleLog AuditRoleLogAdd(AuditRoleLog auditRoleLog) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			auditRoleLog = (AuditRoleLog) session.merge(auditRoleLog);
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
		return auditRoleLog;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditRoleLog> GetAllAuditRoleLogs(AuditRoleLog criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AuditRoleLog> rs = null;
		String sqlString = "select * from wa_authority_auditlog_role where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag =:flag ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AuditRoleLog.class);
			if( criteria != null ) {
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag",  criteria.getFlag() );
				}
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
	public int GetAuditRoleLogsCount(AuditRoleLog criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from wa_authority_auditlog_role where 1=1 ";
		if( criteria != null ) {
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag =:flag ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			if( criteria != null ) {
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag",  criteria.getFlag() );
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
	
	@Override
	public AuditSystemLog AuditSystemLogAdd(AuditSystemLog auditSystemLog) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			auditSystemLog = (AuditSystemLog) session.merge(auditSystemLog);
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
		return auditSystemLog;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditSystemLog> GetAllAuditSystemLogs(AuditSystemLog criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AuditSystemLog> rs = null;
		String sqlString = "select * from wa_authority_auditlog_system where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag =:flag ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AuditSystemLog.class);
			if( criteria != null ) {
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag",  criteria.getFlag() );
				}
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
	public int GetAuditSystemLogsCount(AuditSystemLog criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from wa_authority_auditlog_system where 1=1 ";
		if( criteria != null ) {
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag =:flag ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			if( criteria != null ) {
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag",  criteria.getFlag() );
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
	
	@Override
	public AuditResLog AuditResLogAdd(AuditResLog auditResLog) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			auditResLog = (AuditResLog) session.merge(auditResLog);
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
		return auditResLog;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditResLog> GetAllAuditResLogs(AuditResLog criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AuditResLog> rs = null;
		String sqlString = "select * from wa_authority_auditlog_res where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag =:flag ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AuditResLog.class);
			if( criteria != null ) {
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag",  criteria.getFlag() );
				}
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
	public int GetAuditResLogsCount(AuditResLog criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from wa_authority_auditlog_res where 1=1 ";
		if( criteria != null ) {
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag =:flag ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			if( criteria != null ) {
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag",  criteria.getFlag() );
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
	
	@Override
	public AuditPrivLog AuditPrivLogAdd(AuditPrivLog auditPrivLog) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			auditPrivLog = (AuditPrivLog) session.merge(auditPrivLog);
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
		return auditPrivLog;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AuditPrivLog> GetAllAuditPrivLogs(AuditPrivLog criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<AuditPrivLog> rs = null;
		String sqlString = "select * from wa_authority_auditlog_privilege where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag =:flag ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(AuditPrivLog.class);
			if( criteria != null ) {
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag",  criteria.getFlag() );
				}
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
	public int GetAuditPrivLogsCount(AuditPrivLog criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from wa_authority_auditlog_privilege where 1=1 ";
		if( criteria != null ) {
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag =:flag ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			if( criteria != null ) {
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag",  criteria.getFlag() );
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
	
}