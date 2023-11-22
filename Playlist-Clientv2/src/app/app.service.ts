import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Playlist } from "./playlist"
import { map } from 'rxjs/operators';
import { Song } from "./song";

@Injectable()
export class AppService{
    private urlEndpoint: string = "http://localhost:8080/playlist/lists"
    constructor(private http: HttpClient) { }

    getPlaylists():Observable<Playlist[]>{
        return this.http.get(this.urlEndpoint).pipe(
            map(response => response as Playlist[])
          );
    }

    getPlaylistByName(name:string):Observable<Playlist>{
        return this.http.get(this.urlEndpoint+"/"+name).pipe(
            map(response => response as Playlist)
        )
    }

    getSongs():Observable<Song[]>{
        return this.http.get(this.urlEndpoint+ "/songs").pipe(
            map(response => response as Song[])
        )
    }

    savePlaylist(playlist:Playlist):Observable<Playlist>{
        return this.http.post(this.urlEndpoint, playlist).pipe(
            map(response => response as Playlist)
        )
    }

    deleteService(name:string):void{
        this.http.delete(this.urlEndpoint+"/"+name)
    }
}