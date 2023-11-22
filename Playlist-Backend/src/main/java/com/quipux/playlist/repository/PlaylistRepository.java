package com.quipux.playlist.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.quipux.playlist.dao.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Long>{
	
	Playlist findByNombre(String nombre);
	
	void deleteByNombre(String nombre);

}
