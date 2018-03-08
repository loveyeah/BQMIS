package power.ejb.equ.change;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for EquJChangphotoFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJChangphotoFacadeRemote {

	/**
	 * 增加异动附图信息
	 * @param entity
	 */
	public void save(EquJChangphoto entity);

 /**
  * 删除异动附图信息
  * @param entity
  */
	public void delete(EquJChangphoto entity);
	
	/**
	 * 删除异动附图信息
	 * @param changeNo 异动编号
	 * @param enterpriseCode 企业编码
	 */
	public void deleteByChangeNo(String changeNo,String enterpriseCode);

	/**
	 * 修改异动附图信息
	 * @param entity
	 * @return
	 */
	public EquJChangphoto update(EquJChangphoto entity);
 /**
  * 通过id查询一条异动附图信息
  * @param id
  * @return
  */
	public EquJChangphoto findById(Long id);
	
	/**
	 * 查询异动附图信息
	 * @param changeNo 异动编号
	 * @param enterprisecode 企业编码
	 * @return
	 */
	public EquJChangphoto findByChangeNo(String changeNo,String enterprisecode);
}