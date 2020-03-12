package frc.robot.Egg.Utility.Math;

import java.util.ArrayList;

public class CubicSpline {
	
	//This entire package is a whole lot of really scary math.  This one takes in a list of 
	//DoublePoints, populates a matrix, then solves it finding a series of equations.  As the
	//math is quite complex, I wont explain it here, though if you have questions contact me
	
	public static ArrayList<double[]> smoothCalculate(ArrayList<DoublePoint> Points) {
		ArrayList<double[]> temp = new ArrayList<double[]>();
		ArrayList<DoublePoint> tempPoints = new ArrayList<DoublePoint>();
		
		int index = 0;
		
		while (index < Points.size() - 1) {
			//System.out.println("-----");
			for (int i = 0; i < 10 && index < Points.size(); i++) {
				//System.out.println(index);
				tempPoints.add(Points.get(index));
				index++;			

			}
			
			index--;
			
			//System.out.println(index);
			
			temp.addAll(calculate(tempPoints));
			
			tempPoints = new ArrayList<DoublePoint>();
			
			//System.out.println("-----");

			
		}
		
		return temp;
	}

	
	public static ArrayList<double[]> calculate(ArrayList<DoublePoint> Points) {
		
		Matrix matrix;
		int functionCount = Points.size() - 1;
		
		matrix = new Matrix(functionCount * 4, functionCount * 4, true);
		
		//System.out.println(functionCount);	
		
		
		for (int y = 0; y < functionCount * 4; y++) {
			for (int x = 0; x < functionCount * 4; x++) {
				matrix.set(x, y, 0);
			}
			matrix.setAugment(y, 0);
			
		}
		
		for (int functionNo = 0; functionNo < functionCount; functionNo++) {
			
			matrix.set(functionNo * 4 + 0, functionNo * 4, Math.pow(Points.get(functionNo).getX(), 3));
			matrix.set(functionNo * 4 + 1, functionNo * 4, Math.pow(Points.get(functionNo).getX(), 2));
			matrix.set(functionNo * 4 + 2, functionNo * 4, Math.pow(Points.get(functionNo).getX(), 1));
			matrix.set(functionNo * 4 + 3, functionNo * 4, Math.pow(Points.get(functionNo).getX(), 0));
			
			matrix.setAugment(functionNo * 4, Points.get(functionNo).getY());
			
			matrix.set(functionNo * 4 + 0, functionNo * 4 + 1, Math.pow(Points.get(functionNo + 1).getX(), 3));
			matrix.set(functionNo * 4 + 1, functionNo * 4 + 1, Math.pow(Points.get(functionNo + 1).getX(), 2));
			matrix.set(functionNo * 4 + 2, functionNo * 4 + 1, Math.pow(Points.get(functionNo + 1).getX(), 1));
			matrix.set(functionNo * 4 + 3, functionNo * 4 + 1, Math.pow(Points.get(functionNo + 1).getX(), 0));
			
			matrix.setAugment(functionNo * 4 + 1, Points.get(functionNo + 1).getY());
			
			if (functionNo < functionCount - 1) {
				
				matrix.set(functionNo * 4 + 0, functionNo * 4 + 2, 3 * Math.pow(Points.get(functionNo + 1).getX(), 2));
				matrix.set(functionNo * 4 + 1, functionNo * 4 + 2, 2 * Math.pow(Points.get(functionNo + 1).getX(), 1));
				matrix.set(functionNo * 4 + 2, functionNo * 4 + 2, 1 * Math.pow(Points.get(functionNo + 1).getX(), 0));
				
				matrix.set(functionNo * 4 + 4, functionNo * 4 + 2, -3 * Math.pow(Points.get(functionNo + 1).getX(), 2));
				matrix.set(functionNo * 4 + 5, functionNo * 4 + 2, -2 * Math.pow(Points.get(functionNo + 1).getX(), 1));
				matrix.set(functionNo * 4 + 6, functionNo * 4 + 2, -1 * Math.pow(Points.get(functionNo + 1).getX(), 0));
				
			}
			
			if (functionNo < functionCount - 1) {
				
				matrix.set(functionNo * 4 + 0, functionNo * 4 + 3, 6 * Math.pow(Points.get(functionNo + 1).getX(), 1));
				matrix.set(functionNo * 4 + 1, functionNo * 4 + 3, 2 * Math.pow(Points.get(functionNo + 1).getX(), 0));
				
				matrix.set(functionNo * 4 + 4, functionNo * 4 + 3, -6 * Math.pow(Points.get(functionNo + 1).getX(), 1));
				matrix.set(functionNo * 4 + 5, functionNo * 4 + 3, -2 * Math.pow(Points.get(functionNo + 1).getX(), 0));
				
			}
			
			
			
			
		}
		
		matrix.set(0, matrix.height - 2, 6 * Points.get(0).getX());
		matrix.set(1, matrix.height - 2, 2);
		
		matrix.set(matrix.width - 4, matrix.height - 1, 6 * Points.get(Points.size() - 1).getX());
		matrix.set(matrix.width - 3, matrix.height - 1, 2);		
		
		matrix = Matrix.RREF(matrix);
		
		ArrayList<double[]> output = new ArrayList<double[]>();
		
		for (int i = 0; i < functionCount; i++) {
			double[] constants = new double[4];
			
			constants[0] = matrix.getAugment(i * 4);
			constants[1] = matrix.getAugment(i * 4 + 1);
			constants[2] = matrix.getAugment(i * 4 + 2);
			constants[3] = matrix.getAugment(i * 4 + 3);

			output.add(constants);
			
			/*
			for (int j = 0; j < 4; j++) {
				System.out.print(constants[j] + " ");
			}
			System.out.println();
			*/
			
		}
		
		return output;
		
	}
}
