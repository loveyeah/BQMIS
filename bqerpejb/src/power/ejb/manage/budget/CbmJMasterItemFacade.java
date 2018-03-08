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

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.budget.form.ExeControlForm;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

/**
 * 预算执行控制
 * 
 * @author liuyi 090806
 */
@Stateless
public class CbmJMasterItemFacade implements CbmJMasterItemFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "CbmJBudgetItemFacade")
	protected CbmJBudgetItemFacadeRemote itemRemote;
	WorkflowService service;

	public CbmJMasterItemFacade() {
		service = new WorkflowServiceImpl();
	}

	/**
	 * 新增一条预算执行控制记录
	 */
	public void save(CbmJMasterItem entity) {
		LogUtil.log("saving CbmJMasterItem instance", Level.INFO, null);
		try {
			entity.setHappenId(bll.getMaxId("CBM_J_MASTER_ITEM", "HAPPEN_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条预算执行控制记录
	 */
	public void delete(CbmJMasterItem entity) {
		LogUtil.log("deleting CbmJMasterItem instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmJMasterItem.class, entity
					.getHappenId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条或多条预算执行控制记录
	 */
	public void delete(String ids) {
		String sql = "update  CBM_J_MASTER_ITEM a set a.is_use='N' \n"
				+ " where a.happen_id in (" + ids + ") \n";
		bll.exeNativeSQL(sql);
	}

	/**
	 * 
	 */
	public CbmJMasterItem update(CbmJMasterItem entity) {
		LogUtil.log("updating CbmJMasterItem instance", Level.INFO, null);
		try {
			CbmJMasterItem result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJMasterItem findById(Long id) {
		LogUtil.log("finding CbmJMasterItem instance with id: " + id,
				Level.INFO, null);
		try {
			CbmJMasterItem instance = entityManager.find(CbmJMasterItem.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CbmJMasterItem> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all CbmJMasterItem instances", Level.INFO, null);
		try {
			final String queryString = "select model from CbmJMasterItem model";
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

	@SuppressWarnings("unchecked")
	public PageObject getAllList(String isFeeRegister, String budgetItemId,
			String deptCode, String dataType, String budgetTime,
			String enterpriseCode, int... rowStartIdxAndCount) {
		try {
			PageObject pg = new PageObject();
			// update by sychen 20100611
			String sql="select a.happen_id,\n" +
				"a.budget_item_id,\n" + 
				"a.center_id,\n" + 
				"a.budget_time,\n" + 
				"a.item_id,\n" + 
				"a.happen_serial,\n" + 
				"a.happen_value,\n" + 
				"a.happen_explain,\n" + 
				"a.fill_by,\n" + 
				"a.fill_time,\n" + 
				"a.work_flow_no,\n" + 
				"a.happen_status,\n" + 
				"a.is_use,\n" + 
				"a.enterprise_code,\n" + 
				"c.dep_code,\n" + 
				"c.dep_name,\n" + 
				"d.item_code,\n" + 
				"d.item_name,\n" + 
				"getworkername(a.fill_by),\n" + 
				"to_char(a.fill_time,'yyyy-mm-dd')\n" + 
				"from CBM_J_MASTER_ITEM a,CBM_J_BUDGET_ITEM b,CBM_C_CENTER c,CBM_C_ITEM d\n" + 
				"where a.budget_item_id=b.budget_item_id\n" + 
				"and a.center_id=c.center_id\n" + 
				"and a.item_id=d.item_id\n" + 
				"and a.is_use='Y'\n" + 
				"and c.is_use='Y'\n" + 
				"and d.is_use='Y'\n" + 
				"and a.enterprise_code='hfdc'\n" + 
				"and b.enterprise_code='hfdc'\n" + 
				"and c.enterprise_code='hfdc'\n" + 
				"and d.enterprise_code='hfdc'\n";
			
            String sqlCount="select count(*) \n" + 
 				"from CBM_J_MASTER_ITEM a,CBM_J_BUDGET_ITEM b,CBM_C_CENTER c,CBM_C_ITEM d\n" + 
 				"where a.budget_item_id=b.budget_item_id\n" + 
 				"and a.center_id=c.center_id\n" + 
 				"and a.item_id=d.item_id\n" + 
 				"and a.is_use='Y'\n" + 
 				"and c.is_use='Y'\n" + 
 				"and d.is_use='Y'\n" + 
 				"and a.enterprise_code='hfdc'\n" + 
 				"and b.enterprise_code='hfdc'\n" + 
 				"and c.enterprise_code='hfdc'\n" + 
 				"and d.enterprise_code='hfdc'\n";
			
//			String sql = "select a.happen_id, \n"
//					+ "a.budget_item_id, \n"
//					+ "a.center_id, \n"
//					+ "a.budget_time, \n"
//					+ "a.item_id, \n"
//					+ "a.happen_serial, \n"
//					+ "a.happen_value, \n"
//					+ "a.happen_explain, \n"
//					+ "a.fill_by, \n"
//					+ "a.fill_time, \n"
//					+ "a.work_flow_no, \n"
//					+ "a.happen_status, \n"
//					+ "a.is_use, \n"
//					+ "a.enterprise_code, \n"
//					+ "c.dep_code, \n"
//					+ "c.dep_name, \n"
//					+ "d.item_code, \n"
//					+ "d.item_name, \n"
//					+ "getworkername(a.fill_by), \n"
//					+ "to_char(a.fill_time,'yyyy-mm-dd') \n"
//					+ "from CBM_J_MASTER_ITEM a,CBM_J_BUDGET_ITEM b,CBM_C_CENTER c,CBM_C_ITEM d, \n"
//					+ " CBM_C_MASTER_ITEM e,CBM_C_CENTER f \n"
//					+ "where a.budget_item_id=b.budget_item_id \n"
//					+ "and a.center_id=c.center_id \n"
//					+ "and a.item_id=d.item_id \n" + "and a.is_use='Y' \n"
//					+ "and c.is_use='Y' \n" + "and d.is_use='Y' \n"
//					+ "and e.is_use='Y' \n" + "and f.is_use='Y' \n"
//					+ "and a.enterprise_code='" + enterpriseCode + "' \n"
//					+ "and b.enterprise_code='" + enterpriseCode + "' \n"
//					+ "and c.enterprise_code='" + enterpriseCode + "' \n"
//					+ "and d.enterprise_code='" + enterpriseCode + "' \n"
//					+ "and e.enterprise_code='" + enterpriseCode + "' \n"
//					+ "and f.enterprise_code='" + enterpriseCode + "' \n"
//					+ "and a.center_id=e.center_id \n"
//					+ "and a.item_id=e.item_id \n" + "and e.come_from='1' \n"
//					+ "and a.center_id=f.center_id \n";
//			String sqlCount = "select count(*) \n"
//					+ "from CBM_J_MASTER_ITEM a,CBM_J_BUDGET_ITEM b,CBM_C_CENTER c,CBM_C_ITEM d, \n"
//					+ " CBM_C_MASTER_ITEM e,CBM_C_CENTER f \n"
//					+ "where a.budget_item_id=b.budget_item_id \n"
//					+ "and a.center_id=c.center_id \n"
//					+ "and a.item_id=d.item_id \n" + "and a.is_use='Y' \n"
//					+ "and c.is_use='Y' \n" + "and d.is_use='Y' \n"
//					+ "and e.is_use='Y' \n" + "and f.is_use='Y' \n"
//					+ "and a.enterprise_code='" + enterpriseCode + "' \n"
//					+ "and b.enterprise_code='" + enterpriseCode + "' \n"
//					+ "and c.enterprise_code='" + enterpriseCode + "' \n"
//					+ "and d.enterprise_code='" + enterpriseCode + "' \n"
//					+ "and e.enterprise_code='" + enterpriseCode + "' \n"
//					+ "and f.enterprise_code='" + enterpriseCode + "' \n"
//					+ "and a.center_id=e.center_id \n"
//					+ "and a.item_id=e.item_id \n" + "and e.come_from='1' \n"
//					+ "and a.center_id=f.center_id \n";
			if (budgetItemId != null && !(budgetItemId.equals(""))) {
				sql = sql + "and a.budget_item_id=" + budgetItemId + " \n";
				sqlCount = sqlCount + "and a.budget_item_id=" + budgetItemId
						+ " \n";
			}
			if (deptCode != null && !(deptCode.equals(""))) {
				sql = sql + "and c.dep_code='" + deptCode + "' \n";
				sqlCount = sqlCount + "and c.dep_code='" + deptCode + "' \n";
			}
//			if (dataType != null && !(dataType.equals(""))) {
//				sql = sql + "and e.data_type='" + dataType + "' \n";
//				sqlCount = sqlCount + "and e.data_type='" + dataType + "' \n";
//			}
			if (budgetTime != null && !(budgetTime.equals(""))) {
				sql = sql + "and a.budget_time='" + budgetTime + "' \n";
				sqlCount = sqlCount + "and a.budget_time='" + budgetTime
						+ "' \n";
			}
			sql = sql + " order by a.happen_serial \n";
			sqlCount = sqlCount + " order by a.happen_serial \n";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();

			if (list.size() > 0) {
				while (it.hasNext()) {
					CbmJMasterItem cjm = new CbmJMasterItem();
					ExeControlForm form = new ExeControlForm();
					Object[] data = (Object[]) it.next();
					SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
					if (data[0] != null)
						cjm.setHappenId(Long.parseLong(data[0].toString()));
					if (data[1] != null)
						cjm.setBudgetItemId(Long.parseLong(data[1].toString()));
					if (data[2] != null)
						cjm.setCenterId(Long.parseLong(data[2].toString()));
					if (data[3] != null)
						cjm.setBudgetTime(data[3].toString());
					if (data[4] != null)
						cjm.setItemId(Long.parseLong(data[4].toString()));
					if (data[5] != null)
						cjm.setHappenSerial(Long.parseLong(data[5].toString()));
					if (data[6] != null)
						if (isFeeRegister != null && isFeeRegister.equals("N")) {
							cjm.setHappenValue(Double.parseDouble(data[6]
									.toString()));
						} else {
							cjm
									.setHappenValue(Math
											.rint(Double.parseDouble(data[6]
													.toString()) * 1000000) / 100);
						}
					if (data[7] != null)
						cjm.setHappenExplain(data[7].toString());
					if (data[8] != null)
						cjm.setFillBy(data[8].toString());
					if (data[9] != null)
						try {
							cjm.setFillTime(sbf.parse(data[9].toString()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					if (data[10] != null)
						cjm.setWorkFlowNo(Long.parseLong(data[10].toString()));
					if (data[11] != null)
						cjm.setHappenStatus(data[11].toString());
					if (data[12] != null)
						cjm.setIsUse(data[12].toString());
					if (data[13] != null)
						cjm.setEnterpriseCode(data[13].toString());
					if (data[14] != null)
						form.setCenterCode(data[14].toString());
					if (data[15] != null)
						form.setCenterName(data[15].toString());
					if (data[16] != null)
						form.setItemCode(data[16].toString());
					if (data[17] != null)
						form.setItemName(data[17].toString());
					if (data[18] != null)
						form.setFillName(data[18].toString());
					if (data[19] != null)
						form.setFillTime(data[19].toString());
					form.setCjm(cjm);
					arrlist.add(form);
				}
			}
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString()
					.toString());
			pg.setList(arrlist);
			pg.setTotalCount(totalCount);
			return pg;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void saveModified(String isFeeRegister,
			List<CbmJMasterItem> addList, List<CbmJMasterItem> updateList,
			String ids) {
		if (addList.size() > 0) {
			for (CbmJMasterItem entity : addList) {
				if (isFeeRegister != null && isFeeRegister.equals("Y")) {
					entity.setHappenSerial(bll.getMaxId("CBM_J_MASTER_ITEM",
							"HAPPEN_SERIAL"));
					this.save(entity);
					entityManager.flush();
				} else {
					entity.setHappenSerial(0L);
					this.save(entity);
					CbmJBudgetItem temp = itemRemote.findById(entity
							.getBudgetItemId());
					temp.setFactHappen(entity.getHappenValue());
					itemRemote.update(temp);
					entityManager.flush();
				}
			}
		}
		if (updateList.size() > 0) {
			for (CbmJMasterItem entity : updateList) {
				entity.setFillTime(new Date());
				if (isFeeRegister != null && isFeeRegister.equals("Y")) {
					if (entity.getHappenStatus() != null
							&& !entity.getHappenStatus().equals("1")
							&& !entity.getHappenStatus().equals("2")) {
						this.update(entity);
					}

				} else if (isFeeRegister != null && isFeeRegister.equals("N")) {
					CbmJBudgetItem temp = itemRemote.findById(entity
							.getBudgetItemId());
					temp.setFactHappen(entity.getHappenValue());
					itemRemote.update(temp);
					this.update(entity);
				}
			}
		}
		if (ids != null && ids.length() > 0) {
			this.delete(ids);
		}
	}

	// ----------add by fyyang 090822----------上报及审批-----------------------
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
	public void reportTo(Long happenId, String workflowType, String workerCode,
			Long actionId, String approveText, String nextRoles,
			String eventIdentify) {
		CbmJMasterItem entity = this.findById(happenId);
		if (entity.getWorkFlowNo() == null) {
			long entryId = service.doInitialize(workflowType, workerCode,
					happenId + "");
			service.doAction(entryId, workerCode, actionId, approveText, null,
					nextRoles);
			entity.setWorkFlowNo(entryId);
		} else {
			this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId,
					approveText, nextRoles);
		}
		entity.setHappenStatus("1");
		this.update(entity);
	}

	// 审批
	public void masterItemCommSign(Long happenId, String approveText,
			String workerCode, Long actionId, String responseDate,
			String nextRoles, String eventIdentify) {
		CbmJMasterItem entity = this.findById(happenId);
		if (eventIdentify.equals("ZJ")) {
			entity.setHappenStatus("2");
			CbmJBudgetItem temp = itemRemote.findById(entity.getBudgetItemId());
			if (temp.getFactHappen() == null)
				temp.setFactHappen(0.0);
			temp.setFactHappen(temp.getFactHappen() + entity.getHappenValue());
			itemRemote.update(temp);
			entityManager.flush();
		}
		if (eventIdentify.equals("TH")) {
			entity.setHappenStatus("3");
		}
		this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId,
				approveText, nextRoles);
	}

	// 查询
	@SuppressWarnings("unchecked")
	public PageObject masterItemApproveQuery(String deptId, String budgetTime,
			String entryIds, int... rowStartIdxAndCount) {
		String sqlWhere = "";
		if (deptId != null && !deptId.equals("")) {
			sqlWhere = " and  c.center_id=" + deptId;
		}
		if (budgetTime != null && !budgetTime.equals("")) {
			sqlWhere += "  and c.budget_time='" + budgetTime + "' \n";
		}
		String sqlCount = "select count(1)\n"
				+ " from CBM_J_MASTER_ITEM a,CBM_J_BUDGET_ITEM b,CBM_J_BUDGET_MAKE c,CBM_C_CENTER_TOPIC d,CBM_C_CENTER_ITEM e\n"
				+ " where a.budget_item_id=b.budget_item_id\n"
				+ " and b.budget_make_id=c.budget_make_id\n"
				+ " and c.center_id=d.center_id and c.topic_id=d.topic_id\n"
				+ " and e.center_topic_id=d.center_topic_id\n"
				+ " and e.item_id=b.item_id\n"
				+ " and a.is_use='Y' and d.is_use='Y' and e.is_use='Y'  and  a.work_flow_no in ("
				+ entryIds + ")\n";
		sqlCount += sqlWhere;
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		if (totalCount > 0) {
			PageObject obj = new PageObject();
			obj.setTotalCount(totalCount);
			String sql = "select a.happen_id,b.item_id,e.item_alias,b.advice_budget,a.happen_value,a.fill_by,to_char(a.fill_time,'yyyy-MM-dd'),a.happen_explain,a.work_flow_no,a.center_id,GETWORKERNAME(a.fill_by)\n"
					+ " from CBM_J_MASTER_ITEM a,CBM_J_BUDGET_ITEM b,CBM_J_BUDGET_MAKE c,CBM_C_CENTER_TOPIC d,CBM_C_CENTER_ITEM e\n"
					+ " where a.budget_item_id=b.budget_item_id\n"
					+ " and b.budget_make_id=c.budget_make_id\n"
					+ " and c.center_id=d.center_id and c.topic_id=d.topic_id\n"
					+ " and e.center_topic_id=d.center_topic_id\n"
					+ " and e.item_id=b.item_id\n"
					+ " and a.is_use='Y' and d.is_use='Y' and e.is_use='Y' and  a.work_flow_no in ("
					+ entryIds + ")\n";
			List<ExeControlForm> list = new ArrayList<ExeControlForm>();
			List ojbList = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			Iterator it = ojbList.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				ExeControlForm model = new ExeControlForm();
				CbmJMasterItem entity = new CbmJMasterItem();
				if (data[0] != null) {
					entity.setHappenId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					entity.setItemId(Long.parseLong(data[1].toString()));
				}
				if (data[2] != null) {
					model.setItemName(data[2].toString());
				}
				if (data[3] != null) {
					model.setAdviceBudget(Double
							.parseDouble(data[3].toString()));
				}
				if (data[4] != null) {
					entity.setHappenValue(Double
							.parseDouble(data[4].toString()));
				}
				if (data[5] != null) {
					entity.setFillBy(data[5].toString());
				}
				if (data[6] != null) {
					model.setFillTime(data[6].toString());
				}
				if (data[7] != null) {
					entity.setHappenExplain(data[7].toString());
				}
				if (data[8] != null) {
					entity.setWorkFlowNo(Long.parseLong(data[8].toString()));
				}
				if (data[9] != null) {
					entity.setCenterId(Long.parseLong(data[9].toString()));
				}
				if (data[10] != null) {
					model.setFillName(data[10].toString());
				}
				model.setCjm(entity);
				list.add(model);
			}
			obj.setList(list);
			return obj;
		}
		return null;

	}
}