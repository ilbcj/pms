-- 建立视图 wa_authority_police_role_view 用户和角色关系视图表
CREATE VIEW wa_authority_police_role_view
AS
SELECT u.CERTIFICATE_CODE_MD5 ,p.role_id ,CONCAT(u.CERTIFICATE_CODE_MD5 ,p.role_id) AS id
	FROM privilege p ,wa_authority_police u
		WHERE p.owner_type = 1 AND p.owner_id = u.GA_DEPARTMENT 
UNION DISTINCT
SELECT p.owner_id AS CERTIFICATE_CODE_MD5 ,p.role_id ,CONCAT(p.owner_id ,p.role_id) AS id
	FROM privilege p
		WHERE p.owner_type = 2
UNION DISTINCT
SELECT gu.userid AS CERTIFICATE_CODE_MD5 ,p.role_id ,CONCAT(gu.userid ,p.role_id) AS id
	FROM privilege p ,group_user gu ,groups g
		WHERE p.owner_type = 3 AND p.owner_id = g.code AND gu.groupid = g.code
UNION DISTINCT
SELECT u.CERTIFICATE_CODE_MD5 ,r.business_role ,CONCAT(u.CERTIFICATE_CODE_MD5 ,r.business_role) AS id
	FROM wa_authority_police u, wa_authority_role r, privilege p, rule ru, attrdef attr, rule_attr rv
		WHERE (p.owner_type = 3 AND p.owner_id = ru.groupid  AND ru.attrid = attr.id AND attr.code='SEXCODE' AND rv.rulevalue = u.sexcode  AND  p.role_id = r.business_role AND ru.id = rv.ruleid )
		OR (p.owner_type = 3 AND p.owner_id = ru.groupid  AND ru.attrid = attr.id AND attr.code='BUSINESS_TYPE' AND rv.rulevalue = u.BUSINESS_TYPE  AND  p.role_id = r.business_role AND ru.id = rv.ruleid )
		OR (p.owner_type = 3 AND p.owner_id = ru.groupid  AND ru.attrid = attr.id AND attr.code='POLICE_SORT' AND rv.rulevalue = u.BUSINESS_TYPE  AND  p.role_id = r.business_role AND ru.id = rv.ruleid )
		OR (p.owner_type = 3 AND p.owner_id = ru.groupid  AND ru.attrid = attr.id AND attr.code='SENSITIVE_LEVEL' AND rv.rulevalue = u.BUSINESS_TYPE  AND  p.role_id = r.business_role AND ru.id = rv.ruleid )
-- 查询视图
SELECT * FROM wa_authority_police_role_view;


-- 删除视图
DROP VIEW wa_authority_police_role_view