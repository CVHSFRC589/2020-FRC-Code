package frc.robot.Egg.Utility.Math;

import java.awt.Point;
import java.io.Serializable;

public class Matrix implements Serializable {
	
	//while java has a matrix class, I decided to write my own so I could have control of 
	//what it can do.  It has basic setters and getters as well as basic row operations

	public int width;
	public int height;
	
	public double[][] matrix;	
	
	public double[] augment = null;
	
	
	public Matrix(int Width, int Height) {
		width = Width;
		height = Height;
		
		matrix = new double[width][height];
	}
	
	public Matrix(int Width, int Height, boolean Augment) {
		width = Width;
		height = Height;
		
		matrix = new double[width][height];
		
		if (Augment) {
			augment = new double[Height];
		}
		
	}
	
	public Matrix(double[][] Matrix) {
		width = Matrix[0].length;
		height = Matrix.length;
	
		matrix = Matrix;
	}
	
	public Matrix(double[][] Matrix, double[] Augment) {
		width = Matrix[0].length;
		height = Matrix.length;
	
		matrix = Matrix;
		augment = Augment;
	}
	
	public double get(int x, int y) {
		return matrix[x][y];
	}
	
	public double get(Point P) {
		return matrix[P.x][P.y];
	}
	
	public double getAugment(int y) {
		return augment[y];
	}
	
	public void set(int x, int y, double value) {
		matrix[x][y] = value;
	}
	
	public void setAugment(int y, double value) {
		augment[y] = value;
	}
	
	public void SwapRows(int row1, int row2) {
		double[] row = new double[width]; 
		
		for (int i = 0; i < width; i++) {
			row[i] = matrix[i][row1];
			matrix[i][row1] = matrix[i][row2];
			matrix[i][row2] = row[i];
		}	
		
		if (augment != null) {
			double temp = augment[row1];
			augment[row1] = augment[row2];
			augment[row2] = temp;			
		}
	}
	
	public void DivideRow(int row, double constant) {
		for (int i = 0; i < width; i++) {
			matrix[i][row] /= constant;
		}
		
		if (augment != null) {
			augment[row] /= constant;
			
		}
	}
	
	public void AddRows(int row1, int row2, double multiple) {
		for (int i = 0; i < width; i++) {
			matrix[i][row2] += matrix[i][row1] * multiple;
		}
		
		if (augment != null) {
			augment[row2] += augment[row1] * multiple; 
		}
	}
	
	public static Matrix RREF(Matrix Matrix) {		
		
		Matrix matrix = Matrix;
		
		for (int y = 0; y < matrix.height; y++) {
			for (int x = 0; x < matrix.width; x++) {
				if (matrix.get(x, y) == 0) {
					continue;
				}
								
				if (matrix.get(x, y) != 1) {	
					matrix.DivideRow(y, matrix.get(x, y));	
					
				}
				
				if (matrix.get(x, y) == 1) {
					for (int i = 0; i < matrix.height; i++) {
						if (i == y) {
							continue;
						}
						matrix.AddRows(y, i, -matrix.get(x, i));
						
					}
					x = matrix.width;
				}
			}
		}
		
		
		for (int y = 0; y < matrix.height; y++) {
			for (int x = 0; x < matrix.width; x++) {
				if (matrix.get(x, y) == 1) {
					matrix.SwapRows(y, x);
					x = matrix.width + 1;

				}
				
			}
		}
		
			
		
		return matrix;
		
	}
}
