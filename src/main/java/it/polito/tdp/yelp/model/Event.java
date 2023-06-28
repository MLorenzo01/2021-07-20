package it.polito.tdp.yelp.model;

import java.util.Objects;

public class Event implements Comparable<Event>{

	public enum EventType{
		intervista,
		ferie
	}
	
	int giorno;
	EventType type;
	int giornalista;
	User user;
	
	public Event(int giorno, EventType type, int giornalista, User user) {
		super();
		this.giorno = giorno;
		this.type = type;
		this.giornalista = giornalista;
		this.user = user;
	}
	public int getGiorno() {
		return giorno;
	}
	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public int getGiornalista() {
		return giornalista;
	}
	public void setGiornalista(int giornalista) {
		this.giornalista = giornalista;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public int hashCode() {
		return Objects.hash(giornalista, giorno, type, user);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		return giornalista == other.giornalista && giorno == other.giorno && type == other.type
				&& Objects.equals(user, other.user);
	}
	@Override
	public int compareTo(Event o) {
		return this.giorno-o.getGiorno();
	}
	
	
	
}
