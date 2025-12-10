import {Injectable} from '@angular/core';
import {BaseHttpService} from './base-http-service';
import {Http} from '@angular/http';
import {
    MAILLIST_EDIT_URL,
    MAILLIST_GET_URL, PINGER_INTERVAL_GET_URL, PINGER_INTERVAL_SET_URL, TOP_PARSER_INTERVAL_GET_URL,
    TOP_PARSER_INTERVAL_SET_URL,
    UPDATER_INTERVAL_GET_URL,
    UPDATER_INTERVAL_SET_URL
} from '../support/constants';
import {MailListBundle} from '../domain/mail-list-bundle';
import {TaskSchedulerParams} from '../domain/task-scheduler-params';
import {ConfigService} from './config-service';


@Injectable()
export class MailingSettingService  extends BaseHttpService {
    constructor(private http: Http, private configService: ConfigService) {
        super();
    }

    editMailList(bundle: MailListBundle) {
        return this.http.post(this.configService.getUrl(MAILLIST_EDIT_URL), JSON.stringify(bundle), {headers: this.header})
            .toPromise()
            .catch(this.handleError);
    }

    getMailList() {
        return this.http.get(this.configService.getUrl(MAILLIST_GET_URL), {headers: this.header})
            .toPromise()
            .then(response => response.json() as string[])
            .catch(this.handleError);
    }
}
