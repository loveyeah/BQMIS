package power.web.system.action;

import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.system.SysCRoles;
import power.ejb.system.SysCRolesFacadeRemote;
import power.ejb.system.SysCUl;
import power.ejb.system.SysCUlFacadeRemote;
import power.ejb.system.SysJUr;
import power.ejb.system.SysJUrFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * @author sltang
 * 角色对应用户模块
 *
 */
public class RoleUserAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	protected  SysJUrFacadeRemote remote;
	private SysJUr roleuser;
	private String ids;
	public RoleUserAction(){
		remote = (SysJUrFacadeRemote)factory.getFacadeRemote("SysJUrFacade");
	}
	/**
	 * 增加用户角色
	 * 
	 *
	 */
	public void addUsersInRole(){ 	
		if(ids!=null && ids.length()>0){
			String[] id_array = ids.split(",");
			for(String str : id_array){
					SysJUr ur=remote.findURByURId(roleuser.getRoleId(), Long.parseLong(str), false);
					if(ur!=null){
							ur.setIsUse("Y");
							remote.update(ur);
					}else{
						SysJUr model=remote.findURByURId(roleuser.getRoleId(), Long.parseLong(str), true);
						if(model==null){
							SysJUr entity=new SysJUr();
							entity.setRoleId(roleuser.getRoleId());
							entity.setEnterpriseCode(employee.getEnterpriseCode());
							entity.setIsUse("Y");
							entity.setWorkerId(Long.parseLong(str));
							entity.setModifyBy(employee.getEnterpriseCode());
							entity.setModifyDate(new Date());
							remote.save(entity);
						}
						else{
							model.setIsUse("Y");
							remote.update(model);
						}
					}	
			}
		}	
	}
	/**
	 * 删除用户角色
	 * 
	 *
	 */
	public void delUsersRole(){	
		if(ids!=null && ids.length()>0){
			String[] id_array = ids.split(",");
			for(String str : id_array){
				SysJUr ur=remote.findURByURId(roleuser.getRoleId(), Long.parseLong(str),true);
				if(ur!=null){
					ur.setIsUse("N");
					remote.update(ur);
				}
			}
		}	
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
 
	public SysJUr getRoleuser() {
		return roleuser;
	}
	public void setRoleuser(SysJUr roleuser) {
		this.roleuser = roleuser;
	}
	
}
