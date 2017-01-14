package net.net16.jeremiahlowe.logisim.logisim_xtended.instance;

import com.cburch.logisim.instance.InstanceData;

public class InstanceDataMultiton implements InstanceData, Cloneable {
	private Object[] values;
	public InstanceDataMultiton(Object[] values) {this.values = values;}
	
	@Override
	public InstanceDataMultiton clone() {
		try {return (InstanceDataMultiton) super.clone();} 
		catch (CloneNotSupportedException e) {return null;}
	}
	
	public Object[] getValues() {return values;}
	public void setValues(Object[] values) {this.values= values;}
}
