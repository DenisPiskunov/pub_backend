import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {AppStatisticsService} from '../../services/app-statistics-service';
import {AppStatistics} from '../../domain/app-statistics';

@Component({
    selector: 'app-statistics',
    templateUrl: './statistics.component.html',
    styleUrls: ['./statistics.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class StatisticsComponent implements OnInit {

    fromDate: Date;
    toDate: Date;
    blockedDocument = false;
    hideProgress = true;
    hideChart = true;

    chartData: any;
    chartLabels: string[] = [];
    iosData: number[] = [];
    androidData: number[] = [];
    statListLegend: AppStatistics[] = [];


    constructor(private appStatisticsService: AppStatisticsService) {

    }

    ngOnInit() {
        this.toDate = new Date();
        this.fromDate = new Date(this.toDate.getFullYear(), this.toDate.getMonth(), 1);
    }

    onGetStatistics() {
        this.blockedDocument = true;
        this.hideProgress = false;

        this.appStatisticsService.getDeletedStatistic(
            this.fromDate.toISOString().split('T')[0],
            this.toDate.toISOString().split('T')[0])
            .then(data => {
                this.blockedDocument = false;
                this.hideProgress = true;
                this.hideChart = false;
                this.prepareData(data);
            });
    }

    private prepareData(statList: AppStatistics[]) {
        this.chartLabels = [];
        this.iosData = [];
        this.androidData = [];
        this.statListLegend = [];
        statList.forEach(
            statItem => {
                if (statItem.platform === 'IOS') {
                    this.chartLabels.push(statItem.date);
                    this.iosData.push(statItem.count);
                } else {
                    this.androidData.push(statItem.count);
                }
                if (statItem.count > 0) {
                    this.statListLegend.push(statItem);
                }
            });

        this.chartData = {
            labels: this.chartLabels,
            datasets: [
                {
                    label: 'iOS',
                    backgroundColor: '#dff3ff',
                    borderColor: '#106a87',
                    data: this.iosData
                },
                {
                    label: 'Android',
                    backgroundColor: '#e3ffe3',
                    borderColor: '#4d8e2b',
                    data: this.androidData
                }
            ]
        };
    }
}
