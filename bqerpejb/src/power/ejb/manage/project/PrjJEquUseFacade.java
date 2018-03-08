package power.ejb.manage.project;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity PrjJEquUse.
 * 
 * @see power.ejb.manage.project.PrjJEquUse
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PrjJEquUseFacade implements PrjJEquUseFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public PageObject getEquDept(Long id)
	{
		String sql=
			"select" +
			" e.id," +
			"e.check_sign_id," +
			"e.dept_id , " +
			"d.dept_name\n," +
			"e.check_text," +
			"e.sign_by," +
			"getworkername(e.sign_by)," +
			"to_char(e.sign_date,'yyyy-MM-dd') " +
			"from  PRJ_J_EQU_USE   e,hr_c_dept  d\n" +
			"where  d.dept_id=e.dept_id\n" + 
			"and e.check_sign_id='"+id+"' " +
			"and e.is_use='Y'\n " +
			"and d.is_use='Y'\n"; //update by sychen 20100902
//		"and d.is_use='U'\n";
// System.out.println("the sql"+sql);
    PageObject  obj=new PageObject();
    List list=bll.queryByNativeSQL(sql);
    obj.setList(list);
		return obj;
		
	}
	public  void  delEquUse(String ids)
	{
		String sql="update PRJ_J_EQU_USE  e\n" +
				"  set e.is_use='N'\n" +
				"  where e.id  in  ("+ids+")";
		bll.exeNativeSQL(sql);
		
	}
	public void  saveEquDept(List<PrjJEquUse>addList,List<PrjJEquUse>updateList)
	{
		
		
		for(PrjJEquUse  entity:addList)
		{
			this.save(entity);
			entityManager.flush();
		}

		for(PrjJEquUse  model:updateList)
		{
			this.update(model);
			entityManager.flush();
		}
		
		
	}
	public void save(PrjJEquUse entity) {
		LogUtil.log("saving PrjJEquUse instance", Level.INFO, null);
		try {
			Long id=bll.getMaxId("PRJ_J_EQU_USE", "id");
			entity.setId(id);
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(PrjJEquUse entity) {
		LogUtil.log("deleting PrjJEquUse instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PrjJEquUse.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public PrjJEquUse update(PrjJEquUse entity) {
		LogUtil.log("updating PrjJEquUse instance", Level.INFO, null);
		try {
			PrjJEquUse result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PrjJEquUse findById(Long id) {
		LogUtil.log("finding PrjJEquUse instance with id: " + id, Level.INFO,
				null);
		try {
			PrjJEquUse instance = entityManager.find(PrjJEquUse.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	

}