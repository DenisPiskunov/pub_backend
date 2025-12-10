import {SecurityContextHolder} from '../mt-security/security-context-holder';
import {ROLE_CREATE, ROLE_DELETE, ROLE_UPDATE} from '../mt-security/authorities';

export class RoleUiElementAccessDecisionManager {

    constructor(private securityContextHolder: SecurityContextHolder) {
    }

    hasAccessToCreate(): boolean {
        return this.securityContextHolder.securityContext.hasAuthority(ROLE_CREATE);
    }

    hasAccessToEdit(): boolean {
        return this.securityContextHolder.securityContext.hasAuthority(ROLE_UPDATE);
    }

    hasAccessToDelete(): boolean {
        return this.securityContextHolder.securityContext.hasAuthority(ROLE_DELETE);
    }
}
