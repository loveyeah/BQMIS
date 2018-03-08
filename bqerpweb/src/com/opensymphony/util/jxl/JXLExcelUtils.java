package com.opensymphony.util.jxl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import jxl.Cell;
import jxl.CellType;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.opensymphony.db.DBHelper;

public class JXLExcelUtils {
	public List<String[]> createData(String sql) {
		List<String[]> list = new ArrayList<String[]>();
		list = DBHelper.getList(sql);
		return list;
	}

	/**
	 * 判断是否指标
	 * 
	 * @param cellContent
	 * @return
	 */
	private boolean isItem(String cellContent) {
		boolean result = false;
		if (cellContent != null) {
			if (cellContent.indexOf("$(item)") != -1)
				result = true;
		}
		return result;
	}

	/**
	 * 取得指标值
	 * 
	 * @param cellContent
	 *            单元格内容
	 * @param dateStr
	 *            查询日期
	 * @return
	 */
	private Double getItemValue(String cellContent, String dateStr) {
		Double result = null;
		if (cellContent != null && cellContent.length() > 9) {
			char dateType = cellContent.charAt(7);
			int tableType = 3;
			String itemCode = cellContent.substring(cellContent.indexOf(" "))
					.trim();
			String sql = null;
			switch (dateType) {
			case 'R':
				sql = "select func_jhtj_report.getItemValue(?,?,to_date(?,'yyyy-MM-dd'),?) from dual";
				tableType = 3;
				break;
			case 'Y':
				sql = "select func_jhtj_report.getItemValue(?,?,to_date(?,'yyyy-MM-dd'),?) from dual";
				tableType = 4;
				break;
			case 'J':
				sql = "select func_jhtj_report.getItemValue(?,?,to_date(?,'yyyy-MM-dd'),?) from dual";
				tableType = 5;
				break;
			case 'N':
				tableType = 6;
				sql = "select func_jhtj_report.getItemValue(?,?,to_date(?,'yyyy-MM-dd'),?) from dual";
				break;
			}
			if (sql != null) {
				Object obj = DBHelper.getSingal(sql, new Object[] { itemCode,
						"hfdc", dateStr, tableType });
				if (obj != null) {
					try {
						result = Double.parseDouble(obj.toString());
					} catch (NumberFormatException exc) {

					}
				}
			}
		}
		return result;
	}

//	private void reComputeFormula(WritableSheet sheet) {
//		for (int rowIndex = 0; rowIndex < sheet.getRows(); rowIndex++) {// 行
//			for (int colIndex = 0; colIndex < sheet.getColumns(); colIndex++) {// 列
//				WritableCell cell = sheet.getWritableCell(colIndex, rowIndex);
//				if (cell.getType() == CellType.NUMBER_FORMULA
//						|| cell.getType() == CellType.STRING_FORMULA
//						|| cell.getType() == CellType.BOOLEAN_FORMULA
//						|| cell.getType() == CellType.DATE_FORMULA) {
//					cell.setCellFormat(cell.getCellFormat());
//				}
//			}
//		}
//	}

	/**
	 * 取得单元格样式
	 * 
	 * @param cell
	 * @return
	 */
	private WritableCellFormat getCellFormat(Cell cell) {
		WritableCellFormat format = null;
		format = new WritableCellFormat(); 
		try { 
			if (cell.getCellFormat() != null) {
				format = new WritableCellFormat(cell.getCellFormat());
			}
		} catch (Exception exc) {
			System.out.println("又是样式错了。");
		}
		return format;
	}

	/**
	 * 隐藏列
	 * 
	 * @param sheet
	 * @param hiddenCols
	 */
	private void setColumnsHidden(WritableSheet sheet, String hiddenCols) {
		if (hiddenCols != null && !hiddenCols.trim().equals("")) {
			String[] cols = hiddenCols.split(",");
			for (int i = 0; i < cols.length; i++) {
				int columnIndex = Integer.parseInt(cols[i].trim());
				sheet.setColumnView(columnIndex, 0);
			}
		}
	}

	/**
	 * 插入列表数据
	 * 
	 * @param sheet
	 * @param cell
	 * @param cellContent
	 * @return
	 */
	private int insertList(WritableSheet sheet, Cell cell, Properties args) {
		// 记录要删除的的首行
		int startRowIndex = cell.getRow();
		int rowIndex = startRowIndex;
		// 记录开始的列
		int startColumnIndex = cell.getColumn();
		// String sql = cell.getContents().replace("$(list)", "");
		String cellContent = cell.getContents();
		String sql = cellContent.substring(cellContent.indexOf("select"));
		String prior = cellContent.substring(0, cellContent.indexOf("select"));
		if (prior.length() > 10) {
			prior = prior.substring(9, prior.lastIndexOf(")"));
			sql += " and (" + args.getProperty(prior) + ")";
		}
		// System.out.println(sql);
		List<String[]> list = this.createData(sql);
		if (list != null) {
			// 取得样板行样式
			int columnCount = list.get(0).length;
			WritableCellFormat[] fs = new WritableCellFormat[columnCount];
			for (int j = 0; j < columnCount; j++) {
				fs[j] = this.getCellFormat(sheet.getCell(j + startColumnIndex,
						startRowIndex));
			}
			// 循环数据
			for (int i = 0; i < list.size(); i++) {
				String[] record = list.get(i);
				int insertRowIndex = startRowIndex + i + 1;
				sheet.insertRow(insertRowIndex);
				for (int j = 0; j < record.length; j++) {
					// 此处必须为cell.getRow()不能为rowIndex否则公式不能计算
					Cell _cell = sheet.getCell(j, cell.getRow());
					if (_cell.getType() == CellType.DATE) {
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						// 如果日期格式转换错误就当字符串处理
						try {
							this.insertOneCellData(sheet, j + startColumnIndex,
									insertRowIndex, formatter.parse(record[j]),
									fs[j]);
						} catch (ParseException e) {
							this.insertOneCellData(sheet, j + startColumnIndex,
									insertRowIndex, record[j], fs[j]);
						}
					} else if (_cell.getType() == CellType.NUMBER) {
						// 如果数字转换错误就当字符串处理
						try {
							this.insertOneCellData(sheet, j + startColumnIndex,
									insertRowIndex, Double
											.parseDouble(record[j]), fs[j]);
						} catch (NumberFormatException e) {
							this.insertOneCellData(sheet, j + startColumnIndex,
									insertRowIndex, record[j], fs[j]);
						}
					} else {
						this.insertOneCellData(sheet, j + startColumnIndex,
								insertRowIndex, record[j], fs[j]);
					}
				}
				rowIndex++;
			}
			sheet.removeRow(startRowIndex);
			sheet.removeRow(rowIndex);
			rowIndex--;
		}
		return rowIndex;
	} 
	
	public void changCellText(WritableSheet sheet,WritableCell  cell,Object data) 
	{
		if (data != null) {
			try {
				if (cell.getType() == CellType.LABEL) {
					Label l = (Label) cell;
					l.setString(data.toString());
				} else {
					WritableCellFormat format = new WritableCellFormat(cell
							.getCellFormat());
					Label label = new Label(cell.getColumn(), cell.getRow(),
							data.toString(), format);
					sheet.addCell(label);
				}
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 插入单元格数据
	 * 
	 * @param sheet
	 * @param col
	 * @param row
	 * @param data
	 */
	public void insertOneCellData(WritableSheet sheet, Integer col,
			Integer row, Object data, WritableCellFormat format) {
		try {
			if (data instanceof Double) {
				jxl.write.Number labelNF = new jxl.write.Number(col, row,
						(Double) data, format);
				sheet.addCell(labelNF);
			} else if (data instanceof Boolean) {
				jxl.write.Boolean labelB = new jxl.write.Boolean(col, row,
						(Boolean) data, format);
				sheet.addCell(labelB);
			} else if (data instanceof Date) {
				jxl.write.DateTime labelDT = new jxl.write.DateTime(col, row,
						(Date) data, format);
				sheet.addCell(labelDT);
			} else {
				Label label = new Label(col, row, data == null ? "" : data
						.toString(),format); 
				sheet.addCell(label);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Date formatDate(String dateStr) {
		Date date = null;
		SimpleDateFormat formatter = null;
		if (dateStr != null) {
			try {
				int len = dateStr.length();
				switch (len) {
				case 10:
					formatter = new SimpleDateFormat("yyyy-MM-dd");
					date = formatter.parse(dateStr);
					break;
				case 7:
					formatter = new SimpleDateFormat("yyyy-MM");
					date = formatter.parse(dateStr);
					break;
				case 4:
					formatter = new SimpleDateFormat("yyyy");
					break;
				case 13:
					formatter = new SimpleDateFormat("yyyy-MM-dd HH");
					date = formatter.parse(dateStr);
					break;
				case 16:
					formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					date = formatter.parse(dateStr);
					break;
				case 19:
					formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					date = formatter.parse(dateStr);
					break;
				}
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
		return date;
	}

	/**
	 * 创建excel文件
	 * 
	 * @param writableWorkbook
	 * @param args
	 */
	public void createExcelFileByTemplate(WritableWorkbook writableWorkbook,
			Properties args) {
		try {
			WritableSheet sheet = writableWorkbook.getSheet(0);
			for (int rowIndex = 0; rowIndex < sheet.getRows(); rowIndex++) {// 行
				for (int colIndex = 0; colIndex < sheet.getColumns(); colIndex++) {// 列
					Cell cell = sheet.getCell(colIndex, rowIndex);
					String cellContent = cell.getContents();
					WritableCellFormat format = getCellFormat(cell);
					//指标
					if (this.isItem(cellContent)) {
						Double val = this.getItemValue(cellContent, args
								.getProperty("dateStr"));
						this.insertOneCellData(sheet, colIndex, rowIndex, val,
								format);
					} 
					//标题
					else if (cellContent.indexOf("$(title)") != -1) {
						cellContent = cellContent.replace("$(title)", args.getProperty("title"));
						this.insertOneCellData(sheet, colIndex, rowIndex,
								cellContent, format);
					}
					//操作人
					else if (cellContent.indexOf("$(operator)") != -1) {
						cellContent = cellContent.replace("$(operator)", args
								.getProperty("workerCode"));
						this.insertOneCellData(sheet, colIndex, rowIndex,
								cellContent, format);
					} 
					//日期
					else if (cellContent.indexOf("$(dateStr)") != -1) {
						cellContent = cellContent.replace("$(dateStr)", args
								.getProperty("dateStr"));
						this.insertOneCellData(sheet, colIndex, rowIndex,
								cellContent, format);
					} 
					//循环列表
					else if (cellContent.indexOf("$(list)".toLowerCase()) != -1) {
						rowIndex = insertList(sheet, cell, args);
					}
				}
			}
			// this.reComputeFormula(sheet);
			this.setColumnsHidden(sheet, args.getProperty("hiddenCols"));
			System.out.println("------模板解析完成-------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开文件看看
	 * 
	 * @param exePath
	 * @param filePath
	 */
	public void openExcel(String exePath, String filePath) {
		Runtime r = Runtime.getRuntime();
		String cmd[] = { exePath, filePath };
		try {
			Process p = r.exec(cmd); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		JXLExcelUtils jxl = new JXLExcelUtils();
		String id = "90";
		String title = "开封京源发";
		String workerCode = "王伟年";
		String dateStr = "2009-08-03";
		String hiddenCols = "";
		hiddenCols = "";
		String strWhere = " rownum <5 and t.data_value = 0";
		// 封闭参数
		Properties arguments = new Properties();
		arguments.setProperty("id", id);
		arguments.setProperty("title", title);
		arguments.setProperty("dateStr", dateStr);
		arguments.setProperty("workerCode", workerCode);
		arguments.setProperty("hiddenCols", hiddenCols);
		arguments.setProperty("strWhere", strWhere);
		arguments.setProperty("strWhere2", " rownum <5 and t.data_value <> 0");
		try {
			InputStream is = jxl.getClass().getClassLoader()
					.getResourceAsStream("com/opensymphony/util/jxl/template.xls");
			WorkbookSettings setting = new WorkbookSettings();
			java.util.Locale locale = new java.util.Locale("zh", "CN");
			setting.setLocale(locale);
			setting.setEncoding("ISO-8859-1");
			Workbook fromWb = Workbook.getWorkbook(is, setting);
			is.close();
			File bos = new File("E:/workspace/kf/oscomm-1.0-dev-28Oct08/src/com/opensymphony/util/jxl/temp.xls");
			// ByteArrayOutputStream bos = new ByteArrayOutputStream();
			OutputStream out = new FileOutputStream(bos);
			WritableWorkbook toWb = Workbook.createWorkbook(out, fromWb);
			fromWb.close();
			jxl.createExcelFileByTemplate(toWb, arguments);
			toWb.write();
			toWb.close();
			jxl.openExcel(
					"C:/Program Files/Microsoft Office/Office12/EXCEL.EXE",
					"E:/workspace/kf/oscomm-1.0-dev-28Oct08/src/com/opensymphony/util/jxl/temp.xls");
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
