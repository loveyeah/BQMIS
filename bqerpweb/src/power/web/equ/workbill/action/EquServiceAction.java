package power.web.equ.workbill.action;

import java.util.Date;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.workbill.EquCWoService;
import power.ejb.equ.workbill.EquCWoServiceFacadeRemote;
import power.ejb.equ.workbill.EquCWoServicetype;
import power.ejb.equ.workbill.EquCWoServicetypeFacadeRemote;
import power.ejb.manage.project.PrjCType;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class EquServiceAction extends AbstractAction {
	private EquCWoServicetype sertype;
	private EquCWoService service;
	private Long node;
	private int start;
	private int limit;
	protected EquCWoServicetypeFacadeRemote remote;
	protected EquCWoServiceFacadeRemote sremote;

	public EquServiceAction() {
		remote = (EquCWoServicetypeFacadeRemote) factory
				.getFacadeRemote("EquCWoServicetypeFacade");
		sremote = (EquCWoServiceFacadeRemote) factory
				.getFacadeRemote("EquCWoServiceFacade");
	}

	// 服务类别新增
	public void addServiceType() {
		try {
			if (remote.checkMarkCode(sertype.getMarkCode()) == 0) {
				sertype.setEnterpriseCode(employee.getEnterpriseCode());
				sertype.setLastModifiedBy(employee.getWorkerCode());
				sertype.setLastModifiedDate(new Date());

				EquCWoServicetype model = remote.save(sertype);
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
	public void updateServiceType() {

		EquCWoServicetype model = remote.findById(sertype.getId());

		if ((!model.getMarkCode().equals(sertype.getMarkCode()) && remote
				.checkMarkCode(sertype.getMarkCode()) > 0)
				|| (model.getMarkCode().equals(sertype.getMarkCode()) && remote
						.checkMarkCode(sertype.getMarkCode()) > 1)) {
			write("{failure:true,msg:\"识别码重复，请重新填写!\"}");
		} else {
			model.setTypename(sertype.getTypename());
			model.setMarkCode(sertype.getMarkCode());
			model.setMemo(sertype.getMemo());
			model.setEnterpriseCode(employee.getEnterpriseCode());
			model.setLastModifiedBy(employee.getWorkerCode());
			model.setLastModifiedDate(new Date());
			remote.update(model);

		}
	}

	// 服务类别删除
	public void deleteServiceType() throws Exception {
		try {
			if (!remote.getByPtypeId(sertype.getId())) {
				EquCWoServicetype model = remote.findById(sertype.getId());
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
	// 根据类型选服务
	public void getServiceByType() throws JSONException {
		String typeId = request.getParameter("typeId");
		String queryKey = request.getParameter("queryKey");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		PageObject obj = sremote.getServiceByType(typeId, queryKey, start,limit);
		if (obj != null) {
			write(JSONUtil.serialize(obj));
//			System.out.print(JSONUtil.serialize(obj));
		} else {
			write("{list : [],totalCount : 0}");
		}

	}

	// 类别树
	public void findBySPId() {
		// String node = request.getParameter("node");
		List<EquCWoServicetype> list = remote.findByPtypeId(node);
		String str = toJsonStr(list);
		write(str);
	}

	public void findInfoBySId() {
		try {
			EquCWoServicetype model = remote.findById(node);
			String str = JSONUtil.serialize(model);
			write(str);
			System.out.print(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	private String toJsonStr(List<EquCWoServicetype> list) {
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("[");
		for (int i = 0; i < list.size(); i++) {
			EquCWoServicetype cct = list.get(i);
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
	public void getServiceList() throws JSONException{
		PageObject pg = sremote.findAll(start,limit);
		String jsonstr = JSONUtil.serialize(pg);
//		System.out.print(jsonstr);
		write(jsonstr);
	}
// 服务新增
	public void addService() {
		if(!sremote.checkName(service.getName())){
		service.setEnterpriseCode(employee.getEnterpriseCode());
		sremote.save(service);
		write("{success:true,msg:'操作成功'}");
		} else{
			write("{failure:true,msg:'该服务已经存在!请重新输入!'}");
		}
	}
//服务修改
	public void updateService(){
		EquCWoService model = sremote.findById(service.getId());
		if (!sremote.checkUpdateName(service.getName(),model.getId())) {
			model.setFee(service.getFee());
			model.setName(service.getName());
			model.setType(service.getType());
			sremote.update(model);
			write("{success:true,msg:'操作成功'}");
		} else {
			write("{failure:true,msg:'该服务已经存在!请重新输入!'}");
		}
	}
//服务删除
	public void deleteService(){
		String id = request.getParameter("id");
		String[] ids = id.split(",");
		for (int i = 0; i < ids.length; i++) {
			Long docTypeId = Long.parseLong(ids[i]);
			EquCWoService model = sremote.findById(docTypeId);
			if (model != null) {
				sremote.delete(model);
			}
		}
	}
	public Long getNode() {
		return node;
	}

	public void setNode(Long node) {
		this.node = node;
	}

	public EquCWoServicetype getSertype() {
		return sertype;
	}

	public void setSertype(EquCWoServicetype sertype) {
		this.sertype = sertype;
	}

	public EquCWoService getService() {
		return service;
	}

	public void setService(EquCWoService service) {
		this.service = service;
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

}
