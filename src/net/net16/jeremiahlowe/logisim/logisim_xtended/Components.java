package net.net16.jeremiahlowe.logisim.logisim_xtended;

import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;

import net.net16.jeremiahlowe.logisim.logisim_xtended.parts.*;
import net.net16.jeremiahlowe.logisim.logisim_xtended.parts.io.*;
import net.net16.jeremiahlowe.logisim.logisim_xtended.parts.logic.*;

public class Components extends Library {
    private List<Tool> tools;
    
    public Components() {
    	tools = Arrays.asList(new Tool[]{
    		new AddTool(new RGBSquareLed()),
        	new AddTool(new RGBLed()),
        	new AddTool(new SquareLED()),
        	new AddTool(new Screen()),
        	new AddTool(new ColorScreen()),
        	new AddTool(new BinToBCD()),
        	new AddTool(new SquareAND()),
        	new AddTool(new SquareOR()),
        	new AddTool(new SquareXOR()),
        	new AddTool(new ToggleButton())
    	});
    }
    
    public String getDisplayName() { return "Logisim Xtended"; }
    @Override
	public List<Tool> getTools() { return tools; }
}

