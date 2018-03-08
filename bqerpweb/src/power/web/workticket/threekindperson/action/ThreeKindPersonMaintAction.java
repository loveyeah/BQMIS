/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.workticket.threekindperson.action;

import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.system.SysCRoles;
import power.ejb.system.SysCRolesFacadeRemote;
import power.ejb.system.SysCUlFacadeRemote;
import power.ejb.system.SysJUr;
import power.ejb.system.SysJUrFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
/**
 * 工作票三种人维护action
 * @author zhangqi
 */
public class ThreeKindPersonMaintAction extends AbstractAction{
	/** serial id*/
	private static final long serialVersionUID = 1L;
	/**用户查询服务*/
	private SysCUlFacadeRemote userQueryRemote;
	/**角色查询服务*/
	private SysCRolesFacadeRemote roleRemote;
	/**工作票签发人添加删除服务*/
	private SysJUrFacadeRemote  userOpRemote;
	/**角色名*/
	private String rolename;
	/**角色ID*/
	private Long roleId;
	/**用户区分*/
	private boolean iswait;
	/**查询字符串（工号或者名称）*/
	private String userlike;
	/**分页 开始行号*/
	private int start;
	/**分页 行数*/
	private int limit;
	/**角色用户*/
	private SysJUr roleuser;
	/** 用户id组*/
	private String ids;


	/**
	 * 构造函数，初始化远程服务
	 */
	public ThreeKindPersonMaintAction() {
		//用户查询服务
		userQueryRemote = (SysCUlFacadeRemote)factory.getFacadeRemote("SysCUlFacade");
		//角色查询服务
		roleRemote = (SysCRolesFacadeRemote)factory.getFacadeRemote("SysCRolesFacade");
		//工作票签发人添加删除服务
		userOpRemote = (SysJUrFacadeRemote)factory.getFacadeRemote("SysJUrFacade");
	}

	/**
	 * 查找角色列表
	 * @throws JSONException
	 */
	public void getroleList() throws JSONException{
		List<SysCRoles> roles = roleRemote.findByRoleName(rolename);
		PageObject obj = new PageObject();
		obj.setList(roles);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 用户查询
	 * @throws JSONException
	 */
	public void getUserList() throws JSONException {
		// 如果没有输入查询条件，查询所有
		if (userlike == null || userlike.length() < 1) {
			userlike = "%";
		}
		// 查询
		Object deptId=request.getParameter("deptId");
		PageObject list=new PageObject();
		if(iswait){
			if(deptId==null){
				list= userQueryRemote.findUserByDeptAndRole(roleId, userlike, null, start, limit);
			}
			else{
				list= userQueryRemote.findUserByDeptAndRole(roleId, userlike, Long.parseLong(deptId.toString()), start, limit);
			}
		}else{
			list = userQueryRemote.findByroleIdAndCodeOrName(roleId,
					userlike, iswait, start, limit);
		}
		write(JSONUtil.serialize(list));
	}

	/**
	 * 增加用户角色
	 */
	public void addSelectedUser(){

		if(ids!=null && ids.length()>0){
			// 多选的时候分割
			String[] strIds = ids.split(",");
			for(String str : strIds){
				  // 企业编码
					roleuser.setEnterpriseCode(employee.getEnterpriseCode());
					// 用户id
					roleuser.setWorkerId(Long.parseLong(str));
					// 修改者
					roleuser.setModifyBy(employee.getWorkerCode());
					userOpRemote.save(roleuser);
			}
		}
	}

	/**
	 * 删除用户角色
	 */
	public void delSelectedUser(){
		if(ids!=null && ids.length()>0){
			// 多选的时候分割
			String[] strIds = ids.split(",");
			for(String str : strIds){
				// 查询要删除记录
				roleuser=userOpRemote.findURByURId(roleuser.getRoleId(), Long.parseLong(str),true);
				// 删除
				userOpRemote.delete(roleuser);
			}
		}
	}
	/**
	 * 角色名
	 * @return 角色名
	 */
	public String getRolename() {
		return rolename;
	}
	/**
	 * 角色名
	 * @param rolename 角色名
	 */
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	/**
	 * 角色ID
	 * @return 角色ID
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * 角色ID
	 * @param roleId 角色ID
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * 用户区分
	 * @return 用户区分
	 */
	public boolean isIswait() {
		return iswait;
	}
	/**
	 * 用户区分
	 * @param iswait 用户区分
	 */
	public void setIswait(boolean iswait) {
		this.iswait = iswait;
	}

	/**
	 * 查询字符串
	 * @return 查询字符串
	 */
	public String getUserlike() {
		return userlike;
	}

	/**
	 * 查询字符串
	 * @param userlike 查询字符串
	 */
	public void setUserlike(String userlike) {
		this.userlike = userlike;
	}

	/**
	 * 开始行号
	 * @return 开始行号
	 */
	public int getStart() {
		return start;
	}

	/**
	 * 开始行号
	 * @param start 开始行号
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * 行数
	 * @return 行数
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * 行数
	 * @param limit 行数
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * 用户id组
	 * @return 用户id组
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * 用户id组
	 * @param ids 用户id组
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}

	/**
	 * 角色用户
	 * @return 角色用户
	 */
	public SysJUr getRoleuser() {
		return roleuser;
	}

	/**
	 * 角色用户
	 * @param roleuser 角色用户
	 */
	public void setRoleuser(SysJUr roleuser) {
		this.roleuser = roleuser;
	}

}
