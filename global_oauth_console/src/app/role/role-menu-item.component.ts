import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {RoleService} from '../service/role.service';
import {RolePageItem} from './role-page-item';
import {ConfirmationService, LazyLoadEvent} from 'primeng/api';
import {RoleDialogComponent} from './dialog/role-dialog.component';
import {mergeMap} from 'rxjs/operators';
import {Observable} from 'rxjs';
import {RolePage} from './role-page';
import {BlockUIService} from '../service/blockUI.service';
import {SecurityContextHolder} from '../mt-security/security-context-holder';
import {RoleUiElementAccessDecisionManager} from './role-ui-element-access-decision-manager';

@Component({
    templateUrl: './role-menu-item.component.html',
    providers: [ConfirmationService]
})
export class RoleMenuItemComponent implements AfterViewInit {

    roles: RolePageItem[];

    totalCount: number;

    label: string;

    firstRow = 0;
    currentRows = 10;

    uiElementAccessDecisionManager: RoleUiElementAccessDecisionManager;

    @ViewChild(RoleDialogComponent)
    private rolePopupComponent: RoleDialogComponent;

    constructor(private confirmationService: ConfirmationService, private rolesService: RoleService,
                private blockUIService: BlockUIService, securityContextHolder: SecurityContextHolder) {
        this.uiElementAccessDecisionManager = new RoleUiElementAccessDecisionManager(securityContextHolder);
    }

    ngAfterViewInit() {
        this.paginate();
    }

    loadRoles(event: LazyLoadEvent) {
        this.firstRow = event.first;
        this.currentRows = event.rows;
        this.paginate();
    }

    confirmDeleting(role: RolePageItem) {
        this.confirmationService.confirm({
            accept: () => {
                this.deleteRole(role);
            }
        });
    }

    deleteRole(role: RolePageItem) {
        this.blockUIService.block();
        this.rolesService.deleteRole(role).pipe(
            mergeMap(() => this.paginationRequest())
        )
            .subscribe((data) => {
                    this.onPaginateComplete(data);
                    this.blockUIService.unblock();
                }
            );
    }

    createRole() {
        this.rolePopupComponent.onAdd();
    }

    afterSave() {
        this.paginate();
    }

    editRole(roleId: number) {
        this.rolePopupComponent.onEdit(roleId);
    }

    paginate() {
        this.blockUIService.block();
        this.paginationRequest()
            .subscribe((data) => {
                    this.onPaginateComplete(data);
                    this.blockUIService.unblock();
                }
            );
    }

    private onPaginateComplete(data: RolePage) {
        this.roles = data.items;
        this.totalCount = data.totalCount;
    }

    private paginationRequest(): Observable<RolePage> {
        return this.rolesService.getRoles(this.firstRow, this.currentRows);
    }

}
