/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.ai.pathfinding;

import java.util.List;

/**
 *
 * @author Arthur
 */
public interface IPathFinder<T, K extends IPathCost> {
    public List<IPathNode<T, K>> getPath(T taker, IPathNode<T, K> start, IPathNode<T, K> end);
}
