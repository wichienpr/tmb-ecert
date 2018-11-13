import { Pipe, PipeTransform } from '@angular/core';

import * as moment from 'moment';
import { EnYearToThYear } from '../helpers';

@Pipe({ name: 'dateString' })
export class DateStringPipe implements PipeTransform {

    transform(value: Date, local: string = "en"): string {
        // return moment(value).format('DD/MM/YYYY');
        if (value) {
            const dateStr = moment(value).format('DD/MM/YYYY');
            const _dd = dateStr.split('/')[0];
            const _MM =dateStr.split('/')[1];
            const _yyyy = EnYearToThYear(dateStr.split('/')[2]);;
            return `${_dd}/${_MM}/${_yyyy}`;
        }
        return "";
    }

}