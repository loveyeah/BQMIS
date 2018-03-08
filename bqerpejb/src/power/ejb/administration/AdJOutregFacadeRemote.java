package power.ejb.administration;


import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdJOutregFacade.
 * 
 * @author sufeiyu
 */
@Remote
public interface AdJOutregFacadeRemote {
	/**
	 * 新增物质出门登记信息
	 * @param entity
	 * @author daichunlin
	 * @throws SQLException 
	 */
	public void save(AdJOutreg entity) throws SQLException;

	/**
	 * 物质出门登记信息删除
	 * @param id
	 * @param strEmployee
	 * @param entity	       
	 * @return AdJOutreg
	 * @author daichunlin           
	 */
	public void delete(Long id, String strEmployee);

	/**
	 * 物质出门登记信息修改
	 * @param entity	       
	 * @return AdJOutreg  
	 * @author daichunlin         
	 * @throws DataChangeException 
	 * @throws SQLException 
	 */
	public void update(AdJOutreg entity,String lastmodifiedDate)
	  throws DataChangeException, SQLException;

	public AdJOutreg findById(Long id);

	/**
	 * Find all AdJOutreg entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJOutreg property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJOutreg> found by query
	 */
	public List<AdJOutreg> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJOutreg entities.
	 * 
	 * @return List<AdJOutreg> all AdJOutreg entities
	 */
	public List<AdJOutreg> findAll();
	
	/**
	 * 查询取得出门物资的详细
	 * 
	 * @param dteStartDate 开始时间
	 * @param dteEndDate 结束时间
	 * @param strFirm    所在单位
	 * @param strDcmStatus  上报状态
	 * 
	 * @return PageObject
	 * @author sufeiyu
	 */
	@SuppressWarnings("unchecked")
	public PageObject getData(String strEnterpriseCode, java.sql.Date dteStartDate,
			java.sql.Date dteEndDate, String strFirm, String strDcmStatus,
			int... rowStartIdxAndCount);

	/**
	 * 查询物质出门登记信息
	 * @param page
	 * @param page
	 * @return PageObject
	 * @author daichunlin
	 * @throws SQLException 
	 */
	public PageObject getInfoList(String strEnterpriseCode, final int... rowStartIdxAndCount) throws SQLException;
	/**
	 * 上报时修改单据状态信息
	 * @param updater 更新者
	 * @param id 序号
	 * @param dteRequest 时间
	 * @author daichunlin
	 */
	public void updateState(String updater, Long id, String dteRequest)
	throws DataChangeException, SQLException;
	/**
	 * 取得单位数量
	 * @param unit 单位Id
	 * @return 单位名称
	 * @author sufeiyu
	 */
	public String getUintById(String unit);
}