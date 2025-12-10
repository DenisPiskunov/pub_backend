import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {App} from '../../domain/app';
import {AppsService} from '../../services/apps-service';
import {ANDROID, USER_PREFS} from '../../support/constants';
import {SessionData} from '../../services/session-data';
import {AppDescription} from '../../domain/app-description';
import {AppReview} from '../../domain/app-review';
import {AppScreenshot} from '../../domain/app-screenshot';
import {Utils} from '../../support/utils';
import {isNull, isUndefined, log} from 'util';
import {UserService} from "../../services/user-service";

@Component({
    selector: 'app-apps-grid',
    templateUrl: './apps-grid.component.html',
    styleUrls: ['./apps-grid.component.css'],
    providers: [AppsService],
    encapsulation: ViewEncapsulation.None
})
export class AppsGridComponent implements OnInit {
    displayDialog: boolean;
    displayRestoreConfirmDialog: boolean;
    displayDeleteConfirmDialog: boolean;
    appList: App[];
    selectedApp: App;
    selectedPlatform: string;
    selectedAppDescriptions: AppDescription[];
    selectedAppReviews: AppReview[];
    selectedAppScreenshots: AppScreenshot[];
    appsTotalCount: number;
    appsCountPerPage: number;

    constructor(private appService: AppsService, private sessionData: SessionData, private utils: Utils, private userService: UserService) {
    }

    ngOnInit() {
        if (!isUndefined(this.sessionData.currentPlatform) && !isUndefined(this.sessionData.appStatus)) {
            this.setAppListParam(this.sessionData.currentPlatform, this.sessionData.appStatus);
        } else {
            this.setAppListParam(ANDROID, true);
        }
    }

    setAppListParam(platform: string, status: boolean) {
        this.selectedPlatform = platform;
        this.sessionData.currentPlatform = platform;
        this.sessionData.appStatus = status;
        if ( isUndefined(this.sessionData.currentUserPrefs)
            || this.sessionData.currentUserPrefs === null
            || isUndefined(this.sessionData.currentUserPrefs.appsCountPerPage)
            || this.sessionData.currentUserPrefs.appsCountPerPage === null ) {
            this.appsCountPerPage = 20;
        } else {
            this.appsCountPerPage = this.sessionData.currentUserPrefs.appsCountPerPage;
        }
        this.loadAndShowAppList();
    }

    loadAndShowAppList() {
        this.appService.getMobileApps(this.sessionData.currentPlatform, this.sessionData.appStatus)
            .then(data => {
                this.appList = data;
                this.appsTotalCount = this.appList.length;
            });
    }

    selectApp(app: App) {
        this.appService.getAppData(app.appId)
            .then( data => {
                this.selectedApp = app;
                this.selectedAppDescriptions = data.descriptions;
                this.selectedAppScreenshots = data.screenshots;
                this.selectedAppReviews = data.reviews;
                this.displayDialog = true;
            });
    }

    showConfirmRestoreAppDialog() {
        this.displayRestoreConfirmDialog = true;
    }

    hideConfirmRestoreAppDialog() {
        this.displayRestoreConfirmDialog = false;
    }

    restoreApp() {
        this.appService.restoreApp(this.selectedApp.appId)
            .then(data => {
                this.loadAndShowAppList();
                this.displayRestoreConfirmDialog = false;
                this.displayDialog = false;
                this.selectedApp = null;
            });
    }

    showConfirmMarkAsInvalidAppDialog() {
        this.displayDeleteConfirmDialog = true;
    }

    hideConfirmMarkAsInvalidAppDialog() {
        this.displayDeleteConfirmDialog = false;
    }

    markAsInvalidApp() {
        this.appService.markAppAsInvalid(this.selectedApp.appId)
            .then(data => {
                this.loadAndShowAppList();
                this.displayDeleteConfirmDialog = false;
                this.displayDialog = false;
                this.selectedApp = null;
            });
    }

    onDialogHide() {
        this.selectedApp = null;
        this.displayDialog = false;
    }

    onPage(event) {
        this.appsCountPerPage = event.rows;
        this.sessionData.currentUserPrefs.appsCountPerPage = this.appsCountPerPage;
        localStorage.setItem(this.utils.strFormat([USER_PREFS, this.sessionData.currentUser]),
            JSON.stringify(this.sessionData.currentUserPrefs));
    }

    hasAdminRole(): boolean {
        return this.userService.isAdmin;
    }

    hasRole(role: string): boolean {
        return this.userService.hasRole(role);
    }
}
