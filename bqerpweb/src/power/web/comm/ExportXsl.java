package power.web.comm;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import power.ejb.hr.LogUtil;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;

import java.util.List;
import java.util.logging.Level;
import java.net.URLEncoder;
import java.io.IOException;

public class ExportXsl {

	/** 文件名 */
	String strFileName = "";
	/** 表头 */
	List lstTitle = null;
	/** 行list */
	List lstRow = null;
	/** response */
	HttpServletResponse response = null;

	/**
	 * @return 表头
	 */
	public List getLstTitle() {
		return lstTitle;
	}

	/**
	 * @param 表头
	 *            设置表头
	 */
	public void setLstTitle(List lstTitle) {
		this.lstTitle = lstTitle;
	}

	/**
	 * @return 行list
	 */
	public List getLstRow() {
		return lstRow;
	}

	/**
	 * @param 行list
	 *            设置行list
	 */
	public void setLstRow(List lstRow) {
		this.lstRow = lstRow;
	}

	/**
	 * @return response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * @param response
	 *            设置response
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * @return 文件名
	 */
	public String getStrFileName() {
		return strFileName;
	}

	/**
	 * @param 文件名
	 *            设置文件名
	 */
	public void setStrFileName(String strFileName) {
		this.strFileName = strFileName;
	}

	/**
	 * 创建工作簿
	 * 
	 * @return
	 * @throws Exception
	 */
	public HSSFWorkbook createWorkbook() throws Exception {
		HSSFWorkbook xslWorkBook = new HSSFWorkbook();
		// 生成一个sheet
		HSSFSheet sheet = xslWorkBook.createSheet("Sheet");
		// 对sheet赋值
		setDetailValues(sheet, lstTitle, lstRow); 
		// 返回工作簿
		return xslWorkBook;
	}

	/**
	 * 创建excel文件
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public void createXsl() throws IOException, Exception {
		LogUtil.log("ExportXsl:生成EXCEL开始", Level.INFO, null);
		// 添加文件名后缀
		strFileName = strFileName + ".xls";
		HSSFWorkbook xslWorkBook = createWorkbook();
		try {
			// 将文件名统一为UTF-8编码
			String strName = URLEncoder.encode(strFileName, "UTF-8");
			// 设置编码方式为UTF-8
			response.setCharacterEncoding("utf-8");
			response.setContentType("charset=utf-8");

			// 设置响应头部参数
			response.setHeader("Content-Disposition", "attachment; filename="
					+ strName);
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			response.setContentType("application/vnd.ms-excel");
			// 获得输出流
			ServletOutputStream out = response.getOutputStream();
			// 写入内容
			xslWorkBook.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw e;
		}
		LogUtil.log("ExportXsl:生成EXCEL结束", Level.INFO, null);
	}

	/**
	 * 设表内容具体值
	 * 
	 * @param sheet
	 * @param lstHeader 表头
	 * @param lstRow 表行
	 * @throws Exception
	 */
	private void setDetailValues(HSSFSheet sheet, List lstHeader, List lstRow)
			throws Exception {
		int intStartRow = 0;
		List lstDtlRow = null;
		// 生成EXCEL行
		HSSFRow row = sheet.createRow(intStartRow);
		// 设一行cell的值
		setDtlCellValues(row, lstHeader);
		intStartRow++;
		for (int i = 0; i < lstRow.size(); i++) {
			// 生成EXCEL行
			row = sheet.createRow(intStartRow);
			lstDtlRow = (List) lstRow.get(i);
			// 设一行cell的值
			setDtlCellValues(row, lstDtlRow);
			intStartRow++;
		}
	}

	/**
	 * 设一行cell的值
	 * 
	 * @param row 一行
	 * @param lstRow 行list
	 * @throws Exception
	 */
	private void setDtlCellValues(HSSFRow row, List lstRow) throws Exception {
		HSSFCell cell = null;
		try {
			for (int i = 0; i < lstRow.size(); i++) {
				// 生成EXCEL单元格
				cell = row.createCell((short) i);
				//wzhyan 2010-10-15 加入jxls 包冲突注释下面一行
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				String cellValue = lstRow.get(i).toString();
				cell.setCellValue(new String(cellValue.getBytes("UTF-16"),
						"UTF-16"));
			}
		} catch (Exception e) {
			throw e;
		}

	}
}
