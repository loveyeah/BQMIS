package power.ejb.productiontec.relayProtection;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 *继电保护装置动作情况
 *@author liyi 090717
 */
@Remote
public interface PtJdbhJBhzzdzqkFacadeRemote {
	/**
	 * 新增一条继电保护装置动作情况记录
	 */
	public PtJdbhJBhzzdzqk save(PtJdbhJBhzzdzqk entity);

	/**
	 * 删除一条继电保护装置动作情况记录
	 */
	public void delete(PtJdbhJBhzzdzqk entity);
	
	/**
	 * 删除一条或多条继电保护装置动作情况记录
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);

	/**
	 *更新一条继电保护装置动作情况记录
	 */
	public PtJdbhJBhzzdzqk update(PtJdbhJBhzzdzqk entity);

	/**
	 * 通过id查找一条继电保护装置动作情况记录
	 * @param id
	 * @return
	 */
	public PtJdbhJBhzzdzqk findById(Long id);
	
	/**
	 * 查询继电保护装置动作情况列表
	 * @param name 名称
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String name,String fromTime,String toTime, String enterpriseCode,int... rowStartIdxAndCount);

	
}