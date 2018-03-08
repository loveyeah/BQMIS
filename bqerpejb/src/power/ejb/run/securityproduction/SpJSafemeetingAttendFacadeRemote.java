package power.ejb.run.securityproduction;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SpJSafemeetingAttendFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJSafemeetingAttendFacadeRemote {
	public boolean saveAtnList(List<SpJSafemeetingAttend> addList,
			List<SpJSafemeetingAttend> updateList, String delIds);

	public PageObject getAtnList(String meetingId, String enterpriseCode,
			int... rowStartIdxAndCount);
}