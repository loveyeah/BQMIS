/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.workticket.sourcemaint.action;

import java.util.List;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketSource;
import power.ejb.workticket.RunCWorkticketSourceFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
/**
 * 工作票来源action
 * @author zhangqi
 */
public class RunCWorkticketSourceAction extends AbstractAction {
	/** serial id*/
	private static final long serialVersionUID = 1L;
	/**远程服务*/
	private RunCWorkticketSourceFacadeRemote remote;
	/** 查询字符串*/
	private String fuzzy = "";
	/**工作票来源ID集*/
	private String sourceIds = "";
	/** 工作票来源对象*/
	private RunCWorkticketSource source;
	/** 编码*/
	private String sourceIdHidden;

	/**
	 * 初始化remote
	 */
	public RunCWorkticketSourceAction(){
		// remote初始化
		remote =  (RunCWorkticketSourceFacadeRemote)
			factory	.getFacadeRemote("RunCWorkticketSourceFacade");
	}


	/**
	 * 查询工作票来源列表
	 * @throws JSONException  json转换异常
	 */
	public void getSourceList()throws JSONException{
		// 查询
		List<RunCWorkticketSource> result = remote.findByNameOrId(Constants.ENTERPRISE_CODE, fuzzy);
		PageObject obj = new PageObject();
		obj.setList(result);
		// 返回
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 批量删除工作票来源
	 */
	public void deleteSource(){
		try{
			remote.deleteMutil(sourceIds);
			write(Constants.DELETE_SUCCESS);
		}catch(Exception e){
			write("{success:true,msg:'删除失败:未知错误！'}");
		}
	}

	/**
	 * 增加工作票来源
	 */
	public void addSource(){
		try{
			// 企业编码
			source.setEnterpriseCode(Constants.ENTERPRISE_CODE);
			// 修改者
			source.setModifyBy(employee.getWorkerCode());
			// 增加
			remote.save(source);
			write(Constants.ADD_SUCCESS);
		}catch(CodeRepeatException e){
			write(Constants.ADD_FAILURE);
		}
	}

	/**
	 * 修改工作票来源
	 */
	public void updateSource(){
		try{
			// 查找要修改的记录
			RunCWorkticketSource entity = remote.findById(Long.parseLong(sourceIdHidden));
			// 设定修改值
			entity.setSourceName(source.getSourceName());
			entity.setOrderBy(source.getOrderBy());
			// 修改
			remote.update(entity);
			write(Constants.MODIFY_SUCCESS);
		}catch(CodeRepeatException e){
			write(Constants.MODIFY_FAILURE);
		}
	}
	/**
	 * 获取查询字符串
	 */
	public String getFuzzy() {
		return fuzzy;
	}
	/**
	 * 设置查询字符串
	 * @param fuzzy 查询字符串
	 */
	public void setFuzzy(String fuzzy) {
		this.fuzzy = fuzzy;
	}

	/**
	 * 获取工作票来源ID集
	 */
	public String getSourceIds() {
		return sourceIds;
	}
	/**
	 * 设置工作票来源ID集
	 * @param sourceIds 工作票来源ID集
	 */
	public void setSourceIds(String sourceIds) {
		this.sourceIds = sourceIds;
	}
	/**
	 * 获取工作票来源对象
	 */
	public RunCWorkticketSource getSource() {
		return source;
	}
	/**
	 * 设置工作票来源对象
	 * @param source 工作票来源对象串
	 */
	public void setSource(RunCWorkticketSource source) {
		this.source = source;
	}
	/**
	 * 获取编码
	 */
	public String getSourceIdHidden() {
		return sourceIdHidden;
	}
	/**
	 * 设置编码
	 * @param sourceIdHidden 编码
	 */
	public void setSourceIdHidden(String sourceIdHidden) {
		this.sourceIdHidden = sourceIdHidden;
	}

}
