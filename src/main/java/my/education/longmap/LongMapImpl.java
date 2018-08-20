package my.education.longmap;

import java.util.Arrays;
import java.util.NoSuchElementException;

/*
        1. As the long[] keys(); and V[] values(); methods return array so I made a decision
        that maximum number of elements in the LongMapImpl is limited by the Integer.MAX_VALUE.
        And because of this to solve the collisions I've used an open addressing instead of buckets, as we
        can't have more elements then an array can content.
* */
public class LongMapImpl<V> implements LongMap<V> {

    private int length;
    private int size;
    private Object[] values;
    private long[] keys;

    public LongMapImpl() {
        this.size = 0;
        this.length = 16;
        this.keys = new long[length];
        this.values = new Object[length];
    }

    public LongMapImpl(int length) {
        this.size = 0;
        this.length = length;
        this.keys = new long[length];
        this.values = new Object[length];
    }

    private int index(long key) {
        int hash = Long.hashCode(key);
        if (hash < 0) hash = ~hash;
        return hash % length;
    }

    @Override
    public V put(long key, V value) {
        if (size >= length) {
            resize();
        }
        Node node = new Node(key, value);
        if (values[node.intIndex] != null) {
            int i = node.intIndex;
            while (values[i] != null) {
                if (i == values.length - 1) {
                    i = 0;
                    continue;
                }
                i++;
            }
            node.intIndex = i;
        }
        values[node.intIndex] = node;
        keys[node.intIndex] = key;
        size++;
        return value;
    }

    private void resize() {
        if (length >= Integer.MAX_VALUE / 2) {
            throw new IllegalStateException("There's no place for another elements");
        }
        length *= 2;
        Object[] newValues = new Object[length];
        long[] newKeys = new long[length];

        Arrays.stream(values).forEach(o -> {
            Node currentNode = (Node) o;
            currentNode.intIndex = index(currentNode.longIndex);
            while (newValues[currentNode.intIndex] != null) {
                if (currentNode.intIndex == newValues.length - 1) {
                    currentNode.intIndex = 0;
                    continue;
                }
                currentNode.intIndex++;
            }
            newValues[currentNode.intIndex] = currentNode;
            newKeys[currentNode.intIndex] = currentNode.longIndex;

        });
        this.values = newValues;
        this.keys = newKeys;
    }

    @Override
    public V get(long key) {
        return getNode(key).value;
    }

    private Node getNode(long key) {
        int intIndex = index(key);
        Node currentNode = ((Node) values[intIndex]);
        if (currentNode == null) {
            throw new NoSuchElementException();
        }
        while (currentNode.longIndex != key) {
            if (intIndex == values.length - 1) {
                intIndex = -1;
            }
            currentNode = (Node) values[++intIndex];
        }
        return currentNode;
    }

    @Override
    public V remove(long key) {
        Node node = getNode(key);
        values[node.intIndex] = null;
        keys[node.intIndex] = 0;
        size--;
        return node.value;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(long key) {
        return Arrays.stream(keys).filter(k->k==key).findFirst().isPresent();
    }

    @Override
    public boolean containsValue(V value) {
        return Arrays.stream(values).anyMatch(v->( v!=null && ((Node)v).value.equals(value)));
    }

    @Override
    public long[] keys() {
        return keys;
    }

    /*
      Really don't know how to catch the generic's <V> type in runtime
     */
    @Override
    public V[] values() {
        Object[] justForFun = Arrays.stream(this.values).map(v -> ((Node) v).getValue()).toArray();
        return (V[]) justForFun;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public void clear() {
        this.values = new Object[length];
        size = 0;
    }

    class Node {
        V value;
        long longIndex;
        int intIndex;

        public Node(long longIndex, V value) {
            this.value = value;
            this.longIndex = longIndex;
            this.intIndex = index(longIndex);
        }

        public V getValue() {
            return value;
        }
    }

}
