import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {User} from '../../domain/user';
import {Router} from '@angular/router';
import {UserService} from '../../services/user-service';
import {isNull} from 'util';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
    user: User;
    private loading = false;
    error = '';

    constructor(private authService: AuthService, private router: Router, private userService: UserService) {
        this.user = new User();
    }

    ngOnInit() {
        this.userService.logoutUser(this.user.username);
    }

    onLogin() {
        this.loading = true;
        this.authService.login(this.user)
            .subscribe(result => {
                if (!isNull(result)) {
                    this.userService.setUserLoggedIn(result);
                    this.router.navigate(['dashboard']);
                } else {
                    this.error = 'Имя пользователя или пароль введены неверно.';
                    this.loading = false;
                }
            });
    }
}
