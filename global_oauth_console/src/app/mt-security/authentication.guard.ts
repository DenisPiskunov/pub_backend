import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {SecurityContextHolder} from './security-context-holder';
import {RouterSupport} from '../mt-support/router-support';

@Injectable({
    providedIn: 'root'
})
export class AuthenticationGuard implements CanActivate {

    constructor(private securityContextHolder: SecurityContextHolder, private routerSupport: RouterSupport) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot)
        : Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        if (this.securityContextHolder.securityContext.wasAuthenticated()) {
            return true;
        }
        this.routerSupport.navigateToLogin();
        return false;
    }
}
