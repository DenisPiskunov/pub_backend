
import {Injectable} from '@angular/core';
import {BaseHttpService} from './base-http-service';
import {ConfigService} from './config-service';
import {Utils} from '../support/utils';
import {Http} from '@angular/http';
import {APPS_CATEGORIES_GET_URL, APPS_CATEGORIES_SET_URL} from '../support/constants';
import {AppsCategory} from '../domain/apps-category';
import {log} from 'util';

@Injectable()
export class AppsCategoriesService extends BaseHttpService {
    constructor(protected http: Http, private utils: Utils, private configService: ConfigService) {
        super();
    }

    getAppsCategories() {
        return this.http.get(this.configService.getUrl(APPS_CATEGORIES_GET_URL), {headers: this.header})
            .toPromise()
            .then(response => {
                return JSON.parse(response.text()) as AppsCategory[];
            })
            .catch(this.handleError);
    }

    updateAppsCategories(categoriesList: AppsCategory[]) {
        return this.http.post(this.configService.getUrl(APPS_CATEGORIES_SET_URL), JSON.stringify(categoriesList), {headers: this.header})
                .toPromise()
                .catch(this.handleError);
    }
}
