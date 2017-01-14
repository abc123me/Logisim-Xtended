package net.net16.jeremiahlowe.logisim.logisim_xtended.parts;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.Attributes;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;

import net.net16.jeremiahlowe.logisim.logisim_xtended.instance.StringMaker;
import net.net16.jeremiahlowe.logisim.logisim_xtended.Utility;

public class BinToBCD extends InstanceFactory {
	private static final Attribute<BitWidth> BIT_WIDTH = Attributes.forBitWidth("Bit width", 4, 32);
	private Port[] ports;
	
	public BinToBCD() {
		super("Binary to BCD");
		setAttributes(new Attribute[]{
				BIT_WIDTH
		}, new Object[]{
				BitWidth.create(8)
		});
	}
	
	@Override 
	public Bounds getOffsetBounds(AttributeSet attrs){
		int bitWidth = attrs.getValue(BIT_WIDTH).getWidth();
		int w = (int) Math.ceil(Math.log10(Math.pow(2, bitWidth))) * 10 + 10;
		return Bounds.create(0, 0, w, 20);
	}
	
	@Override
	protected void configureNewInstance(Instance instance){
		instance.addAttributeListener();
		initPorts(instance);
		instance.recomputeBounds();
	}
	
	@Override
	protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) { 
		initPorts(instance);
		instance.recomputeBounds();
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		Bounds bds = painter.getBounds();
		String label = painter.getAttributeValue(BIT_WIDTH).getWidth() < 10 ? "BCD" : "Bin2BCD";
		painter.drawRectangle(Bounds.create(bds.getX() + 1, bds.getY() + 1, bds.getWidth() - 2, bds.getHeight() - 2), label);
		painter.drawPorts();
	}

	@Override
	public void propagate(InstanceState state) {
		Value inputValue = state.getPort(ports.length - 1);
		int bitWidth = state.getAttributeValue(BIT_WIDTH).getWidth();
		String input = Utility.booleanifyValue(inputValue).toIntValue() + "";
		while(input.length() < bitWidth) input = "0" + input;
		Value[] orderedOutputs = new Value[ports.length - 1];
		for(int i = 0; i < ports.length - 1; i++){
			orderedOutputs[i] = Utility.intToValue(Integer.parseInt(input.charAt((input.length() - 1) - i) + ""), 4);
			state.setPort(i, Utility.booleanifyValue(orderedOutputs[i]), 0);
		}
	}
	//Custom methods
	private void initPorts(Instance instance){
		int bitWidth = instance.getAttributeValue(BIT_WIDTH).getWidth();
		int combs = (int) Math.ceil(Math.log10(Math.pow(2, bitWidth)));
		ports = new Port[combs+1];
		ports[combs] = new Port(0, 10, Port.INPUT, bitWidth);
		ports[combs].setToolTip(new StringMaker("Value in"));
		for(int i = 0; i < combs; i++){
			ports[i] = new Port(i * 10 + 10, 0, Port.OUTPUT, 4);
			switch(i){
				case 0: ports[0].setToolTip(new StringMaker("Ones")); break;
				case 1: ports[1].setToolTip(new StringMaker("Tens")); break;
				case 2: ports[2].setToolTip(new StringMaker("Hundreds")); break;
				case 3: ports[3].setToolTip(new StringMaker("Thousands")); break;
				case 4: ports[4].setToolTip(new StringMaker("Ten-Thousands")); break;
				case 5: ports[5].setToolTip(new StringMaker("Hundred-Thousands")); break;
				case 6: ports[6].setToolTip(new StringMaker("Millions")); break;
				case 7: ports[7].setToolTip(new StringMaker("Ten-Millions")); break;
				case 8: ports[8].setToolTip(new StringMaker("Hundred-Millions")); break;
				case 9: ports[9].setToolTip(new StringMaker("Billions")); break;
			}
		}
		instance.setPorts(ports);
	}
}
