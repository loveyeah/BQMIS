package power.web.manage.project.action;

import java.util.Date;
import java.util.List;

import power.ejb.manage.contract.ConCConType;
import power.ejb.manage.contract.ConCConTypeFacadeRemote;
import power.ejb.manage.contract.form.ContypeInfo;
import power.ejb.manage.project.PrjCType;
import power.ejb.manage.project.PrjCTypeFacadeRemote;
import power.ejb.system.SysCFls;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONUtil;

public class ProjectTypeMaintAction extends AbstractAction {
	private PrjCType prjtype;
	private Long node;
	protected PrjCTypeFacadeRemote remote;

	public ProjectTypeMaintAction() {
		remote = (PrjCTypeFacadeRemote) factory
				.getFacadeRemote("PrjCTypeFacade");
	}

	public void addPrjType() {
		try {
			if (remote.checkMarkCode(prjtype.getMarkCode()) == 0) {

				prjtype.setEnterpriseCode(employee.getEnterpriseCode());
				prjtype.setLastModifiedBy(employee.getWorkerCode());
				prjtype.setLastModifiedDate(new Date());

				PrjCType model = remote.save(prjtype);
				String str = JSONUtil.serialize(model);
				write("{success:true,type:" + str + "}");
			} else {
				write("{failure:true,msg:\"识别码重复，请重新填写!\"}");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updatePrjType() {

		PrjCType model = remote.findById(prjtype.getPrjTypeId());
           
		
		if ((!model.getMarkCode().equals(prjtype.getMarkCode()) && remote
				.checkMarkCode(prjtype.getMarkCode()) > 0)
				|| (model.getMarkCode().equals(prjtype.getMarkCode()) && remote
						.checkMarkCode(prjtype.getMarkCode()) > 1)) {
			write("{failure:true,msg:\"识别码重复，请重新填写!\"}");
		} else {
			model.setPrjTypeName(prjtype.getPrjTypeName());

			model.setMarkCode(prjtype.getMarkCode());
			model.setMemo(prjtype.getMemo());
			model.setEnterpriseCode(employee.getEnterpriseCode());
			model.setLastModifiedBy(employee.getWorkerCode());
			model.setLastModifiedDate(new Date());
			remote.update(model);

		}
	}

	public void deletePrjType() throws Exception {
		try {
			if (!remote.getByPPrjtypeId(prjtype.getPrjTypeId())) {
				PrjCType model = remote.findById(prjtype.getPrjTypeId());
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

	public void findByPId() {
//		String node = request.getParameter("node");
		List<PrjCType> list = remote.findByPPrjtypeId(node);
		String str = toJsonStr(list);
		write(str);
	}

	public void findInfoById() {
		try {
			PrjCType model = remote.findById(node);
			String str = JSONUtil.serialize(model);
			write(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String toJsonStr(List<PrjCType> list) {
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("[");
		for (int i = 0; i < list.size(); i++) {
			PrjCType cct = list.get(i);
			boolean isLeaf = !remote.getByPPrjtypeId(cct.getPrjTypeId());
			String icon = isLeaf ? "file" : "folder";
			JSONStr.append("{\"text\":\"" + cct.getPrjTypeName() + "\","
					+ "\"id\":\"" + cct.getPrjTypeId() + "\","
					+ "\"markCode\":\"" + cct.getMarkCode() + "\","
					+ "\"leaf\":" + isLeaf + "," + "\"cls\":\"" + icon + "\","
					+ "\"enterpriseCode\":" + "\"" + cct.getEnterpriseCode()
					+ "\"},");
		}
		if (JSONStr.length() > 1) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
		JSONStr.append("]");
		return JSONStr.toString();
	}
	
	public void findPrjTypeById(){
		String result = remote.findPrjTypeById(node);
		this.write("{prjType:'"+result+"'}");
	}

	public Long getNode() {
		return node;
	}

	public void setNode(Long node) {
		this.node = node;
	}

	public PrjCType getPrjtype() {
		return prjtype;
	}

	public void setPrjtype(PrjCType prjtype) {
		this.prjtype = prjtype;
	}
}
