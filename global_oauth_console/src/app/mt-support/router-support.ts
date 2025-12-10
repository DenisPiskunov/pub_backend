import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {SecurityContextHolder} from '../mt-security/security-context-holder';
import {ACCOUNT_READ, MASTER_ACCOUNT_READ, ROLE_READ} from '../mt-security/authorities';

@Injectable({
    providedIn: 'root'
})
export class RouterSupport {


    constructor(private router: Router, private securityContextHolder: SecurityContextHolder) {
    }

    navigateToLogin(): Promise<boolean> {
        this.securityContextHolder.clearContext();
        return this.router.navigate([LOGIN]);
    }

    navigateToConsoleByAuthorities(): Promise<boolean> {
        const securityContext = this.securityContextHolder.securityContext;
        if (securityContext.hasAuthority(ROLE_READ)) {
            return this.router.navigate([ROLES]);
        }
        if (securityContext.hasAuthority(ACCOUNT_READ)) {
            return this.router.navigate([ACCOUNTS]);
        }
        if (securityContext.hasAuthority(MASTER_ACCOUNT_READ)) {
            return this.router.navigate([MASTER_ACCOUNTS]);
        }
        return this.router.navigate([ERROR_403]);
    }
}

export const LOGIN = 'login';
export const ROLES = 'roles';
export const ACCOUNTS = 'accounts';
export const MASTER_ACCOUNTS = 'master-accounts';
export const ERROR_500 = 'error500';
export const ERROR_403 = 'error403';

