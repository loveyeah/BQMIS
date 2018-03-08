package power.ejb.administration.business;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJCarfile;
import power.ejb.administration.AdJCarwh;
import power.ejb.administration.AdJCarwhInvoice;
import power.ejb.administration.AdJCarwhList;
import power.ejb.administration.AdJCarwhMx;

@Remote
public interface CarRepairManagementFacadeRemote {

	/**
	 * 车辆维修申请画面初始化
	 * @param start
	 * @param limit
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject findPurchaseOrderList( int start, int limit,
				String enterpriseCode) throws SQLException ;
	
	/**
	 * 车辆维修单位维护获取单位编码
	 */
	public PageObject findCarmendWhCpCode(String enterpriseCode) throws SQLException ;
	
	/**
	 * 维修项目详细grid数据初始化
	 */
	public PageObject findCarwhMxDetailList(String whId,int start,int limit,String enterpriseCode) throws SQLException ;
	
	/**
	 * 车辆维护之维修项目
	 */
	public PageObject findRepairProjectList(String enterPriseCode) throws SQLException;
	
	/**
	 * 通过项目编码取名称
	 */
	public String  findProjectName(String proCode,String enterPriseCode) throws SQLException;
	
	/**
	 * 项目详细list
	 */
	public PageObject findCarwhList(String whId,String proCode,String enterpriseCode,int start,int limit) throws SQLException;
	
	/**
	 * 车辆维修申请点击修改的DB操作
	 */
	public void updateDBOperate(AdJCarwh updateAdJCarwhEntity,List<AdJCarwhMx> updateAdJCarwhList,
			List<AdJCarwhMx> deleteAdJCarwhList,List<AdJCarwhMx> saveAdJCarwhList,
			AdJCarfile updateAdjCarFileEntity,List<AdJCarwhList> updateAdJCarwhListList,
			List<AdJCarwhList> deleteAdJCarwhListList,List<AdJCarwhList> saveAdJCarwhListList) throws SQLException,DataChangeException;
	/**
	 * 车辆维修发票附件
	 */
	public PageObject findAdJCarwhInvoiceFile(String whId) throws SQLException;
	/**
	 * 文件上传
	 */
	public void uploadAdJCarwhInvoiceFile(AdJCarwhInvoice entity) throws SQLException;
	/**
	 * 文件删除
	 */
	public void deleteAdJCarwhInvoiceFile(Long id,String updateTime,String workCode)throws SQLException,DataChangeException;
	
}
