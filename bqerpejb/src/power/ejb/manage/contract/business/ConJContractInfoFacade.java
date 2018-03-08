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

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.manage.contract.form.ContractFullInfo;
import power.ejb.manage.contract.form.ContractInfo;
import power.ejb.manage.project.PrjJInfo;
import power.ejb.manage.project.PrjJInfoFacadeRemote;

/**
 * 
 * @author sltang
 */
@Stateless
public class ConJContractInfoFacade implements ConJContractInfoFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "ConJConDocFacade")
	protected ConJConDocFacadeRemote docremote;
	@EJB(beanName = "ConJPaymentPlanFacade")
	protected ConJPaymentPlanFacadeRemote payremote;
	@EJB(beanName = "ConJContractInfoFacade")
	protected ConJContractInfoFacadeRemote conRemote;
	@EJB(beanName = "PrjJInfoFacade")
	protected PrjJInfoFacadeRemote prjRemote;

	public ConJContractInfo save(String markCode, ConJContractInfo entity,
			ConJConDoc condoc, String secondcharge) throws CodeRepeatException {
		try {
			  if (entity.getProjectId() !=null && !entity.getProjectId().equals("")){
				if (secondcharge != null && !secondcharge.equals("")) {
					PrjJInfo prjModel = prjRemote.findByPrjNo(
							entity.getProjectId(), entity.getEnterpriseCode());
					prjModel.setConstructionChargeBy(secondcharge);
					prjModel.setConstructionUnit(entity.getCliendId().toString());
					prjRemote.update(prjModel);
				}
			  }
			if (entity.getConId() == null) {
				entity.setConId(bll.getMaxId("CON_J_CONTRACT_INFO", "CON_ID"));
			}
			Long conTypeId = entity.getConTypeId();
			if(entity.getConTypeId()==1l){
				entity.setConttreesNo(this.createCGCode(markCode, conTypeId));
			}else {
				entity.setConttreesNo(this.createConttreesNo(markCode, conTypeId));
			}
			
			entity.setIsUse("Y");
			entity.setEndDate(new Date());
			entity.setFileStatus("DRF");
			entityManager.persist(entity);
			if (condoc != null) {
				condoc.setKeyId(entity.getConId());
				docremote.save(condoc);
			}
			return entity;
		} catch (RuntimeException re) {

			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(ConJContractInfo entity) throws CodeRepeatException {
		entity.setIsUse("N");
		String paysql = "update CON_J_PAYMENT_PLAN t set t.is_use='N' where t.CON_ID=?";
		String docsql = "update CON_J_CON_DOC t set t.is_use='N' where t.KEY_ID=?";
		// ConJConDoc condoc=
		// List list=docremote.findConDocList(enterpriseCode, keyId, docType)
		bll.exeNativeSQL(paysql, new Object[] { entity.getConId() });
		bll.exeNativeSQL(docsql, new Object[] { entity.getConId() });
		this.update(entity, null, null);
		if (entity.getProjectId() != null && !"".equals(entity.getProjectId())) {
			PrjJInfo prjModel = prjRemote.findByPrjNo(entity.getProjectId(),
					entity.getEnterpriseCode());
			prjModel.setConstructionChargeBy("");
			prjModel.setConstructionUnit("");
			prjRemote.update(prjModel);
		}
	}

	public ConJContractInfo update(ConJContractInfo entity, ConJConDoc condoc,
			String secondcharge) throws CodeRepeatException {
		try {
			if (secondcharge != null && !secondcharge.equals("")) {
				PrjJInfo prjModel = prjRemote.findByPrjNo(
						entity.getProjectId(), entity.getEnterpriseCode());
				prjModel.setConstructionChargeBy(secondcharge);
				prjModel.setConstructionUnit(entity.getCliendId().toString());
				prjRemote.update(prjModel);
			}
			ConJContractInfo result = entityManager.merge(entity);
			if (condoc != null) {
				if (condoc.getConDocId() != null)
					docremote.update(condoc);
				else
					docremote.save(condoc);
			}
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConJContractInfo findById(Long id) {
		try {
			ConJContractInfo instance = entityManager.find(
					ConJContractInfo.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findContractInfos(Long conTypeId, String enterpriseCode,
			String sDate, String eDate, String likeName, String workflowStatus,
			String oprateDept, String entryBy, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		// modify by drdu @2009/03/20and t.exec_flag='1'
		String sql = "select t.con_id,\n"
				+ "       t.conttrees_no,\n"
				+ "       t.contract_name,\n"
				+ "       t.cliend_id,\n"
				+ "       t.currency_type,\n"
				+ "       t.act_amount,\n"
				+ "       t.operate_by,\n"
				+ "       t.operate_dep_code,\n"
				+ "       getdeptname(t.operate_dep_code),\n"
				+ "       getworkername(t.operate_by),\n"
				+ "       to_char(t.sign_start_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       to_char(t.sign_end_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       t.workflow_status,\n"
				+ "       getclientName(t.cliend_id) clientName,\n"
				+ "       t.operate_lead_by,\n"
				+ "       getworkername(t.operate_lead_by),\n"
				+ "       to_char(t.start_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       to_char(t.end_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       t.file_status,\n"
				+ "       t.workflow_no,\n"
				+ "      ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = t.currency_type) currencyName\n"
				+ "  from con_j_contract_info t\n"
				+ " where t.is_use = 'Y' and t.exec_flag='1' ";

		String sqlcount = "select count(1)\n" + "from con_j_contract_info t\n"
				+ "where t.is_use = 'Y' and t.exec_flag='1' ";
		/**
		 * String sql = "select t.con_id,\n" + " t.conttrees_no,\n" + "
		 * t.contract_name,\n" + " t.cliend_id,\n" + " t.currency_type,\n" + "
		 * t.act_amount,\n" + " t.operate_by,\n" + " t.operate_dep_code,\n" + "
		 * getdeptname(t.operate_dep_code),\n" + "
		 * getworkername(t.operate_by),\n" + "
		 * to_char(t.sign_start_date,'yyyy-MM-dd hh24:mi:ss'),\n" + "
		 * to_char(t.sign_end_date,'yyyy-MM-dd hh24:mi:ss'),\n" + "
		 * t.workflow_status,\n" + " c.client_name,\n" + " t.operate_lead_by,\n" + "
		 * getworkername(t.operate_lead_by),\n" + "
		 * to_char(t.start_date,'yyyy-MM-dd
		 * hh24:mi:ss'),\n"+"to_char(t.end_date,'yyyy-MM-dd hh24:mi:ss'),\n" + "
		 * t.file_status" + " from con_j_contract_info t, con_j_client_info c\n" + "
		 * where t.is_use = 'Y'\n" // + " and t.cliend_id = c.cliend_id\n" + "
		 * and c.use_flag='Y'"; // + " and c.use_flag='Y'"; + " and
		 * c.is_use='Y'"; String sqlcount = "select count(1)\n" + " from
		 * con_j_contract_info t, con_j_client_info c\n" + " where t.is_use =
		 * 'Y'\n" + " and t.cliend_id = c.cliend_id\n" // + " and
		 * c.use_flag='Y'\n"; + " and c.is_use='Y'";
		 */
		String whereStr = "";
		if (conTypeId != null && !conTypeId.equals("")) {
			whereStr += "  and t.con_type_id='" + conTypeId + "'";
		}
		if (enterpriseCode != null && enterpriseCode.length() > 0) {
			whereStr += " and t.enterprise_code='" + enterpriseCode + "'\n";
		}
		if (sDate != null && sDate.length() > 0) {
			whereStr += " and t.sign_start_date>to_date('" + sDate
					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (eDate != null && eDate.length() > 0) {
			whereStr += " and t.sign_start_date<to_date('" + eDate
					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (likeName != null && likeName.length() > 0) { // modify by drdu
			// 09/04/27
			whereStr += " and t.conttrees_no||t.contract_name||getclientname(t.cliend_id) like "
					+ "'%" + likeName + "%'\n";
		}
		if (workflowStatus != null && workflowStatus.length() > 0) {
			whereStr += " and t.workflow_status in (2,4)\n";
		}
		if (oprateDept != null && oprateDept.length() > 0) {
			whereStr += " and (t.operate_dep_code='" + oprateDept + "'";
			if (entryBy != null && !"".equals(entryBy)) {
				whereStr += " or t.entry_by='" + entryBy + "')";
			} else {
				whereStr += ")";
			}
		}
		sql += whereStr;
		sqlcount += whereStr;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it = list.iterator();
		List<ContractInfo> arrlist = new ArrayList();
		while (it.hasNext()) {
			ContractInfo con = new ContractInfo();
			Object[] o = (Object[]) it.next();
			con.setConId(Long.parseLong(o[0].toString()));
			if (o[1] != null)
				con.setConttreesNo(o[1].toString());
			if (o[2] != null)
				con.setContractName(o[2].toString());
			if (o[3] != null)
				con.setCliendId(Long.parseLong(o[3].toString()));
			if (o[4] != null)
				con.setCurrencyType(Long.parseLong(o[4].toString()));
			if (o[5] != null)
				con.setActAmount(Double.parseDouble(o[5].toString()));
			if (o[6] != null)
				con.setOperateBy(o[6].toString());
			if (o[7] != null)
				con.setOperateDepCode(o[7].toString());
			if (o[8] != null)
				con.setOperateDeptName(o[8].toString());
			if (o[9] != null)
				con.setOperateName(o[9].toString());
			if (o[10] != null)
				con.setSignStartDate(o[10].toString());
			if (o[11] != null)
				con.setSignEndDate(o[11].toString());
			if (o[12] != null)
				con.setWorkflowStatus(Long.parseLong(o[12].toString()));
			if (o[13] != null)
				con.setClientName(o[13].toString());
			if (o[14] != null)
				con.setOperateLeadBy(o[14].toString());
			if (o[15] != null)
				con.setOperateLeadName(o[15].toString());
			if (o[16] != null)
				con.setStartDate(o[16].toString());
			if (o[17] != null)
				con.setEndDate(o[17].toString());
			if (o[18] != null)
				con.setFileStatue(o[18].toString());
			if (o[19] != null)
				con.setWorkflowNo(Long.parseLong(o[19].toString()));
			if (o[20] != null)
				con.setCurrencyName(o[20].toString());
			arrlist.add(con);
		}
		pg.setList(arrlist);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlcount).toString()));
		return pg;
	}

	@SuppressWarnings("unchecked")
	public PageObject findContractSelect(Long conTypeId, String enterpriseCode,
			String likeName, String workflowStatus, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select t.con_id,\n"
				+ "       t.conttrees_no,\n"
				+ "       t.contract_name,\n"
				+ "       t.cliend_id,\n"
				+ "       t.currency_type,\n"
				+ "       t.act_amount,\n"
				+ "       t.operate_by,\n"
				+ "       t.operate_dep_code,\n"
				+ "       getdeptname(t.operate_dep_code),\n"
				+ "       getworkername(t.operate_by),\n"
				+ "       to_char(t.sign_start_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       to_char(t.sign_end_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       t.workflow_status,\n"
				+ "       getclientname(t.cliend_id) clientName,\n"
				+ "       t.operate_lead_by,\n"
				+ "       getworkername(t.operate_lead_by),\n"
				+ "       to_char(t.start_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       to_char(t.end_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "       t.file_status,\n"
				+ "  (select h.dept_id from hr_c_dept h where h.dept_code = t.operate_dep_code and  h.is_use = 'Y')  deptId \n "//update by sychen 20100902
//				+ "  (select h.dept_id from hr_c_dept h where h.dept_code = t.operate_dep_code and  h.is_use = 'U')  deptId \n "
				+ "  from con_j_contract_info t\n"
				+ " where t.is_use = 'Y'\n"
				+ "   and t.exec_flag = 1 \n"
				+ " and t.con_id not in ( select distinct(d.con_id) from con_j_modify d \n"
				+ " where d.workflow_status in (0,1,3) and d.is_use ='Y')";

		String sqlcount = "select count(1)\n"
				+ "  from con_j_contract_info t\n"
				+ " where t.is_use = 'Y'\n"
				+ "   and t.exec_flag = 1 \n"
				+ " and t.con_id not in ( select distinct(d.con_id) from con_j_modify d \n"
				+ " where d.workflow_status in (0,1,3) and d.is_use ='Y')";
		String whereStr = "";
		if (conTypeId != null && !conTypeId.equals("")) {
			whereStr += "  and t.con_type_id='" + conTypeId + "'";
		}
		if (enterpriseCode != null && enterpriseCode.length() > 0) {
			whereStr += " and t.enterprise_code='" + enterpriseCode + "'\n";
		}
		if (likeName != null && likeName.length() > 0) {
			whereStr += " and t.conttrees_no||t.contract_name||c.client_name like "
					+ "'%" + likeName + "%'\n";
		}
		if (workflowStatus != null && workflowStatus.length() > 0) {
			whereStr += " and t.workflow_status='" + workflowStatus + "'\n";
		}
		sql += whereStr;
		sqlcount += whereStr;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it = list.iterator();
		List<ContractInfo> arrlist = new ArrayList();
		while (it.hasNext()) {
			ContractInfo con = new ContractInfo();
			Object[] o = (Object[]) it.next();
			con.setConId(Long.parseLong(o[0].toString()));
			if (o[1] != null)
				con.setConttreesNo(o[1].toString());
			if (o[2] != null)
				con.setContractName(o[2].toString());
			if (o[3] != null)
				con.setCliendId(Long.parseLong(o[3].toString()));
			if (o[4] != null)
				con.setCurrencyType(Long.parseLong(o[4].toString()));
			if (o[5] != null)
				con.setActAmount(Double.parseDouble(o[5].toString()));
			if (o[6] != null)
				con.setOperateBy(o[6].toString());
			if (o[7] != null)
				con.setOperateDepCode(o[7].toString());
			if (o[8] != null)
				con.setOperateDeptName(o[8].toString());
			if (o[9] != null)
				con.setOperateName(o[9].toString());
			if (o[10] != null)
				con.setSignStartDate(o[10].toString());
			if (o[11] != null)
				con.setSignEndDate(o[11].toString());
			if (o[12] != null)
				con.setWorkflowStatus(Long.parseLong(o[12].toString()));
			if (o[13] != null)
				con.setClientName(o[13].toString());
			if (o[14] != null)
				con.setOperateLeadBy(o[14].toString());
			if (o[15] != null)
				con.setOperateLeadName(o[15].toString());
			if (o[16] != null)
				con.setStartDate(o[16].toString());
			if (o[17] != null)
				con.setEndDate(o[17].toString());
			if (o[18] != null)
				con.setFileStatue(o[18].toString());
			if (o[19] != null)
				con.setDeptId(Long.parseLong(o[19].toString()));
			arrlist.add(con);
		}
		pg.setList(arrlist);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlcount).toString()));
		return pg;
	}

	private String createConttreesNo(String markCode, Long conTypeId) {
		String mark = markCode;
		String sql = "select ? || to_char(sysdate, 'yyyy-') ||\n"
				+ " nvl(trim(to_char(max(substr(t.conttrees_no, length(t.conttrees_no) - 3))+ 1,'0000')),\n"
				+ "'0001')   from CON_J_CONTRACT_INFO t\n"
				+ "where t.con_year =  to_char(sysdate,'yyyy')and t.con_type_id = "
				+ conTypeId
				+ "and t.is_use = 'Y'";

		return bll.getSingal(sql, new Object[] { mark }).toString();

		// modified by xbj, 2009/5/22
	}

	/**
	 * add bjxu 采购合同编号规则
	 */
	@SuppressWarnings("unchecked")
	private String createCGCode(String markCode, Long conTypeId){
		String sql = "select '"+markCode+"' || nvl(trim(to_char(max(substr(t.conttrees_no,length(t.conttrees_no) - 2)) + 1, '000')), '001')\n" + 
			"  from con_j_contract_info t\n" + 
			"  where t.con_type_id="+conTypeId+"\n" + 
			"  and t.conttrees_no like '"+markCode+"%'";
		return bll.getSingal(sql).toString();
	}
	
	// modify by fyyang 0901016
	public ContractFullInfo getConFullInfoById(Long conId) {
		String sql = "select a.con_id,\n"
				+ "       a.workflow_status,\n"
				+ "       a.is_sum,\n"
				+ "       a.contract_name,\n"
				+ "       a.cliend_id,\n"
				+ "       getclientname(a.cliend_id) clientName,\n"
				+ "       a.Currency_Type,\n"
				+ "       a.operate_by,\n"
				+ "       getworkername(a.operate_by) operatebyname,\n"
				+ "       a.operate_dep_code,\n"
				+ "       getdeptname(a.operate_dep_code) operatedepname,\n"
				+ "       to_char(a.sign_start_date, 'yyyy-MM-dd hh24:mi:ss') sign_start_date,\n"
				+ "       to_char(a.sign_end_date, 'yyyy-MM-dd hh24:mi:ss') sign_end_date,\n"
				+ "       a.is_use,\n"
				+ "       a.enterprise_code,\n"
				+ "       a.conttrees_no,\n"
				+ "       a.operate_lead_by,\n"
				+ "       getworkername(a.operate_lead_by) operateleadby,\n"
				+ "       a.currency_type,\n"
				+ "       nvl((select b.modiy_act_amount from con_j_modify b where b.con_modify_no = (select max(c.con_modify_no) from con_j_modify c where c.is_use ='Y' and c.con_id = '"
				+ conId
				+ "') and b.is_use ='Y'),a.act_amount),\n"
				+ "       a.Is_Instant,\n"
				+ "       to_char(a.start_date, 'yyyy-MM-dd hh24:mi:ss') start_date,\n"
				+ "       to_char(a.end_date, 'yyyy-MM-dd hh24:mi:ss') end_date,\n"
				+ "       a.con_year,\n"
				+ "       a.entry_by,\n"
				+ "       getworkername(a.entry_by) entryby,\n"
				+ "       to_char(a.entry_date, 'yyyy-MM-dd hh24:mi:ss') entry_date,\n"
				+ "       a.CON_ABSTRACT,\n"
				+ "       a.is_sign,\n"
				+ "       a.Con_Type_Id,\n"
				+ "       a.Item_Id,\n"
				// + " b.con_type_desc,\n"
				+ " (select b.con_type_desc from con_c_con_type b where b.is_use = 'Y' and b.contype_id=a.con_type_id),\n"
				+ "       a.terminate_by,\n"
				+ "       getworkername(a.terminate_by) terminatebyname,\n"
				+ "       to_char(a.terminate_date, 'yyyy-MM-dd hh24:mi:ss') terminate_date,\n"
				+ "       a.terminate_memo,\n"
				+ " a.applied_amount, a.approved_amount, a.payed_amount,\n"
				+ "       (select c.bank\n"
				+ "          from con_j_clients_info c\n"
				+ "         where c.cliend_id = a.cliend_id\n"
				+ "           ) bankaccount,\n"
				+ "       (select c.account\n"
				+ "          from con_j_clients_info c\n"
				+ "         where c.cliend_id = a.cliend_id\n"
				+ "           ) account,\n"
				+ "       a.workflow_no,\n"
				+ " (select (t.ori_file_name || '.' || t.ori_file_ext) condec from con_j_con_doc t where t.key_id="
				+ conId
				+ " and t.doc_type='CON' and rownum=1),\n"
				+ "      a.third_cliend_id,\n"
				+ "      getclientname(a.third_cliend_id) clientThirdName,\n"
				+ "   ( select p.prj_name from prj_j_info p where p.prj_no = a.project_id) prjName,\n"
				+ "     a.project_id,\n"
				+ "   ( select prj.construction_charge_by from prj_j_info prj where prj.prj_no=a.project_id) constructionName,\n"
				+ "   ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = a.currency_type) currencyName,\n"
				+ "    getclientname(a.cliend_id), \n"
				+ "   ( select p.prj_no_show from prj_j_info p where p.prj_no = a.project_id) prjShow, \n"
				+ " a.warranty_period, \n"
				+ " a.OPERATE_TEL, \n"
				+ " a.OPERATE_ADVICE, \n"
				+ " to_char(a.OPERATE_DATE,'yyyy-mm-dd'), \n"
				+ " a.JYJH_ADVICE, \n"
				+ " to_char(a.JYJH_DATE,'yyyy-mm-dd'), \n"
				+ "a.prosy_by, \n"
				+ "getworkername(a.prosy_by) prosy_byname,\n"
				+ "a.prosy_start_date,\n"
				+ "a.prosy_end_date, \n"
				+ "a.prjtype_id, \n"
				+ " (select cc.prj_type_name from prj_c_type cc where cc.prj_type_id = a.prjtype_id) prj_type_name,\n"
				+ "(select tt.item_name from CON_C_ITEM_SOURCE tt\n"
				+ "where tt.item_code=a.item_id\n"
				+ "and tt.is_use='Y') myitemName,\n"
				+ "a.workflow_no_dg,"
				+ "a.con_code,"
				+ "a.if_secrity"
				+ "  from CON_J_CONTRACT_INFO a\n"
				+ " where a.con_id = "
				+ conId + "\n" + "   and a.is_use = 'Y'";

		Object[] data = (Object[]) bll.getSingal(sql);
		ContractFullInfo model = new ContractFullInfo();
		if (data[0] != null)
			model.setConId(Long.parseLong(data[0].toString()));
		if (data[1] != null)
			model.setWorkflowStatus(Long.parseLong(data[1].toString()));
		if (data[2] != null)
			model.setIsSum(data[2].toString());
		if (data[3] != null)
			model.setContractName(data[3].toString());
		if (data[4] != null)
			model.setCliendId(Long.parseLong(data[4].toString()));
		if (data[5] != null)
			model.setClientName(data[5].toString());
		if (data[6] != null)
			model.setCurrencyType(Long.parseLong(data[6].toString()));
		if (data[7] != null)
			model.setOperateBy(data[7].toString());
		if (data[8] != null)
			model.setOperateName(data[8].toString());
		if (data[9] != null)
			model.setOperateDepCode(data[9].toString());
		if (data[10] != null)
			model.setOperateDepName(data[10].toString());
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
			model.setIsInstant(data[20].toString());
		if (data[21] != null)
			model.setStartDate(data[21].toString());
		if (data[22] != null)
			model.setEndDate(data[22].toString());
		if (data[23] != null)
			model.setConYear(data[23].toString());
		if (data[24] != null)
			model.setEntryBy(data[24].toString());
		if (data[25] != null)
			model.setEntryName(data[25].toString());
		if (data[26] != null)
			model.setEntryDate(data[26].toString());
		if (data[27] != null)
			model.setConAbstract(data[27].toString());
		if (data[28] != null)
			model.setIsSign(data[28].toString());
		if (data[29] != null)
			model.setConTypeId(Long.parseLong(data[29].toString()));
		if (data[30] != null)
			model.setItemId(data[30].toString());
		if (data[31] != null)
			model.setConTypeName(data[31].toString());
		if (data[32] != null)
			model.setTerminateBy(data[32].toString());
		if (data[33] != null)
			model.setTerminateByName(data[33].toString());
		if (data[34] != null)
			model.setTerminateDate(data[34].toString());
		if (data[35] != null)
			model.setTerminateMome(data[35].toString());
		if (data[36] != null)
			model.setAppliedAmount(Double.valueOf(data[36].toString()));
		if (data[37] != null)
			model.setApprovedAmount(Double.valueOf(data[37].toString()));
		if (data[38] != null)
			model.setPayedAmount(Double.valueOf(data[38].toString()));
		if (data[39] != null)
			model.setBankAccount(data[39].toString());
		if (data[40] != null)
			model.setAccount(data[40].toString());
		if (data[41] != null) {
			model.setWorkflowNo(Long.parseLong(data[41].toString()));
		}
		if (data[42] != null) {
			model.setFilePath(data[42].toString());
		}
		if (data[43] != null) {
			model.setThirdClientId(Long.parseLong(data[43].toString()));
		}
		if (data[44] != null) {
			model.setThirdClientName(data[44].toString());
		}
		if (data[45] != null) {
			model.setProjectName(data[45].toString());
		}
		if (data[46] != null) {
			model.setProjectId(data[46].toString());
		}
		if (data[47] != null) {
			model.setConstructionName(data[47].toString());
		}
		if (data[48] != null) {
			model.setCurrencyName(data[48].toString());
		}
		if (data[49] != null) {
			model.setCliendName(data[49].toString());
		}
		if (data[50] != null) {
			model.setPrjShow(data[50].toString());

		}
		if (data[51] != null && !(data[51].equals(""))) {
			model.setWarrantyPeriod(data[51].toString());
		}
		if (data[52] != null)
			model.setOperateTel(data[52].toString());
		if (data[53] != null)
			model.setOperateAdvice(data[53].toString());
		if (data[54] != null)
			model.setOperateDate(data[54].toString());
		if (data[55] != null)
			model.setJyjhAdvice(data[55].toString());
		if (data[56] != null)
			model.setJyjhDate(data[56].toString());
		if (data[57] != null)
			model.setProsy_by(data[57].toString());
		if (data[58] != null)
			model.setProsy_byName(data[58].toString());
		if (data[59] != null)
			model.setProsyStartDate(data[59].toString());
		if (data[60] != null)
			model.setProsyEndDate(data[60].toString());
		if (data[61] != null)
			model.setPrjtypeId(Long.parseLong(data[61].toString()));
		if (data[62] != null)
			model.setPrjTypeName(data[62].toString());
		if (data[63] != null) {
			model.setItemName(data[63].toString());
		}
		if(data[64] != null)
			model.setWorkflowNoDg(Long.parseLong(data[64].toString()));
		if(data[65] != null)
			model.setConCode(data[65].toString());
		if(data[66] != null)
			model.setIfSecrity(data[66].toString());
		// List<ConDocForm> mconlist = docremote.findConDocList(
		// model.getEnterpriseCode(), model.getConId(), "CON").getList();
		// List<ConDocForm> mconAttlist = docremote.findConDocList(
		// model.getEnterpriseCode(), model.getConId(), "CONATT").getList();
		// List<ConDocForm> mconEvilist = docremote.findConDocList(
		// model.getEnterpriseCode(), model.getConId(), "CONEVI").getList();
		// List<PaymentPlanForm> paylist =
		// payremote.findByConId(model.getConId());
		// if(paylist!=null)
		// model.setPaylist(paylist);
		// if(mconlist!=null)
		// model.setMconlist(mconlist);
		// if(mconAttlist!=null)
		// model.setMconAttlist(mconAttlist);
		// if(mconEvilist!=null)
		// model.setMconEvilist(mconEvilist);
		return model;

	}

	public PageObject findReportContractList(String workCode, Long conTypeId,
			String enterpriseCode, String sDate, String eDate, String likeName,
			String oprateDept, String entryBy, String status, String entryIds,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select t.con_id,\n"
				+ "       t.conttrees_no,\n"
				+ "       t.contract_name,\n"
				+ "       t.cliend_id,\n"
				+ "       t.currency_type,\n"
				+ "       t.act_amount,\n"
				+ "       t.operate_by,\n"
				+ "       t.operate_dep_code,\n"
				+ "       getdeptname(t.operate_dep_code),\n"
				+ "       getworkername(t.operate_by),\n"
				+ "       to_char(t.sign_start_date, 'yyyy-MM-dd hh24:mi:ss') sign_start_date,\n"
				+ "       to_char(t.sign_end_date, 'yyyy-MM-dd hh24:mi:ss') sign_end_date,\n"
				+ "       t.workflow_status,\n"
				+ "       getclientname(t.cliend_id) clientName,\n"
				+ "       t.workflow_no,\n"
				+ "      ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = t.currency_type) currencyName,\n"
				+ "      ( select prj.construction_charge_by from prj_j_info prj where prj.prj_no=t.project_id) constructionName,t.jyjh_advice\n"
				+ "	from con_j_contract_info t\n" + " where t.is_use = 'Y'";
		String sqlcount = "select count(1)\n"
				+ "  from con_j_contract_info t\n" + " where t.is_use = 'Y'\n";
		String whereStr = "";
		if (conTypeId != null && !conTypeId.equals("")) {
			whereStr += "  and t.con_type_id='" + conTypeId + "'";
		}
		if (enterpriseCode != null && enterpriseCode.length() > 0) {
			whereStr += " and t.enterprise_code='" + enterpriseCode + "'\n";
		}
		if (sDate != null && sDate.length() > 0) {
			whereStr += " and t.sign_start_date>to_date('" + sDate
					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (eDate != null && eDate.length() > 0) {
			whereStr += "and t.sign_start_date<to_date('" + eDate
					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (likeName != null && likeName.length() > 0) {
			whereStr += " and t.conttrees_no||t.contract_name|| getClientName(t.cliend_id) like "
					+ "'%" + likeName + "%'\n";
		}
		if (status != null) {
			if (status.equals("report")) {
				whereStr += " and t.workflow_status in (0,3)\n";
			} else if (status.equals("approve")) {
				whereStr += " and (t.workflow_status = 1)\n";
//				if (entryIds != null && (!"".equals(entryIds))) {
					whereStr += " and t.workflow_no in (" + entryIds + ")\n";
//				}
			} else if (status.equals("endsign")) {
				whereStr += " and t.workflow_status in(2,4)\n";
//				whereStr += " and t.workflow_no in (" + entryIds + ")\n";
//				whereStr += " and t.workflow_no in (" + entryIds + ")\n";
			}
		}
		if (oprateDept != null && oprateDept.length() > 0) {
			whereStr += " and (t.operate_dep_code='" + oprateDept + "'";
			if (entryBy != null && !"".equals(entryBy)) {
				whereStr += " or t.entry_by='" + entryBy + "')";
			} else {
				whereStr += ")";
			}
		}
		if (!workCode.equals("")) {
			whereStr += " and t.operate_by = '" + workCode + "'";
		}
		sql += whereStr;
		sql += " order by t.con_id";
		sqlcount += whereStr;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it = list.iterator();
		List<ContractInfo> arrlist = new ArrayList();
		while (it.hasNext()) {
			ContractInfo con = new ContractInfo();
			Object[] o = (Object[]) it.next();
			con.setConId(Long.parseLong(o[0].toString()));
			if (o[1] != null)
				con.setConttreesNo(o[1].toString());
			if (o[2] != null)
				con.setContractName(o[2].toString());
			if (o[3] != null)
				con.setCliendId(Long.parseLong(o[3].toString()));
			if (o[4] != null)
				con.setCurrencyType(Long.parseLong(o[4].toString()));
			if (o[5] != null)
				con.setActAmount(Double.parseDouble(o[5].toString()));
			if (o[6] != null)
				con.setOperateBy(o[6].toString());
			if (o[7] != null)
				con.setOperateDepCode(o[7].toString());
			if (o[8] != null)
				con.setOperateDeptName(o[8].toString());
			if (o[9] != null)
				con.setOperateName(o[9].toString());
			if (o[10] != null)
				con.setSignStartDate(o[10].toString());
			if (o[11] != null)
				con.setSignEndDate(o[11].toString());
			if (o[12] != null)
				con.setWorkflowStatus(Long.parseLong(o[12].toString()));
			if (o[13] != null)
				con.setClientName(o[13].toString());
			if (o[14] != null)
				con.setWorkflowNo(Long.parseLong(o[14].toString()));
			if (o[15] != null)
				con.setCurrencyName(o[15].toString());
			if (o[16] != null)
				con.setConstructionName(o[16].toString());
			if (o[17] != null)
				con.setJhFlg(o[17].toString());
			arrlist.add(con);
		}
		pg.setList(arrlist);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlcount).toString()));
		return pg;
	}

	public PageObject findContractTerminateList(Long conTypeId,
			String enterpriseCode, String fuzzy,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = null;
			String sqlTer = "select t.con_id,\n"
					+ "       t.conttrees_no,\n"
					+ "       t.contract_name,\n"
					+ "       t.cliend_id,\n"
					+ "       getclientname(t.cliend_id) clientname,\n"
					+ "       t.is_sum,\n"
					+ "       t.act_amount,\n"
					+ "       t.currency_type,\n"
					+ "       t.APPROVED_AMOUNT,\n"
					+ "       t.operate_by,\n"
					+ "       getworkername(t.operate_by) operatebyname,\n"
					+ "       t.operate_dep_code,\n"
					+ "       getdeptname(t.operate_dep_code) deptname,\n"
					+ "       t.start_date,\n"
					+ "       t.end_date,\n"
					+ "       t.is_use,\n"
					+ "       t.enterprise_code,\n"
					+ "       decode(t.is_sum, 'N', null,'Y', t.act_amount - nvl(t.APPROVED_AMOUNT, 0), '0.0000') unliquidate,\n"
					+ "       (select max(r.con_modify_id)\n"
					+ "          from CON_J_MODIFY r\n"
					+ "         where r.con_id = t.con_id\n"
					+ "           and r.is_use = 'Y') con_modify_id,\n"
					+ "       t.item_id,\n"
					+ "      ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = t.currency_type) currencyName\n"
					+ "  from con_j_contract_info t\n"
					+ " where t.is_use = 'Y'\n"
					+ "   and t.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and t.con_type_id='" + conTypeId + "'"
					+ "   and t.exec_flag = 1";

			String sqlTerCount = "select count(1)\n"
					+ "  from con_j_contract_info t\n"
					+ " where t.is_use = 'Y'\n"
					+ "   and t.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and t.exec_flag = 1";

			if (fuzzy != null && !fuzzy.equals("")) {
				String strWhere = " and t.conttrees_no||t.contract_name||getClientName(t.cliend_id) like '%"
						+ fuzzy + "%'";
				sqlTer += strWhere;
				sqlTerCount += strWhere;
			}
			sqlTer += " order by t.conttrees_no";

			List list = bll.queryByNativeSQL(sqlTer, rowStartIdxAndCount);
			List arraylist = new ArrayList();
			if (list != null && list.size() > 0) {
				result = new PageObject();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					ContractInfo info = new ContractInfo();
					Object[] data = (Object[]) it.next();
					info.setConId(Long.parseLong(data[0].toString()));
					if (data[1] != null)
						info.setConttreesNo(data[1].toString());
					if (data[2] != null)
						info.setContractName(data[2].toString());
					if (data[3] != null)
						info.setCliendId(Long.parseLong(data[3].toString()));
					if (data[4] != null)
						info.setClientName(data[4].toString());
					if (data[5] != null)
						info.setIsSum(data[5].toString());
					if (data[6] != null)
						info.setActAmount(Double
								.parseDouble(data[6].toString()));
					if (data[7] != null)
						info
								.setCurrencyType(Long.parseLong(data[7]
										.toString()));
					if (data[8] != null)
						info.setPayedAmount(Double.parseDouble(data[8]
								.toString()));
					if (data[9] != null)
						info.setOperateBy(data[9].toString());
					if (data[10] != null)
						info.setOperateName(data[10].toString());
					if (data[11] != null)
						info.setOperateDepCode(data[11].toString());
					if (data[12] != null)
						info.setOperateDeptName(data[12].toString());
					if (data[13] != null)
						info.setStartDate(data[13].toString());
					if (data[14] != null)
						info.setEndDate(data[14].toString());
					if (data[15] != null)
						info.setIsUse(data[15].toString());
					if (data[16] != null)
						info.setEnterpriseCode(data[16].toString());
					if (data[17] != null)
						info.setUnliquidate(Double.parseDouble(data[17]
								.toString()));
					if (data[18] != null)
						info
								.setConModifyId(Long.parseLong(data[18]
										.toString()));
					if (data[19] != null)
						info.setItemId(data[19].toString());
					if (data[20] != null)
						info.setCurrencyName(data[20].toString());
					arraylist.add(info);
				}
				Long totalCount = Long.parseLong(bll.getSingal(sqlTerCount)
						.toString());
				result.setList(arraylist);
				result.setTotalCount(totalCount);
			}
			return result;
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 验收终止 form
	 * 
	 * @throws CodeRepeatException
	 */
	public ConJContractInfo saveConTerminate(ConJContractInfo entity)
			throws CodeRepeatException {
		entity.setExecFlag(4l);
		entity.setTerminateDate(new Date());
		// entity.setTerminateMome(entity.getTerminateMome());
		return conRemote.update(entity, null, null);
	}

	@SuppressWarnings("unchecked")
	public PageObject findContractArchiveList(Long conTypeId, String startDate,
			String endDate, String enterprisecode, String fuzzy,
			final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql1 = "select t.con_id,\n"
				+ "       t.file_status,\n"
				+ "       t.conttrees_no,\n"
				+ "       t.contract_name,\n"
				+ "       t.cliend_id,\n"
				+ "       getclientname(t.cliend_id) clientName,\n"
				+ "       t.act_amount,\n"
				+ "       t.currency_type,\n"
				+ "       getworkername(t.operate_by) operate_by,\n"
				+ "       getdeptname(t.operate_dep_code) operate_dep,\n"
				+ "       to_char(t.sign_start_date, 'yyyy-MM-dd hh24:mi:ss') sign_start_date,\n"
				+ "       to_char(t.sign_end_date, 'yyyy-MM-dd hh24:mi:ss') sign_end_date,\n"
				+ "       t.exec_flag,\n"
				+ "       t.workflow_no,\n"
				+ "      ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = t.currency_type) currencyName,\n"
				+ "      ( select prj.construction_charge_by from prj_j_info prj where prj.prj_no=t.project_id) constructionName,\n"
				+ "   1 as YHT  ,\n" + "   0 as WY \n"
				+ "  from con_j_contract_info t\n"
				+ " where t.file_status in ('DRF', 'BAK')\n"
				+ "   and t.exec_flag <> 0\n" + "   and t.is_use = 'Y'";
		String sqlcount1 = "select count(1)\n"
				+ "  from con_j_contract_info t\n"
				+ " where t.file_status in ('DRF', 'BAK')\n"
				+ "   and t.exec_flag <> 0\n" + "   and t.is_use = 'Y'";

		String sqlcount2 = "select count(1)\n"
				+ "  from con_j_modify a, con_j_contract_info b\n"
				+ " where a.con_id = b.con_id\n"
				+ "   and a.file_status in ('DRF', 'BAK')\n"
				+ "   and b.exec_flag <> 0\n" + "   and a.is_use = 'Y'\n"
				+ "   and b.workflow_status=2\n"
				+ "   and a.workflow_status=2\n";

		String sql2 = "select a.con_modify_id,\n"
				+ "       a.file_status,\n"
				+ "       a.con_modify_no,\n"
				+ "       b.contract_name,\n"
				+ "       b.cliend_id,\n"
				+ "       getclientname(b.cliend_id),\n"
				+ "       b.act_amount,\n"
				+ "       b.currency_type,\n"
				+ "       getworkername(a.operate_by),\n"
				+ "       getdeptname(a.operate_dep_code),\n"
				+ "       to_char(a.sign_start_date, 'yyyy-MM-dd hh24:mi:ss') sign_start_date,\n"
				+ "       to_char(a.sign_end_date, 'yyyy-MM-dd hh24:mi:ss') sign_end_date,\n"
				+ "       b.exec_flag,\n"
				+ "       a.work_flow_no,\n"
				+ "       (select sys.currency_name\n"
				+ "          from SYS_C_CURRENCY sys\n"
				+ "         where sys.currency_id = b.currency_type) currencyName,\n"
				+ "       (select prj.construction_charge_by\n"
				+ "          from prj_j_info prj\n"
				+ "         where prj.prj_no = b.project_id) constructionName,\n"
				+ "        2 as BGHT, " + "         b.con_id "
				+ "  from con_j_modify a, con_j_contract_info b\n"
				+ " where a.con_id = b.con_id\n"
				+ "   and a.file_status in ('DRF', 'BAK')\n"
				+ "   and b.exec_flag <> 0\n" + "   and a.is_use = 'Y'\n" +
				// " and b.con_type_id = '2'\n" +
				// " and b.enterprise_code='hfdc'\n" +
				"   and b.workflow_status=2\n" + "   and a.workflow_status=2\n";
		// " and a.enterprise_code = 'hfdc'\"
		// " and a.enterprise_code = 'hfdc'";

		String whereStr = "";
		String whereStr2 = "";
		if (conTypeId != null && !conTypeId.equals("")) {
			whereStr += "  and t.con_type_id='" + conTypeId + "'";
			whereStr2 += "  and b.con_type_id='" + conTypeId + "'";
		}
		if (enterprisecode != null && enterprisecode.length() > 0) {
			whereStr += " and t.enterprise_code='" + enterprisecode + "'\n";
			whereStr2 += " and a.enterprise_code='" + enterprisecode + "'\n"
					+ " and b.enterprise_code='" + enterprisecode + "'\n";
		}
		if (startDate != null && startDate.length() > 0) {
			whereStr += " and t.sign_end_date>to_date('" + startDate
					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
			whereStr2 += " and a.sign_end_date>to_date('" + startDate
					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (endDate != null && endDate.length() > 0) {
			whereStr += "and t.sign_end_date<to_date('" + endDate
					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
			whereStr2 += "and a.sign_end_date<to_date('" + endDate
					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (fuzzy != null && fuzzy.length() > 0) {
			whereStr += " and t.conttrees_no||t.contract_name||getClientName(t.cliend_id) like "
					+ "'%" + fuzzy + "%'\n";
			whereStr2 += " and b.conttrees_no||b.contract_name||getClientName(b.cliend_id) like "
					+ "'%" + fuzzy + "%'\n";
		}
		sql1 += whereStr;
		sqlcount1 += whereStr;

		sqlcount2 += whereStr2;
		sql2 += whereStr2;

		String sql = sql1 + " union all \n" + sql2;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it = list.iterator();
		List<ContractInfo> arrlist = new ArrayList();
		while (it.hasNext()) {
			ContractInfo con = new ContractInfo();
			Object[] o = (Object[]) it.next();
			con.setConId(Long.parseLong(o[0].toString()));
			if (o[1] != null)
				con.setFileStatue(o[1].toString());
			if (o[2] != null)
				con.setConttreesNo(o[2].toString());
			if (o[3] != null)
				con.setContractName(o[3].toString());
			if (o[4] != null)
				con.setCliendId(Long.parseLong(o[4].toString()));
			if (o[5] != null)
				con.setClientName(o[5].toString());
			if (o[6] != null)
				con.setActAmount(Double.parseDouble(o[6].toString()));
			if (o[7] != null)
				con.setCurrencyType(Long.parseLong(o[7].toString()));
			if (o[8] != null)
				con.setOperateName(o[8].toString());
			if (o[9] != null)
				con.setOperateDeptName(o[9].toString());
			if (o[10] != null)
				con.setSignStartDate(o[10].toString());
			if (o[11] != null)
				con.setSignEndDate(o[11].toString());
			if (o[12] != null)
				con.setExecFlag(Long.parseLong(o[12].toString()));
			if (o[13] != null)
				con.setWorkflowNo(Long.parseLong(o[13].toString()));
			if (o[14] != null)
				con.setCurrencyName(o[14].toString());
			if (o[15] != null)
				con.setConstructionName(o[15].toString());
			if (o[16] != null)
				con.setTypeChoose(o[16].toString());
			if (o[17] != null)
				con.setTypeConId(o[17].toString());
			arrlist.add(con);
		}
		pg.setList(arrlist);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlcount1).toString())
				+ Long.parseLong(bll.getSingal(sqlcount2).toString()));
		return pg;
	}

	public PageObject findArchiveList(String startDate, String endDate,
			String enterprisecode, String status, String conNo, String conName,
			String client, String fileNo, Long conTypeId,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		// modified by bjxu 2009/7/9
		// System.out.println(conTypeId);

		String sql1 = "select t.con_id,\n"
				+ "       t.file_status,\n"
				+ "       t.conttrees_no,\n"
				+ "       t.contract_name,\n"
				+ "       t.cliend_id,\n"
				+ "       getclientname(t.cliend_id) clientName,\n"
				+ "       t.act_amount,\n"
				+ "       getworkername(t.operate_by) operate_by,\n"
				+ "       getdeptname(t.operate_dep_code) operate_dep,\n"
				+ "       to_char(t.sign_start_date, 'yyyy-MM-dd hh24:mi:ss') sign_start_date,\n"
				+ "       to_char(t.sign_end_date, 'yyyy-MM-dd hh24:mi:ss') sign_end_date,\n"
				+ "       t.page_count,\n"
				+ "       t.file_count,\n"
				+
				// " t.file_no,\n" +
				"       t.con_type_id,\n"
				+ "       decode(t.is_sum,'Y','有','N','无') is_sum,\n"
				+ "       to_char(t.file_date, 'yyyy-MM-dd hh24:mi:ss') file_date,\n"
				+ "       t.file_no,\n"
				+ "       t.file_memo,\n"
				+ "      ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = t.currency_type) currencyName\n"
				+ "  from con_j_contract_info t\n" + " where t.is_use = 'Y'";

		String sqlcount1 = "select count(1)\n"
				+ "  from con_j_contract_info t\n" + " where t.is_use = 'Y'";

		String sql2 = "select r.con_id,\n"
				+ "       r.file_status,\n"
				+ "       r.con_modify_no,\n"
				+ "       a.contract_name,\n"
				+ "       a.cliend_id,\n"
				+ "       getClientName(a.cliend_id) clientName,\n"
				+ "       a.act_amount,\n"
				+ "        getworkername(r.operate_by) operate_by,\n"
				+ "       getdeptname(r.operate_dep_code) operate_dep,\n"
				+ "        to_char(r.sign_start_date, 'yyyy-MM-dd hh24:mi:ss') sign_start_date,\n"
				+ "       to_char(r.sign_end_date, 'yyyy-MM-dd hh24:mi:ss') sign_end_date,\n"
				+ "       r.page_count,\n"
				+ "       r.file_count,\n"
				+
				// " a.file_no,\n" +
				"       r.con_modify_id,\n"
				+ "       decode(a.is_sum,'Y','有','N','无') is_sum,\n"
				+ "       to_char(r.file_date, 'yyyy-MM-dd hh24:mi:ss') file_date,\n"
				+ "       r.file_no,\n"
				+ "       r.file_memo,\n"
				+ "      ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = a.currency_type) currencyName\n"
				+ "  from con_j_modify r, con_j_contract_info a\n"
				+ "  where a.con_id=r.con_id\n" + "  and r.is_use='Y'\n"
				+ "  and a.is_use='Y'";

		String sqlcount2 = "select count(1)\n"
				+ "  from con_j_modify r,con_j_contract_info a\n"
				+ "  where a.con_id=r.con_id\n" + "  and r.is_use='Y'\n"
				+ "  and a.is_use='Y'";
		String whereStr = "";
		String str = "";
		if (conTypeId != null) {
			whereStr += " and t.con_type_id = " + conTypeId;
			str += " and a.con_type_id = " + conTypeId;
		}

		if (enterprisecode != null && enterprisecode.length() > 0) {
			whereStr += " and t.enterprise_code='" + enterprisecode + "'\n";
			str += " and r.enterprise_code='" + enterprisecode
					+ "' and a.enterprise_code='" + enterprisecode + "'\n";
		}
		if (status != null && "confirm".equals(status)) {
			whereStr += " and t.file_status = 'PRE' \n";
			str += " and r.file_status = 'PRE'\n";
			if (startDate != null && startDate.length() > 0) {
				whereStr += " and t.sign_end_date>to_date('" + startDate
						+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
				str += " and a.sign_end_date>to_date('" + startDate
						+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
			}
			if (endDate != null && endDate.length() > 0) {
				whereStr += " and t.sign_end_date<to_date('" + endDate
						+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
				str += " and a.sign_end_date<to_date('" + endDate
						+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
			}
		} else if (status != null && "file".equals(status)) {
			whereStr += " and t.file_status = 'OK' \n";
			str += " and r.file_status = 'OK'\n";
			if (startDate != null && startDate.length() > 0) {
				whereStr += " and t.file_date>to_date('" + startDate
						+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
				str += " and r.file_date>to_date('" + startDate
						+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
			}
			if (endDate != null && endDate.length() > 0) {
				whereStr += " and t.file_date<to_date('" + endDate
						+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
				str += " and r.file_date<to_date('" + endDate
						+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
			}
		}

		if (conNo != null && conNo.length() > 0) {
			whereStr += " and t.conttrees_no like '%" + conNo + "%'\n";
			str += " and r.con_modify_no like '%" + conNo + "%'\n";
		}
		if (conName != null && conName.length() > 0) {
			whereStr += " and t.contract_name like '%" + conName + "%'\n";
			str += " and a.contract_name like '%" + conName + "%'\n";
		}
		if (client != null && client.length() > 0) {
			whereStr += " and t.cliend_id = '" + client + "'\n";
			str += " and a.cliend_id = '" + client + "'\n";
		}
		if (fileNo != null && fileNo.length() > 0) {
			whereStr += " and t.file_no like '%" + fileNo + "%'\n";
			str += " and r.file_no like '%" + fileNo + "%'\n";
		}
		sql1 += whereStr;
		sqlcount1 += whereStr;
		sql2 += str;
		sqlcount2 += str;
		String sql = sql1 + "union all " + sql2;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		// List list2 = bll.queryByNativeSQL(sql2,rowStartIdxAndCount);
		Iterator it = list.iterator();
		List<ContractInfo> arrlist = new ArrayList();
		while (it.hasNext()) {
			ContractInfo con = new ContractInfo();
			Object[] o = (Object[]) it.next();
			con.setConId(Long.parseLong(o[0].toString()));
			if (o[1] != null)
				con.setFileStatue(o[1].toString());
			if (o[2] != null)
				con.setConttreesNo(o[2].toString());
			if (o[3] != null)
				con.setContractName(o[3].toString());
			if (o[4] != null)
				con.setCliendId(Long.parseLong(o[4].toString()));
			if (o[5] != null)
				con.setClientName(o[5].toString());
			if (o[6] != null)
				con.setActAmount(Double.parseDouble(o[6].toString()));
			if (o[7] != null)
				con.setOperateName(o[7].toString());
			if (o[8] != null)
				con.setOperateDeptName(o[8].toString());
			if (o[9] != null)
				con.setSignStartDate(o[9].toString());
			if (o[10] != null)
				con.setSignEndDate(o[10].toString());
			if (o[11] != null)
				con.setPageCount(o[11].toString());
			if (o[12] != null)
				con.setFileCount(Long.parseLong(o[12].toString()));
			if (o[13] != null)
				con.setConTypeId(Long.parseLong(o[13].toString()));
			// if (o[15] != null) {
			// con.setConTypeName(o[15].toString());
			// } else {
			// con.setConTypeName("");
			// }
			if (o[14] != null)
				con.setIsSum(o[14].toString());
			if (o[15] != null)
				con.setFileDate(o[15].toString());
			if (o[16] != null) {
				con.setFileNo(o[16].toString());
			} else {
				con.setFileNo("");
			}
			if (o[17] != null) {
				con.setFileMemo(o[17].toString());
			} else {
				con.setFileMemo("");
			}
			if (o[18] != null)
				con.setCurrencyName(o[18].toString());
			arrlist.add(con);
		}
		// Iterator itx = list2.iterator();
		// while (itx.hasNext()) {
		// ContractInfo con = new ContractInfo();
		// Object[] o = (Object[]) itx.next();
		// con.setConId(Long.parseLong(o[0].toString()));
		// if (o[1] != null)
		// con.setFileStatue(o[1].toString());
		// if (o[2] != null)
		// con.setConttreesNo(o[2].toString());
		// if (o[3] != null)
		// con.setContractName(o[3].toString());
		// if (o[4] != null)
		// con.setCliendId(Long.parseLong(o[4].toString()));
		// if (o[5] != null)
		// con.setClientName(o[5].toString());
		// if (o[6] != null)
		// con.setActAmount(Double.parseDouble(o[6].toString()));
		// if (o[7] != null)
		// con.setConTypeId(Long.parseLong(o[7].toString()));
		// if (o[8] != null) {
		// con.setConTypeName(o[8].toString());
		// } else {
		// con.setConTypeName("");
		// }
		// if (o[9] != null)
		// con.setOperateName(o[9].toString());
		// if (o[10] != null)
		// con.setOperateDeptName(o[10].toString());
		// if (o[11] != null)
		// con.setPageCount(o[11].toString());
		// if (o[12] != null)
		// con.setFileCount(Long.parseLong(o[12].toString()));
		// if (o[13] != null)
		// con.setFileNo(o[13].toString());
		// if (o[14] != null)
		// con.setConModifyId(Long.parseLong(o[14].toString()));
		// if (o[15] != null)
		// con.setIsSum(o[15].toString());
		// if (o[16] != null)
		// con.setFileDate(o[16].toString());
		// if (o[17] != null) {
		// con.setFileNo(o[17].toString());
		// } else {
		// con.setFileNo("");
		// }
		// if (o[18] != null) {
		// con.setFileMemo(o[18].toString());
		// } else {
		// con.setFileMemo("");
		// }
		// arrlist.add(con);
		// }
		pg.setList(arrlist);
		Long count = Long.parseLong(bll.getSingal(sqlcount1).toString())
				+ Long.parseLong(bll.getSingal(sqlcount2).toString());
		pg.setTotalCount(count);
		return pg;
	}

	public PageObject findExecContractList(String enterpriseCode, String sDate,
			String eDate, String fuzzy, String oprateDept,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select t.con_id,\n"
				+ "       t.conttrees_no,\n"
				+ "       t.contract_name,\n"
				+ "       t.cliend_id,\n"
				+ "       t.currency_type,\n"
				+ "       t.act_amount,\n"
				+ "       t.operate_by,\n"
				+ "       t.operate_dep_code,\n"
				+ "       getdeptname(t.operate_dep_code),\n"
				+ "       getworkername(t.operate_by),\n"
				+ "       to_char(t.start_date, 'yyyy-MM-dd hh24:mi:ss') start_date,\n"
				+ "       to_char(t.end_date, 'yyyy-MM-dd hh24:mi:ss') end_date,\n"
				+ "       t.workflow_status,\n"
				+ "       getClientName(t.cliend_id) cliend_name,\n"
				+ "       t.exec_flag\n" + "	from con_j_contract_info t\n"
				+ " where t.is_use = 'Y' and t.exec_flag=1";
		String sqlcount = "select count(1)\n"
				+ "  from con_j_contract_info t\n"
				+ " where t.is_use = 'Y' and t.exec_flag=1\n";
		String whereStr = "";
		if (enterpriseCode != null && enterpriseCode.length() > 0) {
			whereStr += " and t.enterprise_code='" + enterpriseCode + "'\n";
		}
		if (sDate != null && sDate.length() > 0) {
			whereStr += " and t.sign_end_date>to_date('" + sDate
					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (eDate != null && eDate.length() > 0) {
			whereStr += "and t.sign_end_date<to_date('" + eDate
					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (fuzzy != null && fuzzy.length() > 0) {
			whereStr += " and t.conttrees_no||t.contract_name||getClientName(t.cliend_id) like "
					+ "'%" + fuzzy + "%'\n";
		}
		if (oprateDept != null && !"".equals(oprateDept)) {
			whereStr += " and t.operate_dep_code='" + oprateDept + "'";
		}
		sql += whereStr;
		sqlcount += whereStr;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it = list.iterator();
		List<ContractInfo> arrlist = new ArrayList();
		while (it.hasNext()) {
			ContractInfo con = new ContractInfo();
			Object[] o = (Object[]) it.next();
			if (o[0] != null)
				con.setConId(Long.parseLong(o[0].toString()));
			if (o[1] != null)
				con.setConttreesNo(o[1].toString());
			if (o[2] != null)
				con.setContractName(o[2].toString());
			if (o[3] != null)
				con.setCliendId(Long.parseLong(o[3].toString()));
			if (o[4] != null)
				con.setCurrencyType(Long.parseLong(o[4].toString()));
			if (o[5] != null)
				con.setActAmount(Double.parseDouble(o[5].toString()));
			if (o[6] != null)
				con.setOperateBy(o[6].toString());
			if (o[7] != null)
				con.setOperateDepCode(o[7].toString());
			if (o[8] != null)
				con.setOperateDeptName(o[8].toString());
			if (o[9] != null)
				con.setOperateName(o[9].toString());
			if (o[10] != null)
				con.setStartDate(o[10].toString());
			if (o[11] != null)
				con.setEndDate(o[11].toString());
			if (o[12] != null)
				con.setWorkflowStatus(Long.parseLong(o[12].toString()));
			if (o[13] != null)
				con.setClientName(o[13].toString());
			arrlist.add(con);
		}
		pg.setList(arrlist);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlcount).toString()));
		return pg;
	}

	@SuppressWarnings("unchecked")
	public PageObject findAllContractList(Long conTypeId,
			String enterpriseCode, String sDate, String eDate, String conNO,
			String conName, String client, String operaterBy, Long status,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select t.con_id,\n"
				+ "       decode(t.workflow_status,\n"
				+ "              '0',\n"
				+ "              '未上报',\n"
				+ "              '1',\n"
				+ "              '会签中',\n"
				+ "              '2',\n"
				+ "              '已会签',\n"
				+ "              '3',\n"
				+ "              '退回') workflow_status,\n"
				+ "       t.conttrees_no,\n"
				+ "       t.contract_name,\n"
				+ "       t.cliend_id,\n"
				+ "      getclientname(t.cliend_id) cliend_name,\n"
				+ "       t.currency_type,\n"
				+ "       decode(t.is_sum,'Y','有','N','无') is_sum,\n"
				+ "       t.act_amount,\n"
				+ "       t.operate_by,\n"
				+ "       getworkername(t.operate_by) operate_name,\n"
				+ "       t.operate_dep_code,\n"
				+ "       getdeptname(t.operate_dep_code) operate_dep_name,\n"
				+ "       to_char(t.sign_start_date, 'yyyy-MM-dd hh24:mi:ss') sign_start_date,\n"
				+ "       to_char(t.sign_end_date, 'yyyy-MM-dd hh24:mi:ss') sign_end_date,\n"
				+ "       (select max(r.con_modify_id)\n"
				+ "          from CON_J_MODIFY r\n"
				+ "         where r.con_id = t.con_id\n"
				+ "           and r.is_use = 'Y') con_modify_id,\n"
				+ "       decode(nvl((select max(r.con_modify_id)\n"
				+ "                    from CON_J_MODIFY r\n"
				+ "                   where r.con_id = t.con_id\n"
				+ "                     and r.is_use = 'Y'),\n"
				+ "                  0), 0, '无','有') is_con_modify,\n"
				+ "          t.workflow_no,"
				+ "      ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = t.currency_type) currencyName\n"
				+ "  from con_j_contract_info t\n" + " where t.is_use = 'Y'";
		String sqlcount = "select count(1)\n"
				+ "  from con_j_contract_info t\n" + " where t.is_use = 'Y'\n";
		String whereStr = "";
		if (conTypeId != null && !conTypeId.equals("")) {
			whereStr += "  and t.con_type_id='" + conTypeId + "'";
		}
		if (enterpriseCode != null && enterpriseCode.length() > 0) {
			whereStr += " and t.enterprise_code='" + enterpriseCode + "'\n";
		}
		if (sDate != null && sDate.length() > 0) {
			whereStr += " and t.entry_date >= to_date('" + sDate
					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (eDate != null && eDate.length() > 0) {
			whereStr += "and t.entry_date <= to_date('" + eDate
					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (conNO != null && conNO.length() > 0) {
			whereStr += " and t.conttrees_no like '%" + conNO + "%'\n";
		}
		if (conName != null && !"".equals(conName)) {
			whereStr += " and t.contract_name like '%" + conName + "%'";
		}
		if (client != null && !"".equals(client)) {
			whereStr += " and t.cliend_id = '" + client + "'";
		}
		if (operaterBy != null && !"".equals(operaterBy)) {
			whereStr += " and t.operate_by='" + operaterBy + "'";
		}
		if (status != null && !"".equals(status)) {
			whereStr += " and t.workflow_status=" + status + "";
		}
		sql += whereStr;
		sqlcount += whereStr;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it = list.iterator();
		List<ContractInfo> arrlist = new ArrayList();
		while (it.hasNext()) {
			ContractInfo con = new ContractInfo();
			Object[] o = (Object[]) it.next();
			if (o[0] != null)
				con.setConId(Long.parseLong(o[0].toString()));
			if (o[1] != null)
				con.setWorkflowSta(o[1].toString());
			if (o[2] != null)
				con.setConttreesNo(o[2].toString());

			if (o[3] != null)
				con.setContractName(o[3].toString());

			if (o[4] != null)
				con.setCliendId(Long.parseLong(o[4].toString()));

			if (o[5] != null)
				con.setClientName(o[5].toString());

			if (o[6] != null)
				con.setCurrencyType(Long.parseLong(o[6].toString()));

			if (o[7] != null)
				con.setIsSum(o[7].toString());

			if (o[8] != null)
				con.setActAmount(Double.parseDouble(o[8].toString()));

			if (o[9] != null)
				con.setOperateBy(o[9].toString());

			if (o[10] != null)
				con.setOperateName(o[10].toString());

			if (o[11] != null)
				con.setOperateDepCode(o[11].toString());

			if (o[12] != null)
				con.setOperateDeptName(o[12].toString());
			if (o[13] != null)
				con.setSignStartDate(o[13].toString());
			if (o[14] != null) {
				con.setSignEndDate(o[14].toString());
			} else {
				con.setSignEndDate("");
			}
			if (o[15] != null)
				con.setConModifyId(Long.parseLong(o[15].toString()));
			if (o[16] != null)
				con.setConModify(o[16].toString());
			if (o[17] != null)
				con.setWorkflowNo(Long.parseLong(o[17].toString()));
			if (o[18] != null)
				con.setCurrencyName(o[18].toString());
			arrlist.add(con);
		}
		pg.setList(arrlist);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlcount).toString()));
		return pg;
	}

	public PageObject findContractPayPlayList(Long conTypeId,
			String enterpriseCode, String sDate, String eDate, String conNO,
			String conName, String client, String operaterBy,
			int... rowStartIdxAndCount) {
		try {
			PageObject pg = new PageObject();

			String sql = "select t.con_id,\n"
					+ "       t.conttrees_no,\n"
					+ "       t.contract_name,\n"
					+ "       t.cliend_id,\n"
					+ "       getclientname(t.cliend_id) client_name,\n"
					+ "       decode(t.is_sum, 'Y', '有', 'N', '无') is_sum,\n"
					+ "       t.act_amount,\n"
					+ "       t.currency_type,\n"
					+ "       p.pay_date,\n"
					+ "       p.pay_price,\n"
					+ "       t.operate_by,\n"
					+ "       getworkername(t.operate_by) operate_name,\n"
					+ "       p.memo,\n"

					+ "      ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = t.currency_type) currencyName\n"
					+ "  from con_j_contract_info t, con_j_payment_plan p\n"
					+ " where t.con_id = p.con_id\n"
					+ "   and t.is_use = 'Y'\n" + "   and p.is_use = 'Y'\n"
					+ "   and t.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and p.enterprise_code = '" + enterpriseCode + "'";

			String sqlCount = "select count(1)\n"
					+ "  from con_j_contract_info t, con_j_payment_plan p\n"
					+ " where t.con_id = p.con_id\n"
					+ "   and t.Is_Use = 'Y'\n" + "   AND P.IS_USE = 'Y'";
			String whereStr = "";
			if (conTypeId != null && !conTypeId.equals("")) {
				whereStr += "  and t.con_type_id='" + conTypeId + "'";
			}
			if (sDate != null && sDate.length() > 0) {
				whereStr += " and p.pay_date>to_date('" + sDate
						+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
			}
			if (eDate != null && eDate.length() > 0) {
				whereStr += "and p.pay_date<to_date('" + eDate
						+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
			}
			if (conNO != null && conNO.length() > 0) {
				whereStr += " and t.conttrees_no like " + "'%" + conNO + "%'";
			}
			if (conName != null && !"".equals(conName)) {
				whereStr += " and t.contract_name like " + "'%" + conName
						+ "%'";
			}
			if (client != null && !"".equals(client)) {
				whereStr += " and t.cliend_id = '" + client + "'";
			}
			if (operaterBy != null && !"".equals(operaterBy)) {
				whereStr += " and t.operate_by ='" + operaterBy + "'";
			}
			sql += whereStr;
			sqlCount += whereStr;

			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arraylist = new ArrayList();
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					ContractInfo info = new ContractInfo();
					Object[] data = (Object[]) it.next();
					info.setConId(Long.parseLong(data[0].toString()));
					if (data[1] != null)
						info.setConttreesNo(data[1].toString());
					if (data[2] != null)
						info.setContractName(data[2].toString());
					if (data[3] != null)
						info.setCliendId(Long.parseLong(data[3].toString()));
					if (data[4] != null)
						info.setClientName(data[4].toString());
					if (data[5] != null)
						info.setIsSum(data[5].toString());
					if (data[6] != null)
						info.setActAmount(Double
								.parseDouble(data[6].toString()));
					if (data[7] != null)
						info
								.setCurrencyType(Long.parseLong(data[7]
										.toString()));
					if (data[8] != null)
						info.setPayDate(data[8].toString());
					if (data[9] != null)
						info
								.setPayPrice(Double.parseDouble(data[9]
										.toString()));
					if (data[10] != null)
						info.setOperateBy(data[10].toString());
					if (data[11] != null)
						info.setOperateName(data[11].toString());
					if (data[12] != null) {
						info.setMemo(data[12].toString());
					} else {
						info.setMemo("");
					}
					if (data[13] != null)
						info.setCurrencyName(data[13].toString());
					arraylist.add(info);
				}

			}
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arraylist);
			pg.setTotalCount(totalCount);
			return pg;
		} catch (RuntimeException e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findConIntegrateList(Long conTypeId,
			String enterpriseCode, String sDate, String eDate, String conNO,
			String conName, String client, String operaterBy, String execFlag,
			String fileStatus, int... rowStartIdxAndCount) {
		try {
			PageObject pg = new PageObject();
			String sql = "select t.con_id,\n"
					+ "       t.conttrees_no,\n"
					+ "       t.contract_name,\n"
					+ "       t.cliend_id,\n"
					+ "       getclientname(t.cliend_id) cliend_name,\n"
					+ "       decode(t.is_sum, 'Y', '有', 'N', '无') is_sum,\n"
					+ "       t.act_amount,\n"
					+ "       t.currency_type,\n"
					+ "       decode(t.exec_flag,\n"
					+ "              '0',\n"
					+ "              '登记',\n"
					+ "              '1',\n"
					+ "              '执行',\n"
					+ "              '2',\n"
					+ "              '变更',\n"
					+ "              '3',\n"
					+ "              '解除',\n"
					+ "              '4',\n"
					+ "              '履行终止') exec_flag,\n"
					+ "       decode(t.file_status,\n"
					+ "              'DRF',\n"
					+ "              '未归档',\n"
					+ "              'PRE',\n"
					+ "              '预归档',\n"
					+ "              'OK',\n"
					+ "              '已归档',\n"
					+ "              'BAK',\n"
					+ "              '被退回') file_status,\n"
					+ "       to_char(t.file_date, 'yyyy-MM-dd hh24:mi:ss') file_date,\n"
					+ "       t.operate_by,\n"
					+ "       getworkername(t.operate_by) operate_name,\n"
					+ "       t.operate_dep_code,\n"
					+ "       getdeptname(t.operate_dep_code) operate_dep_name,\n"
					+ "       to_char(t.start_date, 'yyyy-MM-dd hh24:mi:ss') start_date,\n"
					+ "       to_char(t.end_date, 'yyyy-MM-dd hh24:mi:ss') end_date,\n"
					+ "       to_char(t.entry_date, 'yyyy-MM-dd hh24:mi:ss') entry_date1,\n"
					+ "       (select max(r.con_modify_id)\n"
					+ "          from CON_J_MODIFY r\n"
					+ "         where r.con_id = t.con_id\n"
					+ "           and r.is_use = 'Y') con_modify_id,\n"
					+ "       decode(nvl((select count(r.con_modify_id)\n"
					+ "                    from CON_J_MODIFY r\n"
					+ "                   where r.con_id = t.con_id\n"
					+ "                     and r.is_use = 'Y' and r.workflow_status = 2),\n"
					+ "                  0), 0, '无','有') is_con_modify,\n"
					+ "      ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = t.currency_type) currencyName\n"
					+ "  from con_j_contract_info t\n"
					+ " where t.is_use = 'Y'\n"
					+ "   and t.enterprise_code = '" + enterpriseCode + "'";
			// System.out.println(sql);

			String sqlCount = "select count(1) from con_j_contract_info t where t.is_use = 'Y'";

			String whereStr = "";
			if (conTypeId != null && !conTypeId.equals("")) {
				whereStr += "  and t.con_type_id='" + conTypeId + "'";
			}
			if (sDate != null && sDate.length() > 0) {
				whereStr += " and t.entry_date>to_date('" + sDate
						+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
			}
			if (eDate != null && eDate.length() > 0) {
				whereStr += "and t.entry_date<to_date('" + eDate
						+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
			}
			if (conNO != null && conNO.length() > 0) {
				whereStr += " and t.conttrees_no like " + "'%" + conNO + "%'";
			}
			if (conName != null && !"".equals(conName)) {
				whereStr += " and t.contract_name like " + "'%" + conName
						+ "%'";
			}
			if (client != null && !"".equals(client)) {
				whereStr += " and t.cliend_id ='" + client + "'";
			}
			if (operaterBy != null && !"".equals(operaterBy)) {
				whereStr += " and t.operate_by like " + "'%" + operaterBy
						+ "%'";
			}
			if (execFlag != null && !"".equals(execFlag)) {
				whereStr += " and t.exec_flag like " + "'%" + execFlag + "%'";
			}
			if (fileStatus != null && !"".equals(fileStatus)) {
				whereStr += " and t.file_status like " + "'%" + fileStatus
						+ "%'";
			}
			sql += whereStr;
			sqlCount += whereStr;
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arraylist = new ArrayList();
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					ContractInfo info = new ContractInfo();
					Object[] data = (Object[]) it.next();
					info.setConId(Long.parseLong(data[0].toString()));
					if (data[1] != null)
						info.setConttreesNo(data[1].toString());
					if (data[2] != null)
						info.setContractName(data[2].toString());
					if (data[3] != null)
						info.setCliendId(Long.parseLong(data[3].toString()));
					if (data[4] != null)
						info.setClientName(data[4].toString());
					if (data[5] != null)
						info.setIsSum(data[5].toString());
					if (data[6] != null)
						info.setActAmount(Double
								.parseDouble(data[6].toString()));
					if (data[7] != null)
						info
								.setCurrencyType(Long.parseLong(data[7]
										.toString()));
					if (data[8] != null)
						info.setExeFlag(data[8].toString());
					if (data[9] != null)
						info.setFileStatue(data[9].toString());
					if (data[10] != null)
						info.setFileDate(data[10].toString());
					if (data[11] != null)
						info.setOperateBy(data[11].toString());
					if (data[12] != null)
						info.setOperateName(data[12].toString());
					if (data[13] != null)
						info.setOperateDepCode(data[13].toString());
					if (data[14] != null)
						info.setOperateDeptName(data[14].toString());
					if (data[15] != null)
						info.setStartDate(data[15].toString());
					if (data[16] != null)
						info.setEndDate(data[16].toString());
					if (data[17] != null)
						info.setEntryDate(data[17].toString());
					if (data[18] != null)
						info
								.setConModifyId(Long.parseLong(data[18]
										.toString()));
					if (data[19] != null)
						info.setConModify(data[19].toString());
					if (data[20] != null)
						info.setCurrencyName(data[20].toString());
					arraylist.add(info);
				}
			}
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			pg.setList(arraylist);
			pg.setTotalCount(totalCount);
			return pg;
		} catch (RuntimeException e) {
			throw e;
		}
	}

	public boolean isArchive(String undertakeNo) {
		boolean isArc = false;
		String sql = "select count(1) from con_j_contract_info t where t.file_no=? ";
		int num = Integer.parseInt(bll.getSingal(sql,
				new Object[] { undertakeNo }).toString());
		if (num > 0)
			isArc = true;
		else
			isArc = false;
		return isArc;
	}

	public List<ConApproveForm> getApproveList(Long id) {
		Long entryId = this.findById(id).getWorkflowNo();
		List list = new ArrayList();
		Long conType = this.findById(id).getConTypeId();
		String approveDept;
		if ( conType == 2l) {
			String sqlString = "select q.dept_codes from con_c_status q where q.work_flow_no='"
					+ entryId + "'";
			 approveDept = "2,19,20" ;
			 approveDept += (bll.getSingal(sqlString)!=null) ? bll.getSingal(sqlString).toString():"";
		}else{
			approveDept ="2,21";
		}
		
		String sqltime = "SELECT max(t.opinion_time)\n"
				+ "  FROM wf_j_historyoperation t\n" + " WHERE t.entry_id = '"
				+ entryId + "'\n" + "   AND t.step_id in (" + approveDept + ")";
		String comeTime = (bll.getSingal(sqltime)!=null)?bll.getSingal(sqltime).toString() : null;
		String sql="";
		if(this.findById(id).getConTypeId() == 2){
			sql = "SELECT t.*  FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id in (13,14,15,16,17,18,19,20)"
				+ " AND opinion_time > "
				+ "(SELECT MAX(opinion_time)"
				+ "FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id = '2')"
				+ " ORDER BY t.opinion_time ";
		}else{
			 sql = "SELECT t.*" 
			 		+ " FROM wf_j_historyoperation t"
					+ " WHERE t.entry_id = '"
					+ entryId
					+ "'"
					+ "AND t.step_id in (33,34,35,36,37,38,39)"
					+ " AND opinion_time > "
					+ "(SELECT MAX(opinion_time)"
					+ "FROM wf_j_historyoperation t"
					+ " WHERE t.entry_id = '"
					+ entryId
					+ "'"
					+ "AND t.step_id = '2')"
					+ " ORDER BY t.opinion_time ";
		}
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
			if(data[6] !=null)
				model.setCaller(data[6].toString());
			if (data[8] != null) {
				model.setOpinion(data[8].toString());
			}
			if (data[9] != null) {
				model.setOpinionTime(data[9].toString());
			}
//			if (data[10] != null) {
//				model.setCaller(data[10].toString());
//			}
			
			arraylist.add(model);
//			if (this.findById(id).getConTypeId() == 1
//					&& this.findById(id).getActAmount() > 100000
//					&& this.findById(id).getActAmount() < 200000
//					&& arraylist.size() == 2) {
//				arraylist.add(new ConApproveForm());
//			}
		}
		if(conType == 2){
		if (arraylist.size() > 2)
			arraylist.get(2).setComeTime(comeTime);
		}else{
			if (arraylist.size() > 1)
				arraylist.get(1).setComeTime(comeTime);
		}
		return arraylist;
	}
	
	//
	public List<ConApproveForm> getApproveTableList(Long id) {
		
		Long entryId = this.findById(id).getWorkflowNo();
		String sqlString = "select q.dept_codes from con_c_status q where q.work_flow_no='"
				+ entryId + "'";
		String approveDept = "2" + bll.getSingal(sqlString).toString();
		List list = new ArrayList();
		String sql = "SELECT t.* FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id in ("
				+ approveDept
				+ ")"
				+ " ORDER BY t.opinion_time ";
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
			if(data[6] !=null)
				model.setCaller(data[6].toString());
			
			if (data[8] != null) {
				model.setOpinion(data[8].toString());
			}
			if (data[9] != null) {
				model.setOpinionTime(data[9].toString());
				;
			}
//			if (data[10] != null) {
//				model.setCaller(data[10].toString());
//			}
			if (arraylist.size() > 0) {
				// model.setComeTime("");
				// }else{
				model.setComeTime(arraylist.get(arraylist.size() - 1)
						.getOpinionTime());
			}

			arraylist.add(model);
			if (this.findById(id).getConTypeId() == 1
					&& this.findById(id).getActAmount() > 100000
					&& this.findById(id).getActAmount() < 200000
					&& arraylist.size() == 2) {
				arraylist.add(new ConApproveForm());
			}
		}
		return arraylist;

	}


	// 根据币种编码去币种名称
	public String getCurrencyNameByItsCode(Long Currency) {
		String currencyName = "";
		String sql = "select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id ='"
				+ Currency + "'";
		currencyName = bll.getSingal(sql).toString();
		return currencyName;

	}
	
	//add by drdu 091109
	@SuppressWarnings("unchecked")
	public PageObject findDelegationList(String workCode, Long conTypeId,
			String enterpriseCode, String sDate, String eDate, String likeName,
			String oprateDept, String entryBy, String status, String entryIds,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select t.con_id,\n"
				+ "       t.conttrees_no,\n"
				+ "       t.contract_name,\n"
				+ "       t.cliend_id,\n"
				+ "       t.currency_type,\n"
				+ "       t.act_amount,\n"
				+ "       t.operate_by,\n"
				+ "       t.operate_dep_code,\n"
				+ "       getdeptname(t.operate_dep_code),\n"
				+ "       getworkername(t.operate_by),\n"
				+ "       to_char(t.sign_start_date, 'yyyy-MM-dd hh24:mi:ss') sign_start_date,\n"
				+ "       to_char(t.sign_end_date, 'yyyy-MM-dd hh24:mi:ss') sign_end_date,\n"
				+ "       t.workflow_status,\n"
				+ "       getclientname(t.cliend_id) clientName,\n"
				+ "       t.workflow_no,\n"
				+ "      ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = t.currency_type) currencyName,\n"
				+ "      ( select prj.construction_charge_by from prj_j_info prj where prj.prj_no=t.project_id) constructionName,\n"
				+ "       t.prosy_by,getworkername(t.prosy_by),t.prosy_start_date,t.prosy_end_date,t.workflow_no_dg,t.workflow_dg_status\n"
				+ "	from con_j_contract_info t\n" + " where t.is_use = 'Y'";
		String sqlcount = "select count(1)\n"
				+ "  from con_j_contract_info t\n" + " where t.is_use = 'Y'\n";
		String whereStr = "";
		if (conTypeId != null && !conTypeId.equals("")) {
			whereStr += "  and t.con_type_id='" + conTypeId + "'";
		}
		if (enterpriseCode != null && enterpriseCode.length() > 0) {
			whereStr += " and t.enterprise_code='" + enterpriseCode + "'\n";
		}
		if (sDate != null && sDate.length() > 0) {
			whereStr += " and t.sign_start_date>to_date('" + sDate
					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (eDate != null && eDate.length() > 0) {
			whereStr += "and t.sign_start_date<to_date('" + eDate
					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (likeName != null && likeName.length() > 0) {
			whereStr += " and t.conttrees_no||t.contract_name|| getClientName(t.cliend_id) like "
					+ "'%" + likeName + "%'\n";
		}
		if (status != null) {
			if (status.equals("approve")) {
				whereStr += " and (t.workflow_dg_status in(1,2))\n";
//				if (entryIds != null && (!"".equals(entryIds))) {
					whereStr += " and t.workflow_no_dg in (" + entryIds + ")\n";
//				}
			} else if (status.equals("endsign")) {
				whereStr += " and t.workflow_dg_status = 2 \n";
			}
		}
		if (oprateDept != null && oprateDept.length() > 0) {
			whereStr += " and (t.operate_dep_code='" + oprateDept + "'";
			if (entryBy != null && !"".equals(entryBy)) {
				whereStr += " or t.entry_by='" + entryBy + "')";
			} else {
				whereStr += ")";
			}
		}
		if (!workCode.equals("")) {
			whereStr += " and t.ENTRY_BY = '" + workCode + "'";
		}
		sql += whereStr;
		sql += " order by t.con_id";
		sqlcount += whereStr;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it = list.iterator();
		List<ContractInfo> arrlist = new ArrayList();
		while (it.hasNext()) {
			ContractInfo con = new ContractInfo();
			Object[] o = (Object[]) it.next();
			con.setConId(Long.parseLong(o[0].toString()));
			if (o[1] != null)
				con.setConttreesNo(o[1].toString());
			if (o[2] != null)
				con.setContractName(o[2].toString());
			if (o[3] != null)
				con.setCliendId(Long.parseLong(o[3].toString()));
			if (o[4] != null)
				con.setCurrencyType(Long.parseLong(o[4].toString()));
			if (o[5] != null)
				con.setActAmount(Double.parseDouble(o[5].toString()));
			if (o[6] != null)
				con.setOperateBy(o[6].toString());
			if (o[7] != null)
				con.setOperateDepCode(o[7].toString());
			if (o[8] != null)
				con.setOperateDeptName(o[8].toString());
			if (o[9] != null)
				con.setOperateName(o[9].toString());
			if (o[10] != null)
				con.setSignStartDate(o[10].toString());
			if (o[11] != null)
				con.setSignEndDate(o[11].toString());
			if (o[12] != null)
				con.setWorkflowStatus(Long.parseLong(o[12].toString()));
			if (o[13] != null)
				con.setClientName(o[13].toString());
			if (o[14] != null)
				con.setWorkflowNo(Long.parseLong(o[14].toString()));
			if (o[15] != null)
				con.setCurrencyName(o[15].toString());
			if (o[16] != null)
				con.setConstructionName(o[16].toString());
			//---------add by drdu 091110------------
			if(o[17]!= null)
				con.setProsyBy(o[17].toString());
			if(o[18] != null)
				con.setProsyName(o[18].toString());
			if(o[19] != null)
				con.setProsyStartDate(o[19].toString());
			if(o[20] != null)
				con.setProsyEndDate(o[20].toString());
			if(o[21] != null)
				con.setWorkflowNoDg(Long.parseLong(o[21].toString()));
			if(o[22] != null)
				con.setWorkflowDgStatus(Long.parseLong(o[22].toString()));
			//-----------------end---------------------
			arrlist.add(con);
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlcount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}
	@SuppressWarnings("unchecked")
	public List<ConApproveForm>	getdelegationApprove(Long id){
		Long entryId = this.findById(id).getWorkflowNoDg();
		
		List list = new ArrayList();
		String sql = "SELECT t.*,getworkername(t.caller),(select getworkername(w.PROXY_CODE) from con_j_contract_info w where w.WORKFLOW_NO_DG= t.entry_id)" 
	 		+ " FROM wf_j_historyoperation t"
			+ " WHERE t.entry_id = '"
			+ entryId
			+ "'"
			+ "AND t.step_id in (4,5)"
			+ " AND opinion_time > "
			+ "(SELECT MAX(opinion_time)"
			+ "FROM wf_j_historyoperation t"
			+ " WHERE t.entry_id = '"
			+ entryId
			+ "'"
			+ "AND t.step_id = '2')"
			+ " ORDER BY t.opinion_time ";
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
			if(data[6] !=null)
				model.setCaller(data[6].toString());
//			if (data[8] != null) {
//				model.setOpinion(data[8].toString());
//			}
			if (data[9] != null) {
				model.setOpinionTime(data[9].toString());
			}
//			if (data[10] != null) {
//				model.setOpinion(data[10].toString());
//			}
			if(data[11]!=null){
				model.setOpinion(data[11].toString());
			}
			arraylist.add(model);
		}
		return arraylist;
	}
	/**
	 * 功能：查询所有的合同
	 * add bu qxjiao 20100812
	 */
	public PageObject findContactList(String CON_YEAR, String CONTRACT_NAME,
			int start, int limit) {
		String sql = "SELECT t1.con_id,\n"+
					      " t1.conttrees_no,\n"+
					      " t1.contract_name,\n"+
					      " t2.client_name,\n"+
					      " t1.act_amount,\n"+
					      " t1.con_year, \n" +
					      "	t1.cliend_id \n"+
					 " FROM con_j_contract_info t1,\n"+
					      " CON_J_CLIENTS_INFO  t2 \n"+
					" WHERE t1.con_type_id = 2 \n"+
					  " AND t1.cliend_id = t2.cliend_id \n"+
					  " AND t1.con_year = '"+CON_YEAR+"' \n"+
					  " AND t1.contract_name like '%"+CONTRACT_NAME+"%' \n";
		String sqlCount = "select count(*)" +
								" from con_j_contract_info" +
								" where con_type_id = 2" +
								" and con_year = '"+CON_YEAR+"'" +
								" and contract_name like '%"+CONTRACT_NAME+"%'";
		
		List result = bll.queryByNativeSQL(sql, start,limit);
		long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		PageObject obj = new PageObject();
		obj.setList(result);
		obj.setTotalCount(count);
		return obj;
	}

}