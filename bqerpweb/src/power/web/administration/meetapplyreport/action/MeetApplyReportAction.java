package power.web.administration.meetapplyreport.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJMeet;
import power.ejb.administration.AdJMeetFacadeRemote;
import power.ejb.administration.AdJMeetfile;
import power.ejb.administration.AdJMeetfileFacadeRemote;
import power.ejb.administration.business.MeetApplyReportFacadeRemote;
import power.ejb.administration.comm.CodeCommonFacadeRemote;
import power.ejb.basedata.BaseDataManager;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONUtil;

public class MeetApplyReportAction extends AbstractAction{
    private static final long serialVersionUID = 1L;
    /** 日期形式字符串 yyyy-MM-dd */
    private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
    /** 日期形式字符串 yyyy-MM-dd HH:mm */
    private static final String DATE_FORMAT_YYYYMMDD_TIME = "yyyy-MM-dd HH:mm";
    /** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
    private static final String DATE_FORMAT_YYYYMMDD_TIME_SEC = "yyyy-MM-dd HH:mm:ss";
    
    /** 画面参数开始页 */
	public Long start;
	/** 画面参数页面最大值 */
	public Long limit;
	/** 画面申请单号 */
	public String meetNo;
	/** 画面记录上次修改时间 */
	public String userLastModifyTime;
	
	/** 会务申请上报实体 */
	public AdJMeet meetInfo;
	/** 申请人 */
	public String workerName;
	/** 申请部门 */
	public String deptName;
	/** 会议开始时间 */
	public String meetStartTime;
	/** 会议结束时间 */
	public String meetEndTime;
	/** 会议就餐时间 */
	public String dinnerTime;
	/** 画面附件ID */
	public Long affixId;
	/** 画面就餐人数 */
	public Long dinnerPeople;
	/** 画面用烟数量 */
	public Long cigNum;
	/** 画面用酒数量 */
	public Long wineNum;
	/** 画面套间数量 */
	public Long TJNum;
	/** 画面单间数量 */
	public Long DJNum;
	/** 画面标间数量 */
	public Long BJNum;
	/**文件上传**/
	File file;
	/**上传文件名**/
	String fileFileName;
	/**上传文件类型**/
	String fileContentType;
	/** 画面文件上次修改时间 */
	private String fileUpdateTime;
	
	@SuppressWarnings("serial")
	public class myPageObject extends PageObject {
		private String workerName;
		private String deptName;
		@SuppressWarnings("unchecked")
		private List list;
		private Long totalCount;
		/**
		 * @return the workerName
		 */
		public String getWorkerName() {
			return workerName;
		}
		/**
		 * @param workerName the workerName to set
		 */
		public void setWorkerName(String workerName) {
			this.workerName = workerName;
		}
		/**
		 * @return the deptName
		 */
		public String getDeptName() {
			return deptName;
		}
		/**
		 * @param deptName the deptName to set
		 */
		public void setDeptName(String deptName) {
			this.deptName = deptName;
		}
		/**
		 * @return the list
		 */
		@SuppressWarnings("unchecked")
		public List getList() {
			return list;
		}
		/**
		 * @param list the list to set
		 */
		@SuppressWarnings("unchecked")
		public void setList(List list) {
			this.list = list;
		}
		/**
		 * @return the totalCount
		 */
		public Long getTotalCount() {
			return totalCount;
		}
		/**
		 * @param totalCount the totalCount to set
		 */
		public void setTotalCount(Long totalCount) {
			this.totalCount = totalCount;
		}
		
		
	}
	
    /** remote */
    /** 会务申请上报 remote */
    private MeetApplyReportFacadeRemote meetRemote;
    /** 会务申请上报附件 remote */
    private AdJMeetfileFacadeRemote fileRemote;
    /** 会务申请上报表 remote */
    private AdJMeetFacadeRemote adjMeetRemote;
    /** 共通控件接口 */
    BaseDataManager bll = (BaseDataManager) Ejb3Factory.getInstance()
    .getFacadeRemote("BaseDataManagerImpl");
    private CodeCommonFacadeRemote codeRemote;
    
    public MeetApplyReportAction() {
    	meetRemote = (MeetApplyReportFacadeRemote) 
    	factory.getFacadeRemote("MeetApplyReportFacade");
    	
    	fileRemote = (AdJMeetfileFacadeRemote) 
    	factory.getFacadeRemote("AdJMeetfileFacade");
    	
    	adjMeetRemote = (AdJMeetFacadeRemote) 
    	factory.getFacadeRemote("AdJMeetFacade");
    	
    	codeRemote = (CodeCommonFacadeRemote) 
    	factory.getFacadeRemote("CodeCommonFacade");
    }
    
    /**
	 * 返回所有会务申请上报单
	 */
	public void getMeetApplyReportList() throws Exception {
		try {
			LogUtil.log("Action:查询会务申请上报单开始", Level.INFO, null);
			PageObject pobj = new PageObject();
			int intStart = Integer.parseInt(start.toString());
			int intLimit = Integer.parseInt(limit.toString());
	        // 查询操作
			pobj = meetRemote.getMeetApplyReportList(employee.getWorkerCode(), employee.getEnterpriseCode(),
					intStart, intLimit);
			// 解析字符串
			String str = null;
			if(null == pobj.getList()) {
				str = "{\"list\":[],\"totalCount\":0, \"workerName\":" + 
				employee.getWorkerName() + ",\"deptName\":" + employee.getDeptName() + "}";
			} else {
				myPageObject myPobj = new myPageObject();
	 			myPobj.setList(pobj.getList());
				myPobj.setTotalCount(pobj.getTotalCount());
				myPobj.setWorkerName(employee.getWorkerName());
				myPobj.setDeptName(employee.getDeptName());
				str = JSONUtil.serialize(myPobj);
			}
			LogUtil.log("Action:查询会务申请上报单结束", Level.INFO, null);
			write(str);
		} catch (SQLException e) {
			LogUtil.log("Action:查询会务申请上报单失败", Level.INFO, e);
			write(Constants.SQL_FAILURE);
		}
	}
	
	/**
	 * 新增会务申请上报单
	 */
	public void addMeetApplyReport() throws Exception {
		try {
			LogUtil.log("Action:新增会务申请上报单开始", Level.INFO, null);
			AdJMeet meet = new AdJMeet();
			// 把画面上的实体的值复制过来
			meet = meetInfo;
			// 设置申请人
			meet.setApplyMan(employee.getWorkerCode());
			// 设置会议开始时间
			meet.setStartmeetDate(formatStringToDate(meetStartTime));
			// 设置会议结束时间
			meet.setEndmeetDate(formatStringToDate(meetEndTime));
			// 设置会议就餐时间
			meet.setDinnerTime(formatStringToDate(dinnerTime));
			// 设置会议就餐人数
			meet.setDinnerNum(dinnerPeople);
			// 设置会议用烟数量
			meet.setCigNum(cigNum);
			// 设置会议用酒数量
			meet.setWineNum(wineNum);
			// 设置套间数量
			meet.setTfNum(TJNum);
			// 设置单间数量
			meet.setDjNum(DJNum);
			// 设置标间数量
			meet.setBjNum(BJNum);
			// 获得最大流水号
			meet.setId(adjMeetRemote.myGetMaxId());
			// 设置会务编码
			meet.setMeetId(codeRemote.getMeetAplNoCode(String.valueOf(employee.getWorkerCode())));
			// 设置是否使用为Y
			meet.setIsUse(Constants.IS_USE_Y);
			// 设置单据状态为0（未上报）
			meet.setDcmStatus(Constants.NUMBER_ZERO);
			// 设置更新时间
			meet.setUpdateTime(new Date());
			// 设置更新人
			meet.setUpdateUser(employee.getWorkerCode());
			// 设置企业编码
			meet.setEnterpriseCode(employee.getEnterpriseCode());
			adjMeetRemote.save(meet);
			LogUtil.log("Action:新增会务申请上报单结束", Level.INFO, null);
			write(Constants.ADD_SUCCESS);
		} catch (SQLException e) {
			LogUtil.log("Action:新增会务申请上报单失败", Level.INFO, e);
			write(Constants.SQL_FAILURE);
		}
	}
	/**
	 * 修改会务申请上报单
	 */
	public void modifyMeetApplyReport() throws Exception {
		try {
			LogUtil.log("Action:修改会务申请上报单开始", Level.INFO, null);
			
			// 得到数据库中的这个记录
			AdJMeet meet = adjMeetRemote.myFindById(meetNo);
			// 设置会议开始时间
			meet.setStartmeetDate(formatStringToDate(meetStartTime));
			// 设置会议结束时间
			meet.setEndmeetDate(formatStringToDate(meetEndTime));
			// 设置会议名称
			meet.setMeetName(meetInfo.getMeetName());
			// 设置会议地点
			meet.setMeetPlace(meetInfo.getMeetPlace());
			// 设置会议要求
			meet.setRoomNeed(meetInfo.getRoomNeed());
			// 设置会议其他要求
			meet.setMeetOther(meetInfo.getMeetOther());
			// 设置会议就餐时间
			meet.setDinnerTime(formatStringToDate(dinnerTime));
			// 设置会议就餐人数
			meet.setDinnerNum(dinnerPeople);
			// 设置会议用烟名称
			meet.setCigName(meetInfo.getCigName());
			// 设置会议用烟数量
			meet.setCigNum(cigNum);
			// 设置会议用酒名称
			meet.setWineName(meetInfo.getWineName());
			// 设置会议用酒数量
			meet.setWineNum(wineNum);
			// 设置套间数量
			meet.setTfNum(TJNum);
			// 设置套间用品
			meet.setTfThing(meetInfo.getTfThing());
			// 设置单间数量
			meet.setDjNum(DJNum);
			// 设置单间用品
			meet.setDjThing(meetInfo.getDjThing());
			// 设置标间数量
			meet.setBjNum(BJNum);
			// 设置标间用品
			meet.setBjThing(meetInfo.getBjThing());
			// 设置更新人
			meet.setUpdateUser(employee.getWorkerCode());
			// 排他: 更新时间
			meet.setUpdateTime(formatStringToDate(userLastModifyTime));
			
			adjMeetRemote.update(meet);
			LogUtil.log("Action:修改会务申请上报单结束", Level.INFO, null);
			write(Constants.MODIFY_SUCCESS);
		} catch(DataChangeException e) {
			LogUtil.log("Action:修改会务申请上报单失败", Level.INFO, e);
			write(Constants.DATA_USING);
			throw(e);
		} catch (Exception e) {
			LogUtil.log("Action:修改会务申请上报单失败", Level.INFO, e);
			write(Constants.SQL_FAILURE);
		}
	}
	
	/**
	 * 删除会务申请上报单以及该单的附件
	 */
	@SuppressWarnings("unchecked")
	public void deleteMeetApplyReport() throws Exception {
		LogUtil.log("Action:删除会务申请上报单开始", Level.INFO, null);
		
		UserTransaction tx = null;
		try {
			tx = (UserTransaction) new InitialContext()
				.lookup("java:comp/UserTransaction");
			tx.begin();
			
			// 删除附件
			// 由申请单号从附件表中查询所有该申请单的附件，依次删掉
			PageObject pobj = fileRemote.myFindByMeetId(meetNo);
			if (null != pobj && null != pobj.getList()) {
				List<AdJMeetfile> list = pobj.getList();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					AdJMeetfile file = (AdJMeetfile)it.next();
					// 逻辑删除
					file.setIsUse(Constants.IS_USE_N);
					// 设置修改人
					file.setUpdateUser(employee.getWorkerCode());
					fileRemote.update(file);
				}
			}
			
			// 删除上报单
			// 取得该上报单
			AdJMeet meet = adjMeetRemote.myFindById(meetNo);
			// 逻辑删除
			meet.setIsUse(Constants.IS_USE_N);
			// 设置修改人
			meet.setUpdateUser(employee.getWorkerCode());
			// 排他: 更新时间
			meet.setUpdateTime(formatStringToDate(userLastModifyTime));
			
			adjMeetRemote.update(meet);
			// 提交事务
			tx.commit();
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除会务申请上报单结束", Level.INFO, null);
		} catch(DataChangeException e) {
			LogUtil.log("Action:删除会务申请上报单失败", Level.INFO, e);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			write(Constants.DATA_USING);
			throw(e);
		} catch (SQLException e) {
			LogUtil.log("Action:删除会务申请上报单失败", Level.INFO, e);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			write(Constants.SQL_FAILURE);
		} catch (Exception e) {
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			// 删除失败
			LogUtil.log("Action:删除会务附件失败", Level.INFO, e);
		}
	}
	
	/**
	 * 上报会务申请上报单
	 */
	public void reportMeetApplyReport() throws Exception {
		try {
			LogUtil.log("Action:上报会务申请上报单开始", Level.INFO, null);
			// 上报上报单
			// 取得该上报单
			AdJMeet meet = adjMeetRemote.myFindById(meetNo);
			// 上报
			meet.setDcmStatus(Constants.REPORT_STATUS_1);
			// 设置修改时间
			meet.setUpdateTime(new Date());
			// 设置修改人
			meet.setUpdateUser(employee.getWorkerCode());
			// 排他: 更新时间
			meet.setUpdateTime(formatStringToDate(userLastModifyTime));
			
			adjMeetRemote.update(meet);
			LogUtil.log("Action:上报会务申请上报单结束", Level.INFO, null);
			write("{success:true,msg:'上报成功'}");
		} catch(DataChangeException e) {
			LogUtil.log("Action:上报会务申请上报单失败", Level.INFO, e);
			write(Constants.DATA_USING);
			throw(e);
		} catch (SQLException e) {
			LogUtil.log("Action:上报会务申请上报单失败", Level.INFO, null);
			write(Constants.SQL_FAILURE);
		}
	}
	
	/**
	 * 得到会务申请上报单附件list
	 */
	public void getMeetApplyReportAffixList() throws Exception {
		LogUtil.log("Action:得到会务申请上报单附件list开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		int intStart = Integer.parseInt(start.toString());
		int intLimit = Integer.parseInt(limit.toString());
		try {
			pobj = fileRemote.myFindByMeetId(meetNo, intStart, intLimit);
			// 解析字符串
			String str = null;
			if(null == pobj || null == pobj.getList()) {
				str = "{\"list\":[],\"totalCount\":0}";
			} else {
				str = JSONUtil.serialize(pobj);
			}
			LogUtil.log("Action:得到会务申请上报单附件list结束", Level.INFO, null);
			
			write(str);
		} catch (Exception e) {
			LogUtil.log("Action:得到会务申请上报单附件list失败", Level.INFO, null);
			write(Constants.SQL_FAILURE);
			throw e;
		}
	}
	
	/**
	 * 新增会务申请上报单附件
	 */
	public void addMeetApplyReportAffix() throws Exception {
		LogUtil.log("Action:文件的上传操作开始", Level.INFO, null);

		String strWorkerCode = employee.getWorkerCode();

		AdJMeetfile objFileInfo = new AdJMeetfile();
		InputStream bis = null;
		if (file != null) {
			if (file.exists()) {
				Long lngLength = file.length();
				int intLength = Integer.parseInt(lngLength.toString());
				try {
					bis = new BufferedInputStream(new FileInputStream(file),
							1024);
					byte[] data = new byte[intLength];
					if (bis.read(data) != -1) {
						objFileInfo.setFileText(data);
					}
					objFileInfo.setId(fileRemote.myGetMaxId());
					objFileInfo.setMeetId(meetNo);
					objFileInfo.setFileName(fileFileName);
					objFileInfo.setFileType(fileContentType);
					objFileInfo.setUpdateUser(strWorkerCode);
					objFileInfo.setIsUse(Constants.IS_USE_Y);
					objFileInfo.setUpdateTime(new Date());
					fileRemote.save(objFileInfo);
					write(Constants.UPLOAD_SUCCESS);
					LogUtil.log("Action:文件的上传操作正常结束", Level.INFO, null);
				} catch (SQLException e) {
					// 上传失败
					LogUtil.log("Action:上传会务附件失败", Level.INFO, e);
					write(Constants.SQL_FAILURE);
				} finally {
		            if (bis != null ){
		                bis.close();
		            }
				}
			} else {
				write(Constants.FILE_NOT_EXIST);
			}
		} else {
			write(Constants.FILE_NOT_EXIST);
		}
	}

	/**
	 * 下载会务申请上报单
	 * @throws IOException 
	 */
	public void downloadMeetApplyReportAffix() throws IOException {
		OutputStream out = null;
		try {
			LogUtil.log("Action:下载签报申请附件开始。", Level.INFO, null);
			// 由附件ID得到该附件的记录
			AdJMeetfile mFile = fileRemote.myFindById(affixId);
			if(mFile != null) {
				 // 情報設定
		        String name = URLEncoder.encode(mFile.getFileName(), "UTF-8");
	
		        response.setHeader("Content-Disposition", 
		                           "attachment; filename=" + 
		                           name);
		        response.setContentType(mFile.getFileType());
		        
		        out = new BufferedOutputStream(response.getOutputStream());
		        out.write(mFile.getFileText());
		        out.flush();
		        LogUtil.log("Action:下载签报申请附件结束。", Level.INFO, null);
			}
		} catch (IOException e) {
			LogUtil.log("Action:下载签报申请附件失败。", Level.SEVERE, e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * 删除会务申请上报单附件
	 */
	@SuppressWarnings("unchecked")
	public void deleteMeetApplyReportAffix() throws Exception {
		LogUtil.log("Action:删除会务附件开始", Level.INFO, null);
		try {
			// 由附件ID得到该附件的记录
			AdJMeetfile mFile = fileRemote.myFindById(affixId);
			
			// 逻辑删除
			mFile.setIsUse(Constants.IS_USE_N);
			// 设置修改人
			mFile.setUpdateUser(employee.getWorkerCode());
			// 排他: 更新时间
			mFile.setUpdateTime(formatStringToDate(fileUpdateTime));
			
			fileRemote.update(mFile);
			LogUtil.log("Action:删除会务附件结束", Level.INFO, null);
			write(Constants.DELETE_SUCCESS);
		} catch(DataChangeException e) {
			LogUtil.log("Action:删除会务附件失败", Level.INFO, e);
			write(Constants.DATA_USING);
			throw(e);
		} catch (SQLException e) {
			// 删除失败
			LogUtil.log("Action:删除会务附件失败", Level.INFO, e);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
     * 根据日期日期字符串和形式返回日期
     * @param argDateStr 日期字符串
     * @return 日期
     */
    private Date formatStringToDate(String argDateStr) {
        if (argDateStr == null ||argDateStr.trim().length() < 1) {
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
	 * @return the meetNo
	 */
	public String getMeetNo() {
		return meetNo;
	}

	/**
	 * @param meetNo the meetNo to set
	 */
	public void setMeetNo(String meetNo) {
		this.meetNo = meetNo;
	}

	/**
	 * @return the userLastModifyTime
	 */
	public String getUserLastModifyTime() {
		return userLastModifyTime;
	}

	/**
	 * @param userLastModifyTime the userLastModifyTime to set
	 */
	public void setUserLastModifyTime(String userLastModifyTime) {
		this.userLastModifyTime = userLastModifyTime;
	}

	/**
	 * @return the meetInfo
	 */
	public AdJMeet getMeetInfo() {
		return meetInfo;
	}

	/**
	 * @param meetInfo the meetInfo to set
	 */
	public void setMeetInfo(AdJMeet meetInfo) {
		this.meetInfo = meetInfo;
	}

	/**
	 * @return the workerName
	 */
	public String getWorkerName() {
		return workerName;
	}

	/**
	 * @param workerName the workerName to set
	 */
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	/**
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * @return the meetStartTime
	 */
	public String getMeetStartTime() {
		return meetStartTime;
	}

	/**
	 * @param meetStartTime the meetStartTime to set
	 */
	public void setMeetStartTime(String meetStartTime) {
		this.meetStartTime = meetStartTime;
	}

	/**
	 * @return the meetEndTime
	 */
	public String getMeetEndTime() {
		return meetEndTime;
	}

	/**
	 * @param meetEndTime the meetEndTime to set
	 */
	public void setMeetEndTime(String meetEndTime) {
		this.meetEndTime = meetEndTime;
	}

	/**
	 * @return the dinnerTime
	 */
	public String getDinnerTime() {
		return dinnerTime;
	}

	/**
	 * @param dinnerTime the dinnerTime to set
	 */
	public void setDinnerTime(String dinnerTime) {
		this.dinnerTime = dinnerTime;
	}

	/**
	 * @return the affixId
	 */
	public Long getAffixId() {
		return affixId;
	}

	/**
	 * @param affixId the affixId to set
	 */
	public void setAffixId(Long affixId) {
		this.affixId = affixId;
	}

	/**
	 * @return the dinnerPeople
	 */
	public Long getDinnerPeople() {
		return dinnerPeople;
	}

	/**
	 * @param dinnerPeople the dinnerPeople to set
	 */
	public void setDinnerPeople(Long dinnerPeople) {
		this.dinnerPeople = dinnerPeople;
	}

	/**
	 * @return the cigNum
	 */
	public Long getCigNum() {
		return cigNum;
	}

	/**
	 * @param cigNum the cigNum to set
	 */
	public void setCigNum(Long cigNum) {
		this.cigNum = cigNum;
	}

	/**
	 * @return the wineNum
	 */
	public Long getWineNum() {
		return wineNum;
	}

	/**
	 * @param wineNum the wineNum to set
	 */
	public void setWineNum(Long wineNum) {
		this.wineNum = wineNum;
	}

	/**
	 * @return the tJNum
	 */
	public Long getTJNum() {
		return TJNum;
	}

	/**
	 * @param num the tJNum to set
	 */
	public void setTJNum(Long num) {
		TJNum = num;
	}

	/**
	 * @return the dJNum
	 */
	public Long getDJNum() {
		return DJNum;
	}

	/**
	 * @param num the dJNum to set
	 */
	public void setDJNum(Long num) {
		DJNum = num;
	}

	/**
	 * @return the bJNum
	 */
	public Long getBJNum() {
		return BJNum;
	}

	/**
	 * @param num the bJNum to set
	 */
	public void setBJNum(Long num) {
		BJNum = num;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	/**
	 * @return the FileName
	 */
	public String getFileFileName() {
		return fileFileName;
	}
	
	/**
	 * @param file the FileName to set
	 */
	public void setFileFileName(String file) {
		this.fileFileName = file;
	}
	
	/**
	 * @return the ContentType
	 */
	public String getFileContentType() {
		return fileContentType;
	}
	
	/**
	 * @param file the ContentType to set
	 */
	public void setFileContentType(String file) {
		this.fileContentType = file;
	}
	
	/**
	 * @return the fileUpdateTime
	 */
	public String getFileUpdateTime() {
		return fileUpdateTime;
	}
	
	/**
	 * @param file the fileUpdateTime to set
	 */
	public void setFileUpdateTime(String file) {
		this.fileUpdateTime = file;
	}

}