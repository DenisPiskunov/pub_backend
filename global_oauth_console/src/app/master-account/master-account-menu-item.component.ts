import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {ConfirmationService, LazyLoadEvent} from 'primeng/api';
import {MasterAccountService} from '../service/master-account.service';
import {Observable} from 'rxjs';
import {MasterAccountPage} from './master-account-page';
import {MasterAccountPageItem} from './master-account-page-item';
import {mergeMap} from 'rxjs/operators';
import {MasterAccountDialogComponent} from './dialog/master-account-dialog.component';
import {BlockUIService} from '../service/blockUI.service';
import {MasterAccountUiElementAccessDecisionManager} from './master-account-ui-element-access-decision-manager';
import {SecurityContextHolder} from '../mt-security/security-context-holder';

@Component({
    templateUrl: './master-account-menu-item.component.html',
    providers: [ConfirmationService]
})

export class MasterAccountMenuItemComponent implements AfterViewInit {
    accounts: MasterAccountPageItem[];

    totalCount: number;

    label: string;

    firstRow = 0;

    currentRows = 10;

    deleteConfirmDialogKey = 'deleteConfirmDialogKey';

    blockConfirmDialogKey = 'blockConfirmDialogKey';

    uiElementAccessDecisionManager: MasterAccountUiElementAccessDecisionManager;

    @ViewChild(MasterAccountDialogComponent)
    private accountPopupComponent: MasterAccountDialogComponent;

    constructor(private confirmationService: ConfirmationService, private accountsService: MasterAccountService,
                private blockUIService: BlockUIService, securityContextHolder: SecurityContextHolder) {
        this.uiElementAccessDecisionManager = new MasterAccountUiElementAccessDecisionManager(securityContextHolder);
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

    private onPaginateComplete(data: MasterAccountPage) {
        this.accounts = data.items;
        this.totalCount = data.totalCount;
    }

    private paginationRequest(): Observable<MasterAccountPage> {
        return this.accountsService.getAccounts(this.firstRow, this.currentRows);
    }

    confirmDeleting(account: MasterAccountPageItem) {

        this.confirmationService.confirm({
            key: this.deleteConfirmDialogKey,
            accept: () => {
                this.deleteAccount(account);
            }
        });
    }

    deleteAccount(account: MasterAccountPageItem) {
        this.blockUIService.block();
        this.accountsService.deleteAccount(account.uuid).pipe(
            mergeMap(() => this.paginationRequest())
        )
            .subscribe((data) => {
                    this.onPaginateComplete(data);
                    this.blockUIService.unblock();
                }
            );
    }

    confirmBlocking(account: MasterAccountPageItem) {
        this.confirmationService.confirm({
            key: this.blockConfirmDialogKey,
            accept: () => {
                this.blockAccount(account);
            }
        });
    }

    blockAccount(account: MasterAccountPageItem) {
        this.blockUIService.block();
        this.accountsService.blockAccount(account.uuid).pipe(
            mergeMap(() => this.paginationRequest())
        )
            .subscribe((data) => {
                    this.onPaginateComplete(data);
                    this.blockUIService.unblock();
                }
            );
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
