package power.ejb.manage.budget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.budget.form.FianceDetailForm;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

/**
 * 财务费用预算
 * 
 * @author liuyi 090822
 */
@Stateless
public class CbmJFinanceFacade implements CbmJFinanceFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	WorkflowService service;

	public CbmJFinanceFacade() {
		service = new WorkflowServiceImpl();
	}

	/**
	 * 新增一条财务费用预算记录
	 */
	public CbmJFinance save(CbmJFinance entity) {
		LogUtil.log("saving CbmJFinance instance", Level.INFO, null);
		try {
			entity.setFinanceId(bll.getMaxId("CBM_J_FINANCE", "FINANCE_ID"));
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
	 * 删除一条财务费用预算记录
	 */
	public void delete(CbmJFinance entity) {
		LogUtil.log("deleting CbmJFinance instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmJFinance.class, entity
					.getFinanceId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条财务费用预算记录
	 */
	public CbmJFinance update(CbmJFinance entity) {
		LogUtil.log("updating CbmJFinance instance", Level.INFO, null);
		try {
			CbmJFinance result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找一条财务费用预算记录
	 */
	public CbmJFinance findById(Long id) {
		LogUtil.log("finding CbmJFinance instance with id: " + id, Level.INFO,
				null);
		try {
			CbmJFinance instance = entityManager.find(CbmJFinance.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all CbmJFinance entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CbmJFinance> all CbmJFinance entities
	 */
	@SuppressWarnings("unchecked")
	public List<CbmJFinance> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all CbmJFinance instances", Level.INFO, null);
		try {
			final String queryString = "select model from CbmJFinance model";
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

	public Long getIdByTimeType(String budgetTime, String financeType,
			String enterpriseCode) {
		String sql = "select a.finance_id \n" + "from CBM_J_FINANCE a \n"
				+ "where a.is_use='Y' \n" + "and a.enterprise_code='"
				+ enterpriseCode + "' \n" + "and a.budget_time='" + budgetTime
				+ "' \n" + "and a.finance_type='" + financeType + "' \n";
		Object obj = bll.getSingal(sql);
		if (obj == null)
			return null;
		else
			return Long.parseLong(obj.toString());
	}

	// -------------------上报及审批 add by fyyang 090824------------------
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
	public void reportTo(Long financeId, String workflowType,
			String workerCode, Long actionId, String approveText,
			String nextRoles, String eventIdentify) {
		CbmJFinance entity = this.findById(financeId);
		if (entity.getWorkFlowNo() == null) {
			long entryId = service.doInitialize(workflowType, workerCode,
					financeId + "");
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

	public void financeCommSign(Long financeId, String approveText,
			String workerCode, Long actionId, String responseDate,
			String nextRoles, String eventIdentify) {
		CbmJFinance entity = this.findById(financeId);
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
	public PageObject financeApproveQuery(String budgetTime,
			String financeType, String enterpriseCode, String entryIds,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.finance_id, \n" + "a.budget_time, \n"
				+ "a.finance_type, \n" + "a.work_flow_no, \n"
				+ "a.work_flow_status, \n" + "b.finance_detail_id, \n"
				+ "b.loan_name, \n" + "b.last_loan, \n" + "b.new_loan, \n"
				+ "b.repayment, \n" + "b.balance, \n" + "b.interest, \n"
				+ "b.memo, \n" + "b.is_use \n"
				+ "from CBM_J_FINANCE a,CBM_J_FINANCE_DETAIL b \n"
				+ "where a.finance_id=b.finance_id \n" + "and a.is_use='Y' \n"
				+ "and b.is_use='Y' \n" + "and a.enterprise_code='"
				+ enterpriseCode + "' \n" + "and b.enterprise_code='"
				+ enterpriseCode + "' \n" + "and a.budget_time='" + budgetTime
				+ "' \n" + "and a.finance_type='" + financeType + "' \n"
				+ "  and  a.work_flow_no in(" + entryIds + ")";
		String sqlCount = "select count(*) \n" + "from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<FianceDetailForm> arrlist = new ArrayList<FianceDetailForm>();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				FianceDetailForm form = new FianceDetailForm();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					form.setFinanceId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					form.setBudgetTime(data[1].toString());
				if (data[2] != null)
					form.setFinanceType(data[2].toString());
				if (data[3] != null)
					form.setWorkFlowNo(Long.parseLong(data[3].toString()));
				if (data[4] != null)
					form.setWorkFlowStatus(data[4].toString());
				if (data[5] != null)
					form.setFinanceDetailId(Long.parseLong(data[5].toString()));
				if (data[6] != null)
					form.setLoanName(data[6].toString());
				if (data[7] != null)
					form.setLastLoan(Double.parseDouble(data[7].toString()));
				if (data[8] != null)
					form.setNewLoan(Double.parseDouble(data[8].toString()));
				if (data[9] != null)
					form.setRepayment(Double.parseDouble(data[9].toString()));
				if (data[10] != null)
					form.setBalance(Double.parseDouble(data[10].toString()));
				if (data[11] != null)
					form.setInterest(Double.parseDouble(data[11].toString()));
				if (data[12] != null)
					form.setMemo(data[12].toString());
				if (data[13] != null)
					form.setIsUse(data[13].toString());
				arrlist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

}