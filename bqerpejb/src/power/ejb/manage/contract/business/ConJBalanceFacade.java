package power.ejb.manage.contract.business;

import java.util.ArrayList;

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

import power.ejb.manage.contract.form.BpAppDetailForm;
import power.ejb.manage.contract.form.BpAppHeaderForm;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.manage.contract.form.ConBalanceForm;
import power.ejb.manage.contract.form.ConBalanceFullForm;
import power.ejb.manage.contract.form.ContractInfo;

import power.ejb.manage.contract.form.ContractForm;

/**
 * Facade for entity ConJBalance.
 * 
 * @see power.ejb.manage.contract.business.ConJBalance
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConJBalanceFacade implements ConJBalanceFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "ConJConDocFacade")
	protected ConJConDocFacadeRemote docremote;
	@EJB(beanName = "ConJBalinvioceFacade")
	protected ConJBalinvioceFacadeRemote vremote;
	@EJB(beanName = "ConJContractInfoFacade")
	protected ConJContractInfoFacadeRemote cremote;

	public ConJBalance save(ConJBalance entity) throws CodeRepeatException {
		LogUtil.log("saving ConJBalance instance", Level.INFO, null);
		try {
			if (entity.getBalanceId() == null) {
				entity
						.setBalanceId(bll.getMaxId("CON_J_BALANCE",
								"BALANCE_ID"));
			}
			entity.setConBalanceDegreeLong(this.getFindConBalaceDegree(entity.getConId()));
			entity.setIsUse("Y");
			entity.setBalaFlag("0");
			// entity.setOperateDepCode(this.getDeptCodeByWorkCode(entity
			// .getOperateBy()));
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(ConJBalance entity) throws CodeRepeatException {
		if (entity.getBalaFlag().equals("0")
				|| "3".equals(entity.getBalaFlag())) {
			entity.setIsUse("N");
			String paysql = "update CON_J_BALINVIOCE t set t.is_use='N' where t.balance_id='"
					+ entity.getBalanceId() + "'";
			String docsql = "update CON_J_CON_DOC t set t.is_use='N' where t.doc_type='CONPAY' and t.KEY_ID='"
					+ entity.getBalanceId() + "'";
			bll.exeNativeSQL(paysql);
			bll.exeNativeSQL(docsql);
			ConJBalance result = entityManager.merge(entity);

		}
	}

	public ConJBalance update(ConJBalance entity) throws CodeRepeatException {
		try {
			if (entity.getBalaFlag().equals("0")
					|| "3".equals(entity.getBalaFlag())
					|| "2".equals(entity.getBalaFlag())) {
				ConJBalance result = entityManager.merge(entity);
				return result;
			} else
				return entity;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void updateBalance(ConJBalance entity) {
		try {
			ConJBalance result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConJBalance findById(Long id) {
		try {
			ConJBalance instance = entityManager.find(ConJBalance.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ConBalanceForm> findBalanceListByConId(Long conId,
			String enterpriseCode) {
		Double temp = 0d;
		String sql = "select t.balance_id,\n"
				+ "       t.payment_id,\n"
				+ "       t.cliend_id,\n"
				+ "       t.operate_by,\n"
				+ "       t.operate_dep_code,\n"
				+ "       t.balance_by,\n"
				+ "       t.bala_flag,\n"
				+ "       t.bala_batch,\n"
				+ "       t.bala_cause,\n"
				+ "       t.memo,\n"
				+ "       t.workflow_no,\n"
				+ "       t.workflow_status,\n"
				+ "       t.applicat_price,\n"
				+ "       t.pass_price,\n"
				+ "       t.balance_price,\n"
				+ "       t.else_price,\n"
				+ "       t.bala_method,\n"
				+ "       t.item_id,\n"
				+ "       t.applicat_date,\n"
				+ "       t.pass_date,\n"
				+ "       t.bala_date,\n"
				+ "       t.cheque_no,\n"
				+ "       t.receipt_no,\n"
				+ "       t.entry_by,\n"
				+ "       t.entry_date,\n"
				+ "      getworkername(t.operate_by),\n"
				+ "      getdeptname(t.operate_dep_code),\n"
				+ "      getworkername(t.balance_by),\n"
				+ "      getworkername(t.entry_by),\n"
				+ "      (select cl.client_name from con_j_clients_info cl where rownum=1)  clientName,\n"
				+ "      c.contract_name,\n"
				+ "      ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = c.currency_type) currencyName,\n"
				+ "     c.ACT_AMOUNT \n"
				+ "  from con_j_balance t,con_j_contract_info c\n"
				+ " where t.con_id = ?\n" + "   and t.is_use = 'Y'\n"
				+ "   and t.enterprise_code=?\n" + "   and c.con_id=t.con_id";
		List list = bll.queryByNativeSQL(sql, new Object[] { conId,
				enterpriseCode });
		Iterator it = list.iterator();
		List<ConBalanceForm> arrlist = new ArrayList();
		while (it.hasNext()) {
			ConBalanceForm model = new ConBalanceForm();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				model.setBalanceId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setPaymentId(Long.parseLong(data[1].toString()));
			if (data[2] != null)
				model.setCliendId(Long.parseLong(data[2].toString()));
			if (data[3] != null)
				model.setOperateBy(data[3].toString());
			if (data[4] != null)
				model.setOperateDepCode(data[4].toString());
			if (data[5] != null)
				model.setBalanceBy(data[5].toString());
			if (data[6] != null)
				model.setBalaFlag(data[6].toString());
			if (data[7] != null)
				model.setBalaBatch(data[7].toString());
			if (data[8] != null)
				model.setBalaCause(data[8].toString());
			if (data[9] != null)
				model.setMemo(data[9].toString());
			if (data[10] != null)
				model.setWorkflowNo(Long.parseLong(data[10].toString()));
			if (data[11] != null)
				model.setWorkflowStatus(Long.parseLong(data[11].toString()));
			if (data[12] != null) {
				model.setApplicatPrice(Double.valueOf(data[12].toString()));
				temp += model.getApplicatPrice();
			}
			if (data[13] != null)
				model.setPassPrice(Double.valueOf(data[13].toString()));
			if (data[14] != null)
				model.setBalancePrice(Double.valueOf(data[14].toString()));
			if (data[15] != null)
				model.setElsePrice(Double.valueOf(data[15].toString()));
			if (data[16] != null)
				model.setBalaMethod(data[16].toString());
			if (data[17] != null)
				model.setItemId(data[17].toString());
			if (data[18] != null)
				model.setApplicatDate(data[18].toString());
			if (data[19] != null)
				model.setPassDate(data[19].toString());
			if (data[20] != null)
				model.setBalaDate(data[20].toString());
			if (data[21] != null)
				model.setChequeNo(data[21].toString());
			if (data[22] != null)
				model.setReceiptNo(data[22].toString());
			if (data[23] != null)
				model.setEntryBy(data[23].toString());
			if (data[24] != null)
				model.setEntryDate(data[24].toString());
			if (data[25] != null)
				model.setOperateName(data[25].toString());
			if (data[26] != null)
				model.setOperateDeptName(data[26].toString());
			if (data[27] != null)
				model.setBalanceName(data[27].toString());
			if (data[28] != null)
				model.setEntryName(data[28].toString());
			if (data[29] != null)
				model.setClientName(data[29].toString());
			if (data[30] != null)
				model.setContractName(data[30].toString());
			if (data[31] != null)
				model.setCurrencyName(data[31].toString());
			if (data[32] != null)
				model.setActAccount(Double.valueOf(data[32].toString()));
			model.setLeaveAmount(model.getActAccount() - temp);
			arrlist.add(model);
		}
		return arrlist;
	}

	public ContractForm findContractByConId(Long conId) {
		String csql = "select \n" + "       t.conttrees_no,\n"
				+ "       t.contract_name,\n" + "       t.cliend_id,\n"
				+ "       t.act_amount,\n" + "       t.item_id,\n"
				+ "       c.client_name,\n" + "       c.bank,\n"
				+ "       c.account,\n" + "       t.applied_amount,\n"
				+ "       t.approved_amount,\n" + "       t.payed_amount,\n"
				+ "       getdeptname(t.operate_dep_code)\n"
				+ "  from con_j_contract_info t, con_j_clients_info c\n"
				+ " where t.con_id = ?\n"
				+ "   and t.cliend_id = c.cliend_id\n";
		Object[] data = (Object[]) bll.getSingal(csql, new Object[] { conId });
		ContractForm form = new ContractForm();
		if (data[0] != null)
			form.setConttreesNo(data[0].toString());
		if (data[1] != null)
			form.setContractName(data[1].toString());
		if (data[2] != null)
			form.setCliendId(Long.parseLong(data[2].toString()));
		if (data[3] != null)
			form.setActAccount(Double.valueOf(data[3].toString()));
		if (data[4] != null)
			form.setItemId(data[4].toString());
		if (data[5] != null)
			form.setClientName(data[5].toString());
		if (data[6] != null)
			form.setBankAccount(data[6].toString());
		if (data[7] != null)
			form.setAccount(data[7].toString());
		if (data[8] != null)
			form.setAppliedAccount(Double.valueOf(data[8].toString()));
		if (data[9] != null)
			form.setApprovedAccount(Double.valueOf(data[9].toString()));
		if (data[10] != null)
			form.setPayedAccount(Double.valueOf(data[10].toString()));
		if (data[11] != null)
			form.setDeptName(data[11].toString());
		return form;
	}

	public ConBalanceFullForm findBalanceByBalanceId(Long balanceId) {
		String sql = "select t.con_id,\n"
				+ "       t.payment_id,\n"
				+ "       t.cliend_id,\n"
				+ "       t.operate_by,\n"
				+ "       t.operate_dep_code,\n"
				+ "       t.balance_by,\n"
				+ "       t.bala_flag,\n"
				+ "       t.bala_batch,\n"
				+ "       t.bala_cause,\n"
				+ "       t.memo,\n"
				+ "       t.workflow_no,\n"
				+ "       t.workflow_status,\n"
				+ "       t.applicat_price,\n"
				+ "       t.pass_price,\n"
				+ "       t.balance_price,\n"
				+ "       t.else_price,\n"
				+ "       t.bala_method,\n"
				+ "       t.item_id,\n"
				+ "       to_char(t.applicat_date,'yyyy-mm-dd hh24:mi:ss') applicat_date,\n"
				+ "       to_char(t.pass_date,'yyyy-mm-dd hh24:mi:ss') pass_date,\n"
				+ "       to_char(t.bala_date,'yyyy-mm-dd hh24:mi:ss') bala_date,\n"
				+ "       t.cheque_no,\n"
				+ "       t.receipt_no,\n"
				+ "       t.entry_by,\n"
				+ "       t.entry_date,\n"
				+ "      getworkername(t.operate_by),\n"
				+ "      getdeptname(t.operate_dep_code),\n"
				+ "      getworkername(t.balance_by),\n"
				+ "      getworkername(t.entry_by),\n"
				+ "      (select cl.client_name from con_j_clients_info cl where  rownum=1)  clientName,\n"
				+ "      c.contract_name,\n"
				+ "      t.balance_id,\n"
				+ "      c.CURRENCY_TYPE,\n"
				+ "      c.ACT_AMOUNT,\n"
				+ "      c.APPLIED_AMOUNT,\n"
				+ "       nvl((select sum(e.pass_price) from con_j_balance e where e.con_id=c.con_id and e.bala_flag = 2 and e.is_use='Y' and e.con_balance_degree < t.con_balance_degree),0)payAccount,\n"
				+ "    	 (select tt.item_name from CON_C_ITEM_SOURCE tt where tt.item_code=c.item_id and tt.is_use='Y' ) myitemName \n"
				+ "  from con_j_balance t,con_j_contract_info c\n"
				+ " where t.balance_id = ?\n" + "   and t.is_use = 'Y'\n"
				+ "   and c.con_id=t.con_id";
		Object[] data = (Object[]) bll.getSingal(sql,
				new Object[] { balanceId });
		ConBalanceFullForm model = new ConBalanceFullForm();
		if (data != null) {
			if (data[0] != null)
				model.setConId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setPaymentId(Long.parseLong(data[1].toString()));
			if (data[2] != null)
				model.setCliendId(Long.parseLong(data[2].toString()));
			if (data[3] != null)
				model.setOperateBy(data[3].toString());
			if (data[4] != null)
				model.setOperateDepCode(data[4].toString());
			if (data[5] != null)
				model.setBalanceBy(data[5].toString());
			if (data[6] != null)
				model.setBalaFlag(data[6].toString());
			if (data[7] != null)
				model.setBalaBatch(data[7].toString());
			if (data[8] != null)
				model.setBalaCause(data[8].toString());
			if (data[9] != null)
				model.setMemo(data[9].toString());
			if (data[10] != null)
				model.setWorkflowNo(Long.parseLong(data[10].toString()));
			if (data[11] != null)
				model.setWorkflowStatus(Long.parseLong(data[11].toString()));
			if (data[12] != null)
				model.setApplicatPrice(Double.valueOf(data[12].toString()));
			if (data[13] != null)
				model.setPassPrice(Double.valueOf(data[13].toString()));
			if (data[14] != null)
				model.setBalancePrice(Double.valueOf(data[14].toString()));
			if (data[15] != null)
				model.setElsePrice(Double.valueOf(data[15].toString()));
			if (data[16] != null)
				model.setBalaMethod(data[16].toString());
			if (data[17] != null)
				model.setItemId(data[17].toString());
			if (data[18] != null)
				model.setApplicatDate(data[18].toString());
			if (data[19] != null)
				model.setPassDate(data[19].toString());
			if (data[20] != null)
				model.setBalaDate(data[20].toString());
			if (data[21] != null)
				model.setChequeNo(data[21].toString());
			if (data[22] != null)
				model.setReceiptNo(data[22].toString());
			if (data[23] != null)
				model.setEntryBy(data[23].toString());
			if (data[24] != null)
				model.setEntryDate(data[24].toString());
			if (data[25] != null)
				model.setOperateName(data[25].toString());
			if (data[26] != null)
				model.setOperateDeptName(data[26].toString());
			if (data[27] != null)
				model.setBalanceName(data[27].toString());
			if (data[28] != null)
				model.setEntryName(data[28].toString());
			if (data[29] != null)
				model.setClientName(data[29].toString());
			if (data[30] != null)
				model.setContractName(data[30].toString());
			if (data[31] != null)
				model.setBalanceId(Long.parseLong(data[31].toString()));
			if (data[32] != null)
				model.setCurrencyType(Long.parseLong(data[32].toString()));
			if (data[33] != null)
				model.setActAmount(Double.valueOf(data[33].toString()));
			// if (data[34] != null)
			// model.setApplicatPrice(Double.valueOf(data[34].toString()));
			if (data[35] != null)
				model.setPayedAmount(Double.valueOf(data[35].toString()));
			if (data[36] != null)
				model.setItemName(data[36].toString());
			// List<ConDocForm> list = docremote.findConDocList(
			// model.getEnterpriseCode(), balanceId, "CONPAY").getList();
			// List<BalinvioceForm> vlist = vremote.findByBalanceId(model
			// .getEnterpriseCode(), balanceId);
			// model.setList(vlist);
			// model.setDoclist(list);
			// if (model.getPayedAmount() != null) {
			// model.setUnliquidate(model.getActAmount()
			// - model.getPayedAmount()-model.getApplicatPrice());
			// }else{
			model.setUnliquidate(model.getActAmount()
					- (model.getPayedAmount() != null ? model.getPayedAmount()
							: 0l)
					- (model.getApplicatPrice() != null ? model
							.getApplicatPrice() : 0l));
			// }
			return model;
		} else {
			return null;
		}
	}

	private String getDeptCodeByWorkCode(String workerCode) {
		String sql = "select  getdeptbyworkcode(" + workerCode + ") from dual";
		return bll.getSingal(sql).toString();
	}

	@SuppressWarnings("unchecked")
	public PageObject findConPayDetailsList(Long conTypeId,
			String enterpriseCode, String sDate, String eDate, String conNO,
			String conName, String client, String operaterBy,
			int... rowStartIdxAndCount) {
		try {
			PageObject pg = new PageObject();
			String sql = "select t.con_id,\n"
					+ "       t.conttrees_no,\n"
					+ "       t.contract_name,\n"
					+ "       b.cliend_id,\n"
					+ "       getclientname(b.cliend_id) client_name,\n"
					+ "       t.act_amount,\n"
					+ "       t.currency_type,\n"
					+ "       b.item_id,\n"
					+ "        nvl((select sum(e.pass_price) from con_j_balance e where e.con_id=t.con_id and e.bala_flag = 2 and e.is_use='Y'and e.con_balance_degree < b.con_balance_degree),0)payAcount,\n"
					+ "       (t.act_amount - nvl((select sum(b.pass_price) from con_j_balance b where b.workflow_status='2' and b.con_id = t.con_id),0)) unliquidate,\n"
					+ "       p.pay_price,\n"
					+ "       to_char(p.pay_date, 'yyyy-MM-dd hh24:mi:ss') pay_date,\n"
					+ "       b.balance_price,\n"
					+ "       to_char(b.bala_date, 'yyyy-MM-dd hh24:mi:ss') bala_date,\n"
					+ "       b.operate_by,\n"
					+ "       getworkername(b.operate_by) operate_byName,\n"
					+ "       b.memo,\n"
					+ "       b.balance_id,\n"
					+ "       to_char(t.start_date, 'yyyy-MM-dd hh24:mi:ss') start_date,\n"
					+ "        p.payment_id,\n"
					+ "      ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = t.currency_type) currencyName,"
					+ "      p.PAYMENT_MOMENT,\n"
					+ "    (select tt.item_name from CON_C_ITEM_SOURCE tt\n"
					+ "     where tt.item_code=t.item_id\n"
					+ "     and tt.is_use='Y') myitemName\n"
					+ "  from con_j_contract_info t, con_j_balance b, con_j_payment_plan p\n"
					+ " where b.con_id = t.con_id\n"
					+ "   and b.con_id = p.con_id\n"
					+ "   and b.payment_id = p.payment_id\n"
					+ "   and t.exec_flag <> 0\n" + "   and b.is_use = 'Y'\n"
					+ "   and t.is_use = 'Y'\n" + "   and p.is_use = 'Y'\n"
					+ "   and b.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and t.enterprise_code = '" + enterpriseCode + "'\n"
					+ "   and p.enterprise_code = '" + enterpriseCode + "'";

			String sqlCount = "select count(1)\n"
					+ "  from con_j_contract_info t, con_j_balance b, con_j_payment_plan p\n"
					+ " where b.con_id = t.con_id\n"
					+ "   and b.con_id = p.con_id\n"
					+ "   and b.payment_id = p.payment_id\n"
					+ "   and b.is_use = 'Y'\n" + "   and t.is_use = 'Y'\n"
					+ "   and p.is_use = 'Y'";
			String whereStr = "";
			if (conTypeId != null && !conTypeId.equals("")) {
				whereStr += "  and t.con_type_id='" + conTypeId + "'";
			}
			if (sDate != null && sDate.length() > 0) {
				whereStr += " and t.start_date>=to_date('" + sDate
						+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
			}
			if (eDate != null && eDate.length() > 0) {
				whereStr += "and t.start_date<=to_date('" + eDate
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
				whereStr += " and b.cliend_id = '" + client + "'";
			}
			if (operaterBy != null && !"".equals(operaterBy)) {
				whereStr += " and b.operate_by = '" + operaterBy + "'";
			}
			sql += whereStr;
			sqlCount += whereStr;

			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arraylist = new ArrayList();
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					ConBalanceFullForm form = new ConBalanceFullForm();
					Object[] data = (Object[]) it.next();
					form.setConId(Long.parseLong(data[0].toString()));
					if (data[1] != null)
						form.setConttreesNo(data[1].toString());
					if (data[2] != null)
						form.setContractName(data[2].toString());
					if (data[3] != null)
						form.setCliendId(Long.parseLong(data[3].toString()));
					if (data[4] != null)
						form.setClientName(data[4].toString());
					if (data[5] != null)
						form.setActAmount(Double
								.parseDouble(data[5].toString()));
					if (data[6] != null)
						form
								.setCurrencyType(Long.parseLong(data[6]
										.toString()));
					if (data[7] != null)
						form.setItemId(data[7].toString());
					if (data[8] != null)
						form.setPayedAmount(Double.parseDouble(data[8]
								.toString()));
					if (data[9] != null)
						form.setUnliquidate(Double.parseDouble(data[9]
								.toString()));
					if (data[10] != null)
						form.setPayPrice(Double
								.parseDouble(data[10].toString()));
					if (data[11] != null)
						form.setPayDate(data[11].toString());
					if (data[12] != null)
						form.setBalancePrice(Double.parseDouble(data[12]
								.toString()));
					if (data[13] != null) {
						form.setBalaDate(data[13].toString());
					} else {
						form.setBalaDate("");
					}
					if (data[14] != null)
						form.setOperateBy(data[14].toString());
					if (data[15] != null)
						form.setOperateName(data[15].toString());
					if (data[16] != null) {
						form.setMemo(data[16].toString());
					} else {
						form.setMemo("");
					}
					if (data[17] != null)
						form.setBalanceId(Long.parseLong(data[17].toString()));
					if (data[18] != null)
						form.setStartDate(data[18].toString());
					if (data[19] != null)
						form.setPaymentId(Long.parseLong(data[19].toString()));
					if (data[20] != null)
						form.setCurrencyName(data[20].toString());
					if (data[21] != null)
						form.setPaymentMoment(data[21].toString());
					if (data[22] != null) {
						form.setItemName(data[22].toString());
					}
					arraylist.add(form);
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
	public PageObject findConPaymentList(Long conTypeId, String enterpriseCode,
			String sDate, String eDate, String conNO, String conName,
			String client, String operaterBy, int... rowStartIdxAndCount) {
		try {
			PageObject pg = new PageObject();

			String sql = "select t.con_id,\n"
					+ "       t.conttrees_no,\n"
					+ "       t.contract_name,\n"
					+ "       t.cliend_id,\n"
					+ "       getclientname(t.cliend_id) client_name,\n"
					+ "       t.act_amount,\n"
					+ "       t.currency_type,\n"
					+ "       t.item_id,\n"
					+ "       (select sum(b.pass_price) from con_j_balance b where b.bala_flag='2' and b.con_id = t.con_id) balaeceEnd,\n"
					+ "       (t.act_amount - nvl((select sum(b.pass_price) from con_j_balance b where b.workflow_status='2' and b.con_id = t.con_id),0)) unliquidate,\n"
					+ "       getworkername(t.operate_by) operate_byName,\n"
					+ "      ( select sys.currency_name from SYS_C_CURRENCY sys where sys.currency_id = t.currency_type) currencyName,\n"
					+ "      (select max(b.app_id) from con_j_balance b where b.con_id= t.con_id and b.is_use='Y') appId \n"
					+ "   from con_j_contract_info t\n"
					+ " where t.exec_flag <> 0\n" + "   \n"
					+ "   and t.is_use = 'Y'\n"
					+ "   and t.enterprise_code = '" + enterpriseCode + "'\n";

			String sqlCount = "select count(1)\n"
					+ "  from con_j_contract_info t\n"
					+ " where t.exec_flag <> 0\n" + "   \n"
					+ "   and t.is_use = 'Y'\n"
					+ "   and t.enterprise_code = '" + enterpriseCode + "'\n";
			String whereStr = "";
			if (conTypeId != null && !conTypeId.equals("")) {
				whereStr += "  and t.con_type_id='" + conTypeId + "'";
			}
			if (sDate != null && sDate.length() > 0) {
				// whereStr+=" and
				// t.start_date>to_date('"+sDate+"-01'||'00:00:00', 'yyyy-MM-dd
				// hh24:mi:ss')\n";
				whereStr += " and t.ENTRY_DATE>=to_date('" + sDate
						+ "', 'yyyy-mm')\n";
			}
			if (eDate != null && eDate.length() > 0) {
				whereStr += "and t.ENTRY_DATE<add_months(to_date('" + eDate
						+ "', 'yyyy-mm'),1)\n";
			}
			if (conNO != null && conNO.length() > 0) {
				whereStr += " and t.conttrees_no like " + "'%" + conNO + "%'";
			}
			if (conName != null && !"".equals(conName)) {
				whereStr += " and t.contract_name like " + "'%" + conName
						+ "%'";
			}
			if (client != null && !"".equals(client)) {
				whereStr += " and t.cliend_id = " + "'" + client + "'";
			}
			if (operaterBy != null && !"".equals(operaterBy)) {
				whereStr += " and t.operate_by = " + "'" + operaterBy + "'";
			}
			sql += whereStr;
			sqlCount += whereStr;

			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arraylist = new ArrayList();
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					ConBalanceFullForm form = new ConBalanceFullForm();
					Object[] data = (Object[]) it.next();
					form.setConId(Long.parseLong(data[0].toString()));
					if (data[1] != null)
						form.setConttreesNo(data[1].toString());
					if (data[2] != null)
						form.setContractName(data[2].toString());
					if (data[3] != null)
						form.setCliendId(Long.parseLong(data[3].toString()));
					if (data[4] != null)
						form.setClientName(data[4].toString());
					if (data[5] != null)
						form.setActAmount(Double
								.parseDouble(data[5].toString()));
					if (data[6] != null)
						form
								.setCurrencyType(Long.parseLong(data[6]
										.toString()));
					if (data[7] != null)
						form.setItemId(data[7].toString());
					if (data[8] != null)
						form.setPayedAmount(Double.parseDouble(data[8]
								.toString()));
					if (data[9] != null)
						form.setUnliquidate(Double.parseDouble(data[9]
								.toString()));
					// if (data[10] != null)
					// form.setPayPrice(Double
					// .parseDouble(data[10].toString()));
					// if (data[11] != null)
					// form.setPayDate(data[11].toString());
					// if (data[10] != null)
					// form.setBalancePrice(Double.parseDouble(data[10]
					// .toString()));
					// if (data[11] != null)
					// // form.setBalaDate(data[11].toString());
					// if (data[12] != null)
					// form.setOperateBy(data[12].toString());
					if (data[10] != null)
						form.setOperateName(data[10].toString());
					// if (data[13] != null)
					// form.setMemo(data[13].toString());
					// if (data[15] != null)
					// form.setBalanceId(Long.parseLong(data[17].toString()));
					// if (data[13] != null)
					// form.setStartDate(data[13].toString());
					// if (data[16] != null)
					// form.setPaymentId(Long.parseLong(data[16].toString()));
					if (data[11] != null)
						form.setCurrencyName(data[11].toString());
					if (data[12] != null) {
						// 用与保存采购合同申请Id
						form.setBalanceName(data[12].toString());
					}
					arraylist.add(form);
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

	public PageObject findConBalanceApproveList(Long conTypeId,
			String startDate, String endDate, String enterprisecode,
			String woflowNo, String type, int start, int limit) {
		String countsql = "select count(*)\n" + "  from con_j_balance r\n"
				+ " where r.applicat_date between\n"
				+ "       to_date('2009-01-01', 'yyyy-mm-dd hh24:mi:ss') and\n"
				+ "       to_date('2009-04-01', 'yyyy-mm-dd hh24:mi:ss')\n"
				+ "   and r.enterprise_code = '" + enterprisecode + "'\n"
				+ "   and r.is_use = 'Y'";

		String sql = "select r.balance_id,\n"
				+ "       r.con_id,\n"
				+ "       r.bala_flag,\n"
				+ "       r.cliend_id,\n"
				+ "       (select c.client_name\n"
				+ "          from con_j_clients_info c\n"
				+ "         where\n"
				+ "         c.cliend_id = r.cliend_id) client_name,\n"
				+ "       r.applicat_price,\n"
				+ "       r.operate_by,\n"
				+ "       getworkername(r.operate_by) operate_by_name,\n"
				+ "       r.operate_dep_code,\n"
				+ "       getdeptname(r.operate_dep_code) operate_dep_name,\n"
				+ "       to_char(r.applicat_date, 'yyyy-mm-dd hh24:mi:ss') applicat_date,\n"
				+ "       t.conttrees_no,\n" + "       t.contract_name,\n"
				+ "       t.is_sum,\n" + "       t.act_amount,\n"
				+ "       t.payed_amount,\n" + "       r.workflow_status\n"
				+ "  from con_j_balance r, con_j_contract_info t\n"
				+ " where r.applicat_date between\n" + "       to_date('"
				+ startDate + "'||'00:00:00', 'yyyy-mm-dd hh24:mi:ss') and\n"
				+ "       to_date('" + endDate
				+ "'||'23:59:59', 'yyyy-mm-dd hh24:mi:ss')\n"
				+ "   and t.con_id = r.con_id\n"
				+ "   and t.enterprise_code = r.enterprise_code\n"
				+ "   and r.enterprise_code = '" + enterprisecode + "'\n"
				+ "   and t.con_type_id='" + conTypeId + "'\n"
				+ "   and t.is_use = 'Y'\n" + "   and r.is_use = 'Y'";
		if (type.equals("1")) {
			sql += " and r.workflow_status = 1";
			countsql += " and r.workflow_status = 1";
			// if (woflowNo != null && !woflowNo.equals(""))
			sql += "and r.workflow_no in (" + woflowNo + ")";
			countsql += "and r.workflow_no in (" + woflowNo + ")";
		} else if (type.equals("2")) {
			sql += "  and r.workflow_status = 2";
			countsql += "  and r.workflow_status = 2";
		}
		PageObject pg = new PageObject();
		List list = bll.queryByNativeSQL(sql, start, limit);
		List arraylist = new ArrayList();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				ConBalanceFullForm form = new ConBalanceFullForm();
				Object[] data = (Object[]) it.next();
				form.setBalanceId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					form.setConId(Long.parseLong(data[1].toString()));
				if (data[2] != null)
					form.setBalaFlag(data[2].toString());
				if (data[3] != null)
					form.setCliendId(Long.parseLong(data[3].toString()));
				if (data[4] != null)
					form.setClientName(data[4].toString());
				if (data[5] != null)
					form.setApplicatPrice(Double
							.parseDouble(data[5].toString()));
				if (data[6] != null)
					form.setOperateBy(data[6].toString());
				if (data[7] != null)
					form.setOperateName(data[7].toString());
				if (data[8] != null)
					form.setOperateDepCode(data[8].toString());
				if (data[9] != null)
					form.setOperateDeptName(data[9].toString());
				if (data[10] != null)
					form.setApplicatDate(data[10].toString());
				if (data[11] != null)
					form.setConttreesNo(data[11].toString());
				if (data[12] != null)
					form.setContractName(data[12].toString());
				if (data[13] != null)
					form.setIssum(data[13].toString());
				if (data[14] != null)
					form.setActAmount(Double.parseDouble(data[14].toString()));
				if (data[15] != null)
					form
							.setPayedAmount(Double.parseDouble(data[15]
									.toString()));
				if (data[16] != null)
					form.setWorkflowStatus(Long.parseLong(data[16].toString()));
				arraylist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(countsql).toString());
		pg.setList(arraylist);
		pg.setTotalCount(totalCount);
		return pg;
	}

	// 取审批记录
	public List<ConApproveForm> getApproveList(Long id) {
		Long entryId = this.findById(id).getWorkflowNo();
		List list = new ArrayList();

		String sql = "SELECT t.*,getworkername(t.caller) callername  FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id in (4,5,6,7)"
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
			// if (data[10] != null) {
			// model.setCaller(data[10].toString());
			// }
			arraylist.add(model);
		}
		return arraylist;
	}

	/**
	 * add by liuyi 091120 批量修改采购合同付款申请
	 * 
	 * @param addList
	 * @param updateList
	 * @param ids
	 */
	public Long saveModiRec(Long appIdFlag, List<ConJBalance> addList,
			List<ConJBalance> updateList, String ids) {
		if (appIdFlag == null) {
			appIdFlag = bll.getMaxId("CON_J_BALANCE", "app_id");
		}
		if (addList != null && addList.size() > 0) {
			for (ConJBalance entity : addList) {
				try {
					entity.setBalanceId(bll.getMaxId("CON_J_BALANCE",
							"BALANCE_ID"));
					entity.setWorkflowStatus(0L);
					entity.setAppId(appIdFlag);
					entity.setConBalanceDegreeLong(this.getFindConBalaceDegree(entity.getConId()));
					entityManager.persist(entity);
				} catch (RuntimeException re) {
					LogUtil.log("update failed", Level.SEVERE, re);
					throw re;
				}
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (ConJBalance entity : updateList) {
				try {
					entityManager.merge(entity);
				} catch (RuntimeException re) {
					LogUtil.log("update failed", Level.SEVERE, re);
					throw re;
				}
			}
		}
		if (ids != null && ids.length() > 0) {
			String sql = "update Con_J_Balance a set a.is_use='N' where a.balance_id in ("
					+ ids + ") \n";
			bll.exeNativeSQL(sql);
		}
		return appIdFlag;
	}

	private Long getFindConBalaceDegree(Long conId) {
		String sql = "select max(t.con_balance_degree) from con_j_balance t where t.is_use ='Y'" +
				" and t.con_id ="+conId+" ";
		Long count = (bll.getSingal(sql) == null) ? 1l : (Long.parseLong(bll
				.getSingal(sql).toString()) + 1l);
		return count;

	}

	public PageObject bqfindAppConList(String status, String approved,
			String rightIds, String appId, String entryBy,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pObj = new PageObject();
		String sql = "select distinct (b.app_id),\n"
				+ "                b.operate_dep_code,\n"
				+ "                b.workflow_no,\n"
				+ "                b.workflow_status,\n"
				+ "                b.bala_flag,\n"
				+ "                to_char(b.applicat_date, 'yyyy-mm'),\n"
				+ "                b.entry_by,\n"
				+ "                to_char(b.entry_date, 'yyyy-mm-dd'),\n"
				+ "                b.money_unit,\n"
				+ "                getworkername(b.entry_by),\n"
				+ "                getdeptname(b.operate_dep_code),\n"
				+ "                b.ELSE_PRICE \n"
				+ "  from con_j_balance b\n" + " where b.is_use = 'Y'\n"
				// + " and b.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and b.app_id is not null";
		if (appId != null && !appId.equals("")) {
			sql += " and b.app_id ='" + appId + "'";
		}

		if (approved != null && approved.equals("Y")) {
			if (status.equals("approve")) {
				// if (rightIds != null && (!"".equals(rightIds))) {
				sql += " and b.workflow_status=1 \n";
				sql += " and b.workflow_no in (" + rightIds + ")\n";
				// }
			} else if (status.equals("endsign")) {
				sql += " and b.workflow_status=2 \n";
			}
		} else if (approved != null && approved.equals("N")) {
			sql += " and b.workflow_status in (0,3)\n";
			if (entryBy != null && !entryBy.equals("999999")) {
				sql += "and b.entry_by = '" + entryBy + "' \n";
			}
		}
		String sqlCount = "select count(*) from (" + sql + ") \n";
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arraylist = new ArrayList();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				BpAppHeaderForm form = new BpAppHeaderForm();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					form.setAppId(data[0].toString());
				if (data[1] != null)
					form.setOperateDepDode(data[1].toString());
				if (data[2] != null)
					form.setWorkflowNO(data[2].toString());
				if (data[3] != null)
					form.setWorkflowStatus(data[3].toString());
				if (data[4] != null)
					form.setBalaFlag(data[4].toString());
				if (data[5] != null)
					form.setApplicatDate(data[5].toString());
				if (data[6] != null)
					form.setEntryBy(data[6].toString());
				if (data[7] != null)
					form.setEntryDate(data[7].toString());
				if (data[8] != null)
					form.setMoneyUnit(data[8].toString());
				if (data[9] != null)
					form.setEntryByName(data[9].toString());
				if (data[10] != null)
					form.setOperateDepName(data[10].toString());
				if (data[11] != null)
					form.setWzFlag(data[11].toString());
				arraylist.add(form);
			}
			pObj.setList(arraylist);
			pObj.setTotalCount(totalCount);
			return pObj;
		} else {
			return null;
		}
	}

	
	public PageObject bqfindBalanceListByAppId(String appId,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pObj = new PageObject();
		String sql = "select b.balance_id,\n"
				+ "       a.conttrees_no,\n"
				+ "       GETCLIENTNAME(a.cliend_id) clientName,\n"
				+ "       a.contract_name,\n"
				+ "       a.act_amount,\n"
				+ "       (select sum(e.pass_price) from con_j_balance e where e.con_id=a.con_id and e.bala_flag = 2  and e.is_use='Y' and e.con_balance_degree < b.con_balance_degree) payedmoney,\n"
				+ "       b.applicat_price,\n" + "       b.pass_price,\n"
				+ "       b.memo,\n" + "       b.app_id,\n"
				+ "       b.con_id\n"
				+ "  from con_j_contract_info a, con_j_balance b\n"
				+ " where b.app_id = '" + appId + "'\n"
				+ "   and b.is_use = 'Y'\n" + "   and b.enterprise_code = '"
				+ enterpriseCode + "'\n" + "   and a.con_id = b.con_id";
		String sqlCount = "select count(*) from (" + sql + ") \n";
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arraylist = new ArrayList();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				BpAppDetailForm form = new BpAppDetailForm();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					form.setBalanceId(data[0].toString());
				if (data[1] != null)
					form.setContractNo(data[1].toString());
				if (data[2] != null)
					form.setClientName(data[2].toString());
				if (data[3] != null)
					form.setContractName(data[3].toString());
				if (data[4] != null)
					form.setActAccount(Double.parseDouble(data[4].toString()));
				else
					form.setActAccount(0.0);
				if (data[5] != null)
					form
							.setPayedAccount(Double.parseDouble(data[5]
									.toString()));
				else
					form.setPayedAccount(0.0);
				if (data[6] != null)
					form.setApplicatPrice(Double
							.parseDouble(data[6].toString()));
				else
					form.setApplicatPrice(0.0);
				if (data[7] != null)
					form.setPassPrice(Double.parseDouble(data[7].toString()));
				else
					form.setPassPrice(0.0);
				if (data[8] != null)
					form.setMemo(data[8].toString());
				if (data[9] != null)
					form.setAppId(data[9].toString());
				if (data[10] != null)
					form.setConId(data[10].toString());

				form
						.setBalance(form.getActAccount()
								- (form.getPayedAccount() != null ? form
										.getPayedAccount() : 0.0)
								- form.getPassPrice());
				arraylist.add(form);
			}
			pObj.setList(arraylist);
			pObj.setTotalCount(totalCount);
			return pObj;
		} else {
			return null;
		}

	}

	/**
	 * add by liuyi 091120 删除采购合同付款申请数据
	 * 
	 * @param appId
	 */
	public void deleteRecByAppId(String appId) {
		String sql = " update Con_J_Balance a set a.is_use='N' where a.app_id = '"
				+ appId + " '\n";
		bll.exeNativeSQL(sql);

	}

	public List<ConJBalance> getBalaceListByAppId(String appId,
			String enterpriseCode) {
		String sql = "select * from  Con_J_Balance a " + " where a.is_use='Y' "
		// + " and a.enterprise_code = '" + enterpriseCode + "' "
				+ " and a.app_id = '" + appId + "'";
		List<ConJBalance> list = bll.queryByNativeSQL(sql, ConJBalance.class);
		return list;
	}

	public PageObject bqContractSelect(String fuzzy, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select t.con_id,\n"
				+ "                t.conttrees_no,\n"
				+ "                t.contract_name,\n"
				+ "                t.cliend_id,\n"
				+ "                t.act_amount,\n"
				+ "                getclientname(t.cliend_id) clientName,\n"
				+ "                (select sum(a.pass_price) from con_j_balance a where a.con_id = t.con_id and a.workflow_status=2 and a.bala_flag=2 and a.is_use='Y') payedAccount\n"
				+ "           from con_j_contract_info t \n"
				+ "          where t.is_use = 'Y'\n"
				+ "            and t.con_type_id = 1\n"
				+ "            and t.workflow_status = 2\n"
				+ "            and t.act_amount >= (select nvl(sum(b.pass_price),0) from con_j_balance b where b.con_id = t.con_id and b.is_use='Y') \n"
				+ " and t.exec_flag = 1 and ((((select count(*) from con_j_balance b where b.con_id=t.con_id and b.workflow_status=2 and b.is_use='Y') > 0)\n"
				+ "           or (select count(*) from con_j_balance b where b.con_id=t.con_id and b.workflow_status in (1,0) and b.is_use='Y') = 0)\n"
				+ "            or (select count(*) from con_j_balance c where c.con_id=t.con_id) = 0)";

		if (fuzzy != null && !fuzzy.equals("")) {
			sql += "  and (t.conttrees_no like '%" + fuzzy
					+ "%' or t.contract_name like '%" + fuzzy
					+ "%' or getclientname(t.cliend_id) like '%" + fuzzy
					+ "%') \n";
		}
		// String sqlcount = "select count(1)\n"
		// + " from con_j_contract_info t\n"
		// + " where t.is_use = 'Y'\n"
		// + " and t.con_type_id = 1 \n"
		// + " and t.workflow_status = 2 \n"
		// + " and t.exec_flag = 1 \n";
		String sqlcount = "select count(*) from (" + sql + ") \n";
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
				con.setActAmount(Double.parseDouble(o[4].toString()));
			if (o[5] != null)
				con.setClientName(o[5].toString());
			if (o[6] != null)
				con.setPayedAmount(Double.parseDouble((o[6].toString())));
			arrlist.add(con);
		}
		pg.setList(arrlist);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlcount).toString()));
		return pg;
	}

	public List<BpAppDetailForm> bqFindBalaceReportByAppId(String appId) {
		String sql = "select b.balance_id,\n"
				+ "       a.conttrees_no,\n"
				+ "       GETCLIENTNAME(a.cliend_id) clientName,\n"
				+ "       a.contract_name,\n"
				+ "       a.act_amount,\n"
				+ "        (select sum(e.pass_price) from con_j_balance e where e.con_id=a.con_id and e.bala_flag = 2  and e.is_use='Y' and e.con_balance_degree < b.con_balance_degree) payedmoney,\n"
				+ "       b.applicat_price,\n" + "       b.pass_price,\n"
				+ "       b.memo,\n" + "       b.app_id,\n"
				+ "       b.con_id,\n" + "       b.workflow_no,\n"
				+ "       b.workflow_status,\n"
				+ "       getdeptname(b.operate_dep_code) deptName,\n"
				+ "        getworkername(b.entry_by) workerName,\n"
				+ "       decode(b.money_unit,'1','元','2','万元') moneyUnit, \n"
				+ "       to_char(b.applicat_date,'yyyy-mm') reportName \n"
				+ "  from con_j_contract_info a, con_j_balance b\n"
				+ " where b.app_id = '" + appId + "'\n"
				+ "   and b.is_use = 'Y'\n" + "   and a.con_id = b.con_id";
		List list = bll.queryByNativeSQL(sql);
		List<BpAppDetailForm> arraylist = new ArrayList<BpAppDetailForm>();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			Double hjActTemp = 0d;
			Double hjPayTemp = 0d;
			Double hjAppTemp = 0d;
			Double hjPassTemp = 0d;
			Double hjBalaTemp = 0d;
			while (it.hasNext()) {
				BpAppDetailForm form = new BpAppDetailForm();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					form.setBalanceId(data[0].toString());
				if (data[1] != null)
					form.setContractNo(data[1].toString());
				if (data[2] != null)
					form.setClientName(data[2].toString());
				if (data[3] != null)
					form.setContractName(data[3].toString());
				if (data[4] != null)
					form.setActAccount(Double.parseDouble(data[4].toString()));
				else
					form.setActAccount(0.0);
				if (data[5] != null)
					form
							.setPayedAccount(Double.parseDouble(data[5]
									.toString()));
				else
					form.setPayedAccount(0.0);
				if (data[6] != null)
					form.setApplicatPrice(Double
							.parseDouble(data[6].toString()));
				else
					form.setApplicatPrice(0.0);
				if (data[7] != null)
					form.setPassPrice(Double.parseDouble(data[7].toString()));
				else
					form.setPassPrice(0.0);
				if (data[8] != null)
					form.setMemo(data[8].toString());
				if (data[9] != null)
					form.setAppId(data[9].toString());
				if (data[10] != null)
					form.setConId(data[10].toString());

				form
						.setBalance(form.getActAccount()
								- (form.getPayedAccount() != null ? form
										.getPayedAccount() : 0.0)
								- form.getPassPrice());
				if (data[11] != null)
					form.setWorkflowNo(data[11].toString());
				if (data[12] != null)
					form.setWorkflowStatus(data[12].toString());
				if (data[13] != null)
					form.setDeptName(data[13].toString());
				if (data[14] != null)
					form.setWorkerName(data[14].toString());
				if (data[15] != null)
					form.setMoneyUnit(data[15].toString());
				if (data[16] != null)
					form.setReportDate(data[16].toString().substring(0, 4)
							+ "年" + data[16].toString().substring(5, 7) + "月");
				hjActTemp += form.getActAccount();
				hjAppTemp += form.getApplicatPrice();
				hjBalaTemp += form.getBalance();
				hjPassTemp += form.getPassPrice();
				hjPayTemp += form.getPayedAccount();
				form.setHjAct(hjActTemp);
				form.setHjApp(hjAppTemp);
				form.setHjBala(hjBalaTemp);
				form.setHjPass(hjPassTemp);
				form.setHjPay(hjPayTemp);
				arraylist.add(form);

			}
		}
		return arraylist;
	}

	public List<ConApproveForm> getCgApproveList(String appId) {
		Long entryId = this.getBalaceListByAppId(appId, null).get(0)
				.getWorkflowNo();
		List list = new ArrayList();
		String sql = "SELECT t.caller,getworkername(t.caller) callername  FROM wf_j_historyoperation t"
				+ " WHERE t.entry_id = '"
				+ entryId
				+ "'"
				+ "AND t.step_id in (4,7)"
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
				model.setCaller(data[0].toString());
			}
			if (data[1] != null) {
				model.setOpinion(data[1].toString());
			}
			// 保存工作流
			model.setStepName(entryId.toString());
			// if (data[6] != null) {
			// model.setCaller(data[6].toString());
			// }
			// if (data[8] != null) {
			// model.setOpinion(data[8].toString());
			// }
			// if (data[9] != null) {
			// model.setOpinionTime(data[9].toString());
			// ;
			// }
			// if (data[10] != null) {
			// model.setCaller(data[10].toString());
			// }
			arraylist.add(model);
		}
		return arraylist;

	}

	public PageObject balanceQueryList(String startDate, String endDate,
			int... rowStartIdxAndCount) {

		PageObject pObj = new PageObject();
		String sql = "select distinct (b.app_id),\n"
				+ "                b.operate_dep_code,\n"
				+ "                b.workflow_no,\n"
				+ "                b.workflow_status,\n"
				+ "                b.bala_flag,\n"
				+ "                to_char(b.applicat_date, 'yyyy-mm'),\n"
				+ "                b.entry_by,\n"
				+ "                to_char(b.entry_date, 'yyyy-mm-dd'),\n"
				+ "                b.money_unit,\n"
				+ "                getworkername(b.entry_by),\n"
				+ "                getdeptname(b.operate_dep_code),\n"
				+ "                b.ELSE_PRICE \n"
				+ "  from con_j_balance b\n" + " where b.is_use = 'Y'\n"
				// + " and b.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and b.app_id is not null";
		if (startDate != null && !startDate.equals("")) {
			sql += " and to_char(b.entry_date,'yyyy-mm') >='" + startDate + "'";
		}
		if (endDate != null && !endDate.equals("")) {
			sql += " and to_char(b.entry_date,'yyyy-mm') <= '" + endDate + "'";
		}

		String sqlCount = "select count(*) from (" + sql + ") \n";
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arraylist = new ArrayList();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				BpAppHeaderForm form = new BpAppHeaderForm();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					form.setAppId(data[0].toString());
				if (data[1] != null)
					form.setOperateDepDode(data[1].toString());
				if (data[2] != null)
					form.setWorkflowNO(data[2].toString());
				if (data[3] != null)
					form.setWorkflowStatus(data[3].toString());
				if (data[4] != null)
					form.setBalaFlag(data[4].toString());
				if (data[5] != null)
					form.setApplicatDate(data[5].toString());
				if (data[6] != null)
					form.setEntryBy(data[6].toString());
				if (data[7] != null)
					form.setEntryDate(data[7].toString());
				if (data[8] != null)
					form.setMoneyUnit(data[8].toString());
				if (data[9] != null)
					form.setEntryByName(data[9].toString());
				if (data[10] != null)
					form.setOperateDepName(data[10].toString());
				if (data[11] != null)
					form.setWzFlag(data[11].toString());
				arraylist.add(form);
			}
			pObj.setList(arraylist);
			pObj.setTotalCount(totalCount);
			return pObj;
		} else {
			return null;
		}

	}
}