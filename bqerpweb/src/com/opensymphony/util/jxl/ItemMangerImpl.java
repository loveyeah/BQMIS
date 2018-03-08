package com.opensymphony.util.jxl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.opensymphony.db.DBHelper; 

public class ItemMangerImpl implements ItemManger{
    private String date;
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String queryMV(String itemCode) {
		try{
			String sql = "select a.data_value from bp_j_stat_ytz a where a.item_code= ? and to_char(a.data_date,'yyyy-MM-dd')=?";
			Object[] params = new Object[]{itemCode,this.date};
			Object v = DBHelper.getSingal(sql, params); 
			if(v == null) v="-";
			return String.valueOf(v);
		}catch(Exception exc)
		{
			return "";
		}
	}

	
	public String querYV(String itemCode) {
		try{
			String sql = "select a.data_value from bp_j_stat_ntz a where a.item_code= ? and to_char(a.data_date,'yyyy-MM-dd')=?";
			Object[] params = new Object[]{itemCode,this.date};
			Object v = DBHelper.getSingal(sql, params);
			if(v == null) v="";
			return String.valueOf(v);
		}catch(Exception exc)
		{
			return "";
		}
	}
	
	public String queryMV(String itemCode, String date) {
		try{
			String sql = "select a.data_value from bp_j_stat_ytz a where a.item_code= ? and to_char(a.data_date,'yyyy-MM-dd')=?";
			Object[] params = new Object[]{itemCode,date};
			Object v = DBHelper.getSingal(sql, params); 
			if(v == null) v="";
			return String.valueOf(v);
		}catch(Exception exc)
		{
			return "";
		}
	}

	
	public String querYV(String itemCode, String date) {
		try{
		String sql = "select a.data_value from bp_j_stat_ntz a where a.item_code= ? and to_char(a.data_date,'yyyy-MM-dd')=?";
		Object[] params = new Object[]{itemCode,date};
		Object v = DBHelper.getSingal(sql, params);
		if(v == null) v="";
		return String.valueOf(v);
		}catch(Exception exc)
		{
			return "";
		}
	}
	
	public String formatD(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date d = format.parse(date);
			SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
			return f.format(d);
		}catch(ParseException e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	public String formatM(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		try{
			Date d = format.parse(date);
			SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月");
			return f.format(d);
		}catch(ParseException e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
}
