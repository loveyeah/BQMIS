package power.ejb.hr.archives;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCPunishFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCPunishFacadeRemote {
	
	/**查询所有惩罚情况信息
	 * @param enterPriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public  PageObject  getPunishList(String empID,String enterPriseCode,int...rowStartIdxAndCount);
	
	/**批量导入excel表格中的记录到奖励情况表中
	 * @param addOrUpdateList
	 */
	public  void saveOrUpdatePunish(List<HrCPunish> addOrUpdateList);
	
	/**保存惩罚情况记录
	 * @param entity
	 */
	public void save(HrCPunish entity);

	/**删除一条或者多条惩罚情况记录
	 * @param ids
	 */
	public void  delPunish(String ids);
	
	/**删除惩罚情况实体
	 * @param entity
	 */
	public void delete(HrCPunish entity);

	
	/**修改一条惩罚情况记录
	 * @param entity
	 * @return
	 */
	public HrCPunish update(HrCPunish entity);

	/**通过id查找一条惩罚情况记录
	 * @param id
	 * @return
	 */
	public HrCPunish findById(Long id);

	
	
}