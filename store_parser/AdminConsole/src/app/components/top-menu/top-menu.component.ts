import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {UserService} from '../../services/user-service';

@Component({
    selector: 'app-top-menu',
    templateUrl: './top-menu.component.html',
    styleUrls: ['./top-menu.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class TopMenuComponent implements OnInit {
    currentUsername: string;

    constructor(private userService: UserService) {
        this.currentUsername = this.userService.getCurrentUser();
    }

    ngOnInit() {
    }

    onLogout() {
        this.userService.logoutUser(this.currentUsername);
    }

}
