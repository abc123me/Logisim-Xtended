package net.net16.jeremiahlowe.logisim.logisim_xtended;

import java.awt.Color;

import com.cburch.logisim.data.Value;

public class Utility {
	public static <T> boolean isEither(T compare, T a, T b){
		if(compare == a || compare == b) return true;
		else return false;
	}
	public static Value booleanToValue(boolean in){return in ? Value.TRUE : Value.FALSE;}
	public static boolean valueToBoolean(Value in){
		Value[] vals = in.getAll();
		return (vals[0] == Value.TRUE) ? true : false;
	}
	public static Value booleanifyValue(Value in){
		Value[] values = in.getAll();
		for(int i = 0; i < values.length; i++) if(Utility.isEither(values[i], Value.ERROR, Value.UNKNOWN)) values[i] = Value.FALSE;
		return Value.create(values);
	}
	public static Color getColorFromValues(Value r, Value g, Value b){
		int rc = (255 * Utility.booleanifyValue(r).toIntValue()) / ((int) Math.pow(2, r.getWidth()) - 1);
		int gc = (255 * Utility.booleanifyValue(g).toIntValue()) / ((int) Math.pow(2, g.getWidth()) - 1);
		int bc = (255 * Utility.booleanifyValue(b).toIntValue()) / ((int) Math.pow(2, b.getWidth()) - 1);
		return new Color(rc, gc, bc);
	}
	public static Value intToValue(int in, int w){
		Value[] values = new Value[w];
		String binary = Integer.toString(in, 2);
		while(binary.length() < w) binary = "0" + binary; 
		for(int i = 0; i < binary.length(); i++) values[i] = binary.charAt((binary.length() - 1) - i) == '1' ? Value.TRUE : Value.FALSE;
		return Value.create(values);
	}
}
