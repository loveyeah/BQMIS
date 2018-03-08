package power.ejb.run.securityproduction;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

@Remote
public interface SpBoilRepairFacadeRemote {
	public PageObject getBoilRepair(String enterprise, String startTime,
			String endTime, String isMaint, String fillBy);

	public void delBoilRepair(String ids);

	public void saveBoilRepair(List<SpJBoilerRepair> addList,
			List<SpJBoilerRepair> updateList, String enterpriseCode,
			String blockName, String type);

	public SpJBoilerRepair findById(Long id);

	public PageObject getBoilerEquList(String enterprise, Long boilerId,
			String sDate, String eDate, int... rowStartIdxAndCount);
}