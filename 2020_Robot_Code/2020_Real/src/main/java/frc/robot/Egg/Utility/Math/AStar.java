package frc.robot.Egg.Utility.Math;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;

public class AStar {
	public static ArrayList<DoublePoint> calculate(Matrix map, Point start, Point goal) {
		
		ArrayList<DoublePoint> path = new ArrayList<DoublePoint>();
		
		ArrayList<Point> pointList = new ArrayList<Point>();
		Hashtable<Point, Point> cameFrom = new Hashtable<Point, Point>();
		Hashtable<Point, Integer> cost = new Hashtable<Point, Integer>();
		Hashtable<Point, Integer> Gcost = new Hashtable<Point, Integer>();
		Hashtable<Point, Integer> Fcost = new Hashtable<Point, Integer>();

		
		pointList.add(start);
		cameFrom.put(start, new Point(-1, -1));
		cost.put(start, getDistance(start, goal));
		Gcost.put(start, 0);
		Fcost.put(start,  getDistance(start, goal));
		
		Point current = new Point();
		
		//System.out.println(goal);
				
		int newCost;
		while (!pointList.isEmpty()) {			
						
			//System.out.println("AStar");
			
			
			pointList = sort(pointList, cost);
			
			
			//System.out.println(pointList.size());

			
			current = pointList.get(0);
			pointList.remove(0);
				
			if (current.toString().equals(goal.toString())) {
				break;
			}
			
			for (Point next : getNeighbors(current, map)) {
				
				
				//if (!Fcost.containsKey(next) || Fcost.get(next) > getDistance(next, goal)) {
					Fcost.put(next, getDistance(next, goal));
					
					newCost = (int) (Gcost.get(current) + Fcost.get(next) + (100 / map.get(next)));
					
					
					if (!cost.containsKey(next) || newCost < cost.get(next)) {
						if (cost.containsKey(next)) {
							cost.replace(next, newCost);
							cameFrom.replace(next, current);
							Gcost.replace(next, Gcost.get(current) + 1);
						} else {
							cost.put(next, newCost);
							cameFrom.put(next, current);
							Gcost.put(next, Gcost.get(current) + 1);
						}
						pointList.add(next);						
					}
				//} else {
					//continue;
				//}
				
				
			}		
			
		}
		
		//System.out.println(goal);

		
		while (current != start) {
			current = cameFrom.get(current);
			//System.out.println(current);
			
			path.add(new DoublePoint(current));
			
		}
		
		
	
		
		return path;
	}
	
	public static ArrayList<DoublePoint> CloseCull(ArrayList<DoublePoint> input) {
		ArrayList<DoublePoint> line = new ArrayList<DoublePoint>();
		ArrayList<DoublePoint> output = new ArrayList<DoublePoint>();
		
		ArrayList<DoublePoint> temp = input;	
		
		double MaxDistance = 2;
		
		int i;
				
		line.add(temp.get(0));
				
		while (temp.size() > 2) {
			
			//line.add(output.get(output.size() - 1));
			line.add(temp.get(1));
									
			i = 2;
			
			
			while ( DoublePoint.getDistance(line.get(line.size() - 2), temp.get(i)) < MaxDistance ) {
				line.add(temp.get(i));					
				i++;
					
				if (temp.size() < i + 2) {					
					break;
				}
			}
			

			output.add(line.get(line.size() - 1));
					
			for (int j = 0; j < line.size(); j++) {
				temp.remove(0);
			}
						
			while (line.size() > 1) {
				line.remove(0);
			}
			
		}

		return output;
	}
	
	public static ArrayList<DoublePoint> AngleCull(ArrayList<DoublePoint> input) {
		ArrayList<DoublePoint> line = new ArrayList<DoublePoint>();
		ArrayList<DoublePoint> output = new ArrayList<DoublePoint>();
		
		ArrayList<DoublePoint> temp = input;

		double MaxAngle = Math.PI / 500;
		
		System.out.println(MaxAngle * 180 / Math.PI);
		
		double angle;
		
		
		int i;
				
		line.add(temp.get(0));
				
		while (temp.size() > 2) {
			
			//line.add(output.get(output.size() - 1));
			line.add(temp.get(1));
									
			i = 2;
			
			
			while ( angleTest(line.get(0), line.get(i - 1), temp.get(i), MaxAngle) ) {
				line.add(temp.get(i));					
				i++;
					
				if (temp.size() < i + 2) {					
					break;
				}
			}
			

			output.add(line.get(line.size() - 1));
					
			for (int j = 0; j < line.size(); j++) {
				temp.remove(0);
			}
						
			while (line.size() > 1) {
				line.remove(0);
			}
			
		}

		return output;
	}
	
	public static boolean angleTest(DoublePoint p1, DoublePoint p2, DoublePoint p3, double MaxAngle) {
		double a, b, c, angle;
		a = DoublePoint.getDistance(p2, p3);
		b = DoublePoint.getDistance(p1, p3);
		c = DoublePoint.getDistance(p1, p2);
		
		angle = Math.acos((Math.pow(a, 2) - Math.pow(b, 2) - Math.pow(c, 2)) / (-2 * b * c));
		
		System.out.println(angle * 180 / Math.PI);
		
		if (angle < MaxAngle) {		
			return true;
		} else {
			return false;
		}
	}
	
	public static ArrayList<DoublePoint> CountCull(ArrayList<DoublePoint> input, int count) {
		ArrayList<DoublePoint> line = new ArrayList<DoublePoint>();
		ArrayList<DoublePoint> output = new ArrayList<DoublePoint>();
		
		ArrayList<DoublePoint> temp = input;

		//int i = 1;
		
		while (temp.size() > count) {
			for (int i = 0; i < count; i++) {
				temp.remove(0);
			}
			
			output.add(temp.get(0));
		}
		
		for (int i = 0; i < temp.size() - 2; i++) {
			temp.remove(0);
		}
		
		output.add(temp.get(0));

		return output;
	}
	
	public static ArrayList<DoublePoint> LinearCull(ArrayList<DoublePoint> input) {
		
		ArrayList<DoublePoint> line = new ArrayList<DoublePoint>();
		ArrayList<DoublePoint> output = new ArrayList<DoublePoint>();
		
		ArrayList<DoublePoint> temp = input;

		
		double m, b;
		
		int i;
		
		boolean vertical;
		
		line.add(temp.get(0));
				
		while (temp.size() > 2) {
			
			//line.add(output.get(output.size() - 1));
			line.add(temp.get(1));
			
			vertical = false;
			
			if (line.get(1).getX() - line.get(0).getX() != 0) {
				m = (line.get(1).getY() - line.get(0).getY()) / (line.get(1).getX() - line.get(0).getX());
				b = line.get(0).getY() - (m * line.get(0).getX());
			} else {
				m = 0;
				b = line.get(0).getX();
				vertical = true;
			}
			
			//System.out.println("M: " + m + "B: " + b);
									
			i = 2;
			
			//System.out.println(temp.get);
			
			if (!vertical) {
				while (temp.get(i).getY() == (m * temp.get(i).getX()) + b) {
					line.add(temp.get(i));
					//System.out.println(temp.get(i));
					i++;
					
					if (temp.size() < i + 2) {					
						break;
					}
				}
			} else {
				while (temp.get(i).getX() == b) {
					line.add(temp.get(i));
					//System.out.println(temp.get(i));
					i++;
					
					if (temp.size() < i + 2) {					
						break;
					}
				}
			}
			
			//System.out.println("Broke: " + temp.get(i));
			
			//output.add(line.get(0));
			output.add(line.get(line.size() - 1));
					
			for (int j = 0; j < line.size(); j++) {
				temp.remove(0);
			}
						
			while (line.size() > 1) {
				line.remove(0);
			}
			
			//System.out.println("dasg");
		}
		
		for (int j = 0; j < output.size(); j++) {
			//System.out.println(output.get(j));
		}
		
		return output;
	}
	
	static ArrayList<Point> getNeighbors(Point P, Matrix map) {
		
		ArrayList<Point> temp = new ArrayList<Point>();
				
		if (P.y > 0 && map.get(P.x, P.y - 1) != 0) {
			temp.add(new Point(P.x, P.y - 1));
		}
		
		if (P.x < map.width - 1 && map.get(P.x + 1, P.y) != 0) {
			temp.add(new Point(P.x + 1, P.y));
		}
		
		if (P.y < map.height - 1 && map.get(P.x, P.y + 1) != 0) {
			temp.add(new Point(P.x, P.y + 1));
		}
		
		if (P.x > 0 && map.get(P.x - 1, P.y) != 0) {
			temp.add(new Point(P.x - 1, P.y));
		}
		
		/*
		
		if (P.y > 0 && P.x < map.width - 1 && map.get(P.x + 1, P.y - 1) != 0) {
			temp.add(new Point(P.x + 1, P.y - 1));
		}
		
		if (P.x < map.width - 1 && P.y < map.height - 1 && map.get(P.x + 1, P.y + 1) != 0) {
			temp.add(new Point(P.x + 1, P.y + 1));
		}
		
		if (P.y < map.height - 1 && P.x > 0 && map.get(P.x - 1, P.y + 1) != 0) {
			temp.add(new Point(P.x - 1, P.y + 1));
		}
		
		if (P.x > 0 && P.y > 0 && map.get(P.x - 1, P.y - 1) != 0) {
			temp.add(new Point(P.x - 1, P.y - 1));
		}
		
		*/
		
				
		return temp;
		
	}
	
	public static int getDistance(Point P, Point G) {
		
		//return (P.x - G.x) + (P.y - G.y);
	
		return (int) (Math.sqrt(Math.pow(P.x - G.x, 2) + Math.pow(P.y - G.y, 2)) * 10);
	}
	
	static ArrayList<Point> sort(ArrayList<Point> P, Hashtable<Point, Integer> C) {
		
		ArrayList<Point> tempP = new ArrayList<Point>();
		
		int min = (int) Math.floor(C.get(P.get(0)));
		int i = 0;
		
		while (P.size() > 0) {
					
			i = 0;
			min = (int) Math.floor(C.get(P.get(0)));
			for (int k = 0; k < P.size(); k++) {
				if (C.get(P.get(k)) < min) {
					min = (int) Math.floor(C.get(P.get(k)));
					i = k;
				}
			}
			
			tempP.add(P.get(i));
			
			P.remove(i);			
			
		}
					
		return tempP;
		
	}
}
