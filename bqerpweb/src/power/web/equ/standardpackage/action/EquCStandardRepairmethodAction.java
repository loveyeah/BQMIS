package power.web.equ.standardpackage.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.standardpackage.EquCStandardRepairmethod;
import power.ejb.equ.standardpackage.EquCStandardRepairmethodFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * @author derek
 * @since 2009-03-11 维修方案
 */
@SuppressWarnings("serial")
public class EquCStandardRepairmethodAction extends AbstractAction {

	private EquCStandardRepairmethodFacadeRemote remote;
	private EquCStandardRepairmethod baseInfo;
	private String ids;
	private int start;
	private int limit;

	private File repairmethodFile;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public EquCStandardRepairmethodAction() {
		remote = (EquCStandardRepairmethodFacadeRemote) factory
				.getFacadeRemote("EquCStandardRepairmethodFacade");
	}

	/**
	 * 增加新记录
	 * 
	 */
	public void saveEquCStandardRepairmethod() {
		try {
			String filePath = request.getParameter("filepath");
			SimpleDateFormat codeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			Date codevalue = new Date();
			baseInfo.setRepairmodeCode("RC" + codeFormat.format(codevalue));// 按时间设置code
			String backValue = null;
			if (filePath != null) {
				backValue = uploadefile(baseInfo.getRepairmodeCode(), filePath);
			}
			if (backValue != null) {
				if (("").equals(backValue))
					baseInfo.setRepairmethodFile(null);
				else
					baseInfo.setRepairmethodFile(backValue);
				baseInfo.setEnterprisecode(employee.getEnterpriseCode());
				remote.save(baseInfo);
				write("{success:true,msg:'添加成功'}");
			} else {
				write("{success:false,msg:'文件上传失败'}");
			}
		} catch (Exception e) {
			write("{success:false,msg:'保存失败'}");
		}
	}

	/**
	 * 变更记录
	 * 
	 */
	public void updateEquCStandardRepairmethod() {
		try {
			String filePath = request.getParameter("filepath");

			EquCStandardRepairmethod model = new EquCStandardRepairmethod();
			model = remote.findById(baseInfo.getId());
			String backValue = null;
			if (filePath != null) {
				backValue = uploadefile(model.getRepairmodeCode(), filePath);
			}
			if (backValue != null) {
				if (("").equals(backValue))
					baseInfo.setRepairmethodFile(model.getRepairmethodFile());
				else
					baseInfo.setRepairmethodFile(backValue);
				if (baseInfo.getOrderby() == null)
					baseInfo.setOrderby(model.getOrderby());
				baseInfo.setRepairmodeCode(model.getRepairmodeCode());
				baseInfo.setIfUse(model.getIfUse());
				baseInfo.setStatus(model.getStatus());
				baseInfo.setEnterprisecode(model.getEnterprisecode());
				remote.update(baseInfo);
				write("{success:true,msg:'更新成功'}");
			} else {
				write("{success:false,msg:'文件上传失败'}");
			}
		} catch (Exception e) {
			write("{success:false,msg:'更新失败'}");
		}
	}

	/**
	 * 删除记录
	 * 
	 */
	public void deleteEquCStandardRepairmethod() {
		try {
			if (remote.delete(ids))
				write("{success:true,msg:'删除成功'}");
			else
				write("{success:false,msg:'删除失败'}");
		} catch (Exception e) {
			write("{success:false,msg:'删除失败'}");
		}
	}

	/**
	 * 锁定记录
	 * 
	 */
	public void lockEquCStandardRepairmethod() {
		try {
			if (remote.lock(ids))
				write("{success:true,msg:'锁定成功'}");
			else
				write("{success:false,msg:'锁定失败'}");
		} catch (Exception e) {
			write("{success:false,msg:'锁定失败'}");
		}
	}

	/**
	 * 解锁记录
	 * 
	 */
	public void unlockEquCStandardRepairmethod() {
		try {
			if (remote.unlock(ids))
				write("{success:true,msg:'解锁成功'}");
			else
				write("{success:false,msg:'解锁失败'}");
		} catch (Exception e) {
			write("{success:false,msg:'解锁失败'}");
		}
	}

	/**
	 * 删除附件
	 * 
	 */
	public void deleteEquCStandardRepairmethodFile() {
		try {
			if (remote.deleteFile(ids))
				write("{success:true,msg:'附件删除成功'}");
			else
				write("{success:false,msg:'附件删除失败'}");
		} catch (Exception e) {
			write("{success:false,msg:'附件删除失败'}");
		}
	}

	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getEquCStandardRepairmethodList() throws JSONException {
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), start,
				limit);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void getEquCStandardRepairmethodListToUse() throws JSONException {
		PageObject obj = remote.findAllToUse(employee.getEnterpriseCode(),
				start, limit);
		List<EquCStandardRepairmethod> list = obj.getList();
		String str = "[";
		int i = 0;
		for (EquCStandardRepairmethod model : list) {
			i++;
			str += "[\"" + model.getRepairmethodName() + "\",\""
					+ model.getRepairmodeCode() + "\"]";
			if (i < list.size()) {
				str += ",";
			}
		}
		str += "]";
		write(str);
	}

	// ******************************************上传文件******************************************
	/**
	 * 文件上传 （src:原文件，dst：目标文件）
	 * 
	 * @param src
	 * @param dst
	 */
	private static void copy(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), 16 * 1024);
			out = new BufferedOutputStream(new FileOutputStream(dst), 16 * 1024);
			byte[] buffer = new byte[16 * 1024];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 上传文件
	 * 
	 */
	public String uploadefile(String mycode, String filePath) {
		String dstPath = "";
		if (!filePath.equals("")) {
			try {
				File myfile = new File(filePath);
				String uploadPath = session.getServletContext()
						.getInitParameter("upload_dir");
				File f = new File(uploadPath);
				if (!f.exists()) {
					f.mkdir();
				}
				f = new File(uploadPath + "//equStandard");
				if (!f.exists()) {
					f.mkdir();
				}
				String myfileName = myfile.getName();
				String fileType = myfileName.substring(myfileName
						.lastIndexOf("."));
				dstPath = uploadPath + "/equStandard/std" + mycode + fileType;
				File dstFile = new File(dstPath);
				copy(repairmethodFile, dstFile);
				String fileName = "std" + mycode + fileType;
				return fileName;
			} catch (Exception e) {
				return null;
			}
		} else {
			return dstPath;
		}
	}

	// ******************************************get/set变量方法******************************************
	/**
	 * @return the baseInfo
	 */
	public EquCStandardRepairmethod getBaseInfo() {
		return baseInfo;
	}

	/**
	 * @param baseInfo
	 *            the baseInfo to set
	 */
	public void setBaseInfo(EquCStandardRepairmethod baseInfo) {
		this.baseInfo = baseInfo;
	}

	/**
	 * @return the ids
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * @param ids
	 *            the ids to set
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the repairmethodFile
	 */
	public File getRepairmethodFile() {
		return repairmethodFile;
	}

	/**
	 * @param repairmethodFile
	 *            the repairmethodFile to set
	 */
	public void setRepairmethodFile(File repairmethodFile) {
		this.repairmethodFile = repairmethodFile;
	}
}
