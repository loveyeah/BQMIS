package power.ejb.run.securityproduction.safesupervise;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 整改计划管理 动态检查
 * 2010-04-20
 * @author fyyang
 */
@Remote
public interface SpCDynamicCheckMainFacadeRemote {
	
	/**
	 * 增加一条动态检查信息
	 * @param entity
	 * @return
	 */
	public SpCDynamicCheckMain save(SpCDynamicCheckMain entity);

	/**
	 * 删除一条或多条动态检查信息
	 * @param ids
	 */
	public void delete(String ids);

	/**
	 * 修改一条动态检查信息
	 * @param entity
	 * @return
	 */
	public SpCDynamicCheckMain update(SpCDynamicCheckMain entity);

	/**
	 * 通过id查找一条动态检查信息
	 * @param id
	 * @return
	 */
	public SpCDynamicCheckMain findById(Long id);

	

}