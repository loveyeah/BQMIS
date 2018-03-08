package power.ejb.manage.project;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PrjJCheckMeetSignFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PrjJCheckMeetSignFacadeRemote {
	
	public Long  save(PrjJCheckMeetSign entity);

	
	public void delete(PrjJCheckMeetSign entity);

	
	public PrjJCheckMeetSign update(PrjJCheckMeetSign entity);

	public PrjJCheckMeetSign findById(Long id);
	
	/**根据条件查询工程项目验收会签信息
	 * @param conName
	 * @param contractorName
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject  getPrjCheckMeetSign(String conName,String contractorName,String enterpriseCode,String workCode,String flag,final int... rowStartIdxAndCount);
	
	/**删除一条或者多条记录
	 * @param ids
	 */
	public void  delCheckMeetSign(String ids);
	
	/**通过Id查询项目验收会签单的补录信息
	 * @param checkSignById
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject  findCheckSignById(Long checkSignById,String enterpriseCode);
	
	/**根据部门和日期生成工程项目的编号
	 * @param dept
	 * @param date
	 * @return
	 */
	public String findProjectNo(String dept, String date);
	

}