package power.ejb.run.securityproduction;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.form.SpJMonthSafeMeetingFrom;

/**
 * Remote interface for SpJMonthSafeMeetingFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJMonthSafeMeetingFacadeRemote {

	public boolean save(SpJMonthSafeMeeting entity);

	public boolean delete(SpJMonthSafeMeeting entity);

	public boolean update(SpJMonthSafeMeeting entity);

	public SpJMonthSafeMeetingFrom findFromModel(Long id);

	public SpJMonthSafeMeeting findModel(Long id);

	/**
	 * 按年份取会议列表
	 */
	public PageObject findByDate(String meetingDate, String enterpriseCode,
			int... rowStartIdxAndCount);
}