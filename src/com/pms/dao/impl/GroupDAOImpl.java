package com.pms.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.GroupDAO;
import com.pms.model.Group;
import com.pms.model.GroupRule;
import com.pms.model.GroupUser;
import com.pms.model.HibernateUtil;
import com.pms.model.Rule;
import com.pms.model.RuleAttr;
import com.pms.model.User;


public class GroupDAOImpl implements GroupDAO {

	@Override
	public Group GroupAdd(Group group) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			group = (Group) session.merge(group);
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
		return group;
	}

	@Override
	public void UpdateGroupUsers(String id, List<String> userIds) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String sqlString = "delete from group_user where groupid = :groupid ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setString("groupid", id);
			q.executeUpdate();
			
			GroupUser gu;
			if(userIds != null) {
				for(int i = 0; i<userIds.size(); i++) {
					gu = new GroupUser();
					gu.setGroupid(id);
					gu.setUserid(userIds.get(i));
					session.merge(gu);
				}
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Group> GetGroupUsers(Group criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Group> rs = null;
		String sqlString = "select * from groups where type = :type ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getCode() != null && criteria.getCode().length() > 0) {
				sqlString += " and code = :code ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Group.class);
			q.setInteger("type", Group.GROUPTYPEUSER);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getCode() != null && criteria.getCode().length() > 0) {
					q.setString( "code", criteria.getCode());
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
	public int GetGroupUsersCount(Group criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from groups where type = :type ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getCode() != null && criteria.getCode().length() > 0) {
				sqlString += " and code = :code ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("type", Group.GROUPTYPEUSER);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getCode() != null && criteria.getCode().length() > 0) {
					q.setString( "code", criteria.getCode());
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
	public List<User> GetGroupUsersByGroupId(String id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<User> rs = null;
		String sqlString = "SELECT * FROM WA_AUTHORITY_POLICE where certificate_code_md5 in (SELECT userid FROM group_user where groupid = :groupid) ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
			q.setString("groupid", id);
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
	public void GroupOfUserDel(Group group) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String sqlString = "delete from group_user where groupid = :groupid ";
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setString("groupid", group.getCode());
			q.executeUpdate();
			
			sqlString = "delete from groups where code = :code ";
			q = session.createSQLQuery(sqlString);
			q.setString("code", group.getCode());
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
	
//	public void RuleDel(Rule rule) throws Exception {
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		
//		try {
//			session.delete(rule);
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
//	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GroupUser> GetGroupsByUserId(String id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<GroupUser> rs = null;
		String sqlString = "select * from group_user where userid = :userid ";
				
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(GroupUser.class);
			q.setString("userid", id);
			
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
	public List<Group> GetAllGroups(Group criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Group> rs = null;
		String sqlString = "select * from groups where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getCode() != null && criteria.getCode().length() > 0) {
				sqlString += " and code = :code ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Group.class);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getCode() != null && criteria.getCode().length() > 0) {
					q.setString( "code", criteria.getCode());
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

	@SuppressWarnings("unchecked")
	@Override
	public void UpdateGroupRules(String id, List<String> ruleValue, List<Rule> rules) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String sqlString = "select * from group_rule where groupid = :groupid ";
		List<GroupRule> rs = null;
		//sqlString = "delete from group_rule where groupid = :groupid ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(GroupRule.class);
			q.setString("groupid", id);
			rs = q.list();
			//delete rules
			Rule rule = null;
			for(int i = 0; i<rs.size(); i++) {
				rule = new Rule();
				rule.setId(rs.get(i).getRuleid());
				session.delete(rule);
				
				sqlString = "delete from rule_attr where ruleid = :ruleid ";
				q = session.createSQLQuery(sqlString);
				q.setInteger("ruleid", rs.get(i).getRuleid());
				q.executeUpdate();
			}
			
			sqlString = "delete from group_rule where groupid = :groupid ";
			q = session.createSQLQuery(sqlString);
			q.setString("groupid", id);
			q.executeUpdate();
			
			
			GroupRule gr;
			RuleAttr ruleAttr;
			rule = null;
			if(rules != null) {
				for(int i = 0; i<rules.size(); i++) {
					rule = rules.get(i);
					rule.setGroupid(id);
					rule = (Rule)session.merge(rule);
					
					ruleAttr = new RuleAttr();
					String[] strArray = null;
					strArray = ruleValue.get(i).split(",");
					for (int z = 0; z < strArray.length; z++) {
						ruleAttr.setRuleid(rule.getId());
						ruleAttr.setRulevalue(strArray[z]);
						session.merge(ruleAttr);
					}
					
					gr = new GroupRule();
					gr.setGroupid(id);
					gr.setRuleid(rule.getId());
					session.merge(gr);
				}
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Group> GetGroupRules(Group criteria, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Group> rs = null;
		String sqlString = "select * from groups where type = :type ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getCode() != null && criteria.getCode().length() > 0) {
				sqlString += " and code = :code ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Group.class);
			q.setInteger("type", Group.GROUPTYPERULE);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getCode() != null && criteria.getCode().length() > 0) {
					q.setString( "code", criteria.getCode());
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
	public int GetGroupRulesCount(Group criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from groups where type = :type ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getCode() != null && criteria.getCode().length() > 0) {
				sqlString += " and code = :code ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setInteger("type", Group.GROUPTYPERULE);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getCode() != null && criteria.getCode().length() > 0) {
					q.setString( "code", criteria.getCode());
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
	public List<Rule> GetGroupRulesByGroupId(String id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Rule> rs = null;
		String sqlString = "select * from rule where groupid = :groupid ";
				
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Rule.class);
			q.setString("groupid", id);
			
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
	public List<Group> GetGroupByGroupId(String id) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Group> rs = null;
		String sqlString = "select * from groups where code = :code ";
				
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Group.class);
			q.setString("code", id);
			
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
	public void GroupOfRuleDel(Group group) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String  sqlString = "select * from group_rule where groupid = :groupid ";
		List<GroupRule> rs = null;
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(GroupRule.class);
			q.setString("groupid", group.getCode());
			rs = q.list();
			
			sqlString = "delete from groups where code = :code ";
			q = session.createSQLQuery(sqlString);
			q.setString("code", group.getCode());
			q.executeUpdate();
			
			sqlString = "delete from group_rule where groupid = :groupid ";
			q = session.createSQLQuery(sqlString);
			q.setString("groupid", group.getCode());
			q.executeUpdate();
			
			sqlString = "delete from rule where groupid = :groupid ";
			q = session.createSQLQuery(sqlString);
			q.setString("groupid", group.getCode());
			q.executeUpdate();
			
			for(int i = 0; i<rs.size(); i++) {
				sqlString = "delete from rule_attr where ruleid = :ruleid ";
				q = session.createSQLQuery(sqlString);
				q.setInteger("ruleid", rs.get(i).getRuleid());
				q.executeUpdate();
			}
		
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
	public List<RuleAttr> GetRuleAttrByRuleId(int ruleid) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<RuleAttr> rs = null;
		String sqlString = "select * from rule_attr where ruleid = :ruleid ";
				
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(RuleAttr.class);
			q.setInteger("ruleid", ruleid);
			
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
