package power.webservice.ticketManager;

import java.rmi.Remote;

import power.ear.comm.CodeRepeatException;

public interface TicketManager extends Remote{
	
	/**
	 * 任务最终接收时创建任务单
	 * 
	 * @param taskType
	 * @param taskNo
	 * @param taskName
	 * @param receiver
	 * @return
	 * @throws CodeRepeatException
	 */
	public int createTask (int taskType,String taskNo,String taskName,String receiver) throws CodeRepeatException;
	
	/**
	 * 对已开票任务单进行作废
	 * @param taskNo
	 * @return
	 * @throws CodeRepeatException
	 */
	public int sendBlankOutTaskNo (String taskNo) throws CodeRepeatException;

}
