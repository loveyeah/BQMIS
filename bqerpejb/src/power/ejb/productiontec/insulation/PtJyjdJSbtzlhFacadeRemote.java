package power.ejb.productiontec.insulation;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 绝缘设备台帐
 * @author liuyi 090706
 */
@Remote
public interface PtJyjdJSbtzlhFacadeRemote {
	/**
	 *增加一条绝缘设备信息
	 * @param entity
	 */
	public PtJyjdJSbtzlh save(PtJyjdJSbtzlh entity);

	/**
	 * 删除一条或多条绝缘设备信息
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);
	/**
	 * 删除一条绝缘设备信息
	 * @param entity
	 */
	public void delete(PtJyjdJSbtzlh entity);

	/**
	 * 修改一条绝缘设备信息
	 * @param entity
	 * @return
	 */
	public PtJyjdJSbtzlh update(PtJyjdJSbtzlh entity);

	/**
	 * 查找一条绝缘设备信息
	 * @param id
	 * @return
	 */
	public PtJyjdJSbtzlh findById(Long id);

	/**
	 * 查询绝缘设备信息列表
	 * @param name 设备名称
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String name, String enterpriseCode,int... rowStartIdxAndCount);
	
	
	/**
	 * 查询绝缘设备信息列表以及预试计划
	 * @param name 设备名称
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findDeviceTryAll(String name, String enterpriseCode,int... rowStartIdxAndCount);
	

	/**
	 * 对绝缘设备台帐和预试计划表中的一条信息进行编辑
	 */
	public boolean editDeviceTryInfo(Long id,String operateBy,String operateDate,String memo,int testCycle);
}