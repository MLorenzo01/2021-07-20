package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.model.Event.EventType;

public class Simulazione {
	//variabili di ingresso
	int interv;
	int utenti;
	SimpleWeightedGraph<User, DefaultWeightedEdge> graph;
	//variabili di aggiornamento
	List<User> daIntervistare;
	PriorityQueue<Event> queue;
	Model model;
	//variabili di output
	TreeMap<Integer, Integer> interviste;
	int giorni;
	public Simulazione(int interv, int utenti, SimpleWeightedGraph<User, DefaultWeightedEdge> graph) {
		this.interv = interv;
		this.utenti = utenti;
		this.graph = graph;
		this.interviste = new TreeMap<>();
		queue = new PriorityQueue<>();
		daIntervistare = new ArrayList<>();
		for(User u: graph.vertexSet()) {
			daIntervistare.add(u);
		}
	}
	
	public void initialize() {
		for(int i = 0; i <= interv; i++) {
			interviste.put(i, 0);
			User u = daIntervistare.get((int)Math.random()*daIntervistare.size());
			daIntervistare.remove(u);
			queue.add(new Event(0, EventType.intervista, i, u));
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		int giorno = e.getGiorno();
		giorni = giorno;
		EventType type = e.getType();
		int giornalista = e.getGiornalista();
		User user = e.getUser();
		
		switch(type) {
		case intervista:
			if(daIntervistare.size() == 0) break;
			double rand = Math.random();
			if(rand < 0.6) {
				User nuovo = trovaVicino(user);
				interviste.replace(giornalista, interviste.get(giornalista), interviste.get(giornalista)+1);
				
				queue.add(new Event(giorno +1, EventType.intervista, giornalista, nuovo));

			}else if(rand < 0.8) {
				queue.add(new Event(giorno+1, EventType.ferie, giornalista, user));
			}else {
				queue.add(new Event(giorno+1, EventType.intervista, giornalista, user));
			}
			break;
		case ferie:
			if(daIntervistare.size() == 0) break;
			User nuovo = trovaVicino(user);
			queue.add(new Event(giorno+1, EventType.intervista, giornalista, nuovo));
			break;
		}
		
	}
	
	private User trovaVicino(User user) {
		User nuovo = null;
		List<User> users = getSimilarita(user);
		if(users.size() == 0 || (users.size() == 1 && !daIntervistare.contains(users.get(0)))){
			nuovo = daIntervistare.get((int)(Math.random()*daIntervistare.size()));
			daIntervistare.remove(nuovo);
		}else if(users.size() == 1 && daIntervistare.contains(users.get(0))) {
			nuovo = users.get(0);
		}else if(users.size() >1) {
			List<User> validi = new ArrayList<>();
			for(User u: users) {
				if(daIntervistare.contains(u)) {
					validi.add(u);
				}
			}
			if(validi.size() == 1)
				nuovo = validi.get(0);
			else if(validi.size() > 1){
				nuovo = validi.get((int)(Math.random()*validi.size()));
			}else
				nuovo = daIntervistare.get((int)(Math.random()*daIntervistare.size()));
			}
		daIntervistare.remove(nuovo);
		return nuovo;
	}

	public void setModel(Model model) {
    	this.model = model;
    }

	public TreeMap<Integer, Integer> getInterviste() {
		return interviste;
	}

	public void setInterviste(TreeMap<Integer, Integer> interviste) {
		this.interviste = interviste;
	}

	public int getGiorni() {
		return giorni;
	}

	public void setGiorni(int giorni) {
		this.giorni = giorni;
	}
	
	public List<User> getSimilarita(User value) {
		List<User> user = new ArrayList<>();
		int max = 0;
		List<User> vicini = Graphs.neighborListOf(graph, value);
		for(User u: vicini) {
			double peso = graph.getEdgeWeight(graph.getEdge(u, value));
			if(peso >= max) {
				if(peso > max) {
					user.clear();
					user.add(u);
					max = (int)peso;
				}else {
					user.add(u);
				}
			}
		}
		return user;
	}
}
