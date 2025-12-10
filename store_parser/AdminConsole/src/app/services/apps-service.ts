import 'rxjs/add/operator/toPromise';
import {Injectable} from '@angular/core';
import {App} from '../domain/app';
import {Http} from '@angular/http';
import {
    CREATE_APPS_URL,
    GET_APPDATA_URL,
    MARK_APP_AS_INVALID_URL,
    MOBILE_APPS_URL,
    RESTORE_APP_URL,
    STATUS_AVAILABLE,
    STATUS_UNAVAILABLE
} from '../support/constants';
import {NewApp} from '../domain/new-app';
import {Utils} from '../support/utils';
import {BaseHttpService} from './base-http-service';
import {ConfigService} from './config-service';

@Injectable()
export class AppsService extends BaseHttpService {

    constructor(protected http: Http, private utils: Utils, private configService: ConfigService) {
        super();
    }

    getMobileApps(platform: string, available: boolean) {
        let status: string;
        if (available) {
            status = STATUS_AVAILABLE;
        } else {
            status = STATUS_UNAVAILABLE;
        }
        return this.http.get( this.utils.strFormat([this.configService.getUrl(MOBILE_APPS_URL), platform, status]), {headers: this.header})
            .toPromise()
            .then(response => {
                return response.json() as App[];
            })
            .catch(this.handleError);
    }

    createNewApps(newAppList: NewApp[]) {
        return this.http.post(this.configService.getUrl(CREATE_APPS_URL), JSON.stringify(newAppList), {headers: this.header})
            .toPromise()
            .catch(this.handleError);
    }

    getAppData(appId: string) {
        return this.http.get(this.utils.strFormat([this.configService.getUrl(GET_APPDATA_URL), appId]), {headers: this.header})
            .toPromise()
            .then( response => {
                return response.json();
            })
            .catch ( this.handleError);
    }

    restoreApp(appId: string) {
        return this.http.post(this.configService.getUrl(RESTORE_APP_URL), JSON.stringify(appId), {headers: this.header})
            .toPromise()
            .catch(this.handleError);
    }


    markAppAsInvalid(appId: string) {
        return this.http.post(this.configService.getUrl(MARK_APP_AS_INVALID_URL), JSON.stringify(appId), {headers: this.header})
            .toPromise()
            .catch(this.handleError);
    }


}
