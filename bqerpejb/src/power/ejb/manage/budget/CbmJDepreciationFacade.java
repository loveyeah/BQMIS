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
import power.ejb.manage.budget.form.CbmJDepreciationForm;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

/**
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmJDepreciationFacade implements CbmJDepreciationFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	WorkflowService service;

	public CbmJDepreciationFacade() {
		service = new WorkflowServiceImpl();
	}

	public CbmJDepreciation save(CbmJDepreciation entity) {
		LogUtil.log("saving CbmJDepreciation instance", Level.INFO, null);
		try {
			if (entity.getDepreciationId() == null) {
				entity.setDepreciationId(bll.getMaxId("CBM_J_DEPRECIATION",
						"depreciation_id"));
			}
			entity.setIsUse("Y");
			entity.setWorkFlowStatus("0");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(CbmJDepreciation entity) {
		LogUtil.log("deleting CbmJDepreciation instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmJDepreciation.class, entity
					.getDepreciationId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJDepreciation update(CbmJDepreciation entity) {
		LogUtil.log("updating CbmJDepreciation instance", Level.INFO, null);
		try {
			CbmJDepreciation result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJDepreciation findById(Long id) {
		LogUtil.log("finding CbmJDepreciation instance with id: " + id,
				Level.INFO, null);
		try {
			CbmJDepreciation instance = entityManager.find(
					CbmJDepreciation.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Long getIdByTime(String budgetTime, String enterpriseCode) {
		String sql = "select a.depreciation_id\n"
				+ "  from CBM_J_DEPRECIATION a\n" + " where a.is_use = 'Y'\n"
				+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and a.budget_time = '" + budgetTime + "'";
		Object obj = bll.getSingal(sql);
		if (obj == null)
			return null;
		else
			return Long.parseLong(obj.toString());
	}

	// -------add by fyyang 090824------上报及审批----------------------
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
	public void reportTo(Long depreciationId, String workflowType,
			String workerCode, Long actionId, String approveText,
			String nextRoles, String eventIdentify) {
		CbmJDepreciation entity = this.findById(depreciationId);
		if (entity.getWorkFlowNo() == null) {
			long entryId = service.doInitialize(workflowType, workerCode,
					depreciationId + "");
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

	// 审批签字
	public void depreciationCommSign(Long depreciationId, String approveText,
			String workerCode, Long actionId, String responseDate,
			String nextRoles, String eventIdentify) {
		CbmJDepreciation entity = this.findById(depreciationId);
		if (eventIdentify.equals("ZJ")) {
			entity.setWorkFlowStatus("2");
		}
		if (eventIdentify.equals("TH")) {
			entity.setWorkFlowStatus("3");
		}
		this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId,
				approveText, nextRoles);
	}

	// 审批查询
	@SuppressWarnings("unchecked")
	public PageObject depreciationApproveQuery(String budgetTime,
			String enterpriseCode, String entryIds, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "    select b.*,a.budget_time,a.work_flow_no,a.work_flow_status\n"
				+ "    from CBM_J_DEPRECIATION a,CBM_J_DEPRECIATION_DETAIL b\n"
				+ "      where a.depreciation_id = b.depreciation_id\n"
				+ "      and a.is_use = 'Y'\n"
				+ "      and b.is_use = 'Y'\n"
				+ "      and a.budget_time = '"
				+ budgetTime
				+ "'\n"
				+ "      and a.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "      and b.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "       and  a.work_flow_no in ("
				+ entryIds
				+ ")  "
				+ "      union\n"
				+ "      select null,null,'合计',sum(b.last_asset),sum(b.add_asset),\n"
				+ "      sum(b.reduce_asset),sum(b.new_asset),999999999999999,\n"
				+ "      sum(b.depreciation_number),sum(b.depreciation_sum),'','','','',null,''\n"
				+ "      from CBM_J_DEPRECIATION a,CBM_J_DEPRECIATION_DETAIL b\n"
				+ "      where a.depreciation_id = b.depreciation_id\n"
				+ "      and a.is_use = 'Y'\n"
				+ "      and b.is_use = 'Y'\n"
				+ "      and a.budget_time = '"
				+ budgetTime
				+ "'\n"
				+ "      and a.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "      and b.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "       and  a.work_flow_no in (" + entryIds + ")  ";

		String sqlCount = "select count(1)\n"
				+ "  from CBM_J_DEPRECIATION a, CBM_J_DEPRECIATION_DETAIL b\n"
				+ " where a.depreciation_id = b.depreciation_id\n"
				+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
				+ "   and a.budget_time = '" + budgetTime + "'\n"
				+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and b.enterprise_code = '" + enterpriseCode + "' \n"
				+ "   and  a.work_flow_no in (" + entryIds + ")";

		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		if (list.size() > 1) {
			List arraylist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				CbmJDepreciationForm modelDetail = new CbmJDepreciationForm();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					modelDetail.setDepreciationDetailId(Long.parseLong(data[0]
							.toString()));
				if (data[1] != null)
					modelDetail.setDepreciationId(Long.parseLong(data[1]
							.toString()));
				if (data[2] != null)
					modelDetail.setAssetName(data[2].toString());
				if (data[3] != null)
					modelDetail.setLastAsset(Double.parseDouble(data[3]
							.toString()));
				if (data[4] != null)
					modelDetail.setAddAsset(Double.parseDouble(data[4]
							.toString()));
				if (data[5] != null)
					modelDetail.setReduceAsset(Double.parseDouble(data[5]
							.toString()));
				if (data[6] != null) {
					modelDetail.setNewAsset(Double.parseDouble(data[6]
							.toString()));
					modelDetail.setNewAssetCount(Double.parseDouble(data[3]
							.toString())
							+ Double.parseDouble(data[4].toString())
							- Double.parseDouble(data[5].toString()));
				}
				if (data[7] != null)
					modelDetail.setDepreciationRate(Double.parseDouble(data[7]
							.toString()));
				if (data[8] != null)
					modelDetail.setDepreciationNumber(Double
							.parseDouble(data[8].toString()));
				if (data[9] != null)
					modelDetail.setDepreciationSum(Double.parseDouble(data[9]
							.toString()));
				if (data[10] != null)
					modelDetail.setMemo(data[10].toString());
				if (data[11] != null)
					modelDetail.setIsUse(data[11].toString());
				if (data[12] != null)
					modelDetail.setEnterpriseCode(data[12].toString());
				if (data[13] != null)
					modelDetail.setBudgetTime(data[13].toString());
				if (data[14] != null)
					modelDetail.setWorkFlowNo(Long.parseLong(data[14]
							.toString()));
				if (data[15] != null)
					modelDetail.setWorkFlowStatus(data[15].toString());

				arraylist.add(modelDetail);
			}
			pg.setList(arraylist);
			if (list != null) {
				pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount)
						.toString()));
			}
		}
		return pg;
	}

}