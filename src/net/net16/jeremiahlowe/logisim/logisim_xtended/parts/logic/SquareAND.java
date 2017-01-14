package net.net16.jeremiahlowe.logisim.logisim_xtended.parts.logic;

import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstancePainter;

public class SquareAND extends SquareBase{

	public SquareAND() {
		super("Square AND gate");
	}

	@Override
	protected Value doLogicalOperation(Value[] inputs) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void paintInstance(InstancePainter painter) {
		// TODO Auto-generated method stub
		painter.drawBounds();
		painter.drawPorts();
	}
}
