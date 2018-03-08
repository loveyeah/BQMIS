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

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.standardpackage.EquCStandardStepdocuments;
import power.ejb.equ.standardpackage.EquCStandardStepdocumentsFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EquCStandardStepdocumentsAction extends AbstractAction {

	private EquCStandardStepdocumentsFacadeRemote remote;
	private EquCStandardStepdocuments baseInfo;
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
	public EquCStandardStepdocumentsAction() {
		remote = (EquCStandardStepdocumentsFacadeRemote) factory
				.getFacadeRemote("EquCStandardStepdocumentsFacade");
	}

	/**
	 * 增加新记录
	 * 
	 */

	public void saveEquCStandardStepdocuments() {
		try {
			
			String filePath = new String(request.getParameter("filePath")
					.getBytes("iso-8859-1"), ("gb2312"));
			String woCode=request.getParameter("woCode");
			baseInfo.setWoCode(woCode);
			SimpleDateFormat codeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			Date codevalue = new Date();
			baseInfo.setFileCode("RC" + codeFormat.format(codevalue));// 按时间设置code
			
			String backValue = uploadefile(baseInfo.getFileCode(),
					filePath);
			if (backValue != null) {
				if (("").equals(backValue))
					baseInfo.setFilePath(null);
				else
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
	public void updateEquCStandardStepdocuments() {
		try {
			String filePath = new String(request.getParameter("filePath")
					.getBytes("iso-8859-1"), ("gb2312"));
			String id=request.getParameter("id");
			
			EquCStandardStepdocuments model=remote.findById(Long.parseLong(id));
			SimpleDateFormat codeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			Date codevalue = new Date();
			model.setFileCode("RC" + codeFormat.format(codevalue));// 按时间设置code
			
			if(filePath!=null){
			String backValue = uploadefile(model.getFileCode(),
					filePath);
			model.setFilePath(backValue);
			}
			model.setFileName(baseInfo.getFileName());
			model.setRelateFile(baseInfo.getRelateFile());	
			remote.update(model);
			write("{success:true,msg:'保存成功'}");
		} catch (Exception e) {
			write("{success:false,msg:'保存失败'}");
		}
	}

	/**
	 * 删除记录
	 * 
	 */
	public void deleteEquCStandardStepdocuments() {
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
	public void deleteEquCStandardStepdocumentsFiles() {
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
	public void getEquCStandardStepdocumentsList() throws JSONException {
		String woCode = request.getParameter("code");
//		String opCode = request.getParameter("opCode");
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), woCode,
				 start, limit);
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
				if(fileType.equals(".txt"))
				{
					fileType=".doc";
				}
				
				dstPath = uploadPath 
				+ "/standard/std" + mycode + fileType;
				File dstFile = new File(dstPath);				
				copy(uploadFilePath, dstFile);
				return ("std"+mycode + fileType);
			} catch (Exception e) {
				return null;
			}
		} else {
			return dstPath;
		}
	}

	public EquCStandardStepdocuments getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(EquCStandardStepdocuments baseInfo) {
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
