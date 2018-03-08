package power.ejb.workticket.business;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

@Remote 
public interface WorkticketManager {
	
    
	public RunJWorktickets findWorkticketByNo(String workticketNo);
	
    /**
     * 生成工作票号，保存基本信息
	 * if(动火票)    --关联主票内容拷贝   
	 * if(电气一种票) --保存图片
	 * 按照工作票类型拷贝安措
	 * modify by fyyang 090523 增加字段existWorkticketNo
     * @param entity  工作票对象（RunJWorktickets）
     * @return RunJWorktickets
     */
	public RunJWorktickets createWorkticket(RunJWorktickets entity,String enterpriseChar,String dangerId,String workCode,String standticketNo,String existWorkticketNo,byte []...image);
	

	/**
	 * 从现有工作票生成工作票内容及安措
	 * 按照所选的工作票号copy其内容及安措到新工作票
	 * @param newWorkticketNo 新工作票的票号
	 * @param oldWorkticketNo 所选的现有工作票的票号
	 * @param enterpriseCode 企业编码
	 * @param createMan 创建人
	 * @param createDate 创建时间
	 */
	public void updateWorkticketInfoByOld(String newWorkticketNo,
			String oldWorkticketNo,String enterpriseCode,String createMan,String createDate);
	
	
	/**
	 * 修改工作票信息
	 * if(动火票&&修改了关联主票) 将原安措及内容改为现主票的内容及安措（删除原工作内容及安措）
	 * if（电气一种票）修改图片
	 * @param entity
	 * @return
	 */
	public RunJWorktickets updateWorkticket(RunJWorktickets entity,String dangerId,String workCode,byte[]...image);
	
	/**
	 * 删除未上报的工作票
	 * @param workticketNo 工作票号
	 */
	public void deleteWorkticket(String workticketNo);
	
//	/**
//	 * 由标准票生成新工作票
//	 * add by fyyang 090122
//	 */
//	public void createWorkticketByStandTicket(String workticketNo,String enterpriseChar,String workCode);
	

	/**
	 * 由终结票生成新的标准票
	 * add by fyyang 090417
	 * @param endWorkticketNo
	 * @param enterBy
	 */
	public String createStandardByEndTicket(String endWorkticketNo,String enterBy);
	/**
	 * 查找电气一种票对应的图片信息
	 * @param workticketNo 工作票号
	 * @return
	 */
	public RunJWorkticketMap findMapByWorkticketNo(String workticketNo);
	
	/**
	 * 增加工作班成员
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public RunJWorkticketActors addWorkticketMember(RunJWorkticketActors entity) throws CodeRepeatException;
	
	/**
	 * 删除工作班成员
	 * @param id
	 * @throws CodeRepeatException
	 */
	public void deleteWorkticketMember(Long id) throws CodeRepeatException;
	
	/**
	 * 批量删除工作班成员
	 * @param ids
	 */
	public void deleteMultiMember(String ids);
	
	/**
	 * 查询工作票对应的工作班组成员
	 * @param enterpriseCode
	 * @param workticketNo
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAllActors(String enterpriseCode,String workticketNo,final int... rowStartIdxAndCount);
	
	/**
	 * 增加一条动火票作业内容记录
	 * @param entity
	 * @return
	 * @throws CodeRepeatException
	 */
	public RunJWorkticketFireContent saveWorkticketFireContent(RunJWorkticketFireContent entity) throws CodeRepeatException;
	
	/**
	 * 删除一条动火票作业内容记录
	 * @param id
	 */
	public void deleteWorkticketFireContent(Long id);
	
	/**
	 * 批量删除动火票作业内容记录
	 * @param ids
	 */
	public void deleteMultiFireContent(String ids);
	
	/**
	 * 获得动火票作业内容列表
	 * @param enterpriseCode 企业编码
	 * @param workticketNo 工作票号
	 * @param rowStartIdxAndCount 动态参数（开始行数和查询行数）
	 * @return
	 */
	public PageObject findFireContentList(String enterpriseCode,String workticketNo,final int... rowStartIdxAndCount);
	/**
	 * 查询工作内容列表
	 * @param enterpriseCode
	 * @param workticketNO
	 * @return
	 */
	public PageObject findWorkticketContentList(String enterpriseCode,String workticketNO,final int... rowStartIdxAndCount);
	
	/**
	 * 查找一条工作内容记录
	 * @param contentId 主键id
	 * @return
	 */
	public RunJWorkticketContent findWorkticketContenById(Long contentId);
	
	/**
	 * 增加一条工作内容记录
	 * 在使用
	 * addWorkticketContent
	 * @param entity
	 * @return
	 */
	public RunJWorkticketContent addWorkticketContent(RunJWorkticketContent entity);
	
//	
//	/**
//	 * 修改一条工作内容记录
//   *	delete by fyyang 090328 已不用
//	 * @param entity
//	 * @return
//	 */
//	public RunJWorkticketContent updateWorkticketContent(RunJWorkticketContent entity);
	
	
	/**
	 * 删除一条工作内容记录
	 * 在使用
	 * @param contentId
	 */
	public void deleteWorkticketContent(Long contentId);
	
	/**
	 * 查询安措内容列表
	 * modify by fyyang 090311
	 * @param enterpriseCode 企业编码
	 * @param workticketNo 工作票号
	 * @param rowStartIdxAndCount 
	 * @return
	 */
	public PageObject findSafetyContentList(String enterpriseCode,String workticketNo,String workticketTypeCode,final int... rowStartIdxAndCount);
	
	
//	/**
//	 * 修改动火票的安措内容（╳或√互换）
//	 * delete by fyyang 090328
//	 * @param workticketNo
//	 * @param safetyCode
//	 */
//	public void updateFireSafety(String workticketNo,String safetyCode);
	
	
	
	/**
	 * 根据票号及安措编码获得对应的安措内容
	 * @param enterpriseCode企业编码
	 * @param workticketNo工作票号
	 * @param safetyCode安措编码
	 * @return 安措内容
	 */
	public String getSafetyContent(String enterpriseCode,String workticketNo,String safetyCode);
	
	/**
	 * 查询安措明细列表
	 * @param enterpriseCode 企业编码
	 * @param workticketNO 工作票号
	 * @param safetyCode
	 * @return
	 */
	public PageObject findSafetyDetailList(String enterpriseCode,String workticketNO,String safetyCode,final int... rowStartIdxAndCount);
	
	/**
	 * 查找一条安措明细记录
	 * @param safetyId
	 * @return
	 */
	public  RunJWorkticketSafety findSafetyDetailById(Long safetyId);
	
	
	/**
	 * add by liuyi 091116
	  * 工单模块中创建一条工作票记录 
	  * @param entity 
	  * @param enterpriseChar
	  *         企业编码的一位标识
	  * @return RunJWorktickets
	  */
	public RunJWorktickets createBaseWorkticket(RunJWorktickets entity,String enterpriseChar);
	
	
//	/**
//	 * 增加一条安措明细记录delete by fyyang 090328
//	 * @param entity 
	//	 * @return
//	 */
//	//public RunJWorkticketSafety addWorkticketSafetyDetail(RunJWorkticketSafety entity);
	
//	/**
//	 * 修改一条安措明细记录
//	 * delete by fyyang 090328
//	 * @param entity
//	 * @return
//	 */
//	public RunJWorkticketSafety updateWorkticketSafetyDetail(RunJWorkticketSafety entity);
	
	
//	/**
//	 * 删除一条安措明细记录
//	 * delete by fyyang 090328
//	 * @param safetyid
//	 */
//	public void deleteWorkticketSafetyDetail(Long safetyid);
	
	
	
	

	
	
	
	
	
	
	
	
	
	
  
	
	
	
	
}
