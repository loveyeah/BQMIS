package power.ejb.hr.reward;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

@Remote
public interface HrCDeptQuantifyCoefficientFacadeRemote {
	public void save(HrCDeptQuantifyCoefficient entity);

	public void delete(HrCDeptQuantifyCoefficient entity);

	public HrCDeptQuantifyCoefficient update(HrCDeptQuantifyCoefficient entity);

	public HrCDeptQuantifyCoefficient findById(Long id);

	public PageObject getDeptQuantifyList(String monthDate, String enterprise,
			int... rowStartIdxAndCount);

	public void deleteDeptQuantifyList(String ids);

	public void saveDeptQuantifyList(List<HrCDeptQuantifyCoefficient> addList,
			List<HrCDeptQuantifyCoefficient> updateList);
}