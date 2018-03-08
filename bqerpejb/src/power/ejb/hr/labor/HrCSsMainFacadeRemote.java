package power.ejb.hr.labor;

import java.util.List;
import javax.ejb.Remote;

/**
 * 社保管理主信息
 * 
 * @author fyyang 20100605
 */
@Remote
public interface HrCSsMainFacadeRemote {
	
	/**
	 * 保存社保管理主信息
	 * @param entity
	 * @return
	 */
	public HrCSsMain save(HrCSsMain entity);

	/**
	 * 删除社保主信息
	 * @param ids
	 */
	public void delete(String ids);
	
	/**
	 * 修改社保主信息
	 * @param entity
	 * @return
	 */
	public HrCSsMain update(HrCSsMain entity);
    
	/**
	 * 通过id查找社保主信息
	 * @param id
	 * @return
	 */
	public HrCSsMain findById(Long id);
	
	/**
	 * 查找社保主信息
	 * @param strYear
	 * @param yearType
	 * @param insuranceType
	 * @param enterpriseCode
	 * @return
	 */
	public HrCSsMain findSsMainInfo(String strYear,String yearType,String insuranceType,String enterpriseCode);

	
}