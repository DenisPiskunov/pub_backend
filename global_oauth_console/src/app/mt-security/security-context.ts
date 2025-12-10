import {Authentication} from './authentication';
import {ApplicationRepository} from '../mt-repository/application-repository';
import {RepositoryKey} from '../mt-repository/repository-key';
import {RepositoryType} from '../mt-repository/repository';

export class SecurityContext {

    private authentication: Authentication;

    constructor(private applicationRepository: ApplicationRepository) {
        this.authentication = new Authentication();
    }

    isAuthenticated(): boolean {
        return this.authentication.accessToken != null;
    }

    wasAuthenticated(): boolean {
        return this.getLogin() != null;
    }

    clear() {
        this.authentication.accessToken = null;
        this.authentication.login = null;
        this.applicationRepository.removeItem(RepositoryKey.LOGIN, RepositoryType.LOCAL);
        this.authentication.authorities = null;
        this.applicationRepository.removeItem(RepositoryKey.AUTHORITIES, RepositoryType.LOCAL);
    }

    getLogin(): string {
        if (this.authentication.login == null) {
            this.authentication.login = this.applicationRepository.getItem(RepositoryKey.LOGIN, RepositoryType.LOCAL);
        }
        return this.authentication.login;
    }

    setLogin(login: string) {
        this.applicationRepository.setItem(RepositoryKey.LOGIN, login, RepositoryType.LOCAL);
        this.authentication.login = login;
    }

    getAccessToken(): string {
        return this.authentication.accessToken;
    }

    setAccessToken(accessToken: string) {
        this.authentication.accessToken = accessToken;
    }

    hasAuthority(authority: string) {
        const authorities = this.getAuthorities();
        if (authorities) {
            return authorities.indexOf(authority) !== -1;
        }
        return false;
    }

    getAuthorities(): string[] {
        if (this.authentication.authorities == null) {
            this.authentication.authorities =
                JSON.parse(this.applicationRepository.getItem(RepositoryKey.AUTHORITIES, RepositoryType.LOCAL));
        }
        return this.authentication.authorities;
    }

    setAuthorities(authorities: string[]) {
        this.applicationRepository.setItem(RepositoryKey.AUTHORITIES, JSON.stringify(authorities), RepositoryType.LOCAL);
        this.authentication.authorities = authorities;
    }

}
