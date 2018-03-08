package power.ejb.equ.failure;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.opensymphony.util.Data;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrCDept;
import power.ejb.hr.HrCDeptFacadeRemote;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;

@Stateless
public class EquJFailuresQuery implements EquJFailuresQueryRemote {
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	protected EquJFailuresFacadeRemote fremote;
	protected EquJFailureHistoryFacadeRemote hisremote;
	protected HrCDeptFacadeRemote hremote;
	protected RunCSpecialsFacadeRemote spremote;

	public EquJFailuresQuery() {
		fremote = (EquJFailuresFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("EquJFailuresFacade");
		hisremote = (EquJFailureHistoryFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("EquJFailureHistoryFacade");
		hremote = (HrCDeptFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("HrCDeptFacade");
		spremote = (RunCSpecialsFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("RunCSpecialsFacade");
	}

	// 缺陷综合统计
	public List<EquFailuresQueryForm> failureIntegerQuery(String startDate,
			String endDate, String type, String enterpriseCode) {
		String sql = "";
		if ("bug".equals(type)) {
			sql = "select r.bug_code,\n" + "       (select t.bug_name\n"
					+ "          from equ_c_bug t\n"
					+ "         where t.bug_code = r.bug_code\n"
					+ "           and t.is_use = 'Y'\n"
					+ "           and t.enterprise_code = '" + enterpriseCode
					+ "' and rownum=1) bug_name,\n" + "       count(1)\n"
					+ "  from equ_j_failures r\n" + " where r.find_date >=\n"
					+ "       to_date('" + startDate
					+ " 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.find_date <=\n" + "       to_date('" + endDate
					+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.entreprise_code = '" + enterpriseCode
					+ "' and r.wo_status != 0\n" + "   and r.isuse = 'Y'\n"
					+ " group by r.bug_code\n" + " order by r.bug_code";

		} else if ("runprofession".equals(type)) {
			sql = "select r.run_profession,\n"
					+ "       getspecialname(r.run_profession) run_profession_name,\n"
					+ "       count(1)\n" + "  from equ_j_failures r\n"
					+ " where r.find_date >=\n" + "       to_date('"
					+ startDate + " 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.find_date <=\n" + "       to_date('" + endDate
					+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.entreprise_code = '" + enterpriseCode
					+ "' and r.wo_status != 0\n" + "   and r.isuse = 'Y'\n"
					+ " group by r.run_profession\n"
					+ " order by r.run_profession";

		} else if ("repairdep".equals(type)) {
			sql = "select r.repair_dep, getdeptname(r.repair_dep) repair_dep_name, count(1)\n"
					+ "  from equ_j_failures r\n"
					+ " where r.find_date >=\n"
					+ "       to_date('"
					+ startDate
					+ " 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.find_date <=\n"
					+ "       to_date('"
					+ endDate
					+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.entreprise_code = '"
					+ enterpriseCode
					+ "' and r.wo_status != 0\n"
					+ "   and r.isuse = 'Y'\n"
					+ " group by r.repair_dep\n" + " order by r.repair_dep";

		} else if ("status".equals(type)) {
			sql = "select r.wo_status,\n" + "       decode(r.wo_status,\n"
					+ "              '0',\n" + "              '未上报',\n"
					+ "              '1',\n" + "              '待消缺',\n"
					+ "              '2',\n" + "              '待确认',\n"
					+ "              '3',\n" + "              '点检待验收',\n"
					+ "              '4',\n" + "              '运行已验收',\n"
					+ "              '5',\n" + "              '已处理',\n"
					+ "              '6',\n" + "              '作废',\n"
					+ "              '7',\n" + "              '设备部仲裁',\n"
					+ "              '8',\n" + "              '已仲裁待消缺',\n"
					+ "              '9',\n" + "              '验收退回',\n"
					+ "              '10',\n" + "              '退回',\n"
					+ "              '11',\n" + "              '点检延期待处理审批',\n"
					+ "              '12',\n"
					+ "              '设备部主任延期待处理审批',\n"
					+ "              '13',\n" + "              '运行延期待处理审批',\n"
					+ "              '14',\n" + "              '运行待验收',\n"
					+ "              '15',\n" + "              '检修部门主管延期退回',\n"
					+ "              '16',\n" + "              '生技部延期退回',\n"
					+ "              '18',\n" + "              '已确认待消缺',\n"
					+ "              '20',\n" + "              '总工审批',\n"
					+ "              '21',\n"
					+ "              '总工延期待处理审批退回',\n"
					+ "              '17',\n"
					+ "              '运行延期待处理退回') wo_status_name,\n"
					+ "       count(1) count\n" + "  from equ_j_failures r\n"
					+ " where r.find_date >=\n" + "       to_date('"
					+ startDate + " 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.find_date <=\n" + "       to_date('" + endDate
					+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.entreprise_code = '" + enterpriseCode
					+ "' and r.wo_status != 0\n" + "   and r.isuse = 'Y'\n"
					+ " group by r.wo_status";

		} else if ("failuretype".equals(type)) {
			sql = "select r.failuretype_code,\n"
					+ "       (select t.failuretype_name\n"
					+ "          from equ_c_failure_type t\n"
					+ "         where t.failuretype_code = r.failuretype_code\n"
					+ "           and t.is_use = 'Y'\n"
					+ "           and t.enterprise_code = '"
					+ enterpriseCode
					+ "') failuretype_name,\n"
					+ "       count(1)\n"
					+ "  from equ_j_failures r\n"
					+ " where r.find_date >=\n"
					+ "       to_date('"
					+ startDate
					+ " 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.find_date <=\n"
					+ "       to_date('"
					+ endDate
					+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.entreprise_code='"
					+ enterpriseCode
					+ "' and r.wo_status != 0\n"
					+ "   and r.isuse='Y'\n"
					+ " group by r.failuretype_code order by r.failuretype_code";
		} else if ("finddep".equals(type)) {
			sql = "select r.find_dept, getdeptname(r.find_dept) find_dept_name, count(1) count\n"
					+ "  from equ_j_failures r\n"
					+ " where r.find_date >=\n"
					+ "       to_date('"
					+ startDate
					+ " 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.find_date <=\n"
					+ "       to_date('"
					+ endDate
					+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.entreprise_code = '"
					+ enterpriseCode
					+ "' and r.wo_status != 0\n"
					+ "   and r.isuse = 'Y'\n"
					+ " group by r.find_dept";

		} else if ("domprofession".equals(type)) {
			sql = "select r.domination_profession,\n"
					+ "       getspecialname(r.domination_profession) domination_profession_name,\n"
					+ "       count(1)\n" + "  from equ_j_failures r\n"
					+ " where r.find_date >=\n" + "       to_date('"
					+ startDate + " 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.find_date <=\n" + "       to_date('" + endDate
					+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.entreprise_code = '" + enterpriseCode
					+ "' and r.wo_status != 0\n" + "   and r.isuse = 'Y'\n"
					+ " group by r.domination_profession\n"
					+ " order by r.domination_profession";
		}
		List list = bll.queryByNativeSQL(sql);
		List<EquFailuresQueryForm> querylist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			EquFailuresQueryForm model = new EquFailuresQueryForm();
			Object[] o = (Object[]) list.get(i);
			if (o[0] != null) {
				model.setQueryType(o[0].toString());
			} else {
				model.setQueryType("");
			}
			if (o[1] != null && o[1] != "") {
				model.setQueryTypeName(o[1].toString());
			} else {
				model.setQueryTypeName("未知");
			}
			if (o[2] != null) {
				model.setCount(o[2].toString());
			}

			String addCondi = "";
			if ("bug".equals(type)) {
				addCondi = " and r.bug_code ='" + model.getQueryType() + "' \n";
			} else if ("runprofession".equals(type)) {
				addCondi = " and r.run_profession ='" + model.getQueryType()
						+ "' \n";
			} else if ("repairdep".equals(type)) {
				addCondi = " and r.repair_dep ='" + model.getQueryType()
						+ "' \n";
			} else if ("status".equals(type)) {
				addCondi = " and r.wo_status ='" + model.getQueryType()
						+ "' \n";
			} else if ("failuretype".equals(type)) {
				addCondi = " and r.failuretype_code ='" + model.getQueryType()
						+ "' \n";
			} else if ("finddep".equals(type)) {
				addCondi = " and r.find_dept ='" + model.getQueryType()
						+ "' \n";
			} else if ("domprofession".equals(type)) {
				addCondi = " and r.domination_profession ='"
						+ model.getQueryType() + "' \n";
			}
			// add by liuyi 20100428
			String sqlDetail = "select a.eliminate_count,\n"
					+ "       b.noeliminate_count,\n"
					+ "       c.repeat_count,\n" + "       d.back_count\n"
					+ "  from (select count(*) eliminate_count\n"
					+ "          from equ_j_failures r\n"
					+ "         where r.wo_status in (3, 4, 14)\n"
					+ "           and r.find_date >=\n" + "       to_date('"
					+ startDate
					+ " 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.find_date <=\n"
					+ "       to_date('"
					+ endDate
					+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.entreprise_code = '"
					+ enterpriseCode
					+ "' and r.wo_status != 0\n"
					+ "   and r.isuse = 'Y'\n"
					+ addCondi
					+ "           ) a,\n"
					+ "       (select count(*) noeliminate_count\n"
					+ "          from equ_j_failures r\n"
					+ "         where r.wo_status in (1, 2, 5, 7, 8, 9, 11, 12, 13, 15, 16, 17,18,20,21)\n"
					+ "           and r.find_date >=\n"
					+ "       to_date('"
					+ startDate
					+ " 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.find_date <=\n"
					+ "       to_date('"
					+ endDate
					+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.entreprise_code = '"
					+ enterpriseCode
					+ "' and r.wo_status != 0\n"
					+ "   and r.isuse = 'Y'\n"
					+ addCondi
					+ "           ) b,\n"
					+ "       (select count(*) repeat_count\n"
					+ "          from equ_j_failures r\n"
					+ " where r.find_date >=\n"
					+ "       to_date('"
					+ startDate
					+ " 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.find_date <=\n"
					+ "       to_date('"
					+ endDate
					+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.entreprise_code = '"
					+ enterpriseCode
					+ "' and r.wo_status != 0\n"
					+ "   and r.isuse = 'Y'\n"
					+ addCondi
					+ "           and r.unqualified_failure_code is not null) c,\n"
					+ "       (select count(*) back_count\n"
					+ "          from equ_j_failures r\n"
					+ "         where r.wo_status in (10)\n"
					+ "           and r.find_date >=\n"
					+ "       to_date('"
					+ startDate
					+ " 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.find_date <=\n"
					+ "       to_date('"
					+ endDate
					+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and r.entreprise_code = '"
					+ enterpriseCode
					+ "' and r.wo_status != 0\n"
					+ "   and r.isuse = 'Y'\n"
					+ addCondi + "           ) d";
			List detaiList = bll.queryByNativeSQL(sqlDetail);
			if (detaiList != null && detaiList.size() > 0) {
				Object[] detail = (Object[]) detaiList.get(0);
				// 已消除缺陷数 eliminateCount
				if (detail[0] != null) {
					model.setEliminateCount(detail[0].toString());
				} else {
					model.setEliminateCount("0");
				}
				// 未消除缺陷数 noeliminateCount
				if (detail[1] != null) {
					model.setNoeliminateCount(detail[1].toString());
				} else {
					model.setNoeliminateCount("0");
				}
				// 缺陷重复发生数 repeatCount
				if (detail[2] != null) {
					model.setRepeatCount(detail[2].toString());
				} else {
					model.setRepeatCount("0");
				}
				// 退回缺陷数 backCount
				if (detail[3] != null) {
					model.setBackCount(detail[3].toString());
				} else {
					model.setBackCount("0");
				}
			}

			querylist.add(model);
		}
		return querylist;
	}

	// 缺陷个人发现查询
	public PageObject failureFindQuery(String from, String end,
			String enterpriseCode, String workercode, int start, int limit) {
		String sqlc = "select count(*)\n" + "  from equ_j_failures r\n"
				+ " where r.isuse = 'Y'\n" + "   and r.entreprise_code = '"
				+ enterpriseCode + "'\n" + "   and r.find_by = '" + workercode
				+ "'\n" + "   and r.find_date between\n" + "       to_date('"
				+ from + " 00:00:00', 'yyyy-MM-dd hh24:mi:ss') and\n"
				+ "       to_date('" + end
				+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss')";

		String sql = "select r.id,\n" + "       r.failure_code,\n"
				+ "       r.find_by,\n"
				+ "       getworkername(r.find_by) find_name,\n"
				+ "       r.failure_content,\n" + "       r.attribute_code,\n"
				+ "       r.equ_name,\n" + "       r.bug_code,\n"
				+ "       (select t.bug_name\n"
				+ "          from equ_c_bug t\n"
				+ "         where t.bug_code = r.bug_code\n"
				+ "           and t.is_use = 'Y'\n"
				+ "           and t.enterprise_code = '"
				+ enterpriseCode
				+ "' and rownum=1),\n"
				+ "       r.repair_dep,\n"
				+ "       getdeptname(r.repair_dep) repair_dep_name,\n"
				+ "       decode(r.wo_status,\n"
				+ "              '0',\n"
				+ "              '未上报',\n"
				+ "              '1',\n"
				+ "              '待消缺',\n"
				+ "              '2',\n"
				+ "              '待确认',\n"
				+ "              '3',\n"
				+ "              '点检待验收',\n"
				+ "              '4',\n"
				+ "              '运行已验收',\n"
				+ "              '5',\n"
				+ "              '已处理',\n"
				+ "              '6',\n"
				+ "              '作废',\n"
				+ "              '7',\n"
				+ "              '设备部仲裁',\n"
				+ "              '8',\n"
				+ "              '已仲裁待消缺',\n"
				+ "              '9',\n"
				+ "              '验收退回',\n"
				+ "              '10',\n"
				+ "              '退回',\n"
				+ "              '11',\n"
				+ "              '点检延期待处理审批',\n"
				+ "              '12',\n"
				+ "              '设备部主任延期待处理审批',\n"
				+ "              '13',\n"
				+ "              '运行延期待处理审批',\n"
				+ "              '14',\n"
				+ "              '运行待验收',\n"
				+ "              '15',\n"
				+ "              '检修部门主管延期退回',\n"
				+ "              '16',\n"
				+ "              '生技部延期退回',\n"
				+ "              '18',\n"
				+ "              '已确认待消缺',\n"
				+ "              '20',\n"
				+ "              '总工审批',\n"
				+ "              '21',\n"
				+ "              '总工延期待处理审批退回',\n"
				+ "              '17',\n"
				+ "              '运行延期待处理退回') wo_status_name,\n"
				+ "       r.find_dept,\n"
				+ "       getdeptname(r.find_dept) find_dept_name,\n"
				+ "      to_char(r.find_date, 'yyyy-mm-dd hh24:mi:ss') find_date,\n"
				+ "       r.work_flow_no\n"
				+ "  from equ_j_failures r\n"
				+ " where r.isuse = 'Y'\n"
				+ "   and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "   and r.find_by = '"
				+ workercode
				+ "'\n"
				+ "   and r.find_date between\n"
				+ "       to_date('"
				+ from
				+ " 00:00:00', 'yyyy-MM-dd hh24:mi:ss') and\n"
				+ "       to_date('"
				+ end
				+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss') order by r.failure_code";
		Object obj = bll.getSingal(sqlc);
		Long count = 0L;
		if (obj != null) {
			count = Long.parseLong(obj.toString());
		}
		List list = bll.queryByNativeSQL(sql, start, limit);
		List<EquFailuresInfo> querylist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			EquFailuresInfo model = new EquFailuresInfo();
			Object[] o = (Object[]) list.get(i);
			if (o[0] != null) {
				model.setId(Long.parseLong(o[0].toString()));
			}
			if (o[1] != null && o[1] != "") {
				model.setFailureCode(o[1].toString());
			} else {
				model.setFailureCode("");
			}
			if (o[2] != null) {
				model.setFindBy(o[2].toString());
			} else {
				model.setFindBy("");
			}
			if (o[3] != null) {
				model.setFindByName(o[3].toString());
			} else {
				model.setFindByName("");
			}
			if (o[4] != null) {
				model.setFailureContent(o[4].toString());
			} else {
				model.setFailureContent("");
			}
			if (o[5] != null) {
				model.setAttributeCode(o[5].toString());
			} else {
				model.setAttributeCode("");
			}
			if (o[6] != null) {
				model.setEquName(o[6].toString());
			} else {
				model.setEquName("");
			}
			if (o[7] != null) {
				model.setBugCode(o[7].toString());
			} else {
				model.setBugCode("");
			}
			if (o[8] != null) {
				model.setBugName(o[8].toString());
			} else {
				model.setBugName("");
			}
			if (o[9] != null) {
				model.setRepairDep(o[9].toString());
			} else {
				model.setRepairDep("");
			}
			if (o[10] != null) {
				model.setRepairDepName(o[10].toString());
			} else {
				model.setRepairDepName("");
			}
			if (o[11] != null) {
				model.setWoStatusName(o[11].toString());
			} else {
				model.setWoStatusName("");
			}
			if (o[12] != null) {
				model.setFindDept(o[12].toString());
			} else {
				model.setFindDept("");
			}
			if (o[13] != null) {
				model.setFindDeptName(o[13].toString());
			} else {
				model.setFindDeptName("");
			}
			if (o[14] != null) {
				model.setFindDate(o[14].toString());
			} else {
				model.setFindDate("");
			}
			if (o[15] != null) {
				model.setWorkFlowNo(o[15].toString());
			} else {
				model.setWorkFlowNo("");
			}
			querylist.add(model);
		}
		PageObject o = new PageObject();
		o.setList(querylist);
		o.setTotalCount(count);
		return o;
	}

	// 缺陷个人消缺
	public PageObject failueEliminateQuery(String from, String end,
			String enterpriseCode, String workercode, int start, int limit) {
		String csql = "select count(*)\n"
				+ "   from equ_j_failure_history x, equ_j_failures r\n"
				+ " where x.approve_type = '1'\n"
				+ "   and r.failure_code = x.failure_code\n"
				+ "   and x.approve_people = '"
				+ workercode
				+ "'\n"
				+ "   and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "   and x.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "   and r.isuse = 'Y'\n"
				+ "   and x.isuse = 'Y'\n"
				+ "   and r.find_date between\n"
				+ "       to_date('"
				+ from
				+ " 00:00:00', 'yyyy-MM-dd hh24:mi:ss') and\n"
				+ "       to_date('"
				+ end
				+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss') order by x.failure_code desc";

		String sql = "select x.failure_code,\n"
				+ "       x.approve_people,\n"
				+ "       getworkername(x.approve_people) approve_people_name,\n"
				+ "       to_char(x.approve_time, 'yyyy-mm-dd hh24:mi:ss') approve_time,\n"
				+ "       x.eliminate_type,\n"
				+ "       decode(x.eliminate_type,\n" + "              '1',\n"
				+ "              '正常消缺',\n" + "              '2',\n"
				+ "              '低谷消缺',\n" + "              '3',\n"
				+ "              '降负荷消缺',\n" + "              '4',\n"
				+ "              '停机') eliminate_type_name,\n"
				+ "       x.eliminate_class,\n"
				+ "       getdeptname(x.eliminate_class) e_class_name,\n"
				+ "       r.attribute_code,\n" + "       r.equ_name,\n"
				+ "       r.bug_code,\n" + "       (select bug_name t\n"
				+ "          from equ_c_bug t\n"
				+ "         where t.bug_code = r.bug_code\n"
				+ "           and t.is_use = 'Y'\n"
				+ "           and enterprise_code = '"
				+ enterpriseCode
				+ "' and rownum=1) bug_name,\n"
				+ "       r.failure_content,\n"
				+ "       to_char(r.find_date, 'yyyy-mm-dd hh24:mi:ss') find_date,\n"
				+ "       r.repair_dep,\n"
				+ "       getdeptname(r.repair_dep) repair_dep_name,\n"
				+ "       decode(r.wo_status,\n"
				+ "              '0',\n"
				+ "              '未上报',\n"
				+ "              '1',\n"
				+ "              '待消缺',\n"
				+ "              '2',\n"
				+ "              '待确认',\n"
				+ "              '3',\n"
				+ "              '点检待验收',\n"
				+ "              '4',\n"
				+ "              '运行已验收',\n"
				+ "              '5',\n"
				+ "              '已处理',\n"
				+ "              '6',\n"
				+ "              '作废',\n"
				+ "              '7',\n"
				+ "              '设备部仲裁',\n"
				+ "              '8',\n"
				+ "              '已仲裁待消缺',\n"
				+ "              '9',\n"
				+ "              '验收退回',\n"
				+ "              '10',\n"
				+ "              '退回',\n"
				+ "              '11',\n"
				+ "              '点检延期待处理审批',\n"
				+ "              '12',\n"
				+ "              '设备部主任延期待处理审批',\n"
				+ "              '13',\n"
				+ "              '运行延期待处理审批',\n"
				+ "              '14',\n"
				+ "              '运行待验收',\n"
				+ "              '15',\n"
				+ "              '检修部门主管延期退回',\n"
				+ "              '16',\n"
				+ "              '生技部延期退回',\n"
				+ "              '18',\n"
				+ "              '已确认待消缺',\n"
				+ "              '20',\n"
				+ "              '总工审批',\n"
				+ "              '21',\n"
				+ "              '总工延期待处理审批退回',\n"
				+ "              '17',\n"
				+ "              '运行延期待处理退回') wo_status_name,\n"
				+ "       r.id,\n"
				+ "       r.work_flow_no\n"
				+ "  from equ_j_failure_history x, equ_j_failures r\n"
				+ " where x.approve_type = '1'\n"
				+ "   and r.failure_code = x.failure_code\n"
				+ "   and x.approve_people = '"
				+ workercode
				+ "'\n"
				+ "   and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "   and x.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "   and r.isuse = 'Y'\n"
				+ "   and x.isuse = 'Y'\n"
				+ "   and r.find_date between\n"
				+ "       to_date('"
				+ from
				+ " 00:00:00', 'yyyy-MM-dd hh24:mi:ss') and\n"
				+ "       to_date('"
				+ end
				+ " 23:59:59', 'yyyy-MM-dd hh24:mi:ss') order by x.failure_code desc";
		Object obj = bll.getSingal(csql);
		Long count = 0L;
		if (obj != null) {
			count = Long.parseLong(obj.toString());
		}
		List list = bll.queryByNativeSQL(sql, start, limit);
		List<EquFailuresQueryForm> querylist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			EquFailuresQueryForm model = new EquFailuresQueryForm();
			Object[] o = (Object[]) list.get(i);
			if (o[0] != null) {
				model.setFailureCode(o[0].toString());
			} else {
				model.setFailureCode("");
			}
			if (o[2] != null && o[2] != "") {
				model.setApprovePeopleName(o[2].toString());
			} else {
				model.setApprovePeopleName("");
			}
			if (o[3] != null) {
				model.setApproveTime(o[3].toString());
			} else {
				model.setApproveTime("");
			}
			if (o[5] != null) {
				model.setElininateTypeName(o[5].toString());
			} else {
				model.setElininateTypeName("");
			}
			if (o[7] != null) {
				model.setEliminateClassName(o[7].toString());
			} else {
				model.setEliminateClassName("");
			}
			if (o[9] != null) {
				model.setEquName(o[9].toString());
			} else {
				model.setEquName("");
			}
			if (o[11] != null) {
				model.setBugName(o[11].toString());
			} else {
				model.setBugName("");
			}
			if (o[12] != null) {
				model.setFailureContent(o[12].toString());
			} else {
				model.setFailureContent("");
			}
			if (o[13] != null) {
				model.setFindDate(o[13].toString());
			} else {
				model.setFindDate("");
			}
			if (o[15] != null) {
				model.setRepairDepName(o[15].toString());
			} else {
				model.setRepairDepName("");
			}
			if (o[16] != null) {
				model.setWoStatusName(o[16].toString());
			} else {
				model.setWoStatusName("");
			}
			if (o[17] != null) {
				model.setId(Long.parseLong(o[17].toString()));
			}
			if (o[18] != null) {
				model.setWorkFlowNo(o[18].toString());
			} else {
				model.setWorkFlowNo("");
			}
			querylist.add(model);
		}
		PageObject o = new PageObject();
		o.setList(querylist);
		o.setTotalCount(count);
		return o;
	}

	public PageObject noKKSfailureQuery(String enterprisecode, int start,
			int limit) {
		String countsql = "select count(*)\n"
				+ "  from (select count(*),\n"
				+ "               r.write_by,\n"
				+ "               getworkername(write_by) write_by_name,\n"
				+ "               getdeptname(getdeptbyworkcode(r.write_by)) dep_name\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.attribute_code like 'TMP%'\n"
				+ "           and r.entreprise_code = '" + enterprisecode
				+ "'\n" + "           and r.isuse = 'Y'\n"
				+ "         group by r.write_by)";
		String sql = "select count(*),\n"
				+ "       r.write_by,\n"
				+ "       getworkername(write_by) write_by_name,\n"
				+ "       getdeptname(getdeptbyworkcode(r.write_by)) dep_name\n"
				+ "  from equ_j_failures r\n"
				+ " where r.attribute_code like 'TMP%'\n"
				+ "   and r.entreprise_code = '" + enterprisecode + "'\n"
				+ "   and r.isuse = 'Y'\n"
				+ " group by r.write_by order by r.write_by";
		Object obj = bll.getSingal(countsql);
		Long count = 0L;
		if (obj != null) {
			count = Long.parseLong(obj.toString());
		}
		List list = bll.queryByNativeSQL(sql, start, limit);
		List<EquFailuresQueryForm> querylist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			EquFailuresQueryForm model = new EquFailuresQueryForm();
			Object[] o = (Object[]) list.get(i);
			if (o[0] != null) {
				model.setCount(o[0].toString());
			} else {
				model.setCount("");
			}
			if (o[1] != null && o[1] != "") {
				model.setWriteBy(o[1].toString());
			} else {
				model.setWriteBy("");
			}
			if (o[2] != null && o[2] != "") {
				model.setWriteByName(o[2].toString());
			} else {
				model.setWriteByName("");
			}
			if (o[3] != null && o[3] != "") {
				model.setWriteDeptName(o[3].toString());
			} else {
				model.setWriteDeptName("");
			}
			querylist.add(model);
		}
		PageObject o = new PageObject();
		o.setList(querylist);
		o.setTotalCount(count);
		return o;
	}

	public PageObject findNoKKSfailureByWorker(String enterpriseCode,
			String workercode, int start, int limit) {
		String countsql = "select count(1)\n" + "  from equ_j_failures t\n"
				+ " where t.isuse = 'Y'\n" + "   and t.entreprise_code = '"
				+ enterpriseCode + "'\n" + "   and t.write_by = '" + workercode
				+ "'\n" + "   and t.attribute_code like 'TMP%'";

		String strsql = "select t.id,\n"
				+ "       t.failure_code,\n"
				+ "       decode(t.wo_status,\n"
				+ "              '0',\n"
				+ "              '未上报',\n"
				+ "              '1',\n"
				+ "              '待消缺',\n"
				+ "              '2',\n"
				+ "              '待确认',\n"
				+ "              '3',\n"
				+ "              '点检待验收',\n"
				+ "              '4',\n"
				+ "              '运行已验收',\n"
				+ "              '5',\n"
				+ "              '已处理',\n"
				+ "              '6',\n"
				+ "              '作废',\n"
				+ "              '7',\n"
				+ "              '设备部仲裁',\n"
				+ "              '8',\n"
				+ "              '已仲裁待消缺',\n"
				+ "              '9',\n"
				+ "              '验收退回',\n"
				+ "              '10',\n"
				+ "              '退回',\n"
				+ "              '11',\n"
				+ "              '点检延期待处理审批',\n"
				+ "              '12',\n"
				+ "              '设备部主任延期待处理审批',\n"
				+ "              '13',\n"
				+ "              '运行延期待处理审批',\n"
				+ "              '14',\n"
				+ "              '运行待验收',\n"
				+ "              '15',\n"
				+ "              '检修部门主管延期退回',\n"
				+ "              '16',\n"
				+ "              '生技部延期退回',\n"
				+ "              '18',\n"
				+ "              '已确认待消缺',\n"
				+ "              '20',\n"
				+ "              '总工审批',\n"
				+ "              '21',\n"
				+ "              '总工延期待处理审批退回',\n"
				+ "              '17',\n"
				+ "              '运行延期待处理退回') wo_status_text,\n"
				+ "       t1.failuretype_name,\n"
				+ "       t1.failure_pri,\n"
				+ "       t.attribute_code,\n"
				+ "       t.equ_name,\n"
				+ "       t.failure_content,\n"
				+ "       t.location_code,\n"
				+ "       t.location_desc,\n"
				+ "       to_char(t.find_date, 'yyyy-mm-dd hh24:mi:ss') find_date,\n"
				+ "       t.find_by,\n"
				+ "       t.find_dept,\n"
				+ "       t.wo_code,\n"
				+ "       t.bug_code,\n"
				+ "       t.failuretype_code,\n"
				+ "       t.failure_level,\n"
				+ "       t.wo_status,\n"
				+ "       t.pre_content,\n"
				+ "       t.if_stop_run,\n"
				+ "       decode(t.if_stop_run, 'Y', '是', 'N', '否') if_stop_run_text,\n"
				+ "       t.run_profession,\n"
				+ "       t.domination_profession,\n"
				+ "       t.repair_dep,\n"
				+ "       t.installation_code,\n"
				+ "       t.installation_desc,\n"
				+ "       t.belong_system,\n"
				+ "       t.likely_reason,\n"
				+ "       t.wo_priority,\n"
				+ "       t.write_by,\n"
				+ "       t.write_dept,\n"
				+ "       to_char(t.write_date, 'yyyy-mm-dd  hh24:mi:ss') write_date,\n"
				+ "       t.repair_dept,\n"
				+ "       t.realrepair_dept,\n"
				+ "       t.if_open_workorder,\n"
				+ "       decode(t.if_open_workorder, 'Y', '是', 'N', '否') if_open_workorder_text,\n"
				+ "       t.if_repeat,\n"
				+ "       decode(t.if_repeat, 'Y', '是', 'N', '否') if_repeat_text,\n"
				+ "       t.supervisor,\n"
				+ "       t.work_flow_no,\n"
				+ "       t.wf_state,\n"
				+ "       t.entreprise_code,\n"
				+ "       t.isuse,\n"
				+ "       (select t2.bug_name\n"
				+ "          from equ_c_bug t2\n"
				+ "         where t2.is_use = 'Y'\n"
				+ "           and t2.bug_code = t.bug_code and rownum=1) bug_name,\n"
				+ "       getworkername(t.find_by) find_by_name,\n"
				+ "       getdeptname(t.find_dept) find_dept_name,\n"
				+ "       getworkername(t.write_by) write_by_name,\n"
				+ "       getdeptname(t.write_dept) write_dept_name,\n"
				+ "       (select t5.block_name\n"
				+ "          from equ_c_block t5\n"
				+ "         where t5.is_use = 'Y'\n"
				+ "           and t5.block_code = t.belong_system and rownum=1) belong_system_text,\n"
				+ "       getdeptname(t.repair_dep) repair_dep_text,\n"
				+ "       getspecialname(domination_profession) domination_profession_text\n"
				+ "  from equ_j_failures t, equ_c_failure_type t1\n"
				+ " where t.isuse = 'Y'\n" + "   and t1.is_use(+) = 'Y'\n"
				+ "   and t.failuretype_code = t1.failuretype_code(+)\n"
				+ "   and t.entreprise_code = '" + enterpriseCode + "'\n"
				+ "   and t.write_by = '" + workercode + "'\n"
				+ "   and t.attribute_code like 'TMP%'\n"
				+ " order by t.failure_code desc";
		Object objcount = bll.getSingal(countsql);
		Long count = 0L;
		if (objcount != null) {
			count = Long.parseLong(objcount.toString());
		}
		List list = bll.queryByNativeSQL(strsql, start, limit);
		List<EquFailuresInfo> querylist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			EquFailuresInfo form = new EquFailuresInfo();
			Object[] obj = (Object[]) list.get(i);
			if (obj[0] != null) {
				form.setId(Long.parseLong(obj[0].toString()));
			} else {
				form.setId(null);
			}
			if (obj[1] != null) {
				form.setFailureCode(obj[1].toString());
			} else {
				form.setFailureCode("");
			}
			if (obj[2] != null) {
				form.setWoStatusName(obj[2].toString());
			} else {
				form.setWoStatusName("");
			}
			if (obj[3] != null) {
				form.setFailuretypeName(obj[3].toString());
			} else {
				form.setFailuretypeName("");
			}
			if (obj[4] != null) {
				form.setFailurePri(obj[4].toString());
			} else {
				form.setFailurePri("");
			}
			if (obj[5] != null) {
				form.setAttributeCode(obj[5].toString());
			} else {
				form.setAttributeCode("");
			}
			if (obj[6] != null) {
				form.setEquName(obj[6].toString());
			} else {
				form.setEquName("");
			}
			if (obj[7] != null) {
				form.setFailureContent(obj[7].toString());
			} else {
				form.setFailureContent("");
			}
			if (obj[8] != null) {
				form.setLocationCode(obj[8].toString());
			} else {
				form.setLocationCode("");
			}
			if (obj[9] != null) {
				form.setLocationDesc(obj[9].toString());
			} else {
				form.setLocationDesc("");
			}
			if (obj[10] != null) {
				form.setFindDate(obj[10].toString());
			} else {
				form.setFindDate("");
			}
			if (obj[11] != null) {
				form.setFindBy(obj[11].toString());
			} else {
				form.setFindBy("");
			}
			if (obj[12] != null) {
				form.setFindDept(obj[12].toString());
			} else {
				form.setFindDept("");
			}
			if (obj[13] != null) {
				form.setWoCode(obj[13].toString());
			} else {
				form.setWoCode("");
			}
			if (obj[14] != null) {
				form.setBugCode(obj[14].toString());
			} else {
				form.setBugCode("");
			}
			if (obj[15] != null) {
				form.setFailuretypeCode(obj[15].toString());
			} else {
				form.setFailuretypeCode("");
			}
			if (obj[16] != null) {
				form.setFailureLevel(obj[16].toString());
			} else {
				form.setFailureLevel("");
			}
			if (obj[17] != null) {
				form.setWoStatus(obj[17].toString());
			} else {
				form.setWoStatus("");
			}
			if (obj[18] != null) {
				form.setPreContent(obj[18].toString());
			} else {
				form.setPreContent("");
			}
			if (obj[19] != null) {
				form.setIfStopRun(obj[19].toString());
			} else {
				form.setIfStopRun("");
			}
			if (obj[20] != null) {
				form.setIfStopRunName(obj[20].toString());
			} else {
				form.setIfStopRunName("");
			}
			if (obj[21] != null) {
				form.setRunProfession(obj[21].toString());
			} else {
				form.setRunProfession("");
			}
			if (obj[22] != null) {
				form.setDominationProfession(obj[22].toString());
			} else {
				form.setDominationProfession("");
			}
			if (obj[23] != null) {
				form.setRepairDep(obj[23].toString());
			} else {
				form.setRepairDep("");
			}
			if (obj[24] != null) {
				form.setInstallationCode(obj[24].toString());
			} else {
				form.setInstallationCode("");
			}
			if (obj[25] != null) {
				form.setInstallationDesc(obj[25].toString());
			} else {
				form.setInstallationDesc("");
			}
			if (obj[26] != null) {
				form.setBelongSystem(obj[26].toString());
			} else {
				form.setBelongSystem("");
			}
			if (obj[27] != null) {
				form.setLikelyReason(obj[27].toString());
			} else {
				form.setLikelyReason("");
			}
			if (obj[28] != null) {
				form.setWoPriority(Long.parseLong(obj[28].toString()));

			} else {
				form.setWoPriority(null);
			}
			if (obj[29] != null) {
				form.setWriteBy(obj[29].toString());
			} else {
				form.setWriteBy("");
			}
			if (obj[30] != null) {
				form.setWriteDept(obj[30].toString());
			} else {
				form.setWriteDept("");
			}
			if (obj[31] != null) {
				form.setWriteDate(obj[31].toString());
			} else {
				form.setWriteDate("");
			}
			if (obj[32] != null) {
				form.setRepairDept(obj[32].toString());
			} else {
				form.setRepairDept("");
			}
			if (obj[33] != null) {
				form.setRealrepairDept(obj[33].toString());
			} else {
				form.setRealrepairDept("");
			}
			if (obj[34] != null) {
				form.setIfOpenWorkorder(obj[34].toString());
			} else {
				form.setIfOpenWorkorder("");
			}
			if (obj[35] != null) {
				form.setIfOpenWorkorderName(obj[35].toString());
			} else {
				form.setIfOpenWorkorderName("");
			}
			if (obj[36] != null) {
				form.setIfRepeat(obj[36].toString());
			} else {
				form.setIfRepeat("");
			}
			if (obj[37] != null) {
				form.setIfRepeatName(obj[37].toString());
			} else {
				form.setIfRepeatName("");
			}
			if (obj[38] != null) {
				form.setSupervisor(obj[38].toString());
			} else {
				form.setSupervisor("");
			}
			if (obj[39] != null) {
				form.setWorkFlowNo(obj[39].toString());
			} else {
				form.setWorkFlowNo("");
			}
			if (obj[40] != null) {
				form.setWfState(obj[40].toString());
			} else {
				form.setWfState("");
			}
			if (obj[41] != null) {
				form.setEntrepriseCode(obj[41].toString());
			} else {
				form.setEntrepriseCode("");
			}
			if (obj[42] != null) {
				form.setIsuse(obj[42].toString());
			} else {
				form.setIsuse("");
			}
			if (obj[43] != null) {
				form.setBugName(obj[43].toString());
			} else {
				form.setBugName("");
			}
			if (obj[44] != null) {
				form.setFindByName(obj[44].toString());
			} else {
				form.setFindByName("");
			}
			if (obj[45] != null) {
				form.setFindDeptName(obj[45].toString());
			} else {
				form.setFindDeptName("");
			}
			if (obj[46] != null) {
				form.setWriteByName(obj[46].toString());
			} else {
				form.setWriteByName("");
			}
			if (obj[47] != null) {
				form.setWriteDeptName(obj[47].toString());
			} else {
				form.setWriteDeptName("");
			}
			if (obj[48] != null) {
				form.setBelongSystemName(obj[48].toString());
			} else {
				form.setBelongSystemName("");
			}
			if (obj[49] != null) {
				form.setRepairDepName(obj[49].toString());
			} else {
				form.setRepairDepName("");
			}
			if (obj[50] != null) {
				form.setDominationProfessionName(obj[50].toString());
			} else {
				form.setDominationProfessionName("");
			}
			querylist.add(form);
		}
		PageObject o = new PageObject();
		o.setList(querylist);
		o.setTotalCount(count);
		return o;
	}

	public PageObject failureQueryBySystem(String startDate, String end,
			String enterprisecode, int start, int limit) {
		String countsql = "select count(*)\n"
				+ " from (select r.belong_system, count(*) find_count\n"
				+ "         from equ_j_failures r\n"
				+ "        where r.isuse = 'Y'\n"
				+ "          and r.entreprise_code = '" + enterprisecode
				+ "' and r.wo_status != 0\n"
				+ "          and r.write_date between\n"
				+ "              to_date('" + startDate + " 00:00:00',\n"
				+ "                      'yyyy-mm-dd hh24:mi:ss') and\n"
				+ "              to_date('" + end + " 23:59:59',\n"
				+ "                      'yyyy-mm-dd hh24:mi:ss')\n"
				+ "        group by r.belong_system\n"
				+ "        order by r.belong_system)";

		String sql = "select r.belong_system,\n"
				+ "       (select t5.block_name\n"
				+ "          from equ_c_block t5\n"
				+ "         where t5.is_use = 'Y'\n"
				+ "           and t5.block_code = r.belong_system and rownum=1) belong_system_name,\n"
				+ "       count(*) find_count,\n" + "       (select count(*)\n"
				+ "          from equ_j_failures t\n"
				+ "         where t.isuse = 'Y'\n"
				+ "           and t.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "           and t.belong_system = r.belong_system\n"
				+ "           and t.wo_status in (3, 4, 14)) eliminate_count,\n"
				+ "       (select count(*)\n"
				+ "          from equ_j_failures t\n"
				+ "         where t.isuse = 'Y'\n"
				+ "           and t.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "           and t.belong_system = r.belong_system\n"
				+ "           and t.wo_status in (11,12,13,20)) await_count,\n"
				+ "       (select count(*)\n"
				+ "          from equ_j_failures t\n"
				+ "         where t.isuse = 'Y'\n"
				+ "           and t.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "           and t.belong_system = r.belong_system\n"
				+ "           and t.wo_status in (6,10)) back_count,\n"
				+ "       (select count(*)\n"
				+ "          from equ_j_failures t\n"
				+ "         where t.isuse = 'Y'\n"
				+ "           and t.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "           and t.belong_system = r.belong_system\n"
				+ "           and t.wo_status in (4)) ys_count\n"
				+ "  from equ_j_failures r\n"
				+ " where r.isuse = 'Y'\n"
				+ "   and r.entreprise_code = '"
				+ enterprisecode
				+ "' and r.wo_status != 0\n"
				+ "   and r.write_date between\n"
				+ "       to_date('"
				+ startDate
				+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') and\n"
				+ "       to_date('"
				+ end
				+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss')\n"
				+ " group by r.belong_system\n" + " order by r.belong_system";
		Object obj = bll.getSingal(countsql);
		Long count = 0L;
		if (obj != null) {
			count = Long.parseLong(obj.toString());
		}
		List list = bll.queryByNativeSQL(sql, start, limit);
		List<EquFailuresQueryForm> querylist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			EquFailuresQueryForm model = new EquFailuresQueryForm();
			Object[] o = (Object[]) list.get(i);
			if (o[0] != null) {
				model.setBelongSystem(o[0].toString());
			} else {
				model.setBelongSystem("");
			}
			if (o[1] != null && o[1] != "") {
				model.setBelongSystemName(o[1].toString());
			} else {
				model.setBelongSystemName("");
			}
			if (o[2] != null && o[2] != "") {
				model.setCount(o[2].toString());
			} else {
				model.setCount("");
			}
			if (o[3] != null && o[3] != "") {
				model.setEliminateCount(o[3].toString());
			} else {
				model.setEliminateCount("");
			}
			if (o[4] != null && o[4] != "") {
				model.setAwaitCount(o[4].toString());
			} else {
				model.setAwaitCount("");
			}
			if (o[2] != "0") {
				double rate = (Double.parseDouble(o[6].toString()))
						/ (Double.parseDouble(o[2].toString()) - Double
								.parseDouble(o[5].toString()));
				NumberFormat nf = NumberFormat.getPercentInstance();
				nf.setMinimumFractionDigits(2);
				model.setEliminateRate(nf.format(rate));
			}
			querylist.add(model);
		}
		PageObject o = new PageObject();
		o.setList(querylist);
		o.setTotalCount(count);
		return o;
	}

	public PageObject failureQueryByEqu(String startDate, String end,
			String enterprisecode, int start, int limit) {
		String countsql =

		"select count(*)\n"
				+ "  from (select r.attribute_code, count(*) find_count\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.isuse = 'Y'\n"
				+ "           and r.entreprise_code = '" + enterprisecode
				+ "' and r.wo_status != 0\n"
				+ "           and r.write_date between\n"
				+ "               to_date('" + startDate
				+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') and\n"
				+ "               to_date('" + end
				+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss')\n"
				+ "         group by r.attribute_code\n"
				+ "         order by r.attribute_code)";

		String sql = "select r.attribute_code,\n"
				+ "       (select max(a.equ_name)\n"
				+ "          from equ_j_failures a\n"
				+ "         where a.attribute_code = r.attribute_code\n"
				+ "           and a.isuse = 'Y'\n"
				+ "           and a.entreprise_code = '"
				+ enterprisecode
				+ "') equ_name,\n"
				+ "       count(*) find_count,\n"
				+ "       (select count(*)\n"
				+ "          from equ_j_failures t\n"
				+ "         where t.isuse = 'Y'\n"
				+ "           and t.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "           and t.attribute_code = r.attribute_code\n"
				+ "           and t.wo_status in (3, 4, 14)) eliminate_count,\n"
				+ "       (select count(*)\n"
				+ "          from equ_j_failures t\n"
				+ "         where t.isuse = 'Y'\n"
				+ "           and t.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "           and t.attribute_code = r.attribute_code\n"
				+ "           and t.wo_status in (11,12,13,20)) await_count,\n"
				+ "       (select count(*)\n"
				+ "          from equ_j_failures t\n"
				+ "         where t.isuse = 'Y'\n"
				+ "           and t.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "           and t.attribute_code = r.attribute_code\n"
				+ "           and t.wo_status in (6,10)) back_count,\n"
				+ "       (select count(*)\n"
				+ "          from equ_j_failures t\n"
				+ "         where t.isuse = 'Y'\n"
				+ "           and t.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "           and t.attribute_code = r.attribute_code\n"
				+ "           and t.wo_status in (4)) ys_count\n"
				+ "  from equ_j_failures r\n"
				+ " where r.isuse = 'Y'\n"
				+ "   and r.entreprise_code = '"
				+ enterprisecode
				+ "' and r.wo_status !=0\n"
				+ "   and r.write_date between\n"
				+ "       to_date('"
				+ startDate
				+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') and\n"
				+ "       to_date('"
				+ end
				+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss')\n"
				+ " group by r.attribute_code\n" + " order by r.attribute_code";

		Object obj = bll.getSingal(countsql);
		Long count = 0L;
		if (obj != null) {
			count = Long.parseLong(obj.toString());
		}
		List list = bll.queryByNativeSQL(sql, start, limit);
		List<EquFailuresQueryForm> querylist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			EquFailuresQueryForm model = new EquFailuresQueryForm();
			Object[] o = (Object[]) list.get(i);
			if (o[0] != null) {
				model.setAttributeCode(o[0].toString());
			} else {
				model.setAttributeCode("");
			}
			if (o[1] != null && o[1] != "") {
				model.setEquName(o[1].toString());
			} else {
				model.setEquName("");
			}
			if (o[2] != null && o[2] != "") {
				model.setCount(o[2].toString());
			} else {
				model.setCount("");
			}
			if (o[3] != null && o[3] != "") {
				model.setEliminateCount(o[3].toString());
			} else {
				model.setEliminateCount("");
			}
			if (o[4] != null && o[4] != "") {
				model.setAwaitCount(o[4].toString());
			} else {
				model.setAwaitCount("");
			}
			if (o[6].toString() == "0") {
				double rate = 0;
				NumberFormat nf = NumberFormat.getPercentInstance();
				nf.setMinimumFractionDigits(2);
				model.setEliminateRate(nf.format(rate));
			} else if (o[2] != "0") {
				double rate = (Double.parseDouble(o[6].toString()))
						/ (Double.parseDouble(o[2].toString()) - Double
								.parseDouble(o[5].toString()));
				NumberFormat nf = NumberFormat.getPercentInstance();
				nf.setMinimumFractionDigits(2);
				model.setEliminateRate(nf.format(rate));
			}
			querylist.add(model);
		}
		PageObject o = new PageObject();
		o.setList(querylist);
		o.setTotalCount(count);
		return o;
	}

	public PageObject awaitFailureQuery(String from, String to,
			String reparidep, String domProfession, String awaitType,
			String status, String enterprisecode, int start, int limit) {
		if ("".equals(reparidep) || reparidep.equals(null)) {
			reparidep = "%";
		}
		if ("".equals(domProfession) || reparidep.equals(null)) {
			domProfession = "%";
		}
		String countsql = "select count(a.failure_code) from (select distinct r.failure_code\n"
				+ "  from equ_j_failures r, equ_j_failure_history t\n"
				+ " where r.write_date between\n" + "       to_date('"
				+ from
				+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') and\n"
				+ "       to_date('"
				+ to
				+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss')\n"
				+ "   and r.failure_code = t.failure_code\n"
				+ "   and t.approve_type = 11\n"
				+ "   and r.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "   and t.enterprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "   and t.isuse = 'Y'\n"
				+ "   and r.isuse = 'Y'\n"
				+ "   and t.await_type like '"
				+ awaitType
				+ "'\n"
				+ "   and r.domination_profession like '"
				+ domProfession
				+ "'\n"
				+ "   and r.repair_dep like '"
				+ reparidep
				+ "'\n"
				+ "   and r.wo_status like '" + status + "') a";
		String sql = "select r.failure_code,\n"
				+ "       r.wo_status,\n"
				+ "       r.failure_content,\n"
				+ "       r.domination_profession,\n"
				+ "       getspecialname(r.domination_profession) domination_profession_name,\n"
				+ "       r.repair_dep,\n"
				+ "       getdeptname(r.repair_dep) repair_dep_name,\n"
				+ "       r.find_by,\n"
				+ "       getworkername(r.find_by) find_by_name,\n"
				+ "       t.await_type,\n"
				+ "       to_char(t.approve_time, 'yyyy-mm-dd hh24:mi:ss')approve_time,\n"
				+ "       (select b.approve_opinion\n"
				+ "          from equ_j_failure_history b\n"
				+ "         where b.id = (select max(a.id)\n"
				+ "                         from equ_j_failure_history a\n"
				+ "                        where a.failure_code = r.failure_code\n"
				+ "                          and a.approve_type in (12, 13, 15,5)\n"
				+ "                          and a.enterprise_code = r.entreprise_code\n"
				+ "                          and a.isuse = 'Y')) opinion1,\n"
				+ "       (select b.approve_opinion\n"
				+ "          from equ_j_failure_history b\n"
				+ "         where b.id = (select max(a.id)\n"
				+ "                         from equ_j_failure_history a\n"
				+ "                        where a.failure_code = r.failure_code\n"
				+ "                          and a.approve_type in (17,21)\n"
				+ "                          and a.enterprise_code = r.entreprise_code\n"
				+ "                          and a.isuse = 'Y')) opinion2,\n"
				+ "       (select b.approve_opinion\n"
				+ "          from equ_j_failure_history b\n"
				+ "         where b.id = (select max(a.id)\n"
				+ "                         from equ_j_failure_history a\n"
				+ "                        where a.failure_code = r.failure_code\n"
				+ "                          and a.approve_type in (16,19,20)\n"
				+ "                          and a.enterprise_code = r.entreprise_code\n"
				+ "                          and a.isuse = 'Y')) opinion3,\n"
				+ "       t.approve_opinion,\n"
				+ "       r.id,\n"
				+ "       r.work_flow_no,\n"
				+ "       (select b.approve_opinion\n"
				+ "          from equ_j_failure_history b\n"
				+ "         where b.id = (select max(a.id)\n"
				+ "                         from equ_j_failure_history a\n"
				+ "                        where a.failure_code = r.failure_code\n"
				+ "                          and a.approve_type in (22,23)\n"
				+ "                          and a.enterprise_code = r.entreprise_code\n"
				+ "                          and a.isuse = 'Y')) opinion4 ,\n"
				+ "       t1.failuretype_name,\n"
				+ "       (select to_char(b.approve_time, 'yyyy-mm-dd hh24:mi:ss')approve_time\n"
				+ "          from equ_j_failure_history b\n"
				+ "         where b.id = (select max(a.id)\n"
				+ "                         from equ_j_failure_history a\n"
				+ "                        where a.failure_code = r.failure_code\n"
				+ "                          and a.approve_type ='4'\n"
				+ "                          and a.enterprise_code = r.entreprise_code\n"
				+ "                          and a.isuse = 'Y')) overtime\n"
				+ "  from equ_j_failures r, equ_j_failure_history t,equ_c_failure_type t1\n"
				+ " where r.write_date between\n" + "       to_date('"
				+ from
				+ " 00:00:00', 'yyyy-mm-dd hh24:mi:ss') and\n"
				+ "       to_date('"
				+ to
				+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss')\n"
				+ "   and r.failure_code = t.failure_code\n"
				+ "   and t.approve_type = 11\n"
				+ "   and r.failuretype_code = t1.failuretype_code\n"
				+ "   and r.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "   and t.enterprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "   and t.isuse = 'Y'\n"
				+ "   and r.isuse = 'Y'\n"
				+ "   and t.await_type like '"
				+ awaitType
				+ "'\n"
				+ "   and r.domination_profession like '"
				+ domProfession
				+ "'\n"
				+ "   and r.repair_dep like '"
				+ reparidep
				+ "'\n"
				+ "   and r.wo_status like '"
				+ status
				+ "'\n"
				+ "	and t.id in (select max(fh.id) from equ_j_failure_history fh where fh.approve_type =11 group by fh.failure_code)";
		Object obj = bll.getSingal(countsql);
        System.out.println(sql);
		Long count = 0L;
		if (obj != null) {
			count = Long.parseLong(obj.toString());
		}
		List list = bll.queryByNativeSQL(sql, start, limit);
		List<EquFailuresQueryForm> querylist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			EquFailuresQueryForm model = new EquFailuresQueryForm();
			Object[] o = (Object[]) list.get(i);
			if (o[0] != null) {
				model.setFailureCode(o[0].toString());
			} else {
				model.setFailureCode("");
			}
			if (o[1] != null && o[1] != "") {
				model.setWoStatus(o[1].toString());
			} else {
				model.setWoStatus("");
			}
			if (o[2] != null && o[2] != "") {
				model.setFailureContent(o[2].toString());
			} else {
				model.setFailureContent("");
			}
			if (o[3] != null && o[3] != "") {
				model.setDominationProfession(o[3].toString());
			} else {
				model.setDominationProfession("");
			}
			if (o[4] != null && o[4] != "") {
				model.setDominationProfessionName(o[4].toString());
			} else {
				model.setDominationProfessionName("");
			}
			if (o[5] != null && o[5] != "") {
				model.setRepairDep(o[5].toString());
			} else {
				model.setRepairDep("");
			}
			if (o[6] != null && o[6] != "") {
				model.setRepairDepName(o[6].toString());
			} else {
				model.setRepairDepName("");
			}
			if (o[7] != null && o[7] != "") {
				model.setFindBy(o[7].toString());
			} else {
				model.setFindBy("");
			}
			if (o[8] != null && o[8] != "") {
				model.setFindByName(o[8].toString());
			} else {
				model.setFindByName("");
			}
			if (o[9] != null && o[9] != "") {
				model.setAwaitType(o[9].toString());
			} else {
				model.setAwaitType("");
			}
			if (o[10] != null && o[10] != "") {
				model.setApproveTime(o[10].toString());
			} else {
				model.setApproveTime("");
			}
			if (o[11] != null && o[11] != "") {
				model.setRepairOpinion(o[11].toString());
			} else {
				model.setRepairOpinion("");
			}
			if (o[12] != null && o[12] != "") {
				model.setEquOpinion(o[12].toString());
			} else {
				model.setEquOpinion("");
			}
			if (o[13] != null && o[13] != "") {
				model.setLeaderOpinion(o[13].toString());
			} else {
				model.setLeaderOpinion("");
			}
			if (o[14] != null && o[14] != "") {
				model.setApproveOpinion(o[14].toString());
			} else {
				model.setApproveOpinion("");
			}
			if (o[15] != null && o[15] != "") {
				model.setId(Long.parseLong(o[15].toString()));
			}
			if (o[16] != null && o[16] != "") {
				model.setWorkFlowNo(o[16].toString());
			} else {
				model.setWorkFlowNo("");
			}
			if (o[17] != null && o[17] != "")// 总工待处理意见
			{
				model.setType(o[17].toString());
			} else {
				model.setType("");
			}
			if (o[18] != null && o[18] !="")
			{
				model.setFailuretypeName(o[18].toString());
			}else {
				model.setFailuretypeName("");
			}
			if (o[19] != null && o[19] !="")
			{
				model.setOvertime(o[19].toString());
			} else {
				model.setOvertime("");
			}
			querylist.add(model);
		}
		PageObject o = new PageObject();
		o.setList(querylist);
		o.setTotalCount(count);
		return o;
	}

	public List<EquFailuresQueryForm> failureMonthReport(String month,
			String enterprisecode) {
		String sql = "select a.eliminate_count,\n"
				+ "       b.noeliminate_count,\n"
				+ "       c.repeat_count,\n"
				+ "       d.back_count,\n"
				// add by ltong 20100527 超时缺陷数
				+ "        nvl(e.sumNum,0),\n"
				// add by ltong 20100527 缺陷总数
				+ "       f.totalNum,\n"
				// add by ltong 20100527 缺陷及时率
				+ "       (decode(f.totalNum, 0, 0, (f.totalNum - nvl(e.sumNum,0)) / f.totalNum) * 100) || '%'\n"
				+ "  from (select count(*) eliminate_count\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.wo_status in (3, 4, 14)\n"
				+ "           and to_char(r.write_date, 'yyyy-mm') = '"
				+ month
				+ "'\n"
				+ "           and r.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "           and r.isuse = 'Y'\n"
				+ "           and r.domination_profession = ?\n"
				+ "           ) a,\n"
				+ "       (select count(*) noeliminate_count\n"
				+ "          from equ_j_failures t\n"
				+ "         where t.wo_status in (1, 2, 5, 7, 8, 9, 11, 12, 13, 15, 16, 17,18,20,21)\n"
				+ "           and to_char(t.write_date, 'yyyy-mm') = '"
				+ month
				+ "'\n"
				+ "           and t.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "           and t.isuse = 'Y'\n"
				+ "           and t.domination_profession = ?\n"
				+ "           ) b,\n"
				+ "       (select count(*) repeat_count\n"
				+ "          from equ_j_failures t\n"
				+ "         where to_char(t.write_date, 'yyyy-mm') = '"
				+ month
				+ "'\n"
				+ "           and t.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "           and t.isuse = 'Y'\n"
				+ "           and t.domination_profession = ?\n"
				+ "           and t.unqualified_failure_code is not null) c,\n"
				+ "       (select count(*) back_count\n"
				+ "          from equ_j_failures t\n"
				+ "         where t.wo_status in (10)\n"
				+ "           and to_char(t.write_date, 'yyyy-mm') = '"
				+ month
				+ "'\n"
				+ "           and t.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "           and t.isuse = 'Y'\n"
				+ "           and t.domination_profession = ?\n"
				+ "           ) d,\n"
				// ------------add by ltong超时缺陷数-------------
				+ "(select sum(case\n"
				+ "                      when t.await_time is not null then\n"
				+ "                       (case\n"
				+ "                      when nvl(t.end_time, sysdate) > t.await_time then\n"
				+ "                       1\n"
				+ "                      else\n"
				+ "                       0\n"
				+ "                    end) else(case\n"
				+ "                 when nvl(t.end_time, sysdate) >\n"
				+ "                      decode(t.failuretype_code,\n"
				+ "                             2,\n"
				+ "                             t.report_time + 3,\n"
				+ "                             3,\n"
				+ "                             t.report_time + 1,\n"
				+ "                             nvl(t.end_time, sysdate) + 1) then\n"
				+ "                  1\n"
				+ "                 else\n"
				+ "                  0\n"
				+ "               end) end) sumNum\n"
				+ "          from (select r.failure_code,\n"
				+ "                       (select a.approve_time\n"
				+ "                          from equ_j_failure_history a\n"
				+ "                         where a.failure_code = r.failure_code\n"
				+ "                           and a.approve_type = 4\n"
				+ "                           and a.isuse = 'Y'\n"
				+ "                           and a.enterprise_code = r.entreprise_code\n"
				+ "                           and rownum = 1) end_time,\n"
				+ "                       (select max(a.approve_time)\n"
				+ "                          from equ_j_failure_history a\n"
				+ "                         where a.failure_code = r.failure_code\n"
				+ "                           and a.approve_type = 0\n"
				+ "                           and a.isuse = 'Y'\n"
				+ "                           and a.enterprise_code = r.entreprise_code) report_time,\n"
				+ "                       (select c.delay_date\n"
				+ "                          from equ_j_failure_history c\n"
				+ "                         where c.id =\n"
				+ "                               (select max(b.id)\n"
				+ "                                  from equ_j_failure_history b\n"
				+ "                                 where b.failure_code = r.failure_code\n"
				+ "                                   and b.approve_type in (5, 19, 22)\n"
				+ "                                   and b.isuse = 'Y'\n"
				+ "                                   and b.enterprise_code = r.entreprise_code)) await_time,\n"
				+ "                       r.failuretype_code\n"
				+ "                  from equ_j_failures r\n"
				+ "                 where r.isuse = 'Y'\n"
				+ "                   and r.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "                   and r.wo_status <> '0'\n"
				+ "                   and r.domination_profession = ?\n"
				+ "                   and to_char(r.write_date, 'yyyy-mm') = '"
				+ month
				+ "') t\n"
				+ "\n"
				+ "        ) e,\n"
				+ "(select count(*) totalNum\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.isuse = 'Y'\n"
				+ "           and r.entreprise_code = '"
				+ enterprisecode
				+ "'\n"
				+ "           and r.domination_profession = ?\n"
				+ "           and to_char(r.write_date, 'yyyy-mm') = '"
				+ month
				+ "') f";

		List<EquFailuresQueryForm> querylist = new ArrayList();
		List<RunCSpecials> spList = spremote.findByType("0", enterprisecode);
		List<RunCSpecials> rpList = spremote.findByType("2", enterprisecode);
		List<RunCSpecials> speciallist = new ArrayList();
		for (RunCSpecials tmp : spList) {
			if (!speciallist.contains(tmp)) {
				speciallist.add(tmp);
			}
		}
		for (RunCSpecials tmp : rpList) {
			if (!speciallist.contains(tmp)) {
				speciallist.add(tmp);
			}
		}
		for (int i = 0; i < speciallist.size(); i++) {
			RunCSpecials special = speciallist.get(i);
			List list = bll.queryByNativeSQL(sql, new Object[] {
					special.getSpecialityCode(), special.getSpecialityCode(),
					special.getSpecialityCode(), special.getSpecialityCode(),
					special.getSpecialityCode(), special.getSpecialityCode() });
			EquFailuresQueryForm model = new EquFailuresQueryForm();
			Object[] o = (Object[]) list.get(0);
			model.setDominationProfession(special.getSpecialityCode());
			model.setDominationProfessionName(special.getSpecialityName());
			model.setEliminateCount(o[0].toString());
			model.setNoeliminateCount(o[1].toString());
			model.setRepeatCount(o[2].toString());
			model.setBackCount(o[3].toString());
			model.setOverTime(o[4].toString());
			model.setTotalNum(o[5].toString());
			model.setFailureInTime(o[6].toString());

			querylist.add(model);
		}
		return querylist;
	}

	/**
	 * 查询已消除缺陷数列、未消除缺陷数、退回缺陷数、超时缺陷数详细明细
	 * add by sychen 20100915
	 * @param specialty
	 * @param defectType
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject getEquDetailList(String writeDate,String specialty,String defectType,String enterpriseCode,int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql ="";
		if(defectType!=null && !defectType.equals("")&& defectType.equals("D")){
			sql=
				"select *\n" +
				"  from (select t.failure_content,\n" + 
				"               t.repair_dep,\n" + 
				"               getdeptname(t.repair_dep) repairDeptName,\n" + 
				"               t.domination_profession,\n" + 
				"               getspecialname(t.domination_profession) dominationProfessionName,\n" + 
				"               decode(t.wo_status,\n" + 
				"                      '0',\n" + 
				"                      '未上报',\n" + 
				"                      '1',\n" + 
				"                      '待消缺',\n" + 
				"                      '2',\n" + 
				"                      '待确认',\n" + 
				"                      '3',\n" + 
				"                      '点检待验收',\n" + 
				"                      '4',\n" + 
				"                      '运行已验收',\n" + 
				"                      '5',\n" + 
				"                      '已延期',\n" + 
				"                      '6',\n" + 
				"                      '作废',\n" + 
				"                      '7',\n" + 
				"                      '设备部仲裁',\n" + 
				"                      '8',\n" + 
				"                      '已仲裁待消缺',\n" + 
				"                      '9',\n" + 
				"                      '验收退回',\n" + 
				"                      '10',\n" + 
				"                      '退回',\n" + 
				"                      '11',\n" + 
				"                      '点检延期待处理审批',\n" + 
				"                      '12',\n" + 
				"                      '设备部主任延期待处理审批',\n" + 
				"                      '13',\n" + 
				"                      '运行延期待处理审批',\n" + 
				"                      '14',\n" + 
				"                      '运行待验收',\n" + 
				"                      '15',\n" + 
				"                      '点检延期待处理退回',\n" + 
				"                      '16',\n" + 
				"                      '设备部延期待处理退回',\n" + 
				"                      '18',\n" + 
				"                      '已确认待消缺',\n" + 
				"                      '20',\n" + 
				"                      '总工审批',\n" + 
				"                      '21',\n" + 
				"                      '总工延期待处理审批退回',\n" + 
				"                      '17',\n" + 
				"                      '运行延期待处理退回') woStatusText,\n" + 
				"               to_char(t.write_date, 'yyyy-mm-dd hh24:mi:ss') write_date,\n" + 
				"               (select to_char(c.delay_date, 'yyyy-mm-dd hh24:mi:ss')\n" + 
				"                  from equ_j_failure_history c\n" + 
				"                 where c.id =\n" + 
				"                       (select max(b.id)\n" + 
				"                          from equ_j_failure_history b\n" + 
				"                         where b.failure_code = t.failure_code\n" + 
				"                           and b.approve_type in\n" + 
				"                               (5, 19, 22, '12', '13', '20', '21')\n" + 
				"                           and b.isuse = 'Y'\n" + 
				"                           and b.enterprise_code = t.entreprise_code)) delayDate,\n" + 
				"               t.find_by,\n" + 
				"               getworkername(t.find_by) findByName,\n" + 
				"               t.find_dept,\n" + 
				"               getdeptname(t.find_dept) findDeptName,\n" + 
				"               t.failure_code,\n" + 
				"               t.belong_system,\n" + 
				"               (select t5.block_name\n" + 
				"                  from equ_c_block t5\n" + 
				"                 where t5.is_use = 'Y'\n" + 
				"                   and t5.block_code = t.belong_system\n" + 
				"                   and rownum = 1) belongSystemText,\n" + 
				"               to_char(t.Find_Date, 'yyyy-mm-dd hh24:mi:ss') Find_Date,\n" + 
				"               a.failuretype_name,\n" + 
				"               a.failure_pri,\n" + 
				"               (select a.approve_time\n" + 
				"                  from equ_j_failure_history a\n" + 
				"                 where a.failure_code = t.failure_code\n" + 
				"                   and a.approve_type = 4\n" + 
				"                   and a.isuse = 'Y'\n" + 
				"                   and a.enterprise_code = t.entreprise_code\n" + 
				"                   and rownum = 1) end_time,\n" + 
				"               (select max(a.approve_time)\n" + 
				"                  from equ_j_failure_history a\n" + 
				"                 where a.failure_code = t.failure_code\n" + 
				"                   and a.approve_type = 0\n" + 
				"                   and a.isuse = 'Y'\n" + 
				"                   and a.enterprise_code = t.entreprise_code) report_time,\n" + 
				"               (select c.delay_date\n" + 
				"                  from equ_j_failure_history c\n" + 
				"                 where c.id =\n" + 
				"                       (select max(b.id)\n" + 
				"                          from equ_j_failure_history b\n" + 
				"                         where b.failure_code = t.failure_code\n" + 
				"                           and b.approve_type in (5, 19, 22)\n" + 
				"                           and b.isuse = 'Y'\n" + 
				"                           and b.enterprise_code = t.entreprise_code)) await_time,\n" + 
				"               t.failuretype_code\n" + 
				"          from equ_j_failures t, equ_c_failure_type a\n" + 
				"         where t.failuretype_code = a.failuretype_code(+)\n" + 
				"           and t.isuse = 'Y'\n" + 
				"           and a.is_use = 'Y'\n" + 
				"           and t.entreprise_code = '"+enterpriseCode+"'\n" + 
				"           and a.enterprise_code = '"+enterpriseCode+"'\n" + 
				"           and t.wo_status <> '0'\n" + 
				"           and t.domination_profession = '"+specialty+"'\n" + 
				"           and to_char(t.write_date, 'yyyy-mm') = '"+writeDate+"') tt\n" + 
				" where ((tt.await_time is not null and\n" + 
				"       (nvl(tt.end_time, sysdate) > tt.await_time)) or\n" + 
				"       (tt.await_time is null and\n" + 
				"       (nvl(tt.end_time, sysdate) >\n" + 
				"       decode(tt.failuretype_code,\n" + 
				"                 2,\n" + 
				"                 tt.report_time + 3,\n" + 
				"                 3,\n" + 
				"                 tt.report_time + 1,\n" + 
				"                 nvl(tt.end_time, sysdate) + 1))))";

		}
		else{
			sql =
				"select t.failure_content,\n" +
				"       t.repair_dep,\n" + 
				"       getdeptname(t.repair_dep) repairDeptName,\n" + 
				"       t.domination_profession,\n" + 
				"       getspecialname(t.domination_profession) dominationProfessionName,\n" + 
				"       decode(t.wo_status,\n" + 
				"              '0',\n" + 
				"              '未上报',\n" + 
				"              '1',\n" + 
				"              '待消缺',\n" + 
				"              '2',\n" + 
				"              '待确认',\n" + 
				"              '3',\n" + 
				"              '点检待验收',\n" + 
				"              '4',\n" + 
				"              '运行已验收',\n" + 
				"              '5',\n" + 
				"              '已延期',\n" + 
				"              '6',\n" + 
				"              '作废',\n" + 
				"              '7',\n" + 
				"              '设备部仲裁',\n" + 
				"              '8',\n" + 
				"              '已仲裁待消缺',\n" + 
				"              '9',\n" + 
				"              '验收退回',\n" + 
				"              '10',\n" + 
				"              '退回',\n" + 
				"              '11',\n" + 
				"              '点检延期待处理审批',\n" + 
				"              '12',\n" + 
				"              '设备部主任延期待处理审批',\n" + 
				"              '13',\n" + 
				"              '运行延期待处理审批',\n" + 
				"              '14',\n" + 
				"              '运行待验收',\n" + 
				"              '15',\n" + 
				"              '点检延期待处理退回',\n" + 
				"              '16',\n" + 
				"              '设备部延期待处理退回',\n" + 
				"              '18',\n" + 
				"              '已确认待消缺',\n" + 
				"              '20',\n" + 
				"              '总工审批',\n" + 
				"              '21',\n" + 
				"              '总工延期待处理审批退回',\n" + 
				"              '17',\n" + 
				"              '运行延期待处理退回') woStatusText,\n" + 
				"       to_char(t.write_date, 'yyyy-mm-dd hh24:mi:ss') write_date,\n" + 
				"       (select to_char(c.delay_date, 'yyyy-mm-dd hh24:mi:ss')\n" + 
				"          from equ_j_failure_history c\n" + 
				"         where c.id =\n" + 
				"               (select max(b.id)\n" + 
				"                  from equ_j_failure_history b\n" + 
				"                 where b.failure_code = t.failure_code\n" + 
				"                   and b.approve_type in (5, 19, 22, '12', '13', '20', '21')\n" + 
				"                   and b.isuse = 'Y'\n" + 
				"                   and b.enterprise_code = t.entreprise_code)) delayDate,\n" + 
				"       t.find_by,\n" + 
				"       getworkername(t.find_by) findByName,\n" + 
				"       t.find_dept,\n" + 
				"       getdeptname(t.find_dept) findDeptName,\n" + 
				"       t.failure_code,\n" + 
				"       t.belong_system,\n" + 
				"       (select t5.block_name\n" + 
				"          from equ_c_block t5\n" + 
				"         where t5.is_use = 'Y'\n" + 
				"           and t5.block_code = t.belong_system\n" + 
				"           and rownum = 1) belongSystemText,\n" + 
				"       to_char(t.Find_Date, 'yyyy-mm-dd hh24:mi:ss') Find_Date,\n" + 
				"       a.failuretype_name,\n" + 
				"       a.failure_pri\n" + 
				"  from equ_j_failures t, equ_c_failure_type a\n" + 
				" where t.failuretype_code = a.failuretype_code(+)\n" + 
				"   and t.isuse = 'Y'\n" + 
				"   and a.is_use = 'Y'\n" + 
				"   and t.entreprise_code = '"+enterpriseCode+"'\n" + 
				"   and a.enterprise_code = '"+enterpriseCode+"'\n";
			
		      if(writeDate!=null && !writeDate.equals("")){
		        	sql+="and to_char(t.write_date, 'yyyy-mm') = '"+writeDate+"'\n";
		        }
		        if(specialty!=null && !specialty.equals("")){
		        	sql+="and t.domination_profession = '"+specialty+"'\n";
		        }
		        if(defectType!=null && !defectType.equals("")){
		        	if(defectType.equals("A")){
		        		sql+="and t.wo_status in (3, 4, 14)\n";
		        	}
		        	else if(defectType.equals("B")){
		        		sql+="and t.wo_status in (1, 2, 5, 7, 8, 9, 11, 12, 13, 15, 16, 17, 18, 20, 21)\n";
		        	}
		        	else if(defectType.equals("C")){
		        		sql+="and t.wo_status in (10)\n";
		        	}
		        }
		}
			
		String sqlCount = "select count(*) from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}
	
	
	public List<EquFailuresQueryForm> failureYearReport(String year,
			String enterpriseCode, String speicalcode) {
		String sql = "select failuretype,type,\n"
				+ "       sum(nvl(decode(month, '01', count), '')) m1,\n"
				+ "       sum(nvl(decode(month, '02', count), '')) m2,\n"
				+ "       sum(nvl(decode(month, '03', count), '')) m3,\n"
				+ "       sum(nvl(decode(month, '04', count), '')) m4,\n"
				+ "       sum(nvl(decode(month, '05', count), '')) m5,\n"
				+ "       sum(nvl(decode(month, '06', count), '')) m6,\n"
				+ "       sum(nvl(decode(month, '07', count), '')) m7,\n"
				+ "       sum(nvl(decode(month, '08', count), '')) m8,\n"
				+ "       sum(nvl(decode(month, '09', count), '')) m9,\n"
				+ "       sum(nvl(decode(month, '10', count), '')) m10,\n"
				+ "       sum(nvl(decode(month, '11', count), '')) m11,\n"
				+ "       sum(nvl(decode(month, '12', count), '')) m12\n"
				+ "  from ((select '"
				+ speicalcode
				+ "' profession,'1' failuretype,'1' type,to_char(r.write_date, 'mm') month, count(1) count\n"
				+ "           from equ_j_failures r\n"
				+ "          where r.domination_profession = '"
				+ speicalcode
				+ "'\n"
//				+ "            and r.wo_status != '0'\n"
				+ "            and r.wo_status   not in (0, 6, 10)   \n"//modify by wpzhu 20100729
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and (r.failuretype_code = 1 or r.failuretype_code = 2)\n"
				+ "            and r.isuse = 'Y'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '"
				+ speicalcode
				+ "' profession,'1' failuretype,'2' type,to_char(r.write_date, 'mm') month,count(1) count\n"
				+ "           from equ_j_failures r\n"
				+ "          where r.domination_profession = '"
				+ speicalcode
				+ "'\n"
//				+ "            and r.wo_status in (3, 4, 14)\n"//modify by wpzhu 
				+ "            and r.wo_status in (4)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and (r.failuretype_code = 1 or r.failuretype_code = 2)\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '"
				+ speicalcode
				+ "' profession,'1' failuretype,'3' type,to_char(r.write_date, 'mm') month,count(1) count\n"
				+ "           from equ_j_failures r, equ_j_failure_history t\n"
				+ "          where r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "            and r.failure_code = t.failure_code\n"
				+ "            and t.approve_type = 5\n"
				+ "            and r.wo_status not in (6, 10)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and t.isuse = 'Y'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and (r.failuretype_code = 1 or r.failuretype_code = 2)\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '"
				+ speicalcode
				+ "' profession,'1' failuretype,'4' type,b.month month,(nvl(a.yscount, 0) / b.allcount) count\n"
				+ "           from (select to_char(r.write_date, 'mm') month, count(*) yscount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "                    and r.wo_status in (4)\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and (r.failuretype_code = 1 or r.failuretype_code = 2)\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) a,\n"
				+ "                (select to_char(r.write_date, 'mm') month, count(*) allcount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "                    and r.wo_status not in (0, 6, 10)\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and (r.failuretype_code = 1 or r.failuretype_code = 2)\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) b\n"
				+ "          where a.month(+) = b.month) union\n"
				+ "        (select '"
				+ speicalcode
				+ "' profession,'2' failuretype,'1' type,to_char(r.write_date, 'mm') month,count(1)\n"
				+ "           from equ_j_failures r\n"
				+ "          where r.domination_profession = '"
				+ speicalcode
				+ "'\n"
//				+ "            and r.wo_status != '0'\n"
				+ "            and r.wo_status  not in(0,6,10)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and (r.failuretype_code = 3 or r.failuretype_code = 4)\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '"
				+ speicalcode
				+ "' profession,'2' failuretype,'2' type,to_char(r.write_date, 'mm') month,count(1)\n"
				+ "           from equ_j_failures r\n"
				+ "          where r.domination_profession = '"
				+ speicalcode
				+ "'\n"
//				+ "            and r.wo_status in (3, 4, 14)\n"//modify by wpzhu
				+ "            and r.wo_status in (4)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and (r.failuretype_code = 3 or r.failuretype_code = 4)\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm') union\n"
				+ "         select '"
				+ speicalcode
				+ "' profession,'2' failuretype,'3' type,to_char(r.write_date, 'mm') month,count(1) count\n"
				+ "           from equ_j_failures r, equ_j_failure_history t\n"
				+ "          where r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "            and r.failure_code = t.failure_code\n"
				+ "            and t.approve_type = 5\n"
				+ "            and r.wo_status not in (6, 10)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and t.isuse = 'Y'\n"
				+ "            and (r.failuretype_code = 3 or r.failuretype_code = 4)\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '"
				+ speicalcode
				+ "' profession,'2' failuretype,'4' type,b.month month,(nvl(a.yscount, 0) / b.allcount) count\n"
				+ "           from (select to_char(r.write_date, 'mm') month, count(*) yscount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "                    and r.wo_status in (4)\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and (r.failuretype_code = 3 or r.failuretype_code = 4)\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) a,\n"
				+ "                (select to_char(r.write_date, 'mm') month, count(*) allcount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "                    and r.wo_status not in (0, 6, 10)\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and (r.failuretype_code = 3 or r.failuretype_code = 4)\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) b\n"
				+ "          where a.month(+) = b.month)\n"
				+ "         union\n"
				+ "		( select '"
				+ speicalcode
				+ "' profession,'3' failuretype,'4' type,b.month month,(nvl(a.yscount, 0) / b.allcount) count\n"
				+ "           from (select to_char(r.write_date, 'mm') month, count(*) yscount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "                    and r.wo_status in (4)\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) a,\n"
				+ "                (select to_char(r.write_date, 'mm') month, count(*) allcount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "                    and r.wo_status not in (0, 6, 10)\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) b\n"
				+ "          where a.month(+) = b.month) \n"
				+ "       )\n"
				+ " group by failuretype, type\n"
				+ " order by failuretype, type";
//		System.out.println("the sql"+sql);
		List list = bll.queryByNativeSQL(sql);
		List<EquFailuresQueryForm> querylist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			EquFailuresQueryForm model = new EquFailuresQueryForm();
			Object[] o = (Object[]) list.get(i);
			if (o[0] != null) {
				model.setQueryType(o[0].toString());
			}
			if (o[1] != null && o[1] != "") {
				model.setType(o[1].toString());
			}
			if (o[2] != null && o[2] != "") {
				model.setM1count(o[2].toString());
			}
			if (o[3] != null && o[3] != "") {
				model.setM2count(o[3].toString());
			}
			if (o[4] != null && o[4] != "") {
				model.setM3count(o[4].toString());
			}
			if (o[5] != null && o[5] != "") {
				model.setM4count(o[5].toString());
			}
			if (o[6] != null && o[6] != "") {
				model.setM5count(o[6].toString());
			}
			if (o[7] != null && o[7] != "") {
				model.setM6count(o[7].toString());
			}
			if (o[8] != null && o[8] != "") {
				model.setM7count(o[8].toString());
			}
			if (o[9] != null && o[9] != "") {
				model.setM8count(o[9].toString());
			}
			if (o[10] != null && o[10] != "") {
				model.setM9count(o[10].toString());
			}
			if (o[11] != null && o[11] != "") {
				model.setM10count(o[11].toString());
			}
			if (o[12] != null && o[12] != "") {
				model.setM11count(o[12].toString());
			}
			if (o[13] != null && o[13] != "") {
				model.setM12count(o[13].toString());
			}
			querylist.add(model);
		}
		String sql2 = "select decode(b.allcount, '0', '', (nvl(a.yscount, 0) / b.allcount)) count1,\n"
				+ "       decode(d.allcount, '0', '', (nvl(c.yscount, 0) / d.allcount)) count2,\n"
				+ "       decode(f.allcount, '0', '', (nvl(e.yscount, 0) / f.allcount)) count3\n"
				+ "  from (select count(*) yscount\n"
				+ "          from equ_j_failures r\n"
				+ "         where (r.failuretype_code = 1 or r.failuretype_code = 2)\n"
				+ "           and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "           and r.wo_status in (4)\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.isuse = 'Y') a,\n"
				+ "       (select count(*) allcount\n"
				+ "          from equ_j_failures r\n"
				+ "         where (r.failuretype_code = 1 or r.failuretype_code = 2)\n"
				+ "           and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "           and r.wo_status not in (0, 6, 10)\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and r.isuse = 'Y') b,\n"
				+ "       (select count(*) yscount\n"
				+ "          from equ_j_failures r\n"
				+ "         where (r.failuretype_code = 3 or r.failuretype_code = 4)\n"
				+ "           and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "           and r.wo_status in (4)\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.isuse = 'Y') c,\n"
				+ "       (select count(*) allcount\n"
				+ "          from equ_j_failures r\n"
				+ "         where (r.failuretype_code = 3 or r.failuretype_code = 4)\n"
				+ "           and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "           and r.wo_status not in (0, 6, 10)\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and r.isuse = 'Y') d,\n"
				+ "       (select count(*) yscount\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "           and r.wo_status in (4)\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.isuse = 'Y') e,\n"
				+ "       (select count(*) allcount\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "           and r.wo_status not in (0, 6, 10)\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode + "'\n" + "           and r.isuse = 'Y') f";
		List zonglist = bll.queryByNativeSQL(sql2);
		if (zonglist.size() > 0) {
			EquFailuresQueryForm model = new EquFailuresQueryForm();
			Object[] o = (Object[]) zonglist.get(0);
			if (o[0] != null) {
				model.setM1count(o[0].toString());
			}
			if (o[1] != null && o[1] != "") {
				model.setM2count(o[1].toString());
			}
			if (o[2] != null && o[2] != "") {
				model.setM3count(o[2].toString());
			}
			model.setQueryType("zong");
			model.setType("zong");
			querylist.add(model);
		}
		return querylist;
	}

	public List<EquYearReportForm> yearReportQuery(String year,
			String enterpriseCode) {
		List<RunCSpecials> spList = spremote.findByType("0", enterpriseCode);
		List<RunCSpecials> rpList = spremote.findByType("2", enterpriseCode);
		List<RunCSpecials> list = new ArrayList();
		List<EquYearReportForm> querylist = new ArrayList();
		for (RunCSpecials tmp : spList) {
			if (!list.contains(tmp)) {
				list.add(tmp);
			}
		}
		for (RunCSpecials tmp : rpList) {
			if (!list.contains(tmp)) {
				list.add(tmp);
			}
		}
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				EquYearReportForm model = new EquYearReportForm();
				RunCSpecials special = list.get(i);
				model.setSpecialCode(special.getSpecialityCode());
				model.setSpecialName(special.getSpecialityName());
				model.setList(this.failureYearReport(year, enterpriseCode,
						special.getSpecialityCode()));
				querylist.add(model);
			}
		}
		return querylist;
	}

	public List<EquFailuresQueryForm> failureYearReportByDept(String year,
			String enterpriseCode, String deptCode) {
		String sql = "select failuretype,type,\n"
				+ "       sum(nvl(decode(month, '01', count), '')) m1,\n"
				+ "       sum(nvl(decode(month, '02', count), '')) m2,\n"
				+ "       sum(nvl(decode(month, '03', count), '')) m3,\n"
				+ "       sum(nvl(decode(month, '04', count), '')) m4,\n"
				+ "       sum(nvl(decode(month, '05', count), '')) m5,\n"
				+ "       sum(nvl(decode(month, '06', count), '')) m6,\n"
				+ "       sum(nvl(decode(month, '07', count), '')) m7,\n"
				+ "       sum(nvl(decode(month, '08', count), '')) m8,\n"
				+ "       sum(nvl(decode(month, '09', count), '')) m9,\n"
				+ "       sum(nvl(decode(month, '10', count), '')) m10,\n"
				+ "       sum(nvl(decode(month, '11', count), '')) m11,\n"
				+ "       sum(nvl(decode(month, '12', count), '')) m12\n"
				+ "  from ((select '"
				+ deptCode
				+ "' profession,'1' failuretype,'1' type,to_char(r.write_date, 'mm') month, count(1) count\n"
				+ "           from equ_j_failures r\n"
				+ "          where r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "            and r.wo_status != '0'\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and (r.failuretype_code = 1 or r.failuretype_code = 2)\n"
				+ "            and r.isuse = 'Y'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '"
				+ deptCode
				+ "' profession,'1' failuretype,'2' type,to_char(r.write_date, 'mm') month,count(1) count\n"
				+ "           from equ_j_failures r\n"
				+ "          where r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "            and r.wo_status in (3, 4, 14)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and (r.failuretype_code = 1 or r.failuretype_code = 2)\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '"
				+ deptCode
				+ "' profession,'1' failuretype,'3' type,to_char(r.write_date, 'mm') month,count(1) count\n"
				+ "           from equ_j_failures r, equ_j_failure_history t\n"
				+ "          where r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "            and r.failure_code = t.failure_code\n"
				+ "            and t.approve_type = 5\n"
				+ "            and r.wo_status not in (6, 10)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and t.isuse = 'Y'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and (r.failuretype_code = 1 or r.failuretype_code = 2)\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '"
				+ deptCode
				+ "' profession,'1' failuretype,'4' type,b.month month,(nvl(a.yscount, 0) / b.allcount) count\n"
				+ "           from (select to_char(r.write_date, 'mm') month, count(*) yscount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "                    and r.wo_status in (4)\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and (r.failuretype_code = 1 or r.failuretype_code = 2)\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) a,\n"
				+ "                (select to_char(r.write_date, 'mm') month, count(*) allcount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "                    and r.wo_status not in (0, 6, 10)\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and (r.failuretype_code = 1 or r.failuretype_code = 2)\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) b\n"
				+ "          where a.month(+) = b.month) union\n"
				+ "        (select '"
				+ deptCode
				+ "' profession,'2' failuretype,'1' type,to_char(r.write_date, 'mm') month,count(1)\n"
				+ "           from equ_j_failures r\n"
				+ "          where r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "            and r.wo_status != '0'\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and (r.failuretype_code = 3 or r.failuretype_code = 4)\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '"
				+ deptCode
				+ "' profession,'2' failuretype,'2' type,to_char(r.write_date, 'mm') month,count(1)\n"
				+ "           from equ_j_failures r\n"
				+ "          where r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "            and r.wo_status in (3, 4, 14)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and (r.failuretype_code = 3 or r.failuretype_code = 4)\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm') union\n"
				+ "         select '"
				+ deptCode
				+ "' profession,'2' failuretype,'3' type,to_char(r.write_date, 'mm') month,count(1) count\n"
				+ "           from equ_j_failures r, equ_j_failure_history t\n"
				+ "          where r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "            and r.failure_code = t.failure_code\n"
				+ "            and t.approve_type = 5\n"
				+ "            and r.wo_status not in (6, 10)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and t.isuse = 'Y'\n"
				+ "            and (r.failuretype_code = 3 or r.failuretype_code = 4)\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '"
				+ deptCode
				+ "' profession,'2' failuretype,'4' type,b.month month,(nvl(a.yscount, 0) / b.allcount) count\n"
				+ "           from (select to_char(r.write_date, 'mm') month, count(*) yscount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "                    and r.wo_status in (4)\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and (r.failuretype_code = 3 or r.failuretype_code = 4)\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) a,\n"
				+ "                (select to_char(r.write_date, 'mm') month, count(*) allcount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "                    and r.wo_status not in (0, 6, 10)\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and (r.failuretype_code = 3 or r.failuretype_code = 4)\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) b\n"
				+ "          where a.month(+) = b.month)\n"
				+ "         union\n"
				+ "         (select '"
				+ deptCode
				+ "' profession,'3' failuretype,'4' type,b.month month,(nvl(a.yscount, 0) / b.allcount) count\n"
				+ "           from (select to_char(r.write_date, 'mm') month, count(*) yscount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "                    and r.wo_status in (4)\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) a,\n"
				+ "                (select to_char(r.write_date, 'mm') month, count(*) allcount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "                    and r.wo_status not in (0, 6, 10)\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) b\n"
				+ "          where a.month(+) = b.month)\n"
				+ "       )\n"
				+ " group by failuretype, type\n"
				+ " order by failuretype, type";
		List list = bll.queryByNativeSQL(sql);
		List<EquFailuresQueryForm> querylist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			EquFailuresQueryForm model = new EquFailuresQueryForm();
			Object[] o = (Object[]) list.get(i);
			if (o[0] != null) {
				model.setQueryType(o[0].toString());
			}
			if (o[1] != null && o[1] != "") {
				model.setType(o[1].toString());
			}
			if (o[2] != null && o[2] != "") {
				model.setM1count(o[2].toString());
			}
			if (o[3] != null && o[3] != "") {
				model.setM2count(o[3].toString());
			}
			if (o[4] != null && o[4] != "") {
				model.setM3count(o[4].toString());
			}
			if (o[5] != null && o[5] != "") {
				model.setM4count(o[5].toString());
			}
			if (o[6] != null && o[6] != "") {
				model.setM5count(o[6].toString());
			}
			if (o[7] != null && o[7] != "") {
				model.setM6count(o[7].toString());
			}
			if (o[8] != null && o[8] != "") {
				model.setM7count(o[8].toString());
			}
			if (o[9] != null && o[9] != "") {
				model.setM8count(o[9].toString());
			}
			if (o[10] != null && o[10] != "") {
				model.setM9count(o[10].toString());
			}
			if (o[11] != null && o[11] != "") {
				model.setM10count(o[11].toString());
			}
			if (o[12] != null && o[12] != "") {
				model.setM11count(o[12].toString());
			}
			if (o[13] != null && o[13] != "") {
				model.setM12count(o[13].toString());
			}
			querylist.add(model);
		}
		String sql2 = "select decode(b.allcount, '0', '', (nvl(a.yscount, 0) / b.allcount)) count1,\n"
				+ "       decode(d.allcount, '0', '', (nvl(c.yscount, 0) / d.allcount)) count2,\n"
				+ "       decode(f.allcount, '0', '', (nvl(e.yscount, 0) / f.allcount)) count3\n"
				+ "  from (select count(*) yscount\n"
				+ "          from equ_j_failures r\n"
				+ "         where (r.failuretype_code = 1 or r.failuretype_code = 2)\n"
				+ "           and r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "           and r.wo_status in (4)\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.isuse = 'Y') a,\n"
				+ "       (select count(*) allcount\n"
				+ "          from equ_j_failures r\n"
				+ "         where (r.failuretype_code = 1 or r.failuretype_code = 2)\n"
				+ "           and r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "           and r.wo_status not in (0, 6, 10)\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and r.isuse = 'Y') b,\n"
				+ "       (select count(*) yscount\n"
				+ "          from equ_j_failures r\n"
				+ "         where (r.failuretype_code = 3 or r.failuretype_code = 4)\n"
				+ "           and r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "           and r.wo_status in (4)\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.isuse = 'Y') c,\n"
				+ "       (select count(*) allcount\n"
				+ "          from equ_j_failures r\n"
				+ "         where (r.failuretype_code = 3 or r.failuretype_code = 4)\n"
				+ "           and r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "           and r.wo_status not in (0, 6, 10)\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and r.isuse = 'Y') d,\n"
				+ "       (select count(*) yscount\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "           and r.wo_status in (4)\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.isuse = 'Y') e,\n"
				+ "       (select count(*) allcount\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.repair_dep = '"
				+ deptCode
				+ "'\n"
				+ "           and r.wo_status not in (0, 6, 10)\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode + "'\n" + "           and r.isuse = 'Y') f";
		List zonglist = bll.queryByNativeSQL(sql2);
		if (zonglist.size() > 0) {
			EquFailuresQueryForm model = new EquFailuresQueryForm();
			Object[] o = (Object[]) zonglist.get(0);
			if (o[0] != null) {
				model.setM1count(o[0].toString());
			}
			if (o[1] != null && o[1] != "") {
				model.setM2count(o[1].toString());
			}
			if (o[2] != null && o[2] != "") {
				model.setM3count(o[2].toString());
			}
			model.setQueryType("zong");
			model.setType("zong");
			querylist.add(model);
		}
		return querylist;
	}

	public List<EquYearReportForm> yearReportQueryByDept(String year,
			String enterpriseCode) {
		List<EquYearReportForm> querylist = new ArrayList();
		List<HrCDept> deptList = hremote.getFailDeptById();
		if (deptList.size() > 0) {
			for (int i = 0; i < deptList.size(); i++) {
				EquYearReportForm model = new EquYearReportForm();
				HrCDept dept = deptList.get(i);
				model.setSpecialCode(dept.getDeptCode());
				model.setSpecialName(dept.getDeptName());
				model.setList(this.failureYearReportByDept(year,
						enterpriseCode, dept.getDeptCode()));
				querylist.add(model);
			}
		}
		return querylist;
	}

	public EquFailuresQueryForm zongYearEliRate(String year,
			String enterpriseCode) {
		String sql = "select sum(nvl(decode(month, '01', count), '')) m1,\n"
				+ "      sum(nvl(decode(month, '02', count), '')) m2,\n"
				+ "      sum(nvl(decode(month, '03', count), '')) m3,\n"
				+ "      sum(nvl(decode(month, '04', count), '')) m4,\n"
				+ "      sum(nvl(decode(month, '05', count), '')) m5,\n"
				+ "      sum(nvl(decode(month, '06', count), '')) m6,\n"
				+ "      sum(nvl(decode(month, '07', count), '')) m7,\n"
				+ "      sum(nvl(decode(month, '08', count), '')) m8,\n"
				+ "      sum(nvl(decode(month, '09', count), '')) m9,\n"
				+ "      sum(nvl(decode(month, '10', count), '')) m10,\n"
				+ "      sum(nvl(decode(month, '11', count), '')) m11,\n"
				+ "      sum(nvl(decode(month, '12', count), '')) m12\n"
				+ "      from\n"
				+ "(select b.month month,(nvl(a.yscount, 0) / b.allcount) count\n"
				+ "          from (select to_char(r.write_date, 'mm') month, count(*) yscount\n"
				+ "                  from equ_j_failures r\n"
				+ "                 where r.wo_status in (4)\n"
				+ "                   and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                   and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                   and r.isuse = 'Y'\n"
				+ "                 group by to_char(r.write_date, 'mm')) a,\n"
				+ "               (select to_char(r.write_date, 'mm') month, count(*) allcount\n"
				+ "                  from equ_j_failures r\n"
				+ "                 where r.wo_status not in (0, 6, 10)\n"
				+ "                   and to_char(r.write_date, 'yyyy') = '"
				+ year + "'\n" + "                   and r.entreprise_code = '"
				+ enterpriseCode + "'\n"
				+ "                   and r.isuse = 'Y'\n"
				+ "                 group by to_char(r.write_date, 'mm')) b\n"
				+ "         where a.month(+) = b.month)";
		List list = bll.queryByNativeSQL(sql);
		EquFailuresQueryForm model = new EquFailuresQueryForm();
		if (list.size() > 0) {
			Object[] o = (Object[]) list.get(0);
			if (o[0] != null && o[0] != "") {
				model.setM1count(o[0].toString());
			}
			if (o[1] != null && o[1] != "") {
				model.setM2count(o[1].toString());
			}
			if (o[2] != null && o[2] != "") {
				model.setM3count(o[2].toString());
			}
			if (o[3] != null && o[3] != "") {
				model.setM4count(o[3].toString());
			}
			if (o[4] != null && o[4] != "") {
				model.setM5count(o[4].toString());
			}
			if (o[5] != null && o[5] != "") {
				model.setM6count(o[5].toString());
			}
			if (o[6] != null && o[6] != "") {
				model.setM7count(o[6].toString());
			}
			if (o[7] != null && o[7] != "") {
				model.setM8count(o[7].toString());
			}
			if (o[8] != null && o[8] != "") {
				model.setM9count(o[8].toString());
			}
			if (o[9] != null && o[9] != "") {
				model.setM10count(o[9].toString());
			}
			if (o[10] != null && o[10] != "") {
				model.setM11count(o[10].toString());
			}
			if (o[11] != null && o[11] != "") {
				model.setM12count(o[11].toString());
			}
		}
		String sql2 = "select decode(b.allcount,'0','',(nvl(a.yscount, 0) / b.allcount)) count\n"
				+ "  from (select count(*) yscount\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.wo_status in (4)\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.isuse = 'Y') a,\n"
				+ "       (select count(*) allcount\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.wo_status not in (0, 6, 10)\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode + "'\n" + "           and r.isuse = 'Y') b";
		Object o2 = bll.getSingal(sql2);
		if (o2 != null && o2 != "") {
			model.setCount(o2.toString());
		}
		return model;
	}

	public List<EquFailuresQueryForm> failureBugReport(String year,
			String enterpriseCode, String speicalcode) {
		String sql = "select bug,type,\n"
				+ "       sum(nvl(decode(month, '01', count), '')) m1,\n"
				+ "       sum(nvl(decode(month, '02', count), '')) m2,\n"
				+ "       sum(nvl(decode(month, '03', count), '')) m3,\n"
				+ "       sum(nvl(decode(month, '04', count), '')) m4,\n"
				+ "       sum(nvl(decode(month, '05', count), '')) m5,\n"
				+ "       sum(nvl(decode(month, '06', count), '')) m6,\n"
				+ "       sum(nvl(decode(month, '07', count), '')) m7,\n"
				+ "       sum(nvl(decode(month, '08', count), '')) m8,\n"
				+ "       sum(nvl(decode(month, '09', count), '')) m9,\n"
				+ "       sum(nvl(decode(month, '10', count), '')) m10,\n"
				+ "       sum(nvl(decode(month, '11', count), '')) m11,\n"
				+ "       sum(nvl(decode(month, '12', count), '')) m12\n"
				+ "  from ((select '1' bug,'1' type,to_char(r.write_date, 'mm') month, count(1) count\n"
				+ "           from equ_j_failures r\n"
				+ "          where r.bug_code = '1060'\n"
				+ "            and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "            and r.wo_status != '0'\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and r.isuse = 'Y'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '1' bug,'2' type,to_char(r.write_date, 'mm') month,count(1) count\n"
				+ "           from equ_j_failures r\n"
				+ "          where r.bug_code = '1060'\n"
				+ "          and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "            and r.wo_status in (3, 4, 14)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '1' bug,'3' type,to_char(r.write_date, 'mm') month,count(1) count\n"
				+ "           from equ_j_failures r, equ_j_failure_history t\n"
				+ "          where r.bug_code = '1060'\n"
				+ "            and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "            and r.failure_code = t.failure_code\n"
				+ "            and t.approve_type = 5\n"
				+ "            and r.wo_status not in (6, 10)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and t.isuse = 'Y'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '1' bug,'4' type,b.month month,(nvl(a.yscount, 0) / b.allcount)*100 count\n"
				+ "           from (select to_char(r.write_date, 'mm') month, count(*) yscount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.bug_code = '1060'\n"
				+ "                  and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "                    and r.wo_status in (4)\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) a,\n"
				+ "                (select to_char(r.write_date, 'mm') month, count(*) allcount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.bug_code = '1060'\n"
				+ "                    and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "                    and r.wo_status not in (0, 6, 10)\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) b\n"
				+ "          where a.month(+) = b.month) union\n"
				+ "        (select '2' bug,'1' type,to_char(r.write_date, 'mm') month,count(1)\n"
				+ "           from equ_j_failures r\n"
				+ "          where r.bug_code = '1059'\n"
				+ "            and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "            and r.wo_status != '0'\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '2' bug,'2' type,to_char(r.write_date, 'mm') month,count(1)\n"
				+ "           from equ_j_failures r\n"
				+ "          where r.bug_code = '1059'\n"
				+ "            and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "            and r.wo_status in (3, 4, 14)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm') union\n"
				+ "         select '2' bug,'3' type,to_char(r.write_date, 'mm') month,count(1) count\n"
				+ "           from equ_j_failures r, equ_j_failure_history t\n"
				+ "          where r.bug_code = '1059'\n"
				+ "            and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "            and r.failure_code = t.failure_code\n"
				+ "            and t.approve_type = 5\n"
				+ "            and r.wo_status not in (6, 10)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and t.isuse = 'Y'\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '2' bug,'4' type,b.month month,(nvl(a.yscount, 0) / b.allcount)*100 count\n"
				+ "           from (select to_char(r.write_date, 'mm') month, count(*) yscount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.bug_code = '1059'\n"
				+ "                    and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "                    and r.wo_status in (4)\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) a,\n"
				+ "                (select to_char(r.write_date, 'mm') month, count(*) allcount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.bug_code = '1059'\n"
				+ "                    and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "                    and r.wo_status not in (0, 6, 10)\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) b\n"
				+ "          where a.month(+) = b.month) union\n"
				+ "          (select '3' bug,'1' type,to_char(r.write_date, 'mm') month,count(1)\n"
				+ "           from equ_j_failures r\n"
				+ "          where r.bug_code = '1032'\n"
				+ "            and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "            and r.wo_status != '0'\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '3' bug,'2' type,to_char(r.write_date, 'mm') month,count(1)\n"
				+ "           from equ_j_failures r\n"
				+ "          where r.bug_code = '1032'\n"
				+ "            and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "            and r.wo_status in (3, 4, 14)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm') union\n"
				+ "         select '3' bug,'3' type,to_char(r.write_date, 'mm') month,count(1) count\n"
				+ "           from equ_j_failures r, equ_j_failure_history t\n"
				+ "          where r.bug_code = '1032'\n"
				+ "            and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "            and r.failure_code = t.failure_code\n"
				+ "            and t.approve_type = 5\n"
				+ "            and r.wo_status not in (6, 10)\n"
				+ "            and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "            and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "            and t.isuse = 'Y'\n"
				+ "            and r.isuse = 'Y'\n"
				+ "          group by to_char(r.write_date, 'mm')\n"
				+ "         union\n"
				+ "         select '3' bug,'4' type,b.month month,(nvl(a.yscount, 0) / b.allcount)*100 count\n"
				+ "           from (select to_char(r.write_date, 'mm') month, count(*) yscount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.bug_code = '1032'\n"
				+ "                    and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "                    and r.wo_status in (4)\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) a,\n"
				+ "                (select to_char(r.write_date, 'mm') month, count(*) allcount\n"
				+ "                   from equ_j_failures r\n"
				+ "                  where r.bug_code = '1032'\n"
				+ "                    and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "                    and r.wo_status not in (0, 6, 10)\n"
				+ "                    and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                    and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                    and r.isuse = 'Y'\n"
				+ "                  group by to_char(r.write_date, 'mm')) b\n"
				+ "          where a.month(+) = b.month)\n"
				+ "       )\n"
				+ " group by bug, type\n" + " order by bug, type";

		List list = bll.queryByNativeSQL(sql);
		List<EquFailuresQueryForm> querylist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			EquFailuresQueryForm model = new EquFailuresQueryForm();
			Object[] o = (Object[]) list.get(i);
			if (o[0] != null) {
				model.setQueryType(o[0].toString());
			}
			if (o[1] != null && o[1] != "") {
				model.setType(o[1].toString());
			}
			if (o[2] != null && o[2] != "") {
				model.setM1count(o[2].toString());
			}
			if (o[3] != null && o[3] != "") {
				model.setM2count(o[3].toString());
			}
			if (o[4] != null && o[4] != "") {
				model.setM3count(o[4].toString());
			}
			if (o[5] != null && o[5] != "") {
				model.setM4count(o[5].toString());
			}
			if (o[6] != null && o[6] != "") {
				model.setM5count(o[6].toString());
			}
			if (o[7] != null && o[7] != "") {
				model.setM6count(o[7].toString());
			}
			if (o[8] != null && o[8] != "") {
				model.setM7count(o[8].toString());
			}
			if (o[9] != null && o[9] != "") {
				model.setM8count(o[9].toString());
			}
			if (o[10] != null && o[10] != "") {
				model.setM9count(o[10].toString());
			}
			if (o[11] != null && o[11] != "") {
				model.setM10count(o[11].toString());
			}
			if (o[12] != null && o[12] != "") {
				model.setM11count(o[12].toString());
			}
			if (o[13] != null && o[13] != "") {
				model.setM12count(o[13].toString());
			}
			querylist.add(model);
		}

		String sql2 = "select decode(b.allcount, '0', '', (nvl(a.yscount, 0) / b.allcount)) count1,\n"
				+ "       decode(d.allcount, '0', '', (nvl(c.yscount, 0) / d.allcount)) count2,\n"
				+ "       decode(f.allcount, '0', '', (nvl(e.yscount, 0) / f.allcount)) count3\n"
				+ "  from (select count(*) yscount\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.bug_code = '1060'\n"
				+ "           and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "           and r.wo_status in (4)\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.isuse = 'Y') a,\n"
				+ "       (select count(*) allcount\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.bug_code = '1060'\n"
				+ "           and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "           and r.wo_status not in (0, 6, 10)\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and r.isuse = 'Y') b,\n"
				+ "       (select count(*) yscount\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.bug_code = '1059'\n"
				+ "           and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "           and r.wo_status in (4)\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.isuse = 'Y') c,\n"
				+ "       (select count(*) allcount\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.bug_code = '1059'\n"
				+ "           and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "           and r.wo_status not in (0, 6, 10)\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and r.isuse = 'Y') d,\n"
				+ "       (select count(*) yscount\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.bug_code = '1032'\n"
				+ "           and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "           and r.wo_status in (4)\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.isuse = 'Y') e,\n"
				+ "       (select count(*) allcount\n"
				+ "          from equ_j_failures r\n"
				+ "         where r.bug_code = '1032'\n"
				+ "           and r.domination_profession = '"
				+ speicalcode
				+ "'\n"
				+ "           and r.wo_status not in (0, 6, 10)\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode + "'\n" + "           and r.isuse = 'Y') f";
		List zonglist = bll.queryByNativeSQL(sql2);
		if (zonglist.size() > 0) {
			EquFailuresQueryForm model = new EquFailuresQueryForm();
			Object[] o = (Object[]) zonglist.get(0);
			if (o[0] != null) {
				model.setM1count(o[0].toString());
			}
			if (o[1] != null && o[1] != "") {
				model.setM2count(o[1].toString());
			}
			if (o[2] != null && o[2] != "") {
				model.setM3count(o[2].toString());
			}
			model.setQueryType("zong");
			model.setType("zong");
			querylist.add(model);
		}
		return querylist;
	}

	public List<EquYearReportForm> bugReportQuery(String year,
			String enterpriseCode) {
		List<RunCSpecials> spList = spremote.findByType("0", enterpriseCode);
		List<RunCSpecials> rpList = spremote.findByType("2", enterpriseCode);
		List<RunCSpecials> list = new ArrayList();
		List<EquYearReportForm> querylist = new ArrayList();
		for (RunCSpecials tmp : spList) {
			if (!list.contains(tmp)) {
				list.add(tmp);
			}
		}
		for (RunCSpecials tmp : rpList) {
			if (!list.contains(tmp)) {
				list.add(tmp);
			}
		}
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				EquYearReportForm model = new EquYearReportForm();
				RunCSpecials special = list.get(i);
				model.setSpecialCode(special.getSpecialityCode());
				model.setSpecialName(special.getSpecialityName());
				model.setList(this.failureBugReport(year, enterpriseCode,
						special.getSpecialityCode()));
				querylist.add(model);
			}
		}
		return querylist;
	}

	public EquFailuresQueryForm zongBugEliRate(String year,
			String enterpriseCode) {
		String sql = "select sum(nvl(decode(month, '01', count), '')) m1,\n"
				+ "      sum(nvl(decode(month, '02', count), '')) m2,\n"
				+ "      sum(nvl(decode(month, '03', count), '')) m3,\n"
				+ "      sum(nvl(decode(month, '04', count), '')) m4,\n"
				+ "      sum(nvl(decode(month, '05', count), '')) m5,\n"
				+ "      sum(nvl(decode(month, '06', count), '')) m6,\n"
				+ "      sum(nvl(decode(month, '07', count), '')) m7,\n"
				+ "      sum(nvl(decode(month, '08', count), '')) m8,\n"
				+ "      sum(nvl(decode(month, '09', count), '')) m9,\n"
				+ "      sum(nvl(decode(month, '10', count), '')) m10,\n"
				+ "      sum(nvl(decode(month, '11', count), '')) m11,\n"
				+ "      sum(nvl(decode(month, '12', count), '')) m12\n"
				+ "      from\n"
				+ "(select b.month month,(nvl(a.yscount, 0) / b.allcount) count\n"
				+ "          from (select to_char(r.write_date, 'mm') month, count(*) yscount\n"
				+ "                  from equ_j_failures r\n"
				+ "                 where (r.bug_code = '1060' or r.bug_code='1059' or r.bug_code='1032')\n"
				+ "                   and r.wo_status in (4)\n"
				+ "                   and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "                   and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "                   and r.isuse = 'Y'\n"
				+ "                 group by to_char(r.write_date, 'mm')) a,\n"
				+ "               (select to_char(r.write_date, 'mm') month, count(*) allcount\n"
				+ "                  from equ_j_failures r\n"
				+ "                 where (r.bug_code = '1060' or r.bug_code='1059' or r.bug_code='1032')\n"
				+ "                   and r.wo_status not in (0, 6, 10)\n"
				+ "                   and to_char(r.write_date, 'yyyy') = '"
				+ year + "'\n" + "                   and r.entreprise_code = '"
				+ enterpriseCode + "'\n"
				+ "                   and r.isuse = 'Y'\n"
				+ "                 group by to_char(r.write_date, 'mm')) b\n"
				+ "         where a.month(+) = b.month)";
		List list = bll.queryByNativeSQL(sql);
		EquFailuresQueryForm model = new EquFailuresQueryForm();
		if (list.size() > 0) {
			Object[] o = (Object[]) list.get(0);
			if (o[0] != null && o[0] != "") {
				model.setM1count(o[0].toString());
			}
			if (o[1] != null && o[1] != "") {
				model.setM2count(o[1].toString());
			}
			if (o[2] != null && o[2] != "") {
				model.setM3count(o[2].toString());
			}
			if (o[3] != null && o[3] != "") {
				model.setM4count(o[3].toString());
			}
			if (o[4] != null && o[4] != "") {
				model.setM5count(o[4].toString());
			}
			if (o[5] != null && o[5] != "") {
				model.setM6count(o[5].toString());
			}
			if (o[6] != null && o[6] != "") {
				model.setM7count(o[6].toString());
			}
			if (o[7] != null && o[7] != "") {
				model.setM8count(o[7].toString());
			}
			if (o[8] != null && o[8] != "") {
				model.setM9count(o[8].toString());
			}
			if (o[9] != null && o[9] != "") {
				model.setM10count(o[9].toString());
			}
			if (o[10] != null && o[10] != "") {
				model.setM11count(o[10].toString());
			}
			if (o[11] != null && o[11] != "") {
				model.setM12count(o[11].toString());
			}
		}
		String sql2 = "select decode(b.allcount,'0','',(nvl(a.yscount, 0) / b.allcount)) count\n"
				+ "  from (select count(*) yscount\n"
				+ "          from equ_j_failures r\n"
				+ "         where (r.bug_code = '1060' or r.bug_code = '1059' or\n"
				+ "               r.bug_code = '1032')\n"
				+ "           and r.wo_status in (4)\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.isuse = 'Y') a,\n"
				+ "       (select count(*) allcount\n"
				+ "          from equ_j_failures r\n"
				+ "         where (r.bug_code = '1060' or r.bug_code = '1059' or\n"
				+ "               r.bug_code = '1032')\n"
				+ "           and r.wo_status not in (0, 6, 10)\n"
				+ "           and to_char(r.write_date, 'yyyy') = '"
				+ year
				+ "'\n"
				+ "           and r.entreprise_code = '"
				+ enterpriseCode + "'\n" + "           and r.isuse = 'Y') b";
		Object o2 = bll.getSingal(sql2);
		if (o2 != null && o2 != "") {
			model.setCount(o2.toString());
		}
		return model;
	}

}
