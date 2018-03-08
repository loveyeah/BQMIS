package power.ejb.run.securityproduction;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SpJSafemeetingAbsenceFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJSafemeetingAbsenceFacadeRemote {

	public boolean saveAbsList(List<SpJSafemeetingAbsence> addList,
			List<SpJSafemeetingAbsence> updateList, String delIds);

	public PageObject getAbsList(String awardId, String enterpriseCode,
			int... rowStartIdxAndCount);
}