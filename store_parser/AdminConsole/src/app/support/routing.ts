import {Routes, RouterModule} from '@angular/router';
import {LoginComponent} from '../components/login/login.component';
import {AuthGuard} from './auth.guard';
import {AppsGridComponent} from '../components/apps-grid/apps-grid.component';
import {NotFoundComponent} from '../components/not-found/not-found.component';
import {ManualAddingComponent} from '../components/manual-adding/manual-adding.component';
import {KeywordsEditorComponent} from '../components/keywords-editor/keywords-editor.component';
import {MailingSettingsComponent} from '../components/mailing-settings/mailing-settings.component';
import {UserSettingsComponent} from '../components/user-settings/user-settings.component';
import {StatisticsComponent} from '../components/statistics/statistics.component';
import {StatisticsV2Component} from '../components/statistics-v2/statistics-v2.component';
import {TasksManagerComponent} from '../components/tasks-manager/tasks-manager.component';
import {AppsCategoriesComponent} from '../components/apps-categories/apps-categories.component';
import {AndroidMarketDataComponent} from "../components/android-market-data/android-market-data.component";

const appRoutes: Routes = [
    {
        path: '',
        component: LoginComponent
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'dashboard',
        canActivate: [AuthGuard],
        component: AppsGridComponent
    },
    {
        path: 'keywords-editor',
        canActivate: [AuthGuard],
        component: KeywordsEditorComponent
    },
    {
        path: 'manual-adding',
        canActivate: [AuthGuard],
        component: ManualAddingComponent
    },
    {
        path: 'dashboard/apps',
        redirectTo: 'dashboard'
    },
    {
        path: 'mailingsetting',
        canActivate: [AuthGuard],
        component: MailingSettingsComponent
    },
    {
        path: 'users',
        canActivate: [AuthGuard],
        component: UserSettingsComponent
    },
    {
        path: 'apps-statistic',
        canActivate: [AuthGuard],
        component: StatisticsComponent
    },
    {
        path: 'apps-statistic-v2',
        canActivate: [AuthGuard],
        component: StatisticsV2Component
    },
    {
        path: 'task-manager',
        canActivate: [AuthGuard],
        component: TasksManagerComponent
    },
    {
        path: 'categories-editor',
        canActivate: [AuthGuard],
        component: AppsCategoriesComponent
    },
    {
        path: 'android-market-data',
        canActivate: [AuthGuard],
        component: AndroidMarketDataComponent
    },
    {
        path: '**',
        component: NotFoundComponent
    }

];

export const routing = RouterModule.forRoot(appRoutes);
