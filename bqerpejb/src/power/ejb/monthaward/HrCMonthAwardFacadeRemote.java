package power.ejb.monthaward;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCMonthAwardFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCMonthAwardFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCMonthAward entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCMonthAward entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCMonthAward entity);
	public void saveStandar(HrCMonthStandarddays entity);

	/**
	 * Delete a persistent HrCMonthAward entity.
	 * 
	 * @param entity
	 *            HrCMonthAward entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCMonthAward entity);

	/**
	 * Persist a previously saved HrCMonthAward entity and return it or a copy
	 * of it to the sender. A copy of the HrCMonthAward entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCMonthAward entity to update
	 * @return HrCMonthAward the persisted HrCMonthAward entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCMonthAward update(HrCMonthAward entity);
	public HrCMonthStandarddays updateAwardDays(HrCMonthStandarddays entity);

	public HrCMonthAward findById(Long id);
	public HrCMonthStandarddays findByDaysId(Long id) ;
	public void deleteMonAward(String ids);
	public void deleteMonStandDays(String ids);
	public PageObject findMonAward(String Time,String enterpriseCode,final int... rowStartIdxAndCount)throws ParseException;
	public PageObject findMonAwardDays(String equTime,String enterpriseCode,final int... rowStartIdxAndCount)throws ParseException;

	
}