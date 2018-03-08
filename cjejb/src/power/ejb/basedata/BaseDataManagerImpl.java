package power.ejb.basedata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;
import power.ejb.message.form.Message;
import power.ejb.system.SysCUl;

@Stateless
public class BaseDataManagerImpl implements BaseDataManager {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public List<SysCUl> getAdminWorker(String enterprisecode) {
		String sql = "select c.*\n"
				+ "  from sys_c_roles a, sys_j_ur b, sys_c_ul c\n"
				+ " where a.role_id = b.role_id\n"
				+ "   and b.worker_id = c.worker_id\n"
				+ "   and a.role_id = 1\n" + "   and a.is_use = 'Y'\n"
				+ "   and b.is_use = 'Y'\n" + "   and c.is_use = 'Y'\n"
				+ "   and b.enterprise_code = ?\n"
				+ "   and a.enterprise_code = b.enterprise_code\n"
				+ "   and c.enterprise_code = b.enterprise_code";
		List<SysCUl> list = bll.queryByNativeSQL(sql,
				new Object[] { enterprisecode }, SysCUl.class);
		return list;
	}

	public boolean checkWorkerIsAdmin(String workerCode, String enterpriseCode) {
		String sql = "select count(1)\n"
				+ "  from sys_c_roles a, sys_j_ur b, sys_c_ul c\n"
				+ " where a.role_id = b.role_id\n"
				+ "   and b.worker_id = c.worker_id\n"
				+ "   and a.role_id = 1\n" + "   and c.worker_code = ?\n"
				+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
				+ "   and c.is_use = 'Y'\n" + "   and b.enterprise_code = ?\n"
				+ "   and a.enterprise_code = b.enterprise_code\n"
				+ "   and c.enterprise_code = b.enterprise_code";
		Object result = bll.getSingal(sql, new Object[] { workerCode,
				enterpriseCode });
		if (result.toString().equals("0")) {
			return false;
		} else {
			return true;
		}
	}

	public List<TreeNode> getDeptsByPid(String code) {
		List<TreeNode> result = null;
		String sql = "";
		if (code.equals("00")) {
			sql += "select a.dep_code,\n" +
				"       a.dep_name,\n" + 
				"       (select decode(count(1), 0, 'Y', 'N')\n" + 
				"          from hr_c_department t\n" + 
				"         where length(t.dep_code) = 2)\n" + 
				"  from hr_c_department a\n" + 
				" where a.if_attend_dep = '1'\n" + 
				"   and length(a.dep_code) = 2";


		} else {
			sql += "select  a.dep_code,a.dep_name,\n"
					+ " (select  decode(count(1), 0, 'Y', 'N')from hr_c_department t where "
					+ " a.dep_code in(select t.dep_code from hr_c_department t where t.if_attend_dep='1'\n"
					+ "start with t.dep_code= '"
					+ code
					+ "' connect by prior t.dep_code like '"
					+ code
					+ "%'\n"
					+ ")\n"
					// +"t.dep_code like '"
					// + code
					// + "%' "
					+ " and length(t.dep_code)=" + (code.length() + 4) + ")\n"
					+ "from hr_c_department a " + " where a.dep_code like '"
					+ code + "%'\n" + "and length(a.dep_code) = "
					+ (code.length() + 2) + " and a.if_attend_dep='1'";

		}

		List list = bll.queryByNativeSQL(sql);

		if (list != null && list.size() > 0) {
			result = new ArrayList<TreeNode>();
			for (int i = 0; i < list.size(); i++) {
				Object[] r = (Object[]) list.get(i);
				TreeNode node = new TreeNode();
				node.setId(r[0].toString());
				if (r[1] != null)
					node.setText(r[1].toString());
				node.setCls(r[2].toString().equals("Y") ? "file" : "folder");
				node.setLeaf(r[2].toString().equals("Y") ? true : false);
				result.add(node);
			}
		}
		return result;
	}

	public PageObject getWorkersByDeptId(String deptCode, String queryKey,
			String notInWorkerCodes, final int... rowStartIdxAndCount) {
		if (notInWorkerCodes == null || "".equals(notInWorkerCodes)) {
			notInWorkerCodes = "' '";
		}
		String sql = "";
		if (queryKey != null && !"".equals(queryKey)) {
			queryKey = "%" + queryKey + "%";
		} else {
			queryKey = "%";
		}
		String sqlCount = "";
		PageObject result = new PageObject();

		if (deptCode.equals("00")) {
			sql += "select distinct(a.worker_code),\n"
					+ "getworkername(a.worker_code),\n"
					+ "a.dep_code,\n"
					+ "getdeptname(a.dep_code)\n"
					+ "from hr_j_employeeinform a\n"
					+ "where a.worker_code not in ("
					+ notInWorkerCodes
					+ ") \n"
					+ " and a.dep_code in ( select t.dep_code from hr_c_department t where t.if_attend_dep='1'\n"
					+ ")\n"
					+ " and (a.worker_code || getworkername(a.worker_code) like ?)\n"
					+ "and a.ryxx_mark='1'";

		} else {
			sql += "select distinct(a.worker_code),\n"
					+ "getworkername(a.worker_code),\n"
					+ "a.dep_code,\n"
					+ "getdeptname(a.dep_code)\n"
					+ "from hr_j_employeeinform a \n"
					+ "where a.worker_code not in ("
					+ notInWorkerCodes
					+ ")\n"
					+ " and a.dep_code in(select b.dep_code from hr_c_department b where a.dep_code in ( select t.dep_code from hr_c_department t where t.if_attend_dep='1'\n"
					+ "start with t.dep_code= '"
					+ deptCode
					+ "' connect by prior t.dep_code like '"
					+ deptCode
					+ "%'\n"
					+ " ))\n"
					+ " and (a.worker_code || getworkername(a.worker_code) like ?)\n"
					+ "and a.ryxx_mark='1'";
		}
		// sql += "select a.worker_code,\n"
		// + "getworkername(a.worker_code),\n"
		// + "b.dep_code,\n"
		// + "getdeptname(b.dep_code)\n"
		// + "from hr_j_employeeinform a ,hr_c_department b\n"
		// + "where a.worker_code not in ("
		// + notInWorkerCodes
		// + ")\n"
		// + "and b.dep_code in = ?\n"
		// + "and (a.worker_code || getworkername(a.worker_code) like ?)\n"
		// + "and b.if_attend_dep='1'\n" + "and a.dep_code=b.dep_code\n"
		// + "and a.ryxx_mark='1'";

		if (deptCode.equals("00")) {
			sqlCount += "select count(distinct(a.worker_code)) from hr_j_employeeinform a ,hr_c_department b\n"
					+ "where a.worker_code not in ("
					+ notInWorkerCodes
					+ ")\n"
					+ " and a.dep_code in ( select t.dep_code from hr_c_department t where t.if_attend_dep='1'\n"
					+ ")\n"
					+ "and (a.worker_code || getworkername(a.worker_code) like ?)\n"
					+ "and a.ryxx_mark='1'";
		} else {
			sqlCount += "select count(a.dep_code) "
					+ "from hr_j_employeeinform a \n"
					+ "where a.worker_code not in ("
					+ notInWorkerCodes
					+ ")\n"
					+ " and a.dep_code in(select b.dep_code from hr_c_department b where a.dep_code in ( select t.dep_code from hr_c_department t where t.if_attend_dep='1'\n"
					+ "start with t.dep_code= '"
					+ deptCode
					+ "' connect by prior t.dep_code like '"
					+ deptCode
					+ "%'\n"
					+ " ))\n"
					+ " and (a.worker_code || getworkername(a.worker_code) like ?)\n"
					+ "and a.ryxx_mark='1'";
		}
		List list = bll.queryByNativeSQL(sql, new Object[] { queryKey },
				rowStartIdxAndCount);
		if (list != null && list.size() > 0) {
			List<Employee> arr = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				Object[] o = (Object[]) list.get(i);
				Employee e = new Employee();
				if (o[0] != null) {
					e.setWorkerCode(o[0].toString());
				}
				if (o[1] != null) {
					e.setWorkerName(o[1].toString());
				}
				if (o[2] != null) {
					e.setDeptCode(o[2].toString());
				}
				if (o[3] != null) {
					e.setDeptName(o[3].toString());
				}
				arr.add(e);
			}
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount,
					new Object[] { queryKey }).toString());
			result.setList(arr);
			result.setTotalCount(totalCount);
			return result;
		} else {
			return null;
		}

	}

	public String getOriCurrency() {
		String sql = "select t.parm_value\n" + "    from SYS_C_PARAMETERS t\n"
				+ "   where t.parm_no = 'ORICUR'\n" + "     and rownum = 1";
		return bll.getSingal(sql).toString();
	}

	public String getIssueType() {
		String sql = "select t.parm_value\n" + "  from SYS_C_PARAMETERS t\n"
				+ " where t.parm_no = 'ISSTYP'\n" + "   and rownum = 1";
		return bll.getSingal(sql).toString();
	}

	public List<TreeNode> getSpecialsByPCode(String enterpriseCode,
			String specialType, String specialCode) {
		String sql = "select t.speciality_code,\n"
				+ "       t.speciality_name,\n"
				+ "(select decode(count(1), 0, 'Y', 'N')\n"
				+ "          from run_c_specials s\n"
				+ "         where s.p_speciality_code = t.speciality_code and s.is_use = 'Y')\n"
				+ "  from run_c_specials t\n" + " where t.is_use = 'Y'\n"
				+ " and t.p_speciality_code='" + specialCode + "'\n";
		String strWhere = "";
		if (enterpriseCode != null && !"".equals(enterpriseCode)) {
			strWhere += " and t.enterprise_code='" + enterpriseCode + "'\n";
		}
		if (specialType != null && !"".equals(specialType)) {
			strWhere += " and t.speciality_type='" + specialType + "'\n";
		}
		strWhere += " order by t.display_no\n";
		sql = sql + strWhere;
		List list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			List<TreeNode> arrList = new ArrayList();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				TreeNode node = new TreeNode();
				if (obj[0] != null)
					node.setId(obj[0].toString());
				if (obj[1] != null)
					node.setText(obj[1].toString());
				node.setCls(obj[2].toString().equals("Y") ? "file" : "folder");
				node.setLeaf(obj[2].toString().equals("Y") ? true : false);
				arrList.add(node);
			}
			return arrList;
		} else {
			return null;
		}
	}

	public void updateParamValue(String parameterNo, String parameterValue) {
		String sql = "update sys_c_parameters p set p.parm_value = ? where p.parm_no = ?";
		bll.exeNativeSQL(sql, new Object[] { parameterValue, parameterNo });
	}

	public Employee getEmployeeInfo(String workerCode) {
		Employee emp = null;
		String sql = "select t.emp_id,\n" + "       t.emp_code,\n"
				+ "       t.chs_name,\n" + "       t.dept_id,\n"
				+ "       d.dept_code,\n"
				+ "       d.dept_name,t.mobile_phone,t.instancy_tel,t.fax\n"
				+ "  from hr_j_emp_info t, hr_c_dept d\n"
				+ " where t.dept_id = d.dept_id(+)\n"
				+ "   and t.emp_code = ?\n" + "   and d.is_use(+) = 'U'\n"
				+ "   and t.emp_state = 'U' ";
		List list = bll.queryByNativeSQL(sql, new Object[] { workerCode });
		if (list != null && list.size() > 0) {
			emp = new Employee();
			Object[] r = (Object[]) list.get(0);
			emp.setWorkerId(Long.parseLong(r[0].toString()));
			emp.setWorkerCode(r[1].toString());
			if (r[2] != null)
				emp.setWorkerName(r[2].toString());
			if (r[3] != null)
				emp.setDeptId(Long.parseLong(r[3].toString()));
			if (r[4] != null)
				emp.setDeptCode(r[4].toString());
			if (r[5] != null)
				emp.setDeptName(r[5].toString());
			if (r[6] != null)
				emp.setMobilePhoneNo(r[6].toString());
			if (r[7] != null)
				emp.setImmobilePhoneNo(r[7].toString());
			if (r[8] != null)
				emp.setElectrographNo(r[8].toString());
		}
		return emp;
	}

	public boolean checkUserRight(String enterpriseCode, String workerCode,
			String password) {
		power.ear.comm.SecurityManager sm = new power.ear.comm.SecurityManager();
		password = sm.GetMD5Str32(sm.GetMD5Str32(password));
		String sql = "select count(1) from sys_c_ul t where t.enterprise_code='"
				+ enterpriseCode
				+ "' and t.worker_code='"
				+ workerCode
				+ "' and t.login_pwd='" + password + "' and t.is_use='Y' ";
		if (Integer.parseInt(bll.getSingal(sql).toString()) > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkIsDianJianYuan(String workerCode) {
		return this.checkInRoles(workerCode, "9999999999");
	}

	private boolean checkInRoles(String workerCode, String roleId) {
		String sql = "select getroleidsbyworkercode(?) from dual";
		Object re = bll.getSingal(sql, new Object[] { workerCode });
		if (re != null) {
			String[] ids = re.toString().split(",");
			for (String id : ids) {
				if (id.equals(roleId))
					return true;
			}
		}
		return false;
	}
}
