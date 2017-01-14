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
import net.net16.jeremiahlowe.logisim.logisim_xtended.instance.StringMaker;
import net.net16.jeremiahlowe.logisim.logisim_xtended.CommonAttr;
import net.net16.jeremiahlowe.logisim.logisim_xtended.Utility;

public class Screen extends InstanceFactory{
	private static final int X_IN = 0, Y_IN = 1, ON = 2, OFF = 3, IS_ON = 4, RESET = 5;
	private Port[] ports = new Port[6];
	
	public Screen() {
		super("Screen");
		setAttributes(new Attribute[] {
				CommonAttr.OFF_COLOR, CommonAttr.ON_COLOR, CommonAttr.SCREEN_SIZE
			}, new Object[] {
				Color.GRAY, Color.GREEN, 256
			});
		setIcon(IconGetter.getIcon("Screen.png"));
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
		if(attr == CommonAttr.SCREEN_SIZE){
			initPorts(instance);
			instance.recomputeBounds();
		}
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		State state = (State) painter.getData();
		Graphics g = painter.getGraphics();
		Color onColor = painter.getAttributeValue(CommonAttr.ON_COLOR), offColor = painter.getAttributeValue(CommonAttr.OFF_COLOR);
		int size = (Integer) painter.getAttributeValue(CommonAttr.SCREEN_SIZE);
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < size; x++){
			for(int y = 0; y < size; y++){
				if(state.getPixel(x, y)) img.setRGB(x, y, onColor.getRGB());
				else img.setRGB(x, y, offColor.getRGB());
			}
		}
		g.drawImage(img, painter.getBounds().getX() + 1, painter.getBounds().getY() + 1, null);
		painter.drawBounds();
		painter.drawPorts();
	}

	@Override
	public void propagate(InstanceState state) {
		Value[] vals = new Value[]{state.getPort(0), state.getPort(1), state.getPort(2), state.getPort(3), state.getPort(4), state.getPort(5)};
		State currentState = (State) state.getData();
		if (currentState == null) state.setData(new State((Integer) state.getAttributeValue(CommonAttr.SCREEN_SIZE)));
		
		int xIn = Utility.booleanifyValue(vals[X_IN]).toIntValue();
		int yIn = Utility.booleanifyValue(vals[Y_IN]).toIntValue();
		
		if(Utility.valueToBoolean(vals[ON])) currentState.setPixel(xIn, yIn, true);
		if(Utility.valueToBoolean(vals[OFF])) currentState.setPixel(xIn, yIn, false);
		if(Utility.valueToBoolean(vals[RESET])){
			for(int x = 0; x < (Integer) state.getAttributeValue(CommonAttr.SCREEN_SIZE); x++){
				for(int y = 0; y < (Integer) state.getAttributeValue(CommonAttr.SCREEN_SIZE); y++){
					currentState.setPixel(x, y, false);
				}
			}
		}
		if(state != null && currentState != null) state.setPort(IS_ON, Utility.booleanToValue(currentState.getPixel(xIn, yIn)), 0);
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
		ports[X_IN] = new Port(0, -30, Port.INPUT, bitWidth);
		ports[Y_IN] = new Port(0, -20, Port.INPUT, bitWidth);
		ports[ON] = new Port(0, -10, Port.INPUT, 1);
		ports[OFF] = new Port(0, 0, Port.OUTPUT, 1);
		ports[IS_ON] = new Port(0, 10, Port.OUTPUT, 1);
		ports[RESET] = new Port(0, 20, Port.INPUT, 1);
		
		ports[X_IN].setToolTip(new StringMaker("X"));
		ports[Y_IN].setToolTip(new StringMaker("Y"));
		ports[ON].setToolTip(new StringMaker("Turn pixel on"));
		ports[OFF].setToolTip(new StringMaker("Turn pixel off"));
		ports[IS_ON].setToolTip(new StringMaker("Is pixel on?"));
		ports[RESET].setToolTip(new StringMaker("Reset all"));
		instance.setPorts(ports);
	}
	private class State implements InstanceData, Cloneable{
		private boolean[][] pixels;
		
		public State(int size) {
			pixels = new boolean[size][size];
		}
		
		@Override
		public State clone() {
			try {return (State) super.clone();} 
			catch (CloneNotSupportedException e) {return null;}
		}
		public boolean getPixel(int x, int y){return pixels[x][y];}
		public void setPixel(int x, int y, boolean on){pixels[x][y] = on;}
	}
}
