package power.ejb.manage.project;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PrjJEquUseFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PrjJEquUseFacadeRemote {
	
	public void save(PrjJEquUse entity);

	public void delete(PrjJEquUse entity);

	
	public PrjJEquUse update(PrjJEquUse entity);

	public PrjJEquUse findById(Long id);
	public PageObject getEquDept(Long id);
	
	public  void  delEquUse(String ids);
	public void  saveEquDept(List<PrjJEquUse>addList,List<PrjJEquUse>updateList);

	
}