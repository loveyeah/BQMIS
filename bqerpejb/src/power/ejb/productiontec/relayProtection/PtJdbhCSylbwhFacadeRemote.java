package power.ejb.productiontec.relayProtection;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 实验类别保护 
 * 
 * @author fyyang 090713
 */
@Remote
public interface PtJdbhCSylbwhFacadeRemote {
	/**
	 * 增加一条实验类别信息
	 * @param entity
	 * @return
	 * @throws CodeRepeatException 
	 */
	public PtJdbhCSylbwh save(PtJdbhCSylbwh entity) throws CodeRepeatException;

	/**
	 * 删除一条或多条实验类别信息
	 * @param ids
	 */
	public void deleteMulti(String ids);
	/**
	 * 修改一条实验类别信息
	 * @param entity
	 * @return
	 * @throws CodeRepeatException 
	 */
	public PtJdbhCSylbwh update(PtJdbhCSylbwh entity) throws CodeRepeatException;

	/**
	 * 查找一条实验类别信息
	 * @param id
	 * @return
	 */
	public PtJdbhCSylbwh findById(Long id);
	
	/**
	 * 查找实验类别信息列表
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String enterpriseCode,final int... rowStartIdxAndCount);

	
	
}