package power.web.run.securityproduction.action;

import java.util.Iterator;
import java.util.List;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.SpJSafecap;
import power.ejb.run.securityproduction.SpJSafecapFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class SafeCapOfferAction extends AbstractAction{
	
	private SpJSafecap safecap;
	
	private SpJSafecapFacadeRemote safecapRemote;
	
	public SafeCapOfferAction() {    
		safecapRemote = (SpJSafecapFacadeRemote)factory.getFacadeRemote("SpJSafecapFacade");
	}

	
	/**
	 * 增加一条安全帽领用信息
	 */
	public void addSafeCapInfo() throws CodeRepeatException {
		safecap.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			safecapRemote.save(safecap);
			write(Constants.ADD_SUCCESS);
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'增加失败:安全帽编号不能重复！'}");
//			throw e;
		} finally {
			// 设置画面上的数据
		}
		
	}
	
	/**
	 * 修改一条安全帽领用信息
	 */
	public void updateSafeCapInfo() throws CodeRepeatException {
		
		String safecapOfferId = request.getParameter("safecapOfferId");
		SpJSafecap entity = safecapRemote.findById(Long.valueOf(safecapOfferId));
		// 设置企业编码
		entity.setEnterpriseCode(Constants.ENTERPRISE_CODE);
		entity.setSafeCapNo(safecap.getSafeCapNo());
		entity.setSafeCapType(safecap.getSafeCapType());
		entity.setSafeCapColor(safecap.getSafeCapColor());
		entity.setRecipientsBy(safecap.getRecipientsBy());
		if(!"".equals(safecap.getOfferBy()) && safecap.getOfferBy() != null ) {
			entity.setOfferBy(safecap.getOfferBy());
		}
		entity.setRecipientsBy(safecap.getRecipientsBy());
		if(!"".equals(safecap.getWriteBy()) && safecap.getWriteBy() != null) {
			entity.setWriteBy(safecap.getWriteBy());
		}
		entity.setRecipientsDate(safecap.getRecipientsDate());
		entity.setMemo(safecap.getMemo());
		try {
			// 更新
			safecapRemote.update(entity);
			write(Constants.MODIFY_SUCCESS);
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'修改失败:安全帽编号不能重复！'}");
//			throw e;
		}
	}
	
	
	/**
	 * 删除一个或多个安全帽领用信息
	 */
	public void deleteSafeCapInfo() {
		// 取得要删除的记录的id字符串
		String ids = request.getParameter("ids");
		// 批量删除
		safecapRemote.deleteMulti(ids);
		write(Constants.DELETE_SUCCESS);
	}
	
	/**
	 * 根据领用人姓名或者安全帽编号模糊查询安全帽领用信息列表
	 */
	public void findSafeCapList() throws JSONException {
		
		PageObject object = new PageObject();
		
		String fuzzytext = request.getParameter("fuzzytext");
		// 取得查询条件: 开始行
		Object objstart = request.getParameter("start");
		// 取得查询条件：结束行
		Object objlimit = request.getParameter("limit");
		// 判断是否为null
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			// 查询
			object = safecapRemote.findAll(fuzzytext,Constants.ENTERPRISE_CODE, start, limit);
		} else {
			// 查询
			object = safecapRemote.findAll(fuzzytext,Constants.ENTERPRISE_CODE);
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
	
	/**
	 * 根据人名查询工号和所在的部门名称
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getDeptNameAndWorkCode() throws JSONException {
		String workName = request.getParameter("workName");
		List list = safecapRemote.getDeptNameAndWorkCode(workName);
		Iterator it = list.iterator();
		// 输出结果
		String strOutput = "";
		while(it.hasNext()) {
			Object[] data = (Object[])it.next();
			strOutput = JSONUtil.serialize(data);
		}
		write(strOutput);
	}


	/**
	 * @return the safecap
	 */
	public SpJSafecap getSafecap() {
		return safecap;
	}


	/**
	 * @param safecap the safecap to set
	 */
	public void setSafecap(SpJSafecap safecap) {
		this.safecap = safecap;
	}
}
