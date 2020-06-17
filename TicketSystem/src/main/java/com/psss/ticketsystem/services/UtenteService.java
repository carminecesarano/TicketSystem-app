package com.psss.ticketsystem.services;

import com.psss.ticketsystem.entities.Utente;


public interface UtenteService {
	
	public Iterable<Utente> findAll();
	
	public Utente findByUsername(String username);

	public Utente find(int id);
}
