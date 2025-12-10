import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {Dhm} from '../../domain/dhm';
import {Utils} from '../../support/utils';
import {TaskManagerService} from '../../services/task-manager-service';

@Component({
  selector: 'app-tasks-manager',
  templateUrl: './tasks-manager.component.html',
  styleUrls: ['./tasks-manager.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class TasksManagerComponent implements OnInit {

    blockedDocument = false;
    hideProgress = true;

    updaterDataDaysInterval: number;
    updaterDataHoursInteval: number;
    updaterDataMinutesInterval: number;

    topParserDaysInterval: number;
    topParserHoursInteval: number;
    topParserMinutesInterval: number;

    pingerDaysInterval: number;
    pingerHoursInteval: number;
    pingerMinutesInterval: number;


    pingerIntervalChanged = false;
    topParserIntervalChanged = false;
    updaterIntervalChanged = false;

    constructor(private taskManagerService: TaskManagerService, private utils: Utils) { }

  ngOnInit() {
      this.onGetUpdateInterval();
      this.onGetTopParserInterval();
      this.onGetPingerInterval();
  }

    onGetUpdateInterval() {
        this.blockedDocument = true;
        this.hideProgress = false;
        this.taskManagerService.getUpdaterInterval()
            .then(data => {
                this.blockedDocument = false;
                this.hideProgress = true;
                const dhm = this.utils.millisecondsToDHM(data);
                this.updaterDataDaysInterval = dhm.days;
                this.updaterDataHoursInteval = dhm.hours;
                this.updaterDataMinutesInterval = dhm.minutes;
            });
    }

    onSaveUpdaterInterval() {
        this.blockedDocument = true;
        this.hideProgress = false;
        const dhm: Dhm = {
            days: this.updaterDataDaysInterval,
            hours: this.updaterDataHoursInteval,
            minutes: this.updaterDataMinutesInterval
        };
        const milliseconds = this.utils.dhmToMilliseconds(dhm);
        this.taskManagerService.setUpdaterInterval(milliseconds)
            .then(response => {
                this.blockedDocument = false;
                this.hideProgress = true;
                this.updaterIntervalChanged = false;
            });

    }


    onGetTopParserInterval() {
        this.blockedDocument = true;
        this.hideProgress = false;
        this.taskManagerService.getTopParserInterval()
            .then(data => {
                this.blockedDocument = false;
                this.hideProgress = true;
                const dhm = this.utils.millisecondsToDHM(data);
                this.topParserDaysInterval = dhm.days;
                this.topParserHoursInteval = dhm.hours;
                this.topParserMinutesInterval = dhm.minutes;
            });
    }


    onSaveTopParserInterval() {
        this.blockedDocument = true;
        this.hideProgress = false;
        const dhm: Dhm = {
            days: this.topParserDaysInterval,
            hours: this.topParserHoursInteval,
            minutes: this.topParserMinutesInterval
        };
        const milliseconds = this.utils.dhmToMilliseconds(dhm);
        this.taskManagerService.setTopParserInterval(milliseconds)
            .then(response => {
                this.blockedDocument = false;
                this.hideProgress = true;
                this.topParserIntervalChanged = false;
            });
    }

    onGetPingerInterval() {
        this.blockedDocument = true;
        this.hideProgress = false;
        this.taskManagerService.getPingerInterval()
            .then(data => {
                this.blockedDocument = false;
                this.hideProgress = true;
                const dhm = this.utils.millisecondsToDHM(data);
                this.pingerDaysInterval = dhm.days;
                this.pingerHoursInteval = dhm.hours;
                this.pingerMinutesInterval = dhm.minutes;
            });
    }


    onSavePingerInterval() {
        this.blockedDocument = true;
        this.hideProgress = false;
        const dhm: Dhm = {
            days: this.pingerDaysInterval,
            hours: this.pingerHoursInteval,
            minutes: this.pingerMinutesInterval
        };
        const milliseconds = this.utils.dhmToMilliseconds(dhm);
        this.taskManagerService.setPingerInterval(milliseconds)
            .then(response => {
                this.blockedDocument = false;
                this.hideProgress = true;
                this.pingerIntervalChanged = false;
            });
    }

    onPingerIntervalChanged() {
        this.pingerIntervalChanged = true;
    }

    onTopParserIntervalChanged() {
        this.topParserIntervalChanged = true;
    }

    onUpdaterIntervalChanged() {
        this.updaterIntervalChanged = true;
    }

    onPingerRestartSoft() {
        this.onPingerRestart(false);
    }

    onPingerRestartHard() {
        this.onPingerRestart(true);
    }

    onTopParserRestartSoft() {
        this.onTopParserRestart(false);
    }

    onTopParserRestartHard() {
        this.onTopParserRestart(true);
    }

    onUpdaterRestartSoft() {
        this.onUpdaterRestart(false);
    }

    onUpdaterRestartHard() {
        this.onUpdaterRestart(true);
    }

    private onPingerRestart(hard: Boolean) {
        this.blockedDocument = true;
        this.hideProgress = false;
        this.taskManagerService.restartPinger(hard)
            .then(response => {
                this.blockedDocument = false;
                this.hideProgress = true;
            });
    }

    private onTopParserRestart(hard: Boolean) {
        this.blockedDocument = true;
        this.hideProgress = false;
        this.taskManagerService.restartTopParser(hard)
            .then(response => {
                this.blockedDocument = false;
                this.hideProgress = true;
            });
    }

    private onUpdaterRestart(hard: Boolean) {
        this.blockedDocument = true;
        this.hideProgress = false;
        this.taskManagerService.restartUpdater(hard)
            .then(response => {
                this.blockedDocument = false;
                this.hideProgress = true;
            });
    }

}
