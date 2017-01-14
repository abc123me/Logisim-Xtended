package net.net16.jeremiahlowe.logisim.logisim_xtended.parts.logic;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.Attributes;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.instance.StdAttr;

public class SquareBase extends InstanceFactory{
	private static final Attribute<Integer> INPUTS = Attributes.forIntegerRange("Inputs", 2, 32);
	public Port[] ports;

	public SquareBase(String name) {
		super(name);
		setAttributes(new Attribute[]{
				StdAttr.WIDTH, StdAttr.LABEL, StdAttr.FACING, INPUTS
		}, new Object[]{
				BitWidth.create(1), "", Direction.EAST, 2
		});
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propagate(InstanceState state) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void configureNewInstance(Instance instance){
		instance.addAttributeListener();
		initPorts(instance);
		instance.recomputeBounds();
	}
	
	private void initPorts(Instance instance){
		int inputPorts = instance.getAttributeValue(INPUTS);
		ports = new Port[inputPorts + 1];
		int x = 0;
		int y = 0;
		ports[0] = new Port(x, y, Port.OUTPUT, instance.getAttributeValue(StdAttr.WIDTH));
		for(int i = 1; i < ports.length; i++){
			
		}
		instance.setPorts(ports);
	}
}
