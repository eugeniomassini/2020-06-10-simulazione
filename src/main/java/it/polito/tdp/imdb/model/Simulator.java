package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {

	//Modello del mondo
	private List<Actor> daIntervistare;

	private Map<Integer, Actor> intervistati;

	private int giorniPausa;


	public void run(int numeroGiorni, String genere, Graph<Actor, DefaultWeightedEdge> grafo) {

		daIntervistare = new ArrayList<Actor>(grafo.vertexSet());

		intervistati = new HashMap<Integer, Actor>();

		this.giorniPausa=0;

		for(int i =0; i<numeroGiorni; i++) {
			Random random = new Random();

			//			Primo giorno oppure dopo il giorno di pausa,
			//			scelgo tra quelli rimasti da intervistare
			if(i==0 || intervistati.get(i-1)==null) {
				intervistati.put(i, daIntervistare.get(random.nextInt(daIntervistare.size())));
				daIntervistare.remove(intervistati.get(i));
			}
			else {
				//altrimenti controllo se non sono nulli 
				if(intervistati.get(i-1)!=null && intervistati.get(i-2)!=null) {
					//Se non lo sono controllo i gender
					if(intervistati.get(i-1).getGender().equals(intervistati.get(i-2).getGender())) {
						//Se anche i gendere true 0.9 mi fermo 0.1 intervisto
						if(Math.random()<0.9) {
							intervistati.put(i, null);
							this.giorniPausa++;
						}//Intervisto sempre 60/40
						else if(Math.random()<0.6) {
							intervistati.put(i, daIntervistare.get(random.nextInt(daIntervistare.size())));
							daIntervistare.remove(intervistati.get(i));
						}
						else {
							intervistati.put(i,consigliato(intervistati.get(i-1), grafo));
							daIntervistare.remove(intervistati.get(i));
						}
					}
				}//Se non sono al primo giorno e la condizione di gender false faccio normale
				else if(Math.random()<0.6) {
					intervistati.put(i, daIntervistare.get(random.nextInt(daIntervistare.size())));
					daIntervistare.remove(intervistati.get(i));
				}
				else {
					intervistati.put(i,consigliato(intervistati.get(i-1), grafo));
					daIntervistare.remove(intervistati.get(i));

				}
			}
		}
	}


	private Actor consigliato(Actor actor, Graph<Actor, DefaultWeightedEdge> grafo) {

		List<Actor> vicini = new ArrayList<Actor>();
		vicini = Graphs.neighborListOf(grafo, actor);
		Actor consigliato = new Actor(null, null, null, null);
		double max=0.0;

		for(Actor a: vicini) {
			double peso = grafo.getEdgeWeight(grafo.getEdge(actor, a));
			if(peso>max) {
				peso=max;
				consigliato = a;
			}
		}

		return consigliato;
	}


	public List<Actor> getIntervistati() {

		List<Actor> output = new ArrayList<Actor>();

		for(Actor a: this.intervistati.values()) {
			if(a!=null) {
				output.add(a);
			}
			//			else if(a==null) {
			//				this.giorniPausa++;
			//			}
		}

		return output;
	}


	public int getGiorniPausa() {
		return giorniPausa;
	}



}
