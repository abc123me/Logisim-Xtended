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
	private static final Attribute<Boolean> REVERSE_PORTS = Attributes.forBoolean("Reverse ports");
	private static final Attribute<Boolean> OFFSET = Attributes.forBoolean("Offset for 7-seg");
	private static final Attribute<?> INPUT_SIDE = Attributes.forOption("Input side", new Object[]{"Left", "Right"});
	private Port[] ports;
	
	public BinToBCD() {
		super("Binary to BCD");
		setAttributes(new Attribute[]{
				BIT_WIDTH, REVERSE_PORTS, OFFSET, INPUT_SIDE
		}, new Object[]{
				BitWidth.create(8), true, true, "Left"
		});
	}
	
	@Override 
	public Bounds getOffsetBounds(AttributeSet attrs){
		int bitWidth = attrs.getValue(BIT_WIDTH).getWidth();
		int mult = attrs.getValue(OFFSET) ? 40 : 10;
		int w = (int) Math.ceil(Math.log10(Math.pow(2, bitWidth))) * mult + 10;
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
		boolean left = instance.getAttributeValue(INPUT_SIDE) == "Left";
		ports[combs] = new Port((left ? 0 : instance.getBounds().getWidth()), 10, Port.INPUT, bitWidth);
		ports[combs].setToolTip(new StringMaker("Value in"));
		int mult = instance.getAttributeValue(OFFSET) ? 40 : 10;
		int off = instance.getAttributeValue(OFFSET) ? 20 : 10;
		for(int i = 0; i < combs; i++){
			Port tmpNonReversed = new Port(i * mult + off, 0, Port.OUTPUT, 4);
			Port tmpReversed = new Port(((combs * mult) - (i * mult)) - off, 0, Port.OUTPUT, 4);
			ports[i] = instance.getAttributeValue(REVERSE_PORTS) ? tmpReversed : tmpNonReversed;
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
