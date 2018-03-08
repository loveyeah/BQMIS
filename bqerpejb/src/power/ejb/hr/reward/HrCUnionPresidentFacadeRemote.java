package power.ejb.hr.reward;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCUnionPresidentFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCUnionPresidentFacadeRemote {
	
	public void save(HrCUnionPresident entity);
    
	
	public void delete(HrCUnionPresident entity);

	
	public HrCUnionPresident update(HrCUnionPresident entity);

	public HrCUnionPresident findById(Long id);
	/**查询所有工会主席标准信息
	 * @return
	 */
	public PageObject getUnionPresident();
	/**保存一条或者多条工会标准信息
	 * 
	 */
	public void   saveUnionPresident(List<HrCUnionPresident> addList,List<HrCUnionPresident> updateList);
	/**删除一条或者多条工会标准信息
	 * 
	 */
	public void  delUnionPresident(String ids);
	public String  getMaxEndTime();
 
	
	
	
}