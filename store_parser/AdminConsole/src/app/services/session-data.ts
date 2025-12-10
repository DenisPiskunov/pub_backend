import {Injectable} from '@angular/core';
import {UserPrefs} from '../domain/user-prefs';

@Injectable()
export class SessionData {
    get baseUrl(): string {
        return this._baseUrl;
    }

    set baseUrl(value: string) {
        this._baseUrl = value;
    }
    get currentUserPrefs(): UserPrefs {
        return this._currentUserPrefs;
    }

    set currentUserPrefs(value: UserPrefs) {
        this._currentUserPrefs = value;
    }
    get currentUserRoles(): string[] {
        return this._currentUserRoles;
    }

    set currentUserRoles(value: string[]) {
        this._currentUserRoles = value;
    }
    get currentUser(): string {
        return this._currentUser;
    }

    set currentUser(value: string) {
        this._currentUser = value;
    }
    get appStatus(): boolean {
        return this._appStatus;
    }

    set appStatus(value: boolean) {
        this._appStatus = value;
    }
    get currentPlatform(): string {
        return this._currentPlatform;
    }

    set currentPlatform(value: string) {
        this._currentPlatform = value;
    }
    private _currentPlatform: string;
    private _appStatus: boolean;
    private _currentUser: string;
    private _currentUserRoles: string[];
    private _currentUserPrefs: UserPrefs;
    private _baseUrl: string;
}
