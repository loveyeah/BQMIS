package power.ejb.productiontec.relayProtection;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 继电实验报告
 * 
 * @author fyyang 090715
 */
@Remote
public interface PtJdbhJSybgFacadeRemote {
	
	/**
	 * 保存一条继电保护实验报告信息
	 * @param entity
	 * @return
	 */
	public PtJdbhJSybg save(PtJdbhJSybg entity);
	
	/**
	 * 删除一条或多条继电保护实验报告信息
	 * @param ids
	 */
	public void deleteMulti(String ids);
	
	/**
	 * 修改一条继电保护实验报告信息
	 * @param entity
	 * @return
	 */
	public PtJdbhJSybg update(PtJdbhJSybg entity);
    /**
     * 查找一条继电保护实验报告信息
     * @param id
     * @return
     */
	public PtJdbhJSybg findById(Long id);
	
	/**
	 * 获得该装置上次实验的实验时间
	 * @param deviceId
	 * @param enterpriseCode
	 * @return
	 */
	public String getLastTestDate(Long deviceId,String enterpriseCode);
	
	/**
	 * 查找继电保护实验报告信息列表
	 * @param sDate
	 * @param eDate
	 * @param deviceName
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String sDate,String eDate,String deviceName,String enterpriseCode,int... rowStartIdxAndCount);

	
}