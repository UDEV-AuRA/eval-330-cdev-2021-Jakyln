package com.ipiecoles.java.eval.th330.controller;

import com.ipiecoles.java.eval.th330.model.Album;
import com.ipiecoles.java.eval.th330.model.Artist;
import com.ipiecoles.java.eval.th330.repository.AlbumRepository;
import com.ipiecoles.java.eval.th330.service.AlbumService;
import com.ipiecoles.java.eval.th330.service.ArtistService;
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
    private ArtistService artistService;
    @Autowired
    private AlbumRepository albumRepository;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public RedirectView createAlbum(String albumTitle, Long artistId){

        Artist artist = artistService.findById(artistId);

        if(albumTitle.trim().length()>0){  //vérifie si le user n'a pas mis que des espaces
            Album album = new Album(albumTitle,artist);
            albumService.creerAlbum(album);
        }
        return new RedirectView("/artists/"+ artist.getId());
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
