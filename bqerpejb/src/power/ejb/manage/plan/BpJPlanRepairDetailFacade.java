package power.ejb.manage.plan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.plan.form.repairDetailForm;

/**
 * Facade for entity BpJPlanRepairDetail.
 * 
 * @see power.ejb.manage.plan.BpJPlanRepairDetail
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJPlanRepairDetailFacade implements
		BpJPlanRepairDetailFacadeRemote {
	// property constants
	public static final String PLAN_DEP_ID = "planDepId";
	public static final String CONTENT = "content";
	public static final String CHARGE_DEP = "chargeDep";
	public static final String ASSORT_DEP = "assortDep";
	public static final String DAYS = "days";
	public static final String MEMO = "memo";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	protected BpJPlanRepairDepFacadeRemote dept;
	WorkflowService service;
	public  BpJPlanRepairDetailFacade(){
		service = new WorkflowServiceImpl();
		
	dept = (BpJPlanRepairDepFacadeRemote) Ejb3Factory.getInstance()
	.getFacadeRemote("BpJPlanRepairDepFacade");}
	
 public PageObject getAllDetail(String deptID, int... rowStartIdxAndCount){
	 String sql=

		 "select t.plan_detail_id," +
		 "t.plan_dep_id," +
		 "t.content," +
		 "t.charge_dep," +
		 "getdeptname(t.charge_dep)," +
		 "t.assort_dep," +
		 "getdeptname(t.assort_dep),\n" +
		 "to_char(t.begin_time,'yyyy-MM-dd')," +
		 "to_char(t.end_time,'yyyy-MM-dd')," +
		 "t.days,t.memo," +
		 "t.is_use," +
		 "t.enterprise_code " +
		 " from BP_J_PLAN_REPAIR_DETAIL t\n" + 
		 "where t.plan_dep_id='"+deptID+"'\n" + 
		 "order by t.plan_detail_id";
	 String count="select count(1) from ("+sql+") ";
	 List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		
		PageObject result= new  PageObject() ;
		result.setList(list);
		result.setTotalCount(Long.parseLong(bll.getSingal(count).toString()));
		return result;

	
	 
 }
  
	public PageObject findRepair( String approve,String entryIds,String planTime, String people, String enterprise ,String status) throws ParseException{
		PageObject result=new PageObject();
		String sql=
			"select  t.plan_detail_id," +
			" t.plan_dep_id, " +
			"t.content," +
			"t.charge_dep," +
			"getdeptname(t.charge_dep)," +
			"t.assort_dep," +
			"getdeptname(t.assort_dep)," + //add by sychen 20100320
			"to_char(t.begin_time,'yyyy-MM-dd')," +
			"to_char(t.end_time,'yyyy-MM-dd')," +
			"t.days," +
			"t.memo," +
			"t.is_use, " +
			"t.enterprise_code,\n" +
			"d.plan_dep_id," +
			"d.plan_time," +
			"d.edit_dep," +
			"getdeptname(d.edit_dep)," +
			"d.work_flow_no," +
			"d.status," +
			"d.edit_by," +
			"getworkername(d.edit_by)," +
			"to_char(d.edit_date,'yyyy-MM-dd')," +
			"to_char(d.week_start_time,'yyyy-MM-dd' ) ," +
			"to_char(d.week_end_time,'yyyy-MM-dd')\n" + 
			"from BP_J_PLAN_REPAIR_DEP d,BP_J_PLAN_REPAIR_DETAIL  t\n" + 
			"where t.plan_dep_id=d.plan_dep_id\n" + 
			/*"and to_char(d.plan_time,'yyyy-MM')='"+planTime+"'\n" + */
			"and t.is_use='Y'" 
			/*"and d.edit_dep='"+dept+"'\n"*/ ;
		
		if (planTime!=null&&!("").equals(planTime))
			sql += "and  d.plan_time='"+planTime+"'";
		else sql+="and d.plan_time  like '%"+planTime+"%'";
		/*if (people!=null&&!("").equals(people))
			sql += "and  d.edit_by='"+people+"'\n" ;*/
		if (status!=null&&!("").equals(status))
			sql += "and d.status in ("+status+")";	
		if (approve != null && approve.equals("approve")) {
			sql +=
			// " and a.workflow_status='1'" +
			" and  d.work_flow_no in (" + entryIds + ")";
		}
		//add by wpzhu 增加人过滤条件100415
		if(people !=null && !people.equals("")&&(approve==null||approve.equals(""))){
			sql+="and  d.edit_by='"+people+"' ";
			
		}
//		System.out.println("the sql"+sql);
		String count="select count(1) from ("+sql+")";
		List list=bll.queryByNativeSQL(sql);
		List<repairDetailForm> arrlist = new ArrayList<repairDetailForm>();
		Iterator it = list.iterator();
		while (it.hasNext()) {

			Object[] data = (Object[]) it.next();

			repairDetailForm model = new repairDetailForm();

			if (data[0] != null) {
				model.setPlanDetailId(Long.parseLong(data[0].toString()));
			}
			if (data[1] != null) {
				model.setPlanDepId(Long.parseLong(data[1].toString()));
			}
			if (data[2] != null) {
				model.setContent((data[2].toString()));
			}
			if (data[3] != null) {
				model.setChargeDep((data[3].toString()));
			}
			if (data[4] != null) {
				model.setChargeDepName((data[4].toString()));
			}
			if (data[5] != null) {
				model.setAssortDep((data[5].toString()));
			}
			if (data[6] != null) {
				model.setAssortDepName((data[6].toString()));
			}
			if (data[7] != null) {
				DateFormat  sf = new SimpleDateFormat("yyyy-MM-dd");
				model.setBeginTime(sf.parse(data[7].toString()));
				model.getBeginTime();
			}
			if (data[8] != null) {
				DateFormat  sf = new SimpleDateFormat("yyyy-MM-dd");
				model.setEndTime((sf.parse(data[8].toString())));
			}
			if (data[9] != null) {
				model.setDays(Long.parseLong(data[9].toString()));
			}
			if (data[10] != null) {
				model.setMemo((data[10].toString()));
			}
			if (data[11] != null) {
				model.setIsUse((data[11].toString()));
			}
			if (data[12] != null) {
				model.setEnterpriseCode((data[12].toString()));
			}
			if (data[13] != null) {
				model.setDepId(Long.parseLong(data[13].toString()));
			}
			if (data[14] != null) {
			/*	DateFormat  sf = new SimpleDateFormat("yyyy-MM-dd");*/
				model.setPlanTime(data[14].toString());
			}
			if (data[15] != null) {
				model.setEditDep(data[15].toString());
			}
			if (data[16] != null) {
				model.setEditDepName((data[16].toString()));
			}
			if (data[17] != null) {
				model.setWorkFlowNo(Long.parseLong(data[17].toString()));
			}
			if (data[18] != null) {
				model.setStatus(Long.parseLong(data[18].toString()));
			}
			if (data[19] != null) {
				model.setEditBy(data[19].toString());
			}
			if (data[20] != null) {
				model.setEditByName((data[20].toString()));
			}
			if (data[21] != null) {
				DateFormat  sf = new SimpleDateFormat("yyyy-MM-dd");
				model.setEditDate(sf.parse((data[21].toString())));
			}
			if (data[22] != null) {
				model.setWeekStartTime((data[22].toString()));
			}
			if (data[23] != null) {
				model.setWeekEndTime((data[23].toString()));
			}
			
			



			arrlist.add(model);
		}
		Long count1 = Long.parseLong(bll.getSingal(count).toString());
		result.setList(arrlist);
		result.setTotalCount(count1);

		return result;
	} 
		
		
		
	public Long  findDept(String time,String workCode )
	{
		String sql=
			"select count(1)" +
			" from  BP_J_PLAN_REPAIR_DEP d\n" +
			"where d.plan_time='"+time.trim()+"'\n" + 
			"and d.edit_by='"+workCode+"'  "; 
	
		 
		return  Long.parseLong(bll.getSingal(sql).toString()); 
		
	}
	public Long  findDeptID(String time,String workCode )
	{
		String sql=
			"select  d.plan_dep_id" +
			" from  BP_J_PLAN_REPAIR_DEP d\n" +
			"where d.plan_time='"+time.trim()+"'\n" + 
			"and d.edit_by='"+workCode+"'"; 
	//	System.out.println("the dept id is"+sql);
		 
		return  Long.parseLong(bll.getSingal(sql).toString()); 
		
	}
	public  PageObject getRepairDept(String plantime,String dept){
		PageObject result=new PageObject();
		String sql=
			"select d.plan_dep_id," +
			"d.week_start_time," +
			"d.week_end_time " +
			"from BP_J_PLAN_REPAIR_DEP d\n" +
			"where d.plan_time='"+plantime+"'\n" + 
			"and d.edit_dep='"+dept+"'";
		String count="select count(1) from ("+sql+")";
		List list=bll.queryByNativeSQL(sql);
		List<repairDetailForm> arrlist = new ArrayList<repairDetailForm>();
		Iterator it = list.iterator();
		while (it.hasNext()) {

			Object[] data = (Object[]) it.next();

			repairDetailForm model = new repairDetailForm();

			if (data[0] != null) {
				model.setDepId(Long.parseLong(data[0].toString()));
			}
			if (data[1] != null) {
				model.setWeekStartTime(data[1].toString());
				}
			if (data[2] != null) {
				model.setWeekEndTime(data[2].toString());
				}
			arrlist.add(model);
			}
		Long count1 = Long.parseLong(bll.getSingal(count).toString());
		result.setList(arrlist);
		result.setTotalCount(count1);

		return result;

	}
	public BpJPlanRepairDetail addRepairDetail(BpJPlanRepairDetail entity,String  planTime,
			String  planDept,Long  	planDepId)
	{
		entity.setPlanDepId(planDepId);
		Long planDetailId = bll.getMaxId("BP_J_PLAN_REPAIR_DETAIL ",
				"plan_detail_id");
		entity.setPlanDetailId(planDetailId );
		entity.setIsUse("Y");
		entityManager.persist(entity);
		entityManager.flush();
		return entity;
	} 
	public BpJPlanRepairDetail  updateRepairDetail(BpJPlanRepairDetail entity){
		try { 
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
		return entity;
		
		
	}
	public  void delRepairDetail(String ids ){
		String sql=
			"update BP_J_PLAN_REPAIR_DETAIL  d\n" +
			"set  d.is_use='N'\n" + 
			"where d.plan_detail_id in ("+ids+")";
/*System.out.println("the delete is "+sql);*/
		bll.exeNativeSQL(sql);
		
	}
	public void repairReport(Long mainId, Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType) {
		System.out.println("the mainid is "+mainId);
		BpJPlanRepairDep model = dept.findById(mainId);
		Long entryId;
		if (model.getWorkFlowNo() == null) {
			entryId = service.doInitialize(workflowType, workerCode, mainId
					.toString());
			model.setWorkFlowNo(entryId);
		} else {
			entryId = model.getWorkFlowNo();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,nextRoles, "");
		model.setStatus(1l);
		dept.update(model);

	}
	public Long  autoReport(Long mainID,String workerCode,String workflowType)
	{
		    Long entryId;
			entryId = service.doInitialize(workflowType, workerCode, mainID
					.toString());
		
		service.doAction(entryId, workerCode, 24l,"SB", null,"", workerCode );
	
		return entryId;
		
	}
	public void repairApprove(Long mainId, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles) {
		BpJPlanRepairDep model = dept.findById(mainId);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		if (actionId == 42l) {
			model.setStatus(3l);
		} else if (actionId == 43l) {
			model.setStatus(2l);
		}
		dept.update(model);
	}
	


	
	public  void saveRepairList(String flag,List<BpJPlanRepairDetail> addList,List<BpJPlanRepairDetail> updateList,
			String  planTime,String  planDept,String weekStart,String weekEnd,String workCode, String enterpriseCode) throws ParseException{
		Long planDepId;  
		if (this.findDept(planTime, workCode).equals(0L)) {
			// 增加部门主表
			BpJPlanRepairDep model = new BpJPlanRepairDep();
			planDepId = bll.getMaxId("BP_J_PLAN_REPAIR_DEP", "plan_dep_id");
			model.setPlanDepId(planDepId);
			model.setStatus(0l);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if(weekStart!=null&&!weekStart.equals(""))
			{
			model.setWeekStartTime(df.parse(weekStart));
			}
			if(weekEnd!=null&&!weekEnd.equals(""))
			{
			model.setWeekEndTime(df.parse(weekEnd));
			}
			model.setEnterpriseCode(enterpriseCode);
			model.setEditDep(planDept);
			model.setEditDate(new Date());
			model.setEditBy(workCode);
		if(flag!=null&&flag.equals("approve"))
			
		{
		Long 	workNO=this.autoReport(planDepId, workCode, "bqRepair");
		     model.setStatus(1l);
		     model.setWorkFlowNo(workNO);
		}
		  
			model.setPlanTime(planTime); 
			entityManager.persist(model);
		} else { 
			planDepId = this.findDeptID(planTime, workCode);
			BpJPlanRepairDep model = dept.findById(planDepId);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if(weekStart!=null&&!weekStart.equals(""))
			{
			model.setWeekStartTime(df.parse(weekStart));
			}
			if(weekEnd!=null&&!weekEnd.equals(""))
			{
			model.setWeekEndTime(df.parse(weekEnd));
			}
			entityManager.merge(model);
		}

		if (addList != null &&addList.size() > 0) {

			for (BpJPlanRepairDetail entity : addList) {
				this.addRepairDetail(entity, planTime, planDept,
						planDepId);
			}
		}
		if (updateList!=null && updateList.size() > 0) {
			for (BpJPlanRepairDetail entity : updateList) {
				this.updateRepairDetail(entity);
			}
		}
		
	}
		
	


	/**
	 * Perform an initial save of a previously unsaved BpJPlanRepairDetail
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            BpJPlanRepairDetail entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpJPlanRepairDetail entity) {
		LogUtil.log("saving BpJPlanRepairDetail instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent BpJPlanRepairDetail entity.
	 * 
	 * @param entity
	 *            BpJPlanRepairDetail entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanRepairDetail entity) {
		LogUtil.log("deleting BpJPlanRepairDetail instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpJPlanRepairDetail.class,
					entity.getPlanDetailId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpJPlanRepairDetail entity and return it or a
	 * copy of it to the sender. A copy of the BpJPlanRepairDetail entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanRepairDetail entity to update
	 * @return BpJPlanRepairDetail the persisted BpJPlanRepairDetail entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanRepairDetail update(BpJPlanRepairDetail entity) {
		LogUtil.log("updating BpJPlanRepairDetail instance", Level.INFO, null);
		try {
			BpJPlanRepairDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJPlanRepairDetail findById(Long id) {
		LogUtil.log("finding BpJPlanRepairDetail instance with id: " + id,
				Level.INFO, null);
		try {
			BpJPlanRepairDetail instance = entityManager.find(
					BpJPlanRepairDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	


}