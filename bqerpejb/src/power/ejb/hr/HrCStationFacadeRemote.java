package power.ejb.hr;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
/**
 * 
 * 岗位管理
 * @author  modify by wzhyan
 */
@Remote
public interface HrCStationFacadeRemote {
	/**
	 * 增加
	 * @param entity
	 */
	public void save(HrCStation entity)  throws CodeRepeatException;

	/**
	 * 删除
	 * @param entity
	 */
	public void delete(HrCStation entity)  ;

	/**
	 * 修改
	 * @param entity
	 * @return
	 */
	public HrCStation update(HrCStation entity) throws CodeRepeatException;
	/**
	 * 主键查找
	 * @param id 岗位主键
	 * @return
	 */
	public HrCStation findById(Long id);  
	public List<HrCStation> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount); 
	 
	/**
	 * 取得岗位列表
	 * @param start
	 * @param limit 
	 * @return PageObject
	 */
	public PageObject GetStationList(String stationName,final int... rowStartIdxAndCount);
	
	/**
	 * 取得可用岗位列表
	 * @return 
	 */
//	public List<HrCStation> getAvailableStationList();
	 
	
	/**
	 * 批量删除
	 * @param ids 主键集(如2,4,5)
	 */
	public void deletes(String ids);
	public List<HrCStation> GetAllStationList();
	/**
	 * 模糊查询岗位列表
	 * @param fuzzy
	 * @return
	 */
	public List<HrCStation> findStationListByFuzzy(String fuzzy);
	
	/**
	 * add by liuyi 20100610
	 * 通过岗位名称获得岗位id
	 * @param name
	 * @return
	 */
	Long getStationIdByName(String name);
	
}