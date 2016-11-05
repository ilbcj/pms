package com.pms.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.TaskDAO;
import com.pms.model.Task;
import com.pms.model.HibernateUtil;

public class TaskDAOImpl implements TaskDAO{

	public void TaskAdd(Task task) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		String sqlString = "select * from task where eid = :eid ";
		Task rs = null;
		try
		{
			Query q = session.createSQLQuery(sqlString).addEntity(Task.class);
			q.setString("eid", task.getEid());

			rs = (Task)q.uniqueResult();
			if(rs == null) {
				session.save(task);
			}
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			
			throw new Exception("存在重名任务.");
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

	public void TaskMod(Task task) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.update(task);
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

//	public void TaskDel(Task task) throws Exception {
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		try
//		{
//			session.delete(task);
//			tx.commit();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			tx.rollback();
//			System.out.println(e.getMessage());
//			throw e;
//		}
//		finally
//		{
//			HibernateUtil.closeSession();
//		}
//		return;
//		
//	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Task> GetTasks(Task criteria, int start, int length) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Task> rs = null;
		String sqlString = "select * from task where 1=1 ";
		if( criteria != null ) {
			if(criteria.getMessage() != null && criteria.getMessage().length() > 0) {
				sqlString += " and message like :message ";
			}
			if(criteria.getType() != 0) {
				sqlString += " and type = :type ";
			}
			if(criteria.getStatus() != 0) {
				sqlString += " and status = :status ";
			}
		}
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Task.class);
			if( criteria != null ) {
				if(criteria.getMessage() != null && criteria.getMessage().length() > 0) {
					q.setString( "message", "%" + criteria.getMessage() + "%");
				}
				if(criteria.getType() != 0) {
					q.setInteger( "type", criteria.getType() );
				}
				if(criteria.getStatus() != 0) {
					q.setInteger( "status", criteria.getStatus() );
				}
			}
			if( start > 0 || length >0 ) {
				q.setFirstResult(start);   
				q.setMaxResults(length);   
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

	public Task GetTaskById(int id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		Task rs = null;
		String sqlString = "select * from task where id = :id";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Task.class);
			q.setInteger("id", id);

			rs = (Task)q.uniqueResult();
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
