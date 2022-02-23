package com.ipiecoles.java.eval.th330.controller;

import com.ipiecoles.java.eval.th330.model.Album;
import com.ipiecoles.java.eval.th330.model.Artist;
import com.ipiecoles.java.eval.th330.service.AlbumService;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class ArtistController {
    @Autowired
    private ArtistService artistService;

    @Autowired
    private AlbumService albumService;

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
        if(id != null){
            model.addObject("artist",artistService.findById(id));
            return model;
        }
        throw new EntityNotFoundException("L'artiste d'identifiant " + id + " n'existe pas !");
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/artists",
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = "name"

    )
    public ModelAndView findArtistByName(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortProperty,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam String name
    ){
        ModelAndView model = new ModelAndView("listeArtists");
        Page artists =  artistService.findByNameLikeIgnoreCase(name, page,size,sortProperty,sortDirection);

        model.addObject("start",page * size + 1 );
        model.addObject("end",page * size + artists.getNumberOfElements() );
        model.addObject("artists",artists );
        return model;
    }

    // Liste des artistes : ex 3
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/artists"
    )
    public ModelAndView listArtists(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sortProperty,
            @RequestParam String sortDirection
    ){
        Page<Artist> artists = artistService.findAllArtists(page,size,sortProperty,sortDirection);
        ModelAndView model = new ModelAndView("listeArtists");
        model.addObject("start",page * size + 1 );
        model.addObject("end",page * size + artists.getNumberOfElements() );
        model.addObject("artists",artists ); // derniere page aura pas forcement 10 elements
        return model;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/artists/new"
    )
    public ModelAndView newArtist(){ // quand on va dans artists/new , ca nous redirige vers un détail d'artise vide. Ensuite le btn enregistrer utilise la fonction createArtist  (POST)
        ModelAndView model = new ModelAndView("detailArtist");
        Artist artist = new Artist();
        model.addObject("artist", artist);
        return model;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/artists",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public RedirectView createArtist(Artist artist){

        if(artistService.checkExistsByName(artist.getName())){
            throw new EntityExistsException("Il existe déjà un artiste identique en base");
        }

        if(artist.getName().trim().length()>0){ //vérifie si le user n'a pas mis que des espaces
            try {
                if(artist.getId() == null){
                    //Création
                    artist = artistService.creerArtiste(artist);
                }
                else {
                    //Modification
                    artist = artistService.updateArtiste(artist.getId(), artist);
                }
            }
            catch(Exception e){
                throw new IllegalArgumentException("Problème lors de la sauvegarde de l'artiste");
            }
        }
        else{
            throw new IllegalArgumentException("Veuillez remplir le champ du nom de l'artiste");
        }
        return new RedirectView("/artists/" + artist.getId());
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/artists/{id}/delete"
    )
    public RedirectView deleteArtist(@PathVariable Long id){

        Artist artist = artistService.findById(id);
        Set<Album> albums = artist.getAlbums();

        for (Album album : albums) {
            albumService.deleteAlbum(album.getId());
        }

        albumService.deleteAlbum(id);
        artistService.deleteArtist(id);
        return new RedirectView("/artists?page=0&size=10&sortProperty=name&sortDirection=ASC");
    }
}
