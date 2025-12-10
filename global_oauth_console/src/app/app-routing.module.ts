import {RouterModule} from '@angular/router';
import {NgModule} from '@angular/core';
import {MainComponent} from './main/main.component';
import {LoginComponent} from './login/login.component';
import {RoleMenuItemComponent} from './role/role-menu-item.component';
import {MasterAccountMenuItemComponent} from './master-account/master-account-menu-item.component';
import {Error500Component} from './error/500/error500.component';
import {Error403Component} from './error/403/error403.component';
import {AccountMenuItemComponent} from './account/account-menu-item.component';
import {AnonymousGuard} from './mt-security/anonymous.guard';
import {AuthenticationGuard} from './mt-security/authentication.guard';
import {ACCOUNTS, ERROR_403, ERROR_500, LOGIN, MASTER_ACCOUNTS, ROLES} from './mt-support/router-support';

@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                {
                    path: '',
                    redirectTo: LOGIN,
                    pathMatch: 'full'
                },
                {
                    path: LOGIN,
                    component: LoginComponent,
                    canActivate: [AnonymousGuard]
                },
                {path: ERROR_500, component: Error500Component},
                {path: ERROR_403, component: Error403Component},
                {
                    path: '', component: MainComponent,
                    children: [
                        {path: ROLES, component: RoleMenuItemComponent},
                        {path: MASTER_ACCOUNTS, component: MasterAccountMenuItemComponent},
                        {path: ACCOUNTS, component: AccountMenuItemComponent}
                    ],
                    canActivate: [AuthenticationGuard]
                },
                {path: '**', redirectTo: '/'},
            ],
            {scrollPositionRestoration: 'enabled'}
        )
    ],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
