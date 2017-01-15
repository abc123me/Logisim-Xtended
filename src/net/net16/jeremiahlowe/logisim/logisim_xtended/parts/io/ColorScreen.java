package net.net16.jeremiahlowe.logisim.logisim_xtended.parts.io;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;

import net.net16.jeremiahlowe.logisim.logisim_xtended.assets.icons.IconGetter;
import net.net16.jeremiahlowe.logisim.logisim_xtended.instance.Pixel;
import net.net16.jeremiahlowe.logisim.logisim_xtended.instance.StringMaker;
import net.net16.jeremiahlowe.logisim.logisim_xtended.CommonAttr;
import net.net16.jeremiahlowe.logisim.logisim_xtended.Utility;

public class ColorScreen extends InstanceFactory{
	private static final int X_IN = 0, Y_IN = 1, R_IN = 2, G_IN = 3, B_IN = 4, SET = 5, RESET = 6;
	private Port[] ports = new Port[7];
	
	public ColorScreen() {
		super("Color Screen");
		setAttributes(new Attribute[] {
				CommonAttr.SCREEN_SIZE, CommonAttr.COLOR_DETAIL,
			}, new Object[] {
				256, 8
			});
		setIcon(IconGetter.getIcon("ColorScreen.png"));
	}
	
	@Override
	public Bounds getOffsetBounds(AttributeSet attrs) {
		int size = (Integer) attrs.getValue(CommonAttr.SCREEN_SIZE);
		return Bounds.create(0, size / -2, size + 2, size + 2);
	}
	
	@Override
	protected void configureNewInstance(Instance instance) { 
		instance.addAttributeListener();
		initPorts(instance);
	}
	
	@Override
	protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) { 
		if(attr == CommonAttr.COLOR_DETAIL){
			initPorts(instance);
		}
		if(attr == CommonAttr.SCREEN_SIZE){
			instance.recomputeBounds();
			initPorts(instance);
		}
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		State state = (State) painter.getData();
		Graphics g = painter.getGraphics();
		int size = (Integer) painter.getAttributeValue(CommonAttr.SCREEN_SIZE);
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < size; x++){
			for(int y = 0; y < size; y++){
				if(state.getPixel(x, y) == null) state.setPixel(x, y, new Pixel(Color.BLACK));
				img.setRGB(x, y, state.getPixel(x, y).toColor().getRGB());
			}
		}
		g.drawImage(img, painter.getBounds().getX() + 1, painter.getBounds().getY() + 1, null);
		painter.drawBounds();
		painter.drawPorts();
	}

	@Override
	public void propagate(InstanceState state) {
		Value[] vals = new Value[]{state.getPort(0), state.getPort(1), state.getPort(2), state.getPort(3), state.getPort(4), state.getPort(5), state.getPort(6)};
		State currentState = (State) state.getData();
		if (currentState == null) state.setData(new State((Integer) state.getAttributeValue(CommonAttr.SCREEN_SIZE)));
		
		if(Utility.valueToBoolean(vals[SET])){
			int xIn = Utility.booleanifyValue(vals[X_IN]).toIntValue();
			int yIn = Utility.booleanifyValue(vals[Y_IN]).toIntValue();
			currentState.setPixel(xIn, yIn, new Pixel(Utility.getColorFromValues(vals[R_IN], vals[G_IN], vals[B_IN])));
		}
		
		if(Utility.valueToBoolean(vals[RESET])){
			for(int x = 0; x < (Integer) state.getAttributeValue(CommonAttr.SCREEN_SIZE); x++){
				for(int y = 0; y < (Integer) state.getAttributeValue(CommonAttr.SCREEN_SIZE); y++){
					currentState.setPixel(x, y, new Pixel(Color.BLACK));
				}
			}
		}
	}
	
	//Custom methods
	private void initPorts(Instance instance) {
		int bitWidth = 0;
		switch((Integer) instance.getAttributeValue(CommonAttr.SCREEN_SIZE)){
			case 64: bitWidth = 6; break;
			case 128: bitWidth = 7; break;
			case 256: bitWidth = 8; break;
			case 512: bitWidth = 9; break;
			case 1024: bitWidth = 10; break;
			default: bitWidth = 1; break;
		}
		int colorDetail = instance.getAttributeValue(CommonAttr.COLOR_DETAIL);
		ports[X_IN] = new Port(0, -30, Port.INPUT, bitWidth);
		ports[Y_IN] = new Port(0, -20, Port.INPUT, bitWidth);
		ports[R_IN] = new Port(0, -10, Port.INPUT, colorDetail);
		ports[G_IN] = new Port(0, 0, Port.INPUT, colorDetail);
		ports[B_IN] = new Port(0, 10, Port.INPUT, colorDetail);
		ports[SET] = new Port(0, 20, Port.INPUT, 1);
		ports[RESET] = new Port(0, 30, Port.INPUT, 1);
		
		ports[X_IN].setToolTip(new StringMaker("X"));
		ports[Y_IN].setToolTip(new StringMaker("Y"));
		ports[R_IN].setToolTip(new StringMaker("R"));
		ports[G_IN].setToolTip(new StringMaker("G"));
		ports[B_IN].setToolTip(new StringMaker("B"));
		ports[SET].setToolTip(new StringMaker("Set"));
		ports[RESET].setToolTip(new StringMaker("Reset"));
		instance.setPorts(ports);
	}
	private class State implements InstanceData, Cloneable{
		private Pixel[][] pixels;
		
		public State(int size) {
			pixels = new Pixel[size][size];
		}
		
		@Override
		public State clone() {
			try {return (State) super.clone();} 
			catch (CloneNotSupportedException e) {return null;}
		}
		public Pixel getPixel(int x, int y){return pixels[x][y];}
		public void setPixel(int x, int y, Pixel p){pixels[x][y] = p;}
	}
}
