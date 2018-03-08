/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.meetcharge.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.ParseException;
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
import power.ejb.administration.AdJMeet;
import power.ejb.administration.AdJMeetFacadeRemote;
import power.ejb.administration.AdJMeetMx;
import power.ejb.administration.AdJMeetMxFacadeRemote;
import power.ejb.administration.AdJMeetfile;
import power.ejb.administration.AdJMeetfileFacadeRemote;
import power.ejb.administration.business.MeetChargeFacadeRemote;
import power.ejb.administration.form.MeetChargeInfo;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会务费用管理 Action
 * 
 * @author wangyun
 * 
 */
public class MeetChargeAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	/** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHmmss */
	private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	/** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHMM */
	private static final String DATE_FORMAT_YYYYMMDD_HHMM = "yyyy-MM-dd HH:mm";
	/** 会务费用管理接口 */
	private MeetChargeFacadeRemote meetChargeRemote;
	/** 会议附件信息表接口 */
	private AdJMeetfileFacadeRemote adjMeetFileRemote;
	/** 会议审批单表接口 */
	private AdJMeetFacadeRemote adjMeetRemote;
	/** 会议费用审批表接口 */
	private AdJMeetMxFacadeRemote adjMeetMxRemote;
	/** 分页起始 */
	private String start;
	/** 每页记录数 */
	private String limit;
	/** 会议申请单号 */
	private String meetId;
	/** 序号 */
	private Long id;
	/** 会议审批单 修改时间 */
	private String meetUpdateTime;
	/** 会议附件信息表 修改时间 */
	private String meetFileUpdateTime;
	/** 会议接待审批费用 修改时间 */
	private String meetMxUpdateTime;
	/** 新增加的明细记录 */
	private String newDetail;
	/** 原db中修改的记录 */
	private String dbDetail;
	/** 已删除的记录 */
	private String deleteDetailId;
	/** 会议审批单表 */
	private MeetChargeInfo meet;
	/** 文件上传* */
	private File fileUpload;
	/** 文件名称* */
	private String fileUploadFileName;
	private String fileUploadContentType;

	/**
	 * 构造函数
	 */
	public MeetChargeAction() {
		// 会务费用管理接口
		meetChargeRemote = (MeetChargeFacadeRemote) factory
				.getFacadeRemote("MeetChargeFacade");
		// 会议附件信息表接口
		adjMeetFileRemote = (AdJMeetfileFacadeRemote) factory
				.getFacadeRemote("AdJMeetfileFacade");
		// 会议审批单表接口
		adjMeetRemote = (AdJMeetFacadeRemote) factory
				.getFacadeRemote("AdJMeetFacade");
		// 会议费用审批表接口
		adjMeetMxRemote = (AdJMeetMxFacadeRemote) factory
				.getFacadeRemote("AdJMeetMxFacade");
	}

	/**
	 * 会务费用管理一览
	 * 
	 * @throws JSONException
	 */
	public void getMeetChargeInfo() throws JSONException {
		LogUtil.log("Action:会务费用管理一览开始", Level.INFO, null);
		String strWorkerCode = employee.getWorkerCode();
		try {
			String strEnterpriseCode = employee.getEnterpriseCode();
			PageObject pobj = meetChargeRemote.getMeetChargeGeneral(
					strWorkerCode, strEnterpriseCode, Integer.parseInt(start),
					Integer.parseInt(limit));
			String string = null;
			if (pobj.getTotalCount() == null) {
				string = "{\"list\":[],\"totalCount\":0}";
				write(string);
			} else {
				write(JSONUtil.serialize(pobj));
			}
			LogUtil.log("Action:会务费用管理一览结束", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:会务费用管理一览失败", Level.SEVERE, null);
		}
	}

	/**
	 * 取得会务附件信息
	 */
	public void getMeetFile() {
		try {
			LogUtil.log("Action:取得会务附件信息开始。", Level.INFO, null);
			// 查询
			PageObject object = meetChargeRemote.findMeetFile(meetId);
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:取得会务附件信息结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:取得会务附件信息失败。", Level.SEVERE, e);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:取得会务附件信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 文件的上传处理
	 * 
	 */
	public void uploadMeetFile() {
		LogUtil.log("Action:文件的上传操作开始", Level.INFO, null);
		AdJMeetfile entity = new AdJMeetfile();
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
					entity.setMeetId(meetId);
					entity.setUpdateUser(employee.getWorkerCode());
					entity.setFileName(fileUploadFileName);
					entity.setIsUse("Y");
					entity.setFileType(fileUploadContentType);
					entity.setUpdateTime(new java.util.Date());
					adjMeetFileRemote.save(entity);
					write(Constants.UPLOAD_SUCCESS);
					LogUtil.log("Action:文件的上传操作正常结束", Level.INFO, null);
				} catch (IOException e) {
					LogUtil.log("Action:文件的上传操作异常结束", Level.SEVERE, null);
					write(Constants.IO_FAILURE);
				} catch (Exception e) {
					LogUtil.log("Action:文件的上传操作异常结束", Level.SEVERE, null);
				}
			} else {
				write(Constants.FILE_NOT_EXIST);
			}
		}
	}

	/**
	 * 下载会务申请附件
	 * 
	 * @throws IOException
	 */
	public void downloadMeetFile() throws IOException {
		try {
			LogUtil.log("Action:下载会务申请附件开始。", Level.INFO, null);
			// 根据id查找签报申请附件
			PageObject object = meetChargeRemote.findMeetFileById(id);
			if (object.getList().size() != 0) {
				AdJMeetfile adJMeetFile = (AdJMeetfile) object.getList().get(0);
				// 情報設定
				String name = URLEncoder.encode(adJMeetFile.getFileName(),
						"UTF-8");

				response.setHeader("Content-Disposition",
						"attachment; filename=" + name);
				response.setContentType(adJMeetFile.getFileType());
				ServletOutputStream os = response.getOutputStream();
				BufferedOutputStream out = new BufferedOutputStream(os);
				out.write(adJMeetFile.getFileText());
				out.flush();
				out.close();
				os.flush();
				os.close();
				LogUtil.log("Action:下载会务申请附件结束。", Level.INFO, null);
			}
		} catch (IOException e) {
			LogUtil.log("Action:下载会务申请附件失败。", Level.SEVERE, e);
		} catch (SQLException e) {
			LogUtil.log("Action:下载会务申请附件失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 取得费用明细数据
	 * 
	 * @throws JSONException
	 */
	public void getChargeDetail() throws JSONException {
		try {
			LogUtil.log("Action:取得费用明细数据开始。", Level.INFO, null);
			// 查询
			PageObject object = meetChargeRemote.findChargeDetail(meetId);
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:取得费用明细数据结束。", Level.INFO, null);
		} catch (SQLException e) {
			LogUtil.log("Action:取得费用明细数据失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 删除会务申请附件
	 * 
	 * @throws SQLException
	 */
	public void deleteMeetFile() throws SQLException {
		try {
			LogUtil.log("Action:删除会务申请附件开始。", Level.INFO, null);
			AdJMeetfile entity = adjMeetFileRemote.findById(id);
			meetChargeRemote.deleteMeetFile(entity, meetFileUpdateTime,
					employee.getWorkerCode());
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除会务申请附件结束。", Level.INFO, null);
		} catch (DataChangeException e) {
			// 排他操作
			write(Constants.DATA_USING);
			LogUtil.log("Action:删除会务申请附件失败。", Level.SEVERE, null);
		} catch (ParseException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:删除会务申请附件失败。", Level.SEVERE, null);
		}
	}

	/**
	 * 更新会议审批单表
	 */
	private void updateADJMeet(List<AdJMeet> lstUpdateADJMeet) {
		try {
			List<AdJMeet> lstEntity = adjMeetRemote.findByMeetId(meet
					.getMeetId());
			AdJMeet entity = lstEntity.get(0);
			// 会议开始时间
			entity.setStartmeetDate(formatStringToDate(meet.getStartMeetDate(),
					DATE_FORMAT_YYYYMMDD_HHMM));
			// 会议结束时间
			entity.setEndmeetDate(formatStringToDate(meet.getEndMeetDate(),
					DATE_FORMAT_YYYYMMDD_HHMM));
			// 会议名称
			entity.setMeetName(meet.getMeetName());
			// 会议地点
			entity.setMeetPlace(meet.getMeetPlace());
			// 会场要求
			entity.setRoomNeed(meet.getRoomNeed());
			// 会议其他要求
			entity.setMeetOther(meet.getMeetOther());
			// 就餐时间
			entity.setDinnerTime(formatStringToDate(meet.getDinnerTime(),
					DATE_FORMAT_YYYYMMDD_HHMM));
			// 就餐人数
			entity.setDinnerNum(meet.getDinnerNum());
			// 会议用烟名称
			entity.setCigName(meet.getCigName());
			// 会议用烟数量
			entity.setCigNum(meet.getCigNum());
			// 会议用酒名称
			entity.setWineName(meet.getWineName());
			// 会议用酒数量
			entity.setWineNum(meet.getWineNum());
			// 会议住宿-套房数量
			entity.setTfNum(meet.getTfNum());
			// 会议住宿-套房用品
			entity.setTfThing(meet.getTfThing());
			// 会议住宿-单间数量
			entity.setDjNum(meet.getDjNum());
			// 会议住宿-单间用品
			entity.setDjThing(meet.getDjThing());
			// 会议住宿-标间数量
			entity.setBjNum(meet.getBjNum());
			// 会议住宿-标间用品
			entity.setBjThing(meet.getBjThing());
			// 会议用烟价格
			entity.setCigPrice(meet.getCigPrice());
			// 会议用酒价格
			entity.setWinePrice(meet.getWinePrice());
			// 用餐标准
			entity.setDinnerBz(meet.getDinnerBz());
			// 预计费用汇总
			entity.setBudpayInall(meet.getBudpayInall());
			// 实际费用汇总
			entity.setRealpayInall(meet.getRealpayInall());
			// 修改人
			entity.setUpdateUser(employee.getWorkerCode());
			lstUpdateADJMeet.add(entity);
		} catch (DataFormatException e) {
			LogUtil.log("Action:保存会议费用信息失败。", Level.SEVERE, null);
		}
	}

	/**
	 * 删除会议审批费用表记录
	 * 
	 * @throws DataFormatException
	 */
	private void deleteDetails(List<Map> deleteDetails,
			List<AdJMeetMx> lstDeleteADJMeetMx) throws DataFormatException {

		Map map;
		Long detailId;
		// 如果id集为空不需要删除
		if (deleteDetails == null || deleteDetails.size() < 1) {
			return;
		}
		// 检索已删除的记录
		for (int i = 0; i < deleteDetails.size(); i++) {
			// 获得画面明细部数据
			map = deleteDetails.get(i);
			// 获得流水号
			detailId = Long.parseLong(getString(map.get("id")));
			// 会议审批费用信息
			AdJMeetMx entity = adjMeetMxRemote.findById(detailId);
			// 修改者
			entity.setUpdateUser(employee.getWorkerCode());
			entity.setIsUse(Constants.IS_USE_N);
			String strUpdateTime = getString(map.get("updateTime"));
			strUpdateTime = strUpdateTime.substring(0, 10) + " "
					+ strUpdateTime.substring(11, 19);
			entity.setUpdateTime(formatStringToDate(strUpdateTime,
					DATE_FORMAT_YYYYMMDD_HHMMSS));
			// 更新
			lstDeleteADJMeetMx.add(entity);
		}
	}

	/**
	 * 增加会议审批费用表记录
	 */
	private void addDetails(List<Map> newDetails,
			List<AdJMeetMx> lstSaveADJMeetMx) {
		// 会议审批费用表记录
		AdJMeetMx entity;
		Map map;
		// 循环
		for (int i = 0; i < newDetails.size(); i++) {
			// 获得画面明细部数据
			map = newDetails.get(i);
			entity = new AdJMeetMx();
			// 会议申请单号
			entity.setMeetId(meet.getMeetId());
			// 费用名称
			entity.setPayName(getString(map.get("payName")));
			// 费用预算
			entity.setPayBudget(getDouble(getString(map.get("payBudget"))));
			// 实际费用
			entity.setPayReal(getDouble(getString(map.get("payReal"))));
			// 备注
			entity.setNote(getString(map.get("note")));
			// 修改人
			entity.setUpdateUser(employee.getWorkerCode());
			// 是否使用 = 'Y'
			entity.setIsUse(Constants.IS_USE_Y);
			lstSaveADJMeetMx.add(entity);
		}

	}

	/**
	 * 修改会议审批费用表记录
	 * 
	 * @throws DataFormatException
	 */
	private void updateDetails(List<Map> dbDetails,
			List<AdJMeetMx> lstUpdateADJMeetMx) throws DataFormatException {

		// 会议审批费用表
		AdJMeetMx entity;
		Map map;
		Long detailId;
		for (int i = 0; i < dbDetails.size(); i++) {
			// 获得画面明细部数据
			map = dbDetails.get(i);
			// 获得流水号
			detailId = Long.parseLong(getString(map.get("id")));
			// 根据流水号查找供应商资质类型
			entity = adjMeetMxRemote.findById(detailId);
			// 费用名称
			entity.setPayName(getString(map.get("payName")));
			// 费用预算
			entity.setPayBudget(getDouble(getString(map.get("payBudget"))));
			// 实际费用
			entity.setPayReal(getDouble(getString(map.get("payReal"))));
			// 备注
			entity.setNote(getString(map.get("note")));
			entity.setUpdateUser(employee.getWorkerCode());
			String strUpdateTime = getString(map.get("updateTime"));
			strUpdateTime = strUpdateTime.replace("T", " ");
			entity.setUpdateTime(formatStringToDate(strUpdateTime,
					DATE_FORMAT_YYYYMMDD_HHMMSS));
			lstUpdateADJMeetMx.add(entity);
		}
	}

	/**
	 * 保存会议费用信息
	 * 
	 * @throws JSONException
	 */
	public void saveMeetInfo() throws JSONException {
		try {
			LogUtil.log("Action:保存会议费用信息开始。", Level.INFO, null);
			// 更新会议审批单表记录
			List<AdJMeet> lstUpdateADJMeet = new ArrayList<AdJMeet>();
			// 更新会议审批费用表记录
			List<AdJMeetMx> lstUpdateADJMeetMx = new ArrayList<AdJMeetMx>();
			// 增加会议审批费用表记录
			List<AdJMeetMx> lstSaveADJMeetMx = new ArrayList<AdJMeetMx>();
			// 删除会议审批费用表记录
			List<AdJMeetMx> lstDeleteADJMeetMx = new ArrayList<AdJMeetMx>();

			// 修改供应商信息
			updateADJMeet(lstUpdateADJMeet);

			// 新记录
			List<Map> newDetails = (List<Map>) JSONUtil.deserialize(newDetail);
			// 修改的记录
			List<Map> dbDetails = (List<Map>) JSONUtil.deserialize(dbDetail);
			// 删除的记录
			List<Map> deleteDetails = (List<Map>) JSONUtil
					.deserialize(deleteDetailId);
			deleteDetails(deleteDetails, lstDeleteADJMeetMx);

			if (newDetails.size() > 0) {
				addDetails(newDetails, lstSaveADJMeetMx);
			}
			if (dbDetails.size() > 0) {
				// 修改db记录
				updateDetails(dbDetails, lstUpdateADJMeetMx);
			}

			meetChargeRemote.saveMeet(lstUpdateADJMeet, lstUpdateADJMeetMx,
					lstSaveADJMeetMx, lstDeleteADJMeetMx, meetUpdateTime);
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:保存会议费用信息结束。", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:保存会议费用信息失败。", Level.SEVERE, null);
			write(Constants.DATA_USING);
		} catch (DataFormatException e) {
			LogUtil.log("Action:保存会议费用信息失败。", Level.SEVERE, null);
			write(Constants.DATA_FAILURE);
		} catch (SQLException e) {
			LogUtil.log("Action:保存会议费用信息失败。", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		} catch (Exception e) {
			LogUtil.log("Action:保存会议费用信息失败。", Level.SEVERE, null);
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
	 * @throws DataFormatException
	 */
	private Date formatStringToDate(String argDateStr, String argFormat)
			throws DataFormatException {
		if (argDateStr == null || argDateStr.trim().length() < 1) {
			return null;
		}
		Date dd = new java.util.Date();
		// 日期形式
		SimpleDateFormat sdfFrom = null;
		// 返回日期
		Date result = null;

		try {
			sdfFrom = new SimpleDateFormat(argFormat);
			// 格式化日期
			result = sdfFrom.parse(argDateStr);
		} catch (Exception e) {
			write(Constants.DATA_FAILURE);
			result = null;
			throw new DataFormatException();
		} finally {
			sdfFrom = null;
		}
		return result;
	}

	/**
	 * 获得字符串值
	 */
	private String getString(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}

	}

	/**
	 * 获得浮点型值
	 */
	private Double getDouble(String str) {
		if (!"".equals(str) && null != str) {
			return Double.parseDouble(str);
		} else {
			return (double) 0;
		}
	}

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public String getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}

	/**
	 * @return the meetId
	 */
	public String getMeetId() {
		return meetId;
	}

	/**
	 * @param meetId
	 *            the meetId to set
	 */
	public void setMeetId(String meetId) {
		this.meetId = meetId;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the meetUpdateTime
	 */
	public String getMeetUpdateTime() {
		return meetUpdateTime;
	}

	/**
	 * @param meetUpdateTime
	 *            the meetUpdateTime to set
	 */
	public void setMeetUpdateTime(String meetUpdateTime) {
		this.meetUpdateTime = meetUpdateTime;
	}

	/**
	 * @return the meetFileUpdateTime
	 */
	public String getMeetFileUpdateTime() {
		return meetFileUpdateTime;
	}

	/**
	 * @param meetFileUpdateTime
	 *            the meetFileUpdateTime to set
	 */
	public void setMeetFileUpdateTime(String meetFileUpdateTime) {
		this.meetFileUpdateTime = meetFileUpdateTime;
	}

	/**
	 * @return the meetMxUpdateTime
	 */
	public String getMeetMxUpdateTime() {
		return meetMxUpdateTime;
	}

	/**
	 * @param meetMxUpdateTime
	 *            the meetMxUpdateTime to set
	 */
	public void setMeetMxUpdateTime(String meetMxUpdateTime) {
		this.meetMxUpdateTime = meetMxUpdateTime;
	}

	/**
	 * @return the newDetail
	 */
	public String getNewDetail() {
		return newDetail;
	}

	/**
	 * @param newDetail
	 *            the newDetail to set
	 */
	public void setNewDetail(String newDetail) {
		this.newDetail = newDetail;
	}

	/**
	 * @return the dbDetail
	 */
	public String getDbDetail() {
		return dbDetail;
	}

	/**
	 * @param dbDetail
	 *            the dbDetail to set
	 */
	public void setDbDetail(String dbDetail) {
		this.dbDetail = dbDetail;
	}

	/**
	 * @return the deleteDetailId
	 */
	public String getDeleteDetailId() {
		return deleteDetailId;
	}

	/**
	 * @param deleteDetailId
	 *            the deleteDetailId to set
	 */
	public void setDeleteDetailId(String deleteDetailId) {
		this.deleteDetailId = deleteDetailId;
	}

	/**
	 * @return the meet
	 */
	public MeetChargeInfo getMeet() {
		return meet;
	}

	/**
	 * @param meet
	 *            the meet to set
	 */
	public void setMeet(MeetChargeInfo meet) {
		this.meet = meet;
	}

	/**
	 * @return the fileUpload
	 */
	public File getFileUpload() {
		return fileUpload;
	}

	/**
	 * @param fileUpload
	 *            the fileUpload to set
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
	 * @param fileUploadFileName
	 *            the fileUploadFileName to set
	 */
	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}
}
