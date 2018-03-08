package power.ejb.equ.base;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ejb.comm.NativeSqlHelperRemote;

@Stateless
public class EquJBaseInfoFacade implements EquJBaseInfoFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * 保存设备基础信息
	 * @param entity
	 * @return EquJBaseAnnex
	 */
	public EquJBaseInfo save(EquJBaseInfo entity) {
		Long id=bll.getMaxId("EQU_J_BASE_INFO", "EQU_BASE_ID");
		entity.setEquBaseId(id);
		entity.setIsUse("Y");
		entityManager.persist(entity);
		return entity;
	}
	/**
	 * 删除设备基础信息
	 * 
	 * @param ids
	 */
	public void delete(String ids) {
		StringBuffer sql=new StringBuffer("begin \n");
		sql.append("update EQU_J_BASE_INFO set is_use='N' where EQU_BASE_ID="+ids+";");
		sql.append("update EQU_J_BASE_ANNEX set is_use='N' where EQU_BASE_ID="+ids+" and is_use='Y';");
		sql.append("commit;\n");
		sql.append("end;\n");
		bll.exeNativeSQL(sql.toString());
	}
	/**
	 * 更新设备基础信息
	 * @param entity
	 * @return EquJBaseInfo
	 */
	public EquJBaseInfo update(EquJBaseInfo entity) {
			EquJBaseInfo result = entityManager.merge(entity);
			return result;
	}
	/**
	 * 查找设备基础信息附件信息
	 * @param id
	 * @return EquJBaseInfo
	 */
	public EquJBaseInfo findById(Long id) {
			EquJBaseInfo instance = entityManager.find(EquJBaseInfo.class, id);
			return instance;
	}
	/**
	 * 查找设备基础信息
	 * By 设备编码
	 * @param AttributeCode
	 * @param enterprisecode
	 * @param rowStartIdxAndCount
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List findByAttributeCode(String AttributeCode, String enterprisecode){
		String sql=
			"select t1.equ_base_id,\n" +
			"       t1.attribute_code,\n" + 
			"       t1.manufacturer,\n" + 
			"       t1.model,\n" + 
			"       t1.factory_date,\n" + 
			"       t1.installation_date,\n" + 
			"       t1.installation_code,\n" + 
			"       t2.location_desc,\n" +
			"	 	t1.price,\n" + 
			"       t1.use_year,\n" + 
			"       t1.asset_code,\n" + 
			"       t1.technical_parameters,\n" + 
			"       t1.one_responsible,\n" + 
			"       getworkername(t1.one_responsible),\n" + 
			"       getdeptname(getfirstlevelbyid((select t.dept_id\n" + 
			"                                       from hr_j_emp_info t\n" + 
			"                                      where t.is_use = 'Y'\n" + 
			"                                        and t.enterprise_code = 'hfdc'\n" + 
			"                                        and t.emp_code = t1.one_responsible))),\n" + 
			"       t1.two_responsible,\n" + 
			"       getworkername(t1.two_responsible),\n" + 
			"       getdeptname(getfirstlevelbyid((select t.dept_id\n" + 
			"                                       from hr_j_emp_info t\n" + 
			"                                      where t.is_use = 'Y'\n" + 
			"                                        and t.enterprise_code = 'hfdc'\n" + 
			"                                        and t.emp_code = t1.two_responsible))),\n" + 
			"       t1.three_responsible,\n" + 
			"       getworkername(t1.three_responsible),\n" + 
			"       getdeptname(getfirstlevelbyid((select t.dept_id\n" + 
			"                                       from hr_j_emp_info t\n" + 
			"                                      where t.is_use = 'Y'\n" + 
			"                                        and t.enterprise_code = 'hfdc'\n" + 
			"                                        and t.emp_code =\n" + 
			"                                            t1.three_responsible)))\n" + 
			"  from EQU_J_BASE_INFO t1, equ_c_location t2\n" + 
			" where t1.installation_code = t2.location_code\n" + 
			"   and t1.attribute_code = '"+AttributeCode+"'\n" + 
			"   and t1.is_use = 'Y'\n" + 
			"   and t1.enterprise_code = '"+enterprisecode+"'\n" + 
			"   and t2.is_use = 'Y'\n" + 
			"   and t2.enterprise_code = '"+enterprisecode+"'";

			List list=bll.queryByNativeSQL(sql);
			if (list.size()>0) {
				return list;
			}else{
				return null;
			}
	}
}