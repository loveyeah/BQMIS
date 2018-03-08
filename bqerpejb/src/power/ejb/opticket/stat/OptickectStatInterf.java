package power.ejb.opticket.stat;

import java.util.List;

import javax.ejb.Remote;

import power.ejb.opticket.form.OptickectQuyStat;
import power.ejb.opticket.form.OptickectStatuStat;

@Remote
public interface OptickectStatInterf {
	
	/**
	 * 统计某个月份某个专业的各个状态的操作票数量
	 * @param yearMonth
	 * @param spacialCode
	 * @param enterpriseCode
	 * @return
	 */
	public List<OptickectStatuStat> getOptickectStatuStat(String yearMonth,String spacialCode,String enterpriseCode);
	
	/**
	 * 统计某个月份某个专业的操作票数量合格率的统计
	 * @param yearMonth
	 * @param spacialCode
	 * @param enterpriseCode
	 * @return
	 */
	public List<OptickectQuyStat> getOptickectQuyStat(String yearMonth,String spacialCode,String enterpriseCode );
}
