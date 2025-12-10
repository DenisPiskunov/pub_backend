import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {MasterAccountPage} from "../master-account/master-account-page";
import {MasterAccount} from "../master-account/master-account";
import {MasterAccountPageItem} from "../master-account/master-account-page-item";
import {environment} from "../../environments/environment";

@Injectable()
export class MasterAccountService {

    constructor(private http: HttpClient) { }

    getAccounts(offset: number, limit: number): Observable<MasterAccountPage> {
        return this.http.get<MasterAccountPage>(`${environment.apiURL}/m-accounts?limit=${limit}&offset=${offset}`);
    }

    getAccountById(uuid: string): Observable<MasterAccountPageItem> {
        return this.http.get<MasterAccountPageItem>(`${environment.apiURL}/m-accounts/${uuid}`);
    }

    createAccount(account: MasterAccount) {
        return this.http.post(`${environment.apiURL}/m-accounts`, account);
    }

    updateAccount(account: MasterAccount, uuid: string) {
        return this.http.put(`${environment.apiURL}/m-accounts/${uuid}`, account)
    }

    blockAccount(uuid: string) {
        return this.http.put(`${environment.apiURL}/m-accounts/block/${uuid}`, '')

    }

    deleteAccount(uuid: string) {
        return this.http.delete(`${environment.apiURL}/m-accounts/${uuid}`)
    }
}
