import { digit } from "./digit";

export function dateLocale(date) {
    const _date = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()));
    const year = _date.toLocaleString('th-TH', { timeZone: 'UTC' }).split(" ")[0].split("/")[2];
    const month = _date.toLocaleString('th-TH', { timeZone: 'UTC' }).split(" ")[0].split("/")[1];
    const day = _date.toLocaleString('th-TH', { timeZone: 'UTC' }).split(" ")[0].split("/")[0];
    return digit(day) + "/" + digit(month) + "/" + digit(year);
}

export function dateLocaleEN(date) {
    return `${digit(date.getDate())}/${digit(date.getMonth() + 1)}/${date.getFullYear()}`;
}

export function EnDateToThDate(dateStr: string) {
    const DATE_REGEXP: RegExp = new RegExp('^(0?[1-9]|[1-2][0-9]|3[0-1])/(0?[1-9]|1[0-2])/([0-9]{4})$', 'gi');
    return dateStr.replace(DATE_REGEXP,
        (str: string, day: string, month: string, year: string) => {
            return `${day}/${month}/${parseInt(year, 10) + 543}`;
        });
}

export function EnYearToThYear(dateStr: string) {
    const DATE_REGEXP: RegExp = new RegExp('^([0-9]{4})$', 'gi');
    return dateStr.replace(DATE_REGEXP,
        (str: string, day: string, month: string, year: string) => {
            return `${parseInt(year, 10) + 543}`;
        });
}


export function ThDateToEnDate(dateStr: string) {
    const DATE_REGEXP: RegExp = new RegExp('^(0?[1-9]|[1-2][0-9]|3[0-1])/(0?[1-9]|1[0-2])/([0-9]{4})$', 'gi');
    return dateStr.replace(DATE_REGEXP,
        (str: string, day: string, month: string, year: string) => {
            return `${day}/${month}/${parseInt(year, 10) - 543}`;
        });
}

export function ThYearToEnYear(dateStr: string) {
    const DATE_REGEXP: RegExp = new RegExp('^([0-9]{4})$', 'gi');
    return dateStr.replace(DATE_REGEXP,
        (str: string, day: string, month: string, year: string) => {
            return `${parseInt(year, 10) - 543}`;
        });
}

export function strToDate(str) {
    return new Date(str.split("/")[2], str.split("/")[1], str.split("/")[0]);
}