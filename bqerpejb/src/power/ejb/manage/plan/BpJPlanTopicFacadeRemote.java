package power.ejb.manage.plan;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for BpJPlanTopicFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJPlanTopicFacadeRemote {

	public BpJPlanTopic save(BpJPlanTopic entity) throws ParseException;

	
	public void delete(BpJPlanTopic entity);

	public BpJPlanTopic update(BpJPlanTopic entity);

	public BpJPlanTopic findById(Long id);

	
	public List<BpJPlanTopic> findAll();

	public String findByPlanTimeAndTopicCode(String planTime, String topicCode);
}