/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.web.workticket.markcardmaint.action;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketMarkcard;
import power.ejb.workticket.RunCWorkticketMarkcardFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 标识牌维护Action
 * 
 * @author jincong
 * 
 */
public class MarkcardMaintAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 标识牌维护远程对象 */
	private RunCWorkticketMarkcardFacadeRemote remote;
	/** 标识牌 */
	private RunCWorkticketMarkcard markcard;

	/**
	 * 构造函数
	 */
	public MarkcardMaintAction() {
		// 取得标识牌处理远程对象
		remote = (RunCWorkticketMarkcardFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketMarkcardFacade");
	}

	/**
	 * 取得标识牌
	 * 
	 * @return 标识牌
	 */
	public RunCWorkticketMarkcard getMarkcard() {
		return markcard;
	}

	/**
	 * 设置标识牌
	 * 
	 * @param markcard
	 *            标识牌
	 */
	public void setMarkcard(RunCWorkticketMarkcard markcard) {
		this.markcard = markcard;
	}

	/**
	 * 查询标识牌
	 * 
	 * @throws JSONException
	 */
	public void getWorkticketMarkcard() throws JSONException {

		PageObject object = new PageObject();
		// 取得查询条件：标识牌类型
		String markcardTypeID = request.getParameter("markcardTypeID");
		// 取得查询条件：模糊查询字段
		String fuzzy = request.getParameter("fuzzy");
		// 取得查询条件: 开始行
		Object objstart = request.getParameter("start");
		// 取得查询条件：结束行
		Object objlimit = request.getParameter("limit");
		// 判断是否为null
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			// 查询
			object = remote.findAll(Constants.ENTERPRISE_CODE, Long
					.valueOf(markcardTypeID), fuzzy, start, limit);
		} else {
			// 查询
			object = remote.findAll(Constants.ENTERPRISE_CODE, Long
					.valueOf(markcardTypeID), fuzzy);
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
	 * 增加标识牌
	 * 
	 * @throws CodeRepeatException
	 */
	public void addWorkticketMarkcard() throws CodeRepeatException {

		// 设置企业编码
		markcard.setEnterpriseCode(Constants.ENTERPRISE_CODE);
		// 设置添加者
		markcard.setModifyBy(employee.getWorkerCode());

		try {
			// ID由数据库自动生成
			markcard.setMarkcardId(null);
			// 添加
			remote.save(markcard);
			write(Constants.ADD_SUCCESS);
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'增加失败:编号重复！'}");
			throw e;
		} finally {
			// 设置画面上的数据
			markcard.setMarkcardId(new Long(-1));
		}
	}

	/**
	 * 修改标识牌
	 * 
	 * @throws CodeRepeatException
	 */
	public void updateWorkticketMarkcard() throws CodeRepeatException {

		RunCWorkticketMarkcard workticket = remote.findById(markcard
				.getMarkcardId());
		// 设置企业编码
		workticket.setEnterpriseCode(Constants.ENTERPRISE_CODE);
		// 设置更新者
		workticket.setModifyBy(employee.getWorkerCode());
		// 设置标识牌编号
		workticket.setMarkcardCode(markcard.getMarkcardCode());
		// 设置标识牌类型
		workticket.setMarkcardTypeId(markcard.getMarkcardTypeId());
		// 设置排序号
		workticket.setOrderBy(markcard.getOrderBy());
		try {
			// 更新
			remote.update(workticket);
			write(Constants.MODIFY_SUCCESS);
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'修改失败:编号重复！'}");
			throw e;
		}
	}

	/**
	 * 批量删除
	 */
	public void deleteMultiMackcard() {
		// 取得要删除的记录的id字符串
		String ids = request.getParameter("ids");
		// 批量删除
		remote.deleteMulti(ids);
		write(Constants.DELETE_SUCCESS);
	}
}
