import {ConfigService} from './config-service';
import {Http} from '@angular/http';
import {BaseHttpService} from './base-http-service';
import {Injectable} from '@angular/core';
import {
    PINGER_INTERVAL_GET_URL, PINGER_INTERVAL_SET_URL, PINGER_RESTART,
    TOP_PARSER_INTERVAL_GET_URL, TOP_PARSER_INTERVAL_SET_URL, TOP_PARSER_RESTART, UPDATER_INTERVAL_GET_URL,
    UPDATER_INTERVAL_SET_URL, UPDATER_RESTART
} from '../support/constants';

@Injectable()
export class TaskManagerService  extends BaseHttpService {
    constructor(private http: Http, private configService: ConfigService) {
        super();
    }

    getUpdaterInterval() {
        return this.http.get(this.configService.getUrl(UPDATER_INTERVAL_GET_URL), {headers: this.header})
            .toPromise()
            .then(response => response.json() as number)
            .catch(this.handleError);
    }

    setUpdaterInterval(interval: number) {
        return this.http.post(this.configService.getUrl(UPDATER_INTERVAL_SET_URL), interval, {headers: this.header})
            .toPromise()
            .catch(this.handleError);
    }

    getTopParserInterval() {
        return this.http.get(this.configService.getUrl(TOP_PARSER_INTERVAL_GET_URL), {headers: this.header})
            .toPromise()
            .then(response => response.json() as number)
            .catch(this.handleError);
    }

    setTopParserInterval(interval: number) {
        return this.http.post(this.configService.getUrl(TOP_PARSER_INTERVAL_SET_URL), interval, {headers: this.header})
            .toPromise()
            .catch(this.handleError);
    }

    getPingerInterval() {
        return this.http.get(this.configService.getUrl(PINGER_INTERVAL_GET_URL), {headers: this.header})
            .toPromise()
            .then(response => response.json() as number)
            .catch(this.handleError);
    }

    setPingerInterval(interval: number) {
        return this.http.post(this.configService.getUrl(PINGER_INTERVAL_SET_URL), interval, {headers: this.header})
            .toPromise()
            .catch(this.handleError);
    }

    restartPinger(hard: Boolean) {
        return this.http.post(this.configService.getUrl(PINGER_RESTART), hard, {headers: this.header})
            .toPromise()
            .catch(this.handleError);
    }

    restartTopParser(hard: Boolean) {
        return this.http.post(this.configService.getUrl(TOP_PARSER_RESTART), hard, {headers: this.header})
            .toPromise()
            .catch(this.handleError);
    }

    restartUpdater(hard: Boolean) {
        return this.http.post(this.configService.getUrl(UPDATER_RESTART), hard, {headers: this.header})
            .toPromise()
            .catch(this.handleError);
    }

}
