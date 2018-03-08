package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * 技术职称表
 * @author liuyi 091203
 */
@Remote
public interface HrJProfessionalPostFacadeRemote {
	/**
	 * 新增技术职称表记录
	 */
	public void save(HrJProfessionalPost entity);

	/**
	 * 删除一条技术职称表记录
	 */
	public void delete(HrJProfessionalPost entity);

	/**
	 * 删除一条或多条技术职称表记录
	 */
	public void delete(String ids);
	
	/**
	 * 更新一条技术职称表记录
	 */
	public HrJProfessionalPost update(HrJProfessionalPost entity) throws SQLException, DataChangeException;

	public HrJProfessionalPost findById(Long id);

	
	public List<HrJProfessionalPost> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	
	public List<HrJProfessionalPost> findAll(int... rowStartIdxAndCount);
	
	
	/**
	 * 通过员工id获得该员工的所有技术职称信息
	 * @param empId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findEmpProfessionalPost(Long empId,String enterpriseCode,int... rowStartIdxAndCount);
}