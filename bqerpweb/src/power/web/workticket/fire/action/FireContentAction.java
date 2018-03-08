/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.workticket.fire.action;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketFireContent;
import power.ejb.workticket.RunCWorkticketFireContentFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 动火票内容处理Action
 * @author huyou
 *
 */
public class FireContentAction extends AbstractAction {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
    /** 动火票内容处理远程对象 */
    private RunCWorkticketFireContentFacadeRemote remote;
    /** 动火票内容 */
    private RunCWorkticketFireContent fireContent;
    
    /**
     * 构造函数
     */
    public FireContentAction () {
        // 取得动火票内容处理远程对象
        remote = (RunCWorkticketFireContentFacadeRemote) factory.getFacadeRemote("RunCWorkticketFireContentFacade");
    }
    
    /**
     * 取得动火票内容
     */
    public RunCWorkticketFireContent getFireContent() {
        return fireContent;
    }
    
    /**
     * 设置动火票内容
     * @param argFireContent 动火票内容
     */
    public void setFireContent(RunCWorkticketFireContent argFireContent) {
        fireContent = argFireContent;
    }

    /**
     * 获得动火票内容列表
     * @throws JSONException
     */
    public void getFireContentList() throws JSONException {
		String fuzzy = "";
		// 取得查询参数
		Object myobj = request.getParameter("fuzzy");
		if (myobj != null) {
			fuzzy = myobj.toString();
		}
		String enterpriseCode = Constants.ENTERPRISE_CODE;
		PageObject obj = new PageObject();
		// 查询动火票内容信息列表
		obj.setList(remote.findByNameOrId(enterpriseCode, fuzzy));
		// 记录总数
		obj.setTotalCount(new Long(obj.getList().size()));

		// 序列化为JSON对象的字符串形式
		String str = JSONUtil.serialize(obj);
		// 以html方式输出字符串
		write(str);
	}

    /**
	 * 增加动火票内容
	 * 
	 * @throws CodeRepeatException
	 */
    public void addFireContent() throws CodeRepeatException {
		fireContent.setEnterpriseCode(Constants.ENTERPRISE_CODE);

		try {
			fireContent.setFirecontentId(null);
			fireContent.setModifyBy(employee.getWorkerCode());
			// 增加一条动火票内容记录
			remote.save(fireContent);

			write(Constants.ADD_SUCCESS);
		} catch (CodeRepeatException e) {
			write(Constants.ADD_FAILURE);
			throw e;
		} finally {
			fireContent.setFirecontentId(new Long(-1));
		}
	}
    
    /**
	 * 修改动火票内容
	 */
    public void updateFireContent() throws CodeRepeatException {
		// 查找这条动火票内容记录
		RunCWorkticketFireContent model = remote.findById(fireContent
				.getFirecontentId());
		// 动火票内容名称
		model.setFirecontentName(fireContent.getFirecontentName());
		// 显示顺序
		model.setOrderBy(fireContent.getOrderBy());
		// 字段修改人modifyBy
		model.setModifyBy(employee.getWorkerCode());

		try {
			// 修改这条动火票内容记录
			remote.update(model);

			write(Constants.MODIFY_SUCCESS);
		} catch (CodeRepeatException e) {
			write(Constants.MODIFY_FAILURE);
			throw e;
		}
	}

	/**
	 * 删除动火票内容
	 */
	public void deleteFireContent() {
		// 从请求中获得删除的ID
		String ids = request.getParameter("ids");

		if (ids != null && ids.trim().length() > 0) {
			// 删除动火票内容记录
			remote.deleteMutil(ids);
		}

		write(Constants.DELETE_SUCCESS);
	}
}

