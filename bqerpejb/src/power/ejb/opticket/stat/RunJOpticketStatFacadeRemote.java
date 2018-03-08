package power.ejb.opticket.stat;

import java.util.Map;

import javax.ejb.Remote;

/*
 * 
 */
@Remote
public interface RunJOpticketStatFacadeRemote {

	public RunJOpticketStat save(RunJOpticketStat entity);

	public void delete(RunJOpticketStat entity);

	public RunJOpticketStat update(RunJOpticketStat entity);

	public RunJOpticketStat findById(Long id);

	/**
	 *  统计
	 * 
	 * @param statBy
	 * @param title
	 * @param yearMonth
	 * @param specialCode
	 * @param enterprisecode
	 */
	public boolean statOpticket(String statBy, String title, Long yearMonth,
			String specialCode, String enterprisecode);
	/**
	 * 得到主表数据
	 * @param statBy
	 * @param title
	 * @param yearMonth
	 * @param specialCode
	 * @param enterprisecode
	 * @return
	 */

	public Map<String, Object> getStatData(String statBy, String title,
			Long yearMonth, String specialCode, String enterprisecode);
	
	public void clearStat(RunJOpticketStatDetail model);
	
	public  Map<String, Object> getStatDataPrint(String statBy, String title,
			Long yearMonth, String specialCode, String enterprisecode);
	
}