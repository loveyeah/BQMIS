package power.web.system.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.system.SysCRoles;
import power.ejb.system.SysCRolesFacadeRemote;
import power.ejb.system.SysJRrs;
import power.web.comm.AbstractAction; 

/**
 * 角色维护
 * @author sltang
 *
 */

public class RoleMaintenanceAction extends AbstractAction{ 
	private static final long serialVersionUID = 1L;
	protected SysCRolesFacadeRemote remote; 
	private SysCRoles role;
	private String likename;
	private Long fileId;
	private String fileIds;
	private int start;
	private int limit;
	private Long workerId;
	/**
	 * 
	 * 构造方法
	 *
	 */
	public RoleMaintenanceAction(){
		remote = (SysCRolesFacadeRemote) factory.getFacadeRemote("SysCRolesFacade");
	}
	
	public void addRole() throws CodeRepeatException,NamingException,JSONException{
		try{
			SysCRoles model;
			role.setEnterpriseCode(employee.getEnterpriseCode());
			role.setModifyBy(employee.getWorkerCode());
			if(role.getRoleType()==null || role.getRoleType()==""){
				role.setRoleType("0");
			}
			role.setIsUse("Y");
			role.setModifyDate(new Date());
			if(fileIds!=null){
				SysJRrs rolefile=new SysJRrs();
				rolefile.setEnterpriseCode(employee.getEnterpriseCode());
				rolefile.setIsUse("Y");
				rolefile.setModifyBy(employee.getWorkerCode());
				rolefile.setModifyDate(new Date());
				model=remote.saveRF(role, fileIds, rolefile);
			}
			else{
				model=remote.save(role);
			}
			if(model!=null){
				String str=JSONUtil.serialize(model);
				write("{success:true,role:"+str+"}");
			}
		}catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
		
	}
	
	public void updateRole() throws  CodeRepeatException,NamingException,JSONException{
		try{
			SysCRoles model = remote.findById(role.getRoleId());
			model.setRoleName(role.getRoleName());
			model.setMemo(role.getMemo());
			model.setLine(role.getLine());
			model.setRoleType("1");
			model.setModifyDate(new java.util.Date());
			remote.update(model);
			String str=JSONUtil.serialize(model);
			
			write("{success:true,role:"+str+"}");
		}catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
		
	}
	
	public void delRole() throws CodeRepeatException,NamingException,JSONException{
		SysCRoles model = remote.findById(role.getRoleId());
		model.setIsUse("N");
		remote.update(model);
		write("{success:true}");
	}
	
	public void findRolesList() throws Exception{
		List<SysCRoles> list = remote.findByisUse("Y");
		String rolesStr = JSONUtil.serialize(list);
		write("{rolesList:"+rolesStr+"}");
	}
	public void findByProperty() throws Exception{
		if(likename==null && likename.length()<1){
			likename="%";
		}	
		PageObject list = remote.findByProperty("role_name", likename,start,limit);
		if(list!=null){
			String rolesStr = JSONUtil.serialize(list.getList());
			write("{success : true,data:{total:"+list.getTotalCount()+",rolesList:"+rolesStr+"}}");
		}
		write("{success : true,data :{total:0,rolesList:[]}}");
	}
	public void findRoleBywId() throws Exception{
		List<SysCRoles> list = remote.findRolesBywId(workerId);
		if(list!=null){
			String rolesStr=JSONUtil.serialize(list);
			write("{rolesList:"+rolesStr+"}");
		}
		else{
			write("{rolesList:[]}");
		}
	}
	public void findAllRole() throws Exception{
		List<SysCRoles> list = remote.findByisUse("Y");
		String rolesStr = JSONUtil.serialize(list);
		write("{rolesList:"+rolesStr+"}");
	}
	public void findRolesByfileId() throws Exception{
		List<SysCRoles> list=remote.findRolesByfileId(fileId);
		if(list!=null){
			String rolesStr=JSONUtil.serialize(list);
			write("{rolesList:"+rolesStr+"}");
		}
		write("{rolesList:[]}")	;
	}
	public SysCRoles getRole() {
		return role;
	}

	public void setRole(SysCRoles role) {
		this.role = role;
	}

	public String getLikename() {
		return likename;
	}

	public void setLikename(String likename) {
		this.likename = likename;
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

	public Long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getFileIds() {
		return fileIds;
	}

	public void setFileIds(String fileIds) {
		this.fileIds = fileIds;
	}
 
}
