package power.ejb.run.securityproduction;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.form.SpJSafepunishForm;

/**
 * Remote interface for SpJSafepunishFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJSafepunishFacadeRemote {

	public boolean save(SpJSafepunish entity);

	public boolean delete(SpJSafepunish entity);

	public boolean update(SpJSafepunish entity);

	public SpJSafepunishForm findFormModel(Long id);

	public SpJSafepunish findModel(Long id);

	public PageObject findByDate(String encourageDate, String enterpriseCode,
			int... rowStartIdxAndCount);
}