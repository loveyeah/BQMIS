package power.ejb.message;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.message.form.Message;
import power.ejb.message.form.WorkerInfo;
@Remote 
public interface MessageInterface {
	/**
	 * 根据登录人工号，时间查询消息列表
	 * @param wokerCode 工号
	 * @param start 开始时间
	 * @param end 结束时间
	 * @param statusFlag 状态标识集(
	 * 例如：（0，1，2)）
	 * @return List<SysJMessageEmp> 
	 */
//	public  List<Message> findMessageList(String wokerCode,String start,String end,String statusFlag);
	public  PageObject findMessageList(String wokerCode,String starttime,String endtime,String statusFlag,int... rowStartIdxAndCount);
	/**
	 * 根据登录人工号，时间查询消息列表
	 * @param wokerCode 工号
	 * @param start 开始时间
	 * @param end 结束时间
	 * @param statusFlag 状态标识集(例如：（0，1，2)）
	 * @param rowStartIdxAndCount 页面行数大小限制
	 * @return List<SysJMessageEmp> 
	 */
	public  PageObject findReceiveMessageList(String wokerCode,String start,String end,String statusFlag,int... rowStartIdxAndCount);
	/**
	 * 保存，发送消息
	 * @param sysmessage 消息实体
	 * @param List<SysJMessageEmp> list消息对应人实体列表
	 */

	public void saveMessage(SysJMessage sysmessage, List<SysJMessageEmp> list);
	
	
	/*
	*判断登录人
	*@param workerCode 工号
	*@return List<WorkerInfo> 
	*/
	public List<WorkerInfo> judgeAdminCus(String workerCode);
//	public boolean judgeAdminCus(String workerCode);
}
