package net.net16.jeremiahlowe.logisim.logisim_xtended.instance;

import java.awt.Color;

import com.cburch.logisim.instance.InstanceData;

public class Pixel implements InstanceData, Cloneable{
	private int r, g, b;
	@Override
	public Pixel clone() {
		try {return (Pixel) super.clone();} 
		catch (CloneNotSupportedException e) {return null;}
	}
	public Pixel(int r, int g, int b){
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public Pixel(Color c){setColor(c);}
	public Color toColor(){return new Color(r, g, b);}
	public int getRed(){return r;}
	public int getBlue(){return b;}
	public int getGreen(){return g;}
	public void setColor(Color c){
		r = c.getRed();
		g = c.getGreen();
		b = c.getBlue();
	}
	public void setRed(int r){
		verifyRange(r);
		this.r = r;
	}
	public void setGreen(int g){
		verifyRange(g);
		this.g = g;
	}
	public void setBlue(int b){
		verifyRange(b);
		this.b = b;
	}
	private void verifyRange(int i){
		if(i < 0) throw new RuntimeException("Value must be greater than zero " + i);
		if(i > 255) throw new RuntimeException("Value must be under 255 " + i);
	}
}