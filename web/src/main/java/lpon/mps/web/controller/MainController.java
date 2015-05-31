package lpon.mps.web.controller;

import java.util.List;

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
	private ArtikelRepository artikelRepository;

	@Autowired
	private ArtikelService artikelService;

	@RequestMapping(value = "/artikel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Artikel> getArtikel(
			@RequestParam(required = false, value = "search") String needle) {
		return artikelService.getAllArtikel(needle);
	}

	@RequestMapping(value = "/artikel/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Artikel getUser(@PathVariable("id") Long id) {
		return artikelService.getArtikelById(id);
	}

}