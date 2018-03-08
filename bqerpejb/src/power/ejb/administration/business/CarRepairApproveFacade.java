/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdCCarmendWh;
import power.ejb.administration.AdCCarwhPro;
import power.ejb.administration.AdJCarfile;
import power.ejb.administration.AdJCarfileFacadeRemote;
import power.ejb.administration.AdJCarwh;
import power.ejb.administration.AdJCarwhFacadeRemote;
import power.ejb.administration.AdJCarwhList;
import power.ejb.administration.AdJCarwhListFacadeRemote;
import power.ejb.administration.AdJCarwhMx;
import power.ejb.administration.AdJCarwhMxFacadeRemote;
import power.ejb.administration.comm.CodeCommonFacadeRemote;
import power.ejb.administration.form.CarRepairAprroveBean;
import power.ejb.administration.form.CarRepairAprroveDetailInfo;
import power.ejb.administration.form.CarRepairAprroveInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.PurJArrivalFacadeRemote;
import power.ejb.resource.form.PurchaseWarehouseInfo;

/**
 * 车辆维修申请Action
 * 
 * @author fangjihu
 * 
 */
@Stateless
public class CarRepairApproveFacade implements CarRepairApproveFacadeRemote {

	@PersistenceContext
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	@EJB(beanName = "AdJCarwhFacade")
	protected AdJCarwhFacadeRemote adJCarwhRemote;

	@EJB(beanName = "AdJCarwhMxFacade")
	protected AdJCarwhMxFacadeRemote adJCarwhMxRemote;

	@EJB(beanName = "AdJCarfileFacade")
	protected AdJCarfileFacadeRemote adJCarfileRemote;

	@EJB(beanName = "AdJCarwhListFacade")
	protected AdJCarwhListFacadeRemote adJCarwhListRemote;

	@EJB(beanName = "CodeCommonFacade")
	protected CodeCommonFacadeRemote codeCommonRemote;

	/**
	 * 车辆维修申请画面初始化
	 * 
	 * @param start
	 * @param limit
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findPurchaseOrderList(int start, int limit,
			String enterpriseCode,String workCode) throws SQLException {
		try {
			// EJB log 开始
			String strSql = "SELECT " +
			"DISTINCT A.ID ,"+
			"A.WH_ID ,"+
			"B.CAR_NAME ,"+
			"A.CAR_NO ,"+
			"TO_CHAR(A.REPAIR_DATE,'yyyy-mm-dd hh24:mi:ss')REPAIR_DATE,"+
			"A.CP_CODE,"+
			"A.DRIVE_MILE ,"+
			"A.MANAGER ,"+
			"A.IS_USE ,"+
			"A.REASON ,"+
			"A.SUM ,"+
			"A.REAL_SUM ,"+
			"A.MEMO,"+
			"A.DCM_STATUS,"+
			"TO_CHAR(A.UPDATE_TIME,'yyyy-mm-dd hh24:mi:ss')UPDATE_TIME,"+
			"A.REPAIR_STATUS, " +
			"B.ID AS DRIVEFILEID," +
			"TO_CHAR(B.UPDATE_TIME,'yyyy-mm-dd hh24:mi:ss')DRIVEFILEUPDATETIME,"+
			"B.USE_STATUS "+
			"FROM AD_J_CARWH A,AD_J_CARFILE B WHERE A.IS_USE=? AND A.DCM_STATUS IN(?,?) AND B.IS_USE=? AND A.CAR_NO=B.CAR_NO"+ 
			" AND A.ENTERPRISE_CODE =? AND B.ENTERPRISE_CODE =? AND A.UPDATE_USER=?" +
			" ORDER BY A.WH_ID";
			LogUtil.log("EJB:查找车辆维修申请信息开始。SQL=" + strSql, Level.INFO, null);
			List<Object> list = bll.queryByNativeSQL(strSql, new Object[] {
					"Y", "0","3" ,"Y",enterpriseCode,enterpriseCode,workCode}, start, limit);
			PageObject obj = new PageObject();
			
			List<CarRepairAprroveBean> arraylist = new ArrayList<CarRepairAprroveBean>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				CarRepairAprroveBean model = new CarRepairAprroveBean();
				if(data[0]!=null){
					model.setId(Long.parseLong(data[0].toString()));
				}
				if(data[1]!=null){
					model.setWhId(data[1].toString());
				}
				if(data[2]!=null){
					model.setCarName((data[2].toString()));
				}
				if(data[3]!=null){
					model.setCarNo(data[3].toString());
				}
				if(data[4]!=null){
					model.setRepairDate(data[4].toString());
				}
				if(data[5]!=null){
					model.setCpCode(data[5].toString());
				}
				if(data[6]!=null){
					model.setDriveMile(Double.parseDouble(data[6].toString()));
				}
				if(data[7]!=null){
					model.setManager(data[7].toString());
				}
				if(data[8]!=null){
					model.setDriver(data[8].toString());
				}
				if(data[9]!=null){
					model.setReason(data[9].toString());
				}
				if(data[10]!=null){
					model.setSum(Double.parseDouble(data[10].toString()));
				}
				if(data[11]!=null){
					model.setRealSum(Double.parseDouble(data[11].toString()));
				}
				if(data[12]!=null){
					model.setMemo(data[12].toString());
				}
				if(data[13]!=null){
					model.setDcmStatus(data[13].toString());
				}
				if(data[14]!=null){
					model.setUpdateTime(data[14].toString());
				}
				if(data[15]!=null){
					model.setRepairStatus(data[15].toString());
				}
				if(data[16]!=null){
					model.setDriveFileId(Long.parseLong(data[16].toString()));
				}
				if(data[17]!=null){
					model.setDriveFileUpdateTime(data[17].toString());
				}
				if(data[18]!=null){
					model.setUseStatus(data[18].toString());
				}
				arraylist.add(model);
				
			}
			obj.setList(arraylist);
			List<AdJCarwh> totalCountList = bll.queryByNativeSQL(strSql,
					new Object[] { "Y", "0","3" ,"Y",enterpriseCode,enterpriseCode,workCode});
			obj.setTotalCount(new Long(totalCountList.size()));
			LogUtil.log("EJB:查找车辆维修申请信息结束。", Level.INFO, null);
			return obj;
		} catch (Exception e) {
			LogUtil.log("EJB:查找车辆维修申请信息失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 车辆维修单位维护获取单位编码
	 */
	@SuppressWarnings("unchecked")
	public PageObject findCarmendWhCpCode(String enterpriseCode) throws SQLException  {
		try {
			// EJB log 开始
			String strSql = "SELECT " +
			"A.ID ,"+
			"A.CP_CODE ,"+
			"A.CP_NAME ,"+
			"A.CP_ADDRESS ,"+
			"A.CON_TEL ,"+
			"A.CONNMAN ,"+
			"A.BSN_RANGER ,"+
			"A.RETRIEVE_CODE ,"+
			"A.IS_USE ,"+
			"A.UPDATE_USER ,"+
			"A.UPDATE_TIME ,"+
			"A.ENTERPRISE_CODE "+
			" FROM AD_C_CARMEND_WH A WHERE A.IS_USE=? AND A.ENTERPRISE_CODE = ?";
			LogUtil.log("查找车辆维修单位维护获取单位编码开始。SQL=" + strSql, Level.INFO, null);
			List<AdCCarmendWh> list = bll.queryByNativeSQL(strSql,
					new Object[] { "Y",enterpriseCode}, AdCCarmendWh.class);
			AdCCarmendWh bean = new AdCCarmendWh();
			bean.setCpCode("");
			bean.setCpName("");
			list.add(0, bean);
			PageObject obj = new PageObject();
			obj.setList(list);
			List<AdCCarmendWh> totalCountList = bll.queryByNativeSQL(strSql,
					new Object[] { "Y",enterpriseCode}, AdCCarmendWh.class);
			obj.setTotalCount(new Long(totalCountList.size()));
			LogUtil.log("查找车辆维修单位维护获取单位编码结束。", Level.INFO, null);
			return obj;
		} catch (Exception e) {
			LogUtil.log("EJB:查找车辆维修单位维护获取单位编码失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 维修项目详细grid数据初始化
	 */
	@SuppressWarnings("unchecked")
	public PageObject findCarwhMxDetailList(String whId, int start, int limit,
			String enterpriseCode) throws SQLException {
		try {
			// EJB log 开始
			String strSql = "SELECT A.ID AS CARWHMX_ID, "
					+ "A.PRO_CODE,"
					+ "A.PRICE,"
					+ "TO_CHAR(A.UPDATE_TIME,'yyyy-mm-dd hh24:mi:ss')CARWHMX_UPDATE_TIME,"
					+ "B.ID AS CARWHPRO_ID,"
					+ "B.HAVE_LISE,"
					+ "TO_CHAR(B.UPDATE_TIME,'yyyy-mm-dd hh24:mi:ss')CARWHPRO_UPDATE_TIME "
					+ "FROM AD_J_CARWH_MX A ,AD_C_CARWH_PRO B "
					+ "WHERE A.IS_USE=? AND " + "B.IS_USE=? AND "
					+ "A.PRO_CODE=B.PRO_CODE AND A.WH_ID=?";
			LogUtil.log("查找维修项目详细grid数据初始化开始。SQL=" + strSql, Level.INFO, null);
			PageObject obj = new PageObject();
			List<Object> list = bll.queryByNativeSQL(strSql, new Object[] {
					"Y", "Y", whId }, start, limit);
			List<CarRepairAprroveDetailInfo> arraylist = new ArrayList<CarRepairAprroveDetailInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				CarRepairAprroveDetailInfo model = new CarRepairAprroveDetailInfo();
				if (data[0] != null) {
					model.setCarwhMxId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setProCode(data[1].toString());
				}
				if (data[2] != null) {
					model.setPrice(Double.parseDouble(data[2].toString()));
				}
				if (data[3] != null) {
					model.setCarwhMxUpdateTime(data[3].toString());
				}
				if (data[4] != null) {
					model.setCarwhProId(Long.parseLong(data[4].toString()));
				}
				if (data[5] != null) {
					model.setHaveLise(data[5].toString());
				}
				if (data[6] != null) {
					model.setCarwhProUpdateTime(data[6].toString());
				}
				model.setIsNew("0");
				model.setProDetail("项目详细");
				arraylist.add(model);

			}
			obj.setList(arraylist);
			List<Object> lstCount = bll.queryByNativeSQL(strSql, new Object[] {
					"Y", "Y", whId });
			obj.setTotalCount(new Long(lstCount.size()));
			LogUtil.log("查找维修项目详细grid数据初始化结束。", Level.INFO, null);
			return obj;
		} catch (Exception e) {
			LogUtil.log("EJB:查找维修项目详细grid数据初始化失败。", Level.SEVERE, e);
			throw new SQLException();
		}

	}

	/**
	 * 车辆维护之维修项目
	 */
	@SuppressWarnings("unchecked")
	public PageObject findRepairProjectList(String enterPriseCode) throws SQLException {
		try {
			// EJB log 开始
			String strSql = "SELECT " +
			"A.ID,"+
			"A.PAY_CODE,"+
			"A.PRO_CODE,"+
			"A.PRO_NAME,"+
			"A.HAVE_LISE,"+
			"A.IS_USE,"+
			"A.UPDATE_USER,"+
			"A.UPDATE_TIME," +
			"A.ENTERPRISE_CODE "+
			" FROM AD_C_CARWH_PRO A WHERE A.IS_USE=? AND A.ENTERPRISE_CODE = ?";
			LogUtil.log("查找车辆维护之维修项目开始。SQL=" + strSql, Level.INFO, null);
			List<AdCCarwhPro> list = bll.queryByNativeSQL(strSql,
					new Object[] { "Y",enterPriseCode}, AdCCarwhPro.class);
		
			PageObject obj = new PageObject();
			obj.setList(list);
			List<AdCCarwhPro> totalCountList = bll.queryByNativeSQL(strSql,
					new Object[] { "Y",enterPriseCode}, AdCCarwhPro.class);
			obj.setTotalCount(new Long(totalCountList.size()));
			LogUtil.log("查找车辆维护之维修项目结束。", Level.INFO, null);
			return obj;
		} catch (Exception e) {
			LogUtil.log("EJB:查找车辆维护之维修项目失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 通过项目编码取名称
	 */
	@SuppressWarnings("unchecked")
	public String findProjectName(String proCode, String enterPriseCode) throws SQLException{
		try {
			// EJB log 开始
			String strSql = "SELECT A.PRO_NAME FROM AD_C_CARWH_PRO A WHERE A.IS_USE=? AND A.PRO_CODE=?";
			LogUtil.log("查找项目名称开始。SQL=" + strSql, Level.INFO, null);
			List<Object> obj = bll.queryByNativeSQL(strSql, new Object[] { "Y",
					proCode });
			String value = "";
			if (obj.size() > 0) {
				value = obj.get(0).toString();
			}
			LogUtil.log("查找项目名称结束。", Level.INFO, null);
			return value;
		} catch (Exception e) {
			LogUtil.log("EJB:查找项目名称失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 项目详细list
	 */
	@SuppressWarnings("unchecked")
	public PageObject findCarwhList(String whId, String proCode,
			String enterpriseCode, int start, int limit) throws SQLException {

		try {
			// EJB log 开始
			String strSql = "SELECT A.ID,"
					+ "A.WH_ID,"
					+ "A.PRO_CODE,"
					+ "A.PART_NAME,"
					+ "A.UNIT,"
					+ "A.NUM,"
					+ "A.UNIT_PRICE,"
					+ "A.NOTE,"
					+ "TO_CHAR(A.UPDATE_TIME,'yyyy-mm-dd hh24:mi:ss')UPDATE_TIME "
					+ "FROM AD_J_CARWH_LIST A " + "WHERE A.IS_USE=? AND "
					+ "A.WH_ID=?";
			LogUtil.log("查找项目详细list开始。SQL=" + strSql, Level.INFO, null);
			PageObject obj = new PageObject();
			List<Object> list = bll.queryByNativeSQL(strSql, new Object[] {
					"Y", whId }, start, limit);
			List<CarRepairAprroveInfo> arraylist = new ArrayList<CarRepairAprroveInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				CarRepairAprroveInfo model = new CarRepairAprroveInfo();
				if (data[0] != null) {
					model.setId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setWhId(data[1].toString());
				}
				if (data[2] != null) {
					model.setProCode((data[2].toString()));
				}
				if (data[3] != null) {
					model.setPartName(data[3].toString());
				}
				if (data[4] != null) {
					model.setUnit((data[4].toString()));
				}
				if (data[5] != null) {
					model.setNum(Double.parseDouble(data[5].toString()));
				}
				if (data[6] != null) {
					model.setUnitPrice(Double.parseDouble(data[6].toString()));
				}
				if (data[7] != null) {
					model.setNote((data[7].toString()));
				}
				if (data[8] != null) {
					model.setUpdateTime((data[8].toString()));
				}
				if (model.getNum() != null && model.getUnitPrice() != null) {
					model.setSum(model.getNum() * (model.getUnitPrice()));
				}
				model.setIsNew("0");

				arraylist.add(model);

			}
			obj.setList(arraylist);
			List<Object> lstCount = bll.queryByNativeSQL(strSql, new Object[] {
					"Y", whId });
			obj.setTotalCount(new Long(lstCount.size()));
			LogUtil.log("查找项目详细list结束。", Level.INFO, null);
			return obj;
		} catch (Exception e) {
			LogUtil.log("EJB:查找项目详细list失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 插入DB操作
	 */
	public void saveDBOperate(String workCode, AdJCarwh saveAdJCarwhEntity,
			List<AdJCarwhMx> saveAdJCarwhList,
			AdJCarfile updateAdjCarFileEntity,
			List<AdJCarwhList> saveAdJCarwhListList) throws SQLException,DataChangeException {
		try {
			LogUtil.log("插入车辆维护表开始。", Level.INFO, null);
			// 车辆维护ID
			String whId = codeCommonRemote.getCarMaintenanceAppNo(workCode);
			// 车辆维护插入序号
			Long id = bll.getMaxId("AD_J_CARWH", "ID");
			saveAdJCarwhEntity.setId(id);
			saveAdJCarwhEntity.setWhId(whId);
			adJCarwhRemote.save(saveAdJCarwhEntity);
			LogUtil.log("插入车辆维护表结束。", Level.INFO, null);

			LogUtil.log("插入车辆维护明细表开始。", Level.INFO, null);
			id = bll.getMaxId("AD_J_CARWH_MX", "ID");
			for (int i = 0; i < saveAdJCarwhList.size(); i++) {
				AdJCarwhMx entity = saveAdJCarwhList.get(i);
				entity.setId(id);
				entity.setWhId(whId);
				adJCarwhMxRemote.save(entity);
				id++;
			}
			LogUtil.log("插入车辆维护明细表结束。", Level.INFO, null);
			if(updateAdjCarFileEntity != null) {
				LogUtil.log("插入车辆档案开始。", Level.INFO, null);
				List<AdJCarfile> beanList = adJCarfileRemote.findByProperty(
						"carNo", updateAdjCarFileEntity.getCarNo());
				if (beanList.size() > 0) {
					id = beanList.get(0).getId();
					AdJCarfile bean = adJCarfileRemote.findById(id);
					bean.setUseStatus(updateAdjCarFileEntity.getUseStatus());
					bean.setUpdateTime(new Date());
					bean.setUpdateUser(updateAdjCarFileEntity.getUpdateUser());
					adJCarfileRemote.update(bean);
					LogUtil.log("插入车辆档案结束。", Level.INFO, null);
				}
			}

			id = bll.getMaxId("AD_J_CARWH_LIST", "ID");
			LogUtil.log("插入材料结算清单表开始。", Level.INFO, null);
			for (int i = 0; i < saveAdJCarwhListList.size(); i++) {
				AdJCarwhList entity = saveAdJCarwhListList.get(i);
				entity.setId(id);
				entity.setWhId(whId);
				adJCarwhListRemote.save(entity);
				id++;
			}
			LogUtil.log("插入材料结算清单表结束。", Level.INFO, null);

		}catch(Exception e) {
			LogUtil.log("插入数据库失败。", Level.INFO, null);
			throw new SQLException();
		}

	}

	/**
	 * 车辆维修申请点击修改的DB操作
	 */
	public void updateDBOperate(AdJCarwh updateAdJCarwhEntity,
			List<AdJCarwhMx> updateAdJCarwhList,
			List<AdJCarwhMx> deleteAdJCarwhList,
			List<AdJCarwhMx> saveAdJCarwhList,
			AdJCarfile updateAdjCarFileEntity,
			List<AdJCarwhList> updateAdJCarwhListList,
			List<AdJCarwhList> deleteAdJCarwhListList,
			List<AdJCarwhList> saveAdJCarwhListList) throws SQLException,DataChangeException{
		try {
			LogUtil.log("车辆维护表排他开始。", Level.INFO, null);
			Long id = updateAdJCarwhEntity.getId();
			AdJCarwh adJCarwhEntity = adJCarwhRemote.findById(id);
			if (adJCarwhEntity == null) {
				throw new DataChangeException(null);
			} else {
				if (!DateToString(updateAdJCarwhEntity.getUpdateTime()).equals(
						DateToString(adJCarwhEntity.getUpdateTime()))) {
					throw new DataChangeException(null);
				}
			}
			LogUtil.log("车辆维护表排他结束。", Level.INFO, null);
			LogUtil.log("车辆维护明细表排他开始。", Level.INFO, null);
			if(updateAdJCarwhList.size()>0){
				for (int i = 0; i < updateAdJCarwhList.size(); i++) {
					AdJCarwhMx adJCarwhMxBean = updateAdJCarwhList.get(i);
					id = adJCarwhMxBean.getId();
					AdJCarwhMx adJCarwhMxEntity = adJCarwhMxRemote.findById(id);
					if (adJCarwhMxEntity == null) {
						throw new DataChangeException(null);
					} else {
						if (!DateToString(adJCarwhMxBean.getUpdateTime())
								.equals(
										DateToString(adJCarwhMxEntity
												.getUpdateTime()))) {
							throw new DataChangeException(null);
						}

					}
				}
			}
			if(deleteAdJCarwhList.size()>0){
				for(int i = 0;i<deleteAdJCarwhList.size();i++){
					AdJCarwhMx  adJCarwhMxBean = deleteAdJCarwhList.get(i);
					id = adJCarwhMxBean.getId();
					AdJCarwhMx adJCarwhMxEntity = adJCarwhMxRemote.findById(id);
					if (adJCarwhMxEntity == null) {
						throw new DataChangeException(null);
					} else {
						if (!DateToString(adJCarwhMxBean.getUpdateTime()).equals(
								DateToString(adJCarwhMxEntity.getUpdateTime()))) {
							throw new DataChangeException(null);
						}
				}
			}
		}
			LogUtil.log("车辆维护明细表排他结束。", Level.INFO, null);
			
			LogUtil.log("车辆档案排他开始。", Level.INFO, null);
			id = updateAdjCarFileEntity.getId();
			AdJCarfile adjCarfileBean = adJCarfileRemote.findById(id);
			if (adjCarfileBean == null) {
				throw new DataChangeException(null);
			} else {
				if (!DateToString(updateAdjCarFileEntity.getUpdateTime()).equals(
						DateToString(adjCarfileBean.getUpdateTime()))) {
					throw new DataChangeException(null);
				}
			}
			LogUtil.log("车辆档案排他结束。", Level.INFO, null);
			
			LogUtil.log("材料结算清单表排他开始。", Level.INFO, null);
			if(updateAdJCarwhListList.size()>0){
				for (int i = 0; i < updateAdJCarwhListList.size(); i++) {
					AdJCarwhList adJCarwhListBean = updateAdJCarwhListList.get(i);
					id = adJCarwhListBean.getId();
					AdJCarwhList adJCarwhListEntity = adJCarwhListRemote.findById(id);
					if (adJCarwhListEntity == null) {
						throw new DataChangeException(null);
					} else {
						if (!DateToString(adJCarwhListBean.getUpdateTime())
								.equals(
										DateToString(adJCarwhListEntity
												.getUpdateTime()))) {
							throw new DataChangeException(null);
						}

					}
				}
			}
			if(deleteAdJCarwhListList.size()>0){
				for (int i = 0; i < deleteAdJCarwhListList.size(); i++) {
					AdJCarwhList adJCarwhListBean = deleteAdJCarwhListList.get(i);
					id = adJCarwhListBean.getId();
					AdJCarwhList adJCarwhListEntity = adJCarwhListRemote.findById(id);
					if (adJCarwhListEntity == null) {
						throw new DataChangeException(null);
					} else {
						if (!DateToString(adJCarwhListBean.getUpdateTime())
								.equals(
										DateToString(adJCarwhListEntity
												.getUpdateTime()))) {
							throw new DataChangeException(null);
						}
					}
				}
			}
			LogUtil.log("材料结算清单表排他结束。", Level.INFO, null);
			
			LogUtil.log("更新车辆维护表开始。", Level.INFO, null);
			AdJCarwhSetValue(adJCarwhEntity,updateAdJCarwhEntity);
			adJCarwhRemote.update(adJCarwhEntity);
			LogUtil.log("更新车辆维护表结束。", Level.INFO, null);
			
			LogUtil.log("更新车辆维护明细表开始。", Level.INFO, null);
			for(int i = 0; i < updateAdJCarwhList.size(); i++){
				AdJCarwhMx adJCarwhMxBean = updateAdJCarwhList.get(i);
				AdJCarwhMx adJCarwhMxEntity =  adJCarwhMxRemote.findById(adJCarwhMxBean.getId());
				adJCarwhMxEntity.setProCode(adJCarwhMxBean.getProCode());
				adJCarwhMxEntity.setPrice(adJCarwhMxBean.getPrice());
				adJCarwhMxEntity.setUpdateUser(adJCarwhMxBean.getUpdateUser());
				adJCarwhMxEntity.setUpdateTime(new Date());
				adJCarwhMxRemote.update(adJCarwhMxEntity);
			}
			for(int i = 0; i < deleteAdJCarwhList.size(); i++){
				AdJCarwhMx adJCarwhMxBean = deleteAdJCarwhList.get(i);
				AdJCarwhMx adJCarwhMxEntity =  adJCarwhMxRemote.findById(adJCarwhMxBean.getId());
				adJCarwhMxEntity.setIsUse("N");
				adJCarwhMxEntity.setUpdateUser(adJCarwhMxBean.getUpdateUser());
				adJCarwhMxEntity.setUpdateTime(new Date());
				adJCarwhMxRemote.update(adJCarwhMxEntity);
			}
			id = bll.getMaxId("AD_J_CARWH_MX", "ID");
			for(int i = 0; i < saveAdJCarwhList.size(); i++){
				AdJCarwhMx adJCarwhMxBean = saveAdJCarwhList.get(i);
				adJCarwhMxBean.setId(id);
				adJCarwhMxBean.setUpdateTime(new Date());
				id++;
				adJCarwhMxRemote.save(adJCarwhMxBean);
			}
			LogUtil.log("更新车辆维护明细表结束。", Level.INFO, null);
			
			if(updateAdjCarFileEntity != null) {
				LogUtil.log("插入车辆档案开始。", Level.INFO, null);
				adjCarfileBean.setUseStatus(updateAdjCarFileEntity.getUseStatus());
				adjCarfileBean.setUpdateTime(new Date());
				adjCarfileBean.setUpdateUser(updateAdjCarFileEntity.getUpdateUser());
				adJCarfileRemote.update(adjCarfileBean);
				LogUtil.log("插入车辆档案结束。", Level.INFO, null);
				
			}
			LogUtil.log("更新材料结算表开始。", Level.INFO, null);
			for (int i = 0; i < updateAdJCarwhListList.size(); i++) {
				AdJCarwhList adJCarwhListBean = updateAdJCarwhListList.get(i);
				AdJCarwhList adJCarwhListEntity = adJCarwhListRemote.findById(adJCarwhListBean.getId());
				adJCarwhListEntity.setPartName(adJCarwhListBean.getPartName());
				adJCarwhListEntity.setUnit(adJCarwhListBean.getUnit());
				adJCarwhListEntity.setNum(adJCarwhListBean.getNum());
				adJCarwhListEntity.setUnitPrice(adJCarwhListBean.getUnitPrice());
				adJCarwhListEntity.setNote(adJCarwhListBean.getNote());
				adJCarwhListEntity.setUpdateUser(adJCarwhListBean.getUpdateUser());
				adJCarwhListEntity.setUpdateTime(new Date());
				adJCarwhListRemote.update(adJCarwhListEntity);
			}
			
			for(int i = 0; i < deleteAdJCarwhListList.size(); i++) {
				AdJCarwhList adJCarwhListBean = deleteAdJCarwhListList.get(i);
				AdJCarwhList adJCarwhListEntity = adJCarwhListRemote.findById(adJCarwhListBean.getId());
				adJCarwhListEntity.setIsUse("N");
				adJCarwhListEntity.setUpdateUser(adJCarwhListBean.getUpdateUser());
				adJCarwhListEntity.setUpdateTime(new Date());
				adJCarwhListRemote.update(adJCarwhListEntity);
			}
			id = bll.getMaxId("AD_J_CARWH_LIST", "ID");
			for(int i = 0; i < saveAdJCarwhListList.size(); i++) {
				AdJCarwhList adJCarwhListBean = saveAdJCarwhListList.get(i);
				adJCarwhListBean.setId(id);
				adJCarwhListBean.setUpdateTime(new Date());
				adJCarwhListRemote.save(adJCarwhListBean);
				id++;
			}
			LogUtil.log("更新材料结算表结束。", Level.INFO, null);
			
		}catch(DataChangeException e){
        	throw e;
		} catch (Exception e) {
			LogUtil.log("插入数据库失败。", Level.INFO, null);
			throw new SQLException();
		}
	}
	 private AdJCarwh AdJCarwhSetValue(AdJCarwh adJCarwhEntity,AdJCarwh updateAdJCarwhEntity){
		 adJCarwhEntity.setCarNo(updateAdJCarwhEntity.getCarNo());
		 adJCarwhEntity.setDriveMile(updateAdJCarwhEntity.getDriveMile());
		 adJCarwhEntity.setManager(updateAdJCarwhEntity.getManager());
		 adJCarwhEntity.setCpCode(updateAdJCarwhEntity.getCpCode());
		 adJCarwhEntity.setRepairDate(updateAdJCarwhEntity.getRepairDate());
		 adJCarwhEntity.setSum(updateAdJCarwhEntity.getSum());
		 adJCarwhEntity.setReason(updateAdJCarwhEntity.getReason());
		 adJCarwhEntity.setMemo(updateAdJCarwhEntity.getMemo());
		 adJCarwhEntity.setUpdateTime(new Date());
		 adJCarwhEntity.setUpdateUser(updateAdJCarwhEntity.getUpdateUser());
		 return adJCarwhEntity;
	 }
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String DateToString(Date date) {
		SimpleDateFormat defaultFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String sysDate = defaultFormat.format(date);
		return sysDate;
	}
	/**
	 * 删除DB操作
	 * @throws DataChangeException 
	 */
	public void deleteDBOperate(AdJCarwh deleteAdJCarwhEntity) throws SQLException, DataChangeException{
		try{
			LogUtil.log("车辆维护表排他开始。", Level.INFO, null);
			Long id = deleteAdJCarwhEntity.getId();
			AdJCarwh adJCarwhEntity = adJCarwhRemote.findById(id);
			if (adJCarwhEntity == null) {
				throw new DataChangeException(null);
			} else {
				if (!DateToString(deleteAdJCarwhEntity.getUpdateTime()).equals(
						DateToString(adJCarwhEntity.getUpdateTime()))) {
					throw new DataChangeException(null);
				}
			}
			LogUtil.log("车辆维护表排他结束。", Level.INFO, null);
			String whId = deleteAdJCarwhEntity.getWhId();
			LogUtil.log("材料结算清单删除开始。", Level.INFO, null);
			List<AdJCarwhList> deleteAdJCarwhListList = adJCarwhListRemote.findByProperty("whId", whId);
			for(int i =0;i<deleteAdJCarwhListList.size();i++){
				AdJCarwhList entity = deleteAdJCarwhListList.get(i);
				if("Y".equals(entity.getIsUse())){
					entity.setIsUse("N");
					entity.setUpdateTime(new Date());
					entity.setUpdateUser(deleteAdJCarwhEntity.getUpdateUser());
					adJCarwhListRemote.update(entity);
				}
			}
			LogUtil.log("材料结算清单删除结束。", Level.INFO, null);
			LogUtil.log("车辆维护明细删除开始。", Level.INFO, null);
			List<AdJCarwhMx> deleteAdJCarwhMxList = adJCarwhMxRemote.findByProperty("whId", whId);
			for(int i = 0;i<deleteAdJCarwhMxList.size();i++){
				AdJCarwhMx entity = deleteAdJCarwhMxList.get(i);
				if("Y".equals(entity.getIsUse())){
					entity.setIsUse("N");
					entity.setUpdateTime(new Date());
					entity.setUpdateUser(deleteAdJCarwhEntity.getUpdateUser());
					adJCarwhMxRemote.update(entity);
				}
			}
			LogUtil.log("车辆维护明细删除结束。", Level.INFO, null);
			LogUtil.log("车辆维护删除开始。", Level.INFO, null);
			adJCarwhEntity.setIsUse("N");
			adJCarwhEntity.setUpdateTime(new Date());
			adJCarwhEntity.setUpdateUser(deleteAdJCarwhEntity.getUpdateUser());
			adJCarwhRemote.update(adJCarwhEntity);
			LogUtil.log("车辆维护删除结束。", Level.INFO, null);
		}catch(DataChangeException e){
        	throw e;
		}catch(Exception e){
			LogUtil.log("删除数据库失败。", Level.INFO, null);
			throw new SQLException();
		}
	}
	
	/**
	 * 上报处理
	 */
	public void reportDBOperate(AdJCarwh reportAdJCarwhEntity) throws SQLException,DataChangeException{
		try{
		LogUtil.log("车辆维护表排他开始。", Level.INFO, null);
		Long id = reportAdJCarwhEntity.getId();
		AdJCarwh adJCarwhEntity = adJCarwhRemote.findById(id);
		if (adJCarwhEntity == null) {
			throw new DataChangeException(null);
		} else {
			if (!DateToString(reportAdJCarwhEntity.getUpdateTime()).equals(
					DateToString(adJCarwhEntity.getUpdateTime()))) {
				throw new DataChangeException(null);
			}
		}
		LogUtil.log("车辆维护表排他结束。", Level.INFO, null);
		LogUtil.log("车辆维护表上报开始。", Level.INFO, null);
		
		adJCarwhEntity.setDcmStatus("1");
		adJCarwhEntity.setUpdateTime(new Date());
		adJCarwhEntity.setUpdateUser(reportAdJCarwhEntity.getUpdateUser());
		adJCarwhRemote.update(adJCarwhEntity);
		LogUtil.log("车辆维护表上报结束。", Level.INFO, null);
		}catch(DataChangeException e){
        	throw e;
		}catch(Exception e){
			LogUtil.log("车辆维护表上报失败。", Level.INFO, null);
			throw new SQLException();
			
		}
	}
}
