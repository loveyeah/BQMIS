package power.ejb.workticket;

import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class RunCWorkticketSafetyKeyFacade implements
		RunCWorkticketSafetyKeyFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public boolean checkSafeCodeForAdd(String safetyKeyName,String keyType,String workticketTypeCode,
			String enterpriseCode, Long... safetyKeyId) {
		boolean isSame = false;
		String sql ="select count(*)\n" +
					"  from RUN_C_WORKTICKET_SAFETY_KEY t\n" + 
					" where t.SAFETY_KEY_NAME = '"+safetyKeyName+"'\n" + 
					"   and t.key_type = '"+keyType+"'\n" + 
					"   and t.workticket_type_code = '"+workticketTypeCode+"'\n" + 
					"   and t.Enterprise_Code = '"+enterpriseCode+"'\n" + 
					"   and t.is_use = 'Y'";

		if (safetyKeyId != null && safetyKeyId.length > 0) {
			sql += "  and t.SAFETY_KEY_ID <> " + safetyKeyId[0];
		}
		if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
			isSame = true;
		}
		return isSame;
	}
	

	private void updateReverse(String enterpriseCode,Long safeKeyId,final Long... reverseId)
	{
		
		String mysql=
			"update run_c_workticket_safety_key t\n" +
			"set t.reverse_key_id="+safeKeyId+"\n" + 
			"where  t.safety_key_id in\n" + 
			"(\n" + 
			"select a.reverse_key_id from run_c_workticket_safety_key a\n" + 
			"where a.safety_key_id="+safeKeyId+"   and a.key_type='1'\n" + 
			" )";
		bll.exeNativeSQL(mysql);
		String sql=
			"update run_c_workticket_safety_key t\n" +
			"set t.reverse_key_id=null\n";
		if(reverseId!=null&&reverseId.length>0)
		{
		  sql=sql+	"   where (t.reverse_key_id="+safeKeyId+"  or t.reverse_key_id="+reverseId[0]+") and t.safety_key_id<>"+safeKeyId+"  and t.safety_key_id<>"+reverseId[0]+"\n" ;
		}
		else
		{
			sql=sql+"   where t.reverse_key_id="+safeKeyId+"  and t.safety_key_id<>"+safeKeyId+"  \n";
		}
	        sql=sql+"  and t.is_use='Y'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"' \n"+
			"and t.key_type='1'";
	     
	        bll.exeNativeSQL(sql);

		
	}

	public RunCWorkticketSafetyKey save(RunCWorkticketSafetyKey entity)
			throws CodeRepeatException { 
		try {
			if (!this.checkSafeCodeForAdd(entity.getSafetyKeyName(),entity.getKeyType(),entity.getWorkticketTypeCode(),entity
					.getEnterpriseCode())) {
				if (entity.getSafetyKeyId() == null) {
					entity.setSafetyKeyId(bll.getMaxId(
							"RUN_C_WORKTICKET_SAFETY_KEY", "SAFETY_KEY_ID"));
				}
				entity.setModifyDate(new java.util.Date());
				entity.setIsUse("Y");
				entityManager.persist(entity); 
				entityManager.flush();
				if(entity.getKeyType().equals("1"))
				{
				  this.updateReverse(entity.getEnterpriseCode(),entity.getSafetyKeyId(), entity.getReverseKeyId());
				}
				return entity;
			} else {
				throw new CodeRepeatException("安全关键词名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(Long safetyKeyId) {
		
		
		String sql = "update RUN_C_WORKTICKET_SAFETY_KEY t\n"
			+ "set t.is_use='N'\n" + "where t.SAFETY_KEY_ID = " + safetyKeyId; 
	   bll.exeNativeSQL(sql); 
	   String updatesql=
			"update run_c_workticket_safety_key t\n" +
			"set t.reverse_key_id=null\n" + 
			"where  t.safety_key_id in\n" + 
			"(\n" + 
			"select a.reverse_key_id from run_c_workticket_safety_key a\n" + 
			"where a.safety_key_id ="+safetyKeyId+"\n" + 
			" )\n" + 
			"";
		bll.exeNativeSQL(updatesql);
	
	}

	public void deleteMulti(String safetyKeyIds) {
		String sql = "update RUN_C_WORKTICKET_SAFETY_KEY t\n"
				+ "set t.is_use='N'\n" + "where t.SAFETY_KEY_ID in(" + safetyKeyIds
				+ ")";
		bll.exeNativeSQL(sql);
		
		String updatesql=
			"update run_c_workticket_safety_key t\n" +
			"set t.reverse_key_id=null\n" + 
			"where  t.safety_key_id in\n" + 
			"(\n" + 
			"select a.reverse_key_id from run_c_workticket_safety_key a\n" + 
			"where a.safety_key_id in ("+safetyKeyIds+")\n" + 
			" )\n" + 
			"";
		bll.exeNativeSQL(updatesql);
	}

	public RunCWorkticketSafetyKey update(RunCWorkticketSafetyKey entity)
			throws CodeRepeatException { 
		try {
			if (!this.checkSafeCodeForAdd(entity.getSafetyKeyName(),entity.getKeyType(),entity.getWorkticketTypeCode(),entity
					.getEnterpriseCode(), entity.getSafetyKeyId())) {
				entity.setModifyDate(new java.util.Date());
			
				RunCWorkticketSafetyKey result = entityManager.merge(entity); 
				entityManager.flush();
			
			    this.updateReverse(entity.getEnterpriseCode(),entity.getSafetyKeyId(), entity.getReverseKeyId());
			
				
				return result;
			} else {
				throw new CodeRepeatException("安全关键词名称不能重复!");
			}

		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCWorkticketSafetyKey findById(Long id) { 
		try {
			RunCWorkticketSafetyKey instance = entityManager.find(
					RunCWorkticketSafetyKey.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode,String workticketTypeCode,String keyType,
			final int... rowStartIdxAndCount) { 
		try {
			PageObject result = new PageObject();
			String sql = "select * from RUN_C_WORKTICKET_SAFETY_KEY t\n"
					+ "where  t.enterprise_code=? and (t.WORKTICKET_TYPE_CODE=? or t.WORKTICKET_TYPE_CODE ='C') \n"
					+ "and t.key_type=? and t.is_use='Y' order by t.WORKTICKET_TYPE_CODE ,t.order_by";
			List<RunCWorkticketSafetyKey> list = bll.queryByNativeSQL(sql,new Object[]{
					enterpriseCode,workticketTypeCode,keyType
			},RunCWorkticketSafetyKey.class, rowStartIdxAndCount);
			String sqlCount = "select count(1) from RUN_C_WORKTICKET_SAFETY_KEY t\n"
				+ "where  t.enterprise_code=? and (t.WORKTICKET_TYPE_CODE=? or t.WORKTICKET_TYPE_CODE ='C') \n"
				+ "and t.key_type=? and t.is_use='Y' order by t.WORKTICKET_TYPE_CODE ,t.order_by";
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount,new Object[]{
							enterpriseCode,workticketTypeCode,keyType}).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result; 
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	public PageObject findAllWithComm(String enterpriseCode,String workticketTypeCode,String keyType,
			final int... rowStartIdxAndCount) { 
		try {
			PageObject result = new PageObject();
			String sql = "select * from RUN_C_WORKTICKET_SAFETY_KEY t\n"
					+ "where  t.enterprise_code=? and (t.WORKTICKET_TYPE_CODE=? or t.WORKTICKET_TYPE_CODE ='C') \n"
					+ "and t.key_type=? and t.is_use='Y' order by t.WORKTICKET_TYPE_CODE ,t.order_by";
			List<RunCWorkticketSafetyKey> list = bll.queryByNativeSQL(sql,new Object[]{
					enterpriseCode,workticketTypeCode,keyType
			},RunCWorkticketSafetyKey.class, rowStartIdxAndCount);
			String sqlCount = "select count(1) from RUN_C_WORKTICKET_SAFETY_KEY t\n"
				+ "where  t.enterprise_code=? and (t.WORKTICKET_TYPE_CODE=? or t.WORKTICKET_TYPE_CODE ='C') \n"
				+ "and t.key_type=? and t.is_use='Y' order by t.WORKTICKET_TYPE_CODE ,t.order_by";
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount,new Object[]{
							enterpriseCode,workticketTypeCode,keyType}).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result; 
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
}