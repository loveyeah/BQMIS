package power.ejb.equ.workbill;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.equ.workbill.form.WorkticketInfo;

/**
 * Remote interface for EquJWorkticketsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJWorkticketsFacadeRemote {
	/**
	 * 增加
	 */
	//public void save(EquJWorktickets entity);

	/**
	 * 删除
	 */
	public void delete(EquJWorktickets entity);

	/**
	 * 修改
	 */
	public EquJWorktickets update(EquJWorktickets entity);

	public EquJWorktickets findById(Long id);
	
	/**
	 * 根据工单编号查找
	 * @param woCode 工单编号
	 * @return List<EquJWorktickets>
	 */
	public List<EquJWorktickets> findBywoCode(String woCode);
	
	/**
	 * 根据woCode与woticketCode删除
	 * @param String woCode
	 * @param String woticketCode
	 */
	public void deleteMutil(String woCode,String woticketCodes);
	/**
	 * 根据woCode查找对用所有的工作票编号
	 * @param woCode
	 * @return String woticketCode
	 */
	public String findAllWorkTicketCodeBywoCode(String woCode);
	/**
	 * 根据工单编号查找对应所有工作票详细列表
	 * @param workticketCode
	 * @return
	 */
	public List<WorkticketInfo> getWorkticketListByWorkticketCode(String woCode);
	/**
	 * 判断是否有重复的工作票号
	 * @param workticketNo
	 * @return
	 */
	public boolean checkSame(String workticketNo);
	/**
	 * 增加
	 * @param entity
	 * @return
	 */
	public boolean save(EquJWorktickets entity);
	public void delWorkticket(String busiNo,String entryId);
	public  String getWfSql(Long workflowNo);
}