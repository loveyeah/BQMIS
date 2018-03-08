package power.web.workticket.danger;

import java.util.ArrayList;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketDangerType;
import power.ejb.workticket.RunCWorkticketDangerTypeFacadeRemote;
import power.ejb.workticket.RunCWorkticketType;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

public class DangerTypeAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private RunCWorkticketDangerType dangerType;
	private String dangerTypeId;

	private RunCWorkticketDangerTypeFacadeRemote dangerTypeFacadeRemote;

	private RunCWorkticketTypeFacadeRemote runCTypeRemote;

	public RunCWorkticketDangerType getDangerType() {
		return dangerType;
	}

	public void setDangerType(RunCWorkticketDangerType dangerType) {
		this.dangerType = dangerType;
	}

	/**
	 * 构造函数
	 */
	public DangerTypeAction() {
		dangerTypeFacadeRemote = (RunCWorkticketDangerTypeFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketDangerTypeFacade");
		runCTypeRemote = (RunCWorkticketTypeFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketTypeFacade");
	}

	/**
	 * 查询危险点类型
	 * 
	 * @throws JSONException
	 */
	public void getWorkticketDangerTypeList() throws JSONException {

		PageObject object = new PageObject();
		// 取得查询条件：工作票类型
		String workticketTypeCode = request.getParameter("workticketTypeCode");
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
			object = dangerTypeFacadeRemote.findAll(Constants.ENTERPRISE_CODE,
					workticketTypeCode, fuzzy, start, limit);
		} else {
			// 查询
			object = dangerTypeFacadeRemote.findAll(Constants.ENTERPRISE_CODE,
					workticketTypeCode, fuzzy);
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
	 * 增加危险点类型
	 * 
	 * @throws CodeRepeatException
	 */
	public void addWorkticketDangerType() throws CodeRepeatException {

		// 设置企业编码
		dangerType.setEnterpriseCode(Constants.ENTERPRISE_CODE);
		// 设置添加者
		dangerType.setModifyBy(employee.getWorkerCode());

		try {
			// ID由数据库自动生成
			dangerType.setDangerTypeId(null);
			// 添加
			dangerTypeFacadeRemote.save(dangerType);
			write(Constants.ADD_SUCCESS);
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'增加失败:危险点类型重复！'}");
			throw e;
		} finally {
			// 设置画面上的数据
			dangerType.setDangerTypeId(new Long(-1));
		}
	}

	/**
	 * 修改危险点类型
	 * 
	 * @throws CodeRepeatException
	 */
	public void updateWorkticketDangerType() throws CodeRepeatException {

		RunCWorkticketDangerType workticket = dangerTypeFacadeRemote
				.findById(Long.valueOf(dangerTypeId));
		// 设置企业编码
		workticket.setEnterpriseCode(Constants.ENTERPRISE_CODE);
		// 设置更新者
		workticket.setModifyBy(employee.getWorkerCode());
		// 设置危险点类型名称
		workticket.setDangerTypeName(dangerType.getDangerTypeName());
		// 设置危险点类型
		workticket.setWorkticketTypeCode(dangerType.getWorkticketTypeCode());
		// 设置排序号
		dangerType.setOrderBy(dangerType.getOrderBy());
		try {
			// 更新
			dangerTypeFacadeRemote.update(workticket);
			write(Constants.MODIFY_SUCCESS);
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'修改失败:l危险类型名称重复！'}");
			throw e;
		}
	}

	/**
	 * 批量删除
	 */
	public void deleteMultiWorkticketDangerType() {
		// 取得要删除的记录的id字符串
		String ids = request.getParameter("ids");
		// 批量删除
		dangerTypeFacadeRemote.deleteMulti(ids);
		write(Constants.DELETE_SUCCESS);
	}

	/**
	 * 工作票类型的取得
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getQueryWorkticketType() throws JSONException {
		// 根据企业编码取得所有工作票类型
		PageObject object = runCTypeRemote.findAll(Constants.ENTERPRISE_CODE);
		// 查询结果为null
		if (object == null) {
			object.setList(new ArrayList<RunCWorkticketType>());
		}
		// 添加"所有"选项
		RunCWorkticketType runCWorkticketType = new RunCWorkticketType();
		// 种类为"所有"
		runCWorkticketType.setWorkticketTypeName(Constants.ALL_SELECT);
		// 编码为空
		runCWorkticketType.setWorkticketTypeCode(Constants.BLANK_STRING);
		// 添加
		object.getList().add(0, runCWorkticketType);
		write(JSONUtil.serialize(object));
	}

	public String getDangerTypeId() {
		return dangerTypeId;
	}

	public void setDangerTypeId(String dangerTypeId) {
		this.dangerTypeId = dangerTypeId;
	}

}
