package power.ejb.run.securityproduction;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SpJSafeawardDetailsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJSafeawardDetailsFacadeRemote {

	public boolean saveAwardManList(List<SpJSafeawardDetails> addList,
			List<SpJSafeawardDetails> updateList, String delIds);

	public PageObject getAwardManList(String awardId, String enterpriseCode,
			int... rowStartIdxAndCount);
}