import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {User} from '../domain/user';
import 'rxjs/add/operator/map';
import {BaseHttpService} from './base-http-service';
import {AUTH_URL, LOGOUT_URL} from '../support/constants';
import {UserResponse} from '../domain/user-response';
import {ConfigService} from './config-service';

@Injectable()
export class AuthService extends BaseHttpService {

    constructor(private http: Http, private configService: ConfigService) {
        super();
        this.header.set('Content-Type', 'application/x-www-form-urlencoded');
    }

    login(user: User) {
        const bodyParams = Object.keys(user).map((key) => {
            return encodeURIComponent(key) + '=' + encodeURIComponent(user[key]);
        }).join('&');

        return this.http.post(this.configService.getUrl(AUTH_URL), bodyParams, {headers: this.header})
            .map(data => {
                if (data.ok) {
                    const hasToken = data.json() && data.json().token && data.json().token === 'successful';
                    if (hasToken) {
                        const details: UserResponse = {
                            token: data.json().token,
                            userName: data.json().username,
                            userRoles: data.json().userRoles
                        };
                        return details;
                    }
                    return null;
                } else {
                    return null;
                }
            });
    }

    logout(username: string) {
        const bodyParams = encodeURIComponent('username') + '=' + encodeURIComponent(username);
        return this.http.post(LOGOUT_URL, bodyParams, {headers: this.header});
    }
}
