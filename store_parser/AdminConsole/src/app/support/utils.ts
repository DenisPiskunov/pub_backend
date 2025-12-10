import {Injectable} from '@angular/core';
import {Dhm} from '../domain/dhm';

@Injectable()
export class Utils {
    strFormat = function (args: string[]) {
        let theString = args[0];
        for (let i = 1; i < args.length; i++) {
            const regEx = new RegExp('\\{' + (i - 1) + '\\}');
            theString = theString.replace(regEx, args[i]);
        }
        return theString;
    };

    millisecondsToDHM = function(ms: number) {
        const days = Math.floor(ms / (24 * 60 * 60 * 1000));
        const daysms = ms % (24 * 60 * 60 * 1000);
        const hours = Math.floor( daysms / (60 * 60 * 1000));
        const hoursms = ms % (60 * 60 * 1000);
        const minutes = Math.floor(hoursms / (60 * 1000));
        return { days: days, hours: hours, minutes: minutes};
    };

    dhmToMilliseconds = function(dhm: Dhm) {
        const daysms = 24 * 60 * 60 * 1000;
        const hoursms = 60 * 60 * 1000;
        const minutesms = 60 * 1000;
        return (dhm.days * daysms) + (dhm.hours * hoursms) + (dhm.minutes * minutesms);
    };

}
