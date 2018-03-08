package power.ejb.productiontec.powertest;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 
 * 仪器仪表等级维护
 * 
 * @author fyyang 090710
 */
@Remote
public interface PtCYqybdjFacadeRemote {

	public PtCYqybdj save(PtCYqybdj entity);

	public void deleteMulti(String ids);

	public PtCYqybdj update(PtCYqybdj entity);

	public PtCYqybdj findById(Long id);

	public PageObject findAll(String enterpriseCode, Long specialId);

	public void save(List<PtCYqybdj> addList, List<PtCYqybdj> updateList,
			String ids);

}