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
import power.ejb.manage.budget.form.FianceDetailForm;

/**
 * 财务费用预算明细
 * 
 * @author liuyi 090822
 */
@Stateless
public class CbmJFinanceDetailFacade implements CbmJFinanceDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "CbmJFinanceFacade")
	protected CbmJFinanceFacadeRemote financeRemote;

	/**
	 * 新增一条财务费用预算明细记录
	 */
	public void save(CbmJFinanceDetail entity) {
		LogUtil.log("saving CbmJFinanceDetail instance", Level.INFO, null);
		try {
			entity.setFinanceDetailId(bll.getMaxId("CBM_J_FINANCE_DETAIL",
					"FINANCE_DETAIL_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条财务费用预算明细记录
	 */
	public void delete(CbmJFinanceDetail entity) {
		LogUtil.log("deleting CbmJFinanceDetail instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmJFinanceDetail.class, entity
					.getFinanceDetailId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条或多条财务费用预算明细记录
	 */
	public void delete(String ids) {
		String sql = "update  CBM_J_FINANCE_DETAIL a set a.is_use='N' \n"
				+ " where a.FINANCE_DETAIL_ID in (" + ids + ") \n";
		bll.exeNativeSQL(sql);
	}

	/**
	 * 更新一条财务费用预算明细记录
	 */
	public CbmJFinanceDetail update(CbmJFinanceDetail entity) {
		LogUtil.log("updating CbmJFinanceDetail instance", Level.INFO, null);
		try {
			CbmJFinanceDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过id查找一条财务费用预算明细记录
	 */
	public CbmJFinanceDetail findById(Long id) {
		LogUtil.log("finding CbmJFinanceDetail instance with id: " + id,
				Level.INFO, null);
		try {
			CbmJFinanceDetail instance = entityManager.find(
					CbmJFinanceDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查找所有符合条件的财务预算明细
	 */
	public PageObject findAllFinanceDetail(String budgetTime,
			String financeType, String enterpriseCode,
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
				+ "' \n" + "and a.finance_type='" + financeType + "' \n";
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

	public void saveFinanceDetails(List<FianceDetailForm> addList,
			List<FianceDetailForm> updateList, String ids) {
		if (addList != null && addList.size() > 0) {
			FianceDetailForm flag = addList.get(0);
			CbmJFinance finance = new CbmJFinance();
			finance.setBudgetTime(flag.getBudgetTime());
			finance.setFinanceType(flag.getFinanceType());
			finance.setWorkFlowNo(flag.getWorkFlowNo());
			finance.setWorkFlowStatus(flag.getWorkFlowStatus());
			finance.setIsUse(flag.getIsUse());
			finance.setEnterpriseCode(flag.getEnterpriseCode());
			if (flag.getFinanceId() != null) {
				finance.setFinanceId(flag.getFinanceId());
				financeRemote.update(finance);
			} else {
				finance.setFinanceId(financeRemote.getIdByTimeType(flag
						.getBudgetTime(), flag.getFinanceType(), flag
						.getEnterpriseCode()));
				finance = financeRemote.save(finance);
				entityManager.flush();
			}
			for (FianceDetailForm form : addList) {
				CbmJFinanceDetail detail = new CbmJFinanceDetail();
				// detail.setFinanceDetailId(form.getFinanceDetailId());
				detail.setFinanceId(finance.getFinanceId());
				detail.setLoanName(form.getLoanName());
				detail.setLastLoan(form.getLastLoan());
				detail.setNewLoan(form.getNewLoan());
				detail.setRepayment(form.getRepayment());
				detail.setBalance(form.getBalance());
				detail.setInterest(form.getInterest());
				detail.setMemo(form.getMemo());
				detail.setIsUse(form.getIsUse());
				detail.setEnterpriseCode(form.getEnterpriseCode());
				this.save(detail);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (FianceDetailForm form : updateList) {
				CbmJFinanceDetail detail = new CbmJFinanceDetail();
				detail.setFinanceDetailId(form.getFinanceDetailId());
				detail.setFinanceId(form.getFinanceId());
				detail.setLoanName(form.getLoanName());
				detail.setLastLoan(form.getLastLoan());
				detail.setNewLoan(form.getNewLoan());
				detail.setRepayment(form.getRepayment());
				detail.setBalance(form.getBalance());
				detail.setInterest(form.getInterest());
				detail.setMemo(form.getMemo());
				detail.setIsUse(form.getIsUse());
				detail.setEnterpriseCode(form.getEnterpriseCode());
				this.update(detail);
			}
		}
		if (ids != null && ids.length() > 0) {
			this.delete(ids);
		}

	}
}