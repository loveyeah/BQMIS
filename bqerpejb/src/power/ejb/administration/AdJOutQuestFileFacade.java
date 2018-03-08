/**
　* Copyright ustcsoft.com
　* All right reserved.
*/
package power.ejb.administration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.QuestFileInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 申请附件
 * 
 * @author sufeiyu
 */
@Stateless
public class AdJOutQuestFileFacade implements AdJOutQuestFileFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	public   static  final String FILETYPE  = "text/plain";

	/**
	 * 增加一条签报申请附件对象
	 * 
	 * @param entity 签报申请附件对象
	 *            
	 * @throws SQLException 
	 * @throws Exception
	 *            
	 */
	public void save(QuestFileInfo entity) throws SQLException {
		LogUtil.log("EJB：签报申请附件数据增加操作开始", Level.INFO, null);
		try {
			if (entity.getIdFile() == null) {
				entity.setIdFile(bll.getMaxId("AD_J_OUT_QUEST_FILE", "ID"));
			}
			String strSql   = "INSERT INTO AD_J_OUT_QUEST_FILE  ("
							+ " ID,"
							+ " APPLY_ID,"
							+ " FILE_TYPE,"
							+ " FILE_KIND,"
							+ " FILE_NAME,"
							+ " FILE_TEXT,"
							+ " IS_USE,"
							+ " UPDATE_USER,"
							+ " UPDATE_TIME"
							+ " )"
							+ " VALUES"
							+ "  ("
							+ " ?,"
							+ " ?,"
							+ " ?,"
							+ " ?,"
							+ " ?,"
							+ " ?,"
							+ " ?,"
							+ " ?,"
							+ " sysdate"
							+ " )";
            Object[] params = new Object[8];
            params[0] = entity.getIdFile();
            params[1] = entity.getApplyIdFile();
            params[2] = "";
//            params[2] = FILETYPE;
            // 功能性BUG
            //params[2] = entity.getFileTypeFile();
            params[3] = entity.getFileKindFile();
            params[4] = entity.getFileNameFile();
            params[5] = entity.getFileTextFile();
            params[6] = "Y";
            params[7] = entity.getUpdateUserFile();
            
            LogUtil.log("EJB:签报申请附件数据增加操作开始。SQL：", Level.INFO, null);
			LogUtil.log(strSql, Level.INFO, null);
            bll.exeNativeSQL(strSql, params);
			LogUtil.log("EJB：签报申请附件数据增加操作正常结束", Level.INFO, null);
		} catch (Exception re) {
			LogUtil.log("EJB：签报申请附件数据增加异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	/**
	 * 更新签报申请附件表
	 * 
	 * @param entity  签报申请附件对象 
	 * @param strUpdateTime 上次修改时间
	 *            
	 * @throws DataChangeException 
	 * @throws SQLException 
	 * @throws RuntimeException
	 *           
	 */
	public void update(QuestFileInfo entity, String strUpdateTime) throws DataChangeException, SQLException {
		LogUtil.log("EJB：签报申请附件数据更新操作开始", Level.INFO, null);
		try {
			QuestFileInfo objOld = new QuestFileInfo();
			objOld = this.findByPhyId(entity.getIdFile());
			String strLastmodifiedDate = objOld.getUpdateTimeFile().toString().substring(0,19);
			if (strLastmodifiedDate.equals(strUpdateTime.substring(0, 19))) {
				String strSql   = "UPDATE"
								+ " AD_J_OUT_QUEST_FILE "
								+ "SET"
								+ " APPLY_ID = ?,"
								+ " FILE_TYPE = ?,"
								+ " FILE_KIND = ?,"
								+ " FILE_NAME = ?,"
								+ " FILE_TEXT = ?,"
								+ " IS_USE = ?,"
								+ " UPDATE_USER = ?,"
								+ " UPDATE_TIME = sysdate "
								+ "WHERE"
								+ " IS_USE = ? AND"
								+ " ID = ?";
				Object[] params = new Object[9];
	            params[0] = entity.getApplyIdFile();
	            params[1] = entity.getFileTypeFile();
	            params[2] = entity.getFileKindFile();
	            params[3] = entity.getFileNameFile();
	            params[4] = entity.getFileTextFile();
	            params[5] = entity.getIsUseFile();
	            params[6] = entity.getUpdateUserFile();
	            params[7] = "Y";
	            params[8] = entity.getIdFile();
				
				LogUtil.log("EJB:签报申请附件数据更新操作开始。SQL：", Level.INFO, null);
				LogUtil.log(strSql, Level.INFO, null);
				bll.exeNativeSQL(strSql, params);
				LogUtil.log("EJB：签报申请附件数据更新操作正常结束", Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}
		} catch (DataChangeException e) {
			throw new DataChangeException("");
		} catch (Exception re) {
			LogUtil.log("EJB：签报申请附件数据更新操作异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findById(String strApply) throws SQLException {
		    PageObject objResult = new PageObject();
		try {
			QuestFileInfo objReaderInfo = null;
			String strSql   = "SELECT"
							+ " ID,"
							+ " APPLY_ID,"
							+ " FILE_TYPE,"
							+ " FILE_KIND,"
							+ " FILE_NAME,"
							+ " FILE_TEXT,"
							+ " IS_USE,"
							+ " UPDATE_USER,"
							+ " TO_CHAR(UPDATE_TIME,'YYYY-MM-DD HH24:MI:SS') "
							+ "FROM"
							+ " AD_J_OUT_QUEST_FILE "
							+ "WHERE"
							+ " IS_USE = ? AND"
							+ " APPLY_ID =?";
			
			String strSqlCount  = "SELECT (ID) "
								+ "FROM"
								+ " AD_J_OUT_QUEST_FILE "
								+ "WHERE"
								+ " IS_USE = ? AND"
								+ " APPLY_ID =?";
			
			Object[] params = new Object[2];
			params[0] = "Y";
			params[1] = strApply;
			
			LogUtil.log("EJB：签报申请附件数据根据ID查询操作开始。SQL：", Level.INFO, null);
			LogUtil.log(strSql, Level.INFO, null);
			List lstReaderList = bll.queryByNativeSQL(strSql, params);
			List<QuestFileInfo> arrQuery = new ArrayList<QuestFileInfo>();
			
			if (lstReaderList != null && lstReaderList.size() > 0) {
				Iterator it = lstReaderList.iterator();
				while (it.hasNext()) {
					objReaderInfo = new QuestFileInfo();
					Object[] data = (Object[]) it.next();
					objReaderInfo.setIdFile(Long.parseLong(data[0].toString()));
					if (data[1] != null) {
						objReaderInfo.setApplyIdFile(data[1].toString());
					}
					if (data[2] != null) {
						objReaderInfo.setFileTypeFile(data[2].toString());
					}
					if (data[3] != null) {
						objReaderInfo.setFileKindFile(data[3].toString());
					}
					if (data[4] != null) {
						objReaderInfo.setFileNameFile(data[4].toString());
					}
					if (data[5] != null) {
						objReaderInfo.setFileTextFile(data[5].toString()
								.getBytes());
					}
					if (data[6] != null) {
						objReaderInfo.setIsUseFile(data[6].toString());
					}
					if (data[7] != null) {
						objReaderInfo.setUpdateUserFile(data[7].toString());
					}
					if (data[8] != null) {
						objReaderInfo.setUpdateTimeFile(data[8].toString());
					}
					arrQuery.add(objReaderInfo);
				}
				if (arrQuery.size() > 0) {
					Long totalCount = Long.parseLong(bll.getSingal(strSqlCount,
							params).toString());
					objResult.setList(arrQuery);
					objResult.setTotalCount(totalCount);
				}
			}  else {
				Long lngZero = new Long(0);
				objResult.setTotalCount(lngZero);
				}
			    LogUtil.log("EJB：签报申请附件数据根据ID查询操作正常结束", Level.INFO, null);
				return objResult;
		} catch (Exception e) {
			LogUtil.log("EJB：签报申请附件数据根据ID查询操作异常结束", Level.SEVERE, e);
			throw new SQLException("");
		}
	}
	
@SuppressWarnings("unchecked")
public QuestFileInfo findByPhyId(Long lngFileId) throws SQLException {
		
		try {
			QuestFileInfo objReaderInfo = new QuestFileInfo();
			String strSql   = "SELECT"
							+ " ID,"
							+ " APPLY_ID,"
							+ " FILE_TYPE,"
							+ " FILE_KIND,"
							+ " FILE_NAME,"
							+ " FILE_TEXT,"
							+ " IS_USE,"
							+ " UPDATE_USER,"
							+ " TO_CHAR(UPDATE_TIME,'YYYY-MM-DD HH24:MI:SS') "
							+ "FROM"
							+ " AD_J_OUT_QUEST_FILE "
							+ "WHERE"
							+ " IS_USE = ? AND"
							+ " ID =?";
			
			Object[] params = new Object[2];
			params[0] = "Y";
			params[1] = lngFileId;
			
			LogUtil.log("EJB：签报申请附件数据根据物理ID查询操作开始。SQL：", Level.INFO, null);
			LogUtil.log(strSql, Level.INFO, null);
			List lstReaderList = bll.queryByNativeSQL(strSql, params);
			Iterator it = lstReaderList.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				objReaderInfo.setIdFile(Long.parseLong(data[0].toString()));
				if (data[1] != null) {
					objReaderInfo.setApplyIdFile(data[1].toString());
				}
				if (data[2] != null) {
					objReaderInfo.setFileTypeFile(data[2].toString());
				}
				if (data[3] != null) {
					objReaderInfo.setFileKindFile(data[3].toString());
				}
				if (data[4] != null) {
					objReaderInfo.setFileNameFile(data[4].toString());
				}
				if (data[5] != null) {
					objReaderInfo.setFileTextFile(data[5].toString().getBytes());
				}
				if (data[6] != null) {
					objReaderInfo.setIsUseFile(data[6].toString());
				}
				if (data[7] != null) {
					objReaderInfo.setUpdateUserFile(data[7].toString());
				}
				if (data[8] != null) {
					objReaderInfo.setUpdateTimeFile(data[8].toString());
				}
			}
			LogUtil.log("EJB：签报申请附件数据根据物理ID查询操作正常结束", Level.INFO, null);
			return objReaderInfo;
			
		} catch (Exception e) {
			LogUtil.log("EJB：签报申请附件数据根据物理ID查询操作异常结束", Level.SEVERE, e);
			throw new SQLException("");
		}
	}

	/**
	 *  查询所有申请附件
	 * @param strApplyId 申请单号
	 * @return 所有申请附件
	 * @throws SQLException 
	 */@SuppressWarnings("unchecked")
	public PageObject getAllQuestFile(String strApplyId, final int... rowStartIdxAndCount) throws SQLException {
		PageObject objResult = new PageObject();
		
		try {
			String strSql   = "SELECT"
				+ " ID,"
				+ " APPLY_ID,"
				+ " FILE_TYPE,"
				+ " FILE_KIND,"
				+ " FILE_NAME,"
				+ " FILE_TEXT,"
				+ " IS_USE,"
				+ " UPDATE_USER,"
				+ " TO_CHAR(UPDATE_TIME,'YYYY-MM-DD HH24:MI:SS') "
				+ "FROM"
				+ " AD_J_OUT_QUEST_FILE "
				+ "WHERE"
				+ " IS_USE = ? AND"
				+ " APPLY_ID =?";
			
			String strSqlCount  = "SELECT COUNT(ID)"
								+ "FROM"
								+ " AD_J_OUT_QUEST_FILE "
								+ "WHERE"
								+ " IS_USE = ? AND"
								+ " APPLY_ID =?";

			Object[] params = new Object[2];
			params[0] = "Y";
			params[1] = strApplyId;
			
			LogUtil.log("EJB：查询所有申请附件操作开始。SQL：", Level.INFO, null);
			LogUtil.log(strSql, Level.INFO, null);
			List lstQuery = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List<QuestFileInfo> arrQuery = new ArrayList<QuestFileInfo>();
			
			if (lstQuery != null && lstQuery.size() > 0) {
				Iterator it = lstQuery.iterator();
				QuestFileInfo objReaderInfo = null;
				while (it.hasNext()) {
					objReaderInfo = new QuestFileInfo();
					Object[] data = (Object[]) it.next();
					objReaderInfo.setIdFile(Long.parseLong(data[0].toString()));
					if (data[1] != null) {
						objReaderInfo.setApplyIdFile(data[1].toString());
					}
					if (data[2] != null) {
						objReaderInfo.setFileTypeFile(data[2].toString());
					}
					if (data[3] != null) {
						objReaderInfo.setFileKindFile(data[3].toString());
					}
					if (data[4] != null) {
						objReaderInfo.setFileNameFile(data[4].toString());
					}
					if (data[5] != null) {
						objReaderInfo.setFileTextFile(data[5].toString().getBytes());
					}
					if (data[6] != null) {
						objReaderInfo.setIsUseFile(data[6].toString());
					}
					if (data[7] != null) {
						objReaderInfo.setUpdateUserFile(data[7].toString());
					}
					if (data[8] != null) {
						objReaderInfo.setUpdateTimeFile(data[8].toString());
					}
					arrQuery.add(objReaderInfo);
				}
				if (arrQuery.size() > 0) {
					Long totalCount = Long.parseLong(bll.getSingal(strSqlCount, params)
							.toString());
					objResult.setList(arrQuery);
					objResult.setTotalCount(totalCount);
				}
			}  else {
				Long lngZero = new Long(0);
				objResult.setTotalCount(lngZero);
			}
			LogUtil.log("EJB:查询所有申请附件正常结束", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("EJB:查询所有申请附件异常结束", Level.SEVERE, e);
			throw new SQLException("");
		}
		
	    return objResult;
	}
	
}