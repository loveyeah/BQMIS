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
import power.ejb.manage.budget.form.CbmJBudgetItemForm;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

/**
 * Facade for entity CbmJBudgetMake.
 * 
 * @see power.ejb.manage.budget.CbmJBudgetMake
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmJBudgetMakeFacade implements CbmJBudgetMakeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	WorkflowService service;

	public CbmJBudgetMakeFacade() {
		service = new WorkflowServiceImpl();
	}

	public CbmJBudgetMake save(CbmJBudgetMake entity) {
		LogUtil.log("saving CbmJBudgetMake instance", Level.INFO, null);
		try {
			entity.setBudgetMakeId(bll.getMaxId("CBM_J_BUDGET_MAKE",
					"BUDGET_MAKE_ID"));
			// 经济指标录入时保存数据MakeStatus为原来状态 ltong
			if (entity.getMakeStatus() == null) {
				entity.setMakeStatus("0");
			}
			entity.setMakeDate(new java.util.Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(CbmJBudgetMake entity) {
		LogUtil.log("deleting CbmJBudgetMake instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmJBudgetMake.class, entity
					.getBudgetMakeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJBudgetMake update(CbmJBudgetMake entity) {
		LogUtil.log("updating CbmJBudgetMake instance", Level.INFO, null);
		try {
			entity.setMakeDate(new java.util.Date());
			CbmJBudgetMake result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJBudgetMake findById(Long id) {
		LogUtil.log("finding CbmJBudgetMake instance with id: " + id,
				Level.INFO, null);
		try {
			CbmJBudgetMake instance = entityManager.find(CbmJBudgetMake.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据部门和主题查找预算编制单信息
	 */
	@SuppressWarnings("unchecked")
	public List<CbmJBudgetItemForm> findByTopicAndDept(String time,
			String deptCode, Long topicId) {
		List<CbmJBudgetItemForm> resultList = new ArrayList<CbmJBudgetItemForm>();

		String sqlCount = "select count(*)\n"
				+ "  from CBM_J_BUDGET_MAKE t, CBM_J_BUDGET_ITEM r, CBM_C_CENTER d\n"
				+ " where t.budget_make_id = r.budget_make_id\n"
				+ "   and t.center_id = d.center_id\n" + "   and t.topic_id = "
				+ topicId + "\n" + "   and d.dep_code = '" + deptCode + "'\n"
				+ "   and t.budget_time = '" + time + "'\n"
				+ "   and d.is_use = 'Y'";
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		if (totalCount == 0) {
			String sql = "select a.center_item_id as item_id, a.item_alias, c.unit_code, GETUNITNAME(c.unit_code),b.center_id,f.finance_item,a.data_source,h.zbbmtx_code \n"
					+ "  from CBM_C_CENTER_ITEM a, CBM_C_CENTER_TOPIC b, CBM_C_ITEM c,CBM_C_CENTER d,CBM_C_ITEM_FININCE_ITEM e,CBM_C_FINANCE_ITEM f,CBM_C_ITEMTX h\n"
					+ " where a.center_topic_id = b.center_topic_id\n"
					+ "   and b.center_id=d.center_id\n"
					+ "   and a.item_id = c.item_id\n"
					+ "  and a.item_id=e.item_id(+) \n"
					+ " and e.finance_id=f.finance_id(+) \n"
					+ " and a.item_id=h.item_id and h.is_use='Y' \n"
					+ "   and b.topic_id = "
					+ topicId
					+ "\n"
					+ "   and d.dep_code='"
					+ deptCode
					+ "'\n"
					+ "    and a.is_use='Y' and b.is_use='Y'   and c.is_use='Y' and d.is_use='Y'  and e.is_use(+)='Y' and f.is_use(+)='Y' \n"
					+ " order by a.display_no asc";
			List list = bll.queryByNativeSQL(sql);
			Iterator it = list.iterator();
			while (it.hasNext()) {
				CbmJBudgetItemForm form = new CbmJBudgetItemForm();
				form.setTopicId(topicId);
				form.setBudgetTime(time);
				Object[] data = (Object[]) it.next();
				if (data[0] != null) {
					form.setItemId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					form.setItemAlias(data[1].toString());
				}
				if (data[2] != null) {
					form.setUnitId(Long.parseLong(data[2].toString()));
				}
				if (data[3] != null) {
					form.setUnitName(data[3].toString());
				}
				if (data[4] != null) {
					form.setCenterId(Long.parseLong(data[4].toString()));
				}
				if (data[5] != null) {
					form.setFinanceItem(data[5].toString());
				}
				if (data[6] != null) {
					form.setDataSource(data[6].toString());
				}
				if(data[7]!=null)
				{
					//add by fyyang 20100612
					form.setItemCode(data[7].toString());
				}
				resultList.add(form);

			}

		} else {
			String sql = //"select distinct * from (" +  //add by fyyang 20100610
					" select t.budget_make_id,r.budget_item_id,\n"
					+ "       r.center_item_id as item_id,\n"
					+ "       s.item_alias,\n"
					+ "       r.advice_budget,\n"
					+ "       r.budget_basis,\n"
					+ "       u.unit_code,\n"
					+ "       GETUNITNAME(u.unit_code),\n"
					+ "       t.center_id, t.make_status, t.work_flow_no, f.finance_item,s.data_source,h.zbbmtx_code\n"
					+ "  from CBM_J_BUDGET_MAKE t,\n"
					+ "       CBM_J_BUDGET_ITEM r,\n"
					+ "       CBM_C_CENTER_ITEM s,\n"
					+ "       CBM_C_ITEM        u,\n"
					+ "       CBM_C_CENTER d,CBM_C_ITEMTX h\n"
					+ " ,CBM_C_ITEM_FININCE_ITEM e,CBM_C_FINANCE_ITEM f, CBM_C_CENTER_TOPIC  g \n"
					+ " where t.budget_make_id = r.budget_make_id\n"
					+ "   and r.center_item_id = s.center_item_id(+)\n"
					+ "   and s.item_id = u.item_id(+)\n"
					+ "   and t.center_id=d.center_id\n"
					+ "   and s.item_id=e.item_id(+) \n"
					+ " and t.center_id=g.center_id(+) \n"
					+ "  and t.topic_id=g.topic_id(+) \n"
					+ "  and  g.center_topic_id=s.center_topic_id  \n"
					+"and s.item_id=h.item_id  and h.is_use='Y' \n"
					+ " and r.is_use='Y' and t.is_use='Y' and e.finance_id=f.finance_id(+) \n"
					+ "     and t.topic_id = "
					+ topicId
					+ "\n"
					+ "   and d.dep_code='"
					+ deptCode
					+ "'\n"
					+ "   and t.budget_time = '"
					+ time
					+ "'\n"
					//+ "   and s.is_use(+)='Y'\n"//modify by fyyang 20100610
					+ "   and u.is_use(+)='Y'\n"
					+ "   and d.is_use='Y'   and e.is_use(+)='Y' and f.is_use(+)='Y' \n"
					+ " order by s.display_no asc";
			List list = bll.queryByNativeSQL(sql);
			Iterator it = list.iterator();
			while (it.hasNext()) {
				CbmJBudgetItemForm form = new CbmJBudgetItemForm();

				form.setTopicId(topicId);
				form.setBudgetTime(time);
				Object[] data = (Object[]) it.next();
				if (data[0] != null) {
					form.setBudgetMakeId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					form.setBudgetItemId(Long.parseLong(data[1].toString()));
				}
				if (data[2] != null) {
					form.setItemId(Long.parseLong(data[2].toString()));
				}
				if (data[3] != null) {
					form.setItemAlias(data[3].toString());
				}
				if (data[4] != null) {
					form
							.setAdviceBudget(Double.parseDouble(data[4]
									.toString()));
				}
				if (data[5] != null) {
					form.setBudgetBasis(data[5].toString());
				}
				if (data[6] != null) {
					form.setUnitId(Long.parseLong(data[6].toString()));
				}
				if (data[7] != null) {
					form.setUnitName(data[7].toString());
				}
				if (data[8] != null) {
					form.setCenterId(Long.parseLong(data[8].toString()));
				}
				if (data[9] != null) {
					form.setMakeStatus(data[9].toString());
				}
				if (data[10] != null) {
					form.setWorkFlowNo(Long.parseLong(data[10].toString()));
				}
				if (data[11] != null) {
					form.setFinanceItem(data[11].toString());
				}
				if (data[12] != null) {
					form.setDataSource(data[12].toString());
				}
				if(data[13]!=null)
				{
					//add by fyyang 20100612
					form.setItemCode(data[13].toString());
				}
				resultList.add(form);

			}

		}

		return resultList;

	}

	@SuppressWarnings("unchecked")
	public List<CbmJBudgetItemForm> findMakeItemByMakeId(Long budgetMakeId) {
		List<CbmJBudgetItemForm> resultList = new ArrayList<CbmJBudgetItemForm>();
		String sql = "select t.budget_make_id,r.budget_item_id,\n"
				+ "       r.item_id,\n"
				+ "       s.item_alias,\n"
				+ "       r.advice_budget,\n"
				+ "       r.budget_basis,\n"
				+ "       u.unit_code,\n"
				+ "       GETUNITNAME(u.unit_code),\n"
				+ "       t.center_id, t.make_status, t.work_flow_no, f.finance_item,s.data_source,t.topic_id,t.budget_time\n"
				+ "  from CBM_J_BUDGET_MAKE t,\n"
				+ "       CBM_J_BUDGET_ITEM r,\n"
				+ "       CBM_C_CENTER_ITEM s,\n"
				+ "       CBM_C_ITEM        u,\n"
				+ " CBM_C_ITEM_FININCE_ITEM e,CBM_C_FINANCE_ITEM f, CBM_C_CENTER_TOPIC  g\n"
				+ " where t.budget_make_id = r.budget_make_id\n"
				+ "   and r.item_id = s.item_id(+)\n"
				+ "   and r.item_id = u.item_id(+)\n"
				+ "   and r.item_id=e.item_id(+)\n"
				+ " and t.center_id=g.center_id(+)\n"
				+ "  and t.topic_id=g.topic_id(+)\n"
				+ "  and  g.center_topic_id=s.center_topic_id  and e.finance_id=f.finance_id(+)\n"
				+ "   and t.budget_make_id=" + budgetMakeId + "\n"
				+ "   and s.is_use(+)='Y'\n" + "   and u.is_use(+)='Y'\n"
				+ "      and e.is_use(+)='Y' and f.is_use(+)='Y'\n"
				+ " order by s.display_no(+) asc ";
		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		while (it.hasNext()) {
			CbmJBudgetItemForm form = new CbmJBudgetItemForm();

			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				form.setBudgetMakeId(Long.parseLong(data[0].toString()));
			}
			if (data[1] != null) {
				form.setBudgetItemId(Long.parseLong(data[1].toString()));
			}
			if (data[2] != null) {
				form.setItemId(Long.parseLong(data[2].toString()));
			}
			if (data[3] != null) {
				form.setItemAlias(data[3].toString());
			}
			if (data[4] != null) {
				form.setAdviceBudget(Double.parseDouble(data[4].toString()));
			}
			if (data[5] != null) {
				form.setBudgetBasis(data[5].toString());
			}
			if (data[6] != null) {
				form.setUnitId(Long.parseLong(data[6].toString()));
			}
			if (data[7] != null) {
				form.setUnitName(data[7].toString());
			}
			if (data[8] != null) {
				form.setCenterId(Long.parseLong(data[8].toString()));
			}
			if (data[9] != null) {
				form.setMakeStatus(data[9].toString());
			}
			if (data[10] != null) {
				form.setWorkFlowNo(Long.parseLong(data[10].toString()));
			}
			if (data[11] != null) {
				form.setFinanceItem(data[11].toString());
			}
			if (data[12] != null) {
				form.setDataSource(data[12].toString());
			}
			if (data[13] != null) {
				form.setTopicId(Long.parseLong(data[13].toString()));
			}
			if (data[14] != null) {
				form.setBudgetTime(data[14].toString());
			}
			resultList.add(form);

		}
		return resultList;
	}

	// 预算编制单审批列表查询
	@SuppressWarnings("unchecked")
	public PageObject findMakeApproveList(String topicId, String centerId,
			String entryIds, int... rowStartIdxAndCount) {
		String sqlWhere = "";
		String sqlCount = "select count(1)\n"
				+ "  from CBM_J_BUDGET_MAKE t, CBM_C_TOPIC c, CBM_C_CENTER b\n"
				+ " where t.center_id = b.center_id(+)\n"
				+ "   and t.topic_id = c.topic_id(+)\n"
				+ "   and b.is_use(+) = 'Y'\n" + "   and c.is_use(+) = 'Y' \n"
				+ "   and t.work_flow_no in (" + entryIds + ") \n";
		if (topicId != null && !topicId.equals("")) {
			sqlWhere = "  and t.topic_id=" + topicId + " \n";
		}

		if (centerId != null && !centerId.equals("")) {
			sqlWhere += "  and t.center_id=" + centerId + " \n";
		}
		sqlCount += sqlWhere;
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		if (totalCount > 0) {
			PageObject obj = new PageObject();
			obj.setTotalCount(totalCount);
			String sql = "select t.budget_make_id,\n"
					+ "       t.center_id,\n"
					+ "       t.topic_id,\n"
					+ "       t.budget_time,\n"
					+ "       t.make_by,\n"
					+ "     to_char(t.make_date,'yyyy-MM-dd'),\n"
					+ "       t.work_flow_no,\n"
					+ "       t.make_status,\n"
					+ "       GETWORKERNAME(t.make_by),\n"
					+ "       c.topic_name,\n"
					+ "       b.dep_name\n"
					+ "  from CBM_J_BUDGET_MAKE t, CBM_C_TOPIC c, CBM_C_CENTER b\n"
					+ " where t.center_id = b.center_id(+)\n"
					+ "   and t.topic_id = c.topic_id(+)\n"
					+ "   and b.is_use(+) = 'Y'\n"
					+ "   and c.is_use(+) = 'Y'\n"
					+ "   and t.work_flow_no in (" + entryIds + ") \n";
			sql += sqlWhere;
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			Iterator it = list.iterator();
			List<CbmJBudgetItemForm> objList = new ArrayList<CbmJBudgetItemForm>();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				CbmJBudgetItemForm model = new CbmJBudgetItemForm();
				if (data[0] != null) {
					model.setBudgetMakeId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setCenterId(Long.parseLong(data[1].toString()));
				}
				if (data[2] != null) {
					model.setTopicId(Long.parseLong(data[2].toString()));
				}
				if (data[3] != null) {
					model.setBudgetTime(data[3].toString());
				}
				if (data[4] != null) {
					model.setMakeBy(data[4].toString());
				}
				if (data[5] != null) {
					model.setMakeDate(data[5].toString());
				}
				if (data[6] != null) {
					model.setWorkFlowNo(Long.parseLong(data[6].toString()));
				}
				if (data[7] != null) {
					model.setMakeStatus(data[7].toString());
				}
				if (data[8] != null) {
					model.setMakeByName(data[8].toString());
				}
				if (data[9] != null) {
					model.setTopicName(data[9].toString());
				}
				if (data[10] != null) {
					model.setCenterName(data[10].toString());
				}
				objList.add(model);

			}
			obj.setList(objList);
			return obj;
		}

		return null;
	}

	/**
	 * 上报预算编制
	 */
	public void reportTo(Long budgetMakeId, String workflowType,
			String workerCode, Long actionId, String approveText,
			String nextRoles, String eventIdentify) {
		CbmJBudgetMake entity = findById(budgetMakeId);
		if (entity.getWorkFlowNo() == null) {
			long entryId = service.doInitialize(workflowType, workerCode,
					budgetMakeId + "");
			service.doAction(entryId, workerCode, actionId, approveText, null,
					nextRoles);
			entity.setWorkFlowNo(entryId);
		} else {
			this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId,
					approveText, nextRoles);
		}
		entity.setMakeStatus("1");
		this.update(entity);
	}

	public void MakeCommSign(Long budgetMakeId, String approveText,
			String workerCode, Long actionId, String responseDate,
			String nextRoles, String eventIdentify) {
		CbmJBudgetMake entity = findById(budgetMakeId);
		if (eventIdentify.equals("ZJ")) {
			entity.setMakeStatus("2");
		}
		if (eventIdentify.equals("TH")) {
			entity.setMakeStatus("3");
		}
		this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId,
				approveText, nextRoles);
	}

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

	/**
	 * 通过部门，主题，时间获得一个实例的id列表 add by liuyi 090820
	 * 
	 * @param centerId
	 *            部门id
	 * @param topicId
	 *            主题id
	 * @param budgetTime
	 *            时间
	 * @return
	 */
	public List<Long> getInstance(Long centerId, Long topicId,
			String budgetTime, String enterpriseCode) {
		List<Long> arrlist = new ArrayList<Long>();
		String sql = "select a.budget_make_id \n"
				+ "from CBM_J_BUDGET_MAKE a \n" + "where a.center_id="
				+ centerId + " \n" + "and a.topic_id=" + topicId + " \n"
				+ "and a.budget_time='" + budgetTime + "' \n"
				+ "and a.enterprise_code='" + enterpriseCode + "' \n";
		List list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				arrlist.add(Long.parseLong(it.next().toString()));
			}
		}
		return arrlist;
	}

	/**
	 * 根据部门和预算部门主题查找部门预算指标信息 by add ltong 20100504
	 */
	@SuppressWarnings("unchecked")
	public List<CbmJBudgetItemForm> findDeptTopicItemList(String centerTopicId,
			String centerId, String budgetTime, String enterpriseCode) {
		List<CbmJBudgetItemForm> resultList = new ArrayList<CbmJBudgetItemForm>();

		String sqlCount = "select count(*)\n"
				+ "  from CBM_J_BUDGET_MAKE a, CBM_J_BUDGET_ITEM b, CBM_C_CENTER c\n"
				+ " where a.center_id = '"
				+ centerId
				+ "'\n"
				+ "   and a.center_id = c.center_id\n"
				+ "   and a.topic_id =\n"
				+ "       (select t.topic_id from CBM_C_TOPIC t where t.topic_code = '705')\n"
				+ "   and a.budget_time = '" + budgetTime + "'\n"
				+ "   and a.budget_make_id = b.budget_make_id\n"
				+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and b.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and c.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and c.is_use = 'Y'";

		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		if (totalCount == 0) {
			String sql = "select a.center_item_id,\n"
					+ "       a.center_topic_id,\n"
					+ "       a.item_id,\n"
					+ "       a.item_alias,\n"
					+ "       a.data_source,\n"
					+ "       a.display_no,\n"
					+ "       a.is_use,\n"
					+ "       a.enterprise_code,\n"
					+ "       b.item_code,\n"
					+ "       b.item_name,\n"
					+ "       c.center_id,\n"
					+ "       c.topic_id,\n"
					+ "       (select d.finance_name\n"
					+ "          from cbm_c_finance_item d\n"
					+ "         where d.finance_id = (select c.finance_id\n"
					+ "                                 from cbm_c_item_finince_item c\n"
					+ "                                where c.item_id = b.item_id\n"
					+ "                                  and c.is_use = 'Y')\n"
					+ "           and d.is_use = 'Y') financeName\n"
					+ "\n"
					+ "  from CBM_C_CENTER_ITEM a, CBM_C_ITEM b, CBM_C_CENTER_TOPIC c\n"
					+ " where a.center_topic_id = c.center_topic_id\n"
					+ "   and a.item_id = b.item_id\n"
					+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
					+ "   and c.is_use = 'Y'\n"
					+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and b.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and b.enterprise_code = '" + enterpriseCode + "'";
			if (centerTopicId != null && !(centerTopicId.equals(""))) {
				sql = sql + "and a.center_topic_id='" + centerTopicId + "' \n";
			}
			sql = sql + "order by a.display_no \n";
			List list = bll.queryByNativeSQL(sql);
			Iterator it = list.iterator();
			while (it.hasNext()) {
				CbmJBudgetItemForm form = new CbmJBudgetItemForm();
				Object[] data = (Object[]) it.next();
				if (data[0] != null) {
					form.setCenterItemId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					form.setCenterTopicId(Long.parseLong(data[1].toString()));
				}
				if (data[2] != null) {
					form.setItemId(Long.parseLong(data[2].toString()));
				}
				if (data[3] != null) {
					form.setItemAlias(data[3].toString());
				}
				if (data[5] != null) {
					form.setDispalyNo(data[5].toString());
				}
				if (data[8] != null) {
					form.setItemCode(data[8].toString());
				}
				if (data[9] != null) {
					form.setItemName(data[9].toString());
				}
				if (data[10] != null) {
					form.setCenterId(Long.parseLong(data[10].toString()));
				}
				if (data[11] != null) {
					form.setTopicId(Long.parseLong(data[11].toString()));
				}
				if (data[12] != null) {
					form.setFinanceName(data[12].toString());
				}
				resultList.add(form);
			}
		} else {
			String sql = "select b.item_id,\n" + "       b.finance_happen,\n"
					+ "       b.budget_item_id,\n" + " b.budget_make_id,\n"
					+ "       (select t.item_code\n"
					+ "          from cbm_c_item t\n"
					+ "         where t.item_id = b.item_id\n"
					+ "           and t.is_use = 'Y'\n"
					+ "           and t.enterprise_code = '"
					+ enterpriseCode
					+ "') itemCode,\n"
					+ "       (select t.item_alias\n"
					+ "          from cbm_c_center_item t\n"
					+ "         where t.item_id = b.item_id\n"
					+ "           and t.is_use = 'Y'\n"
					+ "           and t.enterprise_code = '"
					+ enterpriseCode
					+ "'\n"
					+ "           and t.center_topic_id =\n"
					+ "               (select t.center_topic_id\n"
					+ "                  from cbm_c_center_topic t\n"
					+ "                 where t.center_id = a.center_id\n"
					+ "                   and t.topic_id = a.topic_id\n"
					+ "                   and t.is_use = 'Y'\n"
					+ "                   and t.enterprise_code = '"
					+ enterpriseCode
					+ "')) itemAlias,\n"
					+ "       (select t.finance_name\n"
					+ "          from cbm_c_finance_item t\n"
					+ "         where t.finance_id =\n"
					+ "               (select d.finance_id\n"
					+ "                  from cbm_c_item_finince_item d\n"
					+ "                 where d.item_id = b.item_id\n"
					+ "                   and d.is_use = 'Y'\n"
					+ "                   and d.enterprise_code = '"
					+ enterpriseCode
					+ "')\n"
					+ "           and t.is_use = 'Y'\n"
					+ "           and t.enterprise_code = '"
					+ enterpriseCode
					+ "') financeName\n"
					+ "  from CBM_J_BUDGET_MAKE a, CBM_J_BUDGET_ITEM b, CBM_C_CENTER c\n"
					+ " where a.center_id = '"
					+ centerId
					+ "'\n"
					+ "   and a.center_id = c.center_id\n"
					+ "   and a.topic_id =\n"
					+ "       (select t.topic_id from CBM_C_TOPIC t where t.topic_code = '705')\n"
					+ "   and a.budget_time = '"
					+ budgetTime
					+ "'\n"
					+ "   and a.budget_make_id = b.budget_make_id\n"
					+ "   and a.enterprise_code = '"
					+ enterpriseCode
					+ "'\n"
					+ "   and b.enterprise_code = '"
					+ enterpriseCode
					+ "'\n"
					+ "   and c.enterprise_code = '"
					+ enterpriseCode
					+ "'\n"
					+ "   and c.is_use = 'Y'";

			List list = bll.queryByNativeSQL(sql);
			Iterator it = list.iterator();
			while (it.hasNext()) {
				CbmJBudgetItemForm form = new CbmJBudgetItemForm();
				Object[] data = (Object[]) it.next();
				if (data[0] != null) {
					form.setItemId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					form.setFinanceHappen(Double
							.parseDouble(data[1].toString()));
				}
				if (data[2] != null) {
					form.setBudgetItemId(Long.parseLong(data[2].toString()));
				}
				if (data[3] != null) {
					form.setBudgetMakeId(Long.parseLong(data[3].toString()));
				}
				if (data[4] != null) {
					form.setItemCode(data[4].toString());
				}
				if (data[5] != null) {
					form.setItemAlias(data[5].toString());
				}
				if (data[6] != null) {
					form.setFinanceName(data[6].toString());
				}
				resultList.add(form);
			}
		}
		return resultList;

	}
	
	@SuppressWarnings("unchecked")
	public PageObject findBudgetBasisList(String time,String deptCode, Long topicId) {
		PageObject result=new PageObject();
			String sql  = 
				"select o.budget_make_id,\n" +
				"       o.budget_item_id,\n" + 
				"       o.item_id,\n" + 
				"       o.item_alias,\n" + 
				"       p.budget_basis,\n" + 
				"       p.budget_amount,\n" + 
				"       o.unit_code,\n" + 
				"       GETUNITNAME(o.unit_code),\n" + 
				"       o.center_id,\n" + 
				"       o.make_status,\n" + 
				"       o.work_flow_no,\n" + 
				"       o.finance_item,\n" + 
				"       o.data_source,\n" + 
				"       o.zbbmtx_code,\n" + 
				"       o.budget_gather_id,\n" + 
				"       o.advice_budget\n" + 
				"  from (select t.budget_make_id,\n" + 
				"               r.budget_item_id,\n" + 
				"               r.item_id,\n" + 
				"               s.item_alias,\n" + 
				"               u.unit_code,\n" + 
				"               GETUNITNAME(u.unit_code),\n" + 
				"               t.center_id,\n" + 
				"               t.make_status,\n" + 
				"               t.work_flow_no,\n" + 
				"               f.finance_item,\n" + 
				"               s.data_source,\n" + 
				"               h.zbbmtx_code,\n" + 
				"               t.budget_gather_id,\n" + 
				"               r.advice_budget\n" + 
				"          from CBM_J_BUDGET_MAKE       t,\n" + 
				"               CBM_J_BUDGET_ITEM       r,\n" + 
				"               CBM_C_CENTER_ITEM       s,\n" + 
				"               CBM_C_ITEM              u,\n" + 
				"               CBM_C_CENTER            d,\n" + 
				"               CBM_C_ITEMTX            h,\n" + 
				"               CBM_C_ITEM_FININCE_ITEM e,\n" + 
				"               CBM_C_FINANCE_ITEM      f,\n" + 
				"               CBM_C_CENTER_TOPIC      g\n" + 
				"         where t.budget_make_id = r.budget_make_id\n" + 
				"           and r.item_id = s.item_id(+)\n" + 
				"           and r.item_id = u.item_id(+)\n" + 
				"           and t.center_id = d.center_id\n" + 
				"           and r.item_id = e.item_id(+)\n" + 
				"           and t.center_id = g.center_id(+)\n" + 
				"           and t.topic_id = g.topic_id(+)\n" + 
				"           and g.center_topic_id = s.center_topic_id\n" + 
				"           and r.item_id = h.item_id\n" + 
				"           and h.is_use = 'Y'\n" + 
				"           and e.finance_id = f.finance_id(+)\n" + 
				"           and t.topic_id = "+topicId+"\n" + 
				"           and d.dep_code = '"+deptCode+"'\n" + 
				"           and t.budget_time = '"+time+"'\n" + 
				"           and u.is_use(+) = 'Y'\n" + 
				"           and d.is_use = 'Y'\n" + 
				"           and e.is_use(+) = 'Y'\n" + 
				"           and f.is_use(+) = 'Y'\n" + 
				"         order by h.zbbmtx_code asc) o,\n" + 
				"       CBM_C_BASIS p\n" + 
				" where o.budget_item_id = p.budget_item_id(+)\n" + 
				"   and p.is_use(+) = 'Y'\n" + 
				"   and o.make_status <> 0\n" + 
				" order by o.budget_item_id, o.item_alias";
			
			List list = bll.queryByNativeSQL(sql);
			result.setList(list);
			return result;
	}
}