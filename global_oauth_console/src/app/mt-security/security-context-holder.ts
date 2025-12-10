import {Injectable} from '@angular/core';
import {SecurityContext} from './security-context';
import {ApplicationRepository} from '../mt-repository/application-repository';

@Injectable({
    providedIn: 'root'
})
export class SecurityContextHolder {

    private readonly _securityContext: SecurityContext;

    constructor(applicationRepository: ApplicationRepository) {
        this._securityContext = new SecurityContext(applicationRepository);
    }

    get securityContext(): SecurityContext {
        return this._securityContext;
    }

    clearContext() {
        this._securityContext.clear();
    }

}
