package frc.robot.Egg.Utility;

import java.awt.Color;

public class ColorFlux {
	
	public Color color;

	public ColorFlux() {
		color = new Color(255, 0, 0);		
	}
	
	public void Set(Color Color) {
		while (color != Color) {
			Flux();
		}
		
	}
	
	private int valueToFlux = 2;
	private int direction = 1;
	
	public Color Flux(int count) {
		for (int i = 0; i < count - 1; i++) {
			Flux();			
		}
		
		return Flux();
	}
	
	public Color Flux() {	
	
		switch(valueToFlux) {
		case 0:
			if (direction == 0) {
				if (color.getRed() > 0) {										
					color = new Color(color.getRed() - 1, color.getGreen(), color.getBlue());					
				} else {
					direction = 1;
					valueToFlux = 1;
				}
			}
			if (direction == 1) {
				if (color.getRed() < 255) {										
					color = new Color(color.getRed() + 1, color.getGreen(), color.getBlue());					
				} else {
					direction = 0;
					valueToFlux = 1;
				}
			}			
			break;
		case 1:
			if (direction == 1) {
				if (color.getGreen() < 255) {										
					color = new Color(color.getRed(), color.getGreen() + 1, color.getBlue());					
				} else {
					direction = 0;
					valueToFlux = 2;
				}
			}
			if (direction == 0) {
				if (color.getGreen() > 0) {										
					color = new Color(color.getRed(), color.getGreen() - 1, color.getBlue());					
				} else {
					direction = 1;
					valueToFlux = 2;
				}
			}
			break;			
		case 2:
			if (direction == 1) {
				if (color.getBlue() < 255) {										
					color = new Color(color.getRed(), color.getGreen(), color.getBlue() + 1);					
				} else {
					direction = 0;
					valueToFlux = 0;
				}
			}			
			if (direction == 0) {
				if (color.getBlue() > 0) {										
					color = new Color(color.getRed(), color.getGreen(), color.getBlue() - 1);					
				} else {
					direction = 1;
					valueToFlux = 0;
				}
			}
			break;	
		
		}
		
		
		return color;
	}

}
