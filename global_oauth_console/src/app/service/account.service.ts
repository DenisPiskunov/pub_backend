import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {EnabledRole} from "../account/enabled-role";
import {AccountPageItem} from "../account/account-page-item";
import {AccountPage} from "../account/account-page";
import {Account} from "../account/account";
import {Authority} from '../authority/authority';

@Injectable()
export class AccountService{
    constructor(private http: HttpClient) { }

    getRoles(): Observable<EnabledRole[]> {
        return this.http.get<EnabledRole[]>(`${environment.apiURL}/roles/enabled`);
    }

    getMasterAccounts(): Observable<AccountPageItem[]> {
        return this.http.get<AccountPageItem[]>(`${environment.apiURL}/m-accounts/enabled`);
    }

    getAccounts(offset: number, limit: number): Observable<AccountPage> {
        return this.http.get<AccountPage>(`${environment.apiURL}/accounts?limit=${limit}&offset=${offset}`);
    }

    getAccountById(uuid: string): Observable<Account> {
        return this.http.get<Account>(`${environment.apiURL}/accounts/${uuid}`);
    }

    createAccount(account: Account) {
        return this.http.post(`${environment.apiURL}/accounts`, account);
    }

    updateAccount(account: Account) {
        return this.http.put(`${environment.apiURL}/accounts`, account);
    }

    getAuthorities(): Observable<Authority[]> {
        return this.http.get<Authority[]>(`${environment.apiURL}/accounts/authorities`);
    }
}
