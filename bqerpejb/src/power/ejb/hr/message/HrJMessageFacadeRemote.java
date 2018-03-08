package power.ejb.hr.message;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJMessageFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJMessageFacadeRemote {
	
	/**
	 * 短信通知保存方法
	 * @param entity
	 */
	public void save(HrJMessage entity);

	
	/**
	 * 短信通知删除方法
	 * @param ids
	 */
	public void delete(String ids);

	/**
	 * 短信通知修改方法
	 * @param entity
	 * @return
	 */
	public HrJMessage update(HrJMessage entity);

	/**
	 * 短信通知通过主键获得短信通知信息
	 * @param id
	 * @return
	 */
	public HrJMessage findById(Long id);

	/**
	 * 短信通知查询列表
	 * @param workerCode
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	
	public PageObject getHrJMessageList(String workerCode, String enterpriseCode,
			 int... rowStartIdxAndCount);
	
	/**
	 *  短信通知发送方法
	 * @param messageId
	 */
	public void sendMessage(String messageId) ;
}