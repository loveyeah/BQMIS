package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for BpCAnalyseAccountSetupFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCAnalyseAccountSetupFacadeRemote {
	
	public void save(BpCAnalyseAccountSetup entity);

	public Long ifUpdate(String accountCode, String enterpriseCode);

	public void delete(String accountCode);

	public boolean deleteAccountSetup(Long accountCode, String enterpriseCode);

	
	public BpCAnalyseAccountSetup update(BpCAnalyseAccountSetup entity);

	public BpCAnalyseAccountSetup findById(BpCAnalyseAccountSetupId id);

	
	public List<BpCAnalyseAccountSetup> findByProperty(String propertyName,
			Object value);

	public List<BpCAnalyseAccountSetup> findByIfCollect(Object ifCollect);

	public List<BpCAnalyseAccountSetup> findByTimeType(Object timeType);

	public List<BpCAnalyseAccountSetup> findByIfAutoSetup(Object ifAutoSetup);

	public List<BpCAnalyseAccountSetup> findByEnterpriseCode(
			Object enterpriseCode);

	/**
	 * Find all BpCAnalyseAccountSetup entities.
	 * 
	 * @return List<BpCAnalyseAccountSetup> all BpCAnalyseAccountSetup entities
	 */
	public List<BpCAnalyseAccountSetup> findAll();
}