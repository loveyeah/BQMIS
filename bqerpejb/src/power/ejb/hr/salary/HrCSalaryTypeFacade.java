package power.ejb.hr.salary;

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
 * Facade for entity HrCSalaryType.
 * 
 * @see power.ejb.hr.salary.HrCSalaryType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCSalaryTypeFacade implements HrCSalaryTypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public HrCSalaryType save(HrCSalaryType entity) throws CodeRepeatException {
		LogUtil.log("saving HrCSalaryType instance", Level.INFO, null);
		try {
			if(!this.ifHasTypeName(entity.getSalaryTypeName(), entity.getEnterpriseCode()))
			{
			entity.setSalaryTypeId(bll.getMaxId("HR_C_SALARY_TYPE", "salary_type_id"));
			entity.setIsUse("Y");
			entity.setIsBasicData("0");
			entity.setIsInput("1");//默认为输入
			entity.setModifyDate(new java.util.Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
			}
			else
			{
				throw new CodeRepeatException("类别名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(String ids) {
	 String sql="update HR_C_SALARY_TYPE t set t.is_use = 'N' where t.salary_type_id in ("+ids+")";
	 bll.exeNativeSQL(sql);
	}

	
	public HrCSalaryType update(HrCSalaryType entity) throws CodeRepeatException {
		LogUtil.log("updating HrCSalaryType instance", Level.INFO, null);
		try {
			if(!this.ifHasTypeName(entity.getSalaryTypeName(), entity.getEnterpriseCode(),entity.getSalaryTypeId()))
			{
			entity.setModifyDate(new java.util.Date());
			HrCSalaryType result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}
			else
			{
				throw new CodeRepeatException("类别名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCSalaryType findById(Long id) {
		LogUtil.log("finding HrCSalaryType instance with id: " + id,
				Level.INFO, null);
		try {
			HrCSalaryType instance = entityManager
					.find(HrCSalaryType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String isBasicData,String enterpriseCode,final int... rowStartIdxAndCount) {
		String sqlCount=
		"select count(1)\n" +
		"  from HR_C_SALARY_TYPE t\n" + 
		" where t.is_basic_data = '"+isBasicData+"'\n" + 
		" and t.enterprise_code='"+enterpriseCode+"'";
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		if(totalCount>0)
		{
			PageObject obj=new PageObject();
			obj.setTotalCount(totalCount);
		  String sql=
			"select t.salary_type_id,\n" +
			"       t.salary_type_name,\n" + 
			"       t.is_input,\n" + 
			"       t.is_basic_data,\n" + 
			"       t.is_need,\n" + 
			"       GETWORKERNAME(t.modify_by) modify_by,\n" + 
			"       t.modify_date,\n" + 
			"       t.is_use,\n" + 
			"       t.enterprise_code\n" + 
			"  from HR_C_SALARY_TYPE t\n" + 
			" where t.is_use = 'Y' and t.is_basic_data = '"+isBasicData+"'\n" + 
			" and t.enterprise_code='"+enterpriseCode+"'";
		  List<HrCSalaryType> list=bll.queryByNativeSQL(sql, HrCSalaryType.class,rowStartIdxAndCount);
		  obj.setList(list);
		  return obj;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<HrCSalaryType> findUseSalaryTypeList(String enterpriseCode)
	{
		String sql=
			"select * from HR_C_SALARY_TYPE t\n" +
			"where t.is_use='Y'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_need='1'";
		  List<HrCSalaryType> list=bll.queryByNativeSQL(sql, HrCSalaryType.class);
		  return list;
	}
	
	private boolean ifHasTypeName(String typeName,String enterpriseCode,Long ... typeId)
	{
		String sql=
			"select count(1)\n" +
			"  from HR_C_SALARY_TYPE t\n" + 
			" where t.salary_type_name = '"+typeName+"'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'";
		if(typeId!=null&&typeId.length>0)
		{
			sql+="   and  t.salary_type_id<>"+typeId[0];
		}
		if(Long.parseLong(bll.getSingal(sql).toString())>0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

}