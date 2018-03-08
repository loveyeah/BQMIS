package power.ejb.productiontec.relayProtection;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.relayProtection.form.ProtectNoticeForm;

/**
 * Remote interface for PtJdbhJDztzdFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJdbhJDztzdFacadeRemote {

	/**
	 * 增加一条继电保护定值登记信息
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public PtJdbhJDztzd save(PtJdbhJDztzd entity) throws CodeRepeatException;

	/**
	 * 删除一条或多条继电保护定值登记记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条继电保护定值登记记录
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public PtJdbhJDztzd update(PtJdbhJDztzd entity) throws CodeRepeatException;

	
	/**
	 * 根据ID查找一条继电保护定值登记记录信息
	 * @param id
	 * @return
	 */
	public PtJdbhJDztzd findById(Long id);
	
	/**
	 * 批量修改继电保护定值登记表信息
	 * @param list
	 * @param noticeId
	 * @throws CodeRepeatException
	 */
	public void modifyRecords(List<ProtectNoticeForm> list, String noticeId) throws CodeRepeatException ;

	/**
	 * 根据企业编码，时间段，装置名称查询列表
	 * @param enterpriseCode
	 * @param sDate
	 * @param eDate
	 * @param equName
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findRelayProtectionNoticeList(String enterpriseCode,String sDate,String eDate,String equName,final int... rowStartIdxAndCount);
	
	/**
	 * 根据继电保护定值通知单ID关联继电保护类型维护，继电定值项目维护，继电保护定值登记表查找列表
	 * @param enterpriseCode
	 * @param noticeId
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findNoticeDetailList(String enterpriseCode,String noticeId,final int... rowStartIdxAndCount);
}