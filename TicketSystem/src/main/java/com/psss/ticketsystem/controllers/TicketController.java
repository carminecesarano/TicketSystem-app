package com.psss.ticketsystem.controllers;


import java.util.Date;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.psss.ticketsystem.entities.Utente;
import com.psss.ticketsystem.entities.StatoTicketAperto;
import com.psss.ticketsystem.entities.StatoTicketChiuso;
import com.psss.ticketsystem.entities.StatoTicketInLavorazione;
import com.psss.ticketsystem.entities.Ticket;
import com.psss.ticketsystem.services.UtenteService;
import com.psss.ticketsystem.services.TicketService;

@Controller
@Component
@RequestMapping(value = "ticket")
public class TicketController implements ServletContextAware{

	@Autowired
	private TicketService ticketService;
		
	@Autowired
	private UtenteService utenteService;
	
	@RequestMapping(value= "send", method=RequestMethod.GET)
	public String send(ModelMap modelMap) {
		Ticket ticket = new Ticket();
		Utente account = utenteService.findByUsername("c01");
		
		modelMap.put("ticket", ticket);
		modelMap.put("account", account);
		return "ticket.send";
	}
	
	@RequestMapping(value= "send", method=RequestMethod.POST)
	public String send(@ModelAttribute("ticket") Ticket ticket, RedirectAttributes redirectAttributes) {
		try {
			if (!ticket.getTitle().equals("") && !ticket.getDescription().equals("")) {
				Utente account = utenteService.findByUsername("c01");
				System.out.println(account.getId());
				ticket.setCreatedDate(new Date());
				ticket.setCliente(account);
				StatoTicketAperto statoAperto = new StatoTicketAperto();
				ticket.setStatoTicket(statoAperto);
				ticket = ticketService.save(ticket);
				
				redirectAttributes.addFlashAttribute("success", "Ticket inviato correttamente.");
			} else {
				redirectAttributes.addFlashAttribute("err", "Invio del ticket fallito.");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			redirectAttributes.addFlashAttribute("err", "Invio del ticket fallito.");
		}
		return "redirect:/ticket/send";
	}	
	
	@RequestMapping(value= "details/{id}/{username}", method=RequestMethod.GET)
	public String details(@PathVariable("id") int id, @PathVariable("username") String username, ModelMap modelMap, Authentication authentication) {
		Utente account = utenteService.findByUsername(username);
		Ticket ticket = ticketService.findTicket(id);
		
		modelMap.put("ticket", ticket);
		modelMap.put("account", account);
		return "ticket.details";
	}
	
	@RequestMapping(value= "aggiorna_stato/{id}/{username}")
	public String aggiorna_stato(@PathVariable("username") String username, @PathVariable("id") int id, ModelMap modelMap, RedirectAttributes redirectAttributes) {
		try {
			Utente operatore = utenteService.findByUsername(username);
			Ticket ticket = ticketService.findTicket(id);
			
			
			switch (ticket.getStatoTicket().getId()) {
				case 1 :
					ticket.setStatoTicket(new StatoTicketAperto());
					break;
				case 2 :
					ticket.setStatoTicket(new StatoTicketInLavorazione());
					break;
				case 3:
					ticket.setStatoTicket(new StatoTicketChiuso());
					break;
			}
			
			if (ticket.getOperatore() == null || ticket.getOperatore().getId() == operatore.getId()) {
				
				ticket.aggiornaStatoTicket(ticket,operatore);
				ticketService.save(ticket);
				
				redirectAttributes.addFlashAttribute("success", "Stato ticket aggiornato a: " + ticket.getStatoTicket().getName());
				return "redirect:/ticket/details/{id}/{username}";
			} else {
				redirectAttributes.addFlashAttribute("err", "Il ticket è stato preso in carico da un altro operatore.");
				return "redirect:/ticket/details/{id}/{username}";
			}
		
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("err", "Errore presa in carico del ticket.");
			return "redirect:/ticket/details/{id}/{username}";
		}	
	}
			
	@Override
	public void setServletContext(ServletContext servletContext) {
	}
}
