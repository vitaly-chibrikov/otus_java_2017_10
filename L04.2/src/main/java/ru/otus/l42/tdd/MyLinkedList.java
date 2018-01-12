package ru.otus.l42.tdd;

public class MyLinkedList implements MyList {

    class Node {
        int val; Node prev, next;
        public Node(int val, Node prev, Node next) {
            this.val = val;this.prev = prev;this.next = next;
        }
    }

    private Node head;
    private int size;

    @Override
    public int get(int pos) {
        throw new UnsupportedOperationException();
//        Node cur = head;
//        int i = 0;
//        while (i++ < pos) {
//            cur = cur.next;
//        }
//        return cur.val;
    }

    @Override
    public void add(int val) {
        throw new UnsupportedOperationException();
//        if (head == null) {
//            head = new Node(val, null, null);
//        } else {
//            Node cur = head;
//            while (cur.next != null) {
//                cur = cur.next;
//            }
//            cur.next = new Node(val, cur, null);
//        }
//        size++;
    }

    @Override
    public int removeFirst() throws ListException {
        throw new UnsupportedOperationException();
//        if (head == null) {
//            throw new ListException();
//        }
//        else return -1;
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
//        return size;
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
//        return size == 0;
    }
}
