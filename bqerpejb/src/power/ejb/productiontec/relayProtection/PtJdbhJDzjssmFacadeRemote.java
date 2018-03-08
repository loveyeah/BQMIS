package power.ejb.productiontec.relayProtection;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 继电保护定值计算说明
 * @author liuyi 090721
 */
@Remote
public interface PtJdbhJDzjssmFacadeRemote {
	/**
	 * 新增一条继电保护定值计算说明记录
	 */
	public PtJdbhJDzjssm save(PtJdbhJDzjssm entity);

	/**
	 * 删除一条继电保护定值计算说明记录
	 */
	public void delete(PtJdbhJDzjssm entity);
	/**
	 * 删除一条或多条继电保护装置台帐记录
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);

	/**
	 * 更新一条继电保护定值计算说明记录
	 */
	public PtJdbhJDzjssm update(PtJdbhJDzjssm entity);

	/**
	 * 通过id查找一条继电保护定值计算说明记录
	 * @param id
	 * @return
	 */
	public PtJdbhJDzjssm findById(Long id);

	

	/**
	 * 查询继电保护定值计算说明列表
	 */
	public PageObject findAll(String name, String enterpriseCode,int... rowStartIdxAndCount);
}