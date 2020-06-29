package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private ImdbDAO dao;
	private Map<Integer, Actor> idMap;
	
	public Model() {
		dao = new ImdbDAO();
		idMap = new HashMap<Integer, Actor>();
		dao.listAllActors(idMap);
	}

	public void creaGrafo(String genere) {
		
		grafo = new SimpleWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//Inserisco i vertici
		Graphs.addAllVertices(this.grafo, dao.getVertici(genere, idMap));
		
		//Metodo Veloce: Inserisco gli archi che prendo da una query
		for(Arco a: dao.getArchi(genere, idMap)){
			//TODO così elimino le coppie ridondanti delle query?
			DefaultWeightedEdge e = this.grafo.getEdge(a.getA1(), a.getA2());
			
			if(e==null) {
				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
			
		}
		
		
		
	}

	public List<Actor> getAttoriSimili(Actor partenza) {
		
		List<Actor> attoriSimili = new ArrayList<Actor>();
		
		//Metodo 1: visita per ampiezza
		
		  BreadthFirstIterator<Actor, DefaultWeightedEdge> iterator = new
		  BreadthFirstIterator<Actor, DefaultWeightedEdge>(this.grafo, partenza);
		  while(iterator.hasNext()) {
				  attoriSimili.add(iterator.next()); 
			  }
		 //elimino il punto di partenza
		  attoriSimili.remove(0);
		
		
		//Metodo 2: connectivity inspector, restituisce tutti i vertici maggiormente connessi
		/*
		 * ConnectivityInspector<Actor, DefaultWeightedEdge> inspector = new
		 * ConnectivityInspector<Actor, DefaultWeightedEdge>(this.grafo);
		 * attoriSimili.addAll(inspector.connectedSetOf(partenza));
		 */
		
		Collections.sort(attoriSimili, new Comparator<Actor>(){

			@Override
			public int compare(Actor o1, Actor o2) {
				// TODO Auto-generated method stub
				return o1.lastName.compareTo(o2.lastName);
			}
			
		});
		
		return attoriSimili;
	}

	public List<String> getGeneri() {
		
		List<String> generi = dao.getGeneri();
		
		//esercizio di stile, potrei farlo nella query
		Collections.sort(generi, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				return o1.compareTo(o2);
			}
			
		});
		
		return generi;
	}

	public Set<Actor> vertexSet() {
		return this.grafo.vertexSet();
	}
	
	public Set<DefaultWeightedEdge> edgeSet() {
		return this.grafo.edgeSet();
	}

	public List<Actor> getAttori() {
		
		List<Actor> attori = new ArrayList<Actor>(this.grafo.vertexSet());
		
		Collections.sort(attori, new Comparator<Actor>() {

			@Override
			public int compare(Actor o1, Actor o2) {
				// TODO Auto-generated method stub
				return o1.lastName.compareTo(o2.lastName);
			}
			
		});
		
		return attori;
	}
	
}
