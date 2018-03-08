package power.ejb.productiontec.relayProtection;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 继电定值项目维护
 * @author liuyi 090713
 */
@Remote
public interface PtJdbhCDzxmwhFacadeRemote {
	/**
	 * 新增一条继电定值项目维护表记录
	 */
	public PtJdbhCDzxmwh save(PtJdbhCDzxmwh entity);

	/**
	 * 删除一条继电定值项目维护表记录
	 */
	public void delete(PtJdbhCDzxmwh entity);

	/**
	 * 删除一条或多条继电定值项目维护表记录
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);
	
	/**
	 * 更新一条P继电定值项目维护表记录
	 */
	public PtJdbhCDzxmwh update(PtJdbhCDzxmwh entity);

	/**
	 * 通过id查找一条继电定值项目维护表记录
	 * @param id
	 * @return
	 */
	public PtJdbhCDzxmwh findById(Long id);

	/**
	 * 查询继电定值项目维护列表
	 * @param name 名称
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String name, String enterpriseCode,int... rowStartIdxAndCount);
}