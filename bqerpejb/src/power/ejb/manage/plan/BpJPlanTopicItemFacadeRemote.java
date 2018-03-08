package power.ejb.manage.plan;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpJPlanTopicItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJPlanTopicItemFacadeRemote {
	
	public void save(BpJPlanTopicItem entity);

	
	public void delete(BpJPlanTopicItem entity);


	public BpJPlanTopicItem update(BpJPlanTopicItem entity);

	public BpJPlanTopicItem findById(Long id);

	
	

	public PageObject queryBpJPlanTopicItemList(String planTime,
			String topicCode, int... rowStartIdxAndCount);

	
	public List<BpJPlanTopicItem> findAll();

	public void save(List<BpJPlanTopicItem> addList,
			List<BpJPlanTopicItem> updateList, String delString);

	public void deleteByReportId(String reportId);
}