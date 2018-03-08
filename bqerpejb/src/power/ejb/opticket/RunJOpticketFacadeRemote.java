package power.ejb.opticket;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.opticket.form.OpInfoForm;
import power.ejb.opticket.form.OpticketInfo;

/**
 * 操作票主记录管理
 * 
 * @author wzhyan
 */
@Remote
public interface RunJOpticketFacadeRemote {

	/**
	 * 新增一个操作票
	 * 
	 * @param enterpriseChar
	 *            企业一位标识
	 * @param entity
	 *            操作票实体
	 * @return RunJOpticket
	 */
	public RunJOpticket save(String enterpriseChar, RunJOpticket entity);

	/**
	 * 新增一个操作票
	 * 
	 * @param enterpriseChar
	 *            企业一位标识
	 * @param entity
	 *            操作票实体
	 * @param standTicketNo
	 *            标准票号
	 * @return RunJOpticket
	 */
	public RunJOpticket saveByStandTicketNo(String enterpriseChar,
			RunJOpticket entity, String standTicketNo);

	/**
	 * 删除
	 * 
	 * @param entity
	 */
	public void delete(String opticketCode);

	/**
	 * 修改
	 * 
	 * @param entity
	 * @return
	 */
	public RunJOpticket update(RunJOpticket entity);

	/**
	 * 按操作票编码查找
	 * 
	 * @param opticketCode
	 *            操作票编码
	 * @return RunJOpticket
	 */
	public RunJOpticket findById(String opticketCode);

	/**
	 * 查询操作票
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param sDate
	 *            计划开始时间
	 * @param eDate
	 *            计划结束时间
	 * @param opType
	 *            操作类型编码
	 * @param specialCode
	 *            专业编码
	 * @param opticketStatus
	 *            操作票状态 *
	 * @param isStandar
	 *            是否标准操作票
	 * @return List<OpticketInfo>
	 */
	public PageObject getOpticketList(String enterpriseCode, String newOrOld,
			String date, String date2, String opType, String specialCode,
			String opticketStatus, String isStandar, String optaskName,
			String createBy,String optaskNo, final int... rowStartIdxAndCount);

	public PageObject getOpticketApproveList(String workerCode,
			String enterpriseCode, String date, String date2, String opType,
			String specialCode, String opticketStatus, String isStandar,
			String optaskName,String taskNo, final int... rowStartIdxAndCount);

	public PageObject getOpticketReportList(String workerCode,
			String enterpriseCode, String date, String date2, String opType,
			String specialCode, String opticketStatus, String isStandar,
			String optaskName, final int... rowStartIdxAndCount);

	public PageObject getStantdOpticktetList(String enterpriseCode,
			String newOrOld, String opticketType, String specialityCodefinal,
			String opStatus, String taskNameKey, int... rowStartIdxAndCount);

	public OpInfoForm findByCode(String opticketCode);

	/**
	 * 按工作票编号查找
	 * 
	 * @param worktickectNo
	 *            工作票编号
	 * @return String 操作票编号字符串集 返回为"":无该工作票对应的操作票
	 */
	public String findByWorktickectNo(String worktickectNo);

	/**
	 * 查询预警操作票
	 * 
	 * @param enterpriseCode
	 * @param date
	 * @param date2
	 * @param opType
	 * @param specialCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getWarOpticketList(String enterpriseCode, String date,
			String date2, String opType, String specialCode,
			final int... rowStartIdxAndCount);

	public RunJOpticket saveByendTicketNo(String enterpriseChar,
			RunJOpticket entity, String endTicketNo);

	public String createAnddelOptickect(String optickect,
			String enterpriseChar, RunJOpticket entity);
}