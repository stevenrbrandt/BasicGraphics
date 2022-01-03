/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trees;

class Node {
  char c;
  Node left, right;

  Node(char c) {
    this.c = c;
  }

  void add(char c,StringBuilder sb) {
    sb.append(this+" ");
    if(c > this.c) {
      if(right == null) {
        System.out.printf("+++ data = %c is added as a rightChild of %s%n",c,this);
        right = new Node(c);
      } else {
        System.out.printf("    following right child of %s%n",c,this);
        right.add(c,sb);
      }
    } else if(c < this.c) {
      if(left == null) {
        System.out.printf("+++ data = %c is added as a leftChild of %s%n",c,this);
        left = new Node(c);
      } else {
        System.out.printf("    following left child of %s%n",c,this);
        left.add(c,sb);
      }
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append('(');
    if(left == null) sb.append('-');
    else sb.append(left);
    sb.append(' ');
    sb.append(c);
    sb.append(' ');
    if(right == null) sb.append('-');
    else sb.append(right);
    sb.append(')');
    return sb.toString();
  }
}

public class BTree {
  Node head;

  public void add(char c) {
    System.out.printf("Adding data = %c%n",c);
    StringBuilder sb = new StringBuilder();
    if(head == null) {
      head = new Node(c);
    } else {
      head.add(c,sb);
    }
    System.out.printf("New tree = %s%n%n",head);
  }

  public String toString() {
    return toString(head);
  }
  public static String toString(Node node) {
    if(node == null)
      return "<>";
    return "<"+node+">";
  }

  public static void main(String[] args) {
    BTree bt = new BTree();
    for(char c : new char[]{'b','i','g','g','e','r'})
        bt.add(c);
  }
}

