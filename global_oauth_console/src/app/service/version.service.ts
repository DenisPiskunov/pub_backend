import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable()
export class VersionService{
    constructor(private http: HttpClient) { }

    getServerVersion(): Observable<string>{
        return this.http.get(`${environment.apiURL}/version`, {responseType: 'text'});
    }

    getAuthServerVersion(): Observable<string>{
        return this.http.get(`${environment.authApiURL}/version`, {responseType: 'text'});
    }
}
