package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for CbmCBasisFacade.
 * 编制依据明细维护
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmCBasisFacadeRemote {

	public void save(CbmCBasis entity);

	public void deleMutl(String ids);
	
	public void delete(CbmCBasis entity);

	public CbmCBasis update(CbmCBasis entity);

	public void  saveBasis(List<CbmCBasis>  addList,List<CbmCBasis>  updateList);
	
	public CbmCBasis findById(Long id);

	public PageObject  findBasisList(String enterPriseCode,String budgetItemId,final int... rowStartIdxAndCount);
}