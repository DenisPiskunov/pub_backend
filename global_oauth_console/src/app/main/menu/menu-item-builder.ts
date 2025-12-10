import {ACCOUNTS, MASTER_ACCOUNTS, ROLES} from '../../mt-support/router-support';
import {LocalizationService} from '../../mt-support/localization.service';

export class MenuItemBuilder {


    constructor(private readonly localizationService: LocalizationService) {
    }

    newRolesMenuItem(): any {
        return {
            label: '', icon: 'pi pi-id-card',
            items: [
                {
                    label: this.localizationService.getTranslation('left-menu.menu-item.role.label'),
                    icon: '',
                    routerLink: [ROLES]
                },
            ]
        };
    }

    newMasterAccountsMenuItem(): any {
        return {
            label: '', icon: 'pi pi-users',
            items: [
                {
                    label: this.localizationService.getTranslation('left-menu.menu-item.master-account.label'),
                    icon: '',
                    routerLink: [MASTER_ACCOUNTS]
                },
            ]
        };
    }

    newAccountsMenuItem() {
        return {
            label: '', icon: 'pi pi-users',
            items: [
                {
                    label: this.localizationService.getTranslation('left-menu.menu-item.account.label'),
                    icon: '',
                    routerLink: [ACCOUNTS]
                },
            ]
        };
    }
}
