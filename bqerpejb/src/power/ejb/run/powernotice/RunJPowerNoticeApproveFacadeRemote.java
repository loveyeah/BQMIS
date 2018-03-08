package power.ejb.run.powernotice;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ejb.run.powernotice.form.PowerNoticeForPrint;

/**
 * 停送电通知单审批
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJPowerNoticeApproveFacadeRemote {
	
	public void save(RunJPowerNoticeApprove entity);

	
	public void delete(RunJPowerNoticeApprove entity);

	
	public RunJPowerNoticeApprove update(RunJPowerNoticeApprove entity);

	public RunJPowerNoticeApprove findById(Long id);
	
	/**
	 * 获取停送电联系单打印数据
	 * @param enterpriseCode
	 * @param noticeNo
	 * @return
	 * @throws ParseException 
	 */
	public PowerNoticeForPrint findByNoForPrint(String enterpriseCode,String noticeNo) throws ParseException;

	
	
}