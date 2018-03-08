package power.web.comm; 
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
@SuppressWarnings("serial")
public class UploadFileAbstractAction extends AbstractAction {  
	/**
	 * 上传文件,返回文件访问的web访问地址
	 * 参数均不能为空
	 * @param sourceFile 等上传的文件
	 * @param fileName   文件目录(如 合同.doc)
	 * @param childCatalogName 上传到服务器的子目录
	 * @return String
	 */
	public String uploadFile(File sourceFile,String fileName,String childCatalogName) { 
		if(sourceFile == null || fileName == null || "".equals(fileName.trim()) || childCatalogName == null||"".equals(childCatalogName.trim()))
		return null;
		String calluploadPath = null; 
		String uploadPath = session.getServletContext().getInitParameter("upload_dir"); 
		calluploadPath = session.getServletContext().getInitParameter("call_upload_dir");  
		this.createCatalog(uploadPath);
		uploadPath += "/"+childCatalogName +"/";
		calluploadPath += "/"+childCatalogName +"/";
		File f = new File(uploadPath);
		if (!f.exists()) {
			f.mkdir();
		}  
		long currentTime = (new Date()).getTime();
		fileName = currentTime + fileName;  
		String dstPath = uploadPath + fileName; 
		calluploadPath += fileName;
		File dstFile = new File(dstPath);
		upload(sourceFile, dstFile); 
		return calluploadPath;
	}
	/**
	 * 根据url创建目录
	 * @param path
	 */
	private void createCatalog(String path)
	{   
		File f = new File(path);
		if (f.exists())
			return;
		String[] ps = path.trim().split("/");
		if (ps.length < 2)
			return;
		String _path = ps[0];
		for (int i = 1; i < ps.length; i++) {
			_path += "\\" + ps[i];
			f = new File(_path);
			if (!f.exists()) {
				f.mkdir();
			}
		}
	}
	/**
	 * 上传处理
	 * @param src
	 * @param dst
	 */
	public void upload(File src, File dst) {
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
}
