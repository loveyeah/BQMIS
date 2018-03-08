package power.ejb.manage.contract.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.manage.contract.form.ConDocForm;
import power.ejb.manage.contract.form.ConModifyForm;
import power.ejb.manage.contract.form.PaymentPlanForm;

@Stateless
public class ConJModifyFacade implements ConJModifyFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "ConJContractInfoFacade")
	protected ConJContractInfoFacadeRemote remote;
	@EJB(beanName = "ConJConDocFacade")
	protected ConJConDocFacadeRemote remoteDoc;
	@EJB(beanName = "ConJPaymentPlanFacade")
	protected ConJPaymentPlanFacadeRemote remotePay;

	public ConJModify save(ConJModify entity) {

		try {
			if (entity.getConModifyId() == null) {
				entity.setConModifyId(bll.getMaxId("CON_J_MODIFY",
						"con_modify_id"));
			}
			entity.setConModifyNo(this.createConModifyNo(entity.getConId()));
			entity.setFileStatus("DRF");
			entity.setIsUse("Y");
			entity.setWorkflowStatus(0L);
			entity.setEntryDate(new Date());
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(ConJModify entity) {
		entity.setIsUse("N");
		String paysql = "update CON_J_PAYMENT_PLAN t set t.is_use='N' where t.CON_ID=?";
		String docsql = "update CON_J_CON_DOC t set t.is_use='N' where t.KEY_ID=?";
		bll.exeNativeSQL(paysql, new Object[] { entity.getConId() });
		bll.exeNativeSQL(docsql, new Object[] { entity.getConId() });
		this.update(entity);
	}

	public ConJModify update(ConJModify entity) {
		LogUtil.log("updating ConJModify instance", Level.INFO, null);
		try {
			ConJModify result = entityManager.merge(entity);
			// entity.setEntryDate(new Date());
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConJModify findById(Long id) {
		LogUtil.log("finding ConJModify instance with id: " + id, Level.INFO,
				null);
		try {
			ConJModify instance = entityManager.find(ConJModify.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConModifyForm findConModifyModel(Long conmodifyId) {
		String sql = "select b.con_modify_id,\n"
				+ "       b.workflow_status,\n"
				+ "       b.con_modify_no,\n"
				+ "       a.contract_name,\n"
				+ "       a.cliend_id,\n"
				+ "       getClientName(a.cliend_id) clientname,\n"
				+ "       b.conomodify_type,\n"
				+ "       b.operate_by,\n"
				+ "       getworkername(b.operate_by) operatebyname,\n"
				+ "       b.operate_dep_code,\n"
				+ "       getdeptname(b.operate_dep_code) operatedepname,\n"
				+ "       to_char(b.sign_start_date, 'yyyy-MM-dd hh24:mi:ss') sign_start_date,\n"
				+ "       to_char(b.sign_end_date, 'yyyy-MM-dd hh24:mi:ss') sign_end_date,\n"
				+ "       b.is_use,\n"
				+ "       b.enterprise_code,\n"
				+ "       a.conttrees_no,\n"
				+ "       b.operate_lead_by,\n"
				+ "       getworkername(b.operate_lead_by) operateleadbyname,\n"
				+ "       a.currency_type,\n"
				+ "       b.act_amount,\n"
				+ "       b.modiy_act_amount,\n"
				+ "       to_char(a.start_date, 'yyyy-MM-dd hh24:mi:ss') start_date,\n"
				+ "       to_char(a.end_date, 'yyyy-MM-dd hh24:mi:ss') end_date,\n"
				+ "       b.entry_by,\n"
				+ "       getworkername(b.entry_by) entrybyname,\n"
				+ "       to_char(b.entry_date, 'yyyy-MM-dd hh24:mi:ss') entry_date,\n"
				+ "       b.con_id,\n"
				+ "       b.conomodify_name, b.work_flow_no,\n"
				+ " ( select (t.ori_file_name || '.' || t.ori_file_ext) condec from con_j_con_doc t where t.key_id='"
				+ conmodifyId
				+ "' and t.doc_type='MCON' and rownum=1) filePath,\n"
				+ "  ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = a.currency_type) currencyName,\n"
				+ "  (select h.dept_id from hr_c_dept h where h.dept_code = b.operate_dep_code and  h.is_use = 'Y')  deptId, \n "//update by sychen 20100902
//				+ "  (select h.dept_id from hr_c_dept h where h.dept_code = b.operate_dep_code and  h.is_use = 'U')  deptId, \n "
				+ "   a.exec_flag, \n"
				+ "   b.DEPT_FLG \n"
				+ "  from CON_J_CONTRACT_INFO a, CON_J_MODIFY b\n"
				+ " where a.con_id = b.con_id\n" + "   and b.con_modify_id = "
				+ conmodifyId + "\n" + "   and a.is_use = 'Y'\n"
				+ "   and b.is_use = 'Y'";

		Object[] data = (Object[]) bll.getSingal(sql);
		if(data.length > 0 || data !=null){
		ConModifyForm model = new ConModifyForm();
		model.setConModifyId(Long.parseLong(data[0].toString()));
		if (data[1] != null)
			model.setWorkflowStatus(Long.parseLong(data[1].toString()));
		if (data[2] != null)
			model.setConModifyNo(data[2].toString());
		if (data[3] != null)
			model.setContractName(data[3].toString());
		if (data[4] != null)
			model.setCliendId(Long.parseLong(data[4].toString()));
		if (data[5] != null)
			model.setClientName(data[5].toString());
		if (data[6] != null)
			model.setConomodifyType(Long.parseLong(data[6].toString()));
		if (data[7] != null)
			model.setOperateBy(data[7].toString());
		if (data[8] != null)
			model.setOperateName(data[8].toString());
		if (data[9] != null)
			model.setOperateDepCode(data[9].toString());
		if (data[10] != null)
			model.setOperateDeptName(data[10].toString());
		if (data[11] != null)
			model.setSignStartDate(data[11].toString());
		if (data[12] != null)
			model.setSignEndDate(data[12].toString());
		if (data[13] != null)
			model.setIsUse(data[13].toString());
		if (data[14] != null)
			model.setEnterpriseCode(data[14].toString());
		if (data[15] != null)
			model.setConttreesNo(data[15].toString());
		if (data[16] != null)
			model.setOperateLeadBy(data[16].toString());
		if (data[17] != null)
			model.setOperateLeadName(data[17].toString());
		if (data[18] != null)
			model.setCurrencyType(Long.parseLong(data[18].toString()));
		if (data[19] != null)
			model.setActAmount(Double.parseDouble(data[19].toString()));
		if (data[20] != null)
			model.setModiyActAmount(Double.parseDouble(data[20].toString()));
		if (data[21] != null)
			model.setStartDate(data[21].toString());
		if (data[22] != null)
			model.setEndDate(data[22].toString());
		if (data[23] != null)
			model.setEntryBy(data[23].toString());
		if (data[24] != null)
			model.setEntryName(data[24].toString());
		if (data[25] != null)
			model.setEntryDate(data[25].toString());
		if (data[26] != null)
			model.setConId(Long.parseLong(data[26].toString()));
		if (data[27] != null)
			model.setConomodifyName(data[27].toString());
		if (data[28] != null)
			model.setWorkFlowNo(Long.parseLong(data[28].toString()));
		if (data[29] != null)
			model.setFilePath(data[29].toString());
		if (data[30] != null)
			model.setCurrencyName(data[30].toString());
		if (data[31] != null)
			model.setDeptId(Long.parseLong(data[31].toString()));
		if (data[32] != null)
			model.setExecFlag(data[32].toString());
		if (data[33] != null)
			model.setDeptFlg(data[33].toString());
		List<ConDocForm> mconlist = remoteDoc
				.findConDocList(model.getEnterpriseCode(),
						model.getConModifyId(), "MCON", null).getList();
		List<ConDocForm> mconAttlist = remoteDoc.findConDocList(
				model.getEnterpriseCode(), model.getConModifyId(), "MCONATT",
				null).getList();
		List<ConDocForm> mconEvilist = remoteDoc.findConDocList(
				model.getEnterpriseCode(), model.getConModifyId(), "MCONEVI",
				null).getList();
		List<PaymentPlanForm> paylist = remotePay.findByConId(model
				.getConModifyId());
		if (mconlist != null) {
			model.setMconlist(mconlist);
		}
		if (mconAttlist != null)
			model.setMconAttlist(mconAttlist);
		if (mconEvilist != null)
			model.setMconEvilist(mconEvilist);
		if (paylist != null)
			model.setConpaylist(paylist);
		return model;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findConModifyList(String workCode, Long conTypeId,
			String enterpriseCode, String sDate, String eDate, String fuzzy,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = null;
			String sql = "select b.con_modify_id,\n"
					+ "       b.workflow_status,\n"
					+ "       b.con_modify_no,\n"
					+ "       a.contract_name,\n"
					+ "       a.cliend_id,\n"
					+ "       getclientname(a.cliend_id) clientname,\n"
					+ "       b.conomodify_type,\n"
					+ "       b.operate_by,\n"
					+ "       getworkername(b.operate_by) operatebyname,\n"
					+ "       b.operate_dep_code,\n"
					+ "       getdeptname(b.operate_dep_code) operatedepname,\n"
					+ "       to_char(b.sign_start_date, 'yyyy-MM-dd hh24:mi:ss') sign_start_date,\n"
					+ "       to_char(b.sign_end_date, 'yyyy-MM-dd hh24:mi:ss') sign_end_date,\n"
					+ "       b.is_use,\n" + "       b.enterprise_code,\n"
					+ "       b.con_id,\n" + "       b.act_amount,\n"
					+ "       b.modiy_act_amount,\n"
					+ "       a.currency_type,\n" + "       b.file_status,\n"
					+ "       b.work_flow_no,\n"
					+ "       a.exec_flag\n"
					+ "  from CON_J_CONTRACT_INFO a, CON_J_MODIFY b\n"
					+ " where a.con_id = b.con_id\n"
					+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
					+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and b.enterprise_code = '" + enterpriseCode + "'"
					+ "   and b.workflow_status in (0,3)";

			String sqlCount = "select count(1)\n"
					+ "      from CON_J_CONTRACT_INFO a, CON_J_MODIFY b\n"
					+ "     where a.con_id = b.con_id\n"
					+ "       and a.is_use = 'Y'\n"
					+ "       and b.is_use = 'Y'\n"
					+ "       and a.enterprise_code = '" + enterpriseCode
					+ "'\n" + "       and b.enterprise_code = '"
					+ enterpriseCode + "'"
					+ "   and b.workflow_status in (0,3)";
			if (conTypeId != null) {
				String strWhere = " and a.con_type_id ='" + conTypeId + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (sDate != null && !sDate.equals("")) {
				String strWhere = "  and a.sign_start_date >=to_date('" + sDate
						+ "', 'yyyy-MM-dd hh24:mi:ss')\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (eDate != null && !eDate.equals("")) {
				String strWhere = "  and a.sign_start_date <=to_date('" + eDate
						+ "', 'yyyy-MM-dd hh24:mi:ss')\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (fuzzy != null && !fuzzy.equals("")) {
				String strWhere = " and b.con_modify_no||a.contract_name||getClientName(a.cliend_id) like '%"
						+ fuzzy + "%'";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (!workCode.equals("")) {
				String strWhere = "  and b.ENTRY_BY = '" + workCode + "'";
				sql += strWhere;
				sqlCount += strWhere;
			}
			sql += " order by b.con_modify_no";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arraylist = new ArrayList();
			if (list != null && list.size() > 0) {
				result = new PageObject();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					ConModifyForm model = new ConModifyForm();
					Object[] data = (Object[]) it.next();
					model.setConModifyId(Long.parseLong(data[0].toString()));
					if (data[1] != null)
						model.setWorkflowStatus(Long.parseLong(data[1]
								.toString()));
					if (data[2] != null)
						model.setConModifyNo(data[2].toString());
					if (data[3] != null)
						model.setContractName(data[3].toString());
					if (data[4] != null)
						model.setCliendId(Long.parseLong(data[4].toString()));
					if (data[5] != null)
						model.setClientName(data[5].toString());
					if (data[6] != null)
						model.setConomodifyType(Long.parseLong(data[6]
								.toString()));
					if (data[7] != null)
						model.setOperateBy(data[7].toString());
					if (data[8] != null)
						model.setOperateName(data[8].toString());
					if (data[9] != null)
						model.setOperateDepCode(data[9].toString());
					if (data[10] != null)
						model.setOperateDeptName(data[10].toString());
					if (data[11] != null)
						model.setSignStartDate(data[11].toString());
					if (data[12] != null)
						model.setSignEndDate(data[12].toString());
					if (data[13] != null)
						model.setIsUse(data[13].toString());
					if (data[14] != null)
						model.setEnterpriseCode(data[14].toString());
					if (data[15] != null)
						model.setConId(Long.parseLong(data[15].toString()));
					if (data[16] != null)
						model.setActAmount(Double.parseDouble(data[16]
								.toString()));
					if (data[17] != null)
						model.setModiyActAmount(Double.parseDouble(data[17]
								.toString()));
					if (data[18] != null)
						model.setCurrencyType(Long.parseLong(data[18]
								.toString()));
					if (data[19] != null)
						model.setFileStatus(data[19].toString());
					if (data[20] != null)
						model
								.setWorkFlowNo(Long.parseLong(data[20]
										.toString()));
					if (data[21] != null)
						model.setExecFlag(data[21].toString());
					arraylist.add(model);
				}
				Long totalCount = Long.parseLong(bll.getSingal(sqlCount)
						.toString());
				result.setList(arraylist);
				result.setTotalCount(totalCount);
			}
			return result;
		} catch (RuntimeException e) {
			throw e;
		}
	}

	private String createConModifyNo(Long conId) {
		String code = "";
		String sqlcon = "select t.conttrees_no from con_j_contract_info t where t.con_id=?";
		code = bll.getSingal(sqlcon, new Object[] { conId }).toString();

		String sql = "select '"
				+ code
				+ "' ||'-'||\n"
				+ "       nvl(trim(to_char(max(substr(t.con_modify_no, length(t.con_modify_no)-1))+1,'00')),\n"
				+ "           '01')\n" + "  from con_j_modify t\n"
				+ " where t.con_modify_no like '" + code + "-%'";

		return bll.getSingal(sql).toString();

	}

	@SuppressWarnings("unchecked")
	public PageObject findConModifyApproveList(Long conTypeId,
			String enterpriseCode, String sDate, String eDate, String status,
			String entryIds, final int... rowStartIdxAndCount) {
		try {
			PageObject result = null;
			String sql = "select b.con_modify_id,\n"
					+ "       b.workflow_status,\n"
					+ "       b.con_modify_no,\n"
					+ "       a.contract_name,\n"
					+ "       a.cliend_id,\n"
					+ "       getclientname(a.cliend_id) clientname,\n"
					+ "       b.conomodify_type,\n"
					+ "       b.operate_by,\n"
					+ "       getworkername(b.operate_by) operatebyname,\n"
					+ "       b.operate_dep_code,\n"
					+ "       getdeptname(b.operate_dep_code) operatedepname,\n"
					+ "       to_char(b.sign_start_date, 'yyyy-MM-dd hh24:mi:ss') sign_start_date,\n"
					+ "       to_char(b.sign_end_date, 'yyyy-MM-dd hh24:mi:ss') sign_end_date,\n"
					+ "       b.is_use,\n" + "       b.enterprise_code,\n"
					+ "       b.con_id,\n" + "       b.act_amount,\n"
					+ "       b.modiy_act_amount,\n"
					+ "       a.currency_type,\n" + "       b.file_status\n"
					+ "  from CON_J_CONTRACT_INFO a, CON_J_MODIFY b\n"
					+ " where a.con_id = b.con_id\n"
					+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
					+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
					// + " and a.con_type_id ='"+conTypeId+"'\n"
					+ "   and b.enterprise_code = '" + enterpriseCode + "'";

			String sqlCount = "select count(1)\n"
					+ "      from CON_J_CONTRACT_INFO a, CON_J_MODIFY b\n"
					+ "     where a.con_id = b.con_id\n"
					+ "       and a.is_use = 'Y'\n"
					+ "       and b.is_use = 'Y'\n"
					+ "       and a.enterprise_code = '" + enterpriseCode
					+ "'\n" + "       and b.enterprise_code = '"
					+ enterpriseCode + "'";
			if (conTypeId != null) {
				String strWhere = " and a.con_type_id ='" + conTypeId + "'\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (sDate != null && !sDate.equals("")) {
				String strWhere = "  and a.sign_start_date >=to_date('" + sDate
						+ "', 'yyyy-MM-dd hh24:mi:ss')\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (eDate != null && !eDate.equals("")) {
				String strWhere = "  and a.sign_start_date <=to_date('" + eDate
						+ "', 'yyyy-MM-dd hh24:mi:ss')\n";
				sql += strWhere;
				sqlCount += strWhere;
			}
			if (status != null && status.equals("approve")) {
				String strWhere = "  and b.workflow_status =1 ";
//				if (entryIds != null && (!"".equals(entryIds))) {
					strWhere += " and b.work_flow_no in (" + entryIds + ")\n";
//				}
				sql += strWhere;
				sqlCount += strWhere;
			} else if (status != null && status.equals("ok")) {
				String strWhere = "  and b.workflow_status =2";
				sql += strWhere;
				sqlCount += strWhere;
			}

			sql += " order by b.con_modify_no";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arraylist = new ArrayList();
			if (list != null && list.size() > 0) {
				result = new PageObject();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					ConModifyForm model = new ConModifyForm();
					Object[] data = (Object[]) it.next();
					model.setConModifyId(Long.parseLong(data[0].toString()));
					if (data[1] != null)
						model.setWorkflowStatus(Long.parseLong(data[1]
								.toString()));
					if (data[2] != null)
						model.setConModifyNo(data[2].toString());
					if (data[3] != null)
						model.setContractName(data[3].toString());
					if (data[4] != null)
						model.setCliendId(Long.parseLong(data[4].toString()));
					if (data[5] != null)
						model.setClientName(data[5].toString());
					if (data[6] != null)
						model.setConomodifyType(Long.parseLong(data[6]
								.toString()));
					if (data[7] != null)
						model.setOperateBy(data[7].toString());
					if (data[8] != null)
						model.setOperateName(data[8].toString());
					if (data[9] != null)
						model.setOperateDepCode(data[9].toString());
					if (data[10] != null)
						model.setOperateDeptName(data[10].toString());
					if (data[11] != null)
						model.setSignStartDate(data[11].toString());
					if (data[12] != null)
						model.setSignEndDate(data[12].toString());
					if (data[13] != null)
						model.setIsUse(data[13].toString());
					if (data[14] != null)
						model.setEnterpriseCode(data[14].toString());
					if (data[15] != null)
						model.setConId(Long.parseLong(data[15].toString()));
					if (data[16] != null)
						model.setActAmount(Double.parseDouble(data[16]
								.toString()));
					if (data[17] != null)
						model.setModiyActAmount(Double.parseDouble(data[17]
								.toString()));
					if (data[18] != null)
						model.setCurrencyType(Long.parseLong(data[18]
								.toString()));
					if (data[19] != null)
						model.setFileStatus(data[19].toString());
					arraylist.add(model);
				}
				Long totalCount = Long.parseLong(bll.getSingal(sqlCount)
						.toString());
				result.setList(arraylist);
				result.setTotalCount(totalCount);
			}
			return result;
		} catch (RuntimeException e) {
			throw e;
		}
	}

	public List<ConApproveForm> getApproveList(Long id) {

		Long entryId = this.findById(id).getWorkFlowNo();
		List list = new ArrayList();

		String sql = "SELECT t.*,getworkername(t.caller) callername  FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id in (4,5,6,7,8,9)"
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
			if ((this.findById(id).getConModifyNo().substring(5, 7)).equals("CG")
					&& this.findById(id).getModiyActAmount() > 100000
					&& this.findById(id).getModiyActAmount() < 200000
					&& arraylist.size() == 2) {
				arraylist.add(new ConApproveForm());
			}
		}
		return arraylist;
	}

	// 取得变更id
	public List<ConJModify> getconModifyId(String conId) {
		String sql = " select t.* from CON_J_MODIFY t where "
				+ "  t.con_id='" + conId + "' and t.is_use ='Y' and t.workflow_status=2"
				+"  order by t.con_modify_id";
		List<ConJModify> list = bll.queryByNativeSQL(sql,ConJModify.class);
		if (list != null) {
			return list;
		} else {
			return null;
		}
	}
}