import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'dateFromString'
})
export class DateFromStringPipe implements PipeTransform {
    transform(value: string): any {
        if (value) {
            const dateStr = value;
            const _dd = dateStr.split('/')[0];
            const _MM =dateStr.split('/')[1];
            const _yyyy = this.getYearTH(new Date(value));
            return `${_dd}/${_MM}/${_yyyy}`;
        }
        return "";
    }

    getYearTH = (date: Date) => {
        if (date.getFullYear() > (new Date().getFullYear() + 500)) {
            return date.getFullYear();
        } else {
            const _date = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate())); // Date.UTC()
            const __dat = _date.toLocaleString('th-TH', { timeZone: 'UTC' }).split(" ");
            const ___da = __dat[0].split("/");
            return ___da[2];
        }
    }
}