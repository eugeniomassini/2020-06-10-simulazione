package it.polito.tdp.imdb.model;

public class Arco {
	
	private Actor a1;
	private Actor a2;
	private Double peso;
	public Arco(Actor a1, Actor a2, Double peso) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}
	public Actor getA1() {
		return a1;
	}
	public void setA1(Actor a1) {
		this.a1 = a1;
	}
	public Actor getA2() {
		return a2;
	}
	public void setA2(Actor a2) {
		this.a2 = a2;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "Arco [a1=" + a1 + ", a2=" + a2 + ", peso=" + peso + "]";
	}
	
	

}
