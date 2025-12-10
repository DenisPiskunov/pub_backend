import {Injectable} from '@angular/core';
import {BASE_URL_SETTING, ROLE_ADMIN, USER_DETAILS, USER_PREFS} from '../support/constants';
import {SessionData} from './session-data';
import {AuthService} from './auth.service';
import {UserResponse} from '../domain/user-response';
import {Router} from '@angular/router';
import {Utils} from '../support/utils';
import {isNull, isUndefined} from 'util';
import {Logger} from "protractor/built/logger";

@Injectable()
export class UserService {
    private _isUserLoggedIn: boolean;
    private accessToken: string;

    get isUserLoggedIn(): boolean {
        return  this._isUserLoggedIn;
    }

    constructor(private  sessionData: SessionData, private authService: AuthService, private router: Router, private utils: Utils) {
        this.getCurrentUser();
    }

    get isAdmin(): boolean {
        let isAdmin = false;
        if (isUndefined(this.sessionData.currentUserRoles)) {
            const userDetails = JSON.parse(localStorage.getItem(USER_DETAILS));
            this.setUserLoggedIn(userDetails);
        }
        this.sessionData.currentUserRoles.forEach( role => {
            if (role === 'ROLE_' + ROLE_ADMIN) {
                isAdmin = true;
            }
        });
        return isAdmin;
    }

    hasRole(_role: String): boolean {
        let result = false;
        if (isUndefined(this.sessionData.currentUserRoles)) {
            const userDetails = JSON.parse(localStorage.getItem(USER_DETAILS));
            this.setUserLoggedIn(userDetails);
        }
        this.sessionData.currentUserRoles.forEach( role => {

            if (role === 'ROLE_' + ROLE_ADMIN) {
                result = true;
            }

            if (role === 'ROLE_' + _role) {
            }
        });
        return result;
    }

    setUserLoggedIn(userDetails: UserResponse) {
        if (isUndefined(userDetails) || isNull(userDetails)) {
            localStorage.removeItem(USER_DETAILS);
            localStorage.removeItem(BASE_URL_SETTING);
            this._isUserLoggedIn = false;
            this.router.navigate(['login']);
            return;
        }
        this.accessToken = userDetails.token;
        localStorage.setItem(USER_DETAILS, JSON.stringify(userDetails));
        this._isUserLoggedIn = true;
        this.sessionData.currentUser = userDetails.userName;
        this.sessionData.currentUserRoles = userDetails.userRoles;
        this.loadUserPrefs(userDetails.userName);
    }

    logoutUser(username: string) {
        this.authService.logout(username);
        this.accessToken = null;
        this.sessionData.currentUser = '';
        this.sessionData.currentUserRoles = [];
        localStorage.removeItem(USER_DETAILS);
        this._isUserLoggedIn = false;
        this.router.navigate(['login']);
    }

    loadUserPrefs(username: string) {
        this.sessionData.currentUserPrefs = JSON.parse(localStorage.getItem( this.utils.strFormat([USER_PREFS, username])));
    }

    getCurrentUser(): string {
        if (isUndefined(this.sessionData.currentUser)) {
            const userDetails = JSON.parse(localStorage.getItem(USER_DETAILS));
            this.setUserLoggedIn(userDetails);
        }
        return this.sessionData.currentUser;
    }
}
