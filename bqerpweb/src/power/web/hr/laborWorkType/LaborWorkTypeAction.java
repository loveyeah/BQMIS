package power.web.hr.laborWorkType;

import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCLbgzbm;
import power.ejb.hr.HrCLbgzbmFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class LaborWorkTypeAction extends AbstractAction {

	private HrCLbgzbmFacadeRemote remote;
	private HrCLbgzbm type;

	public LaborWorkTypeAction() {
		remote = (HrCLbgzbmFacadeRemote) factory
				.getFacadeRemote("HrCLbgzbmFacade");
	}

	/**
	 * 增加一条劳保记录
	 */
	public void addLaborWorkType() {
		type.setEnterpriseCode(employee.getEnterpriseCode());
		type.setLastModifiedBy(Long.parseLong(employee.getWorkerCode()));

		try {
			type = remote.save(type);
			write("{success:true,id:'" + type.getLbWorkId() + "',msg:'增加成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'增加失败:名称不能重复！'}");
		}
	}

	/**、
	 *修改一条劳保记录
	 */
	public void updateLaborWorkType() {
		HrCLbgzbm model = remote.findById(type.getLbWorkId());
		model.setLbWorkName(type.getLbWorkName());
		model.setIfLbSpecialKind(type.getIfLbSpecialKind());
		model.setRetrieveCode(type.getRetrieveCode());
		model.setIsUse("Y");
		model.setLastModifiedBy(Long.parseLong(employee.getWorkerCode()));
		model.setLastModifiedDate(new Date());

		try {
			remote.update(model);
			write("{success:true,id:'" + type.getLbWorkId() + "',msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'修改失败:名称不能重复！'}");
		}
	}

	/**
	 * 删除一条或多条劳保记录
	 */
	public void deleteLaborWorkType() {
		String ids = request.getParameter("ids");
		remote.deletes(ids);
		write("{success:true,msg:'删除成功！'}");

	}

	/**
	 * 查找劳保记录列表记录
	 * @throws JSONException 
	 */
	public void findLaborWorkTypeList() throws JSONException {
		String typeName = request.getParameter("fuzzytext");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;

		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findAll(typeName, employee.getEnterpriseCode(),
					start, limit);
		} else {
			object = remote.findAll(typeName, employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";

		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}

	public HrCLbgzbm getType() {
		return type;
	}

	public void setType(HrCLbgzbm type) {
		this.type = type;
	}

}
