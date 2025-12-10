import {Injectable} from '@angular/core';
import {BaseHttpService} from './base-http-service';
import {Http} from '@angular/http';
import {ConfigService} from './config-service';
import {Utils} from '../support/utils';
import {DELETED_APPS_STATISTIC_V2, APPS_STATISTIC} from '../support/constants';
import {AppStatisticsV2} from '../domain/app-statistics-v2';
import {AppStatistics} from '../domain/app-statistics';

@Injectable()
export class AppStatisticsV2Service extends BaseHttpService {

    constructor(protected http: Http, private utils: Utils, private configService: ConfigService) {
        super();
    }

    getAppStatistics(fromDate: string, toDate: string) {
        return this.http.get(this.utils.strFormat([this.configService.getUrl(APPS_STATISTIC), fromDate, toDate]),
            {headers: this.header})
            .toPromise()
            .then(response => {
                return response.json() as AppStatisticsV2[];
            })
            .catch(this.handleError);
    }

    // getAppStatistics(fromDate: string, toDate: string) {
    //     return this.http.get(this.utils.strFormat([this.configService.getUrl(APPS_STATISTIC), fromDate, toDate]),
    //         {headers: this.header})
    //         .toPromise()
    //         .then(response => {
    //             return response.json() as AppStatistics[][];
    //         })
    //         .catch(this.handleError);
    // }
}

