import {Component, EventEmitter, Output} from '@angular/core';
import {Authority} from '../../authority/authority';
import {RoleService} from '../../service/role.service';
import {Role} from '../role';
import {forkJoin} from 'rxjs';
import {NgForm, NgModel} from '@angular/forms';
import {BlockUIService} from '../../service/blockUI.service';
import {AuthorityService} from '../../authority/authority.service';
import {LocalizationService} from '../../mt-support/localization.service';


@Component({
    selector: 'app-role-popup',
    templateUrl: './role-dialog.component.html'
})

export class RoleDialogComponent {
    isVisible = false;
    headerTitle = '';
    authorities: Authority[];
    role: Role;
    roleId: number;
    selectedItemsLabel = '';
    private errorValidation: boolean;

    @Output() afterSave = new EventEmitter();

    constructor(private rolesService: RoleService, private authorityService: AuthorityService,
                private blockUIService: BlockUIService, private localizationService: LocalizationService) {
    }

    save(f: NgForm) {
        this.errorValidation = this.validate(f);
        if (!this.errorValidation) {
            this.blockUIService.block();
            if (this.roleId) {
                this.rolesService.updateRole(this.role, this.roleId).subscribe(
                    () => {
                        this.onClose();
                        this.afterSave.emit();
                        this.blockUIService.unblock();
                    }
                );
            } else {
                this.rolesService.createRole(this.role).subscribe(
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
        this.headerTitle = this.localizationService.getTranslation('dialog.role.title.add');
        this.selectedItemsLabel = this.localizationService.getTranslation('common.dialog.selected-label');

        this.blockUIService.block();
        this.authorityService.getAuthorities().subscribe(
            (response) => {
                this.authorities = response;
                this.isVisible = true;
                this.blockUIService.unblock();
            }
        );
        this.role = new Role();
        this.roleId = null;
    }

    onEdit(roleId: number) {
        this.headerTitle = this.localizationService.getTranslation('dialog.role.title.edit');
        this.selectedItemsLabel = this.localizationService.getTranslation('common.dialog.selected-label');

        this.blockUIService.block();
        forkJoin(
            [
                this.rolesService.getRoleById(roleId),
                this.authorityService.getAuthorities()
            ]
        )
            .subscribe(
                ([role, authorities]) => {
                    this.role = role;
                    this.authorities = authorities;
                    this.isVisible = true;
                    this.blockUIService.unblock();
                }
            );
        this.roleId = roleId;
    }

    onClose() {
        this.isVisible = false;
        this.role = null;
        this.roleId = null;
    }

    isControlInvalid(model: NgModel): boolean {
        return !model.valid && this.errorValidation;
    }

    setRightRoles(event) {
        const selectedRole = this.authorities.find(item => {
            return item.id === event.itemValue;
        });
        if (selectedRole) {
            const splitRole = selectedRole.name.split('_');
            const roleAction = splitRole.splice(splitRole.length - 1, 1)[0];
            const roleType = splitRole.join('_');
            let readRole = new Authority();
            if (roleAction !== 'READ') {
                readRole = this.authorities.find(item => {
                    return item.name === `${roleType}_READ`;
                });
                if (!this.role.authoritiesIds.find((item) => item === readRole.id)){
                    this.role.authoritiesIds.push(readRole.id);
                }
            }
        }
    }

    private validate(f: NgForm): boolean {
        return !!Object.values(f.controls).find(item => item.invalid);
    }
}
