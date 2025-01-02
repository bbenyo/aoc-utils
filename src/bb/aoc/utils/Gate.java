package bb.aoc.utils;

import java.util.Map;

public class Gate {

	String wire1;
	String wire2;
	
	public enum GateType {AND, OR, XOR}
	
	GateType type;
	
	String wireOut;
	Boolean out;
	
	public Gate(GateType type, String w1, String w2, String wo) {
		this.type = type;
		this.wire1 = w1;
		this.wire2 = w2;
		this.wireOut = wo;
	}
	
	public GateType getType() {
		return type;
	}

	public void setType(GateType type) {
		this.type = type;
	}

	/**
	 * Get the value if the input wires, which can be null (no value)
	 * If either input is null, we do nothing and return null
	 * @param wires
	 * @return output wire (null for no value)
	 */
	public Boolean execute(Map<String, Boolean> wires) {
		Boolean in1 = wires.get(wire1);
		Boolean in2 = wires.get(wire2);
		if (in1 == null || in2 == null) {
			return null;
		}
		out = null;
		switch(type) {
		case AND:
			out = in1 && in2;
			break;
		case OR:
			out = in1 || in2;
			break;
		case XOR:
			out = in1 ^ in2;
			break;
		}
		return out;
	}
	
	public Boolean getOut() {
		return out;
	}
	
	public String getWireOut() {
		return wireOut;
	}
	
	public String getWire1() {
		return wire1;
	}

	public void setWire1(String wire1) {
		this.wire1 = wire1;
	}

	public String getWire2() {
		return wire2;
	}

	public void setWire2(String wire2) {
		this.wire2 = wire2;
	}

	public String toString() {
		return wire1+" "+type+" "+wire2+" -> "+wireOut;
	}
	
	public String toDot() {
		StringBuffer sb = new StringBuffer();
		sb.append("  "+wire1+" -- "+wireOut);
		sb.append(" [label="+type+"]"+System.lineSeparator());
		sb.append("  "+wire2+" -- "+wireOut);
		sb.append(" [label="+type+"]"+System.lineSeparator());
		return sb.toString();
	}
	
}
