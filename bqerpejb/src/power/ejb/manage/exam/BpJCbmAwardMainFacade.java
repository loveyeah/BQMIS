package power.ejb.manage.exam;

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

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.exam.form.BpApproveForm;
import power.ejb.manage.exam.form.CashModel;

/**
 * Facade for entity BpJCbmAwardMain.
 * 
 * @see power.ejb.manage.exam.BpJCbmAwardMain
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJCbmAwardMainFacade implements BpJCbmAwardMainFacadeRemote {
	// property constants
	public static final String MONTH = "month";
	public static final String WORKFLOW_NO = "workflowNo";
	public static final String STATUS = "status";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	WorkflowService service;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public BpJCbmAwardMainFacade() {
		service = new WorkflowServiceImpl();
	}

	public void save(BpJCbmAwardMain entity) {
		LogUtil.log("saving BpJCbmAwardMain instance", Level.INFO, null);
		try {
			if(entity.getDeclareId() == null)
			{
				entity.setDeclareId(bll.getMaxId("Bp_J_Cbm_Award_Main", "Declare_Id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(BpJCbmAwardMain entity) {
		LogUtil.log("deleting BpJCbmAwardMain instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpJCbmAwardMain.class, entity
					.getDeclareId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJCbmAwardMain update(BpJCbmAwardMain entity) {
		LogUtil.log("updating BpJCbmAwardMain instance", Level.INFO, null);
		try {
			BpJCbmAwardMain result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJCbmAwardMain findByDate(String date, String enterpriseCode) {
		try {
			final String queryString = "select model from BpJCbmAwardMain model where model.yearMonth=:yearMonth and model.enterpriseCode=:enterpriseCode and model.isUse='Y'";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("yearMonth", date);
			query.setParameter("enterpriseCode", enterpriseCode);
			List<BpJCbmAwardMain> result = query.getResultList();
			if (result != null && result.size() > 0) {
				return result.get(0);
			}
			return null;

		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Long getMaxAwardId() {
		try {
			final String queryString = "select nvl(max(t.declare_detail_id),0)+1 from  BP_J_CBM_AWARD_DETAIL t";
			Object obj = bll.getSingal(queryString);
			return Long.valueOf(obj.toString());

		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<BpJCbmAwardMain> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all BpJCbmAwardMain instances", Level.INFO, null);
		try {
			final String queryString = "select model from BpJCbmAwardMain model";
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
	public List<CashModel> getAwardValueList(String datetime) {
		String sql =  "select a.affiliated_id,\n" +
			"       b.item_name,\n" + 
			"       getunitname(b.unit_id),\n" + 
			"       (select e.YEAR_BUDGET_VALUE\n" + 
			"          from BP_J_CBM_EXECUTION e\n" + 
			"         where e.year_month = ?\n" + 
			"           and e.belong_block = '3'\n" + 
			"           and e.item_id = a.item_id) fgsjhz,\n" + 
			"       (select e.real_value\n" + 
			"          from BP_J_CBM_EXECUTION e\n" + 
			"         where e.year_month = ?\n" + 
			"           and e.belong_block = '3'\n" + 
			"           and e.item_id = a.item_id) sjz,\n" + 
			"       getdeptname(a.dep_code),\n" + 
			"       a.affiliated_level,\n" + 
			"       a.affiliated_value,\n" + 
			"       c.cash_bonus,\n" + 
			"       c.memo,\n" + 
			"       c.declare_detail_id,b.data_type,\n" + 
			"       (select d.status from bp_j_cbm_award_main d where d.year_month = ? and d.is_use = 'Y')wfstatus\n" + //add by drdu 091201
			"  ,t.topic_id,t.topic_name,t.coefficient,b.item_code \n" + // add by liuyi 091207 
			"  from BP_C_CBM_AFFILIATED a, BP_C_CBM_ITEM b, BP_J_CBM_AWARD_DETAIL c,BP_C_CBM_TOPIC t \n" + 
			" where a.item_id = b.item_id\n" + 
			"   and a.affiliated_id = c.affiliated_id(+)\n" + 
			"   and a.is_use = 'Y'\n" + 
			"  and t.is_use='Y' \n" + 
			" and b.topic_id=t.topic_id \n" + 
			"   and c.year_month(+) = ?" + 
			"   and b.is_use = 'Y' order by t.display_no, b.display_no,a.affiliated_level, a.dep_code";
		 
		List<Object[]> list = bll.queryByNativeSQL(sql,
				new Object[] { datetime,datetime,datetime,datetime});
		List<CashModel> result = new ArrayList<CashModel>();
		if (list != null && list.size() > 0) {
			for (Object[] o : list) {
				CashModel m = new CashModel();
				if (o[0] != null)
					m.setAffiliatedId(Long.parseLong(o[0].toString()));
				if (o[1] != null)
					m.setItemName(o[1].toString());
				if (o[2] != null)
					m.setUnitName(o[2].toString());
				if (o[3] != null)
					m.setPlanValue(Double.parseDouble(o[3].toString()));
				if (o[4] != null)
					m.setRealValue(Double.parseDouble(o[4].toString()));
				if (o[5] != null)
					m.setAffiliatedDept(o[5].toString());
				if (o[6] != null)
					m.setAffiliatedLevel(o[6].toString());
				if (o[7] != null)
					m.setAffiliatedValue(o[7].toString());
				if (o[8] != null)
					m.setCash(Double.parseDouble(o[8].toString()));
				if (o[9] != null)
					m.setMemo(o[9].toString());
				if (o[10] != null)
					m.setDeclarDetailId(Long.parseLong(o[10].toString()));
				String  data="";
				if (o[11]!=null)
					 data=o[11].toString();
				if (data == "1" || data.equals("1")) {
					if (m.getPlanValue() != null && m.getRealValue() != null
							&& m.getRealValue() != 0) {
						double compRate = Math.rint((m.getPlanValue() / m.getRealValue()) * 100);
						m.setComplete((int)compRate + "%");
					} else
						m.setComplete("0%");
				} else if (data == "2" || data.equals("2")) {
					if (m.getPlanValue() != null && m.getRealValue() != null
							&& m.getPlanValue() != 0) {
						double rate = Math.rint((m.getRealValue() / m.getPlanValue()) * 100);
						m.setComplete((int)rate + "%");
					}else
						m.setComplete("0%");
				} else
					m.setComplete("0%");
				
				if (o[12] != null)//add by drdu 091201
				{
					m.setStatus(o[12].toString());
				}
				// add by liuyi 091207
				if(o[13] != null)
					m.setTopicId(Long.parseLong(o[13].toString()));
				if(o[14] != null)
					m.setTopicName(o[14].toString());
				if(o[15] != null)
				{
					double flag = Double.parseDouble(o[15].toString()) * 100;
					m.setCoefficient((int)flag + "%");
				}
				else
					m.setCoefficient("0%");
				if(o[16] != null)
					m.setItemCode(o[16].toString());
				
				if(m.getAffiliatedValue() != null)
				{
					String sub = m.getComplete().substring(0,m.getComplete().length() - 1);
					double subDou = Double.parseDouble(sub);
					if(subDou >= 100 && m.getCash() == null)
					{
//						if(m.getCash() == null)
							m.setCash(Double.parseDouble(m.getAffiliatedValue()));
					}else if(m.getCash() == null)
						m.setCash(0.0);
				}
				result.add(m);
			}
		}
		if(result.size() > 2)
		{
			for(int i = 1; i <= result.size() - 1; i++)
			{
				if(result.get(i).getTopicId() == result.get(i-1).getTopicId()) 
				{
					result.get(i).setTopicName("");
					result.get(i).setCoefficient("");
				}
				if(result.get(i).getItemCode().equals(result.get(i-1).getItemCode())
						|| result.get(i-1).getItemCode().equals(""))
				{
					result.get(i).setItemName("");
					result.get(i).setUnitName("");
					result.get(i).setPlanValue(null);
					result.get(i).setRealValue(null);
					result.get(i).setComplete("");
				}
			}
		}
		return result;

	}

	public boolean saveAwardValuelist(List<BpJCbmAwardDetail> updatelist,
			String datetime, String enterpriseCode,String workCode) {
		BpJCbmAwardMain main = findByDate(datetime, enterpriseCode);
		if (main == null) {
			main = new BpJCbmAwardMain();
			main.setEnterpriseCode(enterpriseCode);
			main.setIsUse("Y");
			main.setYearMonth(datetime);
			main.setStatus("0");
			//add by drdu 091201
			main.setLastModifyBy(workCode);
			main.setLastModifyDate(new Date());
			this.save(main); 
		} 
		Long detailId = bll.getMaxId("BP_J_CBM_AWARD_DETAIL", "declare_detail_id");
		for (BpJCbmAwardDetail model : updatelist) {  
			if (model.getDeclareDetailId() != null) {
				updateBpJCbmAwardDetail(model);
			} else { 
				model.setDeclareDetailId(detailId++);
				saveBpJCbmAwardDetail(model);
			}
		}

		return true;
	}

	public void saveBpJCbmAwardDetail(BpJCbmAwardDetail entity) {
		try { 
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public boolean updateBpJCbmAwardDetail(BpJCbmAwardDetail entity) {
		try {
			entityManager.merge(entity);
			return true;
		} catch (RuntimeException re) {
			throw re;

		}
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findApproveList(String enterpriseCode,String month,final int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		
		String sql =
			"select a.declare_id,\n" +
			"       a.year_month,\n" + 
			"       a.workflow_no,\n" + 
			"       a.status,\n" + 
			"       getworkername(a.last_modify_by),\n" + 
			"       a.last_modify_date\n" + 
			"  from bp_j_cbm_award_main a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.status = '1'";

		String sqlCount = "select count(1)\n" + 
			"  from bp_j_cbm_award_main a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.status = '1'";

		if(month != null && month.length() > 0)
		{
			sql += "  and a.year_month like '" + month + "%'";
			sqlCount+="  and a.year_month like '" + month + "%'";
			
		}
		
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it = list.iterator();
		List<BpApproveForm> arrlist = new ArrayList();
		while (it.hasNext()) {
			BpApproveForm form = new BpApproveForm();
			Object[] data = (Object[]) it.next();
			 form.setDeclareId(Long.parseLong(data[0].toString()));
			 if(data[1] != null)
				 form.setYearMonth(data[1].toString());
			 if(data[2] != null)
				 form.setWorkflowNo(Long.parseLong(data[2].toString()));
			 if(data[3] != null)
				 form.setStatus(data[3].toString());
			 if(data[4] != null)
				 form.setLastModifyName(data[4].toString());
			 if(data[5] != null)
				 form.setLastModifyDate(data[5].toString());
			 
			 arrlist.add(form);
		}
		pg.setList(arrlist);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}
	
	/**
	 * 奖金申报上报
	 * add by drdu 091130
	 * @param month
	 * @param flowCode
	 * @param workerCode
	 * @param actionId
	 */
	public void reportTo(String month,String flowCode,String workerCode,Long actionId)
	{
		WorkflowService service = new WorkflowServiceImpl();
		long entryId = service.doInitialize(flowCode,workerCode,month);
		service.doAction(entryId, workerCode, actionId, "上报", null); 
		String sql=" update bp_j_cbm_award_main  set status=1,workflow_no="+entryId+"  where year_month='"+month+"'";
		bll.exeNativeSQL(sql);
	}
	
	/**
	 * 奖金申报审批
	 * add by drdu 091130
	 * @param entryId
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param date
	 * @param enterpriseCode
	 */
	public void awardApproveSign(String declareId,Long entryId, String workerCode,Long actionId, String approveText,
			String nextRoles,String eventIdentify,String month,String responseDate)
	{
		BpJCbmAwardMain entity = this.findByDate(month,"hfdc");
		if(eventIdentify.equals("WC"))
		{
			entity.setStatus("2");
			//this.update(entity);
		}
		if(eventIdentify.equals("TH"))
		{
			entity.setStatus("3");
		}
		this.changeWfInfo(entryId, workerCode, actionId,approveText, nextRoles);
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

	public PageObject getAllCahsResiter(String month,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = 
			"select  a.item_id,\n" +
			"a.item_name,\n" + 
			"(select sum(c.cash_bonus) from BP_J_CBM_AWARD_DETAIL c,BP_C_CBM_AFFILIATED d\n" + 
			"         where c.affiliated_id=d.affiliated_id\n" + 
			"         and c.is_use='Y'\n" + 
			"         and d.is_use='Y'\n" + 
			"         and d.affiliated_level = 2\n" + 
			"         and c.year_month='" + month+ "'\n" + 
			"         and a.item_id=d.item_id) value2,\n" + 
			"         (select sum(c.cash_bonus) from BP_J_CBM_AWARD_DETAIL c,BP_C_CBM_AFFILIATED d\n" + 
			"         where c.affiliated_id=d.affiliated_id\n" + 
			"         and c.is_use='Y'\n" + 
			"         and d.is_use='Y'\n" + 
			"         and d.affiliated_level = 3\n" + 
			"         and c.year_month='" + month+ "'\n" + 
			"         and a.item_id=d.item_id) value3,\n" + 
			"         (select sum(c.cash_bonus) from BP_J_CBM_AWARD_DETAIL c,BP_C_CBM_AFFILIATED d\n" + 
			"         where c.affiliated_id=d.affiliated_id\n" + 
			"         and c.is_use='Y'\n" + 
			"         and d.is_use='Y'\n" + 
			"         and d.affiliated_level = 4\n" + 
			"         and c.year_month='" + month+ "'\n" + 
			"         and a.item_id=d.item_id) value4\n" + 
			"from BP_C_CBM_ITEM a\n" + 
			"where  a.is_use='Y'\n" + 
			"and a.item_id in (select b.item_id from BP_C_CBM_AFFILIATED b where b.is_use='Y')";
		
		String sqlCount = 
			"select  count(*)\n" +
			"from BP_C_CBM_ITEM a\n" + 
			"where  a.is_use='Y'\n" + 
			"and a.item_id in (select b.item_id from BP_C_CBM_AFFILIATED b where b.is_use='Y')";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;

	}
}
