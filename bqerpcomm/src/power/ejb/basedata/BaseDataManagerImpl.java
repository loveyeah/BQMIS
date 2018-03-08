package power.ejb.basedata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.KeyValue;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;
import power.ejb.hr.form.MeasureUnit;
import power.ejb.hr.form.Parameter;

@Stateless
public class BaseDataManagerImpl implements BaseDataManager {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public PageObject findUnitList(String queryKey, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		PageObject result = null;
		if (queryKey == null || "".equals(queryKey)) {
			queryKey = "%";
		} else {
			queryKey = "%" + queryKey + "%";
		}
		String sql = "select t.unit_id, t.unit_name, t.unit_alias, t.retrieve_code\n"
				+ "  from bp_c_measure_unit t\n"
				+ " where t.unit_name || t.unit_alias like ?\n"
				+ "   and t.enterprise_code = ?\n"
				+ "   and t.is_used = 'Y' order by t.unit_id";
		List<Object[]> list = bll.queryByNativeSQL(sql, new Object[] {
				queryKey, enterpriseCode }, rowStartIdxAndCount);
		if (list != null && list.size() > 0) {
			result = new PageObject();
			List<MeasureUnit> _list = new ArrayList<MeasureUnit>();
			for (Object[] r : list) {
				MeasureUnit u = new MeasureUnit();
				u.setUnitId(Long.parseLong(r[0].toString()));
				if (r[1] != null) {
					u.setUnitName(r[1].toString());
				}
				if (r[2] != null) {
					u.setUnitAlias(r[2].toString());
				}
				if (r[3] != null) {
					u.setRetrieveCode(r[3].toString());
				}
				_list.add(u);
			}
			result.setList(_list);
			sql = "select count(1)\n" + "  from bp_c_measure_unit t\n"
					+ " where t.unit_name || t.unit_alias like ?\n"
					+ "   and t.enterprise_code = ?\n"
					+ "   and t.is_used = 'Y' order by t.unit_id ";
			result.setTotalCount(Long.parseLong(bll.getSingal(sql,
					new Object[] { queryKey, enterpriseCode }).toString()));
		}
		return result;
	}

	/**
	 * 取得子部门
	 */
	public List<TreeNode> getDeptsByPid(Long pid,String needBanzuCheck) {
		List<TreeNode> result = null;
		String sql = "select t.dept_id,\n" + "       t.dept_name,\n"
				+ "       t.memo,\n"
				+ "       (select decode(count(1), 0, 'Y', 'N')\n"
				+ "          from hr_c_dept d\n"
				+ "         where d.pdept_id = t.dept_id";
		if(needBanzuCheck == null || !needBanzuCheck.equals("yes"))
			sql += " and (d.is_banzu is null or d.is_banzu<>'1')";
		sql += "),t.dept_code\n"
			// add by liuyi 20100507 加上是否班组
			+ "  ,t.is_banzu \n"
		+ "  from hr_c_dept t\n"
				+ " where t.pdept_id = ?\n" + "   and t.is_use = 'Y'\n"//update by sychen 20100831
//				+ " where t.pdept_id = ?\n" + "   and t.is_use = 'U'\n"
                + " and (t.dept_status is null or t.dept_status='0' or t.dept_status='3')\n";
		/*if(needBanzuCheck == null || !needBanzuCheck.equals("yes"))
			sql += " and (t.is_banzu is null or t.is_banzu<>'1')";
		else 
			sql += " and (t.is_banzu is null or t.is_banzu='0' or t.is_banzu='1')";*/
		sql += " and (t.is_banzu is null or t.is_banzu='0' or t.is_banzu='1')";
		sql += " and t.is_use = 'Y'";
		sql += " order by t.order_by, t.dept_id";
		List list = bll.queryByNativeSQL(sql, new Object[] { pid });
		if (list != null && list.size() > 0) {
			result = new ArrayList<TreeNode>();
			for (int i = 0; i < list.size(); i++) {
				Object[] r = (Object[]) list.get(i);
				TreeNode node = new TreeNode();
				node.setId(r[0].toString());
				if (r[1] != null)
					node.setText(r[1].toString());
				if (r[2] != null)
					node.setDescription(r[2].toString());
				node.setCls(r[3].toString().equals("Y") ? "file" : "folder");
				node.setLeaf(r[3].toString().equals("Y") ? true : false);
				if (r[4] != null)
					node.setCode(r[4].toString());
				
				if(r[5] != null)
					node.setHref(r[5].toString());
				result.add(node);
			}
		}
		return result;
	}
	/**
	 * 由部门查询人员(级连查询)
	 * 
	 * @param deptId
	 *            部门编码
	 * @param notInWorkerCodes
	 *            格式为 : '1440','0689'
	 * @return PageObject 其中 list为List<Employee>
	 */
	public PageObject getWorkersByDeptId(Long deptId,Long dept, String flag,String queryKey,
			String notInWorkerCodes, final int... rowStartIdxAndCount) {
		if (notInWorkerCodes == null || "".equals(notInWorkerCodes)) {
			notInWorkerCodes = "' '";
		}
		PageObject object = null;
		List<Employee> result = null;
		List list = null;
		String sql = "";
		if (queryKey != null && !"".equals(queryKey)) {
			queryKey = "%" + queryKey + "%";
		} else {
			queryKey = "%";
		}
		if (deptId.intValue() == -2) {
			// 没有部门和部门被删除的人员
			sql = "select t.emp_code, t.chs_name, t.new_emp_code\n"
					+ "  from hr_j_emp_info t\n"
					+ " where t.emp_code not in ("
					+ notInWorkerCodes
					+ ")\n"
//					+ "   and t.emp_code || t.chs_name like ?\n"
					+ "   and t.emp_code || t.new_emp_code || t.chs_name like ?\n"
					+ "   and (t.dept_id not in\n"
					+ "       (select t.dept_id from hr_c_dept t where t.is_use = 'Y') or\n"//update by sychen 20100831
//					+ "       (select t.dept_id from hr_c_dept t where t.is_use = 'U') or\n"
					+ "       t.dept_id is null) order by t.order_by,t.emp_code ";

			list = bll.queryByNativeSQL(sql, new Object[] { queryKey },
					rowStartIdxAndCount);
			if (list != null && list.size() > 0) {
				object = new PageObject();
				result = new ArrayList<Employee>();
				for (int i = 0; i < list.size(); i++) {
					Object[] o = (Object[]) list.get(i);
					Employee e = new Employee();
					if (o[0] != null) {
						e.setWorkerCode(o[0].toString());
					}
					if (o[1] != null) {
						e.setWorkerName(o[1].toString());
					}
					if(o[2] != null){
						e.setNewEmpCode(o[2].toString());
					}
						
					result.add(e);
				}
				object.setList(result);
				sql = "select count(1)\n"
						+ "  from hr_j_emp_info t\n"
						+ " where t.emp_code not in ("
						+ notInWorkerCodes
						+ ")\n"
//						+ "   and t.emp_code || t.chs_name like ?\n"
						+ "   and t.emp_code || t.new_emp_code || t.chs_name like ?\n"
						+ "   and (t.dept_id not in\n"
						+ "       (select t.dept_id from hr_c_dept t where t.is_use = 'Y') or\n"//update by sychen 20100831
//						+ "       (select t.dept_id from hr_c_dept t where t.is_use = 'U') or\n"
						+ "       t.dept_id is null) ";
				object.setTotalCount(Long.parseLong(bll.getSingal(sql,
						new Object[] { queryKey }).toString()));

			}

		} else {
//			sql = "select a.emp_code, a.chs_name, b.dept_id, b.dept_code, b.dept_name\n"
//					+ "  from hr_j_emp_info a, hr_c_dept b\n"
//					+ " where a.dept_id = b.dept_id\n"
//					+ "   and b.is_use = 'U'\n"
//					+ "   and a.emp_code not in ("
//					+ notInWorkerCodes
//					+ ")\n"
//					+ "   and a.emp_code || a.chs_name like ?\n"
//					+ "   and a.dept_id in (select t.dept_id\n"
//					+ "                       from hr_c_dept t\n"
//					+ "                      where t.is_use = 'U'\n"
//					+ "                      start with t.dept_id = ?\n"
//					+ "                     connect by prior t.dept_id = t.pdept_id)\n"
//					+ " order by b.order_by, a.order_by, a.emp_code";
			//modify by fyyang 090930
			
			//add bysychen 20100818
			String strWhere="";
			if(flag!=null&&flag.equals("deptFilter")&&deptId.toString().equals("0")){
				strWhere="and a.dept_id=?\n";
			}
			else {
				strWhere=" and a.dept_id in (select t.dept_id\n"
					+ "                       from hr_c_dept t\n"
					+ "                      where t.is_use = 'Y'\n"//update by sychen 20100831
//					+ "                      where t.is_use = 'U'\n"
					+ "                      start with t.dept_id = ?\n"
					+ "                     connect by prior t.dept_id = t.pdept_id)\n";
			}

			//add bysychen 20100818 end 
			sql = "select a.emp_code, a.chs_name, b.dept_id, b.dept_code, b.dept_name,a.emp_id, \n"
				+ "a.station_id, \n"
				+ "c.station_name, \n"
				+ "a.station_level, \n"
				+ "d.station_level_name,e.worker_id \n"
				+ " ,a.new_emp_code \n"
				+ " ,b.is_banzu \n"//add by sychen 20100726
					+ "  from hr_j_emp_info a, hr_c_dept b,hr_c_station c,hr_c_station_level d,sys_c_ul e\n"
					+ " where a.dept_id = b.dept_id\n"
					// modified by liuyi 091125 人员可能未在e表中进行维护
//					+" and a.emp_code=e.worker_code(+)  and e.is_use='Y' \n"
					+" and a.emp_code=e.worker_code(+)  and e.is_use(+)='Y' \n"
					+ "   and b.is_use = 'Y'\n"//update by sychen 20100831
//					+ "   and b.is_use = 'U'\n"
					+ "   and a.emp_code not in ("
					+ notInWorkerCodes
					+ ")\n"
//					+ "   and a.emp_code || a.chs_name like ?\n"
					+ "   and a.emp_code || a.new_emp_code || a.chs_name like ?\n"
					
					//update by sychen 20100818
					+strWhere
//					+ "   and a.dept_id in (select t.dept_id\n"
//					+ "                       from hr_c_dept t\n"
//					+ "                      where t.is_use = 'U'\n"
//					+ "                      start with t.dept_id = ?\n"
//					+ "                     connect by prior t.dept_id = t.pdept_id)\n"
					//update by sychen 20100818 end
					
					+ " and a.station_id=c.station_id(+) \n"
					+ " and a.station_level=d.station_level_id(+) \n"
					// modified by liuyi 091130 新增查询过滤条件 在人员基本信息表中的is_use为Y,为新增离职登记维护中过滤人员
					+ "  and a.is_use='Y' \n"
					+ " order by b.order_by, a.order_by, a.emp_code";

			list = bll.queryByNativeSQL(sql, new Object[] { queryKey, (flag!=null&&flag.equals("deptFilter")&&deptId.toString().equals("0")?dept:deptId) },
					rowStartIdxAndCount);

			if (list != null && list.size() > 0) {
				object = new PageObject();
				result = new ArrayList<Employee>();
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
						e.setDeptId(Long.parseLong(o[2].toString()));
					}
					if (o[3] != null) {
						e.setDeptCode(o[3].toString());
					}
					if (o[4] != null) {
						e.setDeptName(o[4].toString());
					}
					if(o[5]!=null)
					{
						e.setEmpId(Long.parseLong(o[5].toString()));
					}
					if(o[6] != null)
						e.setStationId(Long.parseLong(o[6].toString()));
					if(o[7] != null)
						e.setStationName(o[7].toString());
					if(o[8] != null)
						e.setStaionLevel(Long.parseLong(o[8].toString()));
					if(o[9] != null)
						e.setStaionLevelName(o[9].toString());
					if(o[10]!=null)
						e.setWorkerId(Long.parseLong(o[10].toString()));
					if(o[11] != null)
					{
						e.setNewEmpCode(o[11].toString());
					}
					if(o[12] != null)//add by sychen 20100726
					{
						e.setIsClass(o[12].toString());
					}
					result.add(e);
				}
				object.setList(result);
				sql = "select count(1)\n"
						+ "  from hr_j_emp_info a, hr_c_dept b\n"
						+ " where a.dept_id = b.dept_id\n"
						// modified by liuyi 091130 新增查询过滤条件 在人员基本信息表中的is_use为Y,为新增离职登记维护中过滤人员
						+ "  and a.is_use='Y' \n"
						+ "   and b.is_use = 'Y'\n"//update by sychen 20100831
//						+ "   and b.is_use = 'U'\n"
						+ "   and a.emp_code not in ("
						+ notInWorkerCodes
						+ ")\n"
//						+ "   and a.emp_code || a.chs_name like ?\n"
						+ "   and a.emp_code || a.new_emp_code || a.chs_name like ?\n"
						
						//update by sychen 20100826
						+strWhere;
				
				object.setTotalCount(Long.parseLong(bll.getSingal(sql,
						new Object[] { queryKey, (flag!=null&&flag.equals("deptFilter")&&deptId.toString().equals("0")?dept:deptId) }).toString()));
					
//				+ "   and a.dept_id in (select t.dept_id\n"
//						+ "                       from hr_c_dept t\n"
//						+ "                      where t.is_use = 'U'\n"
//						+ "                      start with t.dept_id = ?\n"
//						+ "                     connect by prior t.dept_id = t.pdept_id)\n";
//				object.setTotalCount(Long.parseLong(bll.getSingal(sql,
//						new Object[] { queryKey, deptId }).toString()));

				//update by sychen 20100826 end 
			}
		}
		return object;
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

	public List<Parameter> getParamsList() {
		String sql = "select t.parm_no, t.parm_name, t.parm_value, t.enterprise_code\n"
				+ "  from sys_c_parameters t";
		List list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			List<Parameter> arrlist = new ArrayList();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				Parameter model = new Parameter();
				if (obj[0] != null)
					model.setParmNo(obj[0].toString());
				if (obj[1] != null)
					model.setParmName(obj[1].toString());
				if (obj[2] != null)
					model.setParmValue(obj[2].toString());
				if (obj[3] != null)
					model.setEnterpriseCode(obj[3].toString());
				arrlist.add(model);
			}
			return arrlist;
		} else {
			return null;
		}
	}

	public Employee getEmployeeInfo(String workerCode) {
		Employee emp = null;
		String sql = "select t.emp_id,\n" + "       t.emp_code,\n"
				+ "       t.chs_name,\n" + "       t.dept_id,\n"
				+ "       d.dept_code,\n"
				+ "       d.dept_name,t.mobile_phone,t.instancy_tel,t.fax\n"
				// modified by liuyi 20100407 补充新工号
				+ " ,t.new_emp_code "
				+ "  from hr_j_emp_info t, hr_c_dept d\n"
				+ " where t.dept_id = d.dept_id(+)\n"
//				+ "   and t.emp_code = ?\n" + "   and d.is_use(+) = 'U'\n"
				+ "   and (t.emp_code= ?  or t.new_emp_code = ?) \n" + "   and d.is_use(+) = 'Y'\n"//update by sychen 20100831
//				+ "   and (t.emp_code= ?  or t.new_emp_code = ?) \n" + "   and d.is_use(+) = 'U'\n"
				+ "  and rownum = 1  "; 
//		List list = bll.queryByNativeSQL(sql, new Object[] { workerCode });
		List list = bll.queryByNativeSQL(sql, new Object[] { workerCode,workerCode });
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
			if(r[9] != null)
				emp.setNewEmpCode(r[9].toString());
		}
		return emp;
	}

	// 工号密码验证
	public boolean checkUserRight(String enterpriseCode, String workerCode,
			String password) {
		power.ear.comm.SecurityManager sm = new power.ear.comm.SecurityManager();
		password = sm.GetMD5Str32(sm.GetMD5Str32(password));
		String sql = "select count(1) from sys_c_ul t where t.enterprise_code='"
				+ enterpriseCode
				// modified by liuyi 20100407 新工号也可用
//				+ "' and t.worker_code='"
				+ "' and (t.worker_code='"+workerCode+"' or t.login_code = '"
				+ workerCode+ "')" 
				+" and t.login_pwd='" + password + "' and t.is_use='Y' ";
		if (Integer.parseInt(bll.getSingal(sql).toString()) > 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 判断是否点检员
	 * @param workerCode
	 * @return
	 */
	public boolean checkIsDianJianYuan(String workerCode)
	{
		return this.checkInRoles(workerCode, "8");
	}
	
	
	//判断是否属于某种角色
	private boolean checkInRoles(String workerCode,String roleId)
	{
		String sql = "select getroleidsbyworkercode(?) from dual";
		Object re = bll.getSingal(sql,new Object[]{workerCode});
		if(re!=null)
		{
			String [] ids = re.toString().split(",");
			for(String id: ids)
			{
				if(id.equals(roleId))
					return true;
			}
		}
		return false; 
	}
	
	//获得部门标识和父部门标识 用于工作票票号生成
	public String getDeptAndPdeptIdentifier(String deptCode)
	{
		//modify by fyyang 090401
		String identifier="";
		String sql=
			"select nvl((select a.dept_identifier\n" +
			"             from hr_c_dept a\n" + 
			"            where a.is_use='Y'and  a.dept_id = (select b.pdept_id\n" + //update by sychen 20100831
//			"            where a.is_use='U'and  a.dept_id = (select b.pdept_id\n" + 
			"                                 from hr_c_dept b\n" + 
			"                                where b.dept_code = '"+deptCode+"' and rownum=1 and b.is_use='Y')),\n" + //update by sychen 20100831
//			"                                where b.dept_code = '"+deptCode+"' and rownum=1 and b.is_use='U')),\n" +
			"           'XX')\n" + 
			"\n" + 
			"       || nvl((select t.dept_identifier\n" + 
			"                from hr_c_dept t\n" + 
			"               where t.dept_code = '"+deptCode+"' and rownum=1 and t.is_use='Y'),\n" + //update by sychen 20100831
//			"               where t.dept_code = '"+deptCode+"' and rownum=1 and t.is_use='U'),\n" +
			"              'XX')\n" + 
			"  from dual";

		identifier= bll.getSingal(sql).toString();
		return identifier;
	}
	public String getSpecialityShortName(String specialityCode){
		String shortName="专业";
		String sql=
			"select t.short_name from run_c_specials t where t.speciality_code=? and  rownum=1";
		Object ob=bll.getSingal(sql, new Object[]{specialityCode});
		if(ob!=null && !"".equals(ob.toString())){
			shortName=ob.toString();
		}
		return shortName;
	}
	public String getDeptIdentifierByOpCode(String operatorMan){
		String identifier="部门";
		String sql=
			"select d.dept_identifier from hr_j_emp_info t,hr_c_dept d where t.emp_code=? and t.dept_id=d.dept_id";
		Object ob=bll.getSingal(sql, new Object[]{operatorMan});
		if(ob!=null && !"".equals(ob.toString())){
			identifier=ob.toString();
		} 
		return identifier;
	}
	@SuppressWarnings("unchecked")
	public List<KeyValue> getBpBasicDataByType(String enterpriseCode,String type)
	{
		List<KeyValue> list = null;
		String sql = 
			"select t.code, t.name\n" +
			"  from bp_c_basic_data t\n" + 
			" where t.enterprise_code = ?\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and t.type = ?\n" + 
			" order by t.order_by "; 
		List<Object[]> result = bll.queryByNativeSQL(sql, new Object[]{enterpriseCode,type});
		if(result != null)
		{
			list = new ArrayList<KeyValue> ();
			for(Object[] o: result)
			{
				KeyValue kv = new KeyValue(o[0]==null?"":o[0].toString(),o[1]==null?"":o[1].toString());
				list.add(kv);
			}
		}
		return list; 
	}
	
	
	/**
	 * 判断该部门属于哪类部门 
	 * add by fyyang 090414
	 * @param deptCode
	 * @return String  "FD"---发电分公司 "JX"----检修分公司  "SY"---实业分公司
	 */
	public String checkDeptType(String deptCode)
	{
//		String sql=
//			"select  sys_connect_by_path(dept_code,',')   path\n" +
//			"  from hr_c_dept\n" + 
//			"  where dept_code='"+deptCode+"'\n" + 
//			"  start   with   pdept_id=-1\n" + 
//			"  connect   by   prior   dept_id=pdept_id and is_use='U'";
//
//		Object obj = bll.getSingal(sql);
//		if(obj!=null)
//		{
//			String [] data=obj.toString().split(",");
//			for(int i=0;i<data.length;i++)
//			{
//				if(data[i].equals("01")) return "FD";
//				if(data[i].equals("02")) return "JX";
//				if(data[i].equals("03")) return "SY";
//			}
//		}
		return "JX";
	}
	
	@SuppressWarnings("unchecked")
	public PageObject getWorkersOnlyInDeptId(Long deptId, 
		 final int... rowStartIdxAndCount) {
		
		PageObject object = null;
		List<Employee> result = null;
		List list = null;
		String sql = "";
		// modified by liuyi 20100406 启用新工号
//			sql = "select a.emp_code, a.chs_name, b.dept_id, b.dept_code, b.dept_name,a.emp_id\n"
		sql = "select a.new_emp_code, a.chs_name, b.dept_id, b.dept_code, b.dept_name,a.emp_id\n"
					+ "  from hr_j_emp_info a, hr_c_dept b\n"
					+ " where a.dept_id = b.dept_id\n"
					+ "   and b.is_use = 'Y'\n"//update by sychen 20100831
//					+ "   and b.is_use = 'U'\n"
					// add by liuyi 091125 将人员信息表中is_use为N的过滤掉
					+ " and a.is_use = 'Y' \n"
					+ "   and a.dept_id="+deptId+" \n"
					+ " order by b.order_by, a.order_by, a.emp_code";

			list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);

			if (list != null && list.size() > 0) {
				object = new PageObject();
				result = new ArrayList<Employee>();
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
						e.setDeptId(Long.parseLong(o[2].toString()));
					}
					if (o[3] != null) {
						e.setDeptCode(o[3].toString());
					}
					if (o[4] != null) {
						e.setDeptName(o[4].toString());
					}
					if(o[5]!=null)
					{
						e.setEmpId(Long.parseLong(o[5].toString()));
					}
					result.add(e);
				}
				object.setList(result);
				sql = "select count(1)\n"
						+ "  from hr_j_emp_info a, hr_c_dept b\n"
						+ " where a.dept_id = b.dept_id\n"
						+ "   and b.is_use = 'Y'\n" //update by sychen 20100831
//						+ "   and b.is_use = 'U'\n" 
						+ "   and a.dept_id="+deptId+" \n";
				object.setTotalCount(Long.parseLong(bll.getSingal(sql).toString()));

			
		}
		return object;
	}

	/**
	 * add by liuyi 091219 按查询条件查询所有指标
	 * @param argFuzzy
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getAllItemToCommon(String argFuzzy,String dataTimeType,String itemType,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject result = new PageObject();

		if ("".equals(argFuzzy)) {
			argFuzzy = "";
		}

		String sql = "select t.item_code,t.item_name,t.unit_code,getunitname(t.unit_code) from bp_c_stat_item t \n"
				+ " where (t.item_name like '%" + argFuzzy + "%'\n"
				+ "   or t.item_code like '%" + argFuzzy + "%')"
				+ " and t.is_item='Y'";
		String sqlCount = "select count(*)\n" + "  from bp_c_stat_item t\n"
				+ " where (t.item_name like '%" + argFuzzy + "%'\n"
				+ "   or t.item_code like '%" + argFuzzy + "%')"
				+ "and t.is_item='Y'";
		if (dataTimeType != null && !dataTimeType.equals("")) {
			String str = " and t.data_time_type='" + dataTimeType + "'";
			sql += str;
			sqlCount += str;
		}
		if (itemType != null && !itemType.equals("")) {
			String str = " and t.item_type='" + itemType + "'";
			sql += str;
			sqlCount += str;
		}
		List list = bll.queryByNativeSQL(sql,rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());

		result.setList(list);
		result.setTotalCount(count);

		return result;
	}

	/**
	 * add by liuyi 091219 获得指标树
	 * @param itemCode
	 * @param enterpriseCode
	 * @param searchKey
	 * @return
	 */
	public List<TreeNode> findItemTreeToCommon(String node,String dataTimeType,String itemType,
			String enterpriseCode, String searchKey) {
		List<TreeNode> res = null;
		try {
			String sql = "";
			if(searchKey == null || "".equals(searchKey))
			{
			  sql = "select t.item_code,\n"
					+ "       t.item_name,\n"
					+ "       t.is_item,\n"
					+ "       connect_by_isleaf,t.unit_code,getunitname(t.unit_code),t.item_type \n"
					+ "  from bp_c_stat_item t\n" + "where level = 1\n"
					+ " start with t.parent_item_code = ?\n"
					+ "connect by prior t.item_code = t.parent_item_code\n";
			}else
			{
				sql = "select distinct t.item_code,\n"
					+ "       t.item_name,\n"
					+ "       t.is_item,\n"
					+ "       connect_by_isleaf,t.unit_code,getunitname(t.unit_code),t.item_type \n"
					+ "  from ("+ 
					" select *\n" +
					"  from bp_c_stat_item t\n" + 
					" start with t.item_code || t.item_name || t.retrieve_code like '%"+searchKey+"%'\n" + 
					"connect by prior t.parent_item_code = t.item_code"  
					+") t\n" + "where level = 1\n"
					+ " start with t.parent_item_code = '"+node+"'\n"
					+ "connect by prior t.item_code = t.parent_item_code\n";

			}
			if (dataTimeType != null && !dataTimeType.equals("")) {
				String str = " and t.data_time_type='" + dataTimeType + "'";
				sql += str;
			}
			if (itemType != null && !itemType.equals("")) {
				String str = " and t.item_type='" + itemType + "'";
				sql += str;
			}
			sql += " order by t.order_by ";
			List<Object[]> list = bll.queryByNativeSQL(sql,
					new Object[] { node });
			if (list != null && list.size() > 0) {
				res = new ArrayList<TreeNode>();
				for (Object[] o : list) {
					TreeNode n = new TreeNode();
					n.setId(o[0].toString());
					if (o[1] != null)
						n.setText(o[1].toString());
					String isItem = "N";
					if (o[2] != null)
						isItem = o[2].toString();
					n.setDescription(isItem);
					if (o[3] != null)
						n.setLeaf(o[3].toString().equals("1") ? true : false);
					String icon = "";
					if (isItem.equals("N")) {
						icon = "box";
					} else {
						if (("Y").equals(isItem))
							icon = n.getLeaf() ? "my-iconCls" : "my-iconCls";
						else
							icon = n.getLeaf() ? "file" : "folder";
					}
					n.setIconCls(icon);
					// 单位id
					if (o[4] != null)
						n.setCode(o[4].toString());

					// 单位名称
					if (o[5] != null)
						n.setOpenType(o[5].toString());
					// 指标类型（运行指标还是表值指标）
					if (o[6] != null)
						n.setCls(o[6].toString());

					res.add(n);
				}
			}
			return res;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * add by liuyi 091219 按查询条件查询所有经营指标
	 * @param argFuzzy
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getOperateItemForSelect(String argFuzzy,
			 String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject result = new PageObject();

		if ("".equals(argFuzzy)) {
			argFuzzy = "";
		}

		String sql = "select t.item_code,t.item_name,t.unit_code,getunitname(t.unit_code) from bp_c_item t \n"
				+ " where (t.item_name like '%" + argFuzzy + "%'\n"
				+ "   or t.item_code like '%" + argFuzzy + "%')"
				+ " and t.is_item='Y'";
		String sqlCount = "select count(*)\n" + "  from bp_c_item t\n"
				+ " where (t.item_name like '%" + argFuzzy + "%'\n"
				+ "   or t.item_code like '%" + argFuzzy + "%')"
				+ "and t.is_item='Y'";		
		List list = bll.queryByNativeSQL(sql,rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());

		result.setList(list);
		result.setTotalCount(count);

		return result;
	}

	/**
	 * add by liuyi 091219 获得经营指标树
	 * @param itemCode
	 * @param enterpriseCode
	 * @param searchKey
	 * @return
	 */
	public List<TreeNode> findOperateItemTreeForSelect(String node,
			String enterpriseCode, String searchKey) {
		List<TreeNode> res = null;
		try {
			String sql = "";
			if(searchKey == null || "".equals(searchKey))
			{
			  sql = "select t.item_code,\n"
					+ "       t.item_name,\n"
					+ "       t.is_item,\n"
					+ "       connect_by_isleaf,t.unit_code,getunitname(t.unit_code) \n"
					+ "  from bp_c_item t\n" + "where level = 1\n"
					+ " start with t.parent_item_code = ?\n"
					+ "connect by prior t.item_code = t.parent_item_code\n";
			}else
			{
				sql = "select distinct t.item_code,\n"
					+ "       t.item_name,\n"
					+ "       t.is_item,\n"
					+ "       connect_by_isleaf,t.unit_code,getunitname(t.unit_code) \n"
					+ "  from ("+ 
					" select *\n" +
					"  from bp_c_item t\n" + 
					" start with t.item_code || t.item_name || t.retrieve_code like '%"+searchKey+"%'\n" + 
					"connect by prior t.parent_item_code = t.item_code"  
					+") t\n" + "where level = 1\n"
					+ " start with t.parent_item_code = '"+node+"'\n"
					+ "connect by prior t.item_code = t.parent_item_code\n";

			}
			
			sql += " order by t.order_by ";
			List<Object[]> list = bll.queryByNativeSQL(sql,
					new Object[] { node });
			if (list != null && list.size() > 0) {
				res = new ArrayList<TreeNode>();
				for (Object[] o : list) {
					TreeNode n = new TreeNode();
					n.setId(o[0].toString());
					if (o[1] != null)
						n.setText(o[1].toString());
					String isItem = "N";
					if (o[2] != null)
						isItem = o[2].toString();
					n.setDescription(isItem);
					if (o[3] != null)
						n.setLeaf(o[3].toString().equals("1") ? true : false);
					String icon = "";
					if (isItem.equals("N")) {
						icon = "box";
					} else {
						if (("Y").equals(isItem))
							icon = n.getLeaf() ? "my-iconCls" : "my-iconCls";
						else
							icon = n.getLeaf() ? "file" : "folder";
					}
					n.setIconCls(icon);
					// 单位id
					if (o[4] != null)
						n.setCode(o[4].toString());

					// 单位名称
					if (o[5] != null)
						n.setOpenType(o[5].toString());					

					res.add(n);
				}
			}
			return res;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/**
	 * 删除工作流信息
	 * add by fyyang 090727
	 * @param workflowNo
	 */
	public void deleteWf(Long workflowNo)
	{ 	
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		String wfSql =  "delete from wf_c_currentstep a where a.entry_id="+workflowNo+";\n" +
				"delete from wf_j_rrs_cr b where b.entry_id="+workflowNo+";\n" + 
				"delete from wf_j_rrs_cp c where c.entry_id="+workflowNo+";\n" + 
				"insert into wf_j_historyoperation(id,entry_id,step_id,step_name,action_id,action_name,caller,caller_name,opinion,opinion_time)\n" + 
				"values((select nvl(max(id),0)+1 from wf_j_historyoperation),"+workflowNo+",-1,' ',-1,' ','999999','管理员','管理员删除',sysdate);" ;
		sb.append(wfSql);
		sb.append("commit;\n");
		sb.append("end;\n");
		bll.exeNativeSQL(sb.toString());
	}
	/**
	 * 根据部门Id查询一级部门id，编码，名称
	 * @param depid
	 * @return List
	 * add by kzhang20100917
	 */
	public List getFirstLeverDeptByDeptId(String depid)
	{
		String Sql=
			" select c.dept_id, c.dept_code, c.dept_name\n" +
			"         from hr_c_dept c\n" + 
			"        where c.dept_code = getfirstlevelbyid("+depid+")\n" + 
			"          and c.is_use = 'Y'\n";
		 return bll.queryByNativeSQL(Sql);
	}
}
