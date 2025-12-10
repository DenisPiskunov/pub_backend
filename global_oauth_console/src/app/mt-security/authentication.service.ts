import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {map} from 'rxjs/operators';
import {AccessTokenResponse} from './access-token-response';

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    constructor(private httpClient: HttpClient) {
    }

    signIn(login: string, password: string): Observable<string> {
        return this.httpClient
            .post<AccessTokenResponse>(SIGN_IN_URL, {login, password})
            .pipe(
                map(response => response.accessToken)
            );
    }

    signOut(session: Session): Observable<{}> {
        return this.httpClient.post<{}>(`${environment.authApiURL}/sign-out`, {session});
    }
}

export enum Session {
    CURRENT = 'CURRENT',
    ALL = 'ALL'
}

export const SIGN_IN_URL = `${environment.authApiURL}/sign-in`;


