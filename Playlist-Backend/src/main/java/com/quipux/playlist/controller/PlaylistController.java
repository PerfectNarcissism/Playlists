package com.quipux.playlist.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quipux.playlist.dao.Playlist;
import com.quipux.playlist.dao.Song;
import com.quipux.playlist.repository.PlaylistRepository;
import com.quipux.playlist.repository.SongRepository;

import jakarta.transaction.Transactional;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/playlist/lists")
public class PlaylistController {
	
	@Autowired
	PlaylistRepository playlistRepository;
	
	@Autowired
	SongRepository songRepository;
	
	@PostMapping("")
	public ResponseEntity<Playlist> savePlaylist(@RequestBody Playlist playlist){
		Playlist newPlaylist = new Playlist();
		if(!playlist.getNombre().isBlank() || playlist.getNombre()!=null) {
			newPlaylist = playlistRepository.save(playlist);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(newPlaylist, HttpStatus.CREATED);
	}
	
	@GetMapping("")
	public ResponseEntity<List<Playlist>> findAllPlaylists(){
		try {
			List<Playlist> listPlaylists = new ArrayList<Playlist>();
			playlistRepository.findAll().forEach(listPlaylists::add);
			
			if(listPlaylists.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(listPlaylists, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{listName}")
	public ResponseEntity<Playlist> findByListName(@PathVariable("listName") String listName){
		Playlist playlistData = playlistRepository.findByNombre(listName);
		if(playlistData!= null && playlistData.getNombre()!=null) {
			return new ResponseEntity<>(playlistData, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@Transactional
	@DeleteMapping("/{listName}")
	public ResponseEntity<Playlist> deleteByListName(@PathVariable("listName") String listName){
		try {
			Playlist playlistData = playlistRepository.findByNombre(listName);
			if(playlistData==null || playlistData.getNombre()==null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			playlistRepository.deleteByNombre(listName);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping("/songs")
	public ResponseEntity<List<Song>> findAllSongs(){
		try {
			List<Song> listSongs = new ArrayList<Song>();
			songRepository.findAll().forEach(listSongs::add);
			
			if(listSongs.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(listSongs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
