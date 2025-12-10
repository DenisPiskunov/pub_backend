import {SecurityContextHolder} from '../../mt-security/security-context-holder';
import {MenuItemBuilder} from './menu-item-builder';
import {ACCOUNT_READ, MASTER_ACCOUNT_READ, ROLE_READ} from '../../mt-security/authorities';
import {LocalizationService} from '../../mt-support/localization.service';

export class MenuBuilder {

    private readonly securityContextHolder: SecurityContextHolder;
    private readonly menuItemBuilder: MenuItemBuilder;

    constructor(securityContextHolder: SecurityContextHolder, localizationService: LocalizationService) {
        this.securityContextHolder = securityContextHolder;
        this.menuItemBuilder = new MenuItemBuilder(localizationService);
    }

    build(): any[] {
        const securityContext = this.securityContextHolder.securityContext;
        const model: any[] = [];
        if (securityContext.hasAuthority(ROLE_READ)) {
            model.push(this.menuItemBuilder.newRolesMenuItem());
        }
        if (securityContext.hasAuthority(MASTER_ACCOUNT_READ)) {
            model.push(this.menuItemBuilder.newMasterAccountsMenuItem());
        }
        if (securityContext.hasAuthority(ACCOUNT_READ)) {
            model.push(this.menuItemBuilder.newAccountsMenuItem());
        }
        return model;
    }
}
