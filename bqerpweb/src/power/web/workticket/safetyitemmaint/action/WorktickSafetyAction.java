/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.workticket.safetyitemmaint.action;

import java.text.ParseException;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorktickSafety;
import power.ejb.workticket.RunCWorktickSafetyFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 工作票安全项目措施维护Action
 * @author zhangqi
 * @modify fyyang
 * @modify wzhyan 090317
 */
@SuppressWarnings("serial")
public class WorktickSafetyAction extends AbstractAction {
	/**远程服务*/
	protected RunCWorktickSafetyFacadeRemote remote;
	/**工作票类型编码，对应页面工作票类型组合框*/
	private String workticketTypeCode;
	/** 安全措施id*/
	private String safetyId;
	/** 安全措施编码*/
	private String safetyCode;
	/**安全措施描述*/
	private String safetyDesc;
	/** 挂牌内容ID*/
	private String markcard;
	/**工作票安全措施bean*/
	private RunCWorktickSafety safe;
	/**批量删除时safetyid组*/
	private String ids;

	/**
	 * 构造函数，初始化remote
	 */
	public WorktickSafetyAction() {
		remote =  (RunCWorktickSafetyFacadeRemote) factory
		.getFacadeRemote("RunCWorktickSafetyFacade");
	}
	/**
	 * 查找维护好的安措信息
	 */
	public void getMaintSafetyBy()
	{
		try {
			List<RunCWorktickSafety> list = remote.getMaintSafetyBy(employee
					.getEnterpriseCode(), workticketTypeCode);
			write(JSONUtil.serialize(list));
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success:false,msg:'" + exc.getMessage() + "'}");
		}
	}

	/**
	 * 查询
	 * @throws ParseException
	 * @throws JSONException
	 * @throws Exception
	 */
	public void searchSafety() throws ParseException, JSONException{
		PageObject result = remote.findAll(workticketTypeCode,Constants.ENTERPRISE_CODE);
		write(JSONUtil.serialize(result));
	}
	/**
	 * 删除
	 */
	public void deleteSafety() {
		try {
			remote.deleteMulti(ids);
			write("{success:true,msg:'删除成功！'}");
		} catch (Exception e) {
			write("{failure:true,msg:'删除失败！'}");
		}
	}
	/**
	 * 增加
	 */
	public void addSafety() {
		try{
			safe.setEnterpriseCode(Constants.ENTERPRISE_CODE);
			safe.setSafetyCode(safetyCode);
			safe.setSafetyDesc(safetyDesc);
			// start jincong 2008-12-27
			if(!Constants.BLANK_STRING.equals(markcard)) {
				safe.setMarkcardTypeId(Long.parseLong(markcard));
			}
			// end jincong 2008-12-27
			safe.setWorkticketTypeCode(workticketTypeCode);
			safe.setModifyBy(employee.getWorkerCode());

			RunCWorktickSafety safety = remote.save(safe);
			write(Constants.ADD_SUCCESS);
		}catch (Exception e){
			write("{success:true,msg:'安措编码重复！  '}");
		}
	}
	/**
	 * 修改
	 */
	public void updateSafety() {
		try{
			RunCWorktickSafety entity = remote.findById(Long.parseLong(safetyId));

			entity.setSafetyCode(safetyCode);
			entity.setSafetyDesc(safetyDesc);
			entity.setSafetyId(Long.parseLong(safetyId));
			entity.setWorkticketTypeCode(workticketTypeCode);
			// start jincong 2008-12-27
			if(!Constants.BLANK_STRING.equals(markcard)) {
				entity.setMarkcardTypeId(Long.parseLong(markcard));
			}
			// end jincong 2008-12-27
			entity.setModifyBy(employee.getWorkerCode());
			// start jincong 2008-12-27
		//	entity.setIsRunAdd(safe.getIsRunAdd());
			//modify by fyyang 090213
			entity.setSafetyType(safe.getSafetyType());
			// end jincong 2008-12-27
			RunCWorktickSafety safety = remote.update(entity);
			write(Constants.MODIFY_SUCCESS);
		}catch (Exception e){
			write(Constants.MODIFY_FAILURE);
		}
	}
	/**
	 *  获取工作票类型编码
	 */
	public String getWorkticketTypeCode() {
		return workticketTypeCode;
	}

	/**
	 * 设置工作票类型编码
	 * @param workticketTypeCode 工作票类型编码
	 */
	public void setWorkticketTypeCode(String workticketTypeCode) {
		this.workticketTypeCode = workticketTypeCode;
	}

	/**
	 * 获取安全措施id
	 */
	public String getSafetyId() {
		return safetyId;
	}

	/**
	 * 设置安全措施id
	 * @param safetyId 安全措施id
	 */
	public void setSafetyId(String safetyId) {
		this.safetyId = safetyId;
	}

	/**
	 * 获取工作票安全措施bean
	 */
	public RunCWorktickSafety getSafe() {
		return safe;
	}

	/**
	 * 设置工作票安全措施bean
	 * @param safe 工作票安全措施bean
	 */
	public void setSafe(RunCWorktickSafety safe) {
		this.safe = safe;
	}

	/**
	 * 获取安全措施编码
	 */
	public String getSafetyCode() {
		return safetyCode;
	}

	/**
	 * 设置安全措施编码
	 * @param safetyCode 安全措施编码
	 */
	public void setSafetyCode(String safetyCode) {
		this.safetyCode = safetyCode;
	}

	/**
	 * 获取安全措施描述
	 */
	public String getSafetyDesc() {
		return safetyDesc;
	}

	/**
	 * 设置安安全措施描述
	 * @param safetyDesc 安全措施描述
	 */
	public void setSafetyDesc(String safetyDesc) {
		this.safetyDesc = safetyDesc;
	}

	/**
	 * 获取挂牌内容ID
	 */
	public String getMarkcard() {
		return markcard;
	}

	/**
	 * 设置挂牌内容ID
	 * @param markcard 挂牌内容ID
	 */
	public void setMarkcard(String markcard) {
		this.markcard = markcard;
	}

	/**
	 * 获取safetyid组
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * 设置safetyid组
	 * @param ids safetyid组
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}
}
