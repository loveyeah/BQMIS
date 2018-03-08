package power.web.system.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.Employee;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject; //import power.ejb.basedata.BaseDataManager;
//import power.ejb.hr.HrCDeptFacadeRemote;
//import power.ejb.hr.HrJEmpInfo;
//import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.basedata.BaseDataManager;
import power.ejb.system.SysCUl;
import power.ejb.system.SysCUlFacadeRemote;
import power.ejb.system.SysJLoginLog;
import power.ejb.system.SysJLoginLogFacadeRemote;
import power.ejb.system.SysJUr;
import power.ejb.system.SysCUlFacade.workerObj;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 用户维护管理类
 * 
 * @author
 */
public class UserMaintenanceAction extends AbstractAction {
	private static final long serialVersionUID = 6930018429035624564L;
	SysCUlFacadeRemote remote;
	// HrJEmpInfoFacadeRemote hrremote;
	SysJLoginLogFacadeRemote logremote;
	// HrCDeptFacadeRemote deptremote;
	private SysCUl user;
	private String userIds;
	private String userlike;
	private Long roleId;
	private String roleIds;
	private Long fileId;
	private String loginFile;
	private String newPwd;
	private int start;
	private int limit;
	private Long deptId;

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getUserlike() {
		return userlike;
	}

	public void setUserlike(String userlike) {
		this.userlike = userlike;
	}

	public UserMaintenanceAction() {
		logremote = (SysJLoginLogFacadeRemote) factory
				.getFacadeRemote("SysJLoginLogFacade");
		remote = (SysCUlFacadeRemote) factory.getFacadeRemote("SysCUlFacade");
		// hrremote = (HrJEmpInfoFacadeRemote) factory
		// .getFacadeRemote("HrJEmpInfoFacade");
		// deptremote=(HrCDeptFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("HrCDeptFacade");
	}

	/**
	 * 保存用户
	 */
	public void addUser() throws CodeRepeatException, NamingException,
			JSONException {

		try {
			List<SysCUl> list = new ArrayList();
			SysCUl entity = new SysCUl();
			if (user.getWorkerCode() != null) {
				String[] code_array = user.getWorkerCode().split(",");
				String[] name_array = user.getWorkerName().split(",");
				for (int i = 0; i < code_array.length; i++) {
					SysCUl model = new SysCUl();
					model.setWorkerCode(code_array[i]);
					model.setWorkerName(name_array[i]);
					model.setStyle(user.getStyle());
					model.setLoginPwd(power.ear.comm.SecurityManager
							.GetMD5Str32(power.ear.comm.SecurityManager
									.GetMD5Str32("123")));
					model.setEnterpriseCode(employee.getEnterpriseCode());
					model.setIsUse("Y");
					model.setModifyBy(employee.getWorkerCode());
					model.setModifyDate(new Date());
					if (roleIds != "" && roleIds.length() > 1) {
						SysJUr roleuser = new SysJUr();
						roleuser
								.setEnterpriseCode(employee.getEnterpriseCode());
						roleuser.setIsUse("Y");
						roleuser.setModifyBy(employee.getWorkerCode());
						roleuser.setModifyDate(new Date());
						entity = remote.saveUR(model, roleIds, roleuser);
						if (entity != null)
							list.add(entity);
					} else {
						entity = remote.save(model);
						if (entity != null)
							list.add(entity);
					}

				}
			}
			if (list != null) {
				String retStr = JSONUtil.serialize(list);
				write("{success:true,data:{totalCount:" + list.size()
						+ ",list:" + retStr + "}}");
			}
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}

	}

	/**
	 * 修改用户
	 */
	public void updateUser() throws CodeRepeatException, NamingException,
			JSONException {
		try {
			SysCUl curUser = remote.findById(user.getWorkerId());
			curUser.setEmail(user.getEmail());
			curUser.setStyle(user.getStyle());
			curUser.setWorkerName(user.getWorkerName());
			curUser.setModifyBy(employee.getWorkerCode());
			curUser.setModifyDate(new Date());
			SysCUl model = remote.update(curUser);
			String retStr = JSONUtil.serialize(model);
			write("{success:true,user:" + retStr + "}");
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}

	}

	// 管理员修改密码
	public void modifyPassByManager() throws CodeRepeatException {
		SysCUl curUser = remote.findById(user.getWorkerId());
		curUser.setLoginPwd(power.ear.comm.SecurityManager
				.GetMD5Str32(power.ear.comm.SecurityManager.GetMD5Str32(user
						.getLoginPwd())));
		remote.update(curUser);
	}

	/**
	 * 删除用户
	 */
	public void deleteUser() throws CodeRepeatException, NamingException,
			JSONException {
		if (userIds != null && userIds.length() > 0) {
			String[] userId_array = userIds.split(",");
			for (String userId : userId_array) {
				SysCUl curUser = remote.findById(Long.parseLong(userId));
				curUser.setIsUse("N");
				remote.update(curUser);
			}
		}
		write("{success:true}");
	}

	public void findUsersByProperty() throws Exception {
		if (userlike == null && userlike.length() < 1) {
			userlike = "";
		}
		PageObject list = remote.getUsersBy(employee.getEnterpriseCode(),
				userlike, start, limit);
		if (list != null) {
			String retStr = JSONUtil.serialize(list.getList());
			write("{data:{totalCount:" + list.getTotalCount() + ",list:"
					+ retStr + "}}");
		} else
			write("{data:{totalCount:0,list:[]}");

	}

	/**
	 * 登录验证
	 */
	public String checkUserLoginRight() throws IOException {
		String errMsg;
		try {
			SysJLoginLog login = new SysJLoginLog();
			String pwd = power.ear.comm.SecurityManager
					.GetMD5Str32(power.ear.comm.SecurityManager
							.GetMD5Str32(user.getLoginPwd()));
			// user.setEnterpriseCode("hfdc");
			SysCUl sysCUl = remote.checkUserRightAndReturnWorkerId(user
					.getEnterpriseCode(), user.getWorkerCode(), pwd);
			if (sysCUl != null) {
				BaseDataManager bdm = (BaseDataManager) Ejb3Factory
						.getInstance().getFacadeRemote("BaseDataManagerImpl");
				Employee employee = bdm.getEmployeeInfo(user.getWorkerCode());
				// 如果人员基本信息里没有维护该员工的基本信息
				if (employee == null) {
					employee = new Employee();
					employee.setWorkerCode(sysCUl.getWorkerCode());
					employee.setWorkerName(sysCUl.getWorkerName());
				}
				employee.setEmpId(sysCUl.getWorkerId());// sys_c_ul主键值
				employee.setLoginPwd(sysCUl.getLoginPwd());
				employee.setEnterpriseCode(sysCUl.getEnterpriseCode());
				employee.setStyle(sysCUl.getStyle());
				employee.setEnterpriseChar("H");
				session.setAttribute("employee", employee);
				login.setHostName(request.getRemoteHost());
				login.setHostIp(request.getRemoteAddr());
				login.setEnterpriseCode(employee.getEnterpriseCode());
				login.setLoginDate(new Date());
				login.setWorkerCode(employee.getWorkerCode());
				login.setLoginFile(loginFile);
				logremote.save(login);
				return "success";

			} else {
				errMsg = "-1";// 用户名或密码错误
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg = "-2"; // 服务器错误
		}
		response.sendRedirect(request.getContextPath() + "/index.jsp?errMsg="
				+ errMsg);
		return ("input");
	}

	public void insertLogin() {
		SysJLoginLog login = new SysJLoginLog();
		login.setHostName(request.getRemoteHost());
		login.setHostIp(request.getRemoteAddr());
		login.setEnterpriseCode(employee.getEnterpriseCode());
		login.setLoginDate(new Date());
		login.setWorkerCode(employee.getWorkerCode());
		login.setLoginFile(loginFile);
		logremote.save(login);
	}

	public void modifyPwd() throws CodeRepeatException, NamingException,
			JSONException {
		SysCUl model = remote.findById(employee.getEmpId());
		if (model.getLoginPwd().equals(
				power.ear.comm.SecurityManager
						.GetMD5Str32(power.ear.comm.SecurityManager
								.GetMD5Str32(user.getLoginPwd())))) {
			model.setLoginPwd(power.ear.comm.SecurityManager
					.GetMD5Str32(power.ear.comm.SecurityManager
							.GetMD5Str32(newPwd)));
			remote.update(model);
			write("{success : true}");
		} else {
			write("{success:false,errorMessage:'工号或密码错误!'}");
		}

	}

	public void findUserByRP() throws Exception {

		if (userlike == null && userlike.length() < 1) {
			userlike = "%";
		}
		PageObject list = remote.findByroleIdAndCodeOrName(roleId, userlike,
				false, start, limit);
		if (list != null) {
			String retStr = JSONUtil.serialize(list.getList());
			write("{success : true,data:{totalCount:" + list.getTotalCount()
					+ ",list:" + retStr + "}}");
		} else
			write("{success : true,data:{totalCount:0,list:[]}");
	}

	public void findWaitUserByRP() throws Exception {
		if (userlike == null && userlike.length() < 1) {
			userlike = "%";
		}
		PageObject list = remote.findByroleIdAndCodeOrName(roleId, userlike,
				true, start, limit);
		if (list != null) {
			String retStr = JSONUtil.serialize(list.getList());
			write("{success : true,data:{totalCount:" + list.getTotalCount()
					+ ",list:" + retStr + "}}");
		} else
			write("{success : true,data:{totalCount:0,list:[]}");
	}

	public void findWaitByDeptIdAndRoleId() throws Exception {
		if (userlike == null && userlike.length() < 1) {
			userlike = "%";
		}
		// Long deptId =
		// Long.parseLong(request.getParameter("deptId").toString());
		Long roleId = Long.parseLong(request.getParameter("roleId").toString());
		int start = Integer.parseInt(request.getParameter("start").toString());
		int limit = Integer.parseInt(request.getParameter("limit").toString());
		PageObject result = remote.findUserByDeptAndRole(roleId, userlike,
				deptId, start, limit);
		if (result != null) {
			String retStr = JSONUtil.serialize(result.getList());
			write("{success:true,data:{totalCount:" + result.getTotalCount()
					+ ",list:" + retStr + "}}");
		} else {
			write("{success:true,data:{totalCount:0,list:[]}");
		}
	}

	public void findUsersByfileId() throws Exception {
		PageObject list = remote.findUsersByfileId(fileId);
		if (list != null) {
			String retStr = JSONUtil.serialize(list.getList());
			write("{success : true,data:{totalCount:" + list.getTotalCount()
					+ ",list:" + retStr + "}}");
		} else
			write("{success : true,data:{totalCount:0,list:[]}");
	}

	public void findUsersFromHrByName() throws Exception {

		// List<HrJEmpInfo> list = hrremote.findUsersByName(request
		// .getParameter("hrlike"), start, limit);
		// if (list != null) {
		// String retStr = JSONUtil.serialize(list);
		// write("{success : true,data:{totalCount:" + list.size() + ",list:"
		// + retStr + "}}");
		// } else {
		// write("{success : true,data:{totalCount:0,list:[]}");
		// }
	}

	public void validateUser() {
		String plantCode = request.getParameter("plantCode");
		String workerCode = request.getParameter("workerCode");
		String password = request.getParameter("password");
		workerObj obj = remote.checkUserRightoutName(plantCode, workerCode,
				password);
		if (obj.isResult()) {
			write("{success:true,workerCode:'" + workerCode + "',workerName:'"
					+ obj.getWorkerName() + "'}");
		} else {
			write("{failure: false}");
		}
	}

	public void findLoginList() throws Exception {
		String workerCode = request.getParameter("workerCode");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		PageObject pg = logremote.findByWorkerCode(workerCode, beginDate,
				endDate, start, limit);
		if (pg != null) {
			write("{data:{total:" + pg.getTotalCount() + ",root:"
					+ JSONUtil.serialize(pg.getList()) + "}}");
		} else {
			write("{data:{total:0,root:[]}");
		}
	}

	public SysCUl getUser() {
		return user;
	}

	public void setUser(SysCUl user) {
		this.user = user;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getLoginFile() {
		return loginFile;
	}

	public void setLoginFile(String loginFile) {
		this.loginFile = loginFile;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
}
