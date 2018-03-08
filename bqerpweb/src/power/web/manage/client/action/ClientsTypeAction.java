package power.web.manage.client.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.client.ConCClientsType;
import power.ejb.manage.client.ConCClientsTypeFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ClientsTypeAction extends AbstractAction {
	
	private ConCClientsTypeFacadeRemote typeRemote;
	private ConCClientsType type;

	public ClientsTypeAction() {
		typeRemote = (ConCClientsTypeFacadeRemote)factory.getFacadeRemote("ConCClientsTypeFacade");
	}
	
	/**
	 * 增加
	 */
	public void addClientsType()
	{
		type.setEnterpriseCode(employee.getEnterpriseCode());
		try{
		type = typeRemote.save(type);
		write("{success:true,id:'"+type.getTypeId()+"',msg:'增加成功！'}");
		}catch (CodeRepeatException e) {
			write("{success:true,msg:'增加失败:类型名称不能重复！'}");
		}
	}
	
	/**
	 * 修改
	 */
	public void updateClientsType() {

		ConCClientsType model = typeRemote.findById(type.getTypeId());
		model.setTypeName(type.getTypeName());
		model.setMemo(type.getMemo());
		try {
			typeRemote.update(model);
			write("{success:true,id:'"+type.getTypeId()+"',msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'修改失败:类型名称不能重复！'}");
		}
	}
	
	/**
	 * 删除
	 */
	public void deleteClientsType()
	{
		String ids = request.getParameter("ids");
		typeRemote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 根据输入条件查询合作伙伴类型信息
	 */
	public void getClientsTypeList() throws JSONException {
		//取得模糊查询条件
		String typeName = request.getParameter("fuzzytext");
		// 取得查询条件: 开始行
		Object objstart = request.getParameter("start");
		// 取得查询条件：结束行
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		// 判断是否为null
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			// 查询
			object = typeRemote.findAll(typeName, employee.getEnterpriseCode(),start, limit);
		} else {
			// 查询
			object = typeRemote.findAll(typeName,employee.getEnterpriseCode());
		}
		// 输出结果
		String strOutput = "";
		// 查询结果为null
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
			// 不为null
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}

	public ConCClientsType getType() {
		return type;
	}

	public void setType(ConCClientsType type) {
		this.type = type;
	}

}
