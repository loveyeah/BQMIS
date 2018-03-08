package power.ejb.equ.technology;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * Remote interface for EquCTechnoAttrtypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCTechnoAttrtypeFacadeRemote {
	
	public EquCTechnoAttrtype save(EquCTechnoAttrtype entity) throws CodeRepeatException;

	
	public void delete(Long attrtypeId) throws CodeRepeatException;

	
	public EquCTechnoAttrtype update(EquCTechnoAttrtype entity) throws CodeRepeatException;

	public EquCTechnoAttrtype findById(Long id);

	


	public List<EquCTechnoAttrtype> findAll(int... rowStartIdxAndCount);
}