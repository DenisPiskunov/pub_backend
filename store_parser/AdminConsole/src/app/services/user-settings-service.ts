import {Injectable} from '@angular/core';
import {BaseHttpService} from './base-http-service';
import {Http} from '@angular/http';
import {
    USERS_CREATE_URL, USERS_DELETE_URL, USERS_GET_ALL_URL, USERS_SET_PASSWORD_URL,
    USERS_UPDATE_URL
} from '../support/constants';
import {UserDetails} from '../domain/user-details';
import {UserParamsBundle} from '../domain/user-params-bundle';
import {ConfigService} from './config-service';

@Injectable()
export class UserSettingsService  extends BaseHttpService {

    constructor(private http: Http, private configService: ConfigService) {
        super();
    }

    getAllUsers() {
        return this.http.get(this.configService.getUrl(USERS_GET_ALL_URL), {headers: this.header})
            .toPromise()
            .then(response => response.json() as UserDetails[])
            .catch(this.handleError);
    }

    saveUser(user: UserDetails, oldUsername: string) {
        const userParamsBundle: UserParamsBundle = {
            oldUsername: oldUsername,
            user: user
        };
        return this.http.post(this.configService.getUrl(USERS_UPDATE_URL), JSON.stringify(userParamsBundle), {headers: this.header})
            .toPromise()
            .then()
            .catch(this.handleError);
    }

    createUser(user: UserDetails) {
        return this.http.post(this.configService.getUrl(USERS_CREATE_URL), JSON.stringify(user), {headers: this.header})
            .toPromise()
            .then()
            .catch(this.handleError);
    }

    deleteUser(username: string) {
        return this.http.post(this.configService.getUrl(USERS_DELETE_URL), JSON.stringify(username), {headers: this.header})
            .toPromise()
            .then()
            .catch(this.handleError);
    }

    setUserPassword(username: string, password: string) {
        const userParamsBundle: UserParamsBundle = {
            username: username,
            password: password
        };
        return this.http.post(this.configService.getUrl(USERS_SET_PASSWORD_URL), JSON.stringify(userParamsBundle), {headers: this.header})
            .toPromise()
            .then()
            .catch(this.handleError);
    }
}
