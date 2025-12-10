import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable()
export class BlockUIService {

    private blockUISource = new Subject<boolean>();

    blockHandler = this.blockUISource.asObservable();

    block() {
        this.blockUISource.next(true);
    }

    unblock() {
        this.blockUISource.next(false);
    }
}
