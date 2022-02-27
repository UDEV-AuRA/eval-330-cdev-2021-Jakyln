package com.ipiecoles.java.eval.th330.service;

import com.ipiecoles.java.eval.th330.model.Album;
import com.ipiecoles.java.eval.th330.model.Artist;
import com.ipiecoles.java.eval.th330.repository.AlbumRepository;
import com.ipiecoles.java.eval.th330.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    public Page<Artist> findAllArtists(Integer page, Integer size, String sortProperty, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection),sortProperty);
        Pageable pageable = PageRequest.of(page,size,sort);
        return artistRepository.findAll(pageable);
    }

    public Artist findById(Long id) {
        Optional<Artist> artist = this.artistRepository.findById(id);
        if(!artist.isPresent()){
            throw new EntityNotFoundException("Impossible de trouver l'artiste d'identifiant " + id);
        }
        return artist.get();
    }

    public Long countAllArtists() {
        return artistRepository.count();
    }

    public Artist creerArtiste(Artist artist) {
        return artistRepository.save(artist);
    }

    public void deleteArtist(Long id) {
        artistRepository.deleteById(id);
    }

    public Artist updateArtiste(Long id, Artist artist) {
        return artistRepository.save(artist);
    }

    public Page<Artist> findByNameLikeIgnoreCase(String name, Integer page, Integer size, String sortProperty, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection),sortProperty);
        Pageable pageable = PageRequest.of(page,size,sort);
        return artistRepository.findByNameContainingIgnoreCase(name, pageable);
    }


    public Boolean checkExistsByName(String name){
        return artistRepository.existsByName(name);

    }



//    @Query(value = "SELECT * FROM artist a WHERE a.name LIKE :name% ", nativeQuery = true)
//    Page<Artist> findByName(@Param("name") String name, Pageable pageable);
}
