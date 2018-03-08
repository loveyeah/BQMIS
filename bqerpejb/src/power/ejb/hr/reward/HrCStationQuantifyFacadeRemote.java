package power.ejb.hr.reward;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCStationQuantifyFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCStationQuantifyFacadeRemote {
	
	public void save(HrCStationQuantify entity);

	
	public void delete(HrCStationQuantify entity);

	
	public HrCStationQuantify update(HrCStationQuantify entity);

	public HrCStationQuantify findById(Long id);
	
	/**查询所有的岗位量化比例信息
	 * @return
	 */
	public PageObject  getStationQuantity(String enterPriseCode);
	
	/**保存一条或者多条岗位量化比例信息
	 * @param addList
	 * @param updateList
	 */
	public void  saveStationQuantity(List<HrCStationQuantify>  addList,List<HrCStationQuantify>  updateList);
	
	
	/**删除一条或者多条岗位量化信息
	 * @param ids
	 */
	public void delStationQuantity(String ids);

	
}