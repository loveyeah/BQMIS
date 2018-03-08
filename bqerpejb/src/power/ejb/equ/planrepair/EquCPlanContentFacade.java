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
 * Facade for entity EquCPlanContent.
 * add by drdu 090922
 * @see power.ejb.equ.planrepair.EquCPlanContent
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCPlanContentFacade implements EquCPlanContentFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public EquCPlanContent save(EquCPlanContent entity)
			throws CodeRepeatException {
		LogUtil.log("saving EquCPlanContent instance", Level.INFO, null);
		try {
			if (!this.checkNameSame(entity.getContentName())) {
				entity.setContentId(bll.getMaxId("EQU_C_PLAN_CONTENT","content_id"));
				entity.setIsUse("Y");
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("内容名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids) {
		String sql = "update EQU_C_PLAN_CONTENT a set a.is_use = 'N'\n"
				+ " where a.content_id in (" + ids + ")";

		bll.exeNativeSQL(sql);
	}

	public EquCPlanContent update(EquCPlanContent entity) throws CodeRepeatException {
		LogUtil.log("updating EquCPlanContent instance", Level.INFO, null);
		try {
			if(!this.checkNameSame(entity.getContentName(),entity.getContentId()))
			{
			EquCPlanContent result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}else {
				throw new CodeRepeatException("内容名称不能重复！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCPlanContent findById(Long id) {
		LogUtil.log("finding EquCPlanContent instance with id: " + id,
				Level.INFO, null);
		try {
			EquCPlanContent instance = entityManager.find(
					EquCPlanContent.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unused")
	private boolean checkNameSame(String contentName,Long ... id)
	{
		String sql=
			"select count(*)\n" +
			"from EQU_C_PLAN_CONTENT t\n" + 
			"where t.content_name='"+contentName+"' \n";
		if(id!=null&&id.length>0)
		{
			sql=sql+" and t.content_id <>"+id[0];
		}
       int count=Integer.parseInt(bll.getSingal(sql).toString());
       if(count>0) return true;
       else return false;
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findEquPlanContentList(String enterpriseCode,String contentName,final int... rowStartIdxAndCount)
	{
		if(contentName == null)
			contentName ="";
		PageObject result = new PageObject();
		String sqlCount = "select count(*)\n" +
			"  from EQU_C_PLAN_CONTENT a\n" + 
			" where a.is_use = 'Y' and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.content_name like '%"+contentName+"%'";
		Long count=Long.parseLong(bll.getSingal(sqlCount).toString());
		if(count > 0)
		{
			String sql = "select *\n" +
			"  from EQU_C_PLAN_CONTENT a\n" + 
			" where a.is_use = 'Y' and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.content_name like '%"+contentName+"%'";
			List<EquCPlanContent> list = bll.queryByNativeSQL(sql, EquCPlanContent.class,
					rowStartIdxAndCount);
			result.setList(list);
			result.setTotalCount(count);
		}
		return result;
	}

}