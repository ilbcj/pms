package com.pms.dao.impl;


import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ImportLogDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ImportLog;
import com.pms.util.DateTimeUtil;

public class ImportLogDAOImpl implements ImportLogDAO {

	@Override
	public ImportLog ImportLogAdd(ImportLog log) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			log.setTs(new Date().getTime());
			log.setTsStr(DateTimeUtil.GetCurrentTime());
			log = (ImportLog) session.merge(log);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重复日志记录。");
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
		return log;
	}
}
