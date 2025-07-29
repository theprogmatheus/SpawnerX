package com.github.theprogmatheus.mc.plugin.spawnerx.kdtree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class KDTreeIterator<V> implements Iterator<KDNode<V>> {

    private final Deque<KDNode<V>> stack = new ArrayDeque<>();

    public KDTreeIterator(KDNode<V> root) {
        pushLeft(root);
    }

    /**
     *
     * @param node - O node inicial para fazer a varredura
     */
    private void pushLeft(KDNode<V> node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public KDNode<V> next() {
        if (!hasNext()) throw new NoSuchElementException();

        KDNode<V> node = stack.pop();
        if (node.right != null) pushLeft(node.right);
        return node;
    }

}
