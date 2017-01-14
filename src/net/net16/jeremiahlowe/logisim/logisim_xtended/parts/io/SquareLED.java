package net.net16.jeremiahlowe.logisim.logisim_xtended.parts.io;

import java.awt.Color;
import java.awt.Graphics;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceDataSingleton;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.util.GraphicsUtil;

import net.net16.jeremiahlowe.logisim.logisim_xtended.assets.icons.IconGetter;
import net.net16.jeremiahlowe.logisim.logisim_xtended.CommonAttr;

public class SquareLED extends InstanceFactory{
	private Port[] ports = new Port[1];
	
	public SquareLED() {
		super("Square LED");
		setIcon(IconGetter.getIcon("SquareLED.png"));
		setOffsetBounds(Bounds.create(0, 0, 20, 20));
		setAttributes(new Attribute[] {
				CommonAttr.ON_COLOR, CommonAttr.OFF_COLOR, StdAttr.FACING, StdAttr.LABEL
			}, new Object[] {
				Color.RED, Color.GRAY, Direction.WEST, ""
			});
	}
	
	@Override
	protected void configureNewInstance(Instance instance) { 
		instance.addAttributeListener();
		initPorts(instance);
	}
	
	@Override
	protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {
		if(attr == StdAttr.FACING){
			instance.recomputeBounds();
			initPorts(instance);
		}
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		InstanceDataSingleton data = (InstanceDataSingleton) painter.getData();
		Value val = data == null ? Value.FALSE : (Value) data.getValue();
		Bounds ledBounds = painter.getBounds();
		Graphics g = painter.getGraphics();
		
		if(val == Value.TRUE) g.setColor(painter.getAttributeValue(CommonAttr.ON_COLOR));
		else g.setColor(painter.getAttributeValue(CommonAttr.OFF_COLOR));
		
		g.fillRect(ledBounds.getX(), ledBounds.getY(), ledBounds.getWidth(), ledBounds.getHeight());
		g.setColor(Color.black);
		
		GraphicsUtil.drawText(g, painter.getAttributeValue(StdAttr.LABEL), ledBounds.getX() + ledBounds.getWidth() / 2, ledBounds.getY(), GraphicsUtil.H_CENTER, GraphicsUtil.V_BOTTOM);
		painter.drawRectangle(painter.getBounds(), "");
		painter.drawPorts();
	}

	@Override
	public void propagate(InstanceState state) {
		Value val = state.getPort(0);
		InstanceDataSingleton data = (InstanceDataSingleton) state.getData();
		if (data == null) {
			state.setData(new InstanceDataSingleton(val));
		} else {
			data.setValue(val);
		}
	}
	
	private void initPorts(Instance instance){
		Direction facing = instance.getAttributeValue(StdAttr.FACING);
		int w = instance.getBounds().getWidth(), h = instance.getBounds().getHeight();
		if(facing == Direction.WEST) ports[0] = new Port(0, h/2, Port.INPUT, 1);
		if(facing == Direction.EAST) ports[0] = new Port(w, h/2, Port.INPUT, 1);
		if(facing == Direction.NORTH) ports[0] = new Port(w/2, 0, Port.INPUT, 1);
		if(facing == Direction.SOUTH) ports[0] = new Port(w/2, h, Port.INPUT, 1);
		instance.setPorts(ports);
	}
}
