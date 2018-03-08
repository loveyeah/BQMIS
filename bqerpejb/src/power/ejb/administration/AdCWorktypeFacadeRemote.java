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
 * 工作类别维护Remote
 * 
 * @author chaihao
 * 
 */
@Remote
public interface AdCWorktypeFacadeRemote {

	/**
	 * 新增工作类别维护
	 * @param entity 新增工作类别维护实体
	 */
	public void save(AdCWorktype entity);

	/**
	 * 删除工作类别维护
	 * @param entity
	 */
	public void delete(AdCWorktype entity);

	/**
	 * 更新工作类别维护
	 * @param entity 更新工作类别维护实体
	 * @return
	 */
	public AdCWorktype update(AdCWorktype entity) throws SQLException;

	/**
	 * 按序号查找工作类别维护
	 * @param id 序号
	 * @return
	 */
	public AdCWorktype findById(Long id) throws SQLException;

	/**
	 * 按指定属性查找工作类别维护
	 * 
	 * @param strValue 属性值
	 * @param strEnterpriseCode 企业代码
	 * @param rowStartIdxAndCount 检索数据附加参数
	 * @return PageObject 检索结果
	 */
	public PageObject findByProperty(String strValue, String strEnterpriseCode, int... rowStartIdxAndCount) throws SQLException;

	/**
	 * 查找所有内容
	 * @return
	 */
	public List<AdCWorktype> findAll();
}