package com.ipiecoles.java.eval.th330.controller;

import com.ipiecoles.java.eval.th330.model.Album;
import com.ipiecoles.java.eval.th330.model.Artist;
import com.ipiecoles.java.eval.th330.repository.AlbumRepository;
import com.ipiecoles.java.eval.th330.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private AlbumRepository albumRepository;

    @RequestMapping(
            method =RequestMethod.GET,
            value ="/albums/{id}"
    )
    public ModelAndView detailArtist(@PathVariable Long id){
        ModelAndView model = new ModelAndView("detailArtist");
        model.addObject("album",albumRepository.findById(id));
        return model;
    }


    @RequestMapping(
            method = RequestMethod.GET,
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Album createAlbum( Album album){
       /* if(album.getId() == null){
            //Création
            album = albumService.creerAlbum(album);
        }*/
        Album album1 = new Album();
        return album1 = albumService.creerAlbum(album);

        /*else {
            //Modification
            album = albumService.updateArtiste(artist.getId(), artist);
        }*/
        /*if(artist.getId() != null){
            //Création
            artist = artistService.updateArtiste(artist.getId(), artist);
        }*/
        //Redirection vers /artists/id
        //return new RedirectView("/artists/10");
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}"
    )
    //@ResponseStatus(HttpStatus.NO_CONTENT)
    public RedirectView deleteAlbum(
            @PathVariable Long id
    ){
        Optional<Album> album = albumRepository.findById(id);
        Artist artist = album.get().getArtist();
        albumService.deleteAlbum(id);
        return new RedirectView("/artists/" + artist.getId());
    }


}
