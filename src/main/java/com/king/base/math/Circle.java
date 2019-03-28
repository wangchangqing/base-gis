package com.king.base.math;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Random;

import com.king.base.algorithm.Geography;
/**
 * 数学模型 圆
 */
public class Circle {
	private Point2D.Double p;
	private double r;
	/**
	 * 构造方法
	 * @param p 圆心
	 * @param r 半径 米
	 */
	public Circle(Double p, double r) {
		super();
		this.p = p;
		this.r = r;
	}
	/**
	 * 构造方法
	 * @param x 圆心 x坐标
	 * @param y 圆心 y坐标
	 * @param r 半径 米
	 */
	public Circle(double x,double y,double r){
		this(new Point2D.Double(x, y),r);
	}
	public Point2D.Double getP() {
		return p;
	}
	public void setP(Point2D.Double p) {
		this.p = p;
	}
	public double getR() {
		return r;
	}
	public void setR(double r) {
		this.r = r;
	}
	/**
	 * 判断圆是否包含某个点
	 * @param point 检测点
	 * @return true 包含 false 不包含
	 */
	public boolean contains(Point2D.Double point){
		return Geography.calculateLength(point.x, point.y, this.p.x, this.p.y)<=r;
	}
	/**
	 * 判断圆是否包含某个多边形
	 * @param point 检测点
	 * @return true 包含 false 不包含
	 */
	public boolean contains(Polygon polygon){
		for(Point2D.Double point : polygon.getBoundary()){
			if(!contains(point)){
				return false;
			}
		}
		return true;
	}
	/**
	 * 判断圆是否与某个多边形相交
	 * @param point 检测点
	 * @return true 包含 false 不包含
	 */
	public boolean intersect(Polygon polygon){
		if(!contains(polygon)){
			for(Point2D.Double point : polygon.getBoundary()){
				if(contains(point)){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}
	/**
	 * 生成圆内的随机点
	 * @return 圆内的随机点
	 */
	public Point2D.Double randomPoint(){
		//找出包含多边形的矩形的四个端点
		double maxLati = this.p.getY()+Geography.lengthLati(r);
		double minLati = this.p.getY()-Geography.lengthLati(r);
		double maxLongi = this.p.getX()+Geography.lengthLongi(this.p.getY(), r);
		double minLongi = this.p.getX()-Geography.lengthLongi(this.p.getY(), r); 
		//随机一个在里面的点
		while(true){
			Point2D.Double randomPoint = nextRandomPoint(maxLongi, minLongi,maxLati, minLati);
			if(contains(randomPoint)){
				return randomPoint;
			}
		}
	}
	/**
	 * 随机声场一个矩形内的点
	 * @param maxLongi 最大经度
	 * @param minLongi 最小经度
	 * @param maxLati  最大纬度
	 * @param minLati  最小纬度
	 * @return 矩形内的随机点
	 */
	private Point2D.Double nextRandomPoint(double maxLongi, double minLongi,
			double maxLati, double minLati) {
		int a = 1000000000;
		int r1 = (int)((maxLongi-minLongi)*a);
		int r2 = (int)((maxLati-minLati)*a);
		Random randomLongi = new Random();
		Random randomLati = new Random();
		double nextLongi = (double)(randomLongi.nextInt(r1))/a;
		double nextLati = (double)(randomLati.nextInt(r2))/a;
		return new Point2D.Double(minLongi+nextLongi, minLati+nextLati);
	}

}
