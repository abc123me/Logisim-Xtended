package net.net16.jeremiahlowe.logisim.logisim_xtended.parts.logic;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.Attributes;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.util.GraphicsUtil;

import net.net16.jeremiahlowe.logisim.logisim_xtended.instance.StringMaker;

public abstract class SquareBase extends InstanceFactory {
	private static final Attribute<Integer> INPUTS = Attributes.forIntegerRange("Inputs", 2, 32);
	private static final Attribute<?> SIZE = Attributes.forOption("Size", new Object[]{"Extra wide", "Wide", "Narrow"});
	public Port[] ports;

	public SquareBase(String name) {
		super(name);
		setAttributes(new Attribute[] { 
				StdAttr.WIDTH, StdAttr.FACING, INPUTS, SIZE },
			new Object[] { 
				BitWidth.create(1), Direction.EAST, 2, "Wide"
		});
	}
	
	protected Value doLogicalOperation(Value[] inputs){
		return null;
	}

	@Override
	public void propagate(InstanceState state) {
		Value[] inputs = new Value[ports.length - 1];
		for(int i = 1; i < ports.length; i++){
			inputs[i - 1] = state.getPort(i); 
		}
		state.setPort(0, doLogicalOperation(inputs), 0);
	}
	
	@Override
	public void paintInstance(InstancePainter painter) {
		Bounds b = painter.getBounds();
		GraphicsUtil.drawCenteredText(painter.getGraphics(), "WTF", b.getWidth() / 2 + b.getX(), b.getHeight() / 2 + b.getY());
		painter.drawBounds();
		painter.drawPorts();
	}

	@Override
	public Bounds getOffsetBounds(AttributeSet attrs) {
		int x = 0;
		int y = 0;
		String size = (String) attrs.getValue(SIZE);
		int w = size == "Extra wide" ? 30 : (size == "Wide" ? 20 : 10);
		int h = attrs.getValue(INPUTS) * 10 + 10;
		if((h / 10) % 2 != 0) h += 10;
		return Bounds.create(x, y, w, h).rotate(Direction.EAST, attrs.getValue(StdAttr.FACING), x, y);
	}

	@Override
	protected void configureNewInstance(Instance instance) {
		instance.addAttributeListener();
		instance.recomputeBounds();
		initPorts(instance);
	}

	@Override
	protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {
		instance.recomputeBounds();
		initPorts(instance);
	}

	private void initPorts(Instance instance){
		int inputPorts = instance.getAttributeValue(INPUTS);
		int bitWidth = instance.getAttributeValue(StdAttr.WIDTH).getWidth();
		EDirection facing = EDirection.convert(instance.getAttributeValue(StdAttr.FACING));
		Bounds bds = instance.getBounds();
		ports = new Port[inputPorts + 1];
		int xm = 0, ym = 0; 
		boolean flip = false;
		switch(facing){
			case EAST: xm = 1; ym = 1; flip = false; break; 
			case WEST: xm = -1; ym = -1; flip = false; break;
			case NORTH: xm = -1; ym = 1; flip = true; break;
			case SOUTH: xm = 1; ym = -1; flip = true; break;
		}
		
		int x = xm * (!flip ? bds.getWidth() : bds.getHeight());
		int y = ym * (flip ? bds.getWidth() : bds.getHeight()) / 2;
		ports[0] = new Port(flip ? y : x, flip ? x : y, Port.OUTPUT, bitWidth);
		ports[0].setToolTip(new StringMaker("Output"));
		
		boolean halfMid = (inputPorts % 2 == 0); 
		int halfVal = inputPorts / 2, off = 0;
		for(int i = 1; i < ports.length; i++){
			int tmpX = xm * 0;
			int tmpY = ym * ((i * 10) + off);
			off += halfMid && i == halfVal ? 10 : 0;
			
			ports[i] = new Port(flip ? tmpY : tmpX, flip ? tmpX : tmpY, Port.INPUT, bitWidth);
			ports[i].setToolTip(new StringMaker("Input " + i));
		}
		instance.setPorts(ports);
	}
	private static enum EDirection{
		NORTH, SOUTH, EAST, WEST;
		public static EDirection convert(Direction in){
			if(in == Direction.WEST) return WEST;
			else if(in == Direction.NORTH) return NORTH;
			else if(in == Direction.SOUTH) return SOUTH;
			else return EAST;
		}
	}
}
