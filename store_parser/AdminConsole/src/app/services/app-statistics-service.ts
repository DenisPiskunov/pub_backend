import {Injectable} from '@angular/core';
import {BaseHttpService} from './base-http-service';
import {Http} from '@angular/http';
import {ConfigService} from './config-service';
import {DELETED_APPS_STATISTIC} from '../support/constants';
import {Utils} from '../support/utils';
import {AppStatistics} from '../domain/app-statistics';

@Injectable()
export class AppStatisticsService extends BaseHttpService {

    constructor(protected http: Http, private utils: Utils, private configService: ConfigService) {
        super();
    }

    getDeletedStatistic(fromDate: string, toDate: string) {
        return this.http.get(this.utils.strFormat([this.configService.getUrl(DELETED_APPS_STATISTIC), fromDate, toDate]),
            {headers: this.header})
            .toPromise()
            .then(response => {
                return response.json() as AppStatistics[];
            })
            .catch(this.handleError);
    }


}
