package power.ejb.run.securityproduction;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 安全工器具管理
 * 
 * @author fyyang 090602
 */
@Remote
public interface SpJSafetoolsFacadeRemote {
	
	/**
	 * 增加一条安全工器具信息
	 * @param entity
	 * @return
	 * @throws CodeRepeatException 
	 */
	public SpJSafetools save(SpJSafetools entity) throws CodeRepeatException;

	/**
	 * 修改一条安全工器具信息
	 * @param entity
	 * @return
	 * @throws CodeRepeatException 
	 */
	public SpJSafetools update(SpJSafetools entity) throws CodeRepeatException;
	
	/**
	 * 删除一条或多条安全工器具信息
	 * @param ids
	 */
	public void deleteMulti(String ids);
	
	/**
	 * 查找一条安全工器具信息
	 * @param id
	 * @return
	 */
	public SpJSafetools findById(Long id);
	
	/**
	 * 查找安全工器具信息列表
	 * @param toolsNameOrChargeMan 工具名称或责任人
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String toolsNameOrChargeMan,String enterpriseCode,final int... rowStartIdxAndCount);
}