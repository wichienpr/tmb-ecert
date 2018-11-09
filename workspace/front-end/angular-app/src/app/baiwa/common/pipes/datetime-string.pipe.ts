import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

@Pipe({ name: 'datetimeString' })
export class DatetimeStringPipe implements PipeTransform {
    transform(value: Date, local: string = "en"): string {
        // return moment(value).format('DD/MM/YYYY HH:mm');
        if (value) {
            const dateStr = moment(value).format('DD/MM/YYYY HH:mm');
            const _dd = dateStr.split(' ')[0].split('/')[0];
            const _MM =dateStr.split(' ')[0].split('/')[1];
            const _yyyy = this.getYearTH(new Date(value));
            const _HH = dateStr.split(' ')[1].split(':')[0];
            const _mm = dateStr.split(' ')[1].split(':')[1];
            return `${_dd}/${_MM}/${_yyyy} ${_HH}:${_mm}`;
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