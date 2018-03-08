package power.ejb.equ.planrepair;

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
 * Facade for entity EquCPlanType.
 * add by drdu090922
 * @see power.ejb.equ.planrepair.EquCPlanType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCPlanTypeFacade implements EquCPlanTypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public EquCPlanType save(EquCPlanType entity) throws CodeRepeatException {
		LogUtil.log("saving EquCPlanType instance", Level.INFO, null);
		try {
			if (!this.checkNameSame(entity.getPlanTypeName())) {
				entity.setPlanTypeId(bll.getMaxId("EQU_C_PLAN_TYPE","plan_type_id"));
				entity.setIsUse("Y");
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids) {
		String sql = "update EQU_C_PLAN_TYPE a set a.is_use = 'N'\n"
				+ " where a.plan_type_id in (" + ids + ")";

		bll.exeNativeSQL(sql);
	}

	public EquCPlanType update(EquCPlanType entity) throws CodeRepeatException {
		LogUtil.log("updating EquCPlanType instance", Level.INFO, null);
		try {
			if(!this.checkNameSame(entity.getPlanTypeName(), entity.getPlanTypeId()))
			{
			EquCPlanType result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}else{
				throw new CodeRepeatException("名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCPlanType findById(Long id) {
		LogUtil.log("finding EquCPlanType instance with id: " + id, Level.INFO,
				null);
		try {
			EquCPlanType instance = entityManager.find(EquCPlanType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unused")
	private boolean checkNameSame(String planTypeName,Long ... id)
	{
		String sql=
			"select count(*)\n" +
			"from EQU_C_PLAN_TYPE t\n" + 
			"where t.plan_type_name='"+planTypeName+"' \n";
		if(id!=null&&id.length>0)
		{
			sql=sql+" and t.plan_type_id <>"+id[0];
		}
       int count=Integer.parseInt(bll.getSingal(sql).toString());
       if(count>0) return true;
       else return false;
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findEquPlanTypeList(String enterpriseCode,String planTypeName,final int... rowStartIdxAndCount)
	{
		if(planTypeName == null){
			planTypeName = "";
		}
		PageObject result = new PageObject();
		String sqlCount = "select count(*)\n" +
			"  from EQU_C_PLAN_TYPE a\n" + 
			" where a.is_use = 'Y' and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.plan_type_name like '%"+planTypeName+"%'";
		Long count=Long.parseLong(bll.getSingal(sqlCount).toString());
		if(count > 0)
		{
			String sql = "select *\n" +
			"  from EQU_C_PLAN_TYPE a\n" + 
			" where a.is_use = 'Y' and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.plan_type_name like '%"+planTypeName+"%'";
			List<EquCPlanType> list = bll.queryByNativeSQL(sql, EquCPlanType.class,
					rowStartIdxAndCount);
			result.setList(list);
			result.setTotalCount(count);
		}
		return result;
	}
}