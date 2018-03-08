package power.web.equ.workbill.action;

import java.util.Date;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.workbill.EquCWoService;
import power.ejb.equ.workbill.EquCWoServiceFacadeRemote;
import power.ejb.equ.workbill.EquCWoServicetype;
import power.ejb.equ.workbill.EquCWoServicetypeFacadeRemote;
import power.ejb.equ.workbill.EquCWoTools;
import power.ejb.equ.workbill.EquCWoToolsFacadeRemote;
import power.ejb.equ.workbill.EquCWoTooltype;
import power.ejb.equ.workbill.EquCWoTooltypeFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class EquToolsAction extends AbstractAction {
	private EquCWoTooltype tooltype;
	private EquCWoTools tool;
	private Long node;
	private int start;
	private int limit;
	protected EquCWoTooltypeFacadeRemote remote;
	protected EquCWoToolsFacadeRemote tremote;

	public EquToolsAction() {
		remote = (EquCWoTooltypeFacadeRemote) factory
				.getFacadeRemote("EquCWoTooltypeFacade");
		tremote = (EquCWoToolsFacadeRemote) factory
				.getFacadeRemote("EquCWoToolsFacade");
	}

	// 服务类别新增
	public void addToolType() {
		try {
			if (remote.checkMarkCode(tooltype.getMarkCode()) == 0) {
				tooltype.setEnterpriseCode(employee.getEnterpriseCode());
				tooltype.setLastModifiedBy(employee.getWorkerCode());
				tooltype.setLastModifiedDate(new Date());
				EquCWoTooltype model = remote.save(tooltype);
				String str = JSONUtil.serialize(model);
				
				write("{success:true,type:" + str + "}");
			} else {
				write("{failure:true,msg:\"识别码重复，请重新填写!\"}");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 服务类别修改
	public void updateToolType() {

		EquCWoTooltype model = remote.findById(tooltype.getId());

		if ((!model.getMarkCode().equals(tooltype.getMarkCode()) && remote
				.checkMarkCode(tooltype.getMarkCode()) > 0)
				|| (model.getMarkCode().equals(tooltype.getMarkCode()) && remote
						.checkMarkCode(tooltype.getMarkCode()) > 1)) {
			write("{failure:true,msg:\"识别码重复，请重新填写!\"}");
		} else {
			model.setTypename(tooltype.getTypename());
			model.setMarkCode(tooltype.getMarkCode());
			model.setMemo(tooltype.getMemo());
			model.setEnterpriseCode(employee.getEnterpriseCode());
			model.setLastModifiedBy(employee.getWorkerCode());
			model.setLastModifiedDate(new Date());
			remote.update(model);

		}
	}

	// 服务类别删除
	public void deleteToolType() throws Exception {
		try {
			if (!remote.getByPtypeId(tooltype.getId())) {
				EquCWoTooltype model = remote.findById(tooltype.getId());
				model.setIsUse("N");
				model.setLastModifiedBy(employee.getWorkerCode());
				model.setLastModifiedDate(new Date());
				remote.update(model);
				write("{success:true}");
			} else {
				write("{failure:true,Errmsg:\"请先删除子节点！\"}");
			}
		} catch (Exception e) {

			write("{failure:true,Errmsg:\"操作失败！\"}");
			throw e;
		}

	}
	// 根据类型选工具
	public void getToolsByType() throws JSONException {
		String typeId = request.getParameter("typeId");
		String queryKey = request.getParameter("queryKey");
		String objstart = request.getParameter("start");
		String objlimit = request.getParameter("limit");
		int start = 0;
		int limit = 18; 
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit")); 
		}  
		PageObject obj = tremote.getToolsByType(typeId, queryKey, start,limit);
		if (obj != null) {
			write(JSONUtil.serialize(obj));
//			System.out.print(JSONUtil.serialize(obj));
		} else {
			write("{list : [],totalCount : 0}");
		}

	}
	// 类别树
	public void findByPId() {
		// String node = request.getParameter("node");
		List<EquCWoTooltype> list = remote.findByPtypeId(node);
		String str = toJsonStr(list);
		write(str);
	}

	public void findInfoById() {
		try {
			EquCWoTooltype model = remote.findById(node);
			String str = JSONUtil.serialize(model);
			write(str);
			System.out.print(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	private String toJsonStr(List<EquCWoTooltype> list) {
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("[");
		for (int i = 0; i < list.size(); i++) {
			EquCWoTooltype cct = list.get(i);
			boolean isLeaf = !remote.getByPtypeId(cct.getId());
			String icon = isLeaf ? "file" : "folder";
			JSONStr.append("{\"text\":\"" + cct.getTypename() + "\","
					+ "\"id\":\"" + cct.getId() + "\"," + "\"markCode\":\""
					+ cct.getMarkCode() + "\"," + "\"leaf\":" + isLeaf + ","
					+ "\"cls\":\"" + icon + "\"," + "\"enterpriseCode\":"
					+ "\"" + cct.getEnterpriseCode() + "\"},");
		}
		if (JSONStr.length() > 1) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
		JSONStr.append("]");
		return JSONStr.toString();
	}
	
//服务list
	public void getToolList() throws JSONException{
		PageObject pg = tremote.findAll(start,limit);
		String jsonstr = JSONUtil.serialize(pg);
//		System.out.print(jsonstr);
		write(jsonstr);
	}
// 服务新增
	public void addTool() {
		if(!tremote.checkName(tool.getName())){
		tool.setEnterpriseCode(employee.getEnterpriseCode());
		tremote.save(tool);
		write("{success:true,msg:'操作成功'}");
		} else{
			write("{failure:true,msg:'该服务已经存在!请重新输入!'}");
		}
	}
//服务修改
	public void updateTool(){
		EquCWoTools model = tremote.findById(tool.getId());
		if(!tremote.checkUpdateName(tool.getName(),model.getId())){
		model.setFee(tool.getFee());
		model.setName(tool.getName());
		model.setType(tool.getType());
		tremote.update(model);
		write("{success:true,msg:'操作成功'}");
		} else{
			write("{failure:true,msg:'该服务已经存在!请重新输入!'}");
		}
	}
//服务删除
	public void deleteTool(){
		String id = request.getParameter("id");
		String[] ids = id.split(",");
		for (int i = 0; i < ids.length; i++) {
			Long docTypeId = Long.parseLong(ids[i]);
			EquCWoTools model = tremote.findById(docTypeId);
			if (model != null) {
				tremote.delete(model);
			}
		}
	}
	public Long getNode() {
		return node;
	}

	public void setNode(Long node) {
		this.node = node;
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

	public EquCWoTooltype getTooltype() {
		return tooltype;
	}

	public void setTooltype(EquCWoTooltype tooltype) {
		this.tooltype = tooltype;
	}

	public EquCWoTools getTool() {
		return tool;
	}

	public void setTool(EquCWoTools tool) {
		this.tool = tool;
	}

}
