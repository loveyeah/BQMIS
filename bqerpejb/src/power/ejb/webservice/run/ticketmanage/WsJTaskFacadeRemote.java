package power.ejb.webservice.run.ticketmanage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for WsJTaskFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface WsJTaskFacadeRemote {
	
	/**
	 * 任务最终接收时增加一条任务单
	 * @param entity
	 * @return
	 */
	public int save(WsJTask entity);

	/**
	 * 对已开票任务单进行删除
	 * @param taskNo
	 * @return
	 */
	public int delete(String taskNo);
	
	/**
	 * 查询任务单信息列表（用于工作票种任务单选择页面）
	 * @param taskNo 任务单号 （模糊查询）
	 * @param taskType 任务单类型
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数） 
	 * @return
	 */
	public PageObject findListForSelect(String taskNo,int taskType, String workerCode,int... rowStartIdxAndCount); 
	
}


