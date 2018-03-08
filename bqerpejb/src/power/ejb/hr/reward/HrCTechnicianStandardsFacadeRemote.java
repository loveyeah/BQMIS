package power.ejb.hr.reward;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

@Remote
public interface HrCTechnicianStandardsFacadeRemote {
	public void save(HrCTechnicianStandards entity);

	public void delete(HrCTechnicianStandards entity);

	public HrCTechnicianStandards update(HrCTechnicianStandards entity);

	public HrCTechnicianStandards findById(Long id);

	public PageObject getTechnicianList(String enterprise);

	public void deleteTechicianList(String ids);

	public void saveTechicianList(List<HrCTechnicianStandards> addList,
			List<HrCTechnicianStandards> updateList);
}