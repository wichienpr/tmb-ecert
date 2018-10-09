import { Pipe, PipeTransform } from '@angular/core';
import { digit } from 'helpers/';

@Pipe({ name: 'datetimeString' })
export class DatetimeStringPipe implements PipeTransform {
    transform(value: Date, local: string = "en"): string {
        const date = new Date(value);
        const _dd = digit(date.getDate());
        const _MM = digit(date.getMonth()+1);
        const _yyyy = digit(date.getFullYear());
        const _HH = digit(date.getHours());
        const _mm = digit(date.getMinutes());
        return `${_dd}/${_MM}/${_yyyy} ${_HH}:${_mm}`;
    }
}