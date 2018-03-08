package power.ejb.message;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.message.form.Message;

 /**
 * @author bjXu
 *
 */
@Remote
public interface SysJMessageEmpFacadeRemote {
	/**
	 * 新增一条消息
	 * @param entity 消息实体对象
	 * @return void 
	 */
	public void save(SysJMessageEmp entity);

	/**
	 * 删除一条消息
	 * @param entity 消息实体对象
	 * @return void 
	 */
	public void delete(SysJMessageEmp entity);

	/**
	 * 更新一条消息
	 * @param entity 消息实体对象
	 * @return void 
	 */
	public SysJMessageEmp update(SysJMessageEmp entity);

	public SysJMessageEmp findById(Long id);
	
	/**
	 * 根据消息ID查询已保存未发送的消息列表
	 * @param messageId 消息ID
	 * return List<SysJMessageEmp> 消息列表
	 */
	public List<SysJMessageEmp> findByMessageId(Long messageId);
	
	/**
	 * 根据消息Id与消息接收人code查询已保存未发送的消息实体
	 * @param messageId 消息Id
	 * @param reveiveId 接收人code
	 * return SysJMessageEmp
	 */
	public SysJMessageEmp findMessage(Long messageId,String reveiveId);
	/**
	 * 根据消息Id查询已保存未查看的消息实体
	 * @param messageId 消息Id
	 * return SysJMessageEmp
	 */
	public SysJMessageEmp findMessage(Long messageId);
	/**
	 * 根据接收人Id查询未查看消息数目
	 * @param receiveId 接收人Id(workerCode)
	 * return Long
	 */
	public Long getNoVeiwMessageCount(String receiveId);
}