import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {RolePage} from '../role/role-page';
import {RolePageItem} from '../role/role-page-item';
import {Role} from '../role/role';
import {Authority} from '../authority/authority';
import {environment} from '../../environments/environment';

@Injectable()
export class RoleService {

    constructor(private http: HttpClient) {
    }

    getRoles(offset: number, limit: number): Observable<RolePage> {
        return this.http.get<RolePage>(`${environment.apiURL}/roles?limit=${limit}&offset=${offset}`);
    }

    getRoleById(id: number): Observable<Role> {
        return this.http.get<Role>(`${environment.apiURL}/roles/${id}`);
    }

    createRole(role: Role) {
        return this.http.post(`${environment.apiURL}/roles`, role);
    }

    updateRole(role: Role, id): Observable<Role> {
        return this.http.put<Role>(`${environment.apiURL}/roles/${id}`, role);
    }

    deleteRole(role: RolePageItem) {
        return this.http.delete(`${environment.apiURL}/roles/${role.id}`);
    }
}
