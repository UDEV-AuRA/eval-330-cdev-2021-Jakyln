package com.ipiecoles.java.eval.th330.controller;

import com.ipiecoles.java.eval.th330.model.Artist;
import com.ipiecoles.java.eval.th330.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;

@Controller
public class ArtistController {
    @Autowired
    private ArtistService artistService;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/"
    )
    public String accueil(){

        return "accueil";
    }

    @RequestMapping(
            method =RequestMethod.GET,
            value ="/artists/{id}"
    )
    public ModelAndView detailArtist(@PathVariable Long id){
        ModelAndView model = new ModelAndView("detailArtist");
        model.addObject("artist",artistService.findById(id));
        return model;
    }

    /*@RequestMapping(
            method = RequestMethod.GET,
            value = "/employes",
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = "matricule"
    )
    public ModelAndView searchByMatricule(
            @RequestParam String name
    ){
        ModelAndView model = new ModelAndView("detail");
        model.addObject("employe", artistService.findByNameLikeIgnoreCase(name));
        return model;
    }*/


    @RequestMapping(
            method = RequestMethod.GET,
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = "name"

    )
    public Page<Artist> findArtistByName(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortProperty,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam String name
    ){
        Page artists =  artistService.findByNameLikeIgnoreCase(name, page,size,sortDirection,sortProperty);
        if(artists != null){
            return artists;
        }

        throw new EntityNotFoundException("L'artiste de nom " + name + " n'a pas été trouvé !");
    }



}
