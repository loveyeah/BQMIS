package power.ejb.hr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.form.Dept;
import power.ejb.hr.form.DrpCommBeanInfo;

/**
 * 部门管理
 * 
 * @author wzhyan
 */
@Stateless
public class HrCDeptFacade implements HrCDeptFacadeRemote {
	// property constants
	public static final String PDEPT_ID = "pdeptId";
	public static final String DEPT_CODE = "deptCode";
	public static final String PDEPT_CODE = "pdeptCode";
	public static final String DEPT_NAME = "deptName";
	public static final String MANGER = "manger";
	public static final String DEPT_TYPE_ID = "deptTypeId";
	public static final String IS_USE = "isUse";
	public static final String MEMO = "memo";
	public static final String RETRIEVE_CODE = "retrieveCode";
	public static final String ORDER_BY = "orderBy";
	public static final String CREATE_BY = "createBy";
	public static final String MODIFIY_BY = "modifiyBy";
	public static final String LOGOFF_BY = "logoffBy";
	public static final String POWER_PLANT_ID = "powerPlantId";

	/**
	 * 班组类别1
	 */
	private static final String BANZU_SORT_1 = "1";
	/**
	 * 班组类别0
	 */
	private static final String BANZU_SORT_0 = "0";
	/**
	 * 空字符串
	 */
	public static final String BLANK_STRING = "";
	/**
	 * 逗号
	 */
	private static final String COMMA = ",";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增部门
	 */
	public void save(HrCDept entity) throws CodeRepeatException {
		try {
			if (checkCodeSameForAdd(entity.getDeptCode())) {
				throw new CodeRepeatException("编码已经存在!");
			}
			if (entity.getDeptTypeId() == 0) {
				throw new CodeRepeatException("部门类别不能为空!");
			}
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据人员查部门名称
	 */
	public String getDeptNameByEmpCode(String empCode) {
		String sql = "select dept.dept_name from hr_c_dept dept where dept.dept_id =(\n"
				+ "select ei.dept_id from hr_j_emp_info ei where ei.chs_name = '"
				+ empCode + "')";
		String deptName = bll.getSingal(sql).toString();
		if (deptName != null) {
			return deptName;
		} else {
			return "";
		}

	}

	private boolean checkCodeSameForAdd(String stationCode) {
		String sql = "select count(1) from hr_c_dept t where t.dept_code=?";
		int size = Integer.parseInt(bll.getSingal(sql,
				new Object[] { stationCode }).toString());
		if (size > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除
	 */
	public void delete(HrCDept entity) {
		try {
			entity = entityManager.getReference(HrCDept.class, entity
					.getDeptId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 保存部门信息
	 */
	public HrCDept update(HrCDept entity) {
		try {
			HrCDept result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCDept findById(Long id) {
		try {
			HrCDept instance = entityManager.find(HrCDept.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	// /**
	// * Find all HrCDept entities with a specific property value.
	// *
	// * @param propertyName
	// * the name of the HrCDept property to query
	// * @param value
	// * the property value to match
	// * @param rowStartIdxAndCount
	// * Optional int varargs. rowStartIdxAndCount[0] specifies the the
	// * row index in the query result-set to begin collecting the
	// * results. rowStartIdxAndCount[1] specifies the the maximum
	// * number of results to return.
	// * @return List<HrCDept> found by query
	// */
	@SuppressWarnings("unchecked")
	private List<HrCDept> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		try {
			final String queryString = "select model from HrCDept model where model."
					+ propertyName + "= :propertyValue order by model.deptName";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
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
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	//
	// public List<HrCDept> findByProperties(String strWhere, Object[] o,
	// final int... rowStartIdxAndCount) {
	// try {
	// final String queryString = "select model from HrCDept model where "
	// + strWhere;
	// Query query = entityManager.createQuery(queryString);
	// for (int i = 0; i < o.length; i++) {
	// query.setParameter("param" + i, o[i]);
	// }
	// if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
	// int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
	// if (rowStartIdx > 0) {
	// query.setFirstResult(rowStartIdx);
	// }
	//
	// if (rowStartIdxAndCount.length > 1) {
	// int rowCount = Math.max(0, rowStartIdxAndCount[1]);
	// if (rowCount > 0) {
	// query.setMaxResults(rowCount);
	// }
	// }
	// }
	// return query.getResultList();
	// } catch (RuntimeException re) {
	// LogUtil.log("find by property name failed", Level.SEVERE, re);
	// throw re;
	// }
	// }

	public List<HrCDept> findByPdeptId(Object pdeptId,
			int... rowStartIdxAndCount) {
		return findByProperty(PDEPT_ID, pdeptId, rowStartIdxAndCount);
	}

	//
	// public List<HrCDept> findByDeptCode(Object deptCode,
	// int... rowStartIdxAndCount) {
	// return findByProperty(DEPT_CODE, deptCode, rowStartIdxAndCount);
	// }
	//
	// public List<HrCDept> findByPdeptCode(Object pdeptCode,
	// int... rowStartIdxAndCount) {
	// return findByProperty(PDEPT_CODE, pdeptCode, rowStartIdxAndCount);
	// }
	// 
	// public List<HrCDept> findByDeptName(Object deptName,
	// int... rowStartIdxAndCount) {
	// return findByProperty(DEPT_NAME, deptName, rowStartIdxAndCount);
	// }
	//
	// public List<HrCDept> findByManger(Object manger, int...
	// rowStartIdxAndCount) {
	// return findByProperty(MANGER, manger, rowStartIdxAndCount);
	// }
	//
	// public List<HrCDept> findByDeptTypeId(Object deptTypeId,
	// int... rowStartIdxAndCount) {
	// return findByProperty(DEPT_TYPE_ID, deptTypeId, rowStartIdxAndCount);
	// }
	//
	// public List<HrCDept> findByIsUse(Object isUse, int...
	// rowStartIdxAndCount) {
	// return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	// }
	//
	// public List<HrCDept> findByMemo(Object memo, int... rowStartIdxAndCount)
	// {
	// return findByProperty(MEMO, memo, rowStartIdxAndCount);
	// }
	//
	// public List<HrCDept> findByRetrieveCode(Object retrieveCode,
	// int... rowStartIdxAndCount) {
	// return findByProperty(RETRIEVE_CODE, retrieveCode, rowStartIdxAndCount);
	// }
	//
	// public List<HrCDept> findByOrderBy(Object orderBy,
	// int... rowStartIdxAndCount) {
	// return findByProperty(ORDER_BY, orderBy, rowStartIdxAndCount);
	// }
	//
	// public List<HrCDept> findByCreateBy(Object createBy,
	// int... rowStartIdxAndCount) {
	// return findByProperty(CREATE_BY, createBy, rowStartIdxAndCount);
	// }
	//
	// public List<HrCDept> findByModifiyBy(Object modifiyBy,
	// int... rowStartIdxAndCount) {
	// return findByProperty(MODIFIY_BY, modifiyBy, rowStartIdxAndCount);
	// }
	//
	// public List<HrCDept> findByLogoffBy(Object logoffBy,
	// int... rowStartIdxAndCount) {
	// return findByProperty(LOGOFF_BY, logoffBy, rowStartIdxAndCount);
	// }
	//
	// public List<HrCDept> findByPowerPlantId(Object powerPlantId,
	// int... rowStartIdxAndCount) {
	// return findByProperty(POWER_PLANT_ID, powerPlantId, rowStartIdxAndCount);
	// }

	// /**
	// * Find all HrCDept entities.
	// *
	// * @param rowStartIdxAndCount
	// * Optional int varargs. rowStartIdxAndCount[0] specifies the the
	// * row index in the query result-set to begin collecting the
	// * results. rowStartIdxAndCount[1] specifies the the maximum
	// * count of results to return.
	// * @return List<HrCDept> all HrCDept entities
	// */
	// @SuppressWarnings("unchecked")
	// public List<HrCDept> findAll(final int... rowStartIdxAndCount) {
	// LogUtil.log("finding all HrCDept instances", Level.INFO, null);
	// try {
	// final String queryString = "select model from HrCDept model";
	// Query query = entityManager.createQuery(queryString);
	// if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
	// int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
	// if (rowStartIdx > 0) {
	// query.setFirstResult(rowStartIdx);
	// }
	//
	// if (rowStartIdxAndCount.length > 1) {
	// int rowCount = Math.max(0, rowStartIdxAndCount[1]);
	// if (rowCount > 0) {
	// query.setMaxResults(rowCount);
	// }
	// }
	// }
	// return query.getResultList();
	// } catch (RuntimeException re) {
	// LogUtil.log("find all failed", Level.SEVERE, re);
	// throw re;
	// }
	// }

	// public String buildDeptTree(Long pid){
	// List<HrCDept> deptl=findByPdeptId(pid);
	// StringBuffer JSONStr = new StringBuffer();
	// JSONStr.append("[");
	// String icon="";
	// for(int i=0;i<deptl.size();i++){
	// HrCDept dept=deptl.get(i);
	// if(isLeafdept(dept.getDeptId())){
	// icon="file";
	// }else{
	// icon="folder";
	// }
	// JSONStr.append("{\"text\":\"" + dept.getDeptName()+
	// "\",\"id\":\"" + dept.getDeptId() +
	// "\",\"leaf\":" + isLeafdept(dept.getDeptId())+
	// ",\"cls\":\"" + icon+ "\"},");
	// }
	// if (JSONStr.length() > 1) {
	// JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
	// }
	// JSONStr.append("]");
	// return JSONStr.toString();
	// }
	// private boolean isLeafdept(Long pid){
	// List<HrCDept> ld = findByPdeptId(pid);
	// if (ld != null && ld.size() > 0)
	// return false;
	// return true;
	// }

	public List<HrCDept> getDeptByPId(Long pid) {
		String sql = "select d.*\n" + "  from hr_c_dept d\n" + " where \n"
				+ " d.pdept_id=" + pid;
		String sqlcount = "select count(1)\n" + "  from hr_c_dept d\n"
				+ " where " + " d.pdept_id=" + pid;
		;
		if (!bll.getSingal(sqlcount).toString().equals(0)) {
			return bll.queryByNativeSQL(sql, HrCDept.class);
		} else {
			return null;
		}
	}

	/*
	 * 取缺陷要检修部门
	 */
	public List<HrCDept> getFailDeptById() {
		String sql = "select d.*, dt.dept_type_name\n"
				+ "  from hr_c_dept d, hr_c_dept_type dt\n"
				+ " where d.dept_type_id = dt.dept_type_id\n"
				// modifed by liuyi 091219
				// + " and d.pdept_id = '2' and d.dept_type_id=5";
				+ " and d.is_use='Y' and d.dept_type_id=5 and d.dept_status in ('0','3')";//update by sychen 20100831
      //		+ " and d.is_use='U' and d.dept_type_id=5 and d.dept_status in ('0','3')";
		String sqlcount = "select count(1)\n"
				+ "  from hr_c_dept d, hr_c_dept_type dt\n"
				+ " where d.dept_type_id = dt.dept_type_id"
				// modifed by liuyi 091219
				// + " and d.pdept_id = '2' and d.dept_type_id=5";
				+ " and d.is_use='Y' and d.dept_type_id=5 and d.dept_status in ('0','3')";//update by sychen 20100831
      //		+ " and d.is_use='U' and d.dept_type_id=5 and d.dept_status in ('0','3')";
		;
		if (!bll.getSingal(sqlcount).toString().equals(0)) {
			return bll.queryByNativeSQL(sql, HrCDept.class);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<HrCDept> getFailDeptByIdStop() {
		String sql = "select d.*, dt.dept_type_name\n"
				+ "  from hr_c_dept d, hr_c_dept_type dt\n"
				+ " where d.dept_type_id = dt.dept_type_id\n"
				// modifed by liuyi 091219
				// + " and d.pdept_id = '2' and d.dept_type_id=5";
				+ " and d.is_use='Y' and d.dept_type_id=5 and d.dept_status in ('1','3')";//update by sychen 20100831
//		        + " and d.is_use='U' and d.dept_type_id=5 and d.dept_status in ('1','3')";
		String sqlcount = "select count(1)\n"
				+ "  from hr_c_dept d, hr_c_dept_type dt\n"
				+ " where d.dept_type_id = dt.dept_type_id"
				// modifed by liuyi 091219
				// + " and d.pdept_id = '2' and d.dept_type_id=5";
				+ " and d.is_use='Y' and d.dept_type_id=5 and d.dept_status in ('1','3')";//update by sychen 20100831
//		        + " and d.is_use='U' and d.dept_type_id=5 and d.dept_status in ('1','3')";
		;
		if (!bll.getSingal(sqlcount).toString().equals(0)) {
			return bll.queryByNativeSQL(sql, HrCDept.class);
		} else {
			return null;
		}
	}

	public List<HrCDept> getFailDeptById1() {
		String sql = "select d.*, dt.dept_type_name\n"
				+ "  from hr_c_dept d, hr_c_dept_type dt\n"
				+ " where d.dept_type_id = dt.dept_type_id\n"
				+ " and d.pdept_id = '2' and d.dept_type_id=5 and d.dept_id=180 ";
		String sqlcount = "select count(1)\n"
				+ "  from hr_c_dept d, hr_c_dept_type dt\n"
				+ " where d.dept_type_id = dt.dept_type_id"
				+ " and d.pdept_id = '2' and d.dept_type_id=5 and d.dept_id=180";
		;
		if (!bll.getSingal(sqlcount).toString().equals(0)) {
			return bll.queryByNativeSQL(sql, HrCDept.class);
		} else {
			return null;
		}
	}

	public List<HrCDept> getFailDeptById2() {
		String sql = "select d.*, dt.dept_type_name\n"
				+ "  from hr_c_dept d, hr_c_dept_type dt\n"
				+ " where d.dept_type_id = dt.dept_type_id\n"
				+ " and d.pdept_id = '2' and d.dept_type_id=5 and d.dept_id<>180";
		String sqlcount = "select count(1)\n"
				+ "  from hr_c_dept d, hr_c_dept_type dt\n"
				+ " where d.dept_type_id = dt.dept_type_id"
				+ " and d.pdept_id = '2' and d.dept_type_id=5 and d.dept_id<>180";
		;
		if (!bll.getSingal(sqlcount).toString().equals(0)) {
			return bll.queryByNativeSQL(sql, HrCDept.class);
		} else {
			return null;
		}
	}

	public HrCDept getDeptInfoByempCode(String empCode) {
		String sql = "select d.* from hr_j_emp_info t,hr_c_dept d where t.emp_code= ? and d.dept_id=t.dept_id and t.emp_state='U' and d.is_use='Y' and rownum = 1";//update by sychen 20100831
//		String sql = "select d.* from hr_j_emp_info t,hr_c_dept d where t.emp_code= ? and d.dept_id=t.dept_id and t.emp_state='U' and d.is_use='U' and rownum = 1";
		return (HrCDept) bll.queryByNativeSQL(sql, new Object[] { empCode },
				HrCDept.class).get(0);
	}

	public HrCDept getDeptInfoByDeptCode(String deptCode) {
		String sql = "select * from hr_c_dept t\n" + "where t.dept_code='"
//		        + deptCode + "'  and   t.is_use='U'";
				+ deptCode + "'  and   t.is_use='Y'";//update by sychen 20100831
		List<HrCDept> list = bll.queryByNativeSQL(sql, HrCDept.class);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	// /**
	// * 由部门查询人员(级连查询)
	// * @param deptId 部门编码
	// * @param notInWorkerCodes 格式为 : '1440','0689'
	// * @return PageObject 其中 list为List<Employee>
	// */
	// public PageObject getWorkersByDeptId(Long deptId, String queryKey,
	// String notInWorkerCodes, final int... rowStartIdxAndCount) {
	// if (notInWorkerCodes == null || "".equals(notInWorkerCodes)) {
	// notInWorkerCodes = "' '";
	// }
	// PageObject object = null;
	// List<Employee> result = null;
	// List list = null;
	// String sql = "";
	// if (queryKey != null && !"".equals(queryKey)) {
	// queryKey = "%" + queryKey + "%";
	// }
	// else{
	// queryKey = "%";
	// }
	// if(deptId.intValue() ==-2)
	// {
	// //没有部门和部门被删除的人员
	// sql = "select t.emp_code, t.chs_name\n" +
	// " from hr_j_emp_info t\n" +
	// " where t.emp_code not in ("+notInWorkerCodes+")\n" +
	// " and t.emp_code || t.chs_name like ?\n" +
	// " and (t.dept_id not in\n" +
	// " (select t.dept_id from hr_c_dept t where t.is_use = 'U') or\n" +
	// " t.dept_id is null) order by t.order_by,t.emp_code ";
	//
	// list = bll.queryByNativeSQL(sql, new Object[] {queryKey},
	// rowStartIdxAndCount);
	// if (list != null && list.size() > 0) {
	// object = new PageObject();
	// result = new ArrayList<Employee>();
	// for (int i = 0; i < list.size(); i++) {
	// Object[] o = (Object[]) list.get(i);
	// Employee e = new Employee();
	// if (o[0] != null) {
	// e.setWorkerCode(o[0].toString());
	// }
	// if (o[1] != null) {
	// e.setWorkerName(o[1].toString());
	// }
	// result.add(e);
	// }
	// object.setList(result);
	// sql = "select count(1)\n" +
	// " from hr_j_emp_info t\n" +
	// " where t.emp_code not in ("+notInWorkerCodes+")\n" +
	// " and t.emp_code || t.chs_name like ?\n" +
	// " and (t.dept_id not in\n" +
	// " (select t.dept_id from hr_c_dept t where t.is_use = 'U') or\n" +
	// " t.dept_id is null) ";
	// object.setTotalCount(Long.parseLong(bll.getSingal(sql,
	// new Object[] { queryKey }).toString()));
	//	
	// }
	//			 
	// }
	// else
	// {
	// sql = "select a.emp_code, a.chs_name, b.dept_id, b.dept_code,
	// b.dept_name\n"
	// + " from hr_j_emp_info a, hr_c_dept b\n"
	// + " where a.dept_id = b.dept_id\n"
	// + " and b.is_use = 'U'\n"
	// + " and a.emp_code not in ("
	// + notInWorkerCodes
	// + ")\n"
	// + " and a.emp_code || a.chs_name like ?\n"
	// + " and a.dept_id in (select t.dept_id\n"
	// + " from hr_c_dept t\n"
	// + " where t.is_use = 'U'\n"
	// + " start with t.dept_id = ?\n"
	// + " connect by prior t.dept_id = t.pdept_id)\n"
	// + " order by b.order_by, a.order_by, a.emp_code";
	//	
	// list = bll.queryByNativeSQL(sql, new Object[] { queryKey, deptId },
	// rowStartIdxAndCount);
	//	
	// if (list != null && list.size() > 0) {
	// object = new PageObject();
	// result = new ArrayList<Employee>();
	// for (int i = 0; i < list.size(); i++) {
	// Object[] o = (Object[]) list.get(i);
	// Employee e = new Employee();
	// if (o[0] != null) {
	// e.setWorkerCode(o[0].toString());
	// }
	// if (o[1] != null) {
	// e.setWorkerName(o[1].toString());
	// }
	// if (o[2] != null) {
	// e.setDeptId(Long.parseLong(o[2].toString()));
	// }
	// if (o[3] != null) {
	// e.setDeptCode(o[3].toString());
	// }
	// if (o[4] != null) {
	// e.setDeptName(o[4].toString());
	// }
	// result.add(e);
	// }
	// object.setList(result);
	// sql = "select count(1)\n"
	// + " from hr_j_emp_info a, hr_c_dept b\n"
	// + " where a.dept_id = b.dept_id\n"
	// + " and b.is_use = 'U'\n"
	// + " and a.emp_code not in ("
	// + notInWorkerCodes
	// + ")\n"
	// + " and a.emp_code || a.chs_name like ?\n"
	// + " and a.dept_id in (select t.dept_id\n"
	// + " from hr_c_dept t\n"
	// + " where t.is_use = 'U'\n"
	// + " start with t.dept_id = ?\n"
	// + " connect by prior t.dept_id = t.pdept_id)\n";
	// object.setTotalCount(Long.parseLong(bll.getSingal(sql,
	// new Object[] { queryKey, deptId }).toString()));
	//	
	// }
	// }
	// return object;
	// }

	// add by liuyi 090912 班组部分

	/**
	 * add by liuyi 090915 查询该公司的部门信息
	 * 
	 * @param enterpriseCode
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getDeptInfo(String enterpriseCode) throws SQLException {
		LogUtil.log("ejb:查询该公司的部门信息开始: ", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			Object[] params = new Object[2];
			params[0] = enterpriseCode;
			// hard coding 修改开始
			// modify by liuyi 090916 表中用U表示使用中
			// params[1] = IS_USE_Y;
			params[1] = "Y";//update by sychen 20100831
//			params[1] = "U";
			// hard coding 修改结束
			// 查询sql
			String sql = "select * from HR_C_DEPT  t\n"
					+ "where  t.enterprise_code= ? \n"
					+ " and (t.is_banzu is null or t.is_banzu<>'1')"
					+ "and t.is_use= ? order by t.DEPT_ID";
			// 执行查询
			List<HrCDept> list = bll.queryByNativeSQL(sql, params,
					HrCDept.class);
			// 查询sql
			String sqlCount = "select count(t.DEPT_ID) from HR_C_DEPT t\n"
					+ "where  t.enterprise_code= ? \n"
					+ " and (t.is_banzu is null or t.is_banzu<>'1')"
					+ "and t.is_use= ? order by t.DEPT_ID";
			// 执行查询
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
					.toString());
			// 设置PageObject
			result.setList(list);
			result.setTotalCount(totalCount);
			// 返回
			LogUtil.log(" ejb:查询该公司的部门信息结束", Level.INFO, null);
			return result;
		} catch (RuntimeException e) {
			LogUtil.log(" ejb:查询该公司的部门信息失败", Level.INFO, null);
			throw new SQLException(e.getMessage());
		}
	}

	/**
	 * 查询是否班组==指定值的班组信息
	 * 
	 * @param parentClassNo
	 *            父编码
	 * @param enterpriseCode
	 *            企业编码
	 * @return PageObject 部门列表
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getBanzuInfo(String strIsBanzu, String enterpriseCode)
			throws SQLException {
		LogUtil.log("通过是否班组==" + strIsBanzu + "查找班组信息: ", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			Object[] params = new Object[3];
			params[0] = enterpriseCode;
			params[1] = strIsBanzu;
			// hard coding 修改开始
			params[2] = "Y";//update by sychen 20100831
//			params[2] = "U";
			// hard coding 修改结束
			// 查询sql
			String sql = "select * from HR_C_DEPT  t\n"
					+ "where  t.enterprise_code= ? \n" + "and t.IS_BANZU= ? \n"
					+ "and t.is_use= ? order by t.DEPT_ID";
			// 执行查询
			List<HrCDept> list = bll.queryByNativeSQL(sql, params,
					HrCDept.class);
			// 查询sql
			String sqlCount = "select count(t.DEPT_ID) from HR_C_DEPT t\n"
					+ "where  t.enterprise_code= ? \n" + "and t.IS_BANZU= ? \n"
					+ "and t.is_use= ? order by t.DEPT_ID";
			// 执行查询
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
					.toString());
			// 设置PageObject
			result.setList(list);
			result.setTotalCount(totalCount);
			// 返回
			return result;
		} catch (RuntimeException e) {
			LogUtil.log(" Ejb:通过是否班组=='1'查找班组信息失败", Level.INFO, null);
			throw new SQLException(e.getMessage());
		}
	}

	/**
	 * add by liuyi 090914 保存班组信息
	 * 
	 * @param ids
	 *            前台的班组信息
	 * @param enterpriseCode
	 *            企业编码
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateDeptBanzu(String ids, String enterpriseCode,
			String workCode) throws Exception {
		try {
			PageObject obj = new PageObject();
			// 获取已是班组数据
			// hard coding 修改开始
			obj = getBanzuInfo(BANZU_SORT_1, enterpriseCode);
			// 原来的班组数据
			List<HrCDept> listExist = obj.getList();
			// 前台的班组数据
			List<HrCDept> listBanzu = new ArrayList<HrCDept>();
			// 获取设置为班组的数据
			if (ids != null && !ids.equals(BLANK_STRING)) {
				String arrayIds[] = ids.split(COMMA);
				// 如果不为空，就设置上次修改人
				for (int i = 0; i < arrayIds.length; i++) {
					HrCDept entity = findById(Long.parseLong(arrayIds[i]));
					listBanzu.add(entity);
				}
			}
			// 如果不为空
			if (obj != null && obj.getList() != null) {
				// 如果原来班组里的数据在现在班组里，不执行操作
				// 否则，将该数据的是否班组设为空
				for (int i = 0; i < listExist.size(); i++) {
					HrCDept entity = listExist.get(i);
					// modify by liuyi 090912 表中修改人存为数字
					// entity.setModifiyBy(workCode);
					entity.setModifiyBy(Long.parseLong(workCode));
					entity.setIsBanzu(BANZU_SORT_0);
					entity.setModifiyDate(new Date());
					entityManager.merge(entity);
				}
			}
			// 如果不为空
			for (int i = 0; i < listBanzu.size(); i++) {
				HrCDept entity = listBanzu.get(i);
				// 如果现在班组里的数据在原来班组里，不执行操作
				// 否则，将该数据的是否班组设为‘1’
				// modify by liuyi 090912 表中修改人存为数字
				// entity.setModifiyBy(workCode);
				entity.setModifiyBy(Long.parseLong(workCode));
				entity.setIsBanzu(BANZU_SORT_1);
				entity.setModifiyDate(new Date());
				entityManager.merge(entity);
			}
			// hard coding 修改结束
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	/**
	 * add by liuyi 090914 查找所有班组部门
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllBanzuDepts(String enterpriseCode) {
		try {
			String sql = "SELECT D.DEPT_ID ,D.DEPT_NAME FROM HR_C_DEPT D \n"
					+ " WHERE D.IS_BANZU = '1' \n"
					+
					// modify by liuyi D表中is_use存U为使用
					// "AND D.IS_USE = 'Y' AND D.ENTERPRISE_CODE = '" +
					// enterpriseCode +"'";
					"AND D.IS_USE = 'U' AND D.ENTERPRISE_CODE = '"
					+ enterpriseCode + "'";
			LogUtil.log("所有班组部门id和名称开始。SQL=" + sql, Level.INFO, null);
			List list = bll.queryByNativeSQL(sql);
			List<DrpCommBeanInfo> arraylist = new ArrayList<DrpCommBeanInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DrpCommBeanInfo model = new DrpCommBeanInfo();
				if (data[0] != null) {
					model.setId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setText(data[1].toString());
				}
				arraylist.add(model);
			}
			PageObject result = new PageObject();
			result.setList(arraylist);
			LogUtil.log("查找所有班组部门id和名称结束。", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("查找所有班组部门id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}
	public List<HrCDept> getByParentId(Long pdeptId,String needCheck,String flag)//add by wpzhu 20100707
	{
		List<HrCDept> arrlist = new ArrayList<HrCDept>();
		String sql = "select a.dept_id, \n" + "a.pdept_id, \n"
				+ "a.dept_code, \n" + "a.pdept_code, \n" + "a.dept_name, \n"
				+ "a.dept_type_id, \n" + "a.dep_feature, \n" + "a.is_banzu \n"
				+ "from hr_c_dept a \n" + "where a.is_use='Y' \n"//update by sychen 20100831
//				+ "from hr_c_dept a \n" + "where a.is_use='U' \n"
				+ "and a.pdept_id =" + pdeptId;
		if (needCheck != null && needCheck.equals("yes"))
			sql += " and (a.is_banzu is null or a.is_banzu='0') \n";
		if(flag!=null&&!flag.equals(""))
		{
			if(flag.equals("roleQuery"))
			{
				sql+="and  a.dept_status<>1";
			}
		}
//	System.out.println("the sql"+sql);
		List list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				HrCDept dept = new HrCDept();
				Object[] da = (Object[]) it.next();
				if (da[0] != null)
					dept.setDeptId(Long.parseLong(da[0].toString()));
				if (da[1] != null)
					dept.setPdeptId(Long.parseLong(da[1].toString()));
				if (da[2] != null)
					dept.setDeptCode(da[2].toString());
				if (da[3] != null)
					dept.setPdeptCode(da[3].toString());
				if (da[4] != null)
					dept.setDeptName(da[4].toString());
				if (da[5] != null)
					dept.setDeptTypeId(Long.parseLong(da[5].toString()));
				if (da[6] != null)
					dept.setDepFeature(da[6].toString());
				if (da[7] != null)
					dept.setIsBanzu(da[7].toString());
				arrlist.add(dept);
			}
			
		
	}
		return arrlist;
	}
	// add by liuyi 090922
	public List<HrCDept> getListByParentId(Long pdeptId, String needCheck) {
		List<HrCDept> arrlist = new ArrayList<HrCDept>();
		String sql = "select a.dept_id, \n" + "a.pdept_id, \n"
				+ "a.dept_code, \n" + "a.pdept_code, \n" + "a.dept_name, \n"
				+ "a.dept_type_id, \n" + "a.dep_feature, \n" + "a.is_banzu \n"
				+ "from hr_c_dept a \n" + "where a.is_use='Y' \n"//update by sychen 20100831
//				+ "from hr_c_dept a \n" + "where a.is_use='U' \n"
				+ "and a.pdept_id =" + pdeptId;
		if (needCheck != null && needCheck.equals("yes"))
			sql += " and (a.is_banzu is null or a.is_banzu='0') \n";
	
		List list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				HrCDept dept = new HrCDept();
				Object[] da = (Object[]) it.next();
				if (da[0] != null)
					dept.setDeptId(Long.parseLong(da[0].toString()));
				if (da[1] != null)
					dept.setPdeptId(Long.parseLong(da[1].toString()));
				if (da[2] != null)
					dept.setDeptCode(da[2].toString());
				if (da[3] != null)
					dept.setPdeptCode(da[3].toString());
				if (da[4] != null)
					dept.setDeptName(da[4].toString());
				if (da[5] != null)
					dept.setDeptTypeId(Long.parseLong(da[5].toString()));
				if (da[6] != null)
					dept.setDepFeature(da[6].toString());
				if (da[7] != null)
					dept.setIsBanzu(da[7].toString());
				arrlist.add(dept);
			}
		}
		return arrlist;
	}

	/**
	 * add by liuyi 090921 查找具体部门下的班组
	 * 
	 * @param deptId
	 * @return
	 */
	public List getBanzuByDept(Long deptId) {
		String sql = "select a.dept_id, \n" + "a.dept_name \n"
				+ "from hr_c_dept a \n" + "where a.is_use='Y' \n"//update by sychen 20100831
//				+ "from hr_c_dept a \n" + "where a.is_use='U' \n"
				+ "and a.pdept_id=" + deptId + "and a.is_banzu='1' \n";
		List list = bll.queryByNativeSQL(sql);
		return list;
	}

	public Long getDeptIdByDeptName(String deptName) {
		Long deptId = null;
		String sql = "select b.dept_id from hr_c_dept b where b.dept_name ='"+deptName+"'" +
				" and b.is_use='Y'";//update by sychen 20100831
//		        " and b.is_use='U'";
		Object obj = bll.getSingal(sql);
		if(obj != null)
			deptId = Long.parseLong(obj.toString());
		return deptId;
	}
	/**
	 * 根据部门编码或Id查询一级部门信息
	 * @param deptCodeOrIdOrWorkerCode 部门编码或id或员工编号
	 * @param flag 1--部门编码 2---部门id 3---员工编号
	 * @param enterpriseCode 企业编码
	 * @return HrCDept
	 * add by kzhang 20100928
	 */
	public HrCDept getFirstLevelDept(String deptCodeOrIdOrWorkerCode,String flag,String enterpriseCode){
		StringBuffer sql=new StringBuffer("select *\n" +
				"  from hr_c_dept t\n" + 
				" where t.dept_code = getfirstlevelbyid((\n");
		if ("1".equals(flag)) {
			sql.append("select b.dept_id from hr_c_dept b where b.dept_code ="+deptCodeOrIdOrWorkerCode);
		} else if ("2".equals(flag)) {
			sql.append(deptCodeOrIdOrWorkerCode);
		} else if("3".equals(flag)){
			sql.append("select c.dept_id from hr_j_emp_info c where c.emp_code="+deptCodeOrIdOrWorkerCode);
		} else{
			return null;
		}
		sql.append("))\n" + 
				"  and t.is_use = 'Y'\n" +
				"  and t.enterprise_code='"+enterpriseCode+"'");
		List<HrCDept> list=bll.queryByNativeSQL(sql.toString(), HrCDept.class);
		if (list.size()>0) {
			return list.get(0);
		}else{
			return null;
		}
	}
}