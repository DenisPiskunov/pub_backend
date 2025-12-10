import {Component, OnInit, ViewChild} from '@angular/core';
import {AppComponent} from '../app.component';
import {AuthenticationService} from '../mt-security/authentication.service';
import {NgForm, NgModel} from '@angular/forms';
import {RestorePasswordDialogComponent} from './dialog/restore-password-dialog.component';
import {catchError, finalize, mergeMap} from 'rxjs/operators';
import {AccountService} from '../service/account.service';
import {SecurityContextHolder} from '../mt-security/security-context-holder';
import {EMPTY, Observable} from 'rxjs';
import {Authority} from '../authority/authority';
import {RouterSupport} from '../mt-support/router-support';
import {LocalizationService} from '../mt-support/localization.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.scss']
})
export class LoginComponent implements OnInit{

    login: string = null;
    password: string = null;
    language: string;
    languages = [];
    errorAuth = false;
    blockedDocument = false;
    private errorValidation = false;

    @ViewChild(RestorePasswordDialogComponent)
    private restorePasswordDialog: RestorePasswordDialogComponent;

    constructor(private authenticationService: AuthenticationService, private accountService: AccountService,
                private securityContextHolder: SecurityContextHolder, private localizationService: LocalizationService,
                private routerSupport: RouterSupport) {
    }

    ngOnInit(): void {
        this.languages = this.localizationService.languages;
        this.language = this.localizationService.currentLang;
    }

    onLogin(form: NgForm) {
        this.errorAuth = false;
        this.errorValidation = this.validate(form);
        if (!this.errorValidation) {
            this.blockedDocument = true;
            this.authenticationService.signIn(this.login, this.password)
                .pipe(
                    mergeMap((accessToken) => this.signInSuccessCallBack(accessToken)),
                    catchError((error) => this.signInFailedCallBack(error)),
                    finalize(() => this.blockedDocument = false)
                )
                .subscribe((authorities) => this.authoritiesLoadedCallBack(authorities));
        }
    }

    restorePassword() {
        this.restorePasswordDialog.onRestore();
    }

    isControlInvalid(model: NgModel): boolean {
        return !model.valid && this.errorValidation;
    }

    private validate(f: NgForm): boolean {
        return !!Object.values(f.controls).find(item => item.invalid);
    }

    onChangeLanguage() {
        this.localizationService.currentLang = this.language;
    }

    private signInSuccessCallBack(accessToken: string): Observable<Authority[]> {
        this.errorAuth = false;
        this.securityContextHolder.securityContext.setLogin(this.login);
        this.securityContextHolder.securityContext.setAccessToken(accessToken);
        return this.accountService.getAuthorities();
    }

    private signInFailedCallBack(error: any): Observable<never> {
        if (error.status === 401) {
            this.errorAuth = true;
        }
        return EMPTY;
    }

    private authoritiesLoadedCallBack(authorities: Authority[]) {
        this.securityContextHolder.securityContext.setAuthorities(authorities.map(authority => authority.name));
        this.routerSupport.navigateToConsoleByAuthorities();
    }
}

