import { Pipe, PipeTransform } from '@angular/core';
import { EnYearToThYear } from '../helpers';

@Pipe({ name: 'dateFromString' })
export class DateFromStringPipe implements PipeTransform {
    transform(value: string): any {
        if (value) {
            const dateStr = value;
            const _dd = dateStr.split('/')[0];
            const _MM = dateStr.split('/')[1];
            const _yyyy = EnYearToThYear(dateStr.split('/')[2]);
            return `${_dd}/${_MM}/${_yyyy}`;
        }
        return "";
    }
}