package power.ejb.manage.budget;

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
import javax.persistence.Query;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.employeeLeaveBean;
import power.ejb.manage.project.PrjJEndCheck;
import power.ejb.run.powernotice.RunJPowerNoticeApprove;
import power.ejb.run.powernotice.RunJPowerNoticeApproveFacadeRemote;

/**
 * Facade for entity CbmJAssignedFill.
 * 
 * @see power.ejb.manage.budget.CbmJAssignedFill
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmJAssignedFillFacade implements CbmJAssignedFillFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public CbmJAssignedFill save(CbmJAssignedFill entity) {
		LogUtil.log("saving CbmJAssignedFill instance", Level.INFO, null);
		try {
			entity.setAssignId(bll.getMaxId("CBM_J_ASSIGNED_FILL", "ASSIGN_ID"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(Long id) {
		CbmJAssignedFill entity = this.findById(id);
		if (entity != null) {
			entity.setIsUse("N");
			this.update(entity);
		}
		
	} 
	

	public CbmJAssignedFill update(CbmJAssignedFill entity) {
		LogUtil.log("updating CbmJAssignedFill instance", Level.INFO, null);
		try {
			CbmJAssignedFill result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJAssignedFill findById(Long id) {
		LogUtil.log("finding CbmJAssignedFill instance with id: " + id,
				Level.INFO, null);
		try {
			CbmJAssignedFill instance = entityManager.find(
					CbmJAssignedFill.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CbmJAssignedFill> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding CbmJAssignedFill instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from CbmJAssignedFill model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CbmJAssignedFill> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all CbmJAssignedFill instances", Level.INFO, null);
		try {
			final String queryString = "select model from CbmJAssignedFill model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	public PageObject findAssignedFillList(String workerCode,
			String enterpriseCode, String assignName,
			int... rowStartIdxAndCount) throws ParseException {
		PageObject pg = new PageObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String sql = "select a.ASSIGN_ID,\n"
			+ "       a.ASSIGN_NAME,\n"
			+ "       a.ITEM_ID,\n"			
			+ "       a.ESTIMATE_MONEY,\n"
			+ "       a.ASSIGN_FUNCTION,\n"
			+ "       a.MEMO,\n"
			+ "       GETWORKERNAME(a.APPLY_BY),\n"
			+ "       a.APPLY_BY,\n"
			+ "       to_char(a.APPLY_DATE, 'yyyy-MM-dd'),\n"
			+ "       GETDEPTNAME(a.APPLY_DEPT),\n"
			+ "       a.WORK_FLOW_NO,\n"
			+ "decode(a.WORK_FLOW_STATUS,0,'未上报',1,'已上报',2,'部门主任已审批',3,'费用主管部门已审批',4,'主管厂领导已审批',5,'审批结束',6,'已退回','') WORK_FLOW_STATUS,\n" 
			+ "       (select b.item_name from CBM_C_ITEM b where b.item_id=a.item_id),\n"
			+ "       a.APPLY_DEPT\n"
			+ "  from CBM_J_ASSIGNED_FILL a\n";
		
		String sqlCount = "select count(1)\n"
						  + "  from CBM_J_ASSIGNED_FILL a\n";
		
		String strWhere=" a.ENTERPRISE_CODE='"+enterpriseCode +"' and a.IS_USE='Y'and a.WORK_FLOW_STATUS in ('0','6')";
//		if(workerCode != null && !workerCode.equals("") && !workerCode.equals("999999"))
//			strWhere += " and a.APPLY_BY='" + workerCode + "' \n";
		if (assignName != null && assignName.length() > 0) {
			strWhere += " and a.ASSIGN_NAME like '%" + assignName + "%'";
		}
		//add by ypan 20100920
		if (workerCode != null && !workerCode.equals("")
				&& !workerCode.equals("999999"))
			strWhere += " and a.apply_by='" + workerCode + "' \n";
		//sqlCount += " and a.entry_by = '"+workerCode+"'";
//		if(workerCode != null && !workerCode.equals("") && !workerCode.equals("999999"))
//			strWhere += " and a.APPLY_BY='" + workerCode + "' \n";
		if (strWhere != "") {
			   sql = sql + " where  " + strWhere;
			   sqlCount = sqlCount + " where  " + strWhere;
		    }
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		/*System.out.println("list======================"+list);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
		while(it.hasNext()){
			CbmJAssignedFill model = new CbmJAssignedFill();
			Object []data = (Object[])it.next();
			if(data[0] != null){
				model.setAssignId(Long.parseLong(data[0].toString()));
			}else {
				model.setAssignId(bll.getMaxId("CBM_J_ASSIGNED_FILL", "ASSIGN_ID"));
			}
			if(data[1] != null)
				if(data[11]!=null){
					model.setAssignName(data[1].toString()+","+data[11].toString());
				}else{
					model.setAssignName(data[1].toString());
				}
				
			if(data[2] != null)
				model.setItemId(Long.parseLong(data[2].toString()));
			if(data[3] != null)
				model.setEstimateMoney(Double.parseDouble(data[3].toString()));
			if(data[4] != null)
				model.setAssignFunction(data[4].toString());
			if(data[5] != null)
				model.setMemo(data[5].toString());
			if(data[6] != null)
				model.setApplyBy(data[6].toString());
			if(data[7] != null)
				model.setApplyDate(format.parse(data[7].toString()));
			if(data[8] != null)
				model.setApplyDept(data[8].toString());
			if(data[9]!=null)
				model.setWorkFlowNo(Long.parseLong(data[9].toString()));
			if(data[10] != null)
				model.setWorkFlowStatus(data[10].toString());			
			arrlist.add(model);
		 }
	  }*/
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}
	
	public PageObject findAssignedFillListall(String workerCode,
			String enterpriseCode, String assignName, String applyBy,
			int... rowStartIdxAndCount) throws ParseException {
		PageObject pg = new PageObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String sql = "select a.ASSIGN_ID,\n"
			+ "       a.ASSIGN_NAME,\n"
			+ "       a.ITEM_ID,\n"
			+ "       a.ESTIMATE_MONEY,\n"
			+ "       a.ASSIGN_FUNCTION,\n"
			+ "       a.MEMO,\n"
			+ "       GETWORKERNAME(a.APPLY_BY),\n"
			+ "       a.APPLY_BY,\n"
			+ "       to_char(a.APPLY_DATE, 'yyyy-MM-dd'),\n"
			+ "       GETDEPTNAME(a.APPLY_DEPT),\n"
			+ "       a.WORK_FLOW_NO,\n"
			+ "decode(a.WORK_FLOW_STATUS,0,'未上报',1,'已上报',2,'部门主任已审批',3,'费用主管部门已审批',4,'主管厂领导已审批',5,'审批结束',6,'已退回','') WORK_FLOW_STATUS,\n"  
			+ "       (select b.item_name from CBM_C_ITEM b where b.item_id=a.item_id),\n"
			+ "       a.APPLY_DEPT\n"
			+ "  from CBM_J_ASSIGNED_FILL a\n";
		
		String sqlCount = "select count(1)\n"
						  + "  from CBM_J_ASSIGNED_FILL a\n";
		
		String strWhere=" a.ENTERPRISE_CODE='"+enterpriseCode +"' and a.IS_USE='Y'and a.WORK_FLOW_STATUS in ('0','1','2','3','4','5','6')";
//		if(workerCode != null && !workerCode.equals("") && !workerCode.equals("999999"))
//			strWhere += " and a.APPLY_BY='" + workerCode + "' \n";
		if (assignName != null && assignName.length() > 0) {
			strWhere += " and a.ASSIGN_NAME like '%" + assignName + "%'";
		}
		if (applyBy != null && applyBy.length() > 0) {
			strWhere += " and GETWORKERNAME(a.APPLY_BY) like '%" + applyBy + "%'";
		}
		if (strWhere != "") {
			   sql = sql + " where  " + strWhere;
			   sqlCount = sqlCount + " where  " + strWhere;
		    }
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
	/*	System.out.println("list======================"+list);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
		while(it.hasNext()){
			CbmJAssignedFill model = new CbmJAssignedFill();
			Object []data = (Object[])it.next();
			if(data[0] != null){
				model.setAssignId(Long.parseLong(data[0].toString()));
			}else {
				model.setAssignId(bll.getMaxId("CBM_J_ASSIGNED_FILL", "ASSIGN_ID"));
			}
			if(data[1] != null)
				if(data[11]!=null){
					model.setAssignName(data[1].toString()+","+data[11].toString());
				}else{
					model.setAssignName(data[1].toString());
				}
			if(data[2] != null)
				model.setItemId(Long.parseLong(data[2].toString()));
			if(data[3] != null)
				model.setEstimateMoney(Double.parseDouble(data[3].toString()));
			if(data[4] != null)
				model.setAssignFunction(data[4].toString());
			if(data[5] != null)
				model.setMemo(data[5].toString());
			if(data[6] != null)
				model.setApplyBy(data[6].toString());
			if(data[7] != null)
				model.setApplyDate(format.parse(data[7].toString()));
			if(data[8] != null)
				model.setApplyDept(data[8].toString());
			if(data[9]!=null)
				model.setWorkFlowNo(Long.parseLong(data[9].toString()));
			if(data[10] != null)
				model.setWorkFlowStatus(data[10].toString());

			arrlist.add(model);
		 }
	  }*/
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}
	
	

	public void deleteMulti(String ids) {
		String sql = "update CBM_J_ASSIGNED_FILL a\n"
			+ "   set a.is_use = 'N'\n" + " where a.ASSIGN_ID in (" + ids
			+ ")\n" + "   and a.IS_USE = 'Y'";
		bll.exeNativeSQL(sql);	
		
	}

	/**
	 * 上报
	 * @param workticketNo
	 * @param workerCode
	 * @param actionId
	 */
	public void reportTo(String assignId, String flowCode, String workerCode,
			long actionId) {
		WorkflowService service = new WorkflowServiceImpl();
		long entryId = service.doInitialize(flowCode,workerCode,assignId);
		System.out.println("workflowno================="+entryId);
		service.doAction(entryId, workerCode, actionId, "上报", null); 
		String sql=" update CBM_J_ASSIGNED_FILL  set WORK_FLOW_STATUS=1,work_flow_no="+entryId+"  where ASSIGN_ID='"+assignId+"'";
		bll.exeNativeSQL(sql);	
	}
	
	public PageObject findAssignedFillApproveList(String deptId,String enterpriseCode,String assignName,String entryIds,final int... rowStartIdxAndCount) throws ParseException
	{
		if(entryIds==null)
		{
			entryIds="''";
		}
		PageObject pg = new PageObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String sql = "select a.ASSIGN_ID,\n"
			+ "       a.ASSIGN_NAME,\n"
			+ "       a.ITEM_ID,\n"
			+ "       a.ESTIMATE_MONEY,\n"
			+ "       a.ASSIGN_FUNCTION,\n"
			+ "       a.MEMO,\n"
			+ "       GETWORKERNAME(a.APPLY_BY),\n"
			+ "       a.APPLY_BY,\n"
			+ "       to_char(a.APPLY_DATE, 'yyyy-MM-dd'),\n"
			+ "       GETDEPTNAME(a.APPLY_DEPT),\n"
			+ "       a.WORK_FLOW_NO,\n"
			+ "decode(a.WORK_FLOW_STATUS,0,'未上报',1,'已上报',2,'部门主任已审批',3,'费用主管部门已审批',4,'主管厂领导已审批',5,'审批结束',6,'已退回','') WORK_FLOW_STATUS,\n"  
			+ "       (select b.item_name from CBM_C_ITEM b where b.item_id=a.item_id),\n"
			+ "       a.APPLY_DEPT\n"
			+ "  from CBM_J_ASSIGNED_FILL a\n";
		
		String sqlCount = "select count(1)\n"
			  + "  from CBM_J_ASSIGNED_FILL a\n";
		
		String strWhere=" a.ENTERPRISE_CODE='"+enterpriseCode +"' and a.IS_USE='Y' and a.WORK_FLOW_STATUS in ('1','2','3','4','5')";
		

		if (assignName != null && assignName.length() > 0) {
			strWhere += " and a.ASSIGN_NAME like '%" + assignName + "%'";
		}
		if(entryIds!=null)
		{
			strWhere+="  and a.work_flow_no in ("+entryIds+")";
		}
		//---------部门主任审批加一级部门过滤 add by fyyang 20100921-------------------------
		strWhere+=
			"and GETFirstLevelBYID("+deptId+") =\n" +
			"      decode(a.work_flow_status,\n" + 
			"             '1',\n" + 
			"             GETFirstLevelBYID((select aa.dept_id\n" + 
			"                                 from hr_c_dept aa\n" + 
			"                                where aa.dept_code = a.apply_dept\n" + 
			"                                  and rownum = 1)),\n" + 
			"             GETFirstLevelBYID("+deptId+"))";

		//---------费用主管部门审核 add by qxjiao 20101015
		strWhere =strWhere +"and   GETFirstLevelBYID("+deptId+")= decode(a.work_flow_status,2,  (select GETFirstLevelBYID(b.center_id) from cbm_c_item  b"
			     +" where b.item_id=a.item_id and rownum=1),GETFirstLevelBYID("+deptId+")  )";

		
		if (strWhere != "") {
			   sql = sql + " where  " + strWhere;
			   sqlCount = sqlCount + " where  " + strWhere;
		    }
		List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		/*System.out.println("list======================"+list);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		if (list != null && list.size() > 0) {
		while(it.hasNext()){
			CbmJAssignedFill model = new CbmJAssignedFill();
			Object []data = (Object[])it.next();
			if(data[0] != null)
				model.setAssignId(Long.parseLong(data[0].toString()));
			if(data[1] != null)
				if(data[11]!=null){
					model.setAssignName(data[1].toString()+","+data[11].toString());
				}else{
					model.setAssignName(data[1].toString());
				}
			if(data[2] != null)
				model.setItemId(Long.parseLong(data[2].toString()));
			if(data[3] != null)
				model.setEstimateMoney(Double.parseDouble(data[3].toString()));
			if(data[4] != null)
				model.setAssignFunction(data[4].toString());
			if(data[5] != null)
				model.setMemo(data[5].toString());
			if(data[6] != null)
				model.setApplyBy(data[6].toString());
			if(data[7] != null)
				model.setApplyDate(format.parse(data[7].toString()));
			if(data[8] != null)
				model.setApplyDept(data[8].toString());
			if(data[9]!=null)
				model.setWorkFlowNo(Long.parseLong(data[9].toString()));
			if(data[10] != null)
				model.setWorkFlowStatus(data[10].toString());

			arrlist.add(model);
			}
		}*/
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}
	
	//审批签字
	public void approveSign(String assignId,String approveText,String workflowNo,String workerCode,
			Long actionId,String responseDate,String nextRoles,String eventIdentify)
	{
		WorkflowService service = new WorkflowServiceImpl();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String status="";
		if(eventIdentify.equals("TH"))
		{
			status="6";
		}
		else
		{
//			if(actionId == 26)
//			{
//				status="2";
//			}
			if(actionId == 67)
			{
				status="2";
			}
			if(actionId == 78)
			{
				status="3";
			}
			if(actionId == 83)
			{
				status="4";
			}
		}
		String sql=
			"update CBM_J_ASSIGNED_FILL t\n" +
			"set t.WORK_FLOW_STATUS="+status+"\n" + 
			"where t.ASSIGN_ID='"+assignId+"'";
         bll.exeNativeSQL(sql);
         if(actionId==78)
         {
        	 service.doAction(Long.parseLong(workflowNo), workerCode, actionId, approveText, null, "",nextRoles);
         }else{
		this.changeWfInfo(Long.parseLong(workflowNo), workerCode, actionId, approveText,nextRoles);	
         }
	}
	
	/**
	 * 执行
	 * @param entryId 实例编号
	 * @param workerCode 审批人
	 * @param actionId   动作
	 * @param approveText 意见
	 */
	private void changeWfInfo(Long entryId,String workerCode,Long actionId,String approveText,String nextRoles)
	{
		WorkflowService service = new WorkflowServiceImpl();
		service.doAction(entryId, workerCode, actionId, approveText, null,nextRoles,""); 
	}
	
	@SuppressWarnings("unchecked")
	public List<TreeNode> getItemIdTree(String itemCode,
			Long deptId,String budgetTime,String itemType) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		List<TreeNode> res = null;
		;
		Integer size;
		if (itemCode != null && itemCode.equals("000")) {
			itemCode=" ";
			size=3;		
		} else {
			 size = itemCode.length() + 3;
		}
		String sql=
			"select t8.item_id,\n" +
			"       t7.zbbmtx_code,\n" + 
			"       t7.zbbmtx_name,\n" + 
			"  e.data_type\n"+
			"  from cbm_c_itemtx t7, CBM_C_ITEM t8\n" + 

			",(\n" +
			" select a.item_id, a.data_type\n" + 
			"         from CBM_C_CENTER_ITEM a, CBM_J_BUDGET_MAKE b, CBM_J_BUDGET_ITEM c\n" + 
			"        where b.budget_make_id = c.budget_make_id\n" + 
			"          and c.center_item_id = a.center_item_id\n" + 
			"          and b.budget_time = '"+budgetTime+"'\n" + 
			"          and c.is_use = 'Y'\n" + 
			"          and b.is_use = 'Y'\n" + 
			"          and b.enterprise_code = 'hfdc'\n" + 
			"          and c.enterprise_code = 'hfdc'\n" + 
			"          and (GETFirstLevelBYID("+deptId+") in (select GETFirstLevelBYID(d.center_id)\n" + 
			"                         from CBM_C_CENTER_USE d\n" + 
			"                        where d.item_id = a.item_id\n" + 
			"                          and d.is_use = 'Y'\n" + 
			"                          and d.enterprise_code = 'hfdc') or\n" + 
			"              (select count(1)\n" + 
			"                  from CBM_C_CENTER_USE d\n" + 
			"                 where d.item_id = a.item_id\n" + 
			"                   and d.is_use = 'Y'\n" + 
			"                   and d.enterprise_code = 'hfdc') = 0)\n" + 
			" ) e \n"+
			" where t7.zbbmtx_code like '"+itemCode.trim()+"%'\n" + 
			"   and length(t7.zbbmtx_code) = "+size+"\n" + 
			"   and t7.item_id = t8.item_id(+)\n" + 
			"   and t7.is_use = 'Y'\n" + 
			"and t8.item_id=e.item_id(+) \n"+
			"   and t7.zbbmtx_code in\n" + 
			"       (\n" + 
			"\n" + 
			"        select substr(t6.zbbmtx_code, 0, "+size+")\n" + 
			"          from CBM_C_ITEM        t1,\n" + 
			"                CBM_C_CENTER_ITEM t2,\n" + 
			"                CBM_J_BUDGET_ITEM t3,\n" + 
			"                CBM_J_BUDGET_MAKE t4,\n" + 
			"                cbm_c_itemtx      t6\n" + 
			"         where t1.item_id = t2.item_id\n" + 
			"           and t6.item_id = t1.item_id\n" + 
			"           and t2.center_item_id = t3.center_item_id\n" + 
			"           and t3.budget_make_id = t4.budget_make_id\n" + 
			"           and t4.budget_time = '"+budgetTime+"'\n" + 
			"           and t6.zbbmtx_code like '"+itemCode.trim()+"%'\n" + 
			"           and t6.zbbmtx_code <> '"+itemCode+"'\n" + 
			"           and length(t6.zbbmtx_code) >= '"+size+"'\n" + 
			"           and t6.is_use = 'Y'\n" + 
			"           and (GETFirstLevelBYID("+deptId+") in (select GETFirstLevelBYID(t5.center_id)\n" + 
			"                          from CBM_C_CENTER_USE t5\n" + 
			"                         where t5.item_id = t1.item_id\n" + 
			"                           and t5.is_use = 'Y'\n" + 
			"                           and t5.enterprise_code = 'hfdc') or\n" + 
			"               (select count(1)\n" + 
			"                   from CBM_C_CENTER_USE t5\n" + 
			"                  where t5.item_id = t1.item_id\n" + 
			"                    and t5.is_use = 'Y'\n" + 
			"                    and t5.enterprise_code = 'hfdc') = 0)\n" ;
			
			

			if(itemType!=null&&!itemType.equals("")){
				   sql +="and t2.data_type ='" + itemType+"'\n";
			   }
			sql += " )  order by t7.zbbmtx_code asc ";			
	List<Object[]> list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			res = new ArrayList<TreeNode>();
			Integer count = 0;
			for (Object[] o : list) {
				TreeNode n = new TreeNode();
				if(o[0]!=null)
				{
				n.setId(o[0].toString());
				}
				if(o[3]!=null)
				{
					n.setDescription(o[3].toString());
				}
				if (o[1] != null) {
					n.setCode(o[1].toString());
					Integer csize = o[1].toString().length();
					String sqlcount = 
						"select count(1)\n" + 
						"  from CBM_C_ITEM        t1,\n" + 
						"        CBM_C_CENTER_ITEM t2,\n" + 
						"        CBM_J_BUDGET_ITEM t3,\n" + 
						"        CBM_J_BUDGET_MAKE t4,\n" + 
						"        cbm_c_itemtx      t6\n" + 
						" where t1.item_id = t2.item_id\n" + 
						"   and t6.item_id = t1.item_id\n" + 
						"   and t2.center_item_id = t3.center_item_id\n" + 
						"   and t3.budget_make_id = t4.budget_make_id\n" + 
						"   and t4.budget_time = '"+budgetTime+"'\n" + 
						"   and t6.zbbmtx_code like '"+ o[1].toString()+"%'\n" + 
						"   and t6.zbbmtx_code <> '"+ o[1].toString()+"'\n" + 
						"   and length(t6.zbbmtx_code) >= '"+csize+"'\n" + 
						"   and t6.is_use = 'Y'\n" + 
						"   and (GETFirstLevelBYID("+deptId+") in (select GETFirstLevelBYID(t5.center_id)\n" + 
						"                  from CBM_C_CENTER_USE t5\n" + 
						"                 where t5.item_id = t1.item_id\n" + 
						"                   and t5.is_use = 'Y'\n" + 
						"                   and t5.enterprise_code = 'hfdc') or\n" + 
						"       (select count(1)\n" + 
						"           from CBM_C_CENTER_USE t5\n" + 
						"          where t5.item_id = t1.item_id\n" + 
						"            and t5.is_use = 'Y'\n" + 
						"            and t5.enterprise_code = 'hfdc') = 0)";
					count = Integer
							.parseInt(bll.getSingal(sqlcount).toString());
					if (count > 0) {
						n.setLeaf(false);
					} else {
						n.setLeaf(true);
					}
					
				}
				if (o[2] != null)
					n.setText(o[2].toString());
				res.add(n);
			}
			return res;
		} else {
			return null;
		}
	}

	public Long getCurrentItemFinanceLeft(Long itemId,String budgetTime) {
		Long financeAll;
		Long financeUsed;
		String sqlAll=
			"select distinct d.advice_budget\n" +
			"       from CBM_C_ITEM b ,\n" + 
			"            CBM_C_CENTER_ITEM c ,\n" + 
			"            CBM_J_BUDGET_ITEM d ,\n" + 
			"           CBM_J_BUDGET_MAKE e \n" + 
			"       where b.item_id ="+  itemId +"\n" + 
			"         and b.item_id = c.item_id\n" + 
			"         and c.center_item_id =d.center_item_id\n" + 
			"         and d.budget_make_id = e.budget_make_id\n" + 
			"         and e.budget_time='"+ budgetTime +"'\n" + 
			"         and b.is_use ='Y'\n" + 
			"         and c.is_use ='Y'\n" + 
			"         and d.is_use ='Y'\n" + 
			"         and e.is_use ='Y'";
		String sqlUsed =
			"SELECT nvl((SELECT SUM(nvl(a.report_money_lower, 0))\n" +
			"             FROM CBM_J_COST_REPORT a\n" + 
			"            WHERE a.item_id = "+  itemId +"\n" + 
			"              AND a.work_flow_status NOT IN ('0', '10')\n" + 
			"              AND a.work_flow_status IS NOT NULL\n" + 
			"              AND to_char(a.report_date, 'yyyy') = '"+ budgetTime +"'), 0) +\n" + 
			"       nvl((SELECT SUM(nvl(b.estimate_money, 0))\n" + 
			"             FROM CBM_J_ASSIGNED_FILL b\n" + 
			"            WHERE b.item_id = "+  itemId +"\n" + 
			"              AND b.work_flow_status NOT IN ('0', '6')\n" + 
			"              AND b.work_flow_status IS NOT NULL\n" + 
			"              AND to_char(b.apply_date, 'yyyy') = '"+ budgetTime +"'), 0)\n" + 
			"  FROM dual";
		if(bll.getSingal(sqlAll)!=null){
			financeAll = Long.parseLong(bll.getSingal(sqlAll).toString());
		} else {
			financeAll =0l;
		}
		if(bll.getSingal(sqlUsed)!=null){
			financeUsed = Long.parseLong(bll.getSingal(sqlUsed).toString());
		} else {
			financeUsed =0l;
		}
//			Long financeAll = Long.parseLong(bll.getSingal(sqlAll).toString());
//		Long financeUsed = Long.parseLong(bll.getSingal(sqlUsed).toString());
		Long financeLeft = financeAll -financeUsed;
		
		return financeLeft;
	}
}