package power.web.equ.workbill.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import power.web.comm.AbstractAction;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;

import power.ejb.equ.workbill.EquJStepdocuments;
import power.ejb.equ.workbill.EquJStepdocumentsFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

@SuppressWarnings("serial")
public class EquJRelatedFilePackageAction extends UploadFileAbstractAction {

	private EquJStepdocumentsFacadeRemote remote;
	private EquJStepdocuments baseInfo;
	private String ids;
	private String ids2;

	private int start;
	private int limit;
	private File uploadFilePath;

	/**
	 * 
	 * 构造方法
	 * 
	 */
	public EquJRelatedFilePackageAction() {
		remote = (EquJStepdocumentsFacadeRemote) factory
				.getFacadeRemote("EquJStepdocumentsFacade");
	}

	/**
	 * 增加新记录
	 * 
	 */

	public void saveEquJStepdocuments() {
		try {

			String filePath = new String(request.getParameter("filePath"));
			String woCode = request.getParameter("woCode");
			baseInfo.setWoCode(woCode);
			SimpleDateFormat codeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			Date codevalue = new Date();

			baseInfo.setFileCode("RC" + codeFormat.format(codevalue));// 按时间设置code

			String backValue = uploadefile(baseInfo.getFileCode(), filePath);
			// update by ltong 2
			// if (filePath != null && !filePath.equals("")) {
			if (backValue != null && !backValue.equals("")) {
				// String result
				// =filePath.substring(filePath.lastIndexOf("\\")+1);
				// String fileName = result.replaceAll(" ","");
				// String[] filetemp = fileName.split("\\.");
				// if (filetemp[1].equals("txt")) {
				// filetemp[1] = ".doc";
				// fileName = filetemp[0] + filetemp[1];
				// }
				// String Temp = uploadFile(uploadFilePath, fileName,
				// "standard");
				// baseInfo.setFilePath(Temp);
				baseInfo.setFilePath(backValue);
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
	public void updateEquJStepdocuments() {
		try {
			String filePath = new String(request.getParameter("filePath"));
			String id = request.getParameter("id");
			String fileName = request.getParameter("fileName");
			String relateFile = request.getParameter("relateFile");

			EquJStepdocuments model = remote.findById(Long.parseLong(id));
			SimpleDateFormat codeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			Date codevalue = new Date();
			model.setFileCode("RC" + codeFormat.format(codevalue));// 按时间设置code

			// if (filePath != null) {
			// String result = filePath
			// .substring(filePath.lastIndexOf("\\") + 1);
			// if (model.getFilePath() != null
			// && result.equals(model.getFilePath().substring(
			// model.getFilePath().lastIndexOf("/") + 1))) {
			// ;
			// // model.setFilePath(result);
			// } else {
			// String fileName = result.replaceAll(" ", "");
			// String[] filetemp = fileName.split("\\.");
			// if (filetemp[1].equals("txt")) {
			// filetemp[1] = ".doc";
			// fileName = filetemp[0] + filetemp[1];
			// }
			if (filePath != null) {
				String backValue = uploadefile(model.getFileCode(), filePath);
				model.setFilePath(backValue);
			}
			model.setEnterprisecode(employee.getEnterpriseCode());
			model.setFileName(fileName);
			model.setRelateFile(relateFile);
			remote.update(model);
			write("{success:true,msg:'添加成功'}");
		} catch (Exception e) {
			write("{success:false,msg:'保存失败'}");
		}
	}

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
				f = new File(uploadPath + "//standard");
				if (!f.exists()) {
					f.mkdir();
				}
				String myfileName = myfile.getName();
				String fileType = myfileName.substring(myfileName
						.lastIndexOf("."));
				if (fileType.equals(".txt")) {
					fileType = ".doc";
				}

				dstPath = uploadPath + "/standard/std" + mycode + fileType;
				File dstFile = new File(dstPath);
				copy(uploadFilePath, dstFile);
				return ("std" + mycode + fileType);
			} catch (Exception e) {
				return null;
			}
		} else {
			return dstPath;
		}
	}

	/**
	 * 删除记录
	 * 
	 */
	public void deleteEquJStepdocuments() {
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
	 * 删除记录的文档
	 * 
	 */
	public void deleteEquJStepdocumentsFiles() {
		try {
			if (remote.deleteFiles(ids2))
				write("{success:true,msg:'删除成功'}");
			else
				write("{success:false,msg:'删除失败'}");
		} catch (Exception e) {
			write("{success:false,msg:'删除失败'}");
		}
	}

	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */
	public void getEquJStepdocumentsList() throws JSONException {
		String woCode = request.getParameter("code");
		// String opCode = request.getParameter("opCode");
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), woCode,
				start, limit);
		write(JSONUtil.serialize(obj));
	}

	public void getEquCStepdocumentsList() throws JSONException {
		String woCode = request.getParameter("code");
		// String opCode = request.getParameter("opCode");
		PageObject obj = remote.getEquCStepdocumentsList(employee
				.getEnterpriseCode(), woCode, start, limit);
		write(JSONUtil.serialize(obj));
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
	// public String uploadefile(String mycode, String filePath) {
	// String dstPath = "";
	// if (!filePath.equals("")) {
	// try {
	// File myfile = new File(filePath);
	// String uploadPath = session.getServletContext()
	// .getInitParameter("upload_dir");
	//
	// File f = new File(uploadPath);
	// if (!f.exists()) {
	// f.mkdir();
	// }
	// f = new File(uploadPath + "//standard");
	// if (!f.exists()) {
	// f.mkdir();
	// }
	// String myfileName = myfile.getName();
	// String fileType = myfileName.substring(myfileName
	// .lastIndexOf("."));
	// if(fileType.equals(".txt"))
	// {
	// fileType=".doc";
	// }
	//				
	// dstPath = uploadPath + "/standard/std" + mycode + fileType;
	// File dstFile = new File(dstPath);
	// copy(uploadFilePath, dstFile);
	// return ("std"+mycode + fileType);
	// } catch (Exception e) {
	// return null;
	// }
	// } else {
	// return dstPath;
	// }
	// }
	public EquJStepdocuments getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(EquJStepdocuments baseInfo) {
		this.baseInfo = baseInfo;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getIds2() {
		return ids2;
	}

	public void setIds2(String ids2) {
		this.ids2 = ids2;
	}

	public File getUploadFilePath() {
		return uploadFilePath;
	}

	public void setUploadFilePath(File uploadFilePath) {
		this.uploadFilePath = uploadFilePath;
	}

}
