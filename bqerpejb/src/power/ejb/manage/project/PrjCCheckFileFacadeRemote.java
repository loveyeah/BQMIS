package power.ejb.manage.project;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PrjCCheckFileFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PrjCCheckFileFacadeRemote {
	
	/**
	 * 增加一条竣工报告书和工程交工竣工验收证书记录
	 * @param entity
	 * @return
	 */
	public PrjCCheckFile save(PrjCCheckFile entity);

	/**
	 * 删除一条竣工报告书和工程交工竣工验收证书记录
	 * @param entity
	 */
	public void delete(PrjCCheckFile entity);

	/**
	 * 修改一条竣工报告书和工程交工竣工验收证书记录
	 * @param entity
	 * @return
	 */
	public PrjCCheckFile update(PrjCCheckFile entity);

	/**
	 * 根据ID查找一条竣工报告书和工程交工竣工验收证书详细信息
	 * @param id
	 * @return
	 */
	public PrjCCheckFile findById(Long id);

	public List<PrjCCheckFile> findByProperty(String propertyName, Object value);
	
	/**
	 * 删除一条或多条竣工报告书和工程交工竣工验收证书记录
	 * @param checkFileIds
	 */
	public void deleteMulti(String checkFileIds);

	/**
	 * 查找竣工报告书，工程交工竣工验收证书模版下载模块列表记录
	 * @return
	 */
	public PageObject findAll(String enterpriseCode,String fileName,String flag);
	
}
	