import {Component, EventEmitter, Output} from '@angular/core';
import {MasterAccountService} from '../../service/master-account.service';
import {MasterAccount} from '../master-account';
import {NgForm, NgModel} from '@angular/forms';
import {BlockUIService} from '../../service/blockUI.service';
import {LocalizationService} from '../../mt-support/localization.service';

@Component({
    selector: 'app-popup-master-account',
    templateUrl: './master-account-dialog.component.html'
})

export class MasterAccountDialogComponent {
    isVisible = false;
    headerTitle = '';
    account: MasterAccount = new MasterAccount();
    uuid: string;
    private errorValidation: boolean;

    @Output() afterSave = new EventEmitter();
    constructor(private accountsService: MasterAccountService, private localizationService: LocalizationService,
                private blockUIService: BlockUIService) { }

    save(f: NgForm) {
        this.errorValidation = this.validate(f);
        if (!this.errorValidation) {
            this.blockUIService.block();
            if (this.uuid) {
                this.accountsService.updateAccount(this.account, this.uuid).subscribe(
                    () => {
                        this.onClose();
                        this.afterSave.emit();
                        this.blockUIService.unblock();
                    }
                );
            } else {
                this.accountsService.createAccount(this.account).subscribe(
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
        this.headerTitle = this.localizationService.getTranslation('dialog.master-account.title.add');
        this.account = new MasterAccount();
        this.isVisible = true;
        this.uuid = null;
    }

    onEdit(uuid: string) {
        this.headerTitle = this.localizationService.getTranslation('dialog.master-account.title.edit');
        this.blockUIService.block();
        this.accountsService.getAccountById(uuid).subscribe(
            (account) => {
                this.account = account;
                this.isVisible = true;
                this.blockUIService.unblock();
            }
        );
        this.uuid = uuid;
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
