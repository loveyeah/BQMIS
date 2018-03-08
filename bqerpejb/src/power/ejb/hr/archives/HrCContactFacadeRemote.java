package power.ejb.hr.archives;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for HrCContactFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCContactFacadeRemote {
	public void save(HrCContact entity);

	public void delete(HrCContact entity);

	public HrCContact update(HrCContact entity);

	public HrCContact findById(Long id);

	public List<HrCContact> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<HrCContact> findAll(int... rowStartIdxAndCount);

	/**
	 * 根据员工ID查询联系信息
	 * 
	 */
	public HrCContact findByEmpId(Long empId, String enterpriseCode);

	/**
	 * 导入联系信息
	 * 
	 */
	public void importPersonnelFilesContact(List<HrCContact> contactFashionList);
}