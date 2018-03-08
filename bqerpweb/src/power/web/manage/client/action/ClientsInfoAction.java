package power.web.manage.client.action;

import java.util.Date;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.client.ConJClientsInfo;
import power.ejb.manage.client.ConJClientsInfoFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class ClientsInfoAction extends AbstractAction {
	
	private ConJClientsInfo base;
	
	private ConJClientsInfoFacadeRemote clientsInfoRemote;

	public ClientsInfoAction() {
		clientsInfoRemote = (ConJClientsInfoFacadeRemote)factory.getFacadeRemote("ConJClientsInfoFacade");
	}
	
	/**
	 * 保存一条合作伙伴信息
	 */
	public void saveClientsInfo() throws CodeRepeatException {
		base.setLastModifiedBy(employee.getWorkerCode());
		base.setEnterpriseCode(employee.getEnterpriseCode());
		base.setApproveFlag(1l);
		try {
			if(clientsInfoRemote.save(base) != null) {
				write(Constants.ADD_SUCCESS);
			};
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'增加失败:合作伙伴名称重复！'}");
//			throw e;
		}
	}
	
	/**
	 * 更新一条合作伙伴信息
	 */
	public void updateClientsInfo() throws CodeRepeatException {
		String cliendId = request.getParameter("cliendId");
		if(base != null ) {
			base.setCliendId(Long.parseLong(cliendId));
			base.setEnterpriseCode(employee.getEnterpriseCode());
			base.setLastModifiedBy(employee.getWorkerCode());
			base.setLastModifiedDate(new Date());
			try {
				if(clientsInfoRemote.update(base) != null) {
					write(Constants.MODIFY_SUCCESS);
				}
			} catch (CodeRepeatException e) {
				write("{success:true,msg:'增加失败:合作伙伴名称重复！'}");
//				throw e;
			}
		} else {
			// 将合作伙伴启用或者禁用(中止)
			ConJClientsInfo entity = clientsInfoRemote.findById(Long.valueOf(cliendId));
			if(entity.getApproveFlag() == 1) {
				entity.setApproveFlag(2l);
			} else if(entity.getApproveFlag() == 2) {
				entity.setApproveFlag(1l);
			}
			if(clientsInfoRemote.update(entity) != null) {
				write(Constants.MODIFY_SUCCESS);
			}
		}
		
	}
	
	/**
	 * 删除合作伙伴信息
	 */
	public void deleteClientsInfo() {
		String cliendId = request.getParameter("cliendId");
		int exeRow = clientsInfoRemote.delete(cliendId);
		if(exeRow > 0) {
			write(Constants.DELETE_SUCCESS);
		} else {
			write(Constants.DELETE_FAILURE);
		}
	}
	
	/**
	 * 查询合作伙伴信息
	 * @return
	 */
	public void getClientsInfoList() throws JSONException {
		String clientCodeOrClientName = request.getParameter("fuzzyText");
		String approveFlag = request.getParameter("approveFlag");
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
			object = clientsInfoRemote.findAll(clientCodeOrClientName, approveFlag, employee.getEnterpriseCode(), start, limit);
		} else {
			// 查询
			object = clientsInfoRemote.findAll(clientCodeOrClientName, approveFlag, employee.getEnterpriseCode());
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
	
	public void findClientInfoById() throws JSONException
	{
		Long supplyId = Long.parseLong(request.getParameter("supplyId"));
		ConJClientsInfo entity = clientsInfoRemote.findById(supplyId);
		String str = JSONUtil.serialize(entity);
		write(str);
	}

	/**
	 * @return the base
	 */
	public ConJClientsInfo getBase() {
		return base;
	}

	/**
	 * @param base the base to set
	 */
	public void setBase(ConJClientsInfo base) {
		this.base = base;
	}
	
}
