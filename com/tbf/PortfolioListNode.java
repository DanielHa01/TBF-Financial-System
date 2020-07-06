package com.tbf;

public class PortfolioListNode<T> {
	
	private PortfolioListNode<T> next;
	private T item;
	
    public PortfolioListNode(T item) {
        this.item = item;
        this.next = null;
    }
    
    public T getItem() {
        return item;
    }
    
    public void setItem(T item) {
    	this.item = item;
    }
    
    public PortfolioListNode <T> getNext() {
        return next;
    }
    
    public void setNext(PortfolioListNode<T> next) {
        this.next = next;
    }
}
