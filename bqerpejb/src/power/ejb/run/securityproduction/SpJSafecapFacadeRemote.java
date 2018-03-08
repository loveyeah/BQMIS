package power.ejb.run.securityproduction;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 安全帽管理
 * 
 * @author fyyang 090601
 */
@Remote
public interface SpJSafecapFacadeRemote {
	
	/**
	 * 增加一条安全帽领用信息
	 * @param entity
	 * @return
	 */
	public SpJSafecap save(SpJSafecap entity) throws CodeRepeatException;
	
	/**
	 * 删除一个或多个安全帽领用信息
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);
	
	/**
	 * 修改一条安全帽领用信息
	 * @param entity
	 * @return
	 */
	public SpJSafecap update(SpJSafecap entity) throws CodeRepeatException;

	/**
	 * 通过id查找一条安全帽领用信息
	 * @param id
	 * @return
	 */
	public SpJSafecap findById(Long id);
	
	/**
	 * 模糊查询安全帽领用信息列表
	 * @param recipientsNameOrSafeCapNo 领用人姓名或者安全帽编号
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String recipientsNameOrSafeCapNo,String enterpriseCode,int... rowStartIdxAndCount);
	
	/**
	 * 根据人名查询工号和所在的部门名称
	 * @param workName 人员名称
	 */
	@SuppressWarnings("unchecked")
	public List getDeptNameAndWorkCode(String workName);
}