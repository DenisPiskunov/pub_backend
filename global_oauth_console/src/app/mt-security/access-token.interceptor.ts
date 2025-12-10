import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpEvent, HttpInterceptor, HttpHandler, HttpRequest} from '@angular/common/http';
import {SecurityContextHolder} from './security-context-holder';
import {AccessTokenAppender} from './access-token-appender';

@Injectable()
export class AccessTokenInterceptor implements HttpInterceptor {

    constructor(private securityContextHolder: SecurityContextHolder, private accessTokenAppender: AccessTokenAppender) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const securityContext = this.securityContextHolder.securityContext;
        if (securityContext.isAuthenticated()) {
            req = this.accessTokenAppender.appendToken(req, securityContext.getAccessToken());
        }
        return next.handle(req);
    }
}
