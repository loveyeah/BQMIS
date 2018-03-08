package power.ejb.run.securityproduction;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SpJSafepunishDetailsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJSafepunishDetailsFacadeRemote {
	public boolean savePunishManList(List<SpJSafepunishDetails> addList,
			List<SpJSafepunishDetails> updateList, String delIds);

	public PageObject getPunishManList(String awardId, String enterpriseCode,
			int... rowStartIdxAndCount);
}