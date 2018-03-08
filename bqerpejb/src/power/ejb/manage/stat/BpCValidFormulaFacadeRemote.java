package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

/**
 * @author liuyi 20100521
 */
@Remote
public interface BpCValidFormulaFacadeRemote {
	
	public void save(BpCValidFormula entity);

	
	public void delete(String itemCode);

	
	public BpCValidFormula update(BpCValidFormula entity);

	public BpCValidFormula findById(String itemCode);

	
	public List<BpCValidFormula> findAll(int... rowStartIdxAndCount);
	
	List findValidFormulaEntity(String itemCode);
}