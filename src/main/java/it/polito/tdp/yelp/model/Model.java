package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	YelpDao dao;
	SimpleWeightedGraph<User, DefaultWeightedEdge> graph;
	
	public Model() {
		this.dao = new YelpDao();
	}
	
	public void CreaGrafo(int n, int anno) {
		List<User> users = dao.getUsers(n);
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(graph, users);
		
		for(User u1: users) {
			for(User u2:users) {
				if(u1.getUserId().compareTo(u2.getUserId()) != 0) {
					Set<String>  l1 = u1.getRByYear(anno);
					l1.retainAll(u2.getRByYear(anno));
					if(l1.size() != 0) {
						Graphs.addEdge(graph, u1, u2, l1.size());
					}
				}
			}
		}
		System.out.println("Il grafo ha " + graph.vertexSet().size() + " e " + graph.edgeSet().size() + " archi");
	}

	public Set<User> getVertexSet() {
		return graph.vertexSet();
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

	public String simulazione(int n, int n2) {
		Simulazione sim = new Simulazione(n, n2, graph);
		sim.initialize();
		sim.run();
		
		String s = "\nLe interviste:\n";
		
		for(int i = 0; i < n; i++) {
			s += "\n L'intervistatore " + i + " ha intervistato " + sim.getInterviste().get(i) + " persone"; 
		}
		s += "\nIn " + sim.getGiorni() + " giorni.";
		return s;
	}
}
