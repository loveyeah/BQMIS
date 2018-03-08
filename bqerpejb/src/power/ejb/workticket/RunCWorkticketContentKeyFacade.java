package power.ejb.workticket;

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

/**
 * Facade for entity RunCWorkticketContentKey.
 * 
 * @see power.ejb.workticket.RunCWorkticketContentKey
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCWorkticketContentKeyFacade implements
		RunCWorkticketContentKeyFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public boolean checkSafeCodeForAdd(String contentKeyName,String keyType,String workticketTypeCode,String enterpriseCode,Long... contentKeyId)
	{
		boolean isSame = false;
		String sql="select count(*)\n" +
					"  from RUN_C_WORKTICKET_CONTENT_KEY t\n" + 
					" where t.CONTENT_KEY_NAME = '"+contentKeyName+"'\n" + 
					"   and t.key_type = '"+keyType+"'\n" + 
					"   and t.workticket_type_code = '"+workticketTypeCode+"'\n" + 
					"   and t.enterprise_code = 'hfdc'\n" + 
					"   and t.is_use = 'Y'";

	    if(contentKeyId !=null&& contentKeyId.length>0){
	    	sql += "  and t.CONTENT_KEY_ID <> " + contentKeyId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}
	
	public RunCWorkticketContentKey save(RunCWorkticketContentKey entity) throws CodeRepeatException {
		LogUtil.log("saving RunCWorktickSafety instance", Level.INFO, null);
		try {
			if(!this.checkSafeCodeForAdd(entity.getContentKeyName(),entity.getKeyType(),entity.getWorkticketTypeCode(),entity.getEnterpriseCode()))
			{
				if(entity.getContentKeyId()==null)
				{
					entity.setContentKeyId(bll.getMaxId("RUN_C_WORKTICKET_CONTENT_KEY", "CONTENT_KEY_ID"));
				}
				entity.setModifyDate(new java.util.Date());
				entity.setIsUse("Y");
			  entityManager.persist(entity);
			  LogUtil.log("save successful", Level.INFO, null);
			  return entity;
			}
			else
			{
				throw new CodeRepeatException("内容关键词名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(Long contentKeyId) throws CodeRepeatException{
		RunCWorkticketContentKey entity=this.findById(contentKeyId);
		if(entity!=null)
		{
			entity.setIsUse("N");
			this.update(entity);
		}
	}

	public void deleteMulti(String contentKeyIds)
	{
		String sql=
			"update RUN_C_WORKTICKET_CONTENT_KEY t\n" +
			"set t.is_use='N'\n" + 
			"where t.CONTENT_KEY_ID in("+contentKeyIds+")";
        bll.exeNativeSQL(sql);
		
	}
	
	public RunCWorkticketContentKey update(RunCWorkticketContentKey entity) throws CodeRepeatException {
		LogUtil.log("updating RunCWorkticketContentKey instance", Level.INFO, null);
		try {
			if(!this.checkSafeCodeForAdd(entity.getContentKeyName(),entity.getKeyType(),entity.getWorkticketTypeCode(),entity.getEnterpriseCode(), entity.getContentKeyId()))
			{
				entity.setModifyDate(new java.util.Date());
				RunCWorkticketContentKey result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			}
			else
			{
				throw new CodeRepeatException("内容关键词名称不能重复!");
			}
			
			
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCWorkticketContentKey findById(Long id) {
		LogUtil.log("finding RunCWorkticketContentKey instance with id: " + id,
				Level.INFO, null);
		try {
			RunCWorkticketContentKey instance = entityManager.find(
					RunCWorkticketContentKey.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String workticketTypeCode,String enterpriseCode,String keyType,final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from RUN_C_WORKTICKET_CONTENT_KEY t\n"
					+ "where  t.enterprise_code=? and (t.WORKTICKET_TYPE_CODE=? or t.WORKTICKET_TYPE_CODE ='C') \n"
					+ "and t.key_type=? and t.is_use='Y' order by t.WORKTICKET_TYPE_CODE ,t.order_by";
			List<RunCWorkticketContentKey> list = bll.queryByNativeSQL(sql,new Object[]{
					enterpriseCode,workticketTypeCode,keyType
			},RunCWorkticketContentKey.class, rowStartIdxAndCount);
			String sqlCount = "select count(1) from RUN_C_WORKTICKET_CONTENT_KEY t\n"
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
	@SuppressWarnings("unchecked")
	public PageObject findAllWithComm(String workticketTypeCode,String enterpriseCode,String keyType,final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from RUN_C_WORKTICKET_CONTENT_KEY t\n"
					+ "where  t.enterprise_code=? and (t.WORKTICKET_TYPE_CODE=? or t.WORKTICKET_TYPE_CODE ='C') \n"
					+ "and t.key_type=? and t.is_use='Y' order by t.WORKTICKET_TYPE_CODE ,t.order_by";
			List<RunCWorkticketContentKey> list = bll.queryByNativeSQL(sql,new Object[]{
					enterpriseCode,workticketTypeCode,keyType},RunCWorkticketContentKey.class, rowStartIdxAndCount);
			String sqlCount = "select count(1) from RUN_C_WORKTICKET_CONTENT_KEY t\n"
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