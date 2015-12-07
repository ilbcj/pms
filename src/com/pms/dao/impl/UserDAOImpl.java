package com.pms.dao.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.UserDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.Organization;
import com.pms.model.User;
import com.pms.model.UserImport;

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
	public List<User> GetAllUsers() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<User> rs = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE ";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
			
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
	public List<User> GetAllUsers(User criteria, int page, int rows) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<User> rs = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE where DELETE_STATUS =:DELETE_STATUS";

		if( criteria != null ) {
			if(criteria.getNAME() != null && criteria.getNAME().length() > 0) {
				sqlString += " and NAME like :NAME ";
			}
			sqlString += " and USER_STATUS = :USER_STATUS ";
			if(criteria.getBUSINESS_TYPE() != null && criteria.getBUSINESS_TYPE().length() > 0) {
				sqlString += " and BUSINESS_TYPE = :BUSINESS_TYPE ";
			}
			if(criteria.getPOLICE_SORT() != null && criteria.getPOLICE_SORT().length() > 0) {
				sqlString += " and POLICE_SORT = :POLICE_SORT ";
			}
			if(criteria.getSEXCODE() != null && criteria.getSEXCODE().length() > 0) {
				sqlString += " and SEXCODE = :SEXCODE ";
			}
			if(criteria.getCERTIFICATE_CODE_SUFFIX() != null && criteria.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
				sqlString += " and CERTIFICATE_CODE_SUFFIX like :CERTIFICATE_CODE_SUFFIX ";
			}
			if(criteria.getSENSITIVE_LEVEL() != null && criteria.getSENSITIVE_LEVEL().length() > 0) {
				sqlString += " and SENSITIVE_LEVEL = :SENSITIVE_LEVEL ";
			}
			if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
				sqlString += " and position like :position ";
			}
			if(criteria.getDept() != null && criteria.getDept().length() > 0) {
				sqlString += " and dept like :dept ";
			}
			if(criteria.getTAKE_OFFICE() != null && criteria.getTAKE_OFFICE().length() > 0) {
				sqlString += " and TAKE_OFFICE = :TAKE_OFFICE ";
			}
			if(criteria.getPOLICE_NO() != null && criteria.getPOLICE_NO().length() > 0) {
				sqlString += " and POLICE_NO = :POLICE_NO ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
			if( criteria != null ) {
				q.setInteger( "DELETE_STATUS", criteria.getDELETE_STATUS() );
				if(criteria.getNAME() != null && criteria.getNAME().length() > 0) {
					q.setString( "NAME", "%" + criteria.getNAME() + "%" );
				}
				q.setInteger( "USER_STATUS", criteria.getUSER_STATUS() );
				if(criteria.getBUSINESS_TYPE() != null && criteria.getBUSINESS_TYPE().length() > 0) {
					q.setString( "BUSINESS_TYPE", criteria.getBUSINESS_TYPE() );
				}
				if(criteria.getPOLICE_SORT() != null && criteria.getPOLICE_SORT().length() > 0) {
					q.setString( "POLICE_SORT", criteria.getPOLICE_SORT() );
				}
				if(criteria.getSEXCODE() != null && criteria.getSEXCODE().length() > 0) {
					q.setString( "SEXCODE", criteria.getSEXCODE() );
				}
				if(criteria.getCERTIFICATE_CODE_SUFFIX() != null && criteria.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
					q.setString( "CERTIFICATE_CODE_SUFFIX", "%" + criteria.getCERTIFICATE_CODE_SUFFIX() + "%" );
				}
				if(criteria.getSENSITIVE_LEVEL() != null && criteria.getSENSITIVE_LEVEL().length() > 0) {
					q.setString( "SENSITIVE_LEVEL", criteria.getSENSITIVE_LEVEL() );
				}
				if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
					q.setString( "position", "%" + criteria.getPosition() + "%" );
				}
				if(criteria.getDept() != null && criteria.getDept().length() > 0) {
					q.setString( "dept", "%" + criteria.getDept() + "%" );
				}
				if(criteria.getTAKE_OFFICE() != null && criteria.getTAKE_OFFICE().length() > 0) {
					q.setString( "TAKE_OFFICE", criteria.getTAKE_OFFICE() );
				}
				if(criteria.getPOLICE_NO() != null && criteria.getPOLICE_NO().length() > 0) {
					q.setString( "POLICE_NO", criteria.getPOLICE_NO() );
				}
			}
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
	
	@Override
	public int GetAllUsersCount(User criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from WA_AUTHORITY_POLICE where DELETE_STATUS =:DELETE_STATUS";
		if( criteria != null ) {
			if(criteria.getNAME() != null && criteria.getNAME().length() > 0) {
				sqlString += " and NAME like :NAME ";
			}
			sqlString += " and USER_STATUS = :USER_STATUS ";
			if(criteria.getBUSINESS_TYPE() != null && criteria.getBUSINESS_TYPE().length() > 0) {
				sqlString += " and BUSINESS_TYPE = :BUSINESS_TYPE ";
			}
			if(criteria.getPOLICE_SORT() != null && criteria.getPOLICE_SORT().length() > 0) {
				sqlString += " and POLICE_SORT = :POLICE_SORT ";
			}
			if(criteria.getSEXCODE() != null && criteria.getSEXCODE().length() > 0) {
				sqlString += " and SEXCODE = :SEXCODE ";
			}
			if(criteria.getCERTIFICATE_CODE_SUFFIX() != null && criteria.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
				sqlString += " and CERTIFICATE_CODE_SUFFIX like :CERTIFICATE_CODE_SUFFIX ";
			}
			if(criteria.getSENSITIVE_LEVEL() != null && criteria.getSENSITIVE_LEVEL().length() > 0) {
				sqlString += " and SENSITIVE_LEVEL = :SENSITIVE_LEVEL ";
			}
			if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
				sqlString += " and position like :position ";
			}
			if(criteria.getDept() != null && criteria.getDept().length() > 0) {
				sqlString += " and dept like :dept ";
			}
			if(criteria.getTAKE_OFFICE() != null && criteria.getTAKE_OFFICE().length() > 0) {
				sqlString += " and TAKE_OFFICE = :TAKE_OFFICE ";
			}
			if(criteria.getPOLICE_NO() != null && criteria.getPOLICE_NO().length() > 0) {
				sqlString += " and POLICE_NO = :POLICE_NO ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			if( criteria != null ) {
				q.setInteger( "DELETE_STATUS", criteria.getDELETE_STATUS() );
				if(criteria.getNAME() != null && criteria.getNAME().length() > 0) {
					q.setString( "NAME", "%" + criteria.getNAME() + "%" );
				}
				q.setInteger( "USER_STATUS", criteria.getUSER_STATUS() );
				if(criteria.getBUSINESS_TYPE() != null && criteria.getBUSINESS_TYPE().length() > 0) {
					q.setString( "BUSINESS_TYPE", criteria.getBUSINESS_TYPE() );
				}
				if(criteria.getPOLICE_SORT() != null && criteria.getPOLICE_SORT().length() > 0) {
					q.setString( "POLICE_SORT", criteria.getPOLICE_SORT() );
				}
				if(criteria.getSEXCODE() != null && criteria.getSEXCODE().length() > 0) {
					q.setString( "SEXCODE", criteria.getSEXCODE() );
				}
				if(criteria.getCERTIFICATE_CODE_SUFFIX() != null && criteria.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
					q.setString( "CERTIFICATE_CODE_SUFFIX", "%" + criteria.getCERTIFICATE_CODE_SUFFIX() + "%" );
				}
				if(criteria.getSENSITIVE_LEVEL() != null && criteria.getSENSITIVE_LEVEL().length() > 0) {
					q.setString( "SENSITIVE_LEVEL", criteria.getSENSITIVE_LEVEL() );
				}
				if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
					q.setString( "position", "%" + criteria.getPosition() + "%" );
				}
				if(criteria.getDept() != null && criteria.getDept().length() > 0) {
					q.setString( "dept", "%" + criteria.getDept() + "%" );
				}
				if(criteria.getTAKE_OFFICE() != null && criteria.getTAKE_OFFICE().length() > 0) {
					q.setString( "TAKE_OFFICE", criteria.getTAKE_OFFICE() );
				}
				if(criteria.getPOLICE_NO() != null && criteria.getPOLICE_NO().length() > 0) {
					q.setString( "POLICE_NO", criteria.getPOLICE_NO()  );
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> GetUsersByParentId(String pid, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<User> rs = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE where GA_DEPARTMENT = :GA_DEPARTMENT and DELETE_STATUS =:DELETE_STATUS ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
			q.setString("GA_DEPARTMENT", pid);
			q.setInteger("DELETE_STATUS", User.DELSTATUSNO );
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
	public List<User> GetUsersById(String id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<User> rs = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE where GA_DEPARTMENT = :GA_DEPARTMENT";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
			q.setString( "GA_DEPARTMENT", id);
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
	public List<User> GetUsersByParentIdWithNoPage(String pid, User criteria)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<User> rs = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE where GA_DEPARTMENT = :GA_DEPARTMENT and DELETE_STATUS =:DELETE_STATUS";
		if( criteria != null ) {
			if(criteria.getNAME() != null && criteria.getNAME().length() > 0) {
				sqlString += " and NAME like :NAME ";
			}
			sqlString += " and USER_STATUS = :USER_STATUS ";
			if(criteria.getBUSINESS_TYPE() != null && criteria.getBUSINESS_TYPE().length() > 0) {
				sqlString += " and BUSINESS_TYPE = :BUSINESS_TYPE ";
			}
			if(criteria.getPOLICE_SORT() != null && criteria.getPOLICE_SORT().length() > 0) {
				sqlString += " and POLICE_SORT = :POLICE_SORT ";
			}
			if(criteria.getSEXCODE() != null && criteria.getSEXCODE().length() > 0) {
				sqlString += " and SEXCODE = :SEXCODE ";
			}
			if(criteria.getCERTIFICATE_CODE_SUFFIX() != null && criteria.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
				sqlString += " and CERTIFICATE_CODE_SUFFIX like :CERTIFICATE_CODE_SUFFIX ";
			}
			if(criteria.getSENSITIVE_LEVEL() != null && criteria.getSENSITIVE_LEVEL().length() > 0) {
				sqlString += " and SENSITIVE_LEVEL = :SENSITIVE_LEVEL ";
			}
			if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
				sqlString += " and position like :position ";
			}
			if(criteria.getDept() != null && criteria.getDept().length() > 0) {
				sqlString += " and dept like :dept ";
			}
			if(criteria.getTAKE_OFFICE() != null && criteria.getTAKE_OFFICE().length() > 0) {
				sqlString += " and TAKE_OFFICE = :TAKE_OFFICE ";
			}
			if(criteria.getPOLICE_NO() != null && criteria.getPOLICE_NO().length() > 0) {
				sqlString += " and POLICE_NO = :POLICE_NO ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
			q.setString("GA_DEPARTMENT", pid);
			if( criteria != null ) {
				q.setInteger( "DELETE_STATUS", criteria.getDELETE_STATUS() );
				if(criteria.getNAME() != null && criteria.getNAME().length() > 0) {
					q.setString( "NAME", "%" + criteria.getNAME() + "%" );
				}
				q.setInteger( "USER_STATUS", criteria.getUSER_STATUS() );
				if(criteria.getBUSINESS_TYPE() != null && criteria.getBUSINESS_TYPE().length() > 0) {
					q.setString( "BUSINESS_TYPE", criteria.getBUSINESS_TYPE() );
				}
				if(criteria.getPOLICE_SORT() != null && criteria.getPOLICE_SORT().length() > 0) {
					q.setString( "POLICE_SORT", criteria.getPOLICE_SORT() );
				}
				if(criteria.getSEXCODE() != null && criteria.getSEXCODE().length() > 0) {
					q.setString( "SEXCODE", criteria.getSEXCODE() );
				}
				if(criteria.getCERTIFICATE_CODE_SUFFIX() != null && criteria.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
					q.setString( "CERTIFICATE_CODE_SUFFIX", "%" + criteria.getCERTIFICATE_CODE_SUFFIX() + "%" );
				}
				if(criteria.getSENSITIVE_LEVEL() != null && criteria.getSENSITIVE_LEVEL().length() > 0) {
					q.setString( "SENSITIVE_LEVEL", criteria.getSENSITIVE_LEVEL() );
				}
				if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
					q.setString( "position", "%" + criteria.getPosition() + "%" );
				}
				if(criteria.getDept() != null && criteria.getDept().length() > 0) {
					q.setString( "dept", "%" + criteria.getDept() + "%" );
				}
				if(criteria.getTAKE_OFFICE() != null && criteria.getTAKE_OFFICE().length() > 0) {
					q.setString( "TAKE_OFFICE", criteria.getTAKE_OFFICE() );
				}
				if(criteria.getPOLICE_NO() != null && criteria.getPOLICE_NO().length() > 0) {
					q.setString( "POLICE_NO", criteria.getPOLICE_NO() );
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
	public int GetUsersCountByParentId(String pid, User criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from WA_AUTHORITY_POLICE where GA_DEPARTMENT = :GA_DEPARTMENT and DELETE_STATUS =:DELETE_STATUS";
		if( criteria != null ) {
			if(criteria.getNAME() != null && criteria.getNAME().length() > 0) {
				sqlString += " and NAME like :NAME ";
			}
//			if(criteria.getStatus() != User.STATUSNONE) {
//				sqlString += " and status = :status ";
//			}
			if(criteria.getBUSINESS_TYPE() != null && criteria.getBUSINESS_TYPE().length() > 0) {
				sqlString += " and BUSINESS_TYPE = :BUSINESS_TYPE ";
			}
			if(criteria.getPOLICE_SORT() != null && criteria.getPOLICE_SORT().length() > 0) {
				sqlString += " and POLICE_SORT = :POLICE_SORT ";
			}
			if(criteria.getSEXCODE() != null && criteria.getSEXCODE().length() > 0) {
				sqlString += " and SEXCODE = :SEXCODE ";
			}
			if(criteria.getCERTIFICATE_CODE_SUFFIX() != null && criteria.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
				sqlString += " and CERTIFICATE_CODE_SUFFIX like :CERTIFICATE_CODE_SUFFIX ";
			}
			if(criteria.getSENSITIVE_LEVEL() != null && criteria.getSENSITIVE_LEVEL().length() > 0) {
				sqlString += " and SENSITIVE_LEVEL = :SENSITIVE_LEVEL ";
			}
			if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
				sqlString += " and position like :position ";
			}
			if(criteria.getDept() != null && criteria.getDept().length() > 0) {
				sqlString += " and dept like :dept ";
			}
			if(criteria.getTAKE_OFFICE() != null && criteria.getTAKE_OFFICE().length() > 0) {
				sqlString += " and TAKE_OFFICE = :TAKE_OFFICE ";
			}
			if(criteria.getPOLICE_NO() != null && criteria.getPOLICE_NO().length() > 0) {
				sqlString += " and POLICE_NO = :POLICE_NO ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setString("GA_DEPARTMENT", pid);
			q.setInteger("DELETE_STATUS", User.DELSTATUSNO );
			if( criteria != null ) {
				if(criteria.getNAME() != null && criteria.getNAME().length() > 0) {
					q.setString( "NAME", "%" + criteria.getNAME() + "%" );
				}
//				if(criteria.getStatus() != User.STATUSNONE) {
//					q.setInteger( "status", criteria.getStatus() );
//				}
	
				if(criteria.getBUSINESS_TYPE() != null && criteria.getBUSINESS_TYPE().length() > 0) {
					q.setString( "BUSINESS_TYPE", criteria.getBUSINESS_TYPE() );
				}
				if(criteria.getPOLICE_SORT() != null && criteria.getPOLICE_SORT().length() > 0) {
					q.setString( "POLICE_SORT", criteria.getPOLICE_SORT() );
				}
				if(criteria.getSEXCODE() != null && criteria.getSEXCODE().length() > 0) {
					q.setString( "SEXCODE", criteria.getSEXCODE() );
				}
				if(criteria.getCERTIFICATE_CODE_SUFFIX() != null && criteria.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
					q.setString( "CERTIFICATE_CODE_SUFFIX", "%" + criteria.getCERTIFICATE_CODE_SUFFIX() + "%" );
				}
				if(criteria.getSENSITIVE_LEVEL() != null && criteria.getSENSITIVE_LEVEL().length() > 0) {
					q.setString( "SENSITIVE_LEVEL",  criteria.getSENSITIVE_LEVEL() );
				}
				if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
					q.setString( "position", "%" + criteria.getPosition() + "%" );
				}
				if(criteria.getDept() != null && criteria.getDept().length() > 0) {
					q.setString( "dept", "%" + criteria.getDept() + "%" );
				}
				if(criteria.getTAKE_OFFICE() != null && criteria.getTAKE_OFFICE().length() > 0) {
					q.setString( "TAKE_OFFICE", criteria.getTAKE_OFFICE() );
				}
				if(criteria.getPOLICE_NO() != null && criteria.getPOLICE_NO().length() > 0) {
					q.setString( "POLICE_NO", criteria.getPOLICE_NO() );
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
//	public User GetUserByUserName(String name)
//			throws Exception {
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		List<User> rs = null;
//		String sqlString = "select * from WA_AUTHORITY_POLICE where NAME = :NAME ";
//		
//		try {
//			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
//			q.setString("NAME", name);
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
//		if(rs == null) {
//			return null;
//		}
//		else if ( rs.size() != 1) {
//			throw new Exception("query user by name(" + name + ") returns multiple records.");
//		}
//		return rs.get(0);
//	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> GetUsersByTime(String time) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<User> rs = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE where LATEST_MOD_TIME > :LATEST_MOD_TIME";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
			q.setString( "LATEST_MOD_TIME", time);
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
	//public List<User> GetUserById(int id) throws Exception {
	public User GetUserByCertificateCodeMd5(String id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<User> rs = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE where certificate_code_md5 = :certificate_code_md5 ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
			q.setString("certificate_code_md5", id);
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
		if(rs == null) {
			return null;
		}
		else if ( rs.size() != 1) {
			throw new Exception("query user by certificate_code_md5(" + id + ") returns multiple records.");
		}
		return rs.get(0);
	}
	
	@Override
	public void UserImportSave(UserImport ui) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			ui = (UserImport) session.merge(ui);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名待导入用户。");
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
		return ;
	}

	@Override
	public int UserImportClear() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs = 0;
		String sqlString = "delete from WA_AUTHORITY_POLICE_IMPORT ";
		try {
			Query q = session.createSQLQuery(sqlString);
			rs = q.executeUpdate();
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
	public List<UserImport> GetUserImports() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<UserImport> rs = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE_IMPORT ";

		try {
			Query q = session.createSQLQuery(sqlString).addEntity(UserImport.class);
			
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
	public void UserImport(UserImport ui, Organization org) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		User user = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE where CERTIFICATE_CODE_MD5 = :CERTIFICATE_CODE_MD5 ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
			q.setString("CERTIFICATE_CODE_MD5", ui.getCERTIFICATE_CODE_MD5());
			user = (User)q.uniqueResult();
			
			if(user == null){
				user = new User();
			} 
			
			if(ui.getCERTIFICATE_CODE_SUFFIX().equals(user.getCERTIFICATE_CODE_SUFFIX())
					&& ui.getGA_DEPARTMENT().equals(user.getGA_DEPARTMENT())
					&& org.getUNIT().equals(user.getUNIT())
					&& org.getORG_LEVEL() == user.getORG_LEVEL()
					&& ui.getNAME().equals(user.getNAME())
					&& ui.getPOLICE_SORT().equals(user.getPOLICE_SORT())
					&& ui.getSEXCODE().equals(user.getSEXCODE())
					&& ui.getTAKE_OFFICE().equals(user.getTAKE_OFFICE()) ) {
				return;
			}
			
			user.setUNIT(org.getUNIT());
			user.setORG_LEVEL(org.getORG_LEVEL());
			user.setGA_DEPARTMENT(ui.getGA_DEPARTMENT());
			user.setCERTIFICATE_CODE_SUFFIX(ui.getCERTIFICATE_CODE_SUFFIX());
			user.setCERTIFICATE_CODE_MD5(ui.getCERTIFICATE_CODE_MD5());
			user.setNAME(ui.getNAME());
			user.setPOLICE_SORT(ui.getPOLICE_SORT());
			user.setTAKE_OFFICE(ui.getTAKE_OFFICE());
//			if(ui.getSEXCODE().length() > 0) {
//				user.setSEXCODE(ui.getSEXCODE());
//			}
			user.setSEXCODE(ui.getSEXCODE());
			user.setDATA_VERSION(user.getDATA_VERSION() + 1);
			user.setDELETE_STATUS(User.DELSTATUSNO);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.SIMPLIFIED_CHINESE);
			String timenow = sdf.format(new Date());
			user.setLATEST_MOD_TIME(timenow);
			
			session.merge(user);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		
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
