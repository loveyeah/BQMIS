package power.ejb.webservice.run.ticketmanage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * Remote interface for RunJTaskInformationFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJTaskInformationFacadeRemote {
	
	/**
	 * 将远程调用WebService方法的信息记录在日志中
	 * @param entity
	 */
	public void save(RunJTaskInformation entity);
	
	/**
	 * 查询发送未成功的记录
	 * @return List
	 */
	public List<RunJTaskInformation> findListForResend();
	
	/**
	 * 修改发送未成功记录
	 * @param entity
	 * @return
	 */
	public RunJTaskInformation update(RunJTaskInformation entity);
	

}