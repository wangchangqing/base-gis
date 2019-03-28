package com.king.base.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.king.base.execption.CommonException;


public class DataFormatUtil {
	private DataFormatUtil(){
		
	}
	/**
	 * 字符串边界转成list
	 * @param boundary
	 * @return
	 */
	public static List<Point2D.Double> stringBoundary2List(String boundary){
		try {
			List<Point2D.Double> points = new ArrayList<Point2D.Double>();
			for(String pp : boundary.split(",")){
				String[] split = pp.split(" ");
				try {
					points.add(new Point2D.Double(java.lang.Double.parseDouble(split[0]), java.lang.Double.parseDouble(split[1])));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return points;
		} catch (Exception e) {
			throw new CommonException(e);
		}
	}
	/**
	 * list边界转成String
	 * @param boundary
	 * @return
	 */
	public static String listBoundary2String(List<Point2D.Double> boundary){
		StringBuilder sb = new StringBuilder();
		for(Point2D.Double p : boundary){
			sb.append(p.getX()+" "+p.getY()+",");
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}
	/**
	 * 坐标抽稀
	 * @param boundary
	 * @param m 最多保留点数
	 * @return
	 */
	public static String vacuate(String boundary,int m){
		List<Point2D.Double> stringBoundary2List = DataFormatUtil.stringBoundary2List(boundary);
		List<Point2D.Double> newstringBoundary2List = new ArrayList<Point2D.Double>();
		if(stringBoundary2List.size()<m){
			for(int i = 0;i<stringBoundary2List.size();i++){
				newstringBoundary2List.add(new Point2D.Double(CommonUtil.decimal(stringBoundary2List.get(i).x,5), CommonUtil.decimal(stringBoundary2List.get(i).y,5)));
			}
		}else{
			int n = stringBoundary2List.size()/m;
			for(int i = 0;i<stringBoundary2List.size();i++){
				if(i%n==0){
					newstringBoundary2List.add(new Point2D.Double(CommonUtil.decimal(stringBoundary2List.get(i).x,5), CommonUtil.decimal(stringBoundary2List.get(i).y,5)));
				}
			}
		}
		return DataFormatUtil.listBoundary2String(newstringBoundary2List);
	}
}
