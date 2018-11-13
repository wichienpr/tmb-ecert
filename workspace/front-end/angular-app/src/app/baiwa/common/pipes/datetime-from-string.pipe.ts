import { Pipe, PipeTransform } from '@angular/core';
import { EnYearToThYear } from '../helpers';

@Pipe({ name: 'datetimeFromString' })
export class DatetimeFromStringPipe implements PipeTransform {
    transform(value: string, local: string = "en"): string {
        if (value) {
            const dateStr = value;
            const _dd = dateStr.split(' ')[0].split('/')[0];
            const _MM = dateStr.split(' ')[0].split('/')[1];
            const _yyyy = EnYearToThYear(dateStr.split(' ')[0].split('/')[2]);
            const _HH = dateStr.split(' ')[1].split(':')[0];
            const _mm = dateStr.split(' ')[1].split(':')[1];
            return `${_dd}/${_MM}/${_yyyy} ${_HH}:${_mm}`;
        }
        return "";
    }
}