import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {SessionData} from '../../services/session-data';
import {Router} from '@angular/router';
import {AppsGridComponent} from '../apps-grid/apps-grid.component';
import {UserService} from '../../services/user-service';

@Component({
    selector: 'app-left-menu',
    templateUrl: './left-menu.component.html',
    styleUrls: ['./left-menu.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class LeftMenuComponent implements OnInit {

    constructor(private sessionData: SessionData,
                private router: Router,
                private appsGrid: AppsGridComponent,
                private userService: UserService) {
    }

    ngOnInit() {
    }

    setPlatformParams(platform: string, status: boolean) {
        this.sessionData.currentPlatform = platform;
        this.sessionData.appStatus = status;
        if (this.router.url === '/dashboard') {
            this.appsGrid.setAppListParam(platform, status);
        }
    }

    hasAdminRole(): boolean {
        return this.userService.isAdmin;
    }

    hasRole(role: string): boolean {
        return this.userService.hasRole(role);
    }

}
