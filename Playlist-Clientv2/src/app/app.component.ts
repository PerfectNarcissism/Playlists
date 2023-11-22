import { Component, OnInit } from '@angular/core';
import { AppService } from './app.service';
import { Playlist } from './playlist';
import { Song } from './song';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'Playlist-Clientv2';
  isShownCreate: boolean = false;
  isShownFind: boolean = false;
  allSongs: Song[] = [];

  playlists: Playlist[] = [];
  selectedSongs: Song[] = [];

  newPlaylist: Playlist = new Playlist;
  newSongs: Song[] = [];

  findName:string = "";
  findedPlaylist: Playlist = new Playlist;
  findedPlaylistSongs: string = "";

  constructor(private appService: AppService) { }

  ngOnInit() {
    this.findAllPlaylists();
    this.appService.getSongs().subscribe(
      items => this.allSongs = items
    )
  }

  findAllPlaylists(){
    this.appService.getPlaylists().subscribe(
      items => {this.playlists = items
      console.log(this.playlists)}
   );
  }

  showCreateDiv(){
    this.isShownCreate = !this.isShownCreate;
    this.newPlaylist = new Playlist;
    this.newSongs = [];
  }
  showFindDiv(){
    this.isShownFind = !this.isShownFind;
    this.findName = "";
    this.findedPlaylist= new Playlist;
  }

  showSong(songs:Song[]){
    this.selectedSongs=songs;
    console.log(songs)
  }

  addSong(song:Song){
    /*var index = this.allSongs.indexOf(song, 0);
    if(index > -1) {
      this.allSongs.splice(index, 1);
    }*/
    this.newSongs.push(song);
  }

  savePlaylist(){
    this.newPlaylist.canciones = this.newSongs;
    this.appService.savePlaylist(this.newPlaylist).subscribe(
      resp => this.playlists.push(resp)
    )
  }

  findPlaylist(){
    this.appService.getPlaylistByName(this.findName).subscribe(
      resp => {
        this.findedPlaylist=resp
        for(let song of this.findedPlaylist.canciones){
          this.findedPlaylistSongs += song.titulo +", "+song.artista+". "
        }
      }
    )
  }

  deletePlaylist(playlist:Playlist){
    this.appService.deleteService(playlist.nombre).subscribe(
      result => console.log(result)
    );
    this.findAllPlaylists();
  }
  
}
