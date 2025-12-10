import {Injectable} from '@angular/core';
import {Headers, Http} from '@angular/http';

@Injectable()
export class BaseHttpService {
    protected header = new Headers();

    constructor() {
        this.header.append('Content-Type', 'application/json');
        this.header.append('Access-Control-Allow-Origin', '*');
    }

    protected handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
}
