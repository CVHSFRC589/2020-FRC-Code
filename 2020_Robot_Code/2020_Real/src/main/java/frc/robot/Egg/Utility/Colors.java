package frc.robot.Egg.Utility;
import java.awt.*;

public enum Colors {
	
	//This enum allows for the creation of defined colors, such as Falkon Blue
	
	FalkonBlue(0, 223, 255);
	
	private final int r;
    private final int g;
    private final int b;
    private final String rgb;

    private Colors(final int r,final int g,final int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.rgb = r + ", " + g + ", " + b;
    }

    public String getRGB() {
        return rgb;
    }

    //You can add methods like this too
    public int getRed(){
        return r;
    }

    public int getGreen(){
        return g;
    }

    public int getBlue(){
        return r;
    }

    //Or even these
    public Color getColor(){
        return new Color(r,g,b);
    }
}
