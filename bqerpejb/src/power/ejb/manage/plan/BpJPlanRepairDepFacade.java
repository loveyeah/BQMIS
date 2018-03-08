package power.ejb.manage.plan;

import java.util.Date;
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
 * Facade for entity BpJPlanRepairDep.
 * 
 * @see power.ejb.manage.plan.BpJPlanRepairDep
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJPlanRepairDepFacade implements BpJPlanRepairDepFacadeRemote {
	// property constants
	public static final String EDIT_DEP = "editDep";
	public static final String EDIT_BY = "editBy";
	public static final String WORK_FLOW_NO = "workFlowNo";
	public static final String STATUS = "status";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved BpJPlanRepairDep entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJPlanRepairDep entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PageObject  findAlldept( String time , int... rowIndexAndCount){
		String sql=
			"select " +
			"d.plan_dep_id , " +
			"d.plan_time," +
			"d.edit_dep," +
			"getdeptname(d.edit_dep)," +
			"d.edit_by," +
			"getworkername(d.edit_by)," +
			"to_char(d.edit_date,'yyyy-MM-dd hh24:mi:ss'),\n" +
			"d.work_flow_no," +
			"d.status," +
			"d.enterprise_code from BP_J_PLAN_REPAIR_DEP d  " ;
			
		
          if(time!=null &&time!="")
          {
        	  sql += " where d.plan_time ="+time+"";
          }
          sql+="order by d.plan_dep_id";
       /*   System.out.println("the dept sql is"+sql);*/
		String count="select count(1) from BP_J_PLAN_REPAIR_DEP d";
		List list=bll.queryByNativeSQL(sql,rowIndexAndCount);
		System.out.println("yfy:"+sql);
		PageObject result= new  PageObject() ;
		result.setList(list);
		result.setTotalCount(Long.parseLong(bll.getSingal(count).toString()));
		return result;
		
	}
	public void save(BpJPlanRepairDep entity) {
		LogUtil.log("saving BpJPlanRepairDep instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent BpJPlanRepairDep entity.
	 * 
	 * @param entity
	 *            BpJPlanRepairDep entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanRepairDep entity) {
		LogUtil.log("deleting BpJPlanRepairDep instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpJPlanRepairDep.class, entity
					.getPlanDepId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpJPlanRepairDep entity and return it or a
	 * copy of it to the sender. A copy of the BpJPlanRepairDep entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanRepairDep entity to update
	 * @return BpJPlanRepairDep the persisted BpJPlanRepairDep entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanRepairDep update(BpJPlanRepairDep entity) {
		LogUtil.log("updating BpJPlanRepairDep instance", Level.INFO, null);
		try {
			BpJPlanRepairDep result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJPlanRepairDep findById(Long id) {
		LogUtil.log("finding BpJPlanRepairDep instance with id: " + id,
				Level.INFO, null);
		try {
			BpJPlanRepairDep instance = entityManager.find(
					BpJPlanRepairDep.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
    /**
     * 查询审批中最大月份下最大周的数据 add by sychen 20100319
     */
	public String getRepairApproveList(String enterpriseCode) {
		String sql ="select max(a.plan_time) from BP_J_PLAN_REPAIR_DEP a\n" +
			"       where a.enterprise_code='"+enterpriseCode+"'\n" + 
			"       and a.status in(1)";


		Object maxNoObj = bll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}
	
	  /**
     * 判断审批页面时间下主表Id是否存在 add by sychen 20100320
     */
	public String getRepairApproveDeptId(String planTime,String planWeek,String enterpriseCode,String people) {
		String priorStr = planTime.substring(0, 4);
		String priorStr2 = planTime.substring(5, 7);
		String planTimeStr = priorStr + priorStr2 + planWeek;
		String sql ="select a.plan_dep_id from BP_J_PLAN_REPAIR_DEP a\n"
			+ "where a.plan_time='"+planTimeStr+"'\n"
			+ "and a.enterprise_code='"+enterpriseCode+"'\n"
			+ "and a.status in(1)  and a.edit_by='"+people+"'";// modify by wpzhu 100415  增加人员过滤条件

		Object maxNoObj = bll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}

}