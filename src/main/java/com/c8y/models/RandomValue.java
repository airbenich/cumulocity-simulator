package com.c8y.models;

public class RandomValue {

	int startValue;
	int minValue;
	int maxValue;
	int variance;
	
	public RandomValue(int startValue, int minValue, int maxValue, int variance) {
		this.startValue = startValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.variance = variance;
	}
	
	public int getStartValue() {
		return startValue;
	}
	public void setStartValue(int startValue) {
		this.startValue = startValue;
	}
	public int getMinValue() {
		return minValue;
	}
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}
	public int getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
	public int getVariance() {
		return variance;
	}
	public void setVariance(int variance) {
		this.variance = variance;
	}

}
