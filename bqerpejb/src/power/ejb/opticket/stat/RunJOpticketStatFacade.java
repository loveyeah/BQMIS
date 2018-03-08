package power.ejb.opticket.stat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity RunJOpticketStat.
 * 
 * @see power.ejb.opticket.stat.RunJOpticketStat
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJOpticketStatFacade implements RunJOpticketStatFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "BaseDataManagerImpl")
	protected BaseDataManager dll;
	@EJB(beanName = "RunJOpticketStatDetailFacade")
	protected RunJOpticketStatDetailFacadeRemote re;

	public RunJOpticketStat save(RunJOpticketStat entity) {
		try {
			if (entity.getId() == null) {
				entity.setId(bll.getMaxId("run_j_opticket_stat", "id"));
			}
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(RunJOpticketStat entity) {
		LogUtil.log("deleting RunJOpticketStat instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunJOpticketStat.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJOpticketStat update(RunJOpticketStat entity) {
		LogUtil.log("updating RunJOpticketStat instance", Level.INFO, null);
		try {
			RunJOpticketStat result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJOpticketStat findById(Long id) {
		LogUtil.log("finding RunJOpticketStat instance with id: " + id,
				Level.INFO, null);
		try {
			RunJOpticketStat instance = entityManager.find(
					RunJOpticketStat.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Map<String, Object> getStatData(String statBy, String title,
			Long yearMonth, String specialCode, String enterprisecode) {
		RunJOpticketStat model = null;
		String sql1 = "select t.* from RUN_J_OPTICKET_STAT t where t.report_date="
				+ yearMonth
				+ " and t.profession='"
				+ specialCode
				+ "' and t.enterprisecode='" + enterprisecode + "'";
		List<RunJOpticketStat> slist = bll.queryByNativeSQL(sql1,
				RunJOpticketStat.class);
		if (slist.size() > 0) {
			model = slist.get(0);
		}
		if (model == null) {
			if (this.statOpticket(statBy, title, yearMonth, specialCode,
					enterprisecode)) {
				model = (RunJOpticketStat) this.getStatData(statBy, title,
						yearMonth, specialCode, enterprisecode).get("model");
				return this.getStatData(statBy, title, yearMonth, specialCode,
						enterprisecode);
			} else {
				return null;
			}

		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("model", model);
			String sql2 = "select t.id,\n"
					+ "       getworkername(m.emp_code),\n"
					+ "       nvl(t.opticket_count,0),\n"
					+ "       nvl(t.opticket_item_count,0),\n"
					+ "       nvl(t.no_problem_opticket_count,0),\n"
					+ "       nvl(t.no_problem_opticket_item_count,0),\n"
					+ "       m.dept_id,\n"
					+ "       nvl(t.isclear,'N')\n"
					+ "  from run_j_opticket_stat_detail t,(select b.dept_id, a.emp_code, b.order_by dep_order, a.order_by emp_order\n"
					+ "  from hr_j_emp_info a, hr_c_dept b\n"
					+ " where a.dept_id = b.dept_id\n"
					+ "   and b.is_use = 'Y'\n" //update by sychen 20100902
//					+ "   and b.is_use = 'U'\n" 
					+ "   and a.dept_id in (select t.dept_id\n"
					+ "                       from hr_c_dept t\n"
					+ "                      where t.is_use = 'Y'\n"//update by sychen 20100902
//					+ "                      where t.is_use = 'U'\n"
					+ "                      start with t.dept_id = ?\n"
					+ "                     connect by prior t.dept_id = t.pdept_id)\n"
					+ ")m\n" + " where t.workcode(+) = m.emp_code\n"
					+ " and t.report_id(+) = ?\n"
					+ " order by m.dept_id, m.emp_order";
			List list = bll.queryByNativeSQL(sql2, new Object[] { specialCode,
					model.getId() });
			List<RunJOpticketStatDetail> arr = new ArrayList<RunJOpticketStatDetail>();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					Object[] ob = (Object[]) list.get(i);
					RunJOpticketStatDetail entity = new RunJOpticketStatDetail();
					if (ob[0] != null) {
						entity.setId(Long.parseLong(ob[0].toString()));
					}
					if (ob[1] != null) {
						entity.setWorkcode(ob[1].toString());
					}
					if (ob[2] != null) {
						entity.setOpticketCount(Long
								.parseLong(ob[2].toString()));
					}
					if (ob[3] != null) {
						entity.setOpticketItemCount(Long.parseLong(ob[3]
								.toString()));
					}
					if (ob[4] != null) {
						entity.setNoProblemOpticketCount(Long.parseLong(ob[4]
								.toString()));
					}
					if (ob[5] != null) {
						entity.setNoProblemOpticketItemCount(Long
								.parseLong(ob[5].toString()));
					}
					if (ob[6] != null) {
						entity.setShiftId(Long.parseLong(ob[6].toString()));
					}
					if (ob[7] != null) {
						entity.setIsclear(ob[7].toString());
					}
					arr.add(entity);
				}
			}
			map.put("list", arr);
			return map;
		}
	}

	public boolean statOpticket(String statBy, String title, Long yearMonth,
			String specialCode, String enterprisecode) {
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String sd = sf.format(date);
		// modify by liuyi 090930 
//		List<TreeNode> shiftList = dll.getDeptsByPid(Long
//				.parseLong(specialCode));
		List<TreeNode> shiftList = dll.getDeptsByPid(Long
				.parseLong(specialCode),null);
		if (shiftList == null) {
			return false;
		} else {
			RunJOpticketStat report = new RunJOpticketStat();
			report.setReportName(title);
			report.setStatBy(statBy);
			report.setStatDatea(new Date());
			report.setEnterprisecode(enterprisecode);
			report.setProfession(specialCode);
			report.setReportDate(yearMonth);
			report = save(report);
			for (TreeNode shift : shiftList) {
				PageObject po = dll.getWorkersByDeptId(Long.parseLong(shift
						.getId()),null,null, null, null);
				if (po == null || po.getList() == null
						|| po.getList().size() == 0)
					continue;
				List<Employee> members = po.getList();
				if (members == null)
					continue;
				StringBuffer sb = new StringBuffer();
				sb.append("''");
				for (Employee w : members) {
					sb.append(",'" + w.getWorkerCode() + "'");
				}
				String sql = "insert into run_j_opticket_stat_detail(id,report_id,shift_id,workcode,opticket_count,opticket_item_count,no_problem_opticket_count,no_problem_opticket_item_count,isclear,order_by)\n"
						+ "select (select nvl(max(id),0) from run_j_opticket_stat_detail)+rownum,"
						+ report.getId()
						+ ","
						+ shift.getId()
						+ ",decode(n.ma,null,m.ma,n.ma) man,\n"
						// + " m.sft_id,\n"
						+ "       nvl(n.op_count, 0) opc,\n"
						+ "       nvl(n.st_count, 0) ops,\n"
						+ "       nvl(n.op_count, 0) + nvl(m.no_pro_op_count, 0) allcountc,\n"
						+ "       nvl(n.st_count, 0) + nvl(m.no_pro_op_it_count, 0) allcounts,'N',rownum\n"
						+ "  from (select a.man ma,\n"
						+ "               nvl(a.opticket_count, 0) op_count,\n"
						+ "               nvl(b.step_count, 0) st_count\n"
						+ "          from (select sum(c) opticket_count, man\n"
						+ "                  from (select count(1) c, t.operatormans man\n"
						+ "                          from run_j_opticket t\n"
						+ "                         where t.is_standar = 'N'\n"
						+ "                           and t.is_use = 'Y'\n"
						+ "                           and to_char(t.end_time,'yyyyMM')  = '"
						+ yearMonth
						+ "'\n"
						+ "                           and t.operatormans in\n"
						+ "                               ("
						+ sb.toString()
						+ ")\n"
						+ "\n"
						+ "                         group by t.operatormans\n"
						+ "                        union\n"
						+ "\n"
						+ "                        select count(1) c, t.protectormans man\n"
						+ "                          from run_j_opticket t\n"
						+ "                         where t.is_standar = 'N'\n"
						+ "                           and t.is_use = 'Y'\n"
						+ "                           and to_char(t.end_time,'yyyyMM')  = '"
						+ yearMonth
						+ "'\n"
						+ "                           and t.protectormans in\n"
						+ "                               ("
						+ sb.toString()
						+ ")\n"
						+ "                         group by t.protectormans)\n"
						+ "                 group by man) a,\n"
						+ "               (select sum(c) step_count, man\n"
						+ "                  from (select count(1) c, t.operatormans man\n"
						+ "                          from run_j_opticket t, run_j_opticketstep tb\n"
						+ "                         where t.is_standar = 'N'\n"
						+ "                           and t.is_use = 'Y'\n"
						+ "                           and to_char(t.end_time,'yyyyMM')  = '"
						+ yearMonth
						+ "'\n"
						+ "                           and t.opticket_code = tb.opticket_code\n"
						+ "                           and t.operatormans in\n"
						+ "                               ("
						+ sb.toString()
						+ ")\n"
						+ "                         group by t.operatormans\n"
						+ "                        union\n"
						+ "\n"
						+ "                        select count(1) c, t.protectormans man\n"
						+ "                          from run_j_opticket t, run_j_opticketstep tb\n"
						+ "                         where t.is_standar = 'N'\n"
						+ "                           and t.is_use = 'Y'\n"
						+ "                           and to_char(t.end_time,'yyyyMM')  = '"
						+ yearMonth
						+ "'\n"
						+ "                           and t.opticket_code = tb.opticket_code\n"
						+ "                           and t.protectormans is not null\n"
						+ "                           and t.protectormans in\n"
						+ "                               ("
						+ sb.toString()
						+ ")\n"
						+ "                         group by t.protectormans)\n"
						+ "                 group by man) b\n"
						+ "         where a.man = b.man(+)) n\n"
						+ "\n"
						+ "   full outer join    (select j.workcode                       ma,\n"
						+ "               j.shift_id                       sft_id,\n"
						+ "               j.no_problem_opticket_count      no_pro_op_count,\n"
						+ "               j.no_problem_opticket_item_count no_pro_op_it_count\n"
						+ "          from run_j_opticket_stat i, run_j_opticket_stat_detail j\n"
						+ "         where i.id = j.report_id\n"
						+ "           and j.shift_id = "
						+ shift.getId()
						+ "\n"
						+ "           and i.report_date = "
						+ this.getLastMonth(yearMonth)
						+ ") m\n" 
						+" on  n.ma = m.ma";
//						+ " where n.ma(+) = m.ma";
				bll.exeNativeSQL(sql);
				entityManager.flush();
				String opcount_sql = "select count(t.opticket_code)\n"
						+ "  from run_j_opticket t\n"
						+ " where to_char(t.end_time, 'yyyyMM') = " + yearMonth
						+ "\n";
				// "select sum(m.opticket_count)/2 from
				// run_j_opticket_stat_detail m where
				// m.report_id="+report.getId();
				String qucount_sql = "select count(t.opticket_code)\n"
						+ "  from run_j_opticket t\n"
						+ " where to_char(t.end_time, 'yyyyMM') = " + yearMonth
						+ "\n" + " and t.opticket_status='Z'";
				long allNum = Long.parseLong(bll.getSingal(opcount_sql)
						.toString());
				long noquNum = Long.parseLong(bll.getSingal(qucount_sql)
						.toString());
				long quNum = allNum - noquNum;
				report.setAllCount(allNum);
				report.setNotQualifiedCount(noquNum);
				report.setQualifiedCount(quNum);
				report.setMemo("本月电气操作票"+report.getAllCount()+"张,本月电气操作不合格票"+report.getNotQualifiedCount()+"张");
				if (allNum > 0l)
					report.setQualifiedRate((double) quNum / allNum);
				update(report);
			}
			return true;
		}
	}

	public void clearStat(RunJOpticketStatDetail model) {
		RunJOpticketStatDetail detail = re.findById(model.getId());
		detail.setOpticketCount(0l);
		detail.setOpticketItemCount(0l);
		detail.setNoProblemOpticketCount(0l);
		detail.setNoProblemOpticketItemCount(0l);
		detail.setIsclear("Y");
		re.update(detail);
		RunJOpticketStat entity = this.findById(detail.getReportId());
		long allcount = entity.getAllCount();
		long notQualifiedCount = entity.getNotQualifiedCount();
		String sql = "select  count(*) from run_j_opticket t where to_char(t.end_time,'yyyyMM')="
				+ entity.getReportDate()
				+ " and (t.operatormans='"
				+ detail.getWorkcode()
				+ "' or t.protectormans='"
				+ detail.getWorkcode()
				+ "') and t.opticket_status='Z' and  t.is_standar='N'";
		long num = Long.parseLong(bll.getSingal(sql).toString());
		entity.setAllCount(allcount - model.getOpticketCount());
		entity.setNotQualifiedCount(notQualifiedCount - num);
		entity.setQualifiedCount(entity.getAllCount()
				- entity.getNotQualifiedCount());
		entity.setQualifiedRate((double) entity.getQualifiedCount()
				/ entity.getAllCount());
		if(entity.getMemo().indexOf("以下值班人员操作记录清零，并纳入部门考核：")==-1){
			entity.setMemo(entity.getMemo()+",以下值班人员操作记录清零，并纳入部门考核："+model.getWorkcode()+",");
		}else{
			entity.setMemo(entity.getMemo()+model.getWorkcode()+",");
		}
		
		this.update(entity);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getStatDataPrint(String statBy, String title,
			Long yearMonth, String specialCode, String enterprisecode) {
		RunJOpticketStat model = null;
		String sql1 = "select t.* from RUN_J_OPTICKET_STAT t where t.report_date="
				+ yearMonth
				+ " and t.profession='"
				+ specialCode
				+ "' and t.enterprisecode='" + enterprisecode + "'";
		List<RunJOpticketStat> slist = bll.queryByNativeSQL(sql1,
				RunJOpticketStat.class);
		if (slist.size() > 0) {
			model = slist.get(0);
		}
		if (model == null) {
			return null;
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("model", model);
			String sql2 = "select t.id,\n"
					+ "       getworkername(m.emp_code),\n"
					+ "       nvl(t.opticket_count,0),\n"
					+ "       nvl(t.opticket_item_count,0),\n"
					+ "       nvl(t.no_problem_opticket_count,0),\n"
					+ "       nvl(t.no_problem_opticket_item_count,0),\n"
					+ "       m.dept_id,\n"
					+ "       nvl(t.isclear,'N')\n"
					+ "  from run_j_opticket_stat_detail t,(select b.dept_id, a.emp_code, b.order_by dep_order, a.order_by emp_order\n"
					+ "  from hr_j_emp_info a, hr_c_dept b\n"
					+ " where a.dept_id = b.dept_id\n"
					+ "   and b.is_use = 'Y'\n" //update by sychen 20100902
//					+ "   and b.is_use = 'U'\n" 
					+ "   and a.dept_id in (select t.dept_id\n"
					+ "                       from hr_c_dept t\n"
					+ "                      where t.is_use = 'Y'\n" //update by sychen 20100902
//					+ "                      where t.is_use = 'U'\n"
					+ "                      start with t.dept_id = ?\n"
					+ "                     connect by prior t.dept_id = t.pdept_id)\n"
					+ ")m\n" + " where t.workcode(+) = m.emp_code\n"
					+ " and t.report_id(+) = ?\n"
					+ " order by m.dept_id, m.emp_order";
			List list = bll.queryByNativeSQL(sql2, new Object[] { specialCode,
					model.getId() });
			List<RunJOpticketStatDetail> arr = new ArrayList<RunJOpticketStatDetail>();
			List<RunJOpticketStatDetail> arr1 = new ArrayList<RunJOpticketStatDetail>();
			List<RunJOpticketStatDetail> arr2 = new ArrayList<RunJOpticketStatDetail>();
			List<RunJOpticketStatDetail> arr3 = new ArrayList<RunJOpticketStatDetail>();
			List<RunJOpticketStatDetail> arr4 = new ArrayList<RunJOpticketStatDetail>();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					Object[] ob = (Object[]) list.get(i);
					RunJOpticketStatDetail entity = new RunJOpticketStatDetail();
					if (ob[0] != null) {
						entity.setId(Long.parseLong(ob[0].toString()));
					}
					if (ob[1] != null) {
						entity.setWorkcode(ob[1].toString());
					}
					if (ob[2] != null) {
						entity.setOpticketCount(Long
								.parseLong(ob[2].toString()));
					}
					if (ob[3] != null) {
						entity.setOpticketItemCount(Long.parseLong(ob[3]
								.toString()));
					}
					if (ob[4] != null) {
						entity.setNoProblemOpticketCount(Long.parseLong(ob[4]
								.toString()));
					}
					if (ob[5] != null) {
						entity.setNoProblemOpticketItemCount(Long
								.parseLong(ob[5].toString()));
					}
					if (ob[6] != null) {
						entity.setShiftId(Long.parseLong(ob[6].toString()));
					}
					if (ob[7] != null) {
						entity.setIsclear(ob[7].toString());
					}
					if (entity.getShiftId() == 29l) {
						arr.add(entity);
					} else if (entity.getShiftId() == 30l) {
						arr1.add(entity);
					} else if (entity.getShiftId() == 31l) {
						arr2.add(entity);
					} else if (entity.getShiftId() == 32L) {
						arr3.add(entity);
					} else {
						arr4.add(entity);
					}

				}
			}
			map.put("29", arr);
			map.put("30", arr1);
			map.put("31", arr2);
			map.put("32", arr3);
			map.put("33", arr4);
			return map;
		}
	}
	private long getLastMonth(long month){
		long lastMonth=0;
		String monthStr=String.valueOf(month);
		if(monthStr.substring(4,6).equals("00")){
			long year=Long.parseLong(monthStr.substring(0,4))-1;
			lastMonth=Long.parseLong(String.valueOf(year)+"12");
		}else{
			lastMonth=month-1;
		}
		return lastMonth;
	}

}