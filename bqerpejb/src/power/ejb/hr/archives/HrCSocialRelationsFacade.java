package power.ejb.hr.archives;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrCSocialRelations.
 * 
 * @see power.ejb.hr.archives.HrCSocialRelations
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCSocialRelationsFacade implements HrCSocialRelationsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public HrCSocialRelations save(HrCSocialRelations entity) {
		LogUtil.log("saving HrCSocialRelations instance", Level.INFO, null);
		try {
			entity.setRelationId(bll.getMaxId("hr_c_social_relations", "relation_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids) {
		String sql = "update hr_c_social_relations s\n" +
			"   set s.is_use = 'N'\n" + 
			" where s.relation_id in ("+ids+")";
		bll.exeNativeSQL(sql);
	}

	public HrCSocialRelations update(HrCSocialRelations entity) {
		LogUtil.log("updating HrCSocialRelations instance", Level.INFO, null);
		try {
			HrCSocialRelations result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCSocialRelations findById(Long id) {
		LogUtil.log("finding HrCSocialRelations instance with id: " + id,
				Level.INFO, null);
		try {
			HrCSocialRelations instance = entityManager.find(
					HrCSocialRelations.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findSocialRelationList(String empId,String enterpriseCode,final int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		String sql = "select s.relation_id,\n" +
			"       s.emp_id,\n" + 
			"       i.new_emp_code,\n" + 
			"       i.chs_name,\n" + 
			"       s.relation_name,\n" + 
			"       s.relation_title,\n" + 
			"       s.is_death,\n" + 
			"       to_char(s.death_date,'yyyy-MM-dd'),\n" + 
			"       s.is_major_problem,\n" + 
			"       s.major_problem,\n" + 
			"       s.professional,\n" + 
			"       s.face,\n" + 
			"       s.is_nationals\n" + 
			"  from hr_c_social_relations s, hr_j_emp_info i\n" + 
			" where s.emp_id = i.emp_id\n" + 
			"   and s.emp_id = "+empId+"\n" + 
			"   and s.is_use = 'Y'\n" + 
			"   and i.is_use = 'Y'\n" + 
			"   and s.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and i.enterprise_code = '"+enterpriseCode+"'";
		
		String sqlCount = "select count(1) from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}
	
	@SuppressWarnings("unchecked")
	public void importSocialRelationFilesInfo(List<HrCSocialRelations> empList) {
		Long nextId = bll.getMaxId("hr_c_social_relations", "relation_id");
		for(HrCSocialRelations emp : empList){
			emp = this.save(emp);
			entityManager.merge(emp);
			String sql = "select a.* from hr_c_social_relations a where a.relation_id='"+emp.getRelationId()+"'";
			List<HrCSocialRelations> selectList = bll.queryByNativeSQL(sql, HrCSocialRelations.class);
			if(selectList != null && selectList.size() > 0){
				HrCSocialRelations updated = selectList.get(0);
				updated.setEmpId(emp.getEmpId());
				updated.setDeathDate(emp.getDeathDate());
				updated.setFace(emp.getFace());
				updated.setIsDeath(emp.getIsDeath());
				updated.setIsMajorProblem(emp.getIsMajorProblem());
				updated.setIsNationals(emp.getIsNationals());
				updated.setMajorProblem(emp.getMajorProblem());
				updated.setProfessional(emp.getProfessional());
				updated.setRelationName(emp.getRelationName());
				updated.setRelationTitle(emp.getRelationTitle());
				
				entityManager.merge(updated);
			}else{
				emp.setRelationId(nextId++);
				entityManager.persist(emp);
			}
		}
	}
	
}