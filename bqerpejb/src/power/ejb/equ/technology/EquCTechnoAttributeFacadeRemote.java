package power.ejb.equ.technology;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for EquCTechnoAttributeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCTechnoAttributeFacadeRemote {
	
	public void save(EquCTechnoAttribute entity);

	public void delete(EquCTechnoAttribute entity);

	
	public EquCTechnoAttribute update(EquCTechnoAttribute entity);

	public EquCTechnoAttribute findById(Long id);


	
	public List<EquCTechnoAttribute> findAll(int... rowStartIdxAndCount);
}