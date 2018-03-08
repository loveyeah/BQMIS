package power.ejb.manage.project;

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

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.contract.business.ConJBalanceFacadeRemote;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.manage.contract.form.PrjQualityForm;

/**
 * Facade for entity PrjJInfo.
 * 
 * @see power.ejb.manage.project.PrjJInfo
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PrjJInfoFacade implements PrjJInfoFacadeRemote {
	// property constants
	public static final String PRJ_NO = "prjNo";
	public static final String PRJ_NAME = "prjName";
	public static final String PRJ_YEAR = "prjYear";
	public static final String PRJ_TYPE_ID = "prjTypeId";
	public static final String PRJ_CONTENT = "prjContent";
	public static final String CONSTRUCTION_UNIT = "constructionUnit";
	public static final String PRJ_STATUS = "prjStatus";
	public static final String ITEM_ID = "itemId";
	public static final String PLAN_AMOUNT = "planAmount";
	public static final String CHARGE_BY = "chargeBy";
	public static final String CHARGE_DEP = "chargeDep";
	public static final String PLAN_TIME_LIMIT = "planTimeLimit";
	public static final String FACT_TIME_LIMIT = "factTimeLimit";
	public static final String ANNEX = "annex";
	public static final String MEMO = "memo";
	public static final String ENTRY_BY = "entryBy";
	public static final String CML_APPRAISAL = "cmlAppraisal";
	public static final String PRO_WORK_FLOW_NO = "proWorkFlowNo";
	public static final String ACC_WORK_FLOW_NO = "accWorkFlowNo";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	protected ConJBalanceFacadeRemote cbremote;

	WorkflowService service;

	public PrjJInfoFacade() {
		service = new WorkflowServiceImpl();
		cbremote = (ConJBalanceFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("ConJBalanceFacade");
	}

	/**
	 * Perform an initial save of a previously unsaved PrjJInfo entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PrjJInfo entity to persist
	 * @return
	 * @throws CodeRepeatException
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PrjJInfo save(PrjJInfo entity) throws CodeRepeatException {
		try {
			if (checkPrjName(entity.getPrjName())) {
				throw new CodeRepeatException("该项目名称已经存在!,请重新输入!");
			}
			if (checkPrjNoShow(entity.getPrjNoShow())) {
				throw new CodeRepeatException("该项目编号已经存在!,请重新输入!");
			} else {
				entity.setPrjNoShow(this.getPrjNoShow(entity
						.getEnterpriseCode()));
				Long id = bll.getMaxId("PRJ_J_INFO", "id");
				entity.setId(id);
				entity.setPrjNo(getPrjNo(id));
				entity.setPrjStatus(Long.parseLong("0"));
				entity.setIsUse("Y");
				entityManager.persist(entity);
				return entity;
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PrjJInfo entity.
	 * 
	 * @param entity
	 *            PrjJInfo entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PrjJInfo entity) {
		LogUtil.log("deleting PrjJInfo instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PrjJInfo.class, entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PrjJInfo entity and return it or a copy of it
	 * to the sender. A copy of the PrjJInfo entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PrjJInfo entity to update
	 * @return PrjJInfo the persisted PrjJInfo entity instance, may not be the
	 *         same
	 * @throws CodeRepeatException
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PrjJInfo update(PrjJInfo entity) throws CodeRepeatException {
		try {
			if (checkPrjNameforUpdate(entity.getPrjName(), entity.getId())) {
				throw new CodeRepeatException("该项目名称已经存在!,请重新输入!");
			}
			if (checkPrjNoShowForUpdate(entity.getPrjNoShow(), entity.getId())) {
				throw new CodeRepeatException("该项目编号已经存在!,请重新输入!");
			}
			PrjJInfo result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PrjJInfo findById(Long id) {
		try {
			PrjJInfo instance = entityManager.find(PrjJInfo.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	// 根据类型选项目编号
	@SuppressWarnings("unchecked")
	public PageObject getProjectTypeByPrjNo(String typeChoose,
			String prjTypeId, String queryKey, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String strWhere = " where t.is_use='Y'"
				+ " and t.PRJ_STATUS in (4,5,6,7,31,32,34)";
		if (prjTypeId != null && !"".equals(prjTypeId)
				&& !prjTypeId.equals("0")) {
			strWhere += "   and t.PRJ_TYPE_ID in (select a.PRJ_TYPE_ID\n"
					+ "     from PRJ_C_TYPE a\n" + "  where a.is_use = 'Y'\n"
					+ "    start with a.PRJ_TYPE_ID =' " + prjTypeId + "' \n"
					+ "  connect by prior a.PRJ_TYPE_ID = a.PRJ_P_TYPE_ID)";
		}
		if (queryKey != null && queryKey.length() > 0) {
			strWhere += " and (t.PRJ_NO_SHOW like '%" + queryKey
					+ "%' or t.PRJ_NAME like '%" + queryKey
					+ "%' or getworkername(t.CHARGE_BY) like '%" + queryKey
					+ "%')";
		}
		if (typeChoose != null && typeChoose.equals("Choose")) {
			strWhere += "  and t.prj_no not in (select c.project_id from con_j_contract_info c where c.is_use ='Y' and c.project_id is not null ) ";
		}
		strWhere += " order by t.plan_start_date asc,t.id desc";
		String sqlCount = "select count(DISTINCT t.id) from PRJ_J_INFO t ";
		sqlCount += strWhere;
		String str = bll.getSingal(sqlCount).toString();
		Long count = Long.parseLong(str);
		// System.out.println(count);
		if (count > 0) {
			String sql = "select t.*,"
					+ "getworkername(t.CHARGE_BY) requiremanName"
					// + "getworkername(t.ENTRY_BY) entry "
					+ " from PRJ_J_INFO t ";
			sql += strWhere;
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List<PrjJInfoAdd> arraylist = new ArrayList<PrjJInfoAdd>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				PrjJInfoAdd model = new PrjJInfoAdd();
				PrjJInfo basemodel = new PrjJInfo();
				if (data[0] != null) {
					basemodel.setId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					basemodel.setPrjNo(data[1].toString());
				}
				if (data[2] != null) {
					basemodel.setPrjNoShow(data[2].toString());
				}
				if (data[3] != null) {
					basemodel.setPrjName(data[3].toString());
				}
				if (data[9] != null) {
					basemodel.setItemId(data[9].toString());
				}
				if (data[31] != null) {
					model.setChargeByName(data[31].toString());
				}
				model.setPrjjInfo(basemodel);
				arraylist.add(model);
			}
			pg.setList(arraylist);
			pg.setTotalCount(count);
			return pg;
		} else {
			return null;
		}

	}

	/**
	 * Find all PrjJInfo entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PrjJInfo property to query
	 * @param value
	 *            the property value to match
	 * @return List<PrjJInfo> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PrjJInfo> findByProperty(String propertyName, final Object value) {
		LogUtil.log("finding PrjJInfo instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PrjJInfo model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<PrjJInfo> findByPrjNo(Object prjNo) {
		return findByProperty(PRJ_NO, prjNo);
	}

	public List<PrjJInfo> findByPrjName(Object prjName) {
		return findByProperty(PRJ_NAME, prjName);
	}

	public List<PrjJInfo> findByPrjYear(Object prjYear) {
		return findByProperty(PRJ_YEAR, prjYear);
	}

	public List<PrjJInfo> findByPrjTypeId(Object prjTypeId) {
		return findByProperty(PRJ_TYPE_ID, prjTypeId);
	}

	public List<PrjJInfo> findByPrjContent(Object prjContent) {
		return findByProperty(PRJ_CONTENT, prjContent);
	}

	public List<PrjJInfo> findByConstructionUnit(Object constructionUnit) {
		return findByProperty(CONSTRUCTION_UNIT, constructionUnit);
	}

	public List<PrjJInfo> findByPrjStatus(Object prjStatus) {
		return findByProperty(PRJ_STATUS, prjStatus);
	}

	public List<PrjJInfo> findByItemId(Object itemId) {
		return findByProperty(ITEM_ID, itemId);
	}

	public List<PrjJInfo> findByPlanAmount(Object planAmount) {
		return findByProperty(PLAN_AMOUNT, planAmount);
	}

	public List<PrjJInfo> findByChargeBy(Object chargeBy) {
		return findByProperty(CHARGE_BY, chargeBy);
	}

	public List<PrjJInfo> findByChargeDep(Object chargeDep) {
		return findByProperty(CHARGE_DEP, chargeDep);
	}

	public List<PrjJInfo> findByPlanTimeLimit(Object planTimeLimit) {
		return findByProperty(PLAN_TIME_LIMIT, planTimeLimit);
	}

	public List<PrjJInfo> findByFactTimeLimit(Object factTimeLimit) {
		return findByProperty(FACT_TIME_LIMIT, factTimeLimit);
	}

	public List<PrjJInfo> findByAnnex(Object annex) {
		return findByProperty(ANNEX, annex);
	}

	public List<PrjJInfo> findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List<PrjJInfo> findByEntryBy(Object entryBy) {
		return findByProperty(ENTRY_BY, entryBy);
	}

	public List<PrjJInfo> findByCmlAppraisal(Object cmlAppraisal) {
		return findByProperty(CML_APPRAISAL, cmlAppraisal);
	}

	public List<PrjJInfo> findByProWorkFlowNo(Object proWorkFlowNo) {
		return findByProperty(PRO_WORK_FLOW_NO, proWorkFlowNo);
	}

	public List<PrjJInfo> findByAccWorkFlowNo(Object accWorkFlowNo) {
		return findByProperty(ACC_WORK_FLOW_NO, accWorkFlowNo);
	}

	public List<PrjJInfo> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<PrjJInfo> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all PrjJInfo entities.
	 * 
	 * @return List<PrjJInfo> all PrjJInfo entities
	 */

	// 根据不同参数查询项目列表
	@SuppressWarnings("unchecked")
	public PageObject FindByMoreCondition(String workFlowType,
			String entryType, String entryIds, String workCode, String prjNo,
			String prjYear, String prjTypeId, String prjStatus,
			String chargeDep, String argFuzzy, String prjStatusType,
			String timefromDate, String timetoDate, int... rowStartIdxAndCount) {
		try {
			PageObject pg = new PageObject();
			String strWhere = "where t.is_use='Y'";
			if (prjYear != null && !"".equals(prjYear)) {
				strWhere += " and t.PRJ_YEAR='" + prjYear + "'";
			}
			if (prjTypeId != null && !"".equals(prjTypeId)
					&& !"0".equals(prjTypeId)) {
				// 根据根节点查所有子节点
				strWhere += "   and t.PRJ_TYPE_ID in (select a.PRJ_TYPE_ID\n"
						+ "     from PRJ_C_TYPE a\n"
						+ "  where a.is_use = 'Y'\n"
						+ "    start with a.PRJ_TYPE_ID =' " + prjTypeId
						+ "' \n"
						+ "  connect by prior a.PRJ_TYPE_ID = a.PRJ_P_TYPE_ID)";

			}
			if (prjStatus != null && !"".equals(prjStatus)
					&& !"40".equals(prjStatus) && !"41".equals(prjStatus)
					&& !"42".equals(prjStatus) && !"43".equals(prjStatus)
					&& !"44".equals(prjStatus)) {
				strWhere += " and t.PRJ_STATUS in ('" + prjStatus + "')";
			}
			if (timefromDate != null && !"".equals(timefromDate)) {
				strWhere += " and t.acc_sign_start_date>to_date('"
						+ timefromDate
						+ "'||' 23:59:59','yyyy-MM-dd HH24:MI:SS')\n";
			}
			if (timetoDate != null && !"".equals(timetoDate)) {
				strWhere += " and t.acc_sign_start_date<to_date('" + timetoDate
						+ "'||' 23:59:59','yyyy-MM-dd HH24:MI:SS')\n";
			}

			if (prjStatusType != null && !"".equals(prjStatusType)) {
				strWhere += " and g.prj_status_type in ('" + prjStatusType
						+ "')";
			}
			if (chargeDep != null && !"".equals(chargeDep)
					&& !"0".equals(chargeDep)) {
				strWhere += " and t.CHARGE_DEP= (select h.dept_code from hr_c_dept h where h.dept_id='"
						+ chargeDep + "')";
			}
			if (argFuzzy != null && argFuzzy.length() > 0) {
				strWhere += " and (t.PRJ_NO_SHOW like '%" + argFuzzy
						+ "%' or t.PRJ_NAME like '%" + argFuzzy
						+ "%' or getworkername(t.ENTRY_BY) like '%" + argFuzzy
						+ "%' or getworkername(t.CHARGE_BY) like '%" + argFuzzy
						+ "%')";
			}
			if (prjNo != null) {
				strWhere += "and t.prj_no='" + prjNo + "'";
			}
			if (workCode != null && !workCode.equals("999999")
					&& !workCode.equals("") && entryType == null) {
				strWhere += "and t.entry_by='" + workCode + "'";
			}
			if (entryType != null && workFlowType != null
					&& entryType.equals("approve")
					&& !workCode.equals("999999")) {
				if (workFlowType.equals("hfProject1"))
					strWhere += "and t.ACC_WORK_FLOW_NO in (" + entryIds + ")";
				if (workFlowType.equals("hfPCheck"))
					strWhere += " and t.pro_work_flow_no in (" + entryIds + ")";
			}
			strWhere += " and t.PRJ_TYPE_ID=s.PRJ_TYPE_ID "
					+ " and t.prj_status = g.prj_status_id ";

			strWhere += " order by t.entry_date desc,t.id desc";
			String sqlCount = "select count(DISTINCT t.id) from PRJ_J_INFO t ,PRJ_C_TYPE s,prj_c_status g, "
					+ " con_j_clients_info m \n";
			sqlCount += strWhere;
			String str = bll.getSingal(sqlCount).toString();
			Long count = Long.parseLong(str);
			// System.out.println(count);
			if (count > 0) {
				String sql = "select t.*,"
						+ "getworkername(t.CHARGE_BY) requiremanName,"
						+ "s.PRJ_TYPE_NAME,"
						+ " getworkername(t.entry_by) enterByName,"
						+ " getdeptname(t.charge_dep) changeDepName,"
						+ " to_char(t.plan_start_date,'yyyy-MM-dd') planStartDate,"
						+ " to_char(t.plan_end_date,'yyyy-MM-dd') planEndDate,"
						+ " to_char(t.entry_date,'yyyy-MM-dd HH24:MI') entryDate,"
						+ " g.prj_status_name,g.prj_status_id,g.prj_status_type,"
						+ " to_char(t.fact_start_date,'yyyy-MM-dd') factStartDate,"
						+ " to_char(t.fact_end_date,'yyyy-MM-dd') factEndDate, "
						+ " ( select m.client_name from  con_j_clients_info m where t.construction_unit = m.cliend_id) constructionUnitName "

						// + "getworkername(t.ENTRY_BY) entry "
						+ " from PRJ_J_INFO t,PRJ_C_TYPE s ,prj_c_status g ";
				sql += strWhere;
				List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
				List<PrjJInfoAdd> arraylist = new ArrayList<PrjJInfoAdd>();
				Iterator it = list.iterator();
				while (it.hasNext()) {

					Object[] data = (Object[]) it.next();
					PrjJInfoAdd model = new PrjJInfoAdd();
					PrjJInfo basemodel = new PrjJInfo();

					if (data[0] != null) {
						basemodel.setId(Long.parseLong(data[0].toString()));
					}

					if (data[1] != null) {
						basemodel.setPrjNo(data[1].toString());
					}
					if (data[2] != null) {
						basemodel.setPrjNoShow(data[2].toString());
					}

					if (data[3] != null) {
						basemodel.setPrjName(data[3].toString());
					}

					if (data[4] != null) {
						basemodel
								.setPrjYear(Long.parseLong(data[4].toString()));
					}

					if (data[5] != null) {
						basemodel.setPrjTypeId(Long.parseLong(data[5]
								.toString()));
					}
					if (data[6] != null) {
						basemodel.setPrjContent(data[6].toString());
					}
					if (data[7] != null) {
						basemodel.setConstructionUnit(data[7].toString());
					}
					if (data[8] != null) {
						basemodel.setPrjStatus(Long.parseLong(data[8]
								.toString()));
					}
					if (data[9] != null) {
						basemodel.setItemId(data[9].toString());
					}
					if (data[10] != null) {
						basemodel.setPlanAmount(Double.parseDouble(data[10]
								.toString()));
					}
					if (data[11] != null) {
						basemodel.setChargeBy(data[11].toString());
					}
					if (data[12] != null) {
						basemodel.setChargeDep(data[12].toString());
					}
					if (data[13] != null) {
						basemodel.setPlanStartDate((Date) data[13]);
					}
					if (data[14] != null) {
						basemodel.setPlanEndDate((Date) data[14]);
					}
					if (data[15] != null) {
						basemodel.setPlanTimeLimit(Long.parseLong(data[15]
								.toString()));
					}
					if (data[18] != null) {
						basemodel.setFactTimeLimit(Long.parseLong(data[18]
								.toString()));
					}
					if (data[19] != null) {
						basemodel.setAnnex(data[19].toString());
					}
					if (data[20] != null) {
						basemodel.setMemo(data[20].toString());
					}
					if (data[21] != null) {
						basemodel.setEntryBy(data[21].toString());
					}
					if (data[22] != null) {
						basemodel.setEntryDate((Date) data[22]);
					}
					if (data[23] != null) {
						basemodel.setCmlAppraisal(data[23].toString());
					}
					if (data[24] != null) {
						basemodel.setProWorkFlowNo(Long.parseLong(data[24]
								.toString()));
					}
					if (data[25] != null) {
						basemodel.setAccWorkFlowNo(Long.parseLong(data[25]
								.toString()));
					}
					if (data[30] != null) {
						basemodel.setConstructionChargeBy(data[30].toString());
					}
					if (data[31] != null) {
						basemodel.setPrjChangeMemo(data[31].toString());
					}
					if (data[32] != null) {
						basemodel.setPrjDataMove(data[32].toString());
					}
					if (data[33] != null) {
						model.setChargeByName(data[33].toString());
					}
					if (data[34] != null) {
						model.setPrjTypeName(data[34].toString());
					}
					if (data[35] != null) {
						model.setEnterByName(data[35].toString());
					}
					if (data[36] != null) {
						model.setChangeDepName(data[36].toString());
					}
					if (data[37] != null) {
						model.setPlanStartDate(data[37].toString());
					}
					if (data[38] != null) {
						model.setPlanEndDate(data[38].toString());
					}
					if (data[39] != null) {
						model.setEntryDate(data[39].toString());
					}
					if (data[40] != null) {
						model.setPrjStatus(data[40].toString());
					}
					if (data[41] != null) {
						model.setPrjStatusId(Long
								.parseLong(data[41].toString()));
					}
					if (data[42] != null) {
						model.setPrjStatusType(data[42].toString());
					}
					if (data[43] != null) {
						model.setFactStartDate(data[43].toString());
					}
					if (data[44] != null) {
						model.setFactEndDate(data[44].toString());
					}
					if (data[45] != null) {
						model.setConstructionUnitName(data[45].toString());
					}

					model.setPrjjInfo(basemodel);
					arraylist.add(model);
				}
				pg.setList(arraylist);
				pg.setTotalCount(count);
				return pg;
			} else {
				return null;
			}
		} catch (RuntimeException re) {
			LogUtil.log("FindByMoreCondition failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PrjJInfo> findAll() {
		LogUtil.log("finding all PrjJInfo instances", Level.INFO, null);
		try {
			final String queryString = "select model from PrjJInfo model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	// 项目编号生成
	private String getPrjNo(Long Prjid) {
		String issueNo = "HWPRJ";
		String id = String.valueOf(Prjid);
		java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat(
				"yyyyMMdd");
		if (id.length() < 2) {
			issueNo += tempDate.format(new java.util.Date()) + "0" + id;
		} else {
			issueNo += tempDate.format(new java.util.Date()) + id;
		}
		return issueNo;
	}

	// 项目名称同名修改判断
	@SuppressWarnings("unchecked")
	private boolean checkPrjNameforUpdate(String prjName, Long id) {
		String sql = "select count(1) from prj_j_info t where t.prj_name=? "
				+ " and t.is_use='Y'" + " and t.id != ?";
		int size = Integer.parseInt(bll.getSingal(sql,
				new Object[] { prjName, id }).toString());
		if (size > 0) {
			return true;
		}
		return false;
	}

	// 项目编号显示同名修改判断
	@SuppressWarnings("unchecked")
	private boolean checkPrjNoShowForUpdate(String prjNoShow, Long id) {
		String sql = "select count(*) from prj_j_info t where t.prj_no_show=? "
				+ " and t.is_use='Y'" + " and t.id != ?";
		int size = Integer.parseInt(bll.getSingal(sql,
				new Object[] { prjNoShow, id }).toString());
		if (size > 0) {
			return true;
		}
		return false;
	}

	// 项目名称同名判断
	@SuppressWarnings("unchecked")
	private boolean checkPrjName(String prjName) {
		String sql = "select count(1) from prj_j_info t where t.prj_name=? "
				+ " and t.is_use='Y'";
		int size = Integer.parseInt(bll
				.getSingal(sql, new Object[] { prjName }).toString());
		if (size > 0) {
			return true;
		}
		return false;
	}

	// 项目编号显示同名判断
	@SuppressWarnings("unchecked")
	private boolean checkPrjNoShow(String prjNoShow) {
		String sql = "select count(1) from prj_j_info t where t.prj_no_show=? "
				+ " and t.is_use='Y'";
		int size = Integer.parseInt(bll.getSingal(sql,
				new Object[] { prjNoShow }).toString());
		if (size > 0) {
			return true;
		}
		return false;
	}

	// 通过项目编号查找项目
	@SuppressWarnings("unchecked")
	public PrjJInfo findByPrjNo(String prjNo, String enterpriseCode) {
		String sql = "select t.* from prj_j_info t where t.prj_no ='" + prjNo
				+ "' and t.enterprise_code='" + enterpriseCode + "'";
		List<PrjJInfo> list = bll.queryByNativeSQL(sql, PrjJInfo.class);
		if (list != null && list.size() > 0) {
			// modify by fyyang 090618
			PrjJInfo entity = list.get(0);
			return entity;
		} else {
			return null;
		}

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

	// 审批步骤：立项 上报
	// 对应信息：申请页面点“上报”→1
	public String reportTo(String prjNo, String workflowType,
			String workerCode, String actionId, String approveText,
			String nextRoles, String enterpriseCode) {
		try {
			PrjJInfo entity = findByPrjNo(prjNo, enterpriseCode);
			if (entity.getAccWorkFlowNo() == null) {
				// 处理未上报
				if (!workflowType.equals("")) {
					long entryId = service.doInitialize(workflowType,
							workerCode, prjNo);
					long actionIdl = Long.parseLong(actionId);
					service.doAction(entryId, workerCode, actionIdl,
							approveText, null, nextRoles);
					entity.setAccWorkFlowNo(entryId);
					entity.setPrjStatus(1l);
				}
			} else {
				// 处理已退回
				// long entryId = service.doInitialize(workflowType, workerCode,
				// entity.getPrjNo());
				long entryId = entity.getAccWorkFlowNo();
				long actionIdl = Long.parseLong(actionId);
				service.doAction(entryId, workerCode, actionIdl, approveText,
						null, nextRoles);
				entity.setAccWorkFlowNo(entryId);
				entity.setPrjStatus(1l);
			}
			entity.setAccSignStartDate(new Date());
			update(entity);
			return "操作成功！";
		} catch (Exception e) {
			return "操作失败！";
		}
	}

	/**
	 * @param prjNo
	 *            项目编号
	 * @param workflowType
	 *            工作流流程
	 * @param entryId
	 *            实例编号
	 * @param workerCode
	 *            审批人
	 * @param actionId
	 *            动作
	 * @param approveText
	 *            意见
	 * @param nextRoles
	 *            下一步角色
	 * 
	 * 审批步骤：工程项目 审批
	 * 
	 * 对应信息：流程 stepId
	 * 
	 * →项目管理单位审批通过→11
	 * 
	 * →生产技术部审批通过→12
	 * 
	 * →财务部审批通过→13
	 * 
	 * →监察审计部审批通过→14
	 * 
	 * →安全环保部审批通过→15
	 * 
	 * →计划经营部审批通过→16
	 * 
	 * →分管厂领导审批通过→17
	 * 
	 * →厂长审批通过→2
	 * 
	 * →验收页面点“保存”→4
	 * 
	 * →验收页面点“申请”→5
	 * 
	 * →工程建设单位审批通过→21
	 * 
	 * →工程管理单位审批通过→22
	 * 
	 * →安全环保部已验收→23
	 * 
	 * →档案管理部审批通过→24
	 * 
	 * →分管厂领导→6
	 * 
	 */

	// 立项审批
	public String approveStep(String prjNo, String workflowType,
			String workerCode, String actionId, String actionWord,
			String approveText, String nextRoles, String stepId,
			String enterpriseCode) {
		PrjJInfo model = findByPrjNo(prjNo, enterpriseCode);
		try {
			long actionIdl = Long.parseLong(actionId);
			this.changeWfInfo(model.getAccWorkFlowNo(), workerCode, actionIdl,
					approveText, nextRoles);
			model.setPrjStatus(getapproveStatus(stepId, actionWord));
			update(model);
			return "审批成功！";
		} catch (Exception e) {
			return "审批失败！";
		}
	}

	// 验收申请
	public String checkReport(String prjNo, String workflowType,
			String workerCode, String actionId, String actionWord,
			String approveText, String nextRoles, String stepId,
			String enterpriseCode) {
		PrjJInfo model = findByPrjNo(prjNo, enterpriseCode);
		try {
			if (model.getProWorkFlowNo() == null) {
				// 处理未上报
				if (!workflowType.equals("")) {
					long entryId = service.doInitialize(workflowType,
							workerCode, prjNo);
					long actionIdl = Long.parseLong(actionId);
					service.doAction(entryId, workerCode, actionIdl,
							approveText, null, nextRoles);
					model.setProWorkFlowNo(entryId);
				}
			} else {
				// 处理已退回
				long entryId = service.doInitialize(workflowType, workerCode,
						model.getPrjNo());
				long actionIdl = Long.parseLong(actionId);
				service.doAction(entryId, workerCode, actionIdl, approveText,
						null, nextRoles);
				model.setProWorkFlowNo(entryId);
			}
			model.setPrjStatus(5l);
			model.setProSignStartDate(new Date());
			update(model);
			return "上报成功！";
		} catch (Exception e) {
			return "上报失败！";
		}
	}

	// 验收审批
	public String checkStep(String prjNo, String workflowType,
			String workerCode, String actionId, String actionWord,
			String approveText, String nextRoles, String stepId,
			String enterpriseCode) {
		PrjJInfo model = findByPrjNo(prjNo, enterpriseCode);
		try {
			long actionIdl = Long.parseLong(actionId);
			this.changeWfInfo(model.getProWorkFlowNo(), workerCode, actionIdl,
					approveText, nextRoles);
			model.setPrjStatus(getcheckStatus(stepId, actionWord));
			update(model);
			return "审批成功！";
		} catch (Exception e) {
			return "审批失败！";
		}
	}

	private Long getapproveStatus(String stepID, String actionWord) {
		if (actionWord.equals("TY")) {
			switch (Integer.parseInt(stepID)) {
			case 1:
				return 11l;
			case 11:
				return 12l;
			case 12:
				return 13l;
			case 13:
				return 14l;
			case 14:
				return 15l;
			case 15:
				return 17l;
			case 17:
				return 19l;
			case 18:
				return 12l;
			default:
				return 0l;
			}
		} else if (actionWord.equals("TG")) {
			switch (Integer.parseInt(stepID)) {
			case 1:
				return 11l;
			case 11:
				return 13l;
			default:
				return 0l;
			}
		} else if (actionWord.equals("TH")) {
			return 3l;
		} else if (actionWord.equals("WC")) {
			return 4l;
		} else if (actionWord.equals("ZG")) {
			return 11l;
		} else if (actionWord.equals("HF")) {
			return 18l;
		} else {
			return 0l;
		}
	}

	private Long getcheckStatus(String stepID, String actionWord) {
		if (actionWord.equals("TY")) {
			switch (Integer.parseInt(stepID)) {
			case 5:
				return 31l;
			case 31:
				return 32l;
			case 32:
				return 33l;
			case 33:
				return 34l;
			case 34:
				return 35l;
			case 35:
				return 6l;
			default:
				return 0l;
			}
		} else if (actionWord.equals("TH")) {
			return 7l;
		} else if (actionWord.equals("WC")) {
			return 6l;
		} else {
			return 0l;
		}
	}

	// 项目显示编码规则
	@SuppressWarnings("unchecked")
	private String getPrjNoShow(String Enterprisecode) {
		String code = "PR";
		java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat(
				"yyMMdd");
		Date date = new java.util.Date();
		code += tempDate.format(date);
		int count = this.getPrjNoShowCount(Enterprisecode);
		if (count + 1 < 10) {
			code += "00" + (count + 1);
		}
		if (10 <= (count + 1) && (count + 1) < 100) {
			code += "0" + (count + 1);
		}
		if ((count + 1) >= 100) {
			code += count + 1;
		}
		return code;
	}

	// 取得当天项目生成数
	public int getPrjNoShowCount(String Enterprisecode) {
		java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat(
				"yyMMdd");
		Date date = new java.util.Date();
		String PrjNoShow = "PR" + tempDate.format(date);
		String sql = "select count(1) from prj_j_info t where t.prj_no_show like '"
				+ PrjNoShow
				+ "%'"
				+ " and t.is_use='Y'"
				+ " and t.enterprise_code='" + Enterprisecode + "'";
		int count = Integer.parseInt(bll.getSingal(sql).toString());
		return count;
	}

	// 取立项审批记录
	public List<PrjApproveForm> getApplyApprovelist(Long id) {

		Long entryId = this.findById(id).getAccWorkFlowNo();
		List list = new ArrayList();

		String sql =
		// "SELECT * "
		// + "FROM (" +
		"SELECT t.*,getworkername(t.caller) callername  FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '4'"
				+ " AND opinion_time > "
				+ "(SELECT MAX(opinion_time)"
				+ "FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '2')"
				+
				// + " ORDER BY t.opinion_time DESC)"
				// + " WHERE rownum = 1" +

				"UNION ALL"
				+
				// "SELECT * "
				// + "FROM (" +
				" SELECT t.*,getworkername(t.caller) callername  FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '5'"
				+ " AND opinion_time > "
				+ "(SELECT MAX(opinion_time)"
				+ "FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '2')"
				+ "and t.id = (select max(a.id)\n"
				+ "              from wf_j_historyoperation a\n"
				+ "             WHERE a.entry_id = '"
				+ entryId
				+ "'"
				+ "               AND a.step_id = '5')"

				// + " ORDER BY t.opinion_time DESC)"
				// + " WHERE rownum = 1" +
				+ "UNION ALL"
				+
				// "SELECT * "
				// + "FROM (" +
				" SELECT t.*,getworkername(t.caller) callername  FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '12'"
				+ " AND opinion_time > "
				+ "(SELECT MAX(opinion_time)"
				+ "FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '2')"
				+ " and t.id = (select max(a.id)\n"
				+ "                 from wf_j_historyoperation a\n"
				+ "                where a.entry_id = '"
				+ entryId
				+ "'"
				+ "                  and a.step_id = '12')"

				+ "UNION ALL"
				+
				// "SELECT * "
				// + "FROM (" +
				" SELECT t.*,getworkername(t.caller) callername  FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '6'"
				+ " AND opinion_time > "
				+ "(SELECT MAX(opinion_time)"
				+ "FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '2')"
				+
				// + " ORDER BY t.opinion_time DESC)"
				// + " WHERE rownum = 1" +

				"UNION ALL"
				+
				// "SELECT * "
				// + "FROM (" +
				" SELECT t.*,getworkername(t.caller) callername  FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '7'"
				+ " AND opinion_time > "
				+ "(SELECT MAX(opinion_time)"
				+ "FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '2')"
				+
				// + " ORDER BY t.opinion_time DESC)"
				// + " WHERE rownum = 1" +

				"UNION ALL"
				+
				// "SELECT * "
				// + "FROM (" +
				" SELECT t.*,getworkername(t.caller) callername  FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '8'"
				+ " AND opinion_time > "
				+ "(SELECT MAX(opinion_time)"
				+ "FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '2')"
				+
				// + " ORDER BY t.opinion_time DESC)"
				// + " WHERE rownum = 1" +

				"UNION ALL"
				+
				// "SELECT * "
				// + "FROM (" +
				" SELECT t.*,getworkername(t.caller) callername  FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '9'"
				+ " AND opinion_time > "
				+ "(SELECT MAX(opinion_time)"
				+ "FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '2')"
				+
				// + " ORDER BY t.opinion_time DESC)"
				// + " WHERE rownum = 1" +

				"UNION ALL"
				+
				// "SELECT * "
				// + "FROM (" +
				" SELECT t.* ,getworkername(t.caller) callername  FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '11'"
				+ " AND opinion_time > "
				+ "(SELECT MAX(opinion_time)"
				+ "FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '2')"
		// + " ORDER BY t.opinion_time DESC)"
		// + " WHERE rownum = 1" +
		;
		list = bll.queryByNativeSQL(sql);
		List<PrjApproveForm> arraylist = new ArrayList<PrjApproveForm>();
		Iterator it = list.iterator();
		while (it.hasNext()) {

			Object[] data = (Object[]) it.next();
			PrjApproveForm model = new PrjApproveForm();

			if (data[0] != null) {
				model.setId(Long.parseLong(data[0].toString()));
			}
			if (data[2] != null) {
				model.setStepId(data[2].toString());
			}
			if (data[3] != null) {
				model.setStepName(data[3].toString());
			}
			if (data[4] != null) {
				model.setActionId(data[4].toString());
			}
			if (data[8] != null) {
				model.setOpinion(data[8].toString());
			}
			if (data[9] != null) {
				model.setOpinionTime(data[9].toString());
			}
			if (data[10] != null) {
				model.setCaller(data[10].toString());
			}
			arraylist.add(model);
		}
		return arraylist;
	}

	// 取验收审批记录
	public List<PrjApproveForm> getCheckApprovelist(Long id) {

		Long entryId = this.findById(id).getProWorkFlowNo();
		if (entryId != null) {
			List list = new ArrayList();

			String sql = "SELECT t.*,getworkername(t.caller) callername  FROM wf_j_historyoperation t"
					+ " WHERE t.entry_id = '"
					+ entryId
					+ "'"
					+ "AND t.step_id in (4,5,6,7,8,9,10)"
					+ " AND opinion_time > "
					+ "(SELECT MAX(opinion_time)"
					+ "FROM wf_j_historyoperation t"
					+ " WHERE t.entry_id = '"
					+ entryId
					+ "'"
					+ "AND t.step_id = '2')" + " ORDER BY t.opinion_time ";

//			String sql =
//
//			" SELECT t.*,getworkername(t.caller) callername    FROM wf_j_historyoperation t"
//					+ " WHERE t.entry_id = '"
//					+ entryId
//					+ "'"
//					+ "AND t.step_id = '5'"
//					+ " AND opinion_time > "
//					+ "(SELECT MAX(opinion_time)"
//					+ "FROM wf_j_historyoperation t"
//					+ " WHERE t.entry_id = '"
//					+ entryId
//					+ "'"
//					+ "AND t.step_id = '2')"
//					+
//					// + " ORDER BY t.opinion_time DESC)"
//					// + " WHERE rownum = 1" +
//
//					"UNION ALL"
//					+
//					// "SELECT * "
//					// + "FROM (" +
//					" SELECT t.*,getworkername(t.caller) callername    FROM wf_j_historyoperation t"
//					+ " WHERE t.entry_id = '"
//					+ entryId
//					+ "'"
//					+ "AND t.step_id = '6'"
//					+ " AND opinion_time > "
//					+ "(SELECT MAX(opinion_time)"
//					+ "FROM wf_j_historyoperation t"
//					+ " WHERE t.entry_id = '"
//					+ entryId
//					+ "'"
//					+ "AND t.step_id = '2')"
//					+
//					// + " ORDER BY t.opinion_time DESC)"
//					// + " WHERE rownum = 1" +
//
//					"UNION ALL"
//					+
//					// "SELECT * "
//					// + "FROM (" +
//					" SELECT t.*,getworkername(t.caller) callername    FROM wf_j_historyoperation t"
//					+ " WHERE t.entry_id = '"
//					+ entryId
//					+ "'"
//					+ "AND t.step_id = '7'"
//					+ " AND opinion_time > "
//					+ "(SELECT MAX(opinion_time)"
//					+ "FROM wf_j_historyoperation t"
//					+ " WHERE t.entry_id = '"
//					+ entryId
//					+ "'"
//					+ "AND t.step_id = '2')"
//					+
//					// + " ORDER BY t.opinion_time DESC)"
//					// + " WHERE rownum = 1" +
//
//					"UNION ALL"
//					+
//					// "SELECT * "
//					// + "FROM (" +
//					" SELECT t.*,getworkername(t.caller) callername    FROM wf_j_historyoperation t"
//					+ " WHERE t.entry_id = '"
//					+ entryId
//					+ "'"
//					+ "AND t.step_id = '8'"
//					+ " AND opinion_time > "
//					+ "(SELECT MAX(opinion_time)"
//					+ "FROM wf_j_historyoperation t"
//					+ " WHERE t.entry_id = '"
//					+ entryId + "'" + "AND t.step_id = '2')"
//			// + " ORDER BY t.opinion_time DESC)"
//			// + " WHERE rownum = 1" +
//			;
			list = bll.queryByNativeSQL(sql);
			List<PrjApproveForm> arraylist = new ArrayList<PrjApproveForm>();
			Iterator it = list.iterator();
			while (it.hasNext()) {

				Object[] data = (Object[]) it.next();
				PrjApproveForm model = new PrjApproveForm();

				if (data[0] != null) {
					model.setId(Long.parseLong(data[0].toString()));
				}
				if (data[3] != null) {
					model.setStepName(data[3].toString());
				}
				if (data[6] != null) {
					model.setCaller(data[6].toString());
				}
				if (data[8] != null) {
					model.setOpinion(data[8].toString());
				}
				if (data[9] != null) {
					model.setOpinionTime(data[9].toString());
					;
				}
//				if (data[10] != null) {
//					model.setCaller(data[10].toString());
//				}
				arraylist.add(model);
			}
			return arraylist;
		} else {
			return null;
		}

	}

	/**
	 * 根据项目编号取基本信息(报表）
	 * 
	 * @param prjNo
	 * @return add by drdu 090730
	 */
	public PrjQualityForm findPrjPrintInfo(String balanceId) {
		String sql = "select distinct a.prj_name,\n"
				+ "                getclientname(a.construction_unit),\n"
				+ "                c.applicat_price,\n"
				+ "                c.bala_cause,\n"
				+ "                (c.applicat_price) as balaPrice,\n"
				+ "                b.warranty_period\n"
				+ "  from prj_j_info a, con_j_contract_info b, con_j_balance c\n"
				+ " where b.project_id(+) = a.prj_no\n"
				+ "   and c.con_id(+) = b.con_id\n" + "   and c.balance_id = '"
				+ balanceId + "'\n" + "   and a.is_use = 'Y'\n"
				+ "   and b.is_use(+) = 'Y'\n" + "   and c.is_use(+) = 'Y'";
		Object[] data = (Object[]) bll.getSingal(sql);
		PrjQualityForm form = new PrjQualityForm();
		if (data != null) {
			if (data[0] != null)
				form.setPrjName(data[0].toString());
			if (data[1] != null)
				form.setConstructionUnit(data[1].toString());
			if (data[2] != null)
				form.setApplicatPrice(Double.parseDouble(data[2].toString()));
			if (data[3] != null)
				form.setBalaCause(data[3].toString());
			if (data[4] != null)
				form.setBalaPrice(Double.parseDouble(data[4].toString()));
			if (data[5] != null)
				form.setWarrantyPeriod(Long.parseLong(data[5].toString()));

			return form;

		} else {
			return null;
		}
	}

	/**
	 * 根据工作流ID取得审批记录(报表）
	 * 
	 * @param id
	 * @return add by drdu 090730
	 */
	@SuppressWarnings("unchecked")
	public List<ConApproveForm> findPrjPrintApproveInfo(Long id) {

		Long entryId = cbremote.findById(id).getWorkflowNo();
		List list = new ArrayList();
		String sql = "SELECT t.*,getworkername(t.caller) callername  FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id in (4,5,6)"
				+ " AND opinion_time > "
				+ "(SELECT MAX(opinion_time)"
				+ "FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '2')" + " ORDER BY t.opinion_time ";

		list = bll.queryByNativeSQL(sql);
		List<ConApproveForm> arraylist = new ArrayList<ConApproveForm>();
		Iterator it = list.iterator();
		while (it.hasNext()) {

			Object[] data = (Object[]) it.next();
			ConApproveForm model = new ConApproveForm();

			if (data[0] != null) {
				model.setId(Long.parseLong(data[0].toString()));
			}
			if (data[3] != null) {
				model.setStepName(data[3].toString());
			}
			if (data[8] != null) {
				model.setOpinion(data[8].toString());
			}
			if (data[9] != null) {
				model.setOpinionTime(data[9].toString());
				;
			}
			if (data[10] != null) {
				model.setCaller(data[10].toString());
			}
			arraylist.add(model);
		}
		return arraylist;
	}
}