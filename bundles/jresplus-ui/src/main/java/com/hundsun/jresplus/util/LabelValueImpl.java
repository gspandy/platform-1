package com.hundsun.jresplus.util;

/**
 * @author sagahl
 */
public class LabelValueImpl<T> implements LabelValue<T> {

	public LabelValueImpl() {

	}

	public LabelValueImpl(String label, T value) {
		this.label = label;
		this.value = value;
	}

	private String label;
	private T value;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

}
