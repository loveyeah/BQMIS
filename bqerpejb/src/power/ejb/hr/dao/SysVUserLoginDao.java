/**
 * 
 */
package power.ejb.hr.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.*;

/**
 * 用户登录访问类，返回登录用户的信息
 * 
 * @author Administrator date 2008-08-20
 */

public class SysVUserLoginDao implements ISysVUserLogin {

	public SysVUserLogin CheckRight(String username, String password)
			throws Exception {
		// TODO Auto-generated method stub
		try {
			List users = findByProperty(username);
			if (users != null && users.size() > 0) {
				Object[] user = (Object[]) users.get(0);
				SysVUserLogin userLogin = new SysVUserLogin();
				userLogin.setEmpId(Long.parseLong(user[0].toString()));
				userLogin.setUserLoginAccount((String) user[1]);
				userLogin.setUserPassword((String) user[2]);
				userLogin.setDbAccount((String) user[3]);
				userLogin.setUserTheme((String) user[4]);
				userLogin.setChsName((String) user[5]);
				userLogin.setEnName((String) user[6]);
				userLogin.setEmpCode((String) user[7]);
				userLogin.setDeptId(Long.parseLong(user[8].toString()));
				userLogin.setDeptCode((String) user[9]);
				userLogin.setDeptName((String) user[10]);
				userLogin.setPowerPlantId(Long.parseLong(user[11].toString()));
				if (userLogin.getUserPassword().equalsIgnoreCase(password)) {
					List<SysVUserRole> lrole = findRoleByProperty(user[0]
							.toString());
					if (lrole != null && lrole.size() > 0) {
						userLogin.setLuserRole(lrole);
					}
					return userLogin;
				} else {
					throw new Exception("用户口令输入错误!");
				}
			} else {
				throw new Exception("用户账号输入错误!");
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(e.getMessage());
		}

	}

	private List findByProperty(String value) throws NamingException {
		try {
			String queryString = String
					.format(
							"select * from SYS_V_USER_LOGIN v where v.USER_LOGIN_ACCOUNT ='%s'",
							value);
			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
					.getInstance().getFacadeRemote("NativeSQLHelper");

			List luserlogin = bll.queryByNativeSQL(queryString);
			return luserlogin;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	private List<SysVUserRole> findRoleByProperty(String roleid)
			throws NamingException {
		try {
			List<SysVUserRole> lrole = new ArrayList<SysVUserRole>();
			String queryString = "SELECT r.role_id,\n"
					+ "       r.role_name \n" + "  FROM sys_c_role r,\n"
					+ "       sys_j_user_right s\n"
					+ " WHERE r.role_id = s.role_id\n"
					+ "   AND r.role_status = 'U'\n" + "   AND s.emp_id = ?1";

			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
					.getInstance().getFacadeRemote("NativeSQLHelper");
			Object[] params = { roleid };
			List list = bll.queryByNativeSQL(queryString, params);
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				SysVUserRole srole = new SysVUserRole();
				srole.setRoleId(Long.parseLong(data[0].toString()));
				srole.setRoleName(data[1].toString());
				lrole.add(srole);
			}

			return lrole;
		} catch (RuntimeException re) {
			throw re;
		}
	}

}
