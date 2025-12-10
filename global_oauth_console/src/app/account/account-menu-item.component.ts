import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {ConfirmationService, LazyLoadEvent} from 'primeng/api';
import {Observable} from 'rxjs';
import {BlockUIService} from '../service/blockUI.service';
import {AccountPageItem} from './account-page-item';
import {AccountPage} from './account-page';
import {AccountDialogComponent} from './dialog/account-dialog.component';
import {AccountService} from '../service/account.service';
import {AccountUiElementAccessDecisionManager} from './account-ui-element-access-decision-manager';
import {SecurityContextHolder} from '../mt-security/security-context-holder';

@Component({
    templateUrl: './account-menu-item.component.html',
    providers: [ConfirmationService]
})

export class AccountMenuItemComponent implements AfterViewInit{
    accounts: AccountPageItem[];

    totalCount: number;

    label: string;

    firstRow = 0;

    currentRows = 10;

    uiElementAccessDecisionManager: AccountUiElementAccessDecisionManager;

    @ViewChild(AccountDialogComponent)
    private accountPopupComponent: AccountDialogComponent;

    constructor(private confirmationService: ConfirmationService, private accountsService: AccountService,
                private blockUIService: BlockUIService, securityContextHolder: SecurityContextHolder) {
        this.uiElementAccessDecisionManager = new AccountUiElementAccessDecisionManager(securityContextHolder);
    }

    ngAfterViewInit() {
        this.paginate();
    }

    loadAccounts(event: LazyLoadEvent) {
        this.firstRow = event.first;
        this.currentRows = event.rows;
        this.paginate();
    }

    paginate() {
        this.blockUIService.block();
        this.paginationRequest().subscribe(
            (data) => {
                this.onPaginateComplete(data);
                this.blockUIService.unblock();
            }
        );
    }

    private onPaginateComplete(data: AccountPage) {
        this.accounts = data.items;
        this.totalCount = data.totalCount;
    }

    private paginationRequest(): Observable<AccountPage> {
        return this.accountsService.getAccounts(this.firstRow, this.currentRows);
    }

    createAccount() {
        this.accountPopupComponent.onAdd();
    }

    afterSave() {
        this.paginate();
    }

    editAccount(uuid: string) {
        this.accountPopupComponent.onEdit(uuid);
    }
}
