package com.realmo.customview.model;



public class PieData {
    /**
     * 用户传递的数据
     */
    private float value; //值
    /**
     * 计算出来的数据
     */
    private float percentage; //百分比
    private int color = 0;//颜色值
    private float angle = 0;//角度
    private float currentStartAngle = 0;//当前数据起始位置

    public PieData() {
    }
    public PieData(float value) {
        this.value = value;
    }
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getCurrentStartAngle() {
        return currentStartAngle;
    }

    public void setCurrentStartAngle(float currentStartAngle) {
        this.currentStartAngle = currentStartAngle;
    }
}