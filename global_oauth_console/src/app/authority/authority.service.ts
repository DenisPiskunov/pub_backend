import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Authority} from './authority';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AuthorityService {

    constructor(private httpClient: HttpClient) {

    }

    getAuthorities(): Observable<Authority[]> {
        return this.httpClient.get<Authority[]>(`${environment.apiURL}/authorities`);
    }

}
