import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {ShortAndroidAppMarketData} from '../../domain/short-android-app-market-data';
import {SessionData} from '../../services/session-data';
import {Utils} from '../../support/utils';
import {UserService} from '../../services/user-service';
import {AppMarketDataService} from '../../services/app-market-data.service';
import {isNull, isUndefined} from 'util';
import {ANDROID, USER_PREFS} from '../../support/constants';
import {FullAndroidAppMarketData} from '../../domain/full-android-app-market-data';

@Component({
  selector: 'app-android-market-data',
  templateUrl: './android-market-data.component.html',
  styleUrls: ['./android-market-data.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AndroidMarketDataComponent implements OnInit {
  displayDialog: boolean;
  displayDeleteConfirmDialog: boolean;
  appMarketDataList: ShortAndroidAppMarketData[];
  selectedAppMarketData: ShortAndroidAppMarketData;
  fullAppMarketData: FullAndroidAppMarketData;
  selectedPlatform: string;
  appsMarketDataTotalCount: number;
  appMarketDataPerPage: number;
  hideProgress: boolean;

  constructor(private appMarketDataService: AppMarketDataService,
              private sessionData: SessionData,
              private utils: Utils,
              private userService: UserService) { }

  ngOnInit() {
    if (!isUndefined(this.sessionData.currentPlatform)) {
      this.setParams(this.sessionData.currentPlatform);
    } else {
      this.setParams(ANDROID);
    }
  }

  setParams(platform: string) {
    this.sessionData.currentPlatform = platform;
    this.appMarketDataPerPage = 20;
    if (isUndefined(this.sessionData.currentUserPrefs)
        || this.sessionData.currentUserPrefs === null
        || isUndefined(this.sessionData.currentUserPrefs.appMarketDataPerPage)
        || this.sessionData.currentUserPrefs.appMarketDataPerPage === null) {
      this.appMarketDataPerPage = 20;
    } else {
      this.appMarketDataPerPage = this.sessionData.currentUserPrefs.appMarketDataPerPage;
    }
    this.loadAndShowAppMarketDataList();
  }

  loadAndShowAppMarketDataList() {
    this.hideProgress = false;
    this.appMarketDataService.getAndroidAppMarketData()
        .then(data => {
          this.appMarketDataList = data;
          this.appsMarketDataTotalCount = this.appMarketDataList.length;
          this.hideProgress = true;
        });
  }

  selectAppMarketData(appMarketData: ShortAndroidAppMarketData) {
    this.selectedAppMarketData = appMarketData;
    this.appMarketDataService.getAndroidFullAppMarketData(appMarketData.package_name)
        .then(data => {
          this.fullAppMarketData = data;
          this.displayDialog = true;
        });
  }

  onPage(event) {
    this.appMarketDataPerPage = event.rows;
    this.sessionData.currentUserPrefs.appMarketDataPerPage = this.appMarketDataPerPage;
    localStorage.setItem(this.utils.strFormat([USER_PREFS, this.sessionData.currentUser]),
        JSON.stringify(this.sessionData.currentUserPrefs));
  }

  onDialogHide() {
    this.selectedAppMarketData = null;
    this.displayDialog = false;
  }

  showConfirmMarkAsInvalidAppDialog() {
    this.displayDeleteConfirmDialog = true;
  }

  hideConfirmMarkAsInvalidAppDialog() {
    this.displayDeleteConfirmDialog = false;
  }

  markAppAsDeleted() {
    this.appMarketDataService.markAppDeleted(this.selectedAppMarketData.package_name)
        .then(data => {
          this.loadAndShowAppMarketDataList();
          this.displayDeleteConfirmDialog = false;
          this.displayDialog = false;
          this.selectedAppMarketData = null;
        });
  }

}
