package power.ejb.productiontec.powertest;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;


/**
 * 仪器仪表精度维护
 * 
 * @author fyyang 090710
 */
@Remote
public interface PtCYqybjdFacadeRemote {
	
	public PtCYqybjd save(PtCYqybjd entity);
/**
 * 批量增加仪器仪表精度数据
 * @param addList
 */
	public  void save(List<PtCYqybjd>addList,List<PtCYqybjd> updateList,String ids);
	
	public void deleteMulti(String ids);
	
	public PtCYqybjd update(PtCYqybjd entity);

	
	
	public PtCYqybjd findById(Long id);
	public PageObject findAll(String enterpriseCode,Long specialId);
	

}