import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {APP_INITIALIZER, NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {
    BlockUIModule,
    ButtonModule, CalendarModule, CarouselModule, ChartModule, CheckboxModule, ChipsModule,
    DataGridModule, DataListModule,
    DataTableModule,
    DialogModule, DropdownModule, FieldsetModule,
    InputTextModule, MessageModule, MessagesModule,
    PanelModule, PasswordModule, ProgressSpinnerModule, RadioButtonModule, SharedModule, SidebarModule, SpinnerModule,
    TabViewModule
} from 'primeng/primeng';

import {AppComponent} from './components/app-component/app.component';
import {AppsService} from './services/apps-service';
import {AppsGridComponent} from './components/apps-grid/apps-grid.component';
import {LoginComponent} from './components/login/login.component';
import {AuthService} from './services/auth.service';
import {AuthGuard} from './support/auth.guard' ;
import {UserService} from './services/user-service';
import {routing} from './support/routing';
import {NotFoundComponent} from './components/not-found/not-found.component';
import {ManualAddingComponent} from './components/manual-adding/manual-adding.component';
import {Utils} from './support/utils';
import {LeftMenuComponent} from './components/left-menu/left-menu.component';
import {SessionData} from './services/session-data';
import {KeywordsEditorComponent} from './components/keywords-editor/keywords-editor.component';
import {KeywordsService} from './services/keywords-service';
import {TopMenuComponent} from './components/top-menu/top-menu.component';
import {MailingSettingService} from './services/mailing-setting-service';
import { MailingSettingsComponent } from './components/mailing-settings/mailing-settings.component';
import { UserSettingsComponent } from './components/user-settings/user-settings.component';
import {UserSettingsService} from './services/user-settings-service';
import { StatisticsComponent } from './components/statistics/statistics.component';
import {ConfigService} from './services/config-service';
import {AppStatisticsService} from './services/app-statistics-service';
import { StatisticsV2Component } from './components/statistics-v2/statistics-v2.component';
import {AppStatisticsV2Service} from './services/app-statistics-v2-service';
import { TasksManagerComponent } from './components/tasks-manager/tasks-manager.component';
import {TaskManagerService} from './services/task-manager-service';
import { AppsCategoriesComponent } from './components/apps-categories/apps-categories.component';
import { AndroidMarketDataComponent } from './components/android-market-data/android-market-data.component';
import {AppMarketDataService} from "./services/app-market-data.service";

@NgModule({
    declarations: [
        AppComponent,
        AppsGridComponent,
        LoginComponent,
        NotFoundComponent,
        ManualAddingComponent,
        LeftMenuComponent,
        TopMenuComponent,
        KeywordsEditorComponent,
        MailingSettingsComponent,
        UserSettingsComponent,
        StatisticsComponent,
        StatisticsV2Component,
        TasksManagerComponent,
        AppsCategoriesComponent,
        AndroidMarketDataComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        DataTableModule,
        HttpModule,
        InputTextModule,
        DialogModule,
        ButtonModule,
        DataGridModule,
        PanelModule,
        DialogModule,
        TabViewModule,
        routing,
        DropdownModule,
        ChipsModule,
        BlockUIModule,
        MessagesModule,
        MessageModule,
        ProgressSpinnerModule,
        SpinnerModule,
        FieldsetModule,
        CarouselModule,
        SidebarModule,
        SharedModule,
        DataListModule,
        RadioButtonModule,
        CheckboxModule,
        PasswordModule,
        CalendarModule,
        ChartModule],
    providers: [
        AppsService,
        AuthService,
        AuthGuard,
        UserService,
        KeywordsService,
        Utils,
        SessionData,
        AppsGridComponent,
        MailingSettingService,
        UserSettingsService,
        AppStatisticsService,
        AppStatisticsV2Service,
        ConfigService,
        TaskManagerService,
        AppMarketDataService,
        {
            provide: APP_INITIALIZER,
            useFactory: ConfigLoader,
            deps: [ConfigService],
            multi: true }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}

export function ConfigLoader(configService: ConfigService) {
    return () => configService.loadConfig();
}
