package power.ejb.productiontec.relayProtection;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.relayProtection.form.PtCLbyxmdyForm;

/**
 * 类别与项目对应维护
 * 
 * @author fyyang  090713
 */
@Remote
public interface PtJdbhCLbyxmdyFacadeRemote {
	
	public PtJdbhCLbyxmdy save(PtJdbhCLbyxmdy entity);
	public PtJdbhCLbyxmdy update(PtJdbhCLbyxmdy entity);
	public PtJdbhCLbyxmdy findById(PtJdbhCLbyxmdyId id);
	/**
	 * 保存类别与项目对应信息
	 * @param list
	 */
	public void saveRecords(Long sortId,String selectIds,String noselectIds,String enterpriseCode);
	
	/**
	 * 查询类别与项目对于信息列表
	 * @param enterpriseCode
	 * @param sylbId
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAll(String enterpriseCode,Long sylbId,final int... rowStartIdxAndCount);

	
}