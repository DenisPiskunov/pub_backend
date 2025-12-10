import {Directive} from '@angular/core';
import {HostListener} from '@angular/core';
import {KeyboardService} from "./keyboard.service";

@Directive({
    selector: '[upperLatinAndCyrillicRestrictInput]'
})
export class UpperLatinAndCyrillicRestrictInputDirective {

    constructor(private keyboardService: KeyboardService) {
    }

    @HostListener('keydown', ['$event'])
    onKeyDown(event: Event) {
        if (event instanceof KeyboardEvent) {
            if (this.keyboardService.navigationKeys.indexOf(event.key) > -1 || this.keyboardService.isKeyBoardCmd(event)) {
                return;
            }
            if (!this.allowedKeys(event)) {
                event.preventDefault();
            }
        }
    }

    @HostListener('paste', ['$event'])
    onPaste(event: ClipboardEvent) {
        event.preventDefault();
        const replacement = event.clipboardData.getData('text/plain').replace(/([^\x30-\x39][^\x61-\x79])/g, '');
        document.execCommand('insertText', false, replacement);
    }

    private allowedKeys(e: KeyboardEvent) {
        return ((e.key.charCodeAt(0) >= 48 && e.key.charCodeAt(0) <= 57) ||
            (e.key.charCodeAt(0) >= 97 && e.key.charCodeAt(0) <= 122))
    }
}
