package com.psss.ticketsystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.psss.ticketsystem.entities.Utente;
import com.psss.ticketsystem.services.UtenteService;
import com.psss.ticketsystem.services.TicketService;

@Controller
@RequestMapping(value = "dashboard")
public class DashboardController {

	@Autowired
	private UtenteService accountService;
	
	@Autowired
	private TicketService ticketService;
	
	@RequestMapping(value= {"","index"}, method=RequestMethod.GET)
	public String index(Authentication authentication, ModelMap modelMap) {
		Utente account = accountService.findByUsername(authentication.getName());
		int size_cliente = ticketService.cercaTicketCliente(account.getUsername()).size();
		int size_operatore = ticketService.cercaTicketOperatore(account.getUsername()).size();
		int size_aperti = ticketService.cercaTicketStatoAperto().size();
		
		modelMap.put("size_cliente", size_cliente);
		modelMap.put("size_operatore", size_operatore);
		modelMap.put("size_aperti", size_aperti);
		modelMap.put("account", account);
		return "dashboard.index";
	}
	
}
