package power.web.system.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import power.ejb.comm.TreeNode;
import power.ejb.system.SysCFls;
import power.ejb.system.SysCFlsFacadeRemote;
import power.ejb.system.form.Menu;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class FileMaintenanceAction extends AbstractAction { 
	private static final long serialVersionUID = -3241108902924752155L;
	protected SysCFlsFacadeRemote remote;
	
	private Long node;
	private SysCFls fls; 
	private String catalogStr;
	private Long roleId;
	private String filelike;
	private Long workerId;
	private int start;
	private int limit;
	/**
	 * 构造函数
	 */
	public FileMaintenanceAction(){
		remote =(SysCFlsFacadeRemote) factory.getFacadeRemote("SysCFlsFacade");
	}
	/**
	 * 增加功能模块
	 */
	public void addFile()
	{
		fls.setEnterpriseCode(employee.getEnterpriseCode());
		fls.setModifyBy(employee.getWorkerCode());
		fls.setModifyDate(new Date());
		fls.setIsUse("Y"); 
		SysCFls file=remote.save(fls);
		String json="id:"+file.getFileId();
		write("{success: true, " + json + "}");
	}
	/**
	 * 修改功能模块
	 */
	public void updateFile()
	{ 
		SysCFls model = remote.findById(fls.getFileId());
		model.setFileAddr(fls.getFileAddr());
		model.setFileName(fls.getFileName());
		model.setIsFile(fls.getIsFile());
		model.setIsUse(fls.getIsUse());
		model.setFileType(fls.getFileType());
		model.setIsDisp(fls.getIsDisp());
		model.setMemo(fls.getMemo());
		model.setLine(fls.getLine());
		model.setModifyDate(new Date());  
		model.setIsUse("Y"); 
		remote.update(model); 
	}
	/**
	 * 删除功能模块
	 */
	public void deleteFile()
	{
		if(!remote.findFilesByPId(fls.getFileId())){
			SysCFls model = remote.findById(fls.getFileId());
			model.setIsUse("N");
			model.setModifyBy(employee.getWorkerCode());
			model.setModifyDate(new Date());
			remote.update(model);
			write("{success:true}");
		}
		else{
			write("{failure:true,Errmsg:\"请先删除子节点！\"}");
		}
		
	}
	/**
	 * 由主键查找功能模块详细信息
	 * @throws JSONException
	 */
	public void findFile() throws JSONException
	{
		SysCFls model = remote.findById(node); 
		write(JSONUtil.serialize(model));
	}
	/**
	 * 保存目录结构
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	 public void updateDDJson() throws JSONException
	   {	
//		   String ddJson = request.getParameter("catalogStr");
		   Object ooo = JSONUtil.deserialize(catalogStr);
		   List<Map> oo = (List<Map>)ooo;
		   for(Map o : oo)
		   {
		       String sid = ((Map) ((Map) o)).get("sid").toString();
		       String tid = ((Map) ((Map) o)).get("tid").toString(); 
		       SysCFls fls = remote.findById(Long.parseLong(sid));
		       fls.setParentFileId(Long.parseLong(tid));
		       remote.update(fls);
		   } 
	   }
//	public void saveCatalog() throws JSONException{ 
//	    Object catalogStrObje = JSONUtil.deserialize(catalogStr);
//	    List<Map> list = (List<Map>)catalogStrObje;
//	    remote.saveCatalog(list);
//	} 
	public void findfiles()throws Exception
	{
		List<SysCFls> list =  remote.findAll(node);
		String str = this.toJsonStr(list);
		write(str);
	}
	
	
	
	/**
	 * 查询功能模块
	 */
	public void findFilesByWorkerId() throws Exception
	{	String str="";
//		if("999999".equals(employee.getWorkerId())){
//			List<SysCFls> list =  remote.findAll(node);
//			str = this.toJsonStr(list);
//		}
//		else{
			List<SysCFls> list = remote.findFilesByWorkerId(node, employee.getEmpId());
			str = this.toJsonStr(list);
//		}
		write(str);
	} 
	
	/**
	 * 将list转换为json格式数据
	 * @param list
	 * @return
	 */
	private String toJsonStr(List<SysCFls> list) throws Exception
	{
//		StringBuffer JSONStr = new StringBuffer(); 
//		JSONStr.append("[");
		List<TreeNode> li=new ArrayList();
		
		for (int i = 0; i < list.size(); i++) {
			TreeNode model=new TreeNode();
			SysCFls o = list.get(i);
			boolean isLeaf = (o.getIsFile().equals("Y")) ? true : false;
			String icon = isLeaf ? "file" : "folder";
			String addr = "";
			if ("Y".equals(o.getIsFile())) {
				addr = (o.getFileAddr() == null) ? "" : (o.getFileAddr() + "?id=" + o.getFileId());
			}
			String openType = (o.getOpenType()==null?"1":o.getOpenType());
			String description = o.getMemo();
			model.setId(String.valueOf(o.getFileId()));
			model.setText(o.getFileName());
			model.setLeaf(isLeaf);
			model.setCls(icon);
			model.setDescription(description);
			model.setHref(addr);
			model.setOpenType(openType);
			li.add(model);
//			JSONStr.append("{\"text\":\"" + o.getFileName()
//					+ "\",\"id\":\"" + o.getFileId() + "\",\"leaf\":"
//					+ isLeaf + ",\"cls\":\"" + icon
//					+ "\",\"description\":\"" + description
//					+ "\",\"openType\":\"" + openType + "\",\"href\":\""
//					+ addr + "\"},");
			 
		}
//		if (JSONStr.length() > 1) {
//			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
//		}
//		JSONStr.append("]"); 
		return JSONUtil.serialize(li);
	}
	private String toTreeJsonStr(List<SysCFls> list) throws Exception
	{
//		StringBuffer JSONStr = new StringBuffer(); 
//		JSONStr.append("[");
		List<TreeNode> li=new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			TreeNode model=new TreeNode();
			SysCFls o = list.get(i);
			boolean isLeaf = (o.getIsFile().equals("Y")) ? true : false;
			String icon = isLeaf ? "file" : "folder";
			String addr = "";
			if ("Y".equals(o.getIsFile())) {
				addr = (o.getFileAddr() == null) ? "" : (o.getFileAddr() + "?id=" + o.getFileId());
			}
			String openType = (o.getOpenType()==null?"1":o.getOpenType());
			String description = o.getMemo();
			model.setId(String.valueOf(o.getFileId()));
			model.setText(o.getFileName());
			model.setLeaf(isLeaf);
			model.setCls(icon);
			model.setDescription(description);
			model.setHref(addr);
			model.setOpenType(openType);
			li.add(model);}
//			JSONStr.append("{\"text\":\"" + o.getFileName()
//					+ "\",\"id\":\"" ;+ o.getFileId() + "\",\"leaf\":"
//					+ isLeaf + ",\"cls\":\"" + icon
//					+ "\",\"description\":\"" + description
//					+ "\",\"openType\":\"" + openType +"\"},");
//		}
//		if (JSONStr.length() > 1) {
//			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
//		}
//		JSONStr.append("]"); 
//		return JSONStr.toString();
		return JSONUtil.serialize(li);
	}
	public void findFilesByRoleId() throws Exception{
		List<SysCFls> list=remote.findFileByPRoleId(roleId, node,true);
		write(toTreeJsonStr(list));
	}
	
	
	public void findgridFilesByRoleId() throws Exception{
		if(filelike==null && filelike.length()<1){
			filelike="%";
		}
		List<SysCFls> list=remote.findByroleIdP(roleId,"file_name",filelike,false);
		if(list!=null){
			String retStr= JSONUtil.serialize(list);
			write("{success:true,data:{list:"+retStr+"}}");
		}
		else
			write("{success:true,data:{list:[]}}");
	}
	public void findWaitFilesByBoleId() throws Exception{
		List<SysCFls> list=remote.findFileByPRoleId(roleId, node,false);
		write(toTreeJsonStr(list));
	}
	public void findFileBywId() throws Exception {
		List<SysCFls> list = remote.findFileBywId(workerId);
		if(list!=null){
			String rolesStr = JSONUtil.serialize(list);
			write("{list:"+rolesStr+"}");
		}
		else{
			write("{list:[]}");
		}
	}
	public void findMenusByWorkerId() {
		try{ 
			Menu menu = remote.findMenusByWorkerId(employee.getEnterpriseCode(), employee.getEmpId());
			write(JSONUtil.serialize(menu.getMenu()));
		}catch(Exception exc)
		{
			exc.printStackTrace();
			write("{success:false}");
		}
	}
	
	public Long getNode() {
		return node;
	}
	public void setNode(Long node) {
		this.node = node;
	}
	public SysCFls getFls() {
		return fls;
	}
	public void setFls(SysCFls fls) {
		this.fls = fls;
	}
	public String getCatalogStr() {
		return catalogStr;
	}
	public void setCatalogStr(String catalogStr) {
		this.catalogStr = catalogStr;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getFilelike() {
		return filelike;
	}
	public void setFilelike(String filelike) {
		this.filelike = filelike;
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
}
