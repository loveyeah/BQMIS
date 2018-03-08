package power.ejb.manage.project;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PrjJEndCheckFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PrjJEndCheckFacadeRemote {
	/**
	 * 
	 * @param entity
	 */
	public PrjJEndCheck save(PrjJEndCheck entity);

	public void delete(Long id);

	public void deleteMulti(String ids);

	public PrjJEndCheck update(PrjJEndCheck entity);

	public PrjJEndCheck findById(Long id);
	
	public String findProjectNo(String dept, String date);
	
	public PageObject findPrjEndCheckList(String workerCode,String enterpriseCode,String conName,String contractorName,String flag,final int... rowStartIdxAndCount) throws ParseException;
}