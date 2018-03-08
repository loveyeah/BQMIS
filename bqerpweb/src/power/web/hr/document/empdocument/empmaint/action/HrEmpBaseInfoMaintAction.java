/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.web.hr.document.empdocument.empmaint.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCAppellationFacadeRemote;
import power.ejb.hr.HrCContracttermFacadeRemote;
import power.ejb.hr.HrCDegreeFacadeRemote;
import power.ejb.hr.HrCDeptFacadeRemote;
import power.ejb.hr.HrCEducationFacadeRemote;
import power.ejb.hr.HrCNationFacadeRemote;
import power.ejb.hr.HrCNativePlaceFacadeRemote;
import power.ejb.hr.HrCSchoolFacadeRemote;
import power.ejb.hr.HrCSpecialtyFacadeRemote;
import power.ejb.hr.HrCStationFacadeRemote;
import power.ejb.hr.HrCStudytypeFacadeRemote;
import power.ejb.hr.HrCTechnologyGradeFacadeRemote;
import power.ejb.hr.HrCTechnologyTitlesFacadeRemote;
import power.ejb.hr.HrCWorkidFacadeRemote;
import power.ejb.hr.HrJContractstop;
import power.ejb.hr.HrJContractstopFacadeRemote;
import power.ejb.hr.HrJEducation;
import power.ejb.hr.HrJEducationBean;
import power.ejb.hr.HrJEducationFacadeRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoBean;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.HrJEmpPhoto;
import power.ejb.hr.HrJEmpPhotoFacadeRemote;
import power.ejb.hr.HrJEmpStation;
import power.ejb.hr.HrJEmpStationFacadeRemote;
import power.ejb.hr.HrJFamilyMemberFacadeRemote;
import power.ejb.hr.HrJFamilymember;
import power.ejb.hr.HrJFamilymemberBean;
import power.ejb.hr.HrJPolitics;
import power.ejb.hr.HrJPoliticsBean;
import power.ejb.hr.HrJPoliticsFacadeRemote;
import power.ejb.hr.HrJPosition;
import power.ejb.hr.HrJPositionFacadeRemote;
import power.ejb.hr.HrJProfessionalPost;
import power.ejb.hr.HrJProfessionalPostFacadeRemote;
import power.ejb.hr.HrJWorkcontract;
import power.ejb.hr.HrJWorkcontractFacadeRemote;
import power.ejb.hr.HrJWorkresume;
import power.ejb.hr.HrJWorkresumeFacadeRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.WorkContract;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import bsh.org.objectweb.asm.Label;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 员工档案综合查询维护 Action
 * 
 * @author huyou
 * @version 1.2
 */
public class HrEmpBaseInfoMaintAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 日期形式字符串 yyyy-MM-dd */
	private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	/** 日期形式字符串 yyyy-MM-dd HH:mm */
	private static final String DATE_FORMAT_YYYYMMDD_TIME = "yyyy-MM-dd HH:mm";
	/** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
	private static final String DATE_FORMAT_YYYYMMDD_TIME_SEC = "yyyy-MM-dd HH:mm:ss";

	/**
	 * add by liuyi 20100610 人员基本信息导入导出excel模板列名 0:姓名 1:人员编码 2:内部编号 3:部门名称
	 * 4:岗位名称 5:出生日期 6:年龄 7:性别 8:民族 9:员工身份 10:是否有照片 11:政治面貌 12:是否主业 13:籍贯
	 * 14:自定义编码 15:户籍地址 16:户口 17:婚姻状况 18:国籍 19:血型 20:现有文化程度 21:本人成分 22:入党时间
	 * 23:入党转正时间
	 */
	private static final String[] BASEINFO_COLUMN_NAMES = { "姓名", "人员编码",
			"内部编号", "部门名称", "岗位名称", "出生日期", "年龄", "性别", "民族", "员工身份", "是否有照片",
			"政治面貌", "是否主业", "籍贯", "自定义编码", "户籍地址", "户口", "婚姻状况", "国籍", "血型",
			"现有文化程度", "本人成分", "入党时间", "入党转正时间" };

	/**
	 * add by liuyi 20100611 人员工作经历导入excel模板列名 0:人员编码 1:单位名称 2:工作部门 3:岗位名称
	 * 4:开始时间 5:结束时间 6:备注
	 */
	private static final String[] WORKRESUME_COLUMN_NAMES = { "人员编码","人员姓名", "单位名称",
			"工作部门", "岗位名称", "开始时间", "结束时间", "备注" };

	/**
	 * add by liuyi 20100611 人员学习经历导入excel模板列名 0：人员编码 1：学历 2：毕业院校 3：所学专业 4：专业类别
	 * 5：学习方式 6:入学时间 7:毕业时间 8:教育状态 9:毕业证书编号 10:学位
	 */
	private static final String[] STUDYRESUME_COLUMN_NAMES = { "人员编码","人员姓名","学历",
			"毕业院校", "所学专业", "专业类别", "学习方式", "入学时间", "毕业时间", "教育状态", "毕业证书编号",
			"学位" };

	/**
	 * add by liuyi 20100611 人员家庭成员导入excel模板列名 0:人员编码 1:成员姓名 2:与本人关系 3:民族 4:文化程度
	 * 5:工作单位 6:职务 7:政治面貌 8:出生日期 9:备注
	 */
	private static final String[] FAMILYMEMBER_COLUMN_NAMES = { "人员编码", "成员姓名",
			"与本人关系", "民族", "文化程度", "工作单位", "职务", "政治面貌", "出生日期", "备注" };

	/**
	 * add by ywliu 20100611 人员家庭成员导入excel模板列名 0:人员编码 1:成员姓名 2:与本人关系 3:民族 4:文化程度
	 * 5:工作单位 6:职务 7:政治面貌 8:出生日期 9:备注
	 */
	private static final String[] WORKCONTRACT_COLUMN_NAMES = { "状态", "人员编码",
			"甲方", "签订机构", "合同编号", "签订时间", "合同期限（年）", "合同期限（月）", "用工形式", "合同类别",
			"合同解除文号", "合同生效日期", "合同终止日期" };
	/**
	 * 家庭成员基本信息
	 */
	private HrJFamilymemberBean member = null;

	/**
	 * 员工基本信息
	 */
	private HrJEmpInfoBean emp = null;

	/**
	 * 政治面貌基本信息
	 */
	private HrJPoliticsBean politics = null;

	/**
	 * 工作简历基本信息
	 */
	private HrJWorkresume workResume = null;

	/**
	 * 学历简历基本信息
	 */
	private HrJEducationBean studyResume = null;

	/**
	 * 职务任免记录
	 */
	private HrJPosition position = null;

	/**
	 * 技术职称记录
	 */
	private HrJProfessionalPost post = null;

	/**
	 * 上传员工照片
	 */
	private File photo = null;

	/**
	 * 人员基本信息管理处理远程对象
	 */
	private HrJEmpInfoFacadeRemote empRemote = null;

	/**
	 * 工作简历处理远程对象
	 */
	private HrJWorkresumeFacadeRemote workRemote = null;

	/**
	 * 学历简历处理远程对象
	 */
	private HrJEducationFacadeRemote studyRemote = null;

	/**
	 * 政治面貌远程对象
	 */
	private HrJPoliticsFacadeRemote politicsRemote = null;

	/**
	 * 家庭成员处理远程对象
	 */
	private HrJFamilyMemberFacadeRemote familyRemote = null;

	/**
	 * 职务任免处理远程对象
	 */
	private HrJPositionFacadeRemote positionRemote = null;

	/**
	 * 技术职称处理远程对象
	 */
	private HrJProfessionalPostFacadeRemote postRemote = null;

	/**
	 * add by liuyi 20100603 人字档案导出excel文件
	 */
	private File xlsFile;

	/**
	 * add by liuyi 20100603 民族处理远程接口
	 */
	private HrCNationFacadeRemote nationRemote = null;

	/**
	 * add by liuyi 20100603 学历处理远程接口
	 */
	private HrCEducationFacadeRemote educationRemote = null;

	/**
	 * add by liuyi 20100603 职称处理远程接口
	 */
	private HrCTechnologyTitlesFacadeRemote titleRemote = null;

	/**
	 * add by liuyi 20100603 职称等级处理远程接口
	 */
	private HrCTechnologyGradeFacadeRemote gradeRemote = null;
	private HrJContractstopFacadeRemote contractstop = null;
	private HrCContracttermFacadeRemote contractterm = null;
	private HrJWorkcontractFacadeRemote workcontract = null;

	/**
	 * 构造函数
	 */
	public HrEmpBaseInfoMaintAction() {
		// 取得人员基本信息管理处理远程对象
		empRemote = (HrJEmpInfoFacadeRemote) factory
				.getFacadeRemote("HrJEmpInfoFacade");
		// 取得工作简历处理远程对象
		workRemote = (HrJWorkresumeFacadeRemote) factory
				.getFacadeRemote("HrJWorkresumeFacade");
		// 取得学历简历处理远程对象
		studyRemote = (HrJEducationFacadeRemote) factory
				.getFacadeRemote("HrJEducationFacade");
		// 取得政治面貌远程对象
		politicsRemote = (HrJPoliticsFacadeRemote) factory
				.getFacadeRemote("HrJPoliticsFacade");
		// 取得家庭成员处理远程对象
		familyRemote = (HrJFamilyMemberFacadeRemote) factory
				.getFacadeRemote("HrJFamilyMemberFacade");
		// 取得职务任免处理远程对象
		positionRemote = (HrJPositionFacadeRemote) factory
				.getFacadeRemote("HrJPositionFacade");
		// 取得技术职称远程处理对象
		postRemote = (HrJProfessionalPostFacadeRemote) factory
				.getFacadeRemote("HrJProfessionalPostFacade");
		// 民族远程处理对象
		nationRemote = (HrCNationFacadeRemote) factory
				.getFacadeRemote("HrCNationFacade");
		// 学历远程处理对象
		educationRemote = (HrCEducationFacadeRemote) factory
				.getFacadeRemote("HrCEducationFacade");
		// 职称远程处理对象
		titleRemote = (HrCTechnologyTitlesFacadeRemote) factory
				.getFacadeRemote("HrCTechnologyTitlesFacade");
		// 职称等级远程处理对象
		gradeRemote = (HrCTechnologyGradeFacadeRemote) factory
				.getFacadeRemote("HrCTechnologyGradeFacade");
		// 取得
		contractstop = (HrJContractstopFacadeRemote) factory
				.getFacadeRemote("HrJContractstopFacade");// add by wpzhu
		// 20100613
		contractterm = (HrCContracttermFacadeRemote) factory
				.getFacadeRemote("HrCContracttermFacade");
		workcontract = (HrJWorkcontractFacadeRemote) factory
				.getFacadeRemote("HrJWorkcontractFacade");
	}

	/**
	 * 根据员工Id, 获得其基本维护信息
	 * 
	 * @throws Exception
	 */
	public void getEmpMaintBaseInfo() throws Exception {
		LogUtil.log("Action:根据员工Id, 获得其基本维护信息开始", Level.INFO, null);

		try {
			// 从请求中获得参数
			String strEmpId = request.getParameter("empId");
			// modified by liuyi 20100604
			List<HrJEmpInfoBean> list = empRemote.getEmpMaintBaseInfo(Long
					.parseLong(strEmpId), employee.getEnterpriseCode());
			HrJEmpInfoBean bean = null;
			if (list != null && list.size() > 0) {
				bean = list.get(0);
				List list2 = empRemote.getEmpDeptAll(bean.getDeptId()
						.toString());
				String deptName = "";
				Iterator it = list2.iterator();
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					deptName += data[4].toString();
					if (it.hasNext()) {
						deptName += "_";
					}
				}
				bean.setDeptName(deptName);
			}
			// 序列化为JSON对象的字符串形式
			// String strResult =
			// JSONUtil.serialize(empRemote.getEmpMaintBaseInfo(
			// Long.parseLong(strEmpId), employee.getEnterpriseCode()));
			String strResult = JSONUtil.serialize(bean);
			// 以html方式输出字符串
			write(strResult);
			System.out.println("the strResult" + strResult);
			LogUtil.log("Action:根据员工Id, 获得其基本维护信息正常结束", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("Action:根据员工Id, 获得其基本维护信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 根据员工Id, 获得其图片信息
	 * 
	 * @throws Exception
	 */
	public void getEmpPhotoInfo() throws Exception {
		LogUtil.log("Action:根据员工Id, 获得其图片信息开始", Level.INFO, null);

		OutputStream bos = null;
		try {
			// 从请求中获得参数
			String strEmpId = request.getParameter("empId");

			// 取得人员照片处理远程对象
			HrJEmpPhotoFacadeRemote photoRemote = (HrJEmpPhotoFacadeRemote) factory
					.getFacadeRemote("HrJEmpPhotoFacade");
			response.setContentType("image/jpeg");

			// 通过员工ID取得员工照片信息
			HrJEmpPhoto photoInfo = photoRemote.findById(Long
					.parseLong(strEmpId));
			if (photoInfo != null) {
				// 获得图片
				byte[] photoData = photoInfo.getPhoto();
				if (photoData != null) {

					bos = new BufferedOutputStream(response.getOutputStream(),
							1024 * 16);
					bos.write(photoData);
					bos.flush();

				}
			}
			LogUtil.log("Action:根据员工Id, 获得其图片信息正常结束", Level.INFO, null);
		} catch (IOException e) {
			LogUtil.log("Action:根据员工Id, 获得其图片信息异常结束", Level.SEVERE, e);
			write(Constants.IO_FAILURE);
			throw e;
		} catch (Exception e) {
			LogUtil.log("Action:根据员工Id, 获得其图片信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		} finally {
			if (bos != null) {
				bos.close();
			}
		}
	}

	public void getPhotoInfo() throws Exception {
		LogUtil.log("Action:根据员工Id, 获得其图片信息开始", Level.INFO, null);

		OutputStream bos = null;
		try {
			// 从请求中获得参数
			String strEmpId = request.getParameter("empId");

			// 取得人员照片处理远程对象
			HrJEmpPhotoFacadeRemote photoRemote = (HrJEmpPhotoFacadeRemote) factory
					.getFacadeRemote("HrJEmpPhotoFacade");
			response.setContentType("image/jpeg");

			// 通过员工ID取得员工照片信息
			HrJEmpPhoto photoInfo = photoRemote.findById(Long
					.parseLong(strEmpId));
			if (photoInfo != null) {
				// 获得图片
				byte[] photoData = photoInfo.getPhoto();
				if (photoData != null) {

					write("{success:true,msg:'成功'}");

				} else {
					write("{success:true,msg:'失败'}");
				}
			} else {
				write("{success:true,msg:'失败'}");
			}
			LogUtil.log("Action:根据员工Id, 获得其图片信息正常结束", Level.INFO, null);

		} catch (Exception e) {
			LogUtil.log("Action:根据员工Id, 获得其图片信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		} finally {
			if (bos != null) {
				bos.close();
			}
		}
	}

	/**
	 * 人事基础信息维护 保存
	 */
	public void saveHrEmpInfo() {
		String empId = request.getParameter("empId");
		String newEmpCode = request.getParameter("newEmpCode");
		String isCadres = request.getParameter("isCadres");
		String isSpecialTrades = request.getParameter("isSpecialTrades");
		String deptId = request.getParameter("deptId");

		HrJEmpInfo empInfo = empRemote.findById(Long.parseLong(empId));
		empInfo.setNewEmpCode(newEmpCode);
		empInfo.setChsName(emp.getChsName());
		// 所属部门
		empInfo.setDeptId(Long.parseLong(deptId));
		// 政治面貌
		empInfo.setPoliticsId(emp.getPoliticsId());
		// 出生日期
		empInfo.setBrithday(formatStringToDate(emp.getBrithday()));
		// 性别
//		empInfo.setSex(emp.getSex());//update by sychen 20100702
        if(emp.getSex()!=null && !emp.getSex().equals(""))
		    empInfo.setSex(emp.getSex());
		// 工作岗位
		empInfo.setStationId(emp.getStationId());
		// 参加工作时间
		empInfo.setWorkDate(formatStringToDate(emp.getWorkDate()));
		// 入厂时间
		empInfo.setMissionDate(formatStringToDate(emp.getMissionDate()));
		empInfo.setMissionDate(formatStringToDate(emp.getMissionDate()));
		empInfo.setIsSpecialTrades(isSpecialTrades);
		empInfo.setIsCadres(isCadres);

		empRemote.update(empInfo);
		write("{success:true,msg:'修改成功！'}");
	}

	/**
	 * 保存员工基本维护信息
	 * 
	 * @throws Exception
	 */
	public void saveEmpMaintBaseInfo() throws Exception {
		LogUtil.log("Action:保存员工基本维护信息开始", Level.INFO, null);

		UserTransaction tx = null;
		try {
			tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");
			// 事务开始
			tx.begin();

			// 通过员工ID取得员工信息
			HrJEmpInfo empInfo = empRemote.findById(emp.getEmpId());
			// add by liuyi 20100406 新工号
			String newEmpCode = request.getParameter("newEmpCode");
			empInfo.setNewEmpCode(newEmpCode);

			// 填充员工基本信息
			setEmpInfoData(empInfo);
			// 修改员工信息
			empInfo = empRemote.update(empInfo);

			String strFilePath = request.getParameter("filePath");
			// 如果有文件上传
			if (strFilePath != null && strFilePath.length() > 0) {
				if (photo == null || !photo.exists()) {
					throw new FileNotExistException();
				}
				// 取得人员照片处理远程对象
				HrJEmpPhotoFacadeRemote photoRemote = (HrJEmpPhotoFacadeRemote) factory
						.getFacadeRemote("HrJEmpPhotoFacade");

				// 取得照片数据
				byte[] photoData = getPhotoData();

				// 通过员工ID取得员工照片信息
				HrJEmpPhoto photoInfo = photoRemote.findById(emp.getEmpId());
				if (photoInfo == null) {
					// 增加员工照片信息
					photoInfo = new HrJEmpPhoto();

					// 人员ID
					photoInfo.setEmpId(emp.getEmpId());
					// 照片
					photoInfo.setPhoto(photoData);
					// 企业编码
					// modified by liuyi 091123 表中无该属性 已解决
					photoInfo.setEnterpriseCode(employee.getEnterpriseCode());
					// 上次修改人
					// modified by liuyi 091123 表中无该属性 已解决
					photoInfo.setLastModifiedBy(employee.getWorkerCode());
					// 上次修改日期
					// modified by liuyi 091123 表中无该属性 已解决
					photoInfo.setLastModifiedDate(new Date());
					// 是否使用 已解决
					// modified by liuyi 091123 表中无该属性
					photoInfo.setIsUse(Constants.IS_USE_Y);

					photoRemote.save(photoInfo);
				} else {
					// 照片
					photoInfo.setPhoto(photoData);
					// 上次修改人
					// modified by liuyi 091123 表中无该属性 已解决
					photoInfo.setLastModifiedBy(employee.getWorkerCode());

					photoRemote.update(photoInfo);
				}
			}

			// 取得员工岗位更新信息
			String strEmpStationOperInfo = request
					.getParameter("empStationOperInfo");
			if (strEmpStationOperInfo != null
					&& strEmpStationOperInfo.length() > 0) {
				// 职工岗位信息登记操作
				updateStationRegisterInfo(strEmpStationOperInfo);
			}

			// 提交事务
			tx.commit();
			// 以html方式输出字符串
			write("{success:true,msg:true,modifiedDate:\""
					+ formatDate(empInfo.getLastModifiyDate(),
							DATE_FORMAT_YYYYMMDD_TIME_SEC) + "\"}");
			LogUtil.log("Action:保存员工基本维护信息正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:保存员工基本维护信息异常结束", Level.SEVERE, e);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			write(Constants.DATA_USING);
			throw (e);
		} catch (IOException e) {
			LogUtil.log("Action:保存员工基本维护信息异常结束", Level.SEVERE, e);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			write(Constants.IO_FAILURE);
			throw (e);
		} catch (SQLException e) {
			LogUtil.log("Action:保存员工基本维护信息异常结束", Level.SEVERE, e);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			write(Constants.SQL_FAILURE);
			throw e;
		} catch (FileNotExistException e) {
			LogUtil.log("Action:保存员工基本维护信息异常结束", Level.SEVERE, e);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			write(Constants.FILE_NOT_EXIST);
			throw e;
		}
	}

	/**
	 * 职工岗位信息登记操作
	 * 
	 * @param argEmpStationOperInfo
	 *            职工更新岗位信息
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void updateStationRegisterInfo(String argEmpStationOperInfo)
			throws Exception {
		LogUtil.log("Action:职工岗位信息登记操作开始", Level.INFO, null);

		try {
			Object object = JSONUtil.deserialize(argEmpStationOperInfo);
			if (List.class.isAssignableFrom(object.getClass())) {
				// 如果是数组
				List lst = (List) object;
				int intLen = lst.size();
				// 取得职工岗位信息登记处理远程对象
				HrJEmpStationFacadeRemote empStationRemote = (HrJEmpStationFacadeRemote) factory
						.getFacadeRemote("HrJEmpStationFacade");

				// 初始化流水号
				Long lngStation = null;
				HrJEmpStation empStation = null;
				for (int intCnt = 0; intCnt < intLen; intCnt++) {
					// 从请求中获得参数
					// 岗位ID
					String strStationId = (String) ((Map) lst.get(intCnt))
							.get("stationId");
					// 人员ID
					// add by liuyi 091231
					// String strEmpId = (String) ((Map)
					// lst.get(intCnt)).get("empId");
					String strEmpId = ((Map) lst.get(intCnt)).get("empId")
							.toString();
					// 职工岗位上次修改时间
					String strLastModifiedDate = (String) ((Map) lst
							.get(intCnt)).get("lastModifiedDate");
					// 职工岗位ID
					String strEmpStationId = (String) ((Map) lst.get(intCnt))
							.get("empStationId");
					String strOperator = (String) ((Map) lst.get(intCnt))
							.get("operateFlag");

					if ("D".equals(strOperator)) {
						// 删除职工岗位信息

						// 通过员工ID取得员工岗位信息
						empStation = empStationRemote.findById(Long
								.parseLong(strEmpStationId));

						// 是否使用
						empStation.setIsUse(Constants.IS_USE_N);
						// 上次修改日期
						empStation
								.setLastModifiedDate(formatStringToDate(strLastModifiedDate));

						// 修改职工岗位
						empStationRemote.update(empStation);
						continue;
					}

					// 是否主岗位
					String strIsMainStation = (String) ((Map) lst.get(intCnt))
							.get("isMainStation");
					// 备注
					String strMemo = (String) ((Map) lst.get(intCnt))
							.get("memo");

					if ("I".equals(strOperator)) {
						// 新增职工岗位信息
						empStation = new HrJEmpStation();
					} else if ("U".equals(strOperator)) {
						// 修改职工岗位信息

						// 通过员工ID取得员工岗位信息
						empStation = empStationRemote.findById(Long
								.parseLong(strEmpStationId));
					}

					// 人员ID
					empStation.setEmpId(Long.parseLong(strEmpId));
					// 岗位ID
					if (strStationId != null && strStationId.length() > 0) {
						empStation.setStationId(Long.parseLong(strStationId));
					} else {
						empStation.setStationId(null);
					}
					// 是否主岗位
					empStation.setIsMainStation(strIsMainStation);
					// 备注
					empStation.setMemo(strMemo);
					// 上次修改人
					empStation.setLastModifiedBy(employee.getWorkerCode());

					if ("I".equals(strOperator)) {
						// 新增职工岗位信息

						// 职工岗位ID
						empStation.setEmpStationId(null);
						// 企业编码
						empStation.setEnterpriseCode(employee
								.getEnterpriseCode());
						// 上次修改日期
						empStation.setLastModifiedDate(new Date());
						// 是否使用
						empStation.setIsUse(Constants.IS_USE_Y);

						lngStation = empStationRemote.save(empStation,
								lngStation);
					} else if ("U".equals(strOperator)) {
						// 修改职工岗位信息

						// 上次修改日期
						empStation
								.setLastModifiedDate(formatStringToDate(strLastModifiedDate));

						// 修改职工岗位
						empStationRemote.update(empStation);
					}
				}
			}
			LogUtil.log("Action:职工岗位信息登记操作结束", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("Action:职工岗位信息登记操作异常结束", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * 根据员工Id, 获得工作简历信息
	 * 
	 * @throws Exception
	 */
	public void getEmpWorkresumeInfo() throws Exception {
		LogUtil.log("Action:根据员工Id, 获得工作简历信息开始", Level.INFO, null);

		try {
			// 从请求中获得参数
			String strEmpId = request.getParameter("empId");
			if (strEmpId == null || strEmpId.trim().length() < 1) {
				write("{list:[],totalCount:0}");
				LogUtil.log("Action:根据员工Id, 获得工作简历信息正常结束", Level.INFO, null);
				return;
			}

			// 开始行
			int intStart = Integer.parseInt(request.getParameter("start"));
			// 行数
			int intLimit = Integer.parseInt(request.getParameter("limit"));

			// 查询结果
			PageObject result = workRemote.getWorksumeInfo(Long
					.parseLong(strEmpId), employee.getEnterpriseCode(),
					intStart, intLimit);
			// 序列化为JSON对象的字符串形式
			String strResult = JSONUtil.serialize(result);
			// 以html方式输出字符串
			write(strResult);
			LogUtil.log("Action:根据员工Id, 获得工作简历信息正常结束", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("Action:根据员工Id, 获得工作简历信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 保存工作简历信息
	 * 
	 * @throws Exception
	 */
	public void saveEmpWorkresumeInfo() throws Exception {
		LogUtil.log("Action:保存工作简历信息开始", Level.INFO, null);
		try {
			// 从请求中获得参数
			String strAddFlag = request.getParameter("isAdd");
			Date now = new Date();
			if (Boolean.parseBoolean(strAddFlag)) {
				// 工作简历ID
				workResume.setWorkresumeid(null);

				// 企业代码
				workResume.setEnterpriseCode(employee.getEnterpriseCode());
				// 是否使用
				workResume.setIsUse(Constants.IS_USE_Y);
				// 记录人
				workResume.setInsertby(employee.getWorkerCode());
				// 记录时间
				workResume.setInsertdate(now);
				// 修改人
				workResume.setLastModifiedBy(employee.getWorkerCode());
				// 最后修改时间
				workResume.setLastModifiedDate(now);

				workRemote.save(workResume);
				// 以html方式输出字符串
				write(Constants.MODIFY_SUCCESS);
				LogUtil.log("Action:保存学习简历信息正常结束", Level.INFO, null);
				return;
			}

			// 通过ID查找工作简历记录
			HrJWorkresume workInfo = workRemote.findById(workResume
					.getWorkresumeid());

			// 开始日期
			workInfo.setStartDate(workResume.getStartDate());
			// 结束日期
			workInfo.setEndDate(workResume.getEndDate());
			// 单位
			workInfo.setUnit(workResume.getUnit());
			// 岗位名称
			workInfo.setStationName(workResume.getStationName());
			// 职务名称
			workInfo.setHeadshipName(workResume.getHeadshipName());
			// 证明人
			workInfo.setWitness(workResume.getWitness());
			// 备注
			workInfo.setMemo(workResume.getMemo());
			// add by liuyi 20100609 工作部门
			workInfo.setDeptName(workResume.getDeptName());
			// 修改人
			workInfo.setLastModifiedBy(employee.getWorkerCode());
			// 最后修改时间 已解决
			// workInfo.setLastModifiedDate(workResume.getLastModifiedDate());
			workRemote.update(workInfo);
			// 以html方式输出字符串
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:保存工作简历信息正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:保存工作简历信息异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
			throw (e);
		} catch (SQLException e) {
			LogUtil.log("Action:保存工作简历信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 删除工作简历信息
	 * 
	 * @throws Exception
	 */
	public void deleteEmpWorkresumeInfo() throws Exception {
		LogUtil.log("Action:删除工作简历信息开始", Level.INFO, null);
		try {
			// 通过ID查找工作简历记录
			HrJWorkresume workInfo = workRemote.findById(workResume
					.getWorkresumeid());

			// 是否使用
			workInfo.setIsUse(Constants.IS_USE_N);
			// 修改人
			workInfo.setLastModifiedBy(employee.getWorkerCode());
			// 最后修改时间
			workInfo.setLastModifiedDate(workResume.getLastModifiedDate());
			workRemote.update(workInfo);
			// 以html方式输出字符串
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除工作简历信息正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:删除工作简历信息异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
			throw (e);
		} catch (SQLException e) {
			LogUtil.log("Action:删除工作简历信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * add by liuyi 20100609 删除多个工作简历 学习简历
	 */
	public void deleteMutilEmpWorkresumeInfo() {
		String ids = request.getParameter("ids");
		String type = request.getParameter("type");
		if ("workResume".equals(type))
			workRemote.deleteMutilEmpWorkresumeInfo(ids);
		else if ("studyResume".equals(type))
			studyRemote.delete(ids);
		else if ("familyMember".equals(type))
			familyRemote.delete(ids);
		// 以html方式输出字符串
		write(Constants.DELETE_SUCCESS);

	}

	/**
	 * 根据员工Id, 获得学习简历信息
	 * 
	 * @throws Exception
	 */
	public void getEmpStudyResumeInfo() throws Exception {
		LogUtil.log("Action:根据员工Id, 获得学习简历信息开始", Level.INFO, null);

		try {
			// 从请求中获得参数
			String strEmpId = request.getParameter("empId");
			if (strEmpId == null || strEmpId.trim().length() < 1) {
				write("{list:[],totalCount:0}");
				LogUtil.log("Action:根据员工Id, 获得学习简历信息正常结束", Level.INFO, null);
				return;
			}

			// 开始行
			int intStart = Integer.parseInt(request.getParameter("start"));
			// 行数
			int intLimit = Integer.parseInt(request.getParameter("limit"));

			// 查询结果
			PageObject result = studyRemote.getEducationInfo(Long
					.parseLong(strEmpId), employee.getEnterpriseCode(),
					intStart, intLimit);
			// 序列化为JSON对象的字符串形式
			String strResult = JSONUtil.serialize(result);
			// 以html方式输出字符串
			write(strResult);
			LogUtil.log("Action:根据员工Id, 获得学习简历信息正常结束", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("Action:根据员工Id, 获得学习简历信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 保存学习简历信息
	 * 
	 * @throws Exception
	 */
	public void saveEmpStudyResumeInfo() throws Exception {
		LogUtil.log("Action:保存学习简历信息开始", Level.INFO, null);
		try {
			// 从请求中获得参数
			String strAddFlag = request.getParameter("isAdd");
			Date now = new Date();
			HrJEducation studyInfo = null;
			if (Boolean.parseBoolean(strAddFlag)) {
				studyInfo = new HrJEducation();

				// 学历教育ID
				studyInfo.setEducationid(null);

				// 企业代码
				studyInfo.setEnterpriseCode(employee.getEnterpriseCode());
				// 是否使用
				studyInfo.setIsUse(Constants.IS_USE_Y);
				// 记录人
				studyInfo.setInsertby(employee.getWorkerCode());
				// 记录时间
				studyInfo.setInsertdate(now);
				// 最后修改时间
				studyInfo.setLastModifiedDate(now);
			} else {
				// 通过ID查找学习简历记录
				studyInfo = studyRemote.findById(studyResume.getEducationid());
			}

			// 员工ID
			studyInfo.setEmpId(emp.getEmpId());
			// 修改人
			studyInfo.setLastModifiedBy(employee.getWorkerCode());

			// 学校编码ID
//			studyInfo.setSchoolCodeId(studyResume.getSchoolCodeId());
			studyInfo.setGraduateSchool(studyResume.getGraduateSchool());//add by sychen 20100709
			// 入学日期
			studyInfo.setEnrollmentDate(formatStringToDate(studyResume
					.getEnrollmentDate()));
			// 学习专业
//			studyInfo.setSpecialtyCodeId(studyResume.getSpecialtyCodeId());
			studyInfo.setSpecialty(studyResume.getSpecialty());//add by sychen 20100709
			// 毕业日期
			studyInfo.setGraduateDate(formatStringToDate(studyResume
					.getGraduateDate()));
			// 学历
			studyInfo.setEducationId(studyResume.getEducationId());

			// 学位
			studyInfo.setDegreeId(studyResume.getDegreeId());
			// 语种
			studyInfo.setLanguageCodeId(studyResume.getLanguageCodeId());
			// 学习类别
			studyInfo.setStudyTypeCodeId(studyResume.getStudyTypeCodeId());
			// 学制（年）
			studyInfo.setStudyLimit(studyResume.getStudyLimit());
			// 学号
			studyInfo.setStudyCode(studyResume.getStudyCode());

			// 教育结果
			studyInfo.setEducationResult(studyResume.getEducationResult());
			// 培训费用
			studyInfo.setStudyMoney(studyResume.getStudyMoney());
			// 证书号码
			studyInfo.setCertificateCode(studyResume.getCertificateCode());
			// 是否毕业
			studyInfo.setIfGraduate(studyResume.getIfGraduate());
			// 原始学历
			studyInfo.setIfOriginality(studyResume.getIfOriginality());

			// 最高学历
			studyInfo.setIfHightestXl(studyResume.getIfHightestXl());
			// 备注
			studyInfo.setMemo(studyResume.getMemo());
			// 专业类别
			studyInfo.setCategory(studyResume.getCategory());

			if (Boolean.parseBoolean(strAddFlag)) {
				studyRemote.save(studyInfo);
			} else {
				// 最后修改时间
				studyInfo.setLastModifiedDate(formatStringToDate(studyResume
						.getLastModifiedDate()));
				studyRemote.update(studyInfo);
			}
			// 以html方式输出字符串
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:保存学习简历信息正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:保存学习简历信息异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
			throw (e);
		} catch (SQLException e) {
			LogUtil.log("Action:保存学习简历信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 删除学习简历信息
	 * 
	 * @throws Exception
	 */
	public void deleteEmpStudyResumeInfo() throws Exception {
		LogUtil.log("Action:删除学习简历信息开始", Level.INFO, null);
		try {
			// 通过ID查找学习简历记录
			HrJEducation studyInfo = studyRemote.findById(studyResume
					.getEducationid());

			// 是否使用
			studyInfo.setIsUse(Constants.IS_USE_N);
			// 修改人
			studyInfo.setLastModifiedBy(employee.getWorkerCode());
			// 最后修改时间
			studyInfo.setLastModifiedDate(formatStringToDate(studyResume
					.getLastModifiedDate()));
			studyRemote.update(studyInfo);
			// 以html方式输出字符串
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除学习简历信息正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:删除学习简历信息异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
			throw (e);
		} catch (SQLException e) {
			LogUtil.log("Action:删除学习简历信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 根据员工Id, 获得政治面貌信息
	 * 
	 * @throws Exception
	 */
	public void getEmpPoliticsInfo() throws Exception {
		LogUtil.log("Action:根据员工Id, 获得政治面貌信息开始", Level.INFO, null);

		try {
			// 从请求中获得参数
			String strEmpId = request.getParameter("empId");
			if (strEmpId == null || strEmpId.trim().length() < 1) {
				write("{list:[],totalCount:0}");
				LogUtil.log("Action:根据员工Id, 获得政治面貌信息正常结束", Level.INFO, null);
				return;
			}

			// 开始行
			int intStart = Integer.parseInt(request.getParameter("start"));
			// 行数
			int intLimit = Integer.parseInt(request.getParameter("limit"));

			// 查询结果
			PageObject result = politicsRemote.getPoliticsInfo(Long
					.parseLong(strEmpId), employee.getEnterpriseCode(),
					intStart, intLimit);
			// 序列化为JSON对象的字符串形式
			String strResult = JSONUtil.serialize(result);
			// 以html方式输出字符串
			write(strResult);
			LogUtil.log("Action:根据员工Id, 获得政治面貌信息正常结束", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("Action:根据员工Id, 获得政治面貌信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 保存政治面貌信息
	 * 
	 * @throws Exception
	 */
	public void saveEmpPoliticsInfo() throws Exception {
		LogUtil.log("Action:保存政治面貌信息开始", Level.INFO, null);
		try {
			// 从请求中获得参数
			String strAddFlag = request.getParameter("isAdd");
			Date now = new Date();
			HrJPolitics politicsInfo = null;
			if (Boolean.parseBoolean(strAddFlag)) {
				politicsInfo = new HrJPolitics();

				// 政治面貌登记ID
				politicsInfo.setPoliticsid(null);

				// 企业代码
				politicsInfo.setEnterpriseCode(employee.getEnterpriseCode());
				// 是否使用
				politicsInfo.setIsUse(Constants.IS_USE_Y);
				// 记录人
				politicsInfo.setInsertby(employee.getWorkerCode());
				// 记录时间
				politicsInfo.setInsertdate(now);
				// 最后修改时间
				politicsInfo.setLastModifiedDate(now);
			} else {
				// 通过ID查找政治面貌记录
				politicsInfo = politicsRemote
						.findById(politics.getPoliticsid());
			}

			// 员工ID
			politicsInfo.setEmpId(emp.getEmpId());
			// 修改人
			politicsInfo.setLastModifiedBy(employee.getWorkerCode());

			// 政治面貌
			politicsInfo.setPoliticsId(politics.getPoliticsId());
			// 参加日期
			politicsInfo
					.setJoinDate(formatStringToDate(politics.getJoinDate()));
			// 所属组织
			politicsInfo.setBelongUnit(politics.getBelongUnit());
			// 参加单位
			politicsInfo.setJoinUnit(politics.getJoinUnit());
			// 参加地点
			politicsInfo.setJoinPlace(politics.getJoinPlace());

			// 介绍人
			politicsInfo.setIntroducer(politics.getIntroducer());
			// 介绍人单位
			politicsInfo.setIntroducerUnit(politics.getIntroducerUnit());
			// 最新标志
			politicsInfo.setIfNewMark(politics.getIsNewMark());
			// 退出日期
			politicsInfo
					.setExitDate(formatStringToDate(politics.getExitDate()));
			// 备注
			politicsInfo.setMemo(politics.getMemo());

			if (Boolean.parseBoolean(strAddFlag)) {
				politicsRemote.save(politicsInfo);
			} else {
				// 最后修改时间
				politicsInfo.setLastModifiedDate(formatStringToDate(politics
						.getLastModifiedDate()));
				politicsRemote.update(politicsInfo);
			}
			// 以html方式输出字符串
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:保存政治面貌信息正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:保存政治面貌信息异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
			throw (e);
		} catch (SQLException e) {
			LogUtil.log("Action:保存政治面貌信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 删除政治面貌信息
	 * 
	 * @throws Exception
	 */
	public void deleteEmpPoliticsInfo() throws Exception {
		LogUtil.log("Action:删除政治面貌信息开始", Level.INFO, null);
		try {
			// 通过ID查找政治面貌记录
			HrJPolitics politicsInfo = politicsRemote.findById(politics
					.getPoliticsid());

			// 是否使用
			politicsInfo.setIsUse(Constants.IS_USE_N);
			// 修改人
			politicsInfo.setLastModifiedBy(employee.getWorkerCode());
			// 最后修改时间
			politicsInfo.setLastModifiedDate(formatStringToDate(politics
					.getLastModifiedDate()));
			politicsRemote.update(politicsInfo);
			// 以html方式输出字符串
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除政治面貌信息正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:删除政治面貌信息异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
			throw (e);
		} catch (SQLException e) {
			LogUtil.log("Action:删除政治面貌信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 根据员工Id, 获得家庭成员信息
	 * 
	 * @throws Exception
	 */
	public void getEmpFamilyInfo() throws Exception {
		LogUtil.log("Action:根据员工Id, 获得家庭成员信息开始", Level.INFO, null);

		try {
			// 从请求中获得参数
			String strEmpId = request.getParameter("empId");
			if (strEmpId == null || strEmpId.trim().length() < 1) {
				write("{list:[],totalCount:0}");
				LogUtil.log("Action:根据员工Id, 获得家庭成员信息正常结束", Level.INFO, null);
				return;
			}

			// 开始行
			int intStart = Integer.parseInt(request.getParameter("start"));
			// 行数
			int intLimit = Integer.parseInt(request.getParameter("limit"));

			// 查询结果
			PageObject result = familyRemote.getFamilyMemberInfo(Long
					.parseLong(strEmpId), employee.getEnterpriseCode(),
					intStart, intLimit);
			// 序列化为JSON对象的字符串形式
			String strResult = JSONUtil.serialize(result);
			// 以html方式输出字符串
			write(strResult);
			LogUtil.log("Action:根据员工Id, 获得家庭成员信息正常结束", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("Action:根据员工Id, 获得家庭成员信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 保存家庭成员信息
	 * 
	 * @throws Exception
	 */
	public void saveEmpFamilyInfo() throws Exception {
		LogUtil.log("Action:保存家庭成员信息开始", Level.INFO, null);
		try {
			// 从请求中获得参数
			String strAddFlag = request.getParameter("isAdd");
			Date now = new Date();
			HrJFamilymember familyInfo = null;
			if (Boolean.parseBoolean(strAddFlag)) {
				familyInfo = new HrJFamilymember();

				// 家庭成员ID
				familyInfo.setFamilymemberid(null);

				// 企业代码
				familyInfo.setEnterpriseCode(employee.getEnterpriseCode());
				// 是否使用
				familyInfo.setIsUse(Constants.IS_USE_Y);
				// 记录人
				familyInfo.setInsertby(employee.getWorkerCode());
				// 记录时间
				familyInfo.setInsertdate(now);
				// 最后修改时间
				familyInfo.setLastModifiedDate(now);
			} else {
				// 通过ID查找家庭成员记录
				familyInfo = familyRemote.findById(member.getFamilymemberid());
			}

			// 员工ID
			familyInfo.setEmpId(emp.getEmpId());
			// 修改人
			familyInfo.setLastModifiedBy(employee.getWorkerCode());

			// 姓名
			familyInfo.setName(member.getName());
			// 称谓
			familyInfo.setCallsCodeId(member.getCallsCodeId());
			// 出生日期
			familyInfo.setBirthday(formatStringToDate(member.getBirthday()));
			// 性别
			familyInfo.setSex(member.getSex());
			// 籍贯
			familyInfo.setNativePlaceId(member.getNativePlaceId());

			// 民族
			familyInfo.setNationCodeId(member.getNationCodeId());
			// 学历
			familyInfo.setEducationId(member.getEducationId());
			// 政治面貌
			familyInfo.setPoliticsId(member.getPoliticsId());
			// 单位
			familyInfo.setUnit(member.getUnit());
			// 职务名称
			familyInfo.setHeadshipName(member.getHeadshipName());

			// 婚否
			familyInfo.setIfMarried(member.getIfMarried());
			// 是否直系
			familyInfo.setZxqsMark(member.getZxqsMark());
			// 是否供养
			familyInfo.setIfLineally(member.getIfLineally());
			// 备注
			familyInfo.setMemo(member.getMemo());

			if (Boolean.parseBoolean(strAddFlag)) {
				familyRemote.save(familyInfo);
			} else {
				// 最后修改时间
				familyInfo.setLastModifiedDate(formatStringToDate(member
						.getLastModifiedDate()));
				familyRemote.update(familyInfo);
			}
			// 以html方式输出字符串
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:保存家庭成员信息正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:保存家庭成员信息异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
			throw (e);
		} catch (SQLException e) {
			LogUtil.log("Action:保存家庭成员信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 删除家庭成员信息
	 * 
	 * @throws Exception
	 */
	public void deleteEmpFamilyInfo() throws Exception {
		LogUtil.log("Action:删除家庭成员信息开始", Level.INFO, null);
		try {
			// 通过ID查找家庭成员记录
			HrJFamilymember familyInfo = familyRemote.findById(member
					.getFamilymemberid());

			// 是否使用
			familyInfo.setIsUse(Constants.IS_USE_N);
			// 修改人
			familyInfo.setLastModifiedBy(employee.getWorkerCode());
			// 最后修改时间
			familyInfo.setLastModifiedDate(formatStringToDate(member
					.getLastModifiedDate()));
			familyRemote.update(familyInfo);
			// 以html方式输出字符串
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除家庭成员信息正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:删除家庭成员信息异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
			throw (e);
		} catch (SQLException e) {
			LogUtil.log("Action:删除家庭成员信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 根据员工Id, 获得职务任免信息
	 * 
	 * @throws Exception
	 */
	public void getPositionInfo() throws Exception {
		LogUtil.log("Action:根据员工Id, 获得职务任免信息开始", Level.INFO, null);

		try {
			// 从请求中获得参数
			String strEmpId = request.getParameter("empId");
			if (strEmpId == null || strEmpId.trim().length() < 1) {
				write("{list:[],totalCount:0}");
				LogUtil.log("Action:根据员工Id, 获得职务任免信息正常结束", Level.INFO, null);
				return;
			}

			// 开始行
			int intStart = Integer.parseInt(request.getParameter("start"));
			// 行数
			int intLimit = Integer.parseInt(request.getParameter("limit"));

			// 查询结果
			PageObject result = positionRemote.findEmpAllPosition(Long
					.parseLong(strEmpId), employee.getEnterpriseCode(),
					intStart, intLimit);
			// 序列化为JSON对象的字符串形式
			String strResult = JSONUtil.serialize(result);
			// 以html方式输出字符串
			write(strResult);
			LogUtil.log("Action:根据员工Id, 获得职务任免信息正常结束", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("Action:根据员工Id, 获得职务任免信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 保存职务任免
	 * 
	 * @throws Exception
	 */
	public void savePositionInfo() throws Exception {
		LogUtil.log("Action:保存职务任免信息开始", Level.INFO, null);
		try {
			// 从请求中获得参数
			String strAddFlag = request.getParameter("isAdd");
			Date now = new Date();
			HrJPosition temp = null;
			if (Boolean.parseBoolean(strAddFlag)) {
				temp = new HrJPosition();

				// 职务任免ID
				temp.setPositionId(null);
				// 企业代码
				temp.setEnterpriseCode(employee.getEnterpriseCode());
				// 是否使用
				temp.setIsUse(Constants.IS_USE_Y);
				// 记录人
				temp.setLastModifiedBy(employee.getWorkerCode());
				// 最后修改时间
				temp.setLastModifiedDate(now);
			} else {
				// 通过ID查找家庭成员记录
				temp = positionRemote.findById(position.getPositionId());
			}

			// 员工ID
			temp.setEmpId(position.getEmpId());
			// 任免时间
			temp.setRmDate(position.getRmDate());
			// 职务名称
			temp.setPositionName(position.getPositionName());
			// 是否任职
			temp.setIsPosition(position.getIsPosition());
			// 任职文号
			temp.setPositionCode(position.getPositionCode());
			// 当前职务
			temp.setIsNow(position.getIsNow());
			// 职务等级
			temp.setPositionLevel(position.getPositionLevel());
			// 任免批准单位
			temp.setApproveDept(position.getApproveDept());
			// 任免职方式
			temp.setRmMode(position.getRmMode());
			// 任免原因
			temp.setRmReason(position.getRmReason());
			// 任免意见
			temp.setRmView(position.getRmView());
			// 备注
			temp.setMemo(position.getMemo());

			if (Boolean.parseBoolean(strAddFlag)) {
				positionRemote.save(temp);
			} else {
				positionRemote.update(temp);
			}
			// 以html方式输出字符串
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:保存职务任免信息正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:保存职务任免信息异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
			throw (e);
		} catch (SQLException e) {
			LogUtil.log("Action:保存职务任免信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 删除职务任免信息
	 * 
	 * @throws Exception
	 */
	public void deletePositionInfo() throws Exception {
		LogUtil.log("Action:删除职务任免信息开始", Level.INFO, null);
		try {
			// 通过ID查找职务任免记录
			HrJPosition info = positionRemote
					.findById(position.getPositionId());

			// 是否使用
			info.setIsUse(Constants.IS_USE_N);
			// 修改人
			info.setLastModifiedBy(employee.getWorkerCode());
			// // 最后修改时间
			// info.setLastModifiedDate(new Date());
			positionRemote.update(info);
			// 以html方式输出字符串
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除职务任免信息正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:删除职务任免信息异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
			throw (e);
		} catch (SQLException e) {
			LogUtil.log("Action:删除家庭成员信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 根据员工Id, 获得技术职称信息
	 * 
	 * @throws Exception
	 */
	public void getPostInfo() throws Exception {
		LogUtil.log("Action:根据员工Id, 获得技术职称信息开始", Level.INFO, null);

		try {
			// 从请求中获得参数
			String strEmpId = request.getParameter("empId");
			if (strEmpId == null || strEmpId.trim().length() < 1) {
				write("{list:[],totalCount:0}");
				LogUtil.log("Action:根据员工Id, 获得技术职称信息正常结束", Level.INFO, null);
				return;
			}

			// 开始行
			int intStart = Integer.parseInt(request.getParameter("start"));
			// 行数
			int intLimit = Integer.parseInt(request.getParameter("limit"));

			// 查询结果
			PageObject result = postRemote.findEmpProfessionalPost(Long
					.parseLong(strEmpId), employee.getEnterpriseCode(),
					intStart, intLimit);
			// 序列化为JSON对象的字符串形式
			String strResult = JSONUtil.serialize(result);
			// 以html方式输出字符串
			write(strResult);
			LogUtil.log("Action:根据员工Id, 获得技术职称信息正常结束", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("Action:根据员工Id, 获得技术职称信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 保存技术职称信息
	 * 
	 * @throws Exception
	 */
	public void savePostInfo() throws Exception {
		LogUtil.log("Action:保存技术职称信息开始", Level.INFO, null);
		try {
			// 从请求中获得参数
			String strAddFlag = request.getParameter("isAdd");
			Date now = new Date();
			HrJProfessionalPost temp = null;
			if (Boolean.parseBoolean(strAddFlag)) {
				temp = new HrJProfessionalPost();

				// 技术职称id
				temp.setId(null);
				// 企业代码
				temp.setEnterpriseCode(employee.getEnterpriseCode());
				// 是否使用
				temp.setIsUse(Constants.IS_USE_Y);
				// 记录人
				temp.setLastModifiedBy(employee.getWorkerCode());
				// 最后修改时间
				temp.setLastModifiedDate(now);
			} else {
				// 通过ID查找技术职称记录
				temp = postRemote.findById(post.getId());
			}

			// 员工ID
			temp.setEmpId(post.getEmpId());
			// 聘任日期
			temp.setRecruitmentDate(post.getRecruitmentDate());
			// 技术职称
			temp.setProfessionalPost(post.getProfessionalPost());
			// 技术等级
			temp.setProfessionalLevel(post.getProfessionalLevel());
			// 是否当前
			temp.setIsNow(post.getIsNow());
			// 评定方式
			temp.setJudgeMode(post.getJudgeMode());
			// 有效开始日期
			temp.setValidStartDate(post.getValidStartDate());
			// 有效终止日期
			temp.setValidEndDate(post.getValidEndDate());
			// 证书名称
			temp.setCertificateName(post.getCertificateName());
			// 证书编号
			temp.setCertificateCode(post.getCertificateCode());
			// 发证部门
			temp.setCertificateDept(post.getCertificateName());
			// 批准文号
			temp.setApproveCode(post.getApproveCode());
			// 评定所在单位
			temp.setJudgeApproveDept(post.getJudgeApproveDept());
			// 备注
			temp.setMemo(post.getMemo());

			if (Boolean.parseBoolean(strAddFlag)) {
				postRemote.save(temp);
			} else {
				postRemote.update(temp);
			}
			// 以html方式输出字符串
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:保存技术职称信息正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:保存技术职称信息异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
			throw (e);
		} catch (SQLException e) {
			LogUtil.log("Action:保存技术职称信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 删除技术职称信息
	 * 
	 * @throws Exception
	 */
	public void deletePostInfo() throws Exception {
		LogUtil.log("Action:删除技术职称信息开始", Level.INFO, null);
		try {
			// 通过ID查找职务任免记录
			HrJProfessionalPost info = postRemote.findById(post.getId());

			// 是否使用
			info.setIsUse(Constants.IS_USE_N);
			// 修改人
			info.setLastModifiedBy(employee.getWorkerCode());
			// // 最后修改时间
			// info.setLastModifiedDate(new Date());
			postRemote.update(info);
			// 以html方式输出字符串
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除技术职称信息正常结束", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:删除技术职称信息异常结束", Level.SEVERE, e);
			write(Constants.DATA_USING);
			throw (e);
		} catch (SQLException e) {
			LogUtil.log("Action:删除家庭成员信息异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}

	/**
	 * 取得家庭成员基本信息
	 * 
	 * @return 家庭成员基本信息
	 */
	public HrJFamilymemberBean getMember() {
		return member;
	}

	/**
	 * 设置家庭成员基本信息
	 * 
	 * @param argMember
	 *            家庭成员基本信息
	 */
	public void setMember(HrJFamilymemberBean argMember) {
		member = argMember;
	}

	/**
	 * 取得员工基本信息
	 * 
	 * @return 员工基本信息
	 */
	public HrJEmpInfoBean getEmp() {
		return emp;
	}

	/**
	 * 设置员工基本信息
	 * 
	 * @param argEmp
	 *            员工基本信息
	 */
	public void setEmp(HrJEmpInfoBean argEmp) {
		emp = argEmp;
	}

	/**
	 * 取得政治面貌基本信息
	 * 
	 * @return 政治面貌基本信息
	 */
	public HrJPoliticsBean getPolitics() {
		return politics;
	}

	/**
	 * 设置政治面貌基本信息
	 * 
	 * @param argPoliticses
	 *            政治面貌基本信息
	 */
	public void setPolitics(HrJPoliticsBean argPolitics) {
		politics = argPolitics;
	}

	/**
	 * 取得工作简历基本信息
	 * 
	 * @return 工作简历基本信息
	 */
	public HrJWorkresume getWorkResume() {
		return workResume;
	}

	/**
	 * 设置工作简历基本信息
	 * 
	 * @param argWorkResume
	 *            工作简历基本信息
	 */
	public void setWorkResume(HrJWorkresume argWorkResume) {
		workResume = argWorkResume;
	}

	/**
	 * 取得学历简历基本信息
	 * 
	 * @return 学历简历基本信息
	 */
	public HrJEducationBean getStudyResume() {
		return studyResume;
	}

	/**
	 * 设置学历简历基本信息
	 * 
	 * @param argStudyResume
	 *            学历简历基本信息
	 */
	public void setStudyResume(HrJEducationBean argStudyResume) {
		studyResume = argStudyResume;
	}

	/**
	 * 设置上传员工照片
	 * 
	 * @param argPhoto
	 *            上传员工照片
	 */
	public void setPhoto(File argPhoto) {
		photo = argPhoto;
	}

	/**
	 * 取得上传员工照片
	 * 
	 * @return 上传员工照片
	 */
	public File getPhoto() {
		return photo;
	}

	/**
	 * 填充员工基本信息
	 * 
	 * @param argEmpInfo
	 *            员工基本信息
	 */
	private void setEmpInfoData(HrJEmpInfo argEmpInfo) {
		// 员工状态: 是否存档
		argEmpInfo.setEmpState(emp.getEmpState());
		// 英文名
		argEmpInfo.setEnName(emp.getEnName());
		// add by liuyi 091221 所属部门
		argEmpInfo.setDeptId(emp.getDeptId());
		// 人员工号
		// argEmpInfo.setEmpCode(emp.getEmpCode());
		// 籍贯
		argEmpInfo.setNativePlaceId(emp.getNativePlaceId());
		// 政治面貌
		argEmpInfo.setPoliticsId(emp.getPoliticsId());
		// 出生日期
		argEmpInfo.setBrithday(formatStringToDate(emp.getBrithday()));
		// 性别
		argEmpInfo.setSex(emp.getSex());
		// 婚否
		argEmpInfo.setIsWedded(emp.getIsWedded());
		// 档案编号
		argEmpInfo.setArchivesId(emp.getArchivesId());
		// 工作岗位
		argEmpInfo.setStationId(emp.getStationId());

		// 员工类别
		argEmpInfo.setEmpTypeId(emp.getEmpTypeId());
		// 参加工作时间
		argEmpInfo.setWorkDate(formatStringToDate(emp.getWorkDate()));
		// 入厂时间
		argEmpInfo.setMissionDate(formatStringToDate(emp.getMissionDate()));
		// 技术职称
		argEmpInfo.setTechnologyTitlesTypeId(emp.getTechnologyTitlesId());
		// 技术等级
		argEmpInfo.setTechnologyGradeId(emp.getTechnologyGradeId());
		// 所属工种
		argEmpInfo.setTypeOfWorkId(emp.getTypeOfWorkId());
		// 身份证号
		argEmpInfo.setIdentityCard(emp.getIdentityCard());
		// 考勤卡号
		argEmpInfo.setTimeCardId(emp.getTimeCardId());
		// 工资卡号
		argEmpInfo.setPayCardId(emp.getPayCardId());
		// 社保卡号
		argEmpInfo.setSocialInsuranceId(emp.getSocialInsuranceId());

		// 参保时间
		argEmpInfo.setSocialInsuranceDate(formatStringToDate(emp
				.getSocialInsuranceDate()));
		// 手机
		argEmpInfo.setMobilePhone(emp.getMobilePhone());
		// 家庭电话
		argEmpInfo.setFamilyTel(emp.getFamilyTel());
		// 邮政编码
		argEmpInfo.setPostalcode(emp.getPostalcode());
		// 家庭住址
		argEmpInfo.setFamilyAddress(emp.getFamilyAddress());
		// 办公电话1
		argEmpInfo.setOfficeTel1(emp.getOfficeTel1());
		// 办公电话2
		argEmpInfo.setOfficeTel2(emp.getOfficeTel2());
		// 传真
		argEmpInfo.setFax(emp.getFax());
		// 电子邮件
		argEmpInfo.setEMail(emp.getEMail());
		// 紧急联系人
		argEmpInfo.setInstancyMan(emp.getInstancyMan());

		// 紧急联系电话
		argEmpInfo.setInstancyTel(emp.getInstancyTel());
		// 毕业学校
		// modified by liuyi 091123 表中无该属性类型不对应 已解决
		argEmpInfo.setGraduateSchoolId(emp.getSchoolCodeId());
		// 当前学历
		argEmpInfo.setEducationId(emp.getEducationId());
		// 学位
		argEmpInfo.setDegreeId(emp.getDegreeId());
		// 员工身份
		// modified by liuyi 091123 表中无该属性 已解决
		argEmpInfo.setWorkId(emp.getWorkId());
		// 劳保工种
		// modified by liuyi 091123 表中无该属性 已解决
		argEmpInfo.setLbWorkId(emp.getLbWorkId());
		// 毕业时间
		argEmpInfo.setGraduateDate(formatStringToDate(emp.getGraduateDate()));
		// 是否退转军人
		argEmpInfo.setIsVeteran(emp.getIsVeteran());
		// 位置号
		argEmpInfo.setOrderBy(emp.getOrderBy());
		// 推荐人
		argEmpInfo.setRecommendMan(emp.getRecommendMan());

		// 特长
		argEmpInfo.setOneStrongSuit(emp.getOneStrongSuit());
		// 备注
		argEmpInfo.setMemo(emp.getMemo());
		// 名族
		// modified by liuyi 091123 表中无该属性 已解决
		argEmpInfo.setNationCodeId(emp.getNationCodeId());
		// 学习专业
		// modified by liuyi 091123 表中无该属性 已解决
		argEmpInfo.setSpecialtyCodeId(emp.getSpecialtyCodeId());
		// 执行岗级
		argEmpInfo.setCheckStationGrade(emp.getCheckStationGrade());
		// 薪级
		argEmpInfo.setSalaryLevel(emp.getSalaryLevel());
		// 标准岗级
		argEmpInfo.setStationGrade(emp.getStationGrade());
		// 修改人
		// modified by liuyi 091123 表中该属性类型为number
		// argEmpInfo.setLastModifiyBy(employee.getWorkerCode());
		argEmpInfo.setLastModifiyBy(employee.getWorkerId());
		// 修改时间
		argEmpInfo.setLastModifiyDate(formatStringToDate(emp
				.getLastModifiedDate()));
	}

	/**
	 * 取得照片数据
	 * 
	 * @throws IOException
	 */
	private byte[] getPhotoData() throws IOException {
		InputStream bis = null;

		if (photo == null) {
			return null;
		}
		try {
			bis = new BufferedInputStream(new FileInputStream(photo), 1024 * 16);

			int intLength = (int) photo.length();
			byte[] data = new byte[intLength];

			bis.read(data);
			return data;
		} finally {
			if (bis != null) {
				bis.close();
			}
		}
	}

	/**
	 * 根据日期日期字符串和形式返回日期
	 * 
	 * @param argDateStr
	 *            日期字符串
	 * @return 日期
	 */
	private Date formatStringToDate(String argDateStr) {
		if (argDateStr == null || argDateStr.trim().length() < 1) {
			return null;
		}

		// 日期形式
		SimpleDateFormat sdfFrom = null;
		// 返回日期
		Date result = null;

		try {
			String strFormat = DATE_FORMAT_YYYYMMDD;
			if (argDateStr.length() > 16) {
				strFormat = DATE_FORMAT_YYYYMMDD_TIME_SEC;
			} else if (argDateStr.length() > 10) {
				strFormat = DATE_FORMAT_YYYYMMDD_TIME;
			}
			sdfFrom = new SimpleDateFormat(strFormat);
			// 格式化日期
			result = sdfFrom.parse(argDateStr);
		} catch (Exception e) {
			result = null;
		} finally {
			sdfFrom = null;
		}

		return result;
	}

	/**
	 * 根据日期和形式返回日期字符串
	 * 
	 * @param argDate
	 *            日期
	 * @param argFormat
	 *            日期形式字符串
	 * @return 日期字符串
	 */
	private String formatDate(Date argDate, String argFormat) {
		if (argDate == null) {
			return "";
		}

		// 日期形式
		SimpleDateFormat sdfFrom = null;
		// 返回字符串
		String strResult = null;

		try {
			sdfFrom = new SimpleDateFormat(argFormat);
			// 格式化日期
			strResult = sdfFrom.format(argDate).toString();
		} catch (Exception e) {
			strResult = "";
		} finally {
			sdfFrom = null;
		}

		return strResult;
	}

	/**
	 * 文件不存在异常
	 */
	private class FileNotExistException extends RuntimeException {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 构造函数
		 */
		public FileNotExistException() {
			super();
		}

		/**
		 * 构造函数
		 * 
		 * @param argMsg
		 *            异常消息
		 */
		public FileNotExistException(String argMsg) {
			super(argMsg);
		}
	}

	/**
	 * 取得员工职务任免信息
	 */
	public HrJPosition getPosition() {
		return position;
	}

	/**
	 * 设置员工职务任免信息
	 * 
	 * @param position
	 */
	public void setPosition(HrJPosition position) {
		this.position = position;
	}

	/**
	 * 获得员工技术职称信息
	 * 
	 * @return
	 */
	public HrJProfessionalPost getPost() {
		return post;
	}

	/**
	 * 设置员工技术职称信息
	 * 
	 * @param post
	 */
	public void setPost(HrJProfessionalPost post) {
		this.post = post;
	}

	/**
	 * add by liuyi 20100603 保存员工人字档案信息
	 * 
	 * @throws IOException
	 * 
	 * @throws Exception
	 */
	public void savePersonnelFileInfo() throws IOException {
		LogUtil.log("Action:保存员工基本维护信息开始", Level.INFO, null);
		// 通过员工ID取得员工信息
		HrJEmpInfo empInfo = empRemote.findById(emp.getEmpId());

		String strFilePath = request.getParameter("filePath");
		// 如果有文件上传
		if (strFilePath != null && strFilePath.length() > 0) {
			if (photo == null || !photo.exists()) {
				throw new FileNotExistException();
			}
			// 取得人员照片处理远程对象
			HrJEmpPhotoFacadeRemote photoRemote = (HrJEmpPhotoFacadeRemote) factory
					.getFacadeRemote("HrJEmpPhotoFacade");

			// 取得照片数据
			byte[] photoData = getPhotoData();

			// 通过员工ID取得员工照片信息
			HrJEmpPhoto photoInfo = photoRemote.findById(emp.getEmpId());
			if (photoInfo == null) {
				// 增加员工照片信息
				photoInfo = new HrJEmpPhoto();

				// 人员ID
				photoInfo.setEmpId(emp.getEmpId());
				// 照片
				photoInfo.setPhoto(photoData);
				// 企业编码
				// modified by liuyi 091123 表中无该属性 已解决
				photoInfo.setEnterpriseCode(employee.getEnterpriseCode());
				// 上次修改人
				// modified by liuyi 091123 表中无该属性 已解决
				photoInfo.setLastModifiedBy(employee.getWorkerCode());
				// 上次修改日期
				// modified by liuyi 091123 表中无该属性 已解决
				photoInfo.setLastModifiedDate(new Date());
				// 是否使用 已解决
				// modified by liuyi 091123 表中无该属性
				photoInfo.setIsUse(Constants.IS_USE_Y);

				photoRemote.save(photoInfo);
			} else {
				// 照片
				photoInfo.setPhoto(photoData);
				// 上次修改人
				// modified by liuyi 091123 表中无该属性 已解决
				photoInfo.setLastModifiedBy(employee.getWorkerCode());

				photoRemote.update(photoInfo);
			}
		}

		// 名字
		empInfo.setChsName(emp.getChsName());
		// ******************************************
		// 部门
		empInfo.setDeptId(emp.getDeptId());
		// 工作岗位
		empInfo.setStationId(emp.getStationId());
		// 员工身份
		empInfo.setWorkId(emp.getWorkId());
		// 籍贯
		//empInfo.setNativePlaceId(emp.getNativePlaceId()); 
		   //籍贯 add by fyyang 20100714 籍贯改为直接输入
		empInfo.setNativePlaceName(emp.getNativePlaceName()); 
		// 员工工号 老
		empInfo.setEmpCode(emp.getEmpCode());
		// 户口
		empInfo.setHousehold(emp.getHousehold());
		// 家庭住址
		empInfo.setFamilyAddress(emp.getFamilyAddress());
		// 血型
		empInfo.setBloodType(emp.getBloodType());
		// 国籍
		empInfo.setCountry(emp.getCountry());
		// 本人成分
		empInfo.setComponent(emp.getComponent());

		// *******************************************

		// 出生日期
		empInfo.setBrithday(formatStringToDate(emp.getBrithday()));
		// 性别
		empInfo.setSex(emp.getSex());
		// 参加工作日期
		empInfo.setWorkDate(formatStringToDate(emp.getWorkDate()));
		// 是否主业
		empInfo.setIsMainWork(emp.getIsMainWork());

		// 政治面貌
		empInfo.setPoliticsId(emp.getPoliticsId());
		// 入党转正时间
		empInfo.setPartPositiveDate(formatStringToDate(emp
				.getPartPositiveDate()));

		// 民族
		empInfo.setNationCodeId(emp.getNationCodeId());

		// 当前学历
		empInfo.setEducationId(emp.getEducationId());

		// 婚否状况
		empInfo.setIsWedded(emp.getIsWedded());
		// 入党时间
		empInfo.setIntoPartDate(formatStringToDate(emp.getIntoPartDate()));
		// 修改人
		empInfo.setLastModifiyBy(employee.getWorkerId());
		// 修改时间
		// empInfo.setLastModifiyDate(formatStringToDate(emp.getLastModifiedDate()));
		empInfo.setLastModifiyDate(new Date());

		// 社保卡号
		// empInfo.setSocialInsuranceId(emp.getSocialInsuranceId());
		// 职称
		// empInfo.setTechnologyTitlesTypeId(emp.getTechnologyTitlesId());
		// 技能等级
		// empInfo.setJnGrade(emp.getJnGrade());
		// 身份证号
		// empInfo.setIdentityCard(emp.getIdentityCard());
		// 职称等级
		// empInfo.setTechnologyGradeId(emp.getTechnologyGradeId());
		// 进厂日期
		// empInfo.setMissionDate(formatStringToDate(emp.getMissionDate()));

		
		// 修改员工信息
		empInfo = empRemote.update(empInfo);

		write("{success:true,msg:true,modifiedDate:\""
				+ formatDate(empInfo.getLastModifiyDate(),
						DATE_FORMAT_YYYYMMDD_TIME_SEC) + "\"}");
		LogUtil.log("Action:保存员工基本维护信息正常结束", Level.INFO, null);
	}

	/**
	 * add by liuyi 20100603 人字档案导出excel文件
	 * 
	 * @throws IOException
	 * @throws DataChangeException
	 */
	public void importPersonnelFilesInfo() throws IOException,
			DataChangeException {
		InputStream is = null;
		Workbook workbook = null;

		// 返回到页面的reponseText
		String str = "";
		String type = request.getParameter("type");
		if ("baseInfo".equals(type)) {
			str = this.treatBaseInfoXlsFile(is, workbook);
			write(str);

		} else if ("workResume".equals(type)) {
			str = this.treatWorkResumeInfoXlsFile(is, workbook);
			write(str);
		} else if ("studyResume".equals(type)) {
			str = this.treatStudyResumeInfoXlsFile(is, workbook);
			write(str);
		} else if ("familyMember".equals(type)) {
			str = this.treatFamilyMemberInfoXlsFile(is, workbook);
			write(str);
		} else if ("workcontract".equals(type)) {
			str = this.treatWorkcontractInfoXlsFile(is, workbook);
			write(str);
		} else {
			write("{success:true,msg:'导入出现异常！'}");
		}

	}

	/**
	 * add by liuyi 20100611 基本信息
	 * 
	 * @param is
	 * @param workbook
	 * @return
	 * @throws IOException
	 */
	private String treatBaseInfoXlsFile(InputStream is, Workbook workbook)
			throws IOException {

		String msg = "";
		List<HrJEmpInfo> empList = new ArrayList<HrJEmpInfo>();

		try {
			is = new FileInputStream(xlsFile);
			workbook = Workbook.getWorkbook(is);// 得到工作薄
			Sheet sheet = workbook.getSheet(0);// 得到工作薄中的第一个工作表
			int rows = sheet.getRows();// 得到excel的总行数
			if (rows == 0) {
				msg = "{success:true,msg:'无数据进行导入!'}";
			} else if (rows == 1) {
				msg = "{success:true,msg:'文件除一列头行外，至少还需一行数据!'}";
			} else {
				int[] column_indexs = new int[sheet.getRow(0).length];
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String columnError = null;
				columnError = addColumnIndex(column_indexs, sheet.getRow(0),
						"baseInfo");
				// 列名是否存在问题
				if (columnError != null)
					return columnError;

				for (int i = 1; i < rows; i++) {
					HrJEmpInfo empInfo = new HrJEmpInfo();
					Cell[] cells = sheet.getRow(i);// i行的所有单元格
					// if(cells.length > column_indexs.length){
					// i++;
					// msg = "第" + i + "行的数据超过列头行！";
					// break;
					// }
					for (int j = 0; j < column_indexs.length; j++) {
						/*
						 * 0:姓名 1:人员编码 2:内部编号 3:部门名称 4:岗位名称 5:出生日期 6:年龄 7:性别
						 * 8:民族 9:员工身份 10:是否有照片 11:政治面貌 12:是否主业 13:籍贯 14:自定义编码
						 * 15:户籍地址 16:户口 17:婚姻状况 18:国籍 19:血型 20:现有文化程度 21:本人成分
						 * 22:入党时间 23:入党转正时间 j 每行对应的列索引
						 */
						// 0:姓名列
						if (column_indexs[j] == 0) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String name = "";
								String cellString = cells[j].getContents();
								for (int ii = 0; ii < cellString.length(); ii++) {
									if (!"".equals(cellString.substring(ii,
											ii + 1))) {
										name += cellString
												.substring(ii, ii + 1);
									}
								}
								empInfo.setChsName(name);
							} else {
								i++;
								msg = "第" + i + "行姓名必须填写！";
								break;
							}
						}
						// 1:人员编码列
						else if (column_indexs[j] == 1) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								if (cells[j].getContents().length() > 20) {
									i++;
									msg = "第" + i + "行人员编码超过20位！";
									break;
								} else {
									boolean isRepeat = false;
									for (int aa = i + 1; aa < rows; aa++) {
										if (cells[j].getContents().equals(
												sheet.getRow(aa)[j]
														.getContents())) {
											i++;
											msg = "第" + i + "行与第" + (++aa)
													+ "行人员编码重复！";
											isRepeat = true;
										}
									}
									if (isRepeat) {
										break;
									} else {
										empInfo.setNewEmpCode(cells[j]
												.getContents());
										empInfo.setEmpCode(cells[j]
												.getContents());
									}
								}
							} else {
								i++;
								msg = "第" + i + "行人员编码必须填写！";
								break;
							}
						}
						// 2:内部编码列
						else if (column_indexs[j] == 2) {

						}
						// 3:部门名称
						else if (column_indexs[j] == 3) {
							if (cells.length > j) {
								if (!"".equals(cells[j].getContents())) {
									String[] deptNames = cells[j].getContents()
											.split("_");
									HrCDeptFacadeRemote deptRemote = (HrCDeptFacadeRemote) factory
											.getFacadeRemote("HrCDeptFacade");
									boolean isDeptError = false;
									for (int z = 0; z < deptNames.length; z++) {
										if (isDeptError)
											continue;
										Long deptId = deptRemote
												.getDeptIdByDeptName(deptNames[z]);
										if (deptId == null) {
											i++;
											msg = "第" + i + "行部门名称中:"
													+ deptNames[z] + "尚未维护！";
											;
											isDeptError = true;
										} else {
											if (z == deptNames.length - 1)
												empInfo.setDeptId(deptId);
										}
									}
									if (isDeptError)
										break;
								}
							}
						}
						// 4:岗位名称
						else if (column_indexs[j] == 4) {
							if (cells.length > j) {
								if (!"".equals(cells[j].getContents())) {
									HrCStationFacadeRemote stationFacadeRemote = (HrCStationFacadeRemote) factory
											.getFacadeRemote("HrCStationFacade");
									Long stationId = stationFacadeRemote
											.getStationIdByName(cells[j]
													.getContents());
									if (stationId == null) {
										i++;
										msg = "第" + i + "行岗位名称中:"
												+ cells[j].getContents()
												+ "尚未维护！";
										break;
									} else {
										empInfo.setStationId(stationId);
									}
								}
							}
						}
						// 5:出生日期
						else if (column_indexs[j] == 5) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String[] birthDayArr = cells[j].getContents()
										.split("-");
								if (birthDayArr.length != 3
										|| birthDayArr[0].length() != 4) {
									i++;
									msg = "第" + i + "行出生日期格式填写不正确！";
									break;
								}
								if (birthDayArr[1].length() == 1)
									birthDayArr[1] = "0" + birthDayArr[1];
								if (birthDayArr[2].length() == 1)
									birthDayArr[2] = "0" + birthDayArr[2];
								try {
									empInfo.setBrithday(df.parse(birthDayArr[0]
											+ "-" + birthDayArr[1] + "-"
											+ birthDayArr[2]));
								} catch (Exception e1) {
									i++;
									msg = "第" + i + "行出生日期格式填写不正确！";
									break;
								}
							}
						}
						// 6:年龄
						else if (column_indexs[j] == 6) {

						}
						// 7:性别
						else if (column_indexs[j] == 7) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {

								if (cells[j].getContents().trim().equals("男")) {
									empInfo.setSex("M");
								} else if (cells[j].getContents().trim()
										.equals("女")) {
									empInfo.setSex("W");
								} else {
									i++;
									msg = "第" + i + "行性别填写不正确！";
									break;
								}

							}
						}
						// 8:民族
						else if (column_indexs[j] == 8) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								Long nationId = nationRemote
										.findNationIdByName(cells[j]
												.getContents(), employee
												.getEnterpriseCode());
								if (nationId == null) {
									i++;
									msg = "第" + i + "行民族:"
											+ cells[j].getContents() + "尚未维护！";
									;
									break;
								} else {
									empInfo.setNationCodeId(nationId);
								}
							}
						}
						// 9:员工身份
						else if (column_indexs[j] == 9) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								HrCWorkidFacadeRemote workidRemote = (HrCWorkidFacadeRemote) (factory)
										.getFacadeRemote("HrCWorkidFacade");
								Long workId = workidRemote
										.getWorkIdByWorkName(cells[j]
												.getContents());
								if (workId == null) {
									i++;
									msg = "第" + i + "行员工身份:"
											+ cells[j].getContents() + "尚未维护！";
									;
									break;
								} else {
									empInfo.setWorkId(workId);
								}
							}
						}
						// 10:是否有照片
						else if (column_indexs[j] == 10) {

						}

						// 11:政治面貌
						else if (column_indexs[j] == 11) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								Long politicsId = politicsRemote
										.findPoliticsIdByName(cells[j]
												.getContents(), employee
												.getEnterpriseCode());
								if (politicsId == null) {
									i++;
									msg = "第" + i + "行政治面貌:"
											+ cells[j].getContents() + "尚未维护！";
									;
									break;
								} else {
									empInfo.setPoliticsId(politicsId);
								}
							}
						}
						// 12:是否主业
						else if (column_indexs[j] == 12) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								if (cells[j].getContents().trim().equals("是")) {
									empInfo.setIsMainWork("Y");
								} else if (cells[j].getContents().trim()
										.equals("否")) {
									empInfo.setIsMainWork("N");
								} else {
									i++;
									msg = "第" + i + "行是否主业填写不正确！";
									break;
								}
							}
						}
						// 13:籍贯
						else if (column_indexs[j] == 13) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								HrCNativePlaceFacadeRemote nativePlaceremote = (HrCNativePlaceFacadeRemote) factory
										.getFacadeRemote("HrCNativePlaceFacade");
								Long nativePlaceId = nativePlaceremote
										.getNativePlaceIdByName(cells[j]
												.getContents());
								if (nativePlaceId == null) {
									i++;
									msg = "第" + i + "行籍贯:"
											+ cells[j].getContents() + "尚未维护！";
									;
									break;
								} else {
									empInfo.setNativePlaceId(nativePlaceId);
								}
							}
						}
						// 14:自定义编码
						else if (column_indexs[j] == 14) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								if (cells[j].getContents().length() > 20) {
									i++;
									msg = "第" + i + "行自定义编码超过20位！";
									break;
								} else {
									empInfo.setEmpCode(cells[j].getContents());
								}
							}
						}
						// 15:户籍地址
						else if (column_indexs[j] == 15) {
							if (cells.length > j)
								empInfo
										.setFamilyAddress(cells[j]
												.getContents());
						}
						// 16:户口
						else if (column_indexs[j] == 16) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								if (cells[j].getContents().trim().equals("农业")) {
									empInfo.setHousehold("1");
								} else if (cells[j].getContents().trim()
										.equals("非农")) {
									empInfo.setHousehold("2");
								} else {
									i++;
									msg = "第" + i + "行户口只有‘农业’，‘非农’！";
									break;
								}
							}
						}
						// 17:婚姻状况
						else if (column_indexs[j] == 17) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								if (cells[j].getContents().trim().equals("已婚")) {
									empInfo.setIsWedded("0");
								} else if (cells[j].getContents().trim()
										.equals("未婚")) {
									empInfo.setIsWedded("1");
								} else if (cells[j].getContents().trim()
										.equals("离异")) {
									empInfo.setIsWedded("2");
								} else if (cells[j].getContents().trim()
										.equals("丧偶")) {
									empInfo.setIsWedded("3");
								} else {
									i++;
									msg = "第" + i + "行婚姻状况填写不正确！";
									break;
								}
							}
						}
						// 18:国籍
						else if (column_indexs[j] == 18) {
							if (cells.length > j)
								empInfo.setCountry(cells[j].getContents());
						}
						// 19:血型
						else if (column_indexs[j] == 19) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								if (cells[j].getContents().trim().equals("A")) {
									empInfo.setBloodType("A");
								} else if (cells[j].getContents().trim()
										.equals("B")) {
									empInfo.setBloodType("B");
								} else if (cells[j].getContents().trim()
										.equals("AB")) {
									empInfo.setBloodType("AB");
								} else if (cells[j].getContents().trim()
										.equals("O")) {
									empInfo.setBloodType("O");
								} else {
									i++;
									msg = "第" + i + "行血型填写不正确！";
									break;
								}
							}
						}
						// 20:现有文化程度
						else if (column_indexs[j] == 20) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								Long educationId = educationRemote
										.findEducationIdByName(cells[j]
												.getContents(), employee
												.getEnterpriseCode());
								if (educationId == null) {
									i++;
									msg = "第" + i + "行现有文化程度:"
											+ cells[j].getContents() + "尚未维护！";
									;
									break;
								} else {
									empInfo.setEducationId(educationId);
								}
							}
						}
						// 21:本人成分
						else if (column_indexs[j] == 21) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								if (cells[j].getContents().trim().equals("工人")) {
									empInfo.setComponent("1");
								} else if (cells[j].getContents().trim()
										.equals("农民")) {
									empInfo.setComponent("2");
								} else if (cells[j].getContents().trim()
										.equals("知识分子")) {
									empInfo.setComponent("3");
								} else if (cells[j].getContents().trim()
										.equals("其它")) {
									empInfo.setComponent("4");
								} else {
									i++;
									msg = "第" + i + "行本人成分填写不正确！";
									break;
								}
							}
						}
						// 22:入党时间
						else if (column_indexs[j] == 22) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String[] intoPartDateArr = cells[j]
										.getContents().split("-");
								if (intoPartDateArr.length != 3
										|| intoPartDateArr[0].length() != 4) {
									i++;
									msg = "第" + i + "行入党时间日期格式填写不正确！";
									break;
								}
								if (intoPartDateArr[1].length() == 1)
									intoPartDateArr[1] = "0"
											+ intoPartDateArr[1];
								if (intoPartDateArr[2].length() == 1)
									intoPartDateArr[2] = "0"
											+ intoPartDateArr[2];
								try {
									empInfo.setIntoPartDate(df
											.parse(intoPartDateArr[0] + "-"
													+ intoPartDateArr[1] + "-"
													+ intoPartDateArr[2]));
								} catch (Exception e1) {
									i++;
									msg = "第" + i + "行入党时间日期格式填写不正确！";
									break;
								}
							}
						}
						// 23:入党转正时间
						else if (column_indexs[j] == 23) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String[] partPositiveDateArr = cells[j]
										.getContents().split("-");
								if (partPositiveDateArr.length != 3
										|| partPositiveDateArr[0].length() != 4) {
									i++;
									msg = "第" + i + "行入党转正时间日期格式填写不正确！";
									break;
								}
								if (partPositiveDateArr[1].length() == 1)
									partPositiveDateArr[1] = "0"
											+ partPositiveDateArr[1];
								if (partPositiveDateArr[2].length() == 1)
									partPositiveDateArr[2] = "0"
											+ partPositiveDateArr[2];
								try {
									empInfo.setPartPositiveDate(df
											.parse(partPositiveDateArr[0] + "-"
													+ partPositiveDateArr[1]
													+ "-"
													+ partPositiveDateArr[2]));
								} catch (Exception e1) {
									i++;
									msg = "第" + i + "行入党转正时间日期格式填写不正确！";
									break;
								}
							}
						}
					}

					if (msg.equals("")) {
						if (empInfo.getNewEmpCode() == null) {
							msg = "人员编码列必须存在，请检查导入的excel文件!";
							break;
						}
					}
					empInfo.setLastModifiyBy(employee.getWorkerId());
					empInfo.setLastModifiyDate(new Date());
					empInfo.setEmpState("U");
					empInfo.setCreateBy(employee.getWorkerId());
					empInfo.setCreateDate(new Date());
					empInfo.setEnterpriseCode(employee.getEnterpriseCode());
					empInfo.setIsUse("Y");
					empList.add(empInfo);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}

		if (msg.equals("")) {
			empRemote.importPersonnelFilesInfo(empList);
			return "{success:true,msg:'导入成功！'}";
		} else {
			return "{success:true,msg:'数据填写存在问题：" + msg + "'}";
		}

	}

	/**
	 * add by liuyi 20100611 工作经历
	 * 
	 * @param is
	 * @param workbook
	 * @return
	 * @throws IOException
	 */
	private String treatWorkResumeInfoXlsFile(InputStream is, Workbook workbook)
			throws IOException {

		String msg = "";
		List<HrJWorkresume> workResumeList = new ArrayList<HrJWorkresume>();

		try {
			is = new FileInputStream(xlsFile);
			workbook = Workbook.getWorkbook(is);// 得到工作薄
			Sheet sheet = workbook.getSheet(0);// 得到工作薄中的第一个工作表
			int rows = sheet.getRows();// 得到excel的总行数
			if (rows == 0) {
				msg = "{success:true,msg:'无数据进行导入!'}";
			} else if (rows == 1) {
				msg = "{success:true,msg:'文件除一列头行外，至少还需一行数据!'}";
			} else {
				int[] column_indexs = new int[sheet.getRow(0).length];
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String columnError = null;
				columnError = addColumnIndex(column_indexs, sheet.getRow(0),
						"workResume");
				// 列名是否存在问题
				if (columnError != null)
					return columnError;

				for (int i = 1; i < rows; i++) {
					HrJWorkresume workResumeEntity = new HrJWorkresume();
					Cell[] cells = sheet.getRow(i);// i行的所有单元格
					// if(cells.length > column_indexs.length){
					// i++;
					// msg = "第" + i + "行的数据超过列头行！";
					// break;
					// }
					for (int j = 0; j < column_indexs.length; j++) {
						/*
						 * 0:人员编码 1:单位名称 2:工作部门 3:岗位名称 4:开始时间 5:结束时间 6:备注
						 */
						// 0:人员编码
						if (column_indexs[j] == 0) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								Long empId = empRemote
										.getEmpIdByNewEmpCode(cells[j]
												.getContents());
								if (empId == null) {
									i++;
									msg = "第" + i + "行人员编码中:"
											+ cells[j].getContents()
											+ "的人员尚未维护！";
									break;
								} else
									workResumeEntity.setEmpId(empId);
							} else {
								i++;
								msg = "第" + i + "行人员编码必须填写！";
								break;
							}
						}

						// 1:单位名称
						else if (column_indexs[j] == 2) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								workResumeEntity
										.setUnit(cells[j].getContents());
							} else {
								i++;
								msg = "第" + i + "行单位名称必须填写！";
								break;
							}
						}
						// 2:工作部门
						else if (column_indexs[j] == 3) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								workResumeEntity.setDeptName(cells[j]
										.getContents());
							} else {
								i++;
								msg = "第" + i + "行工作部门必须填写！";
								break;
							}

						}
						// 3:岗位名称
						else if (column_indexs[j] == 4) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								workResumeEntity.setStationName(cells[j]
										.getContents());
							} else {
								i++;
								msg = "第" + i + "行岗位名称必须填写！";
								break;
							}
						}
						// 4:开始时间
						else if (column_indexs[j] == 5) {

							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String split = new String();
								if(cells[j].getContents().contains("-")){
									split = "-";
								}else if(cells[j].getContents().contains("/"))
								{
									split = "/";
								}
								String[] startDayArr = cells[j].getContents()
										.split(split);
								if (startDayArr.length != 3
										|| startDayArr[0].length() != 4) {
									i++;
									msg = "第" + i + "行开始时间格式填写不正确！";
									break;
								}
								if (startDayArr[1].length() == 1)
									startDayArr[1] = "0" + startDayArr[1];
								if (startDayArr[2].length() == 1)
									startDayArr[2] = "0" + startDayArr[2];
								try {
									workResumeEntity.setStartDate(df
											.parse(startDayArr[0] + "-"
													+ startDayArr[1] + "-"
													+ startDayArr[2]));
								} catch (Exception e1) {
									i++;
									msg = "第" + i + "行开始时间格式填写不正确！";
									break;
								}
							}

						}
						// 5:结束时间
						else if (column_indexs[j] == 6) {

							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String split = new String();
								if(cells[j].getContents().contains("-")){
									split = "-";
								}else if(cells[j].getContents().contains("/"))
								{
									split = "/";
								}
								String[] endDayArr = cells[j].getContents()
										.split(split);
								if (endDayArr.length != 3
										|| endDayArr[0].length() != 4) {
									i++;
									msg = "第" + i + "行结束时间格式填写不正确！";
									break;
								}
								if (endDayArr[1].length() == 1)
									endDayArr[1] = "0" + endDayArr[1];
								if (endDayArr[2].length() == 1)
									endDayArr[2] = "0" + endDayArr[2];
								try {
									workResumeEntity.setEndDate(df
											.parse(endDayArr[0] + "-"
													+ endDayArr[1] + "-"
													+ endDayArr[2]));
								} catch (Exception e1) {
									i++;
									msg = "第" + i + "行结束时间格式填写不正确！";
									break;
								}
							}

						}
						// 6:备注
						else if (column_indexs[j] == 7) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								workResumeEntity
										.setMemo(cells[j].getContents());
							}

						}
					}

					if (msg.equals("")) {
						if (workResumeEntity.getEmpId() == null) {
							msg = "人员编码列必须存在，请检查导入的excel文件!";
							break;
						}
						if (workResumeEntity.getUnit() == null) {
							msg = "单位名称列必须存在，请检查导入的excel文件!";
							break;
						}
						if (workResumeEntity.getDeptName() == null) {
							msg = "工作部门列必须存在，请检查导入的excel文件!";
							break;
						}
						if (workResumeEntity.getStationName() == null) {
							msg = "岗位名称列必须存在，请检查导入的excel文件!";
							break;
						}
					}
					workResumeEntity.setInsertby(employee.getWorkerCode());
					workResumeEntity.setInsertdate(new Date());
					workResumeEntity.setEnterpriseCode(employee
							.getEnterpriseCode());
					workResumeEntity.setIsUse("Y");
					workResumeEntity
							.setLastModifiedBy(employee.getWorkerCode());
					workResumeEntity.setLastModifiedDate(new Date());

					workResumeList.add(workResumeEntity);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}

		if (msg.equals("")) {
			workRemote.importPersonnelFilesWorkResume(workResumeList);
			return "{success:true,msg:'导入成功！'}";
		} else {
			return "{success:true,msg:'数据填写存在问题：" + msg + "'}";
		}

	}

	private String treatStudyResumeInfoXlsFile(InputStream is, Workbook workbook)
			throws IOException {

		String msg = "";
		List<HrJEducation> educationList = new ArrayList<HrJEducation>();

		try {
			is = new FileInputStream(xlsFile);
			workbook = Workbook.getWorkbook(is);// 得到工作薄
			Sheet sheet = workbook.getSheet(0);// 得到工作薄中的第一个工作表
			int rows = sheet.getRows();// 得到excel的总行数
			if (rows == 0) {
				msg = "{success:true,msg:'无数据进行导入!'}";
			} else if (rows == 1) {
				msg = "{success:true,msg:'文件除一列头行外，至少还需一行数据!'}";
			} else {
				int[] column_indexs = new int[sheet.getRow(0).length];
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String columnError = null;
				columnError = addColumnIndex(column_indexs, sheet.getRow(0),
						"studyResume");
				// 列名是否存在问题
				if (columnError != null)
					return columnError;

				for (int i = 1; i < rows; i++) {
					HrJEducation education = new HrJEducation();
					Cell[] cells = sheet.getRow(i);// i行的所有单元格
					// if(cells.length > column_indexs.length){
					// i++;
					// msg = "第" + i + "行的数据超过列头行！";
					// break;
					// }
					for (int j = 0; j < column_indexs.length; j++) {
						/*
						 * 0：人员编码 1：学历 2：毕业院校 3：所学专业 4：专业类别 5：学习方式 6:入学时间 7:毕业时间
						 * 8:教育状态 9:毕业证书编号 10:学位
						 */
						// 0:人员编码
						if (column_indexs[j] == 0) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								Long empId = empRemote
										.getEmpIdByNewEmpCode(cells[j]
												.getContents());
								if (empId == null) {
									i++;
									msg = "第" + i + "行人员编码中:"
											+ cells[j].getContents()
											+ "的人员尚未维护！";
									break;
								} else
									education.setEmpId(empId);
							} else {
								i++;
								msg = "第" + i + "行人员编码必须填写！";
								break;
							}
						}

						// 1：学历
						else if (column_indexs[j] == 2) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								Long educationId = educationRemote
										.findEducationIdByName(cells[j]
												.getContents(), employee
												.getEnterpriseCode());
								if (educationId == null) {
									i++;
									msg = "第" + i + "行学历:"
											+ cells[j].getContents() + "尚未维护！";
									;
									break;
								} else {
									education.setEducationId(educationId);
								}
							} else {
								i++;
								msg = "第" + i + "行学历必须填写！";
								break;
							}
						}
						// 2：毕业院校
						else if (column_indexs[j] == 3) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								//update by sychen 20100712
                                education.setGraduateSchool(cells[j].getContents());
//								HrCSchoolFacadeRemote schooleRemote = (HrCSchoolFacadeRemote) factory
//										.getFacadeRemote("HrCSchoolFacade");
//								Long schoolCodeId = schooleRemote
//										.getSchoolCodeIdByName(cells[j]
//												.getContents(), employee
//												.getEnterpriseCode());
//								if (schoolCodeId == null) {
//									i++;
//									msg = "第" + i + "行毕业院校:"
//											+ cells[j].getContents() + "尚未维护！";
//									;
//									break;
//								} else
//									education.setSchoolCodeId(schoolCodeId);
							} else {
								i++;
								msg = "第" + i + "行毕业院校必须填写！";
								break;
							}

						}
						// 3：所学专业
						else if (column_indexs[j] == 4) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								//update by sychen 20100712
								education.setSpecialty(cells[j].getContents());
//								HrCSpecialtyFacadeRemote remote = (HrCSpecialtyFacadeRemote) factory
//										.getFacadeRemote("HrCSpecialtyFacade");
//								Long specialtyId = remote.getSpecialyIdByName(
//										cells[j].getContents(), employee
//												.getEnterpriseCode());
//								if (specialtyId == null) {
//									i++;
//									msg = "第" + i + "行所学专业:"
//											+ cells[j].getContents() + "尚未维护！";
//									;
//									break;
//								} else
//									education.setSpecialtyCodeId(specialtyId);
							} else {
								i++;
								msg = "第" + i + "行所学专业必须填写！";
								break;
							}
						}
						// 4：专业类别
						else if (column_indexs[j] == 5) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								education.setCategory(cells[j].getContents());
							}
							// else {
							// i++;
							// msg = "第" + i + "行专业类别必须填写！";
							// break;
							// }
						}
						// 5：学习方式
						else if (column_indexs[j] == 6) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								HrCStudytypeFacadeRemote remote = (HrCStudytypeFacadeRemote) factory
										.getFacadeRemote("HrCStudytypeFacade");
								Long studytypeId = remote.getStudytypeIdByName(
										cells[j].getContents(), employee
												.getEnterpriseCode());
								if (studytypeId == null) {
									i++;
									msg = "第" + i + "行学习方式:"
											+ cells[j].getContents() + "尚未维护！";
									;
									break;
								} else
									education.setStudyTypeCodeId(studytypeId);
							}
						}
						// 6:入学时间
						else if (column_indexs[j] == 7) {

							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String split = new String();
								if(cells[j].getContents().contains("-")){
									split = "-";
								}else if(cells[j].getContents().contains("/"))
								{
									split = "/";
								}
								String[] startDayArr = cells[j].getContents()
										.split(split);
								if (startDayArr.length != 3
										|| startDayArr[0].length() != 4) {
									i++;
									msg = "第" + i + "行入学时间格式填写不正确！";
									break;
								}
								if (startDayArr[1].length() == 1)
									startDayArr[1] = "0" + startDayArr[1];
								if (startDayArr[2].length() == 1)
									startDayArr[2] = "0" + startDayArr[2];
								try {
									education.setEnrollmentDate(df
											.parse(startDayArr[0] + "-"
													+ startDayArr[1] + "-"
													+ startDayArr[2]));
								} catch (Exception e1) {
									i++;
									msg = "第" + i + "行入学时间格式填写不正确！";
									break;
								}
							}

						}
						// 7:毕业时间
						else if (column_indexs[j] == 8) {

							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String split = new String();
								if(cells[j].getContents().contains("-")){
									split = "-";
								}else if(cells[j].getContents().contains("/"))
								{
									split = "/";
								}
								String[] endDayArr = cells[j].getContents()
										.split(split);
								if (endDayArr.length != 3
										|| endDayArr[0].length() != 4) {
									i++;
									msg = "第" + i + "行毕业时间格式填写不正确！";
									break;
								}
								if (endDayArr[1].length() == 1)
									endDayArr[1] = "0" + endDayArr[1];
								if (endDayArr[2].length() == 1)
									endDayArr[2] = "0" + endDayArr[2];
								try {
									education.setGraduateDate(df
											.parse(endDayArr[0] + "-"
													+ endDayArr[1] + "-"
													+ endDayArr[2]));
								} catch (Exception e1) {
									i++;
									msg = "第" + i + "行毕业时间格式填写不正确！";
									break;
								}
							}

						}
						// 8:教育状态
						else if (column_indexs[j] == 9) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								if (cells[j].getContents().trim().equals("毕业"))
									education.setEducationResult("1");
								else if (cells[j].getContents().trim().equals(
										"结业"))
									education.setEducationResult("2");
								else if (cells[j].getContents().trim().equals(
										"肆业"))
									education.setEducationResult("3");
								else if (cells[j].getContents().trim().equals(
										"其他"))
									education.setEducationResult("4");
								else {
									i++;
									msg = "第" + i + "行教育状态填写不正确！";
									break;
								}
							}
						}
						// 9:毕业证书编号
						else if (column_indexs[j] == 10) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								education.setCertificateCode(cells[j]
										.getContents());
							}
						}
						// 10:学位
						else if (column_indexs[j] == 11) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								HrCDegreeFacadeRemote remote = (HrCDegreeFacadeRemote) factory
										.getFacadeRemote("HrCDegreeFacade");
								Long degreeId = remote.getDegreeIdByName(
										cells[j].getContents(), employee
												.getEnterpriseCode());
								if (degreeId == null) {
									i++;
									msg = "第" + i + "行学位:"
											+ cells[j].getContents() + "尚未维护！";
									;
									break;
								} else
									education.setDegreeId(degreeId);
							}
						}
					}

					if (msg.equals("")) {
						if (education.getEmpId() == null) {
							msg = "人员编码列必须存在，请检查导入的excel文件!";
							break;
						}
						if (education.getEducationId() == null) {
							msg = "学历列必须存在，请检查导入的excel文件!";
							break;
						}
						//update by sychen 20100712
						if (education.getGraduateSchool() == null) {
//						if (education.getSchoolCodeId() == null) {
							msg = "毕业院校列必须存在，请检查导入的excel文件!";
							break;
						}
						//update by sychen 20100712
						if (education.getSpecialty()== null) {
//						if (education.getSpecialtyCodeId() == null) {
							msg = "所学专业列必须存在，请检查导入的excel文件!";
							break;
						}
					}
					education.setInsertby(employee.getWorkerCode());
					education.setInsertdate(new Date());
					education.setEnterpriseCode(employee.getEnterpriseCode());
					education.setIsUse("Y");
					education.setLastModifiedBy(employee.getWorkerCode());
					education.setLastModifiedDate(new Date());

					educationList.add(education);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}

		if (msg.equals("")) {
			studyRemote.importPersonnelFilesWorkResume(educationList);
			return "{success:true,msg:'导入成功！'}";
		} else {
			return "{success:true,msg:'数据填写存在问题：" + msg + "'}";
		}

	}

	/**
	 * add by liuyi 20100611 家庭成员
	 * 
	 * @param is
	 * @param workbook
	 * @return
	 * @throws IOException
	 */
	private String treatFamilyMemberInfoXlsFile(InputStream is,
			Workbook workbook) throws IOException {

		String msg = "";
		List<HrJFamilymember> familyMemberList = new ArrayList<HrJFamilymember>();

		try {
			is = new FileInputStream(xlsFile);
			workbook = Workbook.getWorkbook(is);// 得到工作薄
			Sheet sheet = workbook.getSheet(0);// 得到工作薄中的第一个工作表
			int rows = sheet.getRows();// 得到excel的总行数
			if (rows == 0) {
				msg = "{success:true,msg:'无数据进行导入!'}";
			} else if (rows == 1) {
				msg = "{success:true,msg:'文件除一列头行外，至少还需一行数据!'}";
			} else {
				int[] column_indexs = new int[sheet.getRow(0).length];
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String columnError = null;
				columnError = addColumnIndex(column_indexs, sheet.getRow(0),
						"familyMember");
				// 列名是否存在问题
				if (columnError != null)
					return columnError;

				for (int i = 1; i < rows; i++) {
					HrJFamilymember familymember = new HrJFamilymember();
					Cell[] cells = sheet.getRow(i);// i行的所有单元格
					// if(cells.length > column_indexs.length){
					// i++;
					// msg = "第" + i + "行的数据超过列头行！";
					// break;
					// }
					for (int j = 0; j < column_indexs.length; j++) {
						/*
						 * 0:人员编码 1:成员姓名 2:与本人关系 3:民族 4:文化程度 5:工作单位 6:职务 7:政治面貌
						 * 8:出生日期 9:备注
						 */
						// 0:人员编码
						if (column_indexs[j] == 0) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								Long empId = empRemote
										.getEmpIdByNewEmpCode(cells[j]
												.getContents());
								if (empId == null) {
									i++;
									msg = "第" + i + "行人员编码中:"
											+ cells[j].getContents()
											+ "的人员尚未维护！";
									break;
								} else
									familymember.setEmpId(empId);
							} else {
								i++;
								msg = "第" + i + "行人员编码必须填写！";
								break;
							}
						}

						// 1:成员姓名
						else if (column_indexs[j] == 1) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								familymember.setName(cells[j].getContents());
							} else {
								i++;
								msg = "第" + i + "行成员姓名必须填写！";
								break;
							}
						}
						// 2:与本人关系
						else if (column_indexs[j] == 2) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								HrCAppellationFacadeRemote remote = (HrCAppellationFacadeRemote) factory
										.getFacadeRemote("HrCAppellationFacade");
								Long callsCodeId = remote.getCallsCodeIdByName(
										cells[j].getContents(), employee
												.getEnterpriseCode());
								if (callsCodeId == null) {
									i++;
									msg = "第" + i + "行与本人关系:"
											+ cells[j].getContents() + "尚未维护！";
									;
									break;
								} else
									familymember.setCallsCodeId(callsCodeId);
							} else {
								i++;
								msg = "第" + i + "行与本人关系必须填写！";
								break;
							}

						}
						// 3:民族
						else if (column_indexs[j] == 3) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								Long nationId = nationRemote
										.findNationIdByName(cells[j]
												.getContents(), employee
												.getEnterpriseCode());
								if (nationId == null) {
									i++;
									msg = "第" + i + "行民族:"
											+ cells[j].getContents() + "尚未维护！";
									;
									break;
								} else {
									familymember.setNationCodeId(nationId);
								}
							}
						}
						// 4:文化程度
						else if (column_indexs[j] == 4) {
							Long educationId = educationRemote
									.findEducationIdByName(cells[j]
											.getContents(), employee
											.getEnterpriseCode());
							if (educationId == null) {
								i++;
								msg = "第" + i + "行文化程度:"
										+ cells[j].getContents() + "尚未维护！";
								;
								break;
							} else {
								familymember.setEducationId(educationId);
							}
						}
						// 5:工作单位
						else if (column_indexs[j] == 5) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								familymember.setUnit(cells[j].getContents());
							}
						}
						// 6:职务
						else if (column_indexs[j] == 6) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {

								familymember.setHeadshipName(cells[j]
										.getContents());
							}

						}
						// 7:政治面貌
						else if (column_indexs[j] == 7) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {

								Long politicsId = politicsRemote
										.findPoliticsIdByName(cells[j]
												.getContents(), employee
												.getEnterpriseCode());
								if (politicsId == null) {
									i++;
									msg = "第" + i + "行政治面貌:"
											+ cells[j].getContents() + "尚未维护！";
									;
									break;
								} else {
									familymember.setPoliticsId(politicsId);
								}

							}

						}

						// 8:出生日期
						else if (column_indexs[j] == 8) {

							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String[] endDayArr = cells[j].getContents()
										.split("-");
								if (endDayArr.length != 3
										|| endDayArr[0].length() != 4) {
									i++;
									msg = "第" + i + "行出生日期格式填写不正确！";
									break;
								}
								if (endDayArr[1].length() == 1)
									endDayArr[1] = "0" + endDayArr[1];
								if (endDayArr[2].length() == 1)
									endDayArr[2] = "0" + endDayArr[2];
								try {
									familymember.setBirthday(df
											.parse(endDayArr[0] + "-"
													+ endDayArr[1] + "-"
													+ endDayArr[2]));
								} catch (Exception e1) {
									i++;
									msg = "第" + i + "行出生日期格式填写不正确！";
									break;
								}
							}

						}
						// 9:备注
						else if (column_indexs[j] == 9) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								familymember.setMemo(cells[j].getContents());
							}
						}

					}
					if (msg.equals("")) {
						if (familymember.getEmpId() == null) {
							msg = "人员编码列必须存在，请检查导入的excel文件!";
							break;
						}
						if (familymember.getName() == null) {
							msg = "成员姓名列必须存在，请检查导入的excel文件!";
							break;
						}
						if (familymember.getCallsCodeId() == null) {
							msg = "与本人关系列必须存在，请检查导入的excel文件!";
							break;
						}

					}
					familymember.setInsertby(employee.getWorkerCode());
					familymember.setInsertdate(new Date());
					familymember
							.setEnterpriseCode(employee.getEnterpriseCode());
					familymember.setIsUse("Y");
					familymember.setLastModifiedBy(employee.getWorkerCode());
					familymember.setLastModifiedDate(new Date());

					familyMemberList.add(familymember);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}

		if (msg.equals("")) {
			familyRemote.importPersonnelFilesWorkResume(familyMemberList);
			return "{success:true,msg:'导入成功！'}";
		} else {
			return "{success:true,msg:'数据填写存在问题：" + msg + "'}";
		}

	}

	/**
	 * add by liuyi 20100610 设置excel中列头对应的索引位组成的数组
	 */
	private String addColumnIndex(int[] column_indexs, Cell[] cells, String type) {
		for (int i = 0; i < cells.length; i++) {
			boolean isError = true;
			if ("baseInfo".equals(type)) {
				for (int j = 0; j < BASEINFO_COLUMN_NAMES.length; j++) {
					if (BASEINFO_COLUMN_NAMES[j].equals(cells[i].getContents())) {
						column_indexs[i] = j;
						isError = false;
					}
				}
			} else if ("workResume".equals(type)) {
				for (int j = 0; j < WORKRESUME_COLUMN_NAMES.length; j++) {
					if (WORKRESUME_COLUMN_NAMES[j].equals(cells[i]
							.getContents())) {
						column_indexs[i] = j;
						isError = false;
					}
				}
			} else if ("studyResume".equals(type)) {
				for (int j = 0; j < STUDYRESUME_COLUMN_NAMES.length; j++) {
					if (STUDYRESUME_COLUMN_NAMES[j].equals(cells[i]
							.getContents())) {
						column_indexs[i] = j;
						isError = false;
					}
				}
			} else if ("familyMember".equals(type)) {
				for (int j = 0; j < FAMILYMEMBER_COLUMN_NAMES.length; j++) {
					if (FAMILYMEMBER_COLUMN_NAMES[j].equals(cells[i]
							.getContents())) {
						column_indexs[i] = j;
						isError = false;
					}
				}
			} else if ("workcontract".equals(type)) {
				for (int j = 0; j < WORKCONTRACT_COLUMN_NAMES.length; j++) {
					if (WORKCONTRACT_COLUMN_NAMES[j].equals(cells[i]
							.getContents())) {
						column_indexs[i] = j;
						isError = false;
					}
				}
			}

			if (isError) {
				return "{success:true,msg:'" + cells[i].getContents()
						+ "列不是要导入的具体列！'}";
			}
		}

		// if("baseInfo".equals(type)){
		// for(int i=0; i < cells.length; i++)
		// {
		// boolean isError = true;
		// for(int j= 0; j < BASEINFO_COLUMN_NAMES.length; j++)
		// {
		// if(BASEINFO_COLUMN_NAMES[j].equals(cells[i].getContents())){
		// column_indexs[i] = j;
		// isError = false;
		// }
		// }
		// if(isError){
		// return "{success:true,msg:'"+cells[i].getContents()+"列不是要导入的具体列！'}";
		// }
		// }
		// }else if("workResume".equals(type)){
		// for(int i=0; i < cells.length; i++)
		// {
		// boolean isError = true;
		// for(int j= 0; j < WORKRESUME_COLUMN_NAMES.length; j++)
		// {
		// if(WORKRESUME_COLUMN_NAMES[j].equals(cells[i].getContents())){
		// column_indexs[i] = j;
		// isError = false;
		// }
		// }
		// if(isError){
		// return "{success:true,msg:'"+cells[i].getContents()+"列不是要导入的具体列！'}";
		// }
		// }
		// }else if("studyResume".equals(type)){
		// for(int i=0; i < cells.length; i++)
		// {
		// boolean isError = true;
		// for(int j= 0; j < STUDYRESUME_COLUMN_NAMES.length; j++)
		// {
		// if(STUDYRESUME_COLUMN_NAMES[j].equals(cells[i].getContents())){
		// column_indexs[i] = j;
		// isError = false;
		// }
		// }
		// if(isError){
		// return "{success:true,msg:'"+cells[i].getContents()+"列不是要导入的具体列！'}";
		// }
		// }
		// }

		return null;
	}

	/**
	 * add by liuyi 20100610 导出人字档案综合信息
	 * 
	 * @throws JSONException
	 */
	public void writePersonnelFilesExcel() throws JSONException {
		WritableWorkbook wwb = null;
		Label label = null;

		List<HrJEmpInfoBean> list = empRemote.getEmpMaintBaseInfo(null,
				employee.getEnterpriseCode());
		if (list == null || list.size() == 0) {
			write("{success:true,msg:'无数据导出！'}");
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			File file = new File("基本信息" + sdf.format(new Date()) + ".xls");
			System.out.println(file.getAbsolutePath());

			write("{success:true,msg:'导出文件为:"
					+ JSONUtil.serialize(file.getAbsolutePath()) + "！'}");
		}

	}

	private String treatWorkcontractInfoXlsFile(InputStream is,
			Workbook workbook) throws IOException, DataChangeException {

		String msg = "";
		List<HrJWorkcontract> contractList = new ArrayList<HrJWorkcontract>();

		try {
			is = new FileInputStream(xlsFile);
			workbook = Workbook.getWorkbook(is);// 得到工作薄
			Sheet sheet = workbook.getSheet(0);// 得到工作薄中的第一个工作表
			int rows = sheet.getRows();// 得到excel的总行数
			if (rows == 0) {
				msg = "{success:true,msg:'无数据进行导入!'}";
			} else if (rows == 1) {
				msg = "{success:true,msg:'文件除一列头行外，至少还需一行数据!'}";
			} else {
				int[] column_indexs = new int[sheet.getRow(0).length];
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String columnError = null;
				columnError = addColumnIndex(column_indexs, sheet.getRow(0),
						"workcontract");
				// 列名是否存在问题
				if (columnError != null)
					return columnError;

				for (int i = 1; i < rows; i++) {
					HrJWorkcontract workContract = new HrJWorkcontract();
					Cell[] cells = sheet.getRow(i);// i行的所有单元格
					if (cells.length > column_indexs.length) {
						i++;
						msg = "第" + i + "行的数据超过列头行！";
						break;
					}
					for (int j = 0; j < column_indexs.length; j++) {
						/*
						 * 
						 * 0：状态 1：人员编码 2：甲方 3：签订机构 4：合同编号 5：签订时间 6:合同期限（年）
						 * 7:合同期限（月） 8:用工形式 9:合同类别 10合同解除文号, 11:合同生效日期,12:合同终止日期
						 */
						// 0:状态
						if (column_indexs[j] == 0) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								if (cells[j].getContents().trim().equals("有效")) {
									workContract.setIfExecute("1");

								} else {
									if ((cells[j].getContents().trim()
											.equals("无效"))) {
										workContract.setIfExecute("0");
									}
								}

							} else {
								i++;
								msg = "第" + i + "行状态必须填写！";
								break;

							}

						}
						// 1：人员编码
						else if (column_indexs[j] == 1) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								Long empId = empRemote
										.getEmpIdByNewEmpCode(cells[j]
												.getContents());
								if (empId == null) {
									i++;
									msg = "第" + i + "行人员编码中:"
											+ cells[j].getContents()
											+ "的人员尚未维护！";
									break;
								} else
									workContract.setEmpId(empId);
							} else {
								i++;
								msg = "第" + i + "行人员编码必须填写！";
								break;
							}
						}
						// 2甲方
						else if (column_indexs[j] == 2) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								workContract.setOwner(cells[j].getContents());
							} else {
								i++;
								msg = "第" + i + "行甲方必须填写！";
								break;
							}

						}
						// 3：签订机构
						else if (column_indexs[j] == 3) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								workContract.setSignedInstitutions(cells[j]
										.getContents());
							} else {
								i++;
								msg = "第" + i + "行签订机构必须填写！";
								break;
							}
						}
						// 4：合同编号
						else if (column_indexs[j] == 4) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								workContract.setWrokContractNo(cells[j]
										.getContents());
							} else {
								i++;
								msg = "第" + i + "行合同编号必须填写！";
								break;
							}
						}
						// 5：签订时间
						else if (column_indexs[j] == 5) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String[] workSignDate = cells[j].getContents()
										.split("-");
								if (workSignDate.length != 3
										|| workSignDate[0].length() != 4) {
									i++;
									msg = "第" + i + "行签订时间日期格式填写不正确！";
									break;
								}
								if (workSignDate[1].length() == 1)
									workSignDate[1] = "0" + workSignDate[1];
								if (workSignDate[2].length() == 1)
									workSignDate[2] = "0" + workSignDate[2];
								try {
									workContract.setWorkSignDate(df
											.parse(workSignDate[0] + "-"
													+ workSignDate[1] + "-"
													+ workSignDate[2]));
								} catch (Exception e1) {
									i++;
									msg = "第" + i + "行签订日期格式填写不正确！";
									break;
								}
							}
						}
						// 6:合同期限(年)
						else if (column_indexs[j] == 6) {

							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								Long contractId = contractterm
										.findcontractTermId(cells[j]
												.getContents());
								if (contractId == null) {
									i++;
									msg = "第" + i + "行合同期限中:"
											+ cells[j].getContents()
											+ "的期限尚未维护！";
									break;
								} else {
									workContract.setContractTermId(contractId);
									workContract.setContractPeriod(cells[j]
											.getContents());
								}
							}

						}

						// 7:合同期限(月)
						else if (column_indexs[j] == 7) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {

							}

						}
						// 8:用工形式
						else if (column_indexs[j] == 8) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								if (cells[j].getContents().trim().equals("临时工"))
									workContract.setLaborType("1");
								else if (cells[j].getContents().trim().equals(
										"培训工"))
									workContract.setLaborType("2");
								else if (cells[j].getContents().trim().equals(
										"合同工"))
									workContract.setLaborType("3");
								else if (cells[j].getContents().trim().equals(
										"劳务派遣"))
									workContract.setLaborType("4");
								else if (cells[j].getContents().trim().equals(
										"其它"))
									workContract.setLaborType("5");
								else {
									i++;
									msg = "第" + i + "行用工形式填写不正确！";
									break;
								}
							}
						}
						// 9:合同类别
						else if (column_indexs[j] == 9) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {

								if (cells[j].getContents().trim().equals(
										"固定期限合同"))
									workContract.setContractType("1");
								else if (cells[j].getContents().trim().equals(
										"无固定期限合同"))
									workContract.setContractType("2");
								else if (cells[j].getContents().trim().equals(
										"完成一定工作期限合同"))
									workContract.setContractType("3");
								else {
									i++;
									msg = "第" + i + "行合同类别填写不正确！";
									break;
								}
							}
						}
						// 10:合同解除文号
						else if (column_indexs[j] == 10) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								Long contractId = contractstop
										.findContractID(cells[j].getContents());
								if (contractId == null) {
									i++;
									msg = "第" + i + "行合同解除文号中:"
											+ cells[j].getContents()
											+ "的文字号尚未维护！";
									break;
								} else {

									HrJContractstop entity = contractstop
											.findContractstop(contractId);
									entity.setContractTerminatedCode(cells[j]
											.getContents());
									contractstop.update(entity);
								}
							} else {
								// i++;
								// msg = "第" + i + "行合同解除文号必须填写！";
								// break;

							}

						}// 11:合同生效日期
						else if (column_indexs[j] == 11) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String[] startDate = cells[j].getContents()
										.split("-");
								if (startDate.length != 3
										|| startDate[0].length() != 4) {
									i++;
									msg = "第" + i + "行合同生效日期格式填写不正确！";
									break;
								}
								if (startDate[1].length() == 1)
									startDate[1] = "0" + startDate[1];
								if (startDate[2].length() == 1)
									startDate[2] = "0" + startDate[2];
								try {
									workContract.setStartDate(df
											.parse(startDate[0] + "-"
													+ startDate[1] + "-"
													+ startDate[2]));
								} catch (Exception e1) {
									i++;
									msg = "第" + i + "行合同生效日期格式填写不正确！";
									break;
								}
							}
						}
						// 12:合同终止日期
						else if (column_indexs[j] == 11) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String[] endDate = cells[j].getContents()
										.split("-");
								if (endDate.length != 3
										|| endDate[0].length() != 4) {
									i++;
									msg = "第" + i + "行合同终止日期格式填写不正确！";
									break;
								}
								if (endDate[1].length() == 1)
									endDate[1] = "0" + endDate[1];
								if (endDate[2].length() == 1)
									endDate[2] = "0" + endDate[2];
								try {
									workContract.setEndDate(df.parse(endDate[0]
											+ "-" + endDate[1] + "-"
											+ endDate[2]));
								} catch (Exception e1) {
									i++;
									msg = "第" + i + "行合同终止日期格式填写不正确！";
									break;
								}
							}
						}
					}
					workContract.setIsUse("Y");
					workContract
							.setEnterpriseCode(employee.getEnterpriseCode());
					workContract.setInsertby(employee.getWorkerCode());
					workContract.setInsertdate(new Date());
					workContract.setLastModifiedBy(employee.getWorkerCode());
					workContract.setLastModifiedDate(new Date());
					contractList.add(workContract);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}

		if (msg.equals("")) {
			workcontract.saveBat(contractList);
			return "{success:true,msg:'导入成功！'}";
		} else {
			return "{success:true,msg:'数据填写存在问题：" + msg + "'}";
		}
	}

	public void getEmpMaintBaseInfoList() {
		try {
			String strDeptId = request.getParameter("strDeptId");
			if (strDeptId.indexOf("#") != -1) {
				strDeptId = strDeptId.substring(1);
			}
			List<HrJEmpInfoBean> list = empRemote.getEmpMaintBaseInfoList(Long
					.parseLong(strDeptId), employee.getEnterpriseCode());
			Iterator<HrJEmpInfoBean> iterator = list.iterator();
			ArrayList<HrJEmpInfoBean> list3 = new ArrayList();
			while (iterator.hasNext()) {
				HrJEmpInfoBean HrJEmpInfoBean = iterator.next();
				List list2 = empRemote.getEmpDeptAll(HrJEmpInfoBean.getDeptId()
						.toString());
				String deptName = "";
				Iterator it = list2.iterator();
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					deptName += data[4].toString();
					if (it.hasNext()) {
						deptName += "_";
					}
					HrJEmpInfoBean.setDeptName(deptName);
				}
				list3.add(HrJEmpInfoBean);
				
			}
			String strResult = JSONUtil.serialize(list3);
//			 System.out.println(strResult);
			write(strResult);
		} catch (Exception e) {
			write(Constants.SQL_FAILURE);
		}

	}

	public File getXlsFile() {
		return xlsFile;
	}

	public void setXlsFile(File xlsFile) {
		this.xlsFile = xlsFile;
	}
}
