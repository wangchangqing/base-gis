package com.king.base.math;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import com.king.base.algorithm.Geography;
import com.king.base.util.CommonUtil;
import com.king.base.util.DataFormatUtil;
import com.king.base.util.StringUtil;


/**
 * 几何模型 多边形
 */
public class Polygon {
	private Point2D.Double peakPoint;
	private GeneralPath generalPath;
	private List<Point2D.Double> boundary;
	private List<Point2D.Double> boundaryConvert;
	/**
	 * 构造方法
	 */
	public Polygon() {
		super();
	}
	/**
	 * 构造方法
	 * @param boundary 边界点（经纬度）
	 */
	public Polygon(List<Point2D.Double> boundary) {
		super();
		init(boundary);
	}
	/**
	 * 构造方法
	 * @param boundary 边界点（经纬度）
	 */
	public Polygon(String boundary) {
		this(DataFormatUtil.stringBoundary2List(boundary));
	}
	private void init(List<Point2D.Double> boundary) {
		this.boundary = boundary;
		List<Point2D.Double> points = new ArrayList<Point2D.Double>();
		int i = 0;
		for(Point2D.Double point : boundary){
			i++;
			if(i==1){
				this.peakPoint = point;
			}
			points.add(Geography.longilati2Decare(peakPoint, point));
		}
		this.boundaryConvert =points;
		drawMyself();
	}
	public Polygon(Circle circle){
		List<Point2D.Double> points = new ArrayList<Point2D.Double>();
		for(int i = 0 ; i < 360 ; i++){
			double val = 111319.55 * Math.cos(circle.getP().y * Math.PI / 180);
			double lon = circle.getP().x + (circle.getR() * Math.sin(i * Math.PI / 180)) / val;
			double lat = circle.getP().y + (circle.getR() * Math.cos(i * Math.PI / 180))/ 111133.33;
			points.add(new Point2D.Double(lon, lat));
		}
		init(points);
	}
	public void setPeakPoint(Point2D.Double peakPoint) {
		this.peakPoint = peakPoint;
	}

	public Point2D.Double getPeakPoint() {
		return peakPoint;
	}
	
	public GeneralPath getGeneralPath() {
		return generalPath;
	}

	public List<Point2D.Double> getBoundary() {
		return boundary;
	}
	/**
	 * 设置多边形边界点
	 * @param boundary 边界点（经纬度）
	 */
	public void setBoundary(List<Point2D.Double> boundary) {
		init(boundary);
	}
	
	public List<Point2D.Double> getBoundaryConvert() {
		return boundaryConvert;
	}
	/**
	 * 构建多边形
	 */
	public void drawMyself(){
		GeneralPath p = new GeneralPath();  
		
        Point2D.Double first = boundaryConvert.get(0);  
        p.moveTo(first.x, first.y);  
 
        for(int i=1;i<boundaryConvert.size();i++){
     	   p.lineTo(boundaryConvert.get(i).x, boundaryConvert.get(i).y); 
        }
        p.lineTo(first.x, first.y);  

        p.closePath();
        
        this.generalPath = p;
	}
	/**
	 * 计算面积
	 * @return 面积（平方米）
	 */
	public double getArea(){
	   return Math.abs(getSignedArea());
	}
	/**
	 * 计算有向面积
	 * @return 面积（平方米）
	 */
	public double getSignedArea(){
	   //S = 0.5 * ( (x0*y1-x1*y0) + (x1*y2-x2*y1) + ... + (xn*y0-x0*yn) )
	   double area = 0.00;
	   for(int i = 0;i<boundaryConvert.size();i++){
	    if(i<boundaryConvert.size()-1){
	    	Point2D.Double p1 = boundaryConvert.get(i);
	    	Point2D.Double p2 = boundaryConvert.get(i+1);
	    	area += p1.getX()*p2.getY() - p2.getX()*p1.getY();
	    }else{
	    	Point2D.Double pn = boundaryConvert.get(i);
	    	Point2D.Double p0 = boundaryConvert.get(0);
	    	area += pn.getX()*p0.getY()- p0.getX()*pn.getY();
	    }
	   }
	   area = area/2.00;
	   return area;
	}
	/**
	 * 内部随机点
	 * @return 随机点
	 */
	public Point2D.Double randomPoint(){
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
		//随机一个在里面的点
		while(true){
			Point2D.Double randomPoint = nextRandomPoint(maxLongi, minLongi,maxLati, minLati);
			if(randomPoint!=null&&contains(randomPoint)){
				return randomPoint;
			}
		}
	}
	/**
	 * 内部随机点
	 * @return 随机点
	 */
	public Point2D.Double randomPoint(double seedLongi,double seedLati){
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
		//随机一个在里面的点
		while(true){
			Point2D.Double randomPoint = nextRandomPoint(seedLongi,seedLati,maxLongi, minLongi,maxLati, minLati);
			if(randomPoint!=null&&contains(randomPoint)){
				return randomPoint;
			}
		}
	}
	/**
	 * 随机生成一个矩形内的点
	 * @param maxLongi 最大经度
	 * @param minLongi 最小经度
	 * @param maxLati  最大纬度
	 * @param minLati  最小纬度
	 * @return 矩形内的随机点
	 */
	private Point2D.Double nextRandomPoint(double maxLongi, double minLongi,
			double maxLati, double minLati) {
		int a = 100000000;
		int r1 = Math.abs((int)((maxLongi-minLongi)*a));
		int r2 = Math.abs((int)((maxLati-minLati)*a));
		Random randomLongi = new Random();
		Random randomLati = new Random();
		int nextInt1 = randomLongi.nextInt(r1);
		int nextInt2 = randomLati.nextInt(r2);
		double nextLongi = Double.parseDouble(nextInt1+".0")/a;
		double nextLati = Double.parseDouble(nextInt2+".0")/a;
		return new Point2D.Double(minLongi+nextLongi, minLati+nextLati);
	}
	/**
	 * 随机生成一个矩形内的点 靠近种子点分布
	 * @param seedLongi 种子点经度
	 * @param seedLati	种子点纬度
	 * @param maxLongi 最大经度
	 * @param minLongi 最小经度
	 * @param maxLati  最大纬度
	 * @param minLati  最小纬度
	 * @return
	 */
	public Point2D.Double nextRandomPoint(double seedLongi,double seedLati,double maxLongi, double minLongi,
			double maxLati, double minLati) {
		java.awt.geom.Point2D.Double point = nextRandomPoint(maxLongi, minLongi, maxLati, minLati);
		double maxLength = Geography.calculateLength(maxLongi, maxLati, minLongi, minLati);
		double length = Geography.calculateLength(seedLongi, seedLati, point.x, point.y);
		double df = 1/maxLength;//权重衰减因子，每米点的密度衰减
		Random random = new Random();
		int nextInt = random.nextInt(10);
		if(nextInt<(1-length*df)*10){
			return point;
		}else{
			return null;
		}
	}
	/**
	 * 是否包含一个点
	 * @param point 点
	 * @return 是否包含
	 */
	public boolean contains(Point2D.Double point){
		Point2D.Double convertCoord = Geography.longilati2Decare(peakPoint, point);
		return this.getGeneralPath().contains(convertCoord);
	}
	/**
	 * 是否包含一个点
	 * @param x 经度
	 * @param y 纬度
	 * @return 是否包含
	 */
	public boolean contains(double x,double y){
		Point2D.Double convertCoord = Geography.longilati2Decare(peakPoint, new Point2D.Double(x, y));
		return this.getGeneralPath().contains(convertCoord.x,convertCoord.y);
	}
	/**
	 * 判断一个多边形是否包含另一个多边形
	 * @param polygon
	 * @return
	 */
	public boolean contains(Polygon polygon){
		for(Point2D.Double p : polygon.getBoundary()){
			if(!this.contains(p)){
				return false;
			}
		}
		return true;
	}
	public boolean contains(Circle circle){
		return !circle.intersect(this)&&this.contains(circle.getP());
	}
	/**
	 * 是否与另外一个多变形相交
	 * @param polygon 多边形
	 * @return 是否相交
	 */
	public boolean intersect(Polygon polygon){
		if(!contains(polygon)&&!polygon.contains(this)){
			for(Point2D.Double p : polygon.getBoundary()){
				if(this.contains(p)){
					return true;
				}
			}
			for(Point2D.Double p : this.getBoundary()){
				if(polygon.contains(p)){
					return true;
				}
			}
			for (int m = 0; m < polygon.getBoundary().size() - 1; m++) {
				Point2D.Double points =  polygon.getBoundary().get(m);
				Point2D.Double pointe =  polygon.getBoundary().get(m + 1);
				Set<Point2D.Double> intersectPoints = intersectPoints(points, pointe);
				if(!intersectPoints.isEmpty()){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
		
	}
	/**
	 * 计算一个多边形与另一个多边形相交面积
	 * @param polygon
	 * @return
	 */
	public double intersectArea(Polygon polygon){
		Polygon intersectPolygon = intersectPolygon(polygon);
		return intersectPolygon==null?0:intersectPolygon.getArea();
	}
	/**
	 * 计算一个多边形与另一个多边形相交面积
	 * @param polygon
	 * @return
	 */
	public Polygon intersectPolygon(Polygon polygon){
		if(contains(polygon)){
			return polygon;
		}else if(polygon.contains(this)){
			return this;
		}else if(!intersect(polygon)){
			return null;
		}
		Set<Point2D.Double> intersectPoints = new HashSet<Point2D.Double>();
		for (int m = 0; m < polygon.getBoundary().size() - 1; m++) {
			Point2D.Double points =  polygon.getBoundary().get(m);
			Point2D.Double pointe =  polygon.getBoundary().get(m + 1);
			intersectPoints.addAll(intersectPoints(points, pointe));
		}
		Point2D.Double points = polygon.getBoundary().get(polygon.getBoundary().size() - 1);
		Point2D.Double pointe = polygon.getBoundary().get(0);
		intersectPoints.addAll(intersectPoints(points, pointe));
		for(Point2D.Double p : this.getBoundary()){
			if(polygon.contains(p)){
				intersectPoints.add(p);
			}
		}
		for(Point2D.Double p : polygon.getBoundary()){
			if(this.contains(p)){
				intersectPoints.add(p);
			}
		}
		for(Point2D.Double p : intersectPoints){
			Polygon pc = new Polygon(new Circle(p, 1));
			List<Point2D.Double> inpoints = new ArrayList<Point2D.Double>();
			for(Point2D.Double p1 : pc.getBoundary()){
				if(this.contains(p1)&&polygon.contains(p1)){
					inpoints.add(p1);
				}
			}
			if(inpoints.size()>2){
				Point2D.Double selectp = inpoints.get(inpoints.size()/2);
				return sort(selectp, intersectPoints);
			}
		}
		return null;
	}
	private Polygon sort(Point2D.Double pi,Set<Point2D.Double> intersectPoints){
		Map<String, Point2D.Double> treeMap  = new TreeMap<String, Point2D.Double>();
		for(Point2D.Double p : intersectPoints){
			if(p.equals(pi)){
				continue;
			}
			double calculateAngle = Geography.calculateAngle(pi.x, pi.y, p.x, p.y);
			String[] split = (calculateAngle+"").split("\\.");
			String fixlength = StringUtil.addAtFirst2Fixlength(split[0], "0", 30)+"."+split[1];
			treeMap.put(fixlength, p);
		}
		List<Point2D.Double> a = new ArrayList<Point2D.Double>();
		for(Map.Entry<String, Point2D.Double> entry : treeMap.entrySet()){
			a.add(entry.getValue());
		}
		return new Polygon(a); 
	}
	/**
	 * 求多边形与某条线段的交点
	 * @param polygon
	 * @param point1
	 * @param point2
	 * @return
	 */
	public Set<Point2D.Double> intersectPoints(Point2D.Double point1,Point2D.Double point2) {
		Set<Point2D.Double> ps = new HashSet<Point2D.Double>();//相交点
		Point2D.Double pointls = Geography.longilati2Decare(this.getPeakPoint(), point1);
		Point2D.Double pointle = Geography.longilati2Decare(this.getPeakPoint(), point2);
		Line line = new Line(pointls, pointle);
		for (int m = 0; m < this.getBoundaryConvert().size() - 1; m++) {
			Point2D.Double points =  this.getBoundaryConvert().get(m);
			Point2D.Double pointe =  this.getBoundaryConvert().get(m + 1);
			Line line2 = new Line(points, pointe);
			Point2D.Double intersectionPoint = line.getIntersectPoint(line2);
			if ((intersectionPoint != null)&& (CommonUtil.between(intersectionPoint.x, points.x,pointe.x))&& (CommonUtil.between(intersectionPoint.y, points.y,pointe.y))) {
				if ((CommonUtil.between(intersectionPoint.x, pointls.x,pointle.x)) && (CommonUtil.between(intersectionPoint.y, pointls.y,pointle.y))&&(CommonUtil.between(intersectionPoint.x, points.x,pointe.x)) && (CommonUtil.between(intersectionPoint.y, points.y,pointe.y))) {
					ps.add(Geography.decare2Longilati(this.getPeakPoint(), intersectionPoint));
				}
			}
		}
		Point2D.Double points = this.getBoundaryConvert().get(this.getBoundaryConvert().size() - 1);
		Point2D.Double pointe = this.getBoundaryConvert().get(0);
		Line line2 = new Line(points, pointe);
		Point2D.Double intersectionPoint = line.getIntersectPoint(line2);
		if ((intersectionPoint != null) && (CommonUtil.between(intersectionPoint.x, points.x,pointe.x)) && (CommonUtil.between(intersectionPoint.y, points.y,pointe.y))) {
			if ((CommonUtil.between(intersectionPoint.x, pointls.x,pointle.x)) && (CommonUtil.between(intersectionPoint.y, pointls.y,pointle.y))&&(CommonUtil.between(intersectionPoint.x, points.x,pointe.x)) && (CommonUtil.between(intersectionPoint.y, points.y,pointe.y))) {
				ps.add(Geography.decare2Longilati(this.getPeakPoint(), intersectionPoint));
			}
		}
		return ps;
	}
	@Deprecated
	public Point2D.Double getCenterPoint(){
		Point2D.Double point0 = getBoundaryConvert().get(0);
		Point2D.Double point1 = getBoundaryConvert().get(1);
		Point2D.Double point2 = getBoundaryConvert().get(getBoundaryConvert().size() - 1);
		Line line1 = new Line(point0, point1);
		Line line2 = new Line(point0, point2);
		Line line3 = new Line(point1, point2);
		double a = (line1.getA() + line2.getA())
				/ (1.0D - line1.getA() * line2.getA());
		double b = point0.y - a * point0.x;
		Line line4 = new Line(a, b);
		Point2D.Double intersectionPoint = line4.getIntersectPoint(line3);
		return Geography.decare2Longilati(getBoundary().get(0),intersectionPoint);
	}
}
