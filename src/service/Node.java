package service;

import model.Task;

public class Node {
    protected Node prev;
    protected Node next;
    protected Task task;

    public Node(Node prev,Task task, Node next) {
        this.prev = prev;
        this.next = next;
        this.task = task;
    }
}
