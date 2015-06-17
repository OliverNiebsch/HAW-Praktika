package lpon.mps.spring.web.controller;

import java.util.ArrayList;
import java.util.List;

import lpon.mps.auftragsverwaltung.entities.Angebot;
import lpon.mps.auftragsverwaltung.repositories.AngebotRepository;
import lpon.mps.auftragsverwaltung.services.AngebotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MpsRestApi {

	@Autowired
	private AngebotRepository angebotRepo;
	
	@Autowired
	private AngebotService angebotService;
		
    @RequestMapping(value="/angebot", 
            method=RequestMethod.GET, 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Angebot> getUsers() {
    	ArrayList<Angebot> angebotList = new ArrayList<Angebot>();
    	long id = 1;
    	Angebot a = angebotService.getAngebot(id);
    	
    	while (a != null) {
    		angebotList.add(a);
    		a = angebotService.getAngebot(++id);
    	}
    	
    	return angebotList;
    }
    
    @RequestMapping(value="/angebot/{id}", 
            method=RequestMethod.GET, 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Angebot getUser(@PathVariable("id") Long id) {
        return angebotService.getAngebot(id);
    }
}