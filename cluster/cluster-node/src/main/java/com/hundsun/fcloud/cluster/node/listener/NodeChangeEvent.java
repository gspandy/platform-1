package com.hundsun.fcloud.cluster.node.listener;

import com.hundsun.fcloud.cluster.node.Node;

import java.util.EventObject;

/**
 * Created by Gavin Hu on 2015/1/26.
 */
public class NodeChangeEvent extends EventObject {

    private Type type;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public NodeChangeEvent(Object source, Type type) {
        super(source);
        this.type = type;
    }

    public Node getNode() {
        return (Node) getSource();
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        NODE_ADDED,
        NODE_REMOVED,
        NODE_UPDATED
    }

}
