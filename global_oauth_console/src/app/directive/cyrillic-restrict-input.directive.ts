import {Directive} from '@angular/core';
import {HostListener} from '@angular/core';
import {KeyboardService} from './keyboard.service';

@Directive({
    selector: '[CyrillicRestrictInput]'
})
export class CyrillicRestrictInputDirective {

    constructor(private keyboardService: KeyboardService) {
    }

    @HostListener('keydown', ['$event'])
    onKeyDown(event: Event) {
        if (event instanceof KeyboardEvent) {
            if (this.keyboardService.navigationKeys.indexOf(event.key) > -1 || this.keyboardService.isKeyBoardCmd(event)) {
                return;
            }
            if (event.key.charCodeAt(0) > 127) {
                event.preventDefault();
            }
        }
    }

    @HostListener('paste', ['$event'])
    onPaste(event: ClipboardEvent) {
        event.preventDefault();
        const replacement = event.clipboardData.getData('text/plain').replace(/[^\x00-\x7F]/g, '');
        document.execCommand('insertText', false, replacement);
    }
}
