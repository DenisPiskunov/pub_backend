import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {SecurityContextHolder} from './security-context-holder';
import {RouterSupport} from '../mt-support/router-support';

@Injectable({
    providedIn: 'root'
})
export class AnonymousGuard implements CanActivate {

    constructor(private securityContextHolder: SecurityContextHolder, private routerSupport: RouterSupport) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot)
        : Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        if (this.securityContextHolder.securityContext.wasAuthenticated()) {
            this.routerSupport.navigateToConsoleByAuthorities();
            return false;
        }
        return true;
    }

}
