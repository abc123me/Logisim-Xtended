package net.net16.jeremiahlowe.logisim.logisim_xtended.parts.io;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceDataSingleton;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstancePoker;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.util.GraphicsUtil;

import net.net16.jeremiahlowe.logisim.logisim_xtended.CommonAttr;

public class ToggleButton extends InstanceFactory{
	private Port[] ports = new Port[1];
	
	public ToggleButton() {
		super("Toggle button");
		setAttributes(new Attribute[] {
				CommonAttr.ON_COLOR, CommonAttr.OFF_COLOR, StdAttr.FACING, 
				CommonAttr.LABEL_ON, CommonAttr.LABEL_OFF, CommonAttr.LABEL_COLOR,
				StdAttr.LABEL_FONT
			}, new Object[] {
				Color.GREEN, Color.RED, Direction.SOUTH, "On", "Off", Color.BLACK, 
				Font.decode(Font.SANS_SERIF + " bold")
			});
		setIconName("button.gif");
		setOffsetBounds(Bounds.create(0, 0, 20, 20));
		setInstancePoker(Poker.class);
	}
	
	public void initPorts(Instance instance) {
		Direction d = instance.getAttributeValue(StdAttr.FACING);
		int x = 10, y = 10;
		if(d == Direction.WEST) 
			x = 0;
		if(d == Direction.EAST) 
			x = 20;
		if(d == Direction.NORTH) 
			y = 0;
		if(d == Direction.SOUTH) 
			y = 20;
		ports = new Port[] {
			new Port(x, y, Port.OUTPUT, 1)
		};
		instance.setPorts(ports);
	}
	
	@Override
	protected void configureNewInstance(Instance instance) { 
		instance.addAttributeListener();
		initPorts(instance);
	}
	
	@Override
	protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) { 
		initPorts(instance);
	}
	
	@Override
	public void paintGhost(InstancePainter painter) {
		Graphics g = painter.getGraphics();
		Bounds b = painter.getBounds();
		int x = b.getX(), y = b.getY(), w = b.getWidth(), h = b.getHeight();
		g.setColor(Color.BLACK);
		g.drawRect(x, y, w, h);
	}
	@Override
	public void paintInstance(InstancePainter painter) {
		Value val = Value.FALSE;
		InstanceDataSingleton data = (InstanceDataSingleton) painter.getData();
		val = data == null ? Value.FALSE : (Value) data.getValue();
		Graphics g = painter.getGraphics();
		Bounds b = painter.getBounds();
		int x = b.getX(), y = b.getY(), w = b.getWidth(), h = b.getHeight();
		String txt = "";
		if(val == Value.TRUE) {
			g.setColor(painter.getAttributeValue(CommonAttr.ON_COLOR));
			txt = painter.getAttributeValue(CommonAttr.LABEL_ON);
		}
		else {
			g.setColor(painter.getAttributeValue(CommonAttr.OFF_COLOR));
			txt = painter.getAttributeValue(CommonAttr.LABEL_OFF);
		}
		g.fillRect(x, y, w, h);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, w, h);
		g.setFont(painter.getAttributeValue(StdAttr.LABEL_FONT));
		g.setColor(painter.getAttributeValue(CommonAttr.LABEL_COLOR));
		GraphicsUtil.drawCenteredText(g, txt, x + w / 2, y + h / 2);
		painter.drawPorts();
	}

	@Override
	public void propagate(InstanceState state) {
		InstanceDataSingleton data = (InstanceDataSingleton) state.getData();
		Value val = data == null ? Value.FALSE : (Value) data.getValue();
		if(ports != null | ports.length <= 0)
			state.setPort(0, val, 0);
		else
			initPorts(state.getInstance());
	}
	
	public static class Poker extends InstancePoker {
		@Override
		public void mousePressed(InstanceState state, MouseEvent e) {
			toggleValue(state);
		}

		private void toggleValue(InstanceState state) {
			InstanceDataSingleton data = (InstanceDataSingleton) state.getData();
			if (data == null) {
				state.setData(new InstanceDataSingleton(Value.FALSE));
			} else {
				Value cur = Value.FALSE;
				if(data.getValue() == Value.TRUE)
					cur = Value.TRUE;
				data.setValue(cur.not());
			}
			state.getInstance().fireInvalidated();
		}
	}
}
