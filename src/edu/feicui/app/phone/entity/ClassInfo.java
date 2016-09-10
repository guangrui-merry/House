package edu.feicui.app.phone.entity;

public class ClassInfo {

	private String name;
	private int idx;

	public ClassInfo(String name, int idx) {
		super();
		this.name = name;
		this.idx = idx;
	}

	@Override
	public String toString() {
		return "ClassInfo [name=" + name + ", idx=" + idx + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

}
