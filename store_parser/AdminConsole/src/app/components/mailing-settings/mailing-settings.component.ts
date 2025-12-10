import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MailingSettingService} from '../../services/mailing-setting-service';
import {EditMode} from '../../domain/edit-mode';
import {MailListBundle} from '../../domain/mail-list-bundle';
import {Dhm} from '../../domain/dhm';
import {Utils} from '../../support/utils';
import {TaskSchedulerParams} from '../../domain/task-scheduler-params';

@Component({
    selector: 'app-mailing-settings',
    templateUrl: './mailing-settings.component.html',
    styleUrls: ['./mailing-settings.component.css'],
    encapsulation: ViewEncapsulation.None
})

export class MailingSettingsComponent implements OnInit {

    mailList: string[] = [];
    removedEmails: string[] = [];
    addedEmails: string[] = [];
    mailListChanged = false;

    blockedDocument = false;
    hideProgress = true;

    // updaterDataDaysInterval: number;
    // updaterDataHoursInteval: number;
    // updaterDataMinutesInterval: number;
    //
    // topParserDaysInterval: number;
    // topParserHoursInteval: number;
    // topParserMinutesInterval: number;
    //
    // pingerDaysInterval: number;
    // pingerHoursInteval: number;
    // pingerMinutesInterval: number;
    //
    //
    // pingerIntervalChanged = false;
    // topParserIntervalChanged = false;
    // updaterIntervalChanged = false;

    constructor(private mailingSettingService: MailingSettingService, private utils: Utils) {
    }

    ngOnInit() {
        this.getMailList();
    }

    getMailList() {
        this.blockedDocument = true;
        this.hideProgress = false;
        this.mailingSettingService.getMailList()
            .then(data => {
                this.blockedDocument = false;
                this.hideProgress = true;
                this.mailList = data;
            });
    }

    onAddEmail(email: string) {
        this.addedEmails.push(email);
        this.mailListChanged = true;
    }

    onRemoveEmail(email: string) {
        this.removedEmails.push(email);
        this.mailListChanged = true;
    }

    onRevertChanges() {
        this.removedEmails.forEach(
            email => {
                this.mailList.push(email);
            }
        );
        this.removedEmails = [];

        this.addedEmails.forEach(
            email => {
                const index = this.mailList.indexOf(email);
                this.mailList.splice(index, 1);
            }
        );
        this.addedEmails = [];
        this.mailListChanged = false;
    }

    onSaveChanges() {
        if ( this.addedEmails.length > 0 ||
            this.removedEmails.length > 0) {
            this.blockedDocument = true;
            this.hideProgress = false;
        }

        if (this.addedEmails.length > 0) {
            const added: MailListBundle = {
                mailList: this.addedEmails,
                editMode: EditMode[EditMode.ADD]
            };

            this.mailingSettingService.editMailList(added)
                .then(response => {
                    if (response) {
                        this.addedEmails = [];
                    }
                    this.blockedDocument = false;
                    this.hideProgress = true;
                    this.mailListChanged = false;
                });
        }

        if (this.removedEmails.length > 0) {
            const removed: MailListBundle = {
                mailList: this.removedEmails,
                editMode: EditMode[EditMode.DELETE]
            };
            this.mailingSettingService.editMailList(removed)
                .then(response => {
                    if (response) {
                        this.removedEmails = [];
                    }
                    this.blockedDocument = false;
                    this.hideProgress = true;
                    this.mailListChanged = false;
                });
        }
    }
}
