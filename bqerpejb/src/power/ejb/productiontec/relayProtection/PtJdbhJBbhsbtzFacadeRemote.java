package power.ejb.productiontec.relayProtection;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 被保护设备台帐
 */
@Remote
public interface PtJdbhJBbhsbtzFacadeRemote {
	/**
	 * 新增一条被保护设备记录
	 */
	public PtJdbhJBbhsbtz save(PtJdbhJBbhsbtz entity);

	/**
	 *删除一条被保护设备记录
	 */
	public void delete(PtJdbhJBbhsbtz entity);
	
	/**
	 * 删除一条或多条被保护设备记录
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);

	/**
	 * 更新一条被保护设备记录
	 */
	public PtJdbhJBbhsbtz update(PtJdbhJBbhsbtz entity);

	/**
	 * 通过Id查找一条被保护设备记录
	 */
	public PtJdbhJBbhsbtz findById(Long id);

	

	/**
	 * 查询被保护设备列表
	 */
	public PageObject findAll(String name,String enterpriseCode,int... rowStartIdxAndCount);
}