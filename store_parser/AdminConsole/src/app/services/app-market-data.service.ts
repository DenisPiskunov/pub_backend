import { Injectable } from '@angular/core';
import {BaseHttpService} from './base-http-service';
import {Http} from '@angular/http';
import {Utils} from '../support/utils';
import {ConfigService} from './config-service';
import {
    APP_ANDROID_FULL_MARKET_DATA_GET_URL,
    APP_ANDROID_MARKET_DATA_GET_URL, APP_ANDROID_MARKET_DATA_SET_URL
} from '../support/constants';
import {ShortAndroidAppMarketData} from '../domain/short-android-app-market-data';
import {FullAndroidAppMarketData} from '../domain/full-android-app-market-data';

@Injectable()
export class AppMarketDataService extends BaseHttpService {

  constructor(protected http: Http, private utils: Utils, private configService: ConfigService) {
    super();
  }

  getAndroidAppMarketData() {
    return this.http.get( this.utils.strFormat([this.configService.getUrl(APP_ANDROID_MARKET_DATA_GET_URL)]), {headers: this.header})
        .toPromise()
        .then(response => {
          return response.json() as ShortAndroidAppMarketData[];
        })
        .catch(this.handleError);
  }

  getAndroidFullAppMarketData(appPackage: string) {
      return this.http.get(this.utils.strFormat([this.configService.getUrl(APP_ANDROID_FULL_MARKET_DATA_GET_URL), appPackage]),
          {headers: this.header})
          .toPromise()
          .then( response => {
              console.log(response.json());
              return response.json() as FullAndroidAppMarketData;
          })
          .catch ( this.handleError);
  }

    markAppDeleted(appPackage: string) {
        return this.http.post(this.configService.getUrl(APP_ANDROID_MARKET_DATA_SET_URL), JSON.stringify(appPackage),
            {headers: this.header})
            .toPromise()
            .catch(this.handleError);
    }

}
