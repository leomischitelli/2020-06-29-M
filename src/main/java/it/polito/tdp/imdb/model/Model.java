package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private Graph<Director, DefaultWeightedEdge> grafo;
	private ImdbDAO dao;
	private List<Director> listaDirettori;
	private Map<Integer, Director> idMap;
	private List<Director> sequenzaMigliore;
	private int sommaMigliore;
	
	
	public Model() {
		this.dao = new ImdbDAO();
	}
	
	public void creaGrafo(int year) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.listaDirettori = new ArrayList<>(this.dao.listAllDirectorsYear(year));
		
		this.idMap = new HashMap<>();
		for(Director d : this.listaDirettori)
			idMap.put(d.getId(), d);
		
		Graphs.addAllVertices(this.grafo, this.listaDirettori);
		
		List<Coppia> listaCoppie = new ArrayList<>(this.dao.listCoppieAnno(year, idMap));
		
		for(Coppia c : listaCoppie)
			Graphs.addEdge(this.grafo, c.getD1(), c.getD2(), c.getPeso());
		
	}
	
	public List<Adiacenza> registiAdiacenti(Director director) {
		
		List<Director> adiacenti = Graphs.neighborListOf(this.grafo, director);
		List<Adiacenza> result = new ArrayList<>();
		for(Director d : adiacenti) {
			DefaultWeightedEdge e = this.grafo.getEdge(d, director);
			int peso = (int)(this.grafo.getEdgeWeight(e));
			result.add(new Adiacenza(d, peso));
		}
		Collections.sort(result);
		return result;
	}
	
	public int getNumeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Director> getDirettori(){
		return this.listaDirettori;
	}
	
	public List<Director> cercaRegistiAffini(Director director, int soglia){
		
		this.sequenzaMigliore = new ArrayList<>();
		Set<Director> sequenzaAttuale = new LinkedHashSet<>();
		this.sommaMigliore = 0;
		sequenzaAttuale.add(director);
		
		cerca(sequenzaAttuale, 0, soglia, director);
		
		return this.sequenzaMigliore;
		
	}

	private void cerca(Set<Director> sequenzaAttuale, int sommaAttuale, int soglia, Director ultimo) {
		// genero solo soluzioni valide
		if(sequenzaAttuale.size() > sequenzaMigliore.size()) {
			this.sequenzaMigliore = new ArrayList<>(sequenzaAttuale);
			this.sommaMigliore = sommaAttuale;
		}
		
		List<Director> vicini = new ArrayList<Director>(Graphs.neighborListOf(this.grafo, ultimo));
		
		for(Director d : vicini) {
			if(!sequenzaAttuale.contains(d)) {
				DefaultWeightedEdge e = this.grafo.getEdge(ultimo, d);
				int peso = (int) this.grafo.getEdgeWeight(e);
				if((sommaAttuale+peso) <= soglia) {
					sequenzaAttuale.add(d);
					cerca(sequenzaAttuale, sommaAttuale+peso, soglia, d);
					sequenzaAttuale.remove(d);
				}
			}
		}
		
		
	}

	public int getSommaMigliore() {
		return sommaMigliore;
	}
	
	
	
	

}
