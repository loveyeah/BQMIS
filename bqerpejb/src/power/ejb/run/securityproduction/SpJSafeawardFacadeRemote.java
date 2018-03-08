package power.ejb.run.securityproduction;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.form.SpJSafeawardForm;

/**
 * Remote interface for SpJSafeawardFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJSafeawardFacadeRemote {

	public boolean save(SpJSafeaward entity);

	public boolean delete(SpJSafeaward entity);

	public boolean update(SpJSafeaward entity);

	public SpJSafeawardForm findFormModel(Long id);

	public SpJSafeaward findModel(Long id);

	public PageObject findByDate(String meetingDate, String enterpriseCode,
			int... rowStartIdxAndCount);

}