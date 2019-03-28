package com.king.base.algorithm;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.king.base.execption.CommonException;
import com.king.base.math.Polygon;
import com.king.base.util.DataFormatUtil;

/**
 * 经纬度坐标算法工具类
 */
public class Geography {
	/**
	 * 私有构造方法
	 */
	private Geography(){
		
	}
	/**
	 * 地球半径
	 */
	private static final double EARTH_RADIUS = 6378137;
	/**
	 * 每纬度所垮距离
	 */
	private static final double PER_LATI_LENGTH = Math.PI*EARTH_RADIUS*2/360;
	/**
	 * 获取每纬度所对应的距离
	 * @return
	 */
	public static double getPerLatiLength(){
		return PER_LATI_LENGTH;
	}
	/**
	 * 获取某一个纬度下每经度所对应的距离
	 * @param lati 纬度
	 * @return 每经度所对应的距离
	 */
	public static double getPerLongiLength(double lati){
		return Math.abs(PER_LATI_LENGTH * Math.cos(lati * Math.PI / 180));
	}
	/**
	 * 跨指定纬度对应的距离
	 * @param lati 跨纬度
	 * @return
	 */
	public static double latiLength(double lati){
		return PER_LATI_LENGTH*lati;
	}
	/**
	 * 某纬度下，跨指定经度距离
	 * @param lati
	 * @param longi 跨经度
	 * @return
	 */
	public static double longiLength(double lati,double longi){
		return getPerLongiLength(lati)*longi;
	}
	/**
	 * 跨指定距离对应的纬度
	 * @param length 跨距离
	 * @return
	 */
	public static double lengthLati(double length){
		return length/PER_LATI_LENGTH;
	}
	/**
	 * 某纬度下，跨指定距离经度
	 * @param lati
	 * @param length
	 * @return
	 */
	public static double lengthLongi(double lati,double length){
		return length/getPerLongiLength(lati);
	}
	/**
	 * 角度转换为弧度
	 * @param angle 角度
	 * @return 该角度所对应的弧度
	 */
	public static double toRadian(double angle){
		return BigDecimal.valueOf(Math.PI).divide(BigDecimal.valueOf(180),18,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(angle)).doubleValue();
	}
	/**
	 * 弧度转换为角度
	 * @param rad 弧度
	 * @return 角度
	 */
	public static double toAngle(double radian){
		return radian*180d/Math.PI;
	}
	/**
	 * 将经纬度点转换为直角坐标系点，单位米
	 * @param basePoint 原点
	 * @param point
	 * @return
	 */
	public static Point2D.Double longilati2Decare(Point2D.Double basePoint,Point2D.Double point){
		double x = (point.getX()-basePoint.getX())*getPerLongiLength(basePoint.getY());
		double y = (point.getY()-basePoint.getY())*PER_LATI_LENGTH;
		return new Point2D.Double(x, y);
	}
	/**
	 * 将经纬度点转换为直角坐标系点，单位米
	 * @param basePoint
	 * @param point
	 * @return
	 */
	public static Point2D.Double decare2Longilati(Point2D.Double basePoint,Point2D.Double point){
		double x = basePoint.x+point.x/getPerLongiLength(basePoint.y);
		double y = basePoint.y+point.y/PER_LATI_LENGTH;
		return new Point2D.Double(x, y);
	}
	/**
	 * 计算两个经纬度之间的距离
	 * @param longi1  经度1
	 * @param lati1  纬度1
	 * @param longi2  经度2
	 * @param lati2  纬度2
	 * @return 距离
	 */
	public static double calculateLength(double longi1,double lati1,double longi2,double lati2){
	    double er = EARTH_RADIUS; // 地球半径  
		double lat21 = lati1 * Math.PI / 180.0;  
	    double lat22 = lati2 * Math.PI / 180.0;  
	    double a = lat21 - lat22;  
	    double b = (longi1 - longi2) * Math.PI / 180.0;  
	    double sa2 = Math.sin(a / 2.0);  
	    double sb2 = Math.sin(b / 2.0);  
	    double d = 2  * er  * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat21)  * Math.cos(lat22) * sb2 * sb2));
	    return Math.abs(d);  
	}
	/**
	 * 计算两点之间的线与正北方向的夹角
	 * @param longi1  经度1
	 * @param lati1 纬度1
	 * @param longi2  经度2
	 * @param lati2  纬度2
	 * @return 与正北方向的夹角(角度),二三象限为负，一四象限为正
	 */
	public static double calculateAngle(double longi1, double lati1, double longi2,
			double lati2) {
		//原点
		if(Double.doubleToRawLongBits(longi2)==Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)==Double.doubleToRawLongBits(lati1)){
			throw new CommonException("same point");
		}
		//y正半轴
		if(Double.doubleToRawLongBits(longi2)==Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)>Double.doubleToRawLongBits(lati1)){
			return 0;
		}
		//y负半轴
		if(Double.doubleToRawLongBits(longi2)==Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)<Double.doubleToRawLongBits(lati1)){
			return 180;
		}
		//x正半轴
		if(Double.doubleToRawLongBits(longi2)>Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)==Double.doubleToRawLongBits(lati1)){
			return 90;
		}
		//x负半轴
		if(Double.doubleToRawLongBits(longi2)<Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)==Double.doubleToRawLongBits(lati1)){
			return 270;
		}
		double length1 = getPerLongiLength(lati2)*Math.abs(longi2-longi1);//x
		double length2 = PER_LATI_LENGTH*Math.abs(lati2-lati1);//y
		double radian = Math.atan(length2/length1);
		double angle = 90-toAngle(radian);
		//第一象限
		if(Double.doubleToRawLongBits(longi2)>Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)>Double.doubleToRawLongBits(lati1)){
			return angle;
		}
		//第二象限
		if(Double.doubleToRawLongBits(longi2)<Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)>Double.doubleToRawLongBits(lati1)){
			return 360-angle;
		}
		//第三象限
		if(Double.doubleToRawLongBits(longi2)<Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)<Double.doubleToRawLongBits(lati1)){
			return 180+angle;
		}
		//第四象限
		if(Double.doubleToRawLongBits(longi2)>Double.doubleToRawLongBits(longi1)&&Double.doubleToRawLongBits(lati2)<Double.doubleToRawLongBits(lati1)){
			return 180-angle;
		}
		throw new CommonException("no such situation");
	}
	
	/**
	 * 算出直线内的其他点
	 * @param point1
	 * @param point2
	 * @param length
	 * @return
	 */
	public static List<Point2D.Double> caculatePoints(Point2D.Double point1,Point2D.Double point2, double pointInterval) {
		double length = calculateLength(point1.getX(), point1.getY(), point2.getX(), point2.getY());
		int count = (int) (length/pointInterval);
		double latiLength = Geography.latiLength(Math.abs(point1.y-point2.y));
		double longiLength = Geography.longiLength(point1.y, Math.abs(point1.x-point2.x));
		double ylenth = pointInterval*latiLength/length;
		double xlenth = pointInterval*longiLength/length;
		List<Point2D.Double> points = new ArrayList<Point2D.Double>();
		points.add(point1);
		for(int i=1;i<=count;i++){
			double lengthLati = Geography.lengthLati(ylenth*i);
			double lengthLongi = Geography.lengthLongi(point1.y, xlenth*i);
			double longi = 0D;
			double lati = 0D;
			if(point2.y>=point1.y){
				lati = point1.y+lengthLati;
			}else{
				lati = point1.y-lengthLati;
			}
			if(point2.x>=point1.x){
				longi = point1.x+lengthLongi;
			}else{
				longi = point1.x-lengthLongi;
			}
			points.add(new Point2D.Double(longi, lati));
		}
		points.add(point2);
		return points;
	}
	/**
	 * 计算出一个多边形内部的一些点
	 * @param boundary 多边形边界点
	 * @param pointInterval 每个点之间的间隔 m
	 * @return
	 */
	public static List<Point2D.Double> caculateMorePoints(List<Point2D.Double> boundary,double pointInterval){
		Polygon polygon = new Polygon(boundary);
		//找出包含多边形的矩形的四个端点
		double maxLongi = 0d;
		double minLongi = Double.MAX_VALUE;
		double maxLati = 0d;
		double minLati = Double.MAX_VALUE;
		for(Point2D.Double point : boundary){
			if(point.getX()<minLongi){
				minLongi = point.getX();
			}
			if(point.getX()>maxLongi){
				maxLongi = point.getX();
			}
			if(point.getY()<minLati){
				minLati = point.getY();
			}
			if(point.getY()>maxLati){
				maxLati = point.getY();
			}
		}
		//划分网格点 左上角顺时针
		Point2D.Double point1 = new Point2D.Double(minLongi, maxLati);
		Point2D.Double point2 = new Point2D.Double(maxLongi, maxLati);
		Point2D.Double point3 = new Point2D.Double(maxLongi, minLati);
		Point2D.Double point4 = new Point2D.Double(minLongi, minLati);
		//计算出网格点
		List<Point2D.Double> points = new ArrayList<Point2D.Double>();
		List<Point2D.Double> caculatePoints1 = Geography.caculatePoints(point1, point4, pointInterval);
		List<Point2D.Double> caculatePoints2 = Geography.caculatePoints(point2, point3, pointInterval);
		for(int i = 0;i<caculatePoints1.size();i++){
			List<Point2D.Double> caculatePoints = Geography.caculatePoints(caculatePoints1.get(i), caculatePoints2.get(i), pointInterval);
			points.addAll(caculatePoints);
		}
		//筛选出在多边形内的点
		List<Point2D.Double> ppoints = new ArrayList<Point2D.Double>();
		for(Point2D.Double p : points){
			if(polygon.contains(p)){
				ppoints.add(p);
			}
		}
		ppoints.addAll(boundary);
		return ppoints;
	}
	/**
	 * 计算最小网格id
	 * @param gpsX 经度
	 * @param gpsY 纬度
	 * @return
	 */
	public static int findMinGrid(double gpsX, double gpsY) {
		int meshid = findMesh(gpsX, gpsY);

		int yy = meshid / 10000;
		int xx = (meshid - yy * 10000) / 100;
		int y = (meshid - yy * 10000 - xx * 100) / 10;
		int x = meshid - yy * 10000 - xx * 100 - y * 10;

		double miny = yy * 40.0D / 60.0D + y * 5.0D / 60.0D;
		double maxy = yy * 40.0D / 60.0D + (y + 1) * 5.0D / 60.0D;
		double minx = xx + 60 + x * 7.5D / 60.0D;
		double maxx = xx + 60 + (x + 1) * 7.5D / 60.0D;

		double spanx = maxx - minx;
		double spany = maxy - miny;

		double xCellDelta4 = spanx / 2.0D;
		double yCellDelta4 = spany / 2.0D;

		int x4 = (int) ((gpsX - minx) / xCellDelta4);
		int y4 = (int) ((gpsY - miny) / yCellDelta4);

		double minx4 = minx + x4 * xCellDelta4;
		double miny4 = miny + y4 * yCellDelta4;

		double xCellDelta16 = spanx / 4.0D;
		double yCellDelta16 = spany / 4.0D;

		int x16 = (int) ((gpsX - minx4) / xCellDelta16);
		int y16 = (int) ((gpsY - miny4) / yCellDelta16);

		int no4 = x4 * 2 + y4;
		int no16 = x16 * 2 + y16;

		return meshid * 100 + no4 * 10 + no16;
	}
	/**
	 * 计算mapid
	 * @param lon 经度
	 * @param lat 纬度
	 * @return
	 */
	public static int findMesh(double lon, double lat) {
		int xx = (int) (lon - 60.0D);
		int yy = (int) (lat * 60.0D / 40.0D);
		int x = (int) ((lon - (int) lon) / 0.125D);
		int y = (int) ((lat * 60.0D / 40.0D - yy) / 0.125D);
		return yy * 10000 + xx * 100 + y * 10 + x;
	}
	/**
	 * 经纬度抽稀
	 * @param boundary
	 * @return
	 */
	public static String vacuate(String boundary){
		List<Point2D.Double> stringBoundary2List = DataFormatUtil.stringBoundary2List(boundary);
		List<Point2D.Double> newstringBoundary2List = new ArrayList<Point2D.Double>();
		int m = 100;//抽稀倍数
		if(stringBoundary2List.size()<m){
			for(int i = 0;i<stringBoundary2List.size();i++){
				newstringBoundary2List.add(new Point2D.Double(decimal(stringBoundary2List.get(i).x), decimal(stringBoundary2List.get(i).y)));
			}
		}else{
			int n = stringBoundary2List.size()/m;
			for(int i = 0;i<stringBoundary2List.size();i++){
				if(i%n==0){
					newstringBoundary2List.add(new Point2D.Double(decimal(stringBoundary2List.get(i).x), decimal(stringBoundary2List.get(i).y)));
				}
			}
		}
		return DataFormatUtil.listBoundary2String(newstringBoundary2List);
	}
	private static double decimal(double num){
		return BigDecimal.valueOf(num).divide(BigDecimal.valueOf(1), 5, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 找出多边形内切的外围矩形
	 * @param boundary
	 * @return longimin,latimin,longimax,latimax
	 */
	public static double[] outRectangle(List<Point2D.Double> boundary){
		//找出包含多边形的矩形的四个端点
		double maxLongi = 0d;
		double minLongi = Double.MAX_VALUE;
		double maxLati = 0d;
		double minLati = Double.MAX_VALUE;
		for(Point2D.Double point : boundary){
			if(point.getX()<minLongi){
				minLongi = point.getX();
			}
			if(point.getX()>maxLongi){
				maxLongi = point.getX();
			}
			if(point.getY()<minLati){
				minLati = point.getY();
			}
			if(point.getY()>maxLati){
				maxLati = point.getY();
			}
		}
	   return new double[]{minLongi,minLati,maxLongi,maxLati};
	}
	/**
	 * 找出多边形内切的外围矩形
	 * @param boundary
	 * @return longimin,latimin,longimax,latimax
	 */
	public static double[] outRectangle(String boundary){
		return outRectangle(DataFormatUtil.stringBoundary2List(boundary));
	}
}
