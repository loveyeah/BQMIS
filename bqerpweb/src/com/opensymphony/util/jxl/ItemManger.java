package com.opensymphony.util.jxl;

public interface ItemManger {
	public String formatM(String date);
	public String formatD(String date);
	public void setDate(String date) ;
	/**
	 * 查询指标月数据
	 * @param itemCode 指标编码
	 * @param date 查询日期 如 2010-10-10
	 * @return String
	 */
	public String queryMV(String itemCode,String date);
	/**
	 * 查询指标年数据
	 * @param itemCode 指标编码
	 * @param date 查询日期 如 2010-10-10
	 * @return String
	 */
	public String querYV(String itemCode,String date);
	/**
	 * 查询指标月数据
	 * @param itemCode 指标编码
	 * @param date 查询日期 如 2010-10-10
	 * @return String
	 */
	public String queryMV(String itemCode);
	/**
	 * 查询指标年数据
	 * @param itemCode 指标编码
	 * @param date 查询日期 如 2010-10-10
	 * @return String
	 */
	public String querYV(String itemCode);
	
	
}
