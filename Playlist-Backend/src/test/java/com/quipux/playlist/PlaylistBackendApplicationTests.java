package com.quipux.playlist;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.quipux.playlist.controller.PlaylistController;
import com.quipux.playlist.dao.Playlist;
import com.quipux.playlist.dao.Song;

@SpringBootTest
class PlaylistBackendApplicationTests {
	
	@Autowired
	PlaylistController controller;
	
	Playlist playlist = null;
	List<Song> songs = null;
	
	@Test
	public void savePlaylistTest() {
		songs = new ArrayList<Song>();
		songs.add(new Song("Jerk", "Only Real", "Jerk At The End Of The Line", "2015", ""));
		playlist = new Playlist("P1", "D1", songs);
		assertThat(controller.savePlaylist(playlist)).isNotNull();
	}
	
	@Test
	public void findAllPlaylistsTest() {
		assertThat(controller.findAllPlaylists()).isNotNull();
	}
	
	@Test
	public void findByListNameTest() {
		assertThat(controller.findByListName("Moves Heaven")).isNotNull();
	}
	
	@Test
	public void deleteByListNameTest() {
		assertThat(controller.deleteByListName("Moves Heaven")).isNotNull();
	}
	
	@Test
	public void findAllSongsTest() {
		assertThat(controller.findAllSongs()).isNotNull();
	}

}
