import { Pipe, PipeTransform } from '@angular/core';
import { digit } from 'helpers/';

@Pipe({ name: 'dateString' })
export class DateStringPipe implements PipeTransform {
    transform(value: Date, local: string = "th"): string {
        return DateString[`${local}Date`](value);
    }
}

class DateString {
    public static thDate = (date: Date) => {
        if (date.getFullYear() > (new Date().getFullYear() + 540)) {
            return `${digit(date.getDate())}/${digit(date.getMonth() + 1)}/${date.getFullYear()}`;
        } else {
            const _date = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate())); // Date.UTC()
            const __dat = _date.toLocaleString('th-TH', { timeZone: 'UTC' }).split(" ");
            const ___da = __dat[0].split("/");
            return `${digit(___da[0])}/${digit(___da[1])}/${___da[2]}`;
        }
    }

    public static enDate = (date: Date) => {
        return `${digit(date.getDate())}/${digit(date.getMonth() + 1)}/${date.getFullYear()}`;
    }
}