import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'tmbReqNo' })
export class TmbReqNumberPipe implements PipeTransform {
    transform(value: String): string {
        if (value) {
            let len = value.length;
            let first: string = value.substring(0, len - 5);
            let last: string = value.substring(len - 5, len);
            return `${first}-${last}`;
        }
        return `กำลังโหลด...`;
    }
}
