/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.ai.pathfinding;

/**
 *
 * @author Arthur
 */
public interface IPathCost {
    public IPathCost add(IPathCost cost);
    public IPathCost minus(IPathCost cost);
    public boolean isLessThan(IPathCost cost);
    public boolean isGreaterThan(IPathCost cost);
    public boolean isEqualTo(IPathCost cost);
    public int signum();
}
