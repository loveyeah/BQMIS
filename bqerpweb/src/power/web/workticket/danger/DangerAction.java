package power.web.workticket.danger;

import java.util.ArrayList;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketDanger;
import power.ejb.workticket.RunCWorkticketDangerFacadeRemote;
import power.ejb.workticket.RunCWorkticketDangerTypeFacadeRemote;
import power.ejb.workticket.RunCWorkticketDangerType;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class DangerAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private RunCWorkticketDanger danger;
	private String dangerId;
	private Long dangerTypeId = 0l;
	private Long PDangerId = 0l;

	private RunCWorkticketDangerFacadeRemote dangerFacadeRemote;
	
	private RunCWorkticketDangerTypeFacadeRemote dangerTypeFacadeRemote;

	public RunCWorkticketDanger getDanger() {
		return danger;
	}

	public void setDanger(RunCWorkticketDanger danger) {
		this.danger = danger;
	}

	/**
	 * 构造函数
	 */
	public DangerAction() {
		dangerFacadeRemote = (RunCWorkticketDangerFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketDangerFacade");
		dangerTypeFacadeRemote = (RunCWorkticketDangerTypeFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketDangerTypeFacade");
	}

	/**
	 * 查询危险点
	 * 
	 * @throws JSONException
	 */
	public void getWorkticketDangerList() throws JSONException {

		PageObject object = new PageObject();
		// 取得查询条件：危险点类型
		String Id = request.getParameter("dangerTypeId");
		String PId = request.getParameter("PDangerId");
		if(Id != null ) {
			dangerTypeId = Long.parseLong(Id);
		}
		
		if(PId != null) {
			PDangerId = Long.parseLong(PId);
		}
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
			object = dangerFacadeRemote.findAll(Constants.ENTERPRISE_CODE,
					dangerTypeId, PDangerId,fuzzy, start, limit);
		} else {
			// 查询
			object = dangerFacadeRemote.findAll(Constants.ENTERPRISE_CODE,
					dangerTypeId,PDangerId, fuzzy);
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
	 * 增加危险点
	 * 
	 * @throws CodeRepeatException
	 */
	public void addWorkticketDanger() throws CodeRepeatException {

		// 设置企业编码
		danger.setEnterpriseCode(Constants.ENTERPRISE_CODE);
		// 设置添加者
		danger.setModifyBy(employee.getWorkerCode());
		
		String PId = request.getParameter("PDangerId");
		if(PId == null) {
			PDangerId = 0l;
		} else {
            PDangerId = Long.parseLong(PId);
		}
        danger.setPDangerId(PDangerId);
		try {
			// ID由数据库自动生成
			danger.setDangerId(null);
			
			// 添加
			dangerFacadeRemote.save(danger);
			write(Constants.ADD_SUCCESS);
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'增加失败:危险点名称和危险点类型重复！'}");
			throw e;
		} finally {
			// 设置画面上的数据
			danger.setDangerId(new Long(-1));
		}
	}

	/**
	 * 修改危险点类型
	 * 
	 * @throws CodeRepeatException
	 */
	public void updateWorkticketDanger() throws CodeRepeatException {

		RunCWorkticketDanger workticket = dangerFacadeRemote
				.findById(Long.valueOf(dangerId));
		
		String PId = request.getParameter("PDangerId");
		if(PId == null) {
			PDangerId = 0l;
		} else {
            PDangerId = Long.parseLong(PId);
		}
        danger.setPDangerId(PDangerId);
		// 设置企业编码
		workticket.setEnterpriseCode(Constants.ENTERPRISE_CODE);
		// 设置更新者
		workticket.setModifyBy(employee.getWorkerCode());
		// 设置危险点类型名称
		workticket.setDangerName(danger.getDangerName());
		// 设置危险点类型
		workticket.setDangerTypeId(danger.getDangerTypeId());
		// 设置排序号
		workticket.setOrderBy(danger.getOrderBy());
		try {
			// 更新
			dangerFacadeRemote.update(workticket);
			write(Constants.MODIFY_SUCCESS);
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'修改失败:危险点名称和危险点类型重复！'}");
			throw e;
		}
	}

	/**
	 * 批量删除
	 */
	public void deleteMultiWorkticketDanger() {
		// 取得要删除的记录的id字符串
		String ids = request.getParameter("ids");
		// 批量删除
		dangerFacadeRemote.deleteMulti(ids);
		write(Constants.DELETE_SUCCESS);
	}

	/**
	 * 危险点类型的取得
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getQueryDangerType() throws JSONException {
		// 根据企业编码取得所有工作票类型
		PageObject object = dangerTypeFacadeRemote.findAll(Constants.ENTERPRISE_CODE, "", "", null);
		// 查询结果为null
		if (object == null) {
			object.setList(new ArrayList<RunCWorkticketDangerType>());
		}
		// 添加"所有"选项
		RunCWorkticketDangerType runCDangerType = new RunCWorkticketDangerType();
		// 种类为"所有"
		runCDangerType.setDangerTypeName(Constants.ALL_SELECT);
		// 编码为空
//		runCDangerType.setDangerTypeId(Long.parseLong(Constants.BLANK_STRING));
		runCDangerType.setDangerTypeId(0l);
		// 添加
		object.getList().add(0, runCDangerType);
		write(JSONUtil.serialize(object));
	}

	public String getDangerId() {
		return dangerId;
	}

	public void setDangerId(String dangerId) {
		this.dangerId = dangerId;
	}

}
