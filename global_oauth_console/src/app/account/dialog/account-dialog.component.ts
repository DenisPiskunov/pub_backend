import {Component, EventEmitter, Output} from '@angular/core';
import {forkJoin} from 'rxjs';
import {NgForm, NgModel} from '@angular/forms';
import {BlockUIService} from '../../service/blockUI.service';
import {AccountService} from '../../service/account.service';
import {EnabledRole} from '../enabled-role';
import {AccountPageItem} from '../account-page-item';
import {Account} from '../account';
import {LocalizationService} from '../../mt-support/localization.service';

@Component({
    selector: 'app-popup-account',
    templateUrl: './account-dialog.component.html'
})

export class AccountDialogComponent{
    isVisible = false;
    headerTitle = '';
    roles: EnabledRole[];
    masterAccounts: AccountPageItem[];
    account: Account;
    uuid: string;
    selectedItemsLabel: string;
    private errorValidation: boolean;

    @Output() afterSave = new EventEmitter();
    constructor(private accountService: AccountService,
                private blockUIService: BlockUIService,
                private localizationService: LocalizationService) {
    }

    save(f: NgForm) {
        this.errorValidation = this.validate(f);
        if (!this.errorValidation) {
            this.blockUIService.block();
            if (this.uuid) {
                this.accountService.updateAccount(this.account).subscribe(
                    () => {
                        this.onClose();
                        this.afterSave.emit();
                        this.blockUIService.unblock();
                    }
                );
            } else {
                this.accountService.createAccount(this.account).subscribe(
                    () => {
                        this.onClose();
                        this.afterSave.emit();
                        this.blockUIService.unblock();
                    }
                );
            }
        }
    }

    onAdd() {
        this.uuid = null;
        this.headerTitle = this.localizationService.getTranslation('dialog.account.title.add');
        this.selectedItemsLabel = this.localizationService.getTranslation('common.dialog.selected-label');
        this.blockUIService.block();
        forkJoin(
            [
                this.accountService.getRoles(),
                this.accountService.getMasterAccounts()
            ]
        )
            .subscribe(
                ([roles, masterAccounts]) => {
                    this.roles = roles;
                    this.masterAccounts = masterAccounts;
                    this.isVisible = true;
                    this.blockUIService.unblock();
                }
            );
        this.account = new Account();
    }

    onEdit(uuid: string) {
        this.uuid = uuid;
        this.headerTitle = this.localizationService.getTranslation('dialog.account.title.edit');
        this.selectedItemsLabel = this.localizationService.getTranslation('common.dialog.selected-label');
        this.blockUIService.block();
        forkJoin(
            [
                this.accountService.getAccountById(uuid),
                this.accountService.getRoles()
            ]
        )
            .subscribe(
                ([account, roles]) => {
                    this.roles = roles;
                    this.account = account;
                    this.isVisible = true;
                    this.blockUIService.unblock();
                }
            );
    }

    onClose() {
        this.isVisible = false;
        this.account = null;
        this.uuid = null;
    }

    isControlInvalid(model: NgModel): boolean {
        return !model.valid && this.errorValidation;
    }

    private validate(f: NgForm): boolean {
        return !!Object.values(f.controls).find(item => item.invalid);
    }
}
