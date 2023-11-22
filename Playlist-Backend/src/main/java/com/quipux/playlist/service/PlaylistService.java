package com.quipux.playlist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

import com.quipux.playlist.dao.Error;
import com.quipux.playlist.dao.Genres;
import com.quipux.playlist.dao.Playlist;
import com.quipux.playlist.dao.PlaylistSpotifyRequest;
import com.quipux.playlist.dao.Song;
import com.quipux.playlist.repository.PlaylistRepository;
import com.quipux.playlist.repository.SongRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

//Capa de lógica y negocio
@Service
public class PlaylistService {
	
	@Autowired
	PlaylistRepository playlistRepository;
	
	@Autowired
	SongRepository songRepository;
	
	//Valores en propiedades
	@Value("${url.api.spotify}")
	private String urlApiSpotify;
	
	@Value("${token.spotify}")
	private String tokenSpotify;

	//En este metodo no fue claro si se debía crear la playlist en Spotify o simplmente en bd
	public ResponseEntity<?> savePlaylist(@RequestBody Playlist playlist){
		Playlist newPlaylist = new Playlist();
		if(playlist != null && !playlist.getNombre().isBlank()) {
			//Se guarda nueva playlist en bd h2
			newPlaylist = playlistRepository.save(playlist);
		} else {
			//Se devuelve estrucutura básica code / msg
			Error errMsg = new Error(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
			return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(newPlaylist, HttpStatus.CREATED);
	}
	
	public ResponseEntity<?> findAllPlaylists(){
		try {
			String genre = getSpotifyGenres();
			//Se deben recorrer las canciones y agregar el género traido de Spotify, según el enunciado
			List<Playlist> listPlaylists = StreamSupport
					.stream(playlistRepository.findAll().spliterator(), false)
					.map(playlist -> {
	                    playlist.getCanciones().forEach(song -> {
	                        song.setGenero(genre);
	                    });
	                    return playlist;
	                })
	                .collect(Collectors.toList());
			//Validación de lista vacía
			if(listPlaylists.isEmpty()) {
				Error errMsg = new Error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
				return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(listPlaylists, HttpStatus.OK);
		} catch (Exception e) {
			Error errMsg = new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			return new ResponseEntity<>(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ResponseEntity<?> findByListName(@PathVariable("listName") String listName){
		Playlist playlistData = playlistRepository.findByNombre(listName);
		String genre = getSpotifyGenres();
		if(playlistData!= null && playlistData.getNombre()!=null) {
			playlistData.getCanciones().forEach(song ->{
				song.setGenero(genre);
			});
			return new ResponseEntity<>(playlistData, HttpStatus.OK);
		}
		Error errMsg = new Error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
		return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
	}
	
	@Transactional
	public ResponseEntity<?> deleteByListName(@PathVariable("listName") String listName){
		try {
			Playlist playlistData = playlistRepository.findByNombre(listName);
			if(playlistData==null || playlistData.getNombre()==null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			playlistRepository.deleteByNombre(listName);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			Error errMsg = new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			return new ResponseEntity<>(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	public ResponseEntity<?> findAllSongs(){
		try {
			List<Song> listSongs = new ArrayList<Song>();
			String genre = getSpotifyGenres();
			songRepository.findAll().forEach(song ->{
				song.setGenero(genre);
				listSongs.add(song);
			});
			
			if(listSongs.isEmpty()) {
				Error errMsg = new Error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
				return new ResponseEntity<>(errMsg, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(listSongs, HttpStatus.OK);
		} catch (Exception e) {
			Error errMsg = new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			return new ResponseEntity<>(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Función de creción de petición a Spotify para traer los géneros
	public String getSpotifyGenres() {
		Genres clientResponse = WebClient.builder()
				.baseUrl(urlApiSpotify)
				.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenSpotify)
				.build()
				.get()
				.uri("/recommendations/available-genre-seeds")
				.retrieve()
				.bodyToMono(Genres.class)
				.block();
		return String.join(",", clientResponse.getGenres());
	}
	
}
