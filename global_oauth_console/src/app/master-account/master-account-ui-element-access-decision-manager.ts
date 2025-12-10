import {SecurityContextHolder} from '../mt-security/security-context-holder';
import {MASTER_ACCOUNT_BLOCK, MASTER_ACCOUNT_CREATE, MASTER_ACCOUNT_DELETE, MASTER_ACCOUNT_UPDATE} from '../mt-security/authorities';

export class MasterAccountUiElementAccessDecisionManager {

    constructor(private securityContextHolder: SecurityContextHolder) {
    }

    hasAccessToCreate(): boolean {
        return this.securityContextHolder.securityContext.hasAuthority(MASTER_ACCOUNT_CREATE);
    }

    hasAccessToEdit(): boolean {
        return this.securityContextHolder.securityContext.hasAuthority(MASTER_ACCOUNT_UPDATE);
    }

    hasAccessToDelete(): boolean {
        return this.securityContextHolder.securityContext.hasAuthority(MASTER_ACCOUNT_DELETE);
    }

    hasAccessToBlock(): boolean {
        return this.securityContextHolder.securityContext.hasAuthority(MASTER_ACCOUNT_BLOCK);
    }
}
