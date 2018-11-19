import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';
import { EnYearToThYear } from '../helpers';

@Pipe({ name: 'datetimeString' })
export class DatetimeStringPipe implements PipeTransform {
    transform(value: Date, local: string = "en"): string {
        // return moment(value).format('DD/MM/YYYY HH:mm');
        if (value) {
            const dateStr = moment(value).format('DD/MM/YYYY HH:mm');
            const _dd = dateStr.split(' ')[0].split('/')[0];
            const _MM =dateStr.split(' ')[0].split('/')[1];
            const _yyyy = EnYearToThYear(dateStr.split(' ')[0].split('/')[2]);;
            const _HH = dateStr.split(' ')[1].split(':')[0];
            const _mm = dateStr.split(' ')[1].split(':')[1];
            return `${_dd}/${_MM}/${_yyyy} ${_HH}:${_mm}`;
        }
        return "";
    }

}