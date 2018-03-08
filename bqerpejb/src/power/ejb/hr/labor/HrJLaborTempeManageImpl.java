package power.ejb.hr.labor;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrJWorkresume;
import power.ejb.hr.LogUtil;

@Stateless
public class HrJLaborTempeManageImpl implements HrJLaborTempeManage {
	@PersistenceContext
	private EntityManager entityManager;
	WorkflowService service;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public HrJLaborTempeManageImpl() {
		bll = (NativeSqlHelperRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("NativeSqlHelper");
		service = new WorkflowServiceImpl();
	}
	public String  getTempeApproveInfo(String enterpriseCode)
	{
		String sql ="select max(a.tempe_month) from HR_J_LABOR_TEMPE a\n" +
		"       where a.enterprise_code='"+enterpriseCode+"'\n" + 
		"       and a.tempe_state in(1)";

	Object Obj = bll.getSingal(sql);
	String maxMonth = (Obj == null) ? "" : Obj.toString();
	return maxMonth;
		
		
	}

	public HrJLaborTempeDetail findByDetailId (Long id) {
		LogUtil.log("finding HrJLaborTempeDetail instance with id: " + id,
				Level.INFO, null);
		try {
			HrJLaborTempeDetail instance = entityManager.find(
					HrJLaborTempeDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public HrJLaborTempe findByMainId(Long id) {
		LogUtil.log("finding HrJLaborTempe instance with id: " + id,
				Level.INFO, null);
		try {
			HrJLaborTempe instance = entityManager.find(
					HrJLaborTempe.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	public String getHrJLaborTempeMainId(String tempeMonth, String workerCode,
			String enterpriseCode) {

		String sql="SELECT m.tempe_id\n" +
			"  FROM HR_J_LABOR_TEMPE m\n" + 
			" WHERE m.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   AND m.tempe_month ='" + tempeMonth+ "'\n" + 
			"   AND m.entry_by ='"+workerCode+"'";

		Object maxNoObj = bll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}
	
	public String getHrJLaborTempeStatus(String tempeMonth, String workerCode,
			String enterpriseCode) {

		String sql= "SELECT m.tempe_state\n" +
		"  FROM HR_J_LABOR_TEMPE m\n" + 
		" WHERE m.enterprise_code = '"+enterpriseCode+"'\n" + 
		"   AND m.tempe_month = '" + tempeMonth+ "'\n" + 
		"   AND m.entry_by ='"+workerCode+"'";

		Object maxNoObj = bll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}
	
	public HrJLaborTempe save(HrJLaborTempe entity,String flag,String workFlowType,String workerCode) {
		LogUtil.log("saving HrJLaborTempe instance", Level.INFO, null);
		try {

			String id = bll.getMaxId("HR_J_LABOR_TEMPE ", "tempe_id")
			
					.toString();
			entity.setTempeId(Long.parseLong(id));

			if (flag != null && flag.equals("approveAdd")) {
				
				long entryId = service.doInitialize(workFlowType, workerCode,id);	
				entity.setWorkFlowNo(entryId);
				
				service.doAction(entryId, workerCode, 24l, "SB", null,null, workerCode);
			}
			entityManager.persist(entity);

			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public HrJLaborTempe update(HrJLaborTempe entity) {
		LogUtil.log("updating HrJLaborTempe instance", Level.INFO,
				null);
		try {
			HrJLaborTempe result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void saveDetail(List<HrJLaborTempeDetail> addList) {
		if (addList != null && addList.size() > 0) {
			int i = 0;
			Long idtemp = bll.getMaxId("HR_J_LABOR_TEMPE_DETAIL", "tempe_detail_id");
			for (HrJLaborTempeDetail entity : addList) {
				Long id = idtemp + i++;

				entity.setTempeDetailId(id);
				this.saveD(entity);
			}
		}
	}

	public void deleteDetail(String ids) {
		
			String sql = "update HR_J_LABOR_TEMPE_DETAIL t\n" + "   set t.is_use = 'N'\n"
					+ " where t.tempe_detail_id in (" + ids + ")";

			bll.exeNativeSQL(sql);

	}
	
	public void updateDetail(List<HrJLaborTempeDetail> updateList) {
		if (updateList != null && updateList.size() > 0) {

			for (HrJLaborTempeDetail entity : updateList) {

				this.updateD(entity);
			}
		}
	}
	
	public void saveD(HrJLaborTempeDetail entity) {
		LogUtil.log("saving HrJLaborTempeDetail instance", Level.INFO,
				null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	

	public HrJLaborTempeDetail updateD(HrJLaborTempeDetail entity) {
		LogUtil.log("updating HrJLaborTempeDetail instance", Level.INFO,
				null);
		try {
			HrJLaborTempeDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	

	public void reportHrJLaborTempe(Long tempeMainId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String nextRolePs, String workflowType) {
		HrJLaborTempe model = findByMainId(tempeMainId);
		Long entryId;
		if (model.getWorkFlowNo() == null) {
			entryId = service.doInitialize(workflowType, workerCode, tempeMainId.toString());
			model.setWorkFlowNo(entryId);
		} else {
			entryId = model.getWorkFlowNo();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, nextRolePs);
		model.setTempeState("1");
		entityManager.merge(model);

	}
	
	
	@SuppressWarnings("unchecked")
	public PageObject getHrJLaborTempeList(String flag,String tempeMonth,String entryIds,String workerCode
			,String enterpriseCode,int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String st="";
		String sql ="";
		if (flag != null && flag.equals("input")) {
			if(tempeMonth != null && !tempeMonth.equals("")){
				st+="and m.tempe_month ='"+tempeMonth+"' \n";
			}
			if(workerCode != null && !workerCode.equals("")){
				st+="and m.entry_by ='"+workerCode+"' \n";
			}
	
		} 
		
	
		if (flag != null && flag.equals("approve")) {
			if(tempeMonth != null && !tempeMonth.equals("")){
				st+="and m.tempe_month ='"+tempeMonth+"' \n";
			}
			if (entryIds != null) {
				String inEntriyId = bll.subStr(entryIds, ",", 500,
						"m.work_flow_no");
				st += " and " + inEntriyId;
			} else
				st += " and m.work_flow_no =null";
		} 
	
		if (flag != null && flag.equals("query")) {
			if(tempeMonth != null && !tempeMonth.equals("")){
				st+="and substr(m.tempe_month,1,4) ='"+tempeMonth+"' \n";
			}
		} 
		
		if (flag != null && flag.equals("call")) {
			   sql=   "SELECT d.tempe_detail_id,\n" +
				   "       d.tempe_id,\n" + 
				   "       d.dept_name,\n" + 
				   "       d.fact_num,\n" + 
				   "       d.high_tempe_num,\n" + 
				   "       d.high_tempe_standard,\n" + 
				   "       nvl(d.high_tempe_num*d.high_tempe_standard,null) as highAmount,\n" + 
				   "       d.mid_tempe_num,\n" + 
				   "       d.mid_tempe_standard,\n" + 
				   "       nvl(d.mid_tempe_num*d.mid_tempe_standard,null) as midAmount,\n" + 
				   "       d.low_tempe_num,\n" + 
				   "       d.low_tempe_standard,\n" + 
				   "       nvl(d.low_tempe_num*d.low_tempe_standard,null) as lowAmount,\n" + 
				   "       nvl(d.high_tempe_num*d.high_tempe_standard+d.mid_tempe_num*d.mid_tempe_standard+d.low_tempe_num*d.low_tempe_standard,null) as sumAmount,\n" + 
				   "       d.memo,\n" + 
				   "       m.tempe_month,\n" + 
				   "       m.cost_item,\n" + 
				   "       m.tempe_state,\n" + 
				   "       m.work_flow_no\n" + 
				   "  FROM HR_J_LABOR_TEMPE        m,\n" + 
				   "       HR_J_LABOR_TEMPE_DETAIL d\n" + 
				   " WHERE m.tempe_id = d.tempe_id\n" + 
				   "   AND m.is_use = 'Y'\n" + 
				   "   AND d.is_use = 'Y'\n" + 
				   "   AND m.enterprise_code = '"+enterpriseCode+"'\n" + 
				   "   AND d.enterprise_code =  '"+enterpriseCode+"'\n";

				sql+=" AND substr(m.tempe_month, 1, 4) =\n" + 
					"       (SELECT MAX(substr(m.tempe_month, 1, 4))\n" + 
					"          FROM HR_J_LABOR_TEMPE m\n" + 
					"         WHERE m.is_use = 'Y'\n" + 
					"           AND m.enterprise_code = '"+enterpriseCode+"'\n" + 
					"           AND (substr(m.tempe_month, 1, 4) <\n" + 
					"               substr('"+tempeMonth+"', 1, 4)))";
		         sql+="                   order by d.tempe_detail_id 	" ;
			} 
		else {
		 sql =
			"SELECT d.tempe_detail_id,\n" +
			"       d.tempe_id,\n" + 
			"       d.dept_name,\n" + 
			"       nvl(d.fact_num,0),\n" + 
			"       nvl(d.high_tempe_num,0),\n" + 
			"       nvl(d.high_tempe_standard,0),\n" + 
			"       nvl(d.high_tempe_num * d.high_tempe_standard, 0) AS highAmount,\n" + 
			"       nvl(d.mid_tempe_num,0),\n" + 
			"       nvl(d.mid_tempe_standard,0),\n" + 
			"       nvl(d.mid_tempe_num * d.mid_tempe_standard, 0) AS midAmount,\n" + 
			"       nvl(d.low_tempe_num,0),\n" + 
			"       nvl(d.low_tempe_standard,0),\n" + 
			"       nvl(d.low_tempe_num * d.low_tempe_standard, 0) AS lowAmount,\n" + 
			"       nvl(d.high_tempe_num * d.high_tempe_standard +\n" + 
			"           d.mid_tempe_num * d.mid_tempe_standard +\n" + 
			"           d.low_tempe_num * d.low_tempe_standard, 0) AS sumAmount,\n" + 
			"       d.memo,\n" + 
			"       m.tempe_month,\n" + 
			"       m.cost_item,\n" + 
			"       m.tempe_state,\n" + 
			"       m.work_flow_no\n" + 
			"  FROM HR_J_LABOR_TEMPE        m,\n" + 
			"       HR_J_LABOR_TEMPE_DETAIL d\n" + 
			" WHERE m.tempe_id = d.tempe_id\n" + 
			"   AND m.is_use = 'Y'\n" + 
			"   AND d.is_use = 'Y'\n" + 
			"   AND m.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   AND d.enterprise_code = '"+enterpriseCode+"'\n" + 
			 st+	
			"                  \n"+
			"UNION (SELECT NULL,\n" + 
			"              NULL,\n" + 
			"              '合计',\n" + 
			"              SUM(nvl(mm.factNum, 0)),\n" + 
			"              SUM(nvl(mm.highTempeNum, 0)),\n" + 
			"              AVG(nvl(mm.highTempeStandard, 0)),\n" + 
			"              SUM(nvl(mm.highAmount, 0)),\n" + 
			"              SUM(nvl(mm.midTempeNum, 0)),\n" + 
			"              AVG(nvl(mm.midTempeStandard, 0)),\n" + 
			"              SUM(nvl(mm.midAmount, 0)),\n" + 
			"              SUM(nvl(mm.lowTempeNum, 0)),\n" + 
			"              AVG(nvl(mm.lowTempeStandard, 0)),\n" + 
			"              SUM(nvl(mm.lowAmount, 0)),\n" + 
			"              SUM(nvl(mm.sumAmount, 0)),\n" + 
			"              NULL,\n" + 
			"              NULL,\n" + 
			"              NULL,\n" + 
			"              NULL,\n" + 
			"              NULL\n" + 
			"         FROM (SELECT d.tempe_detail_id,\n" + 
			"                      d.tempe_id,\n" + 
			"                      d.dept_name,\n" + 
			"                      nvl(d.fact_num,0) AS factNum,\n" + 
			"                      nvl(d.high_tempe_num,0) AS highTempeNum,\n" + 
			"                      nvl(d.high_tempe_standard,0) AS highTempeStandard,\n" + 
			"                      nvl(d.high_tempe_num * d.high_tempe_standard, 0) AS highAmount,\n" + 
			"                      nvl(d.mid_tempe_num,0) AS midTempeNum,\n" + 
			"                      nvl(d.mid_tempe_standard,0) AS midTempeStandard,\n" + 
			"                      nvl(d.mid_tempe_num * d.mid_tempe_standard, 0) AS midAmount,\n" + 
			"                      nvl(d.low_tempe_num,0) AS lowTempeNum,\n" + 
			"                      nvl(d.low_tempe_standard,0) AS lowTempeStandard,\n" + 
			"                      nvl(d.low_tempe_num * d.low_tempe_standard, 0) AS lowAmount,\n" + 
			"                      nvl(d.high_tempe_num * d.high_tempe_standard +\n" + 
			"                          d.mid_tempe_num * d.mid_tempe_standard +\n" + 
			"                          d.low_tempe_num * d.low_tempe_standard, 0) AS sumAmount,\n" + 
			"                      d.memo,\n" + 
			"                      m.tempe_month,\n" + 
			"                      m.cost_item,\n" + 
			"                      m.tempe_state,\n" + 
			"                      m.work_flow_no\n" + 
			"                 FROM HR_J_LABOR_TEMPE        m,\n" + 
			"                      HR_J_LABOR_TEMPE_DETAIL d\n" + 
			"                WHERE m.tempe_id = d.tempe_id\n" + 
			"                  AND m.is_use = 'Y'\n" + 
			"                  AND d.is_use = 'Y'\n" + 
			"                  AND m.enterprise_code = '"+enterpriseCode+"'\n" + 
			"                  AND d.enterprise_code = '"+enterpriseCode+"'\n"+
			                   st+
			"                  \n"+
			"                   order by d.tempe_detail_id 	" +") mm)\n";
		}

		String sqlCount = "select count(*) from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}
	

	@SuppressWarnings("unchecked")
	public HrJLaborTempe hrJLaborTempeApprove(String tempeMainId,String workflowType,
			String workerCode,String actionId,String eventIdentify, String approveText, String nextRoles,
			String stepId, String enterpriseCode) {

		HrJLaborTempe entity = findByMainId(Long.parseLong(tempeMainId));
		try {
			if (eventIdentify.equals("TH")) {
				entity.setTempeState("3");
			} else {
				if (entity.getTempeState() .equals("1")) {
					if (eventIdentify.equals("TY")) {
						entity.setTempeState("2");
					}
				} 
			}
			WorkflowService service = new WorkflowServiceImpl();
			service.doAction(entity.getWorkFlowNo(), workerCode, Long
					.parseLong(actionId), approveText, null, "", nextRoles);
			entityManager.merge(entity);
		} catch (Exception e) {

		}
		return entity;
	}
	
	
	@SuppressWarnings("unchecked")
	public List getLaborTempeInfo(String tempeMonth,String entryIds,String enterpriseCode) {
		String sql =	"SELECT m.tempe_id,\n" +
			"       m.tempe_month,\n" + 
			"       m.cost_item,\n" + 
			"       m.entry_by,\n" + 
			"       getworkername(m.entry_by) entryName,\n" + 
			"       to_char(m.entry_date, 'yyyy-mm-dd') entry_date,\n" + 
			"       m.tempe_state,\n" + 
			"       m.work_flow_no\n" + 
			"  FROM hr_j_labor_tempe m"+
			"   WHERE  m.is_use = 'Y'\n" + 
			"   AND m.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   AND m.tempe_month ='"+tempeMonth+"'\n";
		if (entryIds != null) {
			String inEntriyId = bll.subStr(entryIds, ",", 500,
					"m.work_flow_no");
			sql += " and " + inEntriyId;
		} else
			sql += " and m.work_flow_no =null";
		sql += "\n order by m.tempe_id";
		return bll.queryByNativeSQL(sql);

	}
	
	@SuppressWarnings("unchecked")
	public void importLaborTempeRecords(List<HrJLaborTempeDetail> laborTempeList,String enterpriseCode) {
		Long tempeDetailId = bll.getMaxId("HR_J_LABOR_TEMPE_DETAIL", "tempe_detail_id");
		for(HrJLaborTempeDetail  entity : laborTempeList){
			String sql = "SELECT d.*\n" +
				"  FROM HR_J_LABOR_TEMPE_DETAIL d\n" + 
				"   WHERE d.is_use = 'Y'\n" + 
				"   AND d.enterprise_code = '"+enterpriseCode+"'\n" + 
				"   AND d.tempe_detail_id =  "+tempeDetailId+"\n";

			List<HrJLaborTempeDetail> list = bll.queryByNativeSQL(sql, HrJLaborTempeDetail.class);
			if(list != null && list.size() > 0){
				HrJLaborTempeDetail updated = list.get(0);
				updated.setDeptName(entity.getDeptName());
				updated.setFactNum(entity.getFactNum());
				updated.setHighTempeNum(entity.getHighTempeNum());
				updated.setHighTempeStandard(entity.getHighTempeStandard());
				updated.setMidTempeNum(entity.getMidTempeNum());
				updated.setMidTempeStandard(entity.getMidTempeStandard());
				updated.setLowTempeNum(entity.getLowTempeNum());
				updated.setLowTempeStandard(entity.getLowTempeStandard());
				updated.setMemo(entity.getMemo());
				entityManager.merge(updated);
			}else{
				
				entity.setTempeDetailId(tempeDetailId++);
				entityManager.persist(entity);
			}

		}
		
	}
	
}