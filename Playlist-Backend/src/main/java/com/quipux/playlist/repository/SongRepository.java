package com.quipux.playlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quipux.playlist.dao.Song;

public interface SongRepository extends JpaRepository<Song, Long>{

}
