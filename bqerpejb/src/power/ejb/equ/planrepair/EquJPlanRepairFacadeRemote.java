package power.ejb.equ.planrepair;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquJPlanRepairFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJPlanRepairFacadeRemote {
	
	public void save(EquJPlanRepair entity);

	
	public void delete(EquJPlanRepair entity);

	
	public EquJPlanRepair update(EquJPlanRepair entity);

	public EquJPlanRepair findById(Long id);

	
	public List<EquJPlanRepair> findByProperty(String propertyName, Object value);

	
	public List<EquJPlanRepair> findAll();

	public boolean judgeAddDetailNo(String detailNo);

	public boolean judgeUpdateDetailNo(String detailNo, String detailId);
	
	public PageObject getDetailPlanList(String repairId,String enterprisecode) throws ParseException;
}