import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Utils} from '../support/utils';
import {isNull, isUndefined} from 'util';
import {BASE_URL_SETTING} from '../support/constants';
import {Route, Router} from '@angular/router';

@Injectable()
export class ConfigService {

    private config: Object = null;

    constructor(protected http: Http, private utils: Utils) {
    }

    loadConfig() {
        this.http.get('assets/config/env.json')
            .toPromise()
            .then(res => {
                return res.json();
            }).then(envData => {

            let request: any = null;

            switch (envData.env) {
                case 'prod': {
                    request = this.http.get('assets/config/config-' + envData.env + '.json');
                }
                    break;
                case 'dev': {
                    request = this.http.get('assets/config/config-' + envData.env + '.json');
                }
                    break;

                case 'default': {
                    console.error('Environment file is not set or invalid');
                }
                    break;
            }

            if (request) {
                request
                    .toPromise()
                    .then(configRes => {
                        this.config = configRes.json();
                        localStorage.setItem(BASE_URL_SETTING, this.config['baseUrl']);

                    })
                    .catch((error: any) => {
                        console.error('Error reading ' + envData.env + ' configuration file');
                        return Observable.throw(error.json().error || 'Server error');
                    });
            } else {
                console.error('Env config file "env.json" is not valid');
            }

        });
    }

    getUrl(serviceUrl: string): string {
        const settingsBaseUrl = localStorage.getItem(BASE_URL_SETTING);
        return this.utils.strFormat(['{0}{1}', settingsBaseUrl, serviceUrl]);
    }



}
