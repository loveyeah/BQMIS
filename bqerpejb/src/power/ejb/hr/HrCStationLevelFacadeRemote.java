package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * add by drdu 090923
 * Remote interface for HrCStationLevelFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCStationLevelFacadeRemote {

	/**
	 * 增加一条岗位级别记录
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public HrCStationLevel save(HrCStationLevel entity) throws CodeRepeatException;

	/**
	 * 删除一条或多条岗位级别记录
	 * @param ids
	 */
	public void deleteMulti(String ids);

	/**
	 * 修改一条岗位级别记录
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public HrCStationLevel update(HrCStationLevel entity) throws CodeRepeatException;

	/**
	 * 根据ID查找一条岗位级别记录详细 
	 * @param id
	 * @return
	 */
	public HrCStationLevel findById(Long id);

	/**
	 * 根据岗位级别名称及企业编码查找列表
	 * @param enterpriseCode
	 * @param stationLevelName
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findStationLevelList(String enterpriseCode,String stationLevelName,final int... rowStartIdxAndCount);
	
	/**
	 * 查找所有记录
	 * @param enterpriseCode
	 * @return
	 */
	public List<HrCStationLevel> findAllList(String enterpriseCode);
	
	/**
	 * 根据岗位ID查找级别
	 * @param stationId
	 * @param enterpriseCode
	 * @return
	 * @throws SQLException
	 */
	public PageObject findByStationId(String stationId,String enterpriseCode) throws SQLException;
	
	
	/**
	 * add by liuyi 091123
	 * 查找岗位级别
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllStationLevels(String enterpriseCode);
}