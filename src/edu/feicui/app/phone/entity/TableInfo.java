package edu.feicui.app.phone.entity;

public class TableInfo {

	private String name;
	private long number;

	public TableInfo(String name, long number) {
		super();
		this.name = name;
		this.number = number;
	}

	@Override
	public String toString() {
		return "TableInfo [name=" + name + ", number=" + number + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}
}
