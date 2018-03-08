package power.ejb.manage.budget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.budget.form.CapitalDetailForm;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

/**
 * 资本性支出预算
 * 
 * @author liuyi 090824
 */
@Stateless
public class CbmJCapitalFacade implements CbmJCapitalFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	WorkflowService service;

	public CbmJCapitalFacade() {
		service = new WorkflowServiceImpl();
	}

	/**
	 * 新增一条资本性支出预算记录
	 */
	public CbmJCapital save(CbmJCapital entity) {
		LogUtil.log("saving CbmJCapital instance", Level.INFO, null);
		try {
			entity.setCapitalId(bll.getMaxId("CBM_J_CAPITAL", "CAPITAL_ID"));
			entity.setWorkFlowStatus("0");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return entity;
	}

	/**
	 * 删除一条资本性支出预算记录
	 */
	public void delete(CbmJCapital entity) {
		LogUtil.log("deleting CbmJCapital instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmJCapital.class, entity
					.getCapitalId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条资本性支出预算记录
	 */
	public CbmJCapital update(CbmJCapital entity) {
		LogUtil.log("updating CbmJCapital instance", Level.INFO, null);
		try {
			CbmJCapital result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJCapital findById(Long id) {
		LogUtil.log("finding CbmJCapital instance with id: " + id, Level.INFO,
				null);
		try {
			CbmJCapital instance = entityManager.find(CbmJCapital.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过预算时间查询资本性支出预算记录id
	 */
	public Long fingIdByTime(String budgetTime, String enterpriseCode) {
		String sql = "select a.CAPITAL_ID \n" + "from CBM_J_CAPITAL a \n"
				+ "where a.is_use='Y' \n" + "and a.enterprise_code='"
				+ enterpriseCode + "' \n" + "and a.budget_time='" + budgetTime
				+ "' \n";
		Object obj = bll.getSingal(sql);
		if (obj == null)
			return null;
		else
			return Long.parseLong(obj.toString());
	}

	// -------------------上报及审批 ------------------
	/**
	 * 执行
	 * 
	 * @param entryId
	 *            实例编号
	 * @param workerCode
	 *            审批人
	 * @param actionId
	 *            动作
	 * @param approveText
	 *            意见
	 */
	private void changeWfInfo(Long entryId, String workerCode, Long actionId,
			String approveText, String nextRoles) {
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
	}

	// 上报
	public void reportTo(Long capitalId, String workflowType,
			String workerCode, Long actionId, String approveText,
			String nextRoles, String eventIdentify) {
		CbmJCapital entity = this.findById(capitalId);
		if (entity.getWorkFlowNo() == null) {
			long entryId = service.doInitialize(workflowType, workerCode,
					capitalId + "");
			service.doAction(entryId, workerCode, actionId, approveText, null,
					nextRoles);
			entity.setWorkFlowNo(entryId);
		} else {
			this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId,
					approveText, nextRoles);
		}
		entity.setWorkFlowStatus("1");
		this.update(entity);
	}

	public void capitalCommSign(Long capitalId, String approveText,
			String workerCode, Long actionId, String responseDate,
			String nextRoles, String eventIdentify) {
		CbmJCapital entity = this.findById(capitalId);
		if (eventIdentify.equals("ZJ")) {
			entity.setWorkFlowStatus("2");
		}
		if (eventIdentify.equals("TH")) {
			entity.setWorkFlowStatus("3");
		}
		this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId,
				approveText, nextRoles);
	}

	@SuppressWarnings("unchecked")
	public PageObject capitalApproveQuery(String budgetTime,
			String enterpriseCode, String entryIds, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.capital_id, \n" + "a.budget_time, \n"
				+ "a.work_flow_no, \n" + "a.work_flow_status, \n"
				+ "b.capital_detail_id, \n" + "b.project, \n"
				+ "b.material_cost, \n" + "b.working_cost, \n"
				+ "b.other_cost, \n" + "b.device_cost, \n" + "b.total_cost, \n"
				+ "b.memo, \n" + "b.is_use, \n" + "b.enterprise_code \n"
				+ "from CBM_J_CAPITAL a,CBM_J_CAPITAL_DETAIL b \n"
				+ "where a.capital_id=b.capital_id \n" + "and a.is_use='Y' \n"
				+ "and b.is_use='Y' \n" + "and a.enterprise_code='"
				+ enterpriseCode + "' \n" + "and b.enterprise_code='"
				+ enterpriseCode + "' \n" + "and a.budget_time='" + budgetTime
				+ "' \n" + "  and  a.work_flow_no in(" + entryIds + ")";
		String sqlCount = "select count(*) \n" + "from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<CapitalDetailForm> arrlist = new ArrayList<CapitalDetailForm>();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				CapitalDetailForm form = new CapitalDetailForm();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					form.setCapitalId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					form.setBudgetTime(data[1].toString());
				if (data[2] != null)
					form.setWorkFlowNo(Long.parseLong(data[2].toString()));
				if (data[3] != null)
					form.setWorkFlowStatus(data[3].toString());
				if (data[4] != null)
					form.setCapitalDetailId(Long.parseLong(data[4].toString()));
				if (data[5] != null)
					form.setProject(data[5].toString());
				if (data[6] != null)
					form
							.setMaterialCost(Double.parseDouble(data[6]
									.toString()));
				if (data[7] != null)
					form.setWorkingCost(Double.parseDouble(data[7].toString()));
				if (data[8] != null)
					form.setOtherCost(Double.parseDouble(data[8].toString()));
				if (data[9] != null)
					form.setDeviceCost(Double.parseDouble(data[9].toString()));
				if (data[10] != null)
					form.setTotalCost(Double.parseDouble(data[10].toString()));
				if (data[11] != null)
					form.setMemo(data[11].toString());
				if (data[12] != null)
					form.setIsUse(data[12].toString());
				if (data[13] != null)
					form.setEnterpriseCode(data[13].toString());
				arrlist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

}