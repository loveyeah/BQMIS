package power.ejb.manage.project;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PrjJRegisterFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PrjJRegisterFacadeRemote {
	
	/**保存一条数据
	 * @param entity
	 */
	public void save(PrjJRegister entity);

	
	/**删除一条数据
	 * @param entity
	 */
	public void delete(PrjJRegister entity);

	
	/**修改一条数据
	 * @param entity
	 * @return
	 */
	public PrjJRegister update(PrjJRegister entity);
	
	/**删除一条或者多条立项录入信息
	 * @param ids
	 */
	public void delPrjRegister(String ids);
	
	/**根据ID 查找一条立项录入信息
	 * @param id
	 * @return
	 */
	public PrjJRegister findById(Long id);
	
	
	/**根据条件查询立项录入信息
	 * @param year
	 * @param prjType
	 * @param isFunds
	 * @param enterPriseCode
	 * @param workerCode
	 * @return PageObject
	 */
	public PageObject getPrjRegister(String prjName,String year,String prjType,String isFunds,String enterPriseCode,String workerCode, int... rowStartIdxAndCount);
	
	/**保存立项录入信息
	 * @param addList
	 * @param updateList
	 */
	public void savePrjRegister(List<PrjJRegister>  addList ,List<PrjJRegister> updateList );
	
	

	
	
}