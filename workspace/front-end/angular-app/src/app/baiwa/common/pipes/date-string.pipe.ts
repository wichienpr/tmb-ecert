import { Pipe, PipeTransform } from '@angular/core';

import * as moment from 'moment';

@Pipe({ name: 'dateString' })
export class DateStringPipe implements PipeTransform {
    transform(value: Date, local: string = "en"): string {
        if (value)
            return moment(value).format('DD/MM/YYYY');
        return "";
    }
}