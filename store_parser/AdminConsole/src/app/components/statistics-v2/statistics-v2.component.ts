import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {AppStatisticsV2Service} from '../../services/app-statistics-v2-service';
import {AppStatisticsV2} from '../../domain/app-statistics-v2';

@Component({
  selector: 'app-statistics-v2',
  templateUrl: './statistics-v2.component.html',
  styleUrls: ['./statistics-v2.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class StatisticsV2Component implements OnInit {

    fromDate: Date;
    toDate: Date;
    blockedDocument = false;
    hideProgress = true;
    hideChart = true;

    chartData: any;
    chartLabels: string[] = [];
    statTableData: AppStatisticsV2[] = [];
    iosAddedData: number[] = [];
    iosDeletedData: number[] = [];
    iosActiveData: number[] = [];
    androidAddedData: number[] = [];
    androidDeletedData: number[] = [];
    androidActiveData: number[] = [];

    showAndroidAdded = true;
    showAndroidDeleted = true;
    showIOSAdded = true;
    showIOSDeleted = true;
    showAndroidActive = true;
    showIOSActive = true;

    constructor(private appStatisticsServiceV2: AppStatisticsV2Service) {

    }

    ngOnInit() {
        this.toDate = new Date();
        this.fromDate = new Date(this.toDate.getFullYear(), this.toDate.getMonth(), 1);
    }

    onGetStatisticsBundle() {
        const  _fromDate: Date = new Date();
        const _toDate: Date = new Date();
        this.blockedDocument = true;
        this.hideProgress = false;

        _fromDate.setDate(this.fromDate.getDate());
        _fromDate.setMonth(this.fromDate.getMonth());
        _fromDate.setFullYear(this.fromDate.getFullYear());
        _toDate.setDate(this.toDate.getDate());
        _toDate.setMonth(this.toDate.getMonth());
        _toDate.setFullYear(this.toDate.getFullYear());

        this.appStatisticsServiceV2.getAppStatistics(
            _fromDate.toISOString().split('T')[0],
            _toDate.toISOString().split('T')[0])
            .then(data => {
                this.blockedDocument = false;
                this.hideProgress = true;
                this.hideChart = false;
                this.prepareData(data);
            });

    }

    private prepareData(statDataList: AppStatisticsV2[]) {
        this.chartLabels = [];
        this.iosAddedData = [];
        this.iosDeletedData = [];
        this.iosActiveData = [];
        this.androidAddedData = [];
        this.androidDeletedData = [];
        this.androidActiveData = [];
        this.statTableData = [];

        statDataList.forEach(
            statItem => {
                if (statItem.platform === 'IOS') {
                    this.chartLabels.push(statItem.date);

                    this.iosActiveData.push(statItem.activeCount);
                    this.iosAddedData.push(statItem.addedCount);
                    this.iosDeletedData.push(statItem.deletedCount);
                } else {
                    this.androidActiveData.push(statItem.activeCount);
                    this.androidAddedData.push(statItem.addedCount);
                    this.androidDeletedData.push(statItem.deletedCount);
                }
                this.statTableData.push(statItem);
            });
        this.drawDigramm();
    }

    private drawDigramm() {
        const androidActive = {
            label: 'Android (активные)',
            backgroundColor: '#cae2ca',
            borderColor: '#ffffff',
            data: this.androidActiveData
        };

        const androidAdded: any = {
            label: 'Android (добавленные)',
            backgroundColor: '#e3ffe3',
            borderColor: '#4d8e2b',
            data: this.androidAddedData
        };

        const androidDeleted: any =   {
            label: 'Android (удаленные)',
            backgroundColor: '#e3ffe3',
            borderColor: '#AA0000',
            data: this.androidDeletedData
        };

        const iosActive = {
            label: 'iOS (активные)',
            backgroundColor: '#aedbe2',
            borderColor: '#ffffff',
            data: this.iosActiveData
        };

        const iosAdded = {
            label: 'iOS (добавленные)',
            backgroundColor: '#dff3ff',
            borderColor: '#106a87',
            data: this.iosAddedData
        };

        const iosDeleted = {
            label: 'iOS (удаленные)',
            backgroundColor: '#dff3ff',
            borderColor: '#aa0000',
            data: this.iosDeletedData
        };

        const dataset: any[] = [];

        if (this.showIOSDeleted) {
            dataset.push(iosDeleted);
        }

        if (this.showIOSAdded) {
            dataset.push(iosAdded);
        }

        if (this.showAndroidDeleted) {
            dataset.push(androidDeleted);
        }

        if (this.showAndroidAdded) {
            dataset.push(androidAdded);
        }

        if (this.showIOSActive) {
            dataset.push(iosActive);
        }

        if (this.showAndroidActive) {
            dataset.push(androidActive);
        }

        this.chartData = {
            labels: this.chartLabels,
            datasets: dataset
        };
    }

}
