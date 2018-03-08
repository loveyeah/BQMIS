/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 值别维护Remote
 * @author chaihao
 * 
 */
@Remote
public interface AdCDutytypeFacadeRemote {
	/**
	 * 新增值别
	 * 
	 * @param entity 新增值别实体
	 */
	public void save(AdCDutytype entity) throws SQLException;

	/**
	 * 删除值别
	 * 
	 * @param entity
	 */
	public void delete(AdCDutytype entity);

	/**
	 * 更新值别
	 * 
	 * @param entity 更新值别实体
	 * @return
	 */
	public AdCDutytype update(AdCDutytype entity) throws SQLException;

	/**
	 * 按序号查找值别
	 * 
	 * @param id 序号
	 * @return
	 */
	public AdCDutytype findById(Long id) throws SQLException;

	/**
	 * 按指定属性查找值别
	 * 
	 * @param strValue 属性值
	 * @param rowStartIdxAndCount 检索数据附加参数
	 * @return PageObject 检索结果
	 */
	public PageObject findByProperty(String strValue, String strEnterpriseCode, int... rowStartIdxAndCount) throws SQLException;

	/**
	 * 查找所有内容
	 * 
	 * @return
	 */
	public List<AdCDutytype> findAll();
}