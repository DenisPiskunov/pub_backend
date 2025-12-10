import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AccessTokenResponse} from './access-token-response';
import {map} from 'rxjs/operators';
import {environment} from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class RefreshTokensService {

    constructor(private httpClient: HttpClient) {
    }

    refreshTokens(): Observable<string> {
        return this.httpClient
            .post<AccessTokenResponse>(REFRESH_TOKENS_URL, {})
            .pipe(
                map(response => response.accessToken)
            );
    }
}

export const REFRESH_TOKENS_URL = `${environment.authApiURL}/refresh-tokens`;
