package com.psss.ticketsystem.services;

import java.util.List;


import com.psss.ticketsystem.entities.Ticket;

public interface TicketService {
	
	public Ticket save(Ticket ticket);
	
	public List<Ticket> findAllTicket();
	
	public Ticket findTicket(int id);

}
