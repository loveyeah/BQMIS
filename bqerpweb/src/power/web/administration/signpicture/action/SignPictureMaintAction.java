/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.signpicture.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJEmployeesignpicture;
import power.ejb.administration.AdJEmployeesignpictureFacadeRemote;
import power.ejb.administration.business.SignPictureFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 个性签名维护Action
 * 
 * @author jincong
 * @version 1.0
 */
public class SignPictureMaintAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 开始行 */
	private String start;
	/** 查询行 */
	private String limit;
	/** ID */
	private String id;
	/** 人员编码 */
	private String workerCode;
	/** 图片 */
	private File picSelect;
	/** 个性签名维护接口 */
	private SignPictureFacadeRemote maintRemote;
	/** 个性签名接口 */
	private AdJEmployeesignpictureFacadeRemote remote;

	/**
	 * 构造函数
	 */
	public SignPictureMaintAction() {
		// 取得远程接口
		maintRemote = (SignPictureFacadeRemote) factory
				.getFacadeRemote("SignPictureFacade");
		remote = (AdJEmployeesignpictureFacadeRemote) factory
				.getFacadeRemote("AdJEmployeesignpictureFacade");
	}

	/**
	 * 查询人员签名信息
	 */
	public void getPersonSignInfo() {
		try {
			LogUtil.log("Action:查询人员签名信息开始。", Level.INFO, null);
			//员工姓名
			String  queryName = request.getParameter("queryName");
			PageObject object = new PageObject();
			// 开始行
			if(start !=null && limit !=null){
				int intStart = Integer.parseInt(start);
				// 查询行
				int intLimit = Integer.parseInt(limit);
			
				// 查询
				object = maintRemote.findPersonSignInfo(queryName,employee
						.getEnterpriseCode(), intStart, intLimit);	
			}else{
				object = maintRemote.findPersonSignInfo(queryName,employee
						.getEnterpriseCode());
			}
			
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:查询人员签名信息结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:查询人员签名信息失败。", Level.SEVERE, e);
		} catch (SQLException e) {
			LogUtil.log("Action:查询人员签名信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 检查人员签名信息是否已存在
	 */
	public void getSignPictureRepeat() {
		try {
			LogUtil.log("Action:查询人员签名信息开始。", Level.INFO, null);
			PageObject object = maintRemote.findPictrueByCode(workerCode,
					employee.getEnterpriseCode());
			if (object.getList().size() > 0) {
				write("{success:true,msg:true}");
				LogUtil.log("Action:查询人员签名信息结束，人员已存在。", Level.INFO, null);
			} else {
				write("{success:true,msg:false}");
				LogUtil.log("Action:查询人员签名信息结束，人员不存在。", Level.INFO, null);
			}
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:查询人员签名信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 新增签名信息
	 */
	public void addSignPicture() {
		try {
			LogUtil.log("Action:保存人员签名信息开始。", Level.INFO, null);
			AdJEmployeesignpicture picture = new AdJEmployeesignpicture();
			if (picSelect != null) {
				if(!picSelect.exists()) {
					throw new FileNotFoundException();
				}
				byte[] data = null;
				java.io.FileInputStream fis = null;
				fis = new java.io.FileInputStream(picSelect);
				data = new byte[(int) fis.available()];
				fis.read(data);
				picture.setSignPic(data);
			}
			picture.setWorkerCode(workerCode);
			picture.setUpdateUser(employee.getWorkerCode());
			picture.setIsUse(Constants.IS_USE_Y);
			picture.setEnterpriseCode(employee.getEnterpriseCode());
			remote.save(picture);
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:保存人员签名信息结束。", Level.INFO, null);
		} catch (FileNotFoundException e) {
			write(Constants.FILE_NOT_EXIST);
			LogUtil.log("Action:保存人员签名信息失败。", Level.SEVERE, e);
		} catch (IOException e) {
			write(Constants.IO_FAILURE);
			LogUtil.log("Action:保存人员签名信息失败。", Level.SEVERE, e);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:保存人员签名信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 根据id查询图片
	 */
	public void getPersonSignPicture() {
		try {
			LogUtil.log("Action:查询图片信息开始。", Level.INFO, null);
			AdJEmployeesignpicture pictrue = maintRemote.findPictrueById(id);
			response.setContentType("image/jpeg");
			if (pictrue != null) {
				// 获得照片
				byte[] materialMap = pictrue.getSignPic();
				if (materialMap != null) {
					OutputStream outs = null;
					outs = response.getOutputStream();
					for (int i = 0; i < materialMap.length; i++) {
						outs.write(materialMap[i]);
					}
					// 输出到页面
					outs.flush();
					outs.close();
				}
			}
			LogUtil.log("Action:查询图片信息结束。", Level.INFO, null);
		} catch (IOException e) {
			LogUtil.log("Action:查询图片信息失败。", Level.SEVERE, e);
		} catch (SQLException e) {
			LogUtil.log("Action:查询图片信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 修改人员签名信息
	 */
	public void updateSignPicture() {
		try {
			LogUtil.log("Action:修改人员签名信息开始。", Level.INFO, null);
			AdJEmployeesignpicture picture = maintRemote.findPictrueById(id);
			if (picture != null) {
				if (picSelect != null) {
					if(!picSelect.exists()) {
						throw new FileNotFoundException();
					}
					byte[] data = null;
					java.io.FileInputStream fis = null;
					fis = new java.io.FileInputStream(picSelect);
					data = new byte[(int) fis.available()];
					fis.read(data);
					picture.setSignPic(data);
				}
				picture.setUpdateUser(employee.getWorkerCode());
				remote.update(picture);
				write(Constants.MODIFY_SUCCESS);
			}
			LogUtil.log("Action:修改人员签名信息结束。", Level.INFO, null);
		} catch (FileNotFoundException e) {
			write(Constants.FILE_NOT_EXIST);
			LogUtil.log("Action:修改人员签名信息失败。", Level.SEVERE, e);
		} catch (IOException e) {
			write(Constants.IO_FAILURE);
			LogUtil.log("Action:修改人员签名信息失败。", Level.SEVERE, e);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:修改人员签名信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 删除人员签名信息
	 */
	public void deleteSignPicture() {
		try {
			LogUtil.log("Action:删除人员签名信息开始。", Level.INFO, null);
			AdJEmployeesignpicture picture = maintRemote.findPictrueById(id);
			if (picture != null) {
				picture.setIsUse(Constants.IS_USE_N);
				picture.setUpdateUser(employee.getWorkerCode());
				remote.update(picture);
				write(Constants.DELETE_SUCCESS);
			}
			LogUtil.log("Action:删除人员签名信息结束。", Level.INFO, null);
		} catch (SQLException e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:删除人员签名信息失败。", Level.SEVERE, e);
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the workerCode
	 */
	public String getWorkerCode() {
		return workerCode;
	}

	/**
	 * @param workerCode
	 *            the workerCode to set
	 */
	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	/**
	 * @return the picSelect
	 */
	public File getPicSelect() {
		return picSelect;
	}

	/**
	 * @param picSelect
	 *            the picSelect to set
	 */
	public void setPicSelect(File picSelect) {
		this.picSelect = picSelect;
	}

}
