package com.tbf;

import java.util.Comparator;

public class PortfolioList<T> {

	PortfolioListNode<T> head;
	private int size;
	Comparator<T> cmp;

	public PortfolioList(Comparator<T> cmp) {
		this.head = null;
		this.size = 0;
		this.cmp = cmp;
	}

	public PortfolioListNode<T> getHead() {
		return this.head;
	}

	public int getSize() {
		return this.size;
	}

	public boolean isEmpty() {
		return (this.size == 0);
	}

	public void clear() {
		this.head = null;
		this.size = 0;
	}

	public void addToStart(T t) {
		PortfolioListNode<T> newHead = new PortfolioListNode<>(t);
		newHead.setNext(this.head);
		this.head = newHead;
		this.size++;
	}
	

	public void addAtIndex(T t, int index) {
//		PortfolioListNode<T> curr = this.head;
		if (index == 0) {
			this.addToStart(t);
		} else if (index == 1) {
			PortfolioListNode <T> newNode = new PortfolioListNode<T>(t);
			PortfolioListNode <T> oldNode = this.head.getNext();
			newNode.setNext(oldNode);
			this.head.setNext(newNode);
			this.size++;
		} else {
			PortfolioListNode <T> newNode = new PortfolioListNode<T>(t);
			PortfolioListNode <T> prevNode = this.getPortfolioListNode(index-1);
			PortfolioListNode <T> oldNode = prevNode.getNext();
			newNode.setNext(oldNode);
			prevNode.setNext(newNode);
			this.size++;
		}
		
	}

	public void add(T t) {
		addAtIndex(t, getIndex(t));
	}

	public int getIndex(T t) {
		PortfolioListNode<T> curr = this.head;
		if (curr == null) {
			return 0;
		}

		int i = 0;
		while (curr != null && this.cmp.compare(t, curr.getItem()) > 0) {
			i++;
			curr = curr.getNext();

		}

		return i;
	}

	public void remove(int position) {
		if (position < 0 || position >= this.size) {
			throw new IllegalArgumentException("index out of bounds!");
		}
		if (size == 1) {
			this.size = 0;
			this.head = null;
		} else if (position == 0 && size > 1) {
			this.head = this.head.getNext();
			this.size--;
		} else {
			PortfolioListNode<T> prev = getPortfolioListNode(position - 1);

			PortfolioListNode<T> curr = prev.getNext();

			prev.setNext(curr.getNext());
			this.size--;
		}
	}

	public PortfolioListNode<T> getPortfolioListNode(int position) {

		PortfolioListNode<T> curr = this.head;
		for (int i = 0; i < position; i++) {
			curr = curr.getNext();
		}
		return curr;
	}

	public T getItem(int position) {
		if (position < 0 || position >= this.size) {
			throw new IllegalArgumentException("index out of bounds!");
		}

		PortfolioListNode<T> curr = this.head;
		for (int i = 0; i < position; i++) {
			curr = curr.getNext();
		}
		return curr.getItem();
	}


	public void print() {
		while (head != null) {
			System.out.println(head.getItem().toString() + "\n");
			head = head.getNext();
		}
	}
}
