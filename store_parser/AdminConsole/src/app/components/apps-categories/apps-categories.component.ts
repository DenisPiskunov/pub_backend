import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {AppsCategoriesService} from '../../services/apps-categories-service';
import {AppsCategory} from '../../domain/apps-category';
import {Utils} from '../../support/utils';
import {isNull, isUndefined} from 'util';
import {SessionData} from '../../services/session-data';
import {USER_PREFS} from '../../support/constants';

@Component({
    selector: 'app-apps-categories',
    templateUrl: './apps-categories.component.html',
    styleUrls: ['./apps-categories.component.css'],
    providers: [AppsCategoriesService],
    encapsulation: ViewEncapsulation.None
})
export class AppsCategoriesComponent implements OnInit {
    appsCategories: AppsCategory[];
    selectedCategory: AppsCategory;
    hideProgress = true;
    isAppsCategoriesChanged = false;
    displayDetailsDialog = false;
    categoryKey: string;
    categoryName: string;
    useSearch: boolean;
    categoriesPerPage: number;

    constructor(private appsCategoriesService: AppsCategoriesService, private sessionData: SessionData, private utils: Utils) {
    }

    ngOnInit() {
        if (isUndefined(this.sessionData.currentUserPrefs)
            || this.sessionData.currentUserPrefs === null
            || isUndefined(this.sessionData.currentUserPrefs.categoriesPerPage)
            || this.sessionData.currentUserPrefs.categoriesPerPage === null ) {
            this.categoriesPerPage = 20;
        } else {
            this.categoriesPerPage = this.sessionData.currentUserPrefs.categoriesPerPage;
        }
        this.loadAppsCategories();
    }

    loadAppsCategories() {
        this.appsCategoriesService.getAppsCategories()
            .then(data => {
                this.appsCategories = data;
            });
    }

    onUpdateAppsCategories() {
        this.appsCategoriesService.updateAppsCategories(this.appsCategories);
        this.onDetailsDialogHide();
        this.isAppsCategoriesChanged = false;
    }

    onRowSelect(event) {
    //     this.selectedCategory = event.data;
    //     this.displayDetailsDialog = true;
    //     this.categoryKey = this.selectedCategory.key;
    //     this.categoryName = this.selectedCategory.name;
    //     this.useSearch = this.selectedCategory.search;
    }

    onCheckBoxClick(event) {
        this.isAppsCategoriesChanged = true;
    }

    onDetailsDialogHide() {
        // this.displayDetailsDialog = false;
        // this.selectedCategory.search = this.useSearch;
    }

    onChangeSelectedCategory() {
        this.isAppsCategoriesChanged = true;
    }

    onPage(event) {
        this.categoriesPerPage = event.rows;
        this.sessionData.currentUserPrefs.categoriesPerPage = this.categoriesPerPage;
        localStorage.setItem(this.utils.strFormat([USER_PREFS, this.sessionData.currentUser]),
            JSON.stringify(this.sessionData.currentUserPrefs));
        console.log(JSON.stringify(this.sessionData.currentUserPrefs));
    }

}
