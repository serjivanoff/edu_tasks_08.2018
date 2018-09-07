package my.education.sortedbinarytree;

import java.util.*;

public class BinarySortedTree<T extends Comparable<T>> {
    private Node<T> root;

    public boolean add(T value) {
        if (value == null) {
            return false;
        }
        if (root == null) {
            root = new Node<>(value);
        } else {
            Node<T> currentNode = root, previousNode = root;
            while (currentNode != null) {
                previousNode = currentNode;
                currentNode = value.compareTo(currentNode.value) > 0 ? currentNode.rightChild : currentNode.leftChild;
            }
            if (value.compareTo(previousNode.value) > 0) {
                previousNode.rightChild = new Node<>(value);
            } else {
                previousNode.leftChild = new Node<>(value);
            }
        }
        return true;
    }

    public T find(T value) {
        Node<T> result = findNode(value);
        return result == null ? null : result.value;
    }

    private Node<T> findNode(T value) {
        if (value == null) return null;
        Node<T> currentNode = root;
        while (currentNode != null && currentNode.value.compareTo(value) != 0) {
            currentNode = value.compareTo(currentNode.value) > 0 ? currentNode.rightChild : currentNode.leftChild;
        }
        return currentNode;
    }

    public boolean delete(T value) {
        if (value != null) {
            Node<T> currentNode = root, previousNode = root;
            while (currentNode != null && currentNode.value.compareTo(value) != 0) {
                previousNode = currentNode;
                currentNode = value.compareTo(currentNode.value) > 0 ? currentNode.rightChild : currentNode.leftChild;
            }

            if (currentNode != null) {
                boolean isLeft = previousNode.leftChild.value.compareTo(currentNode.value) == 0;

                //1. If the element is "leaf" element;
                if (currentNode.leftChild == null && currentNode.rightChild == null) {
                    if (isLeft) previousNode.leftChild = null;
                    else previousNode.rightChild = null;
                    return true;
                }
                //2. If the element has exactly one child
                if (currentNode.leftChild != null && currentNode.rightChild == null) {
                    if (isLeft) previousNode.leftChild = currentNode.leftChild;
                    else previousNode.rightChild = currentNode.leftChild;
                    return true;
                }
                if (currentNode.leftChild == null) {
                    if (isLeft) previousNode.leftChild = currentNode.rightChild;
                    else previousNode.rightChild = currentNode.rightChild;
                    return true;
                }
                //If the element has 2 or more children
                Node<T> minimum = currentNode.rightChild, rightChild = currentNode.rightChild, leftChild = currentNode.leftChild;
                while (minimum.leftChild != null) {
                    currentNode = minimum;
                    minimum = currentNode.leftChild;
                }
                currentNode.leftChild = null;
                minimum.leftChild = leftChild;
                minimum.rightChild = rightChild;
                if (isLeft) previousNode.leftChild = minimum;
                else previousNode.rightChild = minimum;
                return true;
            }
        }
        return false;
    }

    public List<T> visitInDepth() {
        List<T> result = new ArrayList<>();
        Stack<Node<T>> stack = new Stack<>();
        stack.push(root);
        result.add(root.value);
        Node<T> visited = root;
        visited.isVisited = true;
        while (true) {
            if (visited.leftChild != null && !visited.leftChild.isVisited) {
                inDepthVisitor(visited.leftChild, stack, result);
                visited = visited.leftChild;
                continue;
            }
            if (visited.rightChild != null && !visited.rightChild.isVisited) {
                inDepthVisitor(visited.rightChild, stack, result);
                visited = visited.rightChild;
                continue;
            }
            if (stack.empty()) break;
            visited = stack.pop();
        }
        return result;
    }

    private void inDepthVisitor(Node<T> node, Stack<Node<T>> stack, List<T> list) {
        node.isVisited = true;
        stack.push(node);
        list.add(node.value);
    }

    public List<T> visitInWidth() {
        List<T> result = new ArrayList<>();
        Queue<Node<T>> queue = new ArrayDeque<>();
        Node<T> visited = root;
        queue.add(visited);
        visited.isVisited = true;
        result.add(visited.value);
        while (true) {
            if (visited.leftChild != null && !visited.leftChild.isVisited) {
                inWidthVisitor(visited.leftChild, queue, result);
                continue;
            }
            if (visited.rightChild != null && !visited.rightChild.isVisited) {
                inWidthVisitor(visited.rightChild, queue, result);
                continue;
            }
            if (queue.isEmpty()) break;
            visited = queue.poll();
        }
        return result;
    }

    private void inWidthVisitor(Node<T> node, Queue<Node<T>> queue, List<T> list) {
        node.isVisited = true;
        queue.add(node);
        list.add(node.value);
    }

    private class Node<T> {
        T value;
        Node<T> leftChild;
        Node<T> rightChild;
        boolean isVisited = false;

        Node(T value) {
            this.value = value;
            leftChild = null;
            rightChild = null;
        }
    }

}
