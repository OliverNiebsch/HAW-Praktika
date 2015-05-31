package lpon.mps.spring.web.controller;

import java.util.List;
import java.util.Map;

import lpon.mps.spring.web.util.Mapper;
import lpon.mps.stammdatenadapter.entities.Artikel;
import lpon.mps.stammdatenadapter.repositories.ArtikelRepository;
import lpon.mps.stammdatenadapter.services.ArtikelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	@Autowired
	private ArtikelRepository userRepository;
	
	// Java 8 convenience in case we need a different format or different object keys in our json
	private static final Mapper<Artikel> mapper = new Mapper<Artikel>()  
			.map("name", Artikel::getBezeichnung)
			.map("id", Artikel::getId)
			.map("label", u -> "Label: "+u.getBezeichnung());

	@Autowired
	private ArtikelService userService;
		
    @RequestMapping(value="/users", 
            method=RequestMethod.GET, 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String,? extends Object>> getUsers(@RequestParam(required = false, value = "search") String needle) {
        return mapper.apply(userService.getUsers(needle)); // our object have name, id, label
    }
    

    @RequestMapping(value="/users/{id}", 
            method=RequestMethod.GET, 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Artikel getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id); // our object have name, id, label
    }
    

}