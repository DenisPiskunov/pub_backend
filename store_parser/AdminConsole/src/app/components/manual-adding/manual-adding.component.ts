import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {
    IOS, ANDROID, ANDROID_APP_PATTERN, IOS_APP_PATTERN,
    ANDROID_PRE_PATTERN
} from '../../support/constants';
import {NewApp} from '../../domain/new-app';
import {AppsService} from '../../services/apps-service';
import {Message} from 'primeng/primeng';

@Component({
    selector: 'app-manual-adding',
    templateUrl: './manual-adding.component.html',
    styleUrls: ['./manual-adding.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class ManualAddingComponent implements OnInit {
    platform: string;
    appIDList: string[] = [];
    newAppList: NewApp[] = [];
    msgs: Message[] = [];
    blockedDocument = false;
    hideProgress = true;
    idListChanged = false;

    constructor(private appService: AppsService) {
    }

    ngOnInit() {
    }

    onAddApplications() {
        let appId: string;
        let path: string;
        const iosAppRegexp = new RegExp(IOS_APP_PATTERN);
        const androidAppRegexp = new RegExp(ANDROID_APP_PATTERN);
        const androidPreRegexp = new RegExp(ANDROID_PRE_PATTERN);

        this.blockedDocument = true;
        this.hideProgress = false;
        this.msgs = [];

        for (let i = 0; i < this.appIDList.length; i++) {
            const currentItem = this.appIDList[i];
            if (currentItem.search(iosAppRegexp) > -1) {
                this.platform = IOS;
                appId = currentItem.match(iosAppRegexp)[0];
            } else if (currentItem.search(androidPreRegexp) > -1 || currentItem.search(androidAppRegexp) > -1) {
                this.platform = ANDROID;
                appId = currentItem;
                if (appId.search(androidPreRegexp) > -1) {
                    path = appId.match(androidPreRegexp)[0];
                    appId = appId.replace(path, '');
                }
                appId = appId.match(androidAppRegexp)[0];
            } else {
                continue;
            }
            this.newAppList.push({appId: appId, platform: this.platform.toUpperCase()});
        }

        this.appService.createNewApps(this.newAppList)
            .then(data => {
                this.resetControls();
                this.blockedDocument = false;
                this.resetControls();
                this.msgs.push({
                    severity: 'success',
                    summary: 'Приложения были успешно добавлены в обработку.',
                    detail: ''
                });
                this.idListChanged = false;
            });
    }

    resetControls() {
        this.appIDList = [];
        this.platform = '';
        this.hideProgress = true;
        this.idListChanged = false;
    }

    onIdListChanged() {
        this.idListChanged = true;
    }

}
