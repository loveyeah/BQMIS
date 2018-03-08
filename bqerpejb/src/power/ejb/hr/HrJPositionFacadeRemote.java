package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 *  职务任免登记表
 * @author liuyi 091203	
 */
@Remote
public interface HrJPositionFacadeRemote {
	/**
	 * 新增职务任免登记表记录
	 */
	public void save(HrJPosition entity);

	/**
	 * 删除一条职务任免登记表
	 */
	public void delete(HrJPosition entity);
	
	/**
	 * 删除一条或多条职务任免登记表记录
	 * @param ids
	 */
	public void delete(String ids);

	/**
	 * 更新一条职务任免登记表记录
	 */
	public HrJPosition update(HrJPosition entity) throws SQLException, DataChangeException;

	public HrJPosition findById(Long id);

	
	public List<HrJPosition> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<HrJPosition> findAll(int... rowStartIdxAndCount);
	
	/**
	 * 查找指定员工的所有职务任免信息
	 * @param empId
	 * @param enterprise
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findEmpAllPosition(Long empId,String enterpriseCode,int... rowStartIdxAndCount);
}