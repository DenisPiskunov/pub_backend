import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {BehaviorSubject, EMPTY, Observable, throwError} from 'rxjs';
import {SIGN_IN_URL} from './authentication.service';
import {REFRESH_TOKENS_URL, RefreshTokensService} from './refresh-tokens.service';
import {catchError, filter, switchMap, take} from 'rxjs/operators';
import {AccessTokenAppender} from './access-token-appender';
import {SecurityContextHolder} from './security-context-holder';
import {RouterSupport} from '../mt-support/router-support';

/**
 * NOTE: this interceptor must work only after interceptor that append access token in http request
 */
@Injectable()
export class RefreshTokensInterceptor implements HttpInterceptor {

    private isRefreshing = false;
    private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

    constructor(private refreshTokensService: RefreshTokensService, private accessTokenAppender: AccessTokenAppender,
                private securityContextHolder: SecurityContextHolder, private routerSupport: RouterSupport) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (SIGN_IN_URL === req.url) {
            return next.handle(req);
        }
        if (REFRESH_TOKENS_URL === req.url) {
            return next.handle(req)
                .pipe(
                    catchError(error => this.refreshTokensFailedCallBack(error))
                );
        }
        return next.handle(req)
            .pipe(
                catchError(error => this.requestFailedCallBack(error, req, next))
                );
    }

    private requestFailedCallBack(error: any, request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (error instanceof HttpErrorResponse && error.status === 401) {
            return this.handle401Error(request, next);
        } else {
            return throwError(error);
        }
    }

    private handle401Error(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!this.isRefreshing) {
            this.isRefreshing = true;
            this.refreshTokenSubject.next(null);
            return this.refreshTokensService.refreshTokens()
                .pipe(
                    switchMap((accessToken) => {
                        this.isRefreshing = false;
                        this.refreshTokenSubject.next(accessToken);
                        this.securityContextHolder.securityContext.setAccessToken(accessToken);
                        return next.handle(this.accessTokenAppender.appendToken(request, accessToken));
                    })
                );
        } else {
            return this.refreshTokenSubject
                .pipe(
                    filter(accessToken => accessToken != null),
                    take(1),
                    switchMap(accessToken => next.handle(this.accessTokenAppender.appendToken(request, accessToken)))
                );
        }
    }

    private refreshTokensFailedCallBack(error: any): Observable<never> {
        this.isRefreshing = false;
        if (error instanceof HttpErrorResponse && error.status === 401) {
            this.routerSupport.navigateToLogin();
            return EMPTY;
        } else {
            return throwError(error);
        }
    }
}
