import {Component, EventEmitter, Output} from '@angular/core';
import {NgForm, NgModel} from '@angular/forms';

@Component({
    selector: 'app-restore-dialog',
    templateUrl: './restore-password-dialog.component.html'
})

export class RestorePasswordDialogComponent {
    isVisible = false;
    email: string;
    private errorValidation: boolean;
    blockedForm = true;

    @Output() afterSave = new EventEmitter();
    constructor() { }

    save(f: NgForm) {
        this.errorValidation = this.validate(f);
        if (!this.errorValidation) {
            // здесь будет метод
        }
    }

    onRestore() {
        this.email = null;
        this.isVisible = true;
        this.blockedForm = false;
    }

    onClose() {
        this.isVisible = false;
        this.email = null;
    }

    isControlInvalid(model: NgModel): boolean {
        return !model.valid && this.errorValidation;
    }

    private validate(f: NgForm): boolean {
        return !!Object.values(f.controls).find(item => item.invalid);
    }
}
