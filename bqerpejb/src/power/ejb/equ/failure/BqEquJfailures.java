package power.ejb.equ.failure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class BqEquJfailures implements BqEquJfailuresRemote {
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 查询缺陷列表
	 * 
	 * @param status
	 * @param entrepriseCode
	 * @param start
	 * @param limit
	 * @param belongBlock
	 * @param dominationProfession
	 * @param repairDep
	 * @return PageObject
	 */

	@SuppressWarnings("unchecked")
	public PageObject findListByStatus(String status, String entrepriseCode,
			String sdate, String edate, int start, int limit,
			String belongBlock, String dominationProfession, String repairDep,
			String workerCode) {
		try {
			long count = 0;
			if (belongBlock.equals("")) {
				belongBlock = "like '%'";
			} else {
				belongBlock = " = '" + belongBlock + "'";
			}
			String strSql = "select count(1)"
					+ "  from equ_j_failures t, equ_c_failure_type t1\n"
					+ " where t.isuse = 'Y'\n" + "   and t1.is_use = 'Y'\n"
					+ "   and t.failuretype_code = t1.failuretype_code\n"
					+ "   and t.write_by like '%" + workerCode + "'\n"
					+ "   and t.domination_profession like '%"
					+ dominationProfession + "%' and t.repair_dep like '%"
					+ repairDep + "%'\n";
			if (sdate != null || edate != null) {
				strSql += "   and t.write_date >=to_date('" + sdate
						+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
						+ "   and t.write_date <=to_date('" + edate
						+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
			}
			strSql += "   and t.wo_status in (" + status
					+ ") and t.entreprise_code = '" + entrepriseCode
					+ "' and t.belong_system " + belongBlock;
			Object objCount = bll.getSingal(strSql);
			if (objCount != null) {
				count = Long.parseLong(objCount.toString());
			}

			strSql = // "select * from (" +
				"select t.id,\n" +
				"       t.failure_code,\n" + 
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
				"              '已处理',\n" + 
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
				"              '检修部门主管延期退回',\n" + 
				"              '16',\n" + 
				"              '设备部延期待处理退回',\n" + 
				"              '18',\n" + 
				"              '已确认待消缺',\n" + 
				"              '20',\n" + 
				"              '总工审批',\n" + 
				"              '21',\n" + 
				"              '总工延期待处理审批退回',\n" + 
				"              '17',\n" + 
				"              '运行延期待处理退回') wo_status_text,\n" + 
				"       t1.failuretype_name,\n" + 
				"       t1.failure_pri,\n" + 
				"       t.attribute_code,\n" + 
				"       t.equ_name,\n" + 
				"       t.failure_content,\n" + 
				"       t.location_code,\n" + 
				"       t.location_desc,\n" + 
				"       to_char(t.find_date, 'yyyy-mm-dd hh24:mi:ss') find_date,\n" + 
				"       t.find_by,\n" + 
				"       t.find_dept,\n" + 
				"       t.wo_code,\n" + 
				"       t.bug_code,\n" + 
				"       t.failuretype_code,\n" + 
				"       t.failure_level,\n" + 
				"       t.wo_status,\n" + 
				"       t.pre_content,\n" + 
				"       t.if_stop_run,\n" + 
				"       decode(t.if_stop_run, 'Y', '是', 'N', '否') if_stop_run_text,\n" + 
				"       t.run_profession,\n" + 
				"       t.domination_profession,\n" + 
				"       t.repair_dep,\n" + 
				"       t.installation_code,\n" + 
				"       t.installation_desc,\n" + 
				"       t.belong_system,\n" + 
				"       t.likely_reason,\n" + 
				"       t.wo_priority,\n" + 
				"       t.write_by,\n" + 
				"       t.write_dept,\n" + 
				"       to_char(t.write_date, 'yyyy-mm-dd hh24:mi:ss') write_date,\n" + 
				"       t.repair_dept,\n" + 
				"       t.realrepair_dept,\n" + 
				"       t.if_open_workorder,\n" + 
				"       decode(t.if_open_workorder, 'Y', '是', 'N', '否') if_open_workorder_text,\n" + 
				"       t.if_repeat,\n" + 
				"       decode(t.if_repeat, 'Y', '是', 'N', '否') if_repeat_text,\n" + 
				"       t.supervisor,\n" + 
				"       t.work_flow_no,\n" + 
				"       t.wf_state,\n" + 
				"       t.entreprise_code,\n" + 
				"       t.isuse,\n" + 
				"       (select t2.bug_name\n" + 
				"          from equ_c_bug t2\n" + 
				"         where t2.is_use = 'Y'\n" + 
				"           and t2.bug_code = t.bug_code and rownum=1) bug_name,\n" + 
				"       getworkername(t.find_by) find_by_name,\n" + 
				"       getdeptname(t.find_dept) find_dept_name,\n" + 
				"       getworkername(t.write_by) write_by_name,\n" + 
				"       getdeptname(t.write_dept) write_dept_name,\n" + 
				"       (select t5.block_name\n" + 
				"          from equ_c_block t5\n" + 
				"         where t5.is_use = 'Y'\n" + 
				"           and t5.block_code = t.belong_system and rownum=1) belong_system_text,\n" + 
				"       getdeptname(t.repair_dep) repair_dep_text,\n" + 
				"       getspecialname(t.domination_profession) domination_profession_text,\n" + 
				"       t.is_tel,\n" + 
				"       t.is_message,\n" + 
				"       t.tel_man,\n" + 
				"       getworkername(t.tel_man) tel_man_name,\n" + 
				"       to_char(t.tel_time, 'yyyy-mm-dd hh24:mi:ss') tel_time,\n" + 
				"       t.is_check,\n" + 
				"       row_number() over(order by t.failure_code desc) r \n"
					+ "  from equ_j_failures t, equ_c_failure_type t1\n"
					+ " where t.isuse = 'Y'\n" + "   and t1.is_use = 'Y'\n"
					+ "   and t.failuretype_code = t1.failuretype_code\n"
					+ "   and t.write_by like '%" + workerCode + "'\n"
					+ "   and t.domination_profession like '%"
					+ dominationProfession + "%' and t.repair_dep like '%"
					+ repairDep + "%'\n";

			if (sdate != null || edate != null)
				strSql += "   and t.write_date >=to_date('" + sdate
						+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
						+ "   and t.write_date <=to_date('" + edate
						+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
			strSql += "   and t.wo_status in (" + status
					+ ") and t.entreprise_code = '" + entrepriseCode
					+ "' and t.belong_system " + belongBlock;
			// + ") a where a.r>"
			// + start + " and a.r<=" + (start + limit);

			PageObject pobj = new PageObject();
			List list = bll.queryByNativeSQL(strSql, start, limit);
			List<EquFailuresInfo> flist = new ArrayList();
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
				if (obj[51] != null) {
					form.setIsTel(obj[51].toString());
				} else {
					form.setIsTel("");
				}
				if (obj[52] != null) {
					form.setIsMessage(obj[52].toString());
				} else {
					form.setIsMessage("");
				}
				if (obj[53] != null) {
					form.setTelMan(obj[53].toString());
				} else {
					form.setTelMan("");
				}
				if (obj[54] != null) {
					form.setTelManName(obj[54].toString());
				} else {
					form.setTelManName("");
				}
				if (obj[55] != null) {
					form.setTelTime(obj[55].toString());
				} else {
					form.setTelTime("");
				}
				if (obj[56] != null) {
					form.setIsCheck(obj[56].toString());
				}
				flist.add(form);
			}
			pobj.setList(flist);
			pobj.setTotalCount(count);
			return pobj;
		} catch (RuntimeException re) {
			LogUtil.log("find instance failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询缺陷审批列表
	 * 
	 * @author lyu
	 * @param status
	 * @param entrepriseCode
	 * @param start
	 * @param limit
	 * @param belongBlock
	 * @param specialityCode
	 * @param deptCode
	 * @return PageObject
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findApproveList(String workflowno, String entrepriseCode,
			String sdate, String edate, String status, int start, int limit,
			String belongBlock, String specialityCode, String deptCode,
			String whereDep, String whereProfession) throws ParseException {
		LogUtil.log("finding approve EquJFailure instances", Level.INFO, null);
		try {
			long count = 0;
			String sqlWhere = "";
			if (belongBlock.equals("")) {
				belongBlock = "like '%'";
			} else {
				belongBlock = " = '" + belongBlock + "'";
			}
			if (!("%").equals(status)) {
				// status = " = '" + status + "'";
//				 if("14".equals(status))
//				 {
//					 sqlWhere=" and t.wo_status="+status+" and t.run_profession = '"+whereProfession+"'";
//						 sqlWhere=" and t.wo_status="+status+"";//暂不加权限控制
//				}
				// else if("1".equals(status) || "5".equals(status)
				// ||"8".equals(status) ||"15".equals(status)
				// ||"18".equals(status))
				// {
				// //sqlWhere=" and t.wo_status="+status+" and t.repair_dep =
				// '"+whereDep+"'";
				// sqlWhere=" and t.wo_status="+status+"";
				// }
				if ("15".equals(status))// 待处理退回
				{
					sqlWhere = " and t.wo_status in (15,16,17,21)";
				} 
				else if ("5".equals(status))// 已处理
				{
					sqlWhere = " and t.wo_status in (5,19,22)";
				}else {
					sqlWhere = " and t.wo_status=" + status + "";
				}
			} else {
				// sqlWhere=" and (t.wo_status in (2,3, 7,9, 11, 12, 13, 16,
				// 17,20,21) or (t.wo_status = 14) or (t.wo_status in (1, 5, 8,
				// 15,18)))";
				// sqlWhere=" and (t.wo_status in (3, 7, 11, 12, 13, 16, 17) or
				// (t.wo_status = 14 and t.run_profession =
				// '"+whereProfession+"') or (t.wo_status in (1, 5, 8, 15) and
				// t.repair_dep = '"+whereDep+"'))";
				sqlWhere = " and t.wo_status in (1,2,3,5,7,8,9,11,12,13,14,15,16,17,18,20,21)";
			}
			String strSql = "select count(1)"
					+ "  from equ_j_failures t, equ_c_failure_type t1\n"
					+ " where t.isuse = 'Y'\n" + " and t.work_flow_no in ("
					+ workflowno + ") " + sqlWhere
					+ " and t1.is_use(+) = 'Y'\n"
					+ "   and t.failuretype_code = t1.failuretype_code(+)\n"
					+ "   and t.write_date >=to_date('" + sdate
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and t.write_date <=to_date('" + edate
					+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ " and t.entreprise_code = '" + entrepriseCode
					+ "' and t.belong_system " + belongBlock
					+ "   and t.domination_profession like '%" + specialityCode
					+ "%' and t.repair_dep like '%" + deptCode + "%'\n";
			Object objCount = bll.getSingal(strSql);
			if (objCount != null) {
				count = Long.parseLong(objCount.toString());
			}

			
			
			strSql = 
				"select t.id,\n" + 
				"               t.failure_code,\n" + 
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
				"                      '已处理',\n" + 
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
				"                      '运行延期待处理退回') wo_status_text,\n" + 
				"               t1.failuretype_name,\n" + 
				"               t1.failure_pri,\n" + 
				"               t.attribute_code,\n" + 
				"               t.equ_name,\n" + 
				"               t.failure_content,\n" + 
				"               t.location_code,\n" + 
				"               t.location_desc,\n" + 
				"               to_char(t.find_date, 'yyyy-mm-dd hh24:mi:ss') find_date,\n" + 
				"               t.find_by,\n" + 
				"               t.find_dept,\n" + 
				"               t.wo_code,\n" + 
				"               t.bug_code,\n" + 
				"               t.failuretype_code,\n" + 
				"               t.failure_level,\n" + 
				"               t.wo_status,\n" + 
				"               t.pre_content,\n" + 
				"               t.if_stop_run,\n" + 
				"               decode(t.if_stop_run, 'Y', '是', 'N', '否') if_stop_run_text,\n" + 
				"               t.run_profession,\n" + 
				"               t.domination_profession,\n" + 
				"               t.repair_dep,\n" + 
				"               t.installation_code,\n" + 
				"               t.installation_desc,\n" + 
				"               t.belong_system,\n" + 
				"               t.likely_reason,\n" + 
				"               t.wo_priority,\n" + 
				"               t.write_by,\n" + 
				"               t.write_dept,\n" + 
				"               to_char(t.write_date, 'yyyy-mm-dd hh24:mi:ss') write_date,\n" + 
				"               t.repair_dept,\n" + 
				"               t.realrepair_dept,\n" + 
				"               t.if_open_workorder,\n" + 
				"               decode(t.if_open_workorder, 'Y', '是', 'N', '否') if_open_workorder_text,\n" + 
				"               t.if_repeat,\n" + 
				"               decode(t.if_repeat, 'Y', '是', 'N', '否') if_repeat_text,\n" + 
				"               t.supervisor,\n" + 
				"               t.work_flow_no,\n" + 
				"               t.wf_state,\n" + 
				"               t.entreprise_code,\n" + 
				"               t.isuse,\n" + 
				"               (select t2.bug_name\n" + 
				"                  from equ_c_bug t2\n" + 
				"                 where t2.is_use = 'Y'\n" + 
				"                   and t2.bug_code = t.bug_code and rownum=1) bug_name,\n" + 
				"               getworkername(t.find_by) find_by_name,\n" + 
				"               getdeptname(t.find_dept) find_dept_name,\n" + 
				"               getworkername(t.write_by) write_by_name,\n" + 
				"               getdeptname(t.write_dept) write_dept_name,\n" + 
				"               (select t5.block_name\n" + 
				"                  from equ_c_block t5\n" + 
				"                 where t5.is_use = 'Y'\n" + 
				"                   and t5.block_code = t.belong_system and rownum=1) belong_system_text,\n" + 
				"               getdeptname(t.repair_dep) repair_dep_text,\n" + 
				"               getspecialname(t.domination_profession) domination_profession_text,\n" + 
				"               to_char(t.cliam_date, 'yyyy-mm-dd hh24:mi:ss') cliam_date,\n" + 
				"               (select to_char(x.delay_date, 'yyyy-mm-dd hh24:mi:ss')\n" + 
				"                  from equ_j_failure_history x\n" + 
				"                 where x.id = (select max(r.id)\n" + 
				"                                 from equ_j_failure_history r\n" + 
				"                                where r.approve_type in ('5', '19', '22')\n" + 
				"                                  and r.failure_code = t.failure_code\n" + 
				"                                  and r.enterprise_code = t.entreprise_code\n" + 
				"                                  and r.isuse = 'Y')) delay_date,\n" + 
				"       decode(t.wo_status,\n" + 
				"              '0',\n" + 
				"              '未上报',\n" + 
				"              '1',\n" + 
				"              '待消缺',\n" + 
				"              '7',\n" + 
				"              '设备部仲裁',\n" + 
				"              '2',\n" + 
				"              '待确认',\n" + 
				"              '3',\n" + 
				"              '待验收',\n" + 
				"              '8',\n" + 
				"              '待消缺',\n" + 
				"              '11',\n" + 
				"              '延期待处理',\n" + 
				"              '12',\n" + 
				"              '延期待处理',\n" + 
				"              '13',\n" + 
				"              '延期待处理',\n" + 
				"              '15',\n" + 
				"              '延期待处理退回',\n" + 
				"              '16',\n" + 
				"              '延期待处理退回',\n" + 
				"              '17',\n" + 
				"              '延期待处理退回',\n" + 
				"              '5',\n" + 
				"              '待消缺',\n" + 
//				"              '19',\n" + 
//				"              '待处理信息',\n" + 
				"              '20',\n" + 
				"              '延期待处理',\n" + 
				"              '21',\n" + 
				"              '延期待处理退回',\n" + 
//				"              '22',\n" + 
//				"              '待处理信息',\n" + 
//				"              '23',\n" + 
//				"              '待处理信息',\n" + 
				"              '9',\n" + 
				"              '待消缺',\n" + 
				"              '14',\n" + 
				"              '待验收',\n" + 
				"              '4',\n" + 
				"              '运行已验收',\n" + 
				"              '6',\n" + 
				"              '已作废',\n" + 
				"              '18',\n" + 
				"              '待消缺',\n" + 
				"              '10',\n" + 
				"              '退回') group_name,\n" + 
				"               row_number() over(order by t.failure_code desc) r \n"
					+ "  from equ_j_failures t, equ_c_failure_type t1\n"
					+ " where t.isuse = 'Y'\n" + "   and t1.is_use(+) = 'Y'\n"
					+ "   and t.failuretype_code = t1.failuretype_code(+)\n"
					+ "   and t.write_date >=to_date('"
					+ sdate
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and t.write_date <=to_date('"
					+ edate
					+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ sqlWhere
					+ ""
					+ "   and t.domination_profession like '%"
					+ specialityCode
					+ "%' and t.repair_dep like '%"
					+ deptCode
					+ "%'\n"
					+ " and t.work_flow_no in ("
					+ workflowno
					+ ") and t.entreprise_code = '"
					+ entrepriseCode
					+ "' and t.belong_system "
					+ belongBlock
					+ " order by group_name,t.failuretype_code,t.id desc";
			String str = "";
			PageObject pobj = new PageObject();
			List list = bll.queryByNativeSQL(strSql,start,limit);
			List<EquFailuresInfo> flist = new ArrayList();
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
				if (obj[51] != null) {
					form.setCliamDate(obj[51].toString());
				} else {
					form.setCliamDate("");
				}
				if (obj[52] != null) {
					if (checkIn(form.getWoStatus(),"1,5,8,15,16,17,18,21")
							) {
						form.setToEliminDate(obj[52].toString());
					} else {
						form.setToEliminDate("");
					}
				} else {
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					if(form.getCliamDate() !="" && form.getCliamDate() != null)
					{	
						Date claimDate = format.parse(form.getCliamDate());
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(claimDate);
						if (checkIn(form.getWoStatus(),"1,5,8,15,16,17,18,21")) {
							if (form.getFailuretypeCode().equals("2")) {
								calendar.set(Calendar.DAY_OF_MONTH, calendar
										.get(Calendar.DAY_OF_MONTH) + 3);
								form.setToEliminDate(format.format(calendar
										.getTime()));
							} else if (form.getFailuretypeCode().equals("3")) {
								calendar.set(Calendar.DAY_OF_MONTH, calendar
										.get(Calendar.DAY_OF_MONTH) + 1);
								form.setToEliminDate(format.format(calendar
										.getTime()));
							} else {
								calendar.set(Calendar.DAY_OF_MONTH, calendar
										.get(Calendar.DAY_OF_MONTH) + 1);
								form.setToEliminDate(format.format(calendar
										.getTime()));
								//form.setToEliminDate("");
							}
						} else {
							form.setToEliminDate("");
						}
					}
					else
					{
						form.setToEliminDate("");
					}
				}
				if (obj[53] != null) {
					form.setGroupName(obj[53].toString());
				} else {
					form.setGroupName("");
				}
				flist.add(form);
			}
			pobj.setList(flist);
			pobj.setTotalCount(count);
			return pobj;
		} catch (RuntimeException re) {
			LogUtil.log("find instance failed", Level.SEVERE, re);
			throw re;
		}
	}

	private boolean checkIn(String value, String args) {
		if (args != null && args.length() > 0) {
			String[] array = args.split(",");
			for (int i = 0; i < array.length; i++) { 
				if (value.equals(array[i])) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 缺陷查询时取得各种状态的累计数
	 * @param sdate
	 * @param edate
	 * @param status
	 * @param entrepriseCode 
	 * @param belongBlock
	 * @param specialityCode
	 * @param deptCode
	 * @return
	 */
	public List<Object[]> getToptipMsg(String sdate, String edate,
			String status, String entrepriseCode,
			String belongBlock, String specialityCode, String deptCode)
	{
		if (belongBlock.equals("")) {
			belongBlock = "like '%'";
		} else {
			belongBlock = " = '" + belongBlock + "'";
		}
		if (!("%").equals(status)) {
//			if (status.equals("15"))// 待处理退回
//			{
//				status = " in (15,16,17,21)";
//			} 
//			else if (status.equals("5"))// 已处理
//			{
//				status = " in (5,19,22)";
//			} 
//			else {
//				status = " = '" + status + "'";
//			}
			if(status.equals("report"))//未上报
			{
				status = " = '0'";
			}
			else if(status.equals("confirm"))//待确认
			{
				status = " = '2'";
			}
			else if(status.equals("eliminate"))//待消缺
			{
				status = " in (1,18,9,8,15,16,17,21)";
			}
			else if(status.equals("arbitrate"))//仲裁中
			{
				status = " = '7'";
			}
			else if(status.equals("await"))//待延期
			{
				status = " in (11,12,13,20)";
			}
			else if(status.equals("awaitEnd"))//待延期
			{
				status = " in (5,19,22)";
			}
			else if(status.equals("check"))//待验收
			{
				status = " in (3,14)";
			}
			else if(status.equals("end"))//已结束
			{
				status = " = '4'";
			}
			else if(status.equals("invalid"))//已作废
			{
				status = " = '6'";
			}
			else if(status.equals("all"))//已作废
			{
				status = "like '%'";
			}

		} else {

			status = "like '%'";
		}
		String sql = "select count(1), group_name\n"
				+ "  from (select decode(t.wo_status,\n"
				+ "                      '0',\n"
				+ "                      '未上报',\n"
				+ "                      '1',\n"
				+ "                      '待消缺',\n"
				+ "                      '7',\n"
				+ "                      '设备部仲裁',\n"
				+ "                      '2',\n"
				+ "                      '待确认',\n"
				+ "                      '3',\n"
				+ "                      '待验收',\n"
				+ "                      '8',\n"
				+ "                      '待消缺',\n"
				+ "                      '11',\n"
				+ "                      '待延期',\n"
				+ "                      '12',\n"
				+ "                      '待延期',\n"
				+ "                      '13',\n"
				+ "                      '待延期',\n"
				+ "                      '15',\n"
				+ "                      '待消缺',\n"
				+ "                      '16',\n"
				+ "                      '待消缺',\n"
				+ "                      '17',\n"
				+ "                      '待消缺',\n"
				+ "                      '5',\n"
				+ "                      '已延期',\n"
				+ "                      '20',\n"
				+ "                      '待延期',\n"
				+ "                      '21',\n"
				+ "                      '待消缺',\n"
				+ "                      '9',\n"
				+ "                      '待消缺',\n"
				+ "                      '14',\n"
				+ "                      '待验收',\n"
				+ "                      '4',\n"
				+ "                      '运行已验收',\n"
				+ "                      '6',\n"
				+ "                      '已作废',\n"
				+ "                      '18',\n"
				+ "                      '待消缺',\n"
				+ "                      '10',\n"
				+ "                      '退回') group_name\n"
				+ "          from equ_j_failures t, equ_c_failure_type t1"
				+ " where t.isuse = 'Y'\n"
				+ "   and t1.is_use(+) = 'Y'\n"
				+ "   and t.failuretype_code = t1.failuretype_code(+)\n"
				+ "and t.wo_status "
				+ status
				+ "   and t.write_date >=to_date('"
				+ sdate
				+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
				+ "   and t.write_date <=to_date('"
				+ edate
				+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
				+ "   and t.entreprise_code = '"
				+ entrepriseCode
				+ "' and t.belong_system "
				+ belongBlock
				+ "   and t.domination_profession like '%"
				+ specialityCode
				+ "%' and t.repair_dep like '%"
				+ deptCode
				+ "%'\n"
				+ "       )\n"
				+ "group by group_name\n"
				+ "order by decode(group_name,'待消缺',1,'待确认',2,'延期待处理',3,'设备部仲裁',4,'待验收',5,'运行已验收',6,'作废',7,'未上报',8,'已延期',9,100)";
		return bll.queryByNativeSQL(sql);
	}
	
	
	public PageObject queryListByType(String strDate,String  endDate, 
			String enterpriseCode,int  start,int  limit,String type,String queryType)
	{

		
		String strSql = 
			"select t.id,\n" + 
			"               t.failure_code,\n" + 
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
			"                      '运行延期待处理退回') wo_status_text,\n" + 
			"               t1.failuretype_name,\n" + 
			"               t1.failure_pri,\n" + 
			"               t.attribute_code,\n" + 
			"               t.equ_name,\n" + 
			"               t.failure_content,\n" + 
			"               t.location_code,\n" + 
			"               t.location_desc,\n" + 
			"               to_char(t.find_date, 'yyyy-mm-dd hh24:mi:ss') find_date,\n" + 
			"               t.find_by,\n" + 
			"               t.find_dept,\n" + 
			"               t.wo_code,\n" + 
			"               t.bug_code,\n" + 
			"               t.failuretype_code,\n" + 
			"               t.failure_level,\n" + 
			"               t.wo_status,\n" + 
			"               t.pre_content,\n" + 
			"               t.if_stop_run,\n" + 
			"               decode(t.if_stop_run, 'Y', '是', 'N', '否') if_stop_run_text,\n" + 
			"               t.run_profession,\n" + 
			"               t.domination_profession,\n" + 
			"               t.repair_dep,\n" + 
			"               t.installation_code,\n" + 
			"               t.installation_desc,\n" + 
			"               t.belong_system,\n" + 
			"               t.likely_reason,\n" + 
			"               t.wo_priority,\n" + 
			"               t.write_by,\n" + 
			"               t.write_dept,\n" + 
			"               to_char(t.write_date, 'yyyy-mm-dd hh24:mi:ss') write_date,\n" + 
			"               t.repair_dept,\n" + 
			"               t.realrepair_dept,\n" + 
			"               t.if_open_workorder,\n" + 
			"               decode(t.if_open_workorder, 'Y', '是', 'N', '否') if_open_workorder_text,\n" + 
			"               t.if_repeat,\n" + 
			"               decode(t.if_repeat, 'Y', '是', 'N', '否') if_repeat_text,\n" + 
			"               t.supervisor,\n" + 
			"               t.work_flow_no,\n" + 
			"               t.wf_state,\n" + 
			"               t.entreprise_code,\n" + 
			"               t.isuse,\n" + 
			"               (select t2.bug_name\n" + 
			"                  from equ_c_bug t2\n" + 
			"                 where t2.is_use = 'Y'\n" + 
			"                   and t2.bug_code = t.bug_code and rownum=1) bug_name,\n" + 
			"               getworkername(t.find_by) find_by_name,\n" + 
			"               getdeptname(t.find_dept) find_dept_name,\n" + 
			"               getworkername(t.write_by) write_by_name,\n" + 
			"               getdeptname(t.write_dept) write_dept_name,\n" + 
			"               (select t5.block_name\n" + 
			"                  from equ_c_block t5\n" + 
			"                 where t5.is_use = 'Y'\n" + 
			"                   and t5.block_code = t.belong_system and rownum=1) belong_system_text,\n" + 
			"               getdeptname(t.repair_dep) repair_dep_text,\n" + 
			"               getspecialname(t.domination_profession) domination_profession_text,\n" + 
			"       decode(t.wo_status,\n" + 
			"              '0',\n" + 
			"              '未上报',\n" + 
			"              '1',\n" + 
			"              '待消缺',\n" + 
			"              '7',\n" + 
			"              '设备部仲裁',\n" + 
			"              '2',\n" + 
			"              '待确认',\n" + 
			"              '3',\n" + 
			"              '待验收',\n" + 
			"              '8',\n" + 
			"              '待消缺',\n" + 
			"              '11',\n" + 
			"              '待延期',\n" + 
			"              '12',\n" + 
			"              '待延期',\n" + 
			"              '13',\n" + 
			"              '待延期',\n" + 
			"              '15',\n" + 
			"              '待消缺',\n" + 
			"              '16',\n" + 
			"              '待消缺',\n" + 
			"              '17',\n" + 
			"              '待消缺',\n" + 
			"              '5',\n" + 
			"              '已延期',\n" + 
//			"              '19',\n" + 
//			"              '待处理信息',\n" + 
			"              '20',\n" + 
			"              '待延期',\n" + 
			"              '21',\n" + 
			"              '待消缺',\n" + 
//			"              '22',\n" + 
//			"              '待处理信息',\n" + 
//			"              '23',\n" + 
//			"              '待处理信息',\n" + 
			"              '9',\n" + 
			"              '待消缺',\n" + 
			"              '14',\n" + 
			"              '待验收',\n" + 
			"              '4',\n" + 
			"              '运行已验收',\n" + 
			"              '6',\n" + 
			"              '已作废',\n" + 
			"              '18',\n" + 
			"              '待消缺',\n" + 
			"              '10',\n" + 
			"              '退回') group_name,\n" + 
			"(select to_char(c.delay_date, 'yyyy-mm-dd hh24:mi:ss')\n" +
			"         from equ_j_failure_history c\n" + 
			"        where c.id = (select max(b.id)\n" + 
			"                        from equ_j_failure_history b\n" + 
			"                       where b.failure_code = t.failure_code\n" + 
			"                         and b.approve_type in (5, 19, 22,'12','13','20','21')\n" + 
			"                         and b.isuse = 'Y'\n" + 
			"                         and b.enterprise_code = t.entreprise_code)) await_time"+
			
			"  ,t.is_send \n" + 
			"          from equ_j_failures t, equ_c_failure_type t1\n" + 
			"         where t.isuse = 'Y'\n" + 
			"           and t1.is_use(+) = 'Y'\n" + 
			"           and t.failuretype_code = t1.failuretype_code(+)\n" + 
			"           and t.write_date >=\n" + 
			"               to_date('"+strDate+"' || ' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
			"           and t.write_date <=\n" + 
			"               to_date('"+endDate+"' || ' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n" + 
			"           and t.entreprise_code = '"+enterpriseCode+"'\n" ;
		
		   
		  
		   if ("bug".equals(type)) {
			   strSql+="and  t.bug_code='"+queryType+"'";
			   		
		   } else if ("runprofession".equals(type)) {
			   strSql+="and  t.run_profession='"+queryType+"'";
		   }else if ("repairdep".equals(type)) {
			   strSql+="and t.repair_dep ='"+queryType+"'";
		   }else if ("status".equals(type)) {
			   strSql+="and  t.wo_status ='"+queryType+"'";
		   }else if ("failuretype".equals(type)) {
			   strSql+="and  t.failuretype_code ='"+queryType+"'";
		   }else if ("finddep".equals(type)) {
			   strSql+="and  t.find_dept ='"+queryType+"'";
		   }else if ("domprofession".equals(type)) {
			   strSql+="and  t.domination_profession ='"+queryType+"'";
		   }
		   strSql+="         order by group_name,t.failuretype_code,t.id  desc" ;
		   long count = 0;
		
			String countSql ="select count(1)  from ("+strSql+")";
		
			Object objCount = bll.getSingal(countSql);
			if (objCount != null) {
				count = Long.parseLong(objCount.toString());
			}
		PageObject pobj = new PageObject();
		List list = bll.queryByNativeSQL(strSql,start,limit);
		List<EquFailuresInfo> flist = new ArrayList();
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
			if(!("0".equals(form.getWoStatus())) && !("6".equals(form.getWoStatus())))
			{
				try {
					form.setIsOverTime(this.isOverTime(form.getFailureCode(), enterpriseCode, form.getFailuretypeCode()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (obj[51] != null) {
				form.setGroupName(obj[51].toString());
			} else {
				form.setGroupName("");
			}
			if(obj[52] != null)
			{
				form.setDelayDate(obj[52].toString());
			}
			else
			{
				form.setDelayDate("");
			}
			
			if(obj[53] != null)
				form.setIsSend(obj[53].toString());
			flist.add(form);
		}
		pobj.setList(flist);
		pobj.setTotalCount(count);
		return pobj;
	
		
		
	}
	

	/**
	 * 根据条件查询缺陷列表（所有）
	 * @param status
	 * @param entrepriseCode
	 * @param start
	 * @param limit
	 * @param belongBlock
	 * @param specialityCode
	 * @param deptCode
	 * @return PageObject
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject queryListByStatus(String stop,String sdate, String edate,
			String status, String entrepriseCode, int start, int limit,
			String belongBlock, String specialityCode, String deptCode,String findDeptId) {
		LogUtil.log("finding approve EquJFailure instances", Level.INFO, null);
		try {
			long count = 0;
			
			if (belongBlock.equals("")) {
				belongBlock = "like '%'";
			} else {
				belongBlock = " = '" + belongBlock + "'";
			}
			//----------add by fyyang 091214--增加停机消除状态查询------------------
			String strStop=
				"\n (select distinct a.failure_code\n" +
				"  from EQU_J_FAILURE_HISTORY a\n" + 
				" where (a.approve_type = '7' and a.arbitrate_type = '6')\n" + 
				"    or (a.approve_type = '8' and a.arbitrate_type = 'Y')\n" + 
				"   and a.isuse = 'Y'\n" + 
				"   and a.enterprise_code = '"+entrepriseCode+"') \n";
           String strWhere="";
           if(status.equals("stop"))
           {
        	   strWhere=" and  t.failure_code in "+strStop;
           }
           else if(status.equals("end")||status.equals("invalid")||status.equals("%"))
           {
        	   strWhere="";
           }
           else
           {
        	   strWhere=" and  t.failure_code not in "+strStop;
           }
			//--------------------------------------------------
			if (!("%").equals(status)) {
//				if (status.equals("15"))// 待处理退回
//				{
//					status = " in (15,16,17,21)";
//				} 
//				else if (status.equals("5"))// 已处理
//				{
//					status = " in (5,19,22)";
//				} 
//				else {
//					status = " = '" + status + "'";
//				}
				if(status.equals("report"))//未上报
				{
					status = " = '0'";
				}
				else if(status.equals("confirm"))//待确认
				{
					status = " = '2'";
				}
				else if(status.equals("eliminate"))//待消缺
				{
					status = " in (1,18,9,8,15,16,17,21)";
				}
				else if(status.equals("arbitrate"))//仲裁中
				{
					status = " = '7'";
				}
				else if(status.equals("await"))//延期审批中
				{
					status = " in (11,12,13,20)";
				}
				else if(status.equals("check"))//待验收
				{
					status = " in (3,14)";
				}
				else if(status.equals("end"))//已结束
				{
					status = " = '4'";
				}
				else if(status.equals("invalid"))//已作废
				{
					status = " = '6'";
				}
				else if(status.equals("awaitEnd"))//已延期
				{
					status = " in (5,19,22)";
				}
				else if(status.equals("all"))//已作废
				{
					status = "like '%'";
				}
				else if(status.equals("stop")) //停机消除
				{
					//add by fyyang 091214
					status = "like '%'";
				}
			} else {

				status = "like '%'";
			}
			String countSql = "select count(*)"
					+ "  from equ_j_failures t, equ_c_failure_type t1\n"
					+ " where t.isuse = 'Y'\n" + "   and t1.is_use(+) = 'Y'\n"
					+ "   and t.failuretype_code = t1.failuretype_code(+)\n"
					+ "and t.wo_status " + status
					+ "   and t.write_date >=to_date('" + sdate
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and t.write_date <=to_date('" + edate
					+ "'||' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n"
					+ "   and t.entreprise_code = '" + entrepriseCode
					+ "' and t.belong_system " + belongBlock
					+ "   and t.domination_profession like '%" + specialityCode
					+ "%' and t.repair_dep like '%" + deptCode + "%'\n";
			countSql+=strWhere; //add by fyyang 091214
			   if(stop !=null && stop.equals("stop")){
				   countSql+=" and t.repair_dep in (select d.dept_code from hr_c_dept d, hr_c_dept_type dt where d.dept_type_id = dt.dept_type_id and d.dept_type_id=5 and d.is_use='Y' and d.dept_status in ('1','3')) \n"; //update by sychen 20100902
//				   countSql+=" and t.repair_dep in (select d.dept_code from hr_c_dept d, hr_c_dept_type dt where d.dept_type_id = dt.dept_type_id and d.dept_type_id=5 and d.is_use='U' and d.dept_status in ('1','3')) \n"; 
				   
			   }else{
				   countSql+=" and t.repair_dep in (select d.dept_code from hr_c_dept d, hr_c_dept_type dt where d.dept_type_id = dt.dept_type_id and d.dept_type_id=5 and d.is_use='Y' and d.dept_status in ('0','3')) \n"; //update by sychen 20100902
//				   countSql+=" and t.repair_dep in (select d.dept_code from hr_c_dept d, hr_c_dept_type dt where d.dept_type_id = dt.dept_type_id and d.dept_type_id=5 and d.is_use='U' and d.dept_status in ('0','3')) \n";
			   }
			   
			   //--------------add by fyyang 20100805----------
			   if(findDeptId!=null&&!findDeptId.equals("")&&!findDeptId.equals("null"))
			   {
				   countSql+=
					   "\n and  "+findDeptId+" in (select hd.dept_id from hr_c_dept hd\n" +
					   "where hd.is_use='Y'\n" + //update by sychen 20100902
//					   "where hd.is_use='U'\n" + 
					   "start with hd.dept_code=t.find_dept\n" + 
					   "connect by prior hd.pdept_id=hd.dept_id)  \n";

			   }
			   //-----------------------------------------------
			   
			Object objCount = bll.getSingal(countSql);
			if (objCount != null) {
				count = Long.parseLong(objCount.toString());
			}
			String strSql = 
				"select t.id,\n" + 
				"               t.failure_code,\n" + 
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
				"                      '运行延期待处理退回') wo_status_text,\n" + 
				"               t1.failuretype_name,\n" + 
				"               t1.failure_pri,\n" + 
				"               t.attribute_code,\n" + 
				"               t.equ_name,\n" + 
				"               t.failure_content,\n" + 
				"               t.location_code,\n" + 
				"               t.location_desc,\n" + 
				"               to_char(t.find_date, 'yyyy-mm-dd hh24:mi:ss') find_date,\n" + 
				"               t.find_by,\n" + 
				"               t.find_dept,\n" + 
				"               t.wo_code,\n" + 
				"               t.bug_code,\n" + 
				"               t.failuretype_code,\n" + 
				"               t.failure_level,\n" + 
				"               t.wo_status,\n" + 
				"               t.pre_content,\n" + 
				"               t.if_stop_run,\n" + 
				"               decode(t.if_stop_run, 'Y', '是', 'N', '否') if_stop_run_text,\n" + 
				"               t.run_profession,\n" + 
				"               t.domination_profession,\n" + 
				"               t.repair_dep,\n" + 
				"               t.installation_code,\n" + 
				"               t.installation_desc,\n" + 
				"               t.belong_system,\n" + 
				"               t.likely_reason,\n" + 
				"               t.wo_priority,\n" + 
				"               t.write_by,\n" + 
				"               t.write_dept,\n" + 
				"               to_char(t.write_date, 'yyyy-mm-dd hh24:mi:ss') write_date,\n" + 
				"               t.repair_dept,\n" + 
				"               t.realrepair_dept,\n" + 
				"               t.if_open_workorder,\n" + 
				"               decode(t.if_open_workorder, 'Y', '是', 'N', '否') if_open_workorder_text,\n" + 
				"               t.if_repeat,\n" + 
				"               decode(t.if_repeat, 'Y', '是', 'N', '否') if_repeat_text,\n" + 
				"               t.supervisor,\n" + 
				"               t.work_flow_no,\n" + 
				"               t.wf_state,\n" + 
				"               t.entreprise_code,\n" + 
				"               t.isuse,\n" + 
				"               (select t2.bug_name\n" + 
				"                  from equ_c_bug t2\n" + 
				"                 where t2.is_use = 'Y'\n" + 
				"                   and t2.bug_code = t.bug_code and rownum=1) bug_name,\n" + 
				"               getworkername(t.find_by) find_by_name,\n" + 
				"               getdeptname(t.find_dept) find_dept_name,\n" + 
				"               getworkername(t.write_by) write_by_name,\n" + 
				"               getdeptname(t.write_dept) write_dept_name,\n" + 
				"               (select t5.block_name\n" + 
				"                  from equ_c_block t5\n" + 
				"                 where t5.is_use = 'Y'\n" + 
				"                   and t5.block_code = t.belong_system and rownum=1) belong_system_text,\n" + 
				"               getdeptname(t.repair_dep) repair_dep_text,\n" + 
				"               getspecialname(t.domination_profession) domination_profession_text,\n" + 
				"       decode(t.wo_status,\n" + 
				"              '0',\n" + 
				"              '未上报',\n" + 
				"              '1',\n" + 
				"              '待消缺',\n" + 
				"              '7',\n" + 
				"              '设备部仲裁',\n" + 
				"              '2',\n" + 
				"              '待确认',\n" + 
				"              '3',\n" + 
				"              '待验收',\n" + 
				"              '8',\n" + 
				"              '待消缺',\n" + 
				"              '11',\n" + 
				"              '待延期',\n" + 
				"              '12',\n" + 
				"              '待延期',\n" + 
				"              '13',\n" + 
				"              '待延期',\n" + 
				"              '15',\n" + 
				"              '待消缺',\n" + 
				"              '16',\n" + 
				"              '待消缺',\n" + 
				"              '17',\n" + 
				"              '待消缺',\n" + 
				"              '5',\n" + 
				"              '已延期',\n" + 
//				"              '19',\n" + 
//				"              '待处理信息',\n" + 
				"              '20',\n" + 
				"              '待延期',\n" + 
				"              '21',\n" + 
				"              '待消缺',\n" + 
//				"              '22',\n" + 
//				"              '待处理信息',\n" + 
//				"              '23',\n" + 
//				"              '待处理信息',\n" + 
				"              '9',\n" + 
				"              '待消缺',\n" + 
				"              '14',\n" + 
				"              '待验收',\n" + 
				"              '4',\n" + 
				"              '运行已验收',\n" + 
				"              '6',\n" + 
				"              '已作废',\n" + 
				"              '18',\n" + 
				"              '待消缺',\n" + 
				"              '10',\n" + 
				"              '退回') group_name,\n" + 
				"(select to_char(c.delay_date, 'yyyy-mm-dd hh24:mi:ss')\n" +
				"         from equ_j_failure_history c\n" + 
				"        where c.id = (select max(b.id)\n" + 
				"                        from equ_j_failure_history b\n" + 
				"                       where b.failure_code = t.failure_code\n" + 
				"                         and b.approve_type in (5, 19, 22,'12','13','20','21')\n" + 
				"                         and b.isuse = 'Y'\n" + 
				"                         and b.enterprise_code = t.entreprise_code)) await_time"+
				
				// add by liuyi 20100312 增加是否上报
				"  ,t.is_send \n" + 
				"          from equ_j_failures t, equ_c_failure_type t1\n" + 
				"         where t.isuse = 'Y'\n" + 
				"           and t1.is_use(+) = 'Y'\n" + 
				"           and t.failuretype_code = t1.failuretype_code(+)\n" + 
				"           and t.wo_status "+status+"\n" + 
				"           and t.write_date >=\n" + 
				"               to_date('"+sdate+"' || ' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n" + 
				"           and t.write_date <=\n" + 
				"               to_date('"+edate+"' || ' 23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n" + 
				"           and t.entreprise_code = '"+entrepriseCode+"'\n" + 
				"           and t.belong_system " + belongBlock + "\n" + 
				"           and t.domination_profession like '%" + specialityCode+"%'\n" + 
				"           and t.repair_dep like '%" + deptCode + "%'\n" ;
			   strSql+=strWhere; ///add by fyyang 091214
			   if(stop !=null && stop.equals("stop")){
				   strSql+=" and t.repair_dep in (select d.dept_code from hr_c_dept d, hr_c_dept_type dt where d.dept_type_id = dt.dept_type_id and d.dept_type_id=5 and d.is_use='Y' and d.dept_status in ('1','3')) \n";//update by sychen 20100902
//				   strSql+=" and t.repair_dep in (select d.dept_code from hr_c_dept d, hr_c_dept_type dt where d.dept_type_id = dt.dept_type_id and d.dept_type_id=5 and d.is_use='U' and d.dept_status in ('1','3')) \n";
				   
			   }else{
				   strSql+=" and t.repair_dep in (select d.dept_code from hr_c_dept d, hr_c_dept_type dt where d.dept_type_id = dt.dept_type_id and d.dept_type_id=5 and d.is_use='Y' and d.dept_status in ('0','3')) \n";//update by sychen 20100902
//				   strSql+=" and t.repair_dep in (select d.dept_code from hr_c_dept d, hr_c_dept_type dt where d.dept_type_id = dt.dept_type_id and d.dept_type_id=5 and d.is_use='U' and d.dept_status in ('0','3')) \n";
			   }
			   if(findDeptId!=null&&!findDeptId.equals("")&&!findDeptId.equals("null"))
			   {
				   strSql+=
					   "\n and  "+findDeptId+" in (select hd.dept_id from hr_c_dept hd\n" +
					   "where hd.is_use='Y'\n" +  //update by sychen 20100902
//					   "where hd.is_use='U'\n" + 
					   "start with hd.dept_code=t.find_dept\n" + 
					   "connect by prior hd.pdept_id=hd.dept_id)  \n";

			   }
			   
			   strSql+="         order by group_name,t.failuretype_code,t.id  desc" ;

			String str = "";
			PageObject pobj = new PageObject();
			List list = bll.queryByNativeSQL(strSql,start,limit);
			List<EquFailuresInfo> flist = new ArrayList();
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
				if(!("0".equals(form.getWoStatus())) && !("6".equals(form.getWoStatus())))
				{
					try {
						form.setIsOverTime(this.isOverTime(form.getFailureCode(), entrepriseCode, form.getFailuretypeCode()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (obj[51] != null) {
					form.setGroupName(obj[51].toString());
				} else {
					form.setGroupName("");
				}
				if(obj[52] != null)
				{
					form.setDelayDate(obj[52].toString());
				}
				else
				{
					form.setDelayDate("");
				}
				
				if(obj[53] != null)
					form.setIsSend(obj[53].toString());
				flist.add(form);
			}
			pobj.setList(flist);
			pobj.setTotalCount(count);
			return pobj;
		} catch (RuntimeException re) {
			throw re;
		}
	}
/*
 * 判断是否超时
 * 
 */
private String isOverTime(String failurecode,String enterprisecode,String failureTypeCode) throws ParseException
{
	String timesql=
		"select (select to_char(a.approve_time, 'yyyy-mm-dd hh24:mi:ss')\n" +
		"          from equ_j_failure_history a\n" + 
		"         where a.failure_code = r.failure_code\n" + 
		"           and a.approve_type = 4\n" + 
		"           and a.isuse = 'Y'\n" + 
		"           and a.enterprise_code = r.enterprise_code) end_time,\n" + 
		"       (select max(to_char(a.approve_time, 'yyyy-mm-dd hh24:mi:ss'))\n" + 
		"          from equ_j_failure_history a\n" + 
		"         where a.failure_code = r.failure_code\n" + 
		"           and a.approve_type = 0\n" + 
		"           and a.isuse = 'Y'\n" + 
		"           and a.enterprise_code = r.enterprise_code) report_time,\n" + 
		"(select to_char(c.delay_date, 'yyyy-mm-dd hh24:mi:ss')\n" +
		"         from equ_j_failure_history c\n" + 
		"        where c.id = (select max(b.id)\n" + 
		"                        from equ_j_failure_history b\n" + 
		"                       where b.failure_code =r.failure_code\n" + 
		"                         and b.approve_type in (5, 19, 22)\n" + 
		"                         and b.isuse = 'Y'\n" + 
		"                         and b.enterprise_code = r.enterprise_code)) await_time"+
//		"       (select max(to_char(b.delay_date, 'yyyy-mm-dd hh24:mi:ss'))\n" + 
//		"          from equ_j_failure_history b\n" + 
//		"         where b.failure_code = r.failure_code\n" + 
//		"           and b.approve_type in (5, 19, 22)\n" + 
//		"           and b.isuse = 'Y'\n" + 
//		"           and b.enterprise_code = r.enterprise_code) await_time\n" + 
		"  from equ_j_failure_history r\n" + 
		" where r.failure_code = '"+failurecode+"'\n" + 
		"   and r.isuse = 'Y'\n" + 
		"   and r.enterprise_code = '"+enterprisecode+"'";
//		"select to_char(r.approve_time, 'yyyy-mm-dd hh24:mi:ss') end_time,\n" +
//		"       (select to_char(a.approve_time, 'yyyy-mm-dd hh24:mi:ss')\n" + 
//		"          from equ_j_failure_history a\n" + 
//		"         where a.failure_code = r.failure_code\n" + 
//		"           and a.approve_type = 0\n" + 
//		"           and a.isuse = 'Y'\n" + 
//		"           and a.enterprise_code = r.enterprise_code) report_time,\n" + 
//		"       (select max(to_char(b.delay_date, 'yyyy-mm-dd hh24:mi:ss'))\n" + 
//		"          from equ_j_failure_history b\n" + 
//		"         where b.failure_code = r.failure_code\n" + 
//		"           and b.approve_type in (5, 19, 22)\n" + 
//		"           and b.isuse = 'Y'\n" + 
//		"           and b.enterprise_code = r.enterprise_code) await_time\n" + 
//		"  from equ_j_failure_history r\n" + 
//		" where r.failure_code = '"+failurecode+"'\n" + 
//		"   and r.approve_type = 4\n" + 
//		"   and r.isuse = 'Y'\n" + 
//		"   and r.enterprise_code = '"+enterprisecode+"'";
	List timelist=bll.queryByNativeSQL(timesql);
	if(timelist.size()>0)
	{
		Object[] o=(Object[])timelist.get(0);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date et=new Date();
		if(o[0] != null &&  o[0]!="")
		{
			et=format.parse(o[0].toString());
		}
		String reportTime=o[1].toString();
			
			Date rt=format.parse(reportTime);
			if(o[2] !=null && o[2]!="")
			{
				String awaitTime=o[2].toString();
				Date at=format.parse(awaitTime);
				if(et.after(at))
				{
					return "Y";
				}
				else
				{
					return "N";
				}
			}
			else
			{
				Calendar ect = Calendar.getInstance();
				ect.setTime(et);
				Calendar rct = Calendar.getInstance();
				rct.setTime(rt);
				if("2".equals(failureTypeCode))
				{
					rct.set(Calendar.DAY_OF_MONTH, rct
							.get(Calendar.DAY_OF_MONTH) + 3);
					if(ect.after(rct))
					{
						return "Y";
					}
					else
					{
						return "N";
					}
				}
				else if("3".equals(failureTypeCode))
				{
					rct.set(Calendar.DAY_OF_MONTH, rct
							.get(Calendar.DAY_OF_MONTH) + 1);
					if(ect.after(rct))
					{
						return "Y";
					}
					else
					{
						return "N";
					}
				}
				
				}
	}
	return "";
}
	/**
	 * 根据id显示缺陷详细信息
	 * 
	 * @param id
	 * @param entrepriseCode
	 */
	@SuppressWarnings("unchecked")
	public PageObject findFailureById(String id, String entrepriseCode) {
		LogUtil.log("finding approve EquJFailure instances", Level.INFO, null);
		try {
			long count = 0;
//			String strSql = "select count(1)"
//					+ "  from equ_j_failures t, equ_c_failure_type t1\n"
//					+ " where t.isuse = 'Y'\n" + "   and t1.is_use(+) = 'Y'\n"
//					+ "   and t.failuretype_code = t1.failuretype_code(+)\n"
//					+ "   and t.id = " + id + " and t.entreprise_code = '"
//					+ entrepriseCode + "'";
//			Object objCount = bll.getSingal(strSql);
//			if (objCount != null) {
//				count = Long.parseLong(objCount.toString());
//			}
			String strSql = "select t.id,\n"
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
					+ "              '点检延期待处理退回',\n"
					+ "              '16',\n"
					+ "              '设备部退回',\n"
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
					+ "       to_char(t.find_date,'yyyy-mm-dd hh24:mi:ss') find_date,\n"
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
					+ "       to_char(t.write_date,'yyyy-mm-dd hh24:mi:ss') write_date,\n"
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
					+ "(select t2.bug_name\n"
					+ "                  from equ_c_bug t2\n"
					+ "                 where t2.is_use = 'Y' and t2.bug_code = t.bug_code and rownum=1) bug_name,"
					+ "	getworkername(t.find_by) find_by_name,\n"
					+ " getdeptname(t.find_dept) find_dept_name, \n"
					+ "	getworkername(t.write_by) write_by_name,\n"
					+ " getdeptname(t.write_dept) write_dept_name, \n"
					+ "(select t5.block_name\n"
					+ "                  from equ_c_block t5\n"
					+ "                 where t5.is_use = 'Y'\n"
					+ "                   and t5.block_code = t.belong_system and rownum=1) belong_system_text,"
					+ " getspecialname(t.run_profession) run_profession_name, \n"
					+ " getspecialname(t.domination_profession) domination_profession_name, \n"
					+ " getdeptname(t.repair_dep) repair_dep_text, \n"
					+ "    to_char(t.cliam_date,'yyyy-mm-dd hh24:mi:ss') cliam_date,\n"
					+ "    t.cliam_by,\n"
					+ "       t.is_tel,\n"
					+ "       t.is_message,\n"
					+ "       t.tel_man,\n"
					+ "       getworkername(t.tel_man) tel_man_name,\n"
					+ "       to_char(t.tel_time,'yyyy-mm-dd hh24:mi:ss') tel_time,t.is_check,\n"
					+ "       getworkername(t.cliam_by) cliam_by_name,\n"
					+ "		row_number() over(order by t.failure_code desc) r"
					+ "  from equ_j_failures t, equ_c_failure_type t1\n"
					+ " where t.isuse = 'Y'\n" + "   and t1.is_use(+) = 'Y'\n"
					+ "   and t.failuretype_code = t1.failuretype_code(+) \n"
					+ "   and t.id = " + id + " and t.entreprise_code = '"
					+ entrepriseCode + "'";
			PageObject pobj = new PageObject();
			List<EquFailuresInfo> flist = new ArrayList();
			EquFailuresInfo form = new EquFailuresInfo();
			List list = bll.queryByNativeSQL(strSql);
			for (int i = 0; i < list.size(); i++) {
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
					form.setRunProfessionName(obj[49].toString());
				} else {
					form.setRunProfessionName("");
				}
				if (obj[50] != null) {
					form.setDominationProfessionName(obj[50].toString());
				} else {
					form.setDominationProfessionName("");
				}
				if (obj[51] != null) {
					form.setRepairDepName(obj[51].toString());
				} else {
					form.setRepairDepName("");
				}
				if (obj[52] != null) {
					form.setCliamDate(obj[52].toString());
				} else {
					form.setCliamDate("");
				}
				if (obj[53] != null) {
					form.setCliamBy(obj[53].toString());
				} else {
					form.setCliamBy("");
				}
				if (obj[54] != null) {
					form.setIsTel(obj[54].toString());
				} else {
					form.setIsTel("");
				}
				if (obj[55] != null) {
					form.setIsMessage(obj[55].toString());
				} else {
					form.setIsMessage("");
				}
				if (obj[56] != null) {
					form.setTelMan(obj[56].toString());
				} else {
					form.setTelMan("");
				}
				if (obj[57] != null) {
					form.setTelManName(obj[57].toString());
				} else {
					form.setTelManName("");
				}
				if (obj[58] != null) {
					form.setTelTime(obj[58].toString());
				} else {
					form.setTelTime("");
				}
				if (obj[59] != null) {
					form.setIsCheck(obj[59].toString());
				}
				if (obj[60] != null) {
					form.setCliamByName(obj[60].toString());
				}
			}
			flist.add(form);
			pobj.setList(flist);
			pobj.setTotalCount(count);
			return pobj;
		} catch (RuntimeException re) {
			LogUtil.log("find instance failed", Level.SEVERE, re);
			throw re;
		}
	}
}
