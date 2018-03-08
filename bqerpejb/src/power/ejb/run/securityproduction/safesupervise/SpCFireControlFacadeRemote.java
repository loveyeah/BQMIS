package power.ejb.run.securityproduction.safesupervise;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SpCFireControlFacade.
 * 
 * @author drdu 20100421
 */
@Remote
public interface SpCFireControlFacadeRemote {

	/**
	 * 增加一条消防器材配置表记录
	 * @param entity
	 * @return
	 */
	public SpCFireControl save(SpCFireControl entity);

	/**
	 * 删除一条或多条消防器材配置表记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条消防器材配置表记录
	 * @param entity
	 * @return
	 */
	public SpCFireControl update(SpCFireControl entity);

	/**
	 * 根据ID查找一条消防器材配置表的详细 
	 * @param id
	 * @return
	 */
	public SpCFireControl findById(Long id);

	/**
	 * 根据企业编码，配置部位查找消防器材配置表记录
	 * @param enterpriseCode
	 * @param deployPart
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findFireControlList(String enterpriseCode,String deployPart,final int... rowStartIdxAndCount);
}