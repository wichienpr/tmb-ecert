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