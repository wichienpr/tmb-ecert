import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'statementYearBreak' })
export class StatementYearBreakPipe implements PipeTransform {
    transform(value: string): string {
        let result = "";
        if (value.length > 4) {
            let count = 0;
            value.split(",").map((obj, index) => {
                count++;
                if (count < 5) {
                    result += obj + ",";
                } else {
                    result += obj + ", ";
                    count = 0;
                }
            })
        } else {
            result = value;
        }
        return result;
    }
}