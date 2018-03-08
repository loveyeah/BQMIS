package power.ejb.productiontec.chemistry;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtHxjdCHxzxybwhFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtHxjdCHxzxybwhFacadeRemote {
	/**
	 * 增加一条化学在线仪表信息
	 */
	public PtHxjdCHxzxybwh save(PtHxjdCHxzxybwh entity);

	/**
	 * 删除一条化学在线仪表信息
	 */
	public void delete(PtHxjdCHxzxybwh entity);
	
	/**
	 * 删除一条或多条化学在线仪器信息
	 * @param ids  将id以逗号连接成的字符串
	 */
	public void deleteMulti(String ids);

	/**
	 *更新一条化学在线仪表信息
	 */
	public PtHxjdCHxzxybwh update(PtHxjdCHxzxybwh entity);

	/**
	 * 通过Id查找一条化学在线仪表信息
	 */
	public PtHxjdCHxzxybwh findById(Long id);

	/**
	 * 查询化学在线仪器信息列表
	 * @param name 仪器名称
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String name, String enterpriseCode,int... rowStartIdxAndCount);

	
}