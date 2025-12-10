import {SecurityContextHolder} from '../mt-security/security-context-holder';
import {ACCOUNT_CREATE, ACCOUNT_UPDATE} from '../mt-security/authorities';

export class AccountUiElementAccessDecisionManager {

    constructor(private securityContextHolder: SecurityContextHolder) {
    }

    hasAccessToCreate(): boolean {
        return this.securityContextHolder.securityContext.hasAuthority(ACCOUNT_CREATE);
    }

    hasAccessToEdit(): boolean {
        return this.securityContextHolder.securityContext.hasAuthority(ACCOUNT_UPDATE);
    }

}
