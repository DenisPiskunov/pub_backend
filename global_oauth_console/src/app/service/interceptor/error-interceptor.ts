import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse} from '@angular/common/http';
import {catchError} from 'rxjs/operators';
import {Router} from '@angular/router';
import {ERROR_403, ERROR_500} from '../../mt-support/router-support';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

    constructor(private router: Router) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req)
            .pipe(
                catchError((error: HttpErrorResponse) => {
                    if (error.status === 500) {
                        this.router.navigate([ERROR_500]);
                    }
                    if (error.status === 403) {
                        this.router.navigate([ERROR_403]);
                    }
                    return throwError(error);
                })
            );
    }
}
