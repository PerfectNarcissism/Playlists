package com.quipux.playlist.controller;


import org.springframework.beans.factory.annotation.Autowired;

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
import com.quipux.playlist.service.PlaylistService;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/playlist/lists")
public class PlaylistController {
	
	@Autowired
	PlaylistService playlistService;
	
	
	//API para la creación de playlist
	@PostMapping("")
	public ResponseEntity<?> savePlaylist(@RequestBody Playlist playlist){
		return playlistService.savePlaylist(playlist);
	}
	
	//API para la consulta de todas las playlists
	@GetMapping("")
	public ResponseEntity<?> findAllPlaylists(){
		return playlistService.findAllPlaylists();
	}
	
	//API para la consulta por nombre de playlist
	@GetMapping("/{listName}")
	public ResponseEntity<?> findByListName(@PathVariable("listName") String listName){
		return playlistService.findByListName(listName);
	}
	
	//API para la eliminacón de playlist por nombre
	@DeleteMapping("/{listName}")
	public ResponseEntity<?> deleteByListName(@PathVariable("listName") String listName){
		return playlistService.deleteByListName(listName);		
	}
	
	//API para la consulta de todas las canciones en bd
	@GetMapping("/songs")
	public ResponseEntity<?> findAllSongs(){
		return playlistService.findAllSongs();
	}

}
