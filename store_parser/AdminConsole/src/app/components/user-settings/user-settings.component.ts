import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {UserDetails} from '../../domain/user-details';
import {UserSettingsService} from '../../services/user-settings-service';
import {ROLE_ADMIN, ROLE_USER} from '../../support/constants';
import {SelectItem} from 'primeng/primeng';
import {isNull, isUndefined} from 'util';

@Component({
    selector: 'app-user-settings',
    templateUrl: './user-settings.component.html',
    styleUrls: ['./user-settings.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class UserSettingsComponent implements OnInit {
    displayDetailsDialog: boolean;
    displayPasswordDialog: boolean;
    userDetailsList: UserDetails[];
    selectedUser: UserDetails;
    newUser: UserDetails;
    blockedDocument = false;
    hideProgress = true;
    addingUser = false;
    isUserDetailsChanged = false;
    rolesMap: SelectItem[];
    pass = '';
    username = '';
    hasError = false;
    errorText = '';

    constructor(private userSettingsService: UserSettingsService) {
    }

    ngOnInit() {
        this.rolesMap = [
            {label: '', value: null},
            {label: 'Администратор', value: ROLE_ADMIN},
            {label: 'Пользователь', value: ROLE_USER}
        ];

        this.blockedDocument = true;
        this.hideProgress = false;
        this.userSettingsService.getAllUsers().then(data => {
            this.userDetailsList = data;
            this.blockedDocument = false;
            this.hideProgress = true;
        });
    }

    showAddDialog() {
        this.username = '';
        this.pass = '';
        this.addingUser = true;
        this.newUser = new UserDetails();
        this.displayDetailsDialog = true;
    }

    onRowSelect(event) {
        this.username = '';
        this.selectedUser = event.data;
        this.addingUser = false;
        this.newUser = this.cloneDetais(event.data);
        this.displayDetailsDialog = true;
    }

    cloneDetais(event: UserDetails): UserDetails {
        const details = new UserDetails();
        details.id = event.id;
        details.username = event.username;
        details.password = event.password;
        details.roles = event.roles;
        details.enabled = event.enabled;
        return details;
    }

    onSaveUser() {
        const users = [...this.userDetailsList];

        if (this.newUser.username.trim() === '') {
            this.hasError = true;
            this.errorText = 'Имя пользователя не указано.';
            return;
        }

        if (this.addingUser) {
            users.forEach(user => {
                if (user.username.toUpperCase() === this.newUser.username.toUpperCase()) {
                    this.hasError = true;
                    this.errorText = 'Пользователь с таким именем уже существует.';
                    this.newUser.username = '';
                    this.displayPasswordDialog = false;
                    return;
                }
            });
            if (this.hasError) {
                return;
            } else {
                if (isUndefined(this.newUser.roles)) {
                    this.newUser.roles = ROLE_USER;
                }
                this.newUser.password = 'pwd';
                users.push(this.newUser);
                this.username = this.newUser.username;
                this.userSettingsService.createUser(this.newUser);
                if (this.pass === '') {
                    this.displayPasswordDialog = true;
                    this.userDetailsList = users;
                    return;
                }
            }
        } else {
            if (isNull(this.newUser.roles)) {
                this.hasError = true;
                this.errorText = 'Выберите роль пользователя.';
                return;
            }
            const oldUsername = users[this.findSelectedIndex()].username;
            users[this.findSelectedIndex()] = this.newUser;
            this.userSettingsService.saveUser(this.newUser, oldUsername);
        }
        this.userDetailsList = users;
        this.newUser = null;
        this.displayDetailsDialog = false;
        this.isUserDetailsChanged = false;
    }

    onDeleteUser() {
        const index = this.findSelectedIndex();
        const user = this.userDetailsList[index];
        this.username = user.username;
        this.userSettingsService.deleteUser(this.username);
        this.userDetailsList = this.userDetailsList.filter((val, i) => i !== index);
        this.displayDetailsDialog = false;
        this.username = '';
    }

    findSelectedIndex(): number {
        return this.userDetailsList.indexOf(this.selectedUser);
    }

    onDetailsDialogHide() {
        this.newUser = null;
    }

    onPasswordDialogHide() {
        this.displayPasswordDialog = false;
    }

    onShowChangePasswordDialog() {
        this.pass = '';
        this.displayPasswordDialog = true;
    }

    onCloseErrorDialog() {
        this.hasError = false;
        this.errorText = '';
    }

    onChangePassword() {
        if (this.pass.trim() === '') {
            this.hasError = true;
            this.errorText = 'Пароль пользователя не указан.';
            return;
        }

        if (!this.addingUser) {
            const index = this.findSelectedIndex();
            const user = this.userDetailsList[index];
            this.username = user.username;
        }
        this.userSettingsService.setUserPassword(this.username, this.pass);
        this.displayPasswordDialog = false;
        this.displayDetailsDialog = false;
        this.isUserDetailsChanged = false;
        this.newUser = null;
    }

    onChangeUserDetails() {
        this.isUserDetailsChanged = true;
    }

}
