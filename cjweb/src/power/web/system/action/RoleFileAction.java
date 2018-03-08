package power.web.system.action;

import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.system.SysCFls;
import power.ejb.system.SysCFlsFacadeRemote;
import power.ejb.system.SysCRolesFacadeRemote;
import power.ejb.system.SysJRrs;
import power.ejb.system.SysJRrsFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * @author sltang
 *
 */
public class RoleFileAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	protected SysJRrsFacadeRemote remote;
	private SysJRrs rolefile;
	private String ids;
	private Long fileId;
	public RoleFileAction(){
		remote=(SysJRrsFacadeRemote)factory.getFacadeRemote("SysJRrsFacade");
	}
	public void findFilesByRoleId() throws Exception{
		List<SysCFls> list=remote.findFilesByRoleId(rolefile.getRoleId(), false);
		write("{root : "+JSONUtil.serialize(list)+"}");
	}
	public void findWaitFilesByBoleId() throws Exception{
		List<SysCFls> list=remote.findFilesByRoleId(rolefile.getRoleId(), true);
		write("{root : "+JSONUtil.serialize(list)+"}");
		
	}
	public void addRoleFile(){
		if(ids!=null && ids.length()>0){
			String[] id_array = ids.split(",");
			for(String str : id_array){
				SysJRrs model = remote.findByRoleIdFileId(rolefile.getRoleId(), Long.parseLong(str));
				if(model!=null){
					model.setIsUse("Y");
					remote.update(model);
				}
				else{
					rolefile.setEnterpriseCode(employee.getEnterpriseCode());
					rolefile.setFileId(Long.parseLong(str));
					rolefile.setRoleId(rolefile.getRoleId());
					rolefile.setIsUse("Y");
					rolefile.setModifyBy(employee.getWorkerCode());
					rolefile.setModifyDate(new Date());
					remote.save(rolefile);
				}
			}
		}	
		
	}
	public void delRolefile(){
		if(ids!=null && ids.length()>0){
			String[] id_array = ids.split(",");
			for(String str : id_array){
				SysJRrs model = remote.findByRoleIdFileId(rolefile.getRoleId(), Long.parseLong(str));
				if(model!=null){
					model.setIsUse("N");
					remote.update(model);
				}
				else{
					rolefile.setEnterpriseCode(employee.getEnterpriseCode());
					rolefile.setFileId(Long.parseLong(str));
					rolefile.setRoleId(rolefile.getRoleId());
					rolefile.setIsUse("N");
					rolefile.setModifyBy(employee.getWorkerCode());
					rolefile.setModifyDate(new Date());
					remote.save(rolefile);
				}
			}
				
		}	
	   
	}
	private String toJsonStr(List<SysCFls> list)
	{
		StringBuffer JSONStr = new StringBuffer(); 
		JSONStr.append("[");
		for (int i = 0; i < list.size(); i++) {
			SysCFls o = list.get(i);
			boolean isLeaf = (o.getIsFile().equals("Y")) ? true : false;
			String icon = isLeaf ? "file" : "folder";
			String addr = "";
			if ("Y".equals(o.getIsFile())) {
				addr = (o.getFileAddr() == null) ? "" : (o.getFileAddr() + "?id=" + o.getFileId());
			}
			String openType = (o.getOpenType()==null?"1":o.getOpenType());
			String description = o.getMemo();
			JSONStr.append("{\"text\":\"" + o.getFileName()
					+ "\",\"id\":\"" + o.getFileId() + "\",\"leaf\":"
					+ isLeaf + ",\"cls\":\"" + icon
					+ "\",\"description\":\"" + description
					+ "\",\"openType\":\"" + openType + "\",\"href\":\""
					+ addr + "\"},");
		}
		if (JSONStr.length() > 1) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
		JSONStr.append("]"); 
		
		System.out.println(JSONStr.toString());
		return JSONStr.toString();
	}
	public SysJRrs getRolefile() {
		return rolefile;
	}
	public void setRolefile(SysJRrs rolefile) {
		this.rolefile = rolefile;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
}
