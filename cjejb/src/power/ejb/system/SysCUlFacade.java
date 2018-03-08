package power.ejb.system;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

@Stateless
public class SysCUlFacade implements SysCUlFacadeRemote {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "SysJUrFacade")
	protected SysJUrFacadeRemote urRemote;
	@EJB(beanName = "SysCFlsFacade")
	protected SysCFlsFacadeRemote flsRemote;

	/**
	 * 增加用户,工号不能相同 如果流水号为空,则取最大值加1 返回 SysCUl :增加成功 返回 null:工号重复
	 */
	public SysCUl save(SysCUl entity) {
		try {
			if (!checkCodeSameForAdd(entity.getWorkerCode(), entity.getIsUse())) {
				{
					if (entity.getWorkerId() == null) {
						entity.setWorkerId(bll
								.getMaxId("sys_c_ul", "worker_id"));
						entityManager.persist(entity);
					}
				}
			}
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysCUl saveUR(SysCUl entity, String roleIds, SysJUr roleuser) {
		try {
			if (!checkCodeSameForAdd(entity.getWorkerCode(), entity.getIsUse())) {
				if (entity.getWorkerId() == null) {
					entity.setWorkerId(bll.getMaxId("sys_c_ul", "worker_id"));
				}
				power.ear.comm.SecurityManager sm = new power.ear.comm.SecurityManager();
				entity.setLoginPwd(sm.GetMD5Str32(sm.GetMD5Str32("123")));
				entityManager.persist(entity);
				if (roleIds != null && roleIds.length() > 0) {
					String[] roleId_array = roleIds.split(",");
					for (String str : roleId_array) {
						roleuser.setWorkerId(entity.getWorkerId());
						roleuser.setRoleId(Long.parseLong(str));
						urRemote.save(roleuser);

					}
				}
			}
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}

	}

	public void delete(SysCUl entity) {
		LogUtil.log("deleting SysCUl instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(SysCUl.class, entity
					.getWorkerId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysCUl update(SysCUl entity) throws CodeRepeatException {
		LogUtil.log("updating SysCUl instance", Level.INFO, null);
		try {
			if (checkCodeSameForUpdate(entity.getWorkerCode(), entity
					.getIsUse(), entity.getWorkerId())) {
				throw new CodeRepeatException("员工编码已经存在!");
			}
			SysCUl result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysCUl findById(Long id) {
		LogUtil.log("finding SysCUl instance with id: " + id, Level.INFO, null);
		try {
			SysCUl instance = entityManager.find(SysCUl.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List findRolesByUser(Long workerId) {
		String sql = "select a.id, b.role_id, b.role_name\n"
				+ "  from sys_j_ur a, sys_c_roles b\n"
				+ " where a.role_id = b.role_id\n" + "   and a.role_id = "
				+ workerId + "\n" + "   and a.is_use = 'Y'";
		return bll.queryByNativeSQL(sql);
	}

	public boolean checkUserLoninRight(String enterpriseCode,
			String workerCode, String password) {
		String sql = "select count(1) from sys_c_ul t where t.enterprise_code='"
				+ enterpriseCode
				+ "' and t.worker_code='"
				+ workerCode
				+ "' and t.login_pwd='" + password + "' and t.is_use='Y' ";
		if (Long.parseLong(bll.getSingal(sql).toString()) > 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public SysCUl checkUserRightAndReturnWorkerId(String enterpriseCode,
			String workerCode, String password) {
		String sql = "select t.* from sys_c_ul t where t.enterprise_code='"
				+ enterpriseCode + "' and t.worker_code='" + workerCode
				+ "' and t.login_pwd='" + password + "' and t.is_use='Y' ";
		List<SysCUl> list = bll.queryByNativeSQL(sql, SysCUl.class);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
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

	@SuppressWarnings("serial")
	public class workerObj implements java.io.Serializable {
		String workerCode;
		String workerName;
		boolean result;

		/**
		 * @return the workerCode
		 */
		public String getWorkerCode() {
			return workerCode;
		}

		/**
		 * @param workerCode
		 *            the workerCode to set
		 */
		public void setWorkerCode(String workerCode) {
			this.workerCode = workerCode;
		}

		/**
		 * @return the workerName
		 */
		public String getWorkerName() {
			return workerName;
		}

		/**
		 * @param workerName
		 *            the workerName to set
		 */
		public void setWorkerName(String workerName) {
			this.workerName = workerName;
		}

		/**
		 * @return the result
		 */
		public boolean isResult() {
			return result;
		}

		/**
		 * @param result
		 *            the result to set
		 */
		public void setResult(boolean result) {
			this.result = result;
		}
	}

	@SuppressWarnings("static-access")
	public workerObj checkUserRightoutName(String enterpriseCode,
			String workerCode, String password) {
		workerObj obj = new workerObj();
		power.ear.comm.SecurityManager sm = new power.ear.comm.SecurityManager();
		password = sm.GetMD5Str32(sm.GetMD5Str32(password));
		String sql = "select count(1) from sys_c_ul t where t.enterprise_code='"
				+ enterpriseCode
				+ "' and t.worker_code='"
				+ workerCode
				+ "' and t.login_pwd='" + password + "' and t.is_use='Y' ";
		String sqlname = "select worker_name from sys_c_ul t where t.enterprise_code='"
				+ enterpriseCode
				+ "' and t.worker_code='"
				+ workerCode
				+ "' and t.login_pwd='"
				+ password
				+ "' and t.is_use='Y' and rownum=1";

		if (Integer.parseInt(bll.getSingal(sql).toString()) > 0) {
			obj.setResult(true);
		} else {
			obj.setResult(false);
		}
		obj.setWorkerCode(workerCode);
		obj.setWorkerName(bll.getSingal(sqlname).toString());
		return obj;
	}

	/**
	 * 判断工号是否重复
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param workerCode
	 *            工号
	 * @param workerId
	 *            主键
	 * @return boolean
	 */
	private boolean checkCodeSameForAdd(String workerCode, String isUse) {
		String sql = "select count(1) from sys_c_ul t where t.worker_code=? and t.is_use=?";
		int size = Integer.parseInt(bll.getSingal(sql,
				new Object[] { workerCode, isUse }).toString());
		if (size > 0) {
			return true;
		} else
			return false;
	}

	private boolean checkCodeSameForUpdate(String workerCode, String isUse,
			Long workerId) {
		String sql = "select count(1) from sys_c_ul t where t.worker_code=? and t.is_use=? and t.worker_id<>?";
		int size = Integer.parseInt(bll.getSingal(sql,
				new Object[] { workerCode, isUse, workerId }).toString());
		if (size > 0) {
			return true;
		}
		return false;
	}
	public PageObject getUsersBy(String enterpriseCode,String workerCodeOrName, int... rowStartIdxAndCount)
	{
		String sql = "select * from sys_c_ul a where a.enterprise_code='"+enterpriseCode
					+"' and a.worker_code||a.worker_name like '%"+workerCodeOrName
					+"%' and a.is_use='Y' order by a.worker_id";
		String sqlCount = "select count(1) from sys_c_ul a where a.enterprise_code='"+enterpriseCode
					+"' and a.worker_code||a.worker_name like '%"+workerCodeOrName
					+"%' and a.is_use='Y'";
		PageObject result = new PageObject();
		List<SysCUl> list = bll.queryByNativeSQL(sql, SysCUl.class,rowStartIdxAndCount);
		Long totalCount = Long
				.parseLong(bll.getSingal(sqlCount).toString());
		result.setList(list);
		result.setTotalCount(totalCount);
		return result;
	}

	@SuppressWarnings("unchecked")
	public PageObject findUserByProperty(String propertyName,
			String propertyValue, int... rowStartIdxAndCount) {
		try {
			String sql = "";
			String sqlCount = "";
			PageObject result = new PageObject();
			if (propertyValue.equals("%")) {
				sql = "select * from sys_c_ul t where t.worker_code || t."
						+ propertyName + " like '%' and t.is_use='Y'";
				sqlCount = "select count(1) from sys_c_ul t where t.worker_code || t."
						+ propertyName + " like '%' and t.is_use='Y'";
			} else {
				sql = "select * from sys_c_ul t where t.worker_code || t."
						+ propertyName + " like '%" + propertyValue
						+ "%' and t.is_use='Y'";
				sqlCount = "select count(1) from sys_c_ul t where t.worker_code || t."
						+ propertyName
						+ " like '%"
						+ propertyValue
						+ "%' and t.is_use='Y'";
			}
			List<SysCUl> list = bll.queryByNativeSQL(sql, SysCUl.class,
					rowStartIdxAndCount);
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject findByroleIdAndCodeOrName(Long roleId,
			String workerCodeOrName, boolean iswait, int... rowStartIdxAndCount) {
		try {
			String sql = "";
			String sqlCount = "";
			PageObject result = new PageObject();
			if (!iswait) {
				if (workerCodeOrName.equals("%")) {
					sql = "select a.*\n"
							+ "  from sys_c_ul a, sys_j_ur b,sys_c_roles r\n"
							+ " where a.worker_id = b.worker_id\n"
							+ "   and b.role_id = " + roleId + "\n"
							+ "   and b.is_use = 'Y'\n"
							+ "   and a.is_use = 'Y'\n"
							+ "   and b.role_id = r.role_id \n"
							+ "   and r.is_use = 'Y'\n"
							+ " order by a.worker_code asc";
					sqlCount = "select count(1)\n"
							+ "  from sys_c_ul a, sys_j_ur b,sys_c_roles r\n"
							+ " where a.worker_id = b.worker_id\n"
							+ "   and b.role_id = " + roleId + "\n"
							+ "   and b.is_use = 'Y'\n"
							+ "   and a.is_use = 'Y'\n"
							+ "   and b.role_id = r.role_id \n"
							+ "   and r.is_use = 'Y'\n"
							+ " order by a.worker_code asc";
				} else {
					sql = "select a.*\n"
							+ "  from sys_c_ul a, sys_j_ur b,sys_c_roles r\n"
							+ " where a.worker_code||a.worker_name\n"
							+ " like '%" + workerCodeOrName + "%'\n"
							+ " and a.worker_id = b.worker_id\n"
							+ "   and b.role_id = " + roleId + "\n"
							+ "   and b.is_use = 'Y'\n"
							+ "   and a.is_use = 'Y'\n"
							+ "   and b.role_id = r.role_id \n"
							+ "   and r.is_use = 'Y'\n"
							+ " order by a.worker_code asc";
					sqlCount = "select count(1)\n"
							+ "  from sys_c_ul a, sys_j_ur b,sys_c_roles r\n"
							+ " where a.worker_code||a.worker_name\n"
							+ " like '%" + workerCodeOrName + "%'\n"
							+ " and a.worker_id = b.worker_id\n"
							+ "   and b.role_id = " + roleId + "\n"
							+ "   and b.is_use = 'Y'\n"
							+ "   and a.is_use = 'Y'\n"
							+ "   and b.role_id = r.role_id \n"
							+ "   and r.is_use = 'Y'\n"
							+ " order by a.worker_code asc";
				}
			} else {
				sql = "select a.*\n"
						+ "from sys_c_ul a\n"
						+ "where a.is_use = 'Y'"
						+ " and a.worker_code||a.worker_name\n"
						+ " like '%"
						+ workerCodeOrName
						+ "%'\n"
						+ " and a.worker_id not in ("
						+ "select a.worker_id\n"
						+ "from sys_c_ul a, sys_j_ur b \n"
						+ "where a.worker_id = b.worker_id and b.is_use = 'Y' \n"
						+ "and b.role_id=" + roleId + ")";
				sqlCount = "select count(1)\n"
						+ "from sys_c_ul a\n"
						+ "where a.is_use = 'Y'"
						+ " and a.worker_code||a.worker_name\n"
						+ " like '%"
						+ workerCodeOrName
						+ "%'\n"
						+ " and a.worker_id not in ("
						+ "select a.worker_id\n"
						+ "from sys_c_ul a, sys_j_ur b \n"
						+ "where a.worker_id = b.worker_id and b.is_use = 'Y' \n"
						+ "and b.role_id=" + roleId + ")";
			}
			List<SysCUl> list = bll.queryByNativeSQL(sql, SysCUl.class,
					rowStartIdxAndCount);
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject findUsersByfileId(long fileId, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			final String sql = "select ul.*\n"
					+ "  from sys_c_ul ul, sys_j_rrs rr, sys_j_ur ur\n"
					+ " where rr.file_id = " + fileId + "\n"
					+ "   and rr.role_id = ur.role_id\n"
					+ "   and ur.worker_id = ul.worker_id\n"
					+ "   and rr.is_use = 'Y'\n" + "   and ur.is_use = 'Y'\n"
					+ "   and ul.is_use = 'Y'";
			final String countsql = "select count(1)\n"
					+ "  from sys_c_ul ul, sys_j_rrs rr, sys_j_ur ur\n"
					+ " where rr.file_id = " + fileId + "\n"
					+ "   and rr.role_id = ur.role_id\n"
					+ "   and ur.worker_id = ul.worker_id\n"
					+ "   and rr.is_use = 'Y'\n" + "   and ur.is_use = 'Y'\n"
					+ "   and ul.is_use = 'Y'";
			List<SysCUl> list = bll.queryByNativeSQL(sql, SysCUl.class,
					rowStartIdxAndCount);
			Long totalCount = Long
					.parseLong(bll.getSingal(countsql).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<SysCUl> findUserByWorkerCode(String workerCode) {
		final String sql = "select u.* from sys_c_ul u where u.worker_code="
				+ workerCode + " and u.is_use='Y'";
		final String sqlcount = "select count(1)from sys_c_ul u where u.worker_code="
				+ workerCode + " and u.is_use='Y'";
		if (!bll.getSingal(sqlcount).toString().equals(0)) {
			return bll.queryByNativeSQL(sql, SysCUl.class);
		} else {
			return null;
		}
	}
	public PageObject findUserByDeptAndRole(Long roleId,String fuzzy,Long deptId, int... rowStartIdxAndCount){
		try {
			PageObject result = new PageObject();
			 String sql = 
				"select  u.*\n" +
				"  from sys_c_ul u\n" + 
				" where u.is_use = 'Y'\n" + 
				"   and u.worker_id not in (select u.worker_id\n" + 
				"                             from sys_c_roles r, sys_j_ur ur\n" + 
				"                            where r.role_id = ur.role_id\n" + 
				"                              and u.worker_id = ur.worker_id\n" + 
				"                              and u.is_use = 'Y'\n" + 
				"                              and ur.is_use = 'Y'\n" + 
				"                              and r.is_use = 'Y'\n" + 
				"                              and r.role_id = ?)\n"+
				"   and u.worker_code||u.worker_name like '%"+fuzzy+"%'\n";
			 String countsql = 
					"select count(1)\n" +
					"  from sys_c_ul u\n" + 
					" where u.is_use = 'Y'\n" + 
					"   and u.worker_id not in (select u.worker_id\n" + 
					"                             from sys_c_roles r, sys_j_ur ur\n" + 
					"                            where r.role_id = ur.role_id\n" + 
					"                              and u.worker_id = ur.worker_id\n" + 
					"                              and u.is_use = 'Y'\n" + 
					"                              and ur.is_use = 'Y'\n" + 
					"                              and r.is_use = 'Y'\n" +
					"                              and r.role_id = ?)\n" +
			 		"   and u.worker_code||u.worker_name like '%"+fuzzy+"%'\n";
			
			 if(deptId !=null )
			 {
//				 sql += "   and u.worker_code in (select emp.emp_code\n" + 
//					"                           from hr_j_emp_info emp, hr_c_dept d\n" + 
//					"                          where emp.dept_id = d.dept_id\n" + 
//					"                            and emp.emp_state = 'U'\n" + 
//					"                            and d.is_use = 'U'\n"+
//					"                            and d.dept_id = "+deptId+")\n" ;
//				 子部门下面的所有人
				 sql += " and u.worker_code in\n" +
					 "       (select e.emp_code\n" + 
					 "          from Hr_j_Emp_Info e\n" + 
					 "         where e.dept_id in\n" + 
					 "               (select d.dept_id\n" + 
					 "                  from hr_c_dept d\n" + 
					 "                 where d.is_use = 'U'\n" + 
					 "                 start with d.dept_id ="+deptId+"\n" + 
					 "                connect by prior d.dept_id = d.pdept_id))";

				 countsql +=" and u.worker_code in\n" +
				 "       (select e.emp_code\n" + 
				 "          from Hr_j_Emp_Info e\n" + 
				 "         where e.dept_id in\n" + 
				 "               (select d.dept_id\n" + 
				 "                  from hr_c_dept d\n" + 
				 "                 where d.is_use = 'U'\n" + 
				 "                 start with d.dept_id ="+deptId+"\n" + 
				 "                connect by prior d.dept_id = d.pdept_id))";
//					 "   and u.worker_code in (select emp.emp_code\n" + 
//					"                           from hr_j_emp_info emp, hr_c_dept d\n" + 
//					"                          where emp.dept_id = d.dept_id\n" + 
//					"                            and emp.emp_state = 'U'\n" + 
//					"                            and d.is_use = 'U'\n"+
//					"                            and d.dept_id = "+deptId+")";
			 }			
//
//			if(deptId!=null){
//				sql=sql.substring(0,sql.length()-1);
//				sql=sql+" and d.dept_id = ?)";
//				countsql=countsql.substring(0,sql.length()-1);
//				countsql=countsql+" and d.dept_id = ?)";
//			}
			 List<SysCUl> list = bll.queryByNativeSQL(sql, new Object[]{roleId},SysCUl.class,	rowStartIdxAndCount);
			Long totalCount = Long
			.parseLong(bll.getSingal(countsql,new Object[]{roleId}).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	public String findWorkerCodeByName(String workerName){
		String sql=
			"select t.worker_code from sys_c_ul t where t.is_use='Y' and t.worker_name='"+workerName+"' and rownum=1";
		if(bll.getSingal(sql)!=null){
			return bll.getSingal(sql).toString();
		}
		else
			return null;
	}
}