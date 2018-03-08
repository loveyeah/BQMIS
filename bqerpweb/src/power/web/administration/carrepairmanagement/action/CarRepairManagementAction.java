/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.carrepairmanagement.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import javax.servlet.ServletOutputStream;

import power.ear.comm.DataChangeException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJCarfile;
import power.ejb.administration.AdJCarwh;
import power.ejb.administration.AdJCarwhInvoice;
import power.ejb.administration.AdJCarwhInvoiceFacadeRemote;
import power.ejb.administration.AdJCarwhList;
import power.ejb.administration.AdJCarwhMx;
import power.ejb.administration.business.CarRepairManagementFacadeRemote;
import power.ejb.administration.form.CarRepairAprroveBean;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 车辆维修申请Action
 * 
 * @author fangjihu
 * 
 */
public class CarRepairManagementAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 车辆维修申请ejb远程对象 */
	protected CarRepairManagementFacadeRemote remote;

	public CarRepairManagementAction() {
		// 车辆维修申请ejb远程对象
		remote = (CarRepairManagementFacadeRemote) factory
				.getFacadeRemote("CarRepairManagementFacade");
	}

	/**
	 * 车辆维修申请画面初始化
	 * 
	 * @throws JSONException
	 */
	public void getCarRepairApproveQueryList() throws JSONException {
		try{
		LogUtil.log("Action:车辆维修申请画面初始化开始。", Level.INFO, null);
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject obj = remote.findPurchaseOrderList(Integer.parseInt(start),
				Integer.parseInt(limit), employee.getEnterpriseCode());
		write(JSONUtil.serialize(obj));
		LogUtil.log("Action:车辆维修申请画面初始化结束。", Level.INFO, null);
		}catch (JSONException jsone) {
			LogUtil.log("Action:车辆维修申请画面初始化失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:车辆维修申请画面初始化失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 车辆维修单位维护单位名称
	 * 
	 * @throws JSONException
	 */
	public void getAdCCarmendWhCpCodeList() throws JSONException {
		try{
			
		LogUtil.log("Action:检索车辆维修单位维护单位名称开始。", Level.INFO, null);
		PageObject obj = remote.findCarmendWhCpCode(employee
				.getEnterpriseCode());
		write(JSONUtil.serialize(obj));
		LogUtil.log("Action:检索车辆维修单位维护单位名称结束。", Level.INFO, null);
		}catch (JSONException jsone) {
			LogUtil.log("Action:检索车辆维修单位维护单位名称失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:检索车辆维修单位维护单位名称失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 维修项目详细grid数据初始化
	 * 
	 * @throws JSONException
	 */
	public void getCarwhMxDetailList() throws JSONException {
		try{
			LogUtil.log("Action:维修项目详细grid数据初始化开始。", Level.INFO, null);
			
		String whId = request.getParameter("whId");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject obj = remote.findCarwhMxDetailList(whId, Integer
				.parseInt(start), Integer.parseInt(limit), employee
				.getEnterpriseCode());
		write(JSONUtil.serialize(obj));
		LogUtil.log("Action:维修项目详细grid数据初始化结束。", Level.INFO, null);
		}catch (JSONException jsone) {
			LogUtil.log("Action:维修项目详细grid数据初始化失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:维修项目详细grid数据初始化失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}

	}

	/**
	 * 车辆维护之维修项目
	 * 
	 * @throws JSONException
	 */
	public void getRepairProjectList() throws JSONException {
		try{
			LogUtil.log("Action:检索车辆维护之维修项目开始。", Level.INFO, null);
			
		PageObject obj = remote.findRepairProjectList(employee
				.getEnterpriseCode());
		write(JSONUtil.serialize(obj));
		LogUtil.log("Action:检索车辆维护之维修项目结束。", Level.INFO, null);
		}catch (JSONException jsone) {
			LogUtil.log("Action:检索车辆维护之维修项目失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:检索车辆维护之维修项目失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 通过项目编码取名称
	 */
	public void getProjectName() {
		try{
			LogUtil.log("Action:检索项目编码取名称开始。", Level.INFO, null);
		// 项目编码
		String proCode = request.getParameter("proCode");
		String proName = remote.findProjectName(proCode, employee
				.getEnterpriseCode());
		write(proName);
		LogUtil.log("Action:检索项目编码取名称结束。", Level.INFO, null);
		}catch (SQLException sqle) {
			LogUtil.log("Action:检索项目编码取名称失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 项目详细list
	 * 
	 * @throws JSONException
	 */
	public void getAdJCarwhList() throws JSONException {
		try{
			LogUtil.log("Action:查找项目详细list开始。", Level.INFO, null);
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String whId = request.getParameter("whId");
		String proCode = request.getParameter("proCode");
		PageObject obj = remote.findCarwhList(whId, proCode, employee
				.getEnterpriseCode(), Integer.parseInt(start), Integer
				.parseInt(limit));
		LogUtil.log("Action:查找项目详细list结束。", Level.INFO, null);
		write(JSONUtil.serialize(obj));
		}catch (JSONException jsone) {
			LogUtil.log("Action:查找项目详细list失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:查找项目详细list失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 处理保存操作
	 * 
	 * @throws JSONException
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public void updateCarwhList() throws JSONException, SQLException,
			DataChangeException {
		try {
			LogUtil.log("Action:处理保存操作开始。", Level.INFO, null);
			
			this.setFlag(request.getParameter("flag"));
			// 是否继续使用
			String isContiueUse = request.getParameter("isContiueUse");
			this.setIsContiueUse(isContiueUse);
			// 经办人
			String manager = request.getParameter("manager");
			this.setManager(manager);
			// 维修单位
			String cpCode = request.getParameter("cpCode");
			this.setCpCode(cpCode);
			// 车辆维修记录
			String carwh = request.getParameter("carwh");
			// 车辆明细
			String detailList = request.getParameter("detailList");
			// 项目明细
			String detailProjectList = request
					.getParameter("detailProjectList");

			Map carwhMap = null;
			if (!EMPTY.equals(carwh)) {
				carwhMap = (Map) JSONUtil.deserialize(carwh);
			}
			List<Map> detailMapList = (List<Map>) JSONUtil
					.deserialize(detailList);

			List<Map> detailProjectMapList = (List<Map>) JSONUtil
					.deserialize(detailProjectList);
			
				updateDBOperate(carwhMap, detailMapList, detailProjectMapList);
			write(SAVE_SUCCESS);
			LogUtil.log("Action:处理保存操作结束。", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:车辆维修管理DB操作失败", Level.SEVERE, e);
			// 排他操作
			write(Constants.DATA_USING);
		} catch (SQLException e) {
			LogUtil.log("Action:车辆维修管理DB操作失败", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		} catch (DataFormatException e) {
			LogUtil.log("Action:车辆维修管理DB操作失败", Level.SEVERE, e);
			write(Constants.DATA_FAILURE);
		}catch (JSONException e) {
			LogUtil.log("Action:车辆维修管理DB操作失败", Level.SEVERE, e);
			write(Constants.DATA_FAILURE);
		}
		catch (Exception e) {
			LogUtil.log("Action:车辆维修管理DB操作失败", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		}

	}

	/**
	 * 车辆维护设值
	 * @throws DataFormatException 
	 */
	@SuppressWarnings("unchecked")
	private AdJCarwh AdJCarwhBeanSetValue(Map map) throws DataFormatException {
		
			AdJCarwh entity = new AdJCarwh();
			// 序号
			entity.setId(Long.parseLong(map.get("id").toString()));
			
			// 维修里程=画面.维修里程
			entity.setDriveMile(bean.getDriveMile());
			
			// 单位编码=画面.单位编码
			entity.setCpCode(this.getCpCode());
			// 维修日期=画面.维修日期
			entity.setRepairDate(formatStringToDate(bean.getRepairDate(),DATE_FORMAT));
			// 实际费用=画面.实际费用
			entity.setRealSum(bean.getRealSum());
			if(ZERO.equals(this.getFlag())){
				entity.setRepairStatus(ONE);
			}
			// 备注=画面.备注
			entity.setMemo(bean.getMemo());
			// 修改人=登陆用户ID
			entity.setUpdateUser(employee.getWorkerCode());
			// 修改时间=系统时间
			String updateTime = map.get("updateTime").toString();
			// String transferUpdateTime = updateTime.replace('T', ' ');

			entity.setUpdateTime(formatStringToDate(updateTime, DATE_FORMAT_YYYYMMDD));
			return entity;
		
	}
	

	@SuppressWarnings("unchecked")
	private void updateDBOperate(Map carwhMap, List<Map> detailMapList,
			List<Map> detailProjectMapList) throws SQLException,
			DataChangeException, DataFormatException {
		// 车辆维护插入
		AdJCarwh updateAdJCarwhEntity = AdJCarwhBeanSetValue(carwhMap);
		// 车辆维护明细
		List<AdJCarwhMx> updateAdJCarwhList = new ArrayList<AdJCarwhMx>();

		List<AdJCarwhMx> deleteAdJCarwhList = new ArrayList<AdJCarwhMx>();
		List<AdJCarwhMx> saveAdJCarwhList = new ArrayList<AdJCarwhMx>();
		updateAdJCarwhMxOperate(updateAdJCarwhList, deleteAdJCarwhList,
				saveAdJCarwhList, detailMapList);

		// 车辆档案
		AdJCarfile updateAdjCarFileEntity = new AdJCarfile();
			updateAdjCarFileEntity.setId(Long.parseLong(carwhMap.get("driveFileId").toString()));
			if (YES.equals(this.isContiueUse)) {
				// 使用情况=‘Y’(占用)
				updateAdjCarFileEntity.setUseStatus(YES);
			}else{
				// 使用情况=‘N’(不占用)
				updateAdjCarFileEntity.setUseStatus(NO);	
			}
			if(ZERO.equals(this.getFlag())){
				updateAdjCarFileEntity.setUseStatus(NO);
			}
			// 修改人=登陆用户ID
			updateAdjCarFileEntity.setUpdateTime(formatStringToDate(carwhMap.get(
					"driveFileUpdateTime").toString(), DATE_FORMAT_YYYYMMDD));
			// 修改时间=系统时间
			updateAdjCarFileEntity.setUpdateUser(employee.getWorkerCode());
			
		// 材料结算清单表插入
		List<AdJCarwhList> updateAdJCarwhListList = new ArrayList<AdJCarwhList>();
		List<AdJCarwhList> deleteAdJCarwhListList = new ArrayList<AdJCarwhList>();
		List<AdJCarwhList> saveAdJCarwhListList = new ArrayList<AdJCarwhList>();

		updateAdJCarwhListOperate(updateAdJCarwhListList,
				deleteAdJCarwhListList, saveAdJCarwhListList,
				detailProjectMapList);
		remote.updateDBOperate(updateAdJCarwhEntity,updateAdJCarwhList,
				deleteAdJCarwhList,saveAdJCarwhList,
				updateAdjCarFileEntity,updateAdJCarwhListList,
				deleteAdJCarwhListList,saveAdJCarwhListList);

	}

	@SuppressWarnings("unchecked")
	private void updateAdJCarwhMxOperate(List<AdJCarwhMx> updateAdJCarwhList,
			List<AdJCarwhMx> deleteAdJCarwhList,
			List<AdJCarwhMx> saveAdJCarwhList, List<Map> detailMapList)
			throws DataFormatException {

		for (int i = 0; i < detailMapList.size(); i++) {
			Map map = detailMapList.get(i);
			// 序号
			String id = map.get("carwhMxId").toString();
			// 是否删除 "0":不删除 '1':删除
			String isDetele = map.get("isNew").toString();
			if ((!EMPTY.equals(id)) && (ZERO.equals(isDetele))) {
				// 更新
				AdJCarwhMx entity = new AdJCarwhMx();
				entity.setId(Long.parseLong(id));
				entity.setProCode(map.get("proCode").toString());
				entity.setRealPrice(Double.parseDouble(getStringToDouble(map
						.get("realPrice"))));
				entity.setUpdateUser(employee.getWorkerCode());
				String updateTime = map.get("carwhMxUpdateTime").toString();
				entity.setUpdateTime(formatStringToDate(updateTime,
						DATE_FORMAT_YYYYMMDD));
				updateAdJCarwhList.add(entity);
			} else if ((!EMPTY.equals(id)) && (ONE.equals(isDetele))) {
				// 删除
				AdJCarwhMx entity = new AdJCarwhMx();
				entity.setId(Long.parseLong(id));
				entity.setProCode(map.get("proCode").toString());
				entity.setUpdateUser(employee.getWorkerCode());
				String updateTime = map.get("carwhMxUpdateTime").toString();
				entity.setUpdateTime(formatStringToDate(updateTime,
						DATE_FORMAT_YYYYMMDD));
				deleteAdJCarwhList.add(entity);
			} else if ((EMPTY.equals(id)) && (ZERO.equals(isDetele))) {
				// 新规
				AdJCarwhMx entity = new AdJCarwhMx();
				entity.setWhId(bean.getWhId());
				entity.setProCode(map.get("proCode").toString());
				entity.setUpdateUser(employee.getWorkerCode());
				entity.setRealPrice(Double.parseDouble(getStringToDouble(map
						.get("realPrice"))));
				entity.setIsUse(YES);
				saveAdJCarwhList.add(entity);
			}

		}

	}

	/**
	 * 材料结算表处理
	 * @param updateAdJCarwhListList
	 * @param deleteAdJCarwhListList
	 * @param saveAdJCarwhListList
	 * @param detailProjectMapList
	 * @throws DataFormatException
	 */
	@SuppressWarnings("unchecked")
	private void updateAdJCarwhListOperate(
			List<AdJCarwhList> updateAdJCarwhListList,
			List<AdJCarwhList> deleteAdJCarwhListList,
			List<AdJCarwhList> saveAdJCarwhListList,
			List<Map> detailProjectMapList) throws DataFormatException{
			for(int i = 0;i<detailProjectMapList.size();i++){
				Map map = detailProjectMapList.get(i);
				// 序号
				String id = map.get("id").toString();
				// 是否删除 "0":不删除 '1':删除
				String isDetele = map.get("isNew").toString();
				if((!EMPTY.equals(id))&&(ZERO.equals(isDetele))){
					// 更新
					AdJCarwhList entity = new AdJCarwhList();
					// 材料结算清单id
					entity.setId(Long.parseLong(id));
					// 车辆维护ID
					entity.setWhId(map.get("whId").toString());
					// 项目编码
					entity.setProCode(map.get("proCode").toString());
					// 零件名称
					entity.setPartName(map.get("partName").toString());
					// 单位
					entity.setUnit(getString(map.get("unit")));
					// 预算数量
					entity.setNum(Double.parseDouble(map.get("num").toString()));
					// 预算单价
					entity.setUnitPrice(Double.parseDouble(map.get("unitPrice").toString()));
					
					// 实际数量
					// modify by liuyi 14:23 存在不输入实际数量和实际单价就保存的情况
					if(map.get("realNum") != null)
						entity.setRealNum(Double.parseDouble(map.get("realNum").toString()));
					else 
						entity.setRealNum(0.0);
					// 实际单价
					if(map.get("realUnitPrice") != null)
						entity.setRealUnitPrice(Double.parseDouble(map.get("realUnitPrice").toString()));
					else 
						entity.setRealUnitPrice(0.0);
						
					// 备注
					entity.setNote(getString(map.get("note")));
					entity.setUpdateUser(employee.getWorkerCode());
					String updateTime = map.get("updateTime").toString();
					entity.setUpdateTime(formatStringToDate(updateTime,DATE_FORMAT_YYYYMMDD));
					updateAdJCarwhListList.add(entity);
				}else if((!EMPTY.equals(id)) && (ONE.equals(isDetele))){
					AdJCarwhList entity = new AdJCarwhList();
					// 材料结算清单id
					entity.setId(Long.parseLong(id));
					entity.setUpdateUser(employee.getWorkerCode());
					String updateTime = map.get("updateTime").toString();
					entity.setUpdateTime(formatStringToDate(updateTime,DATE_FORMAT_YYYYMMDD));
					deleteAdJCarwhListList.add(entity);
				}else if((EMPTY.equals(id)) && (ZERO.equals(isDetele))){
					AdJCarwhList entity = new AdJCarwhList();
					// 车辆维护ID
					entity.setWhId(map.get("whId").toString());
					// 项目编码
					entity.setProCode(map.get("proCode").toString());
					// 零件名称
					entity.setPartName(map.get("partName").toString());
					// 单位
					entity.setUnit(getString(map.get("unit")));
					// 预算数量
					entity.setNum(Double.parseDouble(map.get("num").toString()));
					// 预算单价
					entity.setUnitPrice(Double.parseDouble(map.get("unitPrice").toString()));
					// 实际数量
					entity.setRealNum(Double.parseDouble(map.get("realNum").toString()));
					// 实际单价
					entity.setRealUnitPrice(Double.parseDouble(map.get("realUnitPrice").toString()));
					// 备注
					entity.setNote(getString(map.get("note")));
					entity.setIsUse(YES);
					entity.setUpdateUser(employee.getWorkerCode());
					saveAdJCarwhListList.add(entity);
				}
			}
			
		}
	
	/**
	 * 取得车辆维修发票附件信息
	 */
	public void getAdJCarwhInvoiceFile(){
		try {
			LogUtil.log("Action:取得车辆维修发票附件信息开始。", Level.INFO, null);
			String whId = request.getParameter("whId");
			// 查询
			PageObject object = remote.findAdJCarwhInvoiceFile(whId);
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:取得车辆维修发票附件信息结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:取得车辆维修发票附件信息失败。", Level.SEVERE, e);
		} catch (SQLException e) {
			LogUtil.log("Action:取得车辆维修发票附件信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 文件的上传处理
	 * 
	 * 
	 */
	public void uploadAdJCarwhInvoiceFile(){
		LogUtil.log("Action:文件的上传操作开始", Level.INFO, null);
		String whId = request.getParameter("whId");	
		AdJCarwhInvoice entity = new AdJCarwhInvoice();
		
		InputStream bis = null;
		if (fileUpload != null) {
			if (fileUpload.exists()) {
				Long lngLength = fileUpload.length();
				int intLength = Integer.parseInt(lngLength.toString());
				try {
					bis = new BufferedInputStream(new FileInputStream(
							fileUpload), 1024);
					byte[] data = new byte[intLength];
					while (bis.read(data) == -1) {

					}
					entity.setFileText(data);
					entity.setWhId(whId);
					entity.setUpdateUser(employee.getWorkerCode());
					entity.setFileName(fileUploadFileName);
					entity.setIsUse("Y");
					entity.setFileType(fileUploadContentType);
					entity.setUpdateTime(new java.util.Date());
					remote.uploadAdJCarwhInvoiceFile(entity);
					write(Constants.UPLOAD_SUCCESS);
					LogUtil.log("Action:文件的上传操作正常结束", Level.INFO, null);
				} catch (IOException e) {
					LogUtil.log("Action:文件的上传操作异常结束", Level.SEVERE, null);
					write(Constants.IO_FAILURE);
				} catch (SQLException e) {
					LogUtil.log("Action:文件的上传操作异常结束", Level.SEVERE, null);
					write(Constants.SQL_FAILURE);
				} catch (Exception e) {
					LogUtil.log("Action:文件的上传操作异常结束", Level.SEVERE, null);
				}
			}else{
				write(Constants.FILE_NOT_EXIST);
			}
		}
	}
	/**
	 * 删除车辆维修发票附件
	 */
	public void deleteAdJCarwhInvoiceFile(){
		try {
			LogUtil.log("Action:删除会务申请附件开始。", Level.INFO, null);
			Long id = Long.parseLong(request.getParameter("id"));
			String updateTime = request.getParameter("updateTime");
			remote.deleteAdJCarwhInvoiceFile(id, updateTime,
					employee.getWorkerCode());
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除会务申请附件结束。", Level.INFO, null);
		} catch (Exception e) {
			// 排他操作
			write(Constants.DATA_USING);
			LogUtil.log("Action:删除会务申请附件失败。", Level.SEVERE, null);
		}
	}
	/**
	 * 下载附件
	 */
	public void downloadAdJCarwhInvoiceFile()throws IOException {
		try {
			LogUtil.log("Action:下载会务申请附件开始。", Level.INFO, null);
			Long id = Long.parseLong(request.getParameter("id"));
			AdJCarwhInvoiceFacadeRemote invoiceRemote =(AdJCarwhInvoiceFacadeRemote)factory.getFacadeRemote("AdJCarwhInvoiceFacade");
			AdJCarwhInvoice entity = invoiceRemote.findById(id);
			
			// 情報設定
			String name = URLEncoder.encode(entity.getFileName(),
					"UTF-8");

			response.setHeader("Content-Disposition",
					"attachment; filename=" + name);
			response.setContentType(entity.getFileType());
			ServletOutputStream os = response.getOutputStream();
			BufferedOutputStream out = new BufferedOutputStream(os);
			out.write(entity.getFileText());
			out.flush();
			out.close();
			os.flush();
			os.close();
			LogUtil.log("Action:下载会务申请附件结束。", Level.INFO, null);
		
		} catch (IOException e) {
			LogUtil.log("Action:下载会务申请附件失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 根据日期日期字符串和形式返回日期
	 * 
	 * @param argDateStr
	 *            日期字符串
	 * @param argFormat
	 *            日期形式字符串
	 * @return 日期
	 */
	private Date formatStringToDate(String argDateStr, String argFormat)
			throws DataFormatException {
		if (argDateStr == null || argDateStr.trim().length() < 1) {
			return null;
		}
		// 日期形式
		SimpleDateFormat sdfFrom = null;
		// 返回日期
		Date dtresult = null;

		try {
			sdfFrom = new SimpleDateFormat(argFormat);
			// 格式化日期
			dtresult = sdfFrom.parse(argDateStr);
		} catch (Exception e) {
			dtresult = null;
		} finally {
			sdfFrom = null;
		}

		return dtresult;
	}

	private String getStringToDouble(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "0";
		}
	}
	
	/**
	 * 字符串处理
	 */
	private String getString(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return null;
		}
	}

	private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMAT ="yyyy-MM-dd";
	private static final String EMPTY = "";
	private static final String YES = "Y";
	private static final String NO = "N";
	private static final String ZERO = "0";
	private static final String ONE = "1";
	private CarRepairAprroveBean bean;
	
	private String flag;
	private String isContiueUse;

	private String manager;

	private String cpCode;
	
	/**文件上传**/
	private File fileUpload;
	/**文件名称**/
	private String fileUploadFileName;
	private String fileUploadContentType;

	private static final String SAVE_SUCCESS = "{success:true,msg:'&nbsp&nbsp&nbsp保存成功！&nbsp&nbsp&nbsp'}";
	
	/**
	 * @return the bean
	 */
	public CarRepairAprroveBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(CarRepairAprroveBean bean) {
		this.bean = bean;
	}

	/**
	 * @return the isContiueUse
	 */
	public String getIsContiueUse() {
		return isContiueUse;
	}

	/**
	 * @param isContiueUse
	 *            the isContiueUse to set
	 */
	public void setIsContiueUse(String isContiueUse) {
		this.isContiueUse = isContiueUse;
	}

	/**
	 * @return the manager
	 */
	public String getManager() {
		return manager;
	}

	/**
	 * @param manager
	 *            the manager to set
	 */
	public void setManager(String manager) {
		this.manager = manager;
	}

	/**
	 * @return the cpCode
	 */
	public String getCpCode() {
		return cpCode;
	}

	/**
	 * @param cpCode
	 *            the cpCode to set
	 */
	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * @return the fileUpload
	 */
	public File getFileUpload() {
		return fileUpload;
	}

	/**
	 * @param fileUpload the fileUpload to set
	 */
	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}

	/**
	 * @return the fileUploadFileName
	 */
	public String getFileUploadFileName() {
		return fileUploadFileName;
	}

	/**
	 * @param fileUploadFileName the fileUploadFileName to set
	 */
	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}

	/**
	 * @return the fileUploadContentType
	 */
	public String getFileUploadContentType() {
		return fileUploadContentType;
	}

	/**
	 * @param fileUploadContentType the fileUploadContentType to set
	 */
	public void setFileUploadContentType(String fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}
}
