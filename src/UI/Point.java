package UI;

import processing.core.PVector;

/***********************************************************************************************/
public class Point {
	public float x, y;

	public Point(Point point) {
		this.x = point.x;
		this.y = point.y;
	}

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point() {
		this.x = 0;
		this.y = 0;
	}

	public Point add(Point that) {
		this.x += that.x;
		this.y += that.y;
		return this;
	}

	public Point add(PVector that) {
		this.x += that.x;
		this.y += that.y;
		return this;
	}

	public void sub(Point that) {
		this.x -= that.x;
		this.y -= that.y;
	}

	public void sub(PVector that) {
		this.x -= that.x;
		this.y -= that.y;
	}

	public double dist(Point that) {
		return Math.sqrt((float) (Math.pow((that.x - this.x), 2) + Math.pow((that.y - this.y), 2)));
	}

	public double dist(PVector that) {
		return Math.sqrt((float) (Math.pow((that.x - this.x), 2) + Math.pow((that.y - this.y), 2)));
	}

	public float heading(Point that) {
		return new PVector(this.x - that.x, this.y - that.y).heading();
	}

	public PVector toPVector() {
		return new PVector(this.x, this.y);
	}
}

/***********************************************************************************************/