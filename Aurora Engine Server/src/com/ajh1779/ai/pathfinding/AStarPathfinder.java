/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.ai.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author Arthur
 * @param <T> The taker of the path. E.g. Creature
 * @param <K> The cost being evaluated. E.g. Distance or Overall Creature Cost
 */
public class AStarPathfinder<T, K extends IPathCost> implements IPathFinder<T, K> {
    @Override
    public List<IPathNode<T, K>> getPath(T taker, IPathNode<T, K> start, IPathNode<T, K> end) {
        // Frontier List - The List of Places to Consider
        PriorityQueue<MoveCost<T, K>> frontier = new PriorityQueue<>();
        frontier.add(new MoveCost<>(start, null));
        // Maps a movement to a cost.
        HashMap<IPathNode<T, K>, MoveCost<T, K>> map = new HashMap<>();
        map.put(start, new MoveCost<>(null, null));
        
        // While there are places to go.
        while(!frontier.isEmpty()) {
            // Get the lowest cost point
            MoveCost<T, K> current = frontier.poll();
            if(current.node == end) {
                break;
            }
            // Gets the cost of moving
            K cost = map.get(current.node).cost;
            List<IPathNode<T, K>> neighbours = current.node.getNeighbours();
            neighbours.stream().forEach((next) -> {
                K new_cost = (K) cost.add(current.node.getCostToMoveTo(taker, next));
                if (!map.containsKey(next) || new_cost.isLessThan(map.get(next).cost)) {
                    map.put(next, new MoveCost<>(current.node, new_cost));
                    K projected_cost = (K) new_cost.add(next.getProjectedCostTo(taker, end));
                    frontier.add(new MoveCost<>(next, projected_cost));
                }
            });
        }
        ArrayList<IPathNode<T, K>> path = new ArrayList<>();
        MoveCost<T, K> temp = map.get(end);
        path.add(end);
        while(temp.node != null) {
            path.add(temp.node);
            temp = map.get(temp.node);
        }
        Collections.reverse(path);
        return path;
    }
    
    private static class MoveCost<T, K extends IPathCost> implements Comparable {
        public MoveCost(IPathNode<T, K> node, K cost) {
            this.node = node;
            this.cost = cost;
        }
        private final IPathNode<T, K> node;
        private final K cost;

        @Override
        public int compareTo(Object o) {
            return cost.minus(((MoveCost) o).cost).signum();
        }
    }
}
