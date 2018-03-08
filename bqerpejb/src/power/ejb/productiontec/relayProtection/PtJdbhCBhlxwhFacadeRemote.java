package power.ejb.productiontec.relayProtection;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 继电保护类型维护
 * @author liuyi 090713
 */
@Remote
public interface PtJdbhCBhlxwhFacadeRemote {
	/**
	 * 新增一条继电保护类型维护记录
	 */
	public PtJdbhCBhlxwh save(PtJdbhCBhlxwh entity);

	/**
	 *删除一条继电保护类型维护记录
	 */
	public void delete(PtJdbhCBhlxwh entity);
	
	/**
	 * 删除一条或多条继电保护类型维护记录
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);

	/**
	 * 更新一条继电保护类型维护记录
	 */
	public PtJdbhCBhlxwh update(PtJdbhCBhlxwh entity);

	/**
	 * 通过id查找一条继电保护类型维护记录
	 * @param id
	 * @return
	 */
	public PtJdbhCBhlxwh findById(Long id);


	/**
	 * 查询继电保护类型维护列表
	 * @param name 名称
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String name, String enterpriseCode,int... rowStartIdxAndCount);
}