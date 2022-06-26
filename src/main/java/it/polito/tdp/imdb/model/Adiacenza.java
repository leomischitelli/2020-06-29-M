package it.polito.tdp.imdb.model;

public class Adiacenza implements Comparable<Adiacenza>{
	
	private Director director;
	private int peso;
	public Adiacenza(Director director, int peso) {
		super();
		this.director = director;
		this.peso = peso;
	}
	public Director getDirector() {
		return director;
	}
	public int getPeso() {
		return peso;
	}
	@Override
	public String toString() {
		return director.toString() + " - # attori condivisi = " + peso;
	}
	@Override
	public int compareTo(Adiacenza o) {
		// TODO Auto-generated method stub
		return o.getPeso() - this.peso;
	}
	
	

}
