package power.ejb.productiontec.relayProtection;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 继电保护装置台帐
 * 
 * @author fyyang 090716
 */
@Remote
public interface PtJdbhJBhzztzFacadeRemote {

	/**
	 * 新增一条继电保护装置台帐记录
	 * @param entity
	 * @return
	 */
	public PtJdbhJBhzztz save(PtJdbhJBhzztz entity);

	/**
	 * 删除一条继电保护装置台帐记录
	 * @param entity
	 */
	public void delete(PtJdbhJBhzztz entity);
	/**
	 * 删除一条或多条继电保护装置台帐记录
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);

	/**
	 * 更新一条继电保护装置台帐记录
	 * @param entity
	 * @return
	 */
	public PtJdbhJBhzztz update(PtJdbhJBhzztz entity);

	/**
	 * 通过Id查找一条继电保护装置台帐记录
	 * @param id
	 * @return
	 */
	public PtJdbhJBhzztz findById(Long id);

	


	/**
	 * 查询继电保护装置列表
	 * @param name 名称
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String name, String enterpriseCode,int... rowStartIdxAndCount);
}