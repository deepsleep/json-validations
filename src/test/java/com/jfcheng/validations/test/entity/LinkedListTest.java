package com.jfcheng.validations.test.entity;

/**
 * Created by jfcheng on 3/14/16.
 */
public class LinkedListTest {

    static Node reverse(Node old){
       if(old == null){
           return null;
       }else{
           Node newList = null;
           while(old != null){
               Node remains = old.next;
               old.next = newList;
               newList = old;
               old = remains;
           }
           return newList;
       }

    }

    public static void main(String[] args){
        Node n1 = new Node(1, null);
        Node n2 = new Node(2, null);
        Node n3 = new Node(3, null);
        Node n4 = new Node(4, null);
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;

        printList(n1);
        System.out.println();

        Node newList = reverse(n1);
        printList(newList);


    }

    static void printList(Node head){
        while(head != null){
            System.out.print(head.value + "  ");
            head = head.next;
        }
    }


   static  class Node{
        int value;
        Node next;

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

}
