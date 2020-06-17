package com.psss.ticketsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.psss.ticketsystem.entities.Ticket;

@Repository("ticketRepository")
public interface TicketRepository extends CrudRepository<Ticket, Integer>{

	@Query("from Ticket")
	public List<Ticket> findAll();

}
