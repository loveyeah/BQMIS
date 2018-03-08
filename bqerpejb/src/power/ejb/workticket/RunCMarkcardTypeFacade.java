package power.ejb.workticket;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity RunCMarkcardType.
 * 
 * @see power.ejb.workticket.RunCMarkcardType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCMarkcardTypeFacade implements RunCMarkcardTypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public RunCMarkcardType save(RunCMarkcardType entity)
			throws CodeRepeatException {
		try {
			if (!this.checkMarkcardTypeName(entity.getMarkcardTypeName(), entity
					.getEnterpriseCode())) {
				if (entity.getMarkcardTypeId() == null) {
					entity.setMarkcardTypeId(bll.getMaxId(
							"RUN_C_MARKCARD_TYPE", "markcard_type_id"));
				}
				entity.setModifyDate(new java.util.Date());
				entity.setIsUse("Y");
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("标识牌类型名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(Long markcardTypeId) throws CodeRepeatException {
		try {
			RunCMarkcardType entity=this.findById(markcardTypeId);
			 if(entity!=null)
			 {
				 entity.setIsUse("N");
				 this.update(entity);
			 }
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void deleteMulti(String markcardTypeIds)
	{
		String sql=
			"update  RUN_C_MARKCARD_TYPE a\n" +
			"set a.is_use='N' \n"+
            "where a.markcard_type_id in ("+markcardTypeIds+")";
		bll.exeNativeSQL(sql);
	}

	
	public RunCMarkcardType update(RunCMarkcardType entity)
			throws CodeRepeatException {
		LogUtil.log("updating RunCMarkcardType instance", Level.INFO, null);
		try {
			if (!this.checkMarkcardTypeName(entity.getMarkcardTypeName(),
					entity.getEnterpriseCode(), entity.getMarkcardTypeId())) {
				entity.setModifyDate(new java.util.Date());
				RunCMarkcardType result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			} else {
				throw new CodeRepeatException("标识牌类型名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCMarkcardType findById(Long id) {
		LogUtil.log("finding RunCMarkcardType instance with id: " + id,
				Level.INFO, null);
		try {
			RunCMarkcardType instance = entityManager.find(
					RunCMarkcardType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}


	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode,String markcardTypeName, final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCMarkcardType instances", Level.INFO, null);
		try {
			PageObject result = new PageObject(); 
			String sql=
				"select * from RUN_C_MARKCARD_TYPE t\n" +
				"where t.markcard_type_name like '%"+markcardTypeName+"%'\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y' "+
				"order by t.MARKCARD_TYPE_ID";
            List<RunCMarkcardType> list=bll.queryByNativeSQL(sql, RunCMarkcardType.class, rowStartIdxAndCount);
            String sqlCount=
            	"select count(*) from RUN_C_MARKCARD_TYPE t\n" +
				"where t.markcard_type_name like '%"+markcardTypeName+"%'\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y'";
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
            return result;
			
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	public boolean checkMarkcardTypeName(String cardTypeName,String enterpriseCode,Long... cardtypeId)
	{
		boolean isSame = false;
		String sql="select count(*) from RUN_C_MARKCARD_TYPE t\n" +
					"where t.markcard_type_name = '"+cardTypeName+"'\n" + 
					"and t.is_use = 'Y'\n" + 
					"and t.enterprise_code = '"+enterpriseCode+"'";

	    if(cardtypeId !=null&& cardtypeId.length>0){
	    	sql += "  and t.markcard_type_id <> " + cardtypeId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}

}