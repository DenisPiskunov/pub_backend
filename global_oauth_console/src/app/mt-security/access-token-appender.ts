import {Injectable} from '@angular/core';
import {HttpRequest} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class AccessTokenAppender {

    appendToken(req: HttpRequest<any>, token: string) {
        return req.clone({
            setHeaders: {
                Authorization: `Bearer ${token}`
            }
        });
    }

}
