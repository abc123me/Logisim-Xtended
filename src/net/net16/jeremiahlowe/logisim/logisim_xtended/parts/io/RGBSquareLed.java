package net.net16.jeremiahlowe.logisim.logisim_xtended.parts.io;

import java.awt.Color;
import java.awt.Graphics;

import com.cburch.logisim.data.Attribute;
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

import net.net16.jeremiahlowe.logisim.logisim_xtended.assets.icons.IconGetter;
import net.net16.jeremiahlowe.logisim.logisim_xtended.instance.InstanceDataMultiton;
import net.net16.jeremiahlowe.logisim.logisim_xtended.instance.StringMaker;
import net.net16.jeremiahlowe.logisim.logisim_xtended.CommonAttr;
import net.net16.jeremiahlowe.logisim.logisim_xtended.Utility;

public class RGBSquareLed extends InstanceFactory{
	private Port[] ports = new Port[3];
	
	public RGBSquareLed() {
		super("Square RGB led");
		setIcon(IconGetter.getIcon("RGBLedSquare.png"));
		setOffsetBounds(Bounds.create(0, 0, 20, 20));
		setAttributes(new Attribute[] {
				StdAttr.LABEL, CommonAttr.COLOR_DETAIL, StdAttr.FACING
			}, new Object[] {
				"", 8, Direction.WEST
			});
	}
	
	@Override
	protected void configureNewInstance(Instance instance) { 
		instance.addAttributeListener();
		initPorts(instance);
	}
	
	@Override
	protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) { 
		if(attr == CommonAttr.COLOR_DETAIL || attr == StdAttr.FACING){
			initPorts(instance);
		}
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		Bounds ledBounds = painter.getBounds();
		Graphics g = painter.getGraphics();
		Value[] vals = (Value[]) ((InstanceDataMultiton) painter.getData()).getValues();
		g.setColor(Utility.getColorFromValues(vals[0], vals[1], vals[2]));
		g.fillRect(ledBounds.getX(), ledBounds.getY(), ledBounds.getWidth(), ledBounds.getHeight());
		g.setColor(Color.black);
		GraphicsUtil.drawText(g, painter.getAttributeValue(StdAttr.LABEL), ledBounds.getX() + ledBounds.getWidth() / 2, ledBounds.getY(), GraphicsUtil.H_CENTER, GraphicsUtil.V_BOTTOM);
		painter.drawRectangle(Bounds.create(ledBounds.getX() + 1, ledBounds.getY() + 1, ledBounds.getHeight() - 2, ledBounds.getWidth() - 2), "");
		painter.drawPorts();
	}

	@Override
	public void propagate(InstanceState state) {
		Value[] vals = new Value[]{state.getPort(0), state.getPort(1), state.getPort(2)};
		InstanceDataMultiton data = (InstanceDataMultiton) state.getData();
		if (data == null) state.setData(new InstanceDataMultiton(vals));
		else data.setValues(vals);
	}
	
	//Custom methods
	private void initPorts(Instance instance){
		Direction facing = (Direction) instance.getAttributeValue(StdAttr.FACING);
		int colorDetail = instance.getAttributeValue(CommonAttr.COLOR_DETAIL);
		if(facing == Direction.WEST){
			ports[0] = new Port(10, 0, Port.INPUT, colorDetail);
			ports[1] = new Port(0, 10, Port.INPUT, colorDetail);
			ports[2] = new Port(10, 20, Port.INPUT, colorDetail);
		}
		else if (facing == Direction.EAST){
			ports[0] = new Port(10, 0, Port.INPUT, colorDetail);
			ports[1] = new Port(20, 10, Port.INPUT, colorDetail);
			ports[2] = new Port(10, 20, Port.INPUT, colorDetail);
		}
		else if (facing == Direction.NORTH){
			ports[0] = new Port(0, 10, Port.INPUT, colorDetail);
			ports[1] = new Port(10, 0, Port.INPUT, colorDetail);
			ports[2] = new Port(20, 10, Port.INPUT, colorDetail);
		}
		else if (facing == Direction.SOUTH){
			ports[0] = new Port(0, 10, Port.INPUT, colorDetail);
			ports[1] = new Port(10, 20, Port.INPUT, colorDetail);
			ports[2] = new Port(20, 10, Port.INPUT, colorDetail);
		}
		ports[0].setToolTip(new StringMaker("Red"));
		ports[1].setToolTip(new StringMaker("Green"));
		ports[2].setToolTip(new StringMaker("Blue"));
		instance.setPorts(ports);
	}
}
