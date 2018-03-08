package power.ejb.run.securityproduction.danger;

import java.util.List;
import javax.ejb.Remote;

/**
 * 重大危险源评估标准和评估报告维护
 * 
 * @author fyyang 20100728
 */
@Remote
public interface SpCDangerAssessFacadeRemote {
	
	public SpCDangerAssess save(SpCDangerAssess entity);

	
	public void delete(String ids);

	
	public SpCDangerAssess update(SpCDangerAssess entity);

	public SpCDangerAssess findById(Long id);
	
	public List<SpCDangerAssess> findAll(int... rowStartIdxAndCount);
	
	public List<SpCDangerAssess> findByKeyword(String name,String sort,String enterprise_code,final int... rowStartIdxAndCount);
	
	public int getCount(String name,String sort,String enterprise_code);
}