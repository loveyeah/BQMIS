package power.ejb.manage.plan;

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
 * Facade for entity BpCPlanJobDept.
 * 
 * @see power.ejb.manage.plan.BpCPlanJobDept
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCPlanJobDeptFacade implements BpCPlanJobDeptFacadeRemote {
	// property constants
	public static final String DEPT_CODE = "deptCode";
	public static final String ORDER_BY = "orderBy";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public PageObject  getAllDept(String deptName)
	{
		PageObject result=new PageObject();
		String sql=
			" select t.dept_code," +
			" t.dept_name, " +
			" a.id," +
			" a.order_by\n" +
			" from hr_c_dept t," +
			" BP_C_PLAN_JOB_DEPT a\n" + 
			" where t.dept_level = 1\n" + 
			" and t.dept_code = a.dept_code(+)\n" ;
			
       if(deptName!=null&&!deptName.equals(""))
       {
    	   sql+="and  t.dept_name like '%"+deptName+"%'";
       }
       sql+="order by a.order_by";
      List   list=bll.queryByNativeSQL(sql);
      result.setList(list);
       
		return result;
		
	}
	public void saveLevelOneDept(List<BpCPlanJobDept>  addList)
	{
		if (addList != null && addList.size() > 0) {

			Long id = bll.getMaxId("BP_C_PLAN_JOB_DEPT", "id");
			for (BpCPlanJobDept entity : addList) {
				entity.setId(id);
				id++;
				this.save(entity);
			}
		}
	}
	public void updateLevalOneDept(List<BpCPlanJobDept> updateList)
	{
		if (updateList != null && updateList.size() > 0) {
			for (BpCPlanJobDept entity : updateList) {	
				this.update(entity);
			}
		}
	}
	public void save(BpCPlanJobDept entity) {
		LogUtil.log("saving BpCPlanJobDept instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public  void deleteDept(String ids)
	{
	    ids=ids.substring(0, ids.length()-1);
		String sql="delete from BP_C_PLAN_JOB_DEPT  p where  p.id in  ("+ids+")";
		bll.exeNativeSQL(sql);
	}
	public void delete(BpCPlanJobDept entity) {
		LogUtil.log("deleting BpCPlanJobDept instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpCPlanJobDept.class, entity
					.getId());
			entityManager.remove(entity);
			entityManager.flush();
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public BpCPlanJobDept update(BpCPlanJobDept entity) {
		LogUtil.log("updating BpCPlanJobDept instance", Level.INFO, null);
		try {
			BpCPlanJobDept result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCPlanJobDept findById(Long id) {
		LogUtil.log("finding BpCPlanJobDept instance with id: " + id,
				Level.INFO, null);
		try {
			BpCPlanJobDept instance = entityManager.find(BpCPlanJobDept.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

}