/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.stat;

/**
 * A stat type for things like health, mana, and stamina.
 * @author Arthur
 */
public class ActiveStat {
    public ActiveStat(int val) {
        this(val, 0, 0, val, val);
    }
    public ActiveStat(int val, int max) {
        this(val, 0, 0, max, max);
    }
    public ActiveStat(int val, int max, int nat_max) {
        this(val, 0, 0, max, nat_max);
    }
    public ActiveStat(int val, int min, int nat_min, int max, int nat_max) {
        this.current_max = Math.max(min, max);
        this.current_min = Math.min(min, max);
        this.current_val = val;
        this.natural_max = Math.max(nat_min, nat_max);
        this.natural_min = Math.min(nat_min, nat_max);
    }
    private int current_val;
    private int current_min, current_max;
    private int natural_min, natural_max;
    
    public int getCurrent() { return current_val; }
    public int getMinimum() { return current_min; }
    public int getMaximum() { return current_max; }
    public int getNaturalMin() { return natural_min; }
    public int getNaturalMax() { return natural_max; }
    
    public void setCurrent(int val) {
        current_val = Math.max(current_min, Math.min(current_max, val));
    }
    public void setMinimum(int val) {
        current_min = val;
        if(current_min > current_max)
            current_max = current_min;
        setCurrent(current_val);
    }
    public void setMaximum(int val) {
        current_max = val;
        if(current_min > current_max)
            current_max = current_min;
        setCurrent(current_val);
    }
    public void setNaturalMin(int val) { this.natural_min = val; }
    public void setNaturalMax(int val) { this.natural_max = val; }
    
    public float getFraction() {
        return (float) (current_val - current_min) / (float) (current_max - current_min);
    }
    public float getNaturalFrac() {
        return (float) (current_val - natural_min) / (float) (natural_max - natural_min);
    }
    public float getMinFrac() {
        return (float) (current_min - natural_min) / (float) (natural_max - natural_min);
    }
    public float getMaxFrac() {
        return (float) (current_max - natural_min) / (float) (natural_max - natural_min);
    }
}