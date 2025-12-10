import { Injectable } from '@angular/core';

@Injectable()
export class KeyboardService {
    readonly navigationKeys = ['Backspace', 'Delete', 'Tab', 'Escape', 'Enter', 'Home', 'End', 'ArrowLeft', 'ArrowRight', 'Clear', 'Copy', 'Paste'];

    isKeyBoardCmd(e: KeyboardEvent): boolean {
        return (e.key === 'a' && (e.ctrlKey || e.metaKey)) ||
            (e.key === 'c' && (e.ctrlKey || e.metaKey)) ||
            (e.key === 'v' && (e.ctrlKey || e.metaKey)) ||
            (e.key === 'x' && (e.ctrlKey || e.metaKey));
    }
}
