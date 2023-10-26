package service;

import model.Task;

public class Node {
    Node prev;
    Node next;
    Task task;

    public Node(Node prev,Task task, Node next) {
        this.prev = prev;
        this.next = next;
        this.task = task;
    }
}
