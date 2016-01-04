/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.ai.pathfinding;

import java.util.List;

/**
 *
 * @author Arthur
 */
public interface IPathNode<T, K extends IPathCost> {
    public List<IPathNode<T, K>> getNeighbours();
    public K getCostToMoveTo(T t, IPathNode n);
    public K getProjectedCostTo(T t, IPathNode n);
}
