import {Component, OnDestroy} from '@angular/core';
import {AppComponent} from '../../app.component';
import {MainComponent} from '../main.component';
import {BreadcrumbService} from '../../service/breadcrumb.service';
import {Subscription} from 'rxjs';
import {MenuItem} from 'primeng/api';
import {SecurityContextHolder} from '../../mt-security/security-context-holder';
import {AuthenticationService, Session} from '../../mt-security/authentication.service';
import {RouterSupport} from '../../mt-support/router-support';

@Component({
    selector: 'app-topbar',
    templateUrl: './topbar.component.html'
})
export class TopbarComponent implements OnDestroy {

    subscription: Subscription;

    items: MenuItem[];

    login: string;

    constructor(public breadcrumbService: BreadcrumbService, public app: AppComponent, public appMain: MainComponent,
                private authenticationService: AuthenticationService, private securityContextHolder: SecurityContextHolder,
                private routerSupport: RouterSupport) {
        this.subscription = breadcrumbService.itemsHandler.subscribe(response => {
            this.items = response;
        });
        this.login = this.securityContextHolder.securityContext.getLogin();
    }

    logout() {
        this.authenticationService.signOut(Session.CURRENT)
            .subscribe(() => {
                this.routerSupport.navigateToLogin();
            });
    }

    logoutAllDevices() {
        this.authenticationService.signOut(Session.ALL)
            .subscribe(() => {
                this.routerSupport.navigateToLogin();
            });
    }

    ngOnDestroy() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }
}
