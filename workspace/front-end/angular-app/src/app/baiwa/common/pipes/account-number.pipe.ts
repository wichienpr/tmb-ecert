import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'accountNo' })
export class AccountNumberPipe implements PipeTransform {
  transform(value: string, to: string = "convert"): string {
    return Acc[`${to}AccNo`](value);
  }
}

class Acc {
    public static convertAccNo(value: string) {
        let str = value.toString();
        if (str.length == 10)
            return `${this.sub(str, 0, 3)}-${this.sub(str, 3, 4)}-${this.sub(str, 4, 9)}-${this.sub(str, 9)}`;
        else
            return value;
    }
    
    public static revertAccNo(value: string) {
        let str = value.toString();
        if (str.replace(/-/g, '').length == 10)
            return str.replace(/-/g, '');
        else
            return value;
    }
    
    private static sub(value: string, start: number, end?: number) {
        if (end) {
            return value.substring(start, end);
        } else {
            return value.substring(start);
        }
    }
}