package power.ejb.resource;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface 物资需求计划审批.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface MrpJPlanRequirementHisFacadeRemote {
	
	public void save(MrpJPlanRequirementHis entity);

	
	public void delete(MrpJPlanRequirementHis entity);

	
	public MrpJPlanRequirementHis update(MrpJPlanRequirementHis entity);

	public MrpJPlanRequirementHis findById(Long id);

}