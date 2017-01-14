package net.net16.jeremiahlowe.logisim.logisim_xtended.instance;

import com.cburch.logisim.util.StringGetter;

public class StringMaker implements StringGetter{
	public String value = "";
	public StringMaker(String value){this.value = value;}
	@Override public String get() {return value;}
}
